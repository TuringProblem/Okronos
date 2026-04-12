package io.ruin.model.activities.blastfurnace;

public enum DispenserBar {
	BRONZE(Bars.BRONZE.bar, 1, 0),
	IRON(Bars.IRON.bar, 15, 0),
	SILVER(Bars.SILVER.bar, 20, 0),
	STEEL(Bars.STEEL.bar, 30, 0),
	GOLD(Bars.GOLD.bar, Bars.GOLD.levelReq, 15),
	MITHRIL(Bars.MITHRIL.bar, Bars.MITHRIL.levelReq, 30),
	ADAMANT(Bars.ADAMANT.bar, Bars.ADAMANT.levelReq, 45),
	RUNITE(Bars.RUNE.bar, Bars.RUNE.levelReq, 75);

	DispenserBar(int itemId, int levelReq, double xp) {
		this.itemId = itemId;
		this.levelReq = levelReq;
		this.xp = xp;
	}

	private int itemId, levelReq;
	private double xp;

	public int getItemId() {
		return itemId;
	}

	public int getLevelReq() {
		return levelReq;
	}

	public double getXp() {
		return xp;
	}

	public static DispenserBar get(int level) {
		if (level >= RUNITE.levelReq)
			return RUNITE;
		if (level >= ADAMANT.levelReq)
			return ADAMANT;
		if (level >= MITHRIL.levelReq)
			return MITHRIL;
		if (level >= GOLD.levelReq)
			return GOLD;
		if (level >= STEEL.levelReq)
			return STEEL;
		if (level >= SILVER.levelReq)
			return SILVER;
		if (level >= IRON.levelReq)
			return IRON;
		if (level >= BRONZE.levelReq)
			return BRONZE;
		return null;
	}
}
