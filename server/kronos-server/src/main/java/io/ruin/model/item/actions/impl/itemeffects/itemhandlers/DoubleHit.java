package io.ruin.model.item.actions.impl.itemeffects.itemhandlers;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;

public class DoubleHit {
	public static boolean consumeCharge(Player player, Item item, Entity target, AttackStyle attackStyle) {
		if (attackStyle == AttackStyle.CANNON)
			return false;
		if (AttributeExtensions.getCharges(AttributeTypes.DOUBLE_HIT, item) > 0) {
			AttributeExtensions.deincrementCharges(AttributeTypes.DOUBLE_HIT, item, 1);
			return Random.get(20) == 0 && target instanceof NPC;
		} else {
			player.sendMessage(Color.RED.wrap("Your double hit effect has run out of charges!"));
			AttributeExtensions.clearCharges(AttributeTypes.DOUBLE_HIT, item);
		}
		return false;
	}

	public static void check(Player player, Item item) {
		player.sendFilteredMessage("The double hit effect on your " + item.getDef().name + " has " + NumberUtils.formatNumber(AttributeExtensions.getCharges(AttributeTypes.DOUBLE_HIT, item)) + " charges remaining.");
	}

	public static void uncharge(Player player, Item item) {
		player.dialogue(new OptionsDialogue(Color.DARK_RED.wrap("Are you sure you want to remove your double hit effect from this weapon?"),
			new Option("Proceed. (This cannot be undone)", () -> {
				AttributeExtensions.clearCharges(AttributeTypes.DOUBLE_HIT, item);
				player.sendMessage("You remove the double hit effect from your " + item.getDef().name + ".");
				player.closeDialogue();
			}),
			new Option("Cancel.", Player::closeDialogue)
		));
	}

	public static void wield(Player player, Item item) {
		if (AttributeExtensions.hasAttribute(item, AttributeTypes.DOUBLE_HIT))
			check(player, item);
	}
}
