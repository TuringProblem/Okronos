package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.content.drop_rate.DropRateBonusManager;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class DropBonus extends QuestTabEntry {

	public static final DropBonus INSTANCE = new DropBonus();

	@Override
	public void send(Player player) {
		// send(player, "Drop Bonus", DropRateBonusManager.getInstance().getTotalBonusDropRate(player) + "%", Color.GREEN);
	}

	@Override
	public void select(Player player) {
	}

}