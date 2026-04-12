package io.ruin.model.activities.newshop.shops;

import io.ruin.cache.ItemID;
import io.ruin.model.activities.newshop.NewShop;
import io.ruin.model.activities.newshop.ShopCategories;
import io.ruin.model.activities.newshop.ShopItem;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;

import java.util.Arrays;
import java.util.List;

public class SkillcapeShop extends NewShop {
	@Override
	public List<ShopItem> getShopItemList() {
		return Arrays.asList(
                /*
                Untrimmed
                 */
			new ShopItem(new Item(ItemID.ATTACK_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.DEFENCE_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.STRENGTH_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.HITPOINTS_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.RANGING_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.PRAYER_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.MAGIC_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.COOKING_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.WOODCUTTING_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.FLETCHING_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.FISHING_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.FIREMAKING_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.CRAFTING_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.SMITHING_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.MINING_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.HERBLORE_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.AGILITY_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.THIEVING_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.SLAYER_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.FARMING_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.RUNECRAFT_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.HUNTER_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
			new ShopItem(new Item(ItemID.CONSTRUCT_CAPE), 99000, ShopCategories.UNTRIMMED, null, true),
                /*
                Trimmed
                 */
			new ShopItem(new Item(ItemID.ATTACK_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.DEFENCE_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.STRENGTH_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.HITPOINTS_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.RANGING_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.PRAYER_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.MAGIC_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.COOKING_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.WOODCUT_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.FLETCHING_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.FISHING_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.FIREMAKING_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.CRAFTING_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.SMITHING_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.MINING_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.HERBLORE_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.AGILITY_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.THIEVING_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.SLAYER_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.FARMING_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.RUNECRAFT_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.HUNTER_CAPET), 99000, ShopCategories.TRIMMED, null, true),
			new ShopItem(new Item(ItemID.CONSTRUCT_CAPET), 99000, ShopCategories.TRIMMED, null, true),
                /*
                  Hoods
                 */
			new ShopItem(new Item(ItemID.ATTACK_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.DEFENCE_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.STRENGTH_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.HITPOINTS_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.RANGING_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.PRAYER_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.MAGIC_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.COOKING_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.WOODCUTTING_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.FLETCHING_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.FISHING_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.FIREMAKING_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.CRAFTING_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.SMITHING_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.MINING_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.HERBLORE_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.AGILITY_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.THIEVING_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.SLAYER_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.FARMING_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.RUNECRAFT_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.HUNTER_HOOD), 0, ShopCategories.HOODS, null, true),
			new ShopItem(new Item(ItemID.CONSTRUCT_HOOD), 0, ShopCategories.HOODS, null, true),
                /*
                Masters
                 */
			new ShopItem(new Item(ItemID.MASTER_CAPE_ATT), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_DEF), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_STR), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_HP), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_RANGED), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_PRAYER), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_MAGIC), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_COOK), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_WOODCUT), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_FLETCH), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_FISH), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_FM), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_CRAFT), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_SMITH), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_MINING), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_HERB), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_AGILITY), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_THIEV), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_SLAYER), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_FARM), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_RC), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_HUNT), 2000000, ShopCategories.MASTERCAPES, null, true),
			new ShopItem(new Item(ItemID.MASTER_CAPE_CONSTRUCT), 2000000, ShopCategories.MASTERCAPES, null, true),


                /*
                Max capes
                 */
			new ShopItem(new Item(ItemID.MAX_HOOD), 0, ShopCategories.MAXCAPES, null, true),
			new ShopItem(new Item(ItemID.MAX_CAPE), 2277000, ShopCategories.MAXCAPES, null, true));
	}

	@Override
	public boolean buy(Player player, ShopItem item, int amount) {
		if (!item.isIronman()) {
			if (player.getGameMode().isIronMan()) {
				player.sendMessage("Ironmen can't buy this item.");
				return false;
			}
		}
		if (amount > 1) {
			if (item.getItem().noteable()) {
				for (int i = 0; i < amount; i++) {
					if (!player.getInventory().hasRoomFor(item.getItem().getDef().notedId, 1)) {
						player.sendMessage("Not enough inventory space.");
						return false;
					} else if (player.getInventory().getAmount(995) < item.getCost()) {
						player.sendMessage("You don't have enough coins.");
						return false;
					} else {
						player.getInventory().remove(995, item.getCost());
						player.getInventory().addOrDrop(item.getItem().getDef().notedId, 1);
					}
				}
			} else {
				for (int i = 0; i < amount; i++) {
					if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
						player.sendMessage("Not enough inventory space.");
						return false;
					} else if (player.getInventory().getAmount(995) < item.getCost()) {
						player.sendMessage("You don't have enough coins.");
						return false;
					} else {
						player.getInventory().remove(995, item.getCost());
						player.getInventory().addOrDrop(item.getItem());
					}
				}
			}
		} else {
			if (!player.getInventory().hasRoomFor(item.getItem().getId(), 1)) {
				player.sendMessage("Not enough inventory space.");
				return false;
			} else if (player.getInventory().getAmount(995) < item.getCost()) {
				player.sendMessage("You don't have enough coins to buy this.");
				return false;
			} else {
				player.getInventory().remove(995, item.getCost());
				player.getInventory().addOrDrop(item.getItem());
			}
		}
		return true;
	}

	@Override
	public List<ShopCategories> getCategories() {
		return Arrays.asList(
			ShopCategories.HOODS,
			ShopCategories.UNTRIMMED,
			ShopCategories.TRIMMED,
			ShopCategories.MASTERCAPES,
			ShopCategories.MAXCAPES
		);
	}

	@Override
	public String getShopName() {
		return "Mac's Cape Emporium";
	}

	@Override
	public String getPointIdentifier() {
		return "coins";
	}

	@Override
	public void openMessage(Player player) {

	}
}
