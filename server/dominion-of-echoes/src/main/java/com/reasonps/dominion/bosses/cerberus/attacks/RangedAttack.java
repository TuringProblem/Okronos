package com.reasonps.dominion.bosses.cerberus.attacks;

import com.reasonps.dominion.bosses.Attack;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;

import static com.reasonps.dominion.bosses.cerberus.Constants.RANGED_PROJECTILE;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class RangedAttack implements Attack {

	@Override
	public void invoke(Player target, NPCCombat boss) {
		int delay = RANGED_PROJECTILE.send(boss.getNpc(), target);
		boss.getNpc().animate(4492);
		var hit = new Hit(boss.getNpc(), AttackStyle.RANGED, null)
			.randDamage(boss.getInfo().max_damage)
			.clientDelay(delay);
			hit.postDamage(e -> {
				if (hit.damage > 0)
					e.graphics(1244, 100, 0);
			});

		target.hit(hit);
	}
}
