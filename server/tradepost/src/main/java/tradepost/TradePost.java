package tradepost;



import java.math.BigInteger;
import com.google.errorprone.annotations.CheckReturnValue;
import core.task.Continuation;
import io.ruin.HooksV2;
import io.ruin.api.utils.MathUtils;
import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import tradepost.TradePostOffer.Kind;
import tradepost.db.TradePostHistoriesDb;
import tradepost.db.TradePostOffersDb;
import tradepost.hook.Attributes;

import static core.task.api.API.*;

@Slf4j
@ExtensionMethod(Attributes.class)
public class TradePost {
	public static final HooksV2<Hook> hooks = new HooksV2<>(Hook.class);

	public sealed interface Hook {
		public record OfferPlaced(Player player, TradePostOffer offer) implements Hook {
		}

		public record OfferAborted(Player player, TradePostOffer offer, BigInteger amount,
				BigInteger priceEach) implements Hook {
		}

		public record ExchangedOffers(
				String buyer,
				String seller,
				int item,
				BigInteger priceEach,
				BigInteger totalCoinsTransacted,
				BigInteger totalAmountTransacted,
				BigInteger buyerRemainder) implements Hook {
		}
	}

	// Loads temporary slotted attributes for a player
	public static void loadSlots(Player player) {
		TradePostCache.offersByPlayer(player.uuid()).forEach(it -> {
			var pTradePost = player.tradePost();
			pTradePost.offers[it.slot] = it;
		});
	}

	@CheckReturnValue
	public static Continuation<TradePostOffer> placeOffer(Player player, int _item, int _amount,
			long priceEach,
			TradePostOffer.Kind kind) {
		return placeOffer(player, _item, _amount, BigInteger.valueOf(priceEach), kind);
	}

	// tries to place an offer, may return null
	@CheckReturnValue
	public static Continuation<TradePostOffer> placeOffer(
			Player player,
			int itemId,
			int itemAmount,
			BigInteger priceEach,
			TradePostOffer.Kind kind) {

		return () -> {
			if (itemAmount < 0) {
				player.sendMessage("Are you serious?");
				return null;
			}

			if (MathUtils.gt(priceEach, BigInteger.valueOf(100_000_000_000L))) {
				player.sendMessage("Are you serious?");
				return null;
			}

			var pTradePost = player.tradePost();
			var slot = pTradePost.offersEmptySlot();
			if (slot == -1) {
				player.sendMessage("You have the maximum amount of offers active.");
				return null;
			}

			var amount = BigInteger.valueOf(itemAmount);
			var item = ObjType.get(itemId);
			var itemIdUnnoted = item.unnotedId();

			if (!removeItemsForOffer(player, itemId, kind, priceEach, amount)) {
				return null;
			}

			var price = priceEach.multiply(amount);
			var offer = insert(player, itemIdUnnoted, slot, price, player.uuid(), amount, kind).await();
			if (offer == null) {
				log.warn("Unable to insert an offer: player = {} slot = {}", player.uuid(), slot);
				rollbackItemsForOffer(player, itemId, kind, priceEach, amount);
				return null;
			}

			hooks.handle(new Hook.OfferPlaced(player, offer));
			TradePostProcess.queueAdd(offer);

			pTradePost.offers[offer.slot] = offer;
			pTradePost.inter.refresh(player);
			return offer;
		};
	}

	private static boolean removeItemsForOffer(
			Player player,
			int itemId,
			Kind kind,
			BigInteger priceEach,
			BigInteger amount) {

		var inv = player.getInventory();
		var item = ObjType.get(itemId);
		switch (kind) {

			case Buy -> {
				var totalPrice = priceEach.multiply(amount);
				if (!inv.removeCoinsOrPlatTokens(totalPrice)) {
					player.sendMessage("You do not have enough coins to buy this item.");
					return false;
				}
			}

			case Sell -> {
				if (!item.tradeable) {
					player.sendMessage("You cannot sell " + item.name + ".");
					return false;
				}

				var amountInt = amount.intValueExact();
				if (inv.remove(item.id, amountInt, true) != amountInt) {
					player.sendMessage("You do not have enough items to place this offer.");
					return false;
				}
			}

			default -> {
				player.sendMessage("Unknown offer kind");
				return false;
			}
		}

		return true;
	}

