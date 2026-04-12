package friendlist.legacy;

import com.google.gson.annotations.Expose;

public class LegacyFriendChatMember {
	@Expose
	public String name;
	@Expose
	public String lastName;
	@Expose
	public LegacyFriendChatRank rank;
}
