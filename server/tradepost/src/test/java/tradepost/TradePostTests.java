package tradepost;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static core.task.api.API.*;
import core.task.test.ContinuationTestExecutor;
import io.ruin.cache.ItemID;
import io.ruin.test.ServerTest;
import lombok.extern.slf4j.Slf4j;
import tradepost.db.TradePostOffersDb;
import tradepost.module.Module;

@Slf4j
public class TradePostTests {

	@BeforeAll
	static void setup() {
		ServerTest.start();
		clearState();
		ServerTest.registerModule(Module.class);
		ServerTest.startModules();
		System.out.println(Arrays.toString(TradePost.Hook.class.getPermittedSubclasses()));

		TradePost.hooks.registerSilentAll((ctx) -> {
			log.info("{}", ctx);
		});
	}

	@AfterAll
	static void shutdown() {
		ServerTest.shutdown();
	}

	@BeforeEach
	void cleanup() {
		clearState();
	}

	@AfterEach
	void cleanupAfter() {
		clearState();
	}


	static void clearState() {
		queue(TradePostOffersDb.prune());
		ContinuationTestExecutor.await();
		TradePostCache.removeAll();
	}

	@Test
	void populatingMultipleSlotsWorks() {
		var player = ServerTest.createPlayer();
		var pInv = player.getInventory();
		pInv.add(995, 10);

		// NOTE: processing 2 inserts on same tick will result inserting on same slot
		// hence the await after each
		for (int i = 0; i < 10; i++) {
			queue(() -> {
				var placed = TradePost.placeOffer(player, 995, 1, 1, TradePostOffer.Kind.Buy).await();
				assertNotNull(placed);
			});
			ContinuationTestExecutor.await();
		}
		assertEquals(10, TradePostCache.offersCount());
		assertFalse(pInv.contains(995));
	}

	@Test
	void insertWorks() {
		var player = ServerTest.createPlayer();
		player.getInventory().add(995, 1);
		queue(TradePost.placeOffer(player, 995, 1, 1, TradePostOffer.Kind.Buy));
		ContinuationTestExecutor.await();
		assertEquals(1, TradePostCache.offersCount());
		assertFalse(player.getInventory().contains(995));
	}

	@Test
	void rollbackItemsOnTransactionFailureWorks() {
		var player = ServerTest.createPlayer();
		var pInv = player.getInventory();
		pInv.add(995, 1);

		var itemId = 995;
		var kind = TradePostOffer.Kind.Buy;
		var slot = 0;
		var owner = player.uuid();

		// assume offer exists
		queue(TradePostOffersDb.insert(itemId, slot, 1, owner, 1, kind));
		ContinuationTestExecutor.await();

		// try place offer
		queue(() -> {
			var placed = TradePost.placeOffer(player, 995, 1, 1, kind).await();
			assertNull(placed);
		});
		ContinuationTestExecutor.await();

		// coins should be rolled back
		assertEquals(1, pInv.getAmount(995));
	}

	@Test
	void abortWorks() {
		var player = ServerTest.createPlayer();
		player.getInventory().add(995, 1);
		queue(() -> {
			var offer = TradePost.placeOffer(player, 995, 1, 1, TradePostOffer.Kind.Buy).await();
			TradePost.abort(player, offer).await();
		});
		ContinuationTestExecutor.await();
		assertEquals(0, TradePostCache.offersCount());

		queue(() -> {
			assertEquals(0, TradePostOffersDb.offersCount(player.uuid()).await());
		});

		ContinuationTestExecutor.await();
	}

	@Test
	void simpleBuySellWorks() {
		var itemId = ItemID.ARMADYL_GODSWORD;

		var buyer = ServerTest.createPlayer();
		var buyerInv = buyer.getInventory();
		buyerInv.add(995, 1);

		var seller = ServerTest.createPlayer();
		var sellerInv = seller.getInventory();
		sellerInv.add(itemId, 1);

		queue(() -> {
			var offer = TradePost.placeOffer(buyer, itemId, 1, 1, TradePostOffer.Kind.Buy).await();
			assertNotNull(offer);
		});

		queue(() -> {
			var offer = TradePost.placeOffer(seller, itemId, 1, 1, TradePostOffer.Kind.Sell).await();
			assertNotNull(offer);
		});

		ContinuationTestExecutor.await();

		assertEquals(0, sellerInv.getAmount(itemId));
		assertEquals(0, buyerInv.getAmount(995));

	}

	@Test
	void givingBackRemainderWorks() {
		var itemId = ItemID.ARMADYL_GODSWORD;

		var buyer = ServerTest.createPlayer();
		var buyerInv = buyer.getInventory();
		buyerInv.add(995, 10);

		var seller = ServerTest.createPlayer();
		var sellerInv = seller.getInventory();
		sellerInv.add(itemId, 1);

		queue(() -> {
			var offer = TradePost.placeOffer(buyer, itemId, 1, 10, TradePostOffer.Kind.Buy).await();
			assertNotNull(offer);
		});

		queue(() -> {
			var offer = TradePost.placeOffer(seller, itemId, 1, 1, TradePostOffer.Kind.Sell).await();
			assertNotNull(offer);
		});

		ContinuationTestExecutor.await();


		assertEquals(BigInteger.valueOf(9), TradePostCoffer.amount(buyer));
	}