	private static void rollbackItemsForOffer(
			Player player,
			int itemId,
			Kind kind,
			BigInteger priceEach,
			BigInteger amount) {

		var inv = player.getInventory();
		var item = ObjType.get(itemId);
		switch (kind) {

			case Buy -> {
				var totalPrice = priceEach.multiply(amount);
				inv.addCoinsOrPlatTokens(totalPrice);
				player.sendMessage("You have been given back the items.");
			}

			case Sell -> {
				var amountInt = amount.intValueExact();
				inv.addOrSendToBank(item.id, amountInt);
			}

			default -> {
				return;
			}
		}

	}

	@CheckReturnValue
	public static Continuation<TradePostOffer> insert(
			Player player,
			int itemId,
			int slot,
			long price,
			String owner,
			long startAmount,
			Kind kind) {
		return insert(player, itemId, slot, BigInteger.valueOf(price), owner,
				BigInteger.valueOf(startAmount), kind);
	}

	// Inserts new offer onto trade post, along with setting temporary slotted
	// player attribute for the offer.
	@CheckReturnValue
	public static Continuation<TradePostOffer> insert(
			Player player,
			int itemId,
			int slot,
			BigInteger price,
			String owner,
			BigInteger startAmount,
			Kind kind) {

		return () -> {
			var offer = TradePostOffersDb.insert(itemId, slot, price, owner, startAmount, kind).await();
			if (offer != null) {
				TradePostCache.insert(offer);
			}
			return offer;
		};
	}

	// Abort the offer by removing it and giving back the offer's items
	@CheckReturnValue
	public static Continuation.Void abort(Player player, TradePostOffer offer) {
		return () -> {
			if (offer.isEmpty()) {
				player.sendMessage("Someone traded this offer, check the coffer.");
				return;
			}

			var pInv = player.getInventory();

			if (offer.isBuy()) {
				var remainingCoins = offer.priceEach().multiply(offer.amount);
				pInv.addCoinsOrPlatTokens(remainingCoins);
			} else {
				var notedId = ObjType.notedId(offer.getItemId());
				pInv.addOrSendToBank(notedId, offer.amount);
			}

			var amount = offer.amount;
			var priceEach = offer.priceEach();
			remove(offer).await();
			player.tradePost().inter.refresh(player);

			hooks.handle(new Hook.OfferAborted(player, offer, amount, priceEach));
		};
	}

	// Removes the offer from the trade post completelly along with temporary
	// player slotted attribute for that offer.
	@CheckReturnValue
	public static Continuation.Void remove(TradePostOffer offer) {
		return () -> {
			var player = World.getPlayer(offer.owner);
			if (player != null) {
				var pTradePost = player.tradePost();
				pTradePost.offers[offer.slot] = null;
			}

			offer.remove();
			TradePostCache.remove(offer);
			TradePostOffersDb.removeOffer(offer).await();
		};
	}

	// Tries to exchange the coins for an existing offer (aka buy without placing an
	// actual offer).
	@CheckReturnValue
	public static Continuation.Void exchange(Player buyer, TradePostOffer offer, int _amount) {

		return () -> {

			if (offer.kind != Kind.Sell) {
				buyer.sendMessage("Oops... I don't think i can do that.");
				return;
			}

			if (offer.isEmpty()) {
				buyer.sendMessage("That offer has already been sold.");
				return;
			}

			var amount = BigInteger.valueOf(_amount);
			var maximumAmount = amount;
			if (offer.amount.compareTo(amount) <= 0) {
				maximumAmount = offer.amount;
			}

			var priceEach = offer.priceEach();
			var totalPrice = maximumAmount.multiply(offer.priceEach());
			if (!buyer.inventory.removeCoinsOrPlatTokens(totalPrice)) {
				return;
			}

			var totalCoinsAmount = TradePostTaxing.apply(totalPrice);

			offer.amount = offer.amount.subtract(amount);
			TradePostOffersDb.upsert(offer).await();

			if (offer.isEmpty()) {
				remove(offer).await();
			}

			exchanged(buyer.uuid(), offer.owner,
					offer.itemId, maximumAmount, BigInteger.ZERO, priceEach, totalCoinsAmount);
		};
	}

