package io.ruin.model.item.actions.impl;

import io.ruin.Server;
import io.ruin.cache.ItemID;
import io.ruin.cache.NPCType;
import io.ruin.cache.ObjType;
import io.ruin.data.DataFile;
import io.ruin.data.impl.items.item_info;
import io.ruin.data.impl.items.shield_types;
import io.ruin.data.impl.items.weapon_types;
import io.ruin.data.impl.npcs.npc_combat;
import io.ruin.data.impl.npcs.npc_drops;
import io.ruin.data.impl.npcs.npc_spawns;
import io.ruin.model.World;
import io.ruin.model.activities.raids.xeric.ChambersOfXeric;
import io.ruin.model.activities.raids.xeric.chamber.Chamber;
import io.ruin.model.activities.raids.xeric.chamber.ChamberDefinition;
import io.ruin.model.activities.raids.xeric.party.Party;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.handlers.OptionScroll;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.skills.construction.actions.Costume;
import io.ruin.model.skills.construction.actions.CostumeStorage;
import io.ruin.services.Punishment;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RottenPotato {

	/**
	 * World Configuration
	 */

	public static void toggleBloodMoney() {
		if (World.bmMultiplier == 1) {
			World.boostBM(2);
		}
		if (World.bmMultiplier == 2) {
			World.boostBM(1);
		}
	}

	/**
	 * Peel Options
	 */

	public static void toggleDebugMode(Player player) {
		player.sendMessage("Debug: " + ((player.debug = !player.debug) ? "ON" : "OFF"));
	}

	public static void giveItemUpgrades(Player player) {
		for (ItemUpgrading upgrade : ItemUpgrading.VALUES) {
			player.getInventory().add(upgrade.upgradeId, 1);
		}
	}

	public static void giveBreakableItems(Player player) {
		for (ItemBreaking breaking : ItemBreaking.VALUES) {
			player.getInventory().add(breaking.fixedId, 1);
		}
	}

	public static void fillCostumRoom(Player player) {
		for (CostumeStorage s : CostumeStorage.VALUES) {
			Map<Costume, int[]> stored = s.getSets(player);
			stored.clear();
		}
	}

	public static void clearCostumRoom(Player player) {
		for (CostumeStorage s : CostumeStorage.VALUES) {
			Map<Costume, int[]> stored = s.getSets(player);
			stored.clear();
		}
	}

	public static void testTut(Player player) {
		player.newPlayer = true;
	}

	public static void refreshItemDefinitions(Player player) {
		Server.executeAsync(() -> {
			player.sendMessage("Reloading item info...");
			DataFile.reload(player, shield_types.class);
			DataFile.reload(player, weapon_types.class);
			DataFile.reload(player, item_info.class);
			player.sendMessage("Done!");
		});
	}

	public static void refreshNpcCombatDefs(Player player) {
		Server.executeAsync(() -> {
			player.sendMessage("Reloading NPC Definitions");
			DataFile.reload(player, npc_combat.class);
		});
	}

	public static void refreshNpcSpawns(Player player) {
		World.npcsNonNull().forEach(NPC::remove); // todo fix this
		DataFile.reload(player, npc_spawns.class);
	}

	public static void refreshNpcDrops(Player player) {
		NPCType.forEach(def -> def.lootTable = null);
		DataFile.reload(player, npc_drops.class);
	}

	public static void reloadAll(Player player) {
		refreshItemDefinitions(player);
		refreshNpcCombatDefs(player);
		refreshNpcSpawns(player);
		refreshNpcDrops(player);
	}

	public static void register() {
		ItemAction.registerInventory(ItemID.ROTTEN_POTATO, "eat", (player, item) -> {
			if (!player.isAdmin()) {
				player.getInventory().remove(5733, 1);
				Punishment.ban(player, player);
				player.sendMessage("Too late it's gone.");
			}
		});
		var potato = ObjType.get(ItemID.ROTTEN_POTATO);
		potato.defaultItemOnNpcAction = (player, item, npc) -> {
			if (!player.dev()) {
				return;
			}
			if (item.getId() != ItemID.ROTTEN_POTATO) {
				return;
			}
			player.sendMessage("npcid set as debuggable " + npc.getId());
		};
		potato.defaultItemOnPlayerAction = ((player, item, other) -> {
			player.sendMessage("sup");
		});
		potato.defaultItemGroundItemAction = ((player, item, groundItem, distance) -> {
			player.sendMessage("sup");
		});
		potato.defaultItemOnObjectAction = ((player, item, gameObject) -> {
			player.sendMessage("sup");
		});

		ItemAction.registerInventory(5733, "mash", (player, item) -> {
			if (!player.isAdmin()) {
				player.getInventory().remove(5733, 1);
				Punishment.ban(player, player);
				player.sendMessage("Too late it's gone.");
				return;
			}
			OptionScroll.open(player, "Game Server Definitions",
					new Option("Reload Item Definitions", () -> refreshItemDefinitions(player)),
					new Option("Refresh NPC Combat Definitions", () -> refreshNpcCombatDefs(player)),
					new Option("Refresh NPC Spawns Definitions", () -> refreshNpcSpawns(player)),
					new Option("Refresh Drop Defiitions", () -> refreshNpcDrops(player)),
					new Option("Reload all", () -> reloadAll(player)));
			return;
		});

		ItemAction.registerInventory(5733, "peel", (player, item) -> {
			if (!player.isAdmin()) {
				player.getInventory().remove(5733, 1);
				Punishment.ban(player, player);
				player.sendMessage("Too late it's gone.");
				return;
			}
			OptionScroll.open(player, "Configure Character",
					new Option("Debug Mode - State: " + (player.debug ? "enabled" : "disabled") + "",
							() -> toggleDebugMode(player)),
					// new Option("Unlock skins", () -> unlockCharacterSkins(player)),
					new Option("Give Item Upgrades", () -> giveItemUpgrades(player)),
					new Option("Give breakables", () -> giveBreakableItems(player)),
					new Option("Fill Costume Room", () -> fillCostumRoom(player)),
					new Option("Empty Costume Room", () -> clearCostumRoom(player)),
					new Option("Test Tutourial", () -> testTut(player)),
					new Option("Bank", () -> player.getBank().open()));
			return;
		});

		ItemAction.registerInventory(5733, "slice", (player, item) -> {
			if (!player.isAdmin()) {
				player.getInventory().remove(5733, 1);
				Punishment.ban(player, player);
				player.sendMessage("Too late it's gone.");
				return;
			}
			OptionScroll.open(player, "Configure Game Server",
					// new Option("Double drops - State: " + (World.doubleDrops ? "enabled" :
					// "disabled") + "", () -> World.toggleDoubleDrops()),
					new Option("Double PKP - State: " + (World.doublePkp ? "enabled" : "disabled") + "",
							() -> World.toggleDoublePkp()),
					new Option("Double Slayer - State: " + (World.doubleSlayer ? "enabled" : "disabled") + "",
							() -> World.toggleDoubleSlayer()),
					new Option("Double Pc Points - State: " + (World.doublePest ? "enabled" : "disabled") + "",
							() -> World.toggleDoublePest()),
					new Option("Weekend XP (25%)- State: " + (World.weekendExpBoost ? "enabled" : "disabled") + "",
							() -> World.toggleWeekendExpBoost()),
					new Option("Double Blood Money - State: " + (World.bmMultiplier > 1 ? "enabled" : "disabled") + "",
							() -> toggleBloodMoney()),
					new Option("DMM Key Event - State: " + (World.wildernessDeadmanKeyEvent ? "on" : "off") + "",
							() -> World.toggleWildernessKeyEvent()),
					// new Option("Staff Bounty (PvP Event) - State: " + (StaffBounty.EVENT_ACTIVE ?
					// "on" : "off") + "", () -> toggleStaffBounty(player)),
					new Option("Start 30 Minute update timer", () -> World.update(30)),
					new Option("Start 5 Minute update timer", () -> World.update(5))
			// new Option("Fetch Update", () -> LatestUpdate.fetch()),
			);
			return;
		});
	}
}
