package io.ruin.model.activities.raids.tob.dungeon.boss.vasilias;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Position;
import io.ruin.utility.Misc;

import java.util.ArrayList;
import java.util.List;

public class NylocasHagiosCombat extends Nylo {
	boolean beenToMiddle = false;
	Position middleGround;


	NPC npc;
	NPC boss;


	int timeSinceMoved = 0;

	List<NPC> possibleTargets = new ArrayList<>();


	public NylocasHagiosCombat(int id, Position spawn, List<NPC> possibleTargets, NPC boss) {
		npc = new NPC(id).spawn(spawn);
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend);
		middleGround = new Position(npc.getPosition().getRegion().baseX + 30,
			npc.getPosition().getRegion().baseY + 25, npc.getHeight());
		npc.getCombat().setAllowRetaliate(true);
		this.possibleTargets = possibleTargets;
		this.boss = boss;
	}


	private void preHitDefend(Hit hit) {
		VasiliasCombat combat = (VasiliasCombat) boss.getCombat();
		if (hit.attackStyle != null && !hit.attackStyle.isMagic()) {
			combat.perfectNylocasFailed = true;
			hit.block();
		}
	}


	private void postDamage(Hit hit) {
	}

	@Override
	public void process() {
		if (!npc.getMovement().hasMoved() && npc.getCombat().getTarget() == null) {
			timeSinceMoved++;
			if (middleGround != null) {
				if (timeSinceMoved >= 10)
					npc.getMovement().teleport(middleGround);
			} else
				middleGround = new Position(npc.getPosition().getRegion().baseX + 30,
					npc.getPosition().getRegion().baseY + 25, npc.getHeight());
		}
		if (npc.getPosition().distance(middleGround) < 2) {
			beenToMiddle = true;
		} else
			npc.getRouteFinder().routeAbsolute(middleGround);
		if (beenToMiddle && npc.getCombat().getTarget() == null) {
			if (possibleTargets != null) {
				if (possibleTargets.size() > 0) {
					NPC target = possibleTargets.get(Random.get(0, possibleTargets.size() - 1));
					npc.face(target);
					npc.getCombat().setTarget(target);
				} else {
					if (npc.getPosition().getRegion().players.size() > 0) {
						Player target = Random.get(npc.getPosition().getRegion().players);
						npc.face(target);
						npc.getCombat().setTarget(target);
					}
				}
			}
		}
	}

	@Override
	public NPCCombat getCombat() {
		return npc.getCombat();
	}

	@Override
	public NPC getNPC() {
		return npc;
	}

	@Override
	public List<NPC> getTargets() {
		return possibleTargets;
	}

	@Override
	public boolean beenToMiddle() {
		return beenToMiddle;
	}

	@Override
	public void setBeenToMiddle(boolean beenToMiddle) {
		this.beenToMiddle = beenToMiddle;
	}
}
