package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class CurrentKillStreak extends QuestTabEntry {

	@Override
	public void send(Player player) {
		send(player, "Current Kill Streak", "32", Color.GREEN);
	}

	@Override
	public void select(Player player) {
	}

}