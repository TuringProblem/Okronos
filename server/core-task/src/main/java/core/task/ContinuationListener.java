package core.task;

public interface ContinuationListener {
	default void started() {
	}

	/// Called after await() or other reason specified at StopReason
	default void stopped(StopReason reason) {
	}

	enum StopReason {
		// When continuation finished job properly.
		Normal,
		// When the continuation received signal from outside (executor stopped) that it
		// should be stopped.
		Outside,
		// When the continuation's parent has been stopped, a child cannot continue,
		// hence needs to be
		// stopped.
		OutsideParent,
		// When the continuation was stopped due to the underlying exception
		Exception;

		public boolean exceptional() {
			return this != StopReason.Normal;
		}
	}
}
