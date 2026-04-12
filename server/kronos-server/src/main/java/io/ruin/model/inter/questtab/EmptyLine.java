package io.ruin.model.inter.questtab;

import io.ruin.model.entity.player.Player;

/**
 * @author Danny
 */
public class EmptyLine extends QuestTabEntry {

	private String key;

	public EmptyLine(String key) {
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
