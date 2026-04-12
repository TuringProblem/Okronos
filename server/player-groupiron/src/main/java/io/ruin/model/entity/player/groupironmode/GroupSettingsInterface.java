package io.ruin.model.entity.player.groupironmode;

import io.ruin.api.utils.StringUtils;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.groupironmode.hook.Attributes;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.network.PacketSender;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Attributes.class)
public class GroupSettingsInterface {

	private static final int interfaceID = 873;

	public static void open(Player player) {
		var pGroupIron = player.getGroupIron();
		if (pGroupIron == null) {
			player.sendMessage("You are currently not assigned to any iron group.");
			return;

		}
		PacketSender ps = player.getPacketSender();
		String groupStatus = player.isHardcoreGroupIronman() ? "Hardcore Iron Group" : "Regular Iron Group";
		String members = "";
		String difficulty = "" + pGroupIron.getGroupDifficulty();
		int memberCount = 0;
		int startingComponentNames = 48;
		int startingComponentPlayTime = 53;
		long[] playTimes = getPlayTimes(player);
		for (int i = 0; i < pGroupIron.getMembers().size(); i++) {
			if (i == 0) {
				members += StringUtils.capitalizeFirst(pGroupIron.getMembers().get(i));
			} else {
				if (memberCount == 2) {
					members += "<br>, " + StringUtils.capitalizeFirst(pGroupIron.getMembers().get(i));
					memberCount = 0;
				} else
					members += ", " + StringUtils.capitalizeFirst(pGroupIron.getMembers().get(i));
			}
			memberCount++;
		}
		for (int i = 0; i < pGroupIron.getMembers().size(); i++) {
			ps.sendString(interfaceID, startingComponentNames,
					StringUtils.capitalizeFirst(pGroupIron.getMembers().get(i)));
			ps.sendString(interfaceID, startingComponentPlayTime, TimeToText(playTimes[i]));
			startingComponentNames++;
			startingComponentPlayTime++;
		}
		int spriteId = player.isHardcoreGroupIronman() ? 2367 : 2366;
		ps.sendClientScript(10202, spriteId);
		ps.sendString(interfaceID, 16, pGroupIron.getGroupName());
		ps.sendString(interfaceID, 31, groupStatus);
		ps.sendString(interfaceID, 32, "" + pGroupIron.getMembers().size());
		ps.sendString(interfaceID, 33,
				player.isHardcoreGroupIronman() ? "" + pGroupIron.getLivesRemaining() : "N/A");
		ps.sendString(interfaceID, 34, StringUtils.capitalizeFirst(pGroupIron.getLeader()));
		ps.sendString(interfaceID, 35, pGroupIron.getCreationDate());
		ps.sendString(interfaceID, 41, "" + StringUtils.capitalizeFirst(difficulty.toLowerCase()));
		ps.sendString(interfaceID, 42, members);

		player.openInterface(ToplevelComponent.MAINMODAL, interfaceID);
	}

	private static String TimeToText(long time) {
		String text;

		long days = time / (24 * 3600);
		time %= (24 * 3600);

		long hours = time / 3600;
		time %= 3600;

		long minutes = time / 60;
		time %= 60;

		long seconds = time;

		text = days + " Days " + hours + " Hours";

		return text;
	}

	static long[] getPlayTimes(Player player) {
		long[] times = new long[5];
		for (int i = 0; i < player.getGroupIron().getMembers().size(); i++) {
			times[i] = player.getGroupIron().getPlayTimes()[i];
		}
		return times;
	}
}
