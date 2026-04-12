package io.ruin.model.map.object.actions.impl.prifddinas.towerofvoices;

import io.ruin.model.map.object.actions.ObjectAction;

public class TowerStairsDown {

	public static void register() {
		ObjectAction.register(36390, 1, (player, obj) -> {
			player.lock();
			player.getPacketSender().fadeOut();
			player.getMovement().teleport(3263, 6078, 0);
			player.getPacketSender().fadeIn();
			player.unlock();
		});

	}

}
