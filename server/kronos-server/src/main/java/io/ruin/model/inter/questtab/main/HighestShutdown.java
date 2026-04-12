package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class HighestShutdown extends QuestTabEntry {

	@Override
	public void send(Player player) {
		send(player, "Highest Shutdown", player.highestShutdown, Color.GREEN);
	}

	@Override
	public void select(Player player) {
		//player.forceText("!" + Color.ORANGE_RED.wrap("HIGHEST SHUTDOWN:") + " " + player.highestShutdown );
	}

}