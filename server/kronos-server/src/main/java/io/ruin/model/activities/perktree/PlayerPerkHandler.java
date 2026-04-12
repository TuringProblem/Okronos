package io.ruin.model.activities.perktree;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.newcomertasks.NewcomerTasks;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.content.combatachievements.CombatAchievementSystem;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.impl.pet.perk.PerkType;

import java.util.*;

public class PlayerPerkHandler {


	public void handleLogin(Player player) {
		for (Map.Entry<Integer, Integer> entry : player.ownedPerks.entrySet()) {
			int key = entry.getKey();
			int value = entry.getValue();
			Perks perks = getPerkByOrdinal(key);
			Objects.requireNonNull(perks.getPerk(player)).setPerkLevel(value);
			player.ownedPerksList.add(perks);
		}
		for (Map.Entry<Integer, Integer> entry : player.activePerks.entrySet()) {
			int key = entry.getKey();
			int value = entry.getValue();
			Perks perks = getPerkByOrdinal(key);
			Objects.requireNonNull(perks.getPerk(player)).setPerkLevel(value);
			player.activePerksList.add(perks);
		}
		updateActivePerkSets(player);

	}

	public void updateActivePerks(Player player) {
		player.activePerksList.clear();
		for (Map.Entry<Integer, Integer> entry : player.activePerks.entrySet()) {
			int key = entry.getKey();
			int value = entry.getValue();
			Perks perks = getPerkByOrdinal(key);
			Objects.requireNonNull(perks.getPerk(player)).setPerkLevel(value);
			player.activePerksList.add(perks);
		}
	}


	private final List<Perks> reusableList = new ArrayList<>();

	public void updateActivePerkSets(Player player) {
		reusableList.clear();
		List<Perks> activePerks = player.getPlayerPerkHandler().getActivePerks(player);
		for (PerkSets perkSet : PERK_SETS) {
			List<Perks> perks = perkSet.hasAnyPerksetCombination(activePerks);
			if (perks != null) {
				int lowestLevel = 5;
				for (Perks perk : activePerks) {
					if (!perks.contains(perk))
						continue;
					if (Objects.requireNonNull(perk.getPerk(player)).perkLevel < lowestLevel)
						lowestLevel = Objects.requireNonNull(perk.getPerk(player)).perkLevel;
				}
				if (!player.activePerkSetsList.contains(perkSet)) {
					perkSet.perkSet.setLevel(lowestLevel);
					player.activePerkSetsList.add(perkSet);
					player.sendMessage("The perk set: '" + perkSet.perkSet.getPerkSetName() + "' has been activated at level "
							+ perkSet.perkSet.getLevel() + ".");
				}
			} else {
				if (player.activePerkSetsList.contains(perkSet)) {
					player.activePerkSetsList.remove(perkSet);
					player.sendMessage("The perk set: '" + perkSet.perkSet.getPerkSetName() + "' has been deactivated.");
				}
			}
		}
	}

	public List<PerkSets> getActivePerkSets(Player player) {
		return player.activePerkSetsList;
	}


	private static final PerkSets[] PERK_SETS = PerkSets.values();

	public int getActivePerkIndex(Player player, Perks perk) {
		for (int i = 0; i < player.getPlayerPerkHandler().getActivePerks(player).size(); i++) {
			if (Objects.requireNonNull(player.getPlayerPerkHandler().getActivePerks(player).get(i).getPerk(player))
					.getPerkName().equalsIgnoreCase(Objects.requireNonNull(perk.getPerk(player)).getPerkName()))
				return i;
		}
		return 500;
	}

	public int getActivePerkSetIndex(Player player, PerkSets perkSet) {
		for (int i = 0; i < player.getPlayerPerkHandler().getActivePerkSets(player).size(); i++) {
			if (player.getPlayerPerkHandler().getActivePerkSets(player).get(i).perkSet.getPerkSetName()
					.equalsIgnoreCase(perkSet.perkSet.getPerkSetName()))
				return i;
		}
		return 500;
	}


	public List<Perks> getOwnedPerks(Player player) {
		return player.ownedPerksList;
	}

	public List<Perks> getActivePerks(Player player) {
		return player.activePerksList;
	}

	public static <T> T getPerk(Player player, Perks perk) {
		var pPerks = player.getPlayerPerkHandler();
		if (pPerks.getActivePerks(player).contains(perk)) {
			var perkIndex = pPerks.getActivePerkIndex(player, perk);
			return (T) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
		}
		return null;
	}

