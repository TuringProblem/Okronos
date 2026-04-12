package io.ruin.model.inter.questtab.toggles;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.inter.questtab.JournalTab.TabComponent;
import io.ruin.utility.Misc;

public class BountyOverlay extends JournalToggle {

	@Override
	public void handle(Player player) {
		player.bountyHunterOverlay = !player.bountyHunterOverlay;
		if (player.bountyHunterOverlay)
			player.sendMessage(Color.DARK_GREEN.wrap("You will no longer see bounty hunter overlay in wilderness."));
		else
			player.sendMessage(Color.DARK_GREEN.wrap("You will no longer see bounty hunter overlay in wilderness."));
		send(player);
	}

	@Override
	public JournalTab.TextField getText() {
		return player -> "Bounty Overlay: " + Misc.stateOf(player.bountyHunterOverlay, true);
	}

	@Override
	public JournalTab.TabComponent getComponent() {
		return JournalTab.TabComponent.BH_OVERLAY;
	}

}
