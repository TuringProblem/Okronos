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
import io.ruin.model.skills.prayer.Bone;
import io.ruin.model.stat.StatType;

public class RespectTheDead {
	public static void consumeCharge(Player player, Item item, double experience, AttackStyle attackStyle) {
		if (attackStyle == AttackStyle.CANNON)
			return;
		if (AttributeExtensions.getCharges(AttributeTypes.RESPECT_FOR_THE_DEAD, item) > 0) {
			AttributeExtensions.deincrementCharges(AttributeTypes.RESPECT_FOR_THE_DEAD, item, 1);
		} else {
			player.sendMessage(Color.RED.wrap("Your respect the dead effect has run out of charges!"));
			AttributeExtensions.clearCharges(AttributeTypes.RESPECT_FOR_THE_DEAD, item);
		}
	}

	public static void getExperience(Player player, Item item, double experience) {
		if (AttributeExtensions.getCharges(AttributeTypes.RESPECT_FOR_THE_DEAD, item) > 0) {
			player.getStats().addXp(StatType.Prayer, experience * 3, true);
		}
	}

	public static void check(Player player, Item item) {
		player.sendFilteredMessage("The respect the dead effect on your " + item.getDef().name + " has " + NumberUtils.formatNumber(AttributeExtensions.getCharges(AttributeTypes.RESPECT_FOR_THE_DEAD, item)) + " charges remaining.");
	}

	public static void uncharge(Player player, Item item) {
		player.dialogue(new OptionsDialogue(Color.DARK_RED.wrap("Are you sure you want to remove your respect the dead effect from this weapon?"),
			new Option("Proceed. (This cannot be undone)", () -> {
				AttributeExtensions.clearCharges(AttributeTypes.RESPECT_FOR_THE_DEAD, item);
				player.sendMessage("You remove the respect the dead effect from your " + item.getDef().name + ".");
				player.closeDialogue();
			}),
			new Option("Cancel.", Player::closeDialogue)
		));
	}

	public static void wield(Player player, Item item) {
		if (AttributeExtensions.hasAttribute(item, AttributeTypes.RESPECT_FOR_THE_DEAD))
			check(player, item);
	}
}
