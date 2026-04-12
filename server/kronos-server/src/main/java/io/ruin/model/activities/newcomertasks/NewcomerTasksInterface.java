package io.ruin.model.activities.newcomertasks;

import io.ruin.model.World;
import io.ruin.model.activities.VoteBossHandler;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.AccessMasks;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.handlers.ServerTeleports;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.map.Position;
import io.ruin.model.skills.magic.spells.modern.ModernTeleport;

public class NewcomerTasksInterface {
	private static int INTERFACE_ID = 1112;
	Item reward = new Item(30590);

	public void openInterface(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, INTERFACE_ID);
		player.getPacketSender().sendString(INTERFACE_ID, 21, reward.getDef().getName());
		player.getPacketSender().sendString(INTERFACE_ID, 22, getCompletionAmount(player));
		int nameComp = 35;
		int descriptionComp = 36;
		int completionAmountComp = 42;
		int taskProgressionbarTextureComponent = 41;
		int taskProgressionbarComponent = 40;
		int startingItemContainerComponent = 45;
		int claimButton = 43;
		int startingItemContainerId = 1000;
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			INTERFACE_ID << 16 | 27, 2000,
			4, 7, 1, -1, "", "", "", "", ""
		);
		player.getPacketSender().sendItems(
			-1,
			27,
			2000,
			reward
		);
		player.getPacketSender().sendIfEvents(INTERFACE_ID, 27, 0, 27, 1024);
		player.getPacketSender().sendIfEvents(INTERFACE_ID, 27, 0, 27, 1086);
		player.getPacketSender().sendIfEvents(INTERFACE_ID, 27, 0, 5, AccessMasks.ClickOp1);
		player.getPacketSender().sendIfEvents(INTERFACE_ID, 27, 0, 27, 1086);

		for (NewcomerTasks task :
			NewcomerTasks.VALUES) {
			player.getPacketSender().setHidden(INTERFACE_ID, claimButton, false);
			player.getPacketSender().sendString(INTERFACE_ID, nameComp, task.name);
			player.getPacketSender().sendString(INTERFACE_ID, descriptionComp, task.description);
			float amountCompleted = getCompletionAmount(player, task);
			if (amountCompleted > task.amountToComplete)
				amountCompleted = task.amountToComplete;

			player.getPacketSender().sendString(INTERFACE_ID, completionAmountComp, (int) amountCompleted + "/" + task.amountToComplete);
			int barTextureInterfaceHash = INTERFACE_ID << 16 | taskProgressionbarTextureComponent;
			int barInterfaceHash = INTERFACE_ID << 16 | taskProgressionbarComponent;
			float barPercentage = (amountCompleted / task.amountToComplete) * 441;

			player.getPacketSender().sendClientScript(10607, "Ii", barInterfaceHash, (int) barPercentage);
			player.getPacketSender().sendClientScript(10608, "Ii", barTextureInterfaceHash, (int) barPercentage);

			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				INTERFACE_ID << 16 | startingItemContainerComponent, startingItemContainerId,
				4, 7, 1, -1, "", "", "", "", ""
			);
			player.getPacketSender().sendItems(
				-1,
				startingItemContainerComponent,
				startingItemContainerId,
				task.reward
			);
			player.getPacketSender().sendIfEvents(INTERFACE_ID, startingItemContainerComponent, 0, 27, 1024);
			player.getPacketSender().sendIfEvents(INTERFACE_ID, startingItemContainerComponent, 0, 27, 1086);
			player.getPacketSender().sendIfEvents(INTERFACE_ID, startingItemContainerComponent, 0, 5, AccessMasks.ClickOp1);
			player.getPacketSender().sendIfEvents(INTERFACE_ID, startingItemContainerComponent, 0, 27, 1086);


			nameComp += 15;
			descriptionComp += 15;
			completionAmountComp += 15;
			taskProgressionbarTextureComponent += 15;
			taskProgressionbarComponent += 15;
			startingItemContainerComponent += 15;
			startingItemContainerId++;
			claimButton += 15;

		}
	}

	public void claimReward(Player player, NewcomerTasks task) {
		if (getCompletionAmount(player, task) < task.amountToComplete) {
			player.sendMessage("You have not completed this task yet!");
			return;
		}
		if (player.newcomerTasks.get(task)) {
			player.sendMessage("You have already claimed this reward!");
			return;
		}
		if (player.getInventory().hasRoomFor(task.reward)) {
			player.newcomerTasks.put(task, true);
			player.getInventory().addOrDrop(task.reward);
		}
	}

	public void claimCompletionReward(Player player) {
		boolean playerHasRing = player.getInventory().hasId(30590) || player.getEquipment().contains(30590) || player.getBank().contains(30590);
		if (canClaimReward(player)) {
			if (playerHasRing) {
				player.sendMessage("You have already claimed this reward!");
				return;
			}
			if (player.getInventory().hasRoomFor(reward)) {
				player.newcomerRewardClaimed = true;
				player.getInventory().addOrDrop(reward);
			}
		} else {
			player.sendMessage("You have not completed all the tasks yet!");
		}
	}

	private boolean canClaimReward(Player player) {
		for (NewcomerTasks task :
			NewcomerTasks.VALUES) {
			if (getCompletionAmount(player, task) < task.amountToComplete)
				return false;
		}
		return true;
	}

	private String getCompletionAmount(Player player) {
		String completionAmount = "0/5";
		int completionTotal = 0;
		for (NewcomerTasks task :
			NewcomerTasks.VALUES) {
			if (getCompletionAmount(player, task) >= task.amountToComplete)
				completionTotal++;
		}

		if (completionTotal > 1 && completionTotal < 7)
			completionAmount = "<col=ff561f@Completed " + completionTotal + "/10";
		else if (completionTotal > 7)
			completionAmount = "<col=16e614>Completed " + completionTotal + "/10";
		else
			completionAmount = "<col=b31a1a>Completed " + completionTotal + "/10";
		return completionAmount;
	}

	public int getCompletionAmount(Player player, NewcomerTasks task) {
		switch (task) {
			case VOTE_CLAIM:
				return player.votesClaimed;
			case MISSING_HOME:
				return player.clanChatSpeeches;
			case PERK_TASK_COMPLETION:
				return player.totalPerkTasksCompleted;
			case SLAYER_TASK_COMPLETION:
				return player.totalSlayerTasksCompleted;
			case ACHIEVEMENTS_COMPLETION:
				return player.totalAchievementsCompleted;
			case DEFEAT_GLOBAL_BOSS:
				return player.globalBossKills.getKills();
			case DEFEAT_VOTE_BOSS:
				return player.voteBossKills.getKills();
			case COMPLETE_DAILY_TASK:
				return player.totalDailyTasksCompleted;
			case SPEND_REASON_POINTS:
				return player.totalReasonPointsSpent;
			case SKIP_SLAYER_TASK:
				return player.totalSlayerTasksSkipped;
		}
		return 0;
	}


	public static void register() {
		ItemAction.registerInventory(30589, "read", (player, item) -> {
			player.getNewcomerTaskInterface().openInterface(player);
		});
		InterfaceHandler.register(INTERFACE_ID, h -> {
			h.actions[31] = (SimpleAction) p -> {
				p.clanChatSpeeches++;
				if (p.clanChatSpeeches == 1)
					p.sendMessage("<col=000080>You have completed the newcomer task: <col=800000>" + NewcomerTasks.MISSING_HOME.getFormattedName() + "!");
				ModernTeleport.teleport(p, ServerTeleports.SAND_CRABS.getTeleportPos());
			};
			h.actions[46] = (SimpleAction) p -> {
				ModernTeleport.teleport(p, new Position(3092, 3503, 0));
			};
			h.actions[61] = (SimpleAction) p -> {
				ModernTeleport.teleport(p, new Position(3097, 3511, 0));
			};
			h.actions[166] = (SimpleAction) p -> {
				ModernTeleport.teleport(p, new Position(3097, 3511, 0));
			};
			h.actions[106] = (SimpleAction) p -> {
				ModernTeleport.teleport(p, new Position(2900, 3616, 0));
			};
			h.actions[121] = (SimpleAction) p -> {
				ModernTeleport.teleport(p, VoteBossHandler.teleportPosition);
			};
			h.actions[149] = (SimpleAction) p -> {
				ModernTeleport.teleport(p, new Position(3087, 3499, 0));
			};
			h.actions[91] = (SimpleAction) p -> {
				String username = p.getName().replace(" ", "-");
				String url = "https://reasonps.com/vote/" + username;
				p.openUrl(World.type.getWorldName() + " Vote", url);
			};
			h.actions[44] = (SimpleAction) JournalTab::openAchievementInterface;
			h.actions[27] = (DefaultAction) (player, option, slot, itemId) -> {
				switch (option) {
					case 10:
						player.sendMessage("" + new Item(itemId).getDef().examine);
						break;
				}
			};
			for (int i = 45; i < 180; i += 15) {
				h.actions[i] = (DefaultAction) (player, option, slot, itemId) -> {
					switch (option) {
						case 10:
							player.sendMessage("" + new Item(itemId).getDef().examine);
							break;
					}
				};
			}
			h.actions[28] = (SimpleAction) p -> {
				p.getNewcomerTaskInterface().claimCompletionReward(p);
			};
			for (int i = 0; i < 10; i++) {
				int component = 43 + (i * 15);
				int finalI = i;
				h.actions[component] = (SimpleAction) p -> {
					p.getNewcomerTaskInterface().claimReward(p, NewcomerTasks.VALUES[finalI]);
				};
			}
		});
		LoginListener.register(player -> {
			if (player.newcomerTasks.isEmpty()) {
				for (NewcomerTasks task :
					NewcomerTasks.VALUES) {
					player.newcomerTasks.put(task, false);
				}
			}
		});
	}
}
