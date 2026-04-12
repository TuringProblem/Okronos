package com.reasonps.dominion.bosses.dagannoth_kings.kings;

import com.reasonps.dominion.bosses.dagannoth_kings.EchoDagannothKingCombatCombat;
import io.ruin.model.entity.npc.NPC;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-28
 */
public class EchoDagannothRexCombat extends EchoDagannothKingCombatCombat {


	@Override
	public void follow() {
		follow(1);
	}

	@Override
	public boolean attack() {
		if (withinDistance(1)) {
			basicAttack(info.attack_animation, info.attack_style, info.max_damage);
			return true;
		}
		return true;
	}

	@Override
	public void togglePrayAgainstAll() {
		if (getNpc().overheadPrayer() != null)
			getNpc().removeHeadIcon();
		else
			getNpc().setHeadIcon(NPC.DefaultHeadIconIndex.MageRangeMelee);
	}
}
