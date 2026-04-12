package io.ruin.model.combat.special.ranged.bolts;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.BlessedBolts;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.impl.ZaryteCrossbow;

import java.util.function.BiFunction;

public class OpalDragonBoltEffect implements BiFunction<Entity, Hit, Boolean> {

	@Override
	public Boolean apply(Entity target, Hit hit) {
		Player player = hit.attacker.player;
		double chance = target.player != null ? 0.05 : 0.1;
		double dmgboost = 0.15;

		if (ZaryteCrossbow.isWorn(player)) {
			dmgboost *= 1.1; // strengthen affect by 10%
			if (ZaryteCrossbow.forciblyProcBolt(player)) {
				chance = 1; // 100% chance to proc.
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
		target.graphics(749);
		target.hit(hit.boostDamage(dmgboost).ignoreDefence());
		return true;
	}
}
