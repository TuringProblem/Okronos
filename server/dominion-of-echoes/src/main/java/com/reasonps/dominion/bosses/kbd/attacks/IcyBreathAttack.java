package com.reasonps.dominion.bosses.kbd.attacks;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.IDirectional;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class IcyBreathAttack implements Attack, IDirectional {

	private final Projectile FREEZE_PROJECTILE =
		new Projectile(396, 43, 31, 51, 120, 5, 15, 250).regionBased();

	@Override
	public void invoke(Player target, NPCCombat boss) {
		var destination = target.getPosition().copy();
		boss.getNpc().animate(81);
		int delay = FREEZE_PROJECTILE.send(boss.getNpc(), destination);

		World.sendGraphics(369, 0, delay, destination);

		// prepare the delay
		boss.getNpc().addEvent(event -> {
			event.setCancelCondition(() -> boss.isDead() || boss.getNpc().isRemoved() || target == null);
			event.delay(World.getTicks(delay) + 1);
			if (target.getPosition().isAtPosition(destination))
				target.freeze(3, boss.getNpc());
		});
	}
}
