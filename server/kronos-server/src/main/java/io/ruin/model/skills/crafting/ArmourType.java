package io.ruin.model.skills.crafting;

import io.ruin.cache.ItemID;

public enum ArmourType {

	LEATHER(1741, "leather armour", "leather"),
	HARD_LEATHER(1743, "hard leather armour", "hard leather"),
	STUDDED_BODY(ItemID.LEATHER_BODY, "studded leather body", "leather"),
	STUDDED_CHAPS(ItemID.LEATHER_CHAPS, "studded leather chaps", "leather"),
	SPIKY_VAMBRANCES(10113, "spiky vambraces", "kebbit claws"),
	CRAB_HELMET(7538, "crab helmets", "fresh crab shell"),
	CRAB_CLAW(7536, "crab claws", "fresh crab claw"),
	GREEN_DRAGONHIDE(1745, "green dragon hide armour", "green dragon leather"),
	BLUE_DRAGONHIDE(2505, "blue dragon hide armour", "blue dragon leather"),
	RED_DRAGONHIDE(2507, "red dragon hide armour", "red dragon leather"),
	BLACK_DRAGONHIDE(2509, "black dragon hide armour", "black dragon leather"),
	SNAKESKIN(6289, "snakeskin armour", "snake skin"),
	YAK_HIDE(10818, "yak hide armour", "yak hide"),
	;


	public final int leather;
	public final String groupName, leatherName;

	ArmourType(int leather, String groupName, String leatherName) {
		this.leather = leather;
		this.groupName = groupName;
		this.leatherName = leatherName;
	}
}
