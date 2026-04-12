package io.ruin.model.activities.newcomertasks;

import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.stat.StatType;

public class PrayerScroll {
	public static void register() {
		ItemAction.registerInventory(28434, "read", (player, item) -> {
			player.dialogue(
				new ItemDialogue().one(item.getId(), "This scroll will grant you 52,000 prayer experience, are you sure you want to claim this?"),
				new OptionsDialogue("This will consume the scroll.",
					new Option("Yes, give me the prayer experience.", () -> {
						item.remove(1);
						player.closeDialogue();
						player.getStats().addXp(StatType.Prayer, 52000, false);
					}),
					new Option("Cancel", player::closeDialogue)
				)
			);
		});
	}
}
