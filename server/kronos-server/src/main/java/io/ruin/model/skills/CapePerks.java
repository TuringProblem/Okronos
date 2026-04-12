package io.ruin.model.skills;

import io.ruin.cache.Color;
import io.ruin.model.activities.duelarena.DuelRule;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Widget;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.item.actions.impl.skillcapes.MagicSkillcape;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.skills.magic.spells.modern.TeleportToHouse;
import io.ruin.utility.FormatMessage;
import io.ruin.utility.TeleportConstants;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CapePerks {

	private static void staminaBoost(Player p, Item item) {
		if (!available(p.lastAgilityCapeBoost)) {
			p.sendMessage("You have already made use of that today.");
			return;
		}

		p.getMovement().restoreEnergy(100);
		VarPlayerRepository.STAMINA_POTION.set(p, 1);
		p.staminaTicks = 100; // One minute
		p.getPacketSender().sendWidgetTimerCustom(Widget.STAMINA, 60);
		p.sendMessage("You activate the effect of your Agility cape.");
		p.lastAgilityCapeBoost = System.currentTimeMillis();
	}

	private static void teleToHouse(Player player, Item item) {
		TeleportToHouse.teleport(player, 0);
	}

	private static void houseDestinations(Player player, Item item) {
		player.dialogue(new OptionsDialogue("Choose a Destination",
			new Option("Rimmington", () -> teleport(player, 2954, 3224)),
			new Option("Taverley", () -> teleport(player, 2894, 3465)),
			new Option("Pollnivneach", () -> teleport(player, 3340, 3004)),
			new Option("Hosidius", () -> teleport(player, 1743, 3517)),
			new Option("More...", () -> {
				player.dialogue(new OptionsDialogue("Choose a Destination",
					new Option("Rellekka", () -> teleport(player, 2670, 3632)),
					new Option("Brimhaven", () -> teleport(player, 2758, 3178)),
					new Option("Yanille", () -> teleport(player, 2544, 3095))));
			})));
	}

	public static void finished2(Player player) {
		player.addEvent(event -> {
			event.delay(700);
			player.sendMessage(Color.DARK_RED, "Your tainted wand's aura speaks to you. The cooldown is up.");
			VarPlayerRepository.TAINTED_WAND_COOLDOWN.set(player, 0);
		});
	}

	private static void teleport(Player player, int x, int y) {
		player.getMovement().startTeleport(e -> {
			player.animate(714);
			player.graphics(111, 92, 0);
			player.publicSound(200);
			e.delay(3);
			player.getMovement().teleport(x, y, 0);
		});
	}

	private static void teleportToCraftingGuild(Player player, Item item) {
		teleport(player, 2933, 3287);
	}

	private static void teleportToFarmingGuild(Player player, Item item) {
		player.sendMessage("Please note that the farming guild is not operated at this time.");
		teleport(player, 1248, 3721);
	}

	private static void teleportToFishingGuild(Player player, Item item) {
		teleport(player, 2590, 3417);
	}

	private static void teleportToOttosGrotto(Player player, Item item) {
		teleport(player, 2505, 3488);
	}

	private static void teleportToWarriorsGuild(Player player, Item item) {
		teleport(player, 2875, 3546);
	}

	private static boolean available(long lastUsed) {
		if (lastUsed <= 0) {
			return true;
		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(System.currentTimeMillis());
		int currentDay = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTimeInMillis(lastUsed);
		int lastUsedDay = cal.get(Calendar.DAY_OF_MONTH);

		return System.currentTimeMillis() > lastUsed && lastUsedDay != currentDay;
	}

	private static void maxCapeInvTeleports(Player player, Item item) {
		player.dialogue(new OptionsDialogue("Choose a Destination",
			new Option("Warriors Guild", () -> teleport(player, 2875, 3546)),
			new Option("Fishing Teleports", () -> {
				player.dialogue(new OptionsDialogue("Choose a Destination",
					new Option("Fishing guild", () -> teleport(player, 2590, 3417)),
					new Option("Ottos grotto", () -> teleport(player, 2505, 3488)),
					new Option("NeverMind", player::closeDialogue)));

			}),
			new Option("Crafting Guild", () -> teleport(player, 2933, 3287)),
			new Option("Tele to POH", () -> TeleportToHouse.teleport(player, 0)),
			new Option("More...", () -> {
				player.dialogue(new OptionsDialogue("Choose a Destination",
					new Option("POH portals", () -> {
						player.dialogue(new OptionsDialogue("Choose a Destination",
							new Option("Rimmington", () -> teleport(player, 2954, 3224)),
							new Option("Taverley", () -> teleport(player, 2894, 3465)),
							new Option("Pollnivneach", () -> teleport(player, 3340, 3004)),
							new Option("Hosidius", () -> teleport(player, 1743, 3517)),
							new Option("More...", () -> {
								player.dialogue(new OptionsDialogue("Choose a Destination",
									new Option("Rellekka", () -> teleport(player, 2670, 3632)),
									new Option("Brimhaven", () -> teleport(player, 2758, 3178)),
									new Option("Yanille", () -> teleport(player, 2544, 3095)),
									new Option("NeverMind", player::closeDialogue)));

							})));
					}),
					new Option("Farming Guild", () -> teleport(player, 1247, 3729)),
					new Option("Hunter", () -> {
						player.dialogue(new OptionsDialogue("Choose a Destination",
							new Option("Carnivorous Chinchompas", () -> teleport(player, 2558, 2936)),
							new Option("Black Chinchompas", () -> {
								player.dialogue(new OptionsDialogue("Are you sure you wish to teleport to level 32 wilderness?",
									new Option("Yes", () -> {
										teleport(player, 3146, 3774);
									}),
									new Option("No", player::closeDialogue)));
							})));
					}), new Option("NeverMind", player::closeDialogue)));

			})));
	}

	private static void maxCapeFeaturesInv(Player player, Item item) {
		player.dialogue(new OptionsDialogue("Select a Option",
			new Option("Toggle Ring of Life", () -> {
				player.dialogue(new OptionsDialogue("Choose a Option",
					new Option("Enable", () -> {
						player.capeRoLEffect = true;
						player.sendMessage("Your cape will act as a ring of life.");
					}),
					new Option("Disable", () -> {
						player.capeRoLEffect = false;
						player.sendMessage("Your cape will not act as a ring of life.");
					})));
			}),
			new Option("Toggle Ring of Life respawn", () -> {
				player.dialogue(new OptionsDialogue("Choose a Option",
					new Option("Edgeville", () -> {
						// SpawnPosition.setPosition(player, TeleportConstants.HOME);
						player.sendMessage(FormatMessage.sendColoredMessage("You set your location to Edgeville.",
							FormatMessage.Colors.DISCORD_PURPLE));
					}),
					new Option("Donator zone", () -> {
						// SpawnPosition.setPosition(player, TeleportConstants.DONATOR_ZONE);
						player.sendMessage(FormatMessage.sendColoredMessage("You set your location to Donator zone.",
							FormatMessage.Colors.DISCORD_PURPLE));
					})));
			}), new Option("NeverMind", player::closeDialogue)));
	}

	private static void otherMaxCapeTeleports(Player player, Item item) {
		player.dialogue(new OptionsDialogue("Choose a Destination",
			new Option("Farming guild", () -> teleport(player, 1247, 3729)),
			new Option("Hunter", () -> {
				player.dialogue(new OptionsDialogue("Choose a Destination",
					new Option("Carnivorous Chinchompas", () -> teleport(player, 2558, 2936)),
					new Option("Black Chinchompas", () -> {
						player.dialogue(new OptionsDialogue("Are you sure you wish to teleport to level 32 wilderness?",
							new Option("Yes", () -> {
								teleport(player, 3146, 3774);
							}),
							new Option("No", player::closeDialogue)));
					})));
			})));
	}

	private static void fishingTeleports(Player player, Item item) {
		player.dialogue(new OptionsDialogue("Choose a Destination",
			new Option("Fishing guild", () -> teleport(player, 2590, 3417)),
			new Option("Ottos grotto", () -> teleport(player, 2505, 3488)),
			new Option("NeverMind", player::closeDialogue)));
	}

	private static void teleportToMythGuild(Player player, Item item) {
		teleport(player, 2458, 2852);
	}

	private static void teleportToMonastery(Player player, Item item) {
		player.getMovement().startTeleport(e -> {
			player.animate(3867);
			player.graphics(1237);
			e.delay(3);
			player.getMovement().teleport(2606, 3221, 0);
		});
	}

	private static void teleportToArdougneFarm(Player player, Item item) {
		player.getMovement().startTeleport(e -> {
			player.animate(3872);
			player.graphics(1238);
			e.delay(3);
			player.getMovement().teleport(2673, 3375, 0);

		});
	}

	private static void defenceCapeEffect(Player player, Item item) {
		player.dialogue(new OptionsDialogue("Choose a Option",
			new Option("Enable", () -> {
				player.capeRoLEffect = true;
				player.sendMessage("Your cape will act as a ring of life.");
			}),
			new Option("Disable", () -> {
				player.capeRoLEffect = false;
				player.sendMessage("Your cape will not act as a ring of life.");
			})));
	}

	private static void rolRespawn(Player player, Item item) {
		player.dialogue(new OptionsDialogue("Choose a Option",
			new Option("Edgeville", () -> {
				// SpawnPosition.setPosition(player, TeleportConstants.HOME);//TODO NATE
				player.sendMessage(FormatMessage.sendColoredMessage("You set your location to Edgeville.",
					FormatMessage.Colors.DISCORD_PURPLE));
			}),
			new Option("Donator zone", () -> {
				// SpawnPosition.setPosition(player, TeleportConstants.DONATOR_ZONE);//TODO NATE
				player.sendMessage(FormatMessage.sendColoredMessage("You set your location to Donator Zone.",
					FormatMessage.Colors.DISCORD_PURPLE));
			})));
	}

	private static boolean teleport(Player player, Item item) {
		return player.getMovement().startTeleport(e -> {
			player.animate(714);
			player.graphics(111, 92, 0);
			player.publicSound(200);
			e.delay(3);
			player.getMovement().teleport(2730, 3412, 0);
		});
	}

	private static void teleportToKourendWoodland(Player player, Item item) {
		teleport(player, 1554, 3456);
	}

	private static void teleportToMountKaruulm(Player player, Item item) {
		teleport(player, 1311, 3805);
	}

	public static boolean wearsAttackCape(Player player) {
		int cape = player.getEquipment().getId(Equipment.SLOT_CAPE);
		return cape == 9747 || cape == 9748;
	}

	public static boolean wearsCookingCape(Player player) {
		int cape = player.getEquipment().getId(Equipment.SLOT_CAPE);
		return cape == 9801 || cape == 9802;
	}

	public static boolean wearsFarmingCape(Player player) {
		int cape = player.getEquipment().getId(Equipment.SLOT_CAPE);
		return cape == 9810 || cape == 9811;
	}

	public static boolean wearsHPCape(Player player) {
		int cape = player.getEquipment().getId(Equipment.SLOT_CAPE);
		return cape == 9768 || cape == 9769;
	}

	public static boolean wearsThievingCape(Player player) {
		int cape = player.getEquipment().getId(Equipment.SLOT_CAPE);
		return cape == 9777 || cape == 9778;
	}

	public static boolean wearsWoodcuttingCape(Player player) {
		int cape = player.getEquipment().getId(Equipment.SLOT_CAPE);
		return cape == 9807 || cape == 9808;
	}

	public static boolean wearsMiningCape(Player player) {
		int cape = player.getEquipment().getId(Equipment.SLOT_CAPE);
		return cape == 9792 || cape == 9793;
	}

	private static void init2(Player player, Item item) {
		if (DuelRule.NO_DRINKS.isToggled(player)) {
			player.sendMessage("You cannot use your Tainted wand's power up with no drinks enabled.");
			return;
		}
		if (player.magicTaintedWandCooldown.isDelayed()) {
			int delay = player.magicTaintedWandCooldown.remaining();
			if (delay >= 100) {
				int minutes = delay / 100;
				player.sendMessage(Color.DARK_RED,
					"The wand isn't ready for you to siphon any more energy. Judging by how it feels, it will be ready in around "
						+ minutes + " minutes.");
			} else {
				int seconds = delay / 10 * 6;
				player.sendMessage(Color.DARK_RED,
					"The wand isn't ready for you to siphon any more energy. Judging by how it feels, it will be ready in around "
						+ seconds + " seconds.");
			}
		} else {
			player.graphics(1398);
			player.animate(533);
			VarPlayerRepository.TAINTED_WAND_COOLDOWN.set(player, 70);
			player.magicTaintedWandCooldown.delay(700);
			player.sendMessage(Color.DARK_RED, "While the power of the magi knocks you down, it smiles upon you.");
			player.getStats().get(StatType.Magic).boost(1, 0.16);
			player.getStats().get(StatType.Defence).boost(1, 0.08);
			finished2(player);
		}
	}

	public static void register() {
		// Agility
		ItemAction.registerInventory(9771, "Stamina Boost", CapePerks::staminaBoost);
		ItemAction.registerInventory(9772, "Stamina Boost", CapePerks::staminaBoost);

		// Tainted Wand
		ItemAction.registerEquipment(30598, "Power", CapePerks::init2);

		// Construction
		ItemAction.registerInventory(9789, "Tele to POH", CapePerks::teleToHouse);
		ItemAction.registerInventory(9790, "Tele to POH", CapePerks::teleToHouse);
		ItemAction.registerEquipment(9789, "Tele to POH", CapePerks::teleToHouse);
		ItemAction.registerEquipment(9790, "Tele to POH", CapePerks::teleToHouse);
		ItemAction.registerInventory(9789, "Teleport", CapePerks::houseDestinations);
		ItemAction.registerInventory(9790, "Teleport", CapePerks::houseDestinations);
		ItemAction.registerEquipment(9789, "Teleport", CapePerks::houseDestinations);
		ItemAction.registerEquipment(9790, "Teleport", CapePerks::houseDestinations);

		// Crafting
		ItemAction.registerInventory(9780, "Teleport", CapePerks::teleportToCraftingGuild);
		ItemAction.registerInventory(9781, "Teleport", CapePerks::teleportToCraftingGuild);
		ItemAction.registerEquipment(9780, "Teleport", CapePerks::teleportToCraftingGuild);
		ItemAction.registerEquipment(9781, "Teleport", CapePerks::teleportToCraftingGuild);

		// Defence
		ItemAction.registerInventory(9753, "Toggle Effect", CapePerks::defenceCapeEffect);
		ItemAction.registerInventory(9754, "Toggle Effect", CapePerks::defenceCapeEffect);
		ItemAction.registerInventory(9753, "Toggle Respawn", CapePerks::rolRespawn);
		ItemAction.registerInventory(9754, "Toggle Respawn", CapePerks::rolRespawn);

		// Farming
		ItemAction.registerInventory(9810, "Teleport", CapePerks::teleportToFarmingGuild);
		ItemAction.registerInventory(9811, "Teleport", CapePerks::teleportToFarmingGuild);
		ItemAction.registerEquipment(9810, "Teleport", CapePerks::teleportToFarmingGuild);
		ItemAction.registerEquipment(9811, "Teleport", CapePerks::teleportToFarmingGuild);

		// Strength
		ItemAction.registerInventory(9750, "Teleport", CapePerks::teleportToWarriorsGuild);
		ItemAction.registerInventory(9751, "Teleport", CapePerks::teleportToWarriorsGuild);
		ItemAction.registerEquipment(9750, "Teleport", CapePerks::teleportToWarriorsGuild);
		ItemAction.registerEquipment(9751, "Teleport", CapePerks::teleportToWarriorsGuild);

		// Fishing
		ItemAction.registerInventory(9798, "Fishing Guild", CapePerks::teleportToFishingGuild);
		ItemAction.registerInventory(9799, "Fishing Guild", CapePerks::teleportToFishingGuild);
		ItemAction.registerEquipment(9798, "Fishing Guild", CapePerks::teleportToFishingGuild);
		ItemAction.registerEquipment(9799, "Fishing Guild", CapePerks::teleportToFishingGuild);
		ItemAction.registerInventory(9798, "Otto's Grotto", CapePerks::teleportToOttosGrotto);
		ItemAction.registerInventory(9799, "Otto's Grotto", CapePerks::teleportToOttosGrotto);
		ItemAction.registerEquipment(9798, "Otto's Grotto", CapePerks::teleportToOttosGrotto);
		ItemAction.registerEquipment(9799, "Otto's Grotto", CapePerks::teleportToOttosGrotto);

		// Rada Blessing 1-4
		ItemAction.registerInventory(22941, "Kourend Woodland", CapePerks::teleportToKourendWoodland);
		ItemAction.registerEquipment(22941, "Kourend Woodland", CapePerks::teleportToKourendWoodland);

		ItemAction.registerInventory(22943, "Kourend Woodland", CapePerks::teleportToKourendWoodland);
		ItemAction.registerEquipment(22943, "Kourend Woodland", CapePerks::teleportToKourendWoodland);

		ItemAction.registerInventory(22945, "Kourend Woodland", CapePerks::teleportToKourendWoodland);
		ItemAction.registerEquipment(22945, "Kourend Woodland", CapePerks::teleportToKourendWoodland);
		ItemAction.registerInventory(22945, "Mount Karuulm", CapePerks::teleportToMountKaruulm);
		ItemAction.registerEquipment(22945, "Mount Karuulm", CapePerks::teleportToMountKaruulm);

		ItemAction.registerInventory(22947, "Kourend Woodland", CapePerks::teleportToKourendWoodland);
		ItemAction.registerEquipment(22947, "Kourend Woodland", CapePerks::teleportToKourendWoodland);
		ItemAction.registerInventory(22947, "Mount Karuulm", CapePerks::teleportToMountKaruulm);
		ItemAction.registerEquipment(22947, "Mount Karuulm", CapePerks::teleportToMountKaruulm);

		// ARDY CLOAK 1-4 & Ardy Max Cape
		ItemAction.registerInventory(13121, "Monastery Teleport", CapePerks::teleportToMonastery);
		ItemAction.registerEquipment(13121, "Kandarin Monastery", CapePerks::teleportToMonastery);

		ItemAction.registerInventory(13122, "Monastery Teleport", CapePerks::teleportToMonastery);
		ItemAction.registerEquipment(13122, "Kandarin Monastery", CapePerks::teleportToMonastery);
		ItemAction.registerInventory(13122, "Farm Teleport", CapePerks::teleportToArdougneFarm);
		ItemAction.registerEquipment(13122, "Ardougne Farm", CapePerks::teleportToArdougneFarm);

		ItemAction.registerInventory(13123, "Monastery Teleport", CapePerks::teleportToMonastery);
		ItemAction.registerInventory(13123, "Farm Teleport", CapePerks::teleportToArdougneFarm);
		ItemAction.registerEquipment(13123, "Kandarin Monastery", CapePerks::teleportToMonastery);
		ItemAction.registerEquipment(13123, "Ardougne Farm", CapePerks::teleportToArdougneFarm);

		ItemAction.registerInventory(13124, "Monastery Teleport", CapePerks::teleportToMonastery);
		ItemAction.registerInventory(13124, "Farm Teleport", CapePerks::teleportToArdougneFarm);
		ItemAction.registerEquipment(13124, "Kandarin Monastery", CapePerks::teleportToMonastery);
		ItemAction.registerEquipment(13124, "Ardougne Farm", CapePerks::teleportToArdougneFarm);

		ItemAction.registerInventory(20760, "Monastery Teleport", CapePerks::teleportToMonastery);
		ItemAction.registerInventory(20760, "Farm Teleport", CapePerks::teleportToArdougneFarm);
		ItemAction.registerEquipment(20760, "Kandarin Monastery", CapePerks::teleportToMonastery);
		ItemAction.registerEquipment(20760, "Ardougne Farm", CapePerks::teleportToArdougneFarm);

		// Max Cape
		ItemAction.registerInventory(13342, "Teleports", CapePerks::maxCapeInvTeleports);
		ItemAction.registerInventory(13342, "Features", CapePerks::maxCapeFeaturesInv);
		ItemAction.registerEquipment(13342, "Warriors' Guild", CapePerks::teleportToWarriorsGuild);
		ItemAction.registerEquipment(13342, "Fishing Teleports", CapePerks::fishingTeleports);
		ItemAction.registerEquipment(13342, "Crafting Guild", CapePerks::teleportToCraftingGuild);
		ItemAction.registerEquipment(13342, "Tele to POH", CapePerks::teleToHouse);
		ItemAction.registerEquipment(13342, "POH portals", CapePerks::houseDestinations);
		ItemAction.registerEquipment(13342, "Other Teleports", CapePerks::otherMaxCapeTeleports);
		ItemAction.registerEquipment(13342, "Spellbook", MagicSkillcape::swapSelection);
		ItemAction.registerEquipment(13342, "Features", CapePerks::maxCapeFeaturesInv);

		// Myth cape
		ItemAction.registerInventory(22114, "Teleport", CapePerks::teleportToMythGuild);
		ItemAction.registerEquipment(22114, "Teleport", CapePerks::teleportToMythGuild);

		// Kandarian headgear
		ItemAction.registerInventory(13140, "teleport", CapePerks::teleport);
		ItemAction.registerEquipment(13140, "teleport", CapePerks::teleport);
	}

}
