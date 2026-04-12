package io.ruin.model.activities.gauntlet;

import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;

public class DarkBeast extends NPCCombat {

	private static final Projectile RANGE_PROJECTILE = new Projectile(1711, 30, 30, 35, 60, 1, 16, 192);

	@Override
	public void init() {

	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		int delay = (RANGE_PROJECTILE).send(npc, target);
		npc.animate(npc.getDef().animations.attack_animation);
		int maxDamage = info.max_damage;
		if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
			maxDamage = 0;
		target.player.hit(new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).clientDelay(delay));
		return true;
	}

	@Override
	public void process() {

	}
}
