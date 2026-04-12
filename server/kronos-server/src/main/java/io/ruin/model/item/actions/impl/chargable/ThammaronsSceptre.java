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
import io.ruin.model.item.attributes.AttributeTypes;

import static io.ruin.model.item.attributes.AttributeTypes.FULLY_LOADED;

public class ThammaronsSceptre {

	private static final int CHARGED = 22555;
	private static final int UNCHARGED = 22552;

	private static final int MAX_AMOUNT = 16000;
	private static final int REVENANT_ETHER = 21820;
	private static final int ACTIVATION_AMOUNT = 1000;

	public static void register() {
		ItemAction.registerInventory(CHARGED, "check", ThammaronsSceptre::check);
		ItemAction.registerEquipment(CHARGED, "check", ThammaronsSceptre::check);
		ItemAction.registerInventory(CHARGED, "uncharge", ThammaronsSceptre::uncharge);
		ItemAction.registerInventory(UNCHARGED, "dismantle", ThammaronsSceptre::dismantle);
		// ItemItemAction.register(UNCHARGED, 30561, ThammaronsSceptre::addVetionSkull);
		// ItemItemAction.register(30561, UNCHARGED, (player, fangs, bow) -> addVetionSkull(player, bow, fangs));
		// ItemItemAction.register(CHARGED, 30561, ThammaronsSceptre::addVetionSkull);
		// ItemItemAction.register(30561, CHARGED, (player, fangs, bow) -> addVetionSkull(player, bow, fangs));

		ItemItemAction.register(CHARGED, REVENANT_ETHER, ThammaronsSceptre::charge);
		ItemItemAction.register(UNCHARGED, REVENANT_ETHER, ThammaronsSceptre::charge);
		ObjType.get(CHARGED).addPreTargetDefendListener((player, item, hit, target) -> {
			if (hit.attackStyle != null && hit.attackStyle.isMagic() && target.npc != null && player.wildernessLevel > 0) {
				hit.boostAttack(1); //100% accuracy increase
				hit.boostDamage(1.25); //25% damage increase
				int charges = AttributeExtensions.getCharges(item);
				if (charges <= 0) {
					item.setId(UNCHARGED);
					player.sendMessage(Color.DARK_RED.wrap("Your weapon has run out of revenant ether!"));
				}
				charges--;
				item.putAttribute(AttributeTypes.CHARGES, charges);
			}
		});
	}

	private static void check(Player player, Item sceptre) {
		int etherAmount = AttributeExtensions.getCharges(sceptre);
		player.sendMessage("Your sceptre has " + (etherAmount) + " charge" + (etherAmount <= 1 ? "" : "s") + " left powering it.");
	}

	private static void charge(Player player, Item sceptre, Item etherItem) {
		int etherAmount = AttributeExtensions.getCharges(sceptre);
		int allowedAmount = MAX_AMOUNT - etherAmount;
		if (allowedAmount == 0) {
			player.sendMessage("Thammaron's sceptre can't hold any more revenant ether.");
			return;
		}
		if (etherAmount == 0 && etherItem.getAmount() < ACTIVATION_AMOUNT) {
			player.sendMessage("You require at least 1000 revenant ether to activate this weapon.");
			return;
		}
		int addAmount = Math.min(allowedAmount, etherItem.getAmount());
		etherItem.incrementAmount(-addAmount);
		AttributeExtensions.addCharges(sceptre, addAmount);
		etherAmount = AttributeExtensions.getCharges(sceptre);
		sceptre.setId(CHARGED);
		if (etherAmount == 0)
			player.sendMessage("You use 1000 ether to activate the weapon.");
		player.sendMessage("You add a further " + (etherAmount == 0 ? addAmount - ACTIVATION_AMOUNT : addAmount)
			+ " revenant ether to your weapon giving it a total of " + (sceptre.getAttributeInt(AttributeTypes.CHARGES) - ACTIVATION_AMOUNT) + " charges");
	}

	private static void uncharge(Player player, Item sceptre) {
		player.dialogue(new YesNoDialogue("Are you sure you want to uncharge it?", "If you uncharge this weapon, all the revenant ether will be returned to your inventory.", sceptre, () -> {
			int etherAmount = AttributeExtensions.getCharges(sceptre);
			player.getInventory().add(REVENANT_ETHER, etherAmount);
			AttributeExtensions.setCharges(sceptre, 0);
			sceptre.setId(UNCHARGED);
		}));
	}

	private static void addVetionSkull(Player player, Item bow, Item fangs) {
		if (bow.getId() == CHARGED && fangs.getId() == 30561) {
			player.dialogue(new OptionsDialogue(
				Color.DARK_RED.wrap("Create accursed sceptre? (this cannot be undone)"),
				new Option("Proceed.", () -> {
					player.getInventory().remove(30561, 1);
					bow.setId(8876);
				}),
				new Option("Cancel.", Player::closeDialogue)
			));
		} else if (bow.getId() == UNCHARGED && fangs.getId() == 30561) {
			player.dialogue(new OptionsDialogue(
				Color.DARK_RED.wrap("Create accursed sceptre? (this cannot be undone)"),
				new Option("Proceed.", () -> {
					player.getInventory().remove(30561, 1);
					bow.setId(30565);
				}),
				new Option("Cancel.", Player::closeDialogue)
			));
		}
	}

	private static void dismantle(Player player, Item sceptre) {
		player.dialogue(new YesNoDialogue("Are you sure you want to dismantle it?", "If you uncharge this weapon, 7500 revenant ether will be returned to your inventory.", sceptre, () -> {
			int etherAmount = AttributeExtensions.getCharges(sceptre);
			player.getInventory().add(REVENANT_ETHER, 7500);
			sceptre.remove();
		}));
	}

	public static boolean consumeCharge(Player player, Item item) {
		int charges = AttributeExtensions.getCharges(item);
		if (charges <= 1000) {
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
