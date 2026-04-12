package io.ruin.model.activities.newshop;

import io.ruin.api.utils.NumberUtils;
import io.ruin.model.ScrollbarClientScript;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.handlers.LootsTables;
import io.ruin.model.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NewShopInterface {

	ShopCategories currentCategory;
	NewShop currentShop;
	HashMap<ShopCategories, List<ShopItem>> shopItems = new HashMap<>();
	ShopItem selectedItem;

	public void openShop(Player player, NewShop shop) {
		shopItems.clear();
		ScrollbarClientScript.create()
			.interfaceId(1105)
			.containerId(46)
			.scrollbarChildId(302)
			.childrenCount(120)
			.withDarkGraphics()
			.build()
			.send(player);
		if (currentShop != null && currentShop != shop) {
			currentCategory = null;
		}
		currentShop = shop;
		player.openInterface(ToplevelComponent.MAINMODAL, 1105);
		List<ShopCategories> shopCategoriesList = shop.getCategories();
		if (currentCategory == null)
			currentCategory = shopCategoriesList.get(0);
		sendItemValue(player, null);
		player.getPacketSender().sendString(1105, 4, shop.getShopName());
		List<ShopItem> shopItemList = shop.getShopItemList();
		shopItemList.forEach(item -> {
			ShopCategories category = item.getCategory();
			shopItems.putIfAbsent(category, new ArrayList<>());
			shopItems.get(category).add(item);
		});
		AtomicInteger startingCategoryComponent = new AtomicInteger(6);
		AtomicInteger startingCategoryNameComponent = new AtomicInteger(9);
		AtomicInteger startingCategorySpriteComponent = new AtomicInteger(10);
		AtomicInteger startingItemContainerId = new AtomicInteger(4000);

		AtomicInteger startingItemComponent = new AtomicInteger(47);
		AtomicInteger startingItemContainerComponent = new AtomicInteger(50);
		AtomicInteger startingItemPriceStringComponent = new AtomicInteger(51);
		for (int i = 6; i < 37; i += 5)
			player.getPacketSender().setHidden(1105, i, true);
		for (int i = 47; i < 298; i += 5)
			player.getPacketSender().setHidden(1105, i, true);
		shopCategoriesList.forEach(cat -> {
			player.getPacketSender().setHidden(1105, startingCategoryComponent.get(), false);
			player.getPacketSender().sendString(1105, startingCategoryNameComponent.get(), cat.getName());
			int interfaceHash = 1105 << 16 | startingCategorySpriteComponent.get();
			player.getPacketSender().sendClientScript(cat.getScriptId(), interfaceHash);
			startingCategoryComponent.addAndGet(5);
			startingCategoryNameComponent.addAndGet(5);
			startingCategorySpriteComponent.addAndGet(5);
		});
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1105 << 16 | 313, 5000,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			313,
			5000,
			new Item(-1)
		);
		for (ShopItem shopItem :
			shopItemList) {
			if (shopItem.getCategory() != currentCategory) continue;

			player.getPacketSender().setHidden(1105, startingItemComponent.get(), false);
			player.getPacketSender().sendString(1105, startingItemPriceStringComponent.get(), NumberUtils.formatNumber(shopItem.cost));

			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1105 << 16 | startingItemContainerComponent.get(), startingItemContainerId.get(),
				4, 7, 1, -1, "Info", "Buy 1", "Buy 5", "Buy 10", "Buy X"
			);
			player.getPacketSender().sendIfEvents(1105, startingItemContainerComponent.get(), 0, 27, 1086);
			player.getPacketSender().sendItems(
				-1,
				startingItemContainerComponent.get(),
				startingItemContainerId.get(),
				shopItem.item
			);


			startingItemComponent.addAndGet(5);
			startingItemPriceStringComponent.addAndGet(5);
			startingItemContainerComponent.addAndGet(5);
			startingItemContainerId.addAndGet(1);
		}

	}

	private int getItemIndex(int componentId) {
		return (componentId - 50) / 5;
	}

	private void sendItemValue(Player player, ShopItem item) {
		selectedItem = item;
		if (item == null) {
			player.getPacketSender().sendString(1105, 307, "");
			player.getPacketSender().sendString(1105, 308, "None selected");
			player.getPacketSender().sendString(1105, 314, "Cost: N/A");
		} else {
			if (item.getCustomDescription() == null) {
				player.getPacketSender().sendString(1105, 307,
					"The store will sell this " + item.item.getDef().name + " for:<br>" +
						" " + item.getCost() + " " + currentShop.getPointIdentifier() + ".");
			} else {
				player.getPacketSender().sendString(1105, 307,
					"The store will sell this " + item.item.getDef().name + " for:<br>" +
						" " + item.getCost() + " " + currentShop.getPointIdentifier() + ".<br>" + item.getCustomDescription());
			}
			player.getPacketSender().sendString(1105, 308, item.item.getDef().name);
			player.getPacketSender().sendString(1105, 314, "Cost: <col=21ff00>" + NumberUtils.formatNumber(item.cost));
			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1105 << 16 | 313, 5000,
				4, 7, 1, -1, "Info", "Buy 1", "Buy 5", "Buy 10", "Buy X"
			);
			player.getPacketSender().sendItems(
				-1,
				313,
				5000,
				item.item
			);
		}
	}

	private void sendCategoryColumnUpdate(Player player, int index) {
		for (int i = 8; i < 39; i += 5)
			player.getPacketSender().setHidden(1105, i, false);
		for (int i = 7; i < 40; i += 5)
			player.getPacketSender().setHidden(1105, i, true);
		player.getPacketSender().setHidden(1105, 8 + (index * 5), true);
		player.getPacketSender().setHidden(1105, 7 + (index * 5), false);
	}

	public static void register() {
		InterfaceHandler.register(1105, h -> {
			for (int i = 50; i < 301; i += 5) {
				int finalI = i;
				h.actions[i] = (DefaultAction) (player, option, slot, itemId) -> {
					switch (option) {
						case 1:
							player.getNewShopInterface().sendItemValue(player, player.getNewShopInterface().shopItems.get(player.getNewShopInterface().currentCategory).get(player.getNewShopInterface().getItemIndex(finalI)));
							break;
						case 2:
							player.getNewShopInterface().currentShop.buy(player, player.getNewShopInterface().shopItems.get(player.getNewShopInterface().currentCategory).get(player.getNewShopInterface().getItemIndex(finalI)), 1);
							break;
						case 3:
							player.getNewShopInterface().currentShop.buy(player, player.getNewShopInterface().shopItems.get(player.getNewShopInterface().currentCategory).get(player.getNewShopInterface().getItemIndex(finalI)), 5);
							break;
						case 4:
							player.getNewShopInterface().currentShop.buy(player, player.getNewShopInterface().shopItems.get(player.getNewShopInterface().currentCategory).get(player.getNewShopInterface().getItemIndex(finalI)), 10);
							break;
						case 5:
							player.integerInput("Enter amount to buy", s -> {
								int amount = s;
								if (amount > 100000)
									amount = 100000;
								if (amount < 1) {
									return;
								}
								player.getNewShopInterface().currentShop.buy(player, player.getNewShopInterface().shopItems.get(player.getNewShopInterface().currentCategory).get(player.getNewShopInterface().getItemIndex(finalI)), amount);
							});
							break;
						case 10:
							player.sendMessage("" + new Item(itemId).getDef().examine);
							if (itemId == 30430) {
								player.getLootsViewer().updateInterface(player, LootsTables.REASON_ULTRA_POINT_BOX);
							}
							if (itemId == 30461 || itemId == 30446 || itemId == 4810) {
								player.getLootsViewer().updateInterface(player, LootsTables.REGULAR_MYSTERY_BOX);
							}
							if (itemId == 30462 || itemId == 30449 || itemId == 30448) {
								player.getLootsViewer().updateInterface(player, LootsTables.REGULAR_MYSTERY_BOX);
							}
							if (itemId == 32000) {
								player.getLootsViewer().updateInterface(player, LootsTables.HOLIDAY_MYSTERY_BOX);
							}
							if (itemId == 32002) {
								player.getLootsViewer().updateInterface(player, LootsTables.SUMMER_MYSTERY_BOX);
							}
							break;
					}
				};
				h.actions[6] = (SimpleAction) (p) -> {
					p.getNewShopInterface().currentCategory = p.getNewShopInterface().currentShop.getCategories().get(0);
					p.getNewShopInterface().openShop(p, p.getNewShopInterface().currentShop);
					p.getNewShopInterface().sendCategoryColumnUpdate(p, 0);

				};
				h.actions[11] = (SimpleAction) (p) -> {
					p.getNewShopInterface().currentCategory = p.getNewShopInterface().currentShop.getCategories().get(1);
					p.getNewShopInterface().openShop(p, p.getNewShopInterface().currentShop);
					p.getNewShopInterface().sendCategoryColumnUpdate(p, 1);
				};
				h.actions[16] = (SimpleAction) (p) -> {
					p.getNewShopInterface().currentCategory = p.getNewShopInterface().currentShop.getCategories().get(2);
					p.getNewShopInterface().openShop(p, p.getNewShopInterface().currentShop);
					p.getNewShopInterface().sendCategoryColumnUpdate(p, 2);
				};
				h.actions[21] = (SimpleAction) (p) -> {
					p.getNewShopInterface().currentCategory = p.getNewShopInterface().currentShop.getCategories().get(3);
					p.getNewShopInterface().openShop(p, p.getNewShopInterface().currentShop);
					p.getNewShopInterface().sendCategoryColumnUpdate(p, 3);
				};
				h.actions[26] = (SimpleAction) (p) -> {
					p.getNewShopInterface().currentCategory = p.getNewShopInterface().currentShop.getCategories().get(4);
					p.getNewShopInterface().openShop(p, p.getNewShopInterface().currentShop);
					p.getNewShopInterface().sendCategoryColumnUpdate(p, 4);
				};
				h.actions[31] = (SimpleAction) (p) -> {
					p.getNewShopInterface().currentCategory = p.getNewShopInterface().currentShop.getCategories().get(5);
					p.getNewShopInterface().openShop(p, p.getNewShopInterface().currentShop);
					p.getNewShopInterface().sendCategoryColumnUpdate(p, 5);
				};
				h.actions[36] = (SimpleAction) (p) -> {
					p.getNewShopInterface().currentCategory = p.getNewShopInterface().currentShop.getCategories().get(6);
					p.getNewShopInterface().openShop(p, p.getNewShopInterface().currentShop);
					p.getNewShopInterface().sendCategoryColumnUpdate(p, 6);
				};
				h.actions[315] = (SimpleAction) (p) -> {
					if (p.getNewShopInterface().selectedItem == null)
						p.sendMessage("You don't have an item selected.");
					else p.getNewShopInterface().currentShop.buy(p, p.getNewShopInterface().selectedItem, 1);
				};
			}
		});

	}
}
