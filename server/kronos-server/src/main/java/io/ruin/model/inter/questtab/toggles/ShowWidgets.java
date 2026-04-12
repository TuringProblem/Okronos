package io.ruin.model.inter.questtab.toggles;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class ShowWidgets extends QuestTabEntry {

	public static void update(Player player) {
		player.getPacketSender().sendVarp(20004, player.showTimers ? 1 : 0);
	}

	@Override
	public void send(Player player) {
		if (!player.showTimers)
			send(player, "Show Timers", "Disabled", Color.RED);
		else
			send(player, "Show Timers", "Enabled", Color.GREEN);
		update(player);
	}

	@Override
	public void select(Player player) {
		player.showTimers = !player.showTimers;
		send(player);
	}

}