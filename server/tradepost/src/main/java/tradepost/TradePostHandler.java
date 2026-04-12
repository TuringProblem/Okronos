package tradepost;

import io.ruin.api.utils.MathUtils;
import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import tradepost.hook.Attributes;

import java.math.BigInteger;
import static core.task.api.API.*;

@Slf4j
@ExtensionMethod(Attributes.class)
public class TradePostHandler {

	public static void promptSellOffer(Player player, Item item) {
		var pTradePost = player.tradePost();

		if (pTradePost.offersEmptySlot() == -1) {
			player.sendMessage("You have the maximum amount of offers active.");
			return;
		}

		if (item == null) {
			return;
		}

		if (!item.getDef().tradeable || item.getId() == ItemID.COINS_995
				|| item.getId() == ItemID.PLATINUM_TOKEN) {
			player.sendMessage("You cannot sell this item.");
			return;
		}

		if (item.hasAttributes()) {
			player.sendMessage("You cannot sell this item.");
			return;
		}

		var pInv = player.getInventory();
		var itemCount = pInv.count(item.getId(), true);
		if (itemCount > 1) {
			player.integerInput("How many would you like to sell?", amt -> {
				if (amt < 1) {
					player.sendMessage("You must sell at least 1.");
					return;
				}
				if (amt > pInv.count(item.getId(), true)) {
					player.sendMessage("You do not have that many to sell.");
					return;
				}
				longInput(player, item, amt, "Enter the price you would like to sell this item for: ");
			});
		} else {
			longInput(player, item, 1, "Enter the price you would like to sell this item for: ");
		}
	}

	public static void promptBuyOffer(Player player) {
		var pTradePost = player.tradePost();
		if (pTradePost.offersEmptySlot() == -1) {
			player.sendMessage("You have the maximum amount of offers active.");
			return;
		}
		player.itemSearch("Enter the name of the item you wish to buy: ", false, s -> {
			if (s == -1) {
				return;
			}

			var item = new Item(s);
			var itemDef = item.getDef();
			if (itemDef == null) {
				player.sendMessage("You cannot buy this item.");
				log.error("Player " + player.getName() + " tried to buy non existing item: " + item.getId());
				return;
			}

			if (!item.getDef().tradeable) {
				player.sendMessage("You cannot buy this item.");
				return;
			}

			player.integerInput("How much would you like to buy?", amt -> {
				if (amt < 1) {
					player.sendMessage("You must buy at least 1.");
					return;
				}
				longInputToBuy(player, item, "Enter the price you are willing to pay for each item: ", amt);
			});
		});
	}

	private static void longInputToBuy(Player player, Item item, String prompt, int amountToBuy) {
		player.stringInput(prompt, s -> {
			try {
				var pricePerItem = generateLongFromString(s);
				placeOffer(player, item.getId(), amountToBuy, pricePerItem, TradePostOffer.Kind.Buy);
			} catch (NumberFormatException e) {
				longInputToBuy(player, item, "You must enter a valid price for the item, try again: ",
						amountToBuy);
			}
		});
	}

	private static void longInput(Player player, Item item, int amount, String prompt) {
		player.stringInput(prompt, s -> {
			try {
				var price = generateLongFromString(s);
				placeOffer(player, item.getId(), amount, price, TradePostOffer.Kind.Sell);
			} catch (NumberFormatException e) {
				longInput(player, item, amount, "You must enter a valid price for the item, try again: ");
			}
		});
	}

	private static void placeOffer(Player player, int item, int amount, BigInteger price,
			TradePostOffer.Kind kind) {
		var unnotedId = ObjType.unnotedId(item);
		var dialogue =
				new YesNoDialogue("Are sure you want to place a " + kind.name().toLowerCase() + " offer",
						" For " + NumberUtils.formatCoins(price) + " ea", unnotedId, amount,
						() -> {

							queue(() -> {
								TradePost.placeOffer(player, item, amount, price, kind).await();
								player.tradePost().inter.openTradePost(player);
							});

						});
		player.dialogue(dialogue);
	}

	public static void promptRemoveFromCoffer(Player player) {
		player.stringInput("How much would you like to withdraw from your coffer?", s -> {
			try {
				var pTradePost = player.tradePost();
				var pTradePostInter = pTradePost.inter;
				var amount = generateLongFromString(s);
				TradePostCoffer.collectCoins(player, amount);
				pTradePostInter.refresh(player);
			} catch (NumberFormatException e) {
				player.sendMessage("You must enter a valid amount to withdraw.");
			}
		});
	}

	private static BigInteger generateLongFromString(String input) {
		input = input.trim().toLowerCase();
		input = input.replace(",", "");
		long multiplier = 1;
		if (input.endsWith("k")) {
			multiplier = 1_000L;
			input = input.substring(0, input.length() - 1);
		} else if (input.endsWith("m")) {
			multiplier = 1_000_000L;
			input = input.substring(0, input.length() - 1);
		} else if (input.endsWith("b")) {
			multiplier = 1_000_000_000L;
			input = input.substring(0, input.length() - 1);
		}
		var baseNumber = new BigInteger(input, 10);
		var result = baseNumber.multiply(BigInteger.valueOf(multiplier));
		if (MathUtils.lt(result, BigInteger.ZERO)) {
			throw new NumberFormatException();
		}
		return result;
	}
}
