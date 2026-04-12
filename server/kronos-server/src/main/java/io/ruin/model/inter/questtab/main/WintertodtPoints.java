package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class WintertodtPoints extends QuestTabEntry {

	public static final WintertodtPoints INSTANCE = new WintertodtPoints();

	@Override
	public void send(Player player) {
		send(player, "Wintertodt Points", player.wintertodtstorePoints, Color.GREEN);
	}

	@Override
	public void select(Player player) {
		player.sendMessage("You gain Wintertodt Points from completing wintertodt events! You currently have "
			+ player.wintertodtstorePoints + " Wintertodt Points.");
	}
}
