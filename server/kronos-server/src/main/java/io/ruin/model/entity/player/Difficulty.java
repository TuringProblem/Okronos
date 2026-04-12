package io.ruin.model.entity.player;

public enum Difficulty {
	EASY("Easy", 100.0f, 1.0f, 5000, 75000, 150000, 300000, 10, false, 0, false, false, true), // Exp rate = 100x?
	INTERMEDIATE("Intermediate", 25.0f, 0.9f, 20000, 175000, 400000, 800000, 10, true, 20, false, false, true), //Exp rate = 25x?
	HARD("Hard", 10.0f, 0.82f, 50000, 250000, 600000, 1200000, 5, true, 10, true, false, true), //Exp rate = 10x?
	EXTREME("Extreme", 3.0f, 0.7f, 85000, 500000, 1250000, 2500000, 1, true, 5, true, true, true), //Exp rate = 3x?
	OSRS("Insane", 1.0f, 0.7f, 100000, 750000, 2000000, 5000000, 2, true, 5, true, true, true);
	public String Name;
	float experienceBonus;
	float dropRateBonus;
	int level99Points;
	int maxExpPoints;
	int maxExpPoints2;
	int maxExpPoints3;
	int percentagePointBoost;
	boolean repeatSlayTask;
	int tasksToCompleteToRepeat;
	boolean trainingWeapons;
	boolean accessPremiumBank;
	boolean receiveTrainingAmulet;


	private Difficulty(String Name, float expBoost, float dropRateBoost, int maxLevelPointBoost, int maxExpPointBoost, int maxExpPointBoost2, int maxExpPointBoost3, int percentagePointBoost, boolean repeatTask,
	                   int tasksToComplete, boolean receiveTrainingWeapons, boolean premiumBankAccess, boolean receiveTrainingAmulet) {
		this.Name = Name;
		this.experienceBonus = expBoost;
		this.dropRateBonus = dropRateBoost;
		this.level99Points = maxLevelPointBoost;
		this.maxExpPoints = maxExpPointBoost;
		this.maxExpPoints2 = maxExpPointBoost2;
		this.maxExpPoints3 = maxExpPointBoost3;
		this.percentagePointBoost = percentagePointBoost;
		this.repeatSlayTask = repeatTask;
		this.tasksToCompleteToRepeat = tasksToComplete;
		this.trainingWeapons = receiveTrainingWeapons;
		this.accessPremiumBank = premiumBankAccess;
		this.receiveTrainingAmulet = receiveTrainingAmulet;
	}

	public float GetExperienceBoost() {
		return experienceBonus;
	}

	public float GetDropRate() {
		return dropRateBonus;
	}

	public int GetMaxLevelRewardBonus() {
		return level99Points;
	}

	public int GetMaxExpReward() {
		return maxExpPoints;
	}

	public int GetMaxExpReward2() {
		return maxExpPoints2;
	}

	public int GetMaxExpReward3() {
		return maxExpPoints3;
	}

	public int GetPercentagePointBoost() {
		return percentagePointBoost;
	}

	public boolean GetRepeatTask() {
		return repeatSlayTask;
	}

	public int GetTasksToComplete() {
		return tasksToCompleteToRepeat;
	}

	public boolean GetReceiveTrainingWeapons() {
		return trainingWeapons;
	}

	public boolean GetPremiumBankAccess() {
		return accessPremiumBank;
	}

	public boolean GetTrainingAmulet() {
		return receiveTrainingAmulet;
	}


}
