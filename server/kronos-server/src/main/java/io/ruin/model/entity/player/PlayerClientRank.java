package io.ruin.model.entity.player;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PlayerClientRank {
	NORMAL(0),
	P_MOD(1),
	J_MOD(2),
	P_IRONMAN(3),
	P_IRONMAN_ULTIMATE(4),
	P_IRONMAN_HARDCORE(5),
	P_LEAGUES(6),
	P_IRONMAN_GROUP(7),
	P_IRONMAN_GROUP_HARDCORE(8),
	IDK_9(9),
	IDK_10(10),
	IDK_11(11),
	IDK_12(12),
	IDK_13(13),
	IDK_14(14),
	IDK_15(15),
	;

	public final int raw;
}
