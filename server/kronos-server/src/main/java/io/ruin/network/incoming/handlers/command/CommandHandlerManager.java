package io.ruin.network.incoming.handlers.command;

import io.ruin.cache.*;
import io.ruin.db.PlayerDatabase;
import io.ruin.model.World;
import io.ruin.model.activities.DonationBossHandler;
import io.ruin.model.activities.bosses.globalboss.GlobalBossHandler;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.entity.player.*;
import io.ruin.model.inter.handlers.TeleInterface;
import io.ruin.model.map.Locations;
import io.ruin.model.map.Region;
import io.ruin.model.var.VarPlayerRepository;
import discord.webhooks.logs.AdministrationHook;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Utils;
import org.json.JSONObject;

import static io.ruin.network.incoming.handlers.command.CommandHandler.*;
import static io.ruin.model.entity.player.GameMode.*;

public class CommandHandlerManager {

	public static boolean handle(Player player, String query, String command, String[] args) {
		if (!player.isManager()) {
			return false;
		}

		switch (command) {

			case "spawnglobal": {
				GlobalBossHandler.spawnBoss();
				return true;
			}


			case "tp":
			case "tele":
			case "teleport": {

				if (args == null || args.length == 0) {
					TeleInterface teleInterface = new TeleInterface();
					teleInterface.open(player);
					return true;
				}
				int x, y, z;
				try {
					x = Integer.parseInt(args[0]);
					y = Integer.parseInt(args[1]);
					if (args.length > 2)
						z = Math.max(0, Math.min(3, Integer.parseInt(args[2])));
					else
						z = player.getPosition().getZ();
				} catch (Exception e) {
					int l = command.length() + 1;
					if (query.length() <= l)
						return true;
					String loc = query.substring(l).trim();
					Locations locations = Locations.find(loc);
					if (locations == null) {
						player.sendMessage("Invalid teleport location: " + loc);
						return true;
					}
					x = locations.x;
					y = locations.y;
					z = locations.z;
				}
				int regionId = Region.getId(x, y);
				if (regionId < 0 || regionId >= Region.LOADED.length) {
					player.sendMessage("Invalid teleport coordinates: " + x + ", " + y + ", " + z);
					return true;
				}
				player.getMovement().teleport(x, y, z);
				return true;
			}

			case "fillwell": {
				int amount = Integer.parseInt(args[0]);
				CamelStatueHandler.adminDonateToWell(player, amount);
				return true;
			}

			case "setvotestreak": {
				forPlayerInt(player, query, "::setvotestreak playerName amount", (p2, amount) -> p2.voteStreak = amount);
				return true;
			}

			case "adddonation": {
				int amount = Integer.parseInt(args[0]);
				DonationBossHandler.addDonationAmount(amount);
				var jsonObject = new JSONObject();
					jsonObject.put("user", player.getName());
				AdministrationHook.sendAdminDBLogsToDiscord(jsonObject);
				return true;
			}

			case "adddonationdealpoints": {
				forPlayerInt(player, query, "::adddonationdealpoints playerName amount", (p2, amount) -> {
					p2.donationDealPoints += amount;
				});
				return true;
			}

			case "adddonordeal": {
				forPlayerInt(player, query, "::adddonordeal playerName amount", (p2, amount) -> {
					if (amount > 500)
						amount = 500;
					p2.donationDealAmount += amount;

					var jsonObject = new JSONObject();
						jsonObject.put("player", player.getName());
						jsonObject.put("player_receiving", p2.getName());
						jsonObject.put("amount", Utils.formatMoneyString(amount));
					AdministrationHook.sendAdminDonorDealAmountLogsToDiscord(jsonObject);

				});
				return true;
			}

			case "checkdb": {
				player.sendMessage("The current donation total is: " + DonationBossHandler.currentDonationAmount + "");
				return true;
			}

			case "removegroupleavetimer": {
				forPlayer(player, query, "::removegroupleavetimer playerName", p2 -> {
					p2.groupLeaveInEpoch = 0;
					player.sendMessage("You have removed " + p2.getName() + " group leave timer.");
				});
				return true;
			}

			case "changepassword": {
				forNameString(player, query, "::changepassword username new_password", (username, newPassword) -> {
					var uuid = username.toLowerCase().trim();
					PlayerDatabase.db().mutateAsync(uuid, (pp) -> {
						if (pp == null) {
							player.sendMessage("Unable to find [" + uuid + "]");
							return;
						}
						if(pp.isStaff()) {
							player.sendMessage("You cannot change the password of a staff member.");
							return;
						}
						pp.password = newPassword;
						player.sendMessage(uuid + " password has been changed.");
					});
				});
				return true;
			}

			case "getgimgroup": {
				var username = query.substring(command.length() + 1);
				var uuid = username.toLowerCase().trim();
				PlayerDatabase.db().mutateAsync(uuid, (pp) -> {
					if (pp == null) {
						player.sendMessage("Unable to find [" + uuid + "]");
						return;
					}
					player.sendMessage("[" + pp.getName() + "] gim_group: " + pp.newGroupId);
				});
				return true;
			}

			case "setgimgroup": {
				forPlayerInt(player, query, "::setgimgroup playerName id", (p2, id) -> {
					p2.newGroupId = id;
					player.sendMessage("You have set " + p2.getName() + " their group to " + id);
				});
				return true;
			}
			case "givehcim": {
				forPlayer(player, query, "::givehcim playerName", p2 -> {
					VarPlayerRepository.IRONMAN_MODE.set(p2, 3);
					changeForumsGroup(p2, HARDCORE_IRONMAN.groupId);
					player.sendMessage("Gave hardcore ironman to " + p2.getName() + ".");
				});
				return true;
			}
			case "giveuim": {
				forPlayer(player, query, "::giveuim playerName", p2 -> {
					VarPlayerRepository.IRONMAN_MODE.set(p2, 2);
					changeForumsGroup(p2, ULTIMATE_IRONMAN.groupId);
					player.sendMessage("Gave ultimate ironman to " + p2.getName() + ".");
				});
				return true;
			}

			case "deironplayer": {
				forPlayer(player, query, "::deiron playerName", p2 -> {
					VarPlayerRepository.IRONMAN_MODE.set(p2, 0);
					VarPlayerRepository.CHAT_ICONS.set(p2, 0);
					GameMode.changeForumsGroup(player, GameMode.STANDARD.groupId);
					player.sendMessage("Removed ironman status from " + p2.getName() + ".");
				});
				return true;
			}

			case "giveironman": {
				forPlayer(player, query, "::giveironman playerName", p2 -> {
					VarPlayerRepository.IRONMAN_MODE.set(p2, 1);
					changeForumsGroup(p2, IRONMAN.groupId);
					player.sendMessage("Gave ironman to " + p2.getName() + ".");
				});
				return true;
			}

			case "givegroupiron": {
				forPlayer(player, query, "::givegroupiron playerName", p2 -> {
					VarPlayerRepository.IRONMAN_MODE.set(p2, 4);
					changeForumsGroup(p2, GROUP_IRONMAN.groupId);
					player.sendMessage("Gave group ironman to " + p2.getName() + ".");
				});
				return true;
			}

			case "givehardgroupiron": {
				forPlayer(player, query, "::givehardgroupiron playerName", p2 -> {
					VarPlayerRepository.IRONMAN_MODE.set(p2, 5);
					changeForumsGroup(p2, HARDCORE_GROUP_IRONMAN.groupId);
					player.sendMessage("Gave group ironman to " + p2.getName() + ".");
				});
				return true;
			}

			case "cancelperktask": {
				String name = query.substring(command.length() + 1);
				Player p2 = World.getPlayer(name);

				if (p2 == null)
					player.sendMessage("Could not find player: " + name);
				else {
					p2.currentPerkTask = null;
					p2.perkTaskCurrentAmount = 0;
					p2.sendMessage("Your perk task has been cancelled.");
				}
				return true;
			}

			case "givedonationdeal", "gdd", "givedd": {
				forPlayerInt(player, query, "::givedonationdeal playerName amount", (p2, amount) -> {
					if (amount > 275)
						amount = 275;
					Broadcast.WORLD.sendNewsDropMessage(p2, Icon.ADMINISTRATOR, "<col=000000>" + p2.getName(),
						" has just claimed a donation deal for an extra $%d! Pm an Admin to learn more about our deals!"
							.formatted(amount));
					giveBond(player, p2, amount);

					var jsonObject = new JSONObject();
						jsonObject.put("player", player.getName());
						jsonObject.put("player_receiving", p2.getName());
						jsonObject.put("amount", Utils.formatMoneyString(amount));
					AdministrationHook.sendAdminDonorDealAmountLogsToDiscord(jsonObject);
				});
				return true;
			}
		}
		return false;
	}

}
