package io.ruin.model.activities.gauntlet;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;

public class BossTile {
	private static final int RED_TILE = 36047;
	private static final int BLUE_TILE = 36150;
	private static final int ORANGE_TILE = 36048;
	private static final int ORIGINAL_TILE_CORRUPTED = 36046;
	private static final int ORIGINAL_TILE_CRYSTAL = 36049;
	Position tile;
	final Player player;
	boolean corrupted;
	boolean active = false;
	boolean running = true;


	BossTile(Player player, Position tile, boolean corrupted) {
		this.player = player;
		this.tile = tile;
		this.corrupted = corrupted;
		init();
	}

	private void init() {
		World.startEvent(e -> {
			while (this.running) {
				e.delay(1);

				if (!this.player.isOnline()) {
					break;
				}

				if (this.player.getCombat().isDead()) {
					break;
				}

				process();
			}
		});
	}

	public void process() {

		if (active) {
			if (player != null) {
				if (player.getPosition().isWithinDistance(tile, 0) && player.gauntlet.inGauntlet) {
					if (!player.getCombat().isDead()) {
						if (corrupted)
							player.hit(new Hit().fixedDamage(Random.get(10, 18)).ignoreDefence().ignorePrayer());
						else
							player.hit(new Hit().fixedDamage(Random.get(6, 13)).ignoreDefence().ignorePrayer());
					}
				}
			}
		}
	}

	public void activateTile(int ticksToActive) {
		World.startEvent(e -> {
			GameObject.spawn(corrupted ? RED_TILE : BLUE_TILE, tile.getX(), tile.getY(), tile.getZ(), 22, 0);
			e.delay(ticksToActive);
			GameObject.spawn(ORANGE_TILE, tile.getX(), tile.getY(), tile.getZ(), 22, 0);
			active = true;
		});
	}

	public void resetTile() {
		GameObject.spawn(corrupted ? ORIGINAL_TILE_CORRUPTED : ORIGINAL_TILE_CRYSTAL, tile.getX(), tile.getY(), tile.getZ(),
				22, 0);
		active = false;
	}

	public void stop() {
		this.running = false;
	}
}
