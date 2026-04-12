package io.ruin.model.inter.questtab.main.UsefulLinks;

import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class Forums extends QuestTabEntry {

	public static final Forums INSTANCE = new Forums();

	@Override
	public void send(Player player) {
		send(player, "Forums", "Open", Color.GREEN);
	}

	@Override
	public void select(Player player) {
		player.openUrl(World.type.getWorldName() + " Forums", World.type.getWebsiteUrl() + "/forums/");
	}
}
