package clanchat.hook;

import clanchat.FriendChatMessaging;
import io.ruin.HooksV2.Result;
import io.ruin.network.incoming.handlers.ChatHandler;

public class ChatHook {

	public static void register() {
		ChatHandler.hooks.register(ChatHandler.Hook.OnMessage.class, ChatHook::onMessage);
	}

	public static Result onMessage(ChatHandler.Hook.OnMessage ctx) {
		if (ctx.msg().getType() != 2) {
			return Result.Pass;
		}
		var message = ctx.msg().getMessage().replaceFirst("/", "");
		FriendChatMessaging.sendMessage(ctx.player(), message);
		return Result.Pass;
	}
}
