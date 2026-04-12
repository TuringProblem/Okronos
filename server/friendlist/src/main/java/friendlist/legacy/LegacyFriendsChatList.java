package friendlist.legacy;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class LegacyFriendsChatList {
	@Expose
	public String username;
	@Expose
	public int privacy;
	@Expose
	public ArrayList<LegacyFriendChatMember> friends = new ArrayList<>();
	@Expose
	public ArrayList<LegacyFriendChatMember> ignores = new ArrayList<>();
}
