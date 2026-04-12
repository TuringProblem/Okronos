package io.ruin.model.skills.slayer;

import io.ruin.model.item.Item;
import io.ruin.model.map.Position;

public enum NomadTask {
	KREE_ARRA(98, "Kree'Arra", new int[]{12443, 3162, 6492}, 100, 1, 3, 35, new Position(2882, 5311, 2), false),
	COMMANDER_ZILYANA(98, "Commander Zilyana", new int[]{12445, 2205, 6493}, 100, 1, 3, 35, new Position(2882, 5311, 2), false),
	GENERAL_GRAARDOR(98, "General Graardor", new int[]{2215, 6494, 12444}, 100, 1, 3, 35, new Position(2882, 5311, 2), false),
	KRIL_TSUTSAROTH(98, "K'ril Tsutsaroth", new int[]{12446, 6495, 3129}, 100, 1, 3, 35, new Position(2882, 5311, 2), false),
	DAGANNOTH_KINGS(98, "Dagannoth Kings", new int[]{2267, 6498, 12439, 2265, 12441, 6496, 12442, 6497, 2266}, 100, 1, 3, 35, new Position(1917, 4363, 0), false),
	THE_GIANT_MOLE(98, "Giant Mole", new int[]{5779, 6499}, 100, 1, 3, 35, new Position(1764, 5179, 0), false),
	THE_KALPHITE_QUEEN(98, "Kalphite Queen", new int[]{4304, 963, 128, 6501, 965, 6500, 4303}, 100, 1, 3, 35, new Position(3509, 9487, 2), false),
	THE_KING_BLACK_DRAGON(98, "King Black Dragon", new int[]{2642, 6502, 12440, 239}, 100, 1, 3, 35, new Position(2271, 4680, 0), false),
	CALLISTO(98, "Callisto", new int[]{6609, 6503}, 100, 1, 3, 35, null, true),
	VENENATIS(98, "Venenatis", new int[]{6610, 6504}, 100, 1, 3, 35, null, true),
	VET_ION(98, "Vet'ion", new int[]{6611, 6612, 12002}, 100, 1, 3, 35, null, true),
	THE_CHAOS_ELEMENTAL(98, "Chaos Elemental", new int[]{2054, 6505}, 100, 1, 3, 35, null, true),
	THE_CHAOS_FANATIC(98, "Chaos Fanatic", new int[]{2054, 6505}, 100, 1, 3, 35, null, true),
	CRAZY_ARCHAEOLOGIST(98, "Crazy Archaeologist", new int[]{6618}, 100, 1, 3, 35, null, true),
	SCORPIA(98, "Scorpia", new int[]{6615}, 100, 1, 3, 35, null, true),
	ZULRAH(98, "Zulrah", new int[]{2042, 2043, 2044}, 100, 1, 3, 35, new Position(2198, 3057, 0), false),
	ARGENTAVIS(98, "Argentavis", new int[]{763}, 100, 1, 3, 35, new Position(2143, 5526, 3), false),
	NEX(98, "Nex", new int[]{11278, 11279, 11280}, 100, 1, 3, 35, new Position(2905, 5204, 0), false),
	JAD(98, "TzTok-Jad", new int[]{3127, 6506}, 100, 1, 3, 5, new Position(2439, 5169, 0), false),
	NIGHTMARE(98, "The Nightmare", new int[]{9464, 9432, 9427, 9433, 9430, 9426, 9429, 9461, 9462, 378, 9425, 9431, 9460, 9428, 9463}, 100, 90, 3, 35, new Position(3808, 9746, 1), false),
	OPHIDIA(98, "Ophidia", new int[]{6477}, 100, 1, 3, 35, null, false),
	GALVEK(98, "Galvek", new int[]{8095, 8096, 8094, 8177, 8097, 8178, 8098, 8179, 11960}, 100, 1, 3, 35, new Position(1630, 5720, 2), false),
	PHANTOM_MUSPAH(98, "Phantom Muspah", new int[]{12079, 12078, 12082, 12077, 12080}, 100, 1, 3, 35, null, false),
	LEVIATHAN(98, "Leviathan", new int[]{12591, 12215, 12221, 12214, 12219}, 100, 1, 3, 35, null, false),
	DUKE_SUCELLUS(98, "Duke Sucellus", new int[]{12191}, 100, 1, 3, 35, null, false),
	VARDORVIS(98, "Vardorvis", new int[]{12223, 12228}, 100, 1, 3, 35, null, false),
	WHISPERER(98, "The Whisperer", new int[]{12204}, 100, 1, 3, 35, null, false),
	SARACHNIS(98, "Sarachnis", new int[]{8713}, 100, 1, 3, 35, new Position(1842, 9913, 0), false),
	CORPOREAL_BEAST(98, "Corporeal Beast", new int[]{319}, 100, 1, 3, 35, new Position(2966, 4382, 2), false),
	BARROWS_BROTHERS(98, "Barrows Brothers", new int[]{1672, 12316, 12322, 1675, 12319, 12325, 1673, 12323, 12317, 1674, 12324, 12318, 1676, 12320, 12326, 12327, 12321, 1677}, 100, 90, 3, 35, new Position(3565, 3310, 0), false),
	KRAKEN(98, "Kraken", new int[]{494, 6656, 6640}, 100, 87, 3, 35, new Position(2280, 10016, 0), false),
	THE_THERMONUCLEAR_SMOKE_DEVIL(98, "Thermonuclear Smoke Devils", new int[]{499}, 100, 93, 3, 35, new Position(2412, 3060, 0), false),
	CERBERUS(98, "Cerberus", new int[]{5866, 5863, 5862}, 100, 91, 3, 35, new Position(1291, 1252, 0), false),
	THE_ABYSSAL_SIRE(98, "Abyssal Sire", new int[]{11961, 5908, 5887, 5888, 5891, 5890, 5886, 5889}, 100, 85, 3, 35, new Position(3030, 4771, 0), false),
	VORKATH(98, "Vorkath", new int[]{8058, 8026, 8059, 11959, 8061, 8060}, 100, 1, 3, 35, new Position(2272, 4051, 0), false),
	ALCHEMICAL_HYDRA(98, "Alchemical Hydra", new int[]{8619, 8634, 8620, 11962, 8616, 8617, 8622, 8615, 8618, 8621}, 100, 95, 3, 35, new Position(1352, 10248, 0), false),
	;

