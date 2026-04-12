package io.ruin.model.entity.player.groupironmode.sorts;

import io.ruin.model.entity.player.groupironmode.GroupIronmanTeam;

import java.util.Comparator;

public class GroupIronmanTeamAverageSort implements Comparator<GroupIronmanTeam> {

	@Override
	public int compare(GroupIronmanTeam o1, GroupIronmanTeam o2) {
		return Integer.compare(o2.getAverageTotalLevel(), o1.getAverageTotalLevel());
	}


	public static final GroupIronmanTeamAverageSort AVERAGE_SORT = new GroupIronmanTeamAverageSort();
}