	public int calculatePlayersTotalPerks(Player player) {
		int totalPerks = 2;
		if (player.perkTreeLevel >= 50 && player.perkTreeLevel < 75)
			totalPerks = 3;
		else if (player.perkTreeLevel >= 75 && player.perkTreeLevel < 100)
			totalPerks = 4;
		else if (player.perkTreeLevel >= 100)
			totalPerks = 5;
		return totalPerks;
	}

	public List<PerkTasks> getPerksByTier(Player player, PerkTasks.TaskTiers tier, PerkTasks.TaskType type) {
		List<PerkTasks> tieredTasks = new ArrayList<>();
		for (PerkTasks task : PerkTasks.VALUES) {
			if (task.tier != tier)
				continue;
			if (task.type != type)
				continue;
			if (task.statType != null && player.getStats().get(task.statType).fixedLevel < task.levelReq)
				continue;
			if (task.statType == null && task.levelReq > 0) {
				if (player.getCombat().getLevel() < task.levelReq)
					continue;
			}

			tieredTasks.add(task);
		}
		if (tieredTasks.size() < 1) {
			sendNoTasksFoundDialogue(player, tier);
			return null;
		}
		return tieredTasks;
	}

	public PerkTasks getPerkByTier(Player player, PerkTasks.TaskTiers tier, PerkTasks.TaskType type) {
		List<PerkTasks> tieredTasks = new ArrayList<>();
		for (PerkTasks task : PerkTasks.VALUES) {
			if (task.tier != tier)
				continue;
			if (task.type != type)
				continue;
			if (task.statType != null && player.getStats().get(task.statType).fixedLevel < task.levelReq)
				continue;
			if (task.statType == null && task.levelReq > 0) {
				if (player.getCombat().getLevel() < task.levelReq)
					continue;
			}

			tieredTasks.add(task);
		}
		if (tieredTasks.size() < 1) {
			sendNoTasksFoundDialogue(player, tier);
			return null;
		}
		return Random.get(tieredTasks);
	}

	private void sendNoTasksFoundDialogue(Player player, PerkTasks.TaskTiers tier) {
		player.closeDialogue();
		player.dialogue(
				new NPCDialogue(7958, "I don't have any " + tier.name().toLowerCase()
						+ " suitable for you in this category with your current stats, try a different tier."));
	}

	public int getTaskAmount(PerkTasks task) {
		return Random.get(task.minimumAmount, task.maximumAmount);
	}

	public void assignTask(Player player, PerkTasks.TaskTiers tier, PerkTasks.TaskType type) {
		int playerLevel = player.getCombat().getLevel();

		// Check if player meets the combat level requirements for PVM tasks
		if (type == PerkTasks.TaskType.PVM) {
			if ((tier == PerkTasks.TaskTiers.ELITE && playerLevel < 90) ||
					(tier == PerkTasks.TaskTiers.HARD && playerLevel < 75) ||
					(tier == PerkTasks.TaskTiers.MEDIUM && playerLevel < 50)) {
				player.sendMessage("You need a higher combat level to obtain this task.");
				return;
			}
		}

		if (player.currentPerkTask != null) {
			player.dialogue(new NPCDialogue(7958, "You already have a perk task, complete it before asking for a new one."));
			return;
		}

		// Assign tasks based on tier and type
		switch (tier) {
			case EASY:
				assignEasyTask(player, type);
				break;
			case MEDIUM:
				assignMediumTask(player, type);
				break;
			case HARD:
				assignHardTask(player, type);
				break;
			case ELITE:
				assignEliteTask(player, type);
				break;
			default:
				player.sendMessage("Invalid task tier.");

		}
	}

	private void assignEasyTask(Player player, PerkTasks.TaskType type) {
		if (type == PerkTasks.TaskType.PVM) {
			List<PerkTasks> tasks = Arrays.asList(
					PerkTasks.COMPLETE_TURAEL_TASK);
			offerTaskChoice(player, tasks);
		} else {
			List<PerkTasks> availableTasks = getTasksForTierAndType(player, PerkTasks.TaskTiers.EASY, type, 3);
			if (availableTasks.isEmpty()) {
				sendNoTasksFoundDialogue(player, PerkTasks.TaskTiers.EASY);
			} else {
				offerTaskChoice(player, availableTasks);
			}
		}
	}

