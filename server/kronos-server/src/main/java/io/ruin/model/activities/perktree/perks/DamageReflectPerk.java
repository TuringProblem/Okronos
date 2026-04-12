package io.ruin.model.activities.perktree.perks;

import com.google.gson.annotations.Expose;
import io.ruin.cache.ItemID;
import io.ruin.model.activities.perktree.PlayerPerk;

public class DamageReflectPerk extends PlayerPerk {

	public int getDeflectAmount(int damage) {
		double deflectCalc = 15 + (perkLevel * 5);
		double division = deflectCalc / 100;
		int damageToDeflect = (int) (division * damage);
		return damageToDeflect;
	}

	public int getDeflectChance() {
		return 10 + (perkLevel * 5);
	}

	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	@Override
	public void activatePerk() {
		isActive = true;

	}

	@Override
	public String getPerkName() {
		return "Damage Reflect";
	}

	@Override
	public String getPerkDescription() {
		return "Deflect enemy damage.";
	}

	@Override
	public String getPerkEffect() {
		double deflectCalc = 15 + (perkLevel * 5);
		return "You will deflect " + deflectCalc + "% of incoming damage if damage deflect is rolled.<br>" +
			"You have a " + getDeflectChance() + "% chance of deflecting incoming damage.";
	}

	private double getDeflect(int level) {
		return 15 + (level * 5);
	}

	private double getDeflectChance(int level) {
		return 10 + (level * 5);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a chance to deflect incoming damage to your opponent.<br><br>" +
			"At level 1 you will have a " + getDeflectChance(1) +
			"% chance of deflecting " + getDeflect(1) + "% of incoming damage.<br><br>" +
			"At level 2 you will have a " + getDeflectChance(2) +
			"% chance of deflecting " + getDeflect(2) + "% of incoming damage.<br><br>" +
			"At level 3 you will have a " + getDeflectChance(3) +
			"% chance of deflecting " + getDeflect(3) + "% of incoming damage.<br><br>" +
			"At level 4 you will have a " + getDeflectChance(4) +
			"% chance of deflecting " + getDeflect(4) + "% of incoming damage.<br><br>" +
			"At level 5 you will have a " + getDeflectChance(5) +
			"% chance of deflecting " + getDeflect(5) + "% of incoming damage."
			;
	}
}
