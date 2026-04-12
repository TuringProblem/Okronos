package io.ruin.model.activities.newshop;

public enum ShopCategories {
	CONSUMABLES("Consumables", 10620),
	MISC("Miscellaneous", 10613),
	ORNAMENTS("Ornament kits", 10617),
	UTILITY("Utility", 10618),
	SKILLING("Skilling", 10610),
	EQUIPMENT("Equipment", 10614),
	WEAPONS("Weapons", 10617),
	ARMOUR("Armour", 10619),
	COSMETICS("Cosmetics", 10619),
	TREASURES("Treasures", 10615),
	RARES("Rares", 10616),
	BOXES("Boxes", 10619),
	BONDS("Bonds", 10615),
	UNTRIMMED("Untrimmed", 10610),
	TRIMMED("Trimmed", 10610),
	HOODS("Hoods", 10610),
	MASTERCAPES("Master Capes", 10610),
	MAXCAPES("Max Capes", 10610),
	MATERIALS("Materials", 10618),
	SCROLLS("Scrolls", 10612),
	SLAYER("Slayer", 10618),
	ACHIEVEMENT_ITEMS("Achiev Items", 10611),
	MEMBERSHIPS("Memberships", 10616),
	;
	String name;
	int scriptId;

	ShopCategories(String name, int scriptId) {
		this.name = name;
		this.scriptId = scriptId;
	}

	public String getName() {
		return name;
	}

	public int getScriptId() {
		return scriptId;
	}
}
