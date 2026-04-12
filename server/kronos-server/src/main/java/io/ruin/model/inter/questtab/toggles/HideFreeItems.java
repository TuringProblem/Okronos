package io.ruin.model.inter.questtab.toggles;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class HideFreeItems extends QuestTabEntry {

	@Override
	public void send(Player player) {
		if (!player.hideFreeItems)
			send(player, "Hide Free Items", "Disabled", Color.RED);
		else
			send(player, "Hide Free Items", "Enabled", Color.GREEN);
	}

	@Override
	public void select(Player player) {
		player.hideFreeItems = !player.hideFreeItems;
		if (player.hideFreeItems)
			player.sendMessage(Color.DARK_GREEN.wrap("Items that are free from the shops will no longer be dropped when killing a player."));
		else
			player.sendMessage(Color.DARK_GREEN.wrap("Items that are free will now be dropped when killing a player."));
		send(player);
	}

}
