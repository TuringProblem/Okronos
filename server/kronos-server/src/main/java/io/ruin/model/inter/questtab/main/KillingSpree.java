package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class KillingSpree extends QuestTabEntry {

	@Override
	public void send(Player player) {
		send(player, "Killing Spree", player.currentKillSpree, Color.GREEN);
	}

	@Override
	public void select(Player player) {
		//player.forceText("!" + Color.ORANGE_RED.wrap("CURRENT KILLING SPREE:") + " " + player.currentKillSpree );
	}

}