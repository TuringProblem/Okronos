package core.task;

/**
 * Continuation signal is used to dermine whether the continuation is
 * running/done or to force stop
 * the execution.
 **/
public final class ContinuationSignal {
	boolean done;

	ContinuationSignal() {
		this.done = false;
	}

	public boolean done() {
		return this.done;
	}

	public void forceStop() {
		this.done = true;
	}
}
