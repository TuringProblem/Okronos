package io.ruin.model.activities.duelarena;

import io.ruin.api.utils.ListUtils;
import io.ruin.model.entity.player.Player;

import java.util.List;

public enum DuelRule {

	NO_RANGED("No Ranged", 4, 37),
	NO_MELEE("No Melee", 5, 38),
	NO_MAGIC("No Magic", 6, 39),
	NO_SPECIALS("No Special Attack", 13, 40),
	FUN_WEAPONS("Fun weapons", 12, 41),
	NO_FORFEIT("Forfeit", 0, 42),
	NO_PRAYER("No Prayer", 9, 43),
	NO_DRINKS("No Drinks", 7, 44),
	NO_FOOD("No Food", 8, 45),
	NO_MOVEMENT("No Movement", 1, 46),
	OBSTACLES("Obstacles", 10, 47),
	NO_WEAPON_SWITCH("No Weapon Switching", 2, 48),
	SHOW_INVENTORIES("Show Inventories", 3, 49),

	NO_HELMS("Disable Head Slot", 14, 56, 67),
	NO_CAPES("Disable Back Slot", 15, 57, 68),
	NO_AMULETS("Disable Neck Slot", 16, 58, 69),
	NO_AMMO("Disable Ammo Slot", 27, 66, 77),
	NO_WEAPON("Disable Right Hand Slot", 17, 59, 70),
	NO_BODY("Disable Torso Slot", 18, 60, 71),
	NO_SHIELD("Disable Left Hand Slot", 19, 61, 72),
	NO_LEGS("Disable Leg Slot", 21, 62, 73),
	NO_RING("Disable Ring Slot", 26, 65, 76),
	NO_BOOTS("Disable Feet Slot", 24, 64, 75),
	NO_GLOVES("Disable Hand Slot", 23, 63, 74);

	public final String message;

	public final int bitValue, bitPos;

	private final List<Integer> childIds;

	DuelRule(String message, int bitPos, Integer... childIds) {
		this.message = message;
		this.bitPos = bitPos;
		this.bitValue = 1 << bitPos;
		this.childIds = ListUtils.toList(childIds);
	}

	public boolean isToggled(Player player) {
		Duel duel = player.getDuel();
		return duel.stage >= 3 && duel.isToggled(this);
	}

	public static DuelRule get(int childId) {
		for (DuelRule rule : values()) {
			if (rule.childIds.contains(childId))
				return rule;
		}
		return null;
	}

}
