package io.ruin.model.inter.questtab.toggles;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.inter.questtab.JournalTab.TabComponent;
import io.ruin.utility.Misc;


public class ShowTimers extends JournalToggle {

	@Override
	public void handle(Player player) {
		player.showTimers = !player.showTimers;
		send(player);
	}

	@Override
	public JournalTab.TextField getText() {
		return player -> "Show Timers: " + Misc.stateOf(player.showTimers, true);
	}

	@Override
	public JournalTab.TabComponent getComponent() {
		return JournalTab.TabComponent.TIMER_TOGGLE;
	}

	@Override
	public void onSend(Player player) {
		player.getPacketSender().sendVarp(20004, player.showTimers ? 1 : 0);
	}

}
