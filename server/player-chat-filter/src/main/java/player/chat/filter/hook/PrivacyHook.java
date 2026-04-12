package player.chat.filter.hook;

import io.ruin.HooksV2.Result;
import io.ruin.network.incoming.handlers.PrivacyHandler;
import player.chat.filter.ChatFiltering;
import player.chat.filter.ChatFiltering.FilterPublic;

public class PrivacyHook implements PrivacyHandler.Hook {

	public static void register() {
		PrivacyHandler.hooks.register(PrivacyHandler.Hook.Accept.class, PrivacyHook::handle);
	}

	public static Result handle(PrivacyHandler.Hook.Accept ctx) {
		var plr = ctx.player();
		var msg = ctx.msg();

		ChatFiltering.setPublic(plr, FilterPublic.fromRaw(msg.getPublicChatFilter()));
		ChatFiltering.update(plr);
		return Result.Pass;
	}

}