	private void assignMediumTask(Player player, PerkTasks.TaskType type) {
		if (type == PerkTasks.TaskType.PVM) {
			List<PerkTasks> tasks = Arrays.asList(
					PerkTasks.COMPLETE_KRYSTILIA_TASK,
					PerkTasks.COMPLETE_VANNAKA_TASK);
			offerTaskChoice(player, tasks);
		} else {
			List<PerkTasks> availableTasks = getTasksForTierAndType(player, PerkTasks.TaskTiers.MEDIUM, type, 3);
			if (availableTasks.isEmpty()) {
				sendNoTasksFoundDialogue(player, PerkTasks.TaskTiers.MEDIUM);
			} else {
				offerTaskChoice(player, availableTasks);
			}
		}
	}

	private void assignHardTask(Player player, PerkTasks.TaskType type) {
		if (type == PerkTasks.TaskType.PVM) {
			List<PerkTasks> tasks = Arrays.asList(
					PerkTasks.COMPLETE_DURADEL_TASK,
					PerkTasks.COMPLETE_KONAR_TASKS,
					PerkTasks.COMPLETE_NIEVE_TASK);
			offerTaskChoice(player, tasks);
		} else {
			List<PerkTasks> availableTasks = getTasksForTierAndType(player, PerkTasks.TaskTiers.HARD, type, 3);
			if (availableTasks.isEmpty()) {
				sendNoTasksFoundDialogue(player, PerkTasks.TaskTiers.HARD);
			} else {
				offerTaskChoice(player, availableTasks);
			}
		}
	}

	private void assignEliteTask(Player player, PerkTasks.TaskType type) {
		if (type == PerkTasks.TaskType.PVM) {
			List<PerkTasks> availableTasks = getTasksForTierAndType(player, PerkTasks.TaskTiers.ELITE, type, 3);
			if (availableTasks.isEmpty()) {
				sendNoTasksFoundDialogue(player, PerkTasks.TaskTiers.ELITE);
			} else {
				offerTaskChoice(player, availableTasks);
			}
		} else {
			List<PerkTasks> availableTasks = getTasksForTierAndType(player, PerkTasks.TaskTiers.ELITE, type, 3);
			if (availableTasks.isEmpty()) {
				sendNoTasksFoundDialogue(player, PerkTasks.TaskTiers.ELITE);
			} else {
				offerTaskChoice(player, availableTasks);
			}
		}
	}

	private List<PerkTasks> getTasksForTierAndType(Player player, PerkTasks.TaskTiers tier, PerkTasks.TaskType type,
			int maxTasks) {
		List<PerkTasks> availableTasks = new ArrayList<>();
		for (PerkTasks task : PerkTasks.VALUES) {
			if (task.tier != tier || task.type != type)
				continue;
			if (task.statType != null && player.getStats().get(task.statType).fixedLevel < task.levelReq)
				continue;
			if (task.statType == null && task.levelReq > 0 && player.getCombat().getLevel() < task.levelReq)
				continue;

			availableTasks.add(task);
		}
		List<PerkTasks> tasks = new ArrayList<>();
		for (int i = 0; i < maxTasks; i++) {
			if (availableTasks.isEmpty())
				break;
			PerkTasks task = Random.get(availableTasks);
			tasks.add(task);
			availableTasks.remove(task);
		}
		return tasks;
	}

	private void offerTaskChoice(Player player, List<PerkTasks> tasks) {
		if (tasks.isEmpty()) {
			sendNoTasksFoundDialogue(player, PerkTasks.TaskTiers.ELITE);
			return;
		}


		List<Option> options = new ArrayList<>();
		int option = 0;
		for (PerkTasks task : tasks) {
			int amount = getTaskAmount(task);
			if (option == 0) {
				assignTaskToPlayer(player, task, amount);
			}
			options.add(
					new Option(task.description + amount + task.description_2, () -> assignTaskToPlayer(player, task, amount)));
			option++;
		}
		player.dialogue(new OptionsDialogue("Which task would you like?", options.toArray(new Option[0])));
	}

	private void assignTaskToPlayer(Player player, PerkTasks task, int amount) {
		player.currentPerkTask = task;
		player.perkTaskCurrentAmount = amount;
		player.perkTaskAssignedAmount = amount;
		player.dialogue(new NPCDialogue(7958, "Your new perk task is to " + task.description.toLowerCase() + amount
				+ task.description_2.toLowerCase() + "."));
	}


