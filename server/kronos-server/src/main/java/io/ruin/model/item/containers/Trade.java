package io.ruin.model.item.containers;

import discord.webhooks.logs.SuspiciousTransactionHook;
import discord.webhooks.logs.TradeBetweenPlayersHook;
import io.ruin.HooksV2;
import io.ruin.Server;
import io.ruin.api.utils.ServerWrapper;
import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.ItemContainerG;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.services.Loggers;
import io.ruin.utility.TickDelay;
import io.ruin.utility.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

import static io.ruin.cache.ItemID.COINS_995;

public class Trade extends ItemContainer {
	public static interface Hook {
		record OnRequest(Player player, Player target) implements Hook {
		};
	}

	private interface TradeAction extends InterfaceAction {

		void handle(Player player, int option, int slot, int itemId);

		default void handleClick(Player player, int option, int slot, int itemId) {
			if (player.getTrade().targetTrade != null) // active check!!
				handle(player, option, slot, itemId);
		}

	}

	private interface SimpleTradeAction extends TradeAction {

		void handle(Player player);

		default void handle(Player player, int option, int slot, int itemId) {
			handle(player);
		}

	}

	public static HooksV2<Hook> hooks = new HooksV2<>(Hook.class);

	/**
	 * Actions
	 */

	public static void register() {
		/*
		 * First screen
		 */
		InterfaceHandler.register(Interface.TRADE_SCREEN, h -> {
			h.actions[10] = (SimpleTradeAction) p -> p.getTrade().accept(true);
			h.actions[25] = (TradeAction) (p, option, slot, itemId) -> {
				Item item = p.getTrade().get(slot, itemId);
				if (item == null)
					return;
				if (option == 1)
					p.getTrade().remove(item, 1);
				else if (option == 2)
					p.getTrade().remove(item, 5);
				else if (option == 3)
					p.getTrade().remove(item, 10);
				else if (option == 4)
					p.getTrade().remove(item, Integer.MAX_VALUE);
				else if (option == 5)
					p.integerInput("Enter amount:", amt -> p.getTrade().remove(item, amt));
				else
					item.examine(p);
			};
			h.actions[28] = (TradeAction) (p, option, slot, itemId) -> {
				Item item = p.getTrade().targetTrade.get(slot, itemId);
				if (item == null)
					return;
				item.examine(p);
			};
		});
		/*
		 * First screen (inventory)
		 */
		InterfaceHandler.register(Interface.TRADE_INVENTORY, h -> {
			h.actions[0] = (TradeAction) (p, option, slot, itemId) -> {
				Item item = p.getInventory().get(slot, itemId);
				if (item == null)
					return;
				if (option == 1)
					p.getTrade().offer(item, 1);
				else if (option == 2)
					p.getTrade().offer(item, 5);
				else if (option == 3)
					p.getTrade().offer(item, 10);
				else if (option == 4)
					p.getTrade().offer(item, Integer.MAX_VALUE);
				else if (option == 5)
					p.integerInput("Enter amount:", amt -> p.getTrade().offer(item, amt));
				else
					item.examine(p);
			};
		});
		/*
		 * Confirmation screen
		 */
		InterfaceHandler.register(Interface.TRADE_CONFIRMATION, h -> {
			h.actions[13] = (SimpleTradeAction) p -> p.getTrade().accept(false);
			h.actions[14] = (SimpleTradeAction) p -> {
				p.getTrade().close();
				p.getTrade().closeInterfaces();
			};
		});
	}

	/**
	 * Initiation
	 */

	private transient int stage = 0;
	private transient boolean accepted;
	private transient Trade targetTrade;
	private transient boolean sigmund;
	private transient TickDelay requestEnd;
	private transient int requestUserId = -1;

	public Trade(Player player) {
		init(player, 28, -1, 64212, 90, false);
		mirror(-2, 60981, 90);
	}

	public void tradeSigmund() {
		Trade sigmundTrade = new Trade(null);
		sigmundTrade.sigmund = true;
		init(sigmundTrade);
	}

