package gemstonecrab;

import io.ruin.HooksV2;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.network.incoming.handlers.CommandHandler;

public class GemstoneCrabCommands implements CommandHandler.Hook {
	public static void register() {
		CommandHandler.hooks.register(CommandHandler.Hook.Handle.class, GemstoneCrabCommands::handle);
	}

	public static HooksV2.Result handle(CommandHandler.Hook.Handle ctx) {
		var command = ctx.command();
		var player = ctx.player();

		switch (command) {
			case "gemstonecrab", "gsc" -> {
				teleportPlayer(player, Position.of(1353, 3098, 0));
				return HooksV2.Result.Return;
			}
		}

		return HooksV2.Result.Pass;
	}

	public static void teleportPlayer(Player player, Position destination) {
		if (player.wildernessLevel > 20 || player.pvpAttackZone) {
			if (!(World.isDev() && player.isAdmin())) {
				player.sendMessage("You can't use this command from where you are standing.");
				return;
			}
		}
		if (player.getInventory().contains(25104)) {
			player.sendMessage("The crystal of memories stores your last location as an available teleport.");
			player.crystalMemoryPosition = player.getPosition().copy();
		}
		player.getMovement().startTeleport(event -> {
			event.setCancelCondition(() -> player.teleportListener != null && !player.teleportListener.allow(player));
			player.animate(3864);
			player.graphics(1039);
			player.privateSound(200, 0, 10);
			event.delay(2);
			player.getMovement().teleport(destination);
		});
	}
}
