package core.task.test;

import core.task.Continuations;

// For the usage with tests
public class ContinuationTestExecutor {

	// await until all continuations are done
	// and some time is given for infinite loops
	public static void await() {
		var lastInfiniteLoop = -1L;

		while (true) {
			Continuations.tick();


			// when everything ticked and returned, exit
			if (Continuations.done()) {
				return;
			}

			// when we have infinite loop in queue, start the timer for them
			if (Continuations.hasInifiniteLoop() && lastInfiniteLoop == -1) {
				lastInfiniteLoop = System.currentTimeMillis();
			}

			if (Continuations.onlyInfiniteLoop()) {
				// give some time for infinite loops to do the work
				if (lastInfiniteLoop != -1 && System.currentTimeMillis() - lastInfiniteLoop >= 50) {
					return;
				}
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
