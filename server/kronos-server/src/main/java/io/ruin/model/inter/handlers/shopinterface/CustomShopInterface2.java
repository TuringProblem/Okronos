package io.ruin.model.inter.handlers.shopinterface;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.StringUtils;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.item.Item;
import io.ruin.model.shop.Currency;
import io.ruin.model.inter.handlers.shopinterface.CustomShop2;

import java.util.Arrays;

public class CustomShopInterface2 {
	public static void register() {
		InterfaceHandler.register(Interface.CUSTOM_SHOP2, h -> {
			h.actions[16] = (DefaultAction) (player, option, slot, itemId) -> {
////                if (player.getGameMode().isIronMan()) {
////              //      player.closeInterface(InterfaceType.MAIN);
////                    player.sendMessage("You can't access this shop.");
//                    return;
//                } else {
				player.getPacketSender().sendString(891, 14, "Melee Shop");
				player.setShopIdentifier(1);
				handleEnteringShop(player, CustomShop2.MELEE_STORE);
				open(player, CustomShop2.getItemsFromShop(player));
				// }
			};
			h.actions[18] = (DefaultAction) (player, option, slot, itemId) -> {
//                if (player.getGameMode().isIronMan()) {
//                //    player.closeInterface(InterfaceType.MAIN);
//                    player.sendMessage("You can't access this shop.");
//                    return;
//                } else {
				player.getPacketSender().sendString(891, 14, "Ranged Shop");
				player.setShopIdentifier(2);
				handleEnteringShop(player, CustomShop2.RANGED_STORE);
				open(player, CustomShop2.getItemsFromShop(player));
				// }
			};
			h.actions[20] = (DefaultAction) (player, option, slot, itemId) -> {
//                if (player.getGameMode().isIronMan()) {
//                //    player.closeInterface(InterfaceType.MAIN);
//                    player.sendMessage("You can't access this shop.");
//                    return;
//                } else {
				player.getPacketSender().sendString(891, 14, "Magic Shop");
				player.setShopIdentifier(3);
				handleEnteringShop(player, CustomShop2.MAGIC_STORE);
				open(player, CustomShop2.getItemsFromShop(player));
//                }
			};
			h.actions[22] = (DefaultAction) (player, option, slot, itemId) -> {
//                if (player.getGameMode().isIronMan()) {
//                //    player.closeInterface(InterfaceType.MAIN);
//                    player.sendMessage("You can't access this shop.");
//                    return;
//                } else {
				player.getPacketSender().sendString(891, 14, "Ironman Shop");
				player.setShopIdentifier(4);
				handleEnteringShop(player, CustomShop2.IRONMAN_SHOP);
				open(player, CustomShop2.getItemsFromShop(player));
//                }
			};
			h.actions[24] = (DefaultAction) (player, option, slot, itemId) -> {
//                if (player.getGameMode().isIronMan()) {
//                //    player.closeInterface(InterfaceType.MAIN);
//                    player.sendMessage("You can't access this shop.");
//                    return;
//                } else {
				player.getPacketSender().sendString(891, 14, "PVM Point Shop");
				player.setShopIdentifier(5);
				handleEnteringShop(player, CustomShop2.PVM_POINT_SHOP);
				open(player, CustomShop2.getItemsFromShop(player));
//                }
			};
			h.actions[26] = (DefaultAction) (player, option, slot, itemId) -> {
//                if (player.getGameMode().isIronMan()) {
//                //    player.closeInterface(InterfaceType.MAIN);
//                    player.sendMessage("You can't access this shop.");
//                    return;
//                } else {
				player.getPacketSender().sendString(891, 14, "Blood Money Shop");
				player.setShopIdentifier(6);
				handleEnteringShop(player, CustomShop2.BLOOD_MONEY_STORE);
				open(player, CustomShop2.getItemsFromShop(player));
//                }
			};
			h.actions[28] = (DefaultAction) (player, option, slot, itemId) -> {
//                if (player.getGameMode().isIronMan()) {
//                //    player.closeInterface(InterfaceType.MAIN);
//                    player.sendMessage("You can't access this shop.");
//                    return;
//                } else {
				player.getPacketSender().sendString(891, 14, "Supplies Shop");
				player.setShopIdentifier(7);
				handleEnteringShop(player, CustomShop2.MISC_STORE);
				open(player, CustomShop2.getItemsFromShop(player));
//                }
			};
			h.actions[30] = (DefaultAction) (player, option, slot, itemId) -> {
				if (player.getGameMode().isIronMan()) {//GameMode().STANDARD) {
					//    player.closeInterface(InterfaceType.MAIN);
					player.sendMessage("You can't access this shop.");
					return;
				} else {
					player.getPacketSender().sendString(891, 14, "Skilling Shop");
					player.setShopIdentifier(8);
					handleEnteringShop(player, CustomShop2.SKILLING_SUPPLIES);
					open(player, CustomShop2.getItemsFromShop(player));
				}
			};
			h.actions[32] = (DefaultAction) (player, option, slot, itemId) -> {
				player.getPacketSender().sendString(891, 14, "Donation Shop");
				player.setShopIdentifier(9);
				handleEnteringShop(player, CustomShop2.DONATOR_STORE);
				open(player, CustomShop2.getItemsFromShop(player));

			};
			h.actions[35] = (DefaultAction) (player, option, slot, itemId) -> {
				player.getPacketSender().sendString(891, 14, "Voting Shop");
				player.setShopIdentifier(10);
				handleEnteringShop(player, CustomShop2.VOTE_STORE);
				open(player, CustomShop2.getItemsFromShop(player));

			};
			h.actions[41] = (DefaultAction) (player, option, slot, itemId) -> {
				if (option == 1) {
					priceCheck(player, slot, itemId);
				} else {
					attemptBuy(player, option, slot, itemId);
				}
			};

			h.closedAction = (p, i) -> {
				if (p.getActiveCustomShop2() != null) {
					p.getActiveCustomShop2().removePlayerFromShop(p);
					p.setActiveCustomShop2(null);
					p.closeInterface(ToplevelComponent.SIDEMODAL);
				}
			};
		});

       /* InterfaceHandler.register(Interface.PLAYER_SHOP_INVENTORY, h -> h.actions[0] = (DefaultAction) (player, option, slot, itemId) -> {
            if(player.isVisibleInterface(Interface.CUSTOM_SHOP2)) {
                attemptSell(player, option, slot, itemId);
            }
        });*/
	}

