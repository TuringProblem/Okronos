package io.ruin.model.inter.questtab.toggles;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class BroadcastBloodyMerchant extends QuestTabEntry {

	@Override
	public void send(Player player) {
		if (!player.broadcastActiveVolcano)
			send(player, "Bloody Merchant", "Disabled", Color.RED);
		else
			send(player, "Bloody Merchant", "Enabled", Color.GREEN);
	}

	@Override
	public void select(Player player) {
		player.broadcastBloodyMechant = !player.broadcastBloodyMechant;
		if (player.broadcastBloodyMechant)
			player.sendMessage(Color.DARK_GREEN.wrap("You will now get broadcasted messages about the Bloody Merchant."));
		else
			player.sendMessage(Color.DARK_GREEN.wrap("You will no longer get broadcasted messaged about the Bloody Merchant."));
		send(player);
	}

}
