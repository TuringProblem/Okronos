package io.ruin.model.combat.special.melee;

import io.ruin.api.utils.Random;
import io.ruin.cache.ObjType;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.AccurateBlows;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;

public class BurningClaws implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("burning claws");
	}

	@Override
	public boolean handle(Player player, Entity victim, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(7514, 10);
		player.graphics(1171, 0, 10);
		player.publicSound(2537);
		double boostAttack = 0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (victim.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		Hit hit1 = new Hit(player, attackStyle, attackType).boostAttack(boostAttack).capDamagePvP(44, victim),
			hit2 = new Hit(player, attackStyle, attackType).boostAttack(boostAttack).capDamagePvP(22, victim),
			hit3 = new Hit(player, attackStyle, attackType).boostAttack(boostAttack).delay(1).capDamagePvP(11, victim);
		int minDamage; //Never lower than 4 because anything less causes unwanted 0s.
		if (Random.rollDie(6))
			minDamage = 4;
		else
			minDamage = Math.max(4, maxDamage / 2);
		int firstDamage = victim.hit(hit1.randDamage(minDamage, maxDamage));
		if (firstDamage > 0) {
			int secondDamage = victim.hit(hit2.fixedDamage(firstDamage / 2).ignoreDefence());
			int thirdDamage = secondDamage / 2;
			int fourthDamage = thirdDamage + (secondDamage % 2);
			victim.hit(hit3.fixedDamage(thirdDamage).ignoreDefence());
		} else {
			int secondDamage = victim.hit(hit2.randDamage(minDamage, maxDamage));
			if (secondDamage > 0) {
				int damage = secondDamage / 2;
				victim.hit(hit3.fixedDamage(damage).ignoreDefence());
			} else {
				int thirdDamage = victim.hit(hit3.randDamage((int) (maxDamage * 1.5)));
			}
		}
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 30;
	}
}
