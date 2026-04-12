package io.ruin.model.item.containers;

import io.ruin.api.utils.Random;
import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.activities.duelarena.DuelRule;
import io.ruin.model.combat.RangedWeapon;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.handlers.EquipmentStats;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.actions.impl.MaxCape;
import io.ruin.model.item.actions.impl.chargable.Blowpipe;
import io.ruin.model.item.actions.impl.combine.SlayerHelm;
import io.ruin.model.item.actions.impl.itemeffects.itemhandlers.*;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.skills.construction.actions.CombatRoom;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Equipment extends ItemContainer {

	public static final int SLOT_HAT = 0,
			SLOT_CAPE = 1,
			SLOT_AMULET = 2,
			SLOT_WEAPON = 3,
			SLOT_CHEST = 4,
			SLOT_SHIELD = 5,
			SLOT_LEGS = 7,
			SLOT_HANDS = 9,
			SLOT_FEET = 10,
			SLOT_RING = 12,
			SLOT_AMMO = 13,
			SLOT_QUIVER = 14;

	public transient int[] bonuses = new int[16];
	public transient double weight;
	private transient Queue<Item> toEquip = new LinkedList<>();
	transient int weaponId = -1;

	@Override
	public Equipment clone() {
		Equipment container = new Equipment();
		Item[] items = new Item[this.items.length];
		for (int id = 0; id < items.length; id++) {
			items[id] = this.items[id] == null ? null : new Item(this.items[id].getId(), this.items[id].getAmount());
		}
		container.weight = weight;
		container.bonuses = bonuses;
		container.items = items;
		container.updatedCount = updatedCount;
		container.updatedSlots = updatedSlots;
		container.sendAll = sendAll;
		container.player = player;
		container.interfaceHash = interfaceHash;
		container.containerId = containerId;
		container.forceStack = forceStack;
		return container;
	}

	public void process() {

		if (toEquip.size() == 0)
			return;

		Item item = null;

		while ((item = toEquip.poll()) != null) {
			equip(item);
		}

	}

	public boolean isWearing(int slot, int... ids) {
		var slotItem = this.get(slot);
		if (slotItem == null) {
			return false;
		}

		var slotItemId = slotItem.getId();
		for (var id : ids) {
			if (id == slotItemId) {
				return true;
			}
		}

		return false;
	}

	public void equipQueue(Item item) {
		toEquip.add(item);
	}

	public void equip(Item selectedItem) {
		ObjType selectedDef = selectedItem.getDef();
		int equipSlot = selectedDef.equipSlot;
		if (equipSlot == -1 || selectedDef.equipOption == -1) {
			player.sendMessage("You can't wear this item.");
			return;
		}
		if (selectedDef.equipReqs != null) {
			for (int req : selectedDef.equipReqs) {
				int statId = req >> 8;
				int lvl = req & 0xff;
				Stat stat = player.getStats().get(statId);
				if (stat.fixedLevel < lvl) {
					player.sendMessage(
							"You need " + StatType.get(statId).descriptiveName + " level of " + lvl + " to equip this item.");
					return;
				}
			}
		}
		if (selectedDef.maxType && !MaxCape.unlocked(player)) {
			player.sendMessage("You don't have the required stats to wear this.");
			return;
		}

		if (selectedDef.masterType) {
			if (selectedDef.id == 30232 && player.getStats().get(StatType.Attack).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Attack to wear this cape.");
				return;
			} else if (selectedDef.id == 30240 && player.getStats().get(StatType.Defence).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Defence to wear this cape.");
				return;
			} else if (selectedDef.id == 30270 && player.getStats().get(StatType.Strength).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Strength to wear this cape.");
				return;
			} else if (selectedDef.id == 30252 && player.getStats().get(StatType.Hitpoints).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Hitpoints to wear this cape.");
				return;
			} else if (selectedDef.id == 30262 && player.getStats().get(StatType.Ranged).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Ranged to wear this cape.");
				return;
			} else if (selectedDef.id == 30260 && player.getStats().get(StatType.Prayer).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Prayer to wear this cape.");
				return;
			} else if (selectedDef.id == 30256 && player.getStats().get(StatType.Magic).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Magic to wear this cape.");
				return;
			} else if (selectedDef.id == 30236 && player.getStats().get(StatType.Cooking).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Cooking to wear this cape.");
				return;
			} else if (selectedDef.id == 30274 && player.getStats().get(StatType.Woodcutting).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Woodcutting to wear this cape.");
				return;
			} else if (selectedDef.id == 30248 && player.getStats().get(StatType.Fletching).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Fletching to wear this cape.");
				return;
			} else if (selectedDef.id == 30246 && player.getStats().get(StatType.Fishing).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Fishing to wear this cape.");
				return;
			} else if (selectedDef.id == 30244 && player.getStats().get(StatType.Firemaking).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Firemaking to wear this cape.");
				return;
			} else if (selectedDef.id == 30238 && player.getStats().get(StatType.Crafting).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Crafting to wear this cape.");
				return;
			} else if (selectedDef.id == 30268 && player.getStats().get(StatType.Smithing).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Smithing to wear this cape.");
				return;
			} else if (selectedDef.id == 30258 && player.getStats().get(StatType.Mining).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Mining to wear this cape.");
				return;
			} else if (selectedDef.id == 30250 && player.getStats().get(StatType.Herblore).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Herblore to wear this cape.");
				return;
			} else if (selectedDef.id == 30230 && player.getStats().get(StatType.Agility).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Agility to wear this cape.");
				return;
			} else if (selectedDef.id == 30272 && player.getStats().get(StatType.Thieving).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Thieving to wear this cape.");
				return;
			} else if (selectedDef.id == 30266 && player.getStats().get(StatType.Slayer).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Slayer to wear this cape.");
				return;
			} else if (selectedDef.id == 30242 && player.getStats().get(StatType.Farming).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Farming to wear this cape.");
				return;
			} else if (selectedDef.id == 30264 && player.getStats().get(StatType.Runecrafting).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Runecrafting to wear this cape.");
				return;
			} else if (selectedDef.id == 30254 && player.getStats().get(StatType.Hunter).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Hunter to wear this cape.");
				return;
			} else if (selectedDef.id == 30234 && player.getStats().get(StatType.Construction).experience < 200_000_000) {
				player.sendMessage("You don't have the required 200,000,000 experience in Construction to wear this cape.");
				return;
			}
		}

		if (player.getDuel().isBlocked(selectedDef)) {
			player.sendMessage("That item cannot be equipped in this duel!");
			return;
		}

		if (player.getDuel().isToggled(DuelRule.NO_WEAPON_SWITCH) && equipSlot == Equipment.SLOT_WEAPON) {
			player.sendMessage("Weapon switching is disabled for this fight!");
			return;
		}

		if (!CombatRoom.allowEquip(player, selectedDef)) {
			return;
		}

		if (selectedDef.name.toLowerCase().contains("goblin mail")) {
			player.sendMessage("You can't wear this item.");
			return;
		}

		Item addLast = null;
		Inventory inventory = player.getInventory();
		if (equipSlot == SLOT_SHIELD) {
			Item weapon = get(SLOT_WEAPON);
			if (weapon != null) {
				if (weapon.getDef().twoHanded) {
					if (inventory.getFreeSlots() == 0 && get(SLOT_SHIELD) != null) {
						player.sendMessage("You don't have enough free inventory space to do that.");
						return;
					}
					addLast = weapon;
					set(SLOT_WEAPON, null);
				}
			}
		} else if (equipSlot == SLOT_WEAPON) {
			Item shield = get(SLOT_SHIELD);
			if (shield != null) {
				if (selectedDef.twoHanded) {
					if (inventory.getFreeSlots() == 0 && get(SLOT_WEAPON) != null) {
						player.sendMessage("You don't have enough free inventory space to do that.");
						return;
					}
					addLast = shield;
					set(SLOT_SHIELD, null);
				}
			}
		}
		if (selectedItem.getId() == 10551) {
			player.fighterTorsosEquipped++;
			if (player.fighterTorsosEquipped == Achievements.OVERNIGHT_MUSCLES.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
						+ Achievements.OVERNIGHT_MUSCLES.getAchievementName());
		}
		if (selectedItem.getId() == 12013 || selectedItem.getId() == 12014 || selectedItem.getId() == 12015
				|| selectedItem.getId() == 12016) {
			if (prospectorEquipped()) {
				player.prospectorEquippedCounter++;
				if (player.prospectorEquippedCounter == 1)
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
							+ Achievements.ALL_MINE.getAchievementName());
			}
		}

		if (AttributeExtensions.hasAttribute(selectedItem, AttributeTypes.RESPECT_FOR_THE_DEAD))
			RespectTheDead.wield(player, selectedItem);
		if (AttributeExtensions.hasAttribute(selectedItem, AttributeTypes.SPECIAL_ENERGY_LOWERER))
			SpecialEnergyManagement.wield(player, selectedItem);
		if (AttributeExtensions.hasAttribute(selectedItem, AttributeTypes.CRITICAL_HIT))
			CriticalHit.wield(player, selectedItem);
		if (AttributeExtensions.hasAttribute(selectedItem, AttributeTypes.AOE_SWING))
			AoESwipe.wield(player, selectedItem);
		if (AttributeExtensions.hasAttribute(selectedItem, AttributeTypes.DAMAGE_FOR_HIRE_LOW))
			DamageForHireLow.wield(player, selectedItem);
		if (AttributeExtensions.hasAttribute(selectedItem, AttributeTypes.DAMAGE_FOR_HIRE_HIGH))
			DamageForHireHigh.wield(player, selectedItem);
		if (AttributeExtensions.hasAttribute(selectedItem, AttributeTypes.ARMOUR_BREAKER))
			ArmourBreaker.wield(player, selectedItem);
		if (AttributeExtensions.hasAttribute(selectedItem, AttributeTypes.DOUBLE_HIT))
			DoubleHit.wield(player, selectedItem);
		if (AttributeExtensions.hasAttribute(selectedItem, AttributeTypes.FREEZE))
			FreezeChance.wield(player, selectedItem);
		if (AttributeExtensions.hasAttribute(selectedItem, AttributeTypes.SIPHON_THE_DEAD))
			SiphonTheDead.wield(player, selectedItem);
		if (AttributeExtensions.hasAttribute(selectedItem, AttributeTypes.VENOM_TIPPED))
			VenomTipped.wield(player, selectedItem);
		if (AttributeExtensions.hasAttribute(selectedItem, AttributeTypes.HEALTH_SIPHON))
			HealthSiphon.wield(player, selectedItem);

		if (selectedItem.getId() == 8842 || selectedItem.getId() == 8839 || selectedItem.getId() == 8840
				|| selectedItem.getId() == 11665
				|| selectedItem.getId() == 11663 || selectedItem.getId() == 11664) {
			if (voidEquipped()) {
				player.voidEquippedCounter++;
				if (player.voidEquippedCounter == 1)
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
							+ Achievements.EMPOWERED.getAchievementName());
			}
		} else if (selectedItem.getId() == 23983 || selectedItem.getId() == 23991) {
			player.crystalEquipmentWorn++;
			if (player.crystalEquipmentWorn == Achievements.ALL_THIS_FROM_A_SEED.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
						+ Achievements.ALL_THIS_FROM_A_SEED.getAchievementName());

		}
		if (selectedItem.getId() == 25975)
			player.specialRestoreTicks = 0;
		if(selectedItem.getId() == 7927) {
			if (player.isLocked()) {
				player.sendMessage("You cannot use this right now!");
				return;
			}
			int[] eggs = { 5538, 5539, 5540, 5541, 5542, 5543};
			player.getMovement().reset();
			player.getAppearance().setNpcId(eggs[Random.get(eggs.length - 1)]);
			player.getAppearance().setCustomRenders(-1, -1, -1, -1, -1, -1, -1);
		}
		Item worn = get(equipSlot);
		if (worn == null) {
			selectedItem.remove();
			set(equipSlot, selectedItem);
		} else {
			if(worn.getId() == 7927) {
				player.getAppearance().setNpcId(-1);
				player.getAppearance().removeCustomRenders();
				player.unlock();
			}
			int selectedId = selectedItem.getId();
			int selectedAmount = selectedItem.getAmount();
			Map<String, String> attributeCopy = selectedItem.copyOfAttributes();
			if (worn.getId() == selectedId && selectedDef.stackable) {
				selectedItem.remove();
				worn.incrementAmount(selectedAmount);
			} else {
				Item inventoryStack = null;
				Map<String, String> attributes = worn.copyOfAttributes();
				if (worn.getDef().stackable)
					inventoryStack = inventory.findItem(worn.getId());
				if (inventoryStack != null) {
					selectedItem.remove();
					inventoryStack.incrementAmount(worn.getAmount());
				} else {
					selectedItem.setId(worn.getId());
					selectedItem.setAmount(worn.getAmount());
					worn.clearAttributes();
					worn.putAttributes(selectedItem.copyOfAttributes());
				}
				worn.setId(selectedId);
				worn.setAmount(selectedAmount);
				worn.putAttributes(attributeCopy);
				selectedItem.clearAttributes();
				selectedItem.putAttributes(attributes);
			}
		}
		if (addLast != null)
			inventory.add(addLast);
		if (!player.recentlyEquipped.isDelayed() && equipSlot == Equipment.SLOT_WEAPON) {
			player.recentlyEquipped.delay(1);
			// player.resetAnimation();
		}
		player.closeDialogue();
	}

	public boolean unequip(Item equipped) {
		if (equipped == null) {
			return false;
		}

		Inventory inventory = player.getInventory();
		Item inventoryStack = null;

		if (equipped.getDef().stackable) {
			inventoryStack = inventory.findItem(equipped.getId());
		}

		if (inventoryStack != null) {
			equipped.remove();
			inventoryStack.incrementAmount(equipped.getAmount());
		} else {
			int freeSlot = inventory.freeSlot();
			if (freeSlot == -1) {
				player.sendMessage("You don't have enough free space to do that.");
				return false;
			}
			equipped.remove();
			inventory.set(freeSlot, equipped);
			if (equipped.getId() == 25975)
				player.specialRestoreTicks = 0;
		}
		if(equipped.getId() == 7927) {
			if (player.isLocked()) {
				player.sendMessage("You cannot unequip this right now!");
				return false;
			}
			player.getAppearance().setNpcId(-1);
			player.getAppearance().removeCustomRenders();
			player.unlock();
		}
		sendUpdates();
		return true;
	}

	@Override
	public boolean sendUpdates() {
		process();
		int updatedAppearanceSlots = updatedCount;
		if (updatedSlots[SLOT_RING])
			updatedAppearanceSlots--;
		if (updatedSlots[SLOT_AMMO])
			updatedAppearanceSlots--;
		if (player.getEquipment().get(SLOT_WEAPON) == null) {
			player.getCombat().updateWeapon(false);
		} else if (updatedSlots[SLOT_WEAPON] && player.getEquipment().get(SLOT_WEAPON).getId() != weaponId) {
			player.getCombat().updateWeapon(false);
		}
		if (updatedAppearanceSlots > 0)
			player.getAppearance().update();
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null)
			weaponId = player.getEquipment().get(Equipment.SLOT_WEAPON).getId();
		else
			weaponId = -1;
		if (!super.sendUpdates())
			return false;
		/**
		 * Reset bonuses/weight
		 */
		for (int i = 0; i < bonuses.length; i++)
			bonuses[i] = 0;
		weight = 0.0;
		/**
		 * Calculate bonuses/weight
		 */
		boolean ignoreRangedAmmoStr = false;
		Item wep = get(SLOT_WEAPON);
		if (wep != null) {
			if (wep.getId() == 12926) { // blowpipe
				Blowpipe.Dart dart = Blowpipe.getDart(wep);
				if (dart != Blowpipe.Dart.NONE) // should always be true
					bonuses[EquipmentStats.RANGED_STRENGTH] += ObjType.get(dart.id).equipBonuses[EquipmentStats.RANGED_STRENGTH];
				ignoreRangedAmmoStr = true;
			} else {
				RangedWeapon rangedWep = wep.getDef().rangedWeapon;
				ignoreRangedAmmoStr = rangedWep != null && rangedWep.allowedAmmo == null;
			}
		}
		for (Item item : getItems()) {
			if (item != null) {
				ObjType def = item.getDef();
				if (def.equipBonuses != null) {
					boolean wilderness = def.wilderness; // If its pvp armor
					boolean inWilderness = player.wildernessLevel > 0;
					for (int i = 0; i < def.equipBonuses.length; i++) {
						int bonus = def.equipBonuses[i];
						if (bonus == 0)
							continue;
						if (ignoreRangedAmmoStr && def.equipSlot == SLOT_AMMO && i == EquipmentStats.RANGED_STRENGTH)
							continue;
						if (wilderness && !inWilderness)
							bonus *= .75;
						bonuses[i] += bonus;
					}
				}
				weight += def.weightEquipment;
			}
		}
		if (wep != null) {
			if (wep.getId() == 27275 || wep.getId() == 33000) {
				if (player.getCurrentToARaid() != null) {
					bonuses[EquipmentStats.MAGIC_DAMAGE] *= 4;
					bonuses[EquipmentStats.MAGIC_ATTACK] *= 4;
				} else {
					bonuses[EquipmentStats.MAGIC_DAMAGE] *= 3;
					bonuses[EquipmentStats.MAGIC_ATTACK] *= 3;
				}
			}
		}
		/**
		 * Update equipment stats interface
		 */
		if (player.isVisibleInterface(Interface.EQUIPMENT_STATS))
			EquipmentStats.update(player, Interface.EQUIPMENT_STATS, 24);
		return true;
	}

	@Override
	public boolean hasId(int id) {
		return getId(ObjType.get(id).equipSlot) == id;
	}

	/*
	 * Amulets and staff equipment boosts
	 */

	public int getItemIdForSlot(int slot) {
		Item item = get(slot);
		return item == null ? -1 : item.getId();
	}

	public boolean wearsAmuletOfTheDamned() {
		return getItemIdForSlot(SLOT_AMULET) == 12851 ||
			getItemIdForSlot(SLOT_AMULET) == 12853 ||
			getItemIdForSlot(SLOT_HANDS) == 30380; // Gloves of the damned
	}

	public boolean wearsTormentedBracelet() {
		return getItemIdForSlot(SLOT_HANDS) == 19544 || getItemIdForSlot(SLOT_HANDS) == 23444;
	}

	public boolean wearsStaffOfTheDead() {
		return getItemIdForSlot(SLOT_WEAPON) == 11791 || getItemIdForSlot(SLOT_WEAPON) == 12904;
	}

	public boolean wearsStaffOfLight() {
		return getItemIdForSlot(SLOT_WEAPON) == 22296;
	}

	public boolean wearsStaffOfBalance() {
		return getItemIdForSlot(SLOT_WEAPON) == 24144;
	}

	public boolean wearsAncestralHat() {
		return getItemIdForSlot(SLOT_HAT) == 24664 || getItemIdForSlot(SLOT_HAT) == 21018;
	}

	public boolean wearsAncestralTop() {
		return getItemIdForSlot(SLOT_CHEST) == 21021 || getItemIdForSlot(SLOT_CHEST) == 24666;
	}

	public boolean wearsAncestralBottom() {
		return getItemIdForSlot(SLOT_LEGS) == 21024 || getItemIdForSlot(SLOT_LEGS) == 24668;
	}

	public boolean wearsSlayerHelm() {
		int currentHelm = getItemIdForSlot(SLOT_HAT);
		for (int helmId : SlayerHelm.ALL_BOOST_HELMS) {
			if (currentHelm == helmId) {
				return true;
			}
		}
		return false;
	}

	public boolean voidEquipped() {
		int hatId = getItemIdForSlot(SLOT_HAT);
		int bodyId = getItemIdForSlot(SLOT_CHEST);
		int legsId = getItemIdForSlot(SLOT_LEGS);
		int glovesId = getItemIdForSlot(SLOT_HANDS);
		boolean wearingHelm = hatId == ItemID.VOID_MAGE_HELM || hatId == ItemID.VOID_MELEE_HELM
				|| hatId == ItemID.VOID_RANGER_HELM;
		return wearingHelm && bodyId == ItemID.VOID_KNIGHT_TOP || legsId == ItemID.VOID_KNIGHT_ROBE
				|| glovesId == ItemID.VOID_KNIGHT_GLOVES;
	}

	public boolean wearsOcculNecklace() {
		return getItemIdForSlot(SLOT_AMULET) == 12002 || getItemIdForSlot(SLOT_AMULET) == 19720;
	}

	public boolean wearsSmokeBattleStaff() {
		return getItemIdForSlot(SLOT_WEAPON) == 11998;
	}

	public boolean wearsKodaiWand() {
		return getItemIdForSlot(SLOT_WEAPON) == 21006;
	}

	public boolean wearsTaintedWand() {
		return getItemIdForSlot(SLOT_WEAPON) == 30598;
	}

	public boolean wearsAhrimStaff() {
		int weaponId = getItemIdForSlot(SLOT_WEAPON);
		return weaponId == 4710 || weaponId == 4862 || weaponId == 4863 || weaponId == 4864 || weaponId == 4865;
	}

	public boolean wearsEliteVoid() {
		return (getItemIdForSlot(SLOT_CHEST) == 13072 || getItemIdForSlot(SLOT_CHEST) == 26469
				|| getItemIdForSlot(SLOT_CHEST) == 24178) &&
				(getItemIdForSlot(SLOT_LEGS) == 13073 || getItemIdForSlot(SLOT_CHEST) == 26471
						|| getItemIdForSlot(SLOT_LEGS) == 24180);
	}

	public boolean wearsImbuedGodCape() {
		int capeId = getItemIdForSlot(SLOT_CAPE);
		return capeId == 21776 || capeId == 21780 || capeId == 21784
				|| capeId == 21791 || capeId == 21793 || capeId == 21795;
	}

	public boolean wearsNightmareStaff() {
		int weaponId = getItemIdForSlot(SLOT_WEAPON);
		return weaponId == 24422 || weaponId == 24423 || weaponId == 24424
				|| weaponId == 24425;
	}

	public boolean wearsAhrimRobes() {
		int hatId = getItemIdForSlot(SLOT_HAT);
		int bodyId = getItemIdForSlot(SLOT_CHEST);
		int legsId = getItemIdForSlot(SLOT_LEGS);
		return (hatId == 4708 || hatId == 4856 || hatId == 4857 || hatId == 4858 || hatId == 4859) &&
				(bodyId == 4712 || bodyId == 4868 || bodyId == 4869 || bodyId == 4870 || bodyId == 4871) &&
				(legsId == 4717 || legsId == 4874 || legsId == 4875 || legsId == 4876 || legsId == 4877);
	}

	private boolean prospectorEquipped() {
		int hatId = getItemIdForSlot(SLOT_HAT);
		int bodyId = getItemIdForSlot(SLOT_CHEST);
		int legsId = getItemIdForSlot(SLOT_LEGS);
		int bootsId = getItemIdForSlot(SLOT_FEET);
		return hatId == ItemID.PROSPECTOR_HELMET && bodyId == ItemID.PROSPECTOR_JACKET || legsId == ItemID.PROSPECTOR_LEGS
				|| bootsId == ItemID.PROSPECTOR_BOOTS;
	}

}
