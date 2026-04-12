package io.ruin.network.incoming.handlers.command;

import io.ruin.cache.*;
import io.ruin.db.PlayerDatabase;
import io.ruin.model.World;
import io.ruin.model.entity.player.*;
import io.ruin.model.entity.player.offline_player.PlayerProfileService;
import io.ruin.network.HWIDManager;
import io.ruin.services.*;

import java.io.IOException;

import static io.ruin.network.incoming.handlers.command.CommandHandler.*;

public class CommandHandlerModerator {

	public static boolean handle(Player player, String query, String command, String[] args) {
		if (!player.isModerator() && !player.isManager() && !player.isOwner() && !player.isAdmin())
			return false;
		switch (command) {
			case "unlock": {
				forPlayer(player, query, "::unlock playerName", p2 -> {
					if (!p2.isLocked()) {
						player.sendMessage(p2.getName() + " is not locked.");
					} else {
						p2.unlock();
						player.sendMessage("Unlocked " + p2.getName() + ".");
					}
				});
				return true;
			}
			case "movehome": {
				String name = query.substring(command.length() + 1);
				Player p2 = World.getPlayer(name);
				if (p2 == null)
					player.sendMessage("Could not find player: " + name);
				else if (p2.isOwner() && !player.isAdmin()) {
					player.sendMessage("You can't teleport an administrator.");
					p2.sendMessage(player.getName() + " has just attempted to teleport you home.");
					return true;
				} else if (p2.wildernessLevel > 0) {
					player.sendMessage("You can't teleport a player who's inside the wilderness.");
					return true;
				} else if (p2.joinedTournament) {
					player.sendMessage("You can't teleport a player who's in a tournament.");
					return true;
				} else
					p2.getMovement().teleport(World.HOME);
				return true;
			}

			case "checkhwid": {
				String playerName = query.substring(command.length() + 1);
				Player p = World.getPlayer(playerName);
				if (p != null) {
					player.sendMessage(Color.GREEN, "Player \"" + playerName + "\" HWID was \"" + p.hwid + "\"");
				} else {
					player.sendMessage(Color.RED, "Player \"" + playerName + "\" was not online");
				}
				return true;
			}

			case "ban": {
				var username = query.substring(command.length() + 1);
				Player p = World.getPlayer(username);
				if (p != null) {
					Punishment.ban(player, p);
				} else {
					var uuid = username.toLowerCase().trim();
					PlayerDatabase.db().mutateAsync(uuid, (pp) -> {
						if (pp == null) {
							player.sendMessage("Unable to find [" + uuid + "]");
							return;
						}
						pp.setPermBanned(true, "(0)");
					});
				}
				return true;
			}

			case "hwidban": {
				String playerName = query.substring(command.length() + 1);
				Player p = World.getPlayer(playerName);
				if (p != null) {
					if (HWIDManager.isHwidValid(p.hwid)) {
						HWIDManager.ban(p.hwid);
						player.sendMessage(Color.ORANGE_RED, "Successfully banned HWID: " + p.hwid);
					} else {
						player.sendMessage("Player's hwid not valid: " + p.hwid);
					}
					Punishment.ban(player, p);
				} else {
					player.sendMessage(Color.RED, "Player \"" + playerName + "\" was not online... fetching offline profile...");
					var service = new PlayerProfileService();
					try {
						var hwid = service.getPlayerHwid(playerName);
						if (hwid.contains("unknown")) {
							player.sendMessage("Unable to retrieve HWID for player: " + playerName);
							return true;
						}
						if (HWIDManager.isHwidValid(hwid)) {
							HWIDManager.ban(hwid);
							player.sendMessage(Color.ORANGE_RED, "Successfully banned HWID: " + hwid);
						} else
							player.sendMessage("Player's hwid not valid: " + hwid);
					} catch (IOException e) {
						player.sendMessage("Unable to retrieve HWID for player: " + playerName);
						throw new RuntimeException(e);
					}
				}
				return true;
			}

			case "hwid_unban_raw": {
				var hwid = query.substring(command.length() + 1);
				if (!HWIDManager.isHwidValid(hwid)) {
					player.sendMessage(Color.RED, "Invalid HWID format: " + hwid);
					return true;
				}

				if (!HWIDManager.isBanned(hwid)) {
					player.sendMessage(Color.RED, "This HWID is not banned: " + hwid);
					return true;
				}

				HWIDManager.unban(hwid);
				player.sendMessage(Color.GREEN, "Successfully unbanned HWID: " + hwid);
				return true;
			}

			case "hwid_ban_raw": {
				var hwid = query.substring(command.length() + 1);
				if (!HWIDManager.isHwidValid(hwid)) {
					player.sendMessage(Color.RED, "Invalid HWID format: " + hwid);
					return true;
				}

				if (HWIDManager.isBanned(hwid)) {
					player.sendMessage(Color.RED, "This HWID is already banned: " + hwid);
					return true;
				}

				HWIDManager.ban(hwid);
				player.sendMessage(Color.GREEN, "Successfully banned HWID: " + hwid);
				return true;
			}

			case "hwid_unban_all": {
				var uuid = query.substring(command.length() + 1).toLowerCase();
				PlayerDatabase.db().getAsync(uuid, (pp) -> {
					if (pp == null) {
						player.sendMessage(Color.RED, "Unable to find player: " + uuid);
						return;
					}

					var hwids = pp.hwids;
					for (var hwid : hwids) {
						if (!HWIDManager.isHwidValid(hwid)) {
							player.sendMessage(Color.RED, "Invalid HWID format: " + hwid);
							return;
						}

						if (!HWIDManager.isBanned(hwid)) {
							player.sendMessage(Color.RED, "This HWID is not banned: " + hwid);
							return;
						}

						HWIDManager.unban(hwid);
						player.sendMessage(Color.GREEN, "Successfully unbanned HWID: " + hwid);
					}
				});
				return true;
			}

			case "hwid_unban": {
				var uuid = query.substring(command.length() + 1).toLowerCase();
				PlayerDatabase.db().getAsync(uuid, (pp) -> {
					if (pp == null) {
						player.sendMessage(Color.RED, "Unable to find player: " + uuid);
						return;
					}

					String hwid = pp.hwid;
					if (hwid == null || hwid.isEmpty()) {
						player.sendMessage(Color.RED, "No valid HWID found for: " + uuid);
						return;
					}

					if (!HWIDManager.isHwidValid(hwid)) {
						player.sendMessage(Color.RED, "Invalid HWID format: " + hwid);
						return;
					}

					if (!HWIDManager.isBanned(hwid)) {
						player.sendMessage(Color.RED, "This HWID is not banned: " + hwid);
						return;
					}

					HWIDManager.unban(hwid);

					player.sendMessage(Color.GREEN, "Successfully unbanned " + hwid + " for: " + uuid);
				});
				return true;
			}

			case "unban": {
				var username = query.substring(command.length() + 1);
				var uuid = username.toLowerCase().trim();
				PlayerDatabase.db().mutateAsync(uuid, (pp) -> {
					if (pp == null) {
						player.sendMessage("Unable to find [" + uuid + "]");
						return;
					}
					pp.setPermBanned(false);
					SecurityPin.resetSecurity(pp);
				});
				return true;
			}

			case "teleto": {
				String name = query.substring(command.length() + 1);
				Player p2 = World.getPlayer(name);
				if (p2 == null) {
					player.sendMessage("Could not find player: " + name);
				} else {
					player.getMovement().teleport(p2.getPosition());
					if (player.wildernessLevel > 0) {
						if (player.isHidden()) {
							player.setHidden(false);
							player.sendMessage("You have entered the wilderness and are now visible.");
						}
					}
					player.sendMessage("You have teleported to " + p2.getName());
				}
				return true;
			}

		}
		return false;

	}
}
