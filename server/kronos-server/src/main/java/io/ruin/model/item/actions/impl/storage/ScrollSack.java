package io.ruin.model.item.actions.impl.storage;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.map.ground.GroundItem;

import java.util.List;

public class ScrollSack extends ItemContainer {

	public static final int CLOSED_BAG = 33035;

	public static void register() {
		InterfaceHandler.register(Interface.MATERIAL_BAG, h -> {
			h.actions[2] = (SimpleAction) p -> {
				p.closeInterface(ToplevelComponent.INVENTORY_TAB_AREA);
				if (p.familiarStorage.deposit) {
					p.familiarStorage.deposit = false;
				}
			};
			h.actions[5] = (DefaultAction) (player, option, slot, itemId) -> {
				if (player.familiarStorage.deposit) {
					FamiliarStorage bag = player.familiarStorage;
					Item item = player.getInventory().get(slot, itemId);
					if (item == null)
						return;
					if (option == 1)
						bag.deposit(item, 1);
					else if (option == 2)
						bag.deposit(item, 5);
					else if (option == 3)
						bag.deposit(item, Integer.MAX_VALUE);
					else if (option == 4)
						player.integerInput("Enter amount:", amt -> bag.deposit(item, amt));
					else
						player.dialogue(
							new OptionsDialogue(
								"Remove how many?",
								new Option("One", () -> removeFromBag(player, bag, item, 1)),
								new Option("Five", () -> removeFromBag(player, bag, item, 5)),
								new Option("X", () -> player.integerInput("How many would you like to remove?", amt -> removeFromBag(player, bag, item, amt))),
								new Option("All", () -> removeFromBag(player, bag, item, Integer.MAX_VALUE))
							)
						);
				} else {
					// Removal logic here
					ScrollSack bag = player.getScrollSack();
					Item item = bag.get(slot, itemId);
					if (item == null)
						return;
					player.dialogue(
						new OptionsDialogue(
							"Remove how many?",
							new Option("One", () -> removeFromBag(player, bag, item, 1)),
							new Option("Five", () -> removeFromBag(player, bag, item, 5)),
							new Option("X", () -> player.integerInput("How many would you like to remove?", amt -> removeFromBag(player, bag, item, amt))),
							new Option("All", () -> removeFromBag(player, bag, item, Integer.MAX_VALUE))
						)
					);
				}
			};
		});

		ItemItemAction.register(CLOSED_BAG, (player, _, itemUsed) -> {
			player.dialogue(new OptionsDialogue(
				"How many would you like to deposit?",
				new Option("One", () -> player.getScrollSack().deposit(itemUsed, 1)),
				new Option("Five", () -> player.getScrollSack().deposit(itemUsed, 5)),
				new Option("X", () -> player.integerInput("How many would you like to deposit?", amt -> player.getScrollSack().deposit(itemUsed, amt))),
				new Option("All", () -> player.getScrollSack().deposit(itemUsed, Integer.MAX_VALUE))
			));
		});

		ItemAction.registerInventory(CLOSED_BAG, 1, (player, _) -> {
			// Fill the sack with items
			for (var scroll : player.getInventory().getItems()) {
				if (getAcceptableItemIds().contains(scroll.getId())) {
					player.getScrollSack().deposit(scroll, scroll.getAmount());
				}
			}
		});
		ItemAction.registerInventory(CLOSED_BAG, "check", (player, _) -> player.getScrollSack().openCheck());
		ItemAction.registerInventory(CLOSED_BAG, "deposit", (player, _) -> player.getScrollSack().openDeposit());
		ItemAction.registerInventory(CLOSED_BAG, "destroy", ScrollSack::destroy);
	}

	public static void destroy(Player player, Item item) {
		player.dialogue(
			new YesNoDialogue("Are you sure you want to do this?",
				"This Scroll Sack will be destroyed and all stored items will be dropped.",
				item, () -> {
					item.remove();
					player.sendMessage("You destroy your Scroll Sack" +
						"!");
					for (Item i : player.getScrollSack().getItems()) {
						if (i != null) {
							new GroundItem(i)
								.owner(player)
								.droppedBy(player)
								.position(player.getPosition())
								.spawnPrivate();
							i.remove();
						}
					}
				}
			)
		);
	}

	private static void removeFromBag(Player player, ItemContainer container, Item item, int amount) {
		int availableInventorySpaces = player.getInventory().getFreeSlots();
		if (!item.copyOfAttributes().isEmpty()) {
			item.clearAttributes();
		}

		if (isStackableOrNoted(item)) {
			// For stackable or noted items, withdraw as many as possible
			int maxWithdrawal = Math.min(amount, container.getAmount(item.getId()));
			withdrawItem(player, container, item, maxWithdrawal);
		} else {
			// For non-stackable and non-noted items, withdraw a specific amount based on free slots
			int maxWithdrawal = Math.min(amount, container.getAmount(item.getId()));
			withdrawItem(player, container, item, maxWithdrawal);
		}
	}

