package io.ruin.model.combat.special.melee;

import io.ruin.cache.ObjType;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.AccurateBlows;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.stat.StatType;

//Smash: Deal an attack that inflicts 50% more damage
//and lowers your target's Defence level by 30%. (50%)
public class DragonWarhammer implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("dragon warhammer");
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(1378);
		player.graphics(1292);
		player.publicSound(2541);
		if (target.player != null)
			target.player.getStats().get(StatType.Defence).drain(0.30);
		else
			target.npc.getCombat().getStat(StatType.Defence).drain(0.30);
		double boostAttack = 0.25;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		int damage = target.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostDamage(0.50).boostAttack(boostAttack));
		if (target instanceof NPC) {
			target.npc.addWeakenedDamage(player, damage);
		}
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}

}