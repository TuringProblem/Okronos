package io.ruin.model.activities.bosses;

import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;

public class CatalystBrute extends NPCCombat {
	@Override
	public void init() {
		npc.hitListener = new HitListener().preDefend(this::preDefend);
		npc.isMovementBlocked(false, true);
	}

	@Override
	public void follow() {
		follow(2);
	}

	@Override
	public boolean attack() {
		basicAttack();
		return true;
	}

	@Override
	public void process() {

	}


	private void preDefend(Hit hit) {
		if (hit.attackStyle == null)
			return;
		if (hit.attackStyle.isMelee()) {
			hit.attacker.player.sendMessage("Only magic attacks can deal damage to this catalyst.");
			hit.block();
		} else if (hit.attackStyle.isRanged()) {
			hit.attacker.player.sendMessage("Only magic attacks can deal damage to this catalyst.");
			hit.block();
		}
	}
}
