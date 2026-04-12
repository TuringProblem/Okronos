package io.ruin.model.inter.questtab.toggles;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.inter.questtab.JournalTab.TabComponent;
import io.ruin.utility.Misc;

public class BreakVials extends JournalToggle {

	@Override
	public void handle(Player player) {
		player.breakVials = !player.breakVials;
		send(player);
	}

	@Override
	public JournalTab.TextField getText() {
		return player -> "Break Vials: " + Misc.stateOf(player.breakVials, true);
	}

	@Override
	public JournalTab.TabComponent getComponent() {
		return JournalTab.TabComponent.BREAK_VIALS;
	}

}