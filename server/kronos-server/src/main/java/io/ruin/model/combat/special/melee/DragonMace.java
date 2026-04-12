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

//Shatter: Increase damage by 50%
//and accuracy by 25% for one hit. (25%)
public class DragonMace implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("dragon mace");
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(1060);
		player.graphics(251, 96, 0);
		player.publicSound(2541);
		double boostAttack = 0.25;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		target.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostDamage(0.50).boostAttack(boostAttack));
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 25;
	}

}