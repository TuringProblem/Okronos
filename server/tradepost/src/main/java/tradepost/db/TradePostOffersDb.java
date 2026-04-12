package tradepost.db;

import io.ruin.Server;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import tradepost.*;
import tradepost.hook.Attributes;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Statement;
import java.time.Instant;

import core.task.Continuation;

@Slf4j
@ExtensionMethod(Attributes.class)
public class TradePostOffersDb {

	public static Continuation.Void removeOffer(TradePostOffer offer) {
		return Server.gameDb.exec(con -> {
			try (var statement = con.prepareStatement(
					"DELETE FROM tradepost_offers WHERE player_name = ? AND slot = ?")) {
				statement.setString(1, offer.getOwner());
				statement.setInt(2, offer.getSlot());
				statement.executeUpdate();
			} catch (Exception e) {
				log.error("Unable to remove offer: " + offer, e);
			}
		});
	}

	public static Continuation.Void prune() {
		return Server.gameDb.exec(con -> {
			var stmt = con.prepareStatement("DELETE FROM tradepost_offers");
			stmt.executeUpdate();
		});
	}

	public static Continuation<Long> offersCount(String owner) {
		return Server.gameDb.exec(con -> {
			var sql = """
			SELECT COUNT(*) AS count
			FROM tradepost_offers
			WHERE player_name = ?
					""";
			var stmt = con.prepareStatement(sql);
			stmt.setString(1, owner);
			var rs = stmt.executeQuery();

			if (!rs.next()) {
				throw new IllegalStateException("Nothing on result set.");
			}

			return rs.getLong(1);
		});
	}

	public static Continuation<TradePostOffer> insert(
			int itemId,
			int slot,
			long price,
			String owner,
			long startAmount,
			TradePostOffer.Kind kind) {
		return insert(itemId, slot, BigInteger.valueOf(price), owner, BigInteger.valueOf(startAmount),
				kind);
	}

	public static Continuation<TradePostOffer> insert(
			int itemId,
			int slot,
			BigInteger price,
			String owner,
			BigInteger startAmount,
			TradePostOffer.Kind kind) {
		return Server.gameDb.exec(con -> {
			var sql =
					"""
							INSERT INTO tradepost_offers (player_name, slot, item_id, item_amount, price, timestamp, buy, start_amount) VALUES(?, ?, ?, ?, ?, ?, ?, ?)
							""";
			var stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, owner);
			stmt.setInt(2, slot);
			stmt.setInt(3, itemId);
			stmt.setString(4, startAmount.toString(10));
			stmt.setString(5, price.toString(10));
			stmt.setLong(6, Instant.now().getEpochSecond());
			stmt.setBoolean(7, kind.isBuy());
			stmt.setString(8, startAmount.toString(10));
			stmt.execute();

			var keys = stmt.getGeneratedKeys();
			if (!keys.next()) {
				throw new IllegalStateException("No object id returned.");
			}

			var id = keys.getLong(1);
			var offer = new TradePostOffer();
			offer.id = id;
			offer.itemId = itemId;
			offer.slot = slot;
			offer.price = price;
			offer.startAmount = startAmount;
			offer.amount = startAmount;
			offer.kind = kind;
			offer.owner = owner;
			return offer;
		});
	}

	public static Continuation.Void upsert(TradePostOffer offer) {
		return Server.gameDb.exec(con -> {
			try (var checkStatement = con.prepareStatement(
					"SELECT COUNT(*) FROM tradepost_offers WHERE player_name = ? AND slot = ?")) {
				checkStatement.setString(1, offer.getOwner());
				checkStatement.setInt(2, offer.getSlot());

				try (var result = checkStatement.executeQuery()) {
					if (result.next() && result.getInt(1) == 0) {
						// NOTE: this may actually happen when out of order upsert/remove happens.
						// gamedb.execute does not guarantee order, however it's fine as the local
						// consistency is more important.
						log.warn("[Trade Post] Offer [" + offer + "] does not exist, skipping update.");
						return;
					}
				}
			}
			try (var statement = con.prepareStatement(
					"UPDATE tradepost_offers SET item_amount = ? WHERE player_name = ? AND slot = ?")) {
				statement.setString(1, offer.amount.toString(10));
				statement.setString(2, offer.getOwner());
				statement.setInt(3, offer.getSlot());
				statement.executeUpdate();
			}
		});
	}

}
