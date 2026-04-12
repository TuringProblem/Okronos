package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class DoubleWintertodt extends QuestTabEntry {

	public static final DoubleWintertodt INSTANCE = new DoubleWintertodt();

	@Override
	public void send(Player player) {
		if (World.doubleWintertodt) {
			send(player, "Double Wintertodt", "Enabled", Color.GREEN);
		} else {
			send(player, "Double Wintertodt", "Disabled", Color.RED);
		}
	}

	@Override
	public void select(Player player) {
		if (World.doubleWintertodt) {
			player.sendMessage("Double Wintertodt Points is active!");
		} else {
			player.sendMessage("Double Wintertodt Points are disabled!");
		}
	}
}
