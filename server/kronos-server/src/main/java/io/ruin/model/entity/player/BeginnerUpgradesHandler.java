package io.ruin.model.entity.player;

import io.ruin.model.item.Item;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.stat.StatType;

public class BeginnerUpgradesHandler {

	public static void HandleMeleeWeaponUpgrade(Player player) {
		int index = player.getCurrentItemUpgrades(player.getCurrentItemUpgrades(player.getCurrentMeleeWeaponUpgrades()));
		if (index >= BeginnerUpgrades.MELEE_WEAPON.upgrades.length - 1) return;
		int weaponToUpgrade = BeginnerUpgrades.MELEE_WEAPON.upgrades[index];
		Item newWeapon = new Item(BeginnerUpgrades.MELEE_WEAPON.upgrades[index + 1]);
		int[] attackLevels = {5, 10, 20, 30, 40, 60};
		int fixedLevel = player.getStats().get(StatType.Attack).fixedLevel;
		if (fixedLevel >= attackLevels[index]) {
			Item currentItem = player.getEquipment().get(Equipment.SLOT_WEAPON);
			if (currentItem != null && currentItem.getId() == weaponToUpgrade) {
				HandleUpgrade(player, Equipment.SLOT_WEAPON, newWeapon, weaponToUpgrade, player.getUpgradeItem(player.meleeWeapon), player.currentMeleeWeaponUpgrade);
				player.SetCurrentMeleeWeaponUpgrade(player.getCurrentMeleeWeaponUpgrades() + 1);
			}
		}
	}

	public static void HandleMeleeHelmUpgrade(Player player) {
		int index = player.getCurrentItemUpgrades(player.getCurrentItemUpgrades(player.getCurrentMeleeHelmUpgrades()));
		if (index >= BeginnerUpgrades.MELEE_HELM.upgrades.length - 1) return;
		int helmToUpgrade = BeginnerUpgrades.MELEE_HELM.upgrades[index];
		Item newHelm = new Item(BeginnerUpgrades.MELEE_HELM.upgrades[index + 1]);
		int[] defenceLevels = {5, 10, 20, 30, 40};
		int fixedLevel = player.getStats().get(StatType.Defence).fixedLevel;
		if (fixedLevel >= defenceLevels[index]) {
			Item currentItem = player.getEquipment().get(Equipment.SLOT_HAT);
			if (currentItem != null && currentItem.getId() == helmToUpgrade) {
				HandleUpgrade(player, Equipment.SLOT_HAT, newHelm, helmToUpgrade, player.getUpgradeItem(player.meleeHelm), player.currentMeleeHelmUpgrade);
				player.SetCurrentMeleeHelmUpgrade(player.getCurrentMeleeHelmUpgrades() + 1);
			}
		}
	}

	public static void HandleMeleeBodyUpgrade(Player player) {

		int index = player.getCurrentItemUpgrades(player.getCurrentItemUpgrades(player.getCurrentMeleeBodyUpgrades()));
		if (index >= BeginnerUpgrades.MELEE_BODY.upgrades.length - 1) return;
		int bodyToUpgrade = BeginnerUpgrades.MELEE_BODY.upgrades[index];
		Item newBody = new Item(BeginnerUpgrades.MELEE_BODY.upgrades[index + 1]);
		int[] defenceLevels = {5, 10, 20, 30, 40};
		int fixedLevel = player.getStats().get(StatType.Defence).fixedLevel;
		if (fixedLevel >= defenceLevels[index]) {
			Item currentItem = player.getEquipment().get(Equipment.SLOT_CHEST);
			if (currentItem != null && currentItem.getId() == bodyToUpgrade) {
				HandleUpgrade(player, Equipment.SLOT_CHEST, newBody, bodyToUpgrade, player.getUpgradeItem(player.meleeBody), player.currentMeleeBodyUpgrade);
				player.SetCurrentMeleeBodyUpgrade(player.getCurrentMeleeBodyUpgrades() + 1);
			}
		}
	}

	public static void HandleMeleeLegsUpgrade(Player player) {
		int index = player.getCurrentItemUpgrades(player.getCurrentItemUpgrades(player.getCurrentMeleeLegsUpgrades()));
		if (index >= BeginnerUpgrades.MELEE_LEGS.upgrades.length - 1) return;
		int legsToUpgrade = BeginnerUpgrades.MELEE_LEGS.upgrades[index];
		Item newLegs = new Item(BeginnerUpgrades.MELEE_LEGS.upgrades[index + 1]);
		int[] defenceLevels = {5, 10, 20, 30, 40};
		int fixedLevel = player.getStats().get(StatType.Defence).fixedLevel;
		if (fixedLevel >= defenceLevels[index]) {
			Item currentItem = player.getEquipment().get(Equipment.SLOT_LEGS);
			if (currentItem != null && currentItem.getId() == legsToUpgrade) {
				HandleUpgrade(player, Equipment.SLOT_LEGS, newLegs, legsToUpgrade, player.getUpgradeItem(player.meleeLegs), player.currentMeleeLegsUpgrade);
				player.SetCurrentMeleeLegsUpgrade(player.getCurrentMeleeLegsUpgrades() + 1);
			}
		}
	}

