package npc.nex.attacks.impl.std;

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
public class ZarosAttack implements Attack {
	@Override
	public void invoke(Player target, NexCombat combat) {
		combat.getNpc().animate(9181);
		combat.getNpc().face(target);
		var targets = getPossibleTargets(combat);
		targets.forEach(p -> {
			p.privateSound(171, 1, 0);
			int delay = ZAROS_MAGIC_PROJECTILE.send(combat.getNpc(), p);
			boolean prayer = p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC);
			Hit hit = new Hit(combat.getNpc(), AttackStyle.MAGIC)
				.randDamage(prayer ? (int) (ZAROS_MAGIC_MAX_HIT * ZAROS_MAGIC_PRAYER_EFFECTIVENESS)
					: ZAROS_MAGIC_MAX_HIT)
				.ignorePrayer()
				.clientDelay(delay);
			hit.postDamage(t -> {
				if (hit.damage > 0) {
					t.privateSound(3012, 1, 0);
					t.graphics(2008, 124, 0);
					t.player.getPrayer()
						.drain(ZarosUtils.hasSpectral(t.player)
							? (int) (ZAROS_MAGIC_PRAYER_DRAIN * ZAROS_MAGIC_PRAYER_DRAIN_SPECTRAL)
							: ZAROS_MAGIC_PRAYER_DRAIN);
					t.player.sendFilteredMessage(PRAYER_DRAIN);
				} else {
					t.privateSound(3015, 1, 0);
					t.graphics(85, 124, 0); // splash
					hit.hide();
				}
			});
			p.hit(hit);
		});
	}
}
