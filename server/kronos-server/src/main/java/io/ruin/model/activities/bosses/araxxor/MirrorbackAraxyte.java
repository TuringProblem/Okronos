package io.ruin.model.activities.bosses.araxxor;

import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;

public class MirrorbackAraxyte extends NPCCombat {
	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(this::postDamage);
	}

	private void postDamage(Hit hit) {
		int damageToReflect = hit.damage / 2;
		if (hit.attacker != null && hit.attacker.isPlayer() && hit.attacker.player.getPosition().distance(npc.getPosition()) < 7) {
			hit.attacker.player.hit(new Hit().fixedDamage(damageToReflect));
		}
	}

	@Override
	public void follow() {
		follow(1);
	}

	@Override
	public boolean attack() {
		if (withinDistance(1)) {
			npc.face(target);
			basicAttack();
		}
		return true;
	}

	@Override
	public void process() {

	}
}
