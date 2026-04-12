package com.reasonps.dominion.hook;

import com.reasonps.dominion.DominionOfEchoes;
import com.reasonps.dominion.loot.RewardGenerator;
import com.reasonps.dominion.rooms.*;
import io.ruin.HooksV2;
import io.ruin.HooksV2.Result;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.network.incoming.handlers.CommandHandler;
import io.ruin.utility.Broadcast;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class Commands implements CommandHandler.Hook {

	public static void register() {
		CommandHandler.hooks.register(CommandHandler.Hook.Handle.class, Commands::handle);
	}

	private static HooksV2.Result handle(CommandHandler.Hook.Handle ctx) {
		var command = ctx.command();
		var player = ctx.player();
		var args = ctx.args();

		if (command.equals("doe")) {
			teleport(player);
			return Result.Return;
		}

		if (!player.isAdmin())
			return Result.Pass;

		switch (command) {
			case "simulate_doe", "sdoe" -> {
				var sessionsToSimulate = Integer.parseInt(args[0]);
				for (int i = 0; i < sessionsToSimulate; i++) {
					var rewards = new RewardGenerator(player)
						.generateLoot(3 + Random.get(-1, 1), null);
					var builder = new StringBuilder();
					for (var reward : rewards) {
						builder.append("%d x %s<br>".formatted(reward.getAmount(), reward.lootBroadcast != null ?
						Color.RAID_PURPLE.wrap(reward.getDef().getName()) :
						reward.getDef().getName()));

						if (reward.lootBroadcast != null)
							Broadcast.WORLD.sendNewsDropMessage(
							player,
							Icon.ADMINISTRATOR,
							"<col=000000>".concat(player.getName()),
							" has just received <shad>%s</shad> from Dominion of Echoes! (Simulation)"
								.formatted(reward.getDef().name));
					}
					player.sendFilteredMessage("Simulated %d DoE sessions. Rewards:<br>%s".formatted(i + 1, builder.toString()));
				}
				return Result.Return;
			}
			case "echo_kalphite", "ekq" -> {
				var area = new EchoKalphiteHive(player);
					area.movePlayerToInstance(player);
				return Result.Return;
			}

			case "echo_puppy", "ec" -> {
				var area = new EchoCerberusMap(player);
					area.movePlayerToInstance(player);
				return Result.Return;
			}

			case "echo_kbd", "ekbd" -> {
				var area = new EchoKingBlackDragonDungeon(player);
					area.movePlayerToInstance(player);
				return Result.Return;
			}

			case "echo_hunllef", "ehun" -> {
				var area = new EchoHunllefMap(player);
					area.movePlayerToInstance(player);
				return Result.Return;
			}

			case "echo_dagannoths", "edks" -> {
				var area = new EchoDagannothCave(player);
					area.movePlayerToInstance(player);
				return Result.Return;
			}

			case "echo_heredit", "esh" -> {
				var area = new EchoSolHereditColiseum(player);
					area.movePlayerToInstance(player);
				return Result.Return;
			}

			case "toggledoe" -> {
				DominionOfEchoes.setDoeActive(!DominionOfEchoes.isDoeActive());
				player.sendMessage("The Dominion of Echoes is now %s".formatted(DominionOfEchoes.isDoeActive() ? "active" : "inactive"));
			}
		}
		return Result.Pass;
	}

	private static void teleport(Player player) {
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
			player.getMovement().teleport(1781, 3105, 0);
			player.sendMessage("Proceed up the stairs to enter the Dominion of Echoes.");
		});
	}
}
