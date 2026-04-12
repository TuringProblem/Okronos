package io.ruin.model.entity.player;

public enum BeginnerUpgrades {
	MELEE_HELM(0, 5, new int[]{1153, 1157, 1165, 1159, 1161, 1163}),
	MELEE_BODY(0, 5, new int[]{1115, 1119, 1125, 1121, 1123, 1127}),
	MELEE_LEGS(0, 5, new int[]{1067, 1069, 1077, 1071, 1073, 1079}),
	MAGIC_HAT(0, 2, new int[]{6109, 13385, 4089}),
	MAGIC_TOP(0, 2, new int[]{6107, 13387, 4091}),
	MAGIC_BOTTOM(0, 2, new int[]{6108, 13389, 4093}),
	RANGE_HAT(0, 1, new int[]{1167, 1169}),
	RANGE_TOP(0, 5, new int[]{1129, 1133, 1135, 2499, 2501, 2503}),
	RANGE_LEGS(0, 5, new int[]{1095, 1097, 1099, 2493, 2495, 2497}),
	RANGE_WEAPON(0, 5, new int[]{841, 843, 849, 853, 857, 861}),
	MELEE_WEAPON(0, 6, new int[]{1323, 1325, 1327, 1329, 1331, 1333, 4587}),
	;

	int totalUpgrades;
	int currentUpgrades = 0;
	int[] upgrades;


	private BeginnerUpgrades(int currUpgrades, int totalUpgrades, int[] upgrade) {
		this.totalUpgrades = totalUpgrades;
		this.upgrades = upgrade;
		this.currentUpgrades = currUpgrades;

	}

	public int GetUpgradeID(int currUpgrades) {
		return upgrades[currUpgrades];
	}

	public int HandleUpgrade(int currUpgrades) {
		if (currUpgrades >= totalUpgrades) return currUpgrades;

		currUpgrades++;
		return GetUpgradeID(currUpgrades);
	}


}
