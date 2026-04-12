package friendlist;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.google.gson.annotations.Expose;

import io.ruin.HooksV2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FriendList {
	public static interface Hook {
		record OnFriendAdded(FriendList list, FriendList.Friend friend) implements Hook {
		}

		record OnFriendRemoved(FriendList list, FriendList.Friend friend) implements Hook {
		};

		record OnFriendRankChanged(FriendList list, FriendList.Friend friend, FriendRank newRank) implements Hook {
		}
	}

	public static HooksV2<Hook> hooks = new HooksV2<>(Hook.class);

	private static int FLAG_UPDATE_PRIVACY = 0x1;
	private static int FLAG_UPDATE_FRIENDS = 0x2;
	private static int FLAG_UPDATE_IGNORES = 0x4;
	private static int FLAG_UPDATE_FRIENDS_STATUS = 0x8;

	@AllArgsConstructor
	public static enum Privacy {
		On(0),
		Friends(1),
		Off(2);

		public final int id;

		public static Privacy byId(int id) {
			return switch (id) {
				case 0 -> {
					yield On;
				}

				case 1 -> {
					yield Friends;
				}

				case 2 -> {
					yield Off;
				}

				default -> {
					yield On;
				}
			};
		}
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class Friend {
		@Expose
		public String username;
		@Expose
		public FriendRank rank;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class Ignore {
		@Expose
		public String username;
	}

	@Expose
	public String owner;

	@Expose
	private Privacy privacy = Privacy.On;

	@Expose
	private final Map<String, Friend> friends = new HashMap<>();

	@Expose
	private final Map<String, Ignore> ignores = new HashMap<>();

	@Expose
	public boolean exists = false;

	public transient Queue<Friend> addedFriends = new ArrayDeque<>();
	public transient Queue<IgnoreAction> ignoreActions = new ArrayDeque<>();

	public FriendList(String owner) {
		this.owner = owner;
	}

	public static record IgnoreAction(boolean added, Ignore ignore) {
	}

	private transient int updateMask = 0x0;

	public void resetUpdates() {
		this.updateMask = 0;
		this.addedFriends.clear();
		this.ignoreActions.clear();
	}

	public void markUpdatePrivacy() {
		this.updateMask |= FLAG_UPDATE_PRIVACY;
	}

	public void markUpdateFriends() {
		this.updateMask |= FLAG_UPDATE_FRIENDS;
	}

	public void markUpdateFriendsStatus() {
		this.updateMask |= FLAG_UPDATE_FRIENDS_STATUS;
	}

	public void markUpdateIgnores() {
		this.updateMask |= FLAG_UPDATE_IGNORES;
	}

	public boolean updatePrivacy() {
		return (this.updateMask & FLAG_UPDATE_PRIVACY) != 0;
	}

	public boolean updateFriends() {
		return (this.updateMask & FLAG_UPDATE_FRIENDS) != 0;
	}

	public boolean updateFriendsStatus() {
		return (this.updateMask & FLAG_UPDATE_FRIENDS_STATUS) != 0;
	}

	public boolean updateIgnores() {
		return (this.updateMask & FLAG_UPDATE_IGNORES) != 0;
	}

	public Friend friend(String _username) {
		var username = _username.toLowerCase().trim();
		return this.friends.get(username);
	}

	public Iterable<Friend> friends() {
		return this.friends.values();
	}

	public Iterable<Ignore> ignores() {
		return this.ignores.values();
	}

	public Privacy privacy() {
		return this.privacy;
	}

	public void setPrivacy(Privacy privacy) {
		if (this.privacy != privacy) {
			this.privacy = privacy;
			this.markUpdatePrivacy();
		}
	}

	public void setRank(String _username, FriendRank rank) {
		var username = _username.toLowerCase().trim();
		var friend = friends.get(username);
		if (friend == null) {
			return;
		}
		if (friend.rank != rank) {
			friend.rank = rank;
			hooks.handle(new Hook.OnFriendRankChanged(this, friend, rank));
		}
	}

	public boolean containsFriend(String _username) {
		if (_username == null) {
			return false;
		}
		var username = _username.toLowerCase().trim();
		return this.friends.containsKey(username);
	}

	public void addFriend(String _username) {
		var username = _username.toLowerCase().trim();
		var friend = new Friend(username, FriendRank.Friend);
		this.friends.put(username, friend);
		this.addedFriends.add(friend);
		this.markUpdateFriends();
		hooks.handle(new Hook.OnFriendAdded(this, friend));
	}

	public void removeFriend(String _username) {
		var username = _username.toLowerCase().trim();
		var friend = this.friends.remove(username);
		if (friend != null) {
			this.markUpdateFriends();
			hooks.handle(new Hook.OnFriendRemoved(this, friend));
		}
	}

	public void removeIgnore(String _username) {
		var username = _username.toLowerCase().trim();
		var ignore = this.ignores.remove(username);
		if (ignore != null) {
			this.markUpdateIgnores();
			this.ignoreActions.add(new IgnoreAction(false, ignore));
		}
	}

	public void addIgnore(String _username) {
		var username = _username.toLowerCase().trim();
		this.removeFriend(username);
		var ignore = new Ignore(username);
		this.ignores.put(username, ignore);
		this.ignoreActions.add(new IgnoreAction(true, ignore));
		this.markUpdateIgnores();
	}
}
