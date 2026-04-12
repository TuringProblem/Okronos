package io.ruin.model.item.actions.impl.combine;

import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.item.actions.ItemAction;

public class NeitiznotFaceguard {


	private static final int NEITIZNOT_HELM = 10828;
	private static final int BASILISK_JAW = 24268;

	public static void register() {
		ItemAction.registerInventory(NEITIZNOT_HELM, 1, (player, item) ->
			player.dialogue(new ItemDialogue().two(NEITIZNOT_HELM, BASILISK_JAW, "You can combine this with a Basilisk jaw to create the faceguard.")));
	}
}


