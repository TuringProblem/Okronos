package io.ruin.model.combat.special.ranged.bolts;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.BlessedBolts;
import io.ruin.model.activities.perktree.perks.TheSoulsplit;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.impl.ZaryteCrossbow;
import io.ruin.model.item.containers.Equipment;

import java.util.function.BiFunction;

public class OnyxBoltEffect implements BiFunction<Entity, Hit, Boolean> {

	@Override
	public Boolean apply(Entity target, Hit hit) {
		Player player = hit.attacker.player;
		double chance = target.player != null ? 0.1 : 0.11;
		double dmgboost = 0.20;
		if (ZaryteCrossbow.isWorn(player)) {
			dmgboost *= 1.1; // strengthen affect by 10%
			if (ZaryteCrossbow.forciblyProcBolt(player)) {
				chance = 1;
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null
			&& player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 30515) {
			dmgboost *= 1.1;
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.BLESSED_BOLTS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.BLESSED_BOLTS);
			BlessedBolts c = (BlessedBolts) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			chance += c.getBoltSpecialChance();

		}
		if (!Random.rollPercent(chance) && chance < 1)
			return false;
		int damage = target.hit(hit.boostDamage(dmgboost));
		float heal = (float) (damage * 0.25);
		if (heal > 0) {
			target.graphics(753);
			player.hit(new Hit(HitType.HEAL).fixedDamage((int) heal));
		}
		return true;
	}

}