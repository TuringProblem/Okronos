package io.ruin.model.skills.slayer;

import io.ruin.model.entity.player.Player;
import io.ruin.model.stat.StatType;

import java.util.Arrays;

public enum SlayerBoss {
	KREE_ARRA(0, 1, 3162, 6492),
	COMMANDER_ZILYANA(1, 1, 2205, 6493),
	GENERAL_GRAARDOR(2, 1, 2215, 6494),
	KRIL_TSUTSAROTH(3, 1, 3129, 6495),
	DAGANNOTH_KINGS(4, 1, 2266, 6497, 2267, 6498, 2265, 6496),
	THE_GIANT_MOLE(5, 1, 5779, 6499),
	THE_KALPHITE_QUEEN(6, 1, 963, 965),
	THE_KING_BLACK_DRAGON(7, 1, 239, 6502),
	CALLISTO(true, 8, 6503, 6609),
	VENENATIS(true, 9, 6504, 6610),
	VET_ION(true, 10, 6611),
	THE_CHAOS_ELEMENTAL(true, 11, 2054, 6505),
	THE_CHAOS_FNATIC(true, 12, 6619),
	CRAZY_ARCHAEOLOGIST(true, 13, 6618),
	SCORPIA(true, 14, 6615),
	ZULRAH(15, 1, 2042, 2043, 2044),
	BARROWS_BROTHERS(16, 1, 1675, 1673, 1677, 1672, 1676, 1674),
	THE_CAVE_KRAKEN(17, 87, 492, 493, 494, 496),
	THE_THERMONUCLEAR_SMOKE_DEVIL(18, 93, 499),
	CERBERUS(19, 91, 5862, 5863, 5866),
	THE_ABYSSAL_SIRE(20, 85, 5886, 5887, 5888, 5889, 5890, 5891, 5908),
	//    THE_GROTESQUE_GUARDIANS(21),
	VORKATH(22, 1, 8059, 8061),
//    THE_ALCHEMICAL_HYDRA(23),
//    SARACHNIS(24),

	;
	public final int[] ids;
	public boolean wilderness;
	public int configId;
	public int slayerReq;


	SlayerBoss(int configId, int slayerReq, int... ids) {
		this.configId = configId;
		this.wilderness = false;
		this.ids = ids;
		this.slayerReq = slayerReq;
	}

	SlayerBoss(boolean wilderness, int configId, int... ids) {
		this.configId = configId;
		this.wilderness = wilderness;
		this.ids = ids;
	}

	public static SlayerBoss getSlayerBoss(Player player, int configID) {
		return Arrays.stream(SlayerBoss.values())
			.filter(sb -> sb.configId == configID && player.getStats().get(StatType.Slayer).fixedLevel >= sb.slayerReq)
			.findFirst()
			.orElse(null);
	}


	public boolean isTask(int id) {
		for (int npcId : ids) {
			if (npcId == id) {
				return true;
			}
		}
		return false;
	}
}
