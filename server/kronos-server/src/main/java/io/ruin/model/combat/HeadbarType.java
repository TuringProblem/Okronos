package io.ruin.model.combat;

public enum HeadbarType {

	NORMAL_1(false, 0),
	STARDUST(false, 1),
	NORMAL_2(false, 2),
	SHIELD_1(false, 3),
	SHIELD_2(false, 4),
	SHIELD_3(false, 5),
	SHIELD_4(false, 6),
	ICE_DEMON(false, 7),
	OLM_RESTORE(false, 8),
	SHIELD_5(false, 9),
	SHIELD_6(false, 10),
	SHIELD_7(false, 11),
	NORMAL_3(true, 12),
	SHIELD_8(false, 13),
	SHIELD_9(false, 14),
	ZALCANO_1(false, 15),
	NORMAL_4(true, 16),
	NORMAL_5(false, 17),
	NORMAL_6(true, 18),
	NORMAL_7(false, 19),
	NORMAL_8(false, 20),
	NORMAL_9(false, 21),
	NORMAL_10(true, 22),
	ZALCANO_2(false, 23),
	ZALCANO_3(true, 24),
	ZALCANO_4(false, 25),
	ZALCANO_5(false, 26),
	ZALCANO_6(false, 27),
	ZALCANO_7(false, 28),
	ZALCANO_8(false, 29),
	ZALCANO_9(false, 30),
	TOTEM_1(false, 31),
	TOTEM_2(false, 32),
	TOTEM_3(false, 33),
	TOTEM_4(false, 34),
	TOTEM_5(false, 35),
	TOTEM_6(false, 36),
	TOTEM_7(false, 37),
	TOTEM_8(false, 38),
	TOTEM_9(false, 39),
	SHIELD_10(false, 40),
	ZALCANO_10(false, 41),
	TOTEM_10(false, 42),
	HIDDEN_HIT(false, 0x7ffe),
	DOUBLE_HIT(false, 0x7fff);
	/**
	 * corruption:0
	 * poison:2
	 * disease_unused:3
	 * disease:4
	 * venom:5
	 * heal:6
	 * block_lit:12
	 * block:13
	 * damage_lit:16
	 * damage:17
	 * shield_lit:18
	 * shield:19
	 * armour_lit:20
	 * armour:21
	 * charge_lit:22
	 * charge:23
	 * discharge_lit:24
	 * discharge:25
	 */

	public final boolean resetActions;
	public final int activeID;
	public final int dynamicID;

	HeadbarType(boolean resetActions, int activeID, int dynamicID) {
		this.resetActions = resetActions;

		this.activeID = activeID;
		this.dynamicID = dynamicID;
	}

	HeadbarType(boolean resetActions, int activeID) {
		this(resetActions, activeID, -1);
	}

}