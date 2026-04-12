package io.ruin.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ruin.Server;
import io.ruin.api.database.DatabaseUtils;
import io.ruin.api.utils.JsonUtils;
import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainerG;
import io.ruin.model.item.containers.bank.Bank;
import io.ruin.model.item.containers.bank.BankItem;
import io.ruin.model.map.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.ruin.cache.ItemID.BLOOD_MONEY;
import static io.ruin.cache.ItemID.COINS_995;

public final class Loggers extends DatabaseUtils {
	private static final Path ROOT_DIR_PATH = Path.of("data/runtime/logs");
	public static final Logger memlog = LoggerFactory.getLogger("MemoryLogger");
	public static final Marker successfulTestMarker = MarkerFactory.getMarker("testOK");

	public static void logRaidsUnique(String player, String itemName, int raidsCount) {
		Server.gameDb.execute(con -> {
			try (PreparedStatement statement = con
					.prepareStatement(insertQuery("logs_raids_uniques", "player", "item", "raids_count"))) {
				statement.setString(1, player);
				statement.setString(2, itemName);
				statement.setInt(3, raidsCount);
			}
		});
	}

	public static void logRaidsCompletion(String[] players, String duration, int points) {
		String playersList = String.join(",", players);
		Server.gameDb.execute(con -> {
			try (PreparedStatement statement = con
					.prepareStatement(insertQuery("logs_raids_completed", "players", "duration", "total_points"))) {
				statement.setString(1, playersList);
				statement.setString(2, duration);
				statement.setInt(3, points);
			}
		});
	}

	public static void logPublicChat(int userId, String userName, String userIp, String message, int type, int effects) {
		Map<String, Object> logEntry = new HashMap<>();
		logEntry.put("user_id", userId);
		logEntry.put("user_name", userName);
		logEntry.put("user_ip", userIp);
		logEntry.put("message", message);
		logEntry.put("type", type);
		logEntry.put("effects", effects);
		logEntry.put("world_id", World.id);
		logEntry.put("world_stage", World.stage.name());
		logEntry.put("world_type", World.type.name());

		writeLog(ROOT_DIR_PATH.resolve("public_chat"), userName + ".json", logEntry);
	}

	public static void logClanChat(int userId, String userName, String userIp, String message) {
		Map<String, Object> logEntry = new HashMap<>();
		logEntry.put("user_id", userId);
		logEntry.put("user_name", userName);
		logEntry.put("user_ip", userIp);
		logEntry.put("message", message);
		logEntry.put("world_id", World.id);
		logEntry.put("world_stage", World.stage.name());
		logEntry.put("world_type", World.type.name());

		writeLog(ROOT_DIR_PATH.resolve("clan_chat"), userName + ".json", logEntry);
	}

	public static void logBond(int userId, String userName, String userIp, String message) {
		Map<String, Object> logEntry = new HashMap<>();
		logEntry.put("user_id", userId);
		logEntry.put("user_name", userName);
		logEntry.put("user_ip", userIp);
		logEntry.put("message", message);
		logEntry.put("world_id", World.id);
		logEntry.put("world_stage", World.stage.name());
		logEntry.put("world_type", World.type.name());

		writeLog(ROOT_DIR_PATH.resolve("bond"), userName + ".json", logEntry);
	}

	public static void logYell(int userId, String userName, String userIp, String message) {
		Map<String, Object> logEntry = new HashMap<>();
		logEntry.put("user_id", userId);
		logEntry.put("user_name", userName);
		logEntry.put("user_ip", userIp);
		logEntry.put("message", message);

		writeLog(ROOT_DIR_PATH.resolve("chat_yell"), userName + ".json", logEntry);
	}

	public static void logPrivateChat(int userId, String userName, String userIp, String friendName, String message) {
		Map<String, Object> logEntry = new HashMap<>();
		logEntry.put("user_id", userId);
		logEntry.put("user_name", userName);
		logEntry.put("user_ip", userIp);
		logEntry.put("friend_name", friendName);
		logEntry.put("message", message);
		logEntry.put("world_id", World.id);
		logEntry.put("world_stage", World.stage.name());
		logEntry.put("world_type", World.type.name());

		writeLog(ROOT_DIR_PATH.resolve("chat_private"), userName + ".json", logEntry);
	}

