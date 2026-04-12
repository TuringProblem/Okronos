package inter.ikod;

import io.ruin.cache.ObjType;
import io.ruin.model.combat.Killer;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.actions.impl.ItemBreaking;
import io.ruin.model.item.actions.impl.ItemUpgrading;
import io.ruin.model.item.actions.impl.chargable.Blowpipe;
import io.ruin.model.item.actions.impl.combine.ItemCombining;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.actions.impl.wilderness_keys.WildernessKeyHandler;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.services.Loggers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.ruin.cache.ItemID.*;

public class IKOD {

	public static void open(Player player) {
		if (player.isVisibleInterface(Interface.ITEMS_KEPT_ON_DEATH)) {
			player.closeInterface(ToplevelComponent.MAINMODAL);
		}
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.ITEMS_KEPT_ON_DEATH);

		player.protect = player.getPrayer().isActive(Prayer.PROTECT_ITEM);
		player.skull = player.getCombat().isSkulled();
		player.wilderness = player.wildernessLevel >= 21;
		ItemsKeptOnDeathInterfaceFlags flags = new ItemsKeptOnDeathInterfaceFlags(player.protect, player.skull,
				player.wilderness, player.killedByAPlayer);
		ItemKeptInterfaceUpdater itemKeptInterfaceState = ItemKeptInterfaceUpdater.create(player, flags);
		itemKeptInterfaceState.update(player);
	}

	private static final List<Integer> CHARGED_UNTRADEABLES = Arrays.asList(
			12809, 12006, 20655, 20657, 19710,
			12926, 22550, 11283, 21633, 22002,
			12931, 13197, 13199, 22555, 20714,
			12904, 11907, 12899, 22545);

	private static boolean allowProtect(Player player, Item item) {
		if (item.getDef().neverProtect)
			return false;
		if (!item.getDef().tradeable && item.getDef().breakTo != null)
			return false;
		if (CHARGED_UNTRADEABLES.contains(item.getId()))
			return true;
		if (item.getDef().breakId != 0)
			return true;
		if (item.getDef().combinedFrom != null)
			return true;
		if (item.getDef().upgradedFrom != null) {
			ObjType broken = ObjType.get(item.getDef().upgradedFrom.regularId);
			return broken.tradeable;
		}
		return item.getDef().tradeable || player.wildernessLevel > 20;
	}

	// todo - still want to clean this up
	public static void forLostItem(Player player, Killer killer, Consumer<Item> dropConsumer) {
		ArrayList<Item> items = itemsSortedByProtectValue(player);
		if (items.isEmpty())
			return;
		player.getInventory().clear();
		player.getEquipment().clear();
		Item currency = new Item(COINS_995, 0);
		ArrayList<Item> loseItems = new ArrayList<>(items.size());
		ArrayList<Item> keepItems = new ArrayList<>();
		int keepCountRemaining = getKeepCount(player.getCombat().isSkulled(), false,
				player.getPrayer().isActive(Prayer.PROTECT_ITEM));
		for (Item item : items) {
			if (killer != null && killer.player != null) {
				if (WildernessKeyHandler.handleDyingWithKey(killer.player, player, item))
					continue;
			}
			boolean lootingBag = isLootingBag(item);
			/* attempt to protect */
			if (keepCountRemaining > 0 && allowProtect(player, item)) {
				int keepAmount = Math.min(item.getAmount(), keepCountRemaining);
				keepItems.add(new Item(item.getId(), keepAmount, item.copyOfAttributes()));
				keepCountRemaining -= keepAmount;
				item.incrementAmount(-keepAmount);
				if (item.getAmount() == 0)
					continue;
			}

			// Auto keep god d'hides on death, when not in pvp
			if (player.wildernessLevel < 1 && !player.pvpAttackZone && item.getDef().name.contains("d'hide")
					&& (item.getDef().name.contains("Armadyl") ||
							item.getDef().name.contains("Ancient") ||
							item.getDef().name.contains("Bandos") ||
							item.getDef().name.contains("Saradomin") ||
							item.getDef().name.contains("Guthix") ||
							item.getDef().name.contains("Zamorak"))) {
				keepItems.add(item);
				continue;
			}

			// Ferocious Gloves
			if (item.getId() == 22981) {
				item.setId(22983);
				loseItems.add(item);
				continue;
			}
			/* rune pouch */
			if (item.getId() == 12791) {
				for (Item rune : player.getRunePouch().getItems()) {
					if (rune != null)
						loseItems.add(rune);
				}

				player.getRunePouch().clear();
				continue;
			}
			/* divine rune pouch */
			if (item.getId() == 27281) {
				for (Item rune : player.DivinerunePouch.getItems()) {
					if (rune != null)
						loseItems.add(rune);
				}

				player.DivinerunePouch.clear();
				continue;
			}
			if (player.wildernessLevel > 0 || player.pvpAttackZone) {
				/* saradomin's blessed sword */
				if (item.getId() == 12809) {
					item.setId(12804);
					AttributeExtensions.setCharges(item, 0);
					loseItems.add(item);
					continue;
				}
				boolean addedItem = false;
				for (int i = 25870; i < 25880; i += 2) {
					if (item.getId() == i) {
						item.setId(25859);
						loseItems.add(item);
						addedItem = true;
					}
				}
				for (int i = 25884; i < 25896; i += 2) {
					if (item.getId() == i) {
						item.setId(25859);
						loseItems.add(item);
						addedItem = true;
					}
				}
				if (addedItem)
					continue;
				if (item.getId() == BLADE_OF_SAELDOR) {
					item.setId(25859);
					loseItems.add(item);
					continue;
				}
				if (item.getId() == BLADE_OF_SAELDOR_C) {
					item.setId(25859);
					loseItems.add(item);
					continue;
				}
				if (item.getId() == BOW_OF_FAERDHINAD) {
					item.setId(25859);
					loseItems.add(item);
					continue;
				}
				if (item.getId() == BOW_OF_FAERDHINAD_C) {
					item.setId(25859);
					loseItems.add(item);
					continue;
				}
				if (item.getId() == CRYSTAL_HELM) {
					item.setId(CRYSTAL_ARMOUR_SEED);
					item.setAmount(1);
					loseItems.add(item);
					continue;
				}
				if (item.getId() == CRYSTAL_BODY) {
					item.setId(CRYSTAL_ARMOUR_SEED);
					item.setAmount(3);
					loseItems.add(item);
					continue;
				}
				if (item.getId() == CRYSTAL_LEGS) {
					item.setId(CRYSTAL_ARMOUR_SEED);
					item.setAmount(2);
					loseItems.add(item);
					continue;
				}
				if (item.getId() == VOLATILE_NIGHTMARE_STAFF) {
					item.setId(NIGHTMARE_STAFF);
					loseItems.add(new Item(VOLATILE_ORB, 1));
					loseItems.add(item);
					continue;
				}
				if (item.getId() == HARMONISED_NIGHTMARE_STAFF) {
					item.setId(NIGHTMARE_STAFF);
					loseItems.add(new Item(HARMONISED_ORB, 1));
					loseItems.add(item);
					continue;
				}
				if (item.getId() == ELDRITCH_NIGHTMARE_STAFF) {
					item.setId(NIGHTMARE_STAFF);
					loseItems.add(new Item(ELDRITCH_ORB, 1));
					loseItems.add(item);
					continue;
				}
				// webweaver dropping as uncharged version
				if (item.getId() == 8874) {
					item.setId(30568);
					loseItems.add(item);
					continue;
				}
				if (item.getId() == 8872 || item.getId() == 30566) {
					item.setId(22542);
					loseItems.add(item);
					continue;
				}
				if (item.getId() == 8876 || item.getId() == 8878 || item.getId() == 30560 || item.getId() == 30565) {
					item.setId(22552);
					loseItems.add(item);
					continue;
				}
				if (item.getDef().name.contains("(deg)")) {
					item.remove();
					loseItems.add(new Item(995, 5000000));
					continue;
				}
				/* tentacle whip */
				if (item.getId() == 12006) {
					item.setId(12004);
					AttributeExtensions.setCharges(item, 0);
					loseItems.add(item);
					continue;
				}
				/* ring of suffering */
				if (item.getId() == 20655 || item.getId() == 20657 || item.getId() == 19710) {
					item.setId(19550);
					AttributeExtensions.setCharges(item, 0);
					loseItems.add(item);
					continue;
				}
				/* serpentine helm */
				if (item.getDef().breakId == 12929) {
					int scalesAmount = AttributeExtensions.getCharges(item);
					if (scalesAmount > 0)
						loseItems.add(new Item(12934, scalesAmount));
					item.setId(12929);
					AttributeExtensions.setCharges(item, 0);
					loseItems.add(item);
					continue;
				}
				/* viggora's chainmace */
				if (item.getId() == 22545) {
					int etherAmount = AttributeExtensions.getCharges(item);
					if (etherAmount > 0)
						loseItems.add(new Item(21820, etherAmount));
					item.setId(22542);
					AttributeExtensions.setCharges(item, 0);
					loseItems.add(item);
					continue;
				}

				/* craw's bow */
				if (item.getId() == 22550) {
					int etherAmount = AttributeExtensions.getCharges(item);
					if (etherAmount > 0)
						loseItems.add(new Item(21820, etherAmount));
					item.setId(22547);
					AttributeExtensions.setCharges(item, 0);
					loseItems.add(item);
					continue;
				}
				/* thammarons's sceptre */
				if (item.getId() == 22555) {
					int etherAmount = AttributeExtensions.getCharges(item);
					if (etherAmount > 0)
						loseItems.add(new Item(21820, etherAmount));
					item.setId(22552);
					AttributeExtensions.setCharges(item, 0);
					loseItems.add(item);
					continue;
				}
				/* dragonfire shield */
				if (item.getDef().id == 11283) {
					item.setId(11284);
					AttributeExtensions.setCharges(item, 0);
					loseItems.add(item);
					continue;
				}
				/* ancient wyvern shield */
				if (item.getDef().id == 21633) {
					item.setId(21634);
					AttributeExtensions.setCharges(item, 0);
					loseItems.add(item);
					continue;
				}
				/* dragonfire ward */
				if (item.getDef().id == 22002) {
					item.setId(22003);

					loseItems.add(item);
					continue;
				}
				/* toxic staff of the dead */
				if (item.getDef().breakId == 12902) {
					int scalesAmount = AttributeExtensions.getCharges(item);
					if (scalesAmount > 0)
						loseItems.add(new Item(12934, scalesAmount));
					item.setId(12902);
					AttributeExtensions.setCharges(item, 0);
					loseItems.add(item);
					continue;
				}
				/* toxic blowpipe */
				if (item.getId() == 12926) {
					Blowpipe.Dart dart = Blowpipe.getDart(item);
					if (dart != Blowpipe.Dart.NONE)
						loseItems.add(new Item(dart.id, Blowpipe.getDartAmount(item)));
					int scales = Blowpipe.getScalesAmount(item);
					if (scales > 0)
						loseItems.add(new Item(12934, scales));
					AttributeExtensions.setCharges(item, 0);
					item.setId(12924);
					loseItems.add(item);
					continue;
				}
				/* blood money pouches */
				if (item.getId() >= 22521 && item.getId() <= 22524) {
					loseItems.add(item);
					continue;
				}
				/* bracelet of ethereum */
				if (item.getDef().breakId == 21817) {
					int etherAmount = AttributeExtensions.getCharges(item);
					if (etherAmount > 0)
						loseItems.add(new Item(21820, etherAmount));
					AttributeExtensions.setCharges(item, 0);
					item.setId(21817);
					loseItems.add(item);
					continue;
				}
				/* tome of fire */
				if (item.getId() == 20714) {
					int charges = AttributeExtensions.getCharges(item);
					if (charges > 0)
						loseItems.add(new Item(20718, Math.max(1, charges / 20)));
					AttributeExtensions.setCharges(item, 0);
					item.setId(20716);
					loseItems.add(item);
					continue;
				}
			}

			/* pet */
			Pet pet = item.getDef().pet;
			if (pet != null) {
				keepItems.add(item);
				continue;
			}

			/* breakable items */
			ItemBreaking breakable = item.getDef().breakTo;
			if (breakable != null && !breakable.freeFromShops) {
				// if not in wilderness keep the item breakable
				if (player.wildernessLevel < 1 && !player.pvpAttackZone) {
					keepItems.add(item);
					continue;
				}
				ObjType brokenDef = ObjType.get(breakable.brokenId);
				if (brokenDef == null) {
					System.out.println("Broken Def is null: " + breakable.brokenId);
					continue;
				}

				// if killed by player add coins to drop
				if (!Objects.isNull(killer)) {
					currency.incrementAmount(breakable.coinRepairCost);
				}

				// if in wilderness and broken item not tradeable, keep that item broken version
				if (!brokenDef.tradeable) {
					item.setId(brokenDef.id);
					keepItems.add(item);
					continue;
				}
				// if in wilderness and item is tradeable set item to broken id
				if (player.wildernessLevel > 0 || player.pvpAttackZone) {
					item.setId(brokenDef.id);
				}
			}
			/* upgraded items */
			ItemUpgrading upgrade = item.getDef().upgradedFrom;
			if (upgrade != null) {
				// if not in wilderness keep the item upgradeable
				if (player.wildernessLevel < 1 && !player.pvpAttackZone) {
					keepItems.add(item);
					continue;
				}
				ObjType regularDef = ObjType.get(upgrade.regularId);
				if (regularDef == null) {
					System.out.println("Regular Def is null: " + upgrade.regularId);
					continue;
				}

				// Always keep these upgrades in wilderness
				if (upgrade.name().toLowerCase().contains("slayer")
						|| upgrade.name().toLowerCase().contains("salve")
						|| upgrade.equals(ItemUpgrading.GUTHIX_CAPE)
						|| upgrade.equals(ItemUpgrading.ZAMORAK_CAPE)
						|| upgrade.equals(ItemUpgrading.SARADOMIN_CAPE)) {
					keepItems.add(item);
					continue;
				}

				// if killed by player add coins to drop
				if (!Objects.isNull(killer)) {
					currency.incrementAmount((long) (upgrade.coinUpgradeCost * .05));
				}

				// if in wilderness and below lvl 20 and regular item not tradeable, keep that
				// item
				if (!regularDef.tradeable) {
					if (player.wildernessLevel > 0 && player.wildernessLevel < 30 || player.pvpAttackZone) {
						keepItems.add(new Item(upgrade.regularId, 1, item.copyOfAttributes()));
					}
					continue;
				}

				// if in wilderness and item is tradeable set item to regular id
				if (player.wildernessLevel > 0 || player.pvpAttackZone) {
					item.setId(regularDef.id);
				}
			}
			/* combined items */
			ItemCombining combined = item.getDef().combinedFrom;
			if (combined != null) {
				loseItems.add(new Item(combined.primaryId, item.getAmount()));
				loseItems.add(new Item(combined.secondaryId, item.getAmount()));
				continue;
			}

			/* keep untradeables */ // Ignore auto-keeping looting bag.
			if (!isLootingBag(item) && !item.getDef().tradeable) {
				keepItems.add(item);
				continue;
			}

			// If the player is carrying a looting bag then we dump all of its contents into
			// the lost items collection.
			if (lootingBag) {
				ItemContainer contents = player.getLootingBag();
				if (!contents.isEmpty()) {
					List<Item> lost = Arrays.stream(contents.getItems()).filter(Objects::nonNull).collect(Collectors.toList());
					loseItems.addAll(lost);
					player.getLootingBag().clear();
				}
			}

			loseItems.add(item);
		}
		if (currency.getAmount() > 0)
			loseItems.add(currency);
		int size = Math.min(keepItems.size(), player.getInventory().getItems().length);
		for (int i = 0; i < size; i++)
			player.getInventory().set(i, keepItems.get(i));
		for (Item dropItem : loseItems) {
			dropConsumer.accept(dropItem);
		}
		if (killer == null)
			Loggers.logDangerousDeath(player.getUserId(), player.getName(), player.getIp(), -1, "", "", keepItems, loseItems);
		else
			Loggers.logDangerousDeath(player.getUserId(), player.getName(), player.getIp(), killer.player.getUserId(),
					killer.player.getName(), killer.player.getIp(), keepItems, loseItems);
	}

	public static int getKeepCount(boolean skulled, boolean ultimateIronman, boolean protectingItem) {
		if (ultimateIronman)
			return 0;
		int keepCount = skulled ? 0 : 3;
		if (protectingItem)
			keepCount++;
		return keepCount;
	}

	public static ArrayList<Item> itemsSortedByProtectValue(Player player) {
		int count = player.getInventory().getCount() + player.getEquipment().getCount();
		ArrayList<Item> list = new ArrayList<>(count);
		if (count > 0) {
			for (Item item : player.getInventory().getItems()) {
				if (item != null && item.getDef() != null)
					list.add(item.copy());
			}
			for (Item item : player.getEquipment().getItems()) {
				if (item != null && item.getDef() != null)
					list.add(item.copy());
			}

			list.sort((i1, i2) -> Integer.compare(i2.getDef().protectValue, i1.getDef().protectValue));
		}
		return list;
	}

	private static long getValue(Item item) {
		if (item.getId() == BLOOD_MONEY)
			return item.getAmount();
		if (item.getId() == COINS_995)
			return item.getAmount();
		long price = item.getDef().protectValue;
		if (price <= 0 && ((price = item.getDef().highAlchValue)) <= 0)
			return 0;
		return item.getAmount() * price;
	}

	public static void register() {

		InterfaceHandler.register(Interface.EQUIPMENT, h -> {
			h.actions[5] = (SimpleAction) IKOD::open;
		});

		InterfaceHandler.register(Interface.ITEMS_KEPT_ON_DEATH, h -> {
			h.actions[12] = (DefaultAction) (p, option, slot, itemId) -> {
				switch (slot) {
					case 0:
						p.protect = !p.protect;
						break;
					case 1:
						p.skull = !p.skull;
						break;
					case 2:
						p.killedByAPlayer = !p.killedByAPlayer;
						break;
					case 3:
						p.wilderness = !p.wilderness;
						break;
				}
				ItemsKeptOnDeathInterfaceFlags flags = new ItemsKeptOnDeathInterfaceFlags(
						p.protect,
						p.skull,
						p.wilderness,
						p.killedByAPlayer);
				ItemKeptInterfaceUpdater updater = ItemKeptInterfaceUpdater.create(p, flags);
				updater.update(p);
			};
		});
	}

	private static boolean isLootingBag(Item item) {
		return item.getId() == 11941 || item.getId() == 22586;
	}
}