	private static void withdrawItem(Player player, ItemContainer container, Item item, int amount) {
		// Calculate how many items can be withdrawn
		int maxWithdrawal = Math.min(amount, container.getAmount(item.getId()));
		var backpack = player.getInventory();
		// Check if the item is stackable or noted
		if (isStackableOrNoted(item)) {
			// if we have this item in our inventory, check how many we have in the backpack
			if (backpack.contains(item.getId())) {
				// How much do we have in the backpack
				var amountInBackpack = backpack.getAmount(item.getId());
				var addedAmount = ((long) amountInBackpack + maxWithdrawal);
				if (addedAmount > Integer.MAX_VALUE) {
					// adding too much, cap it
					int capacityLeft = Integer.MAX_VALUE - amountInBackpack;
					int toTransfer = Math.min(maxWithdrawal, capacityLeft);

					container.remove(item.getId(), toTransfer);
					backpack.add(item.getId(), toTransfer);
				}
				else {
					container.remove(item.getId(), maxWithdrawal);
					backpack.add(item.getId(), maxWithdrawal);
				}
			}
			// else we're trying to add a new item to our inventory
			else if (backpack.getFreeSlots() > 0) {
				container.remove(item.getId(), maxWithdrawal);
				backpack.add(item.getId(), maxWithdrawal);
			}
			// If no inventory space, notify the player and do not withdraw
			else if (backpack.getFreeSlots() == 0) {
				player.sendMessage("Not enough space in your inventory to withdraw any " + item.getDef().name + ".");
			}
		}
		// handling non stackable items
		else {
			// For non-stackable and non-noted items, withdraw a specific amount based on free slots
			int withdrawnAmount = Math.min(maxWithdrawal, player.getInventory().getFreeSlots());
			player.getInventory().add(item.getId(), withdrawnAmount);
			container.remove(item.getId(), withdrawnAmount);

			// Check if there are remaining items that couldn't be withdrawn
			if (withdrawnAmount < maxWithdrawal) {
				player.sendMessage("Not enough space in your inventory to withdraw all " + item.getDef().name + ".");
			}
		}
	}

	private static boolean isStackableOrNoted(Item item) {
		// Replace the condition with your logic to determine if an item is stackable or noted
		return item.getDef().isNote() || item.getDef().stackable;
	}

	private transient boolean deposit;

	{
		sendAll = true;
	}

	public boolean canDeposit(int id) {
		return id != CLOSED_BAG;
	}

	public int getAmount(int itemId) {
		int count = 0;
		for (Item item : getItems()) {
			if (item != null && item.getId() == itemId) {
				count += item.getAmount();
			}
		}
		return count;
	}

	private void openCheck() {
		deposit = false;
		player.openInterface(ToplevelComponent.INVENTORY_TAB_AREA, Interface.MATERIAL_BAG);
		player.getPacketSender().sendIfEvents(Interface.MATERIAL_BAG, 5, 0, 27, 1024);
		player.getPacketSender().sendClientScript(10630, "s1", "Scroll Sack", 0);
		sendAll = true;
	}

	private void openDeposit() {
		deposit = true;
		player.openInterface(ToplevelComponent.INVENTORY_TAB_AREA, Interface.MATERIAL_BAG);
		player.getPacketSender().sendIfEvents(Interface.MATERIAL_BAG, 5, 0, 27, 542);
		player.getPacketSender().sendClientScript(10630, "s1", "Add to sack", 1);
		player.getPacketSender().setHidden(Interface.MATERIAL_BAG, 7, true);
	}

	private void deposit(Item item, int amount) {
		if (!item.copyOfAttributes().isEmpty()) {
			player.sendMessage("You can't deposit charged items.");
			return;
		}
		if (item.getId() == CLOSED_BAG) {
			player.sendMessage("You may be surprised to learn that bagception is not permitted.");
			return;
		}
		if (!getAcceptableItemIds().contains(item.getId())) {
			player.sendMessage("You can't deposit that item in your sack.");
			return;
		}
		if (item.move(item.getId(), amount, this) == 0)
			player.sendMessage("Not enough space in your sack.");
	}


	private static List<Integer> getAcceptableItemIds() {
		return List.of(
			// Scrolls
			ItemID.DOUBLE_EXP_SCROLL,
			ItemID.DOUBLE_DROP_SCROLL,
			ItemID.SLAYER_TASK_PICK_SCROLL,
			ItemID.SLAYER_SKIP_SCROLL,
			ItemID.DAMAGE_BOOST_SCROLL,
			ItemID.DAMAGE_REDUCTION_SCROLL,
			ItemID.BREW_IMMUNITY_SCROLL,
			ItemID.DROP_RATE_SCROLL,
			30463, // Scroll of Revival
			607, // Reroll Dailies Scroll
			30453 // Prayer drain reduction scroll
		);
	}
}
