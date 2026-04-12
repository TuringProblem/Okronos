package tradepost.inter;

import io.ruin.Server;
import io.ruin.api.utils.MathUtils;
import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.Color;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.OptionAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;
import io.ruin.model.map.object.actions.ObjectAction;
import lombok.experimental.ExtensionMethod;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import core.task.Continuations;
import tradepost.*;
import tradepost.TradePostOffer.Kind;
import tradepost.db.TradePostOffersDb;
import tradepost.hook.Attributes;
import tradepost.module.Module;

import static core.task.api.API.*;

@ExtensionMethod(Attributes.class)
public class TradePostInterface {
	enum Filter {
		NAME, NAME_REVERSED, PRICE, PRICE_REVERSED, SELLER, SELLER_REVERSED, AGE, AGE_REVERSED
	}

	private static final int MY_OFFERS = 1131;
	private static final int OFFERS = 1132;
	private static final int MY_OFFERS_INVENTORY_WIDGET = 1133;

	public static void register() {
		TradePostCache.load();

		ObjectAction.register(46240, 1, (player, obj) -> {
			var pTradePost = player.tradePost();
			var pTradePostInter = pTradePost.inter;

			if (!Module.ENABLED) {
				player.sendMessage(
						"The trade post workers are currently out for lunch and will be back later.");
				return;
			}

			if (player.getGameMode().isIronMan()) {
				player.sendMessage("Your gamemode prevents you from accessing the trading post!");
				return;
			}

			pTradePostInter.openTradePost(player);
		});

		ObjectAction.register(46240, 2, (player, obj) -> {
			var pTradePost = player.tradePost();
			var pTradePostInter = pTradePost.inter;

			if (!Module.ENABLED) {
				player.sendMessage(
						"The trade post workers are currently out for lunch and will be back later.");
				return;
			}

			if (player.getGameMode().isIronMan()) {
				player.sendMessage("Your gamemode prevents you from accessing the trading post!");
				return;
			}
			player.stringInput("Enter the name of the item you wish to search for.", s -> {
				pTradePostInter.sendPageFromSearch(player, s);
			});
		});

		ObjectAction.register(46240, "Collect Items", (player, obj) -> {
			TradePostCoffer.collectAll(player);
		});

		ObjectAction.register(46240, "Collect Coins", (player, obj) -> {
			TradePostCoffer.collectCoins(player);
		});

		InterfaceHandler.register(MY_OFFERS_INVENTORY_WIDGET, handler -> {
			handler.actions[0] = new InterfaceAction() {
				@Override
				public void handleClick(Player player, int option, int slot, int itemId) {
					Item item = player.getInventory().get(slot);
					if (item.hasAttributes()) {
						player.sendMessage("You can't offer upgraded items on the trading post.");
						return;
					}
					TradePostHandler.promptSellOffer(player, item);
				}
			};
		});

		InterfaceHandler.register(1132, h -> {
			int buyButton = 45;
			for (int i = 0; i < 50; i++) {
				var finalI = i;
				h.actions[buyButton] = (SimpleAction) player -> {
					var pTradePost = player.tradePost();
					var pTradePostInter = pTradePost.inter;
					if (pTradePostInter.inCofferView) {
						pTradePostInter.handleCollect(player, finalI);
						return;
					}
					pTradePostInter.handleBuy(player, finalI);
				};
				buyButton += 11;
			}
			h.actions[612] = (SimpleAction) player -> {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;

				if (pTradePostInter.offerToBuy != null && pTradePostInter.offerToBuyAmt != -1) {
					var offer = pTradePostInter.offerToBuy;
					var offerAmt = pTradePostInter.offerToBuyAmt;
					queue(TradePost.exchange(player, offer, offerAmt));

					pTradePostInter.offerToBuy = null;
					pTradePostInter.offerToBuyAmt = -1;
					player.getPacketSender().setHidden(1132, 593, true);
					pTradePostInter.refreshCurrentOffers(player);
				}
			};

			h.actions[589] = (SimpleAction) player -> {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;

				pTradePostInter.previousPage(player);
			};

			h.actions[614] = (SimpleAction) player -> {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;

				pTradePostInter.refreshCurrentOffers(player);
			};

			h.actions[591] = (SimpleAction) player -> {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;

				pTradePostInter.nextPage(player);
			};

			h.actions[19] = (SimpleAction) player -> {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;

				pTradePostInter.openTradePost(player);
			};

			h.actions[34] = (SimpleAction) player -> {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;

				if (pTradePostInter.currentFilter != null
						&& pTradePostInter.currentFilter == Filter.AGE) {
					pTradePostInter.sendPageFilter(player, Filter.AGE_REVERSED);
				} else {
					pTradePostInter.sendPageFilter(player, Filter.AGE);
				}
			};
			h.actions[33] = (SimpleAction) player -> {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;

				if (pTradePostInter.currentFilter != null
						&& pTradePostInter.currentFilter == Filter.SELLER) {
					pTradePostInter.sendPageFilter(player, Filter.SELLER_REVERSED);
				} else {
					pTradePostInter.sendPageFilter(player, Filter.SELLER);
				}
			};
			h.actions[32] = (SimpleAction) player -> {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;

				if (pTradePostInter.currentFilter != null
						&& pTradePostInter.currentFilter == Filter.PRICE) {
					pTradePostInter.sendPageFilter(player, Filter.PRICE_REVERSED);
				} else {
					pTradePostInter.sendPageFilter(player, Filter.PRICE);
				}
			};
			h.actions[31] = (SimpleAction) player -> {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;

				if (pTradePostInter.currentFilter != null
						&& pTradePostInter.currentFilter == Filter.NAME) {
					pTradePostInter.sendPageFilter(player, Filter.NAME_REVERSED);
				} else {
					pTradePostInter.sendPageFilter(player, Filter.NAME);
				}
			};
		});
		InterfaceHandler.register(1131, h -> {
			h.actions[38] = (OptionAction) (player, opt) -> {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;
				if (opt == 1) {
					pTradePostInter.viewCoffer(player);
				} else if (opt == 2) {
					pTradePostInter.collectCoffer(player);
				}
			};

			h.actions[362] = (SimpleAction) player -> {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;
				pTradePostInter.viewRecentOffers(player);
			};

			h.actions[32] = (SimpleAction) player -> {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;

				player.stringInput("Enter the name of the player you wish to search for.", s -> {
					pTradePostInter.sendPageFromSearchPlayer(player, s);
				});
			};

			h.actions[41] = (SimpleAction) player -> {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;

				player.stringInput("Enter the name of the item you wish to search for.", s -> {
					pTradePostInter.sendPageFromSearch(player, s);
				});
			};

			h.closedAction = (p, i) -> {
				p.closeInterface(ToplevelComponent.INVENTORY_TAB_AREA);
			};

			h.actions[35] = (SimpleAction) player -> {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;

				pTradePostInter.viewHistory(player);
			};

			int buySlots = 54;
			int sellSlots = 56;
			int abortSlots = 75;
			for (int i = 0; i < 10; i++) {
				h.actions[buySlots] = (SimpleAction) player -> {
					TradePostHandler.promptBuyOffer(player);
				};
				h.actions[sellSlots] = (SimpleAction) player -> {
					player.sendMessage("Click an item in your inventory to sell it.");
				};
				int finalI = i;
				h.actions[abortSlots] = (SimpleAction) player -> {
					var pTradePost = player.tradePost();
					var pTradePostInter = pTradePost.inter;
					var pTradePostOffers = pTradePost.offers;
					var offer = pTradePostOffers[finalI];
					if (offer == null) {
						player.sendMessage("No offer selected.");
						return;
					}
					pTradePostInter.promptAbortOffer(player, offer);
				};
				buySlots += 29;
				sellSlots += 29;
				abortSlots += 29;
			}

			h.actions[360] = (SimpleAction) player -> {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;
				var pTradePostOffers = pTradePost.offers;

				if (pTradePostInter.abortOfferIndex == -1) {
					return;
				}

				var offer = pTradePostOffers[pTradePostInter.abortOfferIndex];
				pTradePostInter.abortOfferIndex = -1;
				if (offer == null) {
					player.sendMessage("No offer selected.");
					return;
				}

				queue(() -> {
					TradePost.abort(player, offer).await();
					player.getPacketSender().setHidden(1131, 341, true);
					pTradePostInter.openTradePost(player);
				});

			};
		});
	}

