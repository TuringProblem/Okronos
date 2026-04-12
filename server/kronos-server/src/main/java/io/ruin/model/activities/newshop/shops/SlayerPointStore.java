package io.ruin.model.activities.newshop.shops;

import io.ruin.cache.ItemID;
import io.ruin.model.activities.newshop.NewShop;
import io.ruin.model.activities.newshop.ShopCategories;
import io.ruin.model.activities.newshop.ShopItem;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.var.VarPlayerRepository;

import java.util.Arrays;
import java.util.List;

public class SlayerPointStore extends NewShop {
	@Override
	public List<ShopItem> getShopItemList() {
		return Arrays.asList(
                /*
                Default store
                 */
			new ShopItem(new Item(ItemID.ENCHANTED_GEM), 1, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.MIRROR_SHIELD), 5000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.LEAFBLADED_SPEAR), 31000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.BROAD_ARROWS_4160), 60, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.BAG_OF_SALT), 10, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.ROCK_HAMMER), 500, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.FACEMASK), 200, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.EARMUFFS), 200, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.NOSE_PEG), 200, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.SLAYERS_STAFF), 21000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.SPINY_HELMET), 650, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.FISHING_EXPLOSIVE_6664), 60, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.ICE_COOLER), 1, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.SLAYER_GLOVES_6720), 200, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.UNLIT_BUG_LANTERN), 130, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.INSULATED_BOOTS), 200, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.FUNGICIDE_SPRAY_10), 300, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(7432)/*FUNGICIDE*/, 10, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.WITCHWOOD_ICON), 900, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.SLAYER_BELL), 150, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.BROAD_ARROWHEADS), 55, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.BROAD_ARROWHEAD_PACK), 120000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.UNFINISHED_BROAD_BOLTS), 55, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.UNFINISHED_BROAD_BOLT_PACK), 120000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.ELEMENTAL_SHIELD), 20, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.BOOTS_OF_STONE), 200, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.REINFORCED_GOGGLES), 200, ShopCategories.UTILITY, null, true),
                /*
                New store
                 */
			new ShopItem(new Item(25426), 100, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(25424), 150, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(25432), 250, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(ItemID.GRANITE_CLAMP), 350, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(26479), 350, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(24729), 350, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(28017), 950, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(26421), 1250, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(ItemID.CRYSTAL_KEY), 75, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(ItemID.BRIMSTONE_KEY), 60, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(30452), 650, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(30629), 350, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(ItemID.DAMAGE_FOR_HIRE_LOW_SIGIL), 2000, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(ItemID.ARMOUR_BREAK_SIGIL), 2500, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(ItemID.HEALTH_SIPHON_SIGIL), 3000, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(ItemID.CRITICAL_HIT_SIGIL), 3000, ShopCategories.SLAYER, null, true),
			new ShopItem(new Item(ItemID.AOE_SWIPE_SIGIL), 3000, ShopCategories.SLAYER, null, true));

	}

	@Override
	public boolean buy(Player player, ShopItem item, int amount) {
		boolean isSlayerShop = item.getCategory() == ShopCategories.SLAYER;
		int cost = item.getCost();

		if (isSlayerShop) {
			int currentPoints = VarPlayerRepository.SLAYER_POINTS.get(player);

			int maxAffordableAmount = currentPoints / cost;

			if (amount > maxAffordableAmount) {
				amount = maxAffordableAmount;
				if (amount == 0) {
					player.sendMessage("You don't have enough Slayer Points to buy this item.");
					return false;
				}
				player.sendMessage("You can only buy " + amount + " of this item with your current Slayer Points.");
			}

			for (int i = 0; i < amount; i++) {
				if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
					player.sendMessage("Not enough inventory space.");
					return false;
				}
				VarPlayerRepository.SLAYER_POINTS.set(player, VarPlayerRepository.SLAYER_POINTS.get(player) - cost);
				player.getInventory().addOrDrop(item.getItem());
			}
		} else {
			if (!item.isIronman() && player.getGameMode().isIronMan()) {
				player.sendMessage("Ironmen can't buy this item.");
				return false;
			}

			if (!handleCoinPurchase(player, item, amount)) {
				return false;
			}
		}
		return true;
	}

	private boolean handleCoinPurchase(Player player, ShopItem item, int amount) {
		for (int i = 0; i < amount; i++) {
			if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
				player.sendMessage("Not enough inventory space.");
				return false;
			} else if (player.getInventory().getAmount(995) < item.getCost()) {
				player.sendMessage("You don't have enough coins.");
				return false;
			} else {
				player.getInventory().remove(995, item.getCost());
				player.getInventory().addOrDrop(item.getItem());
			}
		}
		return true;
	}

	@Override
	public List<ShopCategories> getCategories() {
		return Arrays.asList(
			ShopCategories.UTILITY,
			ShopCategories.SLAYER
		);
	}

	@Override
	public String getShopName() {
		return "Slayer Shop";
	}

	@Override
	public String getPointIdentifier() {
		return "";
	}

	@Override
	public void openMessage(Player player) {

	}
}
