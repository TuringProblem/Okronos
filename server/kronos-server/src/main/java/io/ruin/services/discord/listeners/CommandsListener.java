package io.ruin.services.discord.listeners;

import io.ruin.services.discord.Discord;
import io.ruin.services.discord.configuration.SlashCommandManager;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandsListener extends ListenerAdapter {

	private final SlashCommandManager slashCommandManager = new SlashCommandManager();

	public static void updateCommands() {
		Discord.guild.updateCommands().addCommands(

			// Slash
			Commands.slash("link", "Links your discord account to your in-game account.")
				.addOption(OptionType.STRING, "username", "Your in-game username (no one will see this message in Discord)",
					true, false),

			// Staff Slash
			Commands.slash("players", "Displays all players in-game"),
			Commands.slash("kick", "Kicks a player in-game")
				.addOption(OptionType.STRING, "player", "Player name", true, true)
				.addOption(OptionType.BOOLEAN, "force", "Force kick player (bypasses locks - admin only)", false),

			Commands.slash("move", "Moves a player to a specific location")
				.addOption(OptionType.STRING, "player", "Player name", true, true)
				.addOption(OptionType.STRING, "location", "Location you want to move the player to", true, true),

			Commands
				.slash("reset",
					"Will send you a notification for anything listed in the trade post matching your search text")
				.addSubcommands(
					new SubcommandData("slayertask", "Resets a players slayer task")
						.addOption(OptionType.STRING, "player", "Player name", true, true),
					new SubcommandData("level", "Resets a players level")
						.addOption(OptionType.STRING, "player", "Player name", true, true)
						.addOption(OptionType.STRING, "skill", "Skill to reset", true, true))
			// Interactions
		).queue();
	}

	/**
	 * These aren't really complicated so going to keep it here
	 *
	 * @param event
	 */
	@Override
	public void onMessageContextInteraction(MessageContextInteractionEvent event) {

	}

	@Override
	public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
		slashCommandManager.handleAutoCompleteInteraction(event);
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		slashCommandManager.handleInteraction(event);
	}
}
