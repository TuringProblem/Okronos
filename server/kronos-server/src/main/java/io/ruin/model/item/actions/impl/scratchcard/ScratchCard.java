package io.ruin.model.item.actions.impl.scratchcard;

import io.ruin.model.item.actions.ItemAction;

public class ScratchCard {
	private static final int SCRATCH_CARD_ID = 10600;

	public static void register() {
		ItemAction.registerInventory(SCRATCH_CARD_ID, "scratch", (player, item) -> {
			if (player.getScratchCard() == null || !player.getScratchCard().isRunning()) {
				player.scratchCardManager = new ScratchCardManager();
				if (!player.getInventory().contains(SCRATCH_CARD_ID, 1)) return;
				player.getInventory().remove(SCRATCH_CARD_ID, 1);
				player.getScratchCard().open(player);
			}
		});
	}
}
