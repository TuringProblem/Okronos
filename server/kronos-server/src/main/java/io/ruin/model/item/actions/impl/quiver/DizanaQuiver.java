package io.ruin.model.item.actions.impl.quiver;

import io.ruin.api.utils.NumberUtils;
import io.ruin.model.combat.RangedAmmo;
import io.ruin.model.combat.RangedWeapon;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.dialogue.skill.SkillItem;
import io.ruin.model.inter.handlers.EquipmentStats;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.impl.chargable.Blowpipe;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.var.VarPlayerRepository;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.ruin.cache.ItemID.*;

/**
 * @author Glabay | Glabay-Studios
 * @project server
 * @social Discord: Glabay
 * @since 2025-05-08
 */
public class DizanaQuiver {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DizanaQuiver.class);

	public static final List<Integer> VALID_QUIVER_IDS = List.of(DIZANAS_MAX_CAPE, DIZANAS_QUIVER_UN, DIZANAS_QUIVER, DIZANAS_BLESSED_QUIVER);

	public static void register() {
		/* Item Actions */
		for (Integer quiverId : VALID_QUIVER_IDS) {
			ItemAction.registerInventory(quiverId, "open", DizanaQuiver::openQuiver);
			ItemAction.registerInventory(quiverId, "empty", DizanaQuiver::emptyQuiver);
		}

		// Interface Actions
		InterfaceHandler.register(Interface.DIZANAS_QUIVER_INVENTORY, 12, p ->
			p.closeInterface(ToplevelComponent.DIZANA_QUIVER_TAB_AREA));
	}

	/**
	 * Fills the player's quiver with bolts or arrows from their inventory. If multiple eligible items
	 * are detected, the player is prompted to select one. If no bolts or arrows are found, a message
	 * is sent to inform the player.
	 *
	 * @param player The player attempting to fill their quiver.
	 */
	public static void fillQuiver(Player player) {
		var boltsAndArrows = Arrays.stream(player.getInventory().getItems())
			.filter(Objects::nonNull)
			.filter(item ->
				item.getDef().getName().contains("bolt") || item.getDef().getName().contains("arrow")
			).toList();
		if (boltsAndArrows.isEmpty()) {
			player.sendMessage("You don't have any bolts or arrows in your inventory.");
			return;
		}
		if (boltsAndArrows.size() == 1) {
			fillQuiverWithAmmo(player, boltsAndArrows.getFirst());
			return;
		}
		var ammo = new ArrayList<SkillItem>();
		boltsAndArrows.forEach(item ->
			ammo.add(new SkillItem(item.getId())
				.addAction((p, i, e) ->
					fillQuiverWithAmmo(player, item))
			));
		SkillDialogue.make(player, ammo.toArray(new SkillItem[0]));
	}

	private static void fillQuiverWithAmmo(Player player, Item ammo) {
		var ammoId = VarPlayerRepository.DIZANA_QUIVER_AMMO_ID.get(player);
		var ammoAmount = VarPlayerRepository.DIZANA_QUIVER_AMMO_AMOUNT.get(player);
		// if we're adding existing ammo
		if (ammoId == ammo.getId()) {
			// adding to the existing stack
			try {
				ammoAmount += ammo.getAmount();
				player.getInventory().remove(ammo.getId(), ammo.getAmount());
				VarPlayerRepository.DIZANA_QUIVER_AMMO_AMOUNT.set(player, ammoAmount);
				player.sendMessage("You add %s x %s to your quiver.".formatted(
					NumberUtils.formatNumber(ammo.getAmount()),
					ammo.getDef().getName()
				));
			}
			catch (Exception ignored) {}
			return;
		}
		// Add the new ammo
		VarPlayerRepository.DIZANA_QUIVER_AMMO_ID.set(player, ammo.getId());
		VarPlayerRepository.DIZANA_QUIVER_AMMO_AMOUNT.set(player, ammo.getAmount());
		// remove the old items
		player.getInventory().remove(ammo.getId(), ammo.getAmount());
	}

	private static void openQuiver(Player player, Item quiver) {
		player.openInterface(ToplevelComponent.DIZANA_QUIVER_TAB_AREA);
	}

	/**
	 * Empties the specified quiver of all its ammo, returning the ammo to the player's inventory
	 * if space is available. If the quiver is already empty, the player is notified.
	 *
	 * @param player The player attempting to empty the quiver.
	 * @param quiver The quiver that is being emptied.
	 */
	public static void emptyQuiver(Player player, Item quiver) {
		var ammoId = VarPlayerRepository.DIZANA_QUIVER_AMMO_ID.get(player);
		var ammoAmount = VarPlayerRepository.DIZANA_QUIVER_AMMO_AMOUNT.get(player);
		if (ammoId < 0) {
			player.sendMessage("Your quiver is empty.");
			return;
		}
		if (ammoAmount == 0) {
			player.sendMessage("Your quiver is empty.");
			return;
		}
		// Copy the ammo to remove from the quiver
		var ammo = new Item(ammoId, ammoAmount);
		// reset the varps
		VarPlayerRepository.DIZANA_QUIVER_AMMO_ID.set(player, -1);
		VarPlayerRepository.DIZANA_QUIVER_AMMO_AMOUNT.set(player, -1);
		// return the ammo to the inventory if we can
		player.getInventory().addOrDrop(ammo);
		// Notify the Player
		player.dialogue(new ItemDialogue().one(
			ammo.getId(),
			"You have removed %s x %s."
				.formatted(
					NumberUtils.formatNumber(ammo.getAmount()),
					ammo.getDef().getName()
				)
		));

	}


	/**
	 * Determines the primary ammunition equipped by the player for a ranged weapon or falls back to the ammunition
	 * stored in the player's quiver if the primary is not valid or unavailable. The method validates that the equipped
	 * weapon and ammunition are compatible for use and adheres to weapon-specific conditions.
	 *
	 * @param player The player for whome the equipped or quivered ammunition is being retrieved.
	 * @return The {@link Item} corresponding to the player's primary equipped ammunition if valid, or the quivered
	 * ammunition if no valid primary is equipped. Returns null if no valid ammunition is found.
	 */
	public static Item getPrimaryOrQuiverAmmo(Player player) {
		var weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		if (weapon != null) {
			var bowOrCrossbow = weapon.getDef().rangedWeapon;
			if (bowOrCrossbow != null) {
				if (bowOrCrossbow.isWeaponSelfSupplied())
					return null;
				if (bowOrCrossbow.isWeaponSelfGeneratingAmmo())
					return null;
				var primaryAmmo = player.getEquipment().get(Equipment.SLOT_AMMO);
				if (primaryAmmo == null)
					return getQuiverAmmo(player);
				var ammo = primaryAmmo.getDef().rangedAmmo;
				if (ammo == null)
					return getQuiverAmmo(player);
				if (!bowOrCrossbow.allowAmmo(ammo))
					return getQuiverAmmo(player);
				return primaryAmmo;
			}
		}
		return null;
	}

	/**
	 * Retrieves an {@link Item} representing the ammunition currently stored in the player's quiver.
	 * Validates that the quiver contains both a valid ammunition ID and a positive amount.
	 * If the validation fails, an assertion is triggered to notify that the quiver is empty.
	 *
	 * @param player The player whose quiver ammunition is being retrieved.
	 * @return An {@link Item} containing the ammunition ID and amount from the player's quiver.
	 *         If the quiver contains no valid ammunition, an assertion failure occurs.
	 */
	private static Item getQuiverAmmo(Player player) {
		var ammoId = VarPlayerRepository.DIZANA_QUIVER_AMMO_ID.get(player);
		var ammoAmount = VarPlayerRepository.DIZANA_QUIVER_AMMO_AMOUNT.get(player);
		if (ammoId < 0 || ammoAmount < 0) return null;
		return new Item(ammoId, ammoAmount);
	}


	/**
	 * Calculates the ranged combat bonus based on the player's equipped weapon and ammunition.
	 * Prioritizes the combat bonus from the primary equipped ammunition if valid; otherwise,
	 * attempts to retrieve the combat bonus from quivered ammunition. If neither is valid or present,
	 * a bonus of zero is returned.
	 *
	 * @param player The player whose combat bonus is being calculated.
	 * @return The ranged combat bonus from the equipped primary ammunition or quivered ammunition,
	 *         or zero if no valid bonus can be determined.
	 */
	public static int getPrimaryOrQuiverCombatBonus(Player player) {
		var equipmentBonus = player.getEquipment().bonuses[EquipmentStats.RANGED_STRENGTH];
		var weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		if (weapon == null)
			return equipmentBonus;
		var bowOrCrossbow = weapon.getDef().rangedWeapon;
		if (bowOrCrossbow == null)
			return equipmentBonus;
		if (bowOrCrossbow.isWeaponSelfSupplied())
			return equipmentBonus + Blowpipe.getMagmaOrDart(weapon).getDartRangedStrengthBonus();
		if (bowOrCrossbow.isWeaponSelfGeneratingAmmo())
			return equipmentBonus;
		var primaryAmmo = player.getEquipment().get(Equipment.SLOT_AMMO);
		if (primaryAmmo == null)
			return getQuiveredBonusOrZero(player);
		var ammo = primaryAmmo.getDef().rangedAmmo;
		if (ammo == null)
			return getQuiveredBonusOrZero(player);
		if (!bowOrCrossbow.allowAmmo(ammo))
			return getQuiveredBonusOrZero(player);
		return equipmentBonus;
	}

	/**
	 * Retrieves the ranged strength bonus of the ammunition inside the player's quivered item
	 * if it is a valid quiver. If the equipped cape is not a valid quiver or no bonus is found,
	 * returns zero.
	 *
	 * @param player The player whose quivered ranged strength bonus is being calculated.
	 * @return The ranged strength bonus of the ammunition in the player's quiver
	 *         or zero if no valid bonus is found.
	 */
	private static int getQuiveredBonusOrZero(Player player) {
		var equipmentBonus = player.getEquipment().bonuses[EquipmentStats.RANGED_STRENGTH];
		var capeSlot = player.getEquipment().get(Equipment.SLOT_CAPE);
		if (capeSlot != null && VALID_QUIVER_IDS.contains(capeSlot.getId())) {
			var newAmmo = getQuiverAmmo(player);
			if (newAmmo == null) {
				var ammoSlotItem = player.getEquipment().get(Equipment.SLOT_AMMO);
				if (ammoSlotItem != null) {
					var ammoRangeStrength = ammoSlotItem.getDef().equipBonuses[EquipmentStats.RANGED_STRENGTH];
					var rangeStrength = equipmentBonus - ammoRangeStrength;
					return rangeStrength + DizanaQuiver.getPrimaryOrQuiverCombatBonus(player);
				}
				return equipmentBonus;
			}
			// check if the weapon can fire this ammo
			if (canWeaponFireAmmo(player, newAmmo.getDef().rangedAmmo)) {
				var ammoSlotItem = player.getEquipment().get(Equipment.SLOT_AMMO);
				if (ammoSlotItem != null) {
					var ammoRangeStrength = ammoSlotItem.getDef().equipBonuses[EquipmentStats.RANGED_STRENGTH];
					return (equipmentBonus - ammoRangeStrength) + newAmmo.getDef().equipBonuses[EquipmentStats.RANGED_STRENGTH];
				}
				return equipmentBonus + newAmmo.getDef().equipBonuses[EquipmentStats.RANGED_STRENGTH];
			}
		}
		return player.getEquipment().bonuses[EquipmentStats.RANGED_STRENGTH];
	}

	private static boolean canWeaponFireAmmo(Player player, RangedAmmo ammo) {
		var weaponItem = player.getEquipment().get(Equipment.SLOT_WEAPON);
		if (weaponItem == null)
			return false;
		var weapon = weaponItem.getDef().rangedWeapon;
		if (weapon == null || ammo == null)
			return false;
		return weapon.allowAmmo(ammo);
	}
}
