package io.ruin.model.shop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.Lists;
import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.item.Item;
import io.ruin.model.item.containers.ShopItemContainer;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@ToString(exclude = {"viewingPlayers", "shopItems", "currencyHandler"})
@JsonPropertyOrder({"identifier", "title", "currency", "accessibleByIronMan", "accesibleByRegular", "generalStore",
	"canSellToStore", "restockRules", "priceRules", "defaultStock", "requiredAchievements", "requiredLevels"})
@JsonIgnoreProperties({"currencyHandler", "viewingPlayers", "shopItems", "generatedByBuilder", "onTick"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Shop {

	public Shop() {

	}

	@Builder
	private Shop(String identifier, String title, Currency currency, CurrencyHandler currencyHandler,
	             boolean generalStore, boolean canSellToStore, RestockRules restockRules, PriceRules priceRules,
	             List<ShopItem> defaultStock, boolean accessibleByIronMan, boolean accesibleByRegular, boolean generatedByBuilder,
	             Consumer<Shop> onTick) {
		this.identifier = identifier;
		this.title = title;
		this.currency = currency;
		this.currencyHandler = currencyHandler;
		this.generalStore = generalStore;
		this.canSellToStore = canSellToStore;
		this.restockRules = restockRules;
		this.priceRules = priceRules;
		this.defaultStock = defaultStock;
		this.accessibleByIronMan = accessibleByIronMan;
		this.accessibleByRegular = accesibleByRegular;
		this.generatedByBuilder = generatedByBuilder;
		this.onTick = onTick;
	}

	// TODO Adjust price based on stock
	public List<ShopContainerListener> viewingPlayers = Lists.newArrayList();
	public String identifier;
	public String title;

	public boolean generatedByBuilder;

	/**
	 * Represents the shops currency from the pre-defined enum of Currency.
	 * This value can be null if the shop was created via code instead of
	 * deserialized
	 */
	@JsonInclude(JsonInclude.Include.NON_ABSENT)
	public Currency currency;

	public CurrencyHandler currencyHandler;
	public boolean generalStore;
	public boolean canSellToStore = true;
	public RestockRules restockRules;
	public PriceRules priceRules;
	@JsonInclude(JsonInclude.Include.NON_ABSENT)
	public List<ShopItem> defaultStock;
	public ShopItemContainer shopItems;
	public boolean accessibleByIronMan;
	public boolean accessibleByRegular;

	public void init() {
		if (restockRules == null)
			restockRules = RestockRules.generateDefault();
		if (priceRules == null)
			priceRules = PriceRules.generateDefault();

		shopItems = new ShopItemContainer();
		shopItems.init(null, ShopManager.SHOP_MAX_CAPACITY, 300, 16, 24, true);
		if (currency != null)
			currencyHandler = currency.getCurrencyHandler();
	}

	public void populate() {
		shopItems.clear();
		defaultStock.forEach(shopItem -> shopItems.add(shopItem));
		shopItems.forEach(shopItem -> shopItem.defaultStockItem = true);
	}

	public Shop replace(Shop shop) {
		this.title = shop.title;
		this.defaultStock = shop.defaultStock;

		init();
		populate();
		replaceListeners();

		return this;
	}

	public void replaceListeners() {
		viewingPlayers.forEach(listener -> {
			listener.setShopContainer(shopItems);
			listener.open();
		});

	}

	public void open(Player player) {

		if (player.getGameMode().isIronMan() && !accessibleByIronMan) {
			player.sendMessage("You can't access this shop as an ironman!");
			return;
		}
		if (!player.getGameMode().isIronMan() && !accessibleByRegular) {
			player.sendMessage("You can't access this shop as an regular player!");
			return;
		}

		if (player.isVisibleInterface(301))
			player.closeInterface(ToplevelComponent.MAINMODAL);

		player.openInterface(ToplevelComponent.MAINMODAL, 300);
		player.openInterface(ToplevelComponent.SIDEMODAL, 301);

		player.getPacketSender().sendClientScript(1074, "isii", 4, title, -1, 0);
		player.getPacketSender().sendClientScript(149, "iiiiiisssss", 301 << 16, 93, 4, 7, 0, -1, "Value<col=ff9040>",
			"Sell 1<col=ff9040>", "Sell 10<col=ff9040>", "Sell 50<col=ff9040>", "Sell X<col=ff9040>");
		player.getPacketSender().sendClientScript(149, "iiiiiisssss", 300 << 16 | 16, 24, 8, 5, 0, -1, "Value<col=ff9040>",
			"Buy 1<col=ff9040>", "Buy 10<col=ff9040>", "Buy 50<col=ff9040>", "Buy X<col=ff9040>");

		player.getPacketSender().sendIfEvents(300, 16, 0, ShopManager.SHOP_MAX_CAPACITY, 1086);
		player.getPacketSender().sendIfEvents(301, 0, 0, 27, 1086);

		if (!currency.name().equalsIgnoreCase("coins")) {
			player.sendMessage("You currently have " + NumberUtils.formatNumber(currencyHandler.getCurrencyCount(player))
				+ " " + currencyHandler.name + ".");
		}

		ShopContainerListener containerListener = new ShopContainerListener(player);

		containerListener.setShopContainer(shopItems);
		containerListener.open();

		viewingPlayers.add(containerListener);
		player.viewingShop = this;
	}

	public void close(Player player) {
		viewingPlayers.removeIf(listener -> listener.getPlayer() == null || listener.getPlayer().equals(player));
		player.viewingShop = null;
	}

	public void buyFromShop(Player player, Item requestedItem, int amount) {

		if (requestedItem.getAmount() == 0)
			return;
		if (!shopItems.hasId(requestedItem.getId())) {
			// TODO Logging
			return;
		}
		ShopItem shopItem = shopItems.findItem(requestedItem.getId());

		if (player.getGameMode().isIronMan()) {
			if (!shopItem.defaultStockItem) {
				player.sendMessage("You cannot purchase an item somebody else has sold!");
				return;
			} else if (shopItem.getAmount() > defaultStock.get(shopItem.getSlot()).getAmount()) {
				player.sendMessage("You cannot purchase this as it is currently overstocked!");
				return;
			}
		}

		if (shopItem.requirementCheckType == RequirementCheckType.REQUIRED_TO_BUY
			|| this.title.equalsIgnoreCase("Achievement Rewards")) {
			if (!shopItem.hasAllRequirements(player)) {
				shopItem.printRequirements(player);
				return;
			}
		}

		int possibleBuyAmount = currencyHandler.getPossibleBuyAmount(player, shopItem, amount);
		int buyAmount = Math.min(amount, possibleBuyAmount);
		log.debug("Attempting to buy {} x {}", buyAmount, shopItem);
		if (buyAmount > 0) {
			if (!shopItem.defaultStockItem) {
				buyAmount = Math.min(buyAmount, shopItem.getAmount());
			}
			int pricePer = getSellPrice(shopItem);
			int requiredCurrency = buyAmount * pricePer;

			// Limits the buy amount to 10,000
			int buyLimit = 100000;
			if (buyAmount > buyLimit) {
				player.sendMessage("You can only buy up to " + buyLimit + " items at once.");
				buyAmount = buyLimit;
				// Ensures the gp spent is capped at the max of 10,000 items
				requiredCurrency = buyAmount * pricePer;
			}

			int removedCurrency = currencyHandler.removeCurrency(player, requiredCurrency);

			log.debug("Attempting to buy {} x {}", buyAmount, shopItem);
			if (removedCurrency == requiredCurrency) {
				Item bought = new Item(shopItem.getId(), buyAmount);
				player.getInventory().add(bought);
				if (currency == Currency.STARDUST || currency == Currency.PEST_CONTROL_POINTS
					|| currency == Currency.MAGE_ARENA_POINTS || currency == Currency.GOLDEN_NUGGETS
					|| currency == Currency.MARKS_OF_GRACE) {
					player.addToCollectionLog(bought);
				}
				if (shopItem.getAdditionalItems() != null && !shopItem.getAdditionalItems().isEmpty()) {
					int finalBuyAmount = buyAmount;
					shopItem.getAdditionalItems().forEach(additional -> player.getInventory()
						.add(new Item(additional.getId(), finalBuyAmount * additional.getAmount())));
				}
				if (!shopItem.defaultStockItem) {
					shopItems.remove(bought);
				}
				log.debug("Player bought {}", bought);
				if(this.identifier.equalsIgnoreCase("Ys9Fq6EN8jZcfhUVabt1xaDXkTLlZvA7")) {
					player.addToCollectionLog(bought);
				}
				// if(requiredCurrency > 1000000)
				// RareDropEmbedMessage.sendShopLogsToDiscord(player, "Bought " +
				// bought.getAmount() + " " + bought.getDef().name + " for " +
				// NumberUtils.formatNumber(requiredCurrency) + " " + currencyHandler.name() + "
				// each.", true);
				sendUpdates();
			} else {
				log.debug("Failed to buy {} x {} | removedCurrency {}", buyAmount, shopItem.getId(), removedCurrency);
			}
		} else if (buyAmount == -2) {
			player.sendMessage("You don't have enough space to do that!");
		} else {
			player.sendMessage("You don't have enough " + currencyHandler.name() + " to buy that.");
		}
	}

	public void sendUpdates() {
		viewingPlayers.forEach(ShopContainerListener::update);
	}

	public void sellToShop(Player player, Item requestedItem) {
		if (!requestedItem.getDef().tradeable) {
			player.sendMessage(ShopManager.CANNOT_SELL_TO_SHOP);
			return;
		}
		if (requestedItem.getDef().free) {
			player.sendMessage(ShopManager.CANNOT_SELL_TO_SHOP);
			return;
		}

		if (requestedItem.getDef().isCurrency()) {
			player.sendMessage(ShopManager.CANNOT_SELL_TO_SHOP);
			return;
		}

		if (!canSellToStore) {
			player.sendMessage(ShopManager.CANNOT_SELL_TO_SHOP);
			return;
		}

		ShopItem matchingItem = shopItems.findItem(requestedItem.getId(), true);
		if (generalStore && (matchingItem == null || !matchingItem.defaultStockItem)) {
			if (shopItems.getFreeSlots() == 0 && matchingItem == null) {
				player.sendMessage(ShopManager.SHOP_FULL);
				return;
			}

			int inventoryCount = player.getInventory().count(requestedItem.getId());
			int maxInventory = Math.min(requestedItem.getAmount(), inventoryCount);

			int maxSell = Math.min(Integer.MAX_VALUE - (matchingItem != null ? matchingItem.getAmount() : 0), maxInventory);

			int pricePer = getBuyPrice(requestedItem);

			boolean allSold = maxSell == inventoryCount;// guarantees that 1 slot will be open

			boolean hasCurrency = currencyHandler.getCurrencyCount(player) != 0;

			log.debug("{} {} {}", maxSell, maxInventory, currencyHandler.getCurrencyCount(player));
			if (!hasCurrency && !allSold) {
				if (currencyHandler instanceof ItemCurrencyHandler) {
					if (player.getInventory().getFreeSlots() == 0) {
						player.sendMessage("You have no space for your " + currencyHandler.name());
						return;
					}
				}
			}

			// if(currency.getCurrencyHandler() instanceof ItemCurrencyHandler){ // TODO Fix
			// this
			// ItemCurrencyHandler itemCurrencyHandler = (ItemCurrencyHandler)
			// currency.getCurrencyHandler();
			// if(Bank.hasItemAmountLimit(player, itemCurrencyHandler.getCurrencyItemId(),
			// maxSell, p->{
			// p.sendMessage("You have too much " +
			// ItemDef.get(itemCurrencyHandler.getCurrencyItemId()).name + " in your
			// inventory.");
			// }))
			// {
			// return;
			// }
			// }

			int removed = player.getInventory().remove(requestedItem.getId(), maxSell);

			if (removed > 0) {
				int unnoted = requestedItem.getDef().isNote() ? requestedItem.getDef().fromNote().id : requestedItem.getId();
				int givenCurrency = currencyHandler.addCurrency(player, pricePer * removed);
				log.debug("Sold {} x {}, given currency {}", requestedItem.getDef().name, removed, pricePer * removed);
				shopItems.add(unnoted, removed, -1, null);
				// RareDropEmbedMessage.sendShopLogsToDiscord(player, "Sold " + removed + " " +
				// requestedItem.getDef().name + " for " + NumberUtils.formatNumber((long)
				// pricePer * removed) + " " + currencyHandler.name() + " each.", false);
				sendUpdates();
			}

			return;
		}
		log.debug("Trying to sell to store {}", requestedItem);
		if (matchingItem == null) {
			player.sendMessage(ShopManager.CANNOT_SELL_TO_SHOP);
			return;
		}

		int maxInventory = Math.min(requestedItem.getAmount(), player.getInventory().count(requestedItem.getId()));

		int maxSell = Math.min(Integer.MAX_VALUE - matchingItem.getAmount(), maxInventory);

		int pricePer = getBuyPrice(requestedItem);
		int removed = player.getInventory().remove(requestedItem.getId(), maxSell);
		if (removed > 0) {
			int unnoted = requestedItem.getDef().isNote() ? requestedItem.getDef().fromNote().id : requestedItem.getId();
			currencyHandler.addCurrency(player, pricePer * removed);
			log.debug("Sold {} x {}, given currency {}", requestedItem.getDef().name, removed, pricePer * removed);
			shopItems.add(unnoted, removed, -1, null);
			sendUpdates();
		}

	}

	public int getBuyPrice(Item itemForSlot) {
		ObjType itemDef = itemForSlot.getDef().isNote() ? itemForSlot.getDef().fromNote() : itemForSlot.getDef();
		if (!itemDef.tradeable) {
			return -1;
		}
		if (itemDef.free) {
			return -1;
		}

		if (itemDef.isCurrency()) {
			return -1;
		}
		int value = (itemDef.value * priceRules.buysAt) / 100;
		if (generalStore) {
			return value;
		}
		ShopItem matchingItem = shopItems.findItem(itemForSlot.getId(), true);
		if (canSellToStore && matchingItem != null) {
			return value;
		}
		if (matchingItem == null && !generalStore) {
			return -1;
		}

		return 1;
	}

	public int getSellPrice(ShopItem itemForSlot) {

		if (itemForSlot != null) {
			ObjType itemDef = itemForSlot.getDef().isNote() ? itemForSlot.getDef().fromNote() : itemForSlot.getDef();
			int value = (itemDef.value * priceRules.sellsAt) / 100;
			if (itemForSlot.getPrice() <= 0) {
				return value;
			}
			return itemForSlot.getPrice();// Can this ever be null?
		}
		return Integer.MAX_VALUE;// TODO
	}

	public void save(File saveFile) {
		try (FileWriter fw = new FileWriter(saveFile)) {

			ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
			objectMapper.writeValue(fw, this);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public Consumer<Shop> onTick;

}
