package io.ruin.model.activities.bosses.dagannothkings;

import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.map.Projectile;
import io.ruin.utility.Misc;
import lombok.Getter;

public class DagannothPrime extends NPCCombat {

	private static final Projectile PROJECTILE = new Projectile(162, 65, 31, 15, 56, 10, 15, 64);
	@Getter String lastPlayerAttacked = "";
	@Getter int lastAttacked = 0;
	@Override
	public void init() {
	}

	@Override
	public void follow() {
		follow(7);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(6))
			return false;
		npc.animate(info.attack_animation);
		int delay = PROJECTILE.send(npc, target);
		if(target.player != null) {
			lastPlayerAttacked = target.player.getName();
			lastAttacked = 0;
		}
		target.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(info.max_damage).clientDelay(delay));
		target.graphics(477, 100, delay);
		final int ticks = (delay * 25) / 600;
		npc.addEvent(event -> {
			event.delay(ticks);
			if (target != null)
				target.graphics(477, 100, 0);
		});
		return true;
	}

	@Override
	public void process() {
		lastAttacked++;
	}


	@Override
	public int getAggressionRange() {
		return 6;
	}

	@Override
	public int getAttackBoundsRange() {
		return 6;
	}
}
