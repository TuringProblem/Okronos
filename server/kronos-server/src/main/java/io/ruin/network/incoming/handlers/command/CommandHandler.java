package io.ruin.network.incoming.handlers.command;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import discord.webhooks.logs.AdministrationHook;
import io.ruin.api.discord.dto.CommandNotificationType;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.handlers.TeleInterface;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.map.Locations;
import io.ruin.model.map.Position;
import io.ruin.model.map.Region;
import net.dv8tion.jda.api.EmbedBuilder;
import io.ruin.api.utils.*;
import io.ruin.cache.ObjType;
import org.json.JSONObject;

public class CommandHandler {

	static void handleTeleport(Player player, String query, String command, String[] args) {
		if (args == null || args.length == 0) {
			TeleInterface teleInterface = new TeleInterface();
			teleInterface.open(player);
			return;
		}

		int x, y, z;
		try {
			x = Integer.valueOf(args[0]);
			y = Integer.valueOf(args[1]);
			if (args.length > 2)
				z = Math.max(0, Math.min(3, Integer.valueOf(args[2])));
			else
				z = player.getPosition().getZ();
		} catch (Exception e) {
			int l = command.length() + 1;
			if (query.length() <= l)
				return;
			String loc = query.substring(l).trim();
			Locations locations = Locations.find(loc);
			if (locations == null) {
				player.sendMessage("Invalid teleport location: " + loc);
				return;
			}
			x = locations.x;
			y = locations.y;
			z = locations.z;
		}

		int regionId = Region.getId(x, y);
		if (regionId < 0 || regionId >= Region.LOADED.length) {
			player.sendMessage("Invalid teleport coordinates: " + x + ", " + y + ", " + z);
			return;
		}

		player.getMovement().teleport(x, y, z);

	}

	static void handleFindItem(Player player, String query, String command, String[] args) {
		int l = command.length() + 1;
		if (query.length() > l) {
			String search = query.substring(l).toLowerCase();
			int found = 0;
			ObjType exactMatch = null;
			List<ObjType> matches = new ArrayList<>(ObjType.cached.values());
			matches.sort((a, b) -> Integer.compare(a.id, b.id));
			for (ObjType def : matches) {
				if (def == null || def.name == null)
					continue;
				if (def.isNote() || def.isPlaceholder())
					continue;
				String name = def.name.toLowerCase();
				if (name.contains(search)) {
					player.sendFilteredMessage("    " + def.id + ": " + def.name);
				}
				if (name.equals(search)) {
					if (exactMatch == null)
						exactMatch = def;
				}
			}
			if (exactMatch != null) {
				player.sendFilteredMessage("Most relevant result for '" + search + "':");
				player.sendFilteredMessage("    " + exactMatch.id + ": " + exactMatch.name);
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date = new Date();
				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle("An Item Was Spawned Ingame!");
				eb.addField("Username:", player.getName(), true);
				eb.addField("When: ", formatter.format(date), true);
				eb.addField("Item:", exactMatch.name, true);
				eb.addField("Amount:", "1", true);
				eb.setColor(new java.awt.Color(0xB00D03));
			}
			return;
		}
		player.itemSearch("Select an item to spawn", false, itemId -> {
			Item item = new Item(itemId, 1);
			player.integerInput("How many would you like to spawn:", amt -> {
				player.sendFilteredMessage("Spawned " + amt + "x " + item.getDef().name);
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date = new Date();
				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle("An Item Was Spawned Ingame!");
				eb.addField("Username:", player.getName(), true);
				eb.addField("When: ", formatter.format(date), true);
				eb.addField("Item:", item.getDef().name, true);
				eb.addField("Amount:", String.valueOf(amt), true);
				eb.setColor(new java.awt.Color(0xB00D03));
			});
		});
	}

	static void forName(Player player, String cmdQuery, String exampleUsage, Consumer<String> consumer) {
		try {
			String name = cmdQuery.substring(cmdQuery.indexOf(" ") + 1).trim();
			consumer.accept(name);
		} catch (Exception e) {
			player.sendMessage("Invalid command usage. Example: [" + exampleUsage + "]");
		}
	}

	static void forNameString(Player player, String cmdQuery, String exampleUsage,
			BiConsumer<String, String> consumer) {
		try {
			String s = cmdQuery.substring(cmdQuery.indexOf(" ") + 1).trim();
			int i = s.lastIndexOf(" ");
			String name = s.substring(0, i).trim();
			String string = s.substring(i).trim();
			consumer.accept(name, string);
		} catch (Exception e) {
			player.sendMessage("Invalid command usage. Example: [" + exampleUsage + "]");
		}
	}

	private static void forNameTime(Player player, String cmdQuery, String exampleUsage,
			BiConsumer<String, Long> consumer) {
		forNameString(player, cmdQuery, exampleUsage, (name, string) -> {
			try {
				if (string.equalsIgnoreCase("perm")) {
					consumer.accept(name, -1L);
					return;
				}
				long time = Long.valueOf(string.substring(0, string.length() - 1));
				String unit = string.substring(string.length() - 1).toLowerCase();
				if (unit.equals("h"))
					time = TimeUtils.getHoursToMillis(time);
				else if (unit.equals("d"))
					time = TimeUtils.getDaysToMillis(time);
				else
					throw new IOException("Invalid time unit: " + unit);
				consumer.accept(name, System.currentTimeMillis() + time);
			} catch (Exception e) {
				ServerWrapper.logError("Invalid command usage. Example: [" + exampleUsage + "]", e);
			}
		});
	}

