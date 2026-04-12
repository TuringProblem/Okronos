package io.ruin.model.item.actions.impl.chargable;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;

public class CelestialRing {

	private static final int UNCHARGED_RING = 25539;
	private static final int RING = 25541;

	public static void register() {
		ItemAction.registerInventory(UNCHARGED_RING, "charge", CelestialRing::charge);
	}

	private static void charge(Player player, Item ring) {
		player.getInventory().remove(UNCHARGED_RING, 1);
		player.getInventory().add(RING, 1);
		player.sendMessage("You have successfully charged the celestial ring.");
	}
}
