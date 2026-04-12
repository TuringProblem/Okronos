package io.ruin.model.inter.questtab.toggles;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.inter.questtab.JournalTab.TabComponent;
import io.ruin.utility.Misc;

public class HideYells extends JournalToggle {

	@Override
	public void handle(Player player) {
		player.yellFilter = !player.yellFilter;
		send(player);
	}

	@Override
	public JournalTab.TextField getText() {
		return player -> "Hide Yells: " + Misc.stateOf(player.yellFilter, true);
	}

	@Override
	public JournalTab.TabComponent getComponent() {
		return JournalTab.TabComponent.HIDE_YELLS;
	}

}