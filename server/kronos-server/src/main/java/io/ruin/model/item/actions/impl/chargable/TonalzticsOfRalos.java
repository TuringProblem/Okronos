package io.ruin.model.item.actions.impl.chargable;

import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;

public class TonalzticsOfRalos {
	private static final int SCALES = 28924;
	private static final int UNCHARGED = 28919, CHARGED = 28922;
	private static final int MAX_AMOUNT = 20000;

	private static void check(Player player, Item ralos) {
		String scales;
		int scalesAmount = AttributeExtensions.getCharges(ralos);
		if (scalesAmount == 0)
			scales = "0.0%, 0 charges";
		else
			scales = NumberUtils.formatOnePlace(((double) scalesAmount / MAX_AMOUNT) * 100D) + "%, " + scalesAmount + " scales";
		player.sendMessage("Scales: <col=007f00>" + scales + "</col>");
	}


	public static void consumeCharge(Player player, Item item) {
		System.out.println("consume charge: " + AttributeExtensions.getCharges(item));
		AttributeExtensions.deincrementCharges(item, 1);
		System.out.println("charges after decrement: " + AttributeExtensions.getCharges(item));
		int charges = AttributeExtensions.getCharges(item);
		if (charges <= 0) {
			player.sendMessage(Color.RED.wrap("Your tonalztics of ralos has ran out of charges!"));
			item.setId(UNCHARGED);
		}
	}

	public static void register() {

		ItemAction.registerInventory(UNCHARGED, "check", TonalzticsOfRalos::check);
		ItemAction.registerInventory(CHARGED, "check", TonalzticsOfRalos::check);
		ItemAction.registerEquipment(CHARGED, "check", TonalzticsOfRalos::check);

	}
}
