package io.ruin.model.inter.questtab.toggles;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.inter.questtab.JournalTab.TabComponent;
import io.ruin.utility.Misc;

public class HideIcon extends JournalToggle {

	@Override
	public void handle(Player player) {
		if (player.isModerator() || player.isSupport() || player.isAdmin()) {
			player.sendFilteredMessage("You're unable to use this feature as a staff member.");
			player.hidePlayerIcon = false;
			send(player);
			return;
		}
		if (player.getGameMode().isIronMan()) {
			player.sendFilteredMessage("You're unable to use this feature as a iron man.");
			player.hidePlayerIcon = false;
			send(player);
			return;
		}
		player.hidePlayerIcon = !player.hidePlayerIcon;
		send(player);
	}

	@Override
	public JournalTab.TextField getText() {
		return player -> "Hide Icon: " + Misc.stateOf(player.hidePlayerIcon, true);
	}

	@Override
	public JournalTab.TabComponent getComponent() {
		return JournalTab.TabComponent.HIDE_ICON;
	}

}