	public void request(Player target) {
		if (player.getName().equalsIgnoreCase(target.getName())) {
			return;
		}

		if (player.jailerName != null) {
			player.sendMessage("You can't trade while jailed.");
			return;
		}

		if (target.jailerName != null) {
			player.sendMessage("You can't trade with jailed players.");
			return;
		}

		if (player.getDuel().stage >= 4) {
			player.sendMessage("You can't trade while in a duel.");
			return;
		}

		if (player.joinedTournament) {
			player.sendMessage("You can't trade while you're signed up for a tournament.");
			return;
		}

		if (player.supplyChestRestricted) {
			player.sendMessage("The power of the supply chest prevents you from trading!");
			return;
		}

		if (target.joinedTournament) {
			player.sendMessage("This player can't trade right now.");
			return;
		}

		if (target.isLocked()) {
			player.sendMessage("This player is currently busy.");
			return;
		}

		if (hooks.handle(new Hook.OnRequest(player, target))) {
			return;
		}

		var targetTrade = target.getTrade();
		if (targetTrade.requestUserId == player.getUserId() && targetTrade.requestEnd.isDelayed()) {
			init(targetTrade);
			targetTrade.init(this);
			return;
		}
		if (requestEnd == null)
			requestEnd = new TickDelay();
		requestEnd.delay(25);
		requestUserId = target.getUserId();

		target.getPacketSender().sendMessage(player.getName() + " wishes to trade with you.", player.getName(), 101);
		sendMessage("Sending trade offer...");
		if (player.wildernessLevel > 0)
			sendMessage("Trading in the Wilderness is dangerous - you might get killed!");
	}

	public void close() {
		if (targetTrade == null) {
			return;
		}
		if (!targetTrade.sigmund) {
			targetTrade.sendMessage("The other player declined the trade.");
			targetTrade.destroy(true);
			targetTrade.closeInterfaces();
		}
		sendMessage("You declined the trade.");
		destroy(true);
	}

	/**
	 * Offer
	 */

	public void offer(Item item, int amount) {
		if (stage != 1) {
			/* not on the first screen! */
			return;
		}
		boolean admin = player.isManager() || targetTrade.player.isAdmin() || targetTrade.player.isManager();
		if (targetTrade.sigmund) {
			// if (SigmundTheMerchant.getPrice(player, item) == 0 ||
			// !item.getDef().tradeable || item.getDef().free) {
			sendMessage("Sigmund has no interest in buying that item.");
			return;
			// }
		} else if (!item.getDef().tradeable && !admin) {
			sendMessage("You can't trade this item.");
			return;
		} else if (!canTrade(item) && !admin) {
			sendMessage("You can't trade this item.");
			return;
		}
		int moved = item.move(item.getId(), amount, this);
		if (moved <= 0) {
			sendMessage("Not enough space in your trade.");
			return;
		}
		update(targetTrade);
	}

	/**
	 * Remove
	 */

	public void remove(Item item, int amount) {
		if (stage != 1) {
			/* not on the first screen! */
			return;
		}
		int moved = item.move(item.getId(), amount, player.getInventory());
		if (moved <= 0) {
			Server.logWarning(player.getName() + " failed to remove item (" + item.getId() + ", " + item.getAmount()
					+ ") from trade, this should NEVER happen!");
			return;
		}
		update(targetTrade);

		if (targetTrade.sigmund) {
			/* no need to warn */
			return;
		}

		player.getPacketSender().sendClientScript(765, "Ii", 0, item.getSlot());
		VarPlayerRepository.MY_TRADE_MODIFIED.set(player, 1);

		targetTrade.player.getPacketSender().sendClientScript(765, "Ii", 1, item.getSlot());
		VarPlayerRepository.OTHER_TRADE_MODIFIED.set(targetTrade.player, 1);
	}

	private void init(Trade targetTrade) {
		close();
		this.targetTrade = targetTrade;
		this.requestUserId = -1;
		update(null);
	}

	private void sendMessage(String message) {
		player.getPacketSender().sendMessage(message, null, 102);
	}

	private void updateSigmund() {
		long price = 0L;
		for (Item item : getItems()) {
			if (item != null)
				price += 0;// (long) SigmundTheMerchant.getPrice(player, item) * item.getAmount();
		}
		Item coins = targetTrade.get(0);
		if (price == 0) {
			if (coins == null)
				return;
			coins.remove();
		} else {
			if (price > Integer.MAX_VALUE)
				price = Integer.MAX_VALUE;
			if (coins == null)
				targetTrade.set(0, new Item(COINS_995, (int) price));
			else
				coins.setAmount((int) price);
		}
		targetTrade.sendUpdates(this);
	}

	/**
	 * Updating
	 */

	private void update(ItemContainerG<?> mirrorContainer) {
		if (mirrorContainer == null) {
			mirrorContainer = this;
			open();
		} else {
			accepted = false;
			if (targetTrade.sigmund) {
				updateState();
				updateSigmund();
				sendUpdates(null);
				return;
			}

			targetTrade.accepted = false;
			targetTrade.updateState();
			targetTrade.updateSlots();
			updateState();
		}
		sendUpdates(mirrorContainer);
	}

	/**
	 * Interfaces
	 */

