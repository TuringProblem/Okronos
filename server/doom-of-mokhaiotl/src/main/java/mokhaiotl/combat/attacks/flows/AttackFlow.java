package mokhaiotl.combat.attacks.flows;

import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.utility.TickDelay;
import mokhaiotl.combat.MokhaiotlCombat;
import mokhaiotl.combat.attacks.Attack;
import mokhaiotl.combat.attacks.impl.standard.MagicAttack;
import mokhaiotl.combat.attacks.impl.standard.MeleeAttack;
import mokhaiotl.combat.attacks.impl.standard.RangedAttack;
import mokhaiotl.combat.attacks.impl.thrown_rock.ThrownMagicRockAttack;
import mokhaiotl.combat.attacks.impl.thrown_rock.ThrownRangedRockAttack;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-30
 */
public abstract class AttackFlow {
	protected final Random random = new Random();
	protected final TickDelay shockwaveCooldown = new TickDelay();
	protected final AtomicInteger attackCounter = new AtomicInteger(0);
	protected boolean performingShockwave = false;
	protected boolean throwingRocks = false;

	public abstract void handleAttackFlow(Entity target, NPCCombat mokhaiotl);

	public void init() {
		shockwaveCooldown.delay(100);
		attackCounter.set(0);
	}

	public void reset() {
		shockwaveCooldown.reset();
		attackCounter.set(0);
	}

	protected List<Attack> standardAttacks = List.of(
		new RangedAttack(),
		new MagicAttack(),
		new MeleeAttack()
	);

	protected boolean isChargeAttackProcReady(MokhaiotlCombat mokhaiotl) {
		return !mokhaiotl.isCharging() &&
			attackCounter.get() > 10 &&
			attackCounter.get() % 14 == 0;
	}


	protected void chargeAttack(MokhaiotlCombat mokhaiotl) {
		mokhaiotl.getNpc().animate(12408);
		mokhaiotl.getDoomOfMokhaiotl().transformDoom(14708);
		mokhaiotl.setCharging(true);
		mokhaiotl.getShieldedPhaseTimer().delay(50);
		mokhaiotl.getInfo().attack_ticks = 2;
		mokhaiotl.getNpc().setHeadIcon(NPC.DefaultHeadIconIndex.RangeAndMage);
	}

	protected void standardAttackFirst(Player target, NPCCombat mokhaiotl) {
		if (random.nextBoolean())
			new RangedAttack().invoke(target.player, mokhaiotl);
		else
			new MagicAttack().invoke(target.player, mokhaiotl);
	}

	protected void standardAttack(Player target, NPCCombat mokhaiotl) {
		core.api.Random.get(standardAttacks).invoke(target.player, mokhaiotl);
	}

	protected void throwRocks(Player target, NPCCombat mokhaiotl, boolean ranged) {
		if (ranged)
			new ThrownMagicRockAttack().invoke(target, mokhaiotl);
		else
			new ThrownRangedRockAttack().invoke(target, mokhaiotl);
	}
}
