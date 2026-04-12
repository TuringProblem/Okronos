package io.ruin.model.content.combatachievements.achievements.easy;

import io.ruin.cache.ItemID;
import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.containers.Equipment;

public class BasiliskVeteran extends CombatAchievement {
	@Override
	public void check(Player player) {
		if (!completed && player.getEquipment().get(Equipment.SLOT_SHIELD) != null
			&& player.getEquipment().get(Equipment.SLOT_SHIELD).getId() == ItemID.MIRROR_SHIELD) {
			completed = true;
			complete(player);
		}
	}


	@Override
	public String getName() {
		return "Basilisk Veteran";
	}

	@Override
	public String getDesc() {
		return "Kill a basilisk with a mirror shield equipped.";
	}

	@Override
	public Tier getTier() {
		return CombatAchievement.Tier.EASY;
	}

}
