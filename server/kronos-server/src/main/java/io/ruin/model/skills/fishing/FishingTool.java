package io.ruin.model.skills.fishing;

import io.ruin.cache.ObjType;

public enum FishingTool {

	SMALL_FISHING_NET(303, 621),
	BIG_FISHING_NET(305, 620),
	FISHING_ROD(307, 313, 622, 623),
	FLY_FISHING_ROD(309, 314, 622, 623),
	BARBARIAN_ROD(11323, 314, 622, 623),
	LOBSTER_POT(301, 619),
	HARPOON(311, 618),
	OILY_FISHING_ROD(1585, 313, 622, 623),
	DARK_CRAB_POT(301, 11940, 619),
	ANGLER_ROD(307, 13431, 622, 623),
	PEARL_FISHING_ROD(22846, 313, 8188, 8191),
	PEARL_FLY_FISHING_ROD(22844, 314, 8189, 8192),
	PEARL_BARBARIAN_ROD(22842, 314, 8190, 8193),
	DRAGON_HARPOON(21028, 7401),
	CRYSTAL_HARPOON(23762, 8336),
	INFERNAL_HARPOON(21031, 7402),
	KARAMBWAN_VESSEL(3157, 3150, 1193, 1193);

	public final int id;

	public final int startAnimationId, loopAnimationId;

	public final int secondaryId;

	public final String primaryName, secondaryName;

	FishingTool(int id, int animationId) {
		this(id, -1, animationId, animationId);
	}

	FishingTool(int id, int secondaryId, int animationId) {
		this(id, secondaryId, animationId, animationId);
	}

	FishingTool(int id, int secondaryId, int startAnimationId, int loopAnimationId) {
		this.id = id;
		this.secondaryId = secondaryId;
		this.startAnimationId = startAnimationId;
		this.loopAnimationId = loopAnimationId;
		this.primaryName = ObjType.get(id).name.toLowerCase();
		this.secondaryName = secondaryId == -1 ? null : ObjType.get(secondaryId).name.toLowerCase();
	}
}
