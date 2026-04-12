package collectionlog.hook;

import collectionlog.Attributes;
import io.ruin.HooksV2.Result;
import io.ruin.model.item.Item;
import io.ruin.network.incoming.handlers.CommandHandler;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Attributes.class)
public class CommandHook {

	public static void register() {
		CommandHandler.hooks.register(CommandHandler.Hook.Handle.class, CommandHook::handle);
	}

	private static Result handle(CommandHandler.Hook.Handle ctx) {
		var player = ctx.player();
		var command = ctx.command();
		var args = ctx.args();

		if (command.equalsIgnoreCase("clog")) {
			player.collectionLogUpdated().open(player);
			return Result.Return;
		}

		if (!player.isBetaTester()) {
			return Result.Pass;
		}

		if (command.equals("collectionlog")) {
			switch (args[0]) {
				case "add" -> {
					var id = Integer.parseInt(args[1]);
					var amount = Integer.parseInt(args[2]);
					player.addToCollectionLog(new Item(id, amount));
				}

				case "remove" -> {
					var id = Integer.parseInt(args[1]);
					player.collectionLog().remove(player, new Item(id));
				}
				case "clear" -> {
					player.collectionLog().clear(player);
				}
			}
			return Result.Return;
		}
		return Result.Pass;
	}

}