	public static boolean isOpened(Player player) {
		return player.isVisibleInterface(MY_OFFERS)
				|| player.isVisibleInterface(MY_OFFERS_INVENTORY_WIDGET)
				|| player.isVisibleInterface(OFFERS);
	}

	private int mostRecentOfferIndex = -1;
	private int page = -1;
	private int pageSize = -1;

	private boolean inSearch = false;
	private boolean inCofferView = false;
	private boolean inRecentOffers = false;
	private boolean inPlayerSearch = false;
	private boolean inHistory = false;

	private List<TradePostOffer> offers = new ArrayList<>();
	private int abortOfferIndex = -1;
	TradePostOffer offerToBuy = null;
	int offerToBuyAmt = -1;
	List<TradePostOffer> searchResults = new ArrayList<>();
	String currentSearch = "";
	Filter currentFilter = null;

	private void handleCollect(Player player, int index) {
		var pTradePost = player.tradePost();
		if (index >= pTradePost.coffer.size()) {
			return;
		}
		var entry = pTradePost.coffer.get(index);

		Continuations.schedule(() -> {
			TradePostCoffer.collect(entry, player).await();
			if (!player.isOnline()) {
				return;
			}
			this.refresh(player);
		});
	}

	private void handleBuy(Player player, int index) {
		var pTradePost = player.tradePost();
		var pTradePostInter = pTradePost.inter;

		var offer = pTradePostInter.offers.get(index);
		if (offer == null) {
			player.sendMessage("Failed to find listing for this offer.");
			return;
		}

		player.integerInput("How many would you like to buy?", _amt -> {
			if (_amt < 0) {
				player.sendMessage("You must buy at least 1.");
				return;
			}

			var amt = BigInteger.valueOf(_amt);
			var offerPrice = offer.priceEach();

			if (MathUtils.gt(amt, offer.amount)) {
				amt = offer.amount;
			}

			var pInv = player.getInventory();

			var totalPrice = offerPrice.multiply(amt);
			var buyersCoins = BigInteger.valueOf(pInv.getAmount(995))
					.add(BigInteger.valueOf(pInv.getAmount(13204) * 1000L));

			if (MathUtils.lt(buyersCoins, totalPrice)) {
				player.sendMessage("You do not have enough coins to buy this many.");
				return;
			}

			var finalAmt = amt.intValueExact();
			player.getPacketSender().setHidden(1132, 593, false);
			player.getPacketSender().sendString(1132, 611, "Buy " + finalAmt + "x "
					+ new Item(offer.getItemId()).getDef().name + " for "
					+ NumberUtils.formatCoins(totalPrice)
					+ " coins?");
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					OFFERS << 16 | 610, 748,
					4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(
					-1,
					610,
					748,
					new Item(offer.getItemId(), finalAmt));
			pTradePostInter.offerToBuy = offer;
			pTradePostInter.offerToBuyAmt = finalAmt;
		});

	}

