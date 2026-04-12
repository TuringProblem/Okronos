package io.ruin.model.activities.raids.toa.bosses.kephri;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.raids.toa.Invocations;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;

public class AgileScarabCombat extends NPCCombat {
	Projectile rangeAttack = new Projectile(2152, 30, 31, 0, 100, 6, 16, 64);

	@Override
	public void init() {

	}

	@Override
	public void follow() {
		follow(8);
	}

	@Override
	public boolean isAggressive() {
		return true;
	}

	@Override
	public int getAggressionRange() {
		return 40;
	}

	@Override
	public int getAttackBoundsRange() {
		return 40;
	}

	@Override
	public boolean attack() {
		if (npc.getId() != 11727) return false;
		npc.face(target);
		int delay = rangeAttack.send(npc, target);
		int maxDamage = 11;
		if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
			maxDamage = 4;
			if (target.player != null && target.player.getCurrentToARaid() != null && target.player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
				maxDamage = 6;
		}
		target.hit(new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).clientDelay(delay));
		return true;
	}

	@Override
	public void process() {
		if (npc.getId() != 11727) return;
		if (target == null) {
			if (!npc.getPosition().getRegion().players.isEmpty()) {
				target = Random.get(npc.getPosition().getRegion().players);
			} else if (!npc.localPlayers().isEmpty()) {
				target = Random.get(npc.localPlayers());
			}
		} else {
			npc.face(target);
		}
	}
}
