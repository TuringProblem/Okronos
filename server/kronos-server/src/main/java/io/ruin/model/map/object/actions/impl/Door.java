package io.ruin.model.map.object.actions.impl;

import io.ruin.HooksV2;
import io.ruin.Server;
import io.ruin.api.utils.RsAttribute;
import io.ruin.cache.LocType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// ~~~ DIRECTIONS ~~~ \\
// 0 = east, 1 = south \\
// 2 = west, 3 = north \\
// ~~~~~~~~~~~~~~~~~~~~ \\
@Slf4j
public class Door {
	public static interface Hook {
		record OnHandle(Player player, GameObject obj) implements Hook {
		}
	}

	public static HooksV2<Hook> hooks = new HooksV2<>(Hook.class);

	public static void handle(Player player, GameObject obj) {
		handle(player, obj, false);
	}

	public static void handle(Player player, GameObject obj, boolean skipJammedCheck) {
		if (hooks.handle(new Hook.OnHandle(player, obj))) {
			return;
		}

		LocType def = obj.getDef();
		switch (def.id) {
			case 2144:
			case 2143:
				if (player.getAbsX() <= 2888) {
					player.getMovement().teleport(2889, player.getAbsY());
					return;
				}
				if (player.getAbsX() >= 2889) {
					player.getMovement().teleport(2888, player.getAbsY());
					return;
				}
				player.sendMessage("Unhandled door, report this to a staff member! ID: " + def.id);
				return;
		}
		if (def.id == 3444) {
			if (player.getPosition().equals(3405, 9895)) {
				player.getMovement().teleport(3405, 9894);
			} else {
				if (player.getPosition().equals(3405, 9894)) {
					player.getMovement().teleport(3405, 9895);
				}
			}
		}
		if (def.id == 3445) { // port dungeon
			if (player.getPosition().equals(3431, 9897)) {
				player.getMovement().teleport(3432, 9897);
			} else {
				if (player.getPosition().equals(3432, 9897)) {
					player.getMovement().teleport(3431, 9897);
				}
			}
		}
		if (def.id == 34463) { // farming guild
			if (!player.getStats().check(StatType.Farming, 65, "access this guild"))
				return;
			if (player.getPosition().equals(1248, 3722)) {
				player.getMovement().teleport(1248, 3724);
			} else {
				if (player.getPosition().equals(1248, 3724)) {
					player.getMovement().teleport(1248, 3722);
				}
				return;
			}
		}
		if (def.id == 34464) { // farming guild2
			if (!player.getStats().check(StatType.Farming, 65, "access this guild"))
				return;
			if (player.getPosition().equals(1249, 3722)) {
				player.getMovement().teleport(1249, 3724);
			} else {
				if (player.getPosition().equals(1249, 3724)) {
					player.getMovement().teleport(1249, 3722);
				}
				return;
			}
		}

		if (def.id == 1733 || def.id == 1732) { // Fucking stupid ass shit having to put it here wtf
			if (!player.getStats().check(StatType.Magic, 66, "access this guild"))
				return;
			if (player.getPosition().equals(2597, 3087) || player.getPosition().equals(2597, 3088)) {
				player.getMovement().teleport(2595, 3088);
			}
			if (player.getPosition().equals(2596, 3088) || player.getPosition().equals(2596, 3087)) {
				player.getMovement().teleport(2598, 3088);
			}
			if (player.getPosition().equals(2584, 3088) || player.getPosition().equals(2584, 3087)) {
				player.getMovement().teleport(2586, 3088);
			}
			if (player.getPosition().equals(2585, 3088) || player.getPosition().equals(2585, 3087)) {
				player.getMovement().teleport(2583, 3088);
			}
			// if (player.getPosition().equals(3405, 9895)) {
			// player.getMovement().teleport(3405, 9894);
			// }
			// if (player.getPosition().equals(3431, 9897)) {
			// player.getMovement().teleport(3431, 9898);
			// }
			return;
		}
		if (def.doorOppositeId == -1) {
			// player.sendMessage("The " + (def.gateType ? "gate" : "door") + " won't seem to budge.");
			return;
		}


		// if (obj.conOwner != -1 && obj.conOwner != player.getUserId()) {
		// player.sendMessage("Only the host can " + (def.doorClosed ? "open" : "close") + " these doors.");
		// return;
		// }
		int dir = obj.getDirection();
		if (def.doorReversed)
			dir = (dir + 2) & 0x3;
		GameObject pairObj = null;
		boolean verticalFlip = def.verticalFlip;
		if (def.reversedConstructionDoor)
			verticalFlip = !verticalFlip;
		if (def.doorClosed) {
			if (dir == 3) { // North
				if (verticalFlip)
					pairObj = findPair(obj, 1, 0);
				else
					pairObj = findPair(obj, -1, 0);
			} else if (dir == 1) { // South
				if (verticalFlip)
					pairObj = findPair(obj, -1, 0);
				else
					pairObj = findPair(obj, 1, 0);
			} else if (dir == 0) { // East
				if (verticalFlip)
					pairObj = findPair(obj, 0, -1);
				else
					pairObj = findPair(obj, 0, 1);
			} else if (dir == 2) { // West
				if (verticalFlip)
					pairObj = findPair(obj, 0, 1);
				else
					pairObj = findPair(obj, 0, -1);
			}
		} else {
			if (def.longGate) {
				if (dir == 3) { // North
					if (def.verticalFlip)
						pairObj = findPair(obj, 1, 0);
					else
						pairObj = findPair(obj, -1, 0);
				} else if (dir == 1) { // South
					if (def.verticalFlip)
						pairObj = findPair(obj, -1, 0);
					else
						pairObj = findPair(obj, 1, 0);
				} else if (dir == 0) { // East
					if (def.verticalFlip)
						pairObj = findPair(obj, 0, -1);
					else
						pairObj = findPair(obj, 0, 1);
				} else if (dir == 2) { // West
					if (def.verticalFlip)
						pairObj = findPair(obj, 0, 1);
					else
						pairObj = findPair(obj, 0, -1);
				}
			} else {
				if (dir == 3) { // North
					if (def.doorReversed)
						pairObj = findPair(obj, 0, -1);
					else
						pairObj = findPair(obj, 0, 1);
				} else if (dir == 1) { // South
					if (def.doorReversed)
						pairObj = findPair(obj, 0, 1);
					else
						pairObj = findPair(obj, 0, -1);
				} else if (dir == 0) { // East
					if (def.doorReversed)
						pairObj = findPair(obj, -1, 0);
					else
						pairObj = findPair(obj, 1, 0);
				} else if (dir == 2) { // West
					if (def.doorReversed)
						pairObj = findPair(obj, 1, 0);
					else
						pairObj = findPair(obj, -1, 0);
				}
			}
		}
		if (pairObj != null) {
			if (!def.doorClosed) {
				if (!skipJammedCheck && isJammed(player, def.verticalFlip ? pairObj : obj)) {
					player.sendMessage("The " + (def.gateType ? "gates" : "doors") + " seem to be stuck.");
					log.debug("door stuck expires in {}", obj.doorJamEnd() - Server.currentTick());
					return;
				}
				if (def.doorCloseSound != -1)
					player.privateSound(def.doorCloseSound);
			} else {
				if (def.doorOpenSound != -1)
					player.privateSound(def.doorOpenSound);
			}
			changeState(obj, !def.reversedConstructionDoor);
			changeState(pairObj, !pairObj.getDef().reversedConstructionDoor);
		} else {
			if (!def.doorClosed) {
				if (!skipJammedCheck && isJammed(player, obj)) {
					player.sendMessage("The " + (def.gateType ? "gate" : "door") + " seems to be stuck.");
					log.debug("door stuck expires in {}", obj.doorJamEnd() - Server.currentTick());
					return;
				}
				if (def.doorCloseSound != -1)
					player.privateSound(def.doorCloseSound);
			} else {
				if (def.doorOpenSound != -1)
					player.privateSound(def.doorOpenSound);
			}
			if (obj.getType() == 9) {
				int playerX = player.getAbsX();
				int playerY = player.getAbsY();

				int objectX = obj.x;
				int objectY = obj.y;
				int doorDirection = obj.getDirection();

				if (!def.doorClosed) {
					if (doorDirection == 0 && playerX > objectX && playerY == objectY) {
						walkDiagonal(player, obj, playerX + 1, playerY);
						return;
					} else if (doorDirection == 2 && playerX < objectX && playerY == objectY) {
						walkDiagonal(player, obj, playerX - 1, playerY);
						return;
					} else if (doorDirection == 1 && playerY < objectY) {
						walkDiagonal(player, obj, playerX + 1, objectY - 2);
						return;
					} else if (doorDirection == 3 && playerY > objectY) {
						walkDiagonal(player, obj, playerX - 1, objectY + 2);
						return;
					}
				} else {
					if (doorDirection == 0 && playerX == objectX && playerY > objectY) {
						walkDiagonal(player, obj, objectX - 1, objectY + 1);
						return;
					} else if (doorDirection == 1 && playerX > objectX && playerY == objectY) {
						walkDiagonal(player, obj, objectX, objectY + 1);
						return;
					} else if (doorDirection == 2 && playerX == objectX && playerY < objectY) {
						walkDiagonal(player, obj, objectX + 1, objectY - 1);
						return;
					} else if (doorDirection == 3 && playerX < objectX && playerY == objectY) {
						walkDiagonal(player, obj, playerX + 1, playerY - 1);
						return;
					}
				}
			}

			changeState(obj, false);
		}
	}

