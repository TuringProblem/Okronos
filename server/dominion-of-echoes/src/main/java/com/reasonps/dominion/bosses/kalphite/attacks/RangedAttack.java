package com.reasonps.dominion.bosses.kalphite.attacks;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.kalphite.EchoKalphiteQueen;
import com.reasonps.dominion.bosses.kalphite.Form;
import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;

import static com.reasonps.dominion.bosses.kalphite.Constants.RANGED_PROJECTILE;

/**
 * @author Glabay | Glabay-Studios
 * @project kronos
 * @social Discord: Glabay
 * @since 2025-05-16
 */
public class RangedAttack implements Attack {

	@Override
	public void invoke(Player target, NPCCombat boss) {
		final var rangedHit = new Hit(boss.getNpc(), AttackStyle.RANGED)
				.randDamage(35)
				.ignoreDefence()
				.ignorePrayer();

		boss.getNpc().graphics(3171);
		boss.getNpc().animate(((EchoKalphiteQueen) boss).currentForm().getRangedAnimation());
		int delay = RANGED_PROJECTILE.send(boss.getNpc(), target);
		boss.getNpc().addEvent(event -> {
			event.delay(World.getTicks(delay));
			if (boss.isDead())
				return;
			target.getPosition().getRegion().players
					.stream()
					.filter(p -> p == target)
					.filter(boss::canAttack)
					.forEach(p -> {
						p.hit(rangedHit);
						if (((EchoKalphiteQueen) boss).currentForm().equals(Form.FIRST))
							p.sendMessage(Color.WHITE.wrap("The Queen punishes your distance with a piercing attack!"));
					});
		});
	}
}
