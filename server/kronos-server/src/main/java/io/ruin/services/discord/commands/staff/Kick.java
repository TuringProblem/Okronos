package io.ruin.services.discord.commands.staff;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.services.Punishment;
import io.ruin.services.discord.configuration.DiscordHelper;
import io.ruin.services.discord.configuration.SlashCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Kick implements SlashCommand {
	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		event.deferReply().setEphemeral(true).queue();
		String playerName = event.getOption("player").getAsString().toLowerCase();
		Player player = World.getPlayer(playerName);

		if (player == null) {
			event.getHook().sendMessageFormat("Player %s not found", playerName).queue();
			return;
		}
		boolean force = false;
		force = Objects.requireNonNull(event.getOption("force")).getAsBoolean();
		if (force) {
			Member member = event.getMember();
			if (DiscordHelper.hasRole(member, DiscordHelper.STAFF_ROLE) || DiscordHelper.hasRole(member, DiscordHelper.DEVELOPER_ROLE))
				Punishment.forceKick(null, player);
			else {
				event.getHook().sendMessageFormat("You are not a Staff Member please use /kick [playername]", player.getName()).queue();
				return;
			}
		} else {
			Punishment.kick(null, player);
		}
		event.getHook().sendMessageFormat("%s has been kicked", player.getName()).queue();
	}

	@Override
	public void handleAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
		if (event.getFocusedOption().getName().equals("player")) {
			List<Command.Choice> options = World.getPlayerStream()
				.filter(word -> word.getName().contains(event.getFocusedOption().getValue()))
				.map(word -> new Command.Choice(word.getName(), word.getName()))
				.collect(Collectors.toList());
			event.replyChoices(options).queue();
		}
	}

	@Override
	public String getName() {
		return "kick";
	}
}