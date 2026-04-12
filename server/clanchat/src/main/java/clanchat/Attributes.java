package clanchat;

import static player.attributes.api.API.*;

import io.ruin.model.entity.player.Player;

public class Attributes {

	public static void register() {
		attrib().register().persistent(FriendChatPlayer.class, FriendChatPlayer::new);
	}

	public static FriendChatPlayer clan(Player player) {
		return attrib(FriendChatPlayer.class, player);
	}
}
