package clanchat;

import friendlist.FriendRank;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ClanPermission {
	//@formatter:off
	Friend("Any Friend", 0),
	Recruit("Recruit+", 1),
	Corporal("Corporal+", 2),
	Seregant("Seregant+", 3),
	Lieutenant("Lieutenant+", 4),
	Captain("Captain+", 5),
	General("General+", 6),
	Owner("Only me", 7),
	Anyone("Anyone", -1);
	//@formatter:on

	public final String text;
	public final int id;

	public boolean allow(FriendRank rank) {
		return rank.id >= this.id;
	}
}
