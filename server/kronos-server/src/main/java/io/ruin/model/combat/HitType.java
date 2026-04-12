package io.ruin.model.combat;

public enum HitType {

	CORRUPTION(false, 0),
	EMPTY_1(false, 1),
	POISON(false, 2),
	EMPTY_3(false, 3),
	DISEASE(false, 4),
	VENOM(false, 5),
	HEAL(false, 6),
	EMPTY_7(false, 7),
	EMPTY_8(false, 8),
	EMPTY_9(false, 9),
	EMPTY_10(false, 10),
	EMPTY_11(false, 11),
	BLOCKED(true, 12),
	BLOCK_OTHER(false, 13),
	EMPTY_14(false, 14),
	EMPTY_15(false, 15),
	DAMAGE(true, 16),
	DAMAGE_OTHER(false, 17),
	DAMAGE_SHIELD_ME(true, 18),
	DAMAGE_SHIELD_OTHER(false, 19),
	DAMAGE_ZALCANO_ME(false, 20),
	DAMAGE_ZALCANO_OTHER(false, 21),
	HEAL_TOTEM_ME(true, 22),
	HEAL_TOTEM_OTHER(false, 23),
	DAMAGE_TOTEM_ME(true, 24),
	DAMAGE_TOTEM_OTHER(false, 25),
	HIDDEN_HIT(false, 0x7ffe),
	DOUBLE_HIT(false, 0x7fff),
	MAX_HIT(false, 43);

	public final boolean resetActions;
	public final int activeID;
	public final int dynamicID;

	HitType(boolean resetActions, int activeID, int dynamicID) {
		this.resetActions = resetActions;
		this.activeID = activeID;
		this.dynamicID = dynamicID;
	}

	HitType(boolean resetActions, int activeID) {
		this(resetActions, activeID, -1);
	}

}
