package io.ruin.model.combat.special.ranged.bolts;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.BlessedBolts;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.impl.ZaryteCrossbow;

import java.util.function.BiFunction;

public class EmeraldBoltEffect implements BiFunction<Entity, Hit, Boolean> {

	@Override
	public Boolean apply(Entity target, Hit hit) {
		Player player = hit.attacker.player;
		double chance = target.player != null ? 0.54 : 0.55;
		int poisondmg = 5;

		if (ZaryteCrossbow.isWorn(player)) {
			poisondmg = 6; // not a big deal to boost it +1 which is 20% because it's 10% rounded up in this case.
			if (ZaryteCrossbow.forciblyProcBolt(player)) {
				chance = 1;
			}
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.BLESSED_BOLTS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.BLESSED_BOLTS);
			BlessedBolts c = (BlessedBolts) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			chance += c.getBoltSpecialChance();

		}

		if (!Random.rollPercent(chance) && chance < 1)
			return false;
		target.poison(poisondmg);
		target.graphics(752);
		target.hit(hit);
		return true;
	}

}