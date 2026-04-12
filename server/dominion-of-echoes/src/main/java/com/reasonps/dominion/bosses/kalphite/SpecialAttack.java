package com.reasonps.dominion.bosses.kalphite;

import com.reasonps.dominion.bosses.Attack;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-24
 */
public interface SpecialAttack extends Attack {

	int GFX_ID = 3178;
	int ATTACK_DELAY = 3;

	/**
	 * Casts a lightning attack on a target player and animates lightning graphics
	 * at the specified positions. If the target player is on one of the affected
	 * positions, they are struck with damage that ignores defense and prayer.
	 *
	 * @param target    The player who is the target of the lightning attack. The player
	 *                  may take damage if standing on an affected tile.
	 * @param toLightUp A list of positions where lightning will be cast. Each specified
	 *                  position will show a lightning graphic, and the target may take
	 *                  damage if their position matches one of these tiles.
	 */
	default void castLightning(Form form, Player target, List<Position> toLightUp) {
		if (target.getCombat().isDead() || !target.insideRaid)
			return;
		for (var tile : toLightUp) {
			World.sendGraphics(GFX_ID, 0, 0, tile);
			if (target.getPosition().isAtPosition(tile)) {
				var hit = new Hit(HitType.DAMAGE);
					hit.randDamage(20, 30);
					hit.ignoreDefence();
					hit.ignorePrayer();
				target.hit(hit);
			}
			// else if they are 1 tile away, hit for 50%
			else if (form == Form.SECOND && target.getPosition().isWithinDistance(tile, 1)) {
				var hit = new Hit(HitType.DAMAGE);
					hit.randDamage(10, 15);
					hit.ignoreDefence();
					hit.ignorePrayer();
				target.hit(hit);
			}
		}
	}
}
