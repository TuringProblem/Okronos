package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class DoublePkp extends QuestTabEntry {

	public static final DoublePkp INSTANCE = new DoublePkp();

	@Override
	public void send(Player player) {
		if (World.doublePkp) {
			send(player, "Double Pkp", "Enabled", Color.GREEN);
		} else {
			send(player, "Double Pkp", "Disabled", Color.RED);
		}
	}

	@Override
	public void select(Player player) {
		if (World.doublePkp) {
			player.sendMessage("Double Pk Points is active!");
		} else {
			player.sendMessage("Double Pk Points are disabled!");
		}
	}

}