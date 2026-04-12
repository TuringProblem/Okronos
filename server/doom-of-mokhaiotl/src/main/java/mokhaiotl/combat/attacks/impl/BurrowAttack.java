package mokhaiotl.combat.attacks.impl;

import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import mokhaiotl.combat.MokhaiotlCombat;
import mokhaiotl.combat.attacks.Attack;

import static core.task.api.API.queue;
import static core.task.api.API.sleep;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-17
 */
public class BurrowAttack implements Attack {

	@Override
	public void invoke(Player target, NPCCombat mokhaiotl) {
		var boss = ((MokhaiotlCombat) mokhaiotl).getDoomOfMokhaiotl();
		queue(() -> {
			((MokhaiotlCombat) mokhaiotl).setBurrowed(true);
			boss.lock(LockType.FULL_NULLIFY_DAMAGE);
			boss.animate(12420);
			sleep(3);
			boss.transformDoom(14709);
			// Remember to unlock!
			boss.unlock();
			boss.burrowDigToTarget(target.player, ((MokhaiotlCombat) mokhaiotl)).await();
		});
	}
}
