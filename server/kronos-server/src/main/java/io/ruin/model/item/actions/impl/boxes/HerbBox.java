package io.ruin.model.item.actions.impl.boxes;

import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;

public class HerbBox {

	private static final LootTable chanceTable = new LootTable().addTable(1,
		new LootItem(250, 1, 40),   //Guam leaf
		new LootItem(252, 1, 40),   //Marrentill
		new LootItem(254, 1, 40),   //Tarromin
		new LootItem(256, 1, 40),   //Harralander
		new LootItem(258, 1, 35),    //Ranarr weed
		new LootItem(2999, 1, 25),   //Toadflax
		new LootItem(260, 1, 25),    //Irit leaf
		new LootItem(262, 1, 25),    //Avantoe
		new LootItem(264, 1, 20),    //Kwuarm
		new LootItem(3001, 1, 20),   //Snapdragon
		new LootItem(266, 1, 20),    //Cadantine
		new LootItem(2482, 1, 20),   //Lantadyme
		new LootItem(268, 1, 20),     //Dwarf weed
		new LootItem(270, 1, 20)   //Torstol
	);

	private static void open(Player player, Item item) {
		if (freeSlots(player) < 15) {
			player.sendMessage("You will need up to 15 free inventory spaces to open the herb box.");
			return;
		} else {
			for (int i = 0; i < 15; i++) {
				int itemId = chanceTable.rollItem().getId();
				player.getInventory().add(itemId, 1);
			}
			player.sendMessage("The herbs from your herb box(es) have been added to your inventory.");
			item.remove();
		}
	}

	private static void bankAll(Player player, Item item) {
		if (player.getGameMode().isUltimateIronman()) {
			player.sendMessage("Ultimate ironman accounts aren't able to have their herbs sent to the bank.");
		} else {
			for (int i = 0; i < 15; i++) {
				int itemId = chanceTable.rollItem().getId();
				player.getBank().add(itemId - 1, 1);
			}
			player.sendMessage("The herbs from your herb box(es) have been deposited into your bank.");
			item.remove();
		}
	}

	private static int freeSlots(Player player) {
		int freeSlots = player.getInventory().getFreeSlots();
		if (player.getInventory().hasId(250))
			freeSlots++;
		if (player.getInventory().hasId(252))
			freeSlots++;
		if (player.getInventory().hasId(254))
			freeSlots++;
		if (player.getInventory().hasId(256))
			freeSlots++;
		if (player.getInventory().hasId(258))
			freeSlots++;
		if (player.getInventory().hasId(260))
			freeSlots++;
		if (player.getInventory().hasId(262))
			freeSlots++;
		if (player.getInventory().hasId(264))
			freeSlots++;
		if (player.getInventory().hasId(266))
			freeSlots++;
		if (player.getInventory().hasId(268))
			freeSlots++;
		if (player.getInventory().hasId(270))
			freeSlots++;
		if (player.getInventory().hasId(2482))
			freeSlots++;
		if (player.getInventory().hasId(2999))
			freeSlots++;
		if (player.getInventory().hasId(3001))
			freeSlots++;
		return freeSlots;
	}

	private static final int HERB_BOX = 11738;

	public static void register() {
		ItemAction.registerInventory(HERB_BOX, "take-all", HerbBox::open);
		ItemAction.registerInventory(HERB_BOX, "bank-all", HerbBox::bankAll);
	}
}
