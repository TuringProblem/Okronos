package io.ruin.model.activities.raids.toa.bosses.baba;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;

public class Boulder extends NPC {
	Position targetPosition;
	Bounds bossBounds;
	NPC baba;

	public Boulder(int id, Position pos, NPC baba) {
		super(id);
		this.spawn(pos.getX(), pos.getY(), 0, Direction.WEST, 0);
		npc.getCombat().setAllowRetaliate(false);
		this.baba = baba;
		targetPosition = new Position(this.getPosition().getX() - 19, this.getPosition().getY(), this.getPosition().getZ());
		bossBounds = new Bounds(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 25, npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 38, npc.getPosition().getZ());
		npc.stepAbs(targetPosition.getX(), targetPosition.getY(), StepType.WALK);
		if (npc.getId() == 11783) {
			npc.hitListener = new HitListener().preDefend(hit -> hit.damage = npc.getHp());
		} else {
			npc.hitListener = new HitListener().postDamage(hit -> {
				if (npc.getHp() <= 0) {
					Baba b = (Baba) this.baba.getCombat();
					b.attackedNonWeakBoulder = true;
				}
			});
		}
		World.startEvent(event -> {
			while (npc.getHp() > 0) {
				if (npc.getPosition().distance(targetPosition) < 2) {
					this.remove();
					break;
				}
				event.delay(1);
				update();
			}
		});
	}

	private void update() {
		if (npc.getHp() < 0) {
			Baba baba = (Baba) this.baba.getCombat();
			baba.bouldersKilled++;
			return;
		}
		this.getPosition().getRegion().players.forEach(p -> {
			if (p.getPosition().distance(this.getPosition()) < 2 && p.getPosition().getX() <= this.getPosition().getX() && this.getHp() > 0) {
				if (p.isBoulderDamageImmune())
					return;
				p.hit(new Hit().fixedDamage(25));
				Baba baba = (Baba) this.baba.getCombat();
				baba.damagedPlayer = true;
				p.setBoulderDamageImmune(true);
				Position newPos = new Position(p.getPosition().getX() - 4, p.getPosition().getY(), p.getPosition().getZ());
				if (newPos.getX() < bossBounds.swX)
					newPos = new Position(bossBounds.swX, newPos.getY(), newPos.getZ());

				p.animate(9799);
				p.getMovement().teleport(newPos);
				World.startEvent(event -> {
					event.delay(2);
					p.setBoulderDamageImmune(false);
				});
			}
		});
	}
}