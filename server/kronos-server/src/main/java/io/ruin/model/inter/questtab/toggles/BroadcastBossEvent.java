package io.ruin.model.inter.questtab.toggles;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.inter.questtab.JournalTab.TabComponent;
import io.ruin.utility.Misc;

public class BroadcastBossEvent extends JournalToggle {

	@Override
	public void handle(Player player) {
		player.broadcastBossEvent = !player.broadcastBossEvent;
		if (player.broadcastBossEvent)
			player.sendMessage(Color.DARK_GREEN.wrap("You will now get broadcasted messages about the Wilderness Boss event."));
		else
			player.sendMessage(Color.DARK_GREEN.wrap("You will no longer get broadcasted messaged about the Wilderness Boss event."));
		send(player);
	}

	@Override
	public JournalTab.TextField getText() {
		return player -> "Boss Events: " + Misc.stateOf(player.broadcastBossEvent, true);
	}

	@Override
	public JournalTab.TabComponent getComponent() {
		return JournalTab.TabComponent.BC_BOSS_EVENTS;
	}

}
