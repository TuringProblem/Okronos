package io.ruin.model.activities.raids.toa.bosses.kephri;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.raids.toa.Invocations;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;

public class ScarabRanger extends NPCCombat {

	Projectile projectile = new Projectile(2152, 30, 31, 0, 100, 0, 16, 64);

	public boolean damagedPlayer = false;

	@Override
	public void init() {

	}

	@Override
	public void follow() {
		follow(8);
	}

	@Override
	public boolean attack() {
		if (withinDistance(8)) {
			rangeAttack();
			return true;
		}
		return true;
	}

	@Override
	public void process() {
		if (target == null) {
			if (!npc.getPosition().getRegion().players.isEmpty()) {
				target = Random.get(npc.getPosition().getRegion().players);
			}
		}
	}

	private void rangeAttack() {
		npc.face(target);
		npc.animate(9588);
		projectile.send(npc, target);
		int maxDamage = 33;
		if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
			maxDamage = 8;
			if (target.player != null && target.player.getCurrentToARaid() != null && target.player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
				maxDamage = 11;
		} else damagedPlayer = true;
		target.hit(new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).ignorePrayer());
		if (Random.get(5) == 0)
			target.poison(6);
	}
}
