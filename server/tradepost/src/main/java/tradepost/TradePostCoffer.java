package tradepost;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import core.task.Continuation;
import core.task.Continuations;
import io.ruin.api.utils.MathUtils;
import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.ground.GroundItem;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import tradepost.db.TradePostCoffersDb;
import tradepost.hook.Attributes;

@Slf4j
@ExtensionMethod(Attributes.class)
public class TradePostCoffer {

	public static void load(Player player) {
		resetEntries(player);
	}

	private static void resetEntries(Player player) {
		Continuations.schedule(resetEntriesCont(player));
	}

	private static Continuation.Void resetEntriesCont(Player player) {
		return () -> {
			var entries = TradePostCoffersDb.fetchCollectList(player.uuid()).await();
			if (!player.isOnline()) {
				return;
			}
			if (entries == null) {
				log.error("Unable load coffer items fro player: " + player.uuid());
				return;
			}
			setEntries(player, entries);
		};
	}

	private static void setEntries(Player player, List<TradePostCoffersDb.CofferEntry> entries) {
		var pTradePost = player.tradePost();
		pTradePost.coffer.clear();
		pTradePost.coffer.addAll(entries);
	}

	public static void collectAll(Player player) {
		Continuations.schedule(() -> {
			if (!player.isOnline()) {
				return;
			}

			var pTradePost = player.tradePost();
			if (pTradePost.coffer.isEmpty()) {
				player.sendMessage("No items to collect.");
				return;
			}

			var entries = List.copyOf(pTradePost.coffer);
			pTradePost.coffer.clear();

			if (!TradePostCoffersDb.deleteEntriesCont(player.uuid(), entries).await()) {
				player.sendMessage("Unable to collect an item.");
				return;
			}

			for (var entry : entries) {
				collect(player, entry);
			}

			if (!player.isOnline()) {
				return;
			}
		});
	}

	public static Continuation.Void collect(TradePostCoffersDb.CofferEntry entry, Player player) {
		return () -> {
			if (!player.isOnline()) {
				return;
			}

			var pTradePost = player.tradePost();
			if (!pTradePost.coffer.remove(entry)) {
				player.sendMessage("Unable to collect an item.");
				return;
			}

			if (!TradePostCoffersDb.deleteEntriesCont(player.uuid(), List.of(entry)).await()) {
				player.sendMessage("Unable to collect an item.");
				return;
			}

			collect(player, entry);

			if (!player.isOnline()) {
				return;
			}
		};
	}

	private static void collect(Player player, TradePostCoffersDb.CofferEntry entry) {
		var unnotedId = ObjType.unnotedId(entry.itemId());
		var notedId = ObjType.notedId(unnotedId);
		var itemDef = ObjType.get(unnotedId);

		var pInv = player.getInventory();
		var pBank = player.getBank();
		var remainingAmount = entry.itemAmount();

		var depositInvCnt = pInv.add(notedId, remainingAmount);
		if (depositInvCnt > 0) {
			player.sendMessage("Deposited " + depositInvCnt + " x " + itemDef.name
					+ " into your inventory from trade post coffer.");
			remainingAmount -= depositInvCnt;
		}

		if (remainingAmount > 0) {
			var depositBankCnt = pBank.deposit(unnotedId, remainingAmount);
			if (depositBankCnt > 0) {
				player.sendMessage("Deposited " + depositBankCnt + " x " + itemDef.name
						+ " into bank from trade post coffer.");
				remainingAmount -= depositBankCnt;
			}
		}

		if (remainingAmount > 0) {
			new GroundItem(notedId, remainingAmount).owner(player).position(player.getPosition()).spawn();
			player.sendMessage("Dropped " + remainingAmount + " x " + itemDef.name
					+ " on the floor from trade post coffer.");
		}
	}

	public static void add(String uid, int item, BigInteger amount) {
		Continuations.schedule(() -> {
			var newEntry = TradePostCoffersDb.addItemCont(uid, item, amount.intValueExact()).await();
			if (newEntry == null) {
				return;
			}

			var player = World.getPlayer(uid);
			if (player != null) {
				var pTradePost = player.tradePost();
				pTradePost.coffer.add(newEntry);
				pTradePost.inter.refresh(player);
			}
		});
	}

	public static void addCoins(String uid, BigInteger amount) {
		var player = World.getPlayer(uid);
		if (player != null) {
			var pTradePost = player.tradePost();
			pTradePost.cofferCoins = pTradePost.cofferCoins.add(amount);
		}
		TradePostCoffersDb.addCoins(uid, amount);
	}

	public static void collectCoins(Player player) {
		var pTradePost = player.tradePost();
		collectCoins(player, pTradePost.cofferCoins);
	}

	public static void collectCoins(Player player, BigInteger amount) {
		if (MathUtils.lt(amount, BigInteger.ONE)) {
			player.sendMessage("You must withdraw at least 1.");
			return;
		}

		var pTradePost = player.tradePost();
		var amt = MathUtils.min(pTradePost.cofferCoins, amount);

		if (MathUtils.eq(amt, BigInteger.ZERO)) {
			player.sendMessage("Your coffer is empty.");
			return;
		}

		pTradePost.cofferCoins = pTradePost.cofferCoins.subtract(amt);
		TradePostCoffersDb.removeCoins(player.uuid(), amt.longValueExact());
		player.getInventory().addCoinsOrPlatTokens(amt);
	}

	public static BigInteger amount(Player player) {
		return player.tradePost().cofferCoins;
	}
}
