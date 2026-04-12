package io.ruin.process.event;

import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;

import java.util.function.Supplier;

import core.task.Continuation;

import static core.task.api.API.*;

public class Event implements Continuation.Void {

	private boolean ignoreCombatReset;
	private Supplier<Boolean> cancelCondition;
	public EventType eventType = EventType.DEFAULT;
	private final EventConsumer consumer;

	public Event(EventConsumer consumer) {
		this.consumer = consumer;
	}

	@Override
	public void call() {
		this.consumer.accept(this);
	}

	@Override
	public boolean shouldStop() {
		if (this.cancelCondition != null && this.cancelCondition.get()) {
			return true;
		}
		return false;
	}

	public final void delay(int ticks) {
		sleep(ticks);
	}

	public final void waitForMovement(Entity entity) {
		while (!entity.getMovement().isAtDestination())
			delay(1);
	}

	public final void waitForTile(Entity entity, Position position) {
		waitForTile(entity, position.getX(), position.getY());
	}

	public final void waitForTile(Entity entity, int x, int y) {
		while (!entity.isAt(x, y))
			delay(1);
	}

	public final void waitForEntityToBeAtPos(Entity entity, Position pos) {
		while (entity.getPosition().distance(pos) > 1)
			delay(1);
	}

	public final Event ignoreCombatReset() {
		this.ignoreCombatReset = true;
		return this;
	}

	public boolean isIgnoreCombatReset() {
		return ignoreCombatReset;
	}

	public final void waitForDialogue(Player player) {
		while (player.hasDialogue())
			delay(1);
	}

	public final void path(Entity entity, Position... waypoints) {
		for (Position pos : waypoints) {
			entity.getRouteFinder().routeAbsolute(pos.getX(), pos.getY());
			waitForMovement(entity);
		}
	}

	public final void waitForCondition(Supplier<Boolean> condition, int timeout) {
		int time = 0;
		while (!condition.get() && time < timeout) {
			time++;
			delay(1);
		}
	}

	public final boolean persistent() {
		return eventType == EventType.PERSISTENT;
	}

	/**
	 * When returning from a pause (delay), the given condition will be checked, and if met, the event will be stopped
	 */
	public final void setCancelCondition(Supplier<Boolean> cancelCondition) {
		this.cancelCondition = cancelCondition;
	}
}
