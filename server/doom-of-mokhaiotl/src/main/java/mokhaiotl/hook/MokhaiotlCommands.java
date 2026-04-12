package mokhaiotl.hook;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.HooksV2;
import io.ruin.model.map.Position;
import io.ruin.network.incoming.handlers.CommandHandler;
import mokhaiotl.combat.attacks.impl.standard.MeleeAttack;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-28
 */
public class MokhaiotlCommands implements CommandHandler.Hook {

	public static void register() {
		CommandHandler.hooks.register(CommandHandler.Hook.Handle.class, MokhaiotlCommands::handle);
	}

	public static HooksV2.Result handle(CommandHandler.Hook.Handle ctx) {
		var command = ctx.command();
		var player = ctx.player();
		var args = ctx.args();

		if (World.isLive())
			return HooksV2.Result.Pass;

		if (!player.isAdmin())
			return HooksV2.Result.Pass;

		switch (command) {
			case "dom_test" -> {
				var attack = new MeleeAttack();
				attack.invoke(player, null);
				return HooksV2.Result.Return;
			}
			case "dom" -> {
				teleportPlayer(player, Position.of(1311, 9540, 0));
				return HooksV2.Result.Return;
			}
		}

		return HooksV2.Result.Pass;
	}

	private static void teleportPlayer(Player player, Position destination) {
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
