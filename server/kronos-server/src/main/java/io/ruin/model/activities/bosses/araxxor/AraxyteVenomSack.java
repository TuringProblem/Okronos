package io.ruin.model.activities.bosses.araxxor;

import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.impl.Consumable;

public class AraxyteVenomSack {

	public static void register() {
		ItemAction.registerInventory(29784, "eat", (player, item) -> {
			if (player.isLocked() || player.isStunned())
				return;
			if (player.eatDelay.isDelayed() || player.karamDelay.isDelayed() || player.potDelay.isDelayed())
				return;
			var inv = player.getInventory();
			if (inv.remove(29784, 1) != 1) {
				return;
			}
			Consumable.animEat(player);
			player.hit(new Hit().type(HitType.VENOM).fixedDamage(4));
			player.cureVenom(300);
			player.curePoison(300);
			player.eatDelay.delay(2);
			player.getCombat().delayAttack(3);
		});
	}

}
