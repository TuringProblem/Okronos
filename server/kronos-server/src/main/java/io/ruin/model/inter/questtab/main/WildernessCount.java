package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.activities.wilderness.Wilderness;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class WildernessCount extends QuestTabEntry {

	public static final WildernessCount INSTANCE = new WildernessCount();

	@Override
	public void send(Player player) {
		send(player, "Players in Wilderness", Wilderness.players.size(), Color.GREEN);
	}

	@Override
	public void select(Player player) {
		player.sendMessage("There are currently " + Wilderness.players.size() + " players in the wilderness.");
	}

}