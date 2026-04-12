package io.ruin.model.activities.bosses.bryophyta;

import io.ruin.cache.ItemID;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.map.route.routes.DumbRoute;
import io.ruin.model.skills.woodcutting.Hatchet;

import java.util.Arrays;

public class Growthlin extends NPCCombat {

	private static final int[] ITEMS = {ItemID.MAGIC_SECATEURS};

	private boolean axed = false;

	public static void register() { // TODO our hatchet is different from ours, fix this and boss works
		Arrays.stream(Hatchet.values()).forEach(i -> {
			ItemNPCAction.register(i.id, Bryophyta.GROWTHLIN, (player, item, npc) -> axed(player, npc, true));
		});
	}

	private static void axed(Player p, NPC npc, boolean manual) {
		if (manual && npc.getHp() > 2) {
			p.sendMessage("The growthlin is not weak enough to be axed!");
			return;
		}
		p.addEvent(event -> {
			if (!DumbRoute.withinDistance(p, npc, 1)) {
				p.getRouteFinder().routeEntity(npc);
				event.waitForMovement(p);
			}
			p.animate(1665);
			event.delay(1);
			((Growthlin) npc.getCombat()).axed = true;
			npc.getCombat().startDeath(null);
		});
	}

	@Override // TODO our hatchet is different from ours, fix this and boss works
	public void startDeath(Hit killHit) {
		if (!axed) {
			Arrays.stream(Hatchet.values()).forEach(axe -> {
				if (killHit.attacker != null && killHit.attacker.player != null && killHit.attacker.player.getInventory().contains(axe.id)) {
					axed(killHit.attacker.player, npc, false);
				} else
					npc.setHp(1);
				return;
			});

		}
		super.startDeath(killHit);
	}


	@Override
	public void init() {
		npc.deathEndListener = (entity, killer, killHit) -> {
			axed = false;
			entity.npc.remove();
		};
	}

	@Override
	public void follow() {
		follow(1);
	}

	@Override
	public boolean attack() {
		if (withinDistance(1)) {
			basicAttack();
			return true;
		}
		return false;
	}

	@Override
	public void process() {

	}
}
