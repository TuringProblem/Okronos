package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Tile;

public class KephriMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(3544), map.convertY(5408), 0);
		super.movePlayerToInstance(player);
	}

	Bounds bounds;

	@Override
	public void init() {
		npcs.add(new NPC(11719).spawn(map.convertX(3549), map.convertY(5406), 0, 0));
		bounds = new Bounds(map.convertX(3549), map.convertY(5406), map.convertX(3553), map.convertY(5410), 0);
		bounds.forEachPos(pos -> Tile.get(pos, true).flagUnmovable());

		npcs.forEach(npc -> {
			npc.face(Direction.WEST);
			npc.isMovementBlocked(false, true);
		});
	}

	@Override
	public void destroy() {
		bounds.forEachPos(pos -> Tile.get(pos, true).unflagUnmovable());
		super.destroy();
	}
}
