package tradepost.db;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tradepost.TradePostOffer;

public class Mappers {

	public static TradePostOffer from(ResultSet rs) throws SQLException {
		var id = rs.getLong("id");
		var playerName = rs.getString("player_name");
		var slot = rs.getInt("slot");
		var itemId = rs.getInt("item_id");
		var itemAmount = rs.getString("item_amount");
		var price = rs.getString("price");
		var timestamp = rs.getLong("timestamp");
		var buy = rs.getBoolean("buy");
		var startAmount = rs.getString("start_amount");

		var offer = new TradePostOffer();
		offer.id = id;
		offer.slot = slot;
		offer.itemId = itemId;
		offer.amount = new BigInteger(itemAmount, 10);
		offer.timeStamp = timestamp;
		offer.price = new BigInteger(price, 10);
		offer.owner = playerName;
		offer.startAmount = new BigInteger(startAmount, 10);
		offer.kind = buy ? TradePostOffer.Kind.Buy : TradePostOffer.Kind.Sell;
		return offer;
	}

	public static List<TradePostOffer> fromList(ResultSet rs) throws SQLException {
		var result = new ArrayList<TradePostOffer>();
		while (rs.next()) {
			result.add(Mappers.from(rs));
		}
		return result;
	}
}