	private static void walkDiagonal(Player player, GameObject door, int x, int y) {
		player.startEvent(event -> {
			player.stepAbs(x, y, StepType.WALK);
			event.waitForMovement(player);
			changeState(door, false);
			player.face(door);
		});
	}

	private static GameObject findPair(GameObject obj, int offsetX, int offsetY) {
		final Tile tile = Tile.get(obj.x + offsetX, obj.y + offsetY, obj.z, false);
		if (tile == null) {
			return null;
		}
		final List<GameObject> gameObjects = tile.gameObjects;
		if (gameObjects == null) {
			return null;
		}
		final int size = gameObjects.size();
		if (size == 0) {
			return null;
		}
		for (int i = (size - 1); i >= 0; i--) { // keep backwards loop
			final GameObject pairedObj = tile.gameObjects.get(i);
			if (pairedObj.getId() != -1 && pairedObj.getType() == obj.getType()) {
				final LocType def = pairedObj.getDef();
				if (def != null && def.doorOppositeId != -1) {
					return pairedObj;
				}
			}
		}
		return null;
	}

	private static boolean isJammed(Player pCloser, GameObject obj) {
		if (obj.doorReplaced != null)
			obj = obj.doorReplaced;
		if (Server.isPast(obj.doorJamEnd())) {
			obj.doorCloses(0);
			obj.doorJamEnd(Server.getEnd(pCloser.wildernessLevel == 0 ? 500 : 50));
		}
		obj.doorCloses(obj.doorCloses() + 1);
		return obj.doorCloses() >= 5;
	}

