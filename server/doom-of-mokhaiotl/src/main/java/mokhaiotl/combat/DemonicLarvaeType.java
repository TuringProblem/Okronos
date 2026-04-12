package mokhaiotl.combat;

import io.ruin.api.utils.Random;
import io.ruin.model.entity.npc.NPC;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-02
 */
@Getter
@RequiredArgsConstructor
public enum DemonicLarvaeType {

	NEUTRAL(14710, null),
	RANGED(14711, NPC.DefaultHeadIconIndex.MageAndMelee),
	MAGIC(14712, NPC.DefaultHeadIconIndex.RangeAndMelee),
	MELEE(14713, NPC.DefaultHeadIconIndex.RangeAndMage),
	GIANT_RANGED(14788, NPC.DefaultHeadIconIndex.MageAndMelee),
	GIANT_MAGE(14789, NPC.DefaultHeadIconIndex.RangeAndMelee)

	;
	private final int npcId;
	private final NPC.DefaultHeadIconIndex overheadPrayer;
}