	public static void HandleRangeWeaponUpgrade(Player player) {
		int index = player.getCurrentItemUpgrades(player.getCurrentItemUpgrades(player.getCurrentRangeWeaponUpgrades()));
		if (index >= BeginnerUpgrades.RANGE_WEAPON.upgrades.length - 1) return;
		int weaponToUpgrade = BeginnerUpgrades.RANGE_WEAPON.upgrades[index];
		Item newWeapon = new Item(BeginnerUpgrades.RANGE_WEAPON.upgrades[index + 1]);
		int[] rangeLevels = {5, 20, 30, 40, 50};
		int fixedLevel = player.getStats().get(StatType.Ranged).fixedLevel;
		if (fixedLevel >= rangeLevels[index]) {
			Item currentItem = player.getEquipment().get(Equipment.SLOT_WEAPON);
			if (currentItem != null && currentItem.getId() == weaponToUpgrade) {
				HandleUpgrade(player, Equipment.SLOT_WEAPON, newWeapon, weaponToUpgrade, player.getUpgradeItem(player.rangeWeapon), player.currentRangeWeaponUpgrade);
				player.SetCurrentRangeWeaponUpgrade(player.getCurrentRangeWeaponUpgrades() + 1);
			}
		}
	}


	public static void HandleRangeHelmUpgrade(Player player) {
		int index = player.getCurrentItemUpgrades(player.getCurrentItemUpgrades(player.getCurrentRangeHelmUpgrades()));
		if (index >= BeginnerUpgrades.RANGE_HAT.upgrades.length - 1) return;
		int helmToUpgrade = BeginnerUpgrades.RANGE_HAT.upgrades[index];
		if (index == 1) return;
		Item rangeHelm = new Item(BeginnerUpgrades.RANGE_HAT.upgrades[index + 1]);
		if (player.getStats().get(StatType.Ranged).fixedLevel >= 20) {
			Item currentItem = player.getEquipment().get(Equipment.SLOT_HAT);
			if (currentItem != null && currentItem.getId() == helmToUpgrade) {
				HandleUpgrade(player, Equipment.SLOT_HAT, rangeHelm, helmToUpgrade, player.getUpgradeItem(player.rangeHelm), player.currentRangeHelmUpgrade);
				player.SetCurrentRangeHelmUpgrade(player.getCurrentRangeHelmUpgrades() + 1);
			}
		}
	}

	public static void HandleRangeBodyUpgrade(Player player) {
		int index = player.getCurrentItemUpgrades(player.getCurrentItemUpgrades(player.getCurrentRangeBodyUpgrades()));
		if (index >= BeginnerUpgrades.RANGE_TOP.upgrades.length - 1) return;
		int bodyToUpgrade = BeginnerUpgrades.RANGE_TOP.upgrades[index];
		Item rangeBody = new Item(BeginnerUpgrades.RANGE_TOP.upgrades[index + 1]);
		int[] defenceLevels = {20, 40, 40, 40, 40};
		int[] rangeLevels = {20, 40, 50, 60, 70};
		int fixedDefenceLevel = player.getStats().get(StatType.Defence).fixedLevel;
		int fixedRangedLevel = player.getStats().get(StatType.Ranged).fixedLevel;
		if (fixedRangedLevel >= rangeLevels[index] && fixedDefenceLevel >= defenceLevels[index]) {
			Item currentItem = player.getEquipment().get(Equipment.SLOT_CHEST);
			if (currentItem != null && currentItem.getId() == bodyToUpgrade) {
				HandleUpgrade(player, Equipment.SLOT_CHEST, rangeBody, bodyToUpgrade, player.getUpgradeItem(player.rangeBody), player.currentRangeBodyUpgrade);
				player.SetCurrentRangeBodyUpgrade(player.getCurrentRangeBodyUpgrades() + 1);
			}
		}

	}

	public static void HandleRangeChapsUpgrade(Player player) {
		int index = player.getCurrentItemUpgrades(player.getCurrentItemUpgrades(player.getCurrentRangeLegsUpgrades()));
		if (index >= BeginnerUpgrades.RANGE_LEGS.upgrades.length - 1) return;
		int chapsToUpgrade = BeginnerUpgrades.RANGE_LEGS.upgrades[index];
		Item newChaps = new Item(BeginnerUpgrades.RANGE_LEGS.upgrades[index + 1]);
		int[] rangeLevels = {20, 40, 50, 60, 70};
		int fixedLevel = player.getStats().get(StatType.Ranged).fixedLevel;
		if (fixedLevel >= rangeLevels[index]) {
			Item currentItem = player.getEquipment().get(Equipment.SLOT_LEGS);
			if (currentItem != null && currentItem.getId() == chapsToUpgrade) {
				HandleUpgrade(player, Equipment.SLOT_LEGS, newChaps, chapsToUpgrade, player.getUpgradeItem(player.rangeLegs), player.currentRangeLegsUpgrade);
				player.SetCurrentRangeLegsUpgrade(player.getCurrentRangeLegsUpgrades() + 1);
			}
		}
	}

