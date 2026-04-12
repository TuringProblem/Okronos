package io.ruin.model.activities.nightmarezone;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.var.VarPlayerRepository;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.List;

@Slf4j
public final class NightmareZoneDream {

	// Dream vial:
	// Proceed.
	// Not just now.

	private static final Position START = new Position(2275, 4680, 0);

	private static final Position EXIT = new Position(2608, 3115, 0);

	/*
	 * As far as I know the NMZ monsters can spawn pretty much anywhere in the arena, although the arena is not a perfect
	 * square.
	 */
	private static final Bounds SPAWN_BOUNDS = new Bounds(2256, 4680, 2287, 4711, 0);

	private static final List<Integer> ABSORPTION_POTION = Arrays.asList(11734, 11735, 11736, 11737);

	private static final int[] NORMAL_MONSTERS = { // Add Attributes
			102, // Rock Crabs
			5935, // Sand Crabs
			5816, // Yaks
			2791, // Cows
			695, // Bandits
			6386, // Moss Giant
			421, // Rock Slugs
			480, // Cave Slime
			658, // Goblins
			4501, // Brine Rats
			6357, // Black Demon
	};

	private static final int[] HARD_MONSTERS = { // Add Attributes
			6740, // Shade
			6741, // Zombie MISSING COMBAT SCRIPT
			2527, // Ghost
			2515, // Ankou
			2916, // Waterfiend
			1539, // Skeleton Warlord
			5565, // Barbarian Spirit
			1541, // Skeleton Thug
			249, // Red Dragon
			4005, // Dark Beast
			6295, // Black Demon
			498, // Smoke Devils
	};

	private Player player;

	private final NightmareZoneDreamDifficulty difficulty;

	private DynamicMap map;

	private int npcsRemaining;

	private int rewardPointsGained;

	public NightmareZoneDream(Player player, NightmareZoneDreamDifficulty difficulty)
			throws DynamicMap.DynamicMapBuildException {
		map = createMap();
		this.player = player;
		this.difficulty = difficulty;
		player.absorptionPoints = 0;

		player.deathEndListener = (DeathListener.Simple) () -> {
			player.getMovement().teleport(EXIT);
			player.currentDynamicMap = null;
			player.inDynamicMap = false;
			player.getPacketSender().fadeIn();
			player.absorptionPoints = 0;
			player.sendMessage("You wake up feeling refreshed.");
			player.nmzRewardPoints += rewardPointsGained;
			player.sendMessage(Color.DARK_GREEN.wrap("You have earned " + NumberUtils.formatNumber(rewardPointsGained)
					+ " reward points. New total: " + NumberUtils.formatNumber(player.nmzRewardPoints)));
			player.set("nmz", null);
			player.teleportListener = null;
			player.deathEndListener = null;
		};

		player.teleportListener = p -> {
			p.sendMessage("Drink from the vial at the south of the arena to wake up.");
			return false;
		};
	}

	public static void enter(Player player, NightmareZoneDreamDifficulty difficulty) {
		try {
			var dream = new NightmareZoneDream(player, difficulty);
			dream.enter();
		} catch (DynamicMap.DynamicMapBuildException e) {
			player.sendMessage("Unable to create dynamic map.");
		}
	}

	public static void check(Player player, Hit hit) {
		if (player.absorptionPoints > 0 && player.get("nmz") != null) {
			if (hit.damage > 0 && !hit.absorptionIgnored) {
				if (hit.damage > player.getHp())
					player.absorptionPoints = Math.max(0, player.absorptionPoints - player.getHp());
				else
					player.absorptionPoints = Math.max(0, player.absorptionPoints - hit.damage);
				hit.block();
				VarPlayerRepository.NMZ_ABSORPTION.set(player, player.absorptionPoints);
				player.sendMessage(
						Color.DARK_GREEN.wrap("You now have " + player.absorptionPoints + " hitpoints of damage absorption left."));
			}
		}
	}

	public void enter() {
		player.set("nmz", this);
		prepareMap();

		World.startEvent(event -> {
			player.lock();
			player.getPacketSender().fadeOut();
			event.delay(2);
			player.getMovement().teleport(map.convertPosition(START));
			player.currentDynamicMap = map;
			player.inDynamicMap = true;
			event.delay(1);
			player.getPacketSender().fadeIn();
			prepareInterface();
			player.sendMessage("Welcome to The Nightmare Zone.");
			player.unlock();

			event.delay(30);
			spawnMonsters();
		});

	}

	private static DynamicMap createMap() throws DynamicMap.DynamicMapBuildException {
		DynamicMap arena = new DynamicMap();
		arena.build(9033, 0);
		return arena;
	}

