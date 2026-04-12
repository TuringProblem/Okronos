package io.ruin.model.map.object.actions.impl.dungeons;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;

public class LizardmanTemple {

	private static void BarrierNSRoomOne(Player player, GameObject barrier) {
		player.startEvent(event -> {
			if (player.getAbsY() == 10091) {
				player.stepAbs(player.getAbsX(), 10093, StepType.WALK);
				player.sendFilteredMessage("You pass through the Barrier.");
			} else {
				player.stepAbs(player.getAbsX(), 10091, StepType.WALK);
				player.sendFilteredMessage("You pass through the Barrier.");
			}
		});
	}

	private static void BarrierCorridorOne(Player player, GameObject barrier) {
		player.startEvent(event -> {
			if (player.getAbsX() == 1296) {
				player.stepAbs(player.getAbsX() + 2, player.getAbsY(), StepType.WALK);
				player.sendFilteredMessage("You pass through the Barrier.");
			} else {
				player.stepAbs(player.getAbsX() - 2, player.getAbsY(), StepType.WALK);
				player.sendFilteredMessage("You pass through the Barrier.");
			}
		});
	}

	private static void BarrierRoomTwo(Player player, GameObject barrier) {
		player.startEvent(event -> {
			if (player.getAbsX() == 1307) {
				player.stepAbs(player.getAbsX() + 2, player.getAbsY(), StepType.WALK);
				player.sendFilteredMessage("You pass through the Barrier.");
			} else {
				player.stepAbs(player.getAbsX() - 2, player.getAbsY(), StepType.WALK);
				player.sendFilteredMessage("You pass through the Barrier.");
			}
		});
	}

	private static void BarrierCorridorTwo(Player player, GameObject barrier) {
		player.startEvent(event -> {
			if (player.getAbsX() == 1316) {
				player.stepAbs(player.getAbsX() + 2, player.getAbsY(), StepType.WALK);
				player.sendFilteredMessage("You pass through the Barrier.");
			} else {
				player.stepAbs(player.getAbsX() - 2, player.getAbsY(), StepType.WALK);
				player.sendFilteredMessage("You pass through the Barrier.");
			}
		});
	}

	private static void BarrierRoomThree(Player player, GameObject barrier) {
		player.startEvent(event -> {
			if (player.getAbsX() == 1324) {
				player.stepAbs(player.getAbsX() + 2, player.getAbsY(), StepType.WALK);
				player.sendFilteredMessage("You pass through the Barrier.");
			} else {
				player.stepAbs(player.getAbsX() - 2, player.getAbsY(), StepType.WALK);
				player.sendFilteredMessage("You pass through the Barrier.");
			}
		});
	}

	public static void register() {
		ObjectAction.register(34642, "pass", LizardmanTemple::BarrierNSRoomOne);
		ObjectAction.register(34643, "pass", LizardmanTemple::BarrierCorridorOne);
		ObjectAction.register(34644, "pass", LizardmanTemple::BarrierRoomTwo);
		ObjectAction.register(34645, "pass", LizardmanTemple::BarrierCorridorTwo);
		ObjectAction.register(34646, "pass", LizardmanTemple::BarrierRoomThree);
	}
}
