package io.ruin.model.item.actions.impl.combine;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;

public enum WeaponPoison {


	BRONZE_DAGGER(1205, 1221, 5670, 5688, false),
	IRON_DAGGER(1203, 1219, 5668, 5686, false),
	STEEL_DAGGER(1207, 1223, 5672, 5690, false),
	BLACK_DAGGER(1217, 1233, 5682, 5700, false),
	WHITE_DAGGER(6591, 6593, 6595, 6597, false),
	MITHRIL_DAGGER(1209, 1225, 5674, 5692, false),
	ADAMANT_DAGGER(1211, 1227, 5676, 5694, false),
	RUNE_DAGGER(1213, 1229, 5678, 5696, false),
	DRAGON_DAGGER(1215, 1231, 5680, 5698, false),
	BONE_DAGGER(8872, 8874, 8876, 8878, false),
	KERIS_DAGGER(10581, 10582, 10583, 10584, false),
	ABYSSAL_DAGGER(13265, 13267, 13269, 13271, false),

	BRONZE_SPEAR(1237, 1251, 5704, 5718, false),
	IRON_SPEAR(1239, 1253, 5706, 5720, false),
	STEEL_SPEAR(1241, 1255, 5708, 5722, false),
	BLACK_SPEAR(4580, 4582, 5734, 5736, false),
	MITHRIL_SPEAR(1243, 1257, 5710, 5724, false),
	ADAMANT_SPEAR(1245, 1259, 5712, 5726, false),
	RUNE_SPEAR(1247, 1261, 5714, 5728, false),
	DRAGON_SPEAR(1249, 1263, 5716, 5730, false),

	BRONZE_HASTA(11367, 11379, 11382, 11384, false),
	IRON_HASTA(11369, 11386, 11389, 11391, false),
	STEEL_HASTA(11371, 11393, 11396, 11398, false),
	MITHRIL_HASTA(11373, 11400, 11403, 11405, false),
	ADAMANT_HASTA(11375, 11407, 11410, 11412, false),
	RUNE_HASTA(11377, 11414, 11417, 11419, false),
	DRAGON_HASTA(22731, 22734, 22737, 22740, false),

	BRONZE_KNIFE(864, 870, 5654, 5661, true),
	IRON_KNIFE(863, 871, 5655, 5662, true),
	STEEL_KNIFE(865, 872, 5656, 5663, true),
	BLACK_KNIFE(869, 874, 5658, 5665, true),
	MITHRIL_KNIFE(866, 873, 5657, 5664, true),
	ADAMANT_KNIFE(867, 875, 5659, 5666, true),
	RUNE_KNIFE(868, 876, 5660, 5667, true),
	DRAGON_KNIFE(22804, 22806, 22808, 22810, true),

	BRONZE_DART(806, 812, 5628, 5635, true),
	IRON_DART(807, 813, 5629, 5636, true),
	STEEL_DART(808, 814, 5630, 5637, true),
	BLACK_DART(3093, 3094, 5631, 5638, true),
	MITHRIL_DART(809, 815, 5632, 5639, true),
	ADAMANT_DART(810, 816, 5633, 5640, true),
	RUNE_DART(811, 817, 5634, 5641, true),
	AMETHYST_DART(25849, 25851, 25855, 25857, true),
	DRAGON_DART(11230, 11231, 11233, 11234, true),

	BRONZE_ARROW(882, 883, 5616, 5622, true),
	IRON_ARROW(884, 885, 5617, 5623, true),
	STEEL_ARROW(886, 887, 5618, 5624, true),
	MITHRIL_ARROW(888, 889, 5619, 5625, true),
	ADAMANT_ARROW(890, 891, 5620, 5626, true),
	RUNE_ARROW(892, 893, 5621, 5627, true),
	AMETHYST_ARROW(21326, 21332, 21334, 21336, true),
	DRAGON_ARROW(11212, 11227, 11228, 11229, true),

	BRONZE_JAVELIN(825, 831, 5642, 5648, true),
	IRON_JAVELIN(826, 832, 5643, 5649, true),
	STEEL_JAVELIN(827, 833, 5644, 5650, true),
	MITHRIL_JAVELIN(828, 834, 5645, 5651, true),
	ADAMANT_JAVELIN(829, 835, 5646, 5652, true),
	RUNE_JAVELIN(830, 836, 5647, 5653, true),
	AMETHYST_JAVELIN(21318, 21320, 21322, 21324, true),
	DRAGON_JAVELIN(19484, 19486, 19488, 19490, true),

