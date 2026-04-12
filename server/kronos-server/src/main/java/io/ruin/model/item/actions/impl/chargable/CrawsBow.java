package io.ruin.model.item.actions.impl.chargable;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;

import static io.ruin.model.item.attributes.AttributeTypes.FULLY_LOADED;

public class CrawsBow {

	private static final int CHARGED = 22550;
	private static final int UNCHARGED = 22547;

	private static final int MAX_AMOUNT = 16000;
	private static final int REVENANT_ETHER = 21820;
	private static final int ACTIVATION_AMOUNT = 1000;

	public static void register() {
		ItemAction.registerInventory(CHARGED, "check", CrawsBow::check);
		ItemAction.registerEquipment(CHARGED, "check", CrawsBow::check);
		ItemAction.registerInventory(CHARGED, "uncharge", CrawsBow::uncharge);
		ItemAction.registerInventory(UNCHARGED, "dismantle", CrawsBow::dismantle);
		ItemItemAction.register(CHARGED, REVENANT_ETHER, CrawsBow::charge);
		ItemItemAction.register(UNCHARGED, REVENANT_ETHER, CrawsBow::charge);
		//ItemItemAction.register(UNCHARGED, 30562, CrawsBow::addVenenatisFangs);
		//ItemItemAction.register(30562, UNCHARGED, (player, fangs, bow) -> addVenenatisFangs(player, bow, fangs));
		// ItemItemAction.register(CHARGED, 30562, CrawsBow::addVenenatisFangs);
		// ItemItemAction.register(30562, CHARGED, (player, fangs, bow) -> addVenenatisFangs(player, bow, fangs));
		ObjType.get(CHARGED).addPreTargetDefendListener((player, item, hit, target) -> {
			if (hit.attackStyle != null && hit.attackStyle.isRanged() && target.npc != null && player.wildernessLevel > 0) {
				if (consumeCharge(player, item)) {
					hit.boostAttack(1.2); //50% accuracy increase
					hit.boostDamage(1.2); //50% damage increase
				}
			}
		});
	}

	private static void addVenenatisFangs(Player player, Item bow, Item fangs) {
		if (bow.getId() == CHARGED && fangs.getId() == 30562) {
			player.dialogue(new OptionsDialogue(
				Color.DARK_RED.wrap("Create webweaver bow? (this cannot be undone)"),
				new Option("Proceed.", () -> {
					player.getInventory().remove(30562, 1);
					bow.setId(8874);
				}),
				new Option("Cancel.", Player::closeDialogue)
			));
		} else if (bow.getId() == UNCHARGED && fangs.getId() == 30562) {
			player.dialogue(new OptionsDialogue(
				Color.DARK_RED.wrap("Create webweaver bow? (this cannot be undone)"),
				new Option("Proceed.", () -> {
					player.getInventory().remove(30562, 1);
					bow.setId(30568);
				}),
				new Option("Cancel.", Player::closeDialogue)
			));
		}
	}

	private static void check(Player player, Item bow) {
		int etherAmount = AttributeExtensions.getCharges(bow);
		player.sendMessage("Your bow has " + (etherAmount) + " charge" + (etherAmount <= 1 ? "" : "s") + " left powering it.");
	}

	private static void charge(Player player, Item crawsBow, Item etherItem) {
		int etherAmount = AttributeExtensions.getCharges(crawsBow);
		int allowedAmount = MAX_AMOUNT - etherAmount;
		if (allowedAmount == 0) {
			player.sendMessage("Craw's Bow can't hold any more revenant ether.");
			return;
		}
		if (etherAmount == 0 && etherItem.getAmount() < ACTIVATION_AMOUNT) {
			player.sendMessage("You require at least 1000 revenant ether to activate this weapon.");
			return;
		}
		int addAmount = Math.min(allowedAmount, etherItem.getAmount());
		int newTotal = etherAmount + (addAmount);
		etherItem.incrementAmount(-addAmount);
		AttributeExtensions.setCharges(crawsBow, newTotal);
		etherAmount = AttributeExtensions.getCharges(crawsBow);
		crawsBow.setId(CHARGED);
		if (etherAmount == 0)
			player.sendMessage("You use 1000 ether to activate the weapon.");
		player.sendMessage("You add a further " + (etherAmount == 0 ? addAmount - ACTIVATION_AMOUNT : addAmount)
			+ " revenant ether to your weapon giving it a total of " + (etherAmount - ACTIVATION_AMOUNT) + " charges");
	}

	private static void uncharge(Player player, Item crawsBow) {
		player.dialogue(new YesNoDialogue("Are you sure you want to uncharge it?", "If you uncharge this weapon, all the revenant ether will be returned to your inventory.", crawsBow, () -> {
			int etherAmount = AttributeExtensions.getCharges(crawsBow);
			player.getInventory().add(REVENANT_ETHER, etherAmount);
			AttributeExtensions.setCharges(crawsBow, 0);
			crawsBow.setId(UNCHARGED);
			player.getCombat().updateWeapon(false);
		}));
	}

	private static void dismantle(Player player, Item crawsbow) {
		player.dialogue(new YesNoDialogue("Are you sure you want to dismantle it?", "If you uncharge this weapon, 7500 revenant ether will be returned to your inventory.", crawsbow, () -> {
			int etherAmount = AttributeExtensions.getCharges(crawsbow);
			player.getInventory().add(REVENANT_ETHER, 7500);
			crawsbow.remove();
		}));
	}

	public static boolean consumeCharge(Player player, Item item) {
		int currentCharges = AttributeExtensions.getCharges(item);
		if (currentCharges <= 1000) {
			player.sendMessage(Color.DARK_RED.wrap("Your weapon has run out of revenant ether!"));
			return false;
		}
		boolean removeScale = true;
		if (AttributeExtensions.hasAttribute(item, FULLY_LOADED)) {
			int level = AttributeExtensions.getCharges(FULLY_LOADED, item);
			int chance = 5 - level;
			if (Random.get(chance) == 0)
				removeScale = false;
		}
		if (removeScale) {
			AttributeExtensions.deincrementCharges(item, 1);
		}
		return true;

	}
}