	public static void handleEnteringShop(Player player, CustomShop2 CustomShop2) {
		CustomShop2.addPlayerToShop(player);
		player.setActiveCustomShop2(CustomShop2);
	}

	private static void attemptBuy(Player player, int option, int slot, int itemId) {
		CustomShop2 CustomShop2 = io.ruin.model.inter.handlers.shopinterface.CustomShop2.get(player.getShopIdentifier());
		if (CustomShop2 == null) {
			return;
		}

		ShopItem shopItem = CustomShop2.getShopItem(itemId);
		if (shopItem == null) {
			return;
		}

		Item item = new Item(shopItem.getItemId());

		ObjType itemDef = item.getDef();
		if (itemDef == null) {
			return;
		}

		if (ObjType.get(shopItem.getItemId()) != null && itemId != shopItem.getItemId()
			&& itemId != (ObjType.get(shopItem.getItemId()) != null ? itemDef.placeholderMainId : 0))
			return;

		if (option == 10) {
			item.examine(player);
			return;
		}

		if (!itemInShop(itemId, CustomShop2)) {
			player.sendMessage("");
			return;
		}

		ShopItem CustomShop2Item = Arrays.stream(CustomShop2.getShopItems())
			.filter(si -> si.getItemId() == itemId)
			.findFirst()
			.orElse(null);

		if (CustomShop2Item == null) {
			return;
		}

		if (option == 2) {
			buy(player, CustomShop2Item, CustomShop2, itemId, 1);
		} else if (option == 3) {
			buy(player, CustomShop2Item, CustomShop2, itemId, 5);
		} else if (option == 4) {
			buy(player, CustomShop2Item, CustomShop2, itemId, 10);
		} else if (option == 5) {
			player.integerInput("How many do you want to buy?",
				amt -> buy(player, CustomShop2Item, CustomShop2, itemId, amt));
		}
	}

