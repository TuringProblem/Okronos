package io.ruin.model.entity.player.groupironmode.sorts;

import io.ruin.model.entity.player.groupironmode.GroupIronmanTeam;

import java.util.Comparator;

public class GroupIronmanTeamDateSort implements Comparator<GroupIronmanTeam> {

	@Override
	public int compare(GroupIronmanTeam o1, GroupIronmanTeam o2) {
		return o1.getDateStated().compareTo(o2.getDateStated());
	}

	public static final GroupIronmanTeamDateSort DATE_SORT = new GroupIronmanTeamDateSort();
}