	public static void changeState(GameObject obj, boolean paired) {
		if (obj.doorReplaced != null) {
			if (obj.doorReplaced.getId() == -1) {
				obj.remove();
				int prev = obj.doorReplaced.doorOriginalId();
				if (prev != -1) {
					obj.doorReplaced.setId(prev);
					obj.remove(RsAttribute.doorOriginalId);
				} else {
					obj.doorReplaced.restore();
				}
			} else {
				Tile tile = obj.tile;
				tile.removeObject(obj);
				for (Player player : tile.region.players)
					obj.doorReplaced.sendCreate(player);
			}
			obj.doorReplaced = null;
			return;
		}
		LocType def = obj.getDef();
		int dir = obj.getDirection();
		int diffX = 0, diffY = 0, diffDir = 0;
		if (paired) {
			/**
			 * Double
			 */
			if (def.longGate) {
				if (def.doorClosed) {
					if (def.verticalFlip) {
						diffDir--;

						if (dir == 0) {
							diffX -= 2;
							diffY--;
						} else if (dir == 1) {
							diffX--;
							diffY += 2;
						} else if (dir == 2) {
							diffY++;
							diffX += 2;
						} else if (dir == 3) {
							diffX++;
							diffY -= 2;
						}
					} else {
						diffDir--;
						if (dir == 0) {
							diffX--;
						} else if (dir == 1) {
							diffY++;
						} else if (dir == 2) {
							diffX++;
						} else if (dir == 3) {
							diffY--;
						}
					}
				} else {
					if (def.verticalFlip)
						diffDir--;
					else
						diffDir++;
					if (dir == 0) {
						if (def.verticalFlip)
							diffY--;
						else
							diffY++;
					} else if (dir == 1) {
						if (def.verticalFlip)
							diffX++;
						else
							diffX--;
					} else if (dir == 2) {
						if (def.verticalFlip)
							diffY++;
						else
							diffY--;
					} else if (dir == 3) {
						if (def.verticalFlip)
							diffX--;
						else
							diffX++;
					}
				}
			} else if (def.doorClosed) {
				if (def.verticalFlip)
					diffDir++;
				else
					diffDir--;
				if (dir == 0)
					diffX--;
				else if (dir == 1)
					diffY++;
				else if (dir == 2)
					diffX++;
				else if (dir == 3)
					diffY--;
			} else {
				if (def.verticalFlip)
					diffDir--;
				else
					diffDir++;
				if (dir == 0) {
					if (def.verticalFlip)
						diffY--;
					else
						diffY++;
				} else if (dir == 1) {
					if (def.verticalFlip)
						diffX++;
					else
						diffX--;
				} else if (dir == 2) {
					if (def.verticalFlip)
						diffY++;
					else
						diffY--;
				} else if (dir == 3) {
					if (def.verticalFlip)
						diffX--;
					else
						diffX++;
				}
			}
		} else if (obj.getType() == 9) {
			/**
			 * Single diagonal
			 */
			if (def.doorClosed) {
				if (def.verticalFlip) {
					diffDir--;
				} else {
					diffDir++;
					if (dir == 0)
						diffY++;
					if (dir == 1)
						diffX++;
					if (dir == 2)
						diffY--;
					if (dir == 3)
						diffX--;
				}
			} else {
				if (def.verticalFlip) {
					diffDir++;
				} else {
					diffDir--;
					if (dir == 0) {
						diffX++;
					}
					if (dir == 2) {
						diffX--;
					}
				}
			}
		} else {
			/**
			 * Single regular
			 */
			if (def.doorClosed) {
				if (def.verticalFlip)
					diffDir--;
				else
					diffDir++;
				if (dir == 0)
					diffX--;
				else if (dir == 1)
					diffY++;
				else if (dir == 2)
					diffX++;
				else if (dir == 3)
					diffY--;
			} else {
				if (def.verticalFlip)
					diffDir++;
				else
					diffDir--;
				if (dir == 0)
					diffY++;
				else if (dir == 1)
					diffX++;
				else if (dir == 2)
					diffY--;
				else if (dir == 3)
					diffX--;
			}
		}
		if (/* obj.conOwner == -1 && */def.doorReversed != LocType.get(def.doorOppositeId).doorReversed) {
			diffX = diffY = 0;
			diffDir += 2;
		} else if (def.doorReversed) {
			diffDir += 2;
		}
		if (diffX == 0 && diffY == 0)
			obj.clip(true);
		else {
			if (obj.getId() != obj.originalId)
				obj.doorOriginalId(obj.getId());
			obj.remove();
		}
		GameObject replacement =
				GameObject.spawn(def.doorOppositeId, obj.x + diffX, obj.y + diffY, obj.z, obj.getType(), (dir + diffDir) & 0x3);
		replacement.doorReplaced = obj;
	}

