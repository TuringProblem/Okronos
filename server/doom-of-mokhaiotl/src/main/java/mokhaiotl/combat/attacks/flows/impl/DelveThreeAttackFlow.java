package mokhaiotl.combat.attacks.flows.impl;

import io.ruin.model.World;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPCCombat;
import mokhaiotl.combat.MokhaiotlCombat;
import mokhaiotl.combat.attacks.flows.AttackFlow;
import mokhaiotl.combat.attacks.impl.DemonicLarvaeAttack;
import mokhaiotl.combat.attacks.impl.ShockwaveAttack;
import mokhaiotl.combat.attacks.impl.standard.MeleeLickAttack;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-30
 */
public class DelveThreeAttackFlow extends AttackFlow {

	private boolean isShieldPhase = false;

	public void handleAttackFlow(Entity target, NPCCombat mokhaiotl) {
		if (mokhaiotl.getNpc().isLocked()) return;
		if (throwingRocks) return;
		if (isShieldPhase) {
			mokhaiotl.getNpc().animate(12409);
			return;
		}
		if (mokhaiotl.getNpc().getId() == 14708) {
			mokhaiotl.getNpc().animate(12409);
			mokhaiotl.getNpc().graphics(3412);
			return;
		}
		// Shockwave Attack
		if (!performingShockwave && shockwaveCooldown.finished()) {
			new ShockwaveAttack().invoke(target.player, mokhaiotl);
			performingShockwave = true;
		}

		// Demonic Larvae (can spawn without using an Attack Cycle)
		if (attackCounter.get() > 2 && attackCounter.get() % 5 == 0) {
			new DemonicLarvaeAttack().invoke(target.player, mokhaiotl);
		}

		// Melee Lick
		if (target.getPosition().isWithinDistance(mokhaiotl.getNpc().getCentrePosition(), 1)) {
			new MeleeLickAttack().invoke(target.player, mokhaiotl);
			return;
		}

		// TODO: Shield Phase
		/**
		 * This has been left out of the fight, and in future flows on purpose.
		 * As it was deemed as too much for the Reason community
		 * and more of an annoying extra "drag-out" mechanic rather than a useful part of the fight
		 */


		// Thrown rocks
		if (attackCounter.get() > 2 && attackCounter.get() % 3 == 0) {
			var ranged = random.nextBoolean();
			attackCounter.incrementAndGet();
			throwingRocks = true;
			throwRocks(target.player, mokhaiotl, ranged);
			World.startEvent(1, _ -> {
				throwRocks(target.player, mokhaiotl, ranged);
				throwingRocks = false;
			});
			return;
		}

		// Charge Attack
		if (isChargeAttackProcReady((MokhaiotlCombat) mokhaiotl)) {
			chargeAttack((MokhaiotlCombat) mokhaiotl);
			attackCounter.incrementAndGet();
			return;
		}

		standardAttack(target.player, mokhaiotl);
		attackCounter.incrementAndGet();
	}
}
