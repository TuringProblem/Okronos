package io.ruin.model.activities.raids.toa.bosses.kephri;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.raids.toa.Invocations;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.skills.prayer.Prayer;


public class ScarabMelee extends NPCCombat {

	public boolean damagedPlayer = false;

	@Override
	public void init() {

	}

	@Override
	public void follow() {
		follow(1);
	}

	@Override
	public boolean attack() {
		if (withinDistance(1)) {
			MeleeAttack();
			return true;
		} else {
			npc.getRouteFinder().routeEntity(target);
		}
		return false;
	}

	public void MeleeAttack() {
		npc.animate(9587);
		int maxDamage = 33;
		if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE)) {
			maxDamage = 8;
			if (target.player != null && target.player.getCurrentToARaid() != null && target.player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
				maxDamage = 11;
		} else damagedPlayer = true;
		target.hit(new Hit(npc, AttackStyle.SLASH).randDamage(maxDamage).ignorePrayer());
	}


	@Override
	public void process() {
		if (target == null) {
			if (!npc.getPosition().getRegion().players.isEmpty()) {
				target = Random.get(npc.getPosition().getRegion().players);
			}
		}
	}
}
