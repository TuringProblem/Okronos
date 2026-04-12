package tradepost.db;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Comparator;

import core.task.Continuations;
import io.ruin.Server;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import tradepost.TradePostHistory;
import tradepost.TradePostOffer;
import tradepost.hook.Attributes;

@Slf4j
@ExtensionMethod(Attributes.class)
public class TradePostHistoriesDb {

	public static void load(Player player) {
		Continuations.schedule(() -> {
			Server.gameDb.exec(con -> {
				try (var statement =
						con.prepareStatement("SELECT * FROM tradepost_history WHERE player_name = ?")) {
					statement.setString(1, player.getName());
					statement.execute();
					try (var rs = statement.getResultSet()) {
						while (rs.next()) {
							var itemId = rs.getInt("item_id");
							var amount = rs.getInt("item_amount");
							var price = new BigInteger(rs.getString("price"), 10);
							var type = rs.getInt("type");
							var seller = rs.getString("seller");
							long age = rs.getLong("age");
							player.tradePost().history
									.add(new TradePostHistory(type, new Item(itemId, amount), price, seller, age));
						}
					}
				}
			}).await();
			player.tradePost().history
					.sort(Comparator.comparingLong(TradePostHistory::getAge).reversed());
		});

	}

	public static void append(String name, String otherName, TradePostOffer.Kind offerType, Item item,
			BigInteger priceEach) {
		var player = World.getPlayer(name);
		if (player != null) {
			var type = offerType == TradePostOffer.Kind.Buy ? 1 : 0;
			player.tradePost().history
					.add(new TradePostHistory(type, item, priceEach, otherName,
							Instant.now().getEpochSecond()));
			player.tradePost().history
					.sort(Comparator.comparingLong(TradePostHistory::getAge).reversed());
		}

		Server.gameDb.execute(con -> {
			var sql =
					"INSERT INTO tradepost_history (player_name, item_id, item_amount, price, type, seller, age) VALUES (?, ?, ?, ?, ?, ?, ?)";
			var statement = con.prepareStatement(sql);
			statement.setString(1, name);
			statement.setInt(2, item.getId());
			statement.setInt(3, item.getAmount());
			statement.setString(4, priceEach.toString(10));
			statement.setInt(5, offerType == TradePostOffer.Kind.Buy ? 1 : 0);
			statement.setString(6, otherName);
			statement.setLong(7, Instant.now().getEpochSecond());
			statement.executeUpdate();

			// try (var statement = con.prepareStatement(
			// 		"DELETE FROM tradepost_history WHERE player_name = ? " +
			// 				"AND age < (SELECT age FROM (SELECT age FROM tradepost_history " +
			// 				"WHERE player_name = ? ORDER BY age DESC LIMIT 1 OFFSET 50) AS subquery)")) {
			// 	statement.setString(1, name);
			// 	statement.setString(2, name);
			// 	int deletedRows = statement.executeUpdate();
			// } catch (Exception e) {
			// 	log.error("Unable to clean history.", e);
			// }

		});
	}
}
