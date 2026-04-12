package io.ruin.model.entity.player.groupironmode;

import io.ruin.model.entity.player.groupironmode.db.GroupIronDb;
import java.util.HashMap;

import core.task.Continuations;

public class GroupIronGroups {
	static HashMap<Integer, GroupIron> groups = new HashMap<>();

	public static void register() {
		Continuations.schedule(() -> {
			var groups = GroupIronDb.fetchAll().await();
			groups.forEach(GroupIronGroups::addGroup);
		});
	}

	public static void addGroup(GroupIron group) {
		groups.put(group.getGroupId(), group);
	}

	public static GroupIron getGroup(int groupId) {
		return groups.get(groupId);
	}

}
