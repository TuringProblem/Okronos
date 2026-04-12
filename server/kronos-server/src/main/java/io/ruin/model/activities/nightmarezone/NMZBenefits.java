package io.ruin.model.activities.nightmarezone;

public enum NMZBenefits {
	OVERLOAD(11733, 20000),
	ABSORPTION(11737, 1000);
	public int id, price;

	NMZBenefits(int id, int price) {
		this.id = id;
		this.price = price;
	}

	public static NMZBenefits getBenefits(int id) {
		for (NMZBenefits benefits : values())
			if (benefits.id == id)
				return benefits;
		return null;
	}

	public static final NMZBenefits[] VALUES = values();
}