	private void open() {
		for (int slot = 0; slot < 28; slot++) {
			update(slot);
		}
		sendUpdates(targetTrade);
		stage = 1;
		sendAll = true;
		String targetName = targetTrade.sigmund ? "Sigmund The Merchant" : targetTrade.player.getName();
		player.getPacketSender().sendClientScript(209, "s", targetName);
		VarPlayerRepository.MY_TRADE_MODIFIED.set(player, 0);
		VarPlayerRepository.OTHER_TRADE_MODIFIED.set(player, 0);
		player.getPacketSender().sendClientScript(917, "ii", -1, -1);
		player.openInterface(ToplevelComponent.MAINMODAL, 335);
		player.openInterface(ToplevelComponent.SIDEMODAL, 336);
		player.getPacketSender().sendString(335, 24, player.getName() + " offers:");
		player.getPacketSender().sendString(335, 27, targetName + " offers:");
		player.getPacketSender().sendString(335, 31, "Trading With: " + targetName);
		updateState();
		player.getPacketSender().sendIfEvents(335, 25, 0, 27, 1086);
		player.getPacketSender().sendIfEvents(335, 28, 0, 27, 1024);
		player.getPacketSender().sendClientScript(149, "IviiiIsssss", 22020096, 93, 4, 7, 0, -1, "Offer<col=ff9040>",
				"Offer-5<col=ff9040>", "Offer-10<col=ff9040>", "Offer-All<col=ff9040>", "Offer-X<col=ff9040>");
		player.getPacketSender().sendIfEvents(336, 0, 0, 27, 1086);
		updateSlots();
	}

	private void updateState() {
		String s = null;
		if (accepted)
			s = "Waiting for other player...";
		else if (targetTrade.accepted)
			s = "Other player has accepted.";
		if (stage == 1)
			player.getPacketSender().sendString(335, 30, s == null ? "" : s);
		else
			player.getPacketSender().sendString(334, 4, s == null ? "Are you sure you want to make this trade?" : s);
	}

	private void updateSlots() {
		if (targetTrade.sigmund)
			player.getPacketSender().sendString(335, 9, "Offer items you would like to sell to Sigmund.");
		else
			player.getPacketSender().sendString(335, 9, targetTrade.player.getName() + " has "
					+ targetTrade.player.getInventory().getFreeSlots() + " free inventory slots.");
	}

	private void closeInterfaces() {
		player.closeInterface(ToplevelComponent.MAINMODAL);
		player.closeInterface(ToplevelComponent.SIDEMODAL);
	}

	/**
	 * Accept
	 */

	private void accept(boolean firstScreen) {
		boolean canAccept = true;
		if (accepted)
			return;
		if (firstScreen) {
			if (stage != 1)
				return;
			int slotsRequired = 0;
			for (Item item : targetTrade.getItems()) {
				if (item == null)
					continue;
				if (!player.getInventory().hasRoomFor(item)) {
					canAccept = false;
					break;
				}
				if (item.getDef().stackable && player.getInventory().contains(item.getId()))
					continue;
				slotsRequired++;
			}
			if (!canAccept) {
				sendMessage("You don't have enough inventory space to accept this trade.");
				return;
			}
			if (player.getInventory().getFreeSlots() < slotsRequired) {
				sendMessage("You don't have enough inventory space to accept this trade.");
				return;
			}
			if (targetTrade.sigmund) {
				second();
				return;
			}
			if (targetTrade.accepted) {
				second();
				targetTrade.second();
				return;
			}
		} else {
			if (stage != 2)
				return;
			if (targetTrade.sigmund) {
				destroy(false);
				closeInterfaces();
				return;
			}
			if (targetTrade.accepted) {
				targetTrade.destroy(false);
				targetTrade.closeInterfaces();
				destroy(false);
				closeInterfaces();
				return;
			}
		}
		accepted = true;
		targetTrade.updateState();
		updateState();
	}

	private boolean canTrade(Item item) {
		return !item.hasAttributes();
	}

	/**
	 * Second
	 */

	private void second() {
		stage = 2;
		accepted = false;
		player.getPacketSender().sendString(334, 4, "Are you sure you want to make this trade?");
		player.getPacketSender().sendString(334, 23, "You are about to give:");
		player.getPacketSender().sendString(334, 24, "In return you will receive:");
		player.getPacketSender().sendString(334, 30,
				"Trading with:<br>" + (targetTrade.sigmund ? "Sigmund The Merchant" : targetTrade.player.getName()));
		player.getPacketSender().sendClientScript(917, "ii", -1, -1);
		player.openInterface(ToplevelComponent.MAINMODAL, 334);
	}

	/**
	 * Destroy
	 */

