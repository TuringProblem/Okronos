package io.ruin.model.entity.player.groupironmode;

import io.ruin.Server;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.groupironmode.hook.Attributes;
import io.ruin.model.inter.ClientInterfaceType;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.questtab.JournalTab.Section;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Attributes.class)
public class GroupSettingsTabInterface {

	public static void register() {
		InterfaceHandler.register(1088, h -> {
			h.actions[35] = (SimpleAction) p -> {
				var pGim = p.getGroupIron();
				if (pGim == null) {
					p.sendMessage("You already left the group.");
					return;
				}
				p.getGroupIron().leaveGroup(p);
			};
		});

		InterfaceHandler.register(Interface.QUEST_TAB, interfaceHandler -> {
			interfaceHandler.actions[18] = (SimpleAction) GroupSettingsTabInterface::open;
		});
	}

	public static void open(Player player) {
		player.currentSection = Section.GROUP_IRON;

		if (!player.isGroupIronman()) {
			player.getPacketSender().openSubInterface(1090, 629, 28, ClientInterfaceType.OVERLAY);
			player.getPacketSender().sendString(1090, 0, "No group data could be loaded.");
			return;
		}

		if (player.newGroupId <= 0) {
			player.getPacketSender().openSubInterface(1090, 629, 28, ClientInterfaceType.OVERLAY);
			player.getPacketSender().sendString(1090, 0,
					"Join a group to display group<br>data.<br><br>::creategroup to make one.");
		} else if (player.getGroupIron() != null) {
			player.getPacketSender().openSubInterface(1088, 629, 28, ClientInterfaceType.OVERLAY);
			displayOverview(player);
		}
	}

	private static void displayOverview(Player player) {
		var ps = player.getPacketSender();
		int INTERFACE_ID = 1088;
		ps.sendString(INTERFACE_ID, 2, player.getGroupIron().getGroupName());
		for (int i = 0; i < 5; i++) {
			ps.sendString(INTERFACE_ID, 18 + i, "Vacancy");
			ps.sendString(INTERFACE_ID, 23 + i, "");
		}

		if (player.getGroupIron().isHardcore()) {
			ps.sendString(INTERFACE_ID, 32, String.valueOf(player.getGroupIron().getLivesRemaining()));
		} else {
			ps.sendString(INTERFACE_ID, 32, "N/A");
		}

		ps.sendString(INTERFACE_ID, 29, String.valueOf(player.getGroupIron().getMembers().size()));
		ps.sendString(INTERFACE_ID, 30, Server.getTime());

		for (int i = 0; i < player.getGroupIron().getMembers().size(); i++) {
			var memberName = player.getGroupIron().getMembers().get(i);
			ps.sendString(INTERFACE_ID, 18 + i, memberName);
			if (World.getPlayer(player.getGroupIron().getMembers().get(i)) != null)
				ps.sendString(INTERFACE_ID, 23 + i, "<shad=000000><col=26ed50>Online");
			else
				ps.sendString(INTERFACE_ID, 23 + i, "<shad=000000><col=e71010>Offline");
		}
	}
}
