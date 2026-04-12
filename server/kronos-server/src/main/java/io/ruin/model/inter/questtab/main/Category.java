package io.ruin.model.inter.questtab.main;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class Category extends QuestTabEntry {

	private String key;

	public Category(String key) {
		super.category = true;
		this.key = key;
	}

	@Override
	public void send(Player player) {
		send(player, key);
	}

	@Override
	public void select(Player player) {
	}

}