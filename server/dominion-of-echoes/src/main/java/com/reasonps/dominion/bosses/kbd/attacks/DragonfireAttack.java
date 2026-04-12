package com.reasonps.dominion.bosses.kbd.attacks;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.IDirectional;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;

import java.util.ArrayList;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class DragonfireAttack implements Attack, IDirectional {

	public static final Projectile FIRE_PROJECTILE =
		new Projectile(393, 43, 31, 51, 120, 5, 15, 250).regionBased();

	@Override
	public void invoke(Player target, NPCCombat boss) {
		var destination = target.getPosition().copy();
		var enraged = boss.getNpc().getHp() <= (boss.getNpc().getMaxHp() / 2);
		boss.getNpc().animate(81);
		int delay = FIRE_PROJECTILE.send(boss.getNpc(), destination);

		var size = enraged ? 5 : 3;
		var offset = enraged ? 2 : 1;
		// explodes on impact in a 3x3 X-shaped pattern, dealing 15-damage.
		var southEastPath = getDiagonalTiles(Direction.SOUTH_EAST, destination.translated(-offset, offset), size);
		var southWestPath = getDiagonalTiles(Direction.SOUTH_WEST, destination.translated(offset, offset), size);

		// Collect the tiles to light up
		var hotSpots = new ArrayList<Position>();
			hotSpots.addAll(southEastPath);
			hotSpots.addAll(southWestPath);

		// Build the hit
		var hit = new Hit(boss.getNpc(), AttackStyle.DRAGONFIRE)
			.randDamage(15)
			.ignoreDefence()
			.ignorePrayer();

		// prepare the delay
		boss.getNpc().addEvent(event -> {
			event.setCancelCondition(() -> boss.isDead() || boss.getNpc().isRemoved() || target == null);
			event.delay(World.getTicks(delay) + 1);
			hotSpots.forEach(pos -> {
				World.sendGraphics(131, 0, 0, pos);
				if (target.getPosition().isAtPosition(pos))
					target.hit(hit);
			});
		});
	}
}
