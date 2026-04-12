package npc.nex.modes;

import npc.nex.scripts.CruorCombat;
import npc.nex.scripts.FumusCombat;
import npc.nex.scripts.GlaciesCombat;
import npc.nex.scripts.UmbraCombat;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public enum Phase {
	SPAWN(1, -1, ""),
	SMOKE(1, -1, "Fill my soul with smoke!"),
	SMOKE_LOCKED(0.8, FumusCombat.ID, "Fumus, don't fail me!"),
	SHADOW(0.8, -1, "Darken my shadow!"),
	SHADOW_LOCKED(0.6, UmbraCombat.ID, "Umbra, don't fail me!"),
	BLOOD(0.6, -1, "Flood my lungs with blood!"),
	BLOOD_LOCKED(0.4, CruorCombat.ID, "Cruor, don't fail me!"),
	ICE(0.4, -1, "Infuse me with the power of ice!"),
	ICE_LOCKED(0.2, GlaciesCombat.ID, "Glacies, don't fail me!"),
	ZAROS(0.2, -1, "NOW, THE POWER OF ZAROS!");

	private final double hitpointsPercent;
	@Getter
	private final int minionId;
	@Getter
	private final String phrase;

	Phase(double hitpointsPercent, int minionId, String phrase) {
		this.hitpointsPercent = hitpointsPercent;
		this.minionId = minionId;
		this.phrase = phrase;
	}

	public static final Phase[] VALUES = values();

	double getHitpointsPercent() {
		return hitpointsPercent;
	}

	public int getLockedMinionId() {
		return Arrays.stream(VALUES)
			.filter(value -> this != value)
			.filter(value -> value.name().startsWith(name()))
			.findFirst()
			.map(Phase::getMinionId)
			.orElse(-1);
	}

	public static Phase getPhase(double hpPercent, boolean locked) {
		return IntStream.iterate(VALUES.length - 1, i -> i > 0, i -> i - 1)
			.mapToObj(i -> VALUES[i])
			.filter(value -> !(hpPercent > value.hitpointsPercent))
			.filter(value -> !locked || value.name().endsWith("LOCKED"))
			.filter(value -> locked || !value.name().endsWith("LOCKED"))
			.findFirst()
			.orElse(SMOKE);
	}
}