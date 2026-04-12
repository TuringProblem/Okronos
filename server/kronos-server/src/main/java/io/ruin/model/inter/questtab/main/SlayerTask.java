package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

/**
 * @author Danny
 */
public class SlayerTask extends QuestTabEntry {

	public static final SlayerTask INSTANCE = new SlayerTask();

	@Override
	public void send(Player player) {
		String taskName = player.slayerTaskName;
		int taskAmount = player.slayerTaskRemaining;
		send(player, "Task", (taskName == null ? "None" : taskName + " x" + taskAmount), Color.GREEN);
	}

	@Override
	public void select(Player player) {
	}
}
