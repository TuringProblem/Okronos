package io.ruin.model.combat.special.ranged;

import io.ruin.cache.ObjType;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Projectile;

import static io.ruin.model.entity.player.PlayerCombat.getChebyshevDistance;
import static io.ruin.model.entity.player.PlayerCombat.getThrownHitDelay;

public class DragonThrownaxe implements Special {

	private static final Projectile PROJECTILE = Projectile.thrown(1318, 11);

	@Override
	public boolean accept(ObjType def, String name) {
		return def.id == 20849;
	}

	@Override
	public boolean handle(Player player, Entity victim, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		PROJECTILE.send(player, victim);
		player.animate(7521);
		player.graphics(1317, 120, 0);
		final int distance = getChebyshevDistance(player, victim);
		final int delayTicks = getThrownHitDelay(distance);
		Hit hit = new Hit(player, attackStyle, attackType).randDamage(maxDamage).delay(delayTicks).boostAttack(0.25);
		victim.hit(hit);
		player.getCombat().removeAmmo(player.getEquipment().get(Equipment.SLOT_WEAPON), hit);
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 25;
	}
}
