package clanchat.hook;

import clanchat.Attributes;
import clanchat.FriendChats;
import io.ruin.HooksV2.Result;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.Player.Hook;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Attributes.class)
public class PlayerHook {

	public static void register() {
		Player.hooks.register(Hook.OnStart.class, PlayerHook::onStart);
		Player.hooks.register(Hook.OnFinish.class, PlayerHook::onFinish);
	}

	private static Result onStart(Hook.OnStart ctx) {
		FriendChats.joinOnLogin(ctx.player());
		return Result.Pass;
	}

	private static Result onFinish(Hook.OnFinish ctx) {
		FriendChats.leaveLogout(ctx.player());
		return Result.Pass;
	}
}
