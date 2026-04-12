package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Tile;

public class ZebakMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(3940), map.convertY(5406), 0);
		super.movePlayerToInstance(player);
	}

	@Override
	public void init() {
		npcs.add(new NPC(11730).spawn(map.convertX(3922), map.convertY(5403), 0, Direction.EAST, 0));
		npcs.forEach(npc -> {
			npc.face(Direction.EAST);
			npc.isMovementBlocked(false, true);
		});
	}

	@Override
	public void destroy() {
		NPC zebak = npcs.getFirst();
		for (int x = 21; x < 39; x++) {
			Tile.get(zebak.getPosition().getRegion().baseX + x,
				zebak.getPosition().getRegion().baseY + 23, zebak.getPosition().getZ(), true).unflagUnmovable();
		}
		Tile.get(zebak.getPosition().getRegion().baseX + 39,
			zebak.getPosition().getRegion().baseY + 24, zebak.getPosition().getZ(), true).unflagUnmovable();
		Tile.get(zebak.getPosition().getRegion().baseX + 29,
			zebak.getPosition().getRegion().baseY + 40, zebak.getPosition().getZ(), true).unflagUnmovable();
		Tile.get(zebak.getPosition().getRegion().baseX + 25,
			zebak.getPosition().getRegion().baseY + 38, zebak.getPosition().getZ(), true).unflagUnmovable();
		Tile.get(zebak.getPosition().getRegion().baseX + 24,
			zebak.getPosition().getRegion().baseY + 38, zebak.getPosition().getZ(), true).unflagUnmovable();
		Tile.get(zebak.getPosition().getRegion().baseX + 24,
			zebak.getPosition().getRegion().baseY + 37, zebak.getPosition().getZ(), true).unflagUnmovable();
		Tile.get(zebak.getPosition().getRegion().baseX + 23,
			zebak.getPosition().getRegion().baseY + 37, zebak.getPosition().getZ(), true).unflagUnmovable();
		Tile.get(zebak.getPosition().getRegion().baseX + 23,
			zebak.getPosition().getRegion().baseY + 36, zebak.getPosition().getZ(), true).unflagUnmovable();
		Tile.get(zebak.getPosition().getRegion().baseX + 23,
			zebak.getPosition().getRegion().baseY + 26, zebak.getPosition().getZ(), true).unflagUnmovable();
		Tile.get(zebak.getPosition().getRegion().baseX + 24,
			zebak.getPosition().getRegion().baseY + 25, zebak.getPosition().getZ(), true).unflagUnmovable();
		Tile.get(zebak.getPosition().getRegion().baseX + 39,
			zebak.getPosition().getRegion().baseY + 26, zebak.getPosition().getZ(), true).unflagUnmovable();
		for (int y = 26; y < 41; y++) {
			Tile.get(zebak.getPosition().getRegion().baseX + 38,
				zebak.getPosition().getRegion().baseY + y, zebak.getPosition().getZ(), true).unflagUnmovable();
		}
		for (int x = 26; x < 38; x++) {
			Tile.get(zebak.getPosition().getRegion().baseX + x,
				zebak.getPosition().getRegion().baseY + 41, zebak.getPosition().getZ(), true).unflagUnmovable();
		}
		for (int x = 25; x < 30; x++) {
			Tile.get(zebak.getPosition().getRegion().baseX + x,
				zebak.getPosition().getRegion().baseY + 39, zebak.getPosition().getZ(), true).unflagUnmovable();
		}
		for (int y = 27; y < 37; y++) {
			Tile.get(zebak.getPosition().getRegion().baseX + 22,
				zebak.getPosition().getRegion().baseY + y, zebak.getPosition().getZ(), true).unflagUnmovable();

		}
		for (int x = 25; x < 29; x++) {
			Tile.get(zebak.getPosition().getRegion().baseX + x,
				zebak.getPosition().getRegion().baseY + 24, zebak.getPosition().getZ(), true).unflagUnmovable();

		}
		super.destroy();
	}
}
