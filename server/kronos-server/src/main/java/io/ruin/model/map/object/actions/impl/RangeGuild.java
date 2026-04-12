package io.ruin.model.map.object.actions.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;

public class RangeGuild {

	public static void register() {
		ObjectAction.register(11665, "open", (player, object) -> {
			openDoor(player, object);
		});
	}

	private static void openDoor(Player player, GameObject door) {
		int rangeLevel = player.getStats().get(StatType.Ranged).currentLevel;
		if (rangeLevel < 40) {
			player.dialogue(
				new NPCDialogue(6057, "Sorry, but you're not experienced enough to go in there.").animate(588),
				new MessageDialogue("You need a Ranged level of 40 to access the Ranged Guild."));
			return;
		}

		player.startEvent(event -> {
			player.lock();

			if (!player.isAt(door.x, player.getAbsY())) {
				player.stepAbs(door.x, player.getAbsY(), StepType.WALK);
				event.delay(1);
			}
			GameObject opened = GameObject.spawn(1539, door.x, door.y + 1, door.z, door.getType(), 0);
			door.skipClipping(true).remove();
			if (player.getAbsY() > door.y)
				player.stepAbs(player.getAbsX(), player.getAbsY() - 1, StepType.WALK);
			else
				player.stepAbs(player.getAbsX(), player.getAbsY() + 1, StepType.WALK);
			event.delay(2);
			door.restore().skipClipping(false);
			opened.remove();
			player.unlock();
		});
	}

}
