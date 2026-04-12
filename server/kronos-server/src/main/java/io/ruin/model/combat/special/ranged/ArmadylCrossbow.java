package io.ruin.model.combat.special.ranged;

import io.ruin.cache.ObjType;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.AccurateBlows;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;

import static io.ruin.model.entity.player.PlayerCombat.getBowHitDelay;
import static io.ruin.model.entity.player.PlayerCombat.getChebyshevDistance;

//Armadyl Eye: Deal an attack with double accuracy. (40%)
public class ArmadylCrossbow implements Special {

	private static final Projectile PROJECTILE = new Projectile(301, 38, 36, 41, 51, 5, 5, 11);

	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("armadyl crossbow");
	}

	@Override
	public boolean handle(Player player, Entity victim, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(4230);
		PROJECTILE.send(player, victim);
		final int distance = getChebyshevDistance(player, victim);
		final int delayTicks = getBowHitDelay(distance);
		double boostAttack = 1;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (victim.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		victim.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostAttack(boostAttack).delay(delayTicks));
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 40;
	}

}