package io.ruin.services.discord.configuration;

import io.ruin.services.discord.commands.*;
import io.ruin.services.discord.commands.staff.Kick;
import io.ruin.services.discord.commands.staff.Move;
import io.ruin.services.discord.commands.staff.Players;
import io.ruin.services.discord.commands.staff.Reset;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Slash Command Manager (Discord)
 * Create commands inside manager.slashcommands. and add to this list once done
 *
 * @author CrazzMC - 6-4-2021
 * @project Kano
 */
public class SlashCommandManager {

	private final List<SlashCommand> commands = new ArrayList<>();

	public SlashCommandManager() {
		// Staff
		addCommand(new Kick());
		addCommand(new Move());
		addCommand(new Players());
		addCommand(new Reset());

		// Regular
		addCommand(new Link());
	}

	private void addCommand(SlashCommand command) {
		boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equals(command.getName()));

		if (nameFound)
			throw new IllegalArgumentException("A command with this name is already present.");

		commands.add(command);
	}

	public void handleInteraction(SlashCommandInteractionEvent event) {
		SlashCommand command = this.getCommand(event.getName());
		if (command != null) {
			command.handleSlashCommandInteraction(event);
		}
	}

	public void handleAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
		SlashCommand command = this.getCommand(event.getName());
		if (command != null) {
			command.handleAutoCompleteInteraction(event);
		}
	}

	public SlashCommand getCommand(String search) {
		String searchLower = search.toLowerCase();

		for (SlashCommand cmd : this.commands) {
			if (cmd.getName().equals(searchLower)) {
				return cmd;
			}
		}

		return null;
	}
}
