package core.task.api;

import core.task.*;

public class API {
	public static void queue(Continuation<?> task) {
		Continuations.scheduletyped(task);
	}

	public static void queue(Continuation.Void task) {
		Continuations.schedule(task);
	}

	public static Executor<?> fork(Continuation.Void task) {
		return Executor.current().spawn(task);
	}

	public static Executor<?> fork(Continuation.Void continuation, Runnable whenStopped) {
		return fork(new Continuation.Void() {

			@Override
			public void call() {
				continuation.call();
			}

			@Override
			public void started() {
				continuation.started();
			}

			@Override
			public void stopped(StopReason reason) {
				continuation.stopped(reason);
				if (whenStopped != null) {
					whenStopped.run();
				}
			}
		});
	}

	public static void loop(Continuation.Loop.Action action) {
		Executor.current().await(new Continuation.Loop(action));
	}

	public static Executor<?> forkLoop(Continuation.Loop.Action action) {
		return fork(new Continuation.Loop(action));
	}

	public static void mark_infinite_loop() {
		Executor.current().markInfiniteLoop();
	}

	public static void await_children() {
		Executor.current().awaitChildren();
	}

	public static void sleep(int ticks) {
		Executor.current().yield(ticks);
	}
}
