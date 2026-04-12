package io.ruin.model.inter.notify;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;

public class NotificationInterface {

	public static void open(Player player, String title, String text) {
		open(player, title, text, 16750623);
	}

	public static void open(Player player, String title, String text, int color) {
		open(player, title, text, color, 10);
	}

	public static void open(Player player, String title, String text, int color, int lifecycle) {
		player.openInterface(ToplevelComponent.NOTIFICATION, 660);
		player.getPacketSender().sendClientScript(3343, new Object[] { title, text, color });
		World.startEvent((e) -> {
			e.delay(lifecycle);
			player.closeInterface(ToplevelComponent.NOTIFICATION);
		});
	}
}
