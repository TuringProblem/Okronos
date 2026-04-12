package io.ruin.api.database;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.function.Consumer;

@Slf4j
public class DatabaseUtils {

	private static final int MAX_RETRY_ATTEMPTS = 3;
	private static final long RETRY_DELAY_MS = 1000;

	/**
	 * Connects to multiple databases with enhanced error handling
	 */
	public static void connect(Database[] dbs, Consumer<ArrayList<Throwable>> errorsConsumer) {
		PrintStream err = System.err;
		err.flush();

		// Temporarily redirect error output
		System.setErr(new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				// Skip output during connection attempts
			}
		}));

		ArrayList<Throwable> errors = new ArrayList<>();

		for (int dbIndex = 0; dbIndex < dbs.length; dbIndex++) {
			Database db = dbs[dbIndex];
			if (db == null) continue;

			for (int attempt = 1; attempt <= MAX_RETRY_ATTEMPTS; attempt++) {
				try {
					db.connect();
					// Verify connection is valid
					if (verifyConnection(db)) {
						break;
					} else {
						throw new SQLException("Connection verification failed");
					}
				} catch (Throwable t) {
					String message = extractErrorMessage(t);

					if (attempt == MAX_RETRY_ATTEMPTS) {
						errors.add(new Throwable("Failed to connect to database #" + dbIndex + ": " + message));
					} else {
						log.warn("Database connection attempt {} failed for database #{}: {}",
							attempt, dbIndex, message);
						try {
							Thread.sleep(RETRY_DELAY_MS * attempt); // Exponential backoff
						} catch (InterruptedException ie) {
							Thread.currentThread().interrupt();
							break;
						}
					}
				}
			}
		}

		// Restore error output
		System.setErr(err);
		errorsConsumer.accept(errors);
	}

	/**
	 * Verifies if a database connection is valid
	 */
	public static boolean verifyConnection(Database db) {
		try {
			if (db instanceof DummyDatabase) {
				return true;
			}
			return db.isOperational();
		} catch (Exception e) {
			log.error("Error verifying database connection", e);
			return false;
		}
	}

	/**
	 * Safely closes multiple AutoCloseable resources
	 */
	public static void close(AutoCloseable... closeables) {
		for (AutoCloseable closeable : closeables) {
			if (closeable == null) continue;

			try {
				closeable.close();
			} catch (Exception e) {
				log.error("Error closing resource", e);
			}
		}
	}

	/**
	 * Generates an INSERT query with proper parameter handling
	 */
	public static String insertQuery(String table, String... columnNames) {
		if (table == null || table.isEmpty()) {
			throw new IllegalArgumentException("Table name cannot be null or empty");
		}

		if (columnNames == null || columnNames.length == 0) {
			throw new IllegalArgumentException("Column names cannot be null or empty");
		}

		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();

		for (int i = 0; i < columnNames.length; i++) {
			if (columnNames[i] == null || columnNames[i].isEmpty()) {
				throw new IllegalArgumentException("Column name at index " + i + " cannot be null or empty");
			}

			if (i != 0) {
				columns.append(",");
				values.append(",");
			}
			columns.append("`").append(columnNames[i].trim()).append("`");
			values.append("?");
		}

		return "INSERT INTO `" + table.trim() + "` (" + columns + ") VALUES (" + values + ")";
	}

	/**
	 * Helper method for parameter binding
	 */
	public static void setParameter(PreparedStatement stmt, int index, Object value) throws SQLException {
		if (value == null) {
			stmt.setNull(index, Types.NULL);
		} else if (value instanceof String) {
			stmt.setString(index, (String) value);
		} else if (value instanceof Integer) {
			stmt.setInt(index, (Integer) value);
		} else if (value instanceof Long) {
			stmt.setLong(index, (Long) value);
		} else if (value instanceof Double) {
			stmt.setDouble(index, (Double) value);
		} else if (value instanceof Timestamp) {
			stmt.setTimestamp(index, (Timestamp) value);
		} else if (value instanceof Date) {
			stmt.setDate(index, (Date) value);
		} else {
			stmt.setObject(index, value);
		}
	}

	/**
	 * Extracts a clean error message from an exception
	 */
	private static String extractErrorMessage(Throwable t) {
		String message = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();
		if (message == null) {
			return "Unknown error";
		}
		int newlineIndex = message.indexOf('\n');
		return newlineIndex != -1 ? message.substring(0, newlineIndex) : message;
	}
}