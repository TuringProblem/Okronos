package io.ruin.model.item.actions.impl.scrolls.boosts;

import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;

public class RevivalScroll {
	private static final int REVIVAL_SCROLL = 30463;

	public static void register() {
		ItemAction.registerInventory(REVIVAL_SCROLL, "read", (player, item) -> {
			player.dialogue(new OptionsDialogue("Are you sure you want to activate your scroll?",
				new Option("Yes, activate my revival scroll!", () -> {
					item.remove(1);
					player.reviveActive = true;
					player.sendMessage("You now have death protection outside the wilderness!");
					player.sendMessage("<img=10> Note: <col=ff0000>This does not save your hardcore status.");
				}),
				new Option("No, I'm not ready yet!")));
		});
	}
}
