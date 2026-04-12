package npc.nex.attacks.impl.std;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import npc.nex.attacks.Attack;
import npc.nex.scripts.NexCombat;
import npc.nex.utils.ZarosUtils;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.map.Position;
import io.ruin.model.map.route.routes.ProjectileRoute;
import io.ruin.utility.Common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static core.task.api.API.*;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public class DragAttack implements Attack {

	@Override
	public void invoke(Player target, NexCombat combat) {
		if (combat.getNpc().getPosition().getRegion().players.isEmpty())
			return;
		// Get a random player who we could hit with a projectile
		var players = combat.getNpc().getPosition()
				.getRegion().players
						.stream()
						.filter(t -> t.getPosition().inBounds(combat.ATTACK_BOUNDS))
						.filter(t -> ProjectileRoute.allow(combat.getNpc(), t))
						.toList();

		final var pull = Random.get(players);
		if (pull == null)
			return;

		// Face eachother
		pull.faceTemp(combat.getNpc());
		combat.getNpc().faceTemp(pull);

		// copy positions
		final var npos = combat.getNpc().getPosition().copy();
		final var ppos = pull.getPosition().copy();

		// These booleans represent each direction a player could be pulled from.
		boolean[] shouldPullFrom = new boolean[] {false, false, false, false};

		if (ppos.getX() > (npos.getX() + 1)) // East
			shouldPullFrom[0] = true;

		if (ppos.getX() < (npos.getX() + 1)) // West
			shouldPullFrom[1] = true;

		if (ppos.getY() > (npos.getY() + 1)) // North
			shouldPullFrom[2] = true;

		if (ppos.getY() < (npos.getY() + 1)) // South
			shouldPullFrom[3] = true;

		// if we shouldn't pull from any direction, return
		if (!shouldPullFrom[0] && !shouldPullFrom[1] && !shouldPullFrom[2] && !shouldPullFrom[3])
			return;

		// for each boolean (each direction)
		for (int i = 0; i < shouldPullFrom.length; i++) {
			List<Position> t = getPossiblePullPositions(combat.getNpc(), i); // get pull positions
			if (t.equals(null)) { // if clipping is bad for any given tile
				shouldPullFrom[i] = false; // we should not pull from that tile
				continue;
			}
			for (var somePos : t) {
				if (!Common.clippingIsEmpty(somePos) || !somePos.inBounds(ZarosUtils.NEX_BOUNDS)) {
					shouldPullFrom[i] = false; // set shouldPullFrom to false
				}
			}
		}

		// printShouldPulls(shouldPullFrom);

		// Create a new Map which will contain only valid entries.
		var map = new HashMap<Integer, List<Position>>();
		var newCount = 0; // Create a new key for map key pairs
		// Loop through all the valid tiles' pull positions
		for (var count = 0; count < shouldPullFrom.length; count++) {
			// if a given direction is valid
			if (shouldPullFrom[count]) {
				map.put(newCount, getPossiblePullPositions(combat.getNpc(), count)); // get position
				newCount++;
			}
		}
		// It's important we use newCount -1. Because arrays start at 0.
		final int choice = newCount > 1 ? Random.get(0, newCount - 1) : 0;
		final List<Position> chosenTiles = map.get(choice);
		if (chosenTiles == null)
			return;

		if (chosenTiles.isEmpty())
			return;


		if (!map.isEmpty()) {
			pull.queue(() -> {
				pull.lock(LockType.FULL_CANT_ATTACK);

				var attack = fork(() -> {
					var temp = map.get(choice);
					for (var position : temp) {
						pull.animate(1501);
						pull.getMovement().teleport(position);
						sleep(1);
					}
					pull.hit(new Hit(combat.getNpc(), AttackStyle.MAGICAL_RANGED, null)
							.randDamage(20, 30)
							.ignoreDefence()
							.ignorePrayer());
				});

				while (!attack.isDone()) {
					if (combat.targetIsNotInBossRegion()) {
						break;
					}
					sleep(1);
				}

				pull.unlock();
				pull.animate(-1);
			});
		}
	}

	private List<Position> getPossiblePullPositions(Entity e, int booleanArrayIndex) {
		if (booleanArrayIndex == 0) { // East
			return Arrays.asList(
					Position.of(e.getPosition().getX() + 1 + 4, e.getPosition().getY() + 1, e.getHeight()),
					Position.of(e.getPosition().getX() + 1 + 3, e.getPosition().getY() + 1, e.getHeight()),
					Position.of(e.getPosition().getX() + 1 + 2, e.getPosition().getY() + 1, e.getHeight()));
		} else if (booleanArrayIndex == 1) { // West
			return Arrays.asList(
					Position.of(e.getPosition().getX() + 1 - 4, e.getPosition().getY() + 1, e.getHeight()),
					Position.of(e.getPosition().getX() + 1 - 3, e.getPosition().getY() + 1, e.getHeight()),
					Position.of(e.getPosition().getX() + 1 - 2, e.getPosition().getY() + 1, e.getHeight()));
		} else if (booleanArrayIndex == 2) { // North
			return Arrays.asList(
					Position.of(e.getPosition().getX() + 1, e.getPosition().getY() + 1 + 4, e.getHeight()),
					Position.of(e.getPosition().getX() + 1, e.getPosition().getY() + 1 + 3, e.getHeight()),
					Position.of(e.getPosition().getX() + 1, e.getPosition().getY() + 1 + 2, e.getHeight()));
		} else if (booleanArrayIndex == 3) { // South
			return Arrays.asList(
					Position.of(e.getPosition().getX() + 1, e.getPosition().getY() + 1 - 4, e.getHeight()),
					Position.of(e.getPosition().getX() + 1, e.getPosition().getY() + 1 - 3, e.getHeight()),
					Position.of(e.getPosition().getX() + 1, e.getPosition().getY() + 1 - 2, e.getHeight()));
		} else {
			return List.of(new Position(2925, 5203, 0));
		}
	}
}
