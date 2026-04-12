package io.ruin.model.inter.questtab.toggles;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.inter.questtab.JournalTab.TabComponent;
import io.ruin.model.inter.utils.Option;


public class DragSetting extends JournalToggle {

	@Override
	public void handle(Player player) {
		player.dialogue(
			new OptionsDialogue(
				new Option("5 (OSRS)", () -> set(player, 5)),
				new Option("10 (Pre-EoC)", () -> set(player, 10)),
				new Option("Custom", () -> {
					player.integerInput("Enter desired drag setting: (0-50)", drag -> {
						if (drag < 0 || drag > 50) {
							player.retryIntegerInput("Invalid drag setting, please try again: (0-50)");
							return;
						}
						set(player, drag);
					});
				})
			)
		);
	}

	@Override
	public JournalTab.TextField getText() {
		return player -> "Drag Setting: " + getDragSetting(player);
	}

	@Override
	public JournalTab.TabComponent getComponent() {
		return JournalTab.TabComponent.DRAG_SETTING;
	}

	@Override
	public void onSend(Player player) {
		player.getPacketSender().sendVarp(20000, player.dragSetting);
	}

	public void set(Player player, int drag) {
		player.dragSetting = drag;
		send(player);
	}

	private String getDragSetting(Player player) {
		if (player.dragSetting == 5)
			return Color.GREEN.wrap("5 (OSRS)");
		else if (player.dragSetting == 10)
			return Color.GREEN.wrap("10 (Pre-EOC)");
		else
			return Color.GREEN.wrap(player.dragSetting + " (Custom)");
	}

}