	private static void buy(Player player, ShopItem CustomShop2Item, CustomShop2 CustomShop2, int itemId, int amount) {
		if (amount <= 0 || CustomShop2 == null || CustomShop2Item == null || itemId <= 0)
			return;


		/**
		 * Container check
		 */
		Item item = new Item(CustomShop2Item.getItemId());
		ObjType itemDef = item.getDef();
		if (itemDef == null) {
			return;
		}
		int freeSlots = player.getInventory().getFreeSlots();
		if (amount > freeSlots) {
			/**
			 * Attempt to note the given item.
			 */
			if (!itemDef.isNote() && itemDef.notedId != -1)
				itemDef = itemDef.fromNote();
		}
		if (itemDef.stackable) {
			/**
			 * 'Free' a slot if necessary.
			 */
			if (freeSlots == 0 && player.getInventory().findItem(itemDef.id) != null)
				freeSlots++;
		} else if (amount > freeSlots) {
			/**
			 * Set amount equal to free slots.
			 */
			amount = freeSlots;
		}
		if (freeSlots == 0) {
			player.sendMessage("Not enough space in your inventory.");
			return;
		}

		/**
		 * Currency check
		 */
		int price = CustomShop2Item.getPrice();
		Currency currency = CustomShop2.getCurrency();
		if (price != 0) {
			long buyPrice = (long) price * amount;
			int currencyAmount = currency.getCurrencyHandler().getCurrencyCount(player);
			if (currencyAmount < buyPrice) {
				if (price > currencyAmount) {
					/* not enough currency to even buy 1 */
					amount = 0;
				} else {
					amount = currencyAmount / price;
					buyPrice = (long) price * amount;
				}

				if (amount <= 0) {
					player.sendMessage("You did not bring enough " + StringUtils.fixCaps(currency.name().toString().replace("_", " ")) + " to make this purchase.");
					return;
				}
			}
			if (buyPrice <= 0L || buyPrice < price) {
				player.sendMessage("Please buy this item in a smaller quantity.");
				return;
			}
			currency.getCurrencyHandler().removeCurrency(player, (int) buyPrice);
		}

		player.getInventory().add(itemDef.id, amount);

		CustomShop2.refreshShop();
	}

	public static void attemptSell(Player player, int option, int slot, int itemId) {
		if (slot < 0 || slot > 28)
			return;

		Item item = player.getInventory().get(slot);

		if (item == null || (itemId != item.getId() && itemId != item.getDef().placeholderMainId))
			return;

		if (option == 10) {
			item.examine(player);
			return;
		}

		if (!item.getDef().tradeable) {
			player.sendMessage("You can't sell that item.");
			return;
		}

		CustomShop2 CustomShop2 = io.ruin.model.inter.handlers.shopinterface.CustomShop2.get(player.getShopIdentifier());

		if (CustomShop2 == null) {
			return;
		}

		if (!itemInShop(itemId, CustomShop2)) {
			player.sendMessage("This item is not available right now.");
			return;
		}

		ShopItem CustomShop2Item = Arrays.stream(CustomShop2.getShopItems())
			.filter(si -> si.getItemId() == itemId)
			.findFirst()
			.orElse(null);

		if (CustomShop2Item == null) {
			return;
		}

		int price = (int) (CustomShop2Item.getPrice() * 0.70);

		if (option == 1) {
			if (CustomShop2Item.getPrice() <= 0) {
				player.sendMessage("You can't sell that item to this store.");
			} else {
				player.sendMessage(item.getDef().name + " can be sold for "
					+ (NumberUtils.formatNumber(price)
					+ " " + CustomShop2.getCurrency().name()) + ".");
			}
			return;
		} else if (option == 2) {
			sell(player, CustomShop2Item, CustomShop2, price, itemId, 1);
		} else if (option == 3) {
			sell(player, CustomShop2Item, CustomShop2, price, itemId, 5);
		} else if (option == 4) {
			sell(player, CustomShop2Item, CustomShop2, price, itemId, 10);
		} else if (option == 5) {
			player.integerInput("How many do you want to sell?",
				amt -> sell(player, CustomShop2Item, CustomShop2, price, itemId, amt));
		}
	}

