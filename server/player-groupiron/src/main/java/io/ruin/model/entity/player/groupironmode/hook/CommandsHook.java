package io.ruin.model.entity.player.groupironmode.hook;

import io.ruin.HooksV2.Result;
import io.ruin.model.entity.npc.actions.edgeville.DeironNPC;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.groupironmode.GroupIron;
import io.ruin.network.incoming.handlers.CommandHandler;
import lombok.experimental.ExtensionMethod;
import static io.ruin.network.incoming.handlers.command.CommandHandler.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@ExtensionMethod(Attributes.class)
public class CommandsHook {

	public static void register() {
		CommandHandler.hooks.register(CommandHandler.Hook.Handle.class, CommandsHook::handle);
	}

	private static Result handle(CommandHandler.Hook.Handle ctx) {
		var player = ctx.player();
		var command = ctx.command();
		var args = ctx.args();

		if (player.isLocked()) {
			player.sendMessage("Please finish what you're doing first.");
			return Result.Return;
		}

		return switch (command) {
			case "deiron" -> {
				if (player.getGameMode().isIronMan()) {
					var gameMode = player.getGameMode();
					var isGroup = gameMode.isAnyOf(GameMode.GROUP_IRONMAN, GameMode.HARDCORE_GROUP_IRONMAN);
					var pGroupIron = player.getGroupIron();
					var hasLeader = pGroupIron == null || pGroupIron.getLeader() != null;
					if (isGroup && hasLeader) {
						player.sendMessage("You cannot deiron while in a group.");
						yield Result.Return;
					}
					DeironNPC.deiron(player);
				} else {
					player.sendMessage("You need to be an ironman to deiron.");
				}
				yield Result.Return;
			}

			case "groupmembers" -> {
				for (String name : player.getGroupIron().getMembers()) {
					player.sendMessage(name);
				}
				yield Result.Return;
			}

			case "addgrouplife" -> {
				if (player.isManager()) {
					yield Result.Return;
				}

				var query = Arrays.stream(args).collect(Collectors.joining());
				forPlayer(player, query, "::addgrouplife playerName", p2 -> {
					if (p2.getGroupIron() == null) {
						player.sendMessage("This player is not in a group.");
						return;
					}
					p2.getGroupIron().updateGroupLives(1);
					player.sendMessage("You've added a group life to the group " + p2.getGroupIron().getGroupName() + ".");
				});
				yield Result.Return;
			}

			case "creategroup" -> {
				GroupIron.chooseGroupName(player);
				yield Result.Return;
			}

			default -> {
				yield Result.Pass;
			}
		};
	}
}
