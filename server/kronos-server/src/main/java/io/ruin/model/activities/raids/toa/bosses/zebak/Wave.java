package io.ruin.model.activities.raids.toa.bosses.zebak;

import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;

public class Wave extends NPC {
	Position targetPosition;
	Bounds bossBounds;

	NPC boss;

	public Wave(int id, Position pos, NPC boss) {
		super(id);
		this.boss = boss;
		this.spawn(pos.getX(), pos.getY(), 0, Direction.NORTH, 0);
		targetPosition = new Position(this.getPosition().getX(), this.getPosition().getY() + 18, this.getPosition().getZ());
		npc.face(targetPosition.getX(), targetPosition.getY());
		npc.stepAbs(targetPosition.getX(), targetPosition.getY(), StepType.WALK);
		var region = boss.getPosition().getRegion();
		bossBounds = new Bounds(region.baseX + 23, region.baseY + 24, region.baseX + 37, region.baseY + 41, 0);
	}

	public void update(NPC boss) {
		if (boss.getHp() < 1) {
			this.remove();
			return;
		}
		if (this.getPosition().distance(targetPosition) < 2) {
			this.remove();
		}
		this.getPosition().getRegion().players.forEach(p -> {
			// if the player is in the way of the wave
			if (p.getPosition().isAtPosition(getPosition())) {
				if (p.getHp() <= 0) return;
				// wave hits the player
				p.animate(424);
				// if we can move them, move them
				if (p.getPosition().inBounds(bossBounds)) {
					p.hit(new Hit().randDamage(getWaveDamage(p)));
					p.graphics(163);
					p.getMovement().force(0, 3, 0, 0, 0, 60, Direction.NORTH);
					p.sendMessage("The wave crashes into you, dealing massive damage!");
				}
			}
		});
	}

	private int getWaveDamage(Player player) {
		var damage = 15;
		if (player.getCurrentToARaid().getZebakPathLevel() >= 2)
			damage = 20;
		else if (player.getCurrentToARaid().getZebakPathLevel() >= 4)
			damage = 25;
		return damage;
	}
}
