package com.reasonps.dominion.bosses.cerberus.attacks;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.IDirectional;
import com.reasonps.dominion.bosses.cerberus.EchoCerberus;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.utility.Random;

import static core.combat.api.ReasonUtils.pos;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class LavaAttack implements Attack, IDirectional {

	public static final int LAVA_GFX = 4234;
	private final int LAVA_SPAWN_GFX = 4235;

	@Override
	public void invoke(Player target, NPCCombat boss) {
		boss.getNpc().animate(4493);
		boss.getNpc().forceText("Grrrrrrrrrr");
		var targetPosition = target.getPosition().copy();
		var westPoolSpawn = getDirectionForLavaPool(target, true);
		var eastPoolSpawn = getDirectionForLavaPool(target, false);
		Position[] positions = {
			westPoolSpawn.equals(Direction.WEST) ?
				getHorizontalTiles(westPoolSpawn, targetPosition, 5 + Random.get(-1, 2)).getLast() :
				getDiagonalTiles(westPoolSpawn, targetPosition, 5 + Random.get(-1, 2)).getLast(),
			eastPoolSpawn.equals(Direction.EAST) ?
				getHorizontalTiles(eastPoolSpawn, targetPosition, 5 + Random.get(-1, 2)).getLast() :
				getDiagonalTiles(eastPoolSpawn, targetPosition, 5 + Random.get(-1, 2)).getLast()
		};

		for (Position pos : positions) {
			boss.getNpc().addEvent(event -> {
				event.setCancelCondition(() -> boss.isDead() || boss.getNpc().isRemoved() || target == null);
				World.sendGraphics(LAVA_SPAWN_GFX, 0, 0, pos.getX(), pos.getY(), pos.getZ());
				event.delay(3);
				((EchoCerberus) boss).getLavaPoolPositions().add(pos);
				event.delay(2);
				var hotSpots = getTiles(targetPosition, pos);
				for (var hotSpot : hotSpots) {
					((EchoCerberus) boss).getLavaPoolPositions().add(hotSpot);
					event.delay(1);
				}
			});
		}
	}

	private Direction getDirectionForLavaPool(Player player, boolean westSide) {
		var middleTile = pos(player.currentDynamicMap, 24, 32);
		var distance = player.getPosition().distance(middleTile);
		if (player.getPosition().getY() < middleTile.getY()) {
			if (distance <= 2)
				return westSide ? Direction.SOUTH_WEST : Direction.SOUTH_EAST;
			else
				return westSide ? Direction.NORTH_WEST : Direction.NORTH_EAST;
		}
		return westSide ? Direction.WEST : Direction.EAST;
	}
}
