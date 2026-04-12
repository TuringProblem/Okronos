package io.ruin.model.inter.questtab.main.UsefulLinks;

import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class Donate extends QuestTabEntry {

	public static final Donate INSTANCE = new Donate();

	@Override
	public void send(Player player) {
		send(player, "Donate", "Open", Color.GREEN);
	}

	@Override
	public void select(Player player) {
		player.openUrl(World.type.getWorldName() + " Store", World.type.getWebsiteUrl() + "/store/");
	}
}
