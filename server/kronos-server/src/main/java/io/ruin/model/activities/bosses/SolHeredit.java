package io.ruin.model.activities.bosses;

import discord.webhooks.notifications.RareDropHook;
import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.combat.Hit;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.Broadcast;
import io.ruin.utility.TickDelay;
import io.ruin.utility.Utils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SolHeredit extends NPCCombat {


	public static final LootTable table = new LootTable().addTable(1,
		new LootItem(ItemID.COINS_995, 1000000, 10000000, 30),
		new LootItem(ItemID.PERK_POINT_SCROLL, 1, 3, 15),
		new LootItem(ItemID.GLISTENING_MINERALS, 9, 17, 24),
		new LootItem(ItemID.SHINY_MINERALS, 16, 24, 24),
		new LootItem(ItemID.COINS_995, 50000000, 150000000, 4),
		new LootItem(ItemID.DROP_RATE_SCROLL, 1, 2, 11),
		new LootItem(ItemID.DOUBLE_DROP_SCROLL, 1, 2, 11),
		new LootItem(ItemID.ADAMANTITE_BAR + 1, 70, 240, 30),
		new LootItem(ItemID.RUNITE_BAR + 1, 70, 240, 30),
		new LootItem(ItemID.MAGIC_SEED, 2, 6, 30),
		new LootItem(ItemID.YEW_SEED, 3, 6, 32),
		new LootItem(ItemID.UNCUT_ONYX, 1, 1, 16),
		new LootItem(ItemID.CRYSTAL_KEY, 5, 12, 25),
		new LootItem(ItemID.POINT_MYSTERY_BOX, 1, 2, 25),
		new LootItem(ItemID.INSTANCE_TOKEN, 1, 3, 25),
		new LootItem(ItemID.DRAGON_DART, 250, 400, 25),
		new LootItem(ItemID.DRAGON_BOLTS, 250, 400, 25),
		new LootItem(ItemID.UNCUT_RUBY + 1, 250, 400, 25),
		new LootItem(ItemID.UNCUT_DIAMOND + 1, 250, 400, 25),
		new LootItem(ItemID.UNCUT_DRAGONSTONE + 1, 250, 400, 25),
		new LootItem(ItemID.CANNONBALL, 2000, 40),
		new LootItem(ItemID.SUNFIRE_SPLINTERS, 2000, 40),
		new LootItem(ItemID.DIZANAS_QUIVER_UN, 1, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.DONATOR_MYSTERY_BOX, 1, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.SUNFIRE_FANATIC_HELM, 1, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.SUNFIRE_FANATIC_CUIRASS, 1, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.SUNFIRE_FANATIC_CHAUSSES, 1, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.OSMUMTENS_SPIRIT, 1, 1, 1).broadcast(Broadcast.GLOBAL)

	);
	Bounds bossBounds;
	List<Position> moltenPositions = new ArrayList<>();
	List<Position> currentMoltenPositions = new ArrayList<>();

	public boolean usedNonSolWeapon = false;
	public boolean damagedPlayer = false;
	public boolean playerRan = false;

	@Override
	public void init() {
		npc.deathEndListener = (entity, killer, killHit) -> {
			rewardPlayer(killer.player);
			npc.remove();
		};
		bossBounds = new Bounds(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 26, npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 43, 0);
		npc.hitListener = new HitListener().preDamage(this::preDamage).postDamage(this::postDamage);
	}

	private void preDamage(Hit hit) {
		if (hit.damage > 120)
			hit.damage = 120;
	}

	private boolean usingSpear(Player player) {
		return VarPlayerRepository.WEAPON_TYPE.get(player) == 24 || VarPlayerRepository.WEAPON_TYPE.get(player) == 15 || VarPlayerRepository.WEAPON_TYPE.get(player) == 12;
	}


	private void postDamage(Hit hit) {
		if (hit.attacker != null && hit.attackStyle != null) {
			if (!hit.attackStyle.isMelee())
				usedNonSolWeapon = true;
			if (hit.attacker.isPlayer() && hit.attacker.player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
				if (!usingSpear(hit.attacker.player))
					usedNonSolWeapon = true;
			} else usedNonSolWeapon = true;
		}
		if (phase < 2)
			npc.getCombat().getInfo().attack_ticks = 10;

		if (Objects.isNull(target.player))
			return;

		if (getHpPercentage() < 90 && phase == 1) {
			phase = 2;
			spawnMoltenSand(target.player);
			spawnPrism(target.player);
			npc.forceText("Not bad. Let's try something else...");

		} else if (getHpPercentage() < 75 && phase == 2) {
			phase = 3;
			spawnMoltenSand(target.player);
			spawnPrism(target.player);
			npc.getCombat().getInfo().attack_ticks = 8;
			npc.forceText("Impressive. Let's see how you handle this...");
		} else if (getHpPercentage() < 50 && phase == 3) {
			phase = 4;
			spawnMoltenSand(target.player);
			spawnPrism(target.player);
			npc.forceText("You can't win!");
		} else if (getHpPercentage() < 25 && phase == 4) {
			phase = 5;
			spawnMoltenSand(target.player);
			spawnPrism(target.player);
			npc.forceText("Ralos guides my hand!");
		} else if (getHpPercentage() <= 10 && phase == 5) {
			phase = 6;
			npc.forceText("LET'S END THIS!");
			spawnMoltenSand(target.player);
		}
	}

	private int getHpPercentage() {
		return (npc.getHp() * 100) / npc.getMaxHp();
	}

	@Override
	public void follow() {
		follow(2);
	}

	int phase = 1;

	int lastAttack = 0;

	boolean canAttack = true;
	TickDelay comboDelay = new TickDelay();

	@Override
	public boolean attack() {
		if (!canAttack)
			return false;
		if (!timerStarted) {
			timerStarted = true;
			target.player.solHereditTimer = new ActivityTimer();
		}
		if (phase > 1) {
			if (Random.get(10) == 0 && comboDelay.remaining() < 1) {
				comboDelay.delay(30);
				canAttack = false;
				comboAttack(target.player);
				return true;
			}
		}
		if (Random.get(1) == 0) {
			if (lastAttack == 1) {
				lastAttack = 0;
				shieldPattern(target.player, 5);
				return true;
			} else {
				lastAttack = 1;
				shieldPattern(target.player, 4);
				return true;
			}

		} else {
			if (lastAttack != 4) {
				lastAttack = 4;
				spearSpecialOne(target.player);
			} else {
				lastAttack = 3;
				spearSpecialTwo(target.player);
			}
		}
		return true;
	}

	int moltenTicks = 0;

	boolean timerStarted = false;

	@Override
	public void process() {
		if (target != null && VarPlayerRepository.RUNNING.get(target.player) == 1)
			playerRan = true;

		if (prismTicksUntilLight > 0) {
			prismTicksUntilLight--;
		}
		if (phase == 6) {
			if (moltenTicks++ == 10) {
				moltenTicks = 0;
				if (target != null)
					spawnMoltenSand(target.player);
			}
		}
		for (Position pos : moltenPositions) {
			World.sendGraphics(-1, 0, 0, pos);
			World.sendGraphics(2510, 0, 0, pos);
			npc.localPlayers().forEach(p -> {
				if (pos.distance(p.getPosition()) < 1 && npc.getHp() > 0) {
					p.hit(new Hit().randDamage(6, 8));
					damagedPlayer = true;
				}
			});
		}
	}


	private void shieldPattern(Player player, int safeTileCount) {
		Position startTile = npc.getCentrePosition().copy();
		List<Position> safeTiles = new ArrayList<>();
		List<Position> unsafeTiles = new ArrayList<>();

		for (int x = startTile.getX() - safeTileCount; x <= startTile.getX() + safeTileCount; x++) {
			for (int y = startTile.getY() - safeTileCount; y <= startTile.getY() + safeTileCount; y++) {
				Position pos = new Position(x, y, 0);
				if (startTile.distance(pos) == safeTileCount) {
					safeTiles.add(pos);
				}
			}
		}
		bossBounds.forEachPos(tile -> {
			boolean safe = safeTiles.stream().anyMatch(pos -> tile.distance(pos) < 1);
			if (!safe) {
				unsafeTiles.add(tile);
			}
		});
		npc.animate(10885);
		World.startEvent(event -> {
			event.setCancelCondition(this::targetIsNotInBossRegion);
			event.delay(3);
			for (Position pos : unsafeTiles) {
				World.sendGraphics(2670, 0, pos.distance(startTile), pos);
				if (player.getPosition().distance(pos) < 1 && npc.getHp() > 0) {
					player.hit(new Hit().randDamage(35, 45));
					damagedPlayer = true;
				}
			}
		});
	}

	public static Direction getDirection(Position src, Position dest) {
		// Center of the NPC's area (assuming a 5x5 area for the center calculation)
		int npcCenterX = src.getX() + 2;
		int npcCenterY = src.getY() + 2;

		// Calculate differences
		int deltaX = dest.getX() - npcCenterX;
		int deltaY = dest.getY() - npcCenterY;

		// Calculate absolute values
		int absDeltaX = Math.abs(deltaX);
		int absDeltaY = Math.abs(deltaY);

		// Determine primary direction
		if (deltaX == -2 && deltaY == 2) {
			return Direction.NORTH;
		}
		if (deltaX == -1 && deltaY == 2) {
			return Direction.NORTH;
		}
		if (deltaX == 1 && deltaY == -1) {
			return Direction.NORTH_EAST;
		}
		if (deltaX == 2 && deltaY == 1) {
			return Direction.NORTH_EAST;
		}
		if (deltaX == -4 && deltaY == 1) {
			return Direction.NORTH_WEST;
		}
		if (deltaX == -5 && deltaY == 1) {
			return Direction.NORTH_WEST;
		}
		if (deltaX == -5 && deltaY == 0) {
			return Direction.NORTH_WEST;
		}
		if (deltaX == -6 && deltaY == 0) {
			return Direction.NORTH_WEST;
		}
		if (deltaX == -6 && deltaY == -5) {
			return Direction.SOUTH_WEST;
		}
		if (deltaX == -5 && deltaY == -6) {
			return Direction.SOUTH_WEST;
		}
		if (deltaX == -4 && deltaY == -5) {
			return Direction.SOUTH_WEST;
		}
		if (deltaX == -6 && deltaY == -6) {
			return Direction.SOUTH_WEST;
		}
		if (deltaX == -5 && deltaY == -4) {
			return Direction.SOUTH_WEST;
		}
		if (deltaX == -6 && deltaY == -4) {
			return Direction.SOUTH_WEST;
		}
		if (deltaX == -5 && deltaY == -5) {
			return Direction.SOUTH_WEST;
		}
		if (deltaX == 0 && deltaY == -5) {
			return Direction.SOUTH_EAST;
		}
		if (deltaX == 1 && deltaY == -6) {
			return Direction.SOUTH_EAST;
		}
		if (deltaX == 1 && deltaY == -5) {
			return Direction.SOUTH_EAST;
		}
		if (deltaX == 2 && deltaY == -6) {
			return Direction.SOUTH_EAST;
		}
		if (deltaX == 1 && deltaY == -4) {
			return Direction.SOUTH_EAST;
		}
		if (deltaX == 2 && deltaY == -5) {
			return Direction.SOUTH_EAST;
		}
		if (deltaX == 2 && deltaY == -4) {
			return Direction.SOUTH_EAST;
		}
		if (deltaX == 0 && deltaY == -6) {
			return Direction.SOUTH_EAST;
		}
		if (deltaX == -5 && deltaY == 2) {
			return Direction.NORTH_WEST;
		}
		if (deltaX == -6 && deltaY == 1) {
			return Direction.NORTH_WEST;
		}
		if (deltaX == -6 && deltaY == 2) {
			return Direction.NORTH_WEST;
		}
		if (deltaX == -4 && deltaY == 2) {
			return Direction.NORTH_WEST;
		}
		if (deltaX == 2 && deltaY == 2) {
			return Direction.NORTH_EAST;
		}
		if (deltaX == 1 && deltaY == 0) {
			return Direction.NORTH_EAST;
		}
		if (deltaX == 1 && deltaY == 2) {
			return Direction.NORTH_EAST;
		}
		if (deltaX == 0 && deltaY == 1) {
			return Direction.NORTH_EAST;
		}
		if (deltaX == 0 && deltaY == 2) {
			return Direction.NORTH_EAST;
		}
		if (deltaX == 1 && deltaY == 1) {
			return Direction.NORTH_EAST;
		}
		if (deltaX == 2 && deltaY == 0) {
			return Direction.NORTH_EAST;
		}
		if (deltaX == 2 && deltaY == -2) {
			return Direction.EAST;
		}
		if (absDeltaX == 1 && absDeltaY == 1) {
			return Direction.NORTH;
		}
		if (absDeltaX == 1 && absDeltaY == 2) {
			return Direction.EAST;
		}
		if (absDeltaX == 2 && absDeltaY == 1) {
			return Direction.NORTH;
		}
		if (absDeltaX == 2 && absDeltaY == 3) {
			return Direction.EAST;
		}
		if (absDeltaX == 1 && absDeltaY == 3) {
			return Direction.EAST;
		}
		if (absDeltaX >= 1 && absDeltaX <= 4 && absDeltaY >= 1 && absDeltaY < 3) {
			return Direction.NORTH;
		}
		if (absDeltaX > absDeltaY) {
			// Horizontal movement
			if (deltaX > 0) {
				return Direction.EAST;
			} else {
				return Direction.WEST;
			}
		} else if (absDeltaY > absDeltaX) {
			// Vertical movement
			if (deltaY > 0) {
				return Direction.NORTH;
			} else {
				return Direction.SOUTH;
			}
		} else {
			// Handle diagonal cases if distances are equal
			if (deltaX > 0 && deltaY > 0) {
				return Direction.SOUTH_EAST;
			} else if (deltaX > 0 && deltaY < 0) {
				return Direction.NORTH_EAST;
			} else if (deltaX < 0 && deltaY > 0) {
				return Direction.SOUTH_WEST;
			} else if (deltaX < 0 && deltaY < 0) {
				return Direction.NORTH_WEST;
			}
		}

		// Default to a safe direction if none of the above matches
		return Direction.NORTH;
	}


	private Direction getDirection(int deltaX, int deltaY) {
		// Define thresholds for determining direction
		final int DIAGONAL_THRESHOLD = 2;

		if (Math.abs(deltaX) > Math.abs(deltaY)) {
			// More horizontal movement
			if (deltaX > 0) {
				return Direction.EAST;
			} else {
				return Direction.WEST;
			}
		} else if (Math.abs(deltaY) > Math.abs(deltaX)) {
			// More vertical movement
			if (deltaY > 0) {
				return Direction.SOUTH;
			} else {
				return Direction.NORTH;
			}
		} else {
			// Handle diagonal cases
			if (Math.abs(deltaX) <= DIAGONAL_THRESHOLD && Math.abs(deltaY) <= DIAGONAL_THRESHOLD) {
				if (deltaX > 0 && deltaY > 0) {
					return Direction.SOUTH_EAST;
				} else if (deltaX > 0 && deltaY < 0) {
					return Direction.NORTH_EAST;
				} else if (deltaX < 0 && deltaY > 0) {
					return Direction.SOUTH_WEST;
				} else if (deltaX < 0 && deltaY < 0) {
					return Direction.NORTH_WEST;
				}
			} else {
				// Fall back to primary directions
				if (deltaX > 0) {
					return Direction.EAST;
				} else if (deltaX < 0) {
					return Direction.WEST;
				} else if (deltaY > 0) {
					return Direction.SOUTH;
				} else {
					return Direction.NORTH;
				}
			}
		}

		// Default to a safe direction
		return Direction.NORTH;
	}


	private void spearSpecialOne(Player player) {
		Position startTile = npc.getCentrePosition().copy();
		List<Position> dangerTiles = new ArrayList<>();

		// Determine direction from NPC to player
		Direction direction = getDirection(npc.getCentrePosition(), player.getPosition());

		int baseWidth = 5; // Base square width
		int extendLength = 8; // Length of the extended part

		switch (direction) {
			case EAST:
				// Base 5x5 square
				for (int y = startTile.getY() - 2; y <= startTile.getY() + 2; y++) {
					for (int x = startTile.getX() - 2; x <= startTile.getX() + 2; x++) {
						dangerTiles.add(new Position(x, y, 0));
					}
					// Extend to the right (East)
					for (int x = startTile.getX() + 3; x <= startTile.getX() + 3 + extendLength; x++) {
						if (y == startTile.getY() - 1 || y == startTile.getY() + 1) {
							dangerTiles.add(new Position(x, y, 0));
						}
					}
				}
				break;

			case NORTH:
				// Base 5x5 square
				for (int x = startTile.getX() - 2; x <= startTile.getX() + 2; x++) {
					for (int y = startTile.getY() - 2; y <= startTile.getY() + 2; y++) {
						dangerTiles.add(new Position(x, y, 0));
					}
					// Extend upwards (North)
					for (int y = startTile.getY() + 3; y <= startTile.getY() + 3 + extendLength; y++) {
						if (x == startTile.getX() - 1 || x == startTile.getX() + 1) {
							dangerTiles.add(new Position(x, y, 0));
						}
					}
				}
				break;

			case WEST:
				// Base 5x5 square
				for (int y = startTile.getY() - 2; y <= startTile.getY() + 2; y++) {
					for (int x = startTile.getX() - 2; x <= startTile.getX() + 2; x++) {
						dangerTiles.add(new Position(x, y, 0));
					}
					// Extend to the left (West)
					for (int x = startTile.getX() - 3; x >= startTile.getX() - 3 - extendLength; x--) {
						if (y == startTile.getY() - 1 || y == startTile.getY() + 1) {
							dangerTiles.add(new Position(x, y, 0));
						}
					}
				}
				break;

			case SOUTH:
				// Base 5x5 square
				for (int x = startTile.getX() - 2; x <= startTile.getX() + 2; x++) {
					for (int y = startTile.getY() - 2; y <= startTile.getY() + 2; y++) {
						dangerTiles.add(new Position(x, y, 0));
					}
				}
				// Extend downwards (South)
				for (int x = startTile.getX() - 2; x <= startTile.getX() + 2; x++) {
					for (int y = startTile.getY() - 3; y >= startTile.getY() - 3 - extendLength; y--) {
						if (x == startTile.getX() - 1 || x == startTile.getX() + 1) {
							dangerTiles.add(new Position(x, y, 0));
						}
					}
				}
				break;

			case NORTH_EAST:
				addDangerTiles(dangerTiles, startTile, new int[][]{
					{0, 0}, {-1, -1}, {1, -1}, {0, -2}, {-1, 1},
					{-2, 0}, {-3, 1}, {-2, -2}, {-2, 2}, {-1, -3}, {0, -4}, {2, -2}, {3, -1},
					{2, 0}, {1, 1}, {0, 2}, {-1, 3}, {0, 4},
					{1, 3}, {2, 2}, {3, 1}, {4, 0}, {4, 2},
					{5, 3}, {6, 4}, {7, 5}, {2, 4}, {3, 5},
					{4, 6}, {5, 7}, {1, -3}, {-3, -1}, {-4, 0}
				});
				break;


			case NORTH_WEST:
				addDangerTiles(dangerTiles, startTile, new int[][]{
					{0, 0}, {1, -1}, {-1, -1}, {0, -2}, {1, 1},
					{2, 0}, {3, 1}, {2, -2}, {2, 2}, {1, -3}, {0, -4}, {-2, -2}, {-3, -1},
					{-2, 0}, {-1, 1}, {0, 2}, {1, 3}, {0, 4},
					{-1, 3}, {-2, 2}, {-3, 1}, {-4, 0}, {-4, 2},
					{-5, 3}, {-6, 4}, {-7, 5}, {-2, 4}, {-3, 5},
					{-4, 6}, {-5, 7}, {-1, -3}, {3, -1}, {4, 0}
				});
				break;


			case SOUTH_WEST:
				addDangerTiles(dangerTiles, startTile, new int[][]{
					{0, 0}, {1, 1}, {-1, 1}, {0, 2}, {1, -1},
					{2, 0}, {3, -1}, {2, 2}, {2, -2}, {1, 3}, {0, 4}, {-2, 2}, {-3, 1},
					{-2, 0}, {-1, -1}, {0, -2}, {1, -3}, {0, -4},
					{-1, -3}, {-2, -2}, {-3, -1}, {-4, 0}, {-4, -2},
					{-5, -3}, {-6, -4}, {-7, -5}, {-2, -4}, {-3, -5},
					{-4, -6}, {-5, -7}, {-1, 3}, {3, 1}, {4, 0}
				});
				break;


			case SOUTH_EAST:
				// Specific tiles for South-East diagonal extension
				addDangerTiles(dangerTiles, startTile, new int[][]{
					{0, 0}, {-1, 1}, {1, 1}, {0, 2}, {-1, -1},
					{-2, 0}, {-3, -1}, {-2, 2}, {-2, -2}, {-1, 3}, {0, 4}, {2, 2}, {3, 1},
					{2, 0}, {1, -1}, {0, -2}, {-1, -3}, {0, -4},
					{1, -3}, {2, -2}, {3, -1}, {4, 0}, {4, -2},
					{5, -3}, {6, -4}, {7, -5}, {2, -4}, {3, -5},
					{4, -6}, {5, -7}, {1, 3}, {-3, 1}, {-4, 0},

				});
				break;

			default:
				throw new IllegalStateException("Unexpected direction: " + direction);
		}

		// Animate and send graphics
		npc.animate(10883);
		World.startEvent(event -> {
			event.setCancelCondition(this::targetIsNotInBossRegion);
			event.delay(3);
			for (Position pos : dangerTiles) {
				World.sendGraphics(2670, 0, 0, pos);
				if (player.getPosition().distance(pos) < 1 && npc.getHp() > 0) {
					player.hit(new Hit().randDamage(35, 45));
					damagedPlayer = true;
				}
			}
		});
	}

	private void addDangerTiles(List<Position> dangerTiles, Position startTile, int[][] relativePositions) {
		for (int[] pos : relativePositions) {
			dangerTiles.add(new Position(startTile.getX() + pos[0], startTile.getY() + pos[1], 0));
		}
	}


	private void spearSpecialTwo(Player player) {
		Position startTile = npc.getCentrePosition().copy();
		List<Position> dangerTiles = new ArrayList<>();
		Direction direction = getDirection(npc.getCentrePosition(), player.getPosition());

		int baseWidth = 5; // Base square width
		int extendLength = 8; // Length of the extended part

		switch (direction) {
			case EAST:
				// Base 5x5 square
				for (int y = startTile.getY() - 2; y <= startTile.getY() + 2; y++) {
					for (int x = startTile.getX() - 2; x <= startTile.getX() + 2; x++) {
						dangerTiles.add(new Position(x, y, 0));
					}
					// Extend to the right
					for (int x = startTile.getX() + 3; x <= startTile.getX() + 3 + extendLength; x++) {
						if (y == startTile.getY() - 2 || y == startTile.getY() + 2 || y == startTile.getY()) {
							dangerTiles.add(new Position(x, y, 0));
						}
					}
				}
				break;

			case NORTH:
				// Base 5x5 square
				for (int x = startTile.getX() - 2; x <= startTile.getX() + 2; x++) {
					for (int y = startTile.getY() - 2; y <= startTile.getY() + 2; y++) {
						dangerTiles.add(new Position(x, y, 0));
					}
					// Extend upwards
					for (int y = startTile.getY() + 3; y <= startTile.getY() + 3 + extendLength; y++) {
						if (x == startTile.getX() - 2 || x == startTile.getX() + 2 || x == startTile.getX()) {
							dangerTiles.add(new Position(x, y, 0));
						}
					}
				}
				break;

			case WEST:
				// Base 5x5 square
				for (int y = startTile.getY() - 2; y <= startTile.getY() + 2; y++) {
					for (int x = startTile.getX() - 2; x <= startTile.getX() + 2; x++) {
						dangerTiles.add(new Position(x, y, 0));
					}
					// Extend to the left
					for (int x = startTile.getX() - 3; x >= startTile.getX() - 3 - extendLength; x--) {
						if (y == startTile.getY() - 2 || y == startTile.getY() + 2 || y == startTile.getY()) {
							dangerTiles.add(new Position(x, y, 0));
						}
					}
				}
				break;

			case SOUTH:
				// Base 5x5 square
				for (int x = startTile.getX() - 2; x <= startTile.getX() + 2; x++) {
					for (int y = startTile.getY() - 2; y <= startTile.getY() + 2; y++) {
						dangerTiles.add(new Position(x, y, 0));
					}
					// Extend downwards
					for (int y = startTile.getY() - 3; y >= startTile.getY() - 3 - extendLength; y--) {
						if (x == startTile.getX() - 2 || x == startTile.getX() + 2 || x == startTile.getX()) {
							dangerTiles.add(new Position(x, y, 0));
						}
					}
				}
				break;

			case NORTH_WEST:
				addDangerTiles(dangerTiles, startTile, new int[][]{
					{0, 0}, {1, 1}, {-1, 1}, {0, 2}, {1, -1},
					{-2, 0}, {-3, 1}, {-2, -2}, {-2, 2}, {-1, -3}, {0, -4}, {2, -2}, {3, -1},
					{2, 0}, {1, 1}, {0, 2}, {-1, 3}, {0, 4},
					{1, 3}, {2, 2}, {3, 1}, {4, 0}, {1, -3}, {-3, -1}, {-4, 0},

					{-5, 1},
					{-6, 2}, {-7, 3}, {-8, 4},
					{-3, 3},
					{-4, 4}, {-5, 5}, {-6, 6},
					{-1, 5},
					{-2, 6}, {-3, 7}, {-4, 8},
				});
				break;


			case NORTH_EAST:
				addDangerTiles(dangerTiles, startTile, new int[][]{
					{0, 0}, {-1, -1}, {1, -1}, {0, -2}, {-1, 1},
					{-2, 0}, {-3, 1}, {-2, -2}, {-2, 2}, {-1, -3}, {0, -4}, {2, -2}, {3, -1},
					{2, 0}, {1, 1}, {0, 2}, {-1, 3}, {0, 4},
					{1, 3}, {2, 2}, {3, 1}, {4, 0}, {1, -3}, {-3, -1}, {-4, 0},

					{5, 1},
					{6, 2}, {7, 3}, {8, 4},
					{3, 3},
					{4, 4}, {5, 5}, {6, 6},
					{1, 5},
					{2, 6}, {3, 7}, {4, 8},
				});
				break;


			case SOUTH_WEST:
				addDangerTiles(dangerTiles, startTile, new int[][]{
					{0, 0}, {1, 1}, {-1, 1}, {0, 2}, {1, -1},
					{2, 0}, {3, -1}, {2, 2}, {2, -2}, {1, 3}, {0, 4}, {-2, 2}, {-3, 1},
					{-2, 0}, {-1, -1}, {0, -2}, {1, -3}, {0, -4},
					{-1, -3}, {-2, -2}, {-3, -1}, {-4, 0}, {-1, 3}, {3, 1}, {4, 0},

					{-5, -1},
					{-6, -2}, {-7, -3}, {-8, -4},
					{-3, -3},
					{-4, -4}, {-5, -5}, {-6, -6},
					{-1, -5},
					{-2, -6}, {-3, -7}, {-4, -8},
				});
				break;

			case SOUTH_EAST:
				addDangerTiles(dangerTiles, startTile, new int[][]{
					{0, 0}, {-1, 1}, {1, 1}, {0, 2}, {-1, -1},
					{2, 0}, {3, -1}, {2, 2}, {2, -2}, {1, 3}, {0, 4}, {-2, 2}, {-3, 1},
					{-2, 0}, {-1, -1}, {0, -2}, {1, -3}, {0, -4},
					{-1, -3}, {-2, -2}, {-3, -1}, {-4, 0}, {-1, 3}, {3, 1}, {4, 0},

					{5, -1},
					{6, -2}, {7, -3}, {8, -4},
					{3, -3},
					{4, -4}, {5, -5}, {6, -6},
					{1, -5},
					{2, -6}, {3, -7}, {4, -8},
				});
				break;


			default:
				throw new IllegalStateException("Unexpected direction: " + direction);
		}

		npc.animate(10883);
		World.startEvent(event -> {
			event.setCancelCondition(this::targetIsNotInBossRegion);
			event.delay(3);
			for (Position pos : dangerTiles) {
				World.sendGraphics(2670, 0, pos.distance(startTile), pos);
				if (player.getPosition().distance(pos) < 1 && npc.getHp() > 0) {
					damagedPlayer = true;
					player.hit(new Hit().randDamage(35, 45));
				}
			}
		});
	}


	private void spawnMoltenSand(Player player) {
		if (player == null)
			return;
		Position startPosition = player.getPosition().copy();
		List<Position> spawnPositions = new ArrayList<>();
		List<Position> impactPositions = new ArrayList<>();
		for (int x = startPosition.getX() - 9; x < startPosition.getX() + 9; x++) {
			for (int y = startPosition.getY() - 9; y < startPosition.getY() + 9; y++) {
				Position pos = new Position(x, y, 0);
				if (pos.inBounds(bossBounds))
					spawnPositions.add(pos);
			}
		}
		for (int i = 0; i < 5; i++) {
			Position pos = Random.get(spawnPositions);
			spawnPositions.remove(pos);
			currentMoltenPositions.add(pos);
			impactPositions.add(pos);
		}
		impactPositions.add(startPosition);
		currentMoltenPositions.add(startPosition);
		World.startEvent(e -> {
			e.setCancelCondition(this::targetIsNotInBossRegion);
			for (Position pos : impactPositions) {
				World.sendGraphics(2698, 0, 0, pos);
			}
			e.delay(3);
			for (Position pos : impactPositions) {
				if (pos.distance(player.getPosition()) < 1 && npc.getHp() > 0) {
					player.hit(new Hit().randDamage(35, 55));
					damagedPlayer = true;
				}
			}
			e.delay(2);
			currentMoltenPositions.forEach(pos -> {
				World.sendGraphics(-1, 0, 0, pos);
				if (!moltenPositions.contains(pos))
					moltenPositions.add(pos);
			});
			currentMoltenPositions.clear();
		});


	}


	private void rewardPlayer(Player player) {
		float petDropAverage = 950;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
			ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			petDropAverage *= (float) c.getPetChanceBoost();
		}
		if (player.petDropBonus.isDelayed())
			petDropAverage *= 0.8f;
		if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
			petDropAverage *= 0.85F;

		petDropAverage *= getPetDonatorBoost(player);
		if (Random.get(0, (int) petDropAverage) == 0) {
			Pet.SMOL_HEREDIT.unlock(player, 12821);
		}
		if (player.solHereditTimer != null)
			player.solHereditBestTime = player.solHereditTimer.stop(player, player.solHereditBestTime);
		if (ActivityTimer.timeInSeconds(player.solHereditBestTime) < 35) {
			boolean slayerTask = player.bossSlayerName != null && player.bossSlayerName.equalsIgnoreCase("Sol Heredit");
			if (!slayerTask) {
				Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.SOL_HEREDIT_SPEED_RUNNER.ordinal())).
					getCombatAchievement()).check(player);
			}
		}
		player.sendMessage(Color.RED.wrap("Your reward has been dropped to your feet!"));
		SummerEvent.handleKill(player, "Sol Heredit");
		int rolls = 1;
		if (player.getEquipment().get(Equipment.SLOT_RING) != null && player.getEquipment().get(Equipment.SLOT_RING).getId() == 30592 && Random.get(3) == 0)
			rolls++;
		if (NPCCombat.rollExtraDonatorDrop(player))
			rolls++;
		if (player.doubleDropBonus.remaining() > 0)
			rolls *= 2;
		if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.ADDITIONAL_ROLL_CHANCE) && Random.get(2) == 0)
			rolls++;

		for (int i = 0; i < rolls; i++) {
			Item reward = table.rollItem();
			player.addToCollectionLog(reward);

			new GroundItem(reward).owner(player).position(player.getPosition()).spawn();
			String message = player.getName() + " just received ";
			if (reward.getAmount() > 1)
				message += NumberUtils.formatNumber(reward.getAmount()) + " x " + reward.getDef().name;
			else
				message += reward.getDef().descriptiveName;
			if (reward.lootBroadcast != null) {
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + reward.getAmount() + "x " + reward.getDef().name.toLowerCase() + "</shad> from a " + npc.getDef().name.toLowerCase() + "! (<col=FC0101>" + (player.solHereditKills.getKills() + 1) + " KC<col=000000>)");

				RareDropHook.sendDiscordMessage(() -> {
					var jsonObject = new JSONObject();
					jsonObject.put("player", player.getName());
					jsonObject.put("game_mode", player.getGameMode());
					jsonObject.put("item_id", reward.getId());
					jsonObject.put("item_name", reward.getDef().name);
					jsonObject.put("source", npc.getDef().descriptiveName);
					jsonObject.put("total_attempts", Utils.formatMoneyString(player.solHereditKills.getKills() + 1));
					return jsonObject;
				});
			}
		}
	}

	private void comboAttack(Player player) {
		World.startEvent(e -> {
			e.setCancelCondition(this::targetIsNotInBossRegion);
			// Start the attack animation and graphics
			npc.animate(10886);
			npc.graphics(2667);

			// Loop for the three strikes of the combo attack
			for (int i = 0; i < 3; i++) {
				int delay = 3;
				if (i == 2 && phase >= 4) {
					delay = 4; // Delay the third strike by an additional tick in Phase 4
				}

				// Check prayer status every tick leading up to the hit
				for (int tick = 0; tick < delay; tick++) {
					e.delay(1); // Delay by 1 tick

					// Deactivate prayer if it's active and we're not at the final tick of delay
					if (player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE) && tick != delay - 1) {
						player.getPrayer().deactivate(Prayer.PROTECT_FROM_MELEE);
					}
				}

				// Apply damage based on phase and strike number
				int damage = 0;
				if (target != null && !target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE)) {
					switch (i) {
						case 0:
							damage = 15; // First hit
							break;
						case 1:
							damage = (phase >= 4) ? 30 : 25; // Second hit
							break;
						case 2:
							damage = (phase >= 4) ? 45 : 35; // Third hit
							break;
					}
					if (npc.getHp() > 0) {
						player.hit(new Hit().fixedDamage(damage));
						damagedPlayer = true;
					}
				}

				// After the final attack, allow the NPC to attack again after a delay
				if (i == 2) {
					e.delay(5);
					canAttack = true;
				}
			}
		});
	}


	int prismsSpawned = 0;
	int prismTicksUntilLight = 30;

	private void spawnPrism(Player player) {
		NPC prism = new NPC(12824).spawn(getPrismSpawnPosition());
		AtomicBoolean canMove = new AtomicBoolean(true);
		List<Position> cornerPositions = getPrismCornerPosition();
		final Position[] targetPosition = {Random.get(cornerPositions)};
		Direction lightDirection = getLightDirection();
		if (prismsSpawned == 0)
			prismTicksUntilLight = 30;
		prismsSpawned++;
		AtomicInteger ticks = new AtomicInteger();
		World.startEvent(e -> {
			while (npc.getHp() > 0 && !targetIsNotInBossRegion()) {
				e.delay(1);
				if (prism.getPosition().distance(targetPosition[0]) < 2) {
					targetPosition[0] = getNextCornerPosition(cornerPositions, prism);
				}
				int xDistance = targetPosition[0].getX() - prism.getPosition().getX();
				int yDistance = targetPosition[0].getY() - prism.getPosition().getY();
				if (canMove.get())
					prism.step(xDistance, yDistance, StepType.WALK);
				else prism.resetSteps();
				ticks.getAndIncrement();
				if (prismTicksUntilLight <= 0) {
					Position stopPosition = prism.getPosition().copy();
					ticks.set(0);
					canMove.set(false);
					prism.resetSteps();
					prism.getMovement().reset();
					prism.lock();
					e.delay(1);
					prism.animate(10801);  // Play prism animation

					// Adjust light position based on current and target positions

					e.delay(7);
					Position lightPosition = adjustLightPosition(prism.getPosition(), targetPosition[0]);
					drawLightBeam(stopPosition, lightDirection);
					e.delay(4);
					sendLight(player, stopPosition, lightDirection);

					canMove.set(true);
					prism.animate(-1);
					prism.unlock();
					if (phase == 6)
						prismTicksUntilLight = 18;
					else prismTicksUntilLight = 30;
				}
			}
			if (npc.getHp() < 1)
				prism.remove();
		});


	}

	private Position adjustLightPosition(Position currentPosition, Position targetPosition) {
		int offsetX = 0, offsetY = 0;

		if (currentPosition.getX() < targetPosition.getX()) {
			offsetX = -1;  // Moving East
		} else if (currentPosition.getX() > targetPosition.getX()) {
			offsetX = 1;   // Moving West
		}

		if (currentPosition.getY() < targetPosition.getY()) {
			offsetY = -1;  // Moving North
		} else if (currentPosition.getY() > targetPosition.getY()) {
			offsetY = 1;   // Moving South
		}

		return new Position(currentPosition.getX() + offsetX, currentPosition.getY() + offsetY, currentPosition.getZ());
	}

	private Direction getLightDirection() {
		return switch (prismsSpawned) {
			case 0 -> Direction.EAST;
			case 1 -> Direction.NORTH;
			case 2 -> Direction.WEST;
			case 3 -> Direction.SOUTH;
			default -> null;
		};
	}

	private void drawLightBeam(Position prismPosition, Direction dir) {
		if (dir == Direction.EAST) {
			for (int i = 1; i < 17; i++) {
				Position pos = new Position(prismPosition.getX() + i, prismPosition.getY(), 0);
				World.sendGraphics(2692, 0, i, pos);
			}
		}
		if (dir == Direction.NORTH) {
			for (int i = 1; i < 17; i++) {
				Position pos = new Position(prismPosition.getX(), prismPosition.getY() + i, 0);
				World.sendGraphics(2691, 0, i, pos);
			}
		}
		if (dir == Direction.WEST) {
			for (int i = 1; i < 17; i++) {
				Position pos = new Position(prismPosition.getX() - i, prismPosition.getY(), 0);
				World.sendGraphics(2692, 0, i, pos);
			}
		}
		if (dir == Direction.SOUTH) {
			for (int i = 1; i < 17; i++) {
				Position pos = new Position(prismPosition.getX(), prismPosition.getY() - i, 0);
				World.sendGraphics(2691, 0, i, pos);
			}
		}
	}

	private void sendLight(Player player, Position prismPosition, Direction dir) {
		if (dir == Direction.EAST) {
			for (int i = 0; i < 17; i++) {
				Position pos = new Position(prismPosition.getX() + i, prismPosition.getY(), 0);
				World.sendGraphics(2696, 0, i * 2, pos);
				if (i == 16)
					World.sendGraphics(2696, 0, (i * 2) + 1, pos);
				if (player.getPosition().distance(pos) < 1 && npc.getHp() > 0) {
					player.hit(new Hit().randDamage(55, 70));
					damagedPlayer = true;
				}
			}
		}
		if (dir == Direction.NORTH) {
			for (int i = 0; i < 17; i++) {
				Position pos = new Position(prismPosition.getX(), prismPosition.getY() + i, 0);
				World.sendGraphics(2695, 0, i * 2, pos);
				if (i == 16)
					World.sendGraphics(2697, 0, (i * 2) + 1, pos);
				if (player.getPosition().distance(pos) < 1 && npc.getHp() > 0) {
					damagedPlayer = true;
					player.hit(new Hit().randDamage(55, 70));
				}
			}
		}
		if (dir == Direction.WEST) {
			for (int i = 0; i < 17; i++) {
				Position pos = new Position(prismPosition.getX() - i, prismPosition.getY(), 0);
				World.sendGraphics(2694, 0, i * 2, pos);
				if (i == 16)
					World.sendGraphics(2697, 0, (i * 2) + 1, pos);
				if (player.getPosition().distance(pos) < 1 && npc.getHp() > 0) {
					player.hit(new Hit().randDamage(55, 70));
					damagedPlayer = true;
				}
			}
		}
		if (dir == Direction.SOUTH) {
			for (int i = 0; i < 17; i++) {
				Position pos = new Position(prismPosition.getX(), prismPosition.getY() - i, 0);
				World.sendGraphics(2693, 0, i * 2, pos);
				if (i == 16)
					World.sendGraphics(2697, 0, (i * 2) + 1, pos);
				if (player.getPosition().distance(pos) < 1 && npc.getHp() > 0) {
					player.hit(new Hit().randDamage(55, 70));
					damagedPlayer = true;
				}
			}
		}
	}

	private Position getNextCornerPosition(List<Position> positions, NPC prism) {
		Position position = null;
		for (Position pos : positions) {
			if (position == null)
				position = pos;
			if (prism.getPosition().distance(pos) > prism.getPosition().distance(position))
				position = pos;
		}
		return position;
	}

	private List<Position> getPrismCornerPosition() {
		return switch (prismsSpawned) {
			case 0 ->
				List.of(new Position(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 29, 0),
					new Position(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 40, 0));
			case 1 ->
				List.of(new Position(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 26, 0),
					new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 26, 0));
			case 2 ->
				List.of(new Position(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 40, 0),
					new Position(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 29, 0));
			case 3 ->
				List.of(new Position(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 43, 0),
					new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 43, 0));
			default -> null;
		};
	}

	private Position getPrismSpawnPosition() {
		return switch (prismsSpawned) {
			case 0 ->
				new Position(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 34, 0);//west
			case 1 ->
				new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 26, 0);//south
			case 2 ->
				new Position(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 35, 0);//east
			case 3 ->
				new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 42, 0);//north
			default -> null;
		};
	}


}
