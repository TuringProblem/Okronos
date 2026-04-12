package io.ruin.model.activities.dailytasks;

import io.ruin.model.activities.newcomertasks.NewcomerTasks;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;
import io.ruin.utility.Misc;

public class DailyTasksInterface {
	public void open(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, 1106);
		player.getPacketSender().sendString(1106, 14, "Daily Tasks Interface");
		player.getPacketSender().sendString(1106, 22, getCompletionAmount(player));
		player.getPacketSender().sendString(1106, 21, player.dailyTasksCompletionReward.getDef().getName());
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1106 << 16 | 27, 3000,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			27,
			3000,
			player.dailyTasksCompletionReward
		);
		int startingTitleComponent = 35;
		int startingDescComponent = 36;
		int startingItemContainerComponent = 44;
		int startingItemContainerComponent2 = 45;
		int taskProgressionStringComponent = 42;
		int taskProgressionbarTextureComponent = 41;
		int taskProgressionbarComponent = 40;
		int startingContainerId = 2000;
		for (int i = 0; i < 5; i++) {
			int coinReward = 500000 * (1 + player.currentDailyTasks[i].difficulty.ordinal());
			player.getPacketSender().sendString(1106, startingTitleComponent, player.currentDailyTasks[i].name);
			String desc = player.currentDailyTasks[i].description + " " + player.currentDailyTaskAmounts[i] + " times.";
			player.getPacketSender().sendString(1106, startingDescComponent, desc);
			int amountDone = player.startingDailyTaskAmounts[i] - player.currentDailyTaskAmounts[i];
			player.getPacketSender().sendString(1106, taskProgressionStringComponent, amountDone + "/" + player.startingDailyTaskAmounts[i]);

			int barTextureInterfaceHash = 1106 << 16 | taskProgressionbarTextureComponent;
			int barInterfaceHash = 1106 << 16 | taskProgressionbarComponent;
			float barPercentage = (amountDone / player.startingDailyTaskAmounts[i]) * 441;

			player.getPacketSender().sendClientScript(10607, "Ii", barInterfaceHash, (int) barPercentage);
			player.getPacketSender().sendClientScript(10608, "Ii", barTextureInterfaceHash, (int) barPercentage);

			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1106 << 16 | startingItemContainerComponent, startingContainerId,
				4, 7, 1, -1, "", "", "", "", ""
			);
			player.getPacketSender().sendItems(
				-1,
				startingItemContainerComponent,
				startingContainerId,
				player.currentDailyRewards[i]
			);
			startingContainerId++;
			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1106 << 16 | startingItemContainerComponent2, startingContainerId,
				4, 7, 1, -1, "", "", "", "", ""
			);
			player.getPacketSender().sendItems(
				-1,
				startingItemContainerComponent2,
				startingContainerId,
				new Item(995, coinReward)
			);
			startingDescComponent += 15;
			startingItemContainerComponent += 15;
			startingItemContainerComponent2 += 15;
			taskProgressionbarComponent += 15;
			taskProgressionbarTextureComponent += 15;
			taskProgressionStringComponent += 15;
			startingTitleComponent += 15;
			startingContainerId++;
		}

	}

	private void claimCompletionReward(Player player) {
		if (player.dailyTaskCompletionRewardClaimed) {
			player.sendMessage("You've already claimed your reward today.");
			return;
		}
		int completionTotal = 0;
		for (int i = 0; i < 5; i++) {
			if (player.currentDailyTaskAmounts[i] <= 0)
				completionTotal++;
		}
		if (completionTotal == 5) {
			player.dailyTaskCompletionRewardClaimed = true;
			player.sendMessage("You have claimed your reward.");
			player.getInventory().addOrDrop(player.dailyTasksCompletionReward.getId(), player.dailyTasksCompletionReward.getAmount());
			int pointsReward = Misc.random(2500, 7500);
			player.sendMessage("You receive <col=000000><shad=29F1FE>" + pointsReward + " Reason points<col=000000></shad> for completing the task.");
			player.updateReasonPoints(pointsReward);
		} else player.sendMessage("You must complete all the tasks first.");
	}

	private void claimReward(Player player, int index) {
		if (player.currentDailyTaskAmounts[index] <= 0) {
			if (!player.getInventory().hasRoomFor(player.currentDailyRewards[index])) {
				player.sendMessage("You don't have enough inventory space to claim this reward.");
				return;
			}
			if (!player.currentDailyRewardsClaimed[index]) {
				player.getInventory().addOrDrop(player.currentDailyRewards[index]);
				int pointsReward = Misc.random(1000, 3000) + (player.currentDailyTasks[index].difficulty.ordinal() * 1500);
				player.sendMessage("You receive <col=000000><shad=29F1FE>" + pointsReward + " Reason points<col=000000></shad> for completing the task.");
				player.updateReasonPoints(pointsReward);
				player.currentDailyRewardsClaimed[index] = true;
				int coinReward = 500000 * (1 + player.currentDailyTasks[index].difficulty.ordinal());
				player.getInventory().addOrDrop(new Item(995, coinReward));
				player.totalDailyTasksCompleted++;
				if (player.totalDailyTasksCompleted == 1)
					player.sendMessage("<col=000080>You have completed the newcomer task: <col=800000>" + NewcomerTasks.COMPLETE_DAILY_TASK.getFormattedName() + "!");

				player.sendMessage("You claim the reward.");
			} else player.sendMessage("You've already claimed this reward.");
		} else player.sendMessage("You must complete the task before claiming it!");
	}

	private String getCompletionAmount(Player player) {
		String completionAmount = "0/5";
		int completionTotal = 0;
		for (int i = 0; i < 5; i++) {
			if (player.currentDailyTaskAmounts[i] <= 0)
				completionTotal++;
		}

		if (completionTotal > 1 && completionTotal < 5)
			completionAmount = "<col=ff561f@Completed " + completionTotal + "/5";
		else if (completionTotal > 4)
			completionAmount = "<col=16e614>Completed " + completionTotal + "/5";
		else
			completionAmount = "<col=b31a1a>Completed " + completionTotal + "/5";
		return completionAmount;
	}

	public static void register() {
		InterfaceHandler.register(1106, h -> {
			h.actions[28] = (SimpleAction) (p) -> p.getDailyTaskInterface().claimCompletionReward(p);
			h.actions[43] = (SimpleAction) (p) -> p.getDailyTaskInterface().claimReward(p, 0);
			h.actions[58] = (SimpleAction) (p) -> p.getDailyTaskInterface().claimReward(p, 1);
			h.actions[73] = (SimpleAction) (p) -> p.getDailyTaskInterface().claimReward(p, 2);
			h.actions[88] = (SimpleAction) (p) -> p.getDailyTaskInterface().claimReward(p, 3);
			h.actions[103] = (SimpleAction) (p) -> p.getDailyTaskInterface().claimReward(p, 4);
		});
	}
}
