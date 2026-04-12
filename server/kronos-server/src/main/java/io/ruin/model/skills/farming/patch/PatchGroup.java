package io.ruin.model.skills.farming.patch;

import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum PatchGroup {

	FALADOR(new Bounds(new Position(3055, 3308, 0), 25),
		Arrays.asList(PatchData.FALADOR_FLOWER, PatchData.FALADOR_HERB, PatchData.FALADOR_NORTH, PatchData.FALADOR_SOUTH, PatchData.FALADOR_COMPOST_BIN)),
	CATHERBY(new Bounds(new Position(2810, 3464, 0), 25),
		Arrays.asList(PatchData.CATHERBY_FLOWER, PatchData.CATHERBY_HERB, PatchData.CATHERBY_NORTH, PatchData.CATHERBY_SOUTH, PatchData.CATHERBY_COMPOST_BIN)),
	CANIFIS(new Bounds(new Position(3601, 3526, 0), 25),
		Arrays.asList(PatchData.CANIFIS_FLOWER, PatchData.CANIFIS_HERB, PatchData.CANIFIS_NORTH, PatchData.CANIFIS_SOUTH, PatchData.CANIFIS_COMPOST_BIN)),
	ARDOUGNE(new Bounds(new Position(2666, 3375, 0), 25),
		Arrays.asList(PatchData.ARDOUGNE_FLOWER, PatchData.ARDOUGNE_HERB, PatchData.ARDOUGNE_NORTH, PatchData.ARDOUGNE_SOUTH, PatchData.ARDOUGNE_COMPOST_BIN)),
	ZEAH(new Bounds(new Position(1814, 3486, 0), 25),
		Arrays.asList(PatchData.ZEAH_FLOWER, PatchData.ZEAH_HERB, PatchData.ZEAH_NORTH, PatchData.ZEAH_SOUTH, PatchData.ZEAH_COMPOST_BIN)),
	TROLLHEIM(new Bounds(new Position(2827, 3694, 0), 25),
		Collections.singletonList(PatchData.TROLLHEIM_HERB)),
	FALADOR_TREE(new Bounds(new Position(3004, 3373, 0), 25),
		Collections.singletonList(PatchData.FALADOR_TREE)),
	VARROCK(new Bounds(new Position(3229, 3459, 0), 32),
		Collections.singletonList(PatchData.VARROCK_TREE)),
	TAVERLEY(new Bounds(new Position(2936, 3438, 0), 20),
		Collections.singletonList(PatchData.TAVERLEY_TREE)),
	LUMBRIDGE(new Bounds(new Position(3193, 3231, 0), 32),
		Collections.singletonList(PatchData.LUMBRIDGE_TREE)),
	GNOME_STRONGHOLD(new Bounds(new Position(2461, 3444, 0), 64),
		Arrays.asList(PatchData.GNOME_TREE, PatchData.GNOME_FRUIT)),
	CATHERBY_BEACH(new Bounds(new Position(2860, 3433, 0), 20),
		Collections.singletonList(PatchData.CATHERBY_FRUIT)),
	BRIMHAVEN(new Bounds(new Position(2770, 3213, 0), 30),
		Arrays.asList(PatchData.BRIMHAVEN_FRUIT, PatchData.BRIMHAVEN_SPIRIT_TREE)),
	GNOME_VILLAGE(new Bounds(new Position(2490, 3180, 0), 25),
		Collections.singletonList(PatchData.VILLAGE_FRUIT)),
	LLETYA(new Bounds(new Position(2347, 3162, 0), 25),
		Collections.singletonList(PatchData.LLETYA_FRUIT)),
	VARROCK_BUSH(new Bounds(new Position(3181, 3358, 0), 25),
		Collections.singletonList(PatchData.VARROCK_BUSH)),
	RIMMINGTON_BUSH(new Bounds(new Position(2940, 3222, 0), 25),
		Collections.singletonList(PatchData.RIMMINGTON_BUSH)),
	ETCETERIA(new Bounds(new Position(2603, 3857, 0), 40), // spirit tree in this group
		Arrays.asList(PatchData.ETCETERIA_BUSH, PatchData.ETCETERIA_SPIRIT_TREE)),
	ARDOUGNE_BUSH(new Bounds(new Position(2617, 3226, 0), 25),
		Collections.singletonList(PatchData.ARDOUGNE_BUSH)),
	YANILLE_HOPS(new Bounds(new Position(2575, 3104, 0), 25),
		Collections.singletonList(PatchData.YANILLE_HOPS)),
	ENTRANA_HOPS(new Bounds(new Position(2810, 3336, 0), 25),
		Collections.singletonList(PatchData.ENTRANA_HOPS)),
	LUMBRIDGE_HOPS(new Bounds(new Position(3229, 3315, 0), 25),
		Collections.singletonList(PatchData.LUMBRIDGE_HOPS)),
	SEERS_HOPS(new Bounds(new Position(2666, 3525, 0), 25),
		Collections.singletonList(PatchData.SEERS_HOPS)),
	CALQUAT(new Bounds(new Position(2796, 3101, 0), 25),
		Collections.singletonList(PatchData.CALQUAT)),
	CACTUS(new Bounds(new Position(3315, 3203, 0), 25),
		Collections.singletonList(PatchData.CACTUS)),
	PORT_SARIM_SPIRIT_TREE(new Bounds(new Position(3060, 3258, 0), 25),
		Collections.singletonList(PatchData.PORT_SARIM_SPIRIT_TREE)),
	ZEAH_SPIRIT_TREE(new Bounds(new Position(1693, 3542, 0), 25),
		Collections.singletonList(PatchData.ZEAH_SPIRIT_TREE)),
	CANIFIS_MUSHROOM(new Bounds(new Position(3452, 3473, 0), 25),
		Collections.singletonList(PatchData.CANIFIS_MUSHROOM)),
	HESPORI_PATCH(new Bounds(new Position(1247, 10087, 0), 25),
		Collections.singletonList(PatchData.HESPORI_PATCH)),
	REDWOOD_PATCH(new Bounds(new Position(1228, 3754, 0), 25), Arrays.asList(
		PatchData.GUILD_REDWOOD_TREE_34051, PatchData.GUILD_REDWOOD_TREE_34052, PatchData.GUILD_REDWOOD_TREE_34053,
		PatchData.GUILD_REDWOOD_TREE_34054, PatchData.GUILD_REDWOOD_TREE_34055, PatchData.GUILD_REDWOOD_TREE_34056,
		PatchData.GUILD_REDWOOD_TREE_34057, PatchData.GUILD_REDWOOD_TREE_34058, PatchData.GUILD_REDWOOD_TREE_34059)),
	ANIMA_PATCH(new Bounds(new Position(1231, 3722, 0), 25), Collections.singletonList(PatchData.ANIMA_PATCH)),
	FARMING_GUILD(new Bounds(new Position(1249, 3738, 0), 65),
		Arrays.asList(PatchData.GUILD_FLOWER, PatchData.GUILD_HERB, PatchData.GUILD_NORTH,
			PatchData.GUILD_SOUTH, PatchData.GUILD_CACTUS, PatchData.GUILD_BUSH, PatchData.GUILD_TREE,
			PatchData.GUILD_CELASTRUS, PatchData.GUILD_FRUIT,
			PatchData.GUILD_COMPOST_BIN, PatchData.GUILD_SPIRIT_TREE));
//    DRAGON_STONE_ZONE(new Bounds(new Position(3694,6982,0), 64),
//            Arrays.asList(PatchData.DRAGONSTONEZONE_FLOWER, PatchData.DRAGONSTONEZONE_HERB1, PatchData.DRAGONSTONEZONE_HERB2, PatchData.DRAGONSTONEZONE_NORTH, PatchData.DRAGONSTONEZONE_SOUTH, PatchData.DRAGONSTONEZONE_CACTUS, PatchData.DRAGONSTONEZONE_TREE1, PatchData.DRAGONSTONEZONE_TREE2, PatchData.DRAGONSTONEZONE_TREE3, PatchData.DRAGONSTONEZONE_COMPOST_BIN1, PatchData.DRAGONSTONEZONE_COMPOST_BIN2));

	private Bounds bounds;
	private List<PatchData> patches;

	PatchGroup(Bounds bounds, List<PatchData> patches) {
		this.bounds = bounds;
		this.patches = patches;
	}

	public static final PatchGroup[] VALUES = values();

	public Bounds getBounds() {
		return bounds;
	}

	public List<PatchData> getPatches() {
		return patches;
	}


}