	BRONZE_BOLTS(877, 878, 6061, 6062, true),
	BLURITE_BOLTS(9139, 9286, 9293, 9300, true),
	IRON_BOLTS(9140, 9287, 9294, 9301, true),
	STEEL_BOLTS(9141, 9288, 9295, 9302, true),
	MITHRIL_BOLTS(9142, 9289, 9296, 9303, true),
	ADAMANT_BOLTS(9143, 9290, 9297, 9304, true),
	RUNE_BOLTS(9144, 9291, 9298, 9305, true),
	DRAGON_BOLTS(21905, 21924, 21926, 21928, true);


	public final int primaryId, secondaryId, thirdId, fourthId;
	public final boolean stacked;

	public static final int WEAPON_POISON = 187;
	public static final int WEAPON_POISON_PLUS = 5937;
	public static final int WEAPON_POISON_PLUS_PLUS = 5940;
	public static final int CLEANING_CLOTH = 3188;

	WeaponPoison(int primaryId, int secondaryId, int thirdId, int fourthId, boolean stacked) {
		this.primaryId = primaryId;
		this.secondaryId = secondaryId;
		this.thirdId = thirdId;
		this.fourthId = fourthId;
		this.stacked = stacked;
	}

	private static void makePoison(Player player, Item wepPoison, Item item, int poisonedID, boolean stacked) {
		player.sendMessage("You poison the " + item.getDef().name.substring(item.getDef().name.lastIndexOf(" ") + 1));
		int amt = player.getInventory().count(item.getId());

		if (player.breakVials)
			wepPoison.remove();
		else
			wepPoison.setId(229);

		if (stacked) {
			if (amt < 10) {
				item.remove();
				player.getInventory().add(poisonedID, amt);
			} else {
				item.remove(10);
				player.getInventory().add(poisonedID, 10);
			}
		} else
			item.setId(poisonedID);

	}

	private static void removePoison(Player player, Item cloth, Item item, int primaryId, boolean stacked) {
		int amt = player.getInventory().count(item.getId());

		if (stacked) {
			if (amt < 10) {
				item.remove();
				player.getInventory().add(primaryId, amt);
			} else {
				item.remove(10);
				player.getInventory().add(primaryId, 10);
			}
		} else
			item.setId(primaryId);
		player.sendMessage("You gingerly wipe the poison off of the " + item.getDef().name.substring(item.getDef().name.lastIndexOf(" ") + 1));
	}

	public static void register() {

		for (WeaponPoison poison : values()) {
			ItemItemAction.register(WEAPON_POISON, poison.primaryId, (player, primary, secondary) -> makePoison(player, primary, secondary, poison.secondaryId, poison.stacked));
			ItemItemAction.register(WEAPON_POISON_PLUS, poison.primaryId, (player, primary, secondary) -> makePoison(player, primary, secondary, poison.thirdId, poison.stacked));
			ItemItemAction.register(WEAPON_POISON_PLUS, poison.secondaryId, (player, primary, secondary) -> makePoison(player, primary, secondary, poison.thirdId, poison.stacked));
			ItemItemAction.register(WEAPON_POISON_PLUS_PLUS, poison.primaryId, (player, primary, secondary) -> makePoison(player, primary, secondary, poison.fourthId, poison.stacked));
			ItemItemAction.register(WEAPON_POISON_PLUS_PLUS, poison.secondaryId, (player, primary, secondary) -> makePoison(player, primary, secondary, poison.fourthId, poison.stacked));
			ItemItemAction.register(WEAPON_POISON_PLUS_PLUS, poison.thirdId, (player, primary, secondary) -> makePoison(player, primary, secondary, poison.fourthId, poison.stacked));

			ItemItemAction.register(CLEANING_CLOTH, poison.secondaryId, (player, primary, secondary) -> removePoison(player, primary, secondary, poison.primaryId, poison.stacked));
			ItemItemAction.register(CLEANING_CLOTH, poison.thirdId, (player, primary, secondary) -> removePoison(player, primary, secondary, poison.primaryId, poison.stacked));
			ItemItemAction.register(CLEANING_CLOTH, poison.fourthId, (player, primary, secondary) -> removePoison(player, primary, secondary, poison.primaryId, poison.stacked));

		}
	}

}
