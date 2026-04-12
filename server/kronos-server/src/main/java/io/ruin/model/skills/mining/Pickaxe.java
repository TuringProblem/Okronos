package io.ruin.model.skills.mining;

import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.stat.StatType;

public enum Pickaxe {

	BRONZE(1, 1265, 5, 625, 6753),
	IRON(1, 1267, 9, 626, 6754),
	STEEL(6, 1269, 14, 627, 6755),
	BLACK(11, 12297, 21, 6108, 3866),
	MITHRIL(21, 1273, 26, 629, 6757),
	ADAMANT(31, 1271, 30, 628, 6756),
	RUNE(41, 1275, 36, 624, 6752),
	GILDED(41, 23276, 36, 8313, 8313),
	DRAGON(61, 11920, 42, 7139, 6758),
	THIRD_AGE(61, 20014, 42, 7283, 7282),
	DRAGON_OR(61, 12797, 42, 642, 335),
	INFERNAL(61, 13243, 42, 4482, 4481),
	CRYSTAL(71, 23680, 42, 8347, 8343);;

	public final int levelReq, id, points, regularAnimationID, crystalAnimationID;

	Pickaxe(int levelReq, int id, int points, int regularAnimationID, int crystalAnimationID) {
		this.levelReq = levelReq;
		this.id = id;
		this.points = points;
		this.regularAnimationID = regularAnimationID;
		this.crystalAnimationID = crystalAnimationID;
	}

	private static Pickaxe compare(Player player, Item item, Pickaxe best) {
		if (item == null)
			return best;
		Pickaxe pickaxe = item.getDef().pickaxe;
		if (pickaxe == null)
			return best;
		if (player.getStats().get(StatType.Mining).fixedLevel < pickaxe.levelReq)
			return best;
		if (best == null)
			return pickaxe;
		if (pickaxe.levelReq < best.levelReq)
			return best;
		return pickaxe;
	}

	public static Pickaxe find(Player player) {
		Pickaxe bestPickaxe = null;
		for (Item item : player.getInventory().getItems())
			bestPickaxe = compare(player, item, bestPickaxe);
		Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		return compare(player, weapon, bestPickaxe);
	}

	public static void register() {
		for (Pickaxe pickaxe : values()) {
			ObjType def = ObjType.get(pickaxe.id);
			if (def != null) {
				def.pickaxe = pickaxe;
			}
		}
	}
}
