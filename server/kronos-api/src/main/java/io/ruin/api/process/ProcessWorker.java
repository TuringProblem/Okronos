package io.ruin.api.process;

import io.ruin.api.utils.ExecutorUtils;
import io.ruin.api.utils.SafeRunnable;
import io.ruin.api.utils.ServerWrapper;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ProcessWorker {

	private final long period;
	public final ScheduledExecutorService executor;
	private final ArrayList<Supplier<Boolean>> queuedTasks;
	private volatile long executorThreadId = -1;
	private final AtomicLong executions = new AtomicLong();
	private final AtomicLong slowExecutions = new AtomicLong();

	private volatile long lastExecutionMs, slowestExecutionMs;

	public ProcessWorker(long periodMs, ProcessFactory factory) {
		this(periodMs, factory, null);
	}

	public ProcessWorker(long periodMs, ProcessFactory factory, Consumer<Throwable> errorAction) {
		this.period = periodMs;
		this.executor = Executors.newSingleThreadScheduledExecutor(factory);
		this.executor.scheduleAtFixedRate(() -> {
			try {
				process();
			} catch (Exception e) {
				ServerWrapper.logError("ProcessWorker failed to process", e);
			}
		}, 0L, periodMs, TimeUnit.MILLISECONDS);
		this.queuedTasks = new ArrayList<>();
		executeAwait(() -> this.executorThreadId = Thread.currentThread().threadId());
	}

	public void queue(Supplier<Boolean> task) {
		executor.execute(() -> queuedTasks.add(task));
	}

	public void shutdown() {
		ExecutorUtils.shutdown(this.executor);
	}

	/**
	 * Processing
	 */

	private void process() {
		long startTime = System.nanoTime();
		if (!queuedTasks.isEmpty())
			queuedTasks.removeIf(this::finish);
		lastExecutionMs = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
		if (lastExecutionMs > period)
			slowExecutions.getAndIncrement();
		if (lastExecutionMs > slowestExecutionMs)
			slowestExecutionMs = lastExecutionMs;
		executions.getAndIncrement();
	}

	private boolean finish(Supplier<Boolean> task) {
		try {
			return task.get();
		} catch (Throwable t) {
			ServerWrapper.logError("Unable to process event.", t);
			return false;
		}
	}

	/**
	 * Executing
	 */

	public void execute(Runnable runnable) {
		executor.execute(new SafeRunnable(runnable));
	}

	public boolean executeAwait(Runnable runnable) {
		try {
			executor.submit(new SafeRunnable(runnable)).get();
			return true;
		} catch (Throwable t) {
			ServerWrapper.logError("", t);
			return false;
		}
	}

	public void executeLast(Runnable runnable) {
		queue(() -> {
			new SafeRunnable(runnable).run();
			return true;
		});
	}

	public void sync(Runnable runnable) {
		if (Thread.currentThread().threadId() == executorThreadId)
			new SafeRunnable(runnable).run();
		else
			execute(runnable);
	}

	/**
	 * Getters
	 */

	public long getPeriod() {
		return period;
	}

	public ScheduledExecutorService getExecutor() {
		return executor;
	}

	public long getExecutions() {
		return executions.get();
	}

	public long getSlowExecutions() {
		return slowExecutions.get();
	}

	public long getLastExecutionTime() {
		return lastExecutionMs;
	}

	public long getSlowestExecutionTime() {
		return slowestExecutionMs;
	}

	public long getQueuedTaskCount() {
		return queuedTasks.size();
	}

}
