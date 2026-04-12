package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class PvmPoints extends QuestTabEntry {

	public static final PvmPoints INSTANCE = new PvmPoints();

	@Override
	public void send(Player player) {
		send(player, "PvM Points", player.PvmPoints, Color.GREEN);
	}

	@Override
	public void select(Player player) {
		player.sendMessage("You gain PvM Points from killing all kinds of monsters! You currently have "
			+ player.PvmPoints + " PvM Points.");
	}

}