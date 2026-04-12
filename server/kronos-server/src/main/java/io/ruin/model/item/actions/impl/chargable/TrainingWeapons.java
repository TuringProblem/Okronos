package io.ruin.model.item.actions.impl.chargable;

import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.Color;
import io.ruin.cache.ObjType;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;

public class TrainingWeapons {

	public static void consumeCharge(Player player, Item item, Hit hit, Entity entity) {
		if (AttributeExtensions.getCharges(item) > 0) {
			boolean chargesChanged = false;
			int currentCharges = AttributeExtensions.getCharges(item);
			AttributeExtensions.deincrementCharges(item, 1);
			if (currentCharges != AttributeExtensions.getCharges(item)) {
				chargesChanged = true;
			}
			if (AttributeExtensions.getCharges(item) % 500 == 0 && chargesChanged) {
				player.sendMessage("Your " + item.getDef().name + " has " + NumberUtils.formatNumber(AttributeExtensions.getCharges(item)) + " charges remaining.");
			}
		} else {
			player.sendMessage(Color.RED.wrap("Your " + item.getDef().name + " has ran out of charges and turned to dust!"));
			item.remove();
			player.getCombat().updateWeapon(false);
		}
	}

	public static void check(Player player, Item item) {
		player.sendMessage("Your " + item.getDef().name + " has " + NumberUtils.formatNumber(AttributeExtensions.getCharges(item)) + " charges remaining.");
	}

	public static void register() {
		ObjType.get(30478).addPreTargetDefendListener(TrainingWeapons::consumeCharge);
		ObjType.get(30479).addPreTargetDefendListener(TrainingWeapons::consumeCharge);
		ObjType.get(30480).addPreTargetDefendListener(TrainingWeapons::consumeCharge);
		ItemAction.registerEquipment(30478, "check", TrainingWeapons::check);
		ItemAction.registerInventory(30478, "check", TrainingWeapons::check);
		ItemAction.registerEquipment(30479, "check", TrainingWeapons::check);
		ItemAction.registerInventory(30479, "check", TrainingWeapons::check);
		ItemAction.registerEquipment(30480, "check", TrainingWeapons::check);
		ItemAction.registerInventory(30480, "check", TrainingWeapons::check);
	}

}
