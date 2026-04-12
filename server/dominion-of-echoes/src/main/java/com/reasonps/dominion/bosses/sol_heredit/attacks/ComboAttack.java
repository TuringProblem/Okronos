package com.reasonps.dominion.bosses.sol_heredit.attacks;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.sol_heredit.EchoSolHeredit;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.skills.prayer.Prayer;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-29
 */
public class ComboAttack implements Attack {

	private final int phase;

	public ComboAttack(int phase) {
		this.phase = phase;
	}

	@Override
	public void invoke(Player target, NPCCombat boss) {
		World.startEvent(e -> {
			e.setCancelCondition(boss::targetIsNotInBossRegion);
			// Start the attack animation and graphics
			boss.getNpc().animate(10886);
			boss.getNpc().graphics(2667);

			// Loop for the three strikes of the combo attack
			for (int i = 0; i < 3; i++) {
				int delay = 3;
				if (i == 2 && phase >= 4)
					delay = 4; // Delay the third strike by an additional tick in Phase 4

				// Check prayer status every tick leading up to the hit
				for (int tick = 0; tick < delay; tick++) {
					e.delay(1); // Delay by 1 tick

					// Deactivate prayer if it's active and we're not at the final tick of delay
					if (target.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE) && tick != delay - 1) {
						target.getPrayer().deactivate(Prayer.PROTECT_FROM_MELEE);
					}
				}
				var damage = switch (i) {
					case 0 -> 15; // First hit
					case 1 -> (phase >= 4) ? 30 : 25; // Second hit
					case 2 -> (phase >= 4) ? 45 : 35;
					default -> 0;
				};
				var hit = new Hit().fixedDamage(damage);
				hit.preDamage(entity -> {
					if (entity instanceof Player player) {
						if (player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE)) {
							hit.block();
						}
					}
				});
				target.hit(hit);
				// After the final attack, allow the NPC to attack again after a delay
				if (i == 2) {
					e.delay(5);
					((EchoSolHeredit) boss).setCanAttack(true);
				}
			}
		});
	}
}
