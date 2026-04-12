package io.ruin.model.content;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;
import io.ruin.utility.Broadcast;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

// TODO(polish) - static var logic?
public class UpgradeManager {

	public enum UpgradableItems {

		// Weapon
		ABYSSAL_BLOOD_WHIP(0, Category.WEAPON, "Abyssal blood whip", 26019, 30, new Item[]{new Item(4151), new Item(13307, 2500)}),
		DRAGON_SCIMITARE(1, Category.WEAPON, "Dragon Scimitar (e)", 26018, 45, new Item[]{new Item(4587, 3), new Item(995, 1500000)}),
		BLOODLINE_CHAINMACE(2, Category.WEAPON, "Bloodline chainmace", 25944, 35, new Item[]{new Item(22542, 3), new Item(25965)}),
		BLOODLINE_BOW(3, Category.WEAPON, "Bloodline bow", 25943, 35, new Item[]{new Item(22547, 3), new Item(25965)}),
		ZAMORAK_GODSWORD(4, Category.WEAPON, "Zamorak godsword (or)", 20374, 35, new Item[]{new Item(11808, 2)}),
		SARADOMIN_GODSWORD(5, Category.WEAPON, "Saradomin godsword (or)", 20372, 35, new Item[]{new Item(11806, 2)}),
		BANDOS_GODSWORD(6, Category.WEAPON, "Bandos godsword (or)", 20370, 35, new Item[]{new Item(11804, 2)}),
		ARMADYL_GODSWORD_OR(7, Category.WEAPON, "Armadyl godsword (or)", 20368, 35, new Item[]{new Item(11802, 2)}),
		CURSED_VESTAS_LONGSWORD(8, Category.WEAPON, "Cursed vesta's longsword", 25939, 25, new Item[]{new Item(22613, 2)}),


		CORRUPTED_STAFF(9, Category.WEAPON, "Corrupted Staff", 30023, 25, new Item[]{new Item(12900), new Item(26085)}),
		CORRUPTED_JAVELIN(10, Category.WEAPON, "Corrupted javelin", 30009, 25, new Item[]{new Item(12924), new Item(26085)}),
		CORRUPTED_ORB(11, Category.WEAPON, "Corrupted Orb", 25934, 25, new Item[]{new Item(24511), new Item(24514), new Item(24517)}),
		CREATION_CLAWS(12, Category.WEAPON, "Creation claws", 25946, 15, new Item[]{new Item(13652), new Item(9016)}),
		GALVEK_CROSSBOW(13, Category.WEAPON, "Galvek's crossbow", 26118, 50, new Item[]{new Item(21012), new Item(695)}),
		GALVEK_MACE(14, Category.WEAPON, "Galvek's mace", 26121, 50, new Item[]{new Item(24417), new Item(695)}),
		INFERNAL_DINS(9, Category.WEAPON, "Infernal Bulwark", 30404, 25, new Item[]{new Item(21015), new Item(30041, 100)}),


		// Armour
		DRACO_SET(0, Category.ARMOUR, "Draco full set", 26029, 35, new Item[]{new Item(11335), new Item(21892), new Item(4087), new Item(536, 100), new Item(11943, 100), new Item(6812, 100)}),
		FIGHTER_TORSOE(1, Category.ARMOUR, "Fighter torso (e)", 24175, 30, new Item[]{new Item(10551), new Item(8851, 500)}),
		VAMPYRIC_FACEGUARD(2, Category.ARMOUR, "Vampyric faceguard", 25996, 20, new Item[]{new Item(24271), new Item(24777)}),
		DRAGON_DEFENDER_T(3, Category.ARMOUR, "Dragon defender (t)", 19722, 85, new Item[]{new Item(12954), new Item(10506, 100)}),
		PRIMORDIAL_BOOTS_OR(4, Category.ARMOUR, "Primordial Boots (or)", 30038, 20, new Item[]{new Item(13239)}),
		PEGASIAN_BOOTS_OR(5, Category.ARMOUR, "Pegasian Boots (or)", 26081, 20, new Item[]{new Item(13237)}),
		ETERNAL_BOOTS_OR(6, Category.ARMOUR, "Eternal Boots (or)", 26082, 20, new Item[]{new Item(13235)}),
		FUSED_BOOTS(7, Category.ARMOUR, "Fused Boots", 26005, 100, new Item[]{new Item(30038), new Item(26081), new Item(26082)}),
		LAVA_DHIDE_COIF(8, Category.ARMOUR, "Lava dhide coif", 30074, 100, new Item[]{new Item(30083, 4), new Item(11826)}),
		LAVA_DHIDE_BODY(9, Category.ARMOUR, "Lava d'hide body", 30077, 100, new Item[]{new Item(30083, 6), new Item(11828)}),
		LAVA_DHIDE_CHAPS(10, Category.ARMOUR, "Lava d'hide chaps", 30080, 100, new Item[]{new Item(30083, 5), new Item(11830)}),

