package io.ruin.network.incoming.handlers;

import discord.webhooks.logs.AdministrationHook;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.discord.dto.CommandNotificationType;
import io.ruin.services.Loggers;
import io.ruin.model.entity.player.*;
import io.ruin.network.incoming.Incoming;
import io.ruin.network.incoming.handlers.command.*;
import io.ruin.utility.*;

import lombok.extern.slf4j.Slf4j;
import net.rsprot.protocol.game.incoming.misc.user.ClientCheat;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

import static io.ruin.network.ClientProt204.CLIENT_CHEAT;

import io.ruin.HooksV2;
import org.json.JSONObject;

import java.util.Arrays;

@Slf4j
@IdHolder(ids = { CLIENT_CHEAT })
public class CommandHandler implements Incoming, MessageConsumer<Player, ClientCheat> {

	public static interface Hook {
		record Handle(Player player, String command, String[] args) implements Hook {};
	}

	public static HooksV2<Hook> hooks = new HooksV2<>(Hook.class);

	@Override
	public void consume(Player player, ClientCheat msg) {
		handle(player, msg.getCommand());
	}

	@Override
	public void handle(Player player, InBuffer in, int opcode) {
		String query = in.readStringCp1252NullTerminated();
		handle(player, query);
	}

	public static void handle(Player player, String query) {
		if ((query = query.trim()).isEmpty()) {
			return;
		}

		if (!query.contains("yell") && !query.contains("Yell")) {
			player.sendFilteredMessage("<col=cc0000>::" + query);
		}

		Loggers.logCommand(player.getUserId(), player.getName(), player.getIp(), query);

		String _command;
		String[] args;
		int spaceIndex = query.indexOf(' ');
		if (spaceIndex == -1) {
			_command = query;
			args = new String[0];
		} else {
			_command = query.substring(0, spaceIndex);
			args = query.substring(spaceIndex + 1).split(" ");
		}
		try {
			var command = _command.toLowerCase();

			if (hooks.handle(new Hook.Handle(player, command, args))) {
				return;
			}

			if (CommandHandlerAdmin.handle(player, query, command, args)) {
				logToDiscord(CommandNotificationType.OWNER, player, command, args);
				return;
			}

			if (CommandHandlerCommunityAdmin.handle(player, query, command, args)) {
				logToDiscord(CommandNotificationType.COMMUNITY_MANAGER, player, command, args);
				return;
			}

			if (CommandHandlerManager.handle(player, query, command, args)) {
				logToDiscord(CommandNotificationType.COMMUNITY_MANAGER, player, command, args);
				return;
			}

			if (player.isLocked()) {
				player.sendMessage("Please finish what you're doing first.");
				return;
			}

			if (CommandHandlerBeta.handle(player, query, command, args)) {
				logToDiscord(CommandNotificationType.BETA, player, command, args);
				return;
			}

			if (CommandHandlerSupport.handle(player, query, command, args)) {
				logToDiscord(CommandNotificationType.SUPPORTER, player, command, args);
				return;
			}

			if (CommandHandlerModerator.handle(player, query, command, args)) {
				logToDiscord(CommandNotificationType.MODERATOR, player, command, args);
				return;
			}

			if (CommandHandlerModeratorSenior.handle(player, query, command, args)) {
				logToDiscord(CommandNotificationType.HEAD_MODERATOR, player, command, args);
				return;
			}

			if (CommandHandlerRegular.handle(player, query, command, args)) {
				logToDiscord(CommandNotificationType.USER, player, command, args);
				return;
			}
			player.sendMessage("Sorry, that command does not exist.");
		} catch (Throwable t) {
			log.warn("Error handling command " + player.captureState(), t);
			if (player.isAdmin()) {
				player.sendMessage(
						"Error handling command '" + query + "': " + t.getClass().getSimpleName() + ": " + t.getMessage());
			}
		}
	}

	public static void logToDiscord(CommandNotificationType cal, Player player, String command, String... args) {
		var jsonObject = new JSONObject();
			jsonObject.put("player", player.getName());
			jsonObject.put("command_name", command);
			jsonObject.put("command_args", Arrays.toString(args));
		AdministrationHook.sendCommandLogsToDiscordForOwner(cal, jsonObject);
	}
}
