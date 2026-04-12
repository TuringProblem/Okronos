package tradepost.notify;

import java.math.BigInteger;
import io.ruin.api.utils.MathUtils;
import io.ruin.api.utils.NumberUtils;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import tradepost.*;

public class TradePostNotifyGame {

	public static void register() {
		TradePost.hooks.registerSilentAll((ctx) -> {
			switch (ctx) {
				case TradePost.Hook.OfferPlaced placed -> {
					notifyAdded(placed.player(), placed.offer());
				}
				case TradePost.Hook.ExchangedOffers exchange -> {
					notifyBought(
							exchange.buyer(),
							exchange.item(),
							exchange.totalAmountTransacted(),
							exchange.priceEach(),
							exchange.buyerRemainder());

					notifySold(
							exchange.seller(),
							exchange.item(),
							exchange.totalAmountTransacted(),
							exchange.priceEach());
				}
				default -> {
				}
			}
		});
	}

	private static void notifySold(String ownerUid, int item, BigInteger amount, BigInteger price) {
		var owner = World.getPlayer(ownerUid);
		if (owner == null) {
			return;
		}
		var itemDef = new Item(item).getDef();
		if (MathUtils.gt(1, amount)) {
			owner.sendMessage("[Trade Post] You have sold " + amount + " x "
					+ itemDef.name + " for " + price + " each.");
		} else {
			owner.sendMessage("[Trade Post] You have sold " + itemDef.name + " for " + price + ".");
		}
	}

	private static void notifyBought(String ownerUid, int item, BigInteger amount,
			BigInteger priceEach, BigInteger remainder) {
		var owner = World.getPlayer(ownerUid);
		if (owner == null) {
			return;
		}
		var itemDef = new Item(item).getDef();

		if (MathUtils.gt(1, amount)) {
			owner.sendMessage("[Trade Post] You have bought " + amount + " x	" + itemDef.name + " for "
					+ priceEach + " each.");
		} else {
			owner.sendMessage("[Trade Post] You have bought " + itemDef.name
					+ " for " + priceEach + ".");
		}

		if (MathUtils.gt(0, remainder)) {
			owner.sendMessage(
					"[Trade Post] Remainder of " + remainder + " coins have been added to the coffer.");
		}
	}

	public static void notifyAdded(Player player, TradePostOffer offer) {
		var itemDef = new Item(offer.getItemId()).getDef();
		var coinsAmtTxt = NumberUtils.formatNumber(offer.priceEach());
		var itemAmtTxt = NumberUtils.formatNumber(offer.amount);
		player.sendMessage(
				"[Trade Post] Listed item for buy: " + itemAmtTxt + " x " + itemDef.name +
						" for " + coinsAmtTxt + " coins.");
	}
}