	@Test
	void issue319() {
		var itemId = ItemID.ARMADYL_GODSWORD;

		var buyer = ServerTest.createPlayer();
		var buyerInv = buyer.getInventory();
		buyerInv.add(995, 3800);

		var seller = ServerTest.createPlayer();
		var sellerInv = seller.getInventory();
		sellerInv.add(itemId, 1);

		queue(() -> {
			var offer = TradePost.placeOffer(seller, itemId, 1, 3300, TradePostOffer.Kind.Sell).await();
			assertNotNull(offer);
		});

		ContinuationTestExecutor.await();

		queue(() -> {
			var offer = TradePost.placeOffer(buyer, itemId, 1, 3800, TradePostOffer.Kind.Buy).await();
			assertNotNull(offer);
		});

		ContinuationTestExecutor.await();

		assertEquals(BigInteger.valueOf(500), TradePostCoffer.amount(buyer));

		queue(() -> {
			assertEquals(0, TradePostOffersDb.offersCount(seller.uuid()).await());
		});

		ContinuationTestExecutor.await();
	}

	@Test
	void pickingHighestSellOfferWorks() {
		var itemId = ItemID.ARMADYL_GODSWORD;

		var buyer = ServerTest.createPlayer();
		var buyerInv = buyer.getInventory();
		buyerInv.add(995, 10);

		var seller = ServerTest.createPlayer();
		var sellerInv = seller.getInventory();
		sellerInv.add(itemId, 2);

		queue(() -> {
			var offer = TradePost.placeOffer(seller, itemId, 1, 10, TradePostOffer.Kind.Sell).await();
			assertNotNull(offer);
			offer = TradePost.placeOffer(seller, itemId, 1, 1, TradePostOffer.Kind.Sell).await();
			assertNotNull(offer);
		});

		ContinuationTestExecutor.await();

		queue(() -> {
			var offer = TradePost.placeOffer(buyer, itemId, 1, 10, TradePostOffer.Kind.Buy).await();
			assertNotNull(offer);
		});

		ContinuationTestExecutor.await();

		assertEquals(BigInteger.valueOf(9), TradePostCoffer.amount(buyer));

		queue(() -> {
			assertEquals(1, TradePostOffersDb.offersCount(seller.uuid()).await());
		});

		ContinuationTestExecutor.await();
	}

	@Test
	void pickingHighestBuyOfferWorks() {
		var itemId = ItemID.ARMADYL_GODSWORD;

		var buyer = ServerTest.createPlayer();
		var buyerInv = buyer.getInventory();
		buyerInv.add(995, 11);

		var seller = ServerTest.createPlayer();
		var sellerInv = seller.getInventory();
		sellerInv.add(itemId, 2);

		queue(() -> {
			var offer = TradePost.placeOffer(buyer, itemId, 1, 10, TradePostOffer.Kind.Buy).await();
			assertNotNull(offer);
			offer = TradePost.placeOffer(buyer, itemId, 1, 1, TradePostOffer.Kind.Buy).await();
			assertNotNull(offer);
		});

		ContinuationTestExecutor.await();

		queue(() -> {
			var offer = TradePost.placeOffer(seller, itemId, 1, 10, TradePostOffer.Kind.Sell).await();
			assertNotNull(offer);
		});

		ContinuationTestExecutor.await();

		assertEquals(BigInteger.valueOf(10), TradePostCoffer.amount(seller));

		queue(() -> {
			assertEquals(1, TradePostOffersDb.offersCount(buyer.uuid()).await());
			assertEquals(0, TradePostOffersDb.offersCount(seller.uuid()).await());
		});

		ContinuationTestExecutor.await();
	}

	@Test
	void multipleBuyersCompeteForSingleSellOffer() {
		var itemId = ItemID.ARMADYL_GODSWORD;

		var buyer1 = ServerTest.createPlayer();
		var buyer1Inv = buyer1.getInventory();
		buyer1Inv.add(995, 10);

		var buyer2 = ServerTest.createPlayer();
		var buyer2Inv = buyer2.getInventory();
		buyer2Inv.add(995, 10);

		var seller = ServerTest.createPlayer();
		var sellerInv = seller.getInventory();
		sellerInv.add(itemId, 1);

		queue(() -> {
			var offer = TradePost.placeOffer(buyer1, itemId, 1, 10, TradePostOffer.Kind.Buy).await();
			assertNotNull(offer);
			offer = TradePost.placeOffer(buyer2, itemId, 1, 5, TradePostOffer.Kind.Buy).await();
			assertNotNull(offer);
		});

		ContinuationTestExecutor.await();

		queue(() -> {
			var offer = TradePost.placeOffer(seller, itemId, 1, 1, TradePostOffer.Kind.Sell).await();
			assertNotNull(offer);
		});

		ContinuationTestExecutor.await();

		TradePostCoffer.collectAll(buyer1);
		TradePostCoffer.collectAll(buyer2);
		ContinuationTestExecutor.await();

		assertEquals(0, sellerInv.getAmount(itemId));
		assertTrue(buyer1Inv.contains(itemId, true) || buyer2Inv.contains(itemId, true));
	}

