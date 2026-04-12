package clanchat;

import com.google.gson.annotations.Expose;

import lombok.NoArgsConstructor;

// just a player attribute to hold joined chat name(s)
@NoArgsConstructor
public class FriendChatPlayer {
	@Expose
	public String joinedName = "";
}