	public static void forPlayer(Player player, String cmdQuery, String exampleUsage, Consumer<Player> consumer) {
		forName(player, cmdQuery, exampleUsage, name -> {
			try {
				Player p = getOnlinePlayer(player, name);
				if (p != null)
					consumer.accept(p);
			} catch (Exception e) {
				player.sendMessage("Invalid command usage. Example: [" + exampleUsage + "]");
				e.printStackTrace();
			}
		});
	}

	public static void forPlayerString(Player player, String cmdQuery, String exampleUsage,
			BiConsumer<Player, String> consumer) {
		forNameString(player, cmdQuery, exampleUsage, (name, string) -> {
			try {
				Player p = getOnlinePlayer(player, name);
				if (p != null)
					consumer.accept(p, string);
			} catch (Exception e) {
				player.sendMessage("Invalid command usage. Example: [" + exampleUsage + "]");
			}
		});
	}

	static void forPlayerInt(Player player, String cmdQuery, String exampleUsage,
			BiConsumer<Player, Integer> consumer) {
		forNameString(player, cmdQuery, exampleUsage, (name, string) -> {
			try {
				Player p = getOnlinePlayer(player, name);
				if (p != null)
					consumer.accept(p, Integer.valueOf(string));
			} catch (Exception e) {
				player.sendMessage("Invalid command usage. Example: [" + exampleUsage + "]");
			}
		});
	}

	static void forPlayerTime(Player player, String cmdQuery, String exampleUsage,
			BiConsumer<Player, Long> consumer) {
		forNameTime(player, cmdQuery, exampleUsage, (name, time) -> {
			try {
				Player p = getOnlinePlayer(player, name);
				if (p != null)
					consumer.accept(p, time);
			} catch (Exception e) {
				player.sendMessage("Invalid command usage. Example: [" + exampleUsage + "]");
			}
		});
	}

	static Player getOnlinePlayer(Player player, String name) {
		Player p = World.getPlayer(name);
		if (p == null)
			player.sendMessage("User '" + name + "' is not online.");
		return p;
	}

	static void teleport(Player player, Position position) {
		teleport(player, position.getX(), position.getY(), position.getZ());
	}

	static void teleport(Player player, int x, int y, int z) {
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
			player.getMovement().teleport(x, y, z);
		});
	}

	static void teleportHome(Player player, int x, int y, int z) {
		if (player.wildernessLevel > 20 || player.pvpAttackZone) {
			player.sendMessage("You can't use this command from where you are standing.");
			return;
		}

		player.getMovement().startTeleport(event -> {
			if (player.getInventory().contains(25104)) {
				player.sendMessage("The crystal of memories stores your last location as an available teleport.");
				player.crystalMemoryPosition = player.getPosition().copy();
			}
			player.animate(3864);
			player.graphics(1039);
			player.privateSound(200, 0, 10);
			event.delay(2);
			player.getMovement().teleport(x, y, z);
		});
	}

	static void teleportDangerous(Player player, int x, int y, int z) {
		if (player.wildernessLevel > 20 || player.pvpAttackZone) {
			if (!(World.isDev() && player.isAdmin())) {
				player.sendMessage("You can't use this command from where you are standing.");
				return;
			}
		}
		player.dialogue(
				new MessageDialogue(
						"<col=ff0000>Warning:</col> This teleport is inside the wilderness.<br> Are you sure you want to do this?")
						.lineHeight(24),
				new OptionsDialogue(
						new Option("Yes", () -> teleport(player, x, y, z)),
						new Option("No")));
	}

	static void giveBond(Player player, Player p2, Integer amount) {
		final int fiveDollarBond = 30464;
		final int tenDollarBond = 30497;
		final int twentyFiveDollarBond = 30466;
		final int fiftyDollarBond = 30467;
		final int hundredDollarBond = 30468;

		int hundredDollarBonds = amount / 100;
		amount %= 100;
		int fiftyDollarBonds = amount / 50;
		amount %= 50;
		int twentyFiveDollarBonds = amount / 25;
		amount %= 25;
		int tenDollarBonds = amount / 10;
		amount %= 10;
		int fiveDollarBonds = amount / 5;
		amount %= 5;
		if (hundredDollarBonds > 0) {
			p2.getInventory().addOrDrop(hundredDollarBond, hundredDollarBonds);
		}
		if (fiftyDollarBonds > 0) {
			p2.getInventory().addOrDrop(fiftyDollarBond, fiftyDollarBonds);
		}
		if (twentyFiveDollarBonds > 0) {
			p2.getInventory().addOrDrop(twentyFiveDollarBond, twentyFiveDollarBonds);
		}
		if (tenDollarBonds > 0) {
			p2.getInventory().addOrDrop(tenDollarBond, tenDollarBonds);
		}
		if (fiveDollarBonds > 0) {
			p2.getInventory().addOrDrop(fiveDollarBond, fiveDollarBonds);
		}
	}

	public static void logToDiscord(Player player, String command, String... args) {
		var jsonObject = new JSONObject();
			jsonObject.put("player", player.getName());
			jsonObject.put("command_name", command);
			jsonObject.put("command_args", Arrays.toString(args));
		AdministrationHook.sendCommandLogsToDiscordForOwner(CommandNotificationType.OWNER, jsonObject); // TODO: CommandNotificationType.OWNER
	}
}
