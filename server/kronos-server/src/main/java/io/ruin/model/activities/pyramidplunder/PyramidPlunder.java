package io.ruin.model.activities.pyramidplunder;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.api.utils.TimeUtils;
import io.ruin.cache.Color;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LogoutListener;
import io.ruin.model.inter.Widget;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.Direction;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;

public class PyramidPlunder {

	public static int[] lootablePlunderObjects = {26580, 26600, 26601, 26602, 26603, 26604, 26605, 26606, 26607, 26608, 26609, 26610, 26611, 26612, 26613, 26616, 26626};

	public static void register() {
		ObjectAction.register(26580, 1, ((player, obj) -> searchUrn26580(player)));
		ObjectAction.register(26600, 1, ((player, obj) -> searchUrn26600(player)));
		ObjectAction.register(26601, 1, ((player, obj) -> searchUrn26601(player)));
		ObjectAction.register(26602, 1, ((player, obj) -> searchUrn26602(player)));
		ObjectAction.register(26603, 1, ((player, obj) -> searchUrn26603(player)));
		ObjectAction.register(26604, 1, ((player, obj) -> searchUrn26604(player)));
		ObjectAction.register(26605, 1, ((player, obj) -> searchUrn26605(player)));
		ObjectAction.register(26606, 1, ((player, obj) -> searchUrn26606(player)));
		ObjectAction.register(26607, 1, ((player, obj) -> searchUrn26607(player)));
		ObjectAction.register(26608, 1, ((player, obj) -> searchUrn26608(player)));
		ObjectAction.register(26609, 1, ((player, obj) -> searchUrn26609(player)));
		ObjectAction.register(26610, 1, ((player, obj) -> searchUrn26610(player)));
		ObjectAction.register(26611, 1, ((player, obj) -> searchUrn26611(player)));
		ObjectAction.register(26612, 1, ((player, obj) -> searchUrn26612(player)));
		ObjectAction.register(26613, 1, ((player, obj) -> searchUrn26613(player)));
		ObjectAction.register(26616, 1, ((player, obj) -> searchUrn26616(player))); //main chest
		ObjectAction.register(26626, 1, ((player, obj) -> searchUrn26626(player))); //sarcophagus

		ObjectAction.register(26624, 1, ((player, obj) -> startPyramid(player)));
		ObjectAction.register(21280, 1, ((player, obj) -> disarmTrap(player)));
		ObjectAction.register(26618, 1, ((player, obj) -> player.sendMessage("This door does not lead anywhere. Try another one."))); //next room
		ObjectAction.register(26619, 1, ((player, obj) -> nextRoomDoor(player))); //next room
		ObjectAction.register(26620, 1, ((player, obj) -> player.sendMessage("This door does not lead anywhere. Try another one."))); //next room
		ObjectAction.register(26621, 1, ((player, obj) -> player.sendMessage("This door does not lead anywhere. Try another one."))); //next room
		ObjectAction.register(20931, 1, ((player, obj) -> leaveMinigame(player))); //exit door

	}

	public static void nextRoomDoor(Player player) {
		if (player.nextPlunderRoomId == 8) {
			player.sendMessage("Use the exit door to leave the minigame.");
			return;
		}
		player.dialogue(
			new MessageDialogue("Advance to the next room? You will not be able to return to the current room!"),
			new OptionsDialogue(
				new Option("Yes", () -> nextRoom(player)),
				new Option("No")
			)
		);
	}

	public static void nextRoom(Player player) {
		player.nextPlunderRoomId++;
		player.sendMessage(Color.DARK_GREEN, "[PP Room] " + player.nextPlunderRoomId);
		player.sendMessage(Color.DARK_GREEN, "[PP] " + (TimeUtils.fromMs((player.PyramidtimeRemaining - System.currentTimeMillis()), true)));
		player.disarmedPlunderRoomTrap = false;
		player.loot26580 = false;
		player.loot26600 = false;
		player.loot26601 = false;
		player.loot26603 = false;
		player.loot26604 = false;
		player.loot26606 = false;
		player.loot26607 = false;
		player.loot26608 = false;
		player.loot26609 = false;
		player.loot26610 = false;
		player.loot26611 = false;
		player.loot26612 = false;
		player.loot26613 = false;
		player.loot26616 = false;
		player.loot26626 = false;
		if (player.nextPlunderRoomId == 2 && player.getStats().check(StatType.Thieving, 31, "search for treasure inside this room")) {
			teleport(player, 1954, 4476, 0);
		}
		if (player.nextPlunderRoomId == 3 && player.getStats().check(StatType.Thieving, 41, "search for treasure inside this room")) {
			teleport(player, 1977, 4470, 0);
		}
		if (player.nextPlunderRoomId == 4 && player.getStats().check(StatType.Thieving, 51, "search for treasure inside this room")) {
			teleport(player, 1927, 4452, 0);
		}
		if (player.nextPlunderRoomId == 5 && player.getStats().check(StatType.Thieving, 61, "search for treasure inside this room")) {
			teleport(player, 1965, 4444, 0);
		}
		if (player.nextPlunderRoomId == 6 && player.getStats().check(StatType.Thieving, 71, "search for treasure inside this room")) {
			teleport(player, 1927, 4424, 0);
		}
		if (player.nextPlunderRoomId == 7 && player.getStats().check(StatType.Thieving, 81, "search for treasure inside this room")) {
			teleport(player, 1943, 4421, 0);
		}
		if (player.nextPlunderRoomId == 8 && player.getStats().check(StatType.Thieving, 91, "search for treasure inside this room")) {
			teleport(player, 1974, 4420, 0);
		}
	}