	public void viewHistory(Player player) {
		var ps = player.getPacketSender();

		inHistory = true;
		inCofferView = false;
		inRecentOffers = true;
		inPlayerSearch = false;

		player.openInterface(ToplevelComponent.MAINMODAL, 1132);
		offerToBuy = null;
		offerToBuyAmt = -1;
		ps.setHidden(1132, 593, true);
		int compId = 38;
		int buyButton = 45;
		for (int i = 0; i < 50; i++) {
			ps.setHidden(OFFERS, compId, true);
			ps.setHidden(OFFERS, buyButton, true);
			compId += 11;
			buyButton += 11;
		}
		ps.setHidden(1132, 589, true);
		ps.setHidden(1132, 591, true);
		ps.setHidden(1132, 31, true);
		ps.setHidden(1132, 32, true);
		ps.setHidden(1132, 33, true);
		ps.setHidden(1132, 34, true);
		int index = 0;
		int startingContainerId = 597;
		int startingItemComponentId = 43;
		int startingNameComponentId = 42;
		int startingPriceComponentId = 44;
		int sellerComponentId = 47;
		int ageComponentId = 48;
		int parentComponentId = 38;
		var pTradePost = player.tradePost();
		for (var history : pTradePost.history) {
			ps.setHidden(OFFERS, parentComponentId, false);
			ps.sendString(OFFERS, startingNameComponentId, history.getItem().getDef().name);
			ps.sendString(OFFERS, startingPriceComponentId, NumberUtils.formatCoins(history.getPrice()));
			ps.sendString(OFFERS, sellerComponentId, history.getSeller());
			ps.sendString(OFFERS, ageComponentId, getTimeAgo(history.getAge()));
			ps.sendClientScript(149, "IviiiIsssss", OFFERS << 16 | startingItemComponentId,
					startingContainerId, 4, 7, 1, -1,
					"", "", "", "", "");
			ps.sendItems(-1, startingItemComponentId, startingContainerId, history.getItem());
			startingContainerId++;
			startingItemComponentId += 11;
			startingNameComponentId += 11;
			startingPriceComponentId += 11;
			sellerComponentId += 11;
			ageComponentId += 11;
			parentComponentId += 11;
			if (index++ >= 50)
				break;
		}
	}

	public void viewRecentOffers(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, 1132);
		player.getPacketSender().setHidden(1132, 593, true);
		offerToBuy = null;
		offerToBuyAmt = -1;
		mostRecentOfferIndex = Math.max(0, TradePostCache.offersCount() - 10);
		System.out.println(mostRecentOfferIndex);
		page = 1;
		sendPage(player);

