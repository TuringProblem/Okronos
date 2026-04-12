package io.ruin.model.map.object.actions.impl;

import io.ruin.model.map.object.actions.ObjectAction;

public class LumbridgeSwampCaveEntrance {
	public static void register() {
		ObjectAction.register(5947, 1, (player, obj) -> player.startEvent(event -> {
			player.lock();
			player.animate(828);
			event.delay(2);
			player.getMovement().teleport(3169, 9571, 0);
			player.unlock();
		}));
		ObjectAction.register(5946, 1, (player, obj) -> player.startEvent(event -> {
			player.lock();
			player.animate(828);
			event.delay(2);
			player.getMovement().teleport(3169, 3173, 0);
			player.unlock();
		}));
	}
}
