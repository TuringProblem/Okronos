package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

/**
 * @author Danny
 */
public class SlayerStreak extends QuestTabEntry {

	public static final SlayerStreak INSTANCE = new SlayerStreak();

	@Override
	public void send(Player player) {
		send(player, "Streak", player.slayerTasksCompleted, Color.GREEN);
	}

	@Override
	public void select(Player player) {

	}
}
