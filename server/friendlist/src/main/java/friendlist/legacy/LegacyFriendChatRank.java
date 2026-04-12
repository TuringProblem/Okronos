package friendlist.legacy;

public enum LegacyFriendChatRank {
	FRIEND,
	RECRUIT,
	CORPORAL,
	SERGEANT,
	LIEUTENANT,
	CAPTAIN,
	GENERAL,
	OWNER,
	ADMIN(127);

	public final int id;

	private LegacyFriendChatRank() {
		this.id = this.ordinal();
	}

	private LegacyFriendChatRank(int id) {
		this.id = id;
	}
}
