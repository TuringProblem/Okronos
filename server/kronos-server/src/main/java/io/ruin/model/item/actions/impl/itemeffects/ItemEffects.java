package io.ruin.model.item.actions.impl.itemeffects;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;

public enum ItemEffects {
	VENOM_TIPPED(
		"Venom Tipped",
		"Use this on any weapon of your choice to add a venom effect to it.",
		AttributeTypes.VENOM_TIPPED,
		new Item(30537)
	),
	DAMAGE_FOR_HIRE_LOW(
		"Damage for Hire (Low)",
		"Use this on any weapon of your choice and you'll be given coins after every 500 damage dealt.",
		AttributeTypes.DAMAGE_FOR_HIRE_LOW,
		new Item(30535)
	),
	DAMAGE_FOR_HIRE_HIGH(
		"Damage for Hire (High)",
		"Use this on any weapon of your choice and you'll be given coins after every 500 damage dealt.",
		AttributeTypes.DAMAGE_FOR_HIRE_HIGH,
		new Item(30536)
	),
	ARMOUR_BREAKER(
		"Armour Breaker",
		"Use this on any weapon of your choice to add a 5% chance to lower your opponent's defence.",
		AttributeTypes.ARMOUR_BREAKER,
		new Item(30543)
	),
	CRITICAL_HIT(
		"Critical Hit",
		"Use this on any weapon of your choice to add a 5% chance to deal a critical hit.",
		AttributeTypes.CRITICAL_HIT,
		new Item(30541)
	),
	DOUBLE_HIT(
		"Double Hit",
		"Use this on any weapon of your choice to add a 5% chance to deal a double hit.",
		AttributeTypes.DOUBLE_HIT,
		new Item(30534)
	),
	SPECIAL_ENERGY_MANAGEMENT(
		"Special Energy Management",
		"Use this on any weapon of your choice to make it use 25% less special energy when performing the special attack.",
		AttributeTypes.SPECIAL_ENERGY_LOWERER,
		new Item(30540)
	),
	AOE(
		"AoE Swing",
		"Use this on any weapon to have a 5% chance to perform an AoE attack.",
		AttributeTypes.AOE_SWING,
		new Item(30531)
	),
	HEALTH_SIPHON(
		"Health Siphon",
		"Use this on any weapon to have a 5% chance to siphon 10% of your hit.",
		AttributeTypes.HEALTH_SIPHON,
		new Item(30538)
	),
	FREEZE_CHANCE(
		"Freeze Chance",
		"Use this on any weapon to have a 5% chance to freeze your opponent for 15 seconds every hit.",
		AttributeTypes.FREEZE,
		new Item(30533)
	),
	SIPHON_THE_DEAD(
		"Siphon the Dead",
		"Use this on any weapon to and you will heal 10% of the maximum health of any creature you kill.",
		AttributeTypes.SIPHON_THE_DEAD,
		new Item(30532)
	),
	RESPECT_FOR_THE_DEAD(
		"Respect for the Dead",
		"Use this on any weapon to and you will automatically bury or scatter the bones and ashes of any creature you kill for 3x the experience.",
		AttributeTypes.RESPECT_FOR_THE_DEAD,
		new Item(30539)
	),

	;

	ItemEffects(String name, String description, AttributeTypes effect, Item sigilItem) {
		this.name = name;
		this.description = description;
		this.effect = effect;
		this.sigilItem = sigilItem;
	}

	public static final ItemEffects[] VALUES = values();

	String name;
	String description;

	AttributeTypes effect;
	Item sigilItem;

	private void enhanceWeapon(Player player, Item item) {
		if (item.getDef().equipSlot == Equipment.SLOT_WEAPON) {
			player.getInventory().remove(sigilItem.getId(), 1);
			AttributeExtensions.setCharges(effect, item, 10000);
			item.update();
			player.sendMessage("You enhance your " + item.getDef().name + " with the " + name + " effect.");
		}
	}

	public void useOnWeapon(Player player, Item weapon) {
		if (canUseEffect(weapon)) {
			player.dialogue(new OptionsDialogue(
				Color.DARK_RED.wrap("Charge your " + weapon.getDef().name + " with the " + name + " effect?"),
				new Option("Proceed.", () -> {
					enhanceWeapon(player, weapon);
				}),
				new Option("Cancel.", Player::closeDialogue)
			));
		} else {
			player.sendMessage("This weapon already has an effect on it.");
		}
	}

	private boolean canUseEffect(Item item) {
		if (AttributeExtensions.getCharges(AttributeTypes.VENOM_TIPPED, item) > 0)
			return false;
		if (AttributeExtensions.getCharges(AttributeTypes.DAMAGE_FOR_HIRE_LOW, item) > 0)
			return false;
		if (AttributeExtensions.getCharges(AttributeTypes.DAMAGE_FOR_HIRE_HIGH, item) > 0)
			return false;
		if (AttributeExtensions.getCharges(AttributeTypes.ARMOUR_BREAKER, item) > 0)
			return false;
		if (AttributeExtensions.getCharges(AttributeTypes.CRITICAL_HIT, item) > 0)
			return false;
		if (AttributeExtensions.getCharges(AttributeTypes.DOUBLE_HIT, item) > 0)
			return false;
		if (AttributeExtensions.getCharges(AttributeTypes.SPECIAL_ENERGY_LOWERER, item) > 0)
			return false;
		if (AttributeExtensions.getCharges(AttributeTypes.AOE_SWING, item) > 0)
			return false;
		if (AttributeExtensions.getCharges(AttributeTypes.HEALTH_SIPHON, item) > 0)
			return false;
		if (AttributeExtensions.getCharges(AttributeTypes.FREEZE, item) > 0)
			return false;
		if (AttributeExtensions.getCharges(AttributeTypes.SIPHON_THE_DEAD, item) > 0)
			return false;
		if (AttributeExtensions.getCharges(AttributeTypes.RESPECT_FOR_THE_DEAD, item) > 0)
			return false;
		return true;
	}

	private static void inspect(Player player, ItemEffects effect) {
		player.dialogue(new ItemDialogue().one(effect.sigilItem.getId(), effect.description));
	}

	public Item getSigilItem() {
		return sigilItem;
	}

	public static void register() {
		for (ItemEffects effect : ItemEffects.VALUES) {
			ItemAction.registerInventory(effect.sigilItem.getId(), "inspect", (player, item) -> inspect(player, effect));
		}
	}

}
