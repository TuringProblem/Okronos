package friendlist.hook;

import friendlist.FriendLists;
import io.ruin.HooksV2.Result;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.Player.Hook;

public class PlayerHook implements Hook {
	public static void register() {
		Player.hooks.register(Player.Hook.OnStart.class, PlayerHook::handle);
		Player.hooks.register(Player.Hook.OnFinish.class, PlayerHook::handle);
	}

	private static Result handle(Player.Hook.OnStart ctx) {
		var player = ctx.player();

		FriendLists.initialize(player);
		return Result.Pass;
	}

	private static Result handle(Player.Hook.OnFinish ctx) {
		var player = ctx.player();

		FriendLists.markOthersStatusUpdate(player);
		return Result.Pass;
	}
}
