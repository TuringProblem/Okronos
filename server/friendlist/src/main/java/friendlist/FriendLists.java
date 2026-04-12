package friendlist;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;

import static core.task.api.API.*;

// NOTE: all methods here are executed on main thread
public class FriendLists {
	public static void initialize(Player player) {
		queue(() -> {
			var it = FriendListDb.db().load(player.uuid()).await();
			it.exists = true;
			FriendListUpdater.notifyFriendListLoaded(player, it);
			markOthersStatusUpdate(it);
		});
	}

	public static void setRank(Player player, String friendUsername, FriendRank rank) {
		queue(() -> {
			var it = FriendListDb.db().load(player.uuid()).await();
			it.setRank(friendUsername, rank);
			markOthersStatusUpdate(it);
		});
	}

	public static void setPrivacy(Player player, FriendList.Privacy privacy) {
		queue(() -> {
			var it = FriendListDb.db().load(player.uuid()).await();
			it.setPrivacy(privacy);
			markOthersStatusUpdate(it);
		});
	}

	// queue update of other friend lists that may contain this player
	public static void markOthersStatusUpdate(Player player) {
		queue(() -> {
			var it = FriendListDb.db().load(player.uuid()).await();
			markOthersStatusUpdate(it);
		});
	}

	// happens when players change privacy/becomes online
	private static void markOthersStatusUpdate(FriendList list) {
		queue(() -> {
			for (var player : World.players()) {
				fork(() -> {
					var fl = FriendListDb.db().load(player.uuid()).await();
					if (!fl.containsFriend(list.owner)) {
						return;
					}
					fl.markUpdateFriendsStatus();
				});
			}
			await_children();
		});

	}

	public static void addFriend(Player player, String friendUsername) {
		queue(() -> {
			var fl = FriendListDb.db().load(player.uuid()).await();
			var fl2 = FriendListDb.db().load(friendUsername.toLowerCase().trim()).await();
			if (!fl2.exists) {
				player.sendMessage("Player " + friendUsername + " does not exist.");
				return;
			}
			fl.addFriend(friendUsername);
			updateFriendStatus(friendUsername);
		});
	}

	public static void removeFriend(Player player, String friendUsername) {
		queue(() -> {
			var fl = FriendListDb.db().load(player.uuid()).await();
			fl.removeFriend(friendUsername);
			updateFriendStatus(friendUsername);
		});
	}

	public static void addIgnore(Player player, String ignoreUsername) {
		queue(() -> {
			var fl = FriendListDb.db().load(player.uuid()).await();
			fl.addIgnore(ignoreUsername);
			updateFriendStatus(ignoreUsername);
		});
	}

	public static void removeIgnore(Player player, String ignoreUsername) {
		queue(() -> {
			var fl = FriendListDb.db().load(player.uuid()).await();
			fl.removeIgnore(ignoreUsername);
			updateFriendStatus(ignoreUsername);
		});
	}

	private static void updateFriendStatus(String username) {
		if (World.getPlayer(username) == null) {
			return;
		}
		var fl = FriendListDb.db().load(username.toLowerCase().trim()).await();
		fl.markUpdateFriendsStatus();
	}

}
