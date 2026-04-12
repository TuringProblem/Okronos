package io.ruin.model.inter.questtab.toggles;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.inter.questtab.JournalTab.TabComponent;
import io.ruin.utility.Misc;

public class DiscardBuckets extends JournalToggle {

	@Override
	public void handle(Player player) {
		player.discardBuckets = !player.discardBuckets;
		send(player);
	}

	@Override
	public JournalTab.TextField getText() {
		return player -> "Discard Buckets: " + Misc.stateOf(player.discardBuckets, true);
	}

	@Override
	public JournalTab.TabComponent getComponent() {
		return JournalTab.TabComponent.DISCARD_BUCKETS;
	}

}
