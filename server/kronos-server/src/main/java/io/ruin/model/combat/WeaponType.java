package io.ruin.model.combat;

import com.google.gson.annotations.Expose;
import lombok.ToString;

@ToString
public class WeaponType {

	public static WeaponType UNARMED;

	public int config;
	public int maxDistance;
	public int attackTicks;
	public int attackAnimation;
	public int defendAnimation;
	public int equipSound;
	public int attackSound;
	public AttackSet[] attackSets;
	public int[] renderAnimations;

}