		NECROMANCER_HAT(11, Category.ARMOUR, "necromancer hat", 30020, 50, new Item[]{new Item(21018), new Item(26085)}),
		NECROMANCER_ROBE_TOP(12, Category.ARMOUR, "Necromancer robe top", 30021, 50, new Item[]{new Item(21021), new Item(26085)}),
		NECROMANCER_ROBE_BOTTOMS(13, Category.ARMOUR, "Necromancer robe bottoms", 30022, 50, new Item[]{new Item(21024), new Item(26085)}),

		CORRUPTED_HELM(14, Category.ARMOUR, "Corrupted helm", 23842, 25, new Item[]{new Item(23971), new Item(23866, 1000)}),

		CORRUPTED_BODY(15, Category.ARMOUR, "Corrupted body", 23845, 25, new Item[]{new Item(23975), new Item(23866, 3000)}),

		CORRUPTED_LEGS(16, Category.ARMOUR, "Corrupted legs", 23848, 25, new Item[]{new Item(23979), new Item(23866, 2000)}),


		// Misc
		FUSED_HALO(0, Category.MISC, "Fused halo", 25940, 30, new Item[]{new Item(24201), new Item(24192), new Item(24195),
			new Item(24204), new Item(12639), new Item(12637), new Item(24198), new Item(12638)}),
		COMBATANT_HEART(1, Category.MISC, "Combatant Heart", 30380, 100, new Item[]{new Item(20724, 3)}),
		RANGERS_HEART(2, Category.MISC, "Rangers' Heart", 30383, 100, new Item[]{new Item(20724, 3)}),
		OVERLOAD_HEART(3, Category.MISC, "Overload Heart", 25948, 15, new Item[]{new Item(30380), new Item(30383), new Item(20724)}),
		CAPE_OF_SKULLS(4, Category.MISC, "Cape of Skulls", 23351, 75, new Item[]{new Item(13280), new Item(13302, 5)}),
		GOLDEN_FLIPPERS(5, Category.MISC, "Golden Flippers", 25941, 30, new Item[]{new Item(6666), new Item(8784, 250)}),
		BLESSING_OF_THE_GODS(6, Category.MISC, "Blessing of the gods", 26044, 35, new Item[]{new Item(20220), new Item(20223), new Item(20226),
			new Item(20229), new Item(20235), new Item(20232)}),
		BRIMSTONE_RING_I(7, Category.MISC, "Brimstone ring (i)", 26057, 40, new Item[]{new Item(22975), new Item(6737), new Item(6735),
			new Item(6731), new Item(6733), new Item(10506, 500)}),
		RING_OF_THE_UNDEAD(8, Category.MISC, "Ring of the Undead", 26002, 20, new Item[]{new Item(13202), new Item(13307, 5000)}),
		RING_OF_THE_BEASTS(9, Category.MISC, "Ring of the Beasts", 26003, 20, new Item[]{new Item(12691), new Item(13307, 5000)}),
		RING_OF_THE_ARACHNIDS(10, Category.MISC, "Ring of the Arachnids", 26004, 20, new Item[]{new Item(12692), new Item(13307, 5000)}),
		AMULET_OF_TORTURE_OR(11, Category.MISC, "Amulet of torture (or)", 20366, 35, new Item[]{new Item(19553, 2)}),
		NECKLACE_OF_ANGUISH_OR(12, Category.MISC, "Necklace of Anguish (or)", 22249, 35, new Item[]{new Item(19547, 2)}),
		TORMENTED_BRACELET_OR(13, Category.MISC, "Tormented Bracelet (or)", 23444, 35, new Item[]{new Item(19544, 2)}),
		OCCULT_NECKLACE_OR(14, Category.MISC, "Occult necklace (or)", 19720, 35, new Item[]{new Item(12002, 2)}),
		ULTIMATE_TOTEM(15, Category.MISC, "Ultimate walker totem", 26099, 50, new Item[]{new Item(26096), new Item(26097), new Item(26098)}),


