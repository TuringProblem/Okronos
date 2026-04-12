package io.ruin.model.item.actions.impl.chargable;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;

import static io.ruin.model.item.attributes.AttributeTypes.FULLY_LOADED;

public class TomeOfWater {

	private static final int UNCHARGED = 25576;
	private static final int CHARGED = 25574;
	private static final int SOAKED_PAGE = 25578;
	private static final int MAX_CHARGES = 20000;

	public static void register() {
		ItemAction.registerEquipment(CHARGED, "check", TomeOfWater::check);
		ItemAction.registerInventory(CHARGED, "check", TomeOfWater::check);

		ItemAction.registerInventory(CHARGED, "Add or Remove pages", TomeOfWater::addOrRemovePages);
		ItemAction.registerEquipment(CHARGED, "Add or Remove pages", TomeOfWater::addOrRemovePages);

		ItemAction.registerInventory(UNCHARGED, "Add pages", TomeOfWater::addPages);
		ItemAction.registerEquipment(UNCHARGED, "Add pages", TomeOfWater::addPages);

		ItemItemAction.register(CHARGED, SOAKED_PAGE, (player, primary, secondary) -> addPages(player, primary));
		ItemItemAction.register(UNCHARGED, SOAKED_PAGE, (player, primary, secondary) -> addPages(player, primary));
	}

	private static void addOrRemovePages(Player player, Item primary) {
		player.dialogue(new OptionsDialogue(
			new Option("Add all pages.", () -> addPages(player, primary)),
			new Option("Remove pages.", () -> pagesToRemove(player, primary)))
		);
	}

	private static void pagesToRemove(Player player, Item primary) {
		player.dialogue(new OptionsDialogue(
			new Option("Remove one page.", () -> removePages(player, primary, 1, false)),
			new Option("Remove X pages.", () -> player.integerInput("Remove how many pages? (0-" + AttributeExtensions.getCharges(primary) / 20 + ")", pagesToAdd -> removePages(player, primary, pagesToAdd, false))),
			new Option("Empty book.", () -> removePages(player, primary, MAX_CHARGES, true))
		));
	}

	private static void addPages(Player player, Item tomeOfWater) {
		int charges = AttributeExtensions.getCharges(tomeOfWater);
		int allowedAmount = MAX_CHARGES - charges;
		if (allowedAmount == 0) {
			player.sendMessage("Your tome of water can't hold anymore burnt pages.");
			return;
		}
		Item burntPage = player.getInventory().findItem(SOAKED_PAGE);
		if (burntPage == null) {
			player.sendMessage("You have no pages to add to your tome.");
			return;
		}
		int addAmount = Math.min(allowedAmount, burntPage.getAmount() * 20);
		tomeOfWater.putAttribute(AttributeTypes.CHARGES, charges + addAmount);
		burntPage.incrementAmount(-addAmount / 20);
		tomeOfWater.setId(CHARGED);
		check(player, tomeOfWater);
	}

	private static void removePages(Player player, Item tomeOfWater, int pagesToRemove, boolean emptyBook) {
		int charges = AttributeExtensions.getCharges(tomeOfWater);
		if (charges < 20) {
			player.sendMessage("Your Tome of water doesn't have any pages to remove.");
			return;
		}
		if (player.getInventory().isFull() && player.getInventory().count(SOAKED_PAGE) == 0) {
			player.sendMessage("You need at least 1 free inventory space to do this.");
			return;
		}
		int toRemove = pagesToRemove;
		if (pagesToRemove > charges / 20)
			toRemove = charges / 20;
		while (pagesToRemove-- > 0) {
			if (tomeOfWater.getAttributeInt(AttributeTypes.CHARGES) < 20)
				break;
			tomeOfWater.putAttribute(AttributeTypes.CHARGES, tomeOfWater.getAttributeInt(AttributeTypes.CHARGES) - 20);
			player.getInventory().add(SOAKED_PAGE, 1);
			if (tomeOfWater.getAttributeInt(AttributeTypes.CHARGES) <= 0)
				tomeOfWater.setId(UNCHARGED);
		}
		if (emptyBook)
			player.sendMessage("You empty your book of pages.");
		else if (toRemove == 1)
			player.sendMessage("You remove a page from the book.");
		else
			player.sendMessage("You remove " + toRemove + " pages from the book.");
	}

	private static void check(Player player, Item tomeOfWater) {
		int charges = AttributeExtensions.getCharges(tomeOfWater);
		player.sendMessage("Your tome currently holds " + (charges) + " charge" + (charges <= 1 ? "" : "s") + ".");
	}

	public static boolean consumeCharge(Player player) {
		Item item = player.getEquipment().get(Equipment.SLOT_SHIELD); //but this karen says no

		if (item == null || item.getId() != CHARGED) {
			return false;
		}
		if (player.getEquipment().hasId(1387) || player.getEquipment().hasId(1393) ||
			player.getEquipment().hasId(3054) || player.getEquipment().hasId(11787) ||
			player.getEquipment().hasId(12795) || player.getEquipment().hasId(12796)) {
			return false;
		}
		if (AttributeExtensions.hasAttribute(item, FULLY_LOADED)) {
			int level = AttributeExtensions.getCharges(FULLY_LOADED, item);
			int chance = 5 - level;
			if (Random.get(chance) == 0)
				return false;
		}
		int charges = AttributeExtensions.getCharges(item);
		item.putAttribute(AttributeTypes.CHARGES, charges - 1);
		if (charges == 0) {
			player.sendMessage(Color.RED.wrap("Your tome has ran out of charges!"));
			item.setId(UNCHARGED);
		}
		return true;
	}

}
