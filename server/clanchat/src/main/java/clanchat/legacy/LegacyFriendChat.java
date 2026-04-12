package clanchat.legacy;

import com.google.gson.annotations.Expose;
import friendlist.legacy.*;

public class LegacyFriendChat {
	@Expose
	private String lastName = "";
	@Expose
	public String name;
	@Expose
	public LegacyFriendChatRank enterRank = null;
	@Expose
	public LegacyFriendChatRank kickRank = LegacyFriendChatRank.CORPORAL;
	@Expose
	public int ccMembersCount;
	@Expose
	public LegacyFriendChatMember[] ccMembers = new LegacyFriendChatMember[100];
}
