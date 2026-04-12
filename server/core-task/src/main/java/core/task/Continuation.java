package core.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;

@FunctionalInterface
public interface Continuation<T> extends ContinuationListener {

	T await();

	default boolean shouldStop() {
		return false;
	}

	@RequiredArgsConstructor
	public static class Loop implements Continuation.Void {
		public static class Context {
			private int cycle = 0;

			public int cycle() {
				return this.cycle;
			}

			public void reset() {
				this.cycle = -1;
			}

			public void stop() {
				this.cycle = -2;
			}
		}

		@FunctionalInterface
		public static interface Action {
			void cycle(Context ctx);
		}

		private final Action action;

		@Override
		public void call() {
			var ctx = new Context();
			while (true) {
				action.cycle(ctx);
				if (ctx.cycle == -2) {
					break;
				}
				ctx.cycle += 1;
			}
		}

		@Override
		public String toString() {
			return super.toString() + "@"
					+ this.action.toString().replace(this.action.getClass().getPackageName(), "");
		}
	}

	interface Void extends Continuation<Object> {
		void call();

		@Override
		default Object await() {
			this.call();
			return null;
		}
	}
}