	public static void register() {
		/**
		 * This array is used to manually override opposite ids. Example: Sometimes an open door will use a different model
		 * id from it's closed version, causing it not to pair.
		 */
		int[][] oppositeOverrideIds = {
				{24060, 24061}, // Double doors on the top of the Falador castle.
				{24062, 24063}, // Double doors on the top of the Falador castle.
				{13314, 13315}, // oak cage door
				{13317, 13318}, // oak and steel cage door
				{13320, 13321}, // steel cage door
				{13323, 13324}, // spiked cage door
				{13326, 13327}, // bone cage door
				{13344, 13350}, {13345, 13351}, // oak dungeon door
				{13346, 13352}, {13347, 13353}, // steel dungeon door
				{13348, 13354}, {13349, 13355}, // marble dungeon door
				{9038, 9039}, // Karamja teak tree entrance
				{1511, 1511}, {1513, 1513}
		};
		/**
		 * These objects face 180 degrees different than others. Example: An object in this list with a direction of 0
		 * (East) will look as if it's facing direction 2 (West)
		 */
		int[] reversedIds = {
				24060, 24062, // Double doors on the top of the Falador castle.
				22435, 22437, // Double (closed) doors (Not sure what island these are on..)
				22436, 22438, // Double (opened) doors (Not sure what island these are on..)
				14233, 14235, // Double (closed) gates in Pest Control.
				14234, 14236, // Double (opened) gates in Pest Control.
				13314, 13315, // oak cage door
				13317, 13318, // oak and steel cage door
				13320, 13321, // steel cage door
				13323, 13324, // spiked cage door
				13006, 13007, 13008, 13009, // whitewashed stone style doors
				// 13344, 13350, 13345, 13351, // oak dungeon door

				/* construction doors (deathly mansion doors not reversed!) */
				// HouseStyle.BASIC_WOOD.doorId1, HouseStyle.BASIC_WOOD.doorId2,
				// HouseStyle.BASIC_STONE.doorId1, HouseStyle.BASIC_STONE.doorId2,
				// HouseStyle.WHITEWASHED_STONE.doorId1, HouseStyle.WHITEWASHED_STONE.doorId2,
				// HouseStyle.FREMENNIK_WOOD.doorId1, HouseStyle.FREMENNIK_WOOD.doorId2,
				// HouseStyle.TROPICAL_WOOD.doorId1, HouseStyle.TROPICAL_WOOD.doorId2,
				// HouseStyle.FANCY_STONE.doorId1, HouseStyle.FANCY_STONE.doorId2,
		};

		int[] reversedConstructionDoors = {
				13345, 13351, // oak dungeon door
				13347, 13353, // steel dungeon door
				13349, 13355, // marble dungeon door
		};
		/**
		 * Registering
		 */
		LocType.forEach(def -> {
			if (def.id >= 26502 && def.id <= 26505) // gwd doors
				return;
			if (def.id == 34553 || def.id == 34554) // alchemical hydra doors
				return;
			String name = def.name.toLowerCase();
			if (name.contains("gate")) {
				def.gateType = true;
				if (def.modelIds[0] == 7371 || (def.modelIds[0] == 966 && def.modelIds[1] == 967 && def.modelIds[2] == 968)) {
					def.longGate = true;
					setSound(def, 67, 66);
				} else {
					setSound(def, 69, 68);
				}
			} else if (!name.contains("door")) {
				return;
			} else {
				setSound(def, 62, 60);
			}

			if (def.id == 12657)
				def.doorOppositeId = 12658;
			else if (def.id == 12658)
				def.doorOppositeId = 12657;
			else
				def.doorOppositeId = findOppositeId(def);

			for (int[] opp : oppositeOverrideIds) {
				int id1 = opp[0];
				int id2 = opp[1];
				if (def.id == id1) {
					def.doorOppositeId = id2;
					break;
				}
				if (def.id == id2) {
					def.doorOppositeId = id1;
					break;
				}
			}
			for (int reversedId : reversedIds) {
				if (def.id == reversedId) {
					def.doorReversed = true;
					break;
				}
			}
			for (int reversedId : reversedConstructionDoors) {
				if (def.id == reversedId) {
					def.reversedConstructionDoor = true;
					break;
				}
			}
			if (def.doorClosed = def.hasOption("open"))
				ObjectAction.register(def.id, "open", Door::handle);
			else
				ObjectAction.register(def.id, "close", Door::handle);
		});
		ObjectAction.register(12657, 2319, 3690, 0, "open",
				(player, obj) -> player.sendFilteredMessage("The door seems to be stuck."));
	}

