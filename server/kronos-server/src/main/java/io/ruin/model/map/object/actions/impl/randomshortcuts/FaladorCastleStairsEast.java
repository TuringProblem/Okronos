package io.ruin.model.map.object.actions.impl.randomshortcuts;

import io.ruin.model.map.object.actions.ObjectAction;

public class FaladorCastleStairsEast {
	public static void register() { // stairs up
		ObjectAction.register(24072, 1, (player, obj) -> {
			player.startEvent(event -> {
				player.lock();
				player.getMovement().teleport(3011, 3337, 1);
				player.unlock();

			});
		});
		ObjectAction.register(24074, 1, (player, obj) -> {
			player.startEvent(event -> {
				player.lock();
				player.getMovement().teleport(3011, 3337, 0);
				player.unlock();

			});
		});

		ObjectAction.register(24077, 1, (player, obj) -> {
			player.startEvent(event -> {
				player.lock();
				player.getMovement().teleport(3024, 3332, 1);
				player.unlock();

			});
		});
	}
}



