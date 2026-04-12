package io.ruin.model.activities.gauntlet;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.gauntlet.tilerotations.PhaseOneRotations;
import io.ruin.model.activities.gauntlet.tilerotations.PhaseTwoRotations;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;

import java.util.ArrayList;
import java.util.List;

public class BossTileHandler {
	Bounds bossRoomBounds;
	List<BossTile> tilesToActivate = new ArrayList<>();
	List<BossTile> bossRoomTiles = new ArrayList<>();
	DynamicMap map;
	Player player;
	NPC boss;

	int timeTilNewTiles = 0;
	boolean corrupted;

	BossTileHandler(Player player, DynamicMap map, boolean corrupted, NPC boss) {
		this.map = map;
		this.player = player;
		this.corrupted = corrupted;
		this.boss = boss;
		init();
	}

	void init() {
		bossRoomBounds = new Bounds(map.swRegion.baseX + 34, map.swRegion.baseY + 50, map.swRegion.baseX + 45, map.swRegion.baseY + 61, 1);
		bossRoomBounds.forEachPos(pos -> bossRoomTiles.add(new BossTile(player, pos, corrupted)));
	}


	public void startDangerousTilesEvent(Player player, int timeTilNewTiles) {
		this.timeTilNewTiles = timeTilNewTiles;
		bossRoomTiles.forEach(tile -> tile.resetTile());

		float bossCurrentHp = boss.getHp();
		float bossMaxHp = boss.getMaxHp();
		float bossHpPercentage = bossCurrentHp / bossMaxHp;
		int ticks = (int) (3 + (bossHpPercentage * 12));
		TileRotation tileRotation = bossHpPercentage > 0.4 ? new PhaseOneRotations() : new PhaseTwoRotations();
		Bounds bounds = tileRotation.getTilesBounds(map);

		bounds.forEachPos(pos -> {
			BossTile bossTile = findBossTileByPosition(bossRoomTiles, pos);

			if (bossTile != null) {
				bossTile.activateTile(ticks);
			}
		});
	}

	private BossTile findBossTileByPosition(List<BossTile> bossRoomBounds, Position pos) {
		for (BossTile tile : bossRoomBounds) {
			if (tile.tile.equals(pos)) {
				return tile;
			}
		}
		return null;
	}

	public void deinit() {
		for (var tile : this.bossRoomTiles) {
			tile.stop();
		}
	}
}