	public static void HandleMageHatUpgrade(Player player) {
		int index = player.getCurrentItemUpgrades(player.getCurrentItemUpgrades(player.getCurrentMagicHatUpgrades()));
		if (index >= BeginnerUpgrades.MAGIC_HAT.upgrades.length - 1) return;
		int hatToUpgrade = BeginnerUpgrades.MAGIC_HAT.upgrades[index];
		Item newHat = new Item(BeginnerUpgrades.MAGIC_HAT.upgrades[index + 1]);
		int[] magicLevels = {20, 40};
		int[] defenceLevels = {10, 20};
		int fixedMagicLevel = player.getStats().get(StatType.Magic).fixedLevel;
		int fixedDefenceLevel = player.getStats().get(StatType.Defence).fixedLevel;
		if (fixedMagicLevel >= magicLevels[index] && fixedDefenceLevel >= defenceLevels[index]) {
			Item currentItem = player.getEquipment().get(Equipment.SLOT_HAT);
			if (currentItem != null && currentItem.getId() == hatToUpgrade) {
				HandleUpgrade(player, Equipment.SLOT_HAT, newHat, hatToUpgrade, player.getUpgradeItem(player.mageHelm), player.currentMageHelmUpgrade);
				player.SetCurrentMagicHatUpgrade(player.getCurrentMagicHatUpgrades() + 1);
			}
		}
	}

	public static void HandleMageTopUpgrade(Player player) {
		int index = player.getCurrentItemUpgrades(player.getCurrentItemUpgrades(player.getCurrentMagicTopUpgrades()));
		if (index >= BeginnerUpgrades.MAGIC_TOP.upgrades.length - 1) return;
		int topToUpgrade = BeginnerUpgrades.MAGIC_TOP.upgrades[index];
		Item newTop = new Item(BeginnerUpgrades.MAGIC_TOP.upgrades[index + 1]);
		int[] magicLevels = {20, 40};
		int[] defenceLevels = {10, 20};
		int fixedMagicLevel = player.getStats().get(StatType.Magic).fixedLevel;
		int fixedDefenceLevel = player.getStats().get(StatType.Defence).fixedLevel;
		if (fixedMagicLevel >= magicLevels[index] && fixedDefenceLevel >= defenceLevels[index]) {
			Item currentItem = player.getEquipment().get(Equipment.SLOT_CHEST);
			if (currentItem != null && currentItem.getId() == topToUpgrade) {
				HandleUpgrade(player, Equipment.SLOT_CHEST, newTop, topToUpgrade, player.getUpgradeItem(player.mageBody), player.currentMageBodyUpgrade);
				player.SetCurrentMagicTopUpgrade(player.getCurrentMagicTopUpgrades() + 1);
			}
		}
	}

	public static void HandleMageBottomsUpgrade(Player player) {
		int index = player.getCurrentItemUpgrades(player.getCurrentItemUpgrades(player.getCurrentMagicBottomUpgrades()));
		if (index >= BeginnerUpgrades.MAGIC_BOTTOM.upgrades.length - 1) return;
		int topToUpgrade = BeginnerUpgrades.MAGIC_BOTTOM.upgrades[index];
		Item newBottoms = new Item(BeginnerUpgrades.MAGIC_BOTTOM.upgrades[index + 1]);
		int[] magicLevels = {20, 40};
		int[] defenceLevels = {10, 20};
		int fixedMagicLevel = player.getStats().get(StatType.Magic).fixedLevel;
		int fixedDefenceLevel = player.getStats().get(StatType.Defence).fixedLevel;
		if (fixedMagicLevel >= magicLevels[index] && fixedDefenceLevel >= defenceLevels[index]) {
			Item currentItem = player.getEquipment().get(Equipment.SLOT_LEGS);
			if (currentItem != null && currentItem.getId() == topToUpgrade) {
				HandleUpgrade(player, Equipment.SLOT_LEGS, newBottoms, topToUpgrade, player.getUpgradeItem(player.mageLegs), player.currentMageLegsUpgrade);
				player.SetCurrentMagicBottomUpgrade(player.getCurrentMagicBottomUpgrades() + 1);
			}
		}
	}


	/*
	Handles the upgrade for all the weapons/armours.
	 */
	private static void HandleUpgrade(Player player, int slot, Item new_item, int item_to_upgrade, BeginnerUpgrades upgradeItem, int playerItemUpgrade) {
		Item newItem = new_item;
		Item currentItem = player.getEquipment().get(slot);
		int itemToUpgrade = item_to_upgrade;
		if (currentItem != null && currentItem.getId() == itemToUpgrade) {
			player.sendMessage("<img=15>Note: Some of your gear was upgraded!");
			player.getEquipment().set(slot, newItem);
			player.getAppearance().update();
		}
	}
}

