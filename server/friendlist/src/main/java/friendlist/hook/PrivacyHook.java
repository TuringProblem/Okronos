package friendlist.hook;

import io.ruin.HooksV2;
import io.ruin.network.incoming.handlers.PrivacyHandler;
import io.ruin.network.incoming.handlers.PrivacyHandler.Hook;
import friendlist.*;

public class PrivacyHook implements Hook {
	public static void register() {
		PrivacyHandler.hooks.register(PrivacyHandler.Hook.Accept.class, PrivacyHook::handle);
	}

	private static HooksV2.Result handle(PrivacyHandler.Hook.Accept ctx) {
		var player = ctx.player();
		var msg = ctx.msg();

		FriendLists.setPrivacy(player, FriendList.Privacy.byId(msg.getPrivateChatFilter()));
		return HooksV2.Result.Pass;
	}
}
