package io.ruin.model.inter.questtab.main.UsefulLinks;

import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class Vote extends QuestTabEntry {

	public static final Vote INSTANCE = new Vote();

	@Override
	public void send(Player player) {
		send(player, "Vote", "Open", Color.GREEN);
	}

	@Override
	public void select(Player player) {
		player.openUrl(World.type.getWorldName() + " Voting", World.type.getWebsiteUrl() + "/vote/");
	}
}
