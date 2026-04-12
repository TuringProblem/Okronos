package io.ruin.model.item.actions.impl.itemeffects.itemhandlers;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;

public class FreezeChance {
	public static void consumeCharge(Player player, Item item, NPC npc, AttackStyle attackStyle) {
		if (attackStyle == AttackStyle.CANNON)
			return;
		if (AttributeExtensions.getCharges(AttributeTypes.FREEZE, item) > 0) {
			if (Random.get(20) == 0) {
				npc.freeze(15, npc);
				npc.graphics(2005);
			}
			AttributeExtensions.deincrementCharges(AttributeTypes.FREEZE, item, 1);
		} else {
			player.sendMessage(Color.RED.wrap("Your freeze chance effect has run out of charges!"));
			AttributeExtensions.clearCharges(AttributeTypes.FREEZE, item);
		}
	}

	public static void check(Player player, Item item) {
		player.sendFilteredMessage("The freeze chance effect on your " + item.getDef().name + " has " + NumberUtils.formatNumber(AttributeExtensions.getCharges(AttributeTypes.FREEZE, item)) + " charges remaining.");
	}

	public static void uncharge(Player player, Item item) {
		player.dialogue(new OptionsDialogue(Color.DARK_RED.wrap("Are you sure you want to remove your freeze chance effect from this weapon?"),
			new Option("Proceed. (This cannot be undone)", () -> {
				AttributeExtensions.clearCharges(AttributeTypes.FREEZE, item);
				player.sendMessage("You remove the freeze chance effect from your " + item.getDef().name + ".");
				player.closeDialogue();
			}),
			new Option("Cancel.", Player::closeDialogue)
		));
	}

	public static void wield(Player player, Item item) {
		if (AttributeExtensions.hasAttribute(item, AttributeTypes.FREEZE))
			check(player, item);
	}
}
