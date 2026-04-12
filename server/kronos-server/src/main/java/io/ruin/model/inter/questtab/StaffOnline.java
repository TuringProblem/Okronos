package io.ruin.model.inter.questtab;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerGroup;

import java.util.LinkedList;
import java.util.List;

public class StaffOnline {

	public static void open(Player player) {
		List<String> text = new LinkedList<>();
		List<String> owners = new LinkedList<>();
		List<String> devs = new LinkedList<>();
		List<String> admins = new LinkedList<>();
		List<String> mods = new LinkedList<>();
		List<String> slaves = new LinkedList<>();
		World.players().forEach(p -> {
			if (player.isGroup(PlayerGroup.OWNER)) owners.add(p.getName());
			else if (player.isGroup(PlayerGroup.DEVELOPER)) devs.add(p.getName());
			else if (player.isGroup(PlayerGroup.ADMINISTRATOR)) admins.add(p.getName());
			else if (player.isGroup(PlayerGroup.MODERATOR)) mods.add(p.getName());
			else if (player.isGroup(PlayerGroup.SUPPORT)) slaves.add(p.getName());
		});
		text.add("<img=82><col=00A3FF><shad=0000000> Owners</col></shad>");
		if (owners.size() == 0) text.add("None online!");
		else text.addAll(owners);
		text.add("");

		text.add("<img=80><col=9E00FF><shad=0000000> Developers</col></shad>");
		if (devs.size() == 0) text.add("None online!");
		else text.addAll(devs);
		text.add("");

		text.add("<img=1><col=bbbb00><shad=0000000> Administrators</col></shad>");
		if (admins.size() == 0) text.add("None online!");
		else text.addAll(admins);
		text.add("");

		text.add("<img=0><col=b2b2b2><shad=0000000> Moderators<col></shad>");
		if (mods.size() == 0) text.add("None online!");
		else text.addAll(mods);
		text.add("");

		text.add("<img=18><col=5bccc4><shad=0000000> Supports</col></shad>");
		if (slaves.size() == 0) text.add("None online!");
		else text.addAll(slaves);

		player.sendScroll("Staff Online", text.toArray(new String[0]));
		return;

	}
}
