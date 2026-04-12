package io.ruin.model.inter.questtab.toggles;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.inter.questtab.JournalTab.TabComponent;
import io.ruin.model.var.VarPlayerRepository;

public class KDOverlay extends JournalToggle {

	@Override
	public void handle(Player player) {
		VarPlayerRepository.PVP_KD_OVERLAY.toggle(player);
		send(player);
	}

	@Override
	public JournalTab.TextField getText() {
		return player -> "Wilderness KD Overlay: " + get(player);
	}

	@Override
	public JournalTab.TabComponent getComponent() {
		return JournalTab.TabComponent.KD_OVERLAY;
	}

	private String get(Player player) {
		boolean enabled = VarPlayerRepository.PVP_KD_OVERLAY.get(player) == 1;

		return enabled ? Color.GREEN.wrap("On") : Color.RED.wrap("Off");
	}

}