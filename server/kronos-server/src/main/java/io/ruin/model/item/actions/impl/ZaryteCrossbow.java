package io.ruin.model.item.actions.impl;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Common;

/**
 * @author R-Y-M-R
 * @date 3/7/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public class ZaryteCrossbow {
	public static final int id = 26374; // zaryte
	public static final String SPEC_ATTRIBUTE = "ZARYTE_SPEC_FORCE_PROC"; // This is the key. The value will be a boolean.
	private static final int NIHIL_HORN = 26372;
	private static final int NIHIL_SHARD = 26231;

	public static void register() {
		ItemItemAction.register(ItemID.ARMADYL_CROSSBOW, NIHIL_HORN, ZaryteCrossbow::attemptCreateZaryte);
		ItemItemAction.register(ItemID.ARMADYL_CROSSBOW, NIHIL_SHARD, ZaryteCrossbow::attemptCreateZaryte);
	}

	/**
	 * Verify the player has all required materials, and send them dialogues explaining what will happen.
	 *
	 * @param player
	 * @param item
	 * @param b
	 */

	private static void attemptCreateZaryte(Player player, Item item, Item b) {
		if (!player.getInventory().contains(NIHIL_SHARD, 250)) {
			player.dialogue(new MessageDialogue("You need at least 250 Nihil shards to create a Zaryte crossbow."));
			return;
		}
		if (!player.getInventory().contains(NIHIL_HORN)) {
			player.dialogue(new MessageDialogue("You need a Nihil horn to create a Zaryte crossbow."));
			return;
		}
		if (player.getInventory().findItemExact(ItemID.ARMADYL_CROSSBOW) == null) {
			player.dialogue(new MessageDialogue("You need an Armadyl Crossbow to create a Zaryte crossbow."));
			return;
		}
		player.dialogue(new MessageDialogue("Creating a Zaryte crossbow requires 250 Nihil shards, a Nihil horn, and an Armadyl crossbow. This process cannot be reversed."),
			new OptionsDialogue("Create a Zaryte crossbow? This process is final.",
				new Option("Yes", () -> createZaryte(player, item, b)),
				new Option("No", player::closeDialogue)));
	}

	/**
	 * The actual process of creating a Zaryte. Removes players items. Adds zaryte. Sends a message explaining wtf happened.
	 *
	 * @param player
	 * @param item
	 * @param b
	 */
	private static void createZaryte(Player player, Item item, Item b) {
		// Issue#501: if the xbow has attributes, then make a copy of them to add to the zaryte.
		var crossbow = item;
		if (b.getId() == ItemID.ARMADYL_CROSSBOW)
			crossbow = b;
		var zaryteCrossbow = new Item(ItemID.ZARYTE_CROSSBOW, 1);
		if (!crossbow.copyOfAttributes().isEmpty())
			zaryteCrossbow.attributes = crossbow.copyOfAttributes();

		player.closeDialogue();
		player.getInventory().remove(NIHIL_SHARD, 250);
		player.getInventory().remove(NIHIL_HORN, 1);
		player.getInventory().remove(player.getInventory().findItemExact(ItemID.ARMADYL_CROSSBOW));
		player.getInventory().addOrDrop(zaryteCrossbow);
		Common.sendTwoItemMessage(player, item, b, "You combine your items into a Zaryte crossbow.");
		player.getStats().addXp(StatType.Fletching, 10, false);
	}


	/**
	 * Non-null getter
	 *
	 * @param player
	 * @return
	 */
	public static boolean isWorn(Player player) {
		Item crossbow = player.getEquipment().get(Equipment.SLOT_WEAPON);
		if (crossbow != null) {
			if (crossbow.getId() == id)
				return true;
		}
		return false;
	}

	/**
	 * Checks if players have the spec attribute on them. If it's true, set it to false & return true;
	 *
	 * @param player
	 * @return
	 */
	public static boolean forciblyProcBolt(Player player) {
		if (player.get(SPEC_ATTRIBUTE) != null) {
			if (player.get(SPEC_ATTRIBUTE)) {
				player.set(SPEC_ATTRIBUTE, false);
				return true;
			}
		}
		return false;
	}

}
