package io.ruin.model.content.itembreaking;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.handlers.TabInventory;
import io.ruin.model.item.Item;

public class ItemBreakInterface {
	private Item currentItemSelected;

	public void open(Player player) {
		player.inGamble = false;
		currentItemSelected = null;
		quickBreak = false;
		player.openInterface(ToplevelComponent.MAINMODAL, 1127);
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1127 << 16 | 54, 238,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				54,
				238,
				new Item(ItemBreakingHandler.DULL_MINERAL));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1127 << 16 | 67, 239,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				67,
				239,
				new Item(ItemBreakingHandler.SHINY_MINERAL));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1127 << 16 | 78, 240,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				78,
				240,
				new Item(ItemBreakingHandler.GLISTENING_MINERAL));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1127 << 16 | 89, 241,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				89,
				241,
				new Item(ItemBreakingHandler.OLD_ENHANCER));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1127 << 16 | 100, 242,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				100,
				242,
				new Item(ItemBreakingHandler.MODERN_ENHANCER));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1127 << 16 | 111, 243,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				111,
				243,
				new Item(ItemBreakingHandler.INNOVATIVE_ENHANCER));
		player.openInterface(ToplevelComponent.SIDEMODAL, 879);
		player.getPacketSender().sendClientScript(149, "IviiiIsssss", 879 << 16, 93, 4, 7, 0, -1,
				"Select", "", "", "", "");
		player.getPacketSender().sendIfEvents(879, 0, 0, 27, 1086);
		player.getPacketSender().sendIfEvents(879, 0, 0, 27, 1086);
		update(player);
	}

	private void update(Player player) {
		if (currentItemSelected == null) {
			player.getPacketSender().setHidden(1127, 38, true);
			player.getPacketSender().setHidden(1127, 39, true);
			player.getPacketSender().sendString(1127, 112, "Click the item in your inventory you wish to break down.");
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					1127 << 16 | 34, 244,
					4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(
					-1,
					34,
					244,
					new Item(-1));

		} else {
			player.getPacketSender().setHidden(1127, 38, false);
			player.getPacketSender().setHidden(1127, 39, false);
			player.getPacketSender().sendString(1127, 118,
					"This process breaks down your item into minerals and enhancers, this completely destroys the item and it cannot be undone.");
			BreakItems breakItems = BreakItems.forId(currentItemSelected.getId());
			player.getPacketSender().sendString(1127, 38, "Tier: " + breakItems.tier);
			String dullMineralAmount = ItemBreakingHandler.getDullMineralMinAmount(player, breakItems.tier) + "-"
					+ ItemBreakingHandler.getDullMineralMaxAmount(player, breakItems.tier);
			player.getPacketSender().sendString(1127, 60, dullMineralAmount);
			String shinyMineralAmount = ItemBreakingHandler.getShinyMineralMinAmount(player, breakItems.tier) + "-"
					+ ItemBreakingHandler.getShinyMineralMaxAmount(player, breakItems.tier);
			player.getPacketSender().sendString(1127, 73, shinyMineralAmount);
			String glisteningMineralAmount = ItemBreakingHandler.getGlisteningMineralMinAmount(player, breakItems.tier) + "-"
					+ ItemBreakingHandler.getGlisteningMineralMaxAmount(player, breakItems.tier);
			player.getPacketSender().sendString(1127, 84, glisteningMineralAmount);
			String oldEnhancerAmount = ItemBreakingHandler.getOldEnhancerMinAmount(player, breakItems.tier) + "-"
					+ ItemBreakingHandler.getOldEnhancerMaxAmount(player, breakItems.tier);
			player.getPacketSender().sendString(1127, 95, oldEnhancerAmount);
			String modernEnhancerAmount = ItemBreakingHandler.getModernEnhancerMinAmount(player, breakItems.tier) + "-"
					+ ItemBreakingHandler.getModernEnhancerMaxAmount(player, breakItems.tier);
			player.getPacketSender().sendString(1127, 106, modernEnhancerAmount);
			String innovativeEnhancerAmount = ItemBreakingHandler.getInnovativeEnhancerMinAmount(player, breakItems.tier)
					+ "-" + ItemBreakingHandler.getInnovativeEnhancerMaxAmount(player, breakItems.tier);
			player.getPacketSender().sendString(1127, 117, innovativeEnhancerAmount);
			int shinyMineralChance = ItemBreakingHandler.getShinyMineralChance(breakItems.tier);
			String shinyMineralChanceColour;
			if (shinyMineralChance >= 70)
				shinyMineralChanceColour = "<col=1fff5a>";
			else if (shinyMineralChance >= 40)
				shinyMineralChanceColour = "<col=ffd919>";
			else
				shinyMineralChanceColour = "<col=990000>";
			player.getPacketSender().sendString(1127, 72, shinyMineralChanceColour + shinyMineralChance + "%");
			int modernEnhancerChance = ItemBreakingHandler.getModernEnhancerChance(breakItems.tier);
			String modernEnhancerChanceColour;
			if (modernEnhancerChance >= 70)
				modernEnhancerChanceColour = "<col=1fff5a>";
			else if (modernEnhancerChance >= 40)
				modernEnhancerChanceColour = "<col=ffd919>";
			else
				modernEnhancerChanceColour = "<col=990000>";
			player.getPacketSender().sendString(1127, 105, modernEnhancerChanceColour + modernEnhancerChance + "%");
			int glisteningMineralChance = ItemBreakingHandler.getGlisteningMineralChance(breakItems.tier);
			String glisteningMineralChanceColour;
			if (glisteningMineralChance >= 70)
				glisteningMineralChanceColour = "<col=1fff5a>";
			else if (glisteningMineralChance >= 40)
				glisteningMineralChanceColour = "<col=ffd919>";
			else
				glisteningMineralChanceColour = "<col=990000>";
			player.getPacketSender().sendString(1127, 83, glisteningMineralChanceColour + glisteningMineralChance + "%");
			int innovativeEnhancerChance = ItemBreakingHandler.getInnovativeEnhancerChance(breakItems.tier);
			String innovativeEnhancerChanceColour;
			if (innovativeEnhancerChance >= 70)
				innovativeEnhancerChanceColour = "<col=1fff5a>";
			else if (innovativeEnhancerChance >= 40)
				innovativeEnhancerChanceColour = "<col=ffd919>";
			else
				innovativeEnhancerChanceColour = "<col=990000>";
			player.getPacketSender().sendString(1127, 116, innovativeEnhancerChanceColour + innovativeEnhancerChance + "%");

			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					1127 << 16 | 34, 244,
					4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(
					-1,
					34,
					244,
					currentItemSelected);
		}
	}

	boolean quickBreak = false;


	private void toggleQuickBreak(Player player) {
		quickBreak = !quickBreak;
		if (quickBreak) {
			player.sendMessage("You will now automatically break down items when selected.");
			player.getPacketSender().setHidden(1127, 122, false);
		} else {
			player.sendMessage("You will now need to manually break down items.");
			player.getPacketSender().setHidden(1127, 122, true);
		}
	}

	private void selectItem(Player player, Item item) {
		if (item == null)
			return;
		BreakItems breakItems = BreakItems.forId(item.getId());
		if (breakItems == null) {
			player.sendMessage("You cannot break down this item.");
			return;
		}
		currentItemSelected = item;
		update(player);
		if (quickBreak) {
			breakItem(player);
		}
	}

	private boolean itemHasPerks(Item item) {
		return ItemUpgradeInterface.getTotalAttachments(item) > 0;
	}

	private void breakItem(Player player) {
		if (currentItemSelected == null) {
			player.sendMessage("You must select an item to break down first.");
			return;
		}

		var breakItem = BreakItems.forId(currentItemSelected.getId());
		if (breakItem == null) {
			player.sendMessage("You cannot break down this item.");
			return;
		}


		if (breakItem.tier >= 7) {
			player.dialogue(new YesNoDialogue(
					"Are you sure you want to break down this item?",
					"This item is a high tier item..",
					currentItemSelected, () -> {
						ItemBreakingHandler.handleItemBreaking(player, currentItemSelected);
						currentItemSelected = null;
						open(player);
						update(player);
					}));
			return;
		}

		if (itemHasPerks(currentItemSelected)) {
			player.dialogue(new YesNoDialogue(
					"Are you sure you want to break down this item?",
					"This item has been perked..",
					currentItemSelected, () -> {
						ItemBreakingHandler.handleItemBreaking(player, currentItemSelected);
						currentItemSelected = null;
						open(player);
						update(player);
					}));
			return;
		}

		ItemBreakingHandler.handleItemBreaking(player, currentItemSelected);
		currentItemSelected = null;
		update(player);
	}

	public static void register() {
		InterfaceHandler.register(879, (interfaceHandler -> {
			interfaceHandler.actions[0] = new InterfaceAction() {

				@Override
				public void handleClick(Player player, int option, int slot, int itemId) {
					if (!player.inGamble) {
						if (option == 1) {
							player.getItemBreakInterface().selectItem(player, player.getInventory().get(slot));
						} else if (option == 10) {
							player.sendMessage("" + new Item(itemId).getDef().examine);
						}
					} else {
						Item item = player.getInventory().get(slot, itemId);
						if (item == null)
							return;
						if (option == 1)
							player.getGamble().offer(item, 1);
						else if (option == 2)
							player.getGamble().offer(item, 5);
						else if (option == 3)
							player.getGamble().offer(item, 10);
						else if (option == 4)
							player.getGamble().offer(item, Integer.MAX_VALUE);
						else if (option == 5)
							player.integerInput("Enter amount:", amt -> player.getGamble().offer(item, amt));
						else
							item.examine(player);
					}
				}

				@Override
				public void handleDrag(Player player, int fromSlot, int fromItemId, int toInterfaceId, int toChildId,
						int toSlot, int toItemId) {
					TabInventory.drag(player, fromSlot, toSlot);
				}
			};
		}));
		InterfaceHandler.register(1127, (interfaceHandler -> {
			interfaceHandler.actions[28] = (SimpleAction) player -> player.getItemBreakInterface().breakItem(player);
			interfaceHandler.actions[121] = (SimpleAction) player -> player.getItemBreakInterface().toggleQuickBreak(player);
			interfaceHandler.actions[122] = (SimpleAction) player -> player.getItemBreakInterface().toggleQuickBreak(player);

		}));
	}
}
