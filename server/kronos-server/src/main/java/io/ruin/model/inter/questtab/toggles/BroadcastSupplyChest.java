package io.ruin.model.inter.questtab.toggles;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.inter.questtab.JournalTab.TabComponent;
import io.ruin.utility.Misc;

public class BroadcastSupplyChest extends JournalToggle {

	@Override
	public void handle(Player player) {
		player.broadcastSupplyChest = !player.broadcastSupplyChest;
		if (player.broadcastSupplyChest)
			player.sendMessage(Color.DARK_GREEN.wrap("You will now get broadcasted messages about the Supply Chest."));
		else
			player.sendMessage(Color.DARK_GREEN.wrap("You will no longer get broadcasted messaged about the Supply Ches."));
		send(player);
	}

	@Override
	public JournalTab.TextField getText() {
		return player -> "Supply Chest: " + Misc.stateOf(player.broadcastSupplyChest, true);
	}

	@Override
	public JournalTab.TabComponent getComponent() {
		return JournalTab.TabComponent.BC_SUPPLY_CHEST;
	}

}