		// Pets
		ENCHANTED_GOLDEN_LEPRECHAUN(0, Category.PETS, "Enchanted Golden Leprechaun", 30391, 15, new Item[]{new Item(30389)}),


		;

		public static final UpgradableItems[] VALUES = values();

		public static ArrayList<UpgradableItems> forCategory(Category category) {
			ArrayList<UpgradableItems> items = Lists.newArrayList();
			for (UpgradableItems item : UpgradableItems.VALUES) {
				if (item.catagory.equals(category)) {
					items.add(item);
				}
			}
			return items;
		}

		private final int component;
		private final Category catagory;
		private final String name;
		private final int itemid;
		private final int chance;
		private final Item[] required;


		UpgradableItems(int component, Category category, String name, int itemid, int chance, Item[] required) {
			this.component = component;
			this.catagory = category;
			this.name = name;
			this.itemid = itemid;
			this.chance = chance;
			this.required = required;
		}

		public int getComponent() {
			return component;
		}

		public Category getCategory() {
			return catagory;
		}

		public String getName() {
			return name;
		}

		public int getItemid() {
			return itemid;
		}

		public int getChance() {
			return chance;
		}

		public Item[] getRequired() {
			return required;
		}
	}

	public enum Category {
		WEAPON,
		ARMOUR,
		JEWELLERY,
		PETS,
		MISC
	}

	@Setter
	@Getter
	private static Category lastCategory;

	@Getter
	@Setter
	private static UpgradableItems selectedItem;

	private static final int[] ITEM_COMPONENTS = {
		37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55,
		56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71,
		72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87,
		88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104,
		105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119,
		120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135
	};

	private static final int[] REQUIRED_COMPONENTS = {
		145, 146, 147, 148, 149, 150, 151, 152
	};

	private static final int UPGRADE_BUTTON = 169;

	static boolean upgrading = false;

	public static void register() {

		InterfaceHandler.register(Interface.UPGRADE_INTERFACE, interfaceHandler -> {
			for (int component = 8; component <= 25; component++) {
				int finalComponent = component;
				interfaceHandler.actions[component] = (SimpleAction) player -> player.upgradeManager.selectCategory(finalComponent);
			}
			for (int component : ITEM_COMPONENTS) {
				interfaceHandler.actions[component] = (SimpleAction) player -> player.upgradeManager.clickItem(component);
			}
			interfaceHandler.actions[UPGRADE_BUTTON] = (SimpleAction) (player) -> player.upgradeManager.upgrade();
		});
//            interfaceHandler.actions[8] = (SimpleAction) p -> selectCategory(0, p);
//            interfaceHandler.actions[12] = (SimpleAction) p -> selectCategory(1, p);
//            interfaceHandler.actions[16] = (SimpleAction) p -> selectCategory(2, p);
//            interfaceHandler.actions[20] = (SimpleAction) p -> selectCategory(3, p);
//            interfaceHandler.actions[24] = (SimpleAction) p -> selectCategory(4, p);
//
//            interfaceHandler.actions[UPGRADE_BUTTON] = (SimpleAction) UpgradeManager::upgrade;
	}

	@Getter
	@Setter
	private UpgradableItems item;

	@Setter
	@Getter
	private transient Player player;

	public void sendInterface(Category category) {
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.UPGRADE_INTERFACE);
		UpgradeManager.setLastCategory(category);
		UpgradeManager.setSelectedItem(null);
		sendItems();
		sendInfo(true);
		clearRequired();
	}

	public void sendInterface() {
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.UPGRADE_INTERFACE);
		UpgradeManager.setLastCategory(lastCategory != null ? lastCategory : Category.WEAPON);
		UpgradeManager.setSelectedItem(null);
		sendItems();
		sendInfo(true);
		clearRequired();
	}

	private void sendItems() {
		List<UpgradableItems> items = UpgradableItems.forCategory(UpgradeManager.getLastCategory());
		IntStream.of(ITEM_COMPONENTS).forEach((i -> {
			boolean hide = items.stream().noneMatch(item -> i == ITEM_COMPONENTS[item.getComponent()]);
			if (hide) {
				player.getPacketSender().sendItems(-1, i, 10007, new Item(-1));
			}
		}));
		items.forEach(i -> {
			player.getPacketSender().sendItems(-1, ITEM_COMPONENTS[i.getComponent()], 10007, new Item(i.itemid));
		});
	}

	private void selectCategory(int index) {
		Category category = Category.values()[index];
		UpgradeManager.setLastCategory(category);
		UpgradeManager.setSelectedItem(null);
		sendItems();
		sendInfo(true);
		clearRequired();
	}

	private void sendInfo(boolean clear) {
		player.getPacketSender().sendString(Interface.UPGRADE_INTERFACE, 168, clear ? "0%" : UpgradeManager.getSelectedItem().getChance() + "%");
	}

	private void clearRequired() {
		for (int i = 145; i <= 162; i++) {
			player.getPacketSender().sendItems(-1, i, 10007, new Item(-1));
		}

	}

	private void sendRequired() {
		int component = 0;
		Item[] requiredIds = selectedItem.getRequired();
		for (int i = 145; i <= 162; i++) {
			player.getPacketSender().sendItems(-1, i, 10007, new Item(-1));
		}
		for (Item i : requiredIds) {
			player.getPacketSender().sendItems(-1, REQUIRED_COMPONENTS[component++], 10007, i);
		}
	}
