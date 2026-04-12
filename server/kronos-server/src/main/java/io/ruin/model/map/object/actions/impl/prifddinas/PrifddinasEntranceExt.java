package io.ruin.model.map.object.actions.impl.prifddinas;

import io.ruin.api.utils.Random;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;

public class PrifddinasEntranceExt {

	private static final int[] ELVES = {9151, 9084, 9114, 9109, 9116, 9085, 9115};
	private static final int[] GATES = {36522, 36523};

	public static void register() {
		ObjectAction.register(36523, "exit", (player, obj) -> player.startEvent(event -> {
			if (player.getStats().get(StatType.Agility).currentLevel < 70) {
				player.dialogue(new NPCDialogue(ELVES[Random.get(ELVES.length - 1)], "Sorry, but you need level 70 Agility to enter Prifddinas.").animate(588));
				return;
			} else if (player.getStats().get(StatType.Construction).currentLevel < 70) {
				player.dialogue(new NPCDialogue(ELVES[Random.get(ELVES.length - 1)], "Sorry, but you need level 70 Construction to enter Prifddinas.").animate(588));
				return;
			} else if (player.getStats().get(StatType.Farming).currentLevel < 70) {
				player.dialogue(new NPCDialogue(ELVES[Random.get(ELVES.length - 1)], "Sorry, but you need level 70 Farming to enter Prifddinas.").animate(588));
				return;
			} else if (player.getStats().get(StatType.Herblore).currentLevel < 70) {
				player.dialogue(new NPCDialogue(ELVES[Random.get(ELVES.length - 1)], "Sorry, but you need level 70 Herblore to enter Prifddinas.").animate(588));
				return;
			} else if (player.getStats().get(StatType.Farming).currentLevel < 70) {
				player.dialogue(new NPCDialogue(ELVES[Random.get(ELVES.length - 1)], "Sorry, but you need level 70 Farming to enter Prifddinas.").animate(588));
				return;
			} else if (player.getStats().get(StatType.Hunter).currentLevel < 70) {
				player.dialogue(new NPCDialogue(ELVES[Random.get(ELVES.length - 1)], "Sorry, but you need level 70 Hunter to enter Prifddinas.").animate(588));
				return;
			} else if (player.getStats().get(StatType.Mining).currentLevel < 70) {
				player.dialogue(new NPCDialogue(ELVES[Random.get(ELVES.length - 1)], "Sorry, but you need level 70 Mining to enter Prifddinas.").animate(588));
				return;
			} else if (player.getStats().get(StatType.Smithing).currentLevel < 70) {
				player.dialogue(new NPCDialogue(ELVES[Random.get(ELVES.length - 1)], "Sorry, but you need level 70 Smithing to enter Prifddinas.").animate(588));
				return;
			} else if (player.getStats().get(StatType.Woodcutting).currentLevel < 70) {
				player.dialogue(new NPCDialogue(ELVES[Random.get(ELVES.length - 1)], "Sorry, but you need level 70 Woodcutting to enter Prifddinas.").animate(588));
				return;
			} else {
				player.lock();
				player.getPacketSender().fadeOut();
				event.delay(2);
				player.dialogue(
					new MessageDialogue("You push open the doors and find yourself..."),
					new MessageDialogue("inside Prifddinas.")
				);
				player.getMovement().teleport(3209, 6080, 0);
				player.getPacketSender().fadeIn();
				event.delay(1);
				player.unlock();
			}
		}));
	}

}
