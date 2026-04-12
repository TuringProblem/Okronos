package com.reasonps.dominion.bosses.kalphite.specials;

import com.reasonps.dominion.bosses.kalphite.EchoKalphiteQueen;
import com.reasonps.dominion.bosses.kalphite.Form;
import com.reasonps.dominion.bosses.kalphite.SpecialAttack;
import com.reasonps.dominion.bosses.kalphite.attacks.RangedAttack;
import com.reasonps.dominion.bosses.kalphite.attacks.RangedBuzzAttack;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Glabay | Glabay-Studios
 * @project kronos
 * @social Discord: Glabay
 * @since 2025-05-22
 */
public class PhaseOneSpecial implements SpecialAttack {

	private final int MAX_ATTACKS = 5;

	@Override
	public void invoke(Player target, NPCCombat boss) {
		var underTheQueen = new AtomicBoolean(false);
		int ANIMATION_ID = 6240;
		boss.getNpc().animate(ANIMATION_ID);
		// Lock the boss, but allow her to still take damage if the player is able to :P
		boss.getNpc().lock(LockType.MOVEMENT);
		boss.getNpc().startEvent(event -> {
			event.delay(ATTACK_DELAY);
			for (int i = 0; i <= MAX_ATTACKS; i++) {
				if (target.getPosition().distance(boss.getNpc().getCentrePosition()) > 6)
					new RangedBuzzAttack().invoke(target, boss);
				castLightning(Form.FIRST, target, getTilesToCastLightning(boss.getNpc(), underTheQueen.get()));
				event.delay(ATTACK_DELAY);
				underTheQueen.set(!underTheQueen.get());
			}
			((EchoKalphiteQueen) boss).performingSpecial = false;
			boss.getNpc().unlock();
		}).setCancelCondition(
				() -> boss.getNpc().getId() == Form.SECOND.getNpcId() || boss.isDead() || boss.getNpc().isRemoved());

	}

	/**
	 * Determines the tiles where lightning should be cast during a special attack,
	 * based on whether the attack targets under the NPC (the "queen") or the outer
	 * boundary.
	 *
	 * @param npc           The NPC performing the lightning attack. This NPC's
	 *                      bounds and outer bounds are used
	 *                      to calculate the affected tiles.
	 * @param underTheQueen A boolean flag indicating the lightning's target area.
	 *                      If true, the lightning
	 *                      is targeted under the NPC (inside the boss's bounds). If
	 *                      false, the lightning
	 *                      is targeted outside the boss's bounds.
	 * @return A list of positions representing the tiles affected by the lightning
	 *         cast. These tiles
	 *         are determined based on the NPC's bounds and the attack
	 *         configuration.
	 */
	private List<Position> getTilesToCastLightning(NPC npc, boolean underTheQueen) {
		var hotSpots = new ArrayList<Position>();
		if (underTheQueen) {
			hotSpots.addAll(getBossBounds(npc).getAllPositions());
		} else {
			var all = getOutterBounds(npc).getAllPositions();
			all.removeAll(getBossBounds(npc).getAllPositions());
			hotSpots.addAll(all);
		}
		return hotSpots;
	}

	/**
	 * Calculates the outer bounding area around a given NPC. This area is based on
	 * the
	 * NPC's center position and a predefined fixed range.
	 *
	 * @param npc The NPC whose outer bounds are to be calculated. The NPC object
	 *            must
	 *            contain the necessary center position information.
	 * @return A Bounds object representing the outer area surrounding the NPC.
	 */
	private Bounds getOutterBounds(NPC npc) {
		return new Bounds(npc.getCentrePosition(), 5);
	}

	/**
	 * Calculates the bounding area of a given NPC based on its center position and
	 * size.
	 *
	 * @param npc The NPC whose bounds are to be calculated. The NPC object should
	 *            contain
	 *            the center position and size data needed for bounds computation.
	 * @return A Bounds object representing the area occupied by the NPC.
	 */
	private Bounds getBossBounds(NPC npc) {
		var size = npc.getSize() / 2;
		return new Bounds(npc.getCentrePosition(), size);
	}
}
