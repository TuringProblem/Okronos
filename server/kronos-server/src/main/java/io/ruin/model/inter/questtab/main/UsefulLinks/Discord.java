package io.ruin.model.inter.questtab.main.UsefulLinks;

import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class Discord extends QuestTabEntry {

	public static final Discord INSTANCE = new Discord();

	@Override
	public void send(Player player) {
		send(player, "Discord", "Open", Color.GREEN);
	}

	@Override
	public void select(Player player) {
		//  player.openUrl(World.type.getWorldName() + " Discord", "https://discord.gg/Prifddinas");
	}
}
