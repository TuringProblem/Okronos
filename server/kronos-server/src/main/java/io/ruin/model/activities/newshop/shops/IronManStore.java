package io.ruin.model.activities.newshop.shops;

import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.ItemID;
import io.ruin.model.activities.newshop.NewShop;
import io.ruin.model.activities.newshop.ShopCategories;
import io.ruin.model.activities.newshop.ShopItem;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;

import java.util.Arrays;
import java.util.List;

public class IronManStore extends NewShop {
	@Override
	public List<ShopItem> getShopItemList() {
		return Arrays.asList(
                /*
                Armour
                 */
			new ShopItem(new Item(ItemID.IRON_FULL_HELM), 500, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.IRON_PLATEBODY), 500, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.IRON_PLATELEGS), 500, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.STEEL_FULL_HELM), 800, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.STEEL_PLATEBODY), 800, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.STEEL_PLATELEGS), 800, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MITHRIL_FULL_HELM), 1500, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MITHRIL_PLATEBODY), 5500, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MITHRIL_PLATELEGS), 3000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.ADAMANT_FULL_HELM), 4000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.ADAMANT_PLATEBODY), 18000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.ADAMANT_PLATELEGS), 7000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.ADAMANT_KITESHIELD), 6000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.PROSELYTE_HARNESS_M), 30000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.PROSELYTE_HARNESS_F), 30000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.RUNE_FULL_HELM), 36000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.RUNE_PLATEBODY), 69000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.RUNE_PLATELEGS), 65000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.WARRIOR_HELM), 78000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.BERSERKER_HELM), 78000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.HELM_OF_NEITIZNOT), 100000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.ARCHER_HELM), 78000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.BLUE_WIZARD_HAT), 100, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MYSTIC_HAT), 15000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MYSTIC_ROBE_TOP), 120000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MYSTIC_ROBE_BOTTOM), 80000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MYSTIC_GLOVES), 10000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MYSTIC_BOOTS), 10000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MYSTIC_HAT_DARK), 15000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MYSTIC_ROBE_TOP_DARK), 120000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MYSTIC_ROBE_BOTTOM_DARK), 80000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MYSTIC_GLOVES_DARK), 10000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MYSTIC_BOOTS_DARK), 10000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MYSTIC_HAT_LIGHT), 15000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MYSTIC_ROBE_TOP_LIGHT), 120000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MYSTIC_ROBE_BOTTOM_LIGHT), 80000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MYSTIC_GLOVES_LIGHT), 10000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.MYSTIC_BOOTS_LIGHT), 10000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.FARSEER_HELM), 78000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.LEATHER_COWL), 100, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.LEATHER_BODY), 150, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.LEATHER_CHAPS), 125, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.COIF), 600, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.STUDDED_BODY), 1000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.STUDDED_CHAPS), 1000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.GREEN_DHIDE_BODY), 10500, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.GREEN_DHIDE_CHAPS), 4000, ShopCategories.ARMOUR, null, true),
			new ShopItem(new Item(ItemID.GREEN_DHIDE_VAMB), 2500, ShopCategories.ARMOUR, null, true),
                /*
                Weapons
                 */
			new ShopItem(new Item(ItemID.IRON_SCIMITAR), 250, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.STEEL_SCIMITAR), 500, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.MITHRIL_SCIMITAR), 3000, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.ADAMANT_SCIMITAR), 7000, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.RUNE_SCIMITAR), 25000, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.DRAGON_SCIMITAR), 185000, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.DRAGON_DAGGER), 80000, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.DRAGON_LONGSWORD), 200000, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.DRAGON_BATTLEAXE), 400000, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.DRAGON_MACE), 150000, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.BATTLESTAFF), 20000, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.STAFF_OF_AIR), 1500, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.STAFF_OF_WATER), 1500, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.STAFF_OF_EARTH), 1500, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.STAFF_OF_FIRE), 1500, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.ANCIENT_STAFF), 80000, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.ZAMORAK_STAFF), 80000, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.SARADOMIN_STAFF), 80000, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.GUTHIX_STAFF), 80000, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.OAK_SHORTBOW), 300, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.MAPLE_SHORTBOW), 900, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.YEW_SHORTBOW), 3000, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.MAGIC_SHORTBOW), 15000, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.DORGESHUUN_CROSSBOW), 1500, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.BRONZE_KNIFE), 10, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.IRON_KNIFE), 50, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.IRON_ARROW), 10, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.MITHRIL_ARROW), 50, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.ADAMANT_ARROW), 100, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.MITHRIL_BOLTS), 50, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.BONE_BOLTS), 5, ShopCategories.WEAPONS, null, true),
			new ShopItem(new Item(ItemID.DARKLIGHT), 3500, ShopCategories.WEAPONS, null, true),
                /*
                Equipment
                 */
			new ShopItem(new Item(ItemID.ANTIDRAGON_SHIELD), 1500, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.ELEMENTAL_SHIELD), 1000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.CLIMBING_BOOTS), 10000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.AIR_RUNE), 4, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.WATER_RUNE), 4, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.EARTH_RUNE), 4, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.FIRE_RUNE), 4, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.BODY_RUNE), 3, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.MIND_RUNE), 3, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.CHAOS_RUNE), 200, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.DEATH_RUNE), 450, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.BLOOD_RUNE), 650, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.LAW_RUNE), 300, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.NATURE_RUNE), 300, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.COSMIC_RUNE), 300, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.ASTRAL_RUNE), 500, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.SOUL_RUNE), 500, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.AVAS_ATTRACTOR), 500, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.AVAS_ACCUMULATOR), 1500, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.AMULET_OF_ACCURACY), 20000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.AMULET_OF_POWER), 20000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.AMULET_OF_STRENGTH), 20000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.AMULET_OF_DEFENCE), 20000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.AMULET_OF_MAGIC), 20000, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.ZAMORAK_CAPE), 2, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.SARADOMIN_CAPE), 2, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.GUTHIX_CAPE), 2, ShopCategories.EQUIPMENT, null, true),
			new ShopItem(new Item(ItemID.CAPE_OF_LEGENDS), 2000, ShopCategories.EQUIPMENT, null, true),
                /*
                Skilling
                 */
			new ShopItem(new Item(ItemID.PURE_ESSENCE), 5, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.BRONZE_PICKAXE), 5, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.IRON_PICKAXE), 150, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.MITHRIL_PICKAXE), 1500, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.ADAMANT_PICKAXE), 3200, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.RUNE_PICKAXE), 32000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.BRONZE_AXE), 5, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.IRON_AXE), 150, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.MITHRIL_AXE), 1500, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.ADAMANT_AXE), 3200, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.RUNE_AXE), 32000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.SMALL_FISHING_NET), 5, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.BIG_FISHING_NET), 20, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.FISHING_ROD), 5, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.FLY_FISHING_ROD), 5, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.BARBARIAN_ROD), 5, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.HARPOON), 5, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.LOBSTER_POT), 20, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.KARAMBWAN_VESSEL), 1200, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.RAW_KARAMBWANJI), 15, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.FISHING_BAIT), 3, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.BAIT_PACK), 15000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.SANDWORMS), 90, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.BUTTERFLY_NET), 25, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.BUTTERFLY_JAR), 25, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.IMPLING_JAR), 2000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.NOOSE_WAND), 5, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.BIRD_SNARE), 6, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.TEASING_STICK), 60, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.RABBIT_SNARE), 20, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.BOX_TRAP), 40, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.MAGIC_BOX), 750, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.COMPOST_PACK), 75000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.RAKE), 15, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.EMPTY_PLANT_POT), 40, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.PLANT_POT_PACK), 40000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.WATERING_CAN), 25, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.GARDENING_TROWEL), 15, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.SEED_DIBBER), 15, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.SECATEURS), 15, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.PLANT_CURE), 25, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.SAW), 13, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.STEEL_NAILS), 3, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.BOLT_OF_CLOTH), 25000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.LIMESTONE_BRICK), 2600, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.MARBLE_BLOCK), 500000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.GOLD_LEAF_8784), 3250000, ShopCategories.SKILLING, null, true),
			new ShopItem(new Item(ItemID.MAGIC_STONE_8788), 1300000, ShopCategories.SKILLING, null, true),
                 /*
                Utility
                 */
			new ShopItem(new Item(ItemID.HOME_TELEPORT), 1000000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.SHIELD_RIGHT_HALF), 2000000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.POTATO_WITH_CHEESE), 200, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.ATTACK_POTION4), 900, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.STRENGTH_POTION4), 900, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.DEFENCE_POTION4), 900, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.ANTIPOISON4), 900, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.POT), 5, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.BUCKET), 5, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.JUG), 5, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.EMPTY_JUG_PACK), 35000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.JUG_OF_WATER), 10, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.VIAL), 5, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.EMPTY_VIAL_PACK), 35000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.VIAL_OF_WATER), 6, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.WATERFILLED_VIAL_PACK), 35000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.BOWL), 10, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.ROPE), 5, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.KNIFE), 5, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.SPADE), 5, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.HAMMER), 5, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.TINDERBOX), 5, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.MACHETE), 150, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.NEEDLE), 5, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.THREAD), 5, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.CHISEL), 5, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.GLASSBLOWING_PIPE), 5, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.SHEARS), 5, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.SODA_ASH), 50, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.BUCKET_OF_SAND), 25, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.FEATHER), 20, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.PESTLE_AND_MORTAR), 25, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.EYE_OF_NEWT_PACK), 15000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.CLEANING_CLOTH), 500, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.SILK), 100, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.BROWN_APRON), 50, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.UNLIT_TORCH), 500, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.CLOCKWORK), 1000, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.AMULET_MOULD), 50, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.NECKLACE_MOULD), 50, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.BRACELET_MOULD), 50, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.RING_MOULD), 50, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.BOLT_MOULD), 50, ShopCategories.UTILITY, null, true),
			new ShopItem(new Item(ItemID.TIARA_MOULD), 50, ShopCategories.UTILITY, null, true));


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
			ShopCategories.ARMOUR,
			ShopCategories.WEAPONS,
			ShopCategories.EQUIPMENT,
			ShopCategories.SKILLING,
			ShopCategories.UTILITY
		);
	}

	@Override
	public String getShopName() {
		return "Iron Man Store";
	}

	@Override
	public String getPointIdentifier() {
		return "coins";
	}

	@Override
	public void openMessage(Player player) {

	}
}
