package io.ruin.model.activities.raids.chambersrework;

public enum XericInvocations {
	TEKTON("Tekton", 25), //Adds tekton to raid
	VASA("Vasa", 25), //Adds vasa to raid
	SHAMANS("Shamans", 25), //Adds shaman to raid
	TRUE_GUARDIANS("True Guardians", 15),//Guardians deal more damage, more def & specials hit harder
	FEELING_MYSTICAL("Feeling Mystical", 15),//Mystics hit harder through prayer
	PAY_ATTENTION("Pay Attention", 15), //Vanguard threshold is lower and they have a high attack radius and deal more dmg
	TEKTONIC("Tektonic", 15), //Tekton has a higher attack radius and deals more damage
	UNDEATHLY_FORCE("Undeathly Force", 15), //Deathly magers/rangers hit harder
	DAMN_MUTT("Damn Mutt", 15), //Muttadile hits harder
	SHAMOON("Shamoon", 15), //More shamans spawn
	OH_VAS("Oh Vas", 15), //Vasa has double hp and rock throws deal double damage
	ENHANCED_SPECIALS("Enhanced Specials", 30), //Olm specials deal 50+ minimum
	OVERLOADED_OLM("Overloaded Olm", 30), //Olm has more phases and deals more damage with orbs/auto attacks
	CONTINUOS_CRYSTALS("Continuous Crystals", 30), //Olm crystals spawn throughout fight

	;

	int invocationLevel;
	String invocationName;

	XericInvocations(String invocationName, int invocationLevel) {
		this.invocationLevel = invocationLevel;
		this.invocationName = invocationName;
	}
}
