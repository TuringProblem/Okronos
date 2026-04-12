package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class BossPoints extends QuestTabEntry {

	public static final BossPoints INSTANCE = new BossPoints();

	@Override
	public void send(Player player) {
		send(player, "Boss Points", player.bossPoints, Color.GREEN);
	}

	@Override
	public void select(Player player) {
		player.sendMessage("You gain Boss Points from killing bosses! You currently have "
			+ player.bossPoints + " Boss Points.");
	}

}