	private void destroy(boolean returnItems) {
		if (returnItems) {
			/*
			 * Return my items
			 */
			for (Item item : getItems()) {
				if (item != null)
					player.getInventory().add(item);
			}
		} else if (targetTrade.sigmund) {
			/*
			 * Trade coins
			 */
			Item coins = targetTrade.get(0);
			if (coins != null)
				player.getInventory().add(coins);
			sendMessage("Your trade with Sigmund the Merchant was successful.");
			if (coins != null && coins.getAmount() > 1_000_000) {
				String format = String.format("Sigmund: Player:[%s] Coins:[%d] Sold:[%s] ", player.getName(), coins.getAmount(),
						Arrays.toString(getItems()));
				ServerWrapper.log(format);
			}
			for (Item item : getItems()) {
				if (item != null)
					Loggers.logSigmund(player.getUserId(), item.getId(), item.getAmount());
			}
		} else {
			/*
			 * Trade my items
			 */
			for (Item item : getItems()) {
				if (item != null)
					targetTrade.player.getInventory().add(item);
			}
			sendMessage("Your trade with " + targetTrade.player.getName() + " was successful.");
			if (targetTrade.targetTrade != null && (this.getCount() > 0 || targetTrade.getCount() > 0)) {
				Loggers.logTrade(player, targetTrade.player, items, targetTrade.items);
				StringBuilder sb = new StringBuilder();
				StringBuilder sb2 = new StringBuilder();
				sb.append("Trade: ");
				var tradedItems = new ArrayList<Item>();
				for (Item item : items) {
					if (item != null) {
						tradedItems.add(item);
						sb.append(item.getDef().getName()).append(":").append(item.getAmount()).append(" ");
					}
				}
				for (Item item : targetTrade.items) {
					if (item != null) {
						for (int i = 0; i < tradedItems.size(); i++) {
							var tradeitem = tradedItems.get(i);
							if (tradeitem != null && tradeitem.getId() == item.getId()) {
								tradedItems.set(i, new Item(item.getId(), item.getAmount() + tradeitem.getAmount()));
								break;
							}
						}
						sb2.append(item.getDef().getName()).append(":").append(item.getAmount()).append(" ");
					}
				}

				// Logs
				var primaryPlayerItems = new JSONArray();
				for (Item item : items) {
					if (item == null) continue;
					var itemJson = new JSONObject();
						itemJson.put("id", item.getId());
						itemJson.put("name", item.getDef().getName());
						itemJson.put("amount", item.getAmount());
					primaryPlayerItems.put(itemJson);
				}
				var secondaryPlayerItems = new JSONArray();
				for (Item item : targetTrade.items) {
					if (item == null) continue;
					var itemJson = new JSONObject();
						itemJson.put("id", item.getId());
						itemJson.put("name", item.getDef().getName());
						itemJson.put("amount", item.getAmount());
					secondaryPlayerItems.put(itemJson);
				}

				var jsonObject = new JSONObject();
					jsonObject.put("player", player.getName());
					jsonObject.put("recipient", targetTrade.player.getName());
					jsonObject.put("trade_x", player.getPosition().getX());
					jsonObject.put("trade_y", player.getPosition().getY());
					jsonObject.put("trade_z", player.getPosition().getZ());
					jsonObject.put("primary_trade_items", primaryPlayerItems);
					jsonObject.put("secondary_trade_items", secondaryPlayerItems);

				// Check the transaction as a whole for any flagged sus value of items
				for (var item : tradedItems) {
					if (item.getId() == ItemID.PLATINUM_TOKEN && item.getAmount() >= 10_000_000)
						SuspiciousTransactionHook.sendHookToSusLogsOnDiscord(jsonObject);
				}

				TradeBetweenPlayersHook.sendTradeLogsToDiscord(jsonObject);
				// Replacing the 2 old logs, with a single log
//				RareDropEmbedMessage.sendTradeLogsToDiscord(player, targetTrade.player.getName(), sb.toString());
//				RareDropEmbedMessage.sendTradeLogsToDiscord(targetTrade.player, player.getName(), sb2.toString());

				if (player.isAdmin() || targetTrade.player.isAdmin()) {
					TradeBetweenPlayersHook.sendTradeLogsToDiscordForOwner(jsonObject);
					// Replacing the 2 old logs, with a single log
//					RareDropEmbedMessage.sendTradeLogsToDiscordForOwner(player, targetTrade.player.getName(), sb.toString());
//					RareDropEmbedMessage.sendTradeLogsToDiscordForOwner(targetTrade.player, player.getName(), sb2.toString());
				}
			}

		}
		stage = 0;
		accepted = false;
		targetTrade = null;
		player.getPacketSender().sendClientScript(209, "s", "");
		clear();
	}

	private String getTargetItems(Item[] items) {
		return Arrays.stream(items)
			.filter(Objects::nonNull)
			.map(item -> "%s x%s".formatted(item.getDef().getName(), Utils.formatMoneyString(item.getAmount())))
			.collect(Collectors.joining(", "))
			.replace(", ", "\n");
	}

}
