package io.ruin.services;

import io.ruin.Server;
import io.ruin.api.utils.IPBans;
import io.ruin.api.utils.IPMute;
import io.ruin.api.utils.StringUtils;
import io.ruin.api.utils.TimeUtils;
import io.ruin.model.World;
import io.ruin.model.activities.jail.Jail;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerGroup;
import io.ruin.network.central.CentralClient;
import io.ruin.services.discord.Discord;
import io.ruin.services.discord.configuration.DiscordHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Date;
import java.util.Objects;

public class Punishment {

	/**
	 * Kicking (Admins only to prevent inevitable problems)
	 */
	public static void kick(Player p1, Player p2) {
		p1.sendMessage("Kicking " + p2.getName() + "...");
		World.startEvent(e -> {
			p2.lock();
			p2.resetActions(true, true, true);
			p2.forceLogout();
		});
	}

	/**
	 * Force Kicking (USE LIGHTLY!)
	 */
	public static void forceKick(Player p1, Player p2) {
		if (p1 != null) {
			p1.sendMessage("Kicking " + p2.getName() + "...");
		}
		World.startEvent(e -> {
			if (!p2.isOnline())
				return;
			p2.lock();
			p2.resetActions(true, true, true);
			p2.forceLogout();
		});
	}

	/**
	 * Enhanced Jailing System
	 */
	public static void jail(Player p1, Player p2, int ores) {
		// Input validation
		if (ores <= 0) {
			p1.sendMessage("Ore count must be greater than 0.");
			return;
		}

		if (p2.isJailed()) {
			p1.sendMessage(p2.getName() + " is already in jail.");
			return;
		}

		p1.sendMessage("Jailing " + p2.getName() + " to " + ores + " ores...");

		World.startEvent(e -> {
			while (p2.isLocked())
				e.delay(1); // wait
			if (!p2.isOnline())
				return;

			p2.resetActions(true, true, true);
			Jail.jailPlayer(p2, p1.getName(), ores);  // Use the new jail system
		});

		logPunishment(p1, p2, -2, "jailed", new MessageEmbed.Field("Ores", String.valueOf(ores), true));
	}


	public static void jail(Player player, NPC npc, int ores) {
		if (player.isJailed()) {
			return;
		}

		World.startEvent(e -> {
			if (!player.isOnline())
				return;

			player.resetActions(true, true, true);
			Jail.jailPlayer(player, npc.getDef().name, ores);  // Use the new jail system
		});
	}

	public static void unjail(Player p1, Player p2) {
		if (!p2.isJailed()) {
			p1.sendMessage(p2.getName() + " is not in jail.");
			return;
		}

		// Set collected ores to trigger release
		p2.jailOresCollected = p2.jailOresAssigned;

		p1.sendMessage("You unjailed " + p2.getName() + ".");
		logPunishment(p1, p2, -2, "unjailed");
	}

	/**
	 * Muting System
	 */
	public static void mute(Player p1, Player p2, long time, boolean shadow) {
		p2.muteEnd = time;
		p2.shadowMute = shadow;
		p1.sendMessage(p2.getName() + " is now muted.");
		logPunishment(p1, p2, time, shadow ? "shadowmuted" : "muted");
	}

	public static void unmute(Player p1, Player p2) {
		if (p2.muteEnd == 0) {
			p1.sendMessage(p2.getName() + " isn't muted.");
			return;
		}
		if (p2.shadowMute && !p1.isAdmin()) {
			p1.sendMessage("This player can only be unmuted by an admin.");
			return;
		}
		p2.muteEnd = 0;
		if (p2.shadowMute)
			p2.shadowMute = false;
		else
			p2.sendMessage("Your mute has been lifted.");
		p1.sendMessage(p2.getName() + " is now unmuted.");
		logPunishment(p1, p2, -2, "unmuted");
	}

	public static boolean isMuted(Player player) {
		return player.muteEnd == -1 || (player.muteEnd > 0 && System.currentTimeMillis() < player.muteEnd);
	}

	/**
	 * Banning System
	 */
	public static void ban(Player p1, Player p2) {
		p1.sendMessage("Attempting to ban " + p2.getName() + "...");
		p2.setPermBanned(true);
		p2.forceLogout();
		PlayerGroup.BANNED.sync(p2, "ban", () -> {
			Server.worker.execute(() -> {
				p2.forceLogout();
				p1.sendMessage("Successfully banned " + p2.getName() + "!");
				logPunishment(p1, p2, -1, "banned");
			});
		});
	}

	public static void uuidBan(Player staffMember, Player punishedUser) {
		CentralClient.requestUUIDBan(punishedUser.uuid());
		ban(staffMember, punishedUser);
		World.getPlayerStream()
			.filter(player -> player.uuid().equalsIgnoreCase(punishedUser.uuid()))
			.forEach(Player::forceLogout);
		logPunishment(staffMember, punishedUser, -1, "UUID banned");
	}

	/**
	 * IP-Based Punishments
	 */
	public static void ipMute(Player staffMember, Player punishedUser) {
		IPMute.requestMute(punishedUser.getName(), punishedUser.getIp());
		mute(staffMember, punishedUser, Long.MAX_VALUE, true);
		logPunishment(staffMember, punishedUser, Long.MAX_VALUE, "IP Muted");
	}

	public static void ipBan(Player staffMember, Player punishedUser) {
		if (Objects.requireNonNull(World.getPlayer(punishedUser.getName())).isAdmin())
			return;

		String user = punishedUser.getName();
		String ip = punishedUser.getIp();

		// Ban the IP
		IPBans.requestBan(user, ip);

		// Ban the player and kick all related IPs
		ban(staffMember, punishedUser);
		World.getPlayerStream()
			.filter(player -> player.getIp().equalsIgnoreCase(ip))
			.forEach(Player::forceLogout);

		logPunishment(staffMember, punishedUser, -1, "IP banned");
	}

	/**
	 * Punishment Logging
	 */
	public static void logPunishment(Player staff, Player victim, long time, String type, MessageEmbed.Field... fields) {
		String until;

		if (time == -1 || time == -2) {
			until = "Never (perm).";
		} else {
			until = TimeUtils.dateFormat.format(new Date(time))
				+ " (in " + TimeUtils.fromMs(time - System.currentTimeMillis(), false) + ")";
		}

		EmbedBuilder builder = new EmbedBuilder();

		builder.setTitle(String.format("%s has been %s by %s", victim.getName(), type, staff.getName()));
		builder.addField("Name", victim.getName(), true);
		builder.addField("Staff member", staff.getName(), true);
		builder.addField("Punishment", StringUtils.capitalizeFirst(type), true);

		if (time != -2)
			builder.addField("Expires", until, true);

		if (fields != null && fields.length > 0) {
			for (MessageEmbed.Field field : fields) {
				builder.addField(field);
			}
		}

		// Send to Discord if channel exists
		if (Discord.builder != null && Discord.builder.getTextChannelById(DiscordHelper.CHANNEL_PUNISHMENTS) != null) {
			Discord.builder.getTextChannelById(DiscordHelper.CHANNEL_PUNISHMENTS)
				.sendMessageEmbeds(builder.build()).queue();
		}
	}
}