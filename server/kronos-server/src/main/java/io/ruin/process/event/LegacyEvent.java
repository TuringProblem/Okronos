
package io.ruin.process.event;

import core.task.Continuation;
import core.task.Executor;

// kind of event that is 'executed' at specific places using executor
// for example 'current player event' gets executed each time player is processed
// same with the 'npc' events, these events are contextualized and ordered as opposed to
// the global event executor.
public class LegacyEvent {

	private final Executor<?> executor;
	public final Event event;

	public LegacyEvent(Event event) {
		this.executor = new Executor<Continuation.Void>(event);
		this.event = event;
	}

	public boolean tick() {
		this.executor.run();
		return !this.executor.isDone();
	}

	public void stop() {
		this.executor.stop();
	}
}