	public static void logCommand(int userId, String userName, String userIp, String commandQuery) {
		Map<String, Object> logEntry = new HashMap<>();
		logEntry.put("user_id", userId);
		logEntry.put("user_name", userName);
		logEntry.put("user_ip", userIp);
		logEntry.put("cmd_query", commandQuery);
		logEntry.put("world_id", World.id);
		logEntry.put("world_stage", World.stage.name());
		logEntry.put("world_type", World.type.name());

		writeLog(ROOT_DIR_PATH.resolve("command"), userName + ".json", logEntry);

	}

	public static void logLoyaltyChest(int userId, String userName, String userIp, int spree, Item... rewards) {
		Server.gameDb.execute(con -> {
			try (PreparedStatement statement = con.prepareStatement(insertQuery("logs_loyalty_chest", "user_id", "user_name",
					"user_ip", "spree", "rewards", "world_id", "world_stage", "world_type"))) {
				statement.setInt(1, userId);
				statement.setString(2, userName);
				statement.setString(3, userIp);
				statement.setInt(4, spree);
				statement.setString(5, toJson(rewards));
				statement.setInt(6, World.id);
				statement.setString(7, World.stage.name());
				statement.setString(8, World.type.name());
				statement.execute();
			}
		});
	}

	public static void logTrade(int userId1, String userName1, String userIp1, int userId2, String userName2,
			String userIp2, Item[] userItems1_0, Item[] userItems2_0) {
		Item[] userItems1 = userItems1_0.clone();
		Item[] userItems2 = userItems2_0.clone();
		// ^clone required because the original array is cleared after a trade.
		Server.gameDb.execute(con -> {
			try (PreparedStatement statement = con
					.prepareStatement(insertQuery("logs_trades", "user_id1", "user_name1", "user_ip1", "user_id2", "user_name2",
							"user_ip2", "items1", "items2", "world_id", "world_stage", "world_type"))) {
				statement.setInt(1, userId1);
				statement.setString(2, userName1);
				statement.setString(3, userIp1);
				statement.setInt(4, userId2);
				statement.setString(5, userName2);
				statement.setString(6, userIp2);
				statement.setString(7, toJson(userItems1));
				statement.setString(8, toJson(userItems2));
				statement.setInt(9, World.id);
				statement.setString(10, World.stage.name());
				statement.setString(11, World.type.name());
				statement.execute();
			}
		});
	}

	public static void logTrade(Player trader1, Player trader2, Item[] userItems1_0, Item[] userItems2_0) {
		// Calculate values
		long value1 = 0;
		long value2 = 0;

		// Make copies, these can change immediately after submission
		Item[] offer1Items = userItems1_0.clone();
		Item[] offer2Items = userItems2_0.clone();

		// Calculate total price of the first offer
		for (Item i : offer1Items) {
			if (i != null)
				value1 += getWealth(i);
		}

		// Calculate total of the second half
		for (Item i : offer2Items) {
			if (i != null)
				value2 += getWealth(i);
		}

		// Final values to use them in the anonymous subclass
		final long finalPrice1 = value1;
		final long finalPrice2 = value2;
		final int trader1Id = trader1.getUserId();
		final int trader2Id = trader2.getUserId();
		final String trader1Name = trader1.getName();
		final String trader2Name = trader2.getName();
		final Position tile = trader1.getPosition();

		// Log the trade to JSON for each player
		logTradeToJson(trader1Id, trader1Name, finalPrice1, offer1Items);
		logTradeToJson(trader2Id, trader2Name, finalPrice2, offer2Items);
	}

