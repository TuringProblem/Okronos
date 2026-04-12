package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class MasterThief extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	public int getChanceToAvoidStun() {
		return (int) (15 + (perkLevel * 7.5));
	}

	public int getChanceToSucceed() {
		return (int) (5 + (perkLevel * 7.5));
	}

	@Override
	public String getPerkName() {
		return "Master Thief";
	}

	@Override
	public String getPerkDescription() {
		return "Thieving boost.";
	}

	@Override
	public String getPerkEffect() {
		return "You will have a " + getChanceToAvoidStun() + "% chance to avoid being stunned and a " +
			getChanceToSucceed() + "% chance to succeed a thieve when you should have failed.";
	}

	public int getChanceToAvoidStun(int level) {
		return (int) (15 + (level * 7.5));
	}

	public int getChanceToSucceed(int level) {
		return (int) (5 + (level * 7.5));
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a chance to avoid being stunned and you have a chance to successfully thieve a thieve you should have failed.<br><br>" +
			"At level 1 you will have a " + getChanceToAvoidStun(1) + "% chance to avoid being stunned, you will also have a " + getChanceToSucceed(1) + "% chance to succeed a thieve you should have failed.<br><br>" +
			"At level 2 you will have a " + getChanceToAvoidStun(2) + "% chance to avoid being stunned, you will also have a " + getChanceToSucceed(2) + "% chance to succeed a thieve you should have failed.<br><br>" +
			"At level 3 you will have a " + getChanceToAvoidStun(3) + "% chance to avoid being stunned, you will also have a " + getChanceToSucceed(3) + "% chance to succeed a thieve you should have failed.<br><br>" +
			"At level 4 you will have a " + getChanceToAvoidStun(4) + "% chance to avoid being stunned, you will also have a " + getChanceToSucceed(4) + "% chance to succeed a thieve you should have failed.<br><br>" +
			"At level 5 you will have a " + getChanceToAvoidStun(5) + "% chance to avoid being stunned, you will also have a " + getChanceToSucceed(5) + "% chance to succeed a thieve you should have failed."
			;
	}
}
