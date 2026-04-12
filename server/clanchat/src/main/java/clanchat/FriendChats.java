package clanchat;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import lombok.experimental.ExtensionMethod;

import static core.task.api.API.*;

import clanchat.inter.FriendChatSettingsInterface;
import core.task.Continuation;
import friendlist.FriendListDb;
import friendlist.FriendRank;

@ExtensionMethod(Attributes.class)
public class FriendChats {

	public static void leaveLogout(Player player) {
		var current = player.clan().joinedName;
		if (current.isEmpty()) {
			return;
		}

		queue(() -> {
			var chat = FriendChatDb.db().load(current).await();
			chat.removeMember(player.getName());
			FriendChatUpdater.notifyLeft(player, chat);
		});
	}

	public static void leaveCurrent(Player player) {
		var current = player.clan().joinedName;
		if (current.isEmpty()) {
			return;
		}

		queue(() -> {
			player.clan().joinedName = "";
			var chat = FriendChatDb.db().load(current).await();
			chat.removeMember(player.getName());
			FriendChatUpdater.sendLeave(player);
			FriendChatUpdater.notifyLeft(player, chat);
		});
	}

	public static void leave(FriendChat chat, String username) {
		queue(() -> {
			chat.removeMember(username);
			var player = World.getPlayer(username);
			if (player != null) {
				player.clan().joinedName = "";
				FriendChatUpdater.sendLeave(player);
			}
			FriendChatUpdater.notifyLeft(username, chat);
		});
	}

	public static void joinOnLogin(Player player) {
		var joined = player.clan().joinedName;
		if (joined.isEmpty()) {
			join(player, "kal");
			return;
		}
		joinCurrent(player);
	}

	public static void joinCurrent(Player player) {
		var joined = player.clan().joinedName;
		if (joined.isEmpty()) {
			return;
		}

		queue(() -> {
			var chat = FriendChatDb.db().load(joined).await();
			chat.addMember(player.getName());
			FriendChatUpdater.sendInitJoin(player, chat);
			FriendChatUpdater.notifyMemberJoined(player, chat);
		});
	}

	public static void join(Player player, String ownerName) {
		var current = player.clan().joinedName;
		if (!current.isEmpty()) {
			player.sendMessage("You are already in the friends chat.");
			return;
		}

		queue(() -> {
			var chat = FriendChatDb.db().load(ownerName).await();
			if (!chat.exists) {
				player.sendMessage(ownerName + " does not currently own a clan.");
				return;
			}
			player.clan().joinedName = ownerName;
			chat.addMember(player.getName());
			FriendChatUpdater.sendInitJoin(player, chat);
			FriendChatUpdater.notifyMemberJoined(player, chat);
		});
	}

	public static void updatePermissionTalk(Player owner, ClanPermission permission) {
		queue(() -> {
			var it = FriendChatDb.db().load(owner.getName()).await();
			it.permissionTalk = permission;
			FriendChatSettingsInterface.updateView(owner, it);
		});
	}

	public static void updatePermissionJoin(Player owner, ClanPermission permission) {
		queue(() -> {
			var it = FriendChatDb.db().load(owner.getName()).await();
			it.permissionJoin = permission;
			FriendChatSettingsInterface.updateView(owner, it);
		});
	}

	public static void updatePermissionKick(Player owner, ClanPermission permission) {
		queue(() -> {
			var it = FriendChatDb.db().load(owner.getName()).await();
			it.permissionKick = permission;
			FriendChatSettingsInterface.updateView(owner, it);
		});
	}

	public static void updateChannelName(Player owner, String name) {
		queue(() -> {
			var it = FriendChatDb.db().load(owner.getName()).await();
			it.channelName = name;
			it.exists = true;
			FriendChatSettingsInterface.updateView(owner, it);
		});
	}

	public static void markChannelDisabled(Player owner) {
		queue(() -> {
			var it = FriendChatDb.db().load(owner.getName()).await();
			it.exists = false;
			FriendChatSettingsInterface.updateView(owner, it);
		});
	}

	public static Continuation<FriendRank> memberRank(String clanOwner, String memberUsername) {
		return () -> {
			if (clanOwner.equalsIgnoreCase(memberUsername)) {
				return FriendRank.Owner;
			}

			var ownerFriendChat = FriendListDb.db().load(clanOwner).await();
			var ownerFriend = ownerFriendChat.friend(memberUsername);
			if (ownerFriend == null) {
				return FriendRank.None;
			}

			return ownerFriend.getRank();
		};
	}

	public static void kick(Player kicker, String toBeKicked) {
		queue(() -> {
			var current = kicker.clan().joinedName;
			if (current.isEmpty()) {
				kicker.sendMessage("You are currently not in a friends chat.");
				return;
			}
			var clan = FriendChatDb.db().load(current).await();
			var kickerRank = memberRank(current, kicker.uuid()).await();
			if (!clan.permissionKick.allow(kickerRank)) {
				kicker.sendMessage("You (" + kickerRank + ") are not allowed to kick people.");
				return;
			}
			leave(clan, toBeKicked);
		});
	}
}