	// Helper method to log trade details to JSON file
	@SuppressWarnings("unchecked")
	private static void logTradeToJson(int userId, String userName, long tradeValue, Item[] items) {
		Map<String, Object> tradeLog = new HashMap<>();
		tradeLog.put("user_id", userId);
		tradeLog.put("user_name", userName);
		tradeLog.put("trade_value", tradeValue);

		// Add items to the trade log
		Map<String, Object>[] itemLogs = new Map[items.length];
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				Map<String, Object> itemLog = new HashMap<>();
				itemLog.put("item_id", items[i].getId());
				itemLog.put("amount", items[i].getAmount());
				itemLogs[i] = itemLog;
			}
		}
		tradeLog.put("items", itemLogs);

		writeLog(ROOT_DIR_PATH.resolve("trade"), userName + ".json", tradeLog);
	}

	public static void logDuelStake(int userId1, String userName1, String userIp1, int userId2, String userName2,
			String userIp2, Item[] userItems1_0, Item[] userItems2_0, int winnerId) {
		Item[] userItems = userItems1_0.clone();
		Item[] targetItems = userItems2_0.clone();

		// Log the duel stake information to JSON
		logDuelStakeToJson(userId1, userName1, userIp1, userId2, userName2, userIp2, userItems, targetItems, winnerId);
	}

	// Helper method to log duel stake details to JSON file
	private static void logDuelStakeToJson(int userId1, String userName1, String userIp1, int userId2, String userName2,
			String userIp2, Item[] userItems, Item[] targetItems, int winnerId) {
		Map<String, Object> duelStakeLog = new HashMap<>();
		duelStakeLog.put("user_id1", userId1);
		duelStakeLog.put("user_name1", userName1);
		duelStakeLog.put("user_ip1", userIp1);
		duelStakeLog.put("user_id2", userId2);
		duelStakeLog.put("user_name2", userName2);
		duelStakeLog.put("user_ip2", userIp2);
		duelStakeLog.put("items1", itemsToJsonArray(userItems));
		duelStakeLog.put("items2", itemsToJsonArray(targetItems));
		duelStakeLog.put("winner_id", winnerId);
		duelStakeLog.put("world_id", World.id);
		duelStakeLog.put("world_stage", World.stage.name());
		duelStakeLog.put("world_type", World.type.name());

		writeLog(ROOT_DIR_PATH.resolve("duel_stake"), userName1 + ".json", duelStakeLog);
	}

	// Helper method to convert items to JSON array
	@SuppressWarnings({ "DuplicatedCode", "unchecked" })
	private static Map<String, Object>[] itemsToJsonArray(Item[] items) {
		Map<String, Object>[] itemLogs = new Map[items.length];
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				Map<String, Object> itemLog = new HashMap<>();
				itemLog.put("item_id", items[i].getId());
				itemLog.put("amount", items[i].getAmount());
				itemLogs[i] = itemLog;
			}
		}
		return itemLogs;
	}

	public static void logDangerousDeath(int userId, String userName, String userIp, int killerId, String killerName,
			String killerIp, List<Item> itemsKept, List<Item> itemsLost) {
		// Log the dangerous death information to JSON
		logDangerousDeathToJson(userId, userName, userIp, killerId, killerName, killerIp, itemsKept, itemsLost);
	}

	// Helper method to log dangerous death details to JSON file
	private static void logDangerousDeathToJson(int userId, String userName, String userIp, int killerId,
			String killerName, String killerIp, List<Item> itemsKept, List<Item> itemsLost) {
		Map<String, Object> dangerousDeathLog = new HashMap<>();
		dangerousDeathLog.put("user_id", userId);
		dangerousDeathLog.put("user_name", userName);
		dangerousDeathLog.put("user_ip", userIp);
		dangerousDeathLog.put("killer_id", killerId);
		dangerousDeathLog.put("killer_name", killerName);
		dangerousDeathLog.put("killer_ip", killerIp);
		dangerousDeathLog.put("items_kept", itemsToListMap(itemsKept));
		dangerousDeathLog.put("items_lost", itemsToListMap(itemsLost));
		dangerousDeathLog.put("world_id", World.id);
		dangerousDeathLog.put("world_stage", World.stage.name());
		dangerousDeathLog.put("world_type", World.type.name());

		writeLog(ROOT_DIR_PATH.resolve("danger_death"), userName + ".json", dangerousDeathLog);
	}

	// Helper method to convert items to a list of maps
	private static List<Map<String, Object>> itemsToListMap(List<Item> items) {
		List<Map<String, Object>> itemLogs = new ArrayList<>();
		for (Item item : items) {
			Map<String, Object> itemLog = new HashMap<>();
			itemLog.put("item_id", item.getId());
			itemLog.put("amount", item.getAmount());
			itemLogs.add(itemLog);
		}
		return itemLogs;
	}

	public static void logDrop(int userId, String userName, String userIp, int itemId, int itemAmount, int x, int y,
			int z) {
		// Log the drop information to JSON
		logDropToJson(userId, userName, userIp, itemId, itemAmount, x, y, z);
	}

	// Helper method to log drop details to JSON file
	private static void logDropToJson(int userId, String userName, String userIp, int itemId, int itemAmount, int x,
			int y, int z) {
		Map<String, Object> dropLog = new HashMap<>();
		dropLog.put("user_id", userId);
		dropLog.put("user_name", userName);
		dropLog.put("user_ip", userIp);
		dropLog.put("item_id", itemId);
		dropLog.put("item_name", ObjType.get(itemId).name);
		dropLog.put("item_amount", itemAmount);
		dropLog.put("x", x);
		dropLog.put("y", y);
		dropLog.put("z", z);
		dropLog.put("world_id", World.id);
		dropLog.put("world_stage", World.stage.name());
		dropLog.put("world_type", World.type.name());

		writeLog(ROOT_DIR_PATH.resolve("drop"), userName + ".json", dropLog);
	}

	public static void logPickup(int userId, String userName, String userIp, int itemId, int itemAmount, int x, int y,
			int z) {
		// Log the pickup information to JSON
		logPickupToJson(userId, userName, userIp, itemId, itemAmount, x, y, z);
	}

	// Helper method to log pickup details to JSON file
	private static void logPickupToJson(int userId, String userName, String userIp, int itemId, int itemAmount, int x,
			int y, int z) {
		Map<String, Object> pickupLog = new HashMap<>();
		pickupLog.put("user_id", userId);
		pickupLog.put("user_name", userName);
		pickupLog.put("user_ip", userIp);
		pickupLog.put("item_id", itemId);
		pickupLog.put("item_name", ObjType.get(itemId).name);
		pickupLog.put("item_amount", itemAmount);
		pickupLog.put("x", x);
		pickupLog.put("y", y);
		pickupLog.put("z", z);
		pickupLog.put("world_id", World.id);
		pickupLog.put("world_stage", World.stage.name());
		pickupLog.put("world_type", World.type.name());

		writeLog(ROOT_DIR_PATH.resolve("pickup"), userName + ".json", pickupLog);
	}

	public static void logDropTrade(String takerId, @Nullable String dropperId, String takerIp, String dropperIp,
			String takerName, String dropperName, int itemId, int amount, int x, int y, int z, long timeDropped) {
		if (Objects.equals(takerId, dropperId))
			return;

		// Log the drop trade information to JSON
		logDropTradeToJson(takerId, dropperId, takerIp, dropperIp, takerName, dropperName, itemId, amount, x, y, z,
				timeDropped);
	}

	// Helper method to log drop trade details to JSON file
	private static void logDropTradeToJson(String takerId, @Nullable String dropperId, String takerIp, String dropperIp,
			String takerName, String dropperName, int itemId, int amount, int x, int y, int z, long timeDropped) {
		Map<String, Object> dropTradeLog = new HashMap<>();
		dropTradeLog.put("taker_id", takerId);
		dropTradeLog.put("dropper_id", dropperId == null ? "" : dropperId);
		dropTradeLog.put("taker_ip", takerIp);
		dropTradeLog.put("dropper_ip", dropperIp);
		dropTradeLog.put("taker_name", takerName);
		dropTradeLog.put("dropper_name", dropperName);
		dropTradeLog.put("item_id", itemId);
		dropTradeLog.put("amount", amount);
		dropTradeLog.put("value", ObjType.get(itemId).protectValue * amount);
		dropTradeLog.put("x", x);
		dropTradeLog.put("y", y);
		dropTradeLog.put("z", z);
		dropTradeLog.put("world_id", World.id);
		dropTradeLog.put("time_dropped", new Timestamp(timeDropped));

		writeLog(
				ROOT_DIR_PATH.resolve("drop_trade_logs"),
				takerName + "_vs_" + dropperName + "_drop_trade_log.json",
				dropTradeLog);
	}

	public static void logShopBuy(int userId, String userName, String userIp, int itemId, int itemPrice, int buyAmount) {
		Server.gameDb.execute(con -> {
			try (PreparedStatement statement = con.prepareStatement(insertQuery("logs_shop_buys", "user_id", "user_name",
					"user_ip", "item_id", "item_name", "item_price", "buy_amount", "world_id", "world_stage", "world_type"))) {
				statement.setInt(1, userId);
				statement.setString(2, userName);
				statement.setString(3, userIp);
				statement.setInt(4, itemId);
				statement.setString(5, ObjType.get(itemId).name);
				statement.setInt(6, itemPrice);
				statement.setInt(7, buyAmount);
				statement.setInt(8, World.id);
				statement.setString(9, World.stage.name());
				statement.setString(10, World.type.name());
				statement.execute();
			}
		});
	}

	public static void logSigmund(int userId, int itemId, int amount) {
	}

	public static void logTournamentResults(int userId, String userName, String userIp, int place) {
		Server.gameDb.execute(con -> {
			try (PreparedStatement statement = con.prepareStatement(insertQuery("logs_tournament_results", "user_id",
					"user_name", "user_ip", "place", "world_id", "world_stage", "world_type"))) {
				statement.setInt(1, userId);
				statement.setString(2, userName);
				statement.setString(3, userIp);
				statement.setInt(4, place);
				statement.setInt(5, World.id);
				statement.setString(6, World.stage.name());
				statement.setString(7, World.type.name());
				statement.execute();
			}
		});
	}

	public static void addOnlinePlayer(int userId, String userName, int worldId, String userIp, boolean isHelper,
			boolean isModerator, boolean isAdministrator) {
		Server.gameDb.execute(con -> {
			try (PreparedStatement statement = con.prepareStatement(insertQuery("online_characters", "user_id", "user_name",
					"world_id", "ip", "is_helper", "is_moderator", "is_administrator"))) {
				statement.setInt(1, userId);
				statement.setString(2, userName);
				statement.setInt(3, worldId);
				statement.setString(4, userIp);
				statement.setBoolean(5, isHelper);
				statement.setBoolean(6, isModerator);
				statement.setBoolean(7, isAdministrator);
				statement.execute();
			}
		});
	}

	public static void addCharFile(int userId, String userName, int worldId, String userIp, boolean isHelper,
			boolean isModerator, boolean isAdministrator) {
		Server.gameDb.execute(con -> {
			try (PreparedStatement statement = con.prepareStatement(insertQuery("player_characters", "user_id", "user_name",
					"world_id", "ip", "is_helper", "is_moderator", "is_administrator"))) {
				statement.setInt(1, userId);
				statement.setString(2, userName);
				statement.setInt(3, worldId);
				statement.setString(4, userIp);
				statement.setBoolean(5, isHelper);
				statement.setBoolean(6, isModerator);
				statement.setBoolean(7, isAdministrator);
				statement.execute();
			}
		});
	}

	public static void removeOnlinePlayer(int userId, int worldId) {
		Server.gameDb.execute(con -> {
			PreparedStatement statement = null;
			try {
				statement = con.prepareStatement("DELETE FROM online_characters WHERE user_id = ? AND world_id = ? LIMIT 1",
						ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				statement.setInt(1, userId);
				statement.setInt(2, worldId);
				statement.execute();
			} finally {
				DatabaseUtils.close(statement);
			}
		});
	}

	public static void clearOnlinePlayers(int worldId) {
		Server.gameDb.execute(con -> {
			PreparedStatement statement = null;
			try {
				statement = con.prepareStatement("DELETE FROM online_characters WHERE world_id = ?",
						ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				statement.setInt(1, worldId);
				statement.execute();
			} finally {
				DatabaseUtils.close(statement);
			}
		});
	}

	public static void updateItems(Player player) {
		/* first delete all the items */
		Server.gameDb.execute(con -> {
			PreparedStatement statement = null;
			try {
				statement = con.prepareStatement("DELETE FROM items WHERE user_id = ?", ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				statement.setInt(1, player.getUserId());
				statement.execute();
			} finally {
				DatabaseUtils.close(statement);
			}

		});
		/* now all them all */
		Server.gameDb.execute(con -> {
			PreparedStatement statement = con
					.prepareStatement("INSERT INTO items (user_id, id, amount, container, slot) VALUES (?, ?, ?, ?, ?)");
			// Put all the required containers into the table.
			putItems(player.getUserId(), "inventory", player.getInventory(), statement);
			putItems(player.getUserId(), "equipment", player.getEquipment(), statement);
			putItems(player.getUserId(), "bank", player.getBank(), statement);
			putItems(player.getUserId(), "lootingbag", player.getLootingBag(), statement);

			// Mark the batch as done, commit it.
			statement.executeBatch();
		});
	}

	private static void putItems(int characterId, String name, ItemContainerG<? extends Item> container,
			PreparedStatement insert) throws SQLException {
		for (int slot = 0; slot < container.getItems().length; slot++) {
			Item item = container.get(slot);

			// Only put items into the table that are non-null, obviously :)
			if (item != null) {
				insert.setInt(1, characterId);
				insert.setInt(2, item.getId());
				insert.setInt(3, item.getAmount());
				insert.setString(4, name);
				insert.setInt(5, slot);

				// Mark the batch entry and continue.
				insert.addBatch();
			}
		}
	}

	/**
	 * Player logging - Any data we find useful from a player basically.
	 */

	public static void logPlayer(Player player) {
		Server.gameDb.execute(con -> {
			try (PreparedStatement statement = con.prepareStatement(
					insertQuery("logs_sessions", "user_id", "user_name", "user_ip", "world_id", "world_stage", "world_type"))) {
				statement.setInt(1, player.getUserId());
				statement.setString(2, player.getName());
				statement.setString(3, player.getIp());
				statement.setInt(4, World.id);
				statement.setString(5, World.stage.name());
				statement.setString(6, World.type.name());
				statement.execute();
			}
		});
		Server.gameDb.execute(con -> {
			PreparedStatement statement = null;
			ResultSet resultSet = null;
			try {
				statement = con.prepareStatement(
						"SELECT * FROM logs_players WHERE user_id = ? AND world_stage = ? AND world_type = ? LIMIT 1",
						ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				statement.setInt(1, player.getUserId());
				statement.setString(2, World.stage.name());
				statement.setString(3, World.type.name());
				resultSet = statement.executeQuery();
				if (!resultSet.next()) {
					resultSet.moveToInsertRow();
					updatePlayerLog(player, resultSet);
					resultSet.insertRow();
				} else {
					updatePlayerLog(player, resultSet);
					resultSet.updateRow();
				}
			} finally {
				DatabaseUtils.close(statement, resultSet);
			}
		});
	}

	private static void updatePlayerLog(Player player, ResultSet resultSet) throws SQLException {
		resultSet.updateInt("user_id", player.getUserId());
		resultSet.updateString("user_name", player.getName());
		resultSet.updateString("world_stage", World.stage.name());
		resultSet.updateString("world_type", World.type.name());
		//
		// long totalWealth = 0;
		//
		// // Bank
		// List<BankItem> bankItems = new ArrayList<>(player.getBank().getCount());
		// for (BankItem item : player.getBank().getItems()) {
		// if (item != null && item.getId() != Bank.BLANK_ID) {
		// bankItems.add(item);
		// totalWealth += getWealth(item);
		// }
		// }
		// resultSet.updateString("bank", toJson(bankItems.toArray(new BankItem[0])));
		//
		// // Inventory
		// List<Item> inventoryItems = new
		// ArrayList<>(player.getInventory().getItems().length);
		// for (Item item : player.getInventory().getItems()) {
		// inventoryItems.add(item); // we want to show empty slots
		// if (item != null)
		// totalWealth += getWealth(item);
		// }
		// resultSet.updateString("inventory", toJson(inventoryItems.toArray(new
		// Item[0])));
		//
		// // Equipment
		// List<Item> equipmentItems = new
		// ArrayList<>(player.getEquipment().getItems().length);
		// for (Item item : player.getEquipment().getItems()) {
		// equipmentItems.add(item); // we want to show empty slots
		// if (item != null)
		// totalWealth += getWealth(item);
		// }
		// resultSet.updateString("equipment", toJson(equipmentItems.toArray(new
		// Item[0])));
		//
		// resultSet.updateLong("total_wealth", totalWealth);
		// resultSet.updateLong("total_play_time", player.playTime * Server.tickMs());
		// resultSet.updateInt("rights", player.getRank());
		// resultSet.updateInt("icon", player.getClientGroup().clientImgId);
		// resultSet.updateInt("ironmode", player.getGameMode().ordinal());
		// resultSet.updateInt("x", player.getAbsX());
		// resultSet.updateInt("z", player.getAbsY());
		// resultSet.updateInt("level", player.getHeight());
		// resultSet.updateInt("wilderness_points", player.wildernessPoints);
	}

	public static void logPvMInstance(int ownerId, String typeName, int cost, long timeCreated, long timeDestroyed) {
		Server.gameDb.execute(con -> {
			try (PreparedStatement statement = con.prepareStatement(insertQuery("logs_instances", "user_id", "instance_type",
					"instance_cost", "time_created", "time_destroyed", "world_id", "world_stage", "world_type"))) {
				statement.setInt(1, ownerId);
				statement.setString(2, typeName);
				statement.setInt(3, cost);
				statement.setTimestamp(4, new Timestamp(timeCreated));
				statement.setTimestamp(5, new Timestamp(timeDestroyed));
				statement.setInt(6, World.id);
				statement.setString(7, World.stage.name());
				statement.setString(8, World.type.name());
				statement.execute();
			}
		});
	}

	public static void logStaffBountyKill(Player player, Player pKilled) {
		Server.gameDb.execute(con -> {
			try (PreparedStatement statement = con
					.prepareStatement(insertQuery("logs_staff_bounty_kills", "killer_name", "killed_name", "killed_group_id"))) {
				statement.setString(1, player.getName());
				statement.setString(2, pKilled.getName());
				statement.setInt(3, 0);
				statement.execute();
			}
		});
	}

	/**
	 * Utils
	 */

	private static String toJson(Item... items) {
		// Be sure this method is never called on the main thread lol
		String json = JsonUtils.GSON_EXPOSE.toJson(items);
		if (items != null) {
			for (Item item : items) {
				if (item != null)
					json = json.replace("\"id\":" + item.getId() + ",",
							"\"id\":" + item.getId() + ",\"name\":\"" + item.getDef().name + "\",");
			}
			json = json.replace(",\"uniqueValue\":0", ""); // save some room in the db?!
		}
		return json;
	}

	private static long getWealth(Item item) {
		if (item.getId() == BLOOD_MONEY)
			return item.getAmount();
		if (item.getId() == COINS_995)
			return item.getAmount();
		if (item.getDef() == null)
			return 0;
		long price = item.getDef().highAlchValue;
		return item.getAmount() * price;
	}

	private static void writeLog(Path directoryPath, String fileName, Map<String, Object> entry) {
		Server.executeAsync(() -> {
			try {
				// Create directory if it doesn't exist
				File directory = directoryPath.toFile();
				if (!directory.exists()) {
					directory.mkdirs();
				}

				ObjectMapper objectMapper = new ObjectMapper();
				String json = objectMapper.writeValueAsString(entry);

				var filePath = directoryPath.resolve(fileName);
				try (FileWriter file = new FileWriter(filePath.toFile(), true)) {
					file.write(json + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
