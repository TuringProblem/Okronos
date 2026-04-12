package io.ruin.model.combat.special.ranged;

import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.AccurateBlows;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerCombat;
import io.ruin.model.item.containers.Equipment;

import static io.ruin.model.entity.player.PlayerCombat.getChebyshevDistance;

//Concentrated Shot: Deal an attack with 25% increased accuracy
//and damage, but takes an additional 2.4 seconds to fire. (65%)
public class Ballista implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return def.id == 19478 || def.id == 19481;
	}

	@Override
	public boolean handle(Player player, Entity victim, AttackStyle style, AttackType type, int maxDamage) {
		int weaponId = player.getEquipment().getId(Equipment.SLOT_WEAPON);

		player.animate(7222);
		player.getCombat().rangedData.projectiles[1].send(player, victim);
		final int distance = getChebyshevDistance(player, victim);
		final int delayTicks = PlayerCombat.getBallistaeHitDelay(distance);
		double boostAttack = .25;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (victim.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		victim.hit(new Hit(player, style, type)
			.randDamage(maxDamage)
			.boostDamage(0.25)
			.boostAttack(boostAttack)
			.delay(delayTicks)
			.capDamagePvP(weaponId == ItemID.HEAVY_BALLISTA || weaponId == ItemID.HEAVY_BALLISTA_23630 ? 76 : 57, victim)
			.postDamage(t -> t.graphics(344, 120, 0))
		);
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 65;
	}

}