	public static boolean hasLootedObject(Player player, int objectId) {
		for (int[] lootedPlunderObject : player.lootedPlunderObjects) {
			if (lootedPlunderObject[0] == objectId) {
				return lootedPlunderObject[1] == 1;
			}
		}
		return false;
	}

	public static void lootObject(Player player, int objectId) {
		for (int[] lootedPlunderObject : player.lootedPlunderObjects) {
			if (lootedPlunderObject[0] == objectId) {
				lootedPlunderObject[1] = 1;
				String looted = (player.lootedPlunderObjects[lootedPlunderObject[0]][1] == 1 ? "true" : "false");
				player.sendMessage("Object ID: " + player.lootedPlunderObjects[lootedPlunderObject[0]][0] + " Looted: " + looted);
			}
		}
	}

	public static void resetLootedObjects(Player player) {
		player.loot26580 = false;
		player.loot26600 = false;
		player.loot26601 = false;
		player.loot26603 = false;
		player.loot26604 = false;
		player.loot26606 = false;
		player.loot26607 = false;
		player.loot26608 = false;
		player.loot26609 = false;
		player.loot26610 = false;
		player.loot26611 = false;
		player.loot26612 = false;
		player.loot26613 = false;
		player.loot26616 = false;
		player.loot26626 = false;
	}

	public static void giveUrnLoot(Player player) {
		int amount = Random.get(1000);
		amount *= (player.nextPlunderRoomId + 1);
		player.sendMessage("You search around inside the urn and find " + NumberUtils.formatNumber(amount) + " coins!");
		player.getInventory().add(995, amount);
	}

	public static void giveChestLoot(Player player) {
		int amount = Random.get(2500, 3000);
		amount *= (player.nextPlunderRoomId + 1);

		int experience = 500;
		experience += (player.nextPlunderRoomId * 100);

		player.getStats().addXp(StatType.Thieving, experience, true);
		player.sendMessage("You search around inside the chest and find " + amount + " coins!");
		player.getInventory().add(995, amount);
	}

	public static void giveSarcophLoot(Player player) {
		int amount = Random.get(1000, 1500);
		amount *= (player.nextPlunderRoomId + 1);

		int experience = 300;
		experience += (player.nextPlunderRoomId * 100);

		player.getStats().addXp(StatType.Thieving, experience, true);
		player.sendMessage("You search around inside the sarcophagus and find " + NumberUtils.formatNumber(amount) + " coins!");
		player.getInventory().add(995, amount);
	}

