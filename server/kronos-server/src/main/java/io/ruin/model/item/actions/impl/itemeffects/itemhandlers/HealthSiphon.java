package io.ruin.model.item.actions.impl.itemeffects.itemhandlers;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;

public class HealthSiphon {
	public static void consumeCharge(Player player, Item item, int hit, Entity entity, AttackStyle attackStyle) {
		if (attackStyle == AttackStyle.CANNON)
			return;

		if (AttributeExtensions.getCharges(AttributeTypes.HEALTH_SIPHON, item) > 0) {
			if (Random.get(10) == 0 && player.getHp() > 0)
				player.hit(new Hit(HitType.HEAL).fixedDamage(hit / 2));
			AttributeExtensions.deincrementCharges(AttributeTypes.HEALTH_SIPHON, item, 1);
		} else {
			player.sendMessage(Color.RED.wrap("Your health siphon effect has run out of charges!"));
			AttributeExtensions.clearCharges(AttributeTypes.HEALTH_SIPHON, item);
		}
	}

	public static void check(Player player, Item item) {
		player.sendFilteredMessage("The health siphon effect on your " + item.getDef().name + " has " + NumberUtils.formatNumber(AttributeExtensions.getCharges(AttributeTypes.HEALTH_SIPHON, item)) + " charges remaining.");
	}

	public static void uncharge(Player player, Item item) {
		player.dialogue(new OptionsDialogue(Color.DARK_RED.wrap("Are you sure you want to remove your health siphon effect from this weapon?"),
			new Option("Proceed. (This cannot be undone)", () -> {
				AttributeExtensions.clearCharges(AttributeTypes.HEALTH_SIPHON, item);
				player.sendMessage("You remove the health siphon effect from your " + item.getDef().name + ".");
				player.closeDialogue();
			}),
			new Option("Cancel.", Player::closeDialogue)
		));
	}

	public static void wield(Player player, Item item) {
		if (AttributeExtensions.hasAttribute(item, AttributeTypes.HEALTH_SIPHON))
			check(player, item);
	}

}
