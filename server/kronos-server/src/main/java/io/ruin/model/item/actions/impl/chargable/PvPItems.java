package io.ruin.model.item.actions.impl.chargable;

import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.Color;
import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;

public class PvPItems {
	public static void consumeCharge(Player player, Item item, Hit hit, Entity entity) {
		if (AttributeExtensions.getCharges(item) > 0) {
			AttributeExtensions.deincrementCharges(item, 1);
		} else {
			player.sendMessage(Color.RED.wrap("Your " + item.getDef().name + " has turned to dust!"));
			item.remove();
			player.getCombat().updateWeapon(false);
		}
	}

	public static void degradeItem(Player player, Item item, Hit hit, Entity entity) {
		player.sendMessage("Your " + item.getDef().name + " has degraded.");
		item.setId(getDegradeId(item.getId()));
		AttributeExtensions.setCharges(item, 10000);
	}

	private static int getDegradeId(int id) {
		switch (id) {
			case ItemID.VESTAS_LONGSWORD:
				return 22743;
			case ItemID.VESTAS_SPEAR:
				return 3176;
			case ItemID.VESTAS_CHAINBODY:
				return 30483;
			case ItemID.VESTAS_PLATESKIRT:
				return 30484;
			case ItemID.STATIUSS_FULL_HELM:
				return 30486;
			case ItemID.STATIUSS_PLATEBODY:
				return 30487;
			case ItemID.STATIUSS_PLATELEGS:
				return 30488;
			case ItemID.MORRIGANS_COIF:
				return 30489;
			case ItemID.MORRIGANS_LEATHER_BODY:
				return 30490;
			case ItemID.MORRIGANS_LEATHER_CHAPS:
				return 30491;
			case ItemID.ZURIELS_HOOD:
				return 30493;
			case ItemID.ZURIELS_ROBE_TOP:
				return 30494;
			case ItemID.ZURIELS_ROBE_BOTTOM:
				return 30495;
			case ItemID.ZURIELS_STAFF:
				return 30492;
		}
		return -1;
	}

	public static void check(Player player, Item item) {
		player.sendMessage("Your " + item.getDef().name + " has " + NumberUtils.formatNumber(AttributeExtensions.getCharges(item)) + " charges remaining.");
	}

	public static void register() {
		ObjType.get(ItemID.VESTAS_CHAINBODY).addPreTargetDefendListener(PvPItems::degradeItem);
		ObjType.get(ItemID.VESTAS_PLATESKIRT).addPreTargetDefendListener(PvPItems::degradeItem);
		ObjType.get(ItemID.VESTAS_LONGSWORD).addPreTargetDefendListener(PvPItems::degradeItem);
		ObjType.get(ItemID.VESTAS_SPEAR).addPreTargetDefendListener(PvPItems::degradeItem);
		ObjType.get(ItemID.STATIUSS_FULL_HELM).addPreTargetDefendListener(PvPItems::degradeItem);
		ObjType.get(ItemID.STATIUSS_PLATEBODY).addPreTargetDefendListener(PvPItems::degradeItem);
		ObjType.get(ItemID.STATIUSS_PLATELEGS).addPreTargetDefendListener(PvPItems::degradeItem);
		ObjType.get(ItemID.MORRIGANS_COIF).addPreTargetDefendListener(PvPItems::degradeItem);
		ObjType.get(ItemID.MORRIGANS_LEATHER_BODY).addPreTargetDefendListener(PvPItems::degradeItem);
		ObjType.get(ItemID.MORRIGANS_LEATHER_CHAPS).addPreTargetDefendListener(PvPItems::degradeItem);
		ObjType.get(ItemID.ZURIELS_HOOD).addPreTargetDefendListener(PvPItems::degradeItem);
		ObjType.get(ItemID.ZURIELS_STAFF).addPreTargetDefendListener(PvPItems::degradeItem);
		ObjType.get(ItemID.ZURIELS_ROBE_BOTTOM).addPreTargetDefendListener(PvPItems::degradeItem);
		ObjType.get(ItemID.ZURIELS_ROBE_TOP).addPreTargetDefendListener(PvPItems::degradeItem);
		ObjType.get(22743).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(3176).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(30483).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(30484).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(30486).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(30487).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(30488).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(30489).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(30490).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(30491).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(30492).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(30493).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(30494).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(30495).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(30315).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(30312).addPreTargetDefendListener(PvPItems::consumeCharge);
		ObjType.get(30309).addPreTargetDefendListener(PvPItems::consumeCharge);
		ItemAction.registerInventory(22743, "check", PvPItems::check);
		ItemAction.registerInventory(3176, "check", PvPItems::check);
		ItemAction.registerInventory(30483, "check", PvPItems::check);
		ItemAction.registerInventory(30484, "check", PvPItems::check);
		ItemAction.registerInventory(30486, "check", PvPItems::check);
		ItemAction.registerInventory(30487, "check", PvPItems::check);
		ItemAction.registerInventory(30488, "check", PvPItems::check);
		ItemAction.registerInventory(30489, "check", PvPItems::check);
		ItemAction.registerInventory(30490, "check", PvPItems::check);
		ItemAction.registerInventory(30491, "check", PvPItems::check);
		ItemAction.registerInventory(30492, "check", PvPItems::check);
		ItemAction.registerInventory(30493, "check", PvPItems::check);
		ItemAction.registerInventory(30494, "check", PvPItems::check);
		ItemAction.registerInventory(30495, "check", PvPItems::check);
	}
}
