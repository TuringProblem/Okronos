package friendlist;

import java.util.ArrayList;
import java.util.List;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import net.rsprot.protocol.game.outgoing.misc.player.ChatFilterSettingsPrivateChat;
import net.rsprot.protocol.game.outgoing.social.FriendListLoaded;
import net.rsprot.protocol.game.outgoing.social.UpdateFriendList;
import net.rsprot.protocol.game.outgoing.social.UpdateIgnoreList;
import net.rsprot.protocol.game.outgoing.social.UpdateIgnoreList.IgnoredPlayer;
import net.rsprot.protocol.game.outgoing.social.UpdateIgnoreList.RemovedIgnoredEntry;
import net.rsprot.protocol.game.outgoing.social.UpdateIgnoreList.AddedIgnoredEntry;
import net.rsprot.protocol.game.outgoing.social.UpdateFriendList.Friend;
import net.rsprot.protocol.game.outgoing.social.UpdateFriendList.OfflineFriend;
import net.rsprot.protocol.game.outgoing.social.UpdateFriendList.OnlineFriend;
import io.ruin.api.utils.StringUtils;

public class FriendListUpdater {

	public static void notifyFriendListLoaded(Player player, FriendList list) {
		player.getPacketSender().write(FriendListLoaded.INSTANCE);
		player.getPacketSender().write(new ChatFilterSettingsPrivateChat(list.privacy().id));

		{
			var protocolList = new ArrayList<Friend>();
			for (var friend : list.friends()) {
				protocolList.add(justAdded(player, friend));
			}
			var update = new UpdateFriendList(protocolList);
			player.getPacketSender().write(update);
		}
		{
			var protocolList = new ArrayList<IgnoredPlayer>();
			for (var ignore : list.ignores()) {
				var username = StringUtils.capitalizeName(ignore.getUsername());
				protocolList.add(new AddedIgnoredEntry(username, null, "", true));
			}
			var update = new UpdateIgnoreList(protocolList);
			player.getPacketSender().write(update);
		}
	}

	public static void process() {
		for (var player : World.players()) {
			if (player == null) {
				continue;
			}
			var fl = FriendListDb.db().cached(player.getName());
			if (fl == null) {
				continue;
			}
			process(player, fl);
		}
	}

	private static void process(Player player, FriendList list) {
		if (list.updatePrivacy()) {
			player.getPacketSender().write(new ChatFilterSettingsPrivateChat(list.privacy().id));
		}

		if (list.updateFriendsStatus()) {
			var protocolList = new ArrayList<Friend>();
			for (var friend : list.friends()) {
				protocolList.add(statusUpdate(player, friend));
			}
			var update = new UpdateFriendList(protocolList);
			player.getPacketSender().write(update);
		}

		if (list.updateFriends()) {
			var protocolList = new ArrayList<Friend>();
			FriendList.Friend friend;
			while ((friend = list.addedFriends.poll()) != null) {
				protocolList.add(justAdded(player, friend));
			}
			var update = new UpdateFriendList(protocolList);
			player.getPacketSender().write(update);
		}

		if (list.updateIgnores()) {
			var protocolList = new ArrayList<IgnoredPlayer>();
			FriendList.IgnoreAction action;
			while ((action = list.ignoreActions.poll()) != null) {
				var ignore = action.ignore();
				var username = StringUtils.capitalizeName(ignore.getUsername());
				if (action.added()) {
					protocolList.add(new AddedIgnoredEntry(username, null, "", true));
				} else {
					protocolList.add(new RemovedIgnoredEntry(username));
				}
			}
			var update = new UpdateIgnoreList(protocolList);
			player.getPacketSender().write(update);
		}

		list.resetUpdates();
	}

	private static Friend statusUpdate(Player player, FriendList.Friend friend) {
		var online = isPlayerOnline(player, friend.getUsername());
		var username = StringUtils.capitalizeName(friend.getUsername());
		if (online) {
			return new OnlineFriend(false, username, null, World.id, friend.getRank().id, 0, "", "", 8, 0);
		} else {
			return new OfflineFriend(false, username, null, friend.getRank().id, 0, "");
		}
	}

	private static Friend justAdded(Player player, FriendList.Friend friend) {
		var online = isPlayerOnline(player, friend.getUsername());
		var username = StringUtils.capitalizeName(friend.getUsername());
		if (online) {
			return new OnlineFriend(true, username, null, World.id, friend.getRank().id, 0, "", "", 8, 0);
		} else {
			return new OfflineFriend(true, username, null, friend.getRank().id, 0, "");
		}
	}

	public static boolean isPlayerOnline(Player other, String player) {
		var p = World.getPlayer(player);
		if (p == null) {
			return false;
		}

		var fl = FriendListDb.db().cached(player);
		if (fl == null) {
			// friend list not loaded yet, assuming offline
			return false;
		}

		switch (fl.privacy()) {
			case FriendList.Privacy.Friends:
				return fl.containsFriend(other.getName());
			case FriendList.Privacy.Off:
				return false;
			case FriendList.Privacy.On:
				return true;
			default:
				return false;
		}
	}

	public static boolean isPlayerOnline(Player other, FriendList fl) {
		switch (fl.privacy()) {
			case FriendList.Privacy.Friends:
				return fl.containsFriend(other.getName());
			case FriendList.Privacy.Off:
				return false;
			case FriendList.Privacy.On:
				return true;
			default:
				return false;
		}
	}
}
