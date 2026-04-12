package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.SecondaryGroup;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;
import io.ruin.utility.Misc;

public class Rank extends QuestTabEntry {

	public static final Rank INSTANCE = new Rank();

	@Override
	public void send(Player player) {
		send(player, "Rank", player.getSecondaryGroup() == null ? " " : player.getSecondaryGroup() != SecondaryGroup.NONE ? player.getSecondaryGroup().tag() + Misc.formatStringFirstCapital(player.getSecondaryGroup().name().replace("_", " ")) : Misc.formatStringFirstCapital(player.getSecondaryGroup().name().replace("_", " ")), Color.GREEN);
	}

	@Override
	public void select(Player player) {
	}

}