	@Test
	void multipleSellersCompeteForSingleBuyOffer() {
		var itemId = ItemID.ARMADYL_GODSWORD;

		var buyer = ServerTest.createPlayer();
		var buyerInv = buyer.getInventory();
		buyerInv.add(995, 20);

		var seller1 = ServerTest.createPlayer();
		var seller1Inv = seller1.getInventory();
		seller1Inv.add(itemId, 1);

		var seller2 = ServerTest.createPlayer();
		var seller2Inv = seller2.getInventory();
		seller2Inv.add(itemId, 1);

		queue(() -> {
			var offer = TradePost.placeOffer(buyer, itemId, 1, 10, TradePostOffer.Kind.Buy).await();
			assertNotNull(offer);
		});

		ContinuationTestExecutor.await();

		queue(() -> {
			var offer = TradePost.placeOffer(seller1, itemId, 1, 1, TradePostOffer.Kind.Sell).await();
			assertNotNull(offer);
			offer = TradePost.placeOffer(seller2, itemId, 1, 1, TradePostOffer.Kind.Sell).await();
			assertNotNull(offer);
		});

		ContinuationTestExecutor.await();
		TradePostCoffer.collectAll(buyer);
		ContinuationTestExecutor.await();

		assertEquals(0, seller1Inv.getAmount(itemId) + seller2Inv.getAmount(itemId));

		assertEquals(1, buyerInv.count(itemId, true));
	}

	@Test
	void cannotBuyWithoutEnoughCoins() {
		var itemId = ItemID.ARMADYL_GODSWORD;

		var buyer = ServerTest.createPlayer();
		var buyerInv = buyer.getInventory(); // no coins

		var seller = ServerTest.createPlayer();
		var sellerInv = seller.getInventory();
		sellerInv.add(itemId, 1);

		queue(() -> {
			var offer = TradePost.placeOffer(buyer, itemId, 1, 1, TradePostOffer.Kind.Buy).await();
			assertNull(offer);
		});

		ContinuationTestExecutor.await();
		assertEquals(1, sellerInv.getAmount(itemId));
	}

	@Test
	void cannotSellWithoutItem() {
		var itemId = ItemID.ARMADYL_GODSWORD;

		var buyer = ServerTest.createPlayer();
		var buyerInv = buyer.getInventory();
		buyerInv.add(995, 10);

		var seller = ServerTest.createPlayer();
		var sellerInv = seller.getInventory(); // no item

		queue(() -> {
			var offer = TradePost.placeOffer(seller, itemId, 1, 1, TradePostOffer.Kind.Sell).await();
			assertNull(offer);
		});

		ContinuationTestExecutor.await();
		assertEquals(0, sellerInv.getAmount(itemId));
	}

	@Test
	void abortReturnsItemsOrCoinsProperly() {
		var itemId = ItemID.ARMADYL_GODSWORD;

		var buyer = ServerTest.createPlayer();
		var buyerInv = buyer.getInventory();
		buyerInv.add(995, 5);

		queue(() -> {
			var offer = TradePost.placeOffer(buyer, itemId, 1, 5, TradePostOffer.Kind.Buy).await();
			assertNotNull(offer);
			TradePost.abort(buyer, offer).await();
		});

		ContinuationTestExecutor.await();

		assertEquals(5, buyerInv.getAmount(995));
		assertEquals(0, TradePostCache.offersCount());
	}

	@Test
	void partialFillLeavesRemainingOffer() {
		var itemId = ItemID.ARMADYL_GODSWORD;

		var buyer = ServerTest.createPlayer();
		var buyerInv = buyer.getInventory();
		buyerInv.add(995, 10);

		var seller = ServerTest.createPlayer();
		var sellerInv = seller.getInventory();
		sellerInv.add(itemId, 1);

		queue(() -> {
			var offer = TradePost.placeOffer(buyer, itemId, 2, 5, TradePostOffer.Kind.Buy).await();
			assertNotNull(offer);
		});

		ContinuationTestExecutor.await();

		queue(() -> {
			var offer = TradePost.placeOffer(seller, itemId, 1, 1, TradePostOffer.Kind.Sell).await();
			assertNotNull(offer);
		});

		ContinuationTestExecutor.await();
		TradePostCoffer.collectAll(buyer);
		ContinuationTestExecutor.await();

		// remaining offer
		assertEquals(1, TradePostCache.offersCount());
		assertEquals(0, sellerInv.getAmount(itemId));
		// buyer got the one
		assertEquals(1, buyerInv.count(itemId, true));
	}

}
