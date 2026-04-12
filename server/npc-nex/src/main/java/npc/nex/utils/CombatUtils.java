package npc.nex.utils;

import io.ruin.cache.NPCType;
import npc.nex.modes.Forms;
import npc.nex.scripts.CruorCombat;
import npc.nex.scripts.FumusCombat;
import npc.nex.scripts.GlaciesCombat;
import npc.nex.scripts.UmbraCombat;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public class CombatUtils {

	public static void register() {
		// Minions
		var fumus = NPCType.get(FumusCombat.ID);
		var cruor = NPCType.get(CruorCombat.ID);
		var umbra = NPCType.get(UmbraCombat.ID);
		var glacis = NPCType.get(GlaciesCombat.ID);
		fumus.swimClipping = true;
		cruor.swimClipping = true;
		umbra.swimClipping = true;
		glacis.swimClipping = true;

		// Boss
		var def1 = NPCType.get(Forms.DEFAULT_NEX.getNpcId());
		var def2 = NPCType.get(Forms.UNATTACKABLE_NEX.getNpcId());
		var def3 = NPCType.get(Forms.SOUL_SPLIT_NEX.getNpcId());
		var def4 = NPCType.get(Forms.WRATH_NEX.getNpcId());
		var def5 = NPCType.get(Forms.REFLECT_MELEE_NEX.getNpcId());
		def1.ignoreMultiCheck = true;
		def2.ignoreMultiCheck = true;
		def3.ignoreMultiCheck = true;
		def4.ignoreMultiCheck = true;
		def5.ignoreMultiCheck = true;
	}
}
