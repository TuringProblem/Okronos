package mokhaiotl.combat.attacks.flows.impl;

import io.ruin.model.World;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPCCombat;
import mokhaiotl.combat.MokhaiotlCombat;
import mokhaiotl.combat.attacks.flows.AttackFlow;
import mokhaiotl.combat.attacks.impl.BurrowAttack;
import mokhaiotl.combat.attacks.impl.DemonicLarvaeAttack;
import mokhaiotl.combat.attacks.impl.ShockwaveAttack;
import mokhaiotl.combat.attacks.impl.standard.MeleeLickAttack;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-11
 */
public class DelveSevenAttackFlow extends AttackFlow {

	public void handleAttackFlow(Entity target, NPCCombat mokhaiotl) {
		if (mokhaiotl.getNpc().isLocked()) return;
		if (throwingRocks) return;
		var doom = ((MokhaiotlCombat) mokhaiotl).getDoomOfMokhaiotl();
		if (((MokhaiotlCombat) mokhaiotl).isBurrowed() && mokhaiotl.getNpc().getId() == 14709) {
			if (doom.getDelveLevel() > 5)
				standardAttack(target.player, mokhaiotl);

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

		// Charge Attack
		if (isChargeAttackProcReady((MokhaiotlCombat) mokhaiotl)) {
			chargeAttack((MokhaiotlCombat) mokhaiotl);
			attackCounter.incrementAndGet();
			return;
		}

		// Car Phase entrance
		if (!((MokhaiotlCombat) mokhaiotl).isBurrowed() && mokhaiotl.getNpc().getHpPercentage() <= 0.75)
			new BurrowAttack().invoke(target.player, mokhaiotl);

		// Thrown rocks
		if (attackCounter.get() > 2 && attackCounter.get() % 3 == 0) {
			var ranged = random.nextBoolean();
			attackCounter.incrementAndGet();
			throwingRocks = true;
			throwRocks(target.player, mokhaiotl, ranged);
			World.startEvent(1, event -> {
				throwRocks(target.player, mokhaiotl, ranged);
				event.delay(1);
				throwRocks(target.player, mokhaiotl, ranged);
				throwingRocks = false;
			});
			return;
		}
		standardAttack(target.player, mokhaiotl);
		attackCounter.incrementAndGet();
	}
}
