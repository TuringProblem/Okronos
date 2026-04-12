package io.ruin.services;

import io.ruin.model.entity.npc.actions.edgeville.CreditManager;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerGroup;
import io.ruin.model.entity.player.SecondaryGroup;

public class XenGroup {

	public static void update(Player player) {
		SecondaryGroup donationGroup = CreditManager.getGroup(player);
		if (donationGroup != null)
			donationGroup.sync(player, "donator");
		//todo ironman checks
	}

}
