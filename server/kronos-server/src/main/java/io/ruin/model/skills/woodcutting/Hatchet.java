package io.ruin.model.skills.woodcutting;

import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.AchievementLamp;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.stat.StatType;

public enum Hatchet {

	BRONZE(1, 1351, 879, 3291, 9),
	IRON(1, 1349, 877, 3290, 11),
	STEEL(6, 1353, 875, 3289, 14),
	BLACK(6, 1361, 873, 3288, 18),
	MITHRIL(21, 1355, 871, 3287, 22),
	ADAMANT(31, 1357, 869, 3286, 26),
	RUNE(41, 1359, 867, 3285, 31),
	DRAGON(61, 6739, 2846, 3292, 42),
	INFERNAL(61, 13241, 2117, 3292, 45),
	CRYSTAL(71, 23673, 8324, 3292, 48),
	THIRD_AGE(61, 20011, 7264, 7264, 42);


	public final int levelReq, id, animationId, canoeAnimationId, points;

	Hatchet(int levelReq, int id, int animationId, int canoeAnimationId, int points) {
		this.levelReq = levelReq;
		this.id = id;
		this.animationId = animationId;
		this.canoeAnimationId = canoeAnimationId;
		this.points = points;
	}

	public static final Hatchet[] VALUES = values();

	private static Hatchet compare(Player player, Item item, Hatchet best) {
		if (item == null || item.getDef() == null)
			return best;
		Hatchet hatchet = item.getDef().hatchet;
		if (hatchet == null)
			return best;
		if (player.getStats().get(StatType.Woodcutting).fixedLevel < hatchet.levelReq)
			return best;
		if (best == null)
			return hatchet;
		if (hatchet.levelReq < best.levelReq)
			return best;
		return hatchet;
	}

	public static Hatchet find(Player player) {
		Hatchet bestHatchet = null;
		for (Item item : player.getInventory().getItems())
			bestHatchet = Hatchet.compare(player, item, bestHatchet);
		Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		return Hatchet.compare(player, weapon, bestHatchet);
	}

	public static void register() {
		ObjType.forEach(def -> {
			String name = def.name.toLowerCase();
			for (Hatchet hatchet : Hatchet.values()) {
				if (name.startsWith(hatchet.name().toLowerCase() + " axe") || name.equals("3rd age axe"))
					def.hatchet = hatchet;
			}
		});
	}

}