	// Exchanges two valid offers with each other
	@CheckReturnValue
	public static Continuation.Void exchange(
			TradePostOffer buy,
			TradePostOffer sell,
			BigInteger amount) {

		return () -> {

			if (sell.kind != Kind.Sell) {
				return;
			}

			if (buy.kind != Kind.Buy) {
				return;
			}

			if (buy.isEmpty()) {
				return;
			}

			if (sell.isEmpty()) {
				return;
			}

			if (MathUtils.gt(sell.priceEach(), buy.priceEach())) {
				throw new IllegalStateException("Selling for more than buy offer");
			}

			var amountTransacted = amount;
			if (MathUtils.lt(sell.amount, amountTransacted)) {
				amountTransacted = sell.amount;
			}

			if (MathUtils.lt(buy.amount, amountTransacted)) {
				amountTransacted = buy.amount;
			}

			var priceEach = buy.priceEach();
			if (MathUtils.lt(sell.priceEach(), buy.priceEach())) {
				priceEach = sell.priceEach();
			}

			var buyerRemainder = buy.priceEach()
					.multiply(amountTransacted)
					.subtract(priceEach.multiply(amountTransacted));

			sell.amount = sell.amount.subtract(amountTransacted);
			buy.amount = buy.amount.subtract(amountTransacted);

			TradePostOffersDb.upsert(buy).await();
			TradePostOffersDb.upsert(sell).await();

			var totalCoinsAmount = finalCoinsAmount(amount, priceEach);

			if (buy.isEmpty()) {
				remove(buy).await();
			}

			if (sell.isEmpty()) {
				remove(sell).await();
			}

			exchanged(
					buy.owner, sell.owner,
					sell.itemId, amountTransacted, buyerRemainder, priceEach,
					totalCoinsAmount);
		};
	}

	// When exchange finalized
	private static void exchanged(
			String buyerUid, String sellerUid,
			int itemId, BigInteger amount, BigInteger buyerRemainder, BigInteger priceEach,
			BigInteger finalCoinsAmount) {

		giveItemsToBuyer(buyerUid, itemId, amount, buyerRemainder);
		giveItemsToSeller(sellerUid, finalCoinsAmount);

		hooks.handle(new Hook.ExchangedOffers(
				buyerUid, sellerUid, itemId, priceEach, finalCoinsAmount, amount, buyerRemainder));

		var item = new Item(itemId, amount.intValue());
		TradePostHistoriesDb.append(buyerUid, sellerUid, TradePostOffer.Kind.Sell, item, priceEach);
		TradePostHistoriesDb.append(sellerUid, buyerUid, TradePostOffer.Kind.Buy, item, priceEach);

		var pBuyer = World.getPlayer(buyerUid);
		if (pBuyer != null) {
			pBuyer.tradePost().inter.refresh(pBuyer);
		}

		var pSeller = World.getPlayer(sellerUid);
		if (pSeller != null) {
			pSeller.tradePost().inter.refresh(pSeller);
		}
	}

	private static BigInteger finalCoinsAmount(BigInteger itemAmount, BigInteger priceEach) {
		var totalPrice = itemAmount.multiply(priceEach);
		totalPrice = TradePostTaxing.apply(totalPrice);
		return totalPrice;
	}

	private static void giveItemsToSeller(String sellerUid, BigInteger totalCoinsAmount) {
		TradePostCoffer.addCoins(sellerUid, totalCoinsAmount);
	}

	private static void giveItemsToBuyer(String buyerUid, int item, BigInteger amount,
			BigInteger coinsRemainder) {
		TradePostCoffer.addCoins(buyerUid, coinsRemainder);
		TradePostCoffer.add(buyerUid, item, amount);
	}
}
