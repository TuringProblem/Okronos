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
import io.ruin.model.stat.StatType;

public class DamageForHireLow {
	public static void consumeCharge(Player player, Item item, AttackStyle attackStyle, int damage, Entity entity) {
		if (attackStyle == AttackStyle.CANNON)
			return;
		if (AttributeExtensions.getCharges(AttributeTypes.DAMAGE_FOR_HIRE_LOW, item) > 0) {
			if (entity instanceof NPC) {
				if (!entity.npc.getDef().name.contains("dummy")) {
					player.damageForHireLowCount += damage;
					if (player.damageForHireLowCount >= 250) {
						player.graphics(529);
						player.damageForHireLowCount = 0;
						player.getInventory().addOrDrop(new Item(995, 35000));
						player.sendFilteredMessage("Your damage for hire effect rewards you with some coins.");
					}
				}
			}
			AttributeExtensions.deincrementCharges(AttributeTypes.DAMAGE_FOR_HIRE_LOW, item, 1);
		} else {
			player.sendMessage(Color.RED.wrap("Your damage for hire (low) effect has run out of charges!"));
			AttributeExtensions.clearCharges(AttributeTypes.DAMAGE_FOR_HIRE_LOW, item);
		}
	}

	public static void check(Player player, Item item) {
		player.sendFilteredMessage("The damage for hire (low) effect on your " + item.getDef().name + " has " + NumberUtils.formatNumber(AttributeExtensions.getCharges(AttributeTypes.DAMAGE_FOR_HIRE_LOW, item)) + " charges remaining.");
	}

	public static void uncharge(Player player, Item item) {
		player.dialogue(new OptionsDialogue(Color.DARK_RED.wrap("Are you sure you want to remove your damage for hire (low) effect from this weapon?"),
			new Option("Proceed. (This cannot be undone)", () -> {
				AttributeExtensions.clearCharges(AttributeTypes.DAMAGE_FOR_HIRE_LOW, item);
				player.sendMessage("You remove the damage for hire (low) effect from your " + item.getDef().name + ".");
				player.closeDialogue();
			}),
			new Option("Cancel.", Player::closeDialogue)
		));
	}

	public static void wield(Player player, Item item) {
		if (AttributeExtensions.hasAttribute(item, AttributeTypes.DAMAGE_FOR_HIRE_LOW))
			check(player, item);
	}
}
