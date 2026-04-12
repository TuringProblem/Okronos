package io.ruin.model.activities.newshop.shops;

import discord.webhooks.logs.AdministrationHook;
import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.ItemID;
import io.ruin.model.activities.newshop.NewShop;
import io.ruin.model.activities.newshop.ShopCategories;
import io.ruin.model.activities.newshop.ShopItem;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class AdminStore extends NewShop {
	@Override
	public List<ShopItem> getShopItemList() {
		return Arrays.asList(
                /*
                Bonds
                 */
			new ShopItem(new Item(ItemID.FIVE_BOND), 5, ShopCategories.BONDS, null, true),
			new ShopItem(new Item(ItemID.TEN_BOND), 10, ShopCategories.BONDS, null, true),
			new ShopItem(new Item(ItemID.TWENTY_FIVE_BOND), 25, ShopCategories.BONDS, null, true),
			new ShopItem(new Item(ItemID.FIFTY_BOND), 50, ShopCategories.BONDS, null, true),
			new ShopItem(new Item(ItemID.HUNDRED_BOND), 100, ShopCategories.BONDS, null, true),
                /*
                Boxes
                 */
			new ShopItem(new Item(ItemID.DWARF_CANNON_SET), 0, ShopCategories.BOXES, null, true),
			new ShopItem(new Item(ItemID.TOB_REFUND_CHEST), 0, ShopCategories.BOXES, "A Theatre of Blood Refund Box", true),
			new ShopItem(new Item(ItemID.DONATOR_MYSTERY_BOX), 0, ShopCategories.BOXES, null, true),
			new ShopItem(new Item(ItemID.DONATOR_WEAPON_BOX), 0, ShopCategories.BOXES, null, true),
			new ShopItem(new Item(ItemID.DONATOR_ARMOUR_BOX), 0, ShopCategories.BOXES, null, true),
			new ShopItem(new Item(ItemID.SUMMER_MYSTERY_BOX), 0, ShopCategories.BOXES, null, true),
			new ShopItem(new Item(ItemID.PRESENT_13656), 0, ShopCategories.BOXES, null, true),
                /*
                Materials
                 */
			new ShopItem(new Item(ItemID.DULL_MINERALS), 0, ShopCategories.MATERIALS, "The lowest mineral class", true),
			new ShopItem(new Item(ItemID.SHINY_MINERALS), 0, ShopCategories.MATERIALS, "The second mineral class", true),
			new ShopItem(new Item(ItemID.GLISTENING_MINERALS), 0, ShopCategories.MATERIALS, "The highest mineral class", true),
			new ShopItem(new Item(ItemID.OLD_ENHANCER), 0, ShopCategories.MATERIALS, "The lowest enhancer class", true),
			new ShopItem(new Item(ItemID.MODERN_ENHANCER), 0, ShopCategories.MATERIALS, "The second enhancer class", true),
			new ShopItem(new Item(ItemID.INNOVATIVE_ENHANCER), 0, ShopCategories.MATERIALS, "The highest enhancer class", true),
			new ShopItem(new Item(ItemID.CRYSTAL_SHARD), 0, ShopCategories.MATERIALS, null, true),
                 /*
                Etc
                 */
			new ShopItem(new Item(ItemID.PERK_TASK_SKIP_SCROLL), 0, ShopCategories.SCROLLS, null, true),
			new ShopItem(new Item(ItemID.PERK_POINT_SCROLL), 0, ShopCategories.SCROLLS, null, true),
			new ShopItem(new Item(ItemID.SLAYER_SKIP_SCROLL), 0, ShopCategories.SCROLLS, null, true),
			new ShopItem(new Item(ItemID.SLAYER_TASK_PICK_SCROLL), 0, ShopCategories.SCROLLS, null, true),
			new ShopItem(new Item(ItemID.VOTE_POINT_TICKET), 0, ShopCategories.SCROLLS, null, true));


	}

	@Override
	public boolean buy(Player player, ShopItem item, int amount) {
		if (!item.isIronman()) {
			if (player.getGameMode().isIronMan()) {
				player.sendMessage("Ironmen can't buy this item.");
				return false;
			}
		}
		int amountBought = 0;
		if (amount > 1) {
			if (item.getItem().noteable()) {
				for (int i = 0; i < amount; i++) {
					if (!player.getInventory().hasRoomFor(item.getItem().getDef().notedId, 1)) {
						player.sendMessage("Not enough inventory space.");
//						RareDropEmbedMessage.sendAdminShopLogsToDiscord(player, "Bought " + NumberUtils.formatNumber(amountBought) + " " + item.getItem().getDef().name + " from the Admin shop for " + item.getCost() * amountBought + " Admin tokens.", true);
						return false;
					} else if (player.getInventory().getAmount(20527) < item.getCost()) {
						player.sendMessage("You don't have enough Admin tokens to complete your transaction.");
//						RareDropEmbedMessage.sendAdminShopLogsToDiscord(player, "Bought " + NumberUtils.formatNumber(amountBought) + " " + item.getItem().getDef().name + " from the Admin shop for " + item.getCost() * amountBought + " Admin tokens.", true);
						return false;
					} else {
						player.getInventory().remove(20527, item.getCost());
						player.getInventory().addOrDrop(item.getItem().getDef().notedId, 1);
						amountBought += amount;
					}
				}
			} else {
				for (int i = 0; i < amount; i++) {
					if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
						player.sendMessage("Not enough inventory space.");
//						RareDropEmbedMessage.sendAdminShopLogsToDiscord(player, "Bought " + NumberUtils.formatNumber(amountBought) + " " + item.getItem().getDef().name + " from the Admin shop for " + item.getCost() * amountBought + " Admin tokens.", true);
						return false;
					} else if (player.getInventory().getAmount(20527) < item.getCost()) {
						player.sendMessage("You don't have enough Admin tokens to complete your transaction.");
//						RareDropEmbedMessage.sendAdminShopLogsToDiscord(player, "Bought " + NumberUtils.formatNumber(amountBought) + " " + item.getItem().getDef().name + " from the Admin shop for " + item.getCost() * amountBought + " Admin tokens.", true);
						return false;
					} else {
						player.getInventory().remove(20527, item.getCost());
						player.getInventory().addOrDrop(item.getItem());
						amountBought++;
					}
				}
			}
		} else {
			if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
				player.sendMessage("Not enough inventory space.");
//				RareDropEmbedMessage.sendAdminShopLogsToDiscord(player, "Bought " + NumberUtils.formatNumber(amountBought) + " " + item.getItem().getDef().name + " from the Admin shop for " + item.getCost() * amountBought + " Admin tokens.", true);
				return false;
			} else if (player.getInventory().getAmount(20527) < item.getCost()) {
				player.sendMessage("You don't have enough Admin tokens to complete your transaction.");
//				RareDropEmbedMessage.sendAdminShopLogsToDiscord(player, "Bought " + NumberUtils.formatNumber(amountBought) + " " + item.getItem().getDef().name + " from the Admin shop for " + item.getCost() * amountBought + " Admin tokens.", true);
				return false;
			} else {
				player.getInventory().remove(20527, item.getCost());
				player.getInventory().addOrDrop(item.getItem());
				amountBought++;
			}
		}
//		RareDropEmbedMessage.sendAdminShopLogsToDiscord(player, "Bought " + NumberUtils.formatNumber(amountBought) + " " + item.getItem().getDef().name + " from the Admin shop for " + item.getCost() * amountBought + " Admin tokens.", true);

		var jsonObject = new JSONObject();
			jsonObject.put("player", player.getName());
			jsonObject.put("exchange", "bought");
			jsonObject.put("cost", NumberUtils.formatNumber((long) item.getCost() * amountBought));
			jsonObject.put("item_id", item.getItem().getId());
			jsonObject.put("item_name", item.getItem().getDef().name);
			jsonObject.put("item_amount", NumberUtils.formatNumber(amountBought));
		AdministrationHook.sendAdminShopLogsToDiscord(jsonObject);
		return true;
	}

	@Override
	public List<ShopCategories> getCategories() {
		return Arrays.asList(
			ShopCategories.BONDS,
			ShopCategories.BOXES,
			ShopCategories.MATERIALS,
			ShopCategories.SCROLLS
		);
	}

	@Override
	public String getShopName() {
		return "Admin Shop";
	}

	@Override
	public String getPointIdentifier() {
		return "Admin Tokens";
	}

	@Override
	public void openMessage(Player player) {

	}
}