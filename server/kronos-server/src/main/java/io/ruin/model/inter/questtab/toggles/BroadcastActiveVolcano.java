package io.ruin.model.inter.questtab.toggles;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.inter.questtab.JournalTab.TabComponent;
import io.ruin.utility.Misc;

public class BroadcastActiveVolcano extends JournalToggle {

	@Override
	public void handle(Player player) {
		player.broadcastActiveVolcano = !player.broadcastActiveVolcano;
		if (player.broadcastActiveVolcano)
			player.sendMessage(Color.DARK_GREEN.wrap("You will now get broadcasted messages about the Active Volcano event."));
		else
			player.sendMessage(Color.DARK_GREEN.wrap("You will no longer get broadcasted messaged about the Active Volcano event."));
		send(player);
	}

	@Override
	public JournalTab.TextField getText() {
		return player -> "Active Volcano: " + Misc.stateOf(player.broadcastActiveVolcano, true);
	}

	@Override
	public JournalTab.TabComponent getComponent() {
		return JournalTab.TabComponent.BC_VOLCANO;
	}

}
