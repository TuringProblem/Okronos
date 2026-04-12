package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class VotePoints extends QuestTabEntry {

	public static final VotePoints INSTANCE = new VotePoints();

	@Override
	public void send(Player player) {
		send(player, "Times Voted", player.claimedVotes, Color.GREEN);
	}

	@Override
	public void select(Player player) {
		player.sendMessage("You can get voting reward points by voting on our website, and earn rewards too!");
	}

}