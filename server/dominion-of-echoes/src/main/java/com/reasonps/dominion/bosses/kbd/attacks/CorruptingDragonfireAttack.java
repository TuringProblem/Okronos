package com.reasonps.dominion.bosses.kbd.attacks;

import com.reasonps.dominion.bosses.Attack;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class CorruptingDragonfireAttack implements Attack {

	public static final Projectile PROJECTILE =
		new Projectile(3117, 43, 31, 51, 28, 0, 15, 250).regionBased();

	@Override
	public void invoke(Player target, NPCCombat boss) {
		var destination = target.getPosition().copy();
		boss.getNpc().animate(81);
		int delay = PROJECTILE.send(boss.getNpc(), destination);
		var hit = new Hit(boss.getNpc())
			.fixedDamage(0)
			.ignoreDefence()
			.ignorePrayer()
			.clientDelay(delay)
			.postDamage(e -> {
				target.getPrayer().deactivateAll();
				target.graphics(3163);
				target.sendMessage("Your prayers has been disabled.");
			});
		target.hit(hit);
	}
}
