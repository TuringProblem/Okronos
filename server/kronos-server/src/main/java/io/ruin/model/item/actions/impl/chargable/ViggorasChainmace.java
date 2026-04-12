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
import io.ruin.model.map.Bounds;

import static io.ruin.model.item.attributes.AttributeTypes.FULLY_LOADED;

public class ViggorasChainmace {

	private static final int CHARGED = 22545;
	private static final int UNCHARGED = 22542;

	private static final int MAX_AMOUNT = 16000;
	private static final int REVENANT_ETHER = 21820;
	private static final int ACTIVATION_AMOUNT = 1000;
	private static final Bounds KBD_BOUNDS = new Bounds(2256, 4679, 2287, 4711, 0);

	public static void register() {
		ItemAction.registerInventory(CHARGED, "check", ViggorasChainmace::check);
		ItemAction.registerEquipment(CHARGED, "check", ViggorasChainmace::check);
		ItemAction.registerInventory(CHARGED, "uncharge", ViggorasChainmace::uncharge);
		ItemAction.registerInventory(UNCHARGED, "dismantle", ViggorasChainmace::dismantle);
		ItemItemAction.register(CHARGED, REVENANT_ETHER, ViggorasChainmace::charge);
		ItemItemAction.register(UNCHARGED, REVENANT_ETHER, ViggorasChainmace::charge);
		// ItemItemAction.register(UNCHARGED, 30563, ViggorasChainmace::addCallistoClaws);
		//ItemItemAction.register(30563, UNCHARGED, (player, fangs, bow) -> addCallistoClaws(player, bow, fangs));
		//ItemItemAction.register(CHARGED, 30563, ViggorasChainmace::addCallistoClaws);
		// ItemItemAction.register(30563, CHARGED, (player, fangs, bow) -> addCallistoClaws(player, bow, fangs));

		ObjType.get(CHARGED).addPreTargetDefendListener((player, item, hit, target) -> {
			if (hit.attackStyle != null && hit.attackStyle.isMelee() && target.npc != null && player.wildernessLevel > 0 || player.getPosition().inBounds(KBD_BOUNDS)) {
				if (consumeCharge(player, item)) {
					hit.boostAttack(1.2); //50% accuracy increase
					hit.boostDamage(1.2); //50% damage increase
				}
			}
		});
	}

	private static void check(Player player, Item sceptre) {
		int etherAmount = AttributeExtensions.getCharges(sceptre);
		player.sendMessage("Your chainmace has " + (etherAmount) + " charge" + (etherAmount <= 1 ? "" : "s") + " left powering it.");
	}

	private static void addCallistoClaws(Player player, Item bow, Item fangs) {
		if (bow.getId() == CHARGED && fangs.getId() == 30563) {
			player.dialogue(new OptionsDialogue(
				Color.DARK_RED.wrap("Create ursine chainmace? (this cannot be undone)"),
				new Option("Proceed.", () -> {
					player.getInventory().remove(30563, 1);
					bow.setId(8872);
				}),
				new Option("Cancel.", Player::closeDialogue)
			));
		} else if (bow.getId() == UNCHARGED && fangs.getId() == 30563) {
			player.dialogue(new OptionsDialogue(
				Color.DARK_RED.wrap("Create ursine chainmace? (this cannot be undone)"),
				new Option("Proceed.", () -> {
					player.getInventory().remove(30563, 1);
					bow.setId(30566);
				}),
				new Option("Cancel.", Player::closeDialogue)
			));
		}
	}

	private static void charge(Player player, Item chainmace, Item etherItem) {
		int etherAmount = AttributeExtensions.getCharges(chainmace);
		int allowedAmount = MAX_AMOUNT - etherAmount;
		if (allowedAmount == 0) {
			player.sendMessage("Viggora's Chainmace can't hold any more revenant ether.");
			return;
		}
		if (etherAmount == 0 && etherItem.getAmount() < ACTIVATION_AMOUNT) {
			player.sendMessage("You require at least 1000 revenant ether to activate this weapon.");
			return;
		}
		int addAmount = Math.min(allowedAmount, etherItem.getAmount());
		etherItem.incrementAmount(-addAmount);
		AttributeExtensions.addCharges(chainmace, addAmount);
		etherAmount = AttributeExtensions.getCharges(chainmace);
		chainmace.setId(CHARGED);
		if (etherAmount == 0)
			player.sendMessage("You use 1000 ether to activate the weapon.");
		player.sendMessage("You add a further " + (etherAmount == 0 ? addAmount - ACTIVATION_AMOUNT : addAmount)
			+ " revenant ether to your weapon giving it a total of " + (AttributeExtensions.getCharges(chainmace) - ACTIVATION_AMOUNT) + " charges");
	}

	private static void uncharge(Player player, Item chainmace) {
		player.dialogue(new YesNoDialogue("Are you sure you want to uncharge it?", "If you uncharge this weapon, all the revenant ether will be returned to your inventory.", chainmace, () -> {
			int etherAmount = AttributeExtensions.getCharges(chainmace);
			player.getInventory().add(REVENANT_ETHER, etherAmount);
			AttributeExtensions.setCharges(chainmace, 0);
			chainmace.setId(UNCHARGED);
		}));
	}

	private static void dismantle(Player player, Item chainmace) {
		player.dialogue(new YesNoDialogue("Are you sure you want to dismantle it?", "If you uncharge this weapon, 7500 revenant ether will be returned to your inventory.", chainmace, () -> {
			int etherAmount = AttributeExtensions.getCharges(chainmace);
			player.getInventory().add(REVENANT_ETHER, 7500);
			chainmace.remove();
		}));
	}

	public static boolean consumeCharge(Player player, Item item) {
		if (AttributeExtensions.getCharges(item) <= 1000) {
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
