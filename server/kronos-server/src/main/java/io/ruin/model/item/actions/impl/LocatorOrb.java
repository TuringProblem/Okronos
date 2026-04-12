package io.ruin.model.item.actions.impl;

import io.ruin.model.combat.Hit;
import io.ruin.model.item.actions.ItemAction;

public class LocatorOrb {
	public static void register() {
		ItemAction.registerInventory(22081, "feel", (player, item) -> {
			if (player.getHp() <= 1 && !player.locatorOrbDelay.isDelayed()) {
				return;
			}
			if (player.getHp() > 2 && !player.locatorOrbDelay.isDelayed()) {
				player.locatorOrbDelay.delay(1);
				player.privateSound(1018);
				player.hit(new Hit().fixedDamage(player.getHp() > 10 ? 10 : player.getHp() - 1));
			}
		});
	}
}
