package io.ruin.model.activities.tempoross;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LogoutListener;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.item.Item;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.TickDelay;

import java.util.ArrayList;

import static io.ruin.cache.ItemID.COINS_995;

public class Tempoross {

	private static int Essence = 0;
	private static int Energy = 100;
	private static int Storm_Intensity = 0;

	private boolean ended;

	private static final TickDelay startDelay = new TickDelay();

	private TemporossGameSettings settings;

	public static final int PIRATE = 10593;

	private final ArrayList<Player> players = new ArrayList<>();

	public static boolean isActive() {
		return !startDelay.isDelayed();
	}

	public static DynamicMap map;

	private void initMap() throws DynamicMap.DynamicMapBuildException {
		map = new DynamicMap().build(12078, 3);
	}

	Tempoross(TemporossGameSettings settings) {
		this.settings = settings;
	}

	private NPC addNpc(int id, Position originalTile) {
		NPC npc = new NPC(id).spawn(map.convertX(originalTile.getX()), map.convertY(originalTile.getY()), 0);
		map.addNpc(npc);
		return npc;
	}

	public static NPC SpiritPoolNorth;
	public static NPC SpiritPoolSouth;
	public static NPC AmmunitionCrate1;
	public static NPC AmmunitionCrate2;
	public static NPC AmmunitionCrate3;
	public static NPC AmmunitionCrate4;
	public static NPC LightningCloud;
	public static NPC Tempoross;
	public static Object Cannon1;
	public static Object Cannon2;
	public static Object Cannon3;
	public static Object Cannon4;

	public static int cannon1ammo = 0;
	public static int cannon2ammo = 0;
	public static int cannon3ammo = 0;
	public static int cannon4ammo = 0;

	public void spawnNpcs() {
		addNpc(10568, new Position(3051, 2995, 0));// Fishing Spots
		addNpc(10568, new Position(3048, 3002, 0));// Fishing Spots
		addNpc(10568, new Position(3038, 2996, 0));// Fishing Spots
		addNpc(10568, new Position(3036, 2963, 0));// Fishing Spots
		addNpc(10568, new Position(3046, 2955, 0));// Fishing Spots
		addNpc(10568, new Position(3048, 2957, 0));// Fishing Spots
		SpiritPoolSouth = addNpc(10570, new Position(3046, 2971, 0));// Spirit Pool South
		SpiritPoolNorth = addNpc(10570, new Position(3046, 2981, 0));// Spirit Pool North
		LightningCloud = addNpc(10580, new Position(3047, 2992, 0));// Lightning Cloud
		AmmunitionCrate1 = addNpc(10576, new Position(3038, 2978, 0));
		AmmunitionCrate2 = addNpc(10576, new Position(3038, 2976, 0));
		AmmunitionCrate3 = addNpc(10576, new Position(3056, 2978, 0));
		AmmunitionCrate4 = addNpc(10576, new Position(3056, 2976, 0));
		Tempoross = addNpc(10574, new Position(3043, 2973, 0));
	}

	// Tempoross ID Start of instnace = 10574
	// Tempoross Attack ID = 10572
	// SpiritPool Attack ID 10571
	public void startTempoross() {
		World.startEvent(event -> {
			CannonAttack();
			if (Tempoross.getId() == 10574 && Essence != 100) {
				Essence += Random.get(1, 20);
				System.out.println("[Tempoross Essence] + " + Essence);
			}

			if (Essence >= 100 && Tempoross.getId() == 10574) {
				Tempoross.transform(10572);
				Tempoross.animate(8901);
				SpiritPoolNorth.transform(10571);
				SpiritPoolSouth.transform(10571);
				event.delay(3);
				Essence = 0;
				if (Random.rollPercent(25)) {
					Tempoross.animate(8900);
					event.delay(2);
					stunCannons();
				}
			}
			if (Energy <= 0) {
				end(false);
			}

		});
	}

	public static void CannonAttack() {
		GameObject.forObj(41238, map.convertX(3040), map.convertY(2979), 0, gameObject -> {
			if (cannon1ammo > 0) {
				World.startEvent(event -> {
					gameObject.animate(8871);
					cannon1ammo--;
					event.delay(2);
					Essence -= 2;
					System.out.println("[Tempoross Essense] - " + Essence);
				});
			}
		});
		GameObject.forObj(41239, map.convertX(3040), map.convertY(2974), 0, gameObject -> {
			if (cannon2ammo > 0) {
				World.startEvent(event -> {
					gameObject.animate(8871);
					cannon2ammo--;
					event.delay(2);
					Essence -= 2;
					System.out.println("[Tempoross Essense] - " + Essence);
				});

			}
		});
		GameObject.forObj(41240, map.convertX(3054), map.convertY(2980), 0, gameObject -> {
			if (cannon3ammo > 0) {
				World.startEvent(event -> {
					gameObject.animate(8871);
					cannon3ammo--;
					event.delay(2);
					Essence -= 2;
					System.out.println("[Tempoross Essense] - " + Essence);
				});

			}
		});
		GameObject.forObj(41241, map.convertX(3054), map.convertY(2975), 0, gameObject -> {
			if (cannon4ammo > 0) {
				World.startEvent(event -> {
					gameObject.animate(8871);
					cannon4ammo--;
					event.delay(2);
					Essence -= 2;
					System.out.println("[Tempoross Essense] - " + Essence);
				});
			}
		});
	}