	private void prepareMap() {
		/* Remove KBD stalagmite, add dream potion */
		GameObject potion = new GameObject(26276, map.convertX(2276), map.convertY(4679), 0, 10, 0);
		Tile.getObject(12576, map.convertX(2276), map.convertY(4679), 0).setId(26267);
		Tile.get(map.convertX(2276), map.convertY(4679), 0).addObject(potion.spawn());

		/* Remove KBD lever */
		Tile.getObject(1817, map.convertX(2271), map.convertY(4680), 0).remove();
	}

	private void prepareInterface() {
		player.openInterface(ToplevelComponent.WILDERNESS_OVERLAY, 202);

		// This is a hash of the arena's southwestern-most tile. This is presumably used
		// by the client to differentiate between KBD lair
		int hash = map.convertY(4680) + (map.convertX(2256) << 14);

		// [clientscript,nzone_game_overlay].cs2 -> tile hash does not seem to matter,
		// empty string at end is some sort of unused in-game notification string
		player.getPacketSender().sendClientScript(255, "cs", hash, "");
	}

	public static int npccount;

	private void spawnMonsters() {
		if (difficulty == NightmareZoneDreamDifficulty.HARD) {
			npccount = 10;
		} else {
			npccount = 4;
		}
		for (int i = 0; i < npccount; i++) {
			NPC npc = new NPC(randomMonster());
			if (npc.getCombat() == null) {
				log.error("Tried to spawn monster without combat: " + npc.getId());
				continue;
			}

			Position spawn = map.convertPosition(SPAWN_BOUNDS.randomPosition());
			npc.spawn(spawn);
			map.addNpc(npc);
			npc.face(player);

			npc.deathEndListener = (DeathListener.Simple) () -> {
				rewardPointsGained += Random.get(3000, 5000) * (difficulty == NightmareZoneDreamDifficulty.NORMAL ? 1.0 : 1.85);
				VarPlayerRepository.NMZ_POINTS.set(player, rewardPointsGained);
				npcsRemaining--;
				map.removeNpc(npc);
				if (npcsRemaining == 0) {
					spawnMonsters();
				}
			};

			npc.getCombat().setAllowRespawn(false);
			npc.targetPlayer(player, false);
			npc.attackTargetPlayer();

			npcsRemaining++;
		}
	}

	private int randomMonster() {
		return Random.get(difficulty == NightmareZoneDreamDifficulty.NORMAL ? NORMAL_MONSTERS : HARD_MONSTERS);
	}

	private void end(boolean logout) {
		player.nmzRewardPoints += rewardPointsGained;

		if (!logout) {
			player.getPacketSender().fadeIn();
			player.sendMessage("You wake up feeling refreshed.");
			player.sendMessage(Color.DARK_GREEN.wrap("You have earned " + NumberUtils.formatNumber(rewardPointsGained)
					+ " reward points. New total: " + NumberUtils.formatNumber(player.nmzRewardPoints)));
		}

		player.set("nmz", null);
		player.absorptionPoints = 0;
		player.teleportListener = null;
		player.deathEndListener = null;
		dispose();
	}

	private void dispose() {
		player = null;
		map.destroy();
		map = null;
	}

	public static void register() {
		ABSORPTION_POTION.forEach(id -> {
			ItemAction.registerInventory(id, "drink", NightmareZoneDream::drinkAbsorption);
		});
		ObjectAction.register(26276, 1, (player, obj) -> {
			player.getMovement().teleport(EXIT);
			player.currentDynamicMap = null;
			player.inDynamicMap = false;
			NightmareZoneDream dream = player.get("nmz");
			dream.end(false);
		});
	}

	public static void drinkAbsorption(Player player, Item item) {
		if (player.get("nmz") == null) {
			player.sendMessage("Absorption Potions can only be drunk in your dreams...");
			return;
		}
		if (player.absorptionPoints >= 1000) {
			player.sendMessage("You can't absorb anymore.");
			return;
		}
		if (player.potDelay.isDelayed())
			return;
		item.setId(item.getId() == 11737 ? 229 : item.getId() + 1);
		player.animate(829);
		player.privateSound(2401);
		player.absorptionPoints = player.absorptionPoints + 50;
		VarPlayerRepository.NMZ_ABSORPTION.set(player, player.absorptionPoints);
		player.sendMessage(
				Color.DARK_GREEN.wrap("You now have " + player.absorptionPoints + " hitpoints of damage absorption left."));
		player.potDelay.delay(1);
	}
}
