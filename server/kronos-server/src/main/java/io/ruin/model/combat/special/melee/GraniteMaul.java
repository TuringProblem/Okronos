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

//Quick Smash: Deal an extra attack instantly. (50%)
public class GraniteMaul implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("granite maul");
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(1667);
		player.graphics(340, 96, 0);
		player.publicSound(2715);
		double boostAttack = 0.0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		target.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostAttack(boostAttack));
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}

	public static void register() {
		for (int id : new int[] { 4153, 12848, 20557, 24225 })
			ObjType.get(id).graniteMaul = true;
	}

}
