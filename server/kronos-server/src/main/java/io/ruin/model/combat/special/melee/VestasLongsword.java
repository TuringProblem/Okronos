package io.ruin.model.combat.special.melee;

import io.ruin.api.utils.Random;
import io.ruin.cache.ObjType;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.AccurateBlows;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.CombatUtils;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;

public class VestasLongsword implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return name.equalsIgnoreCase("vesta's longsword") || name.equalsIgnoreCase("vesta's longsword (deg)");
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(7515);
		double boostAttack = 0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		Hit hit = new Hit(player, attackStyle, attackType).boostAttack(boostAttack);
		int sides = (int) CombatUtils.getDefenceBonus(target, hit.attackStyle);
		int chance = (int) (sides * 0.75);
		if (Random.rollDie(sides, chance)) {
			hit.defenceIgnored = true;
			hit.randDamage((int) (maxDamage * 0.20), (int) (maxDamage * 1.20));
		} else {
			hit.block();
		}

		target.hit(hit);
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 25;
	}
}