	public static void sell(Player player, ShopItem CustomShop2Item, CustomShop2 CustomShop2,
	                        int price, int itemId, int amount) {

		if (CustomShop2.getCurrency() != Currency.BLOOD_MONEY) {
			player.sendMessage("You can't sell any items to this store.");
			return;
		}

		if (amount > player.getInventory().getAmount(itemId)) {
			amount = player.getInventory().getAmount(itemId);
		}

		if (amount <= 0)
			return;

		int totalPrice = amount * price;

		player.getInventory().add(13307, totalPrice);
		player.getInventory().remove(itemId, amount);
		CustomShop2.refreshShop();
	}

	private static boolean itemInShop(int itemId, CustomShop2 CustomShop2) {
		if (CustomShop2 != null) {
			return Arrays.stream(CustomShop2.getShopItems()).anyMatch(i -> i.getItemId() == itemId);
		}
		return false;
	}

	private static void priceCheck(Player player, int slot, int itemId) {
		int shopId = player.getShopIdentifier();
		if (shopId < 0) {
			player.sendMessage("Something is wrong with this shop. Please contact a staff member.");
			return;
		}

		CustomShop2 CustomShop2 = io.ruin.model.inter.handlers.shopinterface.CustomShop2.get(shopId);

		if (CustomShop2 != null) {
			ShopItem selectedItem = Arrays.stream(CustomShop2.getShopItems())
				.filter(si -> si.getItemId() == itemId)
				.findFirst()
				.orElse(null);

			if (selectedItem == null) {
				return;
			}

			ObjType def = ObjType.get(selectedItem.getItemId());

			if (def != null) {
				player.sendMessage(def.name + " costs " + NumberUtils.formatNumber(selectedItem.getPrice())
					+ " " + StringUtils.fixCaps(CustomShop2.getCurrency().toString().replace("_", " ")) + "."
				);
			}
		}
	}

	public static void open(Player player, Item[] shopItems) {
		player.getPacketSender().sendItems(-1, 1, 93, player.getInventory().getItems());
		player.getPacketSender().sendItems(10005, shopItems);
		if (player.getShopIdentifier() < 1) {
			player.setShopIdentifier(1);
		}
		handleEnteringShop(player, CustomShop2.MELEE_STORE);
		player.getPacketSender().sendClientScript(917, "ii", -1, -1);
		player.getPacketSender().sendClientScript(10208, 11802, 11235, 12904, 12810, 7462, 13307, 23685, 6739);
		player.getPacketSender().sendClientScript(10207);
		player.getPacketSender().sendClientScript(149, "iiiiiisssss", 19726336, 93, 4, 7, 0, -1,
			"Value<col=ff9040>", "Sell 1<col=ff9040>", "Sell 5<col=ff9040>",
			"Sell 10<col=ff9040>", "Sell X<col=ff9040>");
		player.getPacketSender().sendIfEvents(891, 41, 0,
			127, 1150);
		player.getPacketSender().sendIfEvents(301, 0, 0, 27, 1086);
		if (!player.isVisibleInterface(Interface.CUSTOM_SHOP2)) {
			player.openInterface(ToplevelComponent.MAINMODAL, Interface.CUSTOM_SHOP2);
			player.openInterface(ToplevelComponent.SIDEMODAL, Interface.PLAYER_SHOP_INVENTORY);
		}
	}
}
