package io.ruin.model.inter.handlers;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;

public abstract class TabEntry {

	public boolean category;
	public int childId;

	protected final void send(Player player, String text) {
		player.getPacketSender().sendString(701, childId, text);
	}

	protected final void send(Player player, String text, Color color) {
		player.getPacketSender().sendString(701, childId, color.wrap(text));
	}

	protected final void send(Player player, String key, String value, Color color) {
		player.getPacketSender().sendString(701, childId, ("<col=D37E2A>" + key + ":</col> " + color.wrap(value)));
	}

	protected final void send(Player player, String key, int value, Color color) {
		if (value == 0)
			color = Color.GREEN;
		player.getPacketSender().sendString(701, childId, ("<col=D37E2A>" + key + ":</col> " + color.wrap(String.valueOf(value))));
	}

	public abstract void send(Player player);

	public abstract void select(Player player);

}
