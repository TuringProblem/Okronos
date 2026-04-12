package io.ruin.network.incoming.handlers;

import io.ruin.model.entity.player.Player;

public class CS2TickButtonHandler {
	private static final int RESET_BUTTON_SCRIPT = 10539;
	private static final int TICK_BUTTON_SCRIPT = 10538;

	public static void resetButton(Player player, int interfaceId, int componentId) {
		int interfaceHash = interfaceId << 16 | componentId;
		player.getPacketSender().sendClientScript(RESET_BUTTON_SCRIPT, "I", interfaceHash);
	}

	public static void tickButton(Player player, int interfaceId, int componentId) {
		int interfaceHash = interfaceId << 16 | componentId;
		player.getPacketSender().sendClientScript(TICK_BUTTON_SCRIPT, "I", interfaceHash);
	}
}
