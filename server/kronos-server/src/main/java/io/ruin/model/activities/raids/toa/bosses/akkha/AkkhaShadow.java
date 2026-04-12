package io.ruin.model.activities.raids.toa.bosses.akkha;

import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import lombok.Setter;

public class AkkhaShadow extends NPC {
	Bounds quadrant;
	@Setter
	boolean canAttack = true;

	public AkkhaShadow(int id, Bounds bounds, Position spawnPosition, Direction lookDirection) {
		super(id);
		this.quadrant = bounds;
		this.spawn(spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ(), lookDirection, 0);
		// System.out.println("Akkha shadow spawned at " + this.getPosition());
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend);
	}

	private void preHitDefend(Hit hit) {
		if (!canAttack) {
			hit.block();
		}
	}

	private void postDamage(Hit hit) {
	}


	public Bounds getQuadrant() {
		return quadrant;
	}
}
