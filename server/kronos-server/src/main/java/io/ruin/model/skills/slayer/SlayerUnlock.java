package io.ruin.model.skills.slayer;

import io.ruin.cache.EnumMap;

import io.ruin.cache.ObjType;
import io.ruin.cache.StructType;
import io.ruin.model.World;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;

import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SlotAction;

import io.ruin.model.var.VarPlayerRepository;
import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public enum SlayerUnlock {
	//@formatter:off
	GARGOYLE_SMASHER(VarPlayerRepository.GARGOYLE_SMASHER, 120, 0),
	SLUG_SALTER(VarPlayerRepository.SLUG_SALTER, 80, 1),
	REPTILE_FREEZER(VarPlayerRepository.REPTILE_FREEZER, 90, 2),
	SHROOM_SPRAYER(VarPlayerRepository.SHROOM_SPRAYER, 110, 3),
	BROADER_FLETCHING(VarPlayerRepository.BROADER_FLETCHING, 300, 7),
	MALEVOLENT_MASQUERADE(VarPlayerRepository.SLAYER_UNLOCKED_HELM, 400, 5),
	RING_BLING(VarPlayerRepository.RING_BLING, 300, 6),
	SEEING_RED(VarPlayerRepository.SEEING_RED, 50, 34),
	I_HOPE_YOU_MITH_ME(VarPlayerRepository.I_HOPE_YOU_MITH_ME, 80, 16),
	WATCH_THE_BIRDIE(VarPlayerRepository.WATCH_THE_BIRDIE, 80, 17),
	HOT_STUFF(VarPlayerRepository.HOT_STUFF, 100, 18),
	REPTILE_GOT_RIPPED(VarPlayerRepository.REPTILE_GOT_RIPPED, 75, 30),
	LIKE_A_BOSS(VarPlayerRepository.LIKE_A_BOSS, 200, 19),
	BIGGER_AND_BADDER(VarPlayerRepository.BIGGER_AND_BADDER, 150, 35),
	KING_BLACK_BONNET(VarPlayerRepository.KING_BLACK_BONNET, 1000, 31),
	KALPHITE_KHAT(VarPlayerRepository.KALPHITE_KHAT, 1000, 32),
	UNHOLY_HELMET(VarPlayerRepository.UNHOLY_HELMET, 1000, 33),
	DARK_MANTLE(VarPlayerRepository.DARK_MANTLE, 1000, 38),
	UNDEAD_HEAD(VarPlayerRepository.UNDEAD_HEAD, 1000, 42),
	USE_MORE_HEAD(VarPlayerRepository.USE_MORE_HEAD, 1000, 45),
	DULY_NOTED(VarPlayerRepository.UNLOCK_DULY_NOTED, 200, 37),
	// STOP_THE_WYVERN(VarPlayerRepository.STOP_THE_WYVERN, 500, 37),
	DOUBLE_TROUBLE(VarPlayerRepository.DOUBLE_TROUBLE, 500, 44),
	BASILOCKED(VarPlayerRepository.BASILOCKED, 80, 47),
	TWISTEDVISION(VarPlayerRepository.TWISTEDVISION, 1000, 48),
	VAMPYRESLAYER(VarPlayerRepository.ACTUALVAMPSLAY, 80, 50),
	TASKSTORAGE(VarPlayerRepository.TASKSTORAGE, 1000, 51),
	IWILDYMORESLAYER(VarPlayerRepository.WILDYMORESLAY, 0, 52),
	NEED_MORE_DARKNESS(VarPlayerRepository.NEED_MORE_DARKNESS, 100, 4, true),
	ANKOU_VERY_MUCH(VarPlayerRepository.ANKOU_VERY_MUCH, 100, 8, true),
	SUQANOTHER_ONE(VarPlayerRepository.SUQ_ANOTHER_ONE, 100, 9, true),
	FIRE_AND_DARKNESS(VarPlayerRepository.FIRE_AND_DARKNESS, 50, 10, true),
	PEDAL_TO_THE_METALS(VarPlayerRepository.PEDAL_TO_THE_METALS, 100, 11, true),
	I_REALLY_MITH_YOU(VarPlayerRepository.I_REALLY_MITH_YOU, 120, 23, true),
	// ADAMIND_SOME_MORE(VarPlayerRepository.ADAMIND_SOME_MORE, 100, 40, true),
	// RUUUUUNE(VarPlayerRepository.RUUUUUNE, 100, 41, true),
	SPIRITUAL_FERVOUR(VarPlayerRepository.SPIRITUAL_FERVOUR, 100, 12, true),
	BIRDS_OF_A_FEATHER(VarPlayerRepository.BIRDS_OF_A_FEATHER, 100, 22, true),
	GREATER_CHALLENGER(VarPlayerRepository.GREATER_CHALLENGE, 100, 15, true),
	ITS_DARK_IN_HERE(VarPlayerRepository.ITS_DARK_IN_HERE, 100, 14, true),
	BLEED_ME_DRY(VarPlayerRepository.BLEED_ME_DRY, 75, 20, true),
	SMELL_YA_LATER(VarPlayerRepository.SMELL_YA_LATER, 100, 21, true),
	HORRORFIC(VarPlayerRepository.HORRORIFIC, 100, 24, true),
	TO_DUST_YOU_SHALL_RETURN(VarPlayerRepository.TO_DUST_YOU_SHALL_RETURN, 100, 25, true),
	WYVERNOTHER_ONE(VarPlayerRepository.WYVER_NOTHER_ONE, 100, 26, true),
	GET_SMASHED(VarPlayerRepository.GET_SMASHED, 100, 27, true),
	NECHS_PLEASE(VarPlayerRepository.NECHS_PLEASE, 100, 28, true),
	AUGMENT_MY_ABBIES(VarPlayerRepository.AUGMENT_MY_ABBIES, 100, 13, true),
	KRACK_ON(VarPlayerRepository.KRACK_ON, 100, 29, true),
	GET_SCABARIGHT_ON_IT(VarPlayerRepository.GET_SCABARIGHT_ON_IT, 50, 36, true),
	WYVERNOTHER_TWO(VarPlayerRepository.WYVER_NOTHER_TWO, 100, 39, true),
	BASILONGER(VarPlayerRepository.BASILONGER, 100, 46, true),
	MORE_EYES_THAN_SENSE(VarPlayerRepository.SLAYER_LONGER_ARAXYTES, 150, 55, true),
	EYE_SEE_YOU(VarPlayerRepository.EYE_SEE_YOU_VARP, 1000, 56),
	;
	//@formatter:on

	final VarPlayerRepository config;
	final int price;
	final int slot;
	final boolean extension;

	SlayerUnlock(VarPlayerRepository config, int price, int slot, boolean extension) {
		this.config = config;
		this.price = price;
		this.extension = extension;
		this.slot = slot;
	}

	SlayerUnlock(VarPlayerRepository config, int price, int slot) {
		this(config, price, slot, false);
	}

	void toggle(Player player) {
		if (config.get(player) == 1) {
			config.set(player, 0);
		} else if (VarPlayerRepository.SLAYER_POINTS.get(player) < price) {
			player.sendMessage("You don't have enough slayer points to make that purchase.");
		} else {
			config.set(player, 1);
			VarPlayerRepository.SLAYER_POINTS.set(player,
					VarPlayerRepository.SLAYER_POINTS.get(player) - price);
			player.sendMessage(extension ? "Extension" : "Unlock" + " purchased.");
			if (extension) {
				player.slayerTasksBlockedOrExtended++;

			}
		}
	}

	private static final Map<Integer, SlayerUnlock> UNLOCKS = new HashMap<>();

	public static void register() {
		for (SlayerUnlock slayerUnlock : values()) {
			UNLOCKS.put(slayerUnlock.slot, slayerUnlock);
		}
		InterfaceHandler.register(426, handler -> {
			handler.actions[8] = (SlotAction) SlayerUnlock::handleUnlock;
			handler.actions[23] = (DefaultAction) SlayerUnlock::buyItem;
		});
	}

	private static void handleUnlock(Player player, int slot) {
		SlayerUnlock unlock = UNLOCKS.get(slot);
		if (unlock != null) {
			unlock.toggle(player);
		} else if (slot == 58) {
			cancelTask(player);
		} else if (slot == 59) {
			blockTask(player);
		}

		switch (slot) {
			case 65 -> {
				unblockTask(player, 6);
			}
			case 69 -> {
				unblockTask(player, 5);
			}
			case 64 -> {
				unblockTask(player, 4);
			}
			case 63 -> {
				unblockTask(player, 3);
			}
			case 62 -> {
				unblockTask(player, 2);
			}
			case 61 -> {
				unblockTask(player, 1);
			}
			case 60 -> {
				unblockTask(player, 0);
			}
		}

		refreshInterface(player);
	}

	private static void extendAll(Player player) {
		int cost = UNLOCKS.entrySet().stream()
				.map(Map.Entry::getValue)
				.filter(unlock -> unlock.extension && unlock.config.get(player) == 0)
				.mapToInt(unlock -> unlock.price)
				.sum();
		int pts = VarPlayerRepository.SLAYER_POINTS.get(player);
		if (pts < cost) {
			player.sendMessage("You do not have enough points to make that purchase.");
			return;
		}
		VarPlayerRepository.SLAYER_POINTS.set(player, pts - cost);
		UNLOCKS.entrySet().stream()
				.map(Map.Entry::getValue)
				.filter(unlock -> unlock.extension && unlock.config.get(player) == 0)
				.forEach(unlock -> unlock.config.set(player, 1));
		player.sendMessage("Purchase complete, all tasks extended.");
	}

	private static void unblockTask(Player player, int slot) {
		VarPlayerRepository.BLOCKED_TASKS[slot].set(player, 0);
	}

	public static void cancelTask(Player player) {
		SlayerCreature task = SlayerCreature.lookup(VarPlayerRepository.SLAYER_TASK.get(player));

		if (task == null) {
			player.sendMessage("You don't have a slayer task to block.");
			return;
		}

		int cost = 30;

		if (VarPlayerRepository.SLAYER_POINTS.get(player) < cost) {
			player.sendMessage("You need 30 points to cancel your task.");
			return;
		}

		VarPlayerRepository.SLAYER_POINTS.set(player,
				VarPlayerRepository.SLAYER_POINTS.get(player) - cost);
		VarPlayerRepository.SLAYER_TASK_AMOUNT.set(player, 0);
		VarPlayerRepository.SLAYER_TASK.set(player, 0);
		player.totalSlayerTasksSkipped++;

		player.sendMessage("You have successfully cancelled your task.");
	}

	private static void blockTask(Player player) {
		SlayerCreature task = SlayerCreature.lookup(VarPlayerRepository.SLAYER_TASK.get(player));

		if (task == null) {
			player.sendMessage("You don't have a slayer task to block.");
			return;
		}
		if (VarPlayerRepository.SLAYER_POINTS.get(player) < 100) {
			player.sendMessage("You need 100 points to block your task.");
			return;
		}

		if (task == SlayerCreature.BOSSES)
			return;

		// Grab our current task ID
		final int taskId = VarPlayerRepository.SLAYER_TASK.get(player);

		// Check if the player has already blocked this task
		if (blocked(player, taskId)) {
			player.sendMessage("You are already blocking " + taskName(player, taskId) + ".");
			return;
		}

		// Check for an available slot and block
		for (VarPlayerRepository i : VarPlayerRepository.BLOCKED_TASKS) {
			if (i.get(player) == 0) {
				i.set(player, taskId);

				VarPlayerRepository.SLAYER_TASK_AMOUNT.set(player, 0);
				VarPlayerRepository.SLAYER_TASK.set(player, 0);

				int pts = VarPlayerRepository.SLAYER_POINTS.get(player) - 100;
				player.slayerTasksBlockedOrExtended++;

				VarPlayerRepository.SLAYER_POINTS.set(player, pts);
				player.sendMessage("You have successfully blocked your task.");
				return;
			}
		}
	}

	public static void openRewards(Player player) {
		player.getPacketSender().sendClientScript(917, "ii", -1, -1);
		player.openInterface(ToplevelComponent.MAINMODAL, 426);
		player.getPacketSender().sendIfEvents(426, 8, 0, 69, 2);
		player.getPacketSender().sendIfEvents(426, 23, 0, 8, 1052);
		refreshInterface(player);
	}

	private static void refreshInterface(Player player) {
		var left = VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player);
		var taskIndex = VarPlayerRepository.SLAYER_TASK.get(player);

		if (VarPlayerRepository.BOSS_TASK.get(player) != 0) {
		} else {
			VarPlayerRepository.SLAYER_REWARDS_ASSIGNMENT_COUNT.set(player, left);
			VarPlayerRepository.SLAYER_REWARDS_TASK_INDEX.set(player, taskIndex);
		}
	}

	/**
	 * Returns true if the given task is blocked
	 */
	public static boolean blocked(Player player, int taskId) {
		for (VarPlayerRepository i : VarPlayerRepository.BLOCKED_TASKS) {
			if (i.get(player) == taskId) {
				return true;
			}
		}
		return false;
	}

	private static void buyItem(Player player, int option, int slot, int itemId) {
		if (slot < 0
				|| slot >= (World.isEco() ? ShopItem.values().length : PVPShopItem.values().length))
			return;
		int itemID, itemPrice, itemAmount;
		if (World.isEco()) {
			ShopItem item = ShopItem.values()[slot];
			itemID = item.id;
			itemPrice = item.price;
			itemAmount = item.buyAmount;
		} else {
			PVPShopItem item = PVPShopItem.values()[slot];
			itemID = item.id;
			itemPrice = item.price;
			itemAmount = item.buyAmount;
		}

		if (option == 10) {
			player.sendMessage(ObjType.get(itemID).examine);
			return;
		}
		int pts = VarPlayerRepository.SLAYER_POINTS.get(player);
		if (pts < itemPrice) {
			player.sendMessage("You don't have enough slayer points to buy that.");
			return;
		}
		int amount = ((option == 2) ? 1 : ((option == 3) ? 5 : 10));
		if (ObjType.get(itemID).stackable && !player.getInventory().hasRoomFor(itemID)) {
			player.sendMessage("Not enough space in your inventory.");
		} else if (!ObjType.get(itemID).stackable && player.getInventory().isFull()) {
			player.sendMessage("Not enough space in your inventory.");
		}
		amount = Math.min(amount, pts / itemPrice);
		player.getInventory().add(itemID, amount * itemAmount);
		VarPlayerRepository.SLAYER_POINTS.set(player, pts - (amount * itemPrice));
	}

	public static String tipFor(SlayerCreature task) {
		switch (task.getUid()) {
			case 2:
				return "Goblins can be found at the Goblin Village, Stronghold of Security, and Lumbridge.";
			case 3:
				return "Rats can be found inside the Taverley and Edgeville dungeon, and around Lumbridge.";
			case 4:
				return "Spiders can found inside the Taverley and Edgeville dungeon, and are commonly found around Lumbridge, Varrock, and Falador. Antipoison is recommend (only if fighting thepoisonous variants)";
			case 5:
				return "Birds are commonly found at the Lumbridge or Falador chicken coops.";
			case 6:
				return "Cows can be slaughtered on the Lumbridge or Falador grazing land.";
			case 7:
				return "Scorpions can be slayed inside the Dwarven mines or around the Karamja Volcano.";
			case 8:
				return "Bats can be killed inside the Taverley dungeon or outside of the Mage Arena. Be aware of PKers outside the Mage Arena though, as that is a very popular PVP area.";
			case 9:
				return "Wolves can be killed on the White Wolf Mountain or at the Wilderness Agilitycourse. Be aware of PVPers as this is a popular PVP area.";
			case 10:
				return "Zombies can be slayed inside the Edgeville Dungeon. ";
			case 11:
				return "Skeletons can be found inside the Edgeville, Taverley, or Waterfall dungeon.";
			case 12:
				return "Ghosts can be killed inside the Taverley dungeon.";
			case 13:
				return "Bears can be found around the Ardougne mines or low-to-mid level Wilderness. (Careful for PK'ers!)";
			case 14:
				return "Hill Giants can be found inside the Edgeville and Taverley dungeon, or northeastof the Chaos Temple inside the Wilderness.";
			case 15:
				return "Ice Giants can be found inside the Asgarnian Ice Caves or by the level 44 Obeliskinside the wilderness. Beware of PKers though, as this is a very popular PVP area.";
			case 16:
				return "Fire Giants can be found inside the Stronghold Slayer, Brimhaven, and Waterfall dungeon.";
			case 17:
			case 18:
			case 20:
			case 22:
			case 23:
			case 26:
			case 32:
			case 33:
			case 34:
			case 38:
			case 40:
			case 43:
			case 53:
			case 54:
			case 55:
			case 56:
			case 57:
			case 61:
			case 62:
			case 63:
			case 64:
			case 65:
			case 67:
			case 68:
			case 69:
			case 70:
			case 71:
			case 73:
			case 74:
			case 78:
			case 81:
			case 82:
			case 83:
			case 84:
			case 85:
			case 86:
			case 87:
			case 88:
			case 89:
			case 90:
			case 93:
			case 94:
			case 97:
			case 99:
			case 100:
			case 101:
			case 102:
			case 103:
			case 105:
			default:
				return "I've got nothing, mate.";
			case 19:
				return "Ice Warriors can be found inside the Asgarnian Ice Caves or by the level 44 Obelisk inside the wilderness. Beware of PKers though, as this is a very popular PVP area.";
			case 21:
				return "Hobgoblins can be found inside the Asgarnian Ice and Godwars dungeon.";
			case 24:
				return "Green Dragons can be found North of Goblin Village in 13 Wilderness, between the Ruins and Graveyard of Shadows in level 24 Wilderness, west of the Bone Yard,between the Lava Maze and Hobgoblin mine, and north east of the Chaos Temple. Bewareof the PKers here though, as they're extremely popular PVP areas. A Anti-dragon shield, Dragonfire shield or Antifire potion and a good Stab (Abyssal whip or Dragon scimitar works too!) or Ranged weapon is recommended.";
			case 25:
				return "Blue Dragons can be found inside the Taverley Dungeon. A Anti-dragon shield, Dragonfire shield or Antifire potion and a good Stab (Abyssal whip or Dragon scimitar works too!) or Ranged weapon is recommended.";
			case 27:
				return "Black Dragons can be found inside the Taverley, or Brimhaven dungeon. A Anti-dragon shield, Dragonfire shield or Antifire potion and a good Stab (Abyssal whip or Dragon scimitar works too!) or Ranged weapon is recommended.";
			case 28:
				return "Lesser demons can be found scattered throughout the wild or near a dungeon close to mage bank.";
			case 29:
				return "Greater Demons can be found inside the Strongold Slayer Cave, at the Brimhaven Dungeon, or inside the wilderness at the Demonic Ruins. Be aware of PVPers at the Demonic Ruins, though, as it is a very popular PVP area.";
			case 30:
				return "Black Demons can be found at the Taverley, Edgeville, or Brimhaven Dungeon. Good gear and/or protection from melee, with several prayer potions is highly recommended.";
			case 31:
				return "Hell Hounds can be killed inside the Stronghold Slayer cave, or at the Taverley Dungeon.";
			case 35:
				return "Dagannoths can be killed at the Waterbirth Island.";
			case 36:
				return "Turoths can be killed at the Fremennik Slayer Dungeon. Broad arrows/bolts with a strong range weapon is recommended.";
			case 37:
				return "Cave Crawlers can be found at the Fremennik Slayer Dungeon. Anti poision and a weapon using slash attacks are extremely recommended.";
			case 39:
				return "Crawling Hands are located at the Slayer Tower.";
			case 41:
				return "Aberrant spectres are found at the Stronghold Slayer Cave, or at the Slayer Tower. A Nose pegor Slayer helmet is extremely recommended.";
			case 42:
				return "Abyssal Demons can be found at the Stronghold Slayer cave, or at the Slayer Tower. Good gear and weapon is highly recommended when fighting these monsters.";
			case 44:
				return "Cockatrices can be found at the Fremennik Slayer Dungeon. A mirror shield is highly recommended.";
			case 45:
				return "Kurasks can be found at the Fremennik Slayer Dungeon. A leaf-bladed weapon or Broad arrows/bolts are highly recommend when fighting these monsters.";
			case 46:
				return "Gargoyles can be found at the Stronghold Slayer Cave or the Slayer Tower. A Rock Hammer is required to kill these monsters.";
			case 47:
				return "Pyrefiends can be found at the Fremennik Slayer, or Smoke Dungeon. Magic-resistant armour is recommended.";
			case 48:
				return "Bloodvelds can be killed at the Stronghold Slayer Cave or Slayer Tower. Dragonhide armour and a decent weapon is recommend when fighting these monsters.";
			case 49:
				return "Dust Devils can be killed at the Smoke Dungeon.";
			case 50:
				return "Jellies can be found at the Fremennik Slayer Dungeon. Magic-resistant armour is recommended.";
			case 51:
				return "Rockslugs can be found at the Fremennik Slayer Dungeon. A bag of salt is required to finish these monsters.";
			case 52:
				return "Nechryaels can be found at the Stronghold Slayer Cave or Slayer Tower.";
			case 58:
				return "Bronze Dragons can be found at the Stronghold Slayer Cave or Brimhaven Dungeon. A Anti-dragon shield, Dragonfire shield or Antifire potion and a good Stab (Abyssal whip or Dragon scimitar works too!) or Fire Bold (or better) is recommended.";
			case 59:
				return "Iron Dragons can be found at the Stronghold Slayer Cave or Brimhaven Dungeon. A Anti-dragon shield, Dragonfire shield or Antifire potion and a good Stab (Abyssal whip or Dragon scimitar works too!) or Fire Bold (or better) is recommended.";
			case 60:
				return "Steel Dragons can be found at the Stronghold Slayer Cave or Brimhaven Dungeon. A Anti-dragon shield, Dragonfire shield or Antifire potion and a good Stab (Abyssal whip or Dragon scimitar works too!) or Fire Bold (or better) is recommended.";
			case 66:
				return "Dark Beasts can be found inside the Mourner Tunnels. Good armour/weapon is recommended when fighting these monsters.";
			case 72:
				return "Skeletal Wyverns can be found in the Asgarnian Ice Dungeon. Protect from Range, Mirror Shields, and good armour/weapon is suggested when fighting these monsters.";
			case 75:
				return "Icefiends can be found inside the Godwars Dungeon.";
			case 76:
				return "Minotaurs can be found inside the Godwars Dungeon or on the 1st level of the Stronghold of Security.";
			case 77:
				return "Flesh Crawlers can be found on the 2nd level of the Stronghold of Security.";
			case 79:
				return "Ankous can be found in the Stronghold Slayer Cave, on the 4th level of the Stronghold of Security, and at the Forgotten Cemetery.";
			case 80:
				return "Cave Horrors can be found at Mos Le'Harmless Caves.";
			case 91:
				return "Magic axes can be found in a hut east of mage bank. A lockpick is required to get in.";
			case 92:
				return "Cave Krakens can be awaken at the Stronghold Slayer Cave. A Magic weapon is preferred, as Ranged his are heavily reduced. You're unable to use Melee on these monsters.";
			case 95:
				return "Smoke Devils can be found at the Stronghold Slayer Cave. A Slayer helm or facemask is suggested when fighting these monsters.";
			case 96:
				return "Tzhaar monsters can be found inside the Tzhaar Cave.";
			case 98:
				return "A boss task in W2 counts any major wilderness boss you may encounter. Vet'ion, Chaos Fanatic, Callisto and so on are all valid.";
			case 104:
				return "Lava dragons are a strong breed located north-east of black chinchompas. A form of anti-dragon shield is strongly recommended.";
			case 106:
				return "Fossil Island Wyverns aren't located on Fossil Island in this world. They're near an altar close to the KBD entrance.";
		}
	}

	public static String tipForWilderness(SlayerCreature task) {
		switch (task.getUid()) {
			case 2:
			case 3:
			case 5:
			case 6:
			case 8:
			case 9:
			case 10:
			case 12:
			case 14:
			case 17:
			case 18:
			case 20:
			case 21:
			case 22:
			case 23:
			case 25:
			case 26:
			case 32:
			case 33:
			case 34:
			case 35:
			case 36:
			case 37:
			case 38:
			case 39:
			case 40:
			case 41:
			case 42:
			case 43:
			case 44:
			case 45:
			case 46:
			case 47:
			case 48:
			case 49:
			case 50:
			case 51:
			case 52:
			case 53:
			case 54:
			case 55:
			case 56:
			case 57:
			case 58:
			case 59:
			case 60:
			case 61:
			case 62:
			case 63:
			case 64:
			case 65:
			case 66:
			case 67:
			case 68:
			case 69:
			case 70:
			case 71:
			case 72:
			case 73:
			case 74:
			case 75:
			case 76:
			case 77:
			case 78:
			case 81:
			case 82:
			case 83:
			case 84:
			case 85:
			case 86:
			case 87:
			case 88:
			case 89:
			case 90:
			case 93:
			case 94:
			case 96:
			case 97:
			case 99:
			case 100:
			case 101:
			case 102:
			case 103:
			case 105:
			case 106:
			default:
				return "I don't have much knowledge on this subject for you.";

			case 4:
				return "Spiders are located in the Lava Maze Dungeon, Edgeville Dungeon, and north-west of Lava Dragon Isle.";
			case 7:
				return "Scorpions are scattered through-out the lower levels of the wilderness.";
			case 11:
				return "Skeletons are located in the Bone Yard, Edgeville Dungeon, and Southern Wilderness Mine.";
			case 13:
				return "Bears can be found scattered in the lower levels of the wilderness.";
			case 15:
				return "Ice Giants can be found among the trees on the Frozen Waste Plateau or in the Revenant Caves.";
			case 16:
				return "Fire Giants can be found inside the Deep Wilderness Dungeon. Escape is extremely hard so be-careful.";
			case 19:
				return "Ice Warriors can only be found on the barrens of the Frozen Waste Plateau.";
			case 24:
				return "Green Dragons can be located near the Dark Warrior's Fort, south-west of the Bone Yard, and in the Revenant Caves.";
			case 27:
				return "Black Dragons can be found in the Revenant Caves and near the end of the Lava Maze Dungeon. Don't forget Anti-fire protection of some sort.";
			case 28:
				return "Lesser demons can be found scattered throughout the wild or near a dungeon close to Mage Bank.";
			case 29:
				return "Greater Demons can be found in the Demonic Ruins, Lava Maze Dungeon, and Revenant Caves.";
			case 30:
				return "Black Demons can be found hidden in the Edgeville Wilderness Dungeon. Be extremely careful.";
			case 31:
				return "Hellhounds can be found inside the Revenant Cave or north-east of the Deserted Keep.";
			case 79:
				return "Ankou can be found in Forgotten Cemetery or Revenant Caves.";
			case 80:
			case 91:
				return "Magic axes can be found in a hut east of Mage Bank. A lockpick is required to enter.";
			case 92:
				return "Cave Krakens can be awaken at the Stronghold Slayer Cave. A Magic weapon is preferred, as Ranged his are heavilyreduced. You're unable to use Melee on these monsters.";
			case 95:
				return "Smoke Devils can be found at the Stronghold Slayer Cave. A Slayer helm or facemask is suggested whenfighting these monsters.";
			case 98:
				return "A boss task in W2 counts any major wilderness boss you may encounter. Vet'ion, Chaos Fanatic, Callisto and so on are all valid.";
			case 104:
				return "Lava dragons are located on the Lava Dragon Isle. A form of anti-fire protection is strongly recommended.";

		}
	}

	public static String taskName(Player player, int id) {
		if (VarPlayerRepository.BOSS_TASK.get(player) != 0) {
			var metaStructId =
					EnumMap.get(5008).ints().get(VarPlayerRepository.BOSS_TASK.get(player) + 1);
			var bossName = StructType.get(metaStructId).stringParam(1801);
			return bossName;
		}
		return EnumMap.get(693).strings().get(id);
	}

	public static final List<Pair<Integer, VarPlayerRepository>> multipliable = Arrays.asList(
			new Pair<>(66, VarPlayerRepository.NEED_MORE_DARKNESS),
			new Pair<>(79, VarPlayerRepository.ANKOU_VERY_MUCH),
			new Pair<>(83, VarPlayerRepository.SUQ_ANOTHER_ONE),
			new Pair<>(27, VarPlayerRepository.FIRE_AND_DARKNESS),
			new Pair<>(58, VarPlayerRepository.PEDAL_TO_THE_METALS),
			new Pair<>(59, VarPlayerRepository.PEDAL_TO_THE_METALS),
			new Pair<>(60, VarPlayerRepository.PEDAL_TO_THE_METALS),
			new Pair<>(89, VarPlayerRepository.SPIRITUAL_FERVOUR),
			new Pair<>(91, VarPlayerRepository.SPIRITUAL_FERVOUR),
			new Pair<>(42, VarPlayerRepository.AUGMENT_MY_ABBIES),
			new Pair<>(30, VarPlayerRepository.ITS_DARK_IN_HERE),
			new Pair<>(29, VarPlayerRepository.GREATER_CHALLENGE),
			new Pair<>(48, VarPlayerRepository.BLEED_ME_DRY),
			new Pair<>(41, VarPlayerRepository.SMELL_YA_LATER),
			new Pair<>(94, VarPlayerRepository.BIRDS_OF_A_FEATHER),
			new Pair<>(93, VarPlayerRepository.I_REALLY_MITH_YOU),
			new Pair<>(80, VarPlayerRepository.HORRORIFIC),
			new Pair<>(72, VarPlayerRepository.WYVER_NOTHER_ONE),
			new Pair<>(46, VarPlayerRepository.GET_SMASHED),
			new Pair<>(52, VarPlayerRepository.NECHS_PLEASE),
			new Pair<>(92, VarPlayerRepository.KRACK_ON));

	private enum ShopItem {
		//@formatter:off
		SLAYER_RING(11866, 75, 1), // Slayer ring
		BROAD_BOLTS(11875, 35, 250), // Broad bolts
		BROAD_ARROWS(4160, 35, 250), // Broad arrows
		HERB_SACK(13226, 750, 1), // Herb sack
		RUNE_POUCH(12791, 750, 1), // Rune pouch
		LOOTING_BAG(11941, 10, 1), // Looting Bag
		;
		//@formatter:on

		final int id, price, buyAmount;

		ShopItem(int id, int price, int buyAmount) {
			this.id = id;
			this.price = price;
			this.buyAmount = buyAmount;
		}
	}

	private enum PVPShopItem {
		//@formatter:off
		SLAYER_RING(11866, 75),
		RUNE_POUCH(12791, 300),
		LOOTING_BAG(11941, 100),
		FIRE_CAPE(6570, 100),
		INFERNAL_CAPE(21295, 3000),
		LAVA_WHIP_MIX(12771, 300),
		FROZEN_WHIP_MIX(12769, 300),
		YELLOW_DARK_BOW_PAINT(12761, 300),
		WHITE_DARK_BOW_PAINT(12763, 300),
		BLUE_DARK_BOW_PAINT(12757, 300),
		DRAGON_DEFENDER(12954, 100)
		;
		//@formatter:on


		int id, price, buyAmount;

		PVPShopItem(int id, int price) {
			this.id = id;
			this.price = price;
			this.buyAmount = 1;
		}
	}

}
