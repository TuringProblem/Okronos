package io.ruin.model.activities.raids.toa;

import io.ruin.model.item.actions.impl.AchievementLamp;

public enum Invocations {
	TRY_AGAIN("Try Again", 5),
	PERSISTANCE("Persistance", 10),
	SOFTCORE_RUN("Softcore Run", 15),
	HARDCORE_RUN("Hardcore Run", 25),
	WALK_FOR_IT("Walk For It", 10),
	JOG_FOR_IT("Jog For It", 15),
	RUN_FOR_IT("Run For It", 20),
	SPRINT_FOR_IT("Sprint For It", 25),
	NEED_SOME_HELP("Need Some Help?", 15),
	NEED_LESS_HELP("Need Less Help?", 25),
	NO_HELP_NEEDED("No Help Needed", 40),
	WALK_THE_PATH("Walk The Path", 50),
	PATHSEEKER("Pathseeker", 15),
	PATHFINDER("Pathfinder", 40),
	PATHMASTER("Pathmaster", 50),
	QUIET_PRAYERS("Quiet Prayers", 20),
	DEADLY_PRAYERS("Deadly Prayers", 20),
	ON_A_DIET("On A Diet", 15),
	DEHYDRATION("Dehydration", 30),
	OVERLY_DRAINING("Overly Draining", 15),
	LIVELY_LARVAE("Lively Larvae", 5),
	MORE_OVERLORDS("More Overlords", 15),
	BLOWING_MUD("Blowing Mud", 10),
	MEDIC("Medic!", 15),
	AERIAL_ASSAULT("Aerial Assault", 10),
	NOT_JUST_A_HEAD("Not Just A Head", 15),
	ARTERIAL_SPRAY("Arterial Spray", 10),
	BLOOD_THINNERS("Blood Thinners", 5),
	UPSET_STOMACH("Upset Stomach", 15),
	DOUBLE_TROUBLE("Double Trouble", 20),
	KEEP_BACK("Keep Back", 10),
	STAY_VIGILANT("Stay Vigilant", 15),
	FEELING_SPECIAL("Feeling Special?", 20),
	MIND_THE_GAP("Mind The Gap", 10),
	GOTTA_HAVE_FAITH("Gotta Have Faith", 10),
	JUNGLE_JAPES("Jungle Japes", 5),
	SHAKING_THINGS_UP("Shaking Things Up", 10),
	BOULDERDASH("Boulderdash", 10),
	ACCELERATION("Acceleration", 10),
	PENETRATION("Penetration", 10),
	OVERCLOCKED("Overclocked", 10),
	OVERCLOCKED2("Overclocked 2", 10),
	INSANITY("Insanity", 50),
	;
	int invocationLevel;
	String invocationName;

	Invocations(String invocationName, int invocationLevel) {
		this.invocationLevel = invocationLevel;
		this.invocationName = invocationName;
	}

	public static final Invocations[] VALUES = values();
}
