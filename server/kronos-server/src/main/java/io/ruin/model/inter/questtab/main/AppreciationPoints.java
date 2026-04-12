package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class AppreciationPoints extends QuestTabEntry {

	public static final AppreciationPoints INSTANCE = new AppreciationPoints();

	@Override
	public void send(Player player) {
		send(player, "Appreciation Points", player.afkPoints, Color.GREEN);
	}

	@Override
	public void select(Player player) {
		player.sendMessage("You get between 5-15 points every minute of playtime! You can spend your points on a variety of items. You currently have "
			+ player.afkPoints + " appreciate points.");
	}

}