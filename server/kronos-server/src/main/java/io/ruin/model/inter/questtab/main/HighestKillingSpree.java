package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class HighestKillingSpree extends QuestTabEntry {

	@Override
	public void send(Player player) {
		send(player, "Highest Killing Spree", player.highestKillSpree, Color.GREEN);
	}

	@Override
	public void select(Player player) {
		//player.forceText("!" + Color.ORANGE_RED.wrap("HIGHEST KILLING SPREE:") + " " + player.highestKillSpree );
	}

}