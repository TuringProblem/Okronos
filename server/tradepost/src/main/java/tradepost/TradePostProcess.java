package tradepost;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Queue;

import io.ruin.api.utils.MathUtils;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import tradepost.hook.Attributes;

import static core.task.api.API.*;

@Slf4j
@ExtensionMethod(Attributes.class)
public class TradePostProcess {

	private static final Queue<TradePostOffer> pendingQueue = new ArrayDeque<>();

	public static void register() {
		queue(() -> {
			mark_infinite_loop();

			while (true) {
				process();
				sleep(1);
			}
		});
	}

	public static void process() {
		while (!pendingQueue.isEmpty()) {
			processNext();
		}
	}

	public static void processNext() {
		var offer = pendingQueue.poll();
		if (offer.isBuy()) {
			processBuy(offer);
		} else {
			processSell(offer);
		}
	}

	public static void queueAdd(TradePostOffer offer) {
		pendingQueue.offer(offer);
	}

	private static void processBuy(TradePostOffer buyOffer) {
		log.trace("Processing buy offer: " + buyOffer);
		var sellOffers = TradePostCache.sellOffersFor(buyOffer);
		var remainingAmount = buyOffer.amount;

		for (var offer : sellOffers) {
			if (buyOffer.isEmpty()) {
				break;
			}
			log.trace("Processing buy offer: " + buyOffer + " with " + offer);
			var amount = MathUtils.min(remainingAmount, offer.amount);
			TradePost.exchange(buyOffer, offer, amount).await();
			remainingAmount = remainingAmount.subtract(amount);
		}
	}

	private static void processSell(TradePostOffer sellOffer) {
		log.trace("Processing sell offer: " + sellOffer);
		var buyOffers = TradePostCache.buyOffersFor(sellOffer);
		var remainingAmount = sellOffer.amount;

		for (var offer : buyOffers) {
			if (sellOffer.isEmpty()) {
				break;
			}
			log.trace("Processing sell offer: " + sellOffer + " with " + offer);
			var amount = MathUtils.min(remainingAmount, offer.amount);
			TradePost.exchange(offer, sellOffer, amount).await();
			remainingAmount = remainingAmount.subtract(amount);
		}
	}
}
