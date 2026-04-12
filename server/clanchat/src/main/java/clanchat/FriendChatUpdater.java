package clanchat;

import java.util.ArrayList;
import java.util.List;

import friendlist.FriendList;
import friendlist.FriendListDb;
import friendlist.FriendRank;
import io.ruin.model.World;
import io.ruin.api.utils.StringUtils;
import io.ruin.model.entity.player.Player;
import lombok.experimental.ExtensionMethod;
import net.rsprot.protocol.game.outgoing.friendchat.UpdateFriendChatChannelFullV2;
import net.rsprot.protocol.game.outgoing.friendchat.UpdateFriendChatChannelSingleUser;
import net.rsprot.protocol.game.outgoing.friendchat.UpdateFriendChatChannelFull.FriendChatEntry;
import net.rsprot.protocol.game.outgoing.friendchat.UpdateFriendChatChannelFullV2.JoinUpdate;
import net.rsprot.protocol.game.outgoing.friendchat.UpdateFriendChatChannelFullV2.LeaveUpdate;
import net.rsprot.protocol.game.outgoing.friendchat.UpdateFriendChatChannelSingleUser.AddedFriendChatUser;
import net.rsprot.protocol.game.outgoing.friendchat.UpdateFriendChatChannelSingleUser.RemovedFriendChatUser;

import static core.task.api.API.*;

@ExtensionMethod(Attributes.class)
public class FriendChatUpdater {

	public static void sendInitJoin(Player player, String chatOwner) {
		queue(() -> {
			var fl = FriendChatDb.db().load(chatOwner).await();
			sendInitJoin(player, fl);
		});
	}

	public static void sendInitJoin(Player player, FriendChat chat) {
		queue(() -> {
			var fl = FriendListDb.db().load(chat.owner).await();
			sendInitJoin(player, fl, chat);
		});
	}

	public static void sendLeave(Player player) {
		var msg = new UpdateFriendChatChannelFullV2(LeaveUpdate.INSTANCE);
		player.getPacketSender().write(msg);
	}

	public static void notifyMemberJoined(Player player, FriendChat from) {
		queue(() -> {
			var ownerFl = FriendListDb.db().load(from.owner).await();
			for (var otherPlayer : World.players()) {
				if (otherPlayer == player) {
					continue;
				}
				var clan = otherPlayer.clan();
				if (!from.isOwner(clan.joinedName)) {
					continue;
				}
				var playerName = player.getName();
				var rank = rank(playerName, ownerFl, from);
				var username = StringUtils.capitalizeName(playerName);
				var msg = new UpdateFriendChatChannelSingleUser(new AddedFriendChatUser(username, World.id, rank.id, ""));
				otherPlayer.getPacketSender().write(msg);
			}
		});
	}

	public static void notifyMemberUpdated(String memberName, FriendChat from) {
		queue(() -> {
			var ownerFl = FriendListDb.db().load(from.owner).await();
			for (var otherPlayer : World.players()) {
				var clan = otherPlayer.clan();
				if (!from.isOwner(clan.joinedName)) {
					continue;
				}
				var rank = rank(memberName, ownerFl, from);
				var username = StringUtils.capitalizeName(memberName);
				var msg = new UpdateFriendChatChannelSingleUser(new AddedFriendChatUser(username, World.id, rank.id, ""));
				otherPlayer.getPacketSender().write(msg);
			}
		});
	}

	public static void notifyLeft(Player player, FriendChat from) {
		for (var otherPlayer : World.players()) {
			if (otherPlayer == player) {
				continue;
			}
			var clan = otherPlayer.clan();
			if (clan == null) {
				return;
			}
			if (!from.isOwner(clan.joinedName)) {
				continue;
			}
			var username = StringUtils.capitalizeName(player.getName());
			var msg = new UpdateFriendChatChannelSingleUser(new RemovedFriendChatUser(username, World.id));
			otherPlayer.getPacketSender().write(msg);
		}
	}

	public static void notifyLeft(String playerUsername, FriendChat from) {
		for (var otherPlayer : World.players()) {
			if (otherPlayer.getName().equalsIgnoreCase(playerUsername)) {
				return;
			}

			var clan = otherPlayer.clan();
			if (clan == null) {
				return;
			}

			if (!from.isOwner(clan.joinedName)) {
				continue;
			}

			var username = StringUtils.capitalizeName(playerUsername);
			var msg = new UpdateFriendChatChannelSingleUser(new RemovedFriendChatUser(username, World.id));
			otherPlayer.getPacketSender().write(msg);
		}
	}

	private static void sendInitJoin(Player player, FriendList ownerFriendList, FriendChat ownerChat) {
		var membersProto = new ArrayList<FriendChatEntry>();
		for (var member : onlineMembers(ownerChat)) {
			var rank = rank(member.getUsername(), ownerFriendList, ownerChat);
			var memberUsername = StringUtils.capitalizeName(member.getUsername());
			membersProto.add(new FriendChatEntry(memberUsername, World.id, rank.id, "Reason"));
		}

		var ownerUsername = StringUtils.capitalizeName(ownerChat.owner);
		var msg = new UpdateFriendChatChannelFullV2(
				new JoinUpdate(ownerUsername, ownerChat.channelName, ownerChat.permissionKick.id, membersProto));
		player.getPacketSender().write(msg);
	}

	private static FriendRank rank(String member, FriendList ownerFriendList, FriendChat ownerChat) {
		if (ownerChat.isOwner(member)) {
			return FriendRank.Owner;
		}
		var friend = ownerFriendList.friend(member);
		return friend == null ? FriendRank.None : friend.rank;
	}

	private static List<FriendChat.Member> onlineMembers(FriendChat chat) {
		return chat.members.stream().filter(it -> World.getPlayer(it.getUsername()) != null).toList();
	}
}
