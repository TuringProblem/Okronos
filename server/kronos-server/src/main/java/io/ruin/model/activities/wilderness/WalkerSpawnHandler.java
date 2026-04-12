package io.ruin.model.activities.wilderness;

import io.ruin.api.utils.Random;
import io.ruin.api.utils.StringUtils;
import io.ruin.cache.Color;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.SpawnListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.utility.Broadcast;


public class WalkerSpawnHandler {

	public static final int SUPERIOR_WALKER = 3358;

	public static final Bounds WALKER_BOUNDS = new Bounds(2971, 3572, 3378, 3955, 0);

	private static final Bounds RESTRICTED_SPAWN_1 = new Bounds(3349, 3919, 3383, 3955, 0); // TODO add islands that aren't reachable n also some small areas you can't walk in like hills next to fences, inside some walls that have gaps

	public static Position getRandomSpawn(Bounds bounds) {
		Position spawn = bounds.randomPosition();
		while (spawn.getTile().isTileFreeCheckDecor() == false && spawn.getTile().allowStandardEntrance() == false)
			spawn = bounds.randomPosition();
		return spawn;

	}

	public static final int[] WALKERS = new int[]{15002, 15001, 15000};

	public static NPC walk;

	public static void WalkerSpawner() { // As they spawn like this atm, not perfect but works for initial spawning
		for (int i = 0; i < 85; i++) {
			//spawnRandomWalker();
		}
	}

	public static NPC spawnRandomWalker() {
		if (Random.rollDie(3, 1)) {
			return new NPC(15002).spawn(getRandomSpawn(WALKER_BOUNDS), 30);
		} else if (Random.rollDie(2, 1)) {
			return new NPC(15001).spawn(getRandomSpawn(WALKER_BOUNDS), 30);
		} else {
			return new NPC(15000).spawn(getRandomSpawn(WALKER_BOUNDS), 30);
		}
	}

	public static int chancemod = 150;
	public static NPC npc;

	public static void register() {
		SpawnListener.register(WALKERS, npc -> {
			if (npc.getPosition().inBounds(WALKER_BOUNDS)) {
				npc.deathEndListener = (DeathListener.SimpleKiller) killer -> {
					if (killer.player.getInventory().hasItem(30436, 1)) {
						chancemod = 125;
					} else if (killer.player.getInventory().hasItem(30437, 1)) {
						chancemod = 100;
					} else if (killer.player.getInventory().hasItem(30438, 1)) {
						chancemod = 75;
					} else if (killer.player.getInventory().hasItem(30439, 1)) {
						chancemod = 50;
					} else {
						chancemod = 150;
					}
					if (Random.rollDie(chancemod, 1)) {
						Broadcast.WORLD.sendNews(Color.DARK_RED.wrap(("<shad>") + StringUtils.fixCaps(killer.player.getName() + " has got a superior walker at Wilderness Level: " + killer.player.wildernessLevel)));
						new NPC(SUPERIOR_WALKER).spawn(killer.player.getPosition().getX(), killer.player.getPosition().getY(), killer.player.getPosition().getZ(), 5).getCombat().setAllowRespawn(false);
					}
				};
			}
		});
	}
}