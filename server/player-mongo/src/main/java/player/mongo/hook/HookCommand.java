package player.mongo.hook;

import io.ruin.HooksV2.Result;
import io.ruin.network.incoming.handlers.CommandHandler;
import player.mongo.SaveScheduler;

public class HookCommand {

	public static void register() {
		CommandHandler.hooks.register(CommandHandler.Hook.Handle.class, ctx -> {
			var player = ctx.player();
			if (!player.isAdmin()) {
				return Result.Pass;
			}

			switch (ctx.command()) {
				case "mongo_stats" -> {
					player.sendMessage("pending_saves: " + SaveScheduler.pendingSaves());
					return Result.Return;
				}
				default -> {
					return Result.Pass;
				}
			}
		});
	}
}
