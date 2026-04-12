package io.ruin.model.activities.gauntlet;

import io.ruin.api.utils.Random;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;

import java.util.LinkedList;
import java.util.List;

public class GauntletObjects {

	private static void entrance(Player player) {
		player.gauntlet = new Gauntlet(player);
		player.dialogue(new NPCDialogue(9020, "Ah I see you want to take on the Gauntlet!"),
			new OptionsDialogue("Which Gauntlet would you like to enter?",
				new Option("I'd like to enter the corrupted Gauntlet.", () -> {
					if (!canStartGauntlet(player))
						return;
					player.gauntlet = new Gauntlet(player);
					player.gauntlet.start(true);
					//player.openInterface(ToplevelComponent.OVERLAY, 862);
				}),
				new Option("I'd like to enter the normal Gauntlet.", () -> {
					if (!canStartGauntlet(player))
						return;
					//player.openInterface(ToplevelComponent.OVERLAY, 862);
					player.gauntlet = new Gauntlet(player);
					player.gauntlet.start(false);
				}),
				new Option("Nevermind.", player::closeDialogue)));

	}

	private static void toolStorage(Player player) {
		player.gauntlet.toolStorage(player);
	}

	private static void openEgniolPotionBook(Player player) {
		if (player.gauntlet == null) {
			return;
		}
		player.gauntlet.displayEgniolPotionsBook(player);
	}

	private static boolean canStartGauntlet(Player player) {
		boolean canStart = true;
		if (player.corruptedGauntletBossChestToBeLooted || player.corruptedGauntletChestToBeLooted
			|| player.crystallineGauntletBossChestToBeLooted || player.crystallineGauntletChestToBeLooted) {
			player.sendMessage("You need to loot the chest before starting another Gauntlet!");
			player.closeInterfaces();
			VarPlayerRepository.GAUNTLET_REWARD.set(player, 1);
			return false;
		}


		if (!canStart) {
			player.sendMessage("Your inventory and equipment must be free to start the Gauntlet.");
		}
		if (player.getStats().get(StatType.Agility).currentLevel < 70) {
			player.sendMessage("You need an agility level of 70 to enter the Gauntlet.");
			return false;
		} else if (player.getStats().get(StatType.Construction).currentLevel < 70) {
			player.sendMessage("You need an construction level of 70 to enter the Gauntlet.");
			return false;
		} else if (player.getStats().get(StatType.Farming).currentLevel < 70) {
			player.sendMessage("You need an farming level of 70 to enter the Gauntlet.");
			return false;
		} else if (player.getStats().get(StatType.Herblore).currentLevel < 70) {
			player.sendMessage("You need an herblore level of 70 to enter the Gauntlet.");
			return false;
		} else if (player.getStats().get(StatType.Mining).currentLevel < 70) {
			player.sendMessage("You need an mining level of 70 to enter the Gauntlet.");
			return false;
		} else if (player.getStats().get(StatType.Smithing).currentLevel < 70) {
			player.sendMessage("You need an smithing level of 70 to enter the Gauntlet.");
			return false;
		} else if (player.getStats().get(StatType.Woodcutting).currentLevel < 70) {
			player.sendMessage("You need an woodcutting level of 70 to enter the Gauntlet.");
			return false;
		}

		return canStart;
	}

	private static void outsideGauntletEgniolBook(Player player) {
		List<String> text = new LinkedList<>();
		String replace = "crystal";
		text.add("Those hoping to survive the Gauntlet will need to");
		text.add("take advantage of the Grym roots found within the dungeon.");
		text.add("The leaves that grow on these roots can be used to create");
		text.add("Egniol potions, which are able to restore both energy and prayer.");
		text.add("");
		text.add("To create an Egniol potion, follow these steps:");
		text.add("Fill a vial with water.");
		text.add("Add a Grym leaf to the vial.");
		text.add("Crush ten " + replace + " shards.");
		text.add("Add the " + replace + " dust to the vial.");
		text.add("");

		player.sendScroll("Egniol Potions", text.toArray(new String[0]));
	}

