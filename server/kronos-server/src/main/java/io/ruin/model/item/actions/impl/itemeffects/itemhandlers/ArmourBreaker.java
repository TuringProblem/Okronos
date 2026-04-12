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

public class ArmourBreaker {
	public static void consumeCharge(Player player, Item item, Entity entity, AttackStyle attackStyle) {
		if (attackStyle == AttackStyle.CANNON)
			return;
		if (AttributeExtensions.getCharges(AttributeTypes.ARMOUR_BREAKER, item) > 0) {
			if (entity instanceof NPC) {
				if (Random.get(12) == 0) {
					entity.graphics(1290);
					entity.npc.getCombat().getStat(StatType.Defence).drain(0.10);
				}
				if (!entity.npc.getDef().name.contains("dummy"))
					AttributeExtensions.deincrementCharges(AttributeTypes.ARMOUR_BREAKER, item, 1);
			}
		} else {
			player.sendMessage(Color.RED.wrap("Your armour breaker effect has run out of charges!"));
			AttributeExtensions.clearCharges(AttributeTypes.ARMOUR_BREAKER, item);
		}
	}

	public static void check(Player player, Item item) {
		player.sendFilteredMessage("The armour breaker effect on your " + item.getDef().name + " has " + NumberUtils.formatNumber(AttributeExtensions.getCharges(AttributeTypes.ARMOUR_BREAKER, item)) + " charges remaining.");
	}

	public static void uncharge(Player player, Item item) {
		player.dialogue(new OptionsDialogue(Color.DARK_RED.wrap("Are you sure you want to remove your armour breaker effect from this weapon?"),
			new Option("Proceed. (This cannot be undone)", () -> {
				AttributeExtensions.clearCharges(AttributeTypes.ARMOUR_BREAKER, item);
				player.sendMessage("You remove the armour breaker effect from your " + item.getDef().name + ".");
				player.closeDialogue();
			}),
			new Option("Cancel.", Player::closeDialogue)
		));
	}

	public static void wield(Player player, Item item) {
		if (AttributeExtensions.hasAttribute(item, AttributeTypes.ARMOUR_BREAKER))
			check(player, item);
	}
}
