package io.ruin.model.activities.raids.toa.rooms;

import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.activities.raids.toa.ToARoom;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.actions.ObjectAction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ZebakRoom extends ToARoom {
	@Getter
	NPC zebak;

	static {
		ObjectAction.register(45509, "Climb-up", (player, steps) -> {
			// animate climbing out of the water
			player.graphics(163);
			var region = player.getPosition().getRegion();
			var tileToMoveTo = Position.of(region.baseX + 30, region.baseY + 41);
			player.getMovement().teleport(tileToMoveTo);
		});
	}

	@Override
	public void buildRoom() throws DynamicMap.DynamicMapBuildException {
		map = new DynamicMap().build(InstanceMaps.ZEBAK.getRegionId(), InstanceMaps.ZEBAK.getHeight());
	}

	@Override
	public void populateRoom() {
		zebak = new NPC(11730).spawn(map.convertX(3920), map.convertY(5404), 0, Direction.EAST, 0);
		npcs.add(zebak);
		List<Position> tileBlockPositions = new ArrayList<>();
		for (int x = 21; x < 39; x++) {
			Tile.get(zebak.getPosition().getRegion().baseX + x,
					zebak.getPosition().getRegion().baseY + 23, zebak.getPosition().getZ(), true).flagUnmovable();
			tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + x,
					zebak.getPosition().getRegion().baseY + 23, zebak.getPosition().getZ()));
		}
		Tile.get(zebak.getPosition().getRegion().baseX + 39,
				zebak.getPosition().getRegion().baseY + 24, zebak.getPosition().getZ(), true).flagUnmovable();
		tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + 39,
				zebak.getPosition().getRegion().baseY + 24, zebak.getPosition().getZ()));
		Tile.get(zebak.getPosition().getRegion().baseX + 29,
				zebak.getPosition().getRegion().baseY + 40, zebak.getPosition().getZ(), true).flagUnmovable();
		tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + 29,
				zebak.getPosition().getRegion().baseY + 40, zebak.getPosition().getZ()));
		Tile.get(zebak.getPosition().getRegion().baseX + 25,
				zebak.getPosition().getRegion().baseY + 38, zebak.getPosition().getZ(), true).flagUnmovable();
		tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + 25,
				zebak.getPosition().getRegion().baseY + 38, zebak.getPosition().getZ()));
		Tile.get(zebak.getPosition().getRegion().baseX + 24,
				zebak.getPosition().getRegion().baseY + 38, zebak.getPosition().getZ(), true).flagUnmovable();
		tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + 24,
				zebak.getPosition().getRegion().baseY + 38, zebak.getPosition().getZ()));
		Tile.get(zebak.getPosition().getRegion().baseX + 24,
				zebak.getPosition().getRegion().baseY + 37, zebak.getPosition().getZ(), true).flagUnmovable();
		tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + 24,
				zebak.getPosition().getRegion().baseY + 37, zebak.getPosition().getZ()));
		Tile.get(zebak.getPosition().getRegion().baseX + 23,
				zebak.getPosition().getRegion().baseY + 37, zebak.getPosition().getZ(), true).flagUnmovable();
		tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + 23,
				zebak.getPosition().getRegion().baseY + 37, zebak.getPosition().getZ()));
		Tile.get(zebak.getPosition().getRegion().baseX + 23,
				zebak.getPosition().getRegion().baseY + 36, zebak.getPosition().getZ(), true).flagUnmovable();
		tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + 23,
				zebak.getPosition().getRegion().baseY + 36, zebak.getPosition().getZ()));
		Tile.get(zebak.getPosition().getRegion().baseX + 23,
				zebak.getPosition().getRegion().baseY + 26, zebak.getPosition().getZ(), true).flagUnmovable();
		tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + 23,
				zebak.getPosition().getRegion().baseY + 26, zebak.getPosition().getZ()));
		Tile.get(zebak.getPosition().getRegion().baseX + 24,
				zebak.getPosition().getRegion().baseY + 25, zebak.getPosition().getZ(), true).flagUnmovable();
		tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + 24,
				zebak.getPosition().getRegion().baseY + 25, zebak.getPosition().getZ()));
		Tile.get(zebak.getPosition().getRegion().baseX + 39,
				zebak.getPosition().getRegion().baseY + 26, zebak.getPosition().getZ(), true).flagUnmovable();
		tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + 39,
				zebak.getPosition().getRegion().baseY + 26, zebak.getPosition().getZ()));
		for (int y = 26; y < 41; y++) {
			Tile.get(zebak.getPosition().getRegion().baseX + 38,
					zebak.getPosition().getRegion().baseY + y, zebak.getPosition().getZ(), true).flagUnmovable();
			tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + 38,
					zebak.getPosition().getRegion().baseY + y, zebak.getPosition().getZ()));
		}
		for (int x = 26; x < 38; x++) {
			Tile.get(zebak.getPosition().getRegion().baseX + x,
					zebak.getPosition().getRegion().baseY + 41, zebak.getPosition().getZ(), true).flagUnmovable();
			tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + x,
					zebak.getPosition().getRegion().baseY + 41, zebak.getPosition().getZ()));
		}
		for (int x = 25; x < 30; x++) {
			Tile.get(zebak.getPosition().getRegion().baseX + x,
					zebak.getPosition().getRegion().baseY + 39, zebak.getPosition().getZ(), true).flagUnmovable();
			tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + x,
					zebak.getPosition().getRegion().baseY + 39, zebak.getPosition().getZ()));
		}
		for (int y = 27; y < 37; y++) {
			Tile.get(zebak.getPosition().getRegion().baseX + 22,
					zebak.getPosition().getRegion().baseY + y, zebak.getPosition().getZ(), true).flagUnmovable();
			tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + 22,
					zebak.getPosition().getRegion().baseY + y, zebak.getPosition().getZ()));

		}
		for (int x = 25; x < 29; x++) {
			Tile.get(zebak.getPosition().getRegion().baseX + x,
					zebak.getPosition().getRegion().baseY + 24, zebak.getPosition().getZ(), true).flagUnmovable();
			tileBlockPositions.add(new Position(zebak.getPosition().getRegion().baseX + x,
					zebak.getPosition().getRegion().baseY + 24, zebak.getPosition().getZ()));
		}
		zebak.deathEndListener = (entity, killer, killHit) -> tileBlockPositions
				.forEach(pos -> Tile.get(pos, true).unflagUnmovable());
	}

	@Override
	public Position getEnterPosition() {
		return new Position(map.convertX(3960), map.convertY(5408), 0);
	}
}