	public static void HarpoonAttack(int MinDamage, int MaxDamage) {
		if (Tempoross.getId() == 10572) {
			Energy -= Random.get(MinDamage, MaxDamage);
		}
	}

	public void start(ArrayList<Player> participants) {
		try {
			initMap();
		} catch (DynamicMap.DynamicMapBuildException e) {
			for (var p : participants) {
				p.sendMessage("Unable to build dynamic map.");
			}
			return;
		}
		spawnNpcs();
		players.addAll(participants);
		players.forEach(player -> {
			player.temporossGame = this;
			if (Random.get() <= 50) {
				player.getMovement().teleport(map.convertX(3036), map.convertY(2981));
			} else {
				player.getMovement().teleport(map.convertX(3059), map.convertY(2975));
			}
			player.currentDynamicMap = map;
			player.inDynamicMap = true;
			player.teleportListener = p -> {
				clearActivityOf(p);
				return true;
			};
			player.deathEndListener = (entity, killer, hit) -> {
				if (entity instanceof Player) {
					Player plr = (Player) entity;
					leave(plr);
				}
			};
			player.logoutListener = new LogoutListener().onLogout(this::leave);
		});
		World.startEvent(event -> {
			while (true) {
				if (startDelay.isDelayed()) {
					event.delay(startDelay.remaining());
				}
				startTempoross();
				event.delay(16);
			}
		});

	}

	// Essence recharges
	// Energy is health
	// StormIntensity = 100% then cast waveAttack

	private static void stunCannons() {
		if (Energy >= 10) {
			if (Random.rollPercent(25)) {
				GameObject.forObj(41238, map.convertX(3040), map.convertY(2979), 0, gameObject -> {
					World.startEvent(event -> {
						gameObject.setId(41242);
						event.delay(2);
					});
				});
			}
			if (Random.rollPercent(25)) {
				GameObject.forObj(41239, map.convertX(3040), map.convertY(2974), 0, gameObject -> {
					World.startEvent(event -> {
						gameObject.setId(41242);
						event.delay(2);
					});
				});
			}
			if (Random.rollPercent(25)) {
				GameObject.forObj(41240, map.convertX(3054), map.convertY(2980), 0, gameObject -> {
					World.startEvent(event -> {
						gameObject.setId(41242);
						event.delay(2);
					});
				});
			}
			if (Random.rollPercent(25)) {
				GameObject.forObj(41241, map.convertX(3054), map.convertY(2975), 0, gameObject -> {
					World.startEvent(event -> {
						gameObject.setId(41242);
						event.delay(2);
					});
				});
			}
		}
	}

	private static void spawnClouds() {
		if (Energy >= 10) {

		}
	}

	private void end(boolean failed) {
		if (!failed) {
			players.forEach(p -> {
				// int rewardedPoints = Config.TEMPOROSS_POINTS.get(p);
				p.lock();
				Position tile = settings.exitTile();
				p.getMovement().teleport(tile.getX(), tile.getY());
				p.unlock();
				p.temporossWins++;
				// Need to work out the math for that do I just divide the points by 1000, if >
				// 0
				// 1000 points = 1 reward
				VarPlayerRepository.TEMPOROSS_REWARDS.set(p, 2);
				DailyTasks.handleTaskDecrement(p, "tempoross");
				p.getInventory().addOrDrop(new Item(COINS_995, Random.get(3000, 7000)));
				clearActivityOf(p);
			});
		} else if (failed) {
			players.forEach(p -> {
				p.lock();
				p.getMovement().teleport(3137, 2840, 0);
				p.unlock();
				clearActivityOf(p);
			});
		}

		players.clear();
		map.destroy(); // For Instances
		map = null;
		ended = true;
	}

	private void clearActivityOf(Player player) {
		player.closeInterface(ToplevelComponent.OVERLAY);
		player.temporossGame = null;
		player.temporossActivityScore = 0;
		player.teleportListener = null;
		player.deathEndListener = null;
		player.logoutListener = null;
		player.joinedTempoross = false;
		player.temporossTether = false;
		player.getCombat().restore();
	}

	public void leave(Player player) {
		player.currentDynamicMap = null;
		player.inDynamicMap = false;
		clearActivityOf(player);
		players.remove(player);
		player.getMovement().teleport(settings.exitTile());
	}

	public boolean ended() {
		return ended;
	}
}
