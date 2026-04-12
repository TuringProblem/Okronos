package io.ruin.model.combat.special.magic;

import io.ruin.Server;
import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;

import static io.ruin.model.entity.player.PlayerCombat.getChebyshevDistance;
import static io.ruin.model.entity.player.PlayerCombat.getMagicHitDelay;

public class AccursedSceptre implements Special {
	private static final Projectile PROJECTILE = new Projectile(2134, 38, 0, 41, 51, 5, 5, 11);

	@Override
	public boolean accept(ObjType def, String name) {
		return def.id == 8876 || def.id == 8878;
	}

	private int getTicks(int delay) {
		return Math.max(1, (delay * 16) / Server.tickMs());
	}

	@Override
	public boolean handle(Player player, Entity victim, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.getCombat().updateLastAttack(5);
		player.animate(1168);
		player.graphics(2134, 220, 0);
		World.startEvent(e -> {
			final int distance = getChebyshevDistance(player, victim);
			final int delayTicks = getMagicHitDelay(distance);
			PROJECTILE.send(player, victim);
			e.delay(1);
			player.graphics(-1);

			int damage = victim.hit(new Hit(player, attackStyle, attackType).randDamage((int) (victim instanceof NPC ? maxDamage * 1.7 : maxDamage * 1.3)).delay(delayTicks));
			e.delay(delayTicks - 1);
			victim.graphics(2138, 0, 0);
			if (player.getHp() > 0)
				player.hit(new Hit(HitType.HEAL).fixedDamage(damage / 2));
			player.getCombat().updateLastAttack(4);
		});
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}
}
