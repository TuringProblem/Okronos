package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class DoublePestControl extends QuestTabEntry {

	public static final DoublePestControl INSTANCE = new DoublePestControl();

	@Override
	public void send(Player player) {
		if (World.doublePest) {
			send(player, "Double PC", "Enabled", Color.GREEN);
		} else {
			send(player, "Double PC", "Disabled", Color.RED);
		}
	}

	@Override
	public void select(Player player) {
		if (World.doubleSlayer) {
			player.sendMessage("Double Pest Control Points is active!");
		} else {
			player.sendMessage("Double Pest Control Points are disabled!");
		}
	}

}