	private static void openChest(Player player, GameObject gameObject) {
		if (!player.corruptedGauntletBossChestToBeLooted && !player.crystallineGauntletBossChestToBeLooted
			&& !player.crystallineGauntletChestToBeLooted && !player.corruptedGauntletChestToBeLooted) {
			VarPlayerRepository.GAUNTLET_REWARD.set(player, 0);
			player.sendMessage("The chest is empty.");
			return;
		}
		if (player.gauntlet == null)
			return;
		GauntletRewards gauntletRewards = new GauntletRewards(player.gauntlet.corrupted);
		gauntletRewards.replaceChest(gameObject, 36088, player);
	}

	private static void displayCrystalSingingRecipes(Player player) {
		boolean corrupted = Random.get(1) != 0;
		String replacement = "Crystal";
		String replacement2 = "Crystalline";

		player.getPacketSender().sendString(1091, 11, "<shad=00000>10 " + replacement + " Shards</shad>");
		player.getPacketSender().sendString(1091, 15, "<shad=00000>40 " + replacement + " Shards</shad>");

		player.getPacketSender().sendString(1091, 18, "<shad=00000>Basic " + replacement + " Halberd</shad>");
		player.getPacketSender().sendString(1091, 19, "<shad=00000>20 " + replacement + " Shards & 1 Weapon Frame</shad>");
		player.getPacketSender().sendString(1091, 22, "<shad=00000>Basic " + replacement + " Bow</shad>");
		player.getPacketSender().sendString(1091, 23, "<shad=00000>20 " + replacement + " Shards & 1 Weapon Frame</shad>");
		player.getPacketSender().sendString(1091, 26, "<shad=00000>Basic " + replacement + " Staff</shad>");
		player.getPacketSender().sendString(1091, 27, "<shad=00000>20 " + replacement + " Shards & 1 Weapon Frame</shad>");
		player.getPacketSender().sendString(1091, 30, "<shad=00000>Basic " + replacement + " Helm</shad>");
		player.getPacketSender().sendString(1091, 31, "<shad=00000>40 " + replacement + " Shards, 1 " + replacement + " Ore, 1 Phren Bark &<br><shad=00000> 1 Linum Tirinum</br></shad>");
		player.getPacketSender().sendString(1091, 34, "<shad=00000>Basic " + replacement + " Body</shad>");
		player.getPacketSender().sendString(1091, 35, "<shad=00000>40 " + replacement + " Shards, 1 " + replacement + " Ore, 1 Phren Bark &<br><shad=00000> 1 Linum Tirinum</br></shad>");
		player.getPacketSender().sendString(1091, 38, "<shad=00000>Basic " + replacement + " Legs</shad>");
		player.getPacketSender().sendString(1091, 39, "<shad=00000>40 " + replacement + " Shards, 1 " + replacement + " Ore, 1 Phren Bark &<br><shad=00000> 1 Linum Tirinum</br></shad>");

		player.getPacketSender().sendString(1091, 42, "<shad=00000>Attuned " + replacement + " Halberd</shad>");
		player.getPacketSender().sendString(1091, 43, "<shad=00000>60 " + replacement + " Shards & 1 Basic " + replacement + " Halberd</shad>");
		player.getPacketSender().sendString(1091, 46, "<shad=00000>Attuned " + replacement + " Bow</shad>");
		player.getPacketSender().sendString(1091, 47, "<shad=00000>60 " + replacement + " Shards & 1 Basic " + replacement + " Bow</shad>");
		player.getPacketSender().sendString(1091, 50, "<shad=00000>Attuned " + replacement + " Staff</shad>");
		player.getPacketSender().sendString(1091, 51, "<shad=00000>60 " + replacement + " Shards & 1 Basic " + replacement + " Staff</shad>");
		player.getPacketSender().sendString(1091, 54, "<shad=00000>Attuned " + replacement + " Helm</shad>");
		player.getPacketSender().sendString(1091, 55, "<shad=00000>60 " + replacement + " Shards, 1 Basic " + replacement + " Helm, 2 " + replacement + " Ore,<br><shad=00000> 1 Phren Bark & 1 Linum Tirinum</br></shad>");
		player.getPacketSender().sendString(1091, 58, "<shad=00000>Attuned " + replacement + " Body</shad>");
		player.getPacketSender().sendString(1091, 59, "<shad=00000>60 " + replacement + " Shards, 1 Basic " + replacement + " Body, 2 " + replacement + " Ore,<br><shad=00000> 1 Phren Bark & 1 Linum Tirinum</br></shad>");
		player.getPacketSender().sendString(1091, 62, "<shad=00000>Attuned " + replacement + " Legs</shad>");
		player.getPacketSender().sendString(858, 63, "<shad=00000>60 " + replacement + " Shards, 1 Basic " + replacement + " Legs, 2 " + replacement + " Ore,<br><shad=00000> 1 Phren Bark & 1 Linum Tirinum</br></shad>");

		player.getPacketSender().sendString(1091, 66, "<shad=00000>Perfected " + replacement + " Halberd</shad>");
		player.getPacketSender().sendString(1091, 67, "<shad=00000>1 Attuned " + replacement + " Halberd & 1 " + replacement + " Spike</shad>");
		player.getPacketSender().sendString(1091, 70, "<shad=00000>Perfected " + replacement + " Bow</shad>");
		player.getPacketSender().sendString(1091, 71, "<shad=00000>1 Attuned " + replacement + " Bow & 1 " + replacement2 + " Bowstring</shad>");
		player.getPacketSender().sendString(1091, 74, "<shad=00000>Perfected " + replacement + " Staff</shad>");
		player.getPacketSender().sendString(1091, 75, "<shad=00000>1 Attuned " + replacement + " Staff & 1 " + replacement + " Orb</shad>");
		player.getPacketSender().sendString(1091, 78, "<shad=00000>Perfected " + replacement + " Helm</shad>");
		player.getPacketSender().sendString(1091, 79, "<shad=00000>80 " + replacement + " Shards, 1 Attuned " + replacement + " Helm, 2 " + replacement + " Ore,<br><shad=00000> 2 Phren Bark & 1 Linum Tirinum</br></shad>");
		player.getPacketSender().sendString(1091, 82, "<shad=00000>Perfected " + replacement + " Body</shad>");
		player.getPacketSender().sendString(1091, 83, "<shad=00000>80 " + replacement + " Shards, 1 Attuned " + replacement + " Body, 2 " + replacement + " Ore,<br><shad=00000> 2 Phren Bark & 1 Linum Tirinum</br></shad>");
		player.getPacketSender().sendString(1091, 86, "<shad=00000>Perfected " + replacement + " Legs</shad>");
		player.getPacketSender().sendString(1091, 87, "<shad=00000>80 " + replacement + " Shards, 1 Attuned " + replacement + " Legs, 2 " + replacement + " Ore,<br><shad=00000> 2 Phren Bark & 1 Linum Tirinum</br></shad>");

		player.openInterface(ToplevelComponent.MAINMODAL, 1091);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 8, 1000,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			8,
			1000,
			new Item(23839)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 13, 1001,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			13,
			1001,
			new Item(corrupted ? 23858 : 23904)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 17, 1002,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			17,
			1002,
			new Item(corrupted ? 23849 : 23895)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 21, 1003,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			21,
			1003,
			new Item(corrupted ? 23855 : 23901)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 25, 1004,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			25,
			1004,
			new Item(corrupted ? 23852 : 23898)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 29, 1005,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			29,
			1005,
			new Item(corrupted ? 23840 : 23886)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 33, 1006,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			33,
			1006,
			new Item(corrupted ? 23843 : 23889)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 37, 1007,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			37,
			1007,
			new Item(corrupted ? 23846 : 23892)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 41, 1008,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			41,
			1008,
			new Item(corrupted ? 23850 : 23896)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 45, 1009,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			45,
			1009,
			new Item(corrupted ? 23856 : 23902)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 49, 1010,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			49,
			1010,
			new Item(corrupted ? 23853 : 23899)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 53, 1011,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			53,
			1011,
			new Item(corrupted ? 23841 : 23887)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 57, 1012,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			57,
			1012,
			new Item(corrupted ? 23844 : 23890)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 61, 1013,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			61,
			1013,
			new Item(corrupted ? 23847 : 23893)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 65, 1014,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			65,
			1014,
			new Item(corrupted ? 23851 : 23897)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 69, 1015,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			69,
			1015,
			new Item(corrupted ? 23857 : 23903)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 73, 1016,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			73,
			1016,
			new Item(corrupted ? 23854 : 23900)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 77, 1017,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			77,
			1017,
			new Item(corrupted ? 23842 : 23888)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 81, 1018,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			81,
			1018,
			new Item(corrupted ? 23845 : 23891)
		);
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1091 << 16 | 85, 1019,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			85,
			1019,
			new Item(corrupted ? 23848 : 23894)
		);
	}

	public static int findResourceIndexByPosition(Player player, Position position, GameObject obj, boolean fishingSpot) {
		if (player.gauntlet == null) return -1;
		for (int i = 0; i < player.gauntlet.gauntletResources.size(); i++) {
			GauntletResource resource = player.gauntlet.gauntletResources.get(i);
			if (resource.object.getId() != obj.getId()) continue;
			//  System.out.println("the obj is " + resource.object.getDef().name);
			if (fishingSpot) {
				//  System.out.println("this distance is " + resource.position.distance(position));
				if (resource.position.distance(position) <= 1 && resource.object.getId() == obj.getId())
					return i;
			} else {
				if (resource.position.equals(position) && resource.object.getId() == obj.getId()) {
					return i;
				}
			}
		}
		return -1;
	}


	public static void register() {
		//ObjectAction.register(35977, 1, (player, object) -> toolStorage(player));
		ObjectAction.register(37340, 1, (player, object) -> entrance(player));
		ObjectAction.register(35979, 1, (player, object) -> openEgniolPotionBook(player));
		ObjectAction.register(36076, 1, (player, object) -> openEgniolPotionBook(player));
		// ObjectAction.register(36076, 1, (player, object) -> outsideGauntletEgniolBook(player));
		// ObjectAction.register(35966, 1, (player, object) -> singingBowlSkillDialogue(player));
		// ObjectAction.register(35994, 1, (player, object) -> player.gauntlet.displayCrystalSingingRecipes());
		// ObjectAction.register(35978, 1, (player, object) -> player.gauntlet.displayCrystalSingingRecipes());
		 ObjectAction.register(36087, 1, GauntletObjects::openChest);
		// ObjectAction.register(36075, 1, (player, object) -> displayCrystalSingingRecipes(player));
		// ObjectAction.register(35965, 1, (player, object) -> {
		///     System.out.println("called here too");
		//    player.gauntlet.leaveDialogue(player);
		// });
		// ObjectAction.register(36062, 1, (player, object) -> player.gauntlet.leaveDialogue(player));
		ObjectAction.register(37337, objectActions -> {
			objectActions[2] = ((player1, obj) -> {
				if (player1.gauntlet == null) return;
				player1.gauntlet.passBossBarrier();
			});
			objectActions[1] = ((player, obj) -> {
				if (player.gauntlet == null) return;
				player.dialogue(
					new OptionsDialogue("Are you sure you want to start the boss fight now? (You can't leave again).",
						new Option("Yes.", () -> player.gauntlet.passBossBarrier()),
						new Option("No.", player::closeDialogue)
					)
				);
			});
		});
		ObjectAction.register(37339, objectActions -> {
			objectActions[2] = ((player, obj) -> player.gauntlet.passBossBarrier());
			objectActions[1] = ((player, obj) -> {
				player.dialogue(
					new OptionsDialogue("Are you sure you want to start the boss fight now? (You can't leave again).",
						new Option("Yes.", () -> player.gauntlet.passBossBarrier()),
						new Option("No.", player::closeDialogue)
					)
				);
			});
		});

		ObjectAction.register(35981, 1, (player, obj) -> player.gauntlet.fillVials(player));
		ObjectAction.register(37341, 1, GauntletObjects::openChest);
		ObjectAction.register(36078, 1, (player, obj) -> player.gauntlet.fillVials(player));
		ObjectAction.register(35965, 2, (player, object) -> player.gauntlet.leave(player));
		ObjectAction.register(36062, 1, (player, object) -> player.gauntlet.leave(player));
		ObjectAction.register(36062, 2, (player, object) -> player.gauntlet.leave(player));
		ObjectAction.register(35973, 1, (player, object) -> player.gauntlet.pickGrymRoot(object));
		ObjectAction.register(36070, 1, (player, object) -> player.gauntlet.pickGrymRoot(object));
		ObjectAction.register(35971, "fish", (player, object) -> {
			int resourceIndex = findResourceIndexByPosition(player, object.getPosition(), object, true);


			if (resourceIndex != -1 && player.gauntlet != null) {
				player.gauntlet.gauntletResources.get(resourceIndex).harvest(player, object);
			}
		});
		ObjectAction.register(36068, "fish", (player, object) -> {
			int resourceIndex = findResourceIndexByPosition(player, object.getPosition(), object, true);
			if (resourceIndex != -1 && player.gauntlet != null) {
				player.gauntlet.gauntletResources.get(resourceIndex).harvest(player, object);
			}
		});
		ObjectAction.register(35975, 1, (player, object) -> {
			int resourceIndex = findResourceIndexByPosition(player, object.getPosition(), object, false);
			if (resourceIndex != -1 && player.gauntlet != null) {
				player.gauntlet.gauntletResources.get(resourceIndex).harvest(player, object);
			}
		});
		ObjectAction.register(36072, 1, (player, object) -> {
			int resourceIndex = findResourceIndexByPosition(player, object.getPosition(), object, false);
			if (resourceIndex != -1 && player.gauntlet != null) {
				player.gauntlet.gauntletResources.get(resourceIndex).harvest(player, object);
			}
		});
		ObjectAction.register(36066, 1, (player, object) -> {
			int resourceIndex = findResourceIndexByPosition(player, object.getPosition(), object, false);
			if (resourceIndex != -1 && player.gauntlet != null) {
				player.gauntlet.gauntletResources.get(resourceIndex).harvest(player, object);
			}
		});
		ObjectAction.register(35969, 1, (player, object) -> {
			int resourceIndex = findResourceIndexByPosition(player, object.getPosition(), object, false);
			if (resourceIndex != -1 && player.gauntlet != null) {
				player.gauntlet.gauntletResources.get(resourceIndex).harvest(player, object);
			}
		});
		ObjectAction.register(35967, 1, (player, object) -> {
			int resourceIndex = findResourceIndexByPosition(player, object.getPosition(), object, false);
			if (resourceIndex != -1 && player.gauntlet != null) {
				player.gauntlet.gauntletResources.get(resourceIndex).harvest(player, object);
			}
		});
		ObjectAction.register(36064, 1, (player, object) -> {
			int resourceIndex = findResourceIndexByPosition(player, object.getPosition(), object, false);
			if (resourceIndex != -1 && player.gauntlet != null) {
				player.gauntlet.gauntletResources.get(resourceIndex).harvest(player, object);
			}
		});
	}

}
