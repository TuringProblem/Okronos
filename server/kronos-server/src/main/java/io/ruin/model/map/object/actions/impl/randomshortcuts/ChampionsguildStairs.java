package io.ruin.model.map.object.actions.impl.randomshortcuts;

import io.ruin.model.map.object.actions.ObjectAction;

public class ChampionsguildStairs {
	public static void register() { // stairs up
		ObjectAction.register(11797, 1, (player, obj) -> {
			player.startEvent(event -> {
				player.lock();
				player.getMovement().teleport(3189, 3354, 1);
				player.unlock();

			});
		});
		ObjectAction.register(11799, 1, (player, obj) -> {
			player.startEvent(event -> {
				player.lock();
				player.getMovement().teleport(3189, 3358, 0);
				player.unlock();
			});
		});
	}
}



