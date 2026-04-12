package io.ruin.services.discord.configuration;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashCommand {

	void handleSlashCommandInteraction(SlashCommandInteractionEvent event);

	default void handleAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {

	}

	String getName();

}
