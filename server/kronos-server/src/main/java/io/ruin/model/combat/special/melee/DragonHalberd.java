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
import io.ruin.utility.Misc;

//Sweep: If your target is small, adjacent targets may be hit too.
//Otherwise, your target may be hit a second time, with 25% decreased accuracy.
//Damage in all cases is increased by 10% of your max hit. (30%)
public class DragonHalberd implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("dragon halberd") || name.contains("crystal halberd");
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(1203, 0);
		player.graphics(1231, 100, 0);
		double boostAttack = 0.0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		if (target.getSize() == 1) {
			if (target.inMulti()) {
				double finalBoostAttack = boostAttack;
				target.forLocalEntity(entity -> {
					if (Misc.getDistance(entity.getPosition(), target.getPosition()) > 1)
						return;
					if (!player.getCombat().canAttack(entity, false))
						return;
					if (target.isPlayer()) {
						player.graphics(1231, 100, 16);
					} else
						entity.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostDamage(0.10).boostAttack(finalBoostAttack));
				});
			}
			target.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostDamage(0.10).boostAttack(boostAttack));
		} else {
			target.hit(
				new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostDamage(0.10).boostAttack(boostAttack),
				new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostDamage(0.10).boostDefence(0.25).boostAttack(boostAttack)
			);
		}
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 30;
	}

}