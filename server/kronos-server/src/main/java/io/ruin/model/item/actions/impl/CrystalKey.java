package io.ruin.model.item.actions.impl;

import io.ruin.model.item.actions.ItemItemAction;

public class CrystalKey {

	private static final int CRYSTAL_LOOP = 987;
	private static final int CRYSTAL_TOOTH = 985;
	private static final int CRYSTAL_KEY = 989;

	public static void register() {
		ItemItemAction.register(CRYSTAL_LOOP, CRYSTAL_TOOTH, (player, primary, secondary) -> {
			int amountToMake = Math.min(secondary.getAmount(), primary.getAmount());
			player.getInventory().remove(CRYSTAL_LOOP, amountToMake);
			player.getInventory().remove(CRYSTAL_TOOTH, amountToMake);
			player.getInventory().add(CRYSTAL_KEY, amountToMake);
			player.sendMessage("You join the two halves of the key together.");
		});
	}

}
