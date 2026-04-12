package io.ruin.model.map.object.actions.impl.prifddinas.towerofvoices;

import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.map.object.actions.impl.Ladder;

public class TowerStairsUp {

	public static void register() {
		ObjectAction.register(36387, 1, (player, obj) -> {
			player.lock();
			player.getPacketSender().fadeOut();
			player.getMovement().teleport(3268, 6082, 2);
			player.getPacketSender().fadeIn();
			player.unlock();
		});

	}

}
