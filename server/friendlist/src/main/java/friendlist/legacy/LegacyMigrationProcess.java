package friendlist.legacy;

import friendlist.FriendList;
import friendlist.FriendListDb;
import friendlist.FriendRank;

public class LegacyMigrationProcess {

	public static void start() {
		LegacyFriendChatLoader.loadAll(fl -> {
			var previous = FriendListDb.db().loadBlocking(fl.username);
			if (previous.exists) {
				return;
			}

			var newFl = new FriendList(fl.username);
			newFl.exists = true;
			newFl.setPrivacy(into(fl.privacy));
			for (var oldFriend : fl.friends) {
				newFl.addFriend(oldFriend.name);
				newFl.setRank(oldFriend.name, into(oldFriend.rank));
			}
			for (var oldIgnore : fl.ignores) {
				if (oldIgnore == null) {
					continue;
				}
				newFl.addIgnore(oldIgnore.name);
			}

			FriendListDb.db().upsertBlocking(fl.username, newFl);
		});
	}

	private static FriendList.Privacy into(int privacy) {
		return switch (privacy) {
			case 0 -> {
				yield FriendList.Privacy.On;
			}
			case 1 -> {
				yield FriendList.Privacy.Friends;
			}
			case 2 -> {
				yield FriendList.Privacy.Off;
			}
			default -> {
				yield FriendList.Privacy.On;
			}
		};
	}

	private static FriendRank into(LegacyFriendChatRank rank) {
		return switch (rank) {
			case FRIEND -> {
				yield FriendRank.Friend;
			}

			case RECRUIT -> {
				yield FriendRank.Recruit;
			}

			case CORPORAL -> {
				yield FriendRank.Corporal;
			}

			case SERGEANT -> {
				yield FriendRank.Seregant;
			}

			case LIEUTENANT -> {
				yield FriendRank.Lieutenant;
			}

			case CAPTAIN -> {
				yield FriendRank.Captain;
			}

			case GENERAL -> {
				yield FriendRank.General;
			}

			case OWNER -> {
				yield FriendRank.Owner;
			}

			case ADMIN -> {
				yield FriendRank.Admin;
			}

			default -> {
				yield FriendRank.None;
			}
		};
	}
}
