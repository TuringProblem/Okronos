package io.ruin.model.map.object.actions.impl.prifddinas.trahaearndistrict;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.map.object.actions.ObjectAction;

public class ZalcanoEntrance {

	public static void register() {
		ObjectAction.register(36198, "channel", (player, obj) -> enterZalcano(player));
		ObjectAction.register(36197, "channel", (player, obj) -> leaveZalcano(player));
		ObjectAction.register(36201, "pass", (player, obj) -> enterBoss(player));
		ObjectAction.register(36201, "peek", (player, obj) -> peekBoss(player));
	}

	public static void enterZalcano(Player player) {//3283, 6058, 0
		player.lock();
		player.getPacketSender().fadeOut();
		player.dialogue(
			new MessageDialogue("Welcome to Zalcano's lair.")
		);
		player.getMovement().teleport(3034, 6068, 0);
		player.getPacketSender().fadeIn();
		player.unlock();
	}

	public static void leaveZalcano(Player player) {//3034, 6068
		player.lock();
		player.getPacketSender().fadeOut();
		player.dialogue(
			new MessageDialogue("Welcome to the Trahaearn district.")
		);
		player.getMovement().teleport(3283, 6058, 0);
		player.getPacketSender().fadeIn();
		player.unlock();
	}

	public static void enterBoss(Player player) {
		player.lock();
		player.stepAbs(3034, 6061, StepType.WALK);
		player.unlock();
	}

	public static void peekBoss(Player player) {
		player.sendMessage("<col=880000>This feature will be added at a later date...");
	}

}
