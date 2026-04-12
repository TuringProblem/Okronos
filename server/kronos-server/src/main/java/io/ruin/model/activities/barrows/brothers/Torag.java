package io.ruin.model.activities.barrows.brothers;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.stat.StatType;

public class Torag extends NPCCombat {


	public boolean attackWithNonMagic = false;

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(this::postDamage);

	}

	private void postDamage(Hit hit) {
		if (hit.attackStyle != AttackStyle.MAGIC)
			attackWithNonMagic = true;
	}


	@Override
	public void follow() {
		follow(1);
	}


	@Override
	public boolean attack() {
		if (!withinDistance(1))
			return false;
		Player player = target.player;
		Hit hit = basicAttack();
		hit.postDamage(t -> {

			if (hit.damage > 0)
				player.killedAllBarrowsWithoutDamaged = false;
		});
		player.killedAllMeleeBarrowsWithoutBeingHit = false;
		if (Random.rollDie(4)) {
			target.graphics(399, 0, 0);
			target.player.getMovement().drainEnergy(20);
		}

		return true;
	}

	@Override
	public void process() {
		if (target != null && target.player.getStats().get(StatType.Prayer).currentLevel > 0)
			target.player.barrowsRunWithZeroPrayer = false;
	}

}