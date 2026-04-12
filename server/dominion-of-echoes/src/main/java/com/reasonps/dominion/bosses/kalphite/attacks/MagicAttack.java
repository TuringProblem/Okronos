package com.reasonps.dominion.bosses.kalphite.attacks;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.kalphite.EchoKalphiteQueen;
import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Misc;

import java.util.LinkedList;

import static com.reasonps.dominion.bosses.kalphite.Constants.MAGIC_PROJECTILE;

/**
 * @author Glabay | Glabay-Studios
 * @project kronos
 * @social Discord: Glabay
 * @since 2025-05-16
 */
public class MagicAttack implements Attack {

	@Override
	public void invoke(Player target, NPCCombat boss) {
		boss.getNpc().animate(((EchoKalphiteQueen) boss).currentForm().getMagicAnimation());
		boss.getNpc().graphics(((EchoKalphiteQueen) boss).currentForm().getMagicGfx());
		var hit = new Hit(boss.getNpc(), AttackStyle.MAGIC)
				.ignoreDefence()
				.randDamage(49);

		boss.getNpc().addEvent(event -> {
			event.setCancelCondition(() -> boss.isDead() || boss.getNpc().isRemoved());
			Entity source = boss.getNpc();
			Entity dest = target;
			int bounces = 0;
			while (dest != null && bounces < 4) {
				int delay = MAGIC_PROJECTILE.send(source, target);
				event.delay(World.getTicks(delay));
				World.sendGraphics(4232, 0, 0, target.getPosition());

				// The magic attack will also drain prayer points if it is not prayed against
				// correctly
				if (dest.isPlayer() && !dest.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
					// drain prayer points
				} else if (dest.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
					hit.damage = 0;
				}
				target.hit(hit);
				event.delay(2);
				// looking for a target to bounce to
				var newTargets = new LinkedList<Entity>(boss.getNpc().localPlayers());
				Entity finalDest = dest;
				newTargets.removeIf(e -> e == finalDest || !boss.canAttack(e)
						|| Misc.getDistance(finalDest.getPosition(), e.getPosition()) > 2); // invalid targets
				if (newTargets.isEmpty())
					return; // no targets found, abort
				source = dest;
				dest = Random.get(newTargets);
				bounces++;
			}
		});
	}
}
