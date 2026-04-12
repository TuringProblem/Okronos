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

public class OsmumtensFang implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("osmumten's fang");
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(9544);
		player.graphics(2124);
		double boostAttack = 0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		Hit baseHit = new Hit(player, attackStyle, attackType).randDamage(3, maxDamage);
		baseHit.preDefend(t -> {
			baseHit.ignoreDefence(); //hit is guaranteed
		});
		target.hit(new Hit(player, attackStyle, attackType)
			.randDamage(maxDamage)
			.boostDamage(0.175).boostAttack(boostAttack));
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 25;
	}

}