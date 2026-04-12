package core.task;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Continuations {
	private static final Continuations INSTANCE = new Continuations();

	private List<Executor<?>> continuations;
	private Queue<Executor<?>> queue;

	public Continuations() {
		this.continuations = new ArrayList<>();
		this.queue = new ArrayDeque<>();
	}

	public static boolean tick() {
		try {
			run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INSTANCE.continuations.isEmpty();
	}

	public static boolean done() {
		if (!INSTANCE.queue.isEmpty()) {
			return false;
		}

		return INSTANCE.continuations.isEmpty();
	}

	public static boolean hasInifiniteLoop() {
		for (var continuation : INSTANCE.continuations) {
			if (continuation.isInfiniteLoop()) {
				return true;
			}
		}
		return false;
	}

	public static boolean onlyInfiniteLoop() {
		for (var continuation : INSTANCE.continuations) {
			if (!continuation.isInfiniteLoop()) {
				return false;
			}
		}
		return !INSTANCE.continuations.isEmpty();
	}

	public static List<Executor<?>> current() {
		return INSTANCE.continuations;
	}

	private static void run() {
		while (!INSTANCE.queue.isEmpty()) {
			INSTANCE.continuations.add(INSTANCE.queue.poll());
		}
		INSTANCE.continuations.removeIf(Executor::run);
	}

	public static void cleanup() {
		INSTANCE.continuations.clear();
		INSTANCE.queue.clear();
	}

	public static <T extends Continuation<?>> Executor<T> scheduletyped(T continuation) {
		var executor = new Executor<>(continuation);
		INSTANCE.queue.offer(executor);
		return executor;
	}

	public static Executor<?> schedule(Continuation.Void continuation) {
		var executor = new Executor<>(continuation);
		INSTANCE.queue.offer(executor);
		return executor;
	}

	public static void shutdown() {
		cleanup();
	}
}