//    }
//        int component = 0;
//        Item[] requiredIds = selectedItem.getRequired();
//
//

	/// *        IntStream.of(ITEM_COMPONENTS).forEach((i -> {
//            player.getPacketSender().sendItems(-1, i, 0, new Item(-1));
//        }));*/
//
//        player.getPacketSender().sendItems(-1, 162, 0, new Item(selectedItem.itemid));
//
//        if (requiredIds.length == 1) {
//            player.getPacketSender().sendItems(-1, 145, 0, requiredIds[0]);
//            player.getPacketSender().sendItems(-1, 146, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 147, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 148, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 149, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 150, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 151, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 152, 0, new Item(-1));
//        } else if (requiredIds.length == 2) {
//            player.getPacketSender().sendItems(-1, 145, 0, requiredIds[0]);
//            player.getPacketSender().sendItems(-1, 146, 0, requiredIds[1]);
//            player.getPacketSender().sendItems(-1, 147, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 148, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 149, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 150, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 151, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 152, 0, new Item(-1));
//        } else if (requiredIds.length == 3) {
//            player.getPacketSender().sendItems(-1, 145, 0, requiredIds[0]);
//            player.getPacketSender().sendItems(-1, 146, 0, requiredIds[1]);
//            player.getPacketSender().sendItems(-1, 147, 0, requiredIds[2]);
//            player.getPacketSender().sendItems(-1, 148, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 149, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 150, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 151, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 152, 0, new Item(-1));
//        } else if (requiredIds.length == 4) {
//            player.getPacketSender().sendItems(-1, 145, 0, requiredIds[0]);
//            player.getPacketSender().sendItems(-1, 146, 0,requiredIds[1]);
//            player.getPacketSender().sendItems(-1, 147, 0, requiredIds[2]);
//            player.getPacketSender().sendItems(-1, 148, 0, requiredIds[3]);
//            player.getPacketSender().sendItems(-1, 149, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 150, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 151, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 152, 0, new Item(-1));
//        } else if (requiredIds.length == 5) {
//            player.getPacketSender().sendItems(-1, 145, 0, requiredIds[0]);
//            player.getPacketSender().sendItems(-1, 146, 0, requiredIds[1]);
//            player.getPacketSender().sendItems(-1, 147, 0, requiredIds[2]);
//            player.getPacketSender().sendItems(-1, 148, 0, requiredIds[3]);
//            player.getPacketSender().sendItems(-1, 149, 0, requiredIds[4]);
//            player.getPacketSender().sendItems(-1, 150, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 151, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 152, 0, new Item(-1));
//        } else if (requiredIds.length == 6) {
//            player.getPacketSender().sendItems(-1, 145, 0, requiredIds[0]);
//            player.getPacketSender().sendItems(-1, 146, 0, requiredIds[1]);
//            player.getPacketSender().sendItems(-1, 147, 0, requiredIds[2]);
//            player.getPacketSender().sendItems(-1, 148, 0, requiredIds[3]);
//            player.getPacketSender().sendItems(-1, 149, 0, requiredIds[4]);
//            player.getPacketSender().sendItems(-1, 150, 0, requiredIds[5]);
//            player.getPacketSender().sendItems(-1, 151, 0, new Item(-1));
//            player.getPacketSender().sendItems(-1, 152, 0, new Item(-1));
//        } else if (requiredIds.length == 7) {
//            player.getPacketSender().sendItems(-1, 145, 0, requiredIds[0]);
//            player.getPacketSender().sendItems(-1, 146, 0, requiredIds[1]);
//            player.getPacketSender().sendItems(-1, 147, 0, requiredIds[2]);
//            player.getPacketSender().sendItems(-1, 148, 0, requiredIds[3]);
//            player.getPacketSender().sendItems(-1, 149, 0, requiredIds[4]);
//            player.getPacketSender().sendItems(-1, 150, 0, requiredIds[5]);
//            player.getPacketSender().sendItems(-1, 151, 0, requiredIds[6]);
//            player.getPacketSender().sendItems(-1, 152, 0, new Item(-1));
//        } else if (requiredIds.length == 8) {
//            player.getPacketSender().sendItems(-1, 145, 0, requiredIds[0]);
//            player.getPacketSender().sendItems(-1, 146, 0, requiredIds[1]);
//            player.getPacketSender().sendItems(-1, 147, 0, requiredIds[2]);
//            player.getPacketSender().sendItems(-1, 148, 0, requiredIds[3]);
//            player.getPacketSender().sendItems(-1, 149, 0, requiredIds[4]);
//            player.getPacketSender().sendItems(-1, 150, 0, requiredIds[5]);
//            player.getPacketSender().sendItems(-1, 151, 0, requiredIds[6]);
//            player.getPacketSender().sendItems(-1, 152, 0, requiredIds[7]);
//        } else {
//            System.out.println("Ben fucked up...");
//        }
//    }
	private void clickItem(int component) {
		List<UpgradableItems> itemList = UpgradableItems.forCategory(player.upgradeManager.getLastCategory());
		for (UpgradableItems item : itemList) {
			int button = ITEM_COMPONENTS[item.getComponent()];
			if (button == component) {
				UpgradeManager.setSelectedItem(item);
				sendInfo(false);
				sendRequired();
			}
		}
	}


