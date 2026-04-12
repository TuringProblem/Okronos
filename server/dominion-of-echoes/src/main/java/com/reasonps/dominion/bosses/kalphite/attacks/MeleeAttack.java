package com.reasonps.dominion.bosses.kalphite.attacks;

import com.reasonps.dominion.bosses.Attack;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;

import static io.ruin.model.skills.prayer.Prayer.PROTECT_FROM_MELEE;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-16
 */
public class MeleeAttack implements Attack {

	@Override
	public void invoke(Player target, NPCCombat boss) {
		boss.basicAttack();
		boss.getNpc().animate(boss.getInfo().attack_animation);
		boss.defendAnim();
		Hit hit = new Hit(boss.getNpc(), boss.getAttackStyle(), null).randDamage(38);
		if (target != null) {
			boss.faceTarget();
			if (target.getPrayer().isActive(PROTECT_FROM_MELEE))
				hit.damage = 0;
			target.hit(hit);
		}
	}
}
