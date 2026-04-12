package io.ruin.model.item.actions.impl.storage;

import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.skills.farming.crop.Crop;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static io.ruin.model.skills.farming.Farming.CROPS;

public class SeedBox {

	public static void register() {
		ItemAction.registerInventory(ItemID.SEED_BOX, "fill", SeedBox::fillBox);
		ItemAction.registerInventory(ItemID.SEED_BOX, "check", SeedBox::checkBox);
		ItemAction.registerInventory(ItemID.SEED_BOX, "empty", SeedBox::emptyBox);
		ItemAction.registerInventory(ItemID.SEED_BOX, "destroy", SeedBox::destroyItem);
		/*
		  Item on bag
		 */
		for (Crop crop : CROPS) {
			ItemItemAction.register(crop.getSeed(), ItemID.SEED_BOX, (player, primary, s) ->
				player.getSeedBox().addToSeedBox(player, primary));
			ItemItemAction.register(ItemID.SEED_BOX, crop.getSeed(), (player, p, secondary) ->
				player.getSeedBox().addToSeedBox(player, secondary));
		}
	}


	/**
	 * Displays the contents of the player's seed box, listing each type of seed
	 * along with its quantity. Sends the information as a filtered message to the player.
	 *
	 * @param player The player whose seed box is being checked.
	 * @param item   The item triggering the action, providing context for the operation.
	 */
	private static void checkBox(Player player, Item item) {
		player.sendFilteredMessage("The seed box contains:");
		for (Map.Entry<Integer, Integer> entry : player.getSeedBox().seeds.entrySet()) {
			int seedId = entry.getKey();
			int seedSize = entry.getValue();
			player.sendFilteredMessage("%d %ss"
				.formatted(seedSize, ObjType.get(seedId).name.toLowerCase()));
		}
	}

	/**
	 * Transfers all seeds from the player's inventory into their seed box.
	 * If seeds are found, they are added to the seed box, and removed from the inventory.
	 * Sends a message to the player indicating the result of the operation.
	 *
	 * @param player The player whose inventory and seed box are being modified.
	 * @param item The item triggering the action, providing context for the operation.
	 */
	private static void fillBox(Player player, Item item) {
		int added = 0;
		for (var seed : player.getInventory().getItems()) {
			if (SeedBox.isSeed(seed)) {
				added += seed.getAmount();
				player.getSeedBox().addToSeedBox(player, seed);
				player.getInventory().remove(item.getId(), seed.getAmount());
			}
		}
		if (added > 0)
			player.sendFilteredMessage("You add the seeds to the seed box.");
		else
			player.sendFilteredMessage("You have no seeds in your inventory that your seed box recognises, use them on it instead.");
	}

	/**
	 * Empties the player's seed box by transferring all seeds to the player's inventory.
	 * If the inventory doesn't have enough free slots, the operation stops.
	 *
	 * @param player The player whose seed box will be emptied.
	 * @param item   The item triggering the action, relevant for context.
	 */
	private static void emptyBox(Player player, Item item) {
		var iterator = player.getSeedBox().seeds
			.entrySet()
			.iterator();

		while (iterator.hasNext()) {
			var entry = iterator.next();
			var seedId = entry.getKey();
			var seedSize = entry.getValue();
			var freeSlots = player.getInventory().getFreeSlots();
			if (freeSlots == 0)
				return;
			if (seedSize == 0)
				continue;
			player.getInventory().add(seedId, seedSize);
			iterator.remove();
		}
	}

	/**
	 * Destroys the specified item and clears related data from the player's seed box.
	 *
	 * @param player The player instance that owns the item and seed box.
	 * @param item The item that is to be destroyed.
	 */
	private static void destroyItem(Player player, Item item) {
		player.dialogue(
			new YesNoDialogue(
				"Are you sure you want to destroy the item?",
				"The contents of the sack will be destroyed with it.",
				item,
				() -> {
					item.remove();
					player.getSeedBox().seeds.clear();
				}
			)
		);
	}


	private static boolean isSeed(Item item) {
		if (item == null) return false;
		return CROPS.stream()
			.anyMatch(crop -> crop.getSeed() == item.getId());
	}

	public Map<Integer, Integer> seeds = new HashMap<>();

	public void addToSeedBox(Player player, Item seed) {
		if (isSeed(seed)) {
			player.getSeedBox().seeds.merge(seed.getId(), seed.getAmount(), Integer::sum);
			player.sendMessage("Added " + seed.getAmount() + " x " + seed.getDef().name + " to your seed box.");
			player.getInventory().remove(seed);
		}
		else player.sendMessage("You can't add this to your seed box.");
	}

	public void emptyToBank(Player player) {
		for (var entry : seeds.entrySet()) {
			int seedId = entry.getKey();
			int seedAmount = entry.getValue();
			if (seedAmount > 0) {
				player.getBank().add(seedId, seedAmount);
				player.getSeedBox().seeds.remove(seedId);
			}
		}
		player.sendMessage("Your seeds have been added to your bank.");
	}
}
