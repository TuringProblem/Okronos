package io.ruin.model.map.object.actions.impl.prifddinas.trahaearndistrict;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.map.object.actions.ObjectAction;

public class TrahaearnMine {

	public static void register() {
		ObjectAction.register(36556, "enter", (player, obj) -> enterMine(player));
		ObjectAction.register(36215, "exit", (player, obj) -> leaveMine(player));
		ObjectAction.register(36219, "deposit", (p, obj) -> p.getBank().openDepositBox());
	}

	public static void enterMine(Player player) {
		player.lock();
		player.getPacketSender().fadeOut();
		player.dialogue(
			new MessageDialogue("Welcome to the Trahaearn Mine.")
		);
		player.getMovement().teleport(3302, 12454, 0);
		player.getPacketSender().fadeIn();
		player.unlock();
	}

	public static void leaveMine(Player player) {
		player.lock();
		player.getPacketSender().fadeOut();
		player.dialogue(
			new MessageDialogue("Welcome to the Trahaearn district.")
		);
		player.getMovement().teleport(3271, 6051, 0);
		player.getPacketSender().fadeIn();
		player.unlock();
	}

}
