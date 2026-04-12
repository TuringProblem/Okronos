package com.reasonps.dominion.bosses.cerberus.attacks;

import com.reasonps.dominion.bosses.Attack;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class MeleeAttack implements Attack {

	@Override
	public void invoke(Player target, NPCCombat boss) {
		boss.getNpc().animate(boss.getInfo().attack_animation);
		target.hit(new Hit(boss.getNpc(), AttackStyle.SLASH, null).randDamage(boss.getInfo().max_damage));
	}
}
