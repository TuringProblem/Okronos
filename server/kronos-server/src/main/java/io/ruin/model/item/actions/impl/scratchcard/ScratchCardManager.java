package io.ruin.model.item.actions.impl.scratchcard;

import io.ruin.api.utils.Random;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;


public class ScratchCardManager {
	Item firstItem;
	Item secondItem;
	Item thirdItem;
	Item winningItem;


	/*
	Percentages
	 */
	private final int matchingItemChance = 3; //1/3

	private static final int INTERFACE_ID = 888;

	private boolean running = false;


	private final int uncommonTableChance = 5; //1/5
	private final int rareTableChance = 15; //1/15
	private final int megaTableChance = 30; //1/30

	private static final LootTable COMMON_TABLE = new LootTable().addTable(35,
		new LootItem(6733, 1, 8), // Archers ring
		new LootItem(6735, 1, 8), // Warrior ring
		new LootItem(6737, 1, 8), // Berserker ring
		new LootItem(6731, 1, 8), // Seers ring
		new LootItem(20724, 1, 1)); // Imbued heart

	private static final LootTable UNCOMMON_TABLE = new LootTable().addTable(25,
		new LootItem(290, 1, 20), // Super Mystery box
		new LootItem(12877, 1, 15), // Dharok's armour set
		new LootItem(12875, 1, 15), // Verac's armour set
		new LootItem(12881, 1, 15), // Ahrim's armour set
		new LootItem(12883, 1, 15), // Karil's armour set
		new LootItem(12873, 1, 15)); // Guthan's armour set

	private static final LootTable RARE_TABLE = new LootTable().addTable(15,
		new LootItem(11832, 1, 15), // Bandos chestplate
		new LootItem(11834, 1, 15), // Bandos tassets
		new LootItem(11826, 1, 15), // Armadyl helm
		new LootItem(11828, 1, 15), // Armadyl chest
		new LootItem(11830, 1, 15), // Armadyl skirt
		new LootItem(11824, 1, 15)); // Zamorak spear

	private static final LootTable MEGA_RARE_TABLE = new LootTable().addTable(5,
		new LootItem(25994, 1, 15), // Sotc
		new LootItem(25991, 1, 15), // SoR
		new LootItem(26078, 1, 15), // SOTMM
		new LootItem(26072, 1, 15), // SOTRR
		new LootItem(30185, 1, 5)); // Pet box


	private Item rollItem(Player player) {
		Item item;
		if (Random.get(1, megaTableChance) == 1)
			item = MEGA_RARE_TABLE.rollItem();
		else if (Random.get(1, rareTableChance) == 1)
			item = RARE_TABLE.rollItem();
		else if (Random.get(1, uncommonTableChance) == 1)
			item = UNCOMMON_TABLE.rollItem();
		else
			item = COMMON_TABLE.rollItem();
		return item;
	}

	public void resetInterface(Player player) {
		player.sendMessage("Please finish completing the scratch card before closing the interface!");
		player.getPacketSender().setHidden(INTERFACE_ID, 22, true);
		player.getPacketSender().setHidden(INTERFACE_ID, 36, true);
		player.getPacketSender().setHidden(INTERFACE_ID, 29, true);
		player.openInterface(ToplevelComponent.MAINMODAL, INTERFACE_ID);
		player.getPacketSender().sendString(INTERFACE_ID, 41, "");
		if (firstItem != null)
			sendItemToContainer(player, 23, firstItem, 1000);
		else
			player.getPacketSender().setHidden(INTERFACE_ID, 22, false);
		if (secondItem != null)
			sendItemToContainer(player, 37, secondItem, 1001);
		else
			player.getPacketSender().setHidden(INTERFACE_ID, 36, false);
		if (thirdItem != null) {
			sendItemToContainer(player, 30, thirdItem, 1002);
			if (firstItem != null && secondItem != null && thirdItem != null) {
				if (firstItem.getId() == winningItem.getId() && secondItem.getId() == winningItem.getId()
					&& thirdItem.getId() == winningItem.getId()) {
					player.getPacketSender().sendString(INTERFACE_ID, 41, "You have won: " + firstItem.getDef().name + "!");
				} else {
					player.getPacketSender().sendString(INTERFACE_ID, 41, "<col=aa1b10><shad=000000>You haven't won this time, better luck next time!");
				}
			}
		} else
			player.getPacketSender().setHidden(INTERFACE_ID, 29, false);

	}

	public boolean isRunning() {
		return running;
	}

	private void sendItemToContainer(Player player, int componentID, Item item, int containerID) {
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			INTERFACE_ID << 16 | componentID, containerID,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			componentID,
			containerID,
			new Item(item.getId(), item.getAmount())
		);

	}

	private void scratchCard(Player player, int componentId) {
		if (componentId == 22) {
			firstItem = rollItem(player);
			if (winningItem == null)
				winningItem = firstItem;
			player.getPacketSender().setHidden(INTERFACE_ID, 22, true);
			sendItemToContainer(player, 23, firstItem, 1000);
		} else if (componentId == 36) {
			if (winningItem != null) {
				if (Random.get(1, matchingItemChance) == 1)
					secondItem = winningItem;
				else
					secondItem = rollItem(player);
			} else {
				secondItem = rollItem(player);
				winningItem = secondItem;
			}
			player.getPacketSender().setHidden(INTERFACE_ID, 36, true);
			sendItemToContainer(player, 37, secondItem, 1001);
		}
		if (componentId == 29) {
			if (winningItem != null) {
				if (Random.get(1, matchingItemChance) == 1)
					thirdItem = winningItem;
				else
					thirdItem = rollItem(player);
			} else {
				thirdItem = rollItem(player);
				winningItem = thirdItem;
			}
			player.getPacketSender().setHidden(INTERFACE_ID, 29, true);
			sendItemToContainer(player, 30, thirdItem, 1002);
		}

		if (firstItem != null && secondItem != null && thirdItem != null) {
			if (firstItem.getId() == winningItem.getId() && secondItem.getId() == winningItem.getId()
				&& thirdItem.getId() == winningItem.getId()) {
				player.getPacketSender().sendString(INTERFACE_ID, 41, "You have won: " + firstItem.getDef().name + "!");
				player.getInventory().addOrDrop(winningItem.getId(), winningItem.getAmount());
			} else {
				player.getPacketSender().sendString(INTERFACE_ID, 41, "<col=aa1b10><shad=000000>You haven't won this time, better luck next time!");
			}

			running = false;
			player.unlock();
		}
	}

	public void open(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, INTERFACE_ID);
		player.getPacketSender().sendString(INTERFACE_ID, 41, "");
		running = true;
		player.lock();
	}

	public static void register() {
		InterfaceHandler.register(INTERFACE_ID, h -> {
			h.actions[22] = (SimpleAction) p -> p.getScratchCard().scratchCard(p, 22);
			h.actions[29] = (SimpleAction) p -> p.getScratchCard().scratchCard(p, 29);
			h.actions[36] = (SimpleAction) p -> p.getScratchCard().scratchCard(p, 36);
		});

	}
}
