package io.ruin.model.skills.farming.patch;

import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.map.object.actions.ObjectAction;

public enum PatchData {

	FALADOR_HERB(8150, VarPlayerRepository.FARMING_PATCH_4, 6),
	CATHERBY_HERB(8151, VarPlayerRepository.FARMING_PATCH_4, 6),
	ARDOUGNE_HERB(8152, VarPlayerRepository.FARMING_PATCH_4, 6),
	CANIFIS_HERB(8153, VarPlayerRepository.FARMING_PATCH_4, 6),
	TROLLHEIM_HERB(18816, VarPlayerRepository.FARMING_PATCH_1, 6),
	ZEAH_HERB(27115, VarPlayerRepository.FARMING_PATCH_4, 6),
	//    DRAGONSTONEZONE_HERB2(50016, Config.FARMING_PATCH_4, 6),
//    DRAGONSTONEZONE_HERB1(50017, Config.FARMING_PATCH_4, 6),
	GUILD_HERB(33979, VarPlayerRepository.FARMING_COMPOST_BIN, 6),

	FALADOR_FLOWER(7847, VarPlayerRepository.FARMING_PATCH_3, 5),
	CATHERBY_FLOWER(7848, VarPlayerRepository.FARMING_PATCH_3, 5),
	ARDOUGNE_FLOWER(7849, VarPlayerRepository.FARMING_PATCH_3, 5),
	CANIFIS_FLOWER(7850, VarPlayerRepository.FARMING_PATCH_3, 5),
	ZEAH_FLOWER(27111, VarPlayerRepository.FARMING_PATCH_3, 5),
	//    DRAGONSTONEZONE_FLOWER(50015, Config.FARMING_PATCH_3, 5),
	GUILD_FLOWER(33649, VarPlayerRepository.FARMING_PATCH_6, 5),

	FALADOR_NORTH(8550, VarPlayerRepository.FARMING_PATCH_1, 0),
	FALADOR_SOUTH(8551, VarPlayerRepository.FARMING_PATCH_2, 0),
	CATHERBY_NORTH(8552, VarPlayerRepository.FARMING_PATCH_1, 0),
	CATHERBY_SOUTH(8553, VarPlayerRepository.FARMING_PATCH_2, 0),
	ARDOUGNE_NORTH(8554, VarPlayerRepository.FARMING_PATCH_1, 0),
	ARDOUGNE_SOUTH(8555, VarPlayerRepository.FARMING_PATCH_2, 0),
	CANIFIS_NORTH(8556, VarPlayerRepository.FARMING_PATCH_1, 0),
	CANIFIS_SOUTH(8557, VarPlayerRepository.FARMING_PATCH_2, 0),
	ZEAH_NORTH(27113, VarPlayerRepository.FARMING_PATCH_1, 0),
	ZEAH_SOUTH(27114, VarPlayerRepository.FARMING_PATCH_2, 0),
	//    DRAGONSTONEZONE_NORTH(50021, Config.FARMING_PATCH_1, 0),
//    DRAGONSTONEZONE_SOUTH(50022, Config.FARMING_PATCH_2, 0),
	GUILD_NORTH(33694, VarPlayerRepository.FARMING_PATCH_3, 0),
	GUILD_SOUTH(33693, VarPlayerRepository.FARMING_PATCH_4, 0),

	TAVERLEY_TREE(8388, "saplings", VarPlayerRepository.FARMING_PATCH_1, 2),
	FALADOR_TREE(8389, "saplings", VarPlayerRepository.FARMING_PATCH_1, 2),
	VARROCK_TREE(8390, "saplings", VarPlayerRepository.FARMING_PATCH_1, 2),
	LUMBRIDGE_TREE(8391, "saplings", VarPlayerRepository.FARMING_PATCH_1, 2),
	GNOME_TREE(19147, "saplings", VarPlayerRepository.FARMING_PATCH_1, 2),
	//    DRAGONSTONEZONE_TREE1(50018, "saplings", Config.FARMING_PATCH_1, 2),
//    DRAGONSTONEZONE_TREE2(50019, "saplings", Config.FARMING_PATCH_1, 2),
//    DRAGONSTONEZONE_TREE3(50020, "saplings", Config.FARMING_PATCH_1, 2),
	GUILD_TREE(33732, "saplings", VarPlayerRepository.FARMING_PATCH_5, 2),
	GUILD_REDWOOD_TREE_34051(34051, "saplings", VarPlayerRepository.FARMING_PATCH_7, 0), //TODO Data has 9 patches from 34051 - 34059
	GUILD_REDWOOD_TREE_34052(34052, "saplings", VarPlayerRepository.FARMING_PATCH_7, 0),
	GUILD_REDWOOD_TREE_34053(34053, "saplings", VarPlayerRepository.FARMING_PATCH_7, 0),
	GUILD_REDWOOD_TREE_34054(34054, "saplings", VarPlayerRepository.FARMING_PATCH_7, 0),
	GUILD_REDWOOD_TREE_34055(34055, "saplings", VarPlayerRepository.FARMING_PATCH_7, 0),
	GUILD_REDWOOD_TREE_34056(34056, "saplings", VarPlayerRepository.FARMING_PATCH_7, 0),
	GUILD_REDWOOD_TREE_34057(34057, "saplings", VarPlayerRepository.FARMING_PATCH_7, 0),
	GUILD_REDWOOD_TREE_34058(34058, "saplings", VarPlayerRepository.FARMING_PATCH_7, 0),
	GUILD_REDWOOD_TREE_34059(34059, "saplings", VarPlayerRepository.FARMING_PATCH_7, 0),
	ANIMA_PATCH(33998, VarPlayerRepository.FARMING_PATCH_12, 7), //TODO
	GUILD_CELASTRUS(34629, "saplings", VarPlayerRepository.FARMING_PATCH_11, 7),
	HESPORI_PATCH(34630, VarPlayerRepository.FARMING_PATCH_8, 0),

