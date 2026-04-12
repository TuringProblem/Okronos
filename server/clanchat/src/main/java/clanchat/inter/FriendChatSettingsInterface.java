package clanchat.inter;

import java.util.Arrays;

import clanchat.ClanPermission;
import clanchat.FriendChat;
import clanchat.FriendChats;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.OptionAction;

public class FriendChatSettingsInterface {

	public static void register() {
		InterfaceHandler.register(Interface.CLAN_CHAT_SETTINGS, h -> {
			h.actions[10] = (OptionAction) (player, option) -> {
				if (option == 1) {
					player.nameInput("Enter chat prefix:", name -> {
						name = name.replaceAll("[^a-zA-Z0-9\\s]", "");
						name = name.substring(0, Math.min(name.length(), 12));
						if (name.isEmpty()) {
							player.retryStringInput("Invalid chat prefix, try again:");
							return;
						}
						FriendChats.updateChannelName(player, name);
					});
					return;
				}
				FriendChats.markChannelDisabled(player);
			};
			h.actions[13] = (OptionAction) (player, option) -> {
				var idx = (option - 2);
				var permission = Arrays.stream(ClanPermission.values()).filter(it -> it.id == idx).findFirst()
						.orElse(ClanPermission.Owner);
				FriendChats.updatePermissionJoin(player, permission);
			};
			h.actions[16] = (OptionAction) (player, option) -> {
				var idx = (option - 2);
				var permission = Arrays.stream(ClanPermission.values()).filter(it -> it.id == idx).findFirst()
						.orElse(ClanPermission.Owner);
				FriendChats.updatePermissionTalk(player, permission);
			};
			h.actions[19] = (OptionAction) (player, option) -> {
				var idx = (option - 2);
				var permission = Arrays.stream(ClanPermission.values()).filter(it -> it.id == idx).findFirst()
						.orElse(ClanPermission.Owner);
				FriendChats.updatePermissionKick(player, permission);
			};
		});
	}

	public static void updateView(Player player, FriendChat chat) {
		if (chat.exists) {
			setChannelName(player, chat.channelName);
		} else {
			setChannelName(player, "Channel Disabled");
		}
		setEnterRank(player, chat.permissionJoin);
		setTalkRank(player, chat.permissionTalk);
		setKickRank(player, chat.permissionKick);
	}

	private static void setChannelName(Player player, String name) {
		player.getPacketSender().sendString(Interface.CLAN_CHAT_SETTINGS, 10, name);
	}

	private static void setEnterRank(Player player, ClanPermission rank) {
		player.getPacketSender().sendString(Interface.CLAN_CHAT_SETTINGS, 13, rank.text);
	}

	private static void setTalkRank(Player player, ClanPermission rank) {
		player.getPacketSender().sendString(Interface.CLAN_CHAT_SETTINGS, 16, rank.text);
	}

	private static void setKickRank(Player player, ClanPermission rank) {
		player.getPacketSender().sendString(Interface.CLAN_CHAT_SETTINGS, 19, rank.text);
	}

}
