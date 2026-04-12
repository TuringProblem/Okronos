package donationdeals;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonationDealsInterface {
	private static final int INTERFACE_ID = 1136;


	public static void open(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, INTERFACE_ID);
		player.dealsInterContainerId = 2300;
		player.dealItems.clear();
		player.freeContainerSlots.clear();
		sendSeasonalDealsToInter(player);
		sendDailyDealToInter(player);
		sendWeeklyDealToInter(player);
		sendDealsBar(player);
		World.startEvent(e -> {
			e.setCancelCondition(() -> player.dealsClosed);
			while (true) {
				e.delay(1);
				sendUpdates(player);
			}
		});
	}

	private static void confirmClaimDeal(Player player) {
		if(player.donationDealPoints < 50) {
			player.sendMessage("You don't have enough points to claim a deal, donate more to unlock deals.");
			return;
		}
		if(player.getInventory().getFreeSlots() < 4) {
			player.sendMessage("You need at least 4 free inventory slots to claim a deal.");
			return;
		}
		player.getPacketSender().setHidden(INTERFACE_ID, 159, true);
		for(int i = 0; i < 4; i++) {
			Item item = player.dealItems.get(i);
			player.getInventory().addOrDrop(item);
			player.sendMessage("You have claimed a " + item.getDef().name + " from the donation deal.");
		}
		player.sendMessage("Your donation deal tally has been reset to 0 after claiming the deal.");
		player.donationDealPoints = 0;
		open(player);
	}

	private static void closeConfirm(Player player) {
		player.getPacketSender().setHidden(INTERFACE_ID, 159, true);
	}

	private static void claimDeal(Player player) {
		if(player.donationDealPoints < 50) {
			player.sendMessage("You don't have any available deals to claim, donate another $" +
				DonationManager.seasonDealHandler.getAmountForNextTier(player.donationDealPoints) + " to claim a deal.");
			return;
		}
		SeasonDealHandler.DealTier tier = DonationManager.seasonDealHandler.getTierForAmount(player.donationDealPoints);
		if(tier == null) {
			return;
		}
		player.dealItems.addAll(tier.items);
		int tierOrder = tier.tierOrder;
		Item dailyItem = new Item(DonationManager.dailyDealHandler.dailyDealItem.getId(),
			DonationManager.dailyDealHandler.dailyDealItem.getAmount() * tierOrder);
		Item weeklyItem = new Item(DonationManager.weeklyDealHandler.weeklyDealItem.getId(),
			DonationManager.weeklyDealHandler.weeklyDealItem.getAmount() * tierOrder);
		player.dealItems.add(dailyItem);
		player.dealItems.add(weeklyItem);
		for(int i = 0; i < 4; i++) {
			Item item = player.dealItems.get(i);
			sendItem(player, item, 179 + (i * 4), player.dealsInterContainerId);
			player.dealsInterContainerId++;
		}
		player.getPacketSender().sendString(INTERFACE_ID, 175, getDealTitle(tierOrder));
		player.getPacketSender().setHidden(INTERFACE_ID, 159, false);

	}

	private static String getDealTitle(int tierOrder) {
		switch (tierOrder) {
			case 1:
				return "$50 Donation Deal";
			case 2:
				return "$150 Donation Deal";
			case 3:
				return "$250 Donation Deal";
			case 4:
				return "$500 Donation Deal";
			case 5:
				return "$1,000 Donation Deal";
			default:
				return "Unknown Deal";
		}
	}

	private static void sendDealsBar(Player player) {
		player.getPacketSender().sendString(INTERFACE_ID, 27, "$"+ player.donationDealPoints+"/$1,000");
		player.getPacketSender().sendString(INTERFACE_ID, 28, "Donate an additional $"
			+DonationManager.seasonDealHandler.getAmountForNextTier(player.donationDealPoints)+" for the next deal!");

		int mainBarComponentId = 25;
		int shadowBarComponentId = 26;

		int barInterfaceHash = INTERFACE_ID << 16 | mainBarComponentId;
		int barBackgroundInterfaceHash = INTERFACE_ID << 16 | shadowBarComponentId;
		float percentageCompleted = player.donationDealPoints / 1000f;
		float barWidth = percentageCompleted * 425;

		player.getPacketSender().sendClientScript(10606, "Ii", barInterfaceHash, (int) barWidth);
		player.getPacketSender().sendClientScript(10606, "Ii", barBackgroundInterfaceHash, (int) barWidth);

	}

	private static void sendItem(Player player, Item item, int componentId, int containerId) {
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1136 << 16 | componentId, containerId,
			4, 7, 1, -1, "", "", "", "", "");

		player.getPacketSender().sendItems(
			-1,
			componentId,
			containerId,
			item);
	}

	private static void sendSeasonalDealsToInter(Player player) {
		List<SeasonDealHandler.DealTier> seasonalDeals = DonationManager.seasonDealHandler.getTiers();

		for (int i = 0; i < seasonalDeals.size(); i++) {
			SeasonDealHandler.DealTier tier = seasonalDeals.get(i);
			if (tier == null || tier.items == null)
				continue;
			List<Item> items = tier.items;
			int startingComponent = 52 + (i * 19);
			for(int j = 0; j < items.size(); j++) {
				Item item = items.get(j);
				int componentId = startingComponent + (j * 4);
				sendItem(player, item, componentId, player.dealsInterContainerId);
				player.dealsInterContainerId++;
				if(j == items.size() - 1) {
					int index = j + 1;
					player.freeContainerSlots.put(i, startingComponent + (index * 4));
				}
			}
		}
	}

	private static void sendDailyDealToInter(Player player) {
		Item dailyDealItem = DonationManager.dailyDealHandler.dailyDealItem;
		String dailyDealName = DonationManager.dailyDealHandler.dailyDealTitle;
		player.getPacketSender().sendString(INTERFACE_ID, 35, "Daily Deal: " + dailyDealName);
		sendItem(player, dailyDealItem, 39, player.dealsInterContainerId);
		player.dealsInterContainerId++;
		for(int i = 1; i < 6; i++) {
			int index = i - 1;
			if(player.freeContainerSlots.containsKey(index)) {
				int value = player.freeContainerSlots.get(index);
				Item item = new Item(dailyDealItem.getId(), dailyDealItem.getAmount() * i);
				sendItem(player, item, value, player.dealsInterContainerId);
				player.dealsInterContainerId++;
				player.freeContainerSlots.replace(index, value + 4);
			}
		}
	}

	private static void sendWeeklyDealToInter(Player player) {
		Item weeklyDealItem = DonationManager.weeklyDealHandler.weeklyDealItem;
		String weeklyDealName = DonationManager.weeklyDealHandler.weeklyDealTitle;
		player.getPacketSender().sendString(INTERFACE_ID, 148, "Weekly Deal: " + weeklyDealName);
		sendItem(player, weeklyDealItem, 152, player.dealsInterContainerId);
		player.dealsInterContainerId++;
		for(int i = 1; i < 6; i++) {
			int index = i - 1;
			if(player.freeContainerSlots.containsKey(index)) {
				int value = player.freeContainerSlots.get(index);
				Item item = new Item(weeklyDealItem.getId(), weeklyDealItem.getAmount() * i);
				sendItem(player, item, value, player.dealsInterContainerId);
				player.dealsInterContainerId++;
				player.freeContainerSlots.replace(index, value + 4);
			}
		}
	}

	public static void sendUpdates(Player player) {
		player.getPacketSender().sendString(INTERFACE_ID, 41, DonationManager.dailyDealHandler.getTimeRemainingString());
		player.getPacketSender().sendString(INTERFACE_ID, 154, DonationManager.weeklyDealHandler.getTimeRemainingString());
	}
	public static void register() {
		InterfaceHandler.register(INTERFACE_ID, h -> {
			h.actions[155] = (SimpleAction) DonationDealsInterface::claimDeal;
			h.actions[193] = (SimpleAction) DonationDealsInterface::confirmClaimDeal;
			h.actions[171] = (SimpleAction) DonationDealsInterface::closeConfirm;
			h.closedAction = (p, i) -> {
				p.dealsClosed = true;
			};
		});
	}
}
