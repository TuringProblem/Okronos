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

//Energy Drain: Deal an attack with 25% increased
//accuracy that siphons 10% of your target's run energy. (50%)
public class AbyssalWhip implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("abyssal whip");
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(1658);
		player.publicSound(2713);
		target.graphics(341, 96, 0);
		if (target.player != null)
			target.player.getMovement().drainEnergy(10);
		player.getMovement().restoreEnergy(10);
		Hit hit = new Hit(player, attackStyle, attackType).randDamage(maxDamage);
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				hit.boostAttack(c.getAccuracyBoost());
			}
		}
		target.hit(hit);
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}

}