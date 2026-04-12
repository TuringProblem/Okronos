package io.ruin.model.content.drop_rate;

import io.ruin.model.entity.player.Difficulty;
import io.ruin.model.map.Bounds;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DropRateBonusData {

	// 62 is the max bonus you can currently get while inside the wilderness @ revs
	// 47 outside of revs
	// 37 is the max bonus outside of the wilderness
	// 3 from Row
	// 5 from Donator status
	// 15 from Xp Mode
	// 10 from Pets
	// 4 from Armour
	// 15 from Revs (Wildy)
	// 5 from amulet of avarice (Wildy)
	// 5 ultimate totem (Wildy)


	AMULET_OF_AVARICE((player -> {
		int Avarice = 22557;
		if (player.getEquipment().hasId(Avarice))
			return 5;
		return 0;
	})),

	RING_OF_WEALTH((player -> {
		int rowId = 12785;
		if (player.getEquipment().hasId(rowId))
			return 3;
		return 0;
	})),


	DONATOR_BONUS((player -> {
		if (player.isZenyte())
			return 5;
		else if (player.isOnyx())
			return 4;
		else if (player.isDragonStone())
			return 3.5F;
		else if (player.isDiamond())
			return 3.2F;
		else if (player.isRuby())
			return 3;
		else if (player.isEmerald())
			return 2.75F;
		else if (player.isSapphire())
			return 2.5F;
		return 0;
	})),

	XP_MODE((player -> {
		if (player.getDifficulty() == Difficulty.INTERMEDIATE)
			return 6;
		else if (player.getDifficulty() == Difficulty.HARD)
			return 15;
		else if (player.getDifficulty() == Difficulty.EXTREME)
			return 20;
		return 0;
	})),

	DROP_BONUS_COMMAND((player -> {
		int command = player.modifiedDropBonus;
		if (command >= 1)
			return command;
		return 0;
	})),

	REV_CAVE_SKULL((player -> {
		Bounds REVENANT_CAVE = new Bounds(3136, 10048, 3263, 10239, -1);
		if (player.getCombat().isSkulled() && player.getPosition().inBounds(REVENANT_CAVE))
			return 15;
		return 0;
	})),

	;
	public static final DropRateBonusData[] VALUES = values();

	private DropRateBonus dropRateBonus;

}
