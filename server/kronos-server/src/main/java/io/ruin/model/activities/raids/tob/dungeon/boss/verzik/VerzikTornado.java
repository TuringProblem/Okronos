package io.ruin.model.activities.raids.tob.dungeon.boss.verzik;

import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Bounds;

public class VerzikTornado {
	private final int TORNADO_NPC = 8386;
	Player target;
	public NPC tornado;
	NPC verzik;

	public VerzikTornado(Player target, Bounds room, NPC verzik) {
		this.target = target;
		tornado = new NPC(TORNADO_NPC).spawn(room.randomPosition());
		this.verzik = verzik;
	}


	public void process() {
		if (target.dead() || target == null)
			tornado.remove();
		if (target != null) {
			tornado.stepAbs(target.getPosition().getX(), target.getPosition().getY(), StepType.WALK);
			if (tornado.getPosition().isWithinDistance(target.getPosition(), 0)) {
				int damage = Math.max(1, target.getHp() / 2) + 1;
				target.hit(new Hit(tornado).fixedDamage(damage));
				verzik.hit(new Hit(HitType.HEAL).fixedDamage(damage));
				VerzikCombat verzikCombat = (VerzikCombat) verzik.getCombat();
				verzikCombat.damagedPlayer = true;
			}
		}
	}
}
