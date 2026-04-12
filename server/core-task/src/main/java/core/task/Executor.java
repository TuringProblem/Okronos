package core.task;

import core.task.ContinuationListener.StopReason;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import lombok.extern.slf4j.*;

@Slf4j
public final class Executor<T extends Continuation<?>> {
	public static int TOTAL_TASK_INSTANCES = 0;
	private static final int STATE_STOP = 0x1;
	private static final int STATE_DONE = 0x2;
	private static final int STATE_INFINITE_LOOP = 0x4;

	private static final Stack<Executor<?>> EXECUTORS = new Stack<>();

	private final ContinuationInternal internal;
	private final T continuation;
	private final List<Executor<?>> children;
	private int state = 0;
	private Object result;
	public Executor<?> await;

	public final String origin;
	public long lastExecutionTimeNano = 0;

	public Executor(T continuation) {
		this.internal = new ContinuationInternal(continuation.toString(), this::resume);
		this.continuation = continuation;
		this.children = new ArrayList<>(0);
		this.origin = findOrigin();
		TOTAL_TASK_INSTANCES += 1;
	}

	public static String findOrigin() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		int start = 2;
		int count = 5;
		for (int i = start; i < stackTrace.length && i < start + count; i++) {
			StackTraceElement element = stackTrace[i];
			if (element.getClassName().contains("io.ruin.process.event")) {
				continue;
			}
			if (element.getClassName().contains("core.task")) {
				continue;
			}
			return String.format(
					"%s:%d",
					element.getFileName(),
					element.getLineNumber());
		}

		return "n/a";
	}

	public static Executor<?> current() {
		if (EXECUTORS.isEmpty()) {
			throw new IllegalStateException("Not in continuation loop context.");
		}
		return EXECUTORS.lastElement();
	}

	public boolean run() {
		var start = System.nanoTime();
		if (this.continuation.shouldStop()) {
			this.stop();
		}

		if (!this.isStop()) {
			this.enterScope();
			try {
				this.internal.resume();
			} catch (Exception e) {
				log.error("Continuation failure: " + this.origin, e);
				this.stop();
			} finally {
				this.leaveScope();
			}
		}

		if (this.isDone()) {
			this.stopped(StopReason.Normal);
			this.notifyChildren(StopReason.Outside);
			this.lastExecutionTimeNano = System.nanoTime() - start;
			return true;
		}

		this.runChildren();
		this.lastExecutionTimeNano = System.nanoTime() - start;
		return false;
	}

	public void stop() {
		this.state |= STATE_STOP;
		this.state |= STATE_DONE;
	}

	public boolean isStop() {
		return (this.state & STATE_STOP) != 0;
	}

	public boolean isDone() {
		return (this.state & STATE_DONE) != 0;
	}

	public boolean done() {
		return (this.state & STATE_DONE) != 0;
	}

	public boolean isInfiniteLoop() {
		return (this.state & STATE_INFINITE_LOOP) != 0;
	}

	public boolean isRunning() {
		return !this.isDone();
	}

	public T continuation() {
		return this.continuation;
	}

	boolean childrenDone() {
		return this.children.isEmpty();
	}

	public void markInfiniteLoop() {
		this.state |= STATE_INFINITE_LOOP;
	}

	public void yield(int ticks) {
		this.internal.yield(ticks);
	}

	public <R> Executor<Continuation<R>> spawn(Continuation<R> other) {
		var executor = new Executor<>(other);
		this.children.add(executor);
		return executor;
	}

	/**
	 * Awaits for other continuation by spawning new executor and waiting <br>
	 * until it finishes.
	 */
	public <R> R await(Continuation<R> other) {
		var executor = new Executor<>(other);
		this.await = executor;
		while (!executor.run()) {
			this.yield(1);
		}
		this.await = null;
		return executor.result();
	}

	/**
	 * Awaits until all spawned children of this executor finish.
	 **/
	public void awaitChildren() {
		while (!this.childrenDone()) {
			this.yield(1);
		}
	}

	private void runChildren() {
		for (var child : this.children) {
			child.run();
		}
		this.children.removeIf(Executor::isDone);
	}

	private void resume() {
		this.continuation.started();
		this.result = this.continuation.await();
		this.state |= STATE_DONE;
	}

	private void notifyChildren(StopReason reason) {
		for (var child : this.children) {
			child.stopped(reason);
			child.notifyChildren(reason);
		}

		if (this.await != null) {
			this.await.stopped(reason);
			this.await.notifyChildren(reason);
		}
	}

	private void enterScope() {
		EXECUTORS.push(this);
	}

	private void leaveScope() {
		EXECUTORS.pop();
	}

	private void stopped(StopReason reason) {
		this.continuation.stopped(reason);
		TOTAL_TASK_INSTANCES -= 1;
	}

	@SuppressWarnings("unchecked")
	private <R> R result() {
		return (R) this.result;
	}

	public static class ContinuationInternal {
		private final jdk.internal.vm.Continuation continuation;
		private int ticks = 0;

		public ContinuationInternal(String scope, Runnable action) {
			this.continuation = new jdk.internal.vm.Continuation(null, action);
		}

		public void resume() {
			if (this.ticks > 0) {
				this.ticks -= 1;
				return;
			}
			this.continuation.run();
		}

		public void yield(int ticks) {
			if (ticks <= 0) {
				ticks = 1;
			}
			this.ticks = ticks - 1;
			jdk.internal.vm.Continuation.yield(null);
		}
	}
}
