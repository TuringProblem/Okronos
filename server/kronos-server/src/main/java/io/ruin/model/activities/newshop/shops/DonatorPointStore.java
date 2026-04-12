package io.ruin.model.activities.newshop.shops;

import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.ItemID;
import io.ruin.model.activities.newshop.NewShop;
import io.ruin.model.activities.newshop.ShopCategories;
import io.ruin.model.activities.newshop.ShopItem;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.services.Loggers;
import properties.ServerProperties;

import java.util.Arrays;
import java.util.List;

public class DonatorPointStore extends NewShop {

	@Override
	public List<ShopItem> getShopItemList() {
		return Arrays.asList(

				/*
				 * Treasures
				 */
				new ShopItem(new Item(30611), 0, ShopCategories.TREASURES, "Unlimited teleport tablet to the Donator Zone",
						true),
				new ShopItem(new Item(ItemID.SUMMER_MYSTERY_BOX), 3000, ShopCategories.TREASURES, null, true),
				new ShopItem(new Item(59524), 2300, ShopCategories.TREASURES, null, true),
				new ShopItem(new Item(59525), 1400, ShopCategories.TREASURES, null, true),
				new ShopItem(new Item(30461), 750, ShopCategories.TREASURES, null, true),
				new ShopItem(new Item(4810), 2200, ShopCategories.TREASURES, null, true),
				new ShopItem(new Item(30446), 3500, ShopCategories.TREASURES, null, true),
				new ShopItem(new Item(30462), 1050, ShopCategories.TREASURES, null, true),
				new ShopItem(new Item(30448), 3050, ShopCategories.TREASURES, null, true),
				new ShopItem(new Item(30449), 4850, ShopCategories.TREASURES, null, true),
				new ShopItem(new Item(2528), 800, ShopCategories.TREASURES, null, true),
				new ShopItem(new Item(33020), 1000, ShopCategories.TREASURES, null, true),
				new ShopItem(new Item(ItemID.BLUE_PARTYHAT), 10000, ShopCategories.RARES, null, true),
				new ShopItem(new Item(ItemID.RED_PARTYHAT), 10000, ShopCategories.RARES, null, true),
				new ShopItem(new Item(ItemID.WHITE_PARTYHAT), 10000, ShopCategories.RARES, null, true),
				new ShopItem(new Item(ItemID.PURPLE_PARTYHAT), 10000, ShopCategories.RARES, null, true),
				new ShopItem(new Item(ItemID.GREEN_PARTYHAT), 10000, ShopCategories.RARES, null, true),
				new ShopItem(new Item(ItemID.YELLOW_PARTYHAT), 10000, ShopCategories.RARES, null, true),
				new ShopItem(new Item(ItemID.RAINBOW_PARTYHAT), 10000, ShopCategories.RARES, null, true),
				new ShopItem(new Item(ItemID.BLACK_PARTYHAT), 10000, ShopCategories.RARES, null, true),
				new ShopItem(new Item(ItemID.SANTA_HAT), 7500, ShopCategories.RARES, null, true),
				new ShopItem(new Item(ItemID.BLACK_SANTA_HAT), 7500, ShopCategories.RARES, null, true),
				new ShopItem(new Item(ItemID.BLACK_HWEEN_MASK), 7500, ShopCategories.RARES, null, true),
				new ShopItem(new Item(ItemID.RED_HALLOWEEN_MASK), 7500, ShopCategories.RARES, null, true),
				new ShopItem(new Item(ItemID.BLUE_HALLOWEEN_MASK), 7500, ShopCategories.RARES, null, true),
				new ShopItem(new Item(ItemID.GREEN_HALLOWEEN_MASK), 7500, ShopCategories.RARES, null, true),
				new ShopItem(new Item(ItemID.CHRISTMAS_CRACKER), 10000, ShopCategories.RARES, null, true),

				/*
				 * Scrolls
				 */
				new ShopItem(new Item(30460), 250, ShopCategories.SCROLLS,
						"This scroll will grant you with double experience for 30 minutes.", true),
				new ShopItem(new Item(30459), 250, ShopCategories.SCROLLS,
						"When activated you will receive 2x the item rolls when killing a NPC.", true),
				new ShopItem(new Item(30458), 50, ShopCategories.SCROLLS,
						"This scroll will skip your slayer task for you.", true),
				new ShopItem(new Item(7968), 50, ShopCategories.SCROLLS,
						"This scroll will skip your perk task for you.", true),
				new ShopItem(new Item(607), 50, ShopCategories.SCROLLS,
						"This scroll will reroll your daily tasks for you.", true),
				new ShopItem(new Item(30457), 750, ShopCategories.SCROLLS,
						"This scroll will reduce incoming damage taken by 20% when fighting NPCs for 30 minutes.", true),
				new ShopItem(new Item(30456), 750, ShopCategories.SCROLLS,
						"This scroll will reduce increase your damage by 20% when fighting NPCs for 30 minutes.", true),
				new ShopItem(new Item(30455), 450, ShopCategories.SCROLLS,
						"This scroll will remove the negative effects from drinking saradomin brews for 30 minutes.", true),
				new ShopItem(new Item(30453), 350, ShopCategories.SCROLLS,
						"This scroll will reduce the speed of your prayer drain by 30% for 30 minutes.", true),
				new ShopItem(new Item(30463), 350, ShopCategories.SCROLLS,
						"This scroll will save you from death completely healing you once.<br> (Note: this doesn't protect hardcore status)",
						true),
				new ShopItem(new Item(608), 750, ShopCategories.SCROLLS,
						"This scroll will give you an additional 10% drop rate boost for one hour.", true),
				new ShopItem(new Item(22092), 3500, ShopCategories.SCROLLS,
						"This scroll will give you an additional 25% drop rate boost for obtaining pets for 24 hours.", true),
				new ShopItem(new Item(30570), 200, ShopCategories.SCROLLS,
						"This scroll will give you a random amount of perk points.", true),

				/*
				 * Equipment
				 */
				new ShopItem(new Item(11670), 1950, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.ARMADYL_HELMET), 950, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.ARMADYL_CHESTPLATE), 1250, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.ARMADYL_CHAINSKIRT), 1250, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.BANDOS_CHESTPLATE), 1250, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.BANDOS_TASSETS), 1250, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.ZAMORAKIAN_SPEAR), 950, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.STAFF_OF_THE_DEAD), 950, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.ARMADYL_CROSSBOW), 2450, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.SARADOMIN_GODSWORD), 1250, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.ARMADYL_GODSWORD), 1250, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.ZAMORAK_GODSWORD), 1250, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.BANDOS_GODSWORD), 2250, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.DHAROKS_ARMOUR_SET), 950, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.GUTHANS_ARMOUR_SET), 950, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.KARILS_ARMOUR_SET), 950, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.AHRIMS_ARMOUR_SET), 950, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.TORAGS_ARMOUR_SET), 950, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.VERACS_ARMOUR_SET), 950, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.DRAGONFIRE_SHIELD), 750, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.DRAGONFIRE_WARD), 750, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.ANCIENT_WYVERN_SHIELD), 750, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.FIRE_CAPE), 350, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.INFERNAL_CAPE), 1500, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.IMBUED_GUTHIX_CAPE), 350, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.IMBUED_ZAMORAK_CAPE), 350, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.IMBUED_SARADOMIN_CAPE), 350, ShopCategories.EQUIPMENT, null, true),
				new ShopItem(new Item(ItemID.AVAS_ASSEMBLER), 450, ShopCategories.EQUIPMENT, null, true));
	}

	@Override
	public boolean buy(Player player, ShopItem item, int amount) {
		if (!enabled(player)) {
			player.sendMessage("This shop is currently disabled.");
			return false;
		}

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
					} else if (player.getDonatorPoints() < item.getCost()) {
						player.sendMessage("You don't have enough donator points.");
						return false;
					} else {
						player.updateDonatorPoints(-item.getCost());
						player.getInventory().addOrDrop(item.getItem().getDef().notedId, 1);
					}
				}
			} else {
				for (int i = 0; i < amount; i++) {
					if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
						player.sendMessage("Not enough inventory space.");
						return false;
					} else if (player.getDonatorPoints() < item.getCost()) {
						player.sendMessage("You don't have enough donator points.");
						return false;
					} else {
						player.updateDonatorPoints(-item.getCost());
						player.getInventory().addOrDrop(item.getItem());
					}
				}
			}
		} else {
			if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
				player.sendMessage("Not enough inventory space.");
				return false;
			} else if (player.getDonatorPoints() < item.getCost()) {
				player.sendMessage("You don't have enough donator points.");
				return false;
			} else {
				player.updateDonatorPoints(-item.getCost());
				player.getInventory().addOrDrop(item.getItem());
			}
		}
		return true;
	}

	@Override
	public List<ShopCategories> getCategories() {
		return Arrays.asList(
				ShopCategories.TREASURES,
				ShopCategories.RARES,
				ShopCategories.EQUIPMENT,
				ShopCategories.SCROLLS);
	}

	@Override
	public String getShopName() {
		return "Donator Point Store";
	}

	@Override
	public String getPointIdentifier() {
		return "donator points";
	}

	@Override
	public void openMessage(Player player) {
		player.sendMessage("You have " + NumberUtils.formatNumber(player.getDonatorPoints()) + " donator points.");
	}

	public boolean enabled(Player player) {
		return Boolean.parseBoolean(ServerProperties.get("donation_shop_enabled", "false"));
	}
}
