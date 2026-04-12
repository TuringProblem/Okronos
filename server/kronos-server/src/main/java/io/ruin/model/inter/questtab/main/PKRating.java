package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class PKRating extends QuestTabEntry {

	public static final PKRating INSTANCE = new PKRating();

	@Override
	public void send(Player player) {
		send(player, "PK Rating", player.pkRating, Color.GREEN);
	}

	@Override
	public void select(Player player) {
		//player.forceText("!" + Color.ORANGE_RED.wrap("PK RATING:") + " " + player.pkRating);
	}

}