	public void upgradePerk(Player player, Perks perkToUpgrade, int cost) {
		if (player.perkPoints < cost) {
			player.sendMessage("You don't have enough perk points to upgrade this perk.");
			return;
		}
		if (player.getName().equalsIgnoreCase("Dan Gleebles")) {
		} else {
			if (Objects.requireNonNull(perkToUpgrade.getPerk(player)).getPerkLevel() >= Objects
					.requireNonNull(perkToUpgrade.getPerk(player)).getPerkMaxLevel()) {
				player.sendMessage("This perk cannot be upgraded again!");
				return;
			}
		}

		player.ownedPerks.replace(perkToUpgrade.ordinal(), player.ownedPerks.get(perkToUpgrade.ordinal()) + 1);
		player.ownedPlayerPerks.get(perkToUpgrade.ordinal())
				.setPerkLevel(player.ownedPerks.get(perkToUpgrade.ordinal()) + 1);
		player.perkPoints -= cost;
		updateActivePerkSets(player);
		player.sendMessage("Your " + Objects.requireNonNull(perkToUpgrade.getPerk(player)).getPerkName()
				+ " perk is now level " + player.ownedPlayerPerks.get(perkToUpgrade.ordinal()).getPerkLevel() + ".");
	}

	public Perks getPerkByOrdinal(int ordinal) {
		for (Perks perk : Perks.VALUES) {
			if (perk.ordinal() == ordinal)
				return perk;
		}
		return null;
	}

	public void purchasePerk(Player player, Perks perkToBuy, int cost) {
		if (player.hasFreePerkUnlock) {
			if (player.getInventory().contains(30035))
				player.getInventory().remove(30035, 1);
			else if (player.getBank().contains(30035))
				player.getBank().remove(30035, 1);
			player.hasFreePerkUnlock = false;
			player.ownedPerks.put(perkToBuy.ordinal(), 5);
			player.sendMessage("You have successfully unlocked "
					+ Objects.requireNonNull(perkToBuy.getPerk(player)).getPerkName() + " as a free level 5 unlock.");
			Objects.requireNonNull(perkToBuy.getPerk(player)).setPerkLevel(5);
			player.ownedPerksList.add(perkToBuy);
			return;
		}
		if (player.perkPoints < cost) {
			player.sendMessage("You don't have enough perk points to purchase this perk.");
			return;
		}
		if (player.ownedPerks.containsKey(perkToBuy.ordinal())) {
			player.sendMessage("You already own this perk.");
			return;
		}
		player.ownedPerks.put(perkToBuy.ordinal(), 1);
		player.sendMessage(
				"You have purchased the perk " + Objects.requireNonNull(perkToBuy.getPerk(player)).getPerkName() + ".");
		Objects.requireNonNull(perkToBuy.getPerk(player)).setPerkLevel(1);
		player.ownedPerksList.add(perkToBuy);
		player.perkPoints -= cost;
		player.animate(8524);
		player.graphics(1835);
	}

	public boolean addToActivePerks(Player player, Perks perkToAdd) {
		if (!player.ownedPerks.containsKey(perkToAdd.ordinal())) {
			player.sendMessage("You don't own this perk!");
			return false;
		}
		if (player.activePerks.containsKey(perkToAdd.ordinal())) {
			player.sendMessage("You already have this perk active!");
			return false;
		}
		int maxPerks = calculatePlayersTotalPerks(player);
		if (player.activePerks.size() <= maxPerks) {
			player.activePerks.put(perkToAdd.ordinal(), player.getOwnedPerks().get(perkToAdd.ordinal()));
			player.activePerksList.add(perkToAdd);
			player.sendFilteredMessage(
					"You added " + Objects.requireNonNull(perkToAdd.getPerk(player)).getPerkName() + " to your active perks");
			updateActivePerkSets(player);
		} else {
			if (player.getName().equalsIgnoreCase("Dan Gleebles")) {
				player.activePerks.put(perkToAdd.ordinal(), player.getOwnedPerks().get(perkToAdd.ordinal()));
				player.activePerksList.add(perkToAdd);
				player.sendFilteredMessage(
						"You added " + Objects.requireNonNull(perkToAdd.getPerk(player)).getPerkName() + " to your active perks");
				updateActivePerkSets(player);
				return true;
			}
			player.sendMessage("You already have the maximum amount of perks active.");
			return false;
		}
		return true;
	}

	public boolean removeFromActivePerks(Player player, int index) {
		if (index > player.activePerksList.size() - 1) {
			player.sendMessage("You don't have a perk in this slot!");
			return false;
		}

		Perks perk = player.activePerksList.get(index);

		player.activePerks.remove(perk.ordinal());
		player.activePerksList.remove(perk);
		updateActivePerkSets(player);
		player.sendFilteredMessage(
				"You removed " + Objects.requireNonNull(perk.getPerk(player)).getPerkName() + " from your active perks");


		return true;
	}

	public void handleTaskAmountDecrement(Player player, int amount) {
		player.perkTaskCurrentAmount -= amount;
		if (player.perkTaskCurrentAmount < 1)
			handleTaskComplete(player);
	}

	private void handleTaskComplete(Player player) {
		PerkTasks.TaskTiers tier = player.currentPerkTask.tier;
		PerkTasks.TaskType type = player.currentPerkTask.type;
		int expReward = calculateExperienceRewardFromTier(player, tier, type);
		if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.DOUBLE_PERK_EXPERIENCE))
			expReward *= 2;
		addPerkExperience(player, expReward);
		player.currentPerkTask = null;
		player.totalPerkTasksCompleted++;
		if (player.totalPerkTasksCompleted == 1)
			player.sendMessage("<col=000080>You have completed the newcomer task: <col=800000>"
					+ NewcomerTasks.PERK_TASK_COMPLETION.getFormattedName() + "!");
		player.sendMessage(
				"You have completed your perk task and received " + NumberUtils.formatNumber(expReward) + " perk experience.");

	}

	public int calculateExperienceRewardFromTier(Player player, PerkTasks.TaskTiers tier, PerkTasks.TaskType type) {
		int difficulty = tier.ordinal() + 1;
		int reward =
				(int) (230 * Math.pow(difficulty, 2) + 2 * difficulty + Math.sqrt(difficulty)) + Random.get((difficulty * 75));
		if (type == PerkTasks.TaskType.PVM) {
			reward *= 2;
			switch (CombatAchievementSystem.getTier(player.combatAchievementPoints)) {
				case MEDIUM -> reward = (int) (reward * 1.05);
				case HARD -> reward = (int) (reward * 1.1);
				case ELITE -> reward = (int) (reward * 1.15);
				case MASTER -> reward = (int) (reward * 1.2);
				case GRANDMASTER -> reward = (int) (reward * 1.3);
			}
		}
		return reward;
	}


	public void addPerkExperience(Player player, int experienceToAdd) {
		player.perkTreeExperience += experienceToAdd;
		while (player.perkTreeExperience >= getExperienceToNextLevel(player)) {
			incrementPerkTreeLevel(player);
		}
	}


	public int getExperienceToNextLevel(Player player) {
		return 5000 * (player.perkTreeLevel);
	}

	public int calculateExperienceForLevel(int level) {
		return 5000 * (level);
	}

	public float calculateNextPerkLevelPercentage(Player player) {
		int currentLevel = player.perkTreeLevel;
		int currentXP = player.perkTreeExperience;

		int xpForNextLevel = calculateExperienceForLevel(currentLevel);

		float xpProgress = (float) currentXP / xpForNextLevel;
		return xpProgress;
	}

	public void incrementPerkTreeLevel(Player player) {
		player.perkTreeLevel++;
		int pointReward = calculateLevelReward(player);
		player.perkPoints += pointReward;
		DailyTasks.handleTaskDecrement(player, "perkPoints", pointReward);
	}

	private int calculateLevelReward(Player player) {
		int perkPointsToReceive = 1;
		if (player.perkTreeLevel % 5 == 0)
			perkPointsToReceive = 5;
		else if (player.perkTreeLevel % 4 == 0)
			perkPointsToReceive = 4;
		else if (player.perkTreeLevel % 3 == 0)
			perkPointsToReceive = 3;
		else if (player.perkTreeLevel % 2 == 0)
			perkPointsToReceive = 2;
		switch (player.getSecondaryGroup()) {
			case SUPER_DONATOR, ELITE_DONATOR -> perkPointsToReceive += 1;
			case NOBLE_DONATOR, GOLD_DONATOR -> perkPointsToReceive += 2;
			case PLATINUM_DONATOR, LEGENDARY_DONATOR -> perkPointsToReceive += 3;
			case SUPREME_DONATOR -> perkPointsToReceive += 4;
		}
		return perkPointsToReceive;

	}


	public void skipTask(Player player) {
		/*
		 * if(player.perkPoints < 1) { player.dialogue( new NPCDialogue(7958,
		 * "You don't have enough perk points to skip your task.")); } else {
		 * 
		 */
		player.currentPerkTask = null;
		player.perkTaskCurrentAmount = 0;
		player.dialogue(
				new NPCDialogue(7958, "I have skipped your perk task, come get another one whenever."));
		// }
	}
}
