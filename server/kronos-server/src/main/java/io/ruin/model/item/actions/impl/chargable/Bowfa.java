package io.ruin.model.item.actions.impl.chargable;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;

import static io.ruin.model.item.attributes.AttributeTypes.FULLY_LOADED;

public class Bowfa {

	private static final int BOWFA_INACTIVE = 25862;
	private static final int BOWFA_CHARGED = 25865;
	private static final int BOWFA_CORRUPT = 25867;
	private static final int MAX_AMOUNT = 20000;
	private static final int CRYSTAL_SHARD = 23962;

	public static void register() {
		ItemItemAction.register(BOWFA_INACTIVE, CRYSTAL_SHARD, Bowfa::charge);
		ItemItemAction.register(BOWFA_CHARGED, CRYSTAL_SHARD, Bowfa::charge);
		ItemAction.registerInventory(BOWFA_CHARGED, "check", Bowfa::check);
		ItemAction.registerEquipment(BOWFA_CHARGED, "check", Bowfa::check);
		ItemAction.registerEquipment(BOWFA_INACTIVE, "check", Bowfa::check);
		ItemAction.registerInventory(BOWFA_CHARGED, "uncharge", Bowfa::uncharge);
		ItemAction.registerInventory(BOWFA_INACTIVE, "charge", (player, item) -> {
			charge(player, item, player.getInventory().findItem(CRYSTAL_SHARD));
		});
		ItemAction.registerInventory(BOWFA_CHARGED, "charge", (player, item) -> {
			charge(player, item, player.getInventory().findItem(CRYSTAL_SHARD));
		});

		ObjType.get(BOWFA_CHARGED).addPostTargetDefendListener((player, item, hit, target) -> {
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
					item.setId(BOWFA_INACTIVE);
				item.putAttribute(AttributeTypes.CHARGES, charges);
			}
		});
	}

	public static void charge(Player player, Item bowfa, Item crystalShard) {
		int charges = getChargeAmount(bowfa);
		int remainingSpace = MAX_AMOUNT - charges;

		if (remainingSpace <= 0) {
			player.sendMessage("Your bow is already fully charged.");
			return;
		}

		// Prompt the player with options
		player.dialogue(new OptionsDialogue(
			Color.DARK_RED.wrap("How would you like to charge your bow?"),
			new Option("Charge it regularly.", () -> chargeRegular(player, bowfa, crystalShard)),
			new Option("Corrupt my bow with 2,000 shards.", () -> chargeCorrupted(player, bowfa)),
			new Option("Cancel.", Player::closeDialogue)
		));
	}

	private static void chargeRegular(Player player, Item bowfa, Item crystalShard) {
		int remainingSpace = MAX_AMOUNT - getChargeAmount(bowfa);
		int maxChargesToAdd = remainingSpace * 100;
		int shardsToAdd = Math.min(crystalShard.getAmount(), maxChargesToAdd / 100);

		// Standard charge logic
		int chargesToAdd = Math.min(shardsToAdd * 100, remainingSpace);
		int shardsToDecrement = (chargesToAdd + 99) / 100;

		player.getInventory().remove(crystalShard.getId(), shardsToDecrement);
		bowfa.putAttribute(AttributeTypes.CHARGES, getChargeAmount(bowfa) + chargesToAdd);

		// If the bow was inactive, set it to charged
		if (bowfa.getId() == BOWFA_INACTIVE) {
			bowfa.setId(BOWFA_CHARGED);
		}

		check(player, bowfa);
	}


	private static void chargeCorrupted(Player player, Item bowfa) {
		int shardsRequired = 2000;

		// Check if the player has either the inactive or charged bow and the required number of crystal shards
		bowfa.clearAttributes();
		if (((bowfa.getId() == BOWFA_INACTIVE && player.getInventory().contains(BOWFA_INACTIVE, 1))
			|| (bowfa.getId() == BOWFA_CHARGED && player.getInventory().contains(BOWFA_CHARGED, 1)))
			&& player.getInventory().contains(CRYSTAL_SHARD, shardsRequired)) {

			// Remove the corresponding bowfa version
			player.getInventory().remove(bowfa.getId(), 1);

			// Remove 2000 crystal shards
			player.getInventory().remove(CRYSTAL_SHARD, shardsRequired);

			// Add the corrupted version (ID: 25867)
			player.getInventory().add(BOWFA_CORRUPT, 1);
			player.sendMessage("You have charged the bow with 2,000 shards and upgraded it to the corrupted version.");
		} else {
			player.sendMessage("You do not have the required items to upgrade to the corrupted version.");
		}
	}


	private static void uncharge(Player player, Item bowfaCharged) {
		int remainingCharges = getChargeAmount(bowfaCharged);

		if (remainingCharges > 0) {
			int shardsToReturn = remainingCharges / 100;
			player.dialogue(new YesNoDialogue("Are you sure you want to uncharge it?",
				"If you uncharge the bow, you will receive " + shardsToReturn + " crystal shard(s).", bowfaCharged, () -> {
				player.getInventory().add(CRYSTAL_SHARD, shardsToReturn);
				bowfaCharged.putAttribute(AttributeTypes.CHARGES, 0);
				bowfaCharged.setId(BOWFA_INACTIVE);
			}));
		} else {
			player.sendMessage("Your bow is already uncharged.");
		}
	}

	private static void check(Player player, Item bowfaCharged) {
		int charges = getChargeAmount(bowfaCharged);
		String chargeInfo = NumberUtils.formatOnePlace(((double) charges / MAX_AMOUNT) * 100D) + "%, " + charges + " charges";
		player.sendMessage("Charges: <col=007f00>" + chargeInfo + "</col>");
	}


	public static void setChargesBowfa(Item bowfa, int charges) {
		bowfa.putAttribute(AttributeTypes.CHARGES, charges);
	}

	public static int getChargeAmount(Item bowfa) {
		return bowfa.getAttributeInt(AttributeTypes.CHARGES);
	}

}
