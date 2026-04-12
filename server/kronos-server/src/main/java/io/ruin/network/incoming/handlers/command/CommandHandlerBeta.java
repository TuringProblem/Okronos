package io.ruin.network.incoming.handlers.command;

import io.ruin.model.World;
import io.ruin.model.activities.wintertodt.Wintertodt;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerGroup;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.shop.Currency;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.api.utils.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.ruin.network.incoming.handlers.command.CommandHandler.*;

public class CommandHandlerBeta {

	public static boolean handle(Player player, String query, String command, String[] args) {
		if (!World.isDev()) {
			return false;
		}

		if (!player.isBetaTester()) {
			return false;
		}

		return switch (command) {

			case "wt_add" -> {
				var amount = Integer.parseInt(args[0]);
				Wintertodt.addPoints(player, amount);
				Currency.WINTERTODT_POINTS.getCurrencyHandler().addCurrency(player, amount);
				yield true;
			}

			case "testranks" -> {
				player.startEvent(e -> {
					for (var group : PlayerGroup.values()) {
						player.getChatUpdate().set(false, 0, group.clientRank(), -1, 0, "group: " + group);
						e.delay(1);
					}
				});
				yield true;
			}

			case "max_mage" -> {
				var items = List.of(33000, 33001, 33002, 33003, 33021, 30376, 30509, 30520, 30378, 30380, 30591);
				for (var id : items) {
					player.getInventory().add(id);
				}
				yield true;
			}
			case "max_melee" -> {
				var items = List.of(30376, 30509, 30520, 30378, 30511, 30514, 30388, 30510, 21285, 30380, 30591);
				for (var id : items) {
					player.getInventory().add(id);
				}
				yield true;
			}
			case "max_range" -> {
				var items = List.of(27235, 27238, 27241, 30374, 28902, 30376, 30509, 30520, 30378, 30380, 30591);
				for (var id : items) {
					player.getInventory().add(id);
				}
				yield true;
			}
			case "reason_points" -> {
				player.reasonPoints += 1_000_000;
				player.sendMessage("You now have " + NumberUtils.formatNumber(player.reasonPoints) + " reason points.");
				yield true;
			}

			case "perk" -> {
				if (args.length == 0) {
					player.sendMessage("::perk points [amount] - increases perk point by amount.");
					player.sendMessage("::perk exp [amount] - increases perk experience by amount.");
					yield true;
				}
				var subCommand = args[0];
				switch (subCommand) {
					case "points" -> {
						if (args.length < 2) {
							player.sendMessage("Missing amount parameter, use ::perk points 1 for example.");
							yield true;
						}
						var amount = Integer.parseInt(args[1]);
						player.perkPoints += amount;
						player.sendMessage("You now have " + NumberUtils.formatNumber(player.perkPoints) + " perk points.");
					}
					case "exp" -> {
						if (args.length < 2) {
							player.sendMessage("Missing amount parameter, use ::perk exp 1 for example.");
							yield true;
						}
						var amount = Integer.parseInt(args[1]);
						player.getPlayerPerkHandler().addPerkExperience(player, amount);
						player.sendMessage(
								"You now have " + NumberUtils.formatNumber(player.perkTreeExperience) + " perk experience.");
					}

					default -> player.sendMessage("Invalid ::perk command.");
				}

				yield true;
			}

			case "slayerpoints" -> {
				VarPlayerRepository.SLAYER_POINTS.set(player, 10000);
				player.sendMessage("You now have " + NumberUtils.formatNumber(VarPlayerRepository.SLAYER_POINTS.get(player))
						+ " slayer points.");
				yield true;
			}

			case "pets" -> {
				var bank = player.getBank();
				for (var pet : Pet.values()) {
					bank.add(pet.itemId);
				}
				player.sendMessage("Pets have been added to your bank.");
				yield true;
			}

			case "item" -> {
				var excluded = new HashSet<>(Set.of(
						30466,
						30467,
						30468,
						30497,
						30464,
						59524,
						59525,
						30461,
						4810,
						30446,
						30462,
						30448,
						30449,
						32000));

				var ids = NumberUtils.toIntArray(args[0]);
				var amount = args.length > 1 ? NumberUtils.intValue(args[1]) : 1;

				for (var id : ids) {
					if (id < 0) {
						continue;
					}

					if (excluded.contains(id)) {
						continue;
					}

					player.getInventory().add(id, amount);
				}

				yield true;
			}

			case "fi" -> {
				handleFindItem(player, query, command, args);
				yield true;
			}

			case "master" -> {
				int xp = Stat.xpForLevel(99);
				for (int i = 0; i < StatType.VALUES.length; i++) {
					Stat stat = player.getStats().get(i);
					stat.currentLevel = stat.fixedLevel = 99;
					stat.experience = xp;
					stat.updated = true;
				}

				player.getCombat().updateLevel();
				player.getAppearance().update();
				yield true;
			}

			case "tp", "tele", "teleport" -> {
				handleTeleport(player, query, command, args);
				yield true;
			}

			case "teleto" -> {
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
				yield true;
			}
			case "copyinv" -> {
				String name = query.substring(query.indexOf(" ") + 1);
				Player p2 = World.getPlayer(name);
				if (p2 == null) {
					player.sendMessage(name + " could not be found.");
					yield true;
				}
				for (int slot = 0; slot < player.getInventory().getItems().length; slot++) {
					Item item = p2.getInventory().get(slot);
					if (item == null)
						player.getInventory().set(slot, null);
					else
						player.getInventory().set(slot, item.copy());
				}
				player.sendMessage("You have copied " + name + "'s inventory.");
				yield true;
			}

			case "copyarm" -> {
				String name = query.substring(query.indexOf(" ") + 1);
				Player p2 = World.getPlayer(name);
				if (p2 == null) {
					player.sendMessage(name + " could not be found.");
					yield true;
				}
				for (int slot = 0; slot < player.getEquipment().getItems().length; slot++) {
					Item item = p2.getEquipment().get(slot);
					if (item == null)
						player.getEquipment().set(slot, null);
					else
						player.getEquipment().set(slot, item.copy());
				}
				player.getAppearance().update();
				player.sendMessage("You have copied " + name + "'s armor.");
				yield true;
			}

			default -> false;
		};
	}
}
