package tradepost;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import io.ruin.Server;
import io.ruin.api.utils.MathUtils;
import lombok.extern.slf4j.Slf4j;
import tradepost.db.Mappers;

@Slf4j
public class TradePostCache {
	private static final List<TradePostOffer> activeOffers = new ArrayList<>();

	public static void removeAll() {
		activeOffers.clear();
	}

	public static void remove(TradePostOffer offer) {
		if (offer == null) {
			throw new IllegalStateException("Tried to remove null offer.");
		}
		activeOffers.remove(offer);
	}

	public static void insert(TradePostOffer offer) {
		if (offer == null) {
			throw new IllegalStateException("Tried to add null offer.");
		}
		activeOffers.add(offer);
	}

	public static void insert(List<TradePostOffer> offers) {
		activeOffers.addAll(offers);
	}

	public static int offersCount() {
		return activeOffers.size();
	}

	public static Stream<TradePostOffer> offers() {
		return activeOffers.stream();
	}

	public static List<TradePostOffer> buyOffersFor(TradePostOffer sellOffer) {
		var owner = sellOffer.owner;
		return activeOffers.stream()
				.filter(o -> !o.isEmpty())
				.filter(o -> o.isBuy())
				.filter(buyOffer -> buyOffer.getItemId() == sellOffer.getItemId())
				.filter(o -> !o.getOwner().equalsIgnoreCase(owner))
				.filter(buyOffer -> MathUtils.gte(buyOffer.priceEach(), sellOffer.priceEach()))
				.sorted(MathUtils.comparing(it -> it.priceEach()))
				.toList();
	}

	public static List<TradePostOffer> sellOffersFor(TradePostOffer offer) {
		var owner = offer.owner;
		var result = activeOffers.stream()
				.filter(o -> !o.isEmpty())
				.filter(o -> o.isSell())
				.filter(sellOffer -> sellOffer.getItemId() == offer.getItemId())
				.filter(o -> !o.getOwner().equalsIgnoreCase(owner))
				.filter(sellOffer -> MathUtils.lte(sellOffer.priceEach(), offer.priceEach()))
				.sorted(Comparator.comparing(it -> it.priceEach()))
				.toList();
		return result;
	}

	public static Stream<TradePostOffer> offersByPlayer(String uuid) {
		return activeOffers.stream().filter(it -> it.owner.equalsIgnoreCase(uuid));
	}

	public static void load() {
		Server.gameDb.executeAwait((connection) -> {
			var query =
					"SELECT id, player_name, slot, item_id, item_amount, price, timestamp, buy, start_amount FROM tradepost_offers";
			var statement = connection.prepareStatement(query);
			var rs = statement.executeQuery();
			TradePostCache.insert(Mappers.fromList(rs));
		});
		activeOffers.sort(Comparator.comparingLong(TradePostOffer::getTimeStamp));
		log.info("Loaded: " + activeOffers.size() + " trade post offers.");
	}
}
