package io.ruin.model.activities.newshop.shops;

import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.ItemID;
import io.ruin.model.activities.newcomertasks.NewcomerTasks;
import io.ruin.model.activities.newshop.NewShop;
import io.ruin.model.activities.newshop.ShopCategories;
import io.ruin.model.activities.newshop.ShopItem;
import io.ruin.model.activities.newshop.ShopUpgradeItems;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.services.Loggers;

import java.util.Arrays;
import java.util.List;

public class ReasonPointStore extends NewShop {
	@Override
	public List<ShopItem> getShopItemList() {
		return Arrays.asList(
			/*
			 * Consumables
			 */
			new ShopItem(new Item(ItemID.SUPER_COMBAT_POTION4), 100, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.SUPER_ATTACK4), 50, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.SUPER_STRENGTH4), 50, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.SUPER_DEFENCE4), 50, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.RANGING_POTION4), 100, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.MAGIC_POTION4), 100, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.SARADOMIN_BREW4), 100, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.SANFEW_SERUM4), 150, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.SUPER_RESTORE4), 125, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.PRAYER_POTION4), 125, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.STAMINA_POTION4), 125, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.ANTIFIRE_POTION4), 75, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.SUPER_ANTIFIRE_POTION4), 125, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.ANTIDOTE4_5952), 100, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.ANTIVENOM4_12913), 175, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.SHARK), 100, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.MONKFISH), 75, ShopCategories.CONSUMABLES, null, false),
			new ShopItem(new Item(ItemID.COOKED_KARAMBWAN), 75, ShopCategories.CONSUMABLES, null, false),
			/*
			 * Equipment
			 */

			new ShopItem(new Item(ItemID.VOID_RANGER_HELM), 35000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.VOID_MAGE_HELM), 35000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.VOID_MELEE_HELM), 35000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.VOID_KNIGHT_GLOVES), 45000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.VOID_KNIGHT_TOP), 60000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.VOID_KNIGHT_ROBE), 60000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.ELITE_VOID_TOP), 120000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.ELITE_VOID_ROBE), 120000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.FIGHTER_HAT), 35000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.RANGER_HAT), 35000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.HEALER_HAT), 35000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.FIGHTER_TORSO), 75000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.SALVE_AMULET_E), 75000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.SALVE_AMULETEI), 200000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.RUNE_DEFENDER), 30000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.DRAGON_DEFENDER), 100000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.AVAS_ASSEMBLER), 150000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.INFINITY_HAT), 35000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.INFINITY_TOP), 60000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.INFINITY_BOTTOMS), 60000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.INFINITY_BOOTS), 40000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.INFINITY_GLOVES), 30000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.TORAGS_ARMOUR_SET), 200000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.KARILS_ARMOUR_SET), 200000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.VERACS_ARMOUR_SET), 250000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.AHRIMS_ARMOUR_SET), 250000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.DHAROKS_ARMOUR_SET), 250000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.TOME_OF_FIRE_EMPTY), 250000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.BURNT_PAGE), 15000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.TOME_OF_WATER_EMPTY), 250000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(25578), 15000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.MAGES_BOOK), 70000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.MASTER_WAND), 120000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.IMBUED_GUTHIX_CAPE), 250000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.IMBUED_ZAMORAK_CAPE), 250000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.IMBUED_SARADOMIN_CAPE), 250000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.FIRE_CAPE), 100000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.INFERNAL_CAPE), 300000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.BARROWS_GLOVES), 50000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.ARCLIGHT), 100000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.IBANS_STAFF), 35000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.BOOK_OF_LAW), 25000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.BOOK_OF_DARKNESS), 25000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.UNHOLY_BOOK), 25000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.BOOK_OF_WAR), 25000, ShopCategories.EQUIPMENT, null, true),

			/*
			 * Skilling
			 */

			new ShopItem(new Item(ItemID.HESPORI_SEED), 10000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.MAGIC_SECATEURS), 20000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.CRYSTAL_SAW), 20000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.COOKING_GAUNTLETS), 20000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.GOLDSMITH_GAUNTLETS), 20000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.ICE_GLOVES), 20000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(26850), 25000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(26852), 30000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(26854), 30000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(26856), 25000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.ANGLER_HAT), 25000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.ANGLER_TOP), 30000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.ANGLER_WADERS), 30000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.ANGLER_BOOTS), 25000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.OILY_FISHING_ROD), 5000, ShopCategories.SKILLING, null, true),

			/*
			 * Utility
			 */
			new ShopItem(new Item(ItemID.DWARF_CANNON_SET), 250000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.CANNONBALL), 25, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.GRANITE_DUST), 50, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.AMMO_MOULD), 30000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(27012), 100000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.ANCIENT_SHARD), 5000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.DARK_TOTEM), 250000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.LOCATOR_ORB), 100000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.COAL_BAG_12019), 50000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.GEM_BAG_12020), 50000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.PERK_TASK_SKIP_SCROLL), 10000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.SLAYER_SKIP_SCROLL), 10000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.SLAYER_TASK_PICK_SCROLL), 125000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.DOUBLE_DROP_SCROLL), 125000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.DAMAGE_BOOST_SCROLL), 125000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(30453), 125000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.DROP_RATE_SCROLL), 250000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(22092), 500000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(30430), 1000000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.LOOTING_BAG), 100000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(19564), 50000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(26706), 100000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.SMALL_POUCH), 10000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.MEDIUM_POUCH), 17500, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.LARGE_POUCH), 25000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.GIANT_POUCH), 50000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.COLOSSAL_POUCH), 75000, ShopCategories.UTILITY, null, true)

		);
	}

	public static double getDonatorCostCut(Player player) {
		return switch (player.getSecondaryGroup()) {
			case DONATOR, SUPER_DONATOR -> 0.98;
			case ELITE_DONATOR -> 0.95;
			case NOBLE_DONATOR -> 0.94;
			case GOLD_DONATOR -> 0.93;
			case PLATINUM_DONATOR -> 0.92;
			case LEGENDARY_DONATOR -> 0.91;
			case SUPREME_DONATOR -> 0.9;
			default -> 1;
		};
	}

	@Override
	public boolean buy(Player player, ShopItem item, int amount) {
		if (!item.isIronman()) {
			if (player.getGameMode().isIronMan()) {
				player.sendMessage("Ironmen can't buy this item.");
				return false;
			}
		}
		if (item.getItem().getId() == ItemID.ARCLIGHT) {
			item.getItem().putAttribute(AttributeTypes.CHARGES, 1000);
		}
		if (amount > 1) {
			if (item.getItem().noteable()) {
				for (int i = 0; i < amount; i++) {
					if (!player.getInventory().hasRoomFor(item.getItem().getDef().notedId, 1)) {
						player.sendMessage("Not enough inventory space.");
						return false;
					} else if (player.getReasonPoints() < item.getCost()) {
						player.sendMessage("You don't have enough reason points.");
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
									boolean sent = false;
									if (player.totalReasonPointsSpent > 2500)
										sent = true;
									player.totalReasonPointsSpent += item.getCost();
									if (!sent && player.totalReasonPointsSpent > 2500)
										player.sendMessage("<col=000080>You have completed the newcomer task: <col=800000>"
											+ NewcomerTasks.SPEND_REASON_POINTS.getFormattedName() + "!");
									int cost = (int) (item.getCost() * getDonatorCostCut(player));
									player.updateReasonPoints(-cost);
									return true;
								}
							}
						}
						boolean sent = false;
						if (player.totalReasonPointsSpent > 2500)
							sent = true;
						player.totalReasonPointsSpent += item.getCost();
						if (!sent && player.totalReasonPointsSpent > 2500)
							player.sendMessage("<col=000080>You have completed the newcomer task: <col=800000>"
								+ NewcomerTasks.SPEND_REASON_POINTS.getFormattedName() + "!");
						int cost = (int) (item.getCost() * getDonatorCostCut(player));
						player.updateReasonPoints(-cost);
						player.addToCollectionLog(new Item(item.getItem().getId(), 1));
						player.getInventory().add(item.getItem().getDef().notedId, 1);
					}
				}
			} else {
				for (int i = 0; i < amount; i++) {
					if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
						player.sendMessage("Not enough inventory space.");
						return false;
					} else if (player.getReasonPoints() < item.getCost()) {
						player.sendMessage("You don't have enough reason points.");
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
									player.updateReasonPoints(-cost);
									return true;
								}
							}
						}
						player.addToCollectionLog(new Item(item.getItem().getId(), 1));
						int cost = (int) (item.getCost() * getDonatorCostCut(player));
						player.updateReasonPoints(-cost);
						player.getInventory().addOrDrop(item.getItem().getId(), 1);
					}
				}
			}
		} else {
			if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
				player.sendMessage("Not enough inventory space.");
				return false;
			} else if (player.getReasonPoints() < item.getCost()) {
				player.sendMessage("You don't have enough reason points.");
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
							player.totalReasonPointsSpent += item.getCost();
							int cost = (int) (item.getCost() * getDonatorCostCut(player));
							player.updateReasonPoints(-cost);

							return true;
						}
					}
				}
				int cost = (int) (item.getCost() * getDonatorCostCut(player));
				player.updateReasonPoints(-cost);
				player.totalReasonPointsSpent += item.getCost();
				player.getInventory().addOrDrop(item.getItem());
				player.addToCollectionLog(item.getItem());
			}
		}
		return true;
	}


	@Override
	public List<ShopCategories> getCategories() {
		return Arrays.asList(
			ShopCategories.CONSUMABLES,
			ShopCategories.EQUIPMENT,
			ShopCategories.SKILLING,
			ShopCategories.UTILITY);
	}

	@Override
	public String getShopName() {
		return "Reason Point Store";
	}

	@Override
	public String getPointIdentifier() {
		return "reason points";
	}

	@Override
	public void openMessage(Player player) {
		player.sendMessage("You have " + NumberUtils.formatNumber(player.getReasonPoints()) + " reason points.");
	}
}
