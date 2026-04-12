package io.ruin.model.activities.tempevents.summerevent;

import io.ruin.model.activities.newshop.NewShop;
import io.ruin.model.activities.newshop.ShopCategories;
import io.ruin.model.activities.newshop.ShopItem;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;

import java.util.List;

import static io.ruin.model.activities.newshop.ShopCategories.COSMETICS;
import static io.ruin.model.activities.newshop.ShopCategories.TREASURES;

public class SummerStore extends NewShop {

	@Override
	public List<ShopItem> getShopItemList() {
		return List.of(
			new ShopItem(new Item(8960, 1), 300, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(8953, 1), 400, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(8992, 1), 400, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(8961, 1), 300, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(8954, 1), 400, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(8993, 1), 400, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(8959, 1), 300, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(8952, 1), 400, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(8991, 1), 400, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(8964, 1), 300, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(8957, 1), 400, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(8996, 1), 400, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(22396, 1), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27267, 1), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27265, 1), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(22502, 1), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(24144, 1), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27291, 1), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(30891, 1), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(25981, 1), 500, ShopCategories.COSMETICS, null, true),

			new ShopItem(new Item(30461, 1), 250, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(30459, 1), 40, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(30460, 1), 40, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(608, 1), 80, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(2528, 1), 275, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(33020, 1), 300, ShopCategories.TREASURES, null, true)
		);
	}

	@Override
	public boolean buy(Player player, ShopItem item, int amount) {
		if (amount > 1) {
			if (item.getItem().noteable()) {
				for (int i = 0; i < amount; i++) {
					if (!player.getInventory().hasRoomFor(item.getItem().getDef().notedId, 1)) {
						player.sendMessage("Not enough inventory space.");
						return false;
					} else if (player.summerPoints < item.getCost()) {
						player.sendMessage("You don't have enough summer points.");
						return false;
					} else {
						player.summerPoints -= item.getCost();
						player.getInventory().addOrDrop(item.getItem().getDef().notedId, 1);
					}
				}
			} else {
				for (int i = 0; i < amount; i++) {
					if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
						player.sendMessage("Not enough inventory space.");
						return false;
					} else if (player.summerPoints < item.getCost()) {
						player.sendMessage("You don't have enough summer points.");
						return false;
					} else {
						player.summerPoints -= item.getCost();
						player.getInventory().addOrDrop(item.getItem());
					}
				}
			}
		} else {
			if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
				player.sendMessage("Not enough inventory space.");
				return false;
			} else if (player.summerPoints < item.getCost()) {
				player.sendMessage("You don't have enough summer points.");
				return false;
			} else {
				player.summerPoints -= item.getCost();
				player.getInventory().addOrDrop(item.getItem());
			}
		}
		return true;
	}

	@Override
	public List<ShopCategories> getCategories() {
		return List.of(COSMETICS, TREASURES);
	}

	@Override
	public String getShopName() {
		return "Summer Event Store";
	}

	@Override
	public String getPointIdentifier() {
		return "Summer points";
	}

	@Override
	public void openMessage(Player player) {
		player.sendMessage("You currently have %d summer points.".formatted(player.summerPoints));
	}
}
