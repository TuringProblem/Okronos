package io.ruin.model.activities.tempevents.hweenevent;

import io.ruin.cache.ItemID;
import io.ruin.model.activities.newshop.NewShop;
import io.ruin.model.activities.newshop.ShopCategories;
import io.ruin.model.activities.newshop.ShopItem;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.boxes.mystery.MysteryBox;

import java.util.Arrays;
import java.util.List;

public class HalloweenEventStore extends NewShop {
	@Override
	public List<ShopItem> getShopItemList() {
		return Arrays.asList(
			new ShopItem(new Item(ItemID.BLUE_HALLOWEEN_MASK), 5000, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(ItemID.GREEN_HALLOWEEN_MASK), 5000, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(ItemID.RED_HALLOWEEN_MASK), 5000, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(ItemID.DONATOR_MYSTERY_BOX), 1000, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(30430), 500, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(30451), 100, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(30452), 200, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(ItemID.CRITICAL_HIT_SIGIL), 1500, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(ItemID.HEALTH_SIPHON_SIGIL), 1500, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(ItemID.ARMOUR_BREAK_SIGIL), 1000, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(ItemID.SPECIAL_SAVER_SIGIL), 1000, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(ItemID.RESPECT_DEAD_SIGIL), 1000, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(ItemID.AOE_SWIPE_SIGIL), 1000, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(ItemID.SIPHON_DEAD_SIGIL), 1000, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(ItemID.DAMAGE_FOR_HIRE_HIGH_SIGIL), 1000, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(ItemID.DAMAGE_FOR_HIRE_LOW_SIGIL), 500, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(ItemID.VENOM_TIPPED_SIGIL), 500, ShopCategories.TREASURES, null, true),
			new ShopItem(new Item(ItemID.FREEZE_CHANCE_SIGIL), 500, ShopCategories.TREASURES, null, true),

			new ShopItem(new Item(ItemID.DOUBLE_EXP_SCROLL), 250, ShopCategories.SCROLLS, null, true),
			new ShopItem(new Item(ItemID.DOUBLE_DROP_SCROLL), 400, ShopCategories.SCROLLS, null, true),
			new ShopItem(new Item(ItemID.DAMAGE_BOOST_SCROLL), 400, ShopCategories.SCROLLS, null, true),
			new ShopItem(new Item(ItemID.DAMAGE_REDUCTION_SCROLL), 200, ShopCategories.SCROLLS, null, true),
			new ShopItem(new Item(ItemID.SLAYER_TASK_PICK_SCROLL), 350, ShopCategories.SCROLLS, null, true),
			new ShopItem(new Item(ItemID.SLAYER_SKIP_SCROLL), 50, ShopCategories.SCROLLS, null, true),
			new ShopItem(new Item(ItemID.PERK_POINT_SCROLL), 200, ShopCategories.SCROLLS, null, true),

			new ShopItem(new Item(ItemID.SCYTHE), 1500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(ItemID.RUBBER_CHICKEN), 2000, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(26256), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(26258), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(9920), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(24527), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(24525), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27473), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27475), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27477), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27479), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27481), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(24305), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(24307), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(24302), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(24304), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(24303), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(24315), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(24317), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(24319), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(24323), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(24321), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(22689), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(22692), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(22695), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(22698), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(22701), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(13283), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(13284), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(13285), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(13286), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(13287), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27497), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27499), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27501), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27503), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27505), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(27507), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(20439), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(20436), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(20442), 500, ShopCategories.COSMETICS, null, true),
			new ShopItem(new Item(20433), 500, ShopCategories.COSMETICS, null, true)
		);
	}

	@Override
	public boolean buy(Player player, ShopItem item, int amount) {
		if (amount > 1) {
			if (item.getItem().noteable()) {
				for (int i = 0; i < amount; i++) {
					if (!player.getInventory().hasRoomFor(item.getItem().getDef().notedId, 1)) {
						player.sendMessage("Not enough inventory space.");
						return false;
					} else if (player.halloweenEventPoints < item.getCost()) {
						player.sendMessage("You don't have enough halloween event points.");
						return false;
					} else {
						player.halloweenEventPoints -= item.getCost();
						player.getInventory().addOrDrop(item.getItem().getDef().notedId, 1);
					}
				}
			} else {
				for (int i = 0; i < amount; i++) {
					if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
						player.sendMessage("Not enough inventory space.");
						return false;
					} else if (player.halloweenEventPoints < item.getCost()) {
						player.sendMessage("You don't have enough halloween event points.");
						return false;
					} else {
						player.halloweenEventPoints -= item.getCost();
						player.getInventory().addOrDrop(item.getItem());
					}
				}
			}
		} else {
			if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
				player.sendMessage("Not enough inventory space.");
				return false;
			} else if (player.halloweenEventPoints < item.getCost()) {
				player.sendMessage("You don't have enough halloween event points.");
				return false;
			} else {
				player.halloweenEventPoints -= item.getCost();
				player.getInventory().addOrDrop(item.getItem());
			}
		}
		return true;
	}

	@Override
	public List<ShopCategories> getCategories() {
		return Arrays.asList(
			ShopCategories.COSMETICS,
			ShopCategories.SCROLLS,
			ShopCategories.TREASURES
		);
	}

	@Override
	public String getShopName() {
		return "Halloween Store";
	}

	@Override
	public String getPointIdentifier() {
		return "Halloween Event Points";
	}

	@Override
	public void openMessage(Player player) {
		player.sendMessage("You have a total of " + player.halloweenEventPoints + " Halloween event points.");
	}
}
