package io.ruin.model.inter.questtab.toggles;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.JournalTab;


public abstract class JournalToggle {

	public abstract void handle(Player player);

	public abstract JournalTab.TextField getText();

	public abstract JournalTab.TabComponent getComponent();

	public void send(Player player) {
		getComponent().send(player);
		onSend(player);
	}

	public void onSend(Player player) {
	}

}
