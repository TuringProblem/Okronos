package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.containers.Equipment;

import java.util.Arrays;
import java.util.List;

public class ReturningTheHorror extends CombatAchievement {
	private static final List<Integer> BLACK_MASKS = Arrays.asList(
		8901, 8903, 8905, 8907, 8909, 8911, 8913, 8915, 8917, 8919, 8921, 11784
	);

	@Override
	public void check(Player player) {
		if (!completed && player.getEquipment().get(Equipment.SLOT_HAT) != null && BLACK_MASKS.contains(player.getEquipment().get(Equipment.SLOT_HAT).getId())) {
			completed = true;
			this.complete(player);
		}
	}

	@Override
	public String getName() {
		return "Returning The Horror";
	}

	@Override
	public String getDesc() {
		return "Kill a cave horror wearing a black mask.";
	}

	@Override
	public Tier getTier() {
		return Tier.EASY;
	}

}
