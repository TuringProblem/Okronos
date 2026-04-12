package io.ruin.model.item.actions.impl.chargable;

import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.Color;
import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;

public class TumekensShadow {

	public static final int CHARGED = 27275;
	public static final int UNCHARGED = 27277;
	public static final int MAX_CHARGES = 20_000;

	public static void consumeCharge(Player player, Item staff, Hit hit, Entity entity) {

		if (AttributeExtensions.getCharges(staff) > 0) {
			AttributeExtensions.deincrementCharges(staff, 1);
			if (AttributeExtensions.getCharges(staff) % 1000 == 0) {
				player.sendMessage("Your " + staff.getDef().name + " has " + NumberUtils.formatNumber(AttributeExtensions.getCharges(staff)) + " charges remaining.");
			}

		} else {
			player.sendMessage(Color.RED.wrap("Your staff has ran out of charges!"));
			staff.setId(UNCHARGED);
			player.getCombat().updateWeapon(false);

		}
	}

	public static void check(Player player, Item staff) {
		player.sendMessage("Your Tumeken's shadow has " + NumberUtils.formatNumber(AttributeExtensions.getCharges(staff)) + " charges remaining.");
	}

	public static void uncharge(Player player, Item staff) {
		int charges = AttributeExtensions.getCharges(staff);
		player.dialogue(new OptionsDialogue(Color.DARK_RED.wrap("Uncharge your staff for all its charges? (regaining " + NumberUtils.formatNumber(charges * 3) + " blood runes)."),
			new Option("Proceed.", () -> {
				player.getInventory().add(ItemID.SOUL_RUNE, charges * 2);
				player.getInventory().add(ItemID.CHAOS_RUNE, charges * 5);
				AttributeExtensions.setCharges(staff, 0);
				staff.setId(UNCHARGED);
				player.dialogue(new ItemDialogue().one(CHARGED, "You uncharge your Tumeken's shadow, regaining " +
					NumberUtils.formatNumber(charges * 3) + " blood runes in the process."));
			}),
			new Option("Cancel.", Player::closeDialogue)
		));
	}

	public static void charge(Player player, Item staff) {
		int currentCharges = AttributeExtensions.getCharges(staff);
		if (currentCharges >= MAX_CHARGES) {
			player.sendMessage("Your staff can't hold any more charges.");
			return;
		}
		int chaosRunes = player.getInventory().getAmount(ItemID.CHAOS_RUNE);
		int soulRunes = player.getInventory().getAmount(ItemID.SOUL_RUNE);
		int chargesInInventory = Math.min(chaosRunes / 5, soulRunes / 2);
		if (chargesInInventory == 0) {
			player.sendMessage("You require soul and chaos runes to charge your staff.");
			return;
		}
		int chargesToAdd = Math.min(chargesInInventory, MAX_CHARGES - currentCharges);
		player.integerInput("How many charges do you want to apply? (Up to " + NumberUtils.formatNumber(chargesToAdd) + ")", (input) -> {
			int allowed = MAX_CHARGES - currentCharges;
			if (input < 1 || input > allowed) {
				player.sendMessage("You can only apply between 1 and " + NumberUtils.formatNumber(allowed) + " charges.");
				return;
			}
			if (input > chargesInInventory)
				input = chargesInInventory;
			int chaosCost = input * 5;
			int soulCost = input * 2;
			player.getInventory().remove(ItemID.CHAOS_RUNE, chaosCost);
			player.getInventory().remove(ItemID.SOUL_RUNE, soulCost);
			AttributeExtensions.addCharges(staff, input);
			staff.setId(CHARGED);
			check(player, staff);
		});
	}

	public static void charge(Player player, Item staff, Item rune) {
		charge(player, staff);
	}

	private static void wield(Player player, Item item) {
		player.sendMessage("Your Tumeken's shadow has no charges! You can use blood runes to power the staff.");
	}

	public static void register() {

		ItemItemAction.register(UNCHARGED, ItemID.BLOOD_RUNE, TumekensShadow::charge);
		ItemAction.registerInventory(UNCHARGED, "charge", TumekensShadow::charge);
		ItemAction.registerInventory(UNCHARGED, "wield", TumekensShadow::wield);

		ItemItemAction.register(CHARGED, ItemID.BLOOD_RUNE, TumekensShadow::charge);
		ItemAction.registerInventory(CHARGED, "charge", TumekensShadow::charge);
		ItemAction.registerEquipment(CHARGED, "check", TumekensShadow::check);
		ItemAction.registerInventory(CHARGED, "check", TumekensShadow::check);
		ItemAction.registerInventory(CHARGED, "uncharge", TumekensShadow::uncharge);
		ObjType.get(CHARGED).addPreTargetDefendListener(TumekensShadow::consumeCharge);

	}

}
