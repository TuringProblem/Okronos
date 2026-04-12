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
public class BloodAttack implements Attack {

	@Override
	public void invoke(Player target, NexCombat combat) {
		combat.getNpc().animate(9181);
		combat.getNpc().face(target);
		var targets = getPossibleTargets(combat);
		targets.forEach(p -> {
			p.privateSound(106, 1, 0);
			int delay = BLOOD_MAGIC_PROJECTILE.send(combat.getNpc(), p);
			boolean prayer = p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC);
			Hit hit = new Hit(combat.getNpc(), AttackStyle.MAGIC)
				.randDamage(prayer ? (int) (BLOOD_MAGIC_MAX_HIT * BLOOD_MAGIC_PRAYER_EFFECTIVENESS)
					: BLOOD_MAGIC_MAX_HIT)
				.ignorePrayer()
				.clientDelay(delay);
			hit.postDamage(t -> {
				if (hit.damage > 0) {
					t.privateSound(105, 1, 0);
					t.graphics(377, 0, 0);
					combat.getNpc().incrementHp((int) (hit.damage * 0.25)); // heal 1/4 of dmg done
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
