package io.ruin.network.incoming.handlers.command;

import io.ruin.model.World;
import io.ruin.api.utils.*;
import io.ruin.model.entity.player.*;
import io.ruin.services.*;

import static io.ruin.network.incoming.handlers.command.CommandHandler.*;

import io.ruin.Server;

public class CommandHandlerSupport {

	public static boolean handle(Player player, String query, String command, String[] args) {
		if (!player.isSupport() && !player.isModerator() && !player.isManager() && !player.isOwner() && !player.isAdmin()) {
			return false;
		}

		switch (command) {
			case "kick": {
				forPlayer(player, query, "::kick playerName", p2 -> {
					p2.unlock();
					p2.forceLogout();
				});
				return true;
			}

			case "sz":
			case "staffzone": {
				teleport(player, 3165, 3489, 2);
				return true;
			}

			case "checkpt": {
				forPlayer(player, query, "::checkpt playername", p2 -> {
					String Test = TimeUtils.fromMs(p2.playTime * Server.tickMs(), true);
					player.sendMessage(p2.getName() + "'s Play time is " + Test + ".");
				});
				return true;
			}

			case "jail": {
				forPlayerInt(player, query, "::jail playerName rockCount", (p2, ores) -> Punishment.jail(player, p2, ores));
				return true;
			}

			case "unjail": {
				forPlayer(player, query, "::unjail playerName", p2 -> Punishment.unjail(player, p2));
				return true;
			}

			case "jailinfo": {
				if (player.isJailed()) {
					player.sendMessage("You have mined " + player.jailOresCollected + "/" + player.jailOresAssigned + " rocks.");
					player.sendMessage("You were jailed by: " + player.jailerName);
				} else {
					player.sendMessage("You are not in jail.");
				}
				return true;
			}

			case "mute": {
				forPlayerTime(player, query, "::mute playerName #d/#h/perm",
						(p2, time) -> Punishment.mute(player, p2, time, false));
				return true;
			}

			case "unmute": {
				forPlayer(player, query, "::unmute playerName", p2 -> Punishment.unmute(player, p2));
				return true;
			}

			case "teletome": {
				String name = query.substring(command.length() + 1);
				Player p2 = World.getPlayer(name);
				if (p2 == null) {
					player.sendMessage("Could not find player: " + name);
				} else {
					if (player.wildernessLevel > 0) {
						player.sendMessage("You are unable to teleport this player to you as you are in the wilderness.");
						return true;
					}
					if (p2.wildernessLevel > 0) {
						player.sendMessage("You are unable to teleport this player to you as they are in the wilderness.");
						return true;
					}
					p2.getMovement().teleport(player.getPosition());
					player.sendMessage("You have teleported " + p2.getName() + " to you.");
				}
				return true;
			}
		}
		return false;

	}

}
