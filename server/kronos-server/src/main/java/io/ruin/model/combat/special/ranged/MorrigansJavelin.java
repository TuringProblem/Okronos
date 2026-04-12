package io.ruin.model.combat.special.ranged;

import io.ruin.cache.ObjType;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;

import static io.ruin.model.entity.player.PlayerCombat.getChebyshevDistance;
import static io.ruin.model.entity.player.PlayerCombat.getThrownHitDelay;

public class MorrigansJavelin implements Special {

	private static final Projectile PROJECTILE = Projectile.thrown(1622, 11);

	@Override
	public boolean accept(ObjType def, String name) {
		return name.equalsIgnoreCase("morrigan's javelin");
	}

	@Override
	public boolean handle(Player player, Entity victim, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(806);
		player.graphics(1621, 96, 0);
		PROJECTILE.send(player, victim);
		final int distance = getChebyshevDistance(player, victim);
		final int delayTicks = getThrownHitDelay(distance);
		int damage = victim.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage).delay(delayTicks).capDamagePvP(61, victim));
		if (damage > 0) {
			victim.addEvent(event -> {
				int bleed = damage;
				event.delay(3);
				while (bleed > 0 && !victim.getCombat().isDead()) {
					int damageToDeal = Math.min(5, bleed);
					bleed -= damageToDeal;
					victim.hit(new Hit().fixedDamage(damageToDeal).ignorePrayer().ignoreDefence().capDamagePvP(61, victim));
					event.delay(3);
				}
			});
		}
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}
}
