package io.ruin.model.inter.questtab.toggles;

import io.ruin.cache.Color;
import io.ruin.model.activities.wilderness.BountyHunter;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.inter.questtab.JournalTab.TabComponent;
import io.ruin.model.inter.utils.Option;

public class BountyHunterTargeting extends JournalToggle {

	@Override
	public void handle(Player player) {
		if (player.wildernessLevel > 0) {
			player.sendMessage("You can't toggle bounty hunter targeting inside the wilderness.");
			return;
		}

		player.dialogue(
			new OptionsDialogue(
				new Option("All Wilderness", () -> set(player, BountyHunter.Targeting.ALL)),
				new Option("Disable Targeting", () -> set(player, BountyHunter.Targeting.DISABLED)),
				new Option("Edgeville Only", () -> set(player, BountyHunter.Targeting.EDGEVILLE))
			)
		);
	}

	@Override
	public JournalTab.TextField getText() {
		return player -> "Targeting: " + get(player);
	}

	@Override
	public JournalTab.TabComponent getComponent() {
		return JournalTab.TabComponent.BH_TARGETING;
	}

	public String get(Player player) {
		BountyHunter.Targeting targeting = player.getBountyHunter().targeting;

		if (targeting == BountyHunter.Targeting.DISABLED) {
			return Color.RED.wrap("Disabled");
		}

		return Color.GREEN.wrap(targeting.name);
	}

	private void set(Player player, BountyHunter.Targeting targeting) {
		player.getBountyHunter().targeting = targeting;
		send(player);
	}

}