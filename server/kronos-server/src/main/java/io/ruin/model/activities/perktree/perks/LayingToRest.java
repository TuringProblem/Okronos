package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.PlayerPerk;
import io.ruin.model.entity.player.Player;

import java.text.DecimalFormat;

public class LayingToRest extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public int getPrayerPointsAdditionMultiplier(Player player, double prayerExperienceGiven) {
		int points = 0;
		switch (perkLevel) {
			case 1:
				points = (int) (prayerExperienceGiven * 0.26);
				break;
			case 2:
				points = (int) (prayerExperienceGiven * 0.43);
				break;
			case 3:
				points = (int) (prayerExperienceGiven * 0.61);
				break;
			case 4:
				points = (int) (prayerExperienceGiven * 0.78);
				break;
			case 5:
				points = (int) (prayerExperienceGiven * 0.96);
				break;
		}
		return points;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Laying To Rest";
	}

	@Override
	public String getPerkDescription() {
		return "Auto buryer";
	}

	@Override
	public String getPerkEffect() {
		return "Bones will be automatically buried and converted to prayer points and prayer experience based on the bone tier and perk level.<br>" +
			"The prayer points given is at a rate of experience to prayer points is 1 : " + getPrayerPointsAdditionMultiplier(perkLevel) + ".";
	}

	public double getPrayerPointsAdditionMultiplier(int level) {
		float calc = 1.0f / 12.0f;
		float formula = (level * 31.5f);
		formula /= 15;
		formula += 1;
		calc *= formula;
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return Double.parseDouble(decimalFormat.format(calc));
	}


	@Override
	public String getRepositoryDescription() {
		return "When this perk is active bones will be automatically buried and converted into prayer experience and prayer points based on the type of bone it was.<br><br>" +
			"At level 1 you the prayer points given is at a rate of experience to prayer points is 1:" + getPrayerPointsAdditionMultiplier(1) + ".<br><br>" +
			"At level 2 you the prayer points given is at a rate of experience to prayer points is 1:" + getPrayerPointsAdditionMultiplier(2) + ".<br><br>" +
			"At level 3 you the prayer points given is at a rate of experience to prayer points is 1:" + getPrayerPointsAdditionMultiplier(3) + ".<br><br>" +
			"At level 4 you the prayer points given is at a rate of experience to prayer points is 1:" + getPrayerPointsAdditionMultiplier(4) + ".<br><br>" +
			"At level 5 you the prayer points given is at a rate of experience to prayer points is 1:" + getPrayerPointsAdditionMultiplier(5) + "."
			;
	}
}
