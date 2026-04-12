package io.ruin.model.item.actions.impl.itemeffects.itemhandlers;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.actions.impl.chargable.SanguinestiStaff;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;

public class VenomTipped {

	public static void consumeCharge(Player player, Item item, AttackStyle attackStyle, Entity entity) {
		if (attackStyle == AttackStyle.CANNON)
			return;
		if (AttributeExtensions.getCharges(AttributeTypes.VENOM_TIPPED, item) > 0) {
			if (Random.get(4) == 0)
				entity.envenom(12);
			AttributeExtensions.deincrementCharges(AttributeTypes.VENOM_TIPPED, item, 1);
		} else {
			player.sendMessage(Color.RED.wrap("Your venom tipped effect has run out of charges!"));
			AttributeExtensions.clearCharges(AttributeTypes.VENOM_TIPPED, item);
		}
	}

	public static void check(Player player, Item item) {
		player.sendFilteredMessage("The venom tipped effect on your " + item.getDef().name + " has " + NumberUtils.formatNumber(AttributeExtensions.getCharges(AttributeTypes.VENOM_TIPPED, item)) + " charges remaining.");
	}

	public static void uncharge(Player player, Item item) {
		player.dialogue(new OptionsDialogue(Color.DARK_RED.wrap("Are you sure you want to remove your venom tipped effect from this weapon?"),
			new Option("Proceed. (This cannot be undone)", () -> {
				AttributeExtensions.clearCharges(AttributeTypes.VENOM_TIPPED, item);
				player.sendMessage("You remove the venom tipped effect from your " + item.getDef().name + ".");
				player.closeDialogue();
			}),
			new Option("Cancel.", Player::closeDialogue)
		));
	}

	public static void wield(Player player, Item item) {
		if (AttributeExtensions.hasAttribute(item, AttributeTypes.VENOM_TIPPED))
			check(player, item);
	}


}
