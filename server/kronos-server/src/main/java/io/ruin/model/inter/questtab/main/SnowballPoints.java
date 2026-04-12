package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class SnowballPoints extends QuestTabEntry {

	public static final SnowballPoints INSTANCE = new SnowballPoints();

	@Override
	public void send(Player player) {
		send(player, "Snowball Points", player.snowballPoints, Color.GREEN);
	}

	@Override
	public void select(Player player) {
		player.sendMessage("You can get snowball points by creating snowballs and throwing them at people!");
	}

}