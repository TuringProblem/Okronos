package io.ruin.model.inter.questtab.toggles;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.inter.questtab.JournalTab.TabComponent;
import io.ruin.utility.Misc;

public class BroadcastHotspot extends JournalToggle {

	@Override
	public void handle(Player player) {
		player.broadcastHotspot = !player.broadcastHotspot;
		if (player.broadcastHotspot)
			player.sendMessage(Color.DARK_GREEN.wrap("You will now get broadcasted messages about the Hotspot."));
		else
			player.sendMessage(Color.DARK_GREEN.wrap("You will no longer get broadcasted messaged about the Hotspot."));
		send(player);
	}

	@Override
	public JournalTab.TextField getText() {
		return player -> "Hotspot: " + Misc.stateOf(player.broadcastHotspot, true);
	}

	@Override
	public JournalTab.TabComponent getComponent() {
		return JournalTab.TabComponent.BC_HOTSPOT;
	}

}
