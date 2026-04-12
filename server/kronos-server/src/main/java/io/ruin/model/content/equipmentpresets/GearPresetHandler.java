package io.ruin.model.content.equipmentpresets;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;
import io.ruin.model.item.containers.Equipment;

import java.util.*;

public class GearPresetHandler {
	public static void saveGearPreset(Player player) {
		HashMap<Integer, Item> equipment = new HashMap<>();
		equipment.put(Equipment.SLOT_WEAPON, player.getEquipment().get(Equipment.SLOT_WEAPON));
		equipment.put(Equipment.SLOT_SHIELD, player.getEquipment().get(Equipment.SLOT_SHIELD));
		equipment.put(Equipment.SLOT_CAPE, player.getEquipment().get(Equipment.SLOT_CAPE));
		equipment.put(Equipment.SLOT_AMULET, player.getEquipment().get(Equipment.SLOT_AMULET));
		equipment.put(Equipment.SLOT_HAT, player.getEquipment().get(Equipment.SLOT_HAT));
		equipment.put(Equipment.SLOT_CHEST, player.getEquipment().get(Equipment.SLOT_CHEST));
		equipment.put(Equipment.SLOT_LEGS, player.getEquipment().get(Equipment.SLOT_LEGS));
		equipment.put(Equipment.SLOT_HANDS, player.getEquipment().get(Equipment.SLOT_HANDS));
		equipment.put(Equipment.SLOT_FEET, player.getEquipment().get(Equipment.SLOT_FEET));
		equipment.put(Equipment.SLOT_RING, player.getEquipment().get(Equipment.SLOT_RING));
		equipment.put(Equipment.SLOT_AMMO, player.getEquipment().get(Equipment.SLOT_AMMO));


		List<Item> inventory = new ArrayList<>();
		for (Item item : player.getInventory().getItems()) {
			if (item != null) {
				inventory.add(new Item(item.getId(), item.getAmount()));  // Ensure a new Item is created to avoid shared references
			}
		}

		player.stringInput("Enter a name for your preset", name -> {
			boolean available = true;
			for (GearPreset preset : player.gearPresets) {
				if (preset.presetName.equalsIgnoreCase(name)) {
					player.sendMessage("You already have a preset with that name!");
					available = false;
					break;
				}
			}
			if (available) {
				player.sendMessage("Gear preset saved!");
				System.out.println("Saving equipment size: " + equipment.size());
				for (Map.Entry<Integer, Item> entry : equipment.entrySet()) {
					if (entry.getValue() == null)
						continue;
					System.out.println("Slot: " + entry.getKey() + ", Item ID: " + entry.getValue().getId() + ", Amount: " + entry.getValue().getAmount());
				}
				for (Item item : inventory) {
					if (item == null)
						continue;
					System.out.println("Inventory Item ID: " + item.getId() + ", Amount: " + item.getAmount());
				}
				player.gearPresets.add(new GearPreset(name, inventory, equipment));
				// player.getGearPresetInterface().open(player);
			}
		});
	}

	public static void deleteGearPreset(Player player, GearPreset preset) {
		player.gearPresets.remove(preset);
		//player.getGearPresetInterface().currentPreset = null;
		player.sendMessage("Gear preset deleted!");
		//  player.getGearPresetInterface().open(player);
	}

	public static boolean canEquipPreset(Player player, GearPreset preset) {
		Map<Integer, Integer> requiredItemCounts = new HashMap<>();
		for (Map.Entry<Integer, Item> entry : preset.equipment.entrySet()) {
			Item item = entry.getValue();
			if (item == null)
				continue;
			requiredItemCounts.put(item.getId(), requiredItemCounts.getOrDefault(item.getId(), 0) + 1);
		}
		for (Item item : preset.inventory) {
			if (item == null)
				continue;
			requiredItemCounts.put(item.getId(), requiredItemCounts.getOrDefault(item.getId(), 0) + 1);
		}

		Map<Integer, Integer> availableItemCounts = new HashMap<>();
		for (Item item : player.getBank().getItems()) {
			if (item == null)
				continue;
			availableItemCounts.put(item.getId(), availableItemCounts.getOrDefault(item.getId(), 0) + 1);
		}
		for (Item item : player.getInventory().getItems()) {
			if (item == null)
				continue;
			availableItemCounts.put(item.getId(), availableItemCounts.getOrDefault(item.getId(), 0) + 1);
		}
		for (Item item : player.getEquipment().getItems()) {
			if (item == null)
				continue;
			availableItemCounts.put(item.getId(), availableItemCounts.getOrDefault(item.getId(), 0) + 1);
		}

		for (Map.Entry<Integer, Integer> entry : requiredItemCounts.entrySet()) {
			int itemId = entry.getKey();
			int requiredCount = entry.getValue();
			int availableCount = availableItemCounts.getOrDefault(itemId, 0);
			if (availableCount < requiredCount) {
				return false;
			}
		}

		return true;
	}

	public static void equipPreset(Player player, GearPreset preset) {
		if (!canEquipPreset(player, preset)) {
			player.sendMessage("You do not have the required items to equip this preset!");
			return;
		}
		player.getBank().deposit(player.getInventory(), true);
		player.getBank().deposit(player.getEquipment(), true);

		for (Map.Entry<Integer, Item> entry : preset.equipment.entrySet()) {
			Item item = entry.getValue();
			if (item == null)
				continue;
			player.getEquipment().equip(item);
			player.getBank().remove(item);
		}
		for (Item item : preset.inventory) {
			if (item == null)
				continue;
			player.getInventory().add(item);
			player.getBank().remove(item);
		}
		player.sendMessage("Gear preset equipped!");

	}
}
