package io.ruin.model.activities.newcomertasks;

import io.ruin.cache.ItemID;
import io.ruin.model.item.Item;
import io.ruin.api.utils.StringUtils;

public enum NewcomerTasks {
	MISSING_HOME("Beach Weather", "Teleport to sand crabs.", 1, new Item(28434, 1)),
	PERK_TASK_COMPLETION("Perk Task Completion", "Complete a perk task.", 1, new Item(30570, 3)),
	SLAYER_TASK_COMPLETION("Slayer Task Completion", "Complete a slayer task.", 1, new Item(30458, 3)),
	ACHIEVEMENTS_COMPLETION("Achievements Completion", "Complete 3 achievements.", 3, new Item(30577)),
	VOTE_CLAIM("Vote Claim", "Claim a vote reward.", 1, new Item(30596)),
	DEFEAT_GLOBAL_BOSS("Defeat Global Boss", "Defeat a global boss.", 1, new Item(995, 2500000)),
	DEFEAT_VOTE_BOSS("Defeat The Vote Boss", "Defeat the vote boss.", 1, new Item(30596, 2)),
	COMPLETE_DAILY_TASK("Complete A Daily Task", "Complete a daily task.", 1, new Item(30570, 3)),
	SPEND_REASON_POINTS("Spend Reason Points", "Spend 2500 reason points at the rewards shop.", 2500, new Item(30577)),
	SKIP_SLAYER_TASK("Skip A Slayer Task", "Skip a slayer task using slayer points.", 1, new Item(ItemID.BRIMSTONE_KEY, 5)),
	;

	String name;
	String description;
	int amountToComplete;
	Item reward;

	NewcomerTasks(String name, String description, int amountToComplete, Item reward) {
		this.name = name;
		this.description = description;
		this.amountToComplete = amountToComplete;
		this.reward = reward;
	}

	public String getFormattedName() {
		return StringUtils.capitalizeFirst(name().toLowerCase().replace("_", " "));
	}

	public static final NewcomerTasks[] VALUES = values();
}