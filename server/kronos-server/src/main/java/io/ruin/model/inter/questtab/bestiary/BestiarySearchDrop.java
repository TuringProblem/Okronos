package io.ruin.model.inter.questtab.bestiary;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class BestiarySearchDrop extends QuestTabEntry {

	public static final BestiarySearchDrop INSTANCE = new BestiarySearchDrop();

	@Override
	public void send(Player player) {
		send(player, "<img=33> Search for Drop", Color.ORANGE);
	}

	@Override
	public void select(Player player) {
		player.stringInput("<img=33> Enter drop name to search for:", name -> Bestiary.search(player, name, false));
	}

}