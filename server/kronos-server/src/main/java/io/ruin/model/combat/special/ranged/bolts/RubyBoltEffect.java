package io.ruin.model.combat.special.ranged.bolts;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.BlessedBolts;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.impl.ZaryteCrossbow;
import io.ruin.model.item.containers.Equipment;

import java.util.function.BiFunction;

public class RubyBoltEffect implements BiFunction<Entity, Hit, Boolean> {

	@Override
	public Boolean apply(Entity target, Hit hit) {
		Player player = hit.attacker.player;
		double chance = target.player != null ? 0.11 : 0.06;
		double dmgboost = 0.2;
		boolean blessedBoltsActive = false;
		int damagecap = 100;
		if (ZaryteCrossbow.isWorn(player)) {
			dmgboost *= 1.1; // strengthen affect by 10%
			damagecap *= 1.1; // increase damage cap by 10%
			if (ZaryteCrossbow.forciblyProcBolt(player)) {
				chance = 1;
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null
			&& player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 30515) {
			dmgboost *= 1.5; // strengthen affect by 10%
			damagecap *= 1.5; // increase damage cap by 10%
			float hpPercent = (float) player.getHp() / player.getMaxHp();
			if (hpPercent <= 0.1) {
				return false;
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
		Entity attacker = hit.attacker;
		int sacrificeDamage = (int) (attacker.getHp() * 0.10);
		int specialDamage;
		// Quick little fix to prevent abuse for HP events (where admins set their hitpoints to 10k and players hit 1,500 dmg)
		if (target.player != null)
			specialDamage = (int) ((target.getHp() > 99 ? 99 : target.getHp()) * dmgboost);
		else
			specialDamage = (int) ((target.getHp()) * dmgboost);
		if (sacrificeDamage <= 0 || specialDamage <= 0)
			return false;
		if (specialDamage > damagecap)
			specialDamage = damagecap;
		attacker.hit(new Hit().fixedDamage(sacrificeDamage));
		target.hit(hit.fixedDamage(specialDamage).ignoreDefence().ignorePrayer());
		target.graphics(754);
		return true;
	}

}