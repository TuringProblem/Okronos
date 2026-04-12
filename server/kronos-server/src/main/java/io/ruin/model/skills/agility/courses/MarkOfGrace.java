package io.ruin.model.skills.agility.courses;

import io.ruin.api.utils.Random;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.SecondaryGroup;
import io.ruin.model.item.Item;
import io.ruin.model.map.Position;
import io.ruin.model.map.ground.GroundItem;

import java.util.List;

public class MarkOfGrace {

	public static void rollMark(Player player, int levelReq, List<Position> spawns) {
		if (spawns == null)
			return;
		double chance = levelReq / 2.0 / 100.0;
		if (Random.get() <= chance) {
			Position spawn = Random.get(spawns);
			int amount = (Random.get(1, 4) + markOfGraceDonatorIncrease(player));
			player.addToCollectionLog(new Item(11849, amount));
			new GroundItem(new Item(11849, amount)).owner(player).position(spawn).spawn(2);
		}
	}

	private static int markOfGraceDonatorIncrease(Player player) {
		if (player.isGroups(SecondaryGroup.SUPREME_DONATOR)) {
			return 8;
		} else if (player.isGroups(SecondaryGroup.LEGENDARY_DONATOR)) {
			return 7;
		} else if (player.isGroups(SecondaryGroup.PLATINUM_DONATOR)) {
			return 6;
		} else if (player.isGroups(SecondaryGroup.GOLD_DONATOR)) {
			return 5;
		} else if (player.isGroups(SecondaryGroup.NOBLE_DONATOR)) {
			return 4;
		} else if (player.isGroups(SecondaryGroup.ELITE_DONATOR)) {
			return 3;
		} else if (player.isGroups(SecondaryGroup.SUPER_DONATOR)) {
			return 2;
		} else if (player.isGroups(SecondaryGroup.DONATOR)) {
			return 1;
		} else {
			return 0;
		}
	}
}
