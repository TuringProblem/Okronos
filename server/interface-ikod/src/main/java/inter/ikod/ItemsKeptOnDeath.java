package inter.ikod;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.ItemBreaking;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class ItemsKeptOnDeath {
	public final List<Item> kept;
	public final List<KeptOnDeathItem> otherItems;

	ItemsKeptOnDeath(List<Item> kept, List<KeptOnDeathItem> otherItems) {
		this.kept = kept;
		this.otherItems = otherItems;
	}

	public static ItemsKeptOnDeath create(Player player, ItemsKeptOnDeathInterfaceFlags flags) {
		ArrayList<Item> items = IKOD.itemsSortedByProtectValue(player);

		boolean ultimateIronMan = player.getGameMode().isUltimateIronman();
		int keepCount = IKOD.getKeepCount(flags.skull, ultimateIronMan, flags.protect);
		List<Item> kept = forceKeptItems(keepCount, items);
		items.removeAll(kept);

		ArrayList<KeptOnDeathItem> remainingItems = new ArrayList<>();

		remainingItems.addAll(lostUntradeables(items, flags));
		remainingItems.addAll(otherKept(items, flags));
		remainingItems.addAll(lostTradeables(items, flags));
		return new ItemsKeptOnDeath(kept, remainingItems);
	}

	private static List<KeptOnDeathItem> otherKept(List<Item> items, ItemsKeptOnDeathInterfaceFlags flags) {
		ArrayList<KeptOnDeathItem> result = new ArrayList<>();
		for (Item item : new ArrayList<>(items)) {
			if (!item.getDef().tradeable) {
				result.add(new KeptOnDeathItem(ItemOnDeathItemKind.OtherKept, item));
				items.remove(item);
			}
		}
		return result;
	}

	private static List<KeptOnDeathItem> lostTradeables(List<Item> items, ItemsKeptOnDeathInterfaceFlags flags) {
		ArrayList<KeptOnDeathItem> result = new ArrayList<>();
		for (Item item : new ArrayList<>(items)) {
			if (!item.getDef().tradeable) {
				continue;
			}
			ItemBreaking breakTo = item.getDef().breakTo;
			if (flags.killedByAPlayer) {
				result.add(new KeptOnDeathItem(ItemOnDeathItemKind.LostToThePlayerWhoKillsYou, item));
			} else if (breakTo == null) {
				result.add(new KeptOnDeathItem(ItemOnDeathItemKind.Lost, item));
			} else {
				result.add(new KeptOnDeathItem(ItemOnDeathItemKind.LostDowngraded, new Item(breakTo.brokenId, item.getAmount())));
			}
			items.remove(item);
		}
		return result;
	}

	private static List<KeptOnDeathItem> lostUntradeables(List<Item> items, ItemsKeptOnDeathInterfaceFlags flags) {
		ArrayList<KeptOnDeathItem> result = new ArrayList<>();
		for (Item item : new ArrayList<>(items)) {
			if (!item.getDef().tradeable) {
				boolean convertible = item.getDef().highAlchValue > 0;
				ItemOnDeathItemKind kind;
				if (convertible && flags.wilderness && !flags.killedByAPlayer) {
					kind = ItemOnDeathItemKind.LostGraveyardCoins;
				} else if (!convertible && flags.wilderness) {
					kind = ItemOnDeathItemKind.Deleted;
				} else if (convertible && flags.killedByAPlayer) {
					kind = ItemOnDeathItemKind.LostToThePlayerWhoKillsYou;
				} else {
					kind = ItemOnDeathItemKind.Lost;
				}
				result.add(new KeptOnDeathItem(kind, item));
				items.remove(item);
			}
		}
		return result;
	}

	private static List<Item> forceKeptItems(int keepCount, List<Item> sortedByProtectValue) {
		return sortedByProtectValue.stream()
			.filter(it -> !it.getDef().neverProtect)
			.limit(keepCount)
			.collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return "kept: " + this.kept + "\n" + "other: " + this.otherItems;
	}
}
