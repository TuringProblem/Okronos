package io.ruin.model.item.actions.impl.chargable;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;

import static io.ruin.model.item.attributes.AttributeTypes.FULLY_LOADED;

public class BladeOfSaeldor {

	private static final int BLADE_INACTIVE = 23997;
	private static final int BLADE_CHARGED = 23995;
	private static final int BLADE_CORRUPTED = 24551;
	private static final int MAX_AMOUNT = 20000;
	private static final int CRYSTAL_SHARD = 23962;

	public static void register() {
		ItemItemAction.register(BLADE_INACTIVE, CRYSTAL_SHARD, BladeOfSaeldor::charge);
		ItemItemAction.register(BLADE_CHARGED, CRYSTAL_SHARD, BladeOfSaeldor::charge);
		ItemAction.registerInventory(BLADE_CHARGED, "check", BladeOfSaeldor::check);
		ItemAction.registerEquipment(BLADE_CHARGED, "check", BladeOfSaeldor::check);
		ItemAction.registerEquipment(BLADE_INACTIVE, "check", BladeOfSaeldor::check);
		ItemAction.registerInventory(BLADE_CHARGED, "uncharge", BladeOfSaeldor::uncharge);
		ItemAction.registerInventory(BLADE_INACTIVE, "charge", (player, item) -> {
			charge(player, item, player.getInventory().findItem(CRYSTAL_SHARD));
		});
		ItemAction.registerInventory(BLADE_CHARGED, "charge", (player, item) -> {
			charge(player, item, player.getInventory().findItem(CRYSTAL_SHARD));
		});

		ObjType.get(BLADE_CHARGED).addPostTargetDefendListener((player, item, hit, target) -> {
			int charges = getChargeAmount(item);
			boolean removeScale = true;
			if (AttributeExtensions.hasAttribute(item, FULLY_LOADED)) {
				int level = AttributeExtensions.getCharges(FULLY_LOADED, item);
				int chance = 5 - level;
				if (Random.get(chance) == 0)
					removeScale = false;
			}
			if (removeScale) {
				if (--charges <= 0)
					item.setId(BLADE_INACTIVE);
				item.putAttribute(AttributeTypes.CHARGES, charges);
			}
		});
	}

	public static void charge(Player player, Item blade, Item crystalShard) {
		int charges = getChargeAmount(blade);
		int remainingSpace = MAX_AMOUNT - charges;

		if (remainingSpace <= 0) {
			player.sendMessage("Your blade is already fully charged.");
			return;
		}

		// Prompt the player with options
		player.dialogue(new OptionsDialogue(
			Color.DARK_RED.wrap("How would you like to charge your blade?"),
			new Option("Charge it regularly.", () -> chargeRegular(player, blade, crystalShard)),
			new Option("Corrupt my blade with 1,000 shards.", () -> chargeCorrupted(player, blade)),
			new Option("Cancel.", Player::closeDialogue)
		));
	}

	private static void chargeRegular(Player player, Item blade, Item crystalShard) {
		int remainingSpace = MAX_AMOUNT - getChargeAmount(blade);
		int maxChargesToAdd = remainingSpace * 100;
		int shardsToAdd = Math.min(crystalShard.getAmount(), maxChargesToAdd / 100);

		// Standard charge logic
		int chargesToAdd = Math.min(shardsToAdd * 100, remainingSpace);
		int shardsToDecrement = (chargesToAdd + 99) / 100;


		player.getInventory().remove(crystalShard.getId(), shardsToDecrement);
		blade.putAttribute(AttributeTypes.CHARGES, getChargeAmount(blade) + chargesToAdd);

		// If the blade was inactive, set it to charged
		if (blade.getId() == BLADE_INACTIVE) {
			blade.setId(BLADE_CHARGED);
		}

		check(player, blade);
	}

	private static void chargeCorrupted(Player player, Item blade) {
		int shardsRequired = 1000;

		// Check if the player has either the inactive or charged bow and the required number of crystal shards
		blade.clearAttributes();
		if (((blade.getId() == BLADE_INACTIVE && player.getInventory().contains(BLADE_INACTIVE, 1))
			|| (blade.getId() == BLADE_CHARGED && player.getInventory().contains(BLADE_CHARGED, 1)))
			&& player.getInventory().contains(CRYSTAL_SHARD, shardsRequired)) {

			// Remove the corresponding bowfa version
			player.getInventory().remove(blade.getId(), 1);

			// Remove 1000 crystal shards
			player.getInventory().remove(CRYSTAL_SHARD, shardsRequired);

			// Add the corrupted version (ID: 25867)
			player.getInventory().add(BLADE_CORRUPTED, 1);
			player.sendMessage("You have charged the blade with 1,000 shards and upgraded it to the corrupted version.");
		} else {
			player.sendMessage("You do not have the required items to upgrade to the corrupted version.");
		}
	}

	private static void uncharge(Player player, Item bladeCharged) {
		int remainingCharges = getChargeAmount(bladeCharged);

		if (remainingCharges > 0) {
			int shardsToReturn = remainingCharges / 100;
			player.dialogue(new YesNoDialogue("Are you sure you want to uncharge it?",
				"If you uncharge the blade, you will receive " + shardsToReturn + " crystal shard(s).", bladeCharged, () -> {
				player.getInventory().add(CRYSTAL_SHARD, shardsToReturn);
				bladeCharged.putAttribute(AttributeTypes.CHARGES, 0);
				bladeCharged.setId(BLADE_INACTIVE);
			}));
		} else {
			player.sendMessage("Your blade is already uncharged.");
		}
	}

	private static void check(Player player, Item bladeCharged) {
		int charges = getChargeAmount(bladeCharged);
		String chargeInfo = NumberUtils.formatOnePlace(((double) charges / MAX_AMOUNT) * 100D) + "%, " + charges + " charges";
		player.sendMessage("Charges: <col=007f00>" + chargeInfo + "</col>");
	}


	public static void setChargesBlade(Item blade, int charges) {
		blade.putAttribute(AttributeTypes.CHARGES, charges);
	}

	public static int getChargeAmount(Item blade) {
		return blade.getAttributeInt(AttributeTypes.CHARGES);
	}

}
