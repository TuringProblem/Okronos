package tradepost.notify;

import java.math.BigInteger;

import io.ruin.Server;
import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.services.discord.old.Webhook;
import io.ruin.services.discord.old.util.Embed;
import io.ruin.services.discord.old.util.Footer;
import io.ruin.services.discord.old.util.Message;
import io.ruin.services.discord.old.util.Thumbnail;
import lombok.extern.slf4j.Slf4j;
import properties.ServerProperties;
import tradepost.*;

@Slf4j
public class TradePostNotifyDiscord {

	private static final String DISCORD_HOOK = ServerProperties.get("trade_post_discord_hook", "");

	public static void register() {
		TradePost.hooks.registerSilentAll(ctx -> {
			switch (ctx) {
				case TradePost.Hook.OfferPlaced placed -> {
					var offer = placed.offer();
					notifyPlaced(offer.owner, offer.kind, offer.itemId, offer.amount, offer.priceEach());
				}
				case TradePost.Hook.ExchangedOffers exchanged -> {
					notifyExchange(exchanged.buyer(), exchanged.seller(), exchanged.item(),
							exchanged.totalAmountTransacted(), exchanged.priceEach(),
							exchanged.totalCoinsTransacted(), exchanged.buyerRemainder());
				}
				case TradePost.Hook.OfferAborted aborted -> {
					var offer = aborted.offer();
					notifyAborted(offer.owner, offer.kind, offer.itemId, aborted.amount(),
							aborted.priceEach());
				}
				default -> {
				}
			}
		});
	}

	private static void notifyAborted(String owner, TradePostOffer.Kind kind, int item,
			BigInteger amount, BigInteger priceEach) {
		if (DISCORD_HOOK.isEmpty()) {
			return;
		}

		Server.executeAsync(() -> {
			try {
				Webhook webhook = new Webhook(DISCORD_HOOK);
				Message message = new Message();

				Embed embedMessage = new Embed();

				var itemName = ObjType.get(ObjType.unnotedId(item)).getName();

				var title = "Aborted";
				title += "\n";
				title += "Owner: " + owner;
				title += "\n";
				title += "Item: " + itemName;
				title += "\n";
				title += "Kind: " + kind;
				title += "\n";
				title += "Amount: " + NumberUtils.formatNumber(amount);
				title += "\n";
				title += "Price each: " + NumberUtils.formatCoins(priceEach);

				embedMessage.setTitle(title);
				embedMessage.setDescription("Trade post abortion");
				embedMessage.setColor(8917522);

				Thumbnail thumbnail = new Thumbnail();
				thumbnail.setUrl("https://static.runelite.net/cache/item/icon/" + item + ".png");
				embedMessage.setThumbnail(thumbnail);

				Footer footer = new Footer();
				footer.setText("");
				embedMessage.setFooter(footer);

				message.setEmbeds(embedMessage);
				webhook.sendMessage(message.toJson());
			} catch (Exception e) {
				log.error("Failed to notify discord", e);
			}
		});

	}

	private static void notifyPlaced(String owner, TradePostOffer.Kind kind, int item,
			BigInteger amount, BigInteger priceEach) {
		if (DISCORD_HOOK.isEmpty()) {
			return;
		}

		Server.executeAsync(() -> {
			try {
				Webhook webhook = new Webhook(DISCORD_HOOK);
				Message message = new Message();

				Embed embedMessage = new Embed();

				var itemName = ObjType.get(ObjType.unnotedId(item)).getName();

				var title = "Placed";
				title += "\n";
				title += "Owner: " + owner;
				title += "\n";
				title += "Item: " + itemName;
				title += "\n";
				title += "Kind: " + kind;
				title += "\n";
				title += "Amount: " + NumberUtils.formatNumber(amount);
				title += "\n";
				title += "Price each: " + NumberUtils.formatCoins(priceEach);
				embedMessage.setTitle(title);

				embedMessage.setDescription("Trade post listing");
				embedMessage.setColor(8917522);

				Thumbnail thumbnail = new Thumbnail();
				thumbnail.setUrl("https://static.runelite.net/cache/item/icon/" + item + ".png");
				embedMessage.setThumbnail(thumbnail);

				Footer footer = new Footer();
				footer.setText("");
				embedMessage.setFooter(footer);

				message.setEmbeds(embedMessage);
				webhook.sendMessage(message.toJson());
			} catch (Exception e) {
				log.error("Failed to notify discord", e);
			}
		});

	}

	public static void notifyExchange(String buyer, String seller, int item, BigInteger amount,
			BigInteger priceEach,
			BigInteger finalCoins,
			BigInteger buyerRemainder) {
		if (DISCORD_HOOK.isEmpty()) {
			return;
		}

		Server.executeAsync(() -> {
			try {
				Webhook webhook = new Webhook(DISCORD_HOOK);
				Message message = new Message();

				Embed embedMessage = new Embed();

				var title = "Exchanged";
				title += "\n";
				title += "Item: " + ObjType.unnoted(item).getName();
				title += "\n";
				title += "Amount: " + NumberUtils.formatCoins(amount);
				title += "\n";
				title += "Buyer: " + buyer;
				title += "\n";
				title += "Seller: " + seller;
				title += "\n";
				title += "Price - tax: " + NumberUtils.formatCoins(finalCoins);
				title += "\n";
				title += "Price each: " + NumberUtils.formatCoins(priceEach);
				title += "\n";
				title += "Remainder: " + NumberUtils.formatCoins(buyerRemainder);
				embedMessage.setTitle(title);
				embedMessage.setDescription("Trade post exchange");
				embedMessage.setColor(8917522);

				Thumbnail thumbnail = new Thumbnail();
				thumbnail.setUrl("https://static.runelite.net/cache/item/icon/" + item + ".png");
				embedMessage.setThumbnail(thumbnail);

				Footer footer = new Footer();
				footer.setText("");
				embedMessage.setFooter(footer);

				message.setEmbeds(embedMessage);
				webhook.sendMessage(message.toJson());
			} catch (Exception e) {
				log.error("Failed to send discord embed", e);
			}
		});

	}
}
