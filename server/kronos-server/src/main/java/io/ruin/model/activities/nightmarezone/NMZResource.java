package io.ruin.model.activities.nightmarezone;

public enum NMZResource {
	FLAX(1779, 75),
	BUCKET_OF_SANE(1783, 200),
	SEAWEED(401, 200),
	DRAGON_SCALE_DUST(241, 750),
	COMPOST_POTION(6470, 5000),
	AIR_RUNE(556, 25),
	WATER_RUNE(555, 25),
	EARTH_RUNE(557, 25),
	FIRE_RUNE(554, 25),
	ESSENSE(1436, 60),
	PURE_ESSENSE(7936, 70),
	HERB_BOX(11738, 9500),
	VIAL_OF_WATER(227, 145),
	SCROLL_OF_REDIRECTION(11740, 775);
	public int id, price;

	NMZResource(int id, int price) {
		this.id = id;
		this.price = price;
	}

	public static NMZResource getUpgradable(int id) {
		for (NMZResource upgradable : values())
			if (upgradable.id == id)
				return upgradable;
		return null;
	}

	public static final NMZResource[] VALUES = values();
}