package friendlist;

import java.util.Arrays;

public enum FriendRank {
	Friend,
	Recruit,
	Corporal,
	Seregant,
	Lieutenant,
	Captain,
	General,
	Owner,
	Admin(127),
	None(-1);

	public final int id;

	private FriendRank() {
		this.id = this.ordinal();
	}

	private FriendRank(int id) {
		this.id = id;
	}

	public static FriendRank byId(int id) {
		return Arrays.stream(values()).filter(it -> it.id == id).findFirst().orElse(FriendRank.None);
	}

	public boolean anyOf(FriendRank... ranks) {
		for (var rank : ranks) {
			if (rank == this) {
				return true;
			}
		}
		return false;
	}
}
