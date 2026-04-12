package io.ruin.model.activities.newshop;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.services.Loggers;

import java.util.ArrayList;
import java.util.List;

public abstract class NewShop {

	public abstract List<ShopItem> getShopItemList();


	public abstract boolean buy(Player player, ShopItem item, int amount);


	public abstract List<ShopCategories> getCategories();

	public abstract String getShopName();

	public abstract String getPointIdentifier();

	public abstract void openMessage(Player player);

	public void handleUpgradeItem(Player player, ShopUpgradeItems upgrade) {
		player.getInventory().remove(upgrade.getToUpggradeId(), 1);
		player.getInventory().add(upgrade.getUpgradeId());
		player.addToCollectionLog(new Item(upgrade.getUpgradeId(), 1));
	}


}