	private static void setSound(LocType def, int open, int close) {
		if (def.hasOption("open"))
			def.doorOpenSound = open;
		else
			def.doorCloseSound = close;
	}

	private static int findOppositeId(LocType originalDef) {
		if (originalDef.getOption("open", "close") == -1)
			return -1;
		IntList ids = new IntArrayList();

		List<LocType> types = LocType.getByName(originalDef.name);
		if (types != null) {
			defs: for (LocType def : types) {
				if (def.id == originalDef.id)
					continue;
				if (def.render0x2 != originalDef.render0x2)
					continue;
				if (!Arrays.equals(def.modelIds, originalDef.modelIds))
					continue;
				if (!Arrays.equals(def.modelTypes, originalDef.modelTypes))
					continue;
				if (!Arrays.equals(def.modifiedModelColors, originalDef.modifiedModelColors))
					continue;
				if (def.verticalFlip != originalDef.verticalFlip)
					continue;
				if (Arrays.equals(def.options, originalDef.options))
					continue;
				for (int i = 0; i < def.options.length; i++) {
					String s1 = def.options[i];
					String s2 = originalDef.options[i];
					if (!Objects.equals(s1, s2)) {
						if ("open".equalsIgnoreCase(s1) && "close".equalsIgnoreCase(s2))
							continue;
						if ("close".equalsIgnoreCase(s1) && "open".equalsIgnoreCase(s2))
							continue;
						continue defs;
					}
				}
				ids.add(def.id);
			}
		}
		if (!ids.isEmpty()) {
			ids.sort((i1, i2) -> {
				int diff1 = Math.abs(i1 - originalDef.id);
				int diff2 = Math.abs(i2 - originalDef.id);
				return Integer.compare(diff1, diff2);
			});
			return ids.getInt(0);
		}
		return -1;
	}

}
