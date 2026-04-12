package io.ruin.model.item.actions.impl;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.raids.tob.dungeon.boss.verzik.VerzikPillar;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;

import java.util.ArrayList;
import java.util.List;

public class RingOfAoE {
	private static final Projectile AOE_PROJECTILE = new Projectile(1522, 50, 43, 35, 40, 6, 16, 192);


	public static void handleAoE(Player player) {
		var blackListedNpcIds = List.of(
			8379,
			17021, // Summer event NPC
			17022, // Summer event NPC
			// Nightmare Totems START
			9434,
			9435,
			9436,

			9437,
			9438,
			9439,

			9440,
			9441,
			9442,

			9443,
			9444,
			9445
			// Nightmare Totems END
		);
		if (Random.get(3) == 0) {
			List<NPC> potentialNPCs = new ArrayList<>();
			player.localNpcs().forEach(npc -> {
				if (npc.getPosition().isWithinDistance(player.getPosition(), 5) && npc.getCombat() != null) {
					if (!blackListedNpcIds.contains(npc.getId()))
						potentialNPCs.add(npc);
				}
			});
			if (player.inMulti()) {
				int targetsToHit = Random.get(4, 8);
				for (int i = 0; i < targetsToHit; i++) {
					if (potentialNPCs.isEmpty())
						break;
					NPC target = potentialNPCs.get(Random.get(potentialNPCs.size() - 1));
					int delay = AOE_PROJECTILE.send(player, target);
					if (target.getCombat() == null) return;
					target.hit(new Hit(player, AttackStyle.MAGIC).randDamage(5).ignorePrayer().ignoreDefence().clientDelay(delay));
					potentialNPCs.remove(target);
				}
			}
			else {
				if (player.getCombat().getTarget() instanceof NPC target) {
					int delay = AOE_PROJECTILE.send(player, target);
					target.hit(new Hit(player, AttackStyle.MAGIC).randDamage(5, 8).ignorePrayer().ignoreDefence().clientDelay(delay));
				}
			}
			player.sendFilteredMessage("Your Ring of AoE has just hit your target. (Pew-pew)");
		}
	}
}