//    public static void checkInterface(Player player, int itemID) {
//        List<UpgradableItems> itemList = UpgradableItems.forCategory(UpgradeManager.getLastCategory());
//        for (UpgradableItems item : itemList) {
//            if (itemID == item.itemid) {
//                UpgradeManager.setSelectedItem(item);
//                sendInfo(false, player);
//                sendRequired(player);
//                return;
//            }
//        }
//        ObjType def = ObjType.get(itemID);
//        if (def == null)
//            return;
//        player.sendMessage(def.examine == null ? "This item has no examine" : def.examine + "<br> <col=6f0000> Low Alchemy Value: " + Duel.formatPrice(def.lowAlchValue) + ", High Alchemy Value: " + Duel.formatPrice(def.highAlchValue) + ".");
//    }

	private void upgrade() {
		if (upgrading) {
			player.sendMessage("You are already trying to upgrade an item!");
			return;
		}

		if (selectedItem == null) {
			return;
		}

		Item[] requiredIds = selectedItem.getRequired();
		if (player.getInventory().containsAll(true, requiredIds) == false) {
			player.sendMessage(Color.RED.wrap("You do not have the required items to do this."));
			return;
		}
		player.getInventory().removeAll(true, requiredIds);
		upgrading = true;
		player.lock();
		if (Random.rollPercent(selectedItem.chance)) {
			if (!player.SuccessfulUpgrades.containsKey(selectedItem.itemid)) {
				player.SuccessfulUpgrades.put(selectedItem.itemid, 1);
			} else {
				player.SuccessfulUpgrades.put(selectedItem.itemid, player.SuccessfulUpgrades.get(selectedItem.itemid) + 1);
			}
			player.getInventory().add(getSelectedItem().itemid);
			player.sendMessage(Color.DARK_GREEN.wrap("You have successfully upgraded a " + getSelectedItem().name + "."));
			Broadcast.WORLD.sendNews(player.getName() + " has just successfully upgraded a " + getSelectedItem().name + "!");
			player.unlock();
		} else {
			if (!player.FailedUpgrades.containsKey(selectedItem.itemid)) {
				player.FailedUpgrades.put(selectedItem.itemid, 1);
			} else {
				player.FailedUpgrades.put(selectedItem.itemid, player.FailedUpgrades.get(selectedItem.itemid) + 1);
			}
			player.unlock();
			player.sendMessage(Color.RED.wrap("You have failed to upgrade a " + getSelectedItem().name + "."));
		}
		upgrading = false;
		player.unlock();
	}
}
