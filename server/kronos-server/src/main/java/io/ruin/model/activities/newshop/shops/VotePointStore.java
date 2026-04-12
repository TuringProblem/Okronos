package io.ruin.model.activities.newshop.shops;

import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.ItemID;
import io.ruin.model.activities.newshop.NewShop;
import io.ruin.model.activities.newshop.ShopCategories;
import io.ruin.model.activities.newshop.ShopItem;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.services.Loggers;

import java.util.Arrays;
import java.util.List;

public class VotePointStore extends NewShop {
	@Override
	public List<ShopItem> getShopItemList() {
		return Arrays.asList(
                /*
                Cosmetics
                 */
			new ShopItem(new Item(ItemID.FLIPPERS), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.MUDSKIPPER_HAT), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.ZAMORAK_HALO), 7, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.SARADOMIN_HALO), 7, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.GUTHIX_HALO), 7, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(26451), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(26454), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(26457), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(26460), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(25129), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(25131), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(25133), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(25135), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(25137), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27035), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27141), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27143), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27145), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27147), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27149), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27151), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27153), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27153), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_ARMOUR_11898), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_ARMOUR_11896), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_ARMOUR_11897), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_ARMOUR_11899), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_ARMOUR_11900), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_HELM), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_ARMOUR), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_ARMOUR_4070), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_ARMOUR_11893), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_SWORD), 5, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_HELM_4506), 10, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_ARMOUR_4504), 10, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_ARMOUR_4505), 10, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_ARMOUR_11894), 10, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_SWORD_4503), 10, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_HELM_4511), 15, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_ARMOUR_4509), 15, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_ARMOUR_4510), 15, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_ARMOUR_11895), 15, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.DECORATIVE_SWORD_4508), 15, ShopCategories.COSMETICS, null, true),

                /*
                Misc
                 */
			new ShopItem(new Item(30046), 100, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(30501), 100, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(30500), 100, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(20068), 25, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(20071), 25, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(20074), 25, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(20077), 25, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(22236), 25, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(22239), 25, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(26707), 25, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(26709), 25, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(26711), 25, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(26713), 25, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(26717), 25, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(27098), 25, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(27113), 25, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(27121), 25, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(ItemID.FURY_ORNAMENT_KIT), 10, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(ItemID.TORTURE_ORNAMENT_KIT), 20, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(ItemID.ANGUISH_ORNAMENT_KIT), 20, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(ItemID.TORMENTED_ORNAMENT_KIT), 20, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(ItemID.OCCULT_ORNAMENT_KIT), 20, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(ItemID.DRAGON_BOOTS_ORNAMENT_KIT), 5, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(ItemID.DRAGON_SCIMITAR_ORNAMENT_KIT), 5, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(ItemID.DRAGON_DEFENDER_ORNAMENT_KIT), 5, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(ItemID.RUNE_DEFENDER_ORNAMENT_KIT), 3, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(12536), 3, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(12534), 3, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(12532), 3, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(12538), 5, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(24670), 40, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(25742), 30, ShopCategories.ORNAMENTS, null, true),
			new ShopItem(new Item(25744), 30, ShopCategories.ORNAMENTS, null, true),

                /*
                Misc
                 */
			new ShopItem(new Item(620), 1, ShopCategories.MISC, null, true),
			new ShopItem(new Item(30461), 30, ShopCategories.MISC, null, true),
			new ShopItem(new Item(11738), 1, ShopCategories.MISC, null, true),
			new ShopItem(new Item(19473), 1, ShopCategories.MISC, null, true),
			new ShopItem(new Item(7478), 1, ShopCategories.MISC, null, true),
			new ShopItem(new Item(ItemID.ECUMENICAL_KEY), 1, ShopCategories.MISC, null, true),
			new ShopItem(new Item(30460), 1, ShopCategories.MISC, null, true),
			new ShopItem(new Item(30458), 1, ShopCategories.MISC, null, true),
			new ShopItem(new Item(7968), 1, ShopCategories.MISC, null, true),
			new ShopItem(new Item(607), 2, ShopCategories.MISC, null, true),
			new ShopItem(new Item(30459), 5, ShopCategories.MISC, null, true),
			new ShopItem(new Item(608), 4, ShopCategories.MISC, null, true)
		);
	}

	@Override
	public boolean buy(Player player, ShopItem item, int amount) {
		if (!item.isIronman()) {
			if (player.getGameMode().isIronMan()) {
				player.sendMessage("Ironmen can't buy this item.");
				return false;
			}
		}
		if (amount > 1) {
			if (item.getItem().noteable()) {
				for (int i = 0; i < amount; i++) {
					if (!player.getInventory().hasRoomFor(item.getItem().getDef().notedId, 1)) {
						player.sendMessage("Not enough inventory space.");
						return false;
					} else if (player.getVotePoints() < item.getCost()) {
						player.sendMessage("You don't have enough vote points.");
						return false;
					} else {
						player.addToCollectionLog(item.getItem());
						player.updateVotePoints(-item.getCost());
						player.getInventory().addOrDrop(item.getItem().getDef().notedId, 1);
					}
				}
			} else {
				for (int i = 0; i < amount; i++) {
					if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
						player.sendMessage("Not enough inventory space.");
						return false;
					} else if (player.getVotePoints() < item.getCost()) {
						player.sendMessage("You don't have enough vote points.");
						return false;
					} else {
						player.addToCollectionLog(item.getItem());

						player.updateVotePoints(-item.getCost());
						player.getInventory().addOrDrop(item.getItem());
					}
				}
			}
		} else {
			if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
				player.sendMessage("Not enough inventory space.");
				return false;
			} else if (player.getVotePoints() < item.getCost()) {
				player.sendMessage("You don't have enough vote points.");
				return false;
			} else {
				player.addToCollectionLog(item.getItem());
				player.updateVotePoints(-item.getCost());
				player.getInventory().addOrDrop(item.getItem());
			}
		}
		return true;
	}

	@Override
	public List<ShopCategories> getCategories() {
		return Arrays.asList(
			ShopCategories.MISC,
			ShopCategories.COSMETICS,
			ShopCategories.ORNAMENTS
		);
	}

	@Override
	public String getShopName() {
		return "Vote Point Store";
	}

	@Override
	public String getPointIdentifier() {
		return "vote points";
	}

	@Override
	public void openMessage(Player player) {
		player.sendMessage("You have " + NumberUtils.formatNumber(player.getVotePoints()) + " vote points.");
	}
}