		inRecentOffers = true;
		inSearch = false;
		inPlayerSearch = false;
		inCofferView = false;
		inHistory = false;
	}

	public void viewCoffer(Player player) {
		var ps = player.getPacketSender();

		inCofferView = true;
		inSearch = false;
		inRecentOffers = false;
		inPlayerSearch = false;
		inHistory = false;

		player.openInterface(ToplevelComponent.MAINMODAL, 1132);
		offerToBuy = null;
		offerToBuyAmt = -1;
		ps.setHidden(1132, 593, true);
		ps.setHidden(1132, 27, true);
		ps.setHidden(1132, 28, true);
		ps.setHidden(1132, 29, true);
		ps.setHidden(1132, 30, true);
		int compId = 38;
		int buyButton = 46;
		ps.setHidden(OFFERS, 614, true);
		for (int i = 0; i < 50; i++) {
			ps.setHidden(OFFERS, compId, true);
			ps.sendString(OFFERS, buyButton, "Collect");
			compId += 11;
			buyButton += 11;
		}
		ps.setHidden(1132, 589, true);
		ps.setHidden(1132, 591, true);
		ps.setHidden(1132, 31, true);
		ps.setHidden(1132, 32, true);
		ps.setHidden(1132, 33, true);
		ps.setHidden(1132, 34, true);
		int index = 0;
		int startingContainerId = 597;
		int startingItemComponentId = 43;
		int startingNameComponentId = 42;
		int startingPriceComponentId = 44;
		int sellerComponentId = 47;
		int ageComponentId = 48;
		int parentComponentId = 38;
		var pTradePost = player.tradePost();
		for (var entry : pTradePost.coffer) {
			var item = new Item(ObjType.unnotedId(entry.itemId()), entry.itemAmount());
			ps.setHidden(OFFERS, parentComponentId, false);
			ps.sendString(OFFERS, startingNameComponentId, item.getDef().name);
			ps.sendString(OFFERS, startingPriceComponentId, "");
			ps.sendString(OFFERS, sellerComponentId, "");
			ps.sendString(OFFERS, ageComponentId, "");
			ps.sendClientScript(149, "IviiiIsssss", OFFERS << 16 | startingItemComponentId,
					startingContainerId, 4, 7, 1, -1,
					"", "", "", "", "");
			ps.sendItems(-1, startingItemComponentId, startingContainerId, item);
			startingContainerId++;
			startingItemComponentId += 11;
			startingNameComponentId += 11;
			startingPriceComponentId += 11;
			sellerComponentId += 11;
			ageComponentId += 11;
			parentComponentId += 11;
			if (index++ >= 50)
				break;
		}
	}

	public void refresh(Player player) {
		if (player.isVisibleInterface(1132)) {
			if (this.inCofferView) {
				this.viewCoffer(player);
				return;
			}

			this.refreshCurrentOffers(player);
			return;
		}

		if (!player.isVisibleInterface(MY_OFFERS)) {
			return;
		}

		this.openTradePost(player);
	}

	public void openTradePost(Player player) {
		var pTradePost = player.tradePost();
		var pTradePostCofferCoins = pTradePost.cofferCoins;
		var pTradePostOffers = pTradePost.offers;

		offerToBuy = null;
		offerToBuyAmt = -1;
		inRecentOffers = false;
		inCofferView = false;

		player.openInterface(ToplevelComponent.MAINMODAL, MY_OFFERS);
		player.getPacketSender().sendString(MY_OFFERS, 31,
				String.valueOf(TradePostCache.offersCount()));
		player.getPacketSender().sendString(MY_OFFERS, 29,
				Color.GREEN.wrap(NumberUtils.formatCoins(pTradePostCofferCoins)));
		changeInventoryAccess(player);
		int startingEmpty = 48;
		int startingFull = 58;
		int startingFullContainer = 73;
		int startingContainerId = 732;
		int startingNameContainerId = 74;
		int startingBarContainerId = 67;
		int startingBarShadowContainerId = 68;
		int startingBarStringComponent = 69;
		int startingPriceComponent = 76;
		int titleString = 63;
		final int WIDTH_SCRIPT = 10606;

		for (int i = 0; i < pTradePostOffers.length; i++) {
			TradePostOffer offer = pTradePostOffers[i];
			if (offer == null) {
				player.getPacketSender().setHidden(MY_OFFERS, startingEmpty, false);
				player.getPacketSender().setHidden(MY_OFFERS, startingFull, true);
			} else {
				int barInterfaceHash = 1131 << 16 | startingBarContainerId;
				int offerBought = offer.getStartAmount() - offer.getAmount();
				float width = (float) offerBought / offer.getStartAmount();
				float barWidth = (width * 204);
				if (offerBought == 0)
					barWidth = 0;
				player.getPacketSender().sendString(MY_OFFERS, startingBarStringComponent,
						offerBought + "/" + offer.getStartAmount());
				player.getPacketSender().sendString(MY_OFFERS, startingPriceComponent,
						NumberUtils.formatCoins(offer.priceEach()) + " coins each");
				player.getPacketSender().sendClientScript(WIDTH_SCRIPT, "Ii", barInterfaceHash,
						(int) barWidth);
				player.getPacketSender().sendString(MY_OFFERS, titleString,
						offer.isSell() ? "Sell" : "Buy");
				int barInterfaceHash2 = 1131 << 16 | startingBarShadowContainerId;
				player.getPacketSender().sendClientScript(WIDTH_SCRIPT, "Ii", barInterfaceHash2,
						(int) barWidth);
				player.getPacketSender().setHidden(MY_OFFERS, startingEmpty, true);
				player.getPacketSender().setHidden(MY_OFFERS, startingFull, false);
				player.getPacketSender().sendString(MY_OFFERS, startingNameContainerId,
						new Item(offer.getItemId()).getDef().name);
				player.getPacketSender().sendClientScript(
						149, "IviiiIsssss",
						MY_OFFERS << 16 | startingFullContainer, startingContainerId,
						4, 7, 1, -1, "", "", "", "", "");
				player.getPacketSender().sendItems(
						-1,
						startingFullContainer,
						startingContainerId,
						new Item(offer.getItemId(), offer.getAmount()));
			}
			startingEmpty += 29;
			startingFull += 29;
			startingFullContainer += 29;
			startingContainerId++;
			startingNameContainerId += 29;
			startingBarContainerId += 29;
			startingBarShadowContainerId += 29;
			startingBarStringComponent += 29;
			startingPriceComponent += 29;
			titleString += 29;
		}
	}

	private void changeInventoryAccess(Player player) {
		if (player.isVisibleInterface(MY_OFFERS_INVENTORY_WIDGET)) {
			return;
		}
		player.openInterface(ToplevelComponent.INVENTORY_TAB_AREA, MY_OFFERS_INVENTORY_WIDGET);
		player.getPacketSender().sendIfEvents(MY_OFFERS_INVENTORY_WIDGET, 0, 0, 27, 1086);
	}

	private void previousPage(Player player) {
		if (page == 1 || inPlayerSearch) {
			player.sendMessage("You are already on the first page.");
			return;
		}
		page--;
		mostRecentOfferIndex += pageSize;
		if (!inSearch)
			sendPage(player);
		else
			sendSearchPage(player);
	}

	private void nextPage(Player player) {
		if (mostRecentOfferIndex < 0 || inPlayerSearch) {
			player.sendMessage("You are already on the last page.");
			return;
		}
		page++;
		if (!inSearch)
			sendPage(player);
		else
			sendSearchPage(player);
	}

	private void collectCoffer(Player player) {
		var pTradePost = player.tradePost();
		if (pTradePost.cofferCoins.equals(BigInteger.ZERO)) {
			player.sendMessage("You have no coins in your coffer.");
			return;
		}
		TradePostHandler.promptRemoveFromCoffer(player);
	}

	private void sendPageFromSearch(Player player, String search) {
		inSearch = true;

		currentSearch = search;
		searchResults = TradePostCache.offers()
				.filter(o -> o.isSell())
				.filter(buyOffer -> new Item(buyOffer.getItemId()).getDef().name.toLowerCase()
						.contains(search.toLowerCase()))
				.filter(buyOffer -> !buyOffer.getOwner().equalsIgnoreCase(player.getName()))
				.toList();

		mostRecentOfferIndex = searchResults.size() - 1;
		player.openInterface(ToplevelComponent.MAINMODAL, OFFERS);
		player.getPacketSender().setHidden(1132, 593, true);
		offerToBuy = null;
		offerToBuyAmt = -1;
		sendSearchPage(player);
	}

	private void refreshCurrentOffers(Player player) {
		if (inRecentOffers) {
			viewRecentOffers(player);
		} else if (inHistory) {
			viewHistory(player);
		} else if (currentFilter != null) {
			sendPageFilter(player, currentFilter);
		} else if (inSearch) {
			sendPageFromSearch(player, currentSearch);
		} else if (inPlayerSearch) {
			sendPageFromSearchPlayer(player, currentSearch);
		} else {
			sendPage(player);
		}
	}

	private void sendPageFromSearchPlayer(Player player, String playerName) {
		currentSearch = playerName;
		if (player.getName().equalsIgnoreCase(playerName)) {
			player.sendMessage("You cannot search for your own offers.");
			return;
		}
		searchResults = TradePostCache.offers()
				.filter(o -> o.isSell())
				.filter(o -> o.getOwner().equalsIgnoreCase(playerName)).toList();

		mostRecentOfferIndex = searchResults.size() - 1;
		inPlayerSearch = true;
		inCofferView = false;
		inRecentOffers = false;
		player.openInterface(ToplevelComponent.MAINMODAL, OFFERS);
		sendSearchPage(player);
	}

	private void sendSearchPage(Player player) {

		int compId = 38;
		int buyButton = 45;
		for (int i = 0; i < 50; i++) {
			player.getPacketSender().setHidden(OFFERS, compId, true);
			player.getPacketSender().setHidden(OFFERS, buyButton, false);
			compId += 11;
			buyButton += 11;
		}
		pageSize = 0;
		player.getPacketSender().setHidden(1132, 589, false);
		player.getPacketSender().setHidden(1132, 591, false);
		player.getPacketSender().setHidden(1132, 31, false);
		player.getPacketSender().setHidden(1132, 32, false);
		player.getPacketSender().setHidden(1132, 33, false);
		player.getPacketSender().setHidden(1132, 34, false);

		offers.clear();
		int startingContainerId = 832;
		int startingItemComponentId = 43;
		int startingNameComponentId = 42;
		int startingPriceComponentId = 44;
		int sellerComponentId = 47;
		int ageComponentId = 48;
		int parentComponentId = 38;
		for (int i = 0; i < 50; i++) {
			if (mostRecentOfferIndex < 0)
				break;
			TradePostOffer offer = searchResults.get(mostRecentOfferIndex);
			offers.add(offer);
			player.getPacketSender().setHidden(OFFERS, parentComponentId, false);
			player.getPacketSender().sendString(OFFERS, startingNameComponentId,
					new Item(offer.getItemId()).getDef().name);
			player.getPacketSender().sendString(OFFERS, startingPriceComponentId,
					NumberUtils.formatCoins(offer.priceEach()));
			player.getPacketSender().sendString(OFFERS, sellerComponentId, offer.getOwner());
			player.getPacketSender().sendString(OFFERS, ageComponentId, getTimeAgo(offer.getTimeStamp()));
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					OFFERS << 16 | startingItemComponentId, startingContainerId,
					4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(
					-1,
					startingItemComponentId,
					startingContainerId,
					new Item(offer.getItemId(), offer.getAmount()));
			pageSize++;
			mostRecentOfferIndex--;
			startingContainerId++;
			startingItemComponentId += 11;
			startingNameComponentId += 11;
			startingPriceComponentId += 11;
			sellerComponentId += 11;
			ageComponentId += 11;
			parentComponentId += 11;
		}
	}

	private void sendPageFilter(Player player, Filter filter) {
		currentFilter = filter;
		switch (filter) {
			case NAME -> {
				if (searchResults == null) {
					var offers = new ArrayList<>(TradePostCache.offers()
							.filter(o -> o.isSell())
							.filter(buyOffer -> !buyOffer.getOwner().equalsIgnoreCase(player.getName()))
							.toList());

					offers.sort(
							Comparator.comparing(obj -> new Item(obj.getItemId()).getDef().name.toLowerCase()));
					sendFilteredPage(player, offers);
				} else {
					List<TradePostOffer> sortedResults = new ArrayList<>(searchResults);
					sortedResults.sort(
							Comparator.comparing(obj -> new Item(obj.getItemId()).getDef().name.toLowerCase()));
					searchResults = sortedResults;
					mostRecentOfferIndex = searchResults.size() - 1;
					inPlayerSearch = true;
					sendSearchPage(player);

				}
			}
			case NAME_REVERSED -> {
				if (searchResults == null) {
					List<TradePostOffer> offers = new ArrayList<>(TradePostCache.offers()
							.filter(o -> o.isSell())
							.filter(buyOffer -> !buyOffer.getOwner().equalsIgnoreCase(player.getName()))
							.toList());
					offers.sort(
							Comparator.comparing(obj -> new Item(obj.getItemId()).getDef().name.toLowerCase()));
					offers.reversed();
					sendFilteredPage(player, offers);
				} else {
					List<TradePostOffer> sortedResults = new ArrayList<>(searchResults);
					sortedResults.sort(
							Comparator.comparing(obj -> new Item(obj.getItemId()).getDef().name.toLowerCase()));
					sortedResults.reversed();
					searchResults = sortedResults;
					mostRecentOfferIndex = searchResults.size() - 1;
					inPlayerSearch = true;
					sendSearchPage(player);

				}
			}
			case AGE -> {
				if (searchResults == null) {
					List<TradePostOffer> offers = new ArrayList<>(TradePostCache.offers()
							.filter(o -> o.isSell())
							.filter(buyOffer -> !buyOffer.getOwner().equalsIgnoreCase(player.getName()))
							.toList());
					offers.sort(Comparator.comparingLong(TradePostOffer::getTimeStamp));
					sendFilteredPage(player, offers);
				} else {
					List<TradePostOffer> sortedResults = new ArrayList<>(searchResults);
					sortedResults.sort(Comparator.comparingLong(TradePostOffer::getTimeStamp));
					searchResults = sortedResults;
					mostRecentOfferIndex = searchResults.size() - 1;
					inPlayerSearch = true;
					sendSearchPage(player);
				}
			}
			case AGE_REVERSED -> {
				if (searchResults == null) {
					List<TradePostOffer> offers = new ArrayList<>(TradePostCache.offers()
							.filter(o -> o.isSell())
							.filter(buyOffer -> !buyOffer.getOwner().equalsIgnoreCase(player.getName()))
							.toList());
					offers.sort(Comparator.comparingLong(TradePostOffer::getTimeStamp).reversed());
					sendFilteredPage(player, offers);
				} else {
					List<TradePostOffer> sortedResults = new ArrayList<>(searchResults);
					sortedResults.sort(Comparator.comparingLong(TradePostOffer::getTimeStamp).reversed());
					searchResults = sortedResults;
					mostRecentOfferIndex = searchResults.size() - 1;
					inPlayerSearch = true;
					sendSearchPage(player);

				}
			}
			case PRICE -> {
				if (searchResults == null) {
					List<TradePostOffer> offers = new ArrayList<>(TradePostCache.offers()
							.filter(o -> o.isSell())
							.filter(buyOffer -> !buyOffer.getOwner().equalsIgnoreCase(player.getName()))
							.toList());
					offers.sort(MathUtils.comparing(TradePostOffer::priceEach));
					sendFilteredPage(player, offers);
				} else {
					List<TradePostOffer> sortedResults = new ArrayList<>(searchResults);
					sortedResults.sort(MathUtils.comparing(TradePostOffer::priceEach));
					searchResults = sortedResults;
					mostRecentOfferIndex = searchResults.size() - 1;
					inPlayerSearch = true;
					sendSearchPage(player);
				}
			}
			case PRICE_REVERSED -> {
				if (searchResults == null) {
					List<TradePostOffer> offers = new ArrayList<>(TradePostCache.offers()
							.filter(o -> o.isSell())
							.filter(buyOffer -> !buyOffer.getOwner().equalsIgnoreCase(player.getName()))
							.toList());
					offers.sort(MathUtils.comparing(TradePostOffer::priceEach).reversed());
					sendFilteredPage(player, offers);
				} else {
					List<TradePostOffer> sortedResults = searchResults.stream()
							.sorted(MathUtils.comparing(TradePostOffer::priceEach).reversed())
							.collect(Collectors.toList());
					searchResults = sortedResults;
					mostRecentOfferIndex = searchResults.size() - 1;
					inPlayerSearch = true;
					sendSearchPage(player);
				}
			}
			case SELLER -> {
				if (searchResults == null) {
					List<TradePostOffer> offers = new ArrayList<>(TradePostCache.offers()
							.filter(o -> o.isSell())
							.filter(buyOffer -> !buyOffer.getOwner().equalsIgnoreCase(player.getName()))
							.toList());
					offers.sort(Comparator.comparing(obj -> obj.getOwner().toLowerCase()));
					sendFilteredPage(player, offers);
				} else {
					List<TradePostOffer> sortedResults = new ArrayList<>(searchResults);
					sortedResults.sort(Comparator.comparing(obj -> obj.getOwner().toLowerCase()));
					searchResults = sortedResults;
					mostRecentOfferIndex = searchResults.size() - 1;
					inPlayerSearch = true;
					sendSearchPage(player);

				}
			}
			case SELLER_REVERSED -> {
				if (searchResults == null) {
					var offers = new ArrayList<>(TradePostCache.offers()
							.filter(o -> o.isSell())
							.filter(buyOffer -> !buyOffer.getOwner().equalsIgnoreCase(player.getName()))
							.toList());
					offers.sort(Comparator.comparing((TradePostOffer obj) -> obj.getOwner().toLowerCase())
							.reversed());
					sendFilteredPage(player, offers);
				} else {
					List<TradePostOffer> sortedResults = new ArrayList<>(searchResults);
					sortedResults.sort(Comparator
							.comparing((TradePostOffer obj) -> obj.getOwner().toLowerCase()).reversed());
					searchResults = sortedResults;
					mostRecentOfferIndex = searchResults.size() - 1;
					inPlayerSearch = true;
					sendSearchPage(player);
				}
			}
		}
	}

	private void sendFilteredPage(Player player, List<TradePostOffer> offersToSend) {
		/*
		 * Hiding all the offer components, then unhide when sendin g the offer
		 */
		int compId = 38;
		int buyButton = 45;
		for (int i = 0; i < 50; i++) {
			player.getPacketSender().setHidden(OFFERS, compId, true);
			player.getPacketSender().setHidden(OFFERS, buyButton, false);
			compId += 11;
			buyButton += 11;
		}
		pageSize = 0;
		offers.clear();
		player.getPacketSender().setHidden(1132, 589, false);
		player.getPacketSender().setHidden(1132, 591, false);
		player.getPacketSender().setHidden(1132, 31, false);
		player.getPacketSender().setHidden(1132, 32, false);
		player.getPacketSender().setHidden(1132, 33, false);
		player.getPacketSender().setHidden(1132, 34, false);
		int startingContainerId = 832;
		int startingItemComponentId = 43;
		int startingNameComponentId = 42;
		int startingPriceComponentId = 44;
		int sellerComponentId = 47;
		int ageComponentId = 48;
		int parentComponentId = 38;
		for (int i = 0; i < 50; i++) {
			if (mostRecentOfferIndex < 0)
				break;
			var offer = offersToSend.get(mostRecentOfferIndex);
			offers.add(offer);
			player.getPacketSender().setHidden(OFFERS, parentComponentId, false);
			player.getPacketSender().sendString(OFFERS, startingNameComponentId,
					new Item(offer.getItemId()).getDef().name);
			player.getPacketSender().sendString(OFFERS, startingPriceComponentId,
					NumberUtils.formatCoins(offer.price));
			player.getPacketSender().sendString(OFFERS, sellerComponentId, offer.getOwner());
			player.getPacketSender().sendString(OFFERS, ageComponentId, getTimeAgo(offer.getTimeStamp()));
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					OFFERS << 16 | startingItemComponentId, startingContainerId,
					4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(
					-1,
					startingItemComponentId,
					startingContainerId,
					new Item(offer.getItemId(), offer.getAmount()));
			pageSize++;
			mostRecentOfferIndex--;
			startingContainerId++;
			startingItemComponentId += 11;
			startingNameComponentId += 11;
			startingPriceComponentId += 11;
			sellerComponentId += 11;
			ageComponentId += 11;
			parentComponentId += 11;
		}
	}

	private void sendPage(Player player) {
		List<TradePostOffer> offersToSend = new ArrayList<>(TradePostCache.offers()
				.filter(sell -> !sell.getOwner().equalsIgnoreCase(player.getName()))
				.filter(sell -> sell.kind == Kind.Sell)
				.toList());

		/*
		 * Hiding all the offer components, then unhide when sendin g the offer
		 */
		int compId = 38;
		int buyButton = 45;
		for (int i = 0; i < 50; i++) {
			player.getPacketSender().setHidden(OFFERS, compId, true);
			player.getPacketSender().setHidden(OFFERS, buyButton, false);
			compId += 11;
			buyButton += 11;
		}
		pageSize = 0;
		offers.clear();
		player.getPacketSender().setHidden(1132, 589, false);
		player.getPacketSender().setHidden(1132, 591, false);
		player.getPacketSender().setHidden(1132, 31, false);
		player.getPacketSender().setHidden(1132, 32, false);
		player.getPacketSender().setHidden(1132, 33, false);
		player.getPacketSender().setHidden(1132, 34, false);
		int startingContainerId = 832;
		int startingItemComponentId = 43;
		int startingNameComponentId = 42;
		int startingPriceComponentId = 44;
		int sellerComponentId = 47;
		int ageComponentId = 48;
		int parentComponentId = 38;
		for (int i = 0; i < 50; i++) {
			if (mostRecentOfferIndex < 0 || offersToSend.size() <= mostRecentOfferIndex) {
				break;
			}
			var offer = offersToSend.get(mostRecentOfferIndex);
			offers.add(offer);
			player.getPacketSender().setHidden(OFFERS, parentComponentId, false);
			player.getPacketSender().sendString(OFFERS, startingNameComponentId,
					new Item(offer.getItemId()).getDef().name);
			player.getPacketSender().sendString(OFFERS, startingPriceComponentId,
					NumberUtils.formatCoins(offer.price));
			player.getPacketSender().sendString(OFFERS, sellerComponentId, offer.getOwner());
			player.getPacketSender().sendString(OFFERS, ageComponentId, getTimeAgo(offer.getTimeStamp()));
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					OFFERS << 16 | startingItemComponentId, startingContainerId,
					4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(
					-1,
					startingItemComponentId,
					startingContainerId,
					new Item(offer.getItemId(), offer.getAmount()));
			pageSize++;
			mostRecentOfferIndex--;
			startingContainerId++;
			startingItemComponentId += 11;
			startingNameComponentId += 11;
			startingPriceComponentId += 11;
			sellerComponentId += 11;
			ageComponentId += 11;
			parentComponentId += 11;
		}
	}

	private String getTimeAgo(long timeStamp) {
		Instant instant = Instant.ofEpochSecond(timeStamp);
		long secondsAgo = ChronoUnit.SECONDS.between(instant, Instant.now());
		if (secondsAgo < 60)
			return secondsAgo + "s";
		long minutesAgo = secondsAgo / 60;
		if (minutesAgo < 60)
			return minutesAgo + "m";
		long hoursAgo = minutesAgo / 60;
		if (hoursAgo < 24)
			return hoursAgo + "h";
		long daysAgo = hoursAgo / 24;
		if (daysAgo < 7)
			return daysAgo + "d";
		long weeksAgo = daysAgo / 7;
		if (weeksAgo < 4)
			return weeksAgo + "w";
		return "N/A";
	}

	private void promptAbortOffer(Player player, TradePostOffer offer) {
		abortOfferIndex = offer.getSlot();
		player.getPacketSender().setHidden(1131, 341, false);
		player.getPacketSender().sendString(1131, 354, "Abort offer?");
		player.getPacketSender().sendString(1131, 359,
				"Are you sure you want to abort this " + (offer.isSell() ? "sell " : "buy ") + "offer?");
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				MY_OFFERS << 16 | 358, 2000,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				358,
				2000,
				new Item(offer.getItemId(), offer.getAmount()));
	}

}
