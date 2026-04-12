package io.ruin.api.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import core.task.Continuation;
import io.ruin.api.utils.ExecutorUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static core.task.api.API.*;

@Slf4j
public class Database {

	private final String host;
	private final String database;
	private final String username;
	private final String password;
	private HikariDataSource dataSource;

	private static final int CORE_POOL_SIZE = 2;
	private static final int MAX_POOL_SIZE = 4;
	private static final int QUEUE_CAPACITY = 1500;
	private static final long KEEP_ALIVE_TIME = 60L;
	private static final long MAX_WAIT_TIME = 10_000;

	private final ThreadPoolExecutor executor;
	private final AtomicInteger failedAttempts = new AtomicInteger(0);

	public Database(String host, String database, String username, String password) {
		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
		this.executor = createExecutor();
	}

	private ThreadPoolExecutor createExecutor() {
		return new ThreadPoolExecutor(
				CORE_POOL_SIZE,
				MAX_POOL_SIZE,
				KEEP_ALIVE_TIME,
				TimeUnit.SECONDS,
				new LinkedBlockingQueue<>(QUEUE_CAPACITY),
				new ThreadFactory() {
					private final AtomicInteger threadCount = new AtomicInteger(0);

					@Override
					public Thread newThread(Runnable r) {
						Thread thread = new Thread(r);
						thread.setName("Database-Worker-" + threadCount.incrementAndGet());
						thread.setDaemon(true);
						thread.setPriority(Thread.NORM_PRIORITY - 1);
						return thread;
					}
				},
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public void connect() {
		HikariConfig config = new HikariConfig();
		config.setDriverClassName("com.mysql.cj.jdbc.Driver");
		config.setJdbcUrl("jdbc:mysql://" + host + "/" + database +
				"?useSSL=false&serverTimezone=UTC&autoReconnect=true" +
				"&maxReconnects=3&connectTimeout=10000");
		config.setUsername(username);
		config.setPassword(password);
		config.setPoolName(database + "-pool");

		// Connection pool settings
		config.setMaximumPoolSize(10);
		config.setMinimumIdle(2);
		config.setIdleTimeout(300000); // 5 minutes
		config.setMaxLifetime(600000); // 10 minutes
		config.setConnectionTimeout(10000); // 10 seconds
		config.setValidationTimeout(5000); // 5 seconds
		config.setLeakDetectionThreshold(60000); // 1 minute

		// Connection testing
		config.setConnectionTestQuery("SELECT 1");
		config.setInitializationFailTimeout(-1);

		// Performance settings
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("useServerPrepStmts", "true");
		config.addDataSourceProperty("useLocalSessionState", "true");
		config.addDataSourceProperty("rewriteBatchedStatements", "true");
		config.addDataSourceProperty("cacheResultSetMetadata", "true");
		config.addDataSourceProperty("cacheServerConfiguration", "true");
		config.addDataSourceProperty("elideSetAutoCommits", "true");
		config.addDataSourceProperty("maintainTimeStats", "false");

		// Connection cleanup settings
		config.setAutoCommit(true);
		config.setTransactionIsolation("TRANSACTION_READ_COMMITTED");

		try {
			dataSource = new HikariDataSource(config);
			startHealthCheck();
		} catch (Exception e) {
			log.error("Failed to initialize database connection pool", e);
			throw new RuntimeException("Database connection failed", e);
		}
	}

	private static ScheduledExecutorService healthChecker =
			Executors.newSingleThreadScheduledExecutor(r -> {
				Thread thread = new Thread(r, "DB-HealthCheck");
				thread.setDaemon(true);
				return thread;
			});

	private void startHealthCheck() {
		healthChecker.scheduleAtFixedRate(() -> {
			try (Connection conn = dataSource.getConnection()) {
				conn.createStatement().execute("SELECT 1");
				failedAttempts.set(0);
				log.trace("Database health check passed");
			} catch (SQLException e) {
				int failures = failedAttempts.incrementAndGet();
				log.error("Database health check failed (attempt {})", failures, e);

				if (failures >= 3) {
					log.error("Multiple database health checks failed, attempting recovery...");
					attemptRecovery();
				}
			}
		}, 0, 30, TimeUnit.SECONDS);
	}

	private void attemptRecovery() {
		try {
			if (dataSource != null && !dataSource.isClosed()) {
				dataSource.close();
			}
			connect();
			log.info("Database connection recovered successfully");
		} catch (Exception e) {
			log.error("Failed to recover database connection", e);
		}
	}

	public Continuation.Void exec(DatabaseStatement statement) {
		return () -> {
			var future = submitNoRetry(statement);
			var start = System.currentTimeMillis();
			while (!timeExceeded(start)) {
				switch (future.state()) {
					case CANCELLED:
						throw new IllegalStateException("Cancelled.");
					case FAILED:
						throw new RuntimeException(future.exceptionNow());
					case RUNNING:
						sleep(1);
						break;
					case SUCCESS:
						return;
				}
			}
			throw new IllegalStateException("Timed out " + future.state());
		};
	}

	public <R> Continuation<R> exec(DatabaseStatement.Typed<R> statement) {
		return () -> {
			var future = submitNoRetry(statement);
			var start = System.currentTimeMillis();
			while (!timeExceeded(start)) {
				switch (future.state()) {
					case CANCELLED:
						throw new IllegalStateException("Cancelled.");
					case FAILED:
						throw new RuntimeException(future.exceptionNow());
					case RUNNING:
						sleep(1);
						break;
					case SUCCESS:
						return future.resultNow();
				}
			}
			throw new IllegalStateException("Timed out. " + future.state());
		};
	}

	public void execute(DatabaseStatement statement) {
		execute(statement, false);
	}

	public void executeAwait(DatabaseStatement statement) {
		execute(statement, true);
	}

	private Future<?> submitNoRetry(DatabaseStatement statement) {
		Runnable task = () -> {
			Connection connection = null;
			try {
				connection = getConnection();
				statement.execute(connection);
				return;
			} catch (SQLException e) {
				log.error("Database operation failed.", e);
				statement.failed(e);
			} finally {
				closeQuietly(connection);
			}
		};

		return executor.submit(task);
	}

	private <R> Future<R> submitNoRetry(DatabaseStatement.Typed<R> statement) {
		Callable<R> task = () -> {
			Connection connection = null;
			try {
				connection = getConnection();
				return statement.execute(connection);
			} catch (SQLException e) {
				log.error("Database operation failed.", e);
			} finally {
				closeQuietly(connection);
			}
			return null;
		};

		return executor.submit(task);
	}

	private void execute(DatabaseStatement statement, boolean wait) {
		Runnable task = () -> {
			int retryCount = 0;
			while (retryCount < 3) {
				Connection connection = null;
				try {
					connection = getConnection();
					statement.execute(connection);
					return;
				} catch (Exception e) {
					retryCount++;
					log.error("Database operation failed (attempt {}/3)", retryCount, e);

					if (retryCount >= 3) {
						statement.failed(e);
					}

					try {
						Thread.sleep(1000L * retryCount); // Exponential backoff
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
						break;
					}
				} finally {
					closeQuietly(connection);
				}
			}
		};

		if (wait) {
			try {
				Future<?> future = executor.submit(task);
				future.get(30, TimeUnit.SECONDS);
			} catch (TimeoutException e) {
				log.error("Database operation timed out", e);
				statement.failed(e);
			} catch (Exception e) {
				log.error("Database operation failed", e);
				statement.failed(e);
			}
		} else {
			executor.execute(task);
		}
	}

	private Connection getConnection() throws SQLException {
		Connection conn = dataSource.getConnection();
		conn.setAutoCommit(true);
		return conn;
	}

	private void closeQuietly(Connection connection) {
		if (connection != null) {
			try {
				if (!connection.getAutoCommit()) {
					connection.rollback();
				}
				connection.close();
			} catch (SQLException e) {
				log.error("Error closing connection", e);
			}
		}
	}

	public void shutdown() {
		ExecutorUtils.shutdown(executor);
		ExecutorUtils.shutdownNow(healthChecker);
		if (dataSource != null) {
			dataSource.close();
		}
	}

	public boolean isOperational() {
		return dataSource != null && !dataSource.isClosed() && failedAttempts.get() < 3;
	}

	private static boolean timeExceeded(long startTime) {
		return System.currentTimeMillis() - startTime > MAX_WAIT_TIME;
	}
}
