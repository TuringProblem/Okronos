package io.ruin.model.inter.questtab.bestiary;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class BestiarySearchMonster extends QuestTabEntry {

	public static final BestiarySearchMonster INSTANCE = new BestiarySearchMonster();

	@Override
	public void send(Player player) {
		send(player, "<img=108> Search for Monster", Color.ORANGE);
	}

	@Override
	public void select(Player player) {
		player.stringInput("<img=108> Enter monster name to search for:", name -> Bestiary.search(player, name, true));
	}

}