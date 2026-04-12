package npc.nex.attacks.impl.std;

import npc.nex.attacks.Attack;
import npc.nex.modes.Phase;
import npc.nex.scripts.NexCombat;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.player.Player;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Misc;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public class StandardMeleeAttack implements Attack {

	@Override
	public void invoke(Player target, NexCombat nex) {
		boolean drainPrayer = nex.getPhase().equals(Phase.ZAROS);
		var chosenTarget = nex.getNpc().getPosition()
			.getRegion().players.stream()
			.filter(t -> Misc.getDistance(t.getPosition().copy(), nex.getNpc().getPosition().copy()) == 1)
			.findFirst();
		if (chosenTarget.isPresent()) {
			var p = chosenTarget.get();
			nex.getNpc().targetPlayer(p, false);
			nex.getNpc().getCombat().setTarget(p);
			nex.getNpc().face(p);
			nex.getNpc().animate(nex.getInfo().attack_animation);
			var prayer = p.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE);
			Hit hit = new Hit(nex.getNpc(), AttackStyle.SLASH)
				.randDamage(prayer ?
					(int) (Attack.MELEE_MAX_HIT * Attack.MELEE_PRAYER_EFFECTIVENESS) :
					Attack.MELEE_MAX_HIT
				)
				.ignorePrayer()
				.preDefend(_ -> nex.forceMelee = false);
			hit.postDamage(entity -> {
				if (drainPrayer && hit.damage > 0) {
					if (entity != null) {
						if (entity.isPlayer()) {
							entity.player.getPrayer().drain(hit.damage / 2);
							entity.player.sendFilteredMessage(Attack.PRAYER_DRAIN);
						}
					}
				}
			});
			nex.getTarget().hit(hit);
		}
		else
			nex.forceMelee = false;

	}
}
