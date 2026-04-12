package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class CurrentDeathStreak extends QuestTabEntry {

	@Override
	public void send(Player player) {
		send(player, "Current Death Streak", "0", Color.GREEN);
	}

	@Override
	public void select(Player player) {
	}

}