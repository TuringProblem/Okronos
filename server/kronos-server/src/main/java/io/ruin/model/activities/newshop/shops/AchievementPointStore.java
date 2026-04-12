package io.ruin.model.activities.newshop.shops;

import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.ItemID;
import io.ruin.model.activities.newshop.NewShop;
import io.ruin.model.activities.newshop.ShopCategories;
import io.ruin.model.activities.newshop.ShopItem;
import io.ruin.model.activities.newshop.ShopUpgradeItems;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.services.Loggers;

import java.util.Arrays;
import java.util.List;

public class AchievementPointStore extends NewShop {

	public static double getDonatorCostCut(Player player) {
		switch (player.getSecondaryGroup()) {
			case DONATOR -> {
				return 0.99;
			}
			case SUPER_DONATOR -> {
				return 0.97;
			}
			case ELITE_DONATOR -> {
				return 0.95;
			}
			case NOBLE_DONATOR -> {
				return 0.94;
			}
			case GOLD_DONATOR -> {
				return 0.93;
			}
			case PLATINUM_DONATOR -> {
				return 0.92;
			}
			case LEGENDARY_DONATOR -> {
				return 0.91;
			}
			case SUPREME_DONATOR -> {
				return 0.9;
			}
		}
		return 1;
	}

	@Override
	public List<ShopItem> getShopItemList() {
		return Arrays.asList(
			/*
			 * Achievement Items
			 */
			new ShopItem(new Item(ItemID.KANDARIN_HEADGEAR_1), 3, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.VARROCK_ARMOUR_1), 3, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.MORYTANIA_LEGS_1), 3, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.FALADOR_SHIELD_1), 3, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.KARAMJA_GLOVES_1), 3, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.FREMENNIK_SEA_BOOTS_1), 3, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.EXPLORERS_RING_1), 3, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.RADAS_BLESSING_1), 3, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.DESERT_AMULET_1), 3, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.ARDOUGNE_CLOAK_1), 3, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.WILDERNESS_SWORD_1), 3, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.WESTERN_BANNER_1), 3, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.KANDARIN_HEADGEAR_2), 5, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.VARROCK_ARMOUR_2), 5, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.MORYTANIA_LEGS_2), 5, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.FALADOR_SHIELD_2), 5, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.KARAMJA_GLOVES_2), 5, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.FREMENNIK_SEA_BOOTS_2), 5, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.EXPLORERS_RING_2), 5, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.RADAS_BLESSING_2), 5, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.DESERT_AMULET_2), 5, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.ARDOUGNE_CLOAK_2), 5, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.WILDERNESS_SWORD_2), 5, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.WESTERN_BANNER_2), 5, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.KANDARIN_HEADGEAR_3), 7, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.VARROCK_ARMOUR_3), 7, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.MORYTANIA_LEGS_3), 7, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.FALADOR_SHIELD_3), 7, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.KARAMJA_GLOVES_3), 7, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.FREMENNIK_SEA_BOOTS_3), 7, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.EXPLORERS_RING_3), 7, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.RADAS_BLESSING_3), 7, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.DESERT_AMULET_3), 7, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.ARDOUGNE_CLOAK_3), 7, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.WILDERNESS_SWORD_3), 7, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.WESTERN_BANNER_3), 7, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.KANDARIN_HEADGEAR_4), 8, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.VARROCK_ARMOUR_4), 8, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.MORYTANIA_LEGS_4), 8, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.FALADOR_SHIELD_4), 8, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.KARAMJA_GLOVES_4), 8, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.FREMENNIK_SEA_BOOTS_4), 8, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.EXPLORERS_RING_4), 8, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.RADAS_BLESSING_4), 8, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.DESERT_AMULET_4), 8, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.ARDOUGNE_CLOAK_4), 8, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.WILDERNESS_SWORD_4), 8, ShopCategories.ACHIEVEMENT_ITEMS, null, true),
			new ShopItem(new Item(ItemID.WESTERN_BANNER_4), 8, ShopCategories.ACHIEVEMENT_ITEMS, null, true),

			/*
			 * Equipment
			 */
			new ShopItem(new Item(ItemID.CRYSTAL_HALBERD), 12, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.ANCIENT_MACE), 8, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(25644), 7, ShopCategories.EQUIPMENT, "A wielded hammer", true),
			new ShopItem(new Item(ItemID.MITHRIL_GLOVES), 3, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.ADAMANT_GLOVES), 6, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.RUNE_GLOVES), 9, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.DRAGON_GLOVES), 12, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.RUNE_POUCH), 13, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.HERB_SACK), 9, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.SMALL_POUCH), 3, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.MEDIUM_POUCH), 5, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.LARGE_POUCH), 7, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.GIANT_POUCH), 9, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.COLOSSAL_POUCH), 12, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.BONECRUSHER), 18, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.HOLY_WRENCH), 6, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.SEED_BOX), 13, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.SOUL_BEARER), 15, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.SHAYZIEN_HELM_5), 3, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.SHAYZIEN_PLATEBODY_5), 3, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.SHAYZIEN_GREAVES_5), 3, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.SHAYZIEN_BOOTS_5), 3, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.SHAYZIEN_GLOVES_5), 3, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.DAMAGED_BOOK), 0, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.DAMAGED_BOOK_3841), 0, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.DAMAGED_BOOK_3843), 0, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.DAMAGED_BOOK_12607), 0, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.DAMAGED_BOOK_12609), 0, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.DAMAGED_BOOK_12611), 0, ShopCategories.EQUIPMENT, null, true)

		);
	}

	@Override
	public boolean buy(Player player, ShopItem item, int amount) {
		if (!item.isIronman()) {
			if (player.getGameMode().isIronMan()) {
				player.sendMessage("Ironman accounts do not have access to this item.");
				return false;
			}
		}

		if (amount > 1) {
			if (item.getItem().noteable()) {
				for (int i = 0; i < amount; i++) {
					if (!player.getInventory().hasRoomFor(item.getItem().getDef().notedId, 1)) {
						player.sendMessage("Not enough inventory space.");
						return false;
					} else if (player.getAchievementPoints() < item.getCost()) {
						player.sendMessage("You don't have enough achievement points.");
						return false;
					} else {
						for (ShopUpgradeItems shopUpgrade : ShopUpgradeItems.VALUES) {
							if (item.getItem().getId() == shopUpgrade.getUpgradeId()) {
								if (!player.getInventory().contains(shopUpgrade.getToUpggradeId())) {
									Item itemToUpgrade = new Item(shopUpgrade.getToUpggradeId());
									player.sendMessage(
										"You need a " + itemToUpgrade.getDef().name + " in your inventory to purchase this.");
									return false;
								} else {
									handleUpgradeItem(player, shopUpgrade);
									int cost = (int) (item.getCost() * getDonatorCostCut(player));
									player.updateAchievementPoints(-cost);
									return true;
								}
							}
						}
						player.addToCollectionLog(item.getItem());

						int cost = (int) (item.getCost() * getDonatorCostCut(player));
						player.updateAchievementPoints(-cost);
						player.getInventory().addOrDrop(item.getItem().getDef().notedId, 1);
					}
				}
			} else {
				for (int i = 0; i < amount; i++) {
					if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
						player.sendMessage("Not enough inventory space.");
						return false;
					} else if (player.getAchievementPoints() < item.getCost()) {
						player.sendMessage("You don't have enough achievement points.");
						return false;
					} else {
						for (ShopUpgradeItems shopUpgrade : ShopUpgradeItems.VALUES) {
							if (item.getItem().getId() == shopUpgrade.getUpgradeId()) {
								if (!player.getInventory().contains(shopUpgrade.getToUpggradeId())) {
									Item itemToUpgrade = new Item(shopUpgrade.getToUpggradeId());
									player.sendMessage(
										"You need a " + itemToUpgrade.getDef().name + " in your inventory to purchase this.");
									return false;
								} else {
									handleUpgradeItem(player, shopUpgrade);
									int cost = (int) (item.getCost() * getDonatorCostCut(player));
									player.updateAchievementPoints(-cost);
									return true;
								}
							}
						}
						player.addToCollectionLog(item.getItem());

						int cost = (int) (item.getCost() * getDonatorCostCut(player));
						player.updateAchievementPoints(-cost);
						player.getInventory().addOrDrop(item.getItem());
					}
				}
			}
		} else {
			if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
				player.sendMessage("Not enough inventory space.");
				return false;
			} else if (player.getAchievementPoints() < item.getCost()) {
				player.sendMessage("You don't have enough achievement points.");
				return false;
			} else {
				for (ShopUpgradeItems shopUpgrade : ShopUpgradeItems.VALUES) {
					if (item.getItem().getId() == shopUpgrade.getUpgradeId()) {
						if (!player.getInventory().contains(shopUpgrade.getToUpggradeId())) {
							Item itemToUpgrade = new Item(shopUpgrade.getToUpggradeId());
							player.sendMessage("You need a " + itemToUpgrade.getDef().name + " in your inventory to purchase this.");
							return false;
						} else {
							handleUpgradeItem(player, shopUpgrade);
							int cost = (int) (item.getCost() * getDonatorCostCut(player));
							player.updateAchievementPoints(-cost);
							return true;
						}
					}
				}
				player.addToCollectionLog(item.getItem());

				int cost = (int) (item.getCost() * getDonatorCostCut(player));
				player.updateAchievementPoints(-cost);
				player.getInventory().addOrDrop(item.getItem());
			}
		}
		return true;
	}

	@Override
	public List<ShopCategories> getCategories() {
		return Arrays.asList(
			ShopCategories.ACHIEVEMENT_ITEMS,
			ShopCategories.EQUIPMENT);
	}

	@Override
	public String getShopName() {
		return "Achievement Point Store";
	}

	@Override
	public String getPointIdentifier() {
		return "achievement points";
	}

	@Override
	public void openMessage(Player player) {
		player.sendMessage("You have " + NumberUtils.formatNumber(player.getAchievementPoints()) + " achievement points.");
	}
}
