package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.cache.Color;
import io.ruin.cache.NpcID;
import io.ruin.model.World;
import io.ruin.model.activities.bosses.instancetoken.InstanceManager;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.*;
import io.ruin.model.map.dynamic.DynamicMap;

import java.util.ArrayList;
import java.util.List;

public class VardorvisMapHandler extends MapHandler {

	Bounds bossArea;

	@Override
	public void movePlayerToInstance(Player player) {
		player.perfectVardorvisKills = 0;
		player.getMovement().teleport(map.convertX(1121), map.convertY(3426), 0);
		super.movePlayerToInstance(player);
	}

	private boolean isBoundsEmpty(Player player, Bounds bounds) {
		return !player.getPosition().inBounds(bounds);
	}

	@Override
	public void init() {
		NPC vard = new NPC(12223).spawn(map.convertX(1129), map.convertY(3417), 0, Direction.SOUTH, 0);
		npcs.add(vard);
		npcs.forEach(npc -> {
			npc.isMovementBlocked(false, true);
			npc.setHidden(true);
		});
		bossArea = new Bounds(map.convertX(1124), map.convertY(3413), map.convertX(1134), map.convertY(3423), 0);
		Bounds checkArea = new Bounds(map.convertX(1125), map.convertY(3414), map.convertX(1132), map.convertY(3421), 0);
		World.startEvent(e -> {
			while (vard != null && vard.isHidden() && !vard.isRemoved()) {
				e.delay(1);
				if (!vard.getPosition().getRegion().players.isEmpty()) {
					Player player = vard.getPosition().getRegion().players.getFirst();
					if (!isBoundsEmpty(player, checkArea)) {
						onEnter(vard);
						break;
					}
				}
			}
		});
	}

	private void onEnter(NPC npc) {
		npc.setHidden(false);
		findOuterPositions(bossArea).forEach(pos -> Tile.get(pos, true).flagUnmovable());
	}


	List<Position> outerPositions = new ArrayList<>();

	List<Position> findOuterPositions(Bounds bounds) {
		outerPositions.clear();
		for (int x = bounds.swX; x <= bounds.neX; x++) {
			for (int y = bounds.swY; y <= bounds.neY; y++) {
				boolean onPerimeter = (x == bounds.swX || x == bounds.neX) && (y != bounds.swY && y != bounds.neY)
						|| (y == bounds.swY || y == bounds.neY) && (x != bounds.swX && x != bounds.neX);
				if (onPerimeter) {
					outerPositions.add(new Position(x, y));
				}
			}
		}
		return outerPositions;
	}


	@Override
	public void destroy() {
		findOuterPositions(bossArea).forEach(pos -> Tile.get(pos, true).unflagUnmovable());
		players.forEach(plr -> plr.perfectVardorvisKills = 0);
		super.destroy();
	}
}
