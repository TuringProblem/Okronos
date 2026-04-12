package io.ruin.model.activities.perktree;

import io.ruin.model.entity.player.Player;

public class PerkTaskHandler {

	public static void handleGatherResource(Player player, int resourceId, int amount) {
		if (player != null) {
			if (player.currentPerkTask != null) {
				if (player.currentPerkTask.resourceId == resourceId)
					player.getPlayerPerkHandler().handleTaskAmountDecrement(player, amount);
			}
		}
	}

	public static void handleMonsterKill(Player player, int npcId) {
		if (player != null) {
			if (player.currentPerkTask != null) {
				if (npcId < 15) return;
				if (player.currentPerkTask.type == PerkTasks.TaskType.WILDERNESS) {
					if (player.wildernessLevel > 0 && player.currentPerkTask.npcId instanceof Integer && (int) player.currentPerkTask.npcId == npcId)
						player.getPlayerPerkHandler().handleTaskAmountDecrement(player, 1);
				} else if (player.currentPerkTask.npcId instanceof Integer && (int) player.currentPerkTask.npcId == npcId)
					player.getPlayerPerkHandler().handleTaskAmountDecrement(player, 1);
			}
		}
	}

	public static void handleMonsterKill(Player player, String npcId) {
		if (player != null) {
			if (player.currentPerkTask != null) {
				if (player.currentPerkTask.npcId instanceof String && npcId.toLowerCase().contains((String) player.currentPerkTask.npcId))
					player.getPlayerPerkHandler().handleTaskAmountDecrement(player, 1);
			}
		}
	}

	public static void handleSlayerTaskCompletion(Player player, int master) {
		if (player != null) {
			if (player.currentPerkTask != null) {
				if (player.currentPerkTask.npcId instanceof Integer && (int) player.currentPerkTask.npcId == master)
					player.getPlayerPerkHandler().handleTaskAmountDecrement(player, 1);
			}
		}
	}

	public static void handleCompleteAction(Player player, int resourceObjId) {
		if (player != null) {
			if (player.currentPerkTask != null) {
				if (player.currentPerkTask.resourceId == resourceObjId)
					player.getPlayerPerkHandler().handleTaskAmountDecrement(player, 1);
			}
		}
	}

	/*
	We'll assign resourceId in the enum to different activity 1-10 etc
	When calling the method we have the params as the activity id we set it as in enum
	 */
	public static void handleCompleteActivity(Player player, int activityId) {
		if (player != null) {
			if (player.currentPerkTask != null) {
				if (player.currentPerkTask.resourceId == activityId)
					player.getPlayerPerkHandler().handleTaskAmountDecrement(player, 1);
			}
		}
	}
}
