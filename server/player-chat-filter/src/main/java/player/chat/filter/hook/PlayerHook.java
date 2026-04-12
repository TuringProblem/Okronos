package player.chat.filter.hook;

import io.ruin.HooksV2.Result;
import io.ruin.model.entity.player.Player;
import player.chat.filter.ChatFiltering;

public class PlayerHook {

	public static void register() {
		Player.hooks.register(Player.Hook.OnStart.class, PlayerHook::handle);
	}

	public static Result handle(Player.Hook.OnStart ctx) {
		var player = ctx.player();

		ChatFiltering.update(player);
		return Result.Pass;
	}
}
