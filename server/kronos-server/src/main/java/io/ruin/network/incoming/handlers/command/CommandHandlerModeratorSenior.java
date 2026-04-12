package io.ruin.network.incoming.handlers.command;

import io.ruin.cache.*;
import io.ruin.db.PlayerDatabase;
import io.ruin.model.World;
import io.ruin.model.entity.player.*;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.services.*;
import io.ruin.utility.Broadcast;

import static io.ruin.network.incoming.handlers.command.CommandHandler.*;

public class CommandHandlerModeratorSenior {

	public static boolean handle(Player player, String query, String command, String[] args) {
		if (!player.isHeadModerator() && !player.isManager() && !player.isAdmin() && !player.isOwner()) {
			return false;
		}

		switch (command) {

			case "getsecuritypin": {
				forPlayer(player, query, "::getsecuritypin playerName", p2 -> {
					if (p2 != null) {
						player.sendMessage("Their pin is: " + p2.getSecurityPin());
					}
				});
				return true;
			}

			case "resetgrouptimer": {
				String name = query.substring(command.length() + 1);
				Player p2 = World.getPlayer(name);
				if (p2 == null) {
					player.sendMessage("That player is not online.");
					return true;
				}
				p2.groupLeaveInEpoch = 0;
				player.sendMessage("Their group wait time has been reset. They can now join another group.");
				p2.sendMessage("Your group wait time has been reset. You are free to join another group.");
				return true;
			}

			case "broadcast":
			case "bc": {
				String message = String.join(" ", args);
				World.players().forEach(p -> p.getPacketSender().sendBroadcast(message));
				return true;
			}

			case "hide": {
				if (player.isHidden()) {
					player.setHidden(false);
					player.sendMessage("You are now visible.");
				} else {
					player.setHidden(true);
					player.sendMessage("You are now hidden.");
				}
				return true;
			}

			case "resettask": {
				forPlayer(player, query, "::resettask playerName", p2 -> {
					VarPlayerRepository.SLAYER_TASK_AMOUNT.set(p2, 0);
					VarPlayerRepository.SLAYER_TASK.set(p2, 0);
					VarPlayerRepository.SLAYER_MASTER.set(p2, 0);
					player.sendMessage("You've reset " + p2.getName() + "'s slayer task...");
				});
				return true;
			}

			case "doublexp":
			case "doublexpweekend": {
				World.doubleExpActive = !World.doubleExpActive; // Toggle between true and false
				if (World.doubleExpActive) {
					player.sendMessage("Double XP Weekend is now active.");
					Broadcast.WORLD.sendNews(Icon.MYSTERY_BOX,
							"  <col=000000>[Broadcast]</col> The <col=000000>Double XP Weekend Boost</col> has just been activated!");
				} else {
					player.sendMessage("Double XP Weekend is now inactive.");
				}
				return true;
			}

			case "doublepkp": {
				World.toggleDoublePkp();
				return true;
			}

			case "doubleslay":
			case "doubleslayerpoints":
			case "doubleslayer": {
				String message = World.toggleDoubleSlayer();
				if (message != null && !message.isEmpty()) {
					Broadcast.WORLD.sendNews(Icon.MYSTERY_BOX,
							"<col=000000>  [Broadcast]</col> The <col=000000>Double Slayer Point Boost</col> has just been activated!");
				} else {
					player.sendMessage("The Double Slayer Point Boost has been deactivated.");
				}
				return true;
			}

			case "doublepc": {
				World.toggleDoublePest();
				return true;
			}

			case "doublewinter": {
				World.toggleDoubleWintertodt();
				return true;
			}
		}
		return false;

	}

}
