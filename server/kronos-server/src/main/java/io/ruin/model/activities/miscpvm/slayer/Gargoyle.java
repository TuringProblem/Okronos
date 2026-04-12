package io.ruin.model.activities.miscpvm.slayer;

import io.ruin.cache.NpcID;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.map.route.routes.DumbRoute;

import java.util.Arrays;

public class Gargoyle extends NPCCombat {

	public static void register() {
		for (int i : Arrays.asList(412, 1543))
			ItemNPCAction.register(4162, i, (player, item, npc) -> smash(player, npc, true));
	}

	private static void smash(Player p, NPC npc, boolean manual) {
		if (npc.getCombat().getTarget() != p) {
			p.sendMessage("That gargoyle is not fighting you.");
			return;
		}
		if (manual && npc.getHp() > 9) {
			p.sendMessage("The gargoyle is not weak enough to be smashed!");
			return;
		}
		p.addEvent(event -> {
			if (!DumbRoute.withinDistance(p, npc, 1)) {
				p.getRouteFinder().routeEntity(npc);
				event.waitForMovement(p);
			}
			p.animate(1665);
			event.delay(1);
			((Gargoyle) npc.getCombat()).smashed = true;
			npc.getCombat().startDeath(null);
		});
	}

	private boolean smashed = false;

	@Override
	public void init() {
		npc.deathEndListener = (entity, killer, killHit) -> {
			smashed = false;
			npc.transform(getNormalId());
		};
		npc.hitListener = new HitListener()
			.postDamage(h -> {
				if (h != null && h.attacker != null && h.attacker.player != null
					&& VarPlayerRepository.GARGOYLE_SMASHER.get(h.attacker.player) == 1
					&& h.attacker.player.getInventory().contains(4162, 1)) { // autosmash
					if (!smashed && npc.getHp() < 8)
						smash(h.attacker.player, npc, false);
				}
			});
	}

	public int getNormalId() {
		return 412;
	}

	public int getCrumblingId() {
		return 412;
	}

	@Override
	public void startDeath(Hit killHit) {
		npc.transform(getCrumblingId());
		npc.animate(1518);
		super.startDeath(killHit);
	}

	@Override
	public void follow() {
		follow(1);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(1))
			return false;
		basicAttack();
		return true;
	}

	@Override
	public void process() {

	}
}