	public static void searchUrn(int objectId, Player player) {
		player.startEvent(event -> {
			if (hasLootedObject(player, objectId)) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
			}
		});
	}

	public static void searchUrn26580(Player player) {
		player.startEvent(event -> {
			if (player.loot26580) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
				player.loot26580 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26580 = true;
			}
		});
	}

	public static void searchUrn26600(Player player) {
		player.startEvent(event -> {
			if (player.loot26600) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
				player.loot26600 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26600 = true;
			}
		});
	}

	public static void searchUrn26601(Player player) {
		player.startEvent(event -> {
			if (player.loot26601) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
				player.loot26601 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26601 = true;
			}
		});
	}

	public static void searchUrn26602(Player player) {
		player.startEvent(event -> {
			if (player.loot26602) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
				player.loot26602 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26602 = true;
			}
		});
	}

	public static void searchUrn26603(Player player) {
		player.startEvent(event -> {
			if (player.loot26603) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
				player.loot26603 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26603 = true;
			}
		});
	}

	public static void searchUrn26604(Player player) {
		player.startEvent(event -> {
			if (player.loot26604) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
				player.loot26604 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26604 = true;
			}
		});
	}

	public static void searchUrn26605(Player player) {
		player.startEvent(event -> {
			if (player.loot26605) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
				player.loot26605 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26605 = true;
			}
		});
	}

	public static void searchUrn26606(Player player) {
		player.startEvent(event -> {
			if (player.loot26606) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
				player.loot26606 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26606 = true;
			}
		});
	}

	public static void searchUrn26607(Player player) {
		player.startEvent(event -> {
			if (player.loot26607) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
				player.loot26607 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26607 = true;
			}
		});
	}

	public static void searchUrn26608(Player player) {
		player.startEvent(event -> {
			if (player.loot26608) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
				player.loot26608 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26608 = true;
			}
		});
	}

	public static void searchUrn26609(Player player) {
		player.startEvent(event -> {
			if (player.loot26609) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
				player.loot26609 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26609 = true;
			}
		});
	}

	public static void searchUrn26610(Player player) {
		player.startEvent(event -> {
			if (player.loot26610) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
				player.loot26610 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26610 = true;
			}
		});
	}

	public static void searchUrn26611(Player player) {
		player.startEvent(event -> {
			if (player.loot26611) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
				player.loot26611 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26611 = true;
			}
		});
	}

	public static void searchUrn26612(Player player) {
		player.startEvent(event -> {
			if (player.loot26612) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
				player.loot26612 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26612 = true;
			}
		});
	}

	public static void searchUrn26613(Player player) {
		player.startEvent(event -> {
			if (player.loot26613) {
				player.sendMessage("You've already searched this urn.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You search the urn..");
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				player.sendFilteredMessage("You find an artifact inside.");
				giveUrnLoot(player);
				player.animate(4342);
				event.delay(1);
				urnXp(player);
				player.unlock();
				player.loot26613 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the urn.");
				player.getStats().addXp(StatType.Thieving, 5, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26613 = true;
			}
		});
	}

	public static void searchUrn26616(Player player) {
		player.startEvent(event -> {
			if (player.loot26616) {
				player.sendMessage("You've already searched this chest.");
				return;
			}
			event.delay(1);
			player.animate(4340);
			event.delay(1);
			player.lock();
			giveChestLoot(player);
			player.animate(4342);
			event.delay(1);
			player.unlock();
			player.loot26616 = true;
		});
	}

	public static void searchUrn26626(Player player) {
		player.startEvent(event -> {
			if (player.loot26626) {
				player.sendMessage("You've already searched this sarcophagus.");
				return;
			}
			event.delay(1);
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				giveSarcophLoot(player);
				player.animate(4342);
				event.delay(1);
				player.unlock();
				player.loot26626 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the sarcophagus.");
				player.getStats().addXp(StatType.Thieving, 10, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26626 = true;
			}
		});
	}

	public static void searchMainChest(Player player) {
		player.startEvent(event -> {
			if (hasLootedObject(player, 26616)) {
				player.sendMessage("You've already searched this chest.");
				return;
			}
			event.delay(1);
			player.animate(4340);
			event.delay(1);
			player.lock();
			giveChestLoot(player);
			player.animate(4342);
			event.delay(1);
			player.unlock();
			player.loot26616 = true;
		});
	}

	public static void searchSarcophagus(Player player) {
		player.startEvent(event -> {
			if (hasLootedObject(player, 26626)) {
				player.sendMessage("You've already searched this sarcophagus.");
				return;
			}
			event.delay(1);
			player.animate(4340);
			event.delay(1);
			if (Random.get(1, 2) == 1) {
				player.lock();
				giveSarcophLoot(player);
				player.animate(4342);
				event.delay(1);
				player.unlock();
				player.loot26626 = true;
			} else {
				player.lock();
				player.sendFilteredMessage("You find nothing inside of the sarcophagus.");
				player.getStats().addXp(StatType.Thieving, 10, true);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
				player.loot26626 = true;
			}
		});
	}

	public static void disarmTrap(Player player) {
		double chance = 0.5 + (double) (player.getStats().get(StatType.Thieving).currentLevel - 10) * 0.01;
		player.startEvent(event -> {
			if (player.disarmedPlunderRoomTrap) {
				player.sendFilteredMessage("This trap is already disabled.");
				return;
			}
			event.delay(1);
			player.sendFilteredMessage("You start to disarm the trap..");
			player.animate(4340);
			event.delay(1);
			if (Random.get() > Math.min(chance, 0.85)) {
				player.lock();
				player.privateSound(1242);
				player.sendFilteredMessage("You slip and trigger the trap.");
				player.hit(new Hit().randDamage(6));
				player.animate(1113);
				event.delay(1);
				player.resetAnimation();
				player.unlock();
			} else {
				player.sendFilteredMessage("You successfully disarm the trap.");
				player.disarmedPlunderRoomTrap = true;
				player.privateSound(1243);
				player.animate(4342);
				event.delay(1);

				if (player.nextPlunderRoomId == 1) {
					player.getMovement().force(0, -3, 0, 0, 45, 100, Direction.SOUTH);
					player.getStats().addXp(StatType.Thieving, 10, false);
				}
				if (player.nextPlunderRoomId == 2) {
					player.getMovement().force(0, -3, 0, 0, 45, 100, Direction.SOUTH);
					player.getStats().addXp(StatType.Thieving, 12, false);
				}
				if (player.nextPlunderRoomId == 3) {
					player.getMovement().force(0, -3, 0, 0, 45, 100, Direction.SOUTH);
					player.getStats().addXp(StatType.Thieving, 15, false);
				}
				if (player.nextPlunderRoomId == 4) {
					player.getMovement().force(3, 0, 0, 0, 45, 100, Direction.EAST);
					player.getStats().addXp(StatType.Thieving, 18, false);
				}
				if (player.nextPlunderRoomId == 5) {
					player.getMovement().force(-3, 0, 0, 0, 45, 100, Direction.WEST);
					player.getStats().addXp(StatType.Thieving, 21, false);
				}
				if (player.nextPlunderRoomId == 6) {
					player.getMovement().force(0, 3, 0, 0, 45, 100, Direction.NORTH);
					player.getStats().addXp(StatType.Thieving, 24, false);
				}
				if (player.nextPlunderRoomId == 7) {
					player.getMovement().force(0, 3, 0, 0, 45, 100, Direction.NORTH);
					player.getStats().addXp(StatType.Thieving, 27, false);
				}
				if (player.nextPlunderRoomId == 8) {
					player.getMovement().force(0, 3, 0, 0, 45, 100, Direction.NORTH);
					player.getStats().addXp(StatType.Thieving, 30, false);
				}
			}
		});
	}

	public static void urnXp(Player player) {
		int amount = 50;
		amount += (player.nextPlunderRoomId * 100);
		player.getStats().addXp(StatType.Thieving, amount, false);
	}

	public static void leaveMinigame(Player player) {
		player.dialogue(
			new OptionsDialogue("Exit the minigame?",
				new Option("Yes", () -> endMinigame(player)),
				new Option("No"))
		);
	}

	public static void startPyramid(Player player) {
		player.dialogue(
			new OptionsDialogue("Enter Pyramid Plunder?",
				new Option("Yes", () -> startMinigame(player)),
				new Option("No"))
		);
	}

	public static void startMinigame(Player player) {
		if (!player.getStats().check(StatType.Thieving, 21, "search for treasure inside the Pyramid"))
			return;

		player.startEvent(e -> {
			e.delay(1);
			teleport(player, 1927, 4476, 0);
			player.nextPlunderRoomId++;
			player.inPyramidPlunder = true;
			handleTime(player);
		});
	}

	public static void handleTime(Player player) {
		beginTimer(player);
	}

	public static void endMinigame(Player player) {
		resetLootedObjects(player);
		player.getMovement().teleport(3288, 2786, 0);
		player.nextPlunderRoomId = 0;
		player.disarmedPlunderRoomTrap = false;
		player.dialogue(new MessageDialogue("You have left the minigame."));
		player.inPyramidPlunder = false;
		player.totalPyramidPlunderGames++;
		player.teleportListener = null;
		player.logoutListener = null;
		player.PyramidtimeRemaining = 0;
	}

	public static void ranOutOfTime(Player player) {
		if (player.inPyramidPlunder) {
			player.getMovement().teleport(3288, 2786, 0);
			player.nextPlunderRoomId = 0;
			player.disarmedPlunderRoomTrap = false;
			player.dialogue(new MessageDialogue("You've run out of time and the minigame has ended!"));
			resetLootedObjects(player);
			player.inPyramidPlunder = false;
			player.totalPyramidPlunderGames++;
		}
		player.teleportListener = null;
		player.logoutListener = null;
		player.PyramidtimeRemaining = 0;
	}

	private static void beginTimer(Player player) {
		player.lastTimeEnteredPlunder = System.currentTimeMillis();
		player.teleportListener = p -> {
			endMinigame(p);
			return true;
		};
		player.logoutListener = new LogoutListener().onLogout(p -> {
			endMinigame(p);
			p.getMovement().teleport(3288, 2786, 0);
		});
	}

	public static void teleport(Player player, int x, int y, int z) {
		player.resetActions(true, true, true);
		player.lock();
		player.startEvent(e -> {
			player.getPacketSender().fadeOut();
			e.delay(1);
			player.getMovement().teleport(x, y, z);
			player.getPacketSender().clearFade();
			player.getPacketSender().fadeIn();
			player.unlock();
		});
	}
}
