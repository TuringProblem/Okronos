package io.ruin.model.activities.nightmarezone;

import io.ruin.api.utils.NumberUtils;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.item.Item;
import io.ruin.model.var.VarPlayerRepository;

import java.util.ArrayList;

public class NightmareZoneRewards {

	public static void imbueItem(Player player, int id) {
		if (!player.getInventory().contains(id))
			return;
		NMZUpgradable upgradable = NMZUpgradable.getUpgradable(id);
		if (upgradable == null) {
			player.sendMessage("This item currently cannot be upgraded, if this is a mistake, please contact staff.");
			return;
		}
		String name = new Item(id).getDef().name;

		if (player.nmzRewardPoints < upgradable.price) {
			player.sendMessage("You must have at least " + NumberUtils.formatNumber(upgradable.price) + " NMZ Points to upgrade your " + name);
			return;
		}

		player.nmzRewardPoints -= upgradable.price;
		player.getInventory().remove(id, 1);
		player.getInventory().add(new Item(upgradable.upgraded));
		player.sendMessage("You have upgraded your " + name + " for " + NumberUtils.formatNumber(upgradable.price) + " points.");
		refresh(player);
	}

	public static void purchaseReward(Player player, int id, int amount) {
		NMZResource resource = NMZResource.getUpgradable(id);
		if (resource.price * amount < 0)
			amount = Integer.MAX_VALUE / 1000;
		if (resource == null) {
			player.sendMessage("This item currently cannot be purchased, if this is a mistake, please contact staff.");
			return;
		}
		String name = new Item(id).getDef().name;
		if (player.nmzRewardPoints < amount * resource.price)
			amount = player.nmzRewardPoints / resource.price;
		if (amount <= 0) {
			player.sendMessage("You must have at least " + NumberUtils.formatNumber(resource.price) + " NMZ Points to purchase " + name);
			return;
		}
		if (!new Item(id).noteable() && amount > player.getInventory().getFreeSlots())
			amount = player.getInventory().getFreeSlots();
		if (player.getInventory().getFreeSlots() < 1) {
			player.sendMessage("You currently have no space in your inventory.");
			return;
		}
		player.sendMessage("" + resource.price * amount);


		player.nmzRewardPoints -= resource.price * amount;
		player.getInventory().add(new Item(id, amount).note());
		refresh(player);
	}

	public static void purchaseBenefit(Player player, int id, int amount) {
		NMZBenefits benefit = NMZBenefits.getBenefits(id);
		if (amount > 5)
			amount = player.getInventory().getFreeSlots();
		if (amount < 0)
			amount = player.getInventory().getFreeSlots();
		if (benefit == null) {
			player.sendMessage("This item currently cannot be purchased, if this is a mistake, please contact staff.");
			return;
		}
		String name = new Item(id).getDef().name;
		if (player.nmzRewardPoints < amount * benefit.price)
			amount = player.nmzRewardPoints / benefit.price;
		if (amount <= 0) {
			player.sendMessage("You must have at least " + NumberUtils.formatNumber(benefit.price) + " NMZ Points to purchase " + name);
			return;
		}
		if (new Item(id).noteable() && amount > player.getInventory().getFreeSlots())
			amount = player.getInventory().getFreeSlots();
		if (player.getInventory().getFreeSlots() < 1) {
			player.sendMessage("You currently have no space in your inventory.");
			return;
		}
		player.nmzRewardPoints -= benefit.price * amount;
		player.getInventory().add(new Item(id - 3, amount).note());
		refresh(player);
	}

	public static void open(Player player) {
		/**
		 * sends the nightmare zone rewards interface
		 */
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.NIGHTMARE_ZONE_REWARDS);
		refresh(player);
	}

	public static void refresh(Player player) {
		/**
		 * setting the nmz reward points (config refresher)
		 */
		VarPlayerRepository.NMZ_REWARD_POINTS_TOTAL.set(player, player.nmzRewardPoints);
		for (int i = 0; i < 200; i++) {
			/** 4 = Resources, 5 = Upgrades, 6 = Benefits */
			player.getPacketSender().sendIfEvents(Interface.NIGHTMARE_ZONE_REWARDS, NightmareZoneObjects.RESOURCES, 0, i, 1124);
			player.getPacketSender().sendIfEvents(Interface.NIGHTMARE_ZONE_REWARDS, NightmareZoneObjects.UPGRADES, 0, i, 2);//1124
			player.getPacketSender().sendIfEvents(Interface.NIGHTMARE_ZONE_REWARDS, NightmareZoneObjects.BENEFITS, 0, i, 1124);

			/**
			 * Send item prices
			 * let test
			 * *
			 */
			ArrayList<Item> upgradables = new ArrayList<Item>();
			for (NMZUpgradable upgradable : NMZUpgradable.VALUES) {
				upgradables.add(new Item(upgradable.id, upgradable.price));
			}
			for (NMZResource resource : NMZResource.VALUES) {
				upgradables.add(new Item(resource.id, resource.price));
			}
			for (NMZBenefits benefits : NMZBenefits.VALUES) {
				upgradables.add(new Item(benefits.id, benefits.price));
			}
			player.getPacketSender().sendItems(-1, 4, 464, upgradables.toArray(new Item[0]));

		}
	}

	public static void handleImbueItemAction(Player player, int option, int slot, int itemId) {
		imbueItem(player, itemId);
	}

	public static void handlePurchaseRewardItemAction(Player player, int option, int slot, int itemId) {
		if (option == 2)
			purchaseReward(player, itemId, 5);
		else if (option == 5)
			player.integerInput("How many would you like to purchase?", amt -> purchaseReward(player, itemId, amt));
	}

	public static void handlePurchaseBenefitItemAction(Player player, int option, int slot, int itemId) {
		if (option == 2)
			purchaseBenefit(player, itemId, 5);
		else if (option == 5)
			player.integerInput("How many would you like to purchase?", amt -> purchaseBenefit(player, itemId, amt));
	}

	public static void action(Player player, int childId, int option, int itemId, int slot) {
		if (childId == 5)
			imbueItem(player, itemId);
		else if (childId == 4) {
			if (option == 2)
				purchaseReward(player, itemId, 5);
			else if (option == 5)
				player.integerInput("How many would you like to purchase?", amt -> purchaseReward(player, itemId, amt));
		} else if (childId == 6)
			if (option == 2)
				purchaseBenefit(player, itemId, 5);
			else if (option == 5)
				player.integerInput("How many would you like to purchase?", amt -> purchaseBenefit(player, itemId, amt));

	}

	public static void register() {
		try {
			InterfaceHandler.register(Interface.NIGHTMARE_ZONE_REWARDS, h -> {
				h.actions[4] = (DefaultAction) (player, option, slot, itemId) -> handlePurchaseRewardItemAction(player, option, slot, itemId);

				h.actions[5] = (DefaultAction) (player, option, slot, itemId) -> handleImbueItemAction(player, option, slot, itemId);

				h.actions[6] = (DefaultAction) (player, option, slot, itemId) -> handlePurchaseBenefitItemAction(player, option, slot, itemId);
			});

		} catch (Throwable e) {
			System.out.println(e);
		}
	}
}

