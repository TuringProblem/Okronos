package npc.nex.attacks.impl.std;

import io.ruin.api.utils.Random;
import npc.nex.attacks.Attack;
import npc.nex.scripts.NexCombat;
import npc.nex.utils.ZarosUtils;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.player.Player;
import io.ruin.model.skills.prayer.Prayer;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public class ShadowAttack implements Attack {
	@Override
	public void invoke(Player target, NexCombat combat) {
		combat.getNpc().animate(9181);
		combat.getNpc().face(target);
		// stat-drain prayer
		var targets = getPossibleTargets(combat);
		targets.forEach(p -> {
			var delay = Attack.SHADOW_RANGE_PROJECTILE.send(combat.getNpc(), p);
			var prayer = p.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES);
			var hit = new Hit(combat.getNpc(), AttackStyle.RANGED)
				.randDamage(prayer ? (int) (Attack.SHADOW_RANGE_MAX_HIT * Attack.SHADOW_RANGE_PRAYER_EFFECTIVENESS)
					: Attack.SHADOW_RANGE_MAX_HIT)
				.ignorePrayer()
				.clientDelay(delay)
				.postDamage((t) -> {
					t.player.getPrayer()
						.drain(ZarosUtils.hasSpectral(t.player) ? Random.get(1, Attack.SHADOW_MAX_PRAYER_DRAIN - 1)
							: Random.get(1, Attack.SHADOW_MAX_PRAYER_DRAIN));
					t.player.sendFilteredMessage(Attack.PRAYER_DRAIN);
				});
			p.hit(hit);
		});
	}
}
