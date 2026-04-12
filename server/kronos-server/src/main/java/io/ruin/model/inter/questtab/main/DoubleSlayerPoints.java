package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class DoubleSlayerPoints extends QuestTabEntry {

	public static final DoubleSlayerPoints INSTANCE = new DoubleSlayerPoints();

	@Override
	public void send(Player player) {
		if (World.doubleSlayer) {
			send(player, "Double Slayer points", "Enabled", Color.GREEN);
		} else {
			send(player, "Double Slayer Points", "Disabled", Color.RED);
		}
	}

	@Override
	public void select(Player player) {
		if (World.doubleSlayer) {
			player.sendMessage("Double Slayer Points is active!");
		} else {
			player.sendMessage("Double Slayer Points are disabled!");
		}
	}

}