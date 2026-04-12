package npc.nex.attacks.impl.std;

import npc.nex.attacks.Attack;
import npc.nex.scripts.NexCombat;
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
public class SmokeAttack  implements Attack {
	@Override
	public void invoke(Player target, NexCombat nexCombat) {
		nexCombat.getNpc().animate(9181);
		nexCombat.getNpc().face(target);
		var targets = getPossibleTargets(nexCombat);
		targets.forEach(p -> {
			p.privateSound(183, 1, 0);
			var delay = Attack.SMOKE_MAGIC_PROJECTILE.send(nexCombat.getNpc(), p);
			var prayer = p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC);
			var hit = new Hit(nexCombat.getNpc(), AttackStyle.MAGIC)
				.randDamage(prayer ? (int) (Attack.SMOKE_MAGIC_MAX_HIT * Attack.SMOKE_MAGIC_PRAYER_EFFECTIVENESS)
					: Attack.SMOKE_MAGIC_MAX_HIT)
				.ignorePrayer()
				.clientDelay(delay);
			hit.postDamage(t -> {
				if (hit.damage > 0) {
					t.privateSound(185, 1, 0);
					t.graphics(385, 124, 0);
					t.poison(8);
				}
				else {
					t.privateSound(227, 1, 0);
					t.graphics(85, 124, 0); // splash
					hit.hide();
				}
			});
			p.hit(hit);
		});
	}
}
