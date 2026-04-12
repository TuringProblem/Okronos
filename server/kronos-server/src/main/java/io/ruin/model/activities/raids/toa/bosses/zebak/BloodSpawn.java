package io.ruin.model.activities.raids.toa.bosses.zebak;

import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;

import java.util.Comparator;

public class BloodSpawn extends NPC {
	int ticksAlive = 0;
	Player target;

	public BloodSpawn(int id) {
		super(id);
	}

	public void update(NPC boss) {
		if (boss.getHp() < 1 || ticksAlive++ > 40 || this.getHp() < 1) {
			this.remove();
			return;
		}
		if (target == null || target.getHp() < 1)
			target = getNearestPlayer();
		if (target != null) {
			this.stepAbs(target.getPosition().getX(), target.getPosition().getY(), StepType.WALK);
			this.hit(new Hit().fixedDamage(2));
			for (Player player : this.localPlayers()) {
				if (player.getPosition().distance(this.getPosition()) < 1) {
					if (npc.getHp() > 0)
						player.hit(new Hit().randDamage(18));
				}
			}
		}
	}

	private Player getNearestPlayer() {
		Player nearest = null;
		for (Player player : this.localPlayers()) {
			if (player.getHp() < 1) continue;
			if (nearest == null || player.getPosition().distance(this.getPosition()) < nearest.getPosition().distance(this.getPosition())) {
				nearest = player;
			}
		}
		return nearest;
	}
}
