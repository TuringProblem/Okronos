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

//Saradomin's Lightning: Call upon Saradomin's power
//to perform an attack with 25% higher max hit. (65%)
public class SaradominBlessedSword implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("blessed sword");
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(1132);
		player.graphics(1213, 0, 0);
		target.graphics(1196, 30, 0);
		player.publicSound(2537);
		double boostAttack = 0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		target.hit(new Hit(player, AttackStyle.MAGICAL_MELEE, attackType).randDamage(maxDamage).boostDamage(0.25).boostAttack(boostAttack));
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 65;
	}

}