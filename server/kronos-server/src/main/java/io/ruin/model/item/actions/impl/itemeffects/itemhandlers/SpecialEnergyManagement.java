package io.ruin.model.item.actions.impl.itemeffects.itemhandlers;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;

public class SpecialEnergyManagement {
	public static void consumeCharge(Player player, Item item, AttackStyle attackStyle) {
		if (attackStyle == AttackStyle.CANNON)
			return;
		if (AttributeExtensions.getCharges(AttributeTypes.SPECIAL_ENERGY_LOWERER, item) > 0) {
			AttributeExtensions.deincrementCharges(AttributeTypes.SPECIAL_ENERGY_LOWERER, item, 1);
		} else {
			player.sendMessage(Color.RED.wrap("Your special saver effect has run out of charges!"));
			AttributeExtensions.clearCharges(AttributeTypes.SPECIAL_ENERGY_LOWERER, item);
		}
	}

	public static void check(Player player, Item item) {
		player.sendFilteredMessage("The special saver effect on your " + item.getDef().name + " has " + NumberUtils.formatNumber(AttributeExtensions.getCharges(AttributeTypes.SPECIAL_ENERGY_LOWERER, item)) + " charges remaining.");
	}

	public static void uncharge(Player player, Item item) {
		player.dialogue(new OptionsDialogue(Color.DARK_RED.wrap("Are you sure you want to remove your special saver effect from this weapon?"),
			new Option("Proceed. (This cannot be undone)", () -> {
				AttributeExtensions.clearCharges(AttributeTypes.SPECIAL_ENERGY_LOWERER, item);
				player.sendMessage("You remove the special saver effect from your " + item.getDef().name + ".");
				player.closeDialogue();
			}),
			new Option("Cancel.", Player::closeDialogue)
		));
	}

	public static void wield(Player player, Item item) {
		if (AttributeExtensions.hasAttribute(item, AttributeTypes.SPECIAL_ENERGY_LOWERER))
			check(player, item);
	}
}
