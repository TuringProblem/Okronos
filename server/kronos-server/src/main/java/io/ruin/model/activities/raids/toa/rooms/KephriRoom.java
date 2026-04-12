package io.ruin.model.activities.raids.toa.rooms;

import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.activities.raids.toa.ToARoom;
import io.ruin.model.activities.raids.toa.bosses.kephri.Kephri;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.dynamic.DynamicMap;
import lombok.Getter;

import java.util.Objects;

public class KephriRoom extends ToARoom {
	@Getter
	NPC kephri;

	@Override
	public void buildRoom() throws DynamicMap.DynamicMapBuildException {
		map = new DynamicMap().build(InstanceMaps.KEPHRI.getRegionId(), InstanceMaps.KEPHRI.getHeight());
	}

	@Override
	public void populateRoom() {
		kephri = new NPC(11719).spawn(map.convertX(3549), map.convertY(5406), 0, 0);
		npcs.add(kephri);
		Bounds bounds = new Bounds(map.convertX(3549), map.convertY(5406), map.convertX(3553), map.convertY(5410), 0);
		bounds.forEachPos(pos -> Tile.get(pos, true).flagUnmovable());
		kephri.deathEndListener = (entity, killer, killHit) -> {
			bounds.forEachPos(pos -> Tile.get(pos, true).unflagUnmovable());
			((Kephri) getKephri().getCombat()).getSwarms().stream()
					.filter(Objects::nonNull)
					.forEach(NPC::remove);
		};
	}

	@Override
	public Position getEnterPosition() {
		return new Position(map.convertX(3534), map.convertY(5408), 0);
	}

}
