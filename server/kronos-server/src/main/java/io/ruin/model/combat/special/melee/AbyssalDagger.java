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

//Abyssal Puncture: Deal an attack that hits twice with
//25% increased accuracy, but inflicts 15% less damage per hit.
//The second hit is guaranteed if the first deals damage. (50%)
public class AbyssalDagger implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("abyssal dagger");
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(3300);
		player.graphics(1283);
		player.publicSound(2537);
		maxDamage *= 0.85;

		Hit baseHit = new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostAttack(0.25).capDamagePvP(51, target);
		Hit secondaryHit = new Hit(player, attackStyle, attackType).randDamage(maxDamage).capDamagePvP(51, target);
		baseHit.postDefend(t -> {
			if (baseHit.damage == 0)
				secondaryHit.block();
			else
				secondaryHit.ignoreDefence(); //second hit is garunateed
		});
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				baseHit.boostAttack(c.getAccuracyBoost());
				secondaryHit.boostAttack(c.getAccuracyBoost());
			}
		}
		target.hit(baseHit, secondaryHit);
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}

}