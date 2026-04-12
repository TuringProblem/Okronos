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

//Saradomin's Lightning: Call upon Saradomin's power to perform an attack
//that inflicts 10% more melee damage and 1-16 extra magic damage. (100%)
public class SaradominSword implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("saradomin sword");
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(1132, 0);
		player.graphics(1213, 0, 0);
		target.graphics(1196, 30, 0);
		double boostAttack = 0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		int damage = target.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostDamage(0.1).boostAttack(boostAttack));
		if (damage > 0)
			target.hit(new Hit(player, AttackStyle.MAGIC, AttackType.ACCURATE).fixedDamage(Random.get(1, 16)));
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 100;
	}

}