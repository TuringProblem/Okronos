package io.ruin.model.item.actions.impl.combine;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;

import java.util.HashMap;
import java.util.Map;

public class NightmareStaffs {

	/**
	 * Orbs
	 */
	private static final int HARMONISED_ORB = 24511;
	private static final int VOLATILE_ORB = 24514;
	private static final int ELDRITCH_ORB = 24517;
	private static final int CORRUPTED_ORB = 25934;

	/**
	 * KROTA Staff
	 */
	private static final int Krota_STAFF = 24422;

	/**
	 * KROTA staffs with Orb
	 */
	private static final int HARMONISED_NIGHTMARESTAFF = 24423;
	private static final int VOLATILE_NIGHTMARESTAFF = 24424;
	private static final int ELDRITCH_NIGHTMARESTAFF = 24425;
	private static final int CORRUPTED_NIGHTMARESTAFF = 25932;

	private static void combine(Player player, Item itemOne, Item itemTwo, int result) {
		if (itemOne.getId() != Krota_STAFF && itemTwo.getId() != Krota_STAFF) {
			player.sendMessage("You need to use the orb on a regular nightmare staff.");
			return;
		}
		Map<String, String> attributes = new HashMap<>();
		attributes.putAll(itemTwo.attributes);
		Item primary = new Item(result);
		primary.attributes.putAll(attributes);
		itemOne.remove();
		itemTwo.remove();
		player.getInventory().add(primary);
	}

	private static void dismantle(Player player, Item item, int resultOne, int resultTwo) {
		if (player.getInventory().getFreeSlots() < 1) {
			player.sendMessage("You don't have enough free space to do this.");
			return;
		}
		Map<String, String> attributes = new HashMap<>();
		attributes.putAll(item.attributes);
		Item primary = new Item(resultTwo);
		primary.attributes.putAll(attributes);
		item.remove();
		player.getInventory().add(resultOne, 1);
		player.getInventory().add(primary);
	}

	public static void register() {
		/**
		 * Combine
		 */
		ItemItemAction.register(HARMONISED_ORB, Krota_STAFF, (player, primary, secondary) -> combine(player, primary, secondary, HARMONISED_NIGHTMARESTAFF));
		ItemItemAction.register(VOLATILE_ORB, Krota_STAFF, (player, primary, secondary) -> combine(player, primary, secondary, VOLATILE_NIGHTMARESTAFF));
		ItemItemAction.register(ELDRITCH_ORB, Krota_STAFF, (player, primary, secondary) -> combine(player, primary, secondary, ELDRITCH_NIGHTMARESTAFF));
		ItemItemAction.register(CORRUPTED_ORB, Krota_STAFF, (player, primary, secondary) -> combine(player, primary, secondary, CORRUPTED_NIGHTMARESTAFF));

		/**
		 * Dismantle
		 */
		ItemAction.registerInventory(HARMONISED_NIGHTMARESTAFF, "dismantle", (player, item) -> dismantle(player, item, HARMONISED_ORB, Krota_STAFF));
		ItemAction.registerInventory(VOLATILE_NIGHTMARESTAFF, "dismantle", (player, item) -> dismantle(player, item, VOLATILE_ORB, Krota_STAFF));
		ItemAction.registerInventory(ELDRITCH_NIGHTMARESTAFF, "dismantle", (player, item) -> dismantle(player, item, ELDRITCH_ORB, Krota_STAFF));
		ItemAction.registerInventory(CORRUPTED_NIGHTMARESTAFF, "dismantle", (player, item) -> dismantle(player, item, CORRUPTED_ORB, Krota_STAFF));
	}

}
