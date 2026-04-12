package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class tournamentPoints extends QuestTabEntry {

	public static final tournamentPoints INSTANCE = new tournamentPoints();

	@Override
	public void send(Player player) {
		send(player, "Tournament Points", player.tournamentPoints, Color.GREEN);
	}

	@Override
	public void select(Player player) {
		player.sendMessage("You gain Tournament Points from placing 1st, 2nd & 3rd in tournaments! You currently have "
			+ player.tournamentPoints + " Tournament Points.");
	}
}
