package mokhaiotl.combat.attacks.impl.thrown_rock;

import core.api.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.skills.prayer.Prayer;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-30
 */
public class ThrownRangedRockAttack extends ThrownRockAttack {

	@Override
	public void invoke(Player target, NPCCombat mokhaiotl) {
		mokhaiotl.getNpc().animate(12411);
		var destination = target.getPosition().copy();
		var delay = RANGED_ROCK_PROJECTILE.send(mokhaiotl.getNpc().getCentrePosition(), destination);
		World.startEvent(5, event -> {
			target.getPrayer().deactivateProtectionPrayer();
			event.delay(1);
			GameObject.spawn(57286, destination, 10, Random.get(0, 4));
			if (target.getPosition().isAtPosition(destination)) {
				target.resetActions(true, true, true);
				var hit = new Hit(mokhaiotl.getNpc())
					.randDamage(52)
					.ignoreDefence()
					.ignorePrayer();
				target.hit(hit);
			}
		}).setCancelCondition(mokhaiotl::isDead);
		explodeRockMidAir(RANGED_PROJECTILE, Prayer.PROTECT_FROM_MISSILES, delay, destination, target, mokhaiotl);
	}
}