	GNOME_FRUIT(7962, "saplings", VarPlayerRepository.FARMING_PATCH_2, 3),
	VILLAGE_FRUIT(7963, "saplings", VarPlayerRepository.FARMING_PATCH_1, 3),
	BRIMHAVEN_FRUIT(7964, "saplings", VarPlayerRepository.FARMING_PATCH_1, 3),
	CATHERBY_FRUIT(7965, "saplings", VarPlayerRepository.FARMING_PATCH_1, 3),
	LLETYA_FRUIT(26579, "saplings", VarPlayerRepository.FARMING_PATCH_1, 3),
	GUILD_FRUIT(34007, "saplings", VarPlayerRepository.FARMING_PATCH_9, 3),

	FALADOR_COMPOST_BIN(7836, VarPlayerRepository.FARMING_COMPOST_BIN, -1),
	CATHERBY_COMPOST_BIN(7837, VarPlayerRepository.FARMING_COMPOST_BIN, -1),
	CANIFIS_COMPOST_BIN(7838, VarPlayerRepository.FARMING_COMPOST_BIN, -1),
	ARDOUGNE_COMPOST_BIN(7839, VarPlayerRepository.FARMING_COMPOST_BIN, -1),
	ZEAH_COMPOST_BIN(27112, VarPlayerRepository.FARMING_COMPOST_BIN, -1),
	//    DRAGONSTONEZONE_COMPOST_BIN1(50013, Config.FARMING_COMPOST_BIN, -1),
//    DRAGONSTONEZONE_COMPOST_BIN2(50014, Config.FARMING_COMPOST_BIN, -1),
	GUILD_COMPOST_BIN(34631, VarPlayerRepository.FARMING_COMPOST_BIN, -1),


	VARROCK_BUSH(7577, VarPlayerRepository.FARMING_PATCH_1, 4),
	RIMMINGTON_BUSH(7578, VarPlayerRepository.FARMING_PATCH_1, 4),
	ETCETERIA_BUSH(7579, VarPlayerRepository.FARMING_PATCH_1, 4),
	ARDOUGNE_BUSH(7580, VarPlayerRepository.FARMING_PATCH_1, 4),
	GUILD_BUSH(34006, VarPlayerRepository.FARMING_PATCH_2, 4),

	YANILLE_HOPS(8173, VarPlayerRepository.FARMING_PATCH_1, 1),
	ENTRANA_HOPS(8174, VarPlayerRepository.FARMING_PATCH_1, 1),
	LUMBRIDGE_HOPS(8175, VarPlayerRepository.FARMING_PATCH_1, 1),
	SEERS_HOPS(8176, VarPlayerRepository.FARMING_PATCH_1, 1),

	CALQUAT(7807, "saplings", VarPlayerRepository.FARMING_PATCH_1, 7),
	CACTUS(7771, VarPlayerRepository.FARMING_PATCH_1, 7),
	CANIFIS_MUSHROOM(8337, VarPlayerRepository.FARMING_PATCH_1, 7),
	//    DRAGONSTONEZONE_CACTUS(50012, Config.FARMING_PATCH_1, 7),
	GUILD_CACTUS(33761, VarPlayerRepository.FARMING_PATCH_10, 7),


	BRIMHAVEN_SPIRIT_TREE(8383, "saplings", VarPlayerRepository.FARMING_PATCH_2, 7),
	PORT_SARIM_SPIRIT_TREE(8338, "saplings", VarPlayerRepository.FARMING_PATCH_1, 7),
	ETCETERIA_SPIRIT_TREE(8382, "saplings", VarPlayerRepository.FARMING_PATCH_2, 7),
	ZEAH_SPIRIT_TREE(27116, "saplings", VarPlayerRepository.FARMING_PATCH_1, 7),
	GUILD_SPIRIT_TREE(33733, "saplings", VarPlayerRepository.FARMING_PATCH_1, 7);

	PatchData(int objectId, VarPlayerRepository config, int guideChildId) {
		this.objectId = objectId;
		this.config = config;
		this.type = "seeds";
		this.guideChildId = guideChildId;
	}


	PatchData(int objectId, String type, VarPlayerRepository config, int guideChildId) {
		this.objectId = objectId;
		this.config = config;
		this.type = type;
		this.guideChildId = guideChildId;
	}

	public int getObjectId() {
		return objectId;
	}

	public VarPlayerRepository getConfig() {
		return config;
	}

	public int getGuideChildId() {
		return guideChildId;
	}

	public String getType() {
		return type;
	}

	private final int objectId;
	private final VarPlayerRepository config;
	private final String type;
	private final int guideChildId;

	public static void register() {
		for (PatchData pd : values()) {
			for (int i = 1; i < 6; i++) {
				final int opt = i;
				ObjectAction.register(pd.getObjectId(), opt, ((player, obj) -> player.getFarming().handleObject(obj, opt)));
			}
			ItemObjectAction.register(pd.getObjectId(), ((player, item, obj) -> player.getFarming().itemOnPatch(item, obj)));
		}

	}

}
