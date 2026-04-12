package npc.nex.modes;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public enum Forms {
	DEFAULT_NEX(11278),
	UNATTACKABLE_NEX(11279),
	SOUL_SPLIT_NEX(11280),
	REFLECT_MELEE_NEX(11281),
	WRATH_NEX(11282),
	;
	@Getter
	private final int npcId;
	Forms(int npcId) {
		this.npcId = npcId;
	}

	public static boolean isNex(int npcId) {
		return Arrays.stream(Forms.values())
			.anyMatch(form -> form.npcId == npcId);
	}
}
