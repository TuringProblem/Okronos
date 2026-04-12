package io.ruin.model.combat.special.melee;

import io.ruin.cache.ObjType;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.AccurateBlows;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.stat.StatType;

public class SaradominGodswordOr implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return name.equalsIgnoreCase("saradomin godsword (or)");
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(player.getEquipment().getId(Equipment.SLOT_WEAPON) == 20372 ? 7640 : 7641);
		if (player.goldenSaradominGodsword == true) {
			player.graphics(1745);
		} else {
			player.graphics(1209);
		}
		player.publicSound(3869);
		double boostAttack = 1;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		int damage = target.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostDamage(0.10).boostAttack(boostAttack));
		if (damage > 0) {
			player.incrementHp((int) Math.ceil(damage));
			player.getStats().get(StatType.Prayer).restore((int) Math.ceil(damage * 0.50), 0);
		}
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}

}
