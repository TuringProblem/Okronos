package tradepost.hook;

import io.ruin.HooksV2.Result;
import io.ruin.HooksV2.Hook;
import io.ruin.network.incoming.handlers.CommandHandler;
import tradepost.module.Module;

public class Commands implements Hook<CommandHandler.Hook.Handle> {

	public static void register() {
		CommandHandler.hooks.register(CommandHandler.Hook.Handle.class, new Commands());
	}

	@Override
	public Result handle(CommandHandler.Hook.Handle ctx) {
		var player = ctx.player();
		var command = ctx.command();

		if (!player.isAdmin()) {
			return Result.Pass;
		}

		switch (command) {
			case "tp_disable" -> {
				Module.ENABLED = false;
				return Result.Return;
			}

			case "tp_enable" -> {
				Module.ENABLED = true;
				return Result.Return;
			}
		}

		return Result.Pass;
	}
}
