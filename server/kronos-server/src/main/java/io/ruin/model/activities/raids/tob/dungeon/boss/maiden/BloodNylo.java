package io.ruin.model.activities.raids.tob.dungeon.boss.maiden;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Position;

public class BloodNylo {

	NPC npc;
	NPC boss;
	Position spawnPosition;

	boolean dead = false;

	BloodNylo(NPC boss, Position spawnPosition) {
		this.spawnPosition = spawnPosition;
		this.npc = new NPC(8366).spawn(spawnPosition);
		this.boss = boss;
		npc.getCombat().setAllowRetaliate(false);
		npc.getCombat().setAllowRespawn(false);
		npc.getRouteFinder().routeEntity(boss);
		npc.occupyingTiles = false;
	}


	public void process(NPC npc) {
		if (dead) return;
		if (!this.npc.getPosition().isWithinDistance(npc.getPosition(), 4)) {
			int x = npc.getAbsX() - this.npc.getAbsX();
			int y = npc.getAbsY() - this.npc.getAbsY();
			this.npc.step(x, y, StepType.WALK);
		} else if (this.npc.getPosition().isWithinDistance(npc.getPosition(), 4)) {
			dead = true;
			int amountToHeal = this.npc.getHp() * 10;
			MaidenCombat combat = (MaidenCombat) boss.getCombat();
			combat.maxHit += Random.get(3, 4);
			combat.perfectMaidenFailed = true;
			npc.hit(new Hit(HitType.HEAL).fixedDamage(amountToHeal));
			this.npc.hit(new Hit().fixedDamage(npc.getHp()));
			this.npc.remove();
		}
		if (npc.freezeDelay.remaining() > 0) {
			npc.lock();
			npc.getRouteFinder().routeEntity(npc);
		}
	}
}
