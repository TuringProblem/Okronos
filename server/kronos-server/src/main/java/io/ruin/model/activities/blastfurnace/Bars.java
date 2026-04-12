package io.ruin.model.activities.blastfurnace;

public enum Bars {
	BRONZE(2349, "bronze bar", 1, 17.5),
	IRON(2351, "iron bar", 15, 25.0),
	SILVER(2355, "silver bar", 20, 30.0),
	STEEL(2353, "steel bar", 30, 35.0),
	GOLD(2357, "gold bar", 40, 65.0),
	MITHRIL(2359, "mithril bar", 55, 75.0),
	LOVAKITE(13354, "lovakite bar", 65, 10.0),
	ADAMANT(2361, "adamantite bar", 70, 85.0),
	RUNE(2363, "rune bar", 85, 100.0),
	;

	public final int bar, levelReq;
	public final String barname;
	public final double experience;
	public final int[] multiOre;
	public final int[] multiExp;

	Bars(int bar, String barname, int levelReq, double experience) {
		this.bar = bar;
		this.barname = barname;
		this.levelReq = levelReq;
		this.experience = experience;
		this.multiOre = null;
		this.multiExp = null;
	}
}


