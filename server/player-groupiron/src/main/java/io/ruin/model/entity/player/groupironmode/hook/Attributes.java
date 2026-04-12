package io.ruin.model.entity.player.groupironmode.hook;

import static player.attributes.api.API.*;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.groupironmode.GroupBank;
import io.ruin.model.entity.player.groupironmode.GroupIron;
import io.ruin.model.entity.player.groupironmode.GroupIronGroups;

public class Attributes {

	public static void register() {
		//attrib().register().persistent(GroupIron.class, GroupIron::new);
		attrib().register().persistent(GroupBank.class, GroupBank::new);
	}

	public static GroupIron getGroupIron(Player player) {
		return GroupIronGroups.getGroup(player.newGroupId);
	}

	public static GroupBank getGroupBank(Player player) {
		return attrib(GroupBank.class, player);
	}
}