	private String taskName;
	private int[] npcIds;
	private Position location;
	private Item[] requiredItems;
	private int combatRequirement;
	private int slayerRequirement;
	private int minAmount;
	private int maxAmount;
	private int taskId;
	public boolean wilderness;

	NomadTask(int taskId, String taskName, Position location, int[] npcIds, int combatRequirement, int slayerRequirement, int minAmount, int maxAmount, boolean wilderness) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.npcIds = npcIds;
		this.location = location;
		this.combatRequirement = combatRequirement;
		this.slayerRequirement = slayerRequirement;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.requiredItems = new Item[]{};
		this.wilderness = wilderness;
	}

	NomadTask(int taskId, String taskName, int[] npcIds, int combatRequirement, int slayerRequirement, int minAmount, int maxAmount, Position taskLocation, boolean wilderness) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.npcIds = npcIds;
		this.location = taskLocation;
		this.combatRequirement = combatRequirement;
		this.slayerRequirement = slayerRequirement;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.requiredItems = new Item[]{};
		this.wilderness = wilderness;
	}

	public int[] getNpcIds() {
		return npcIds;
	}

	public int getCombatRequirement() {
		return combatRequirement;
	}

	public int getSlayerRequirement() {
		return slayerRequirement;
	}

	public int getMinAmount() {
		return minAmount;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

	public String getTaskName() {
		return taskName;
	}

	public int getTaskId() {
		return taskId;
	}

	public Position getLocation() {
		return location;
	}

	public Item[] getRequiredItems() {
		return requiredItems;
	}
}
