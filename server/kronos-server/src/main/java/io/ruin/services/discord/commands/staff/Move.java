package io.ruin.services.discord.commands.staff;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.impl.AchievementLamp;
import io.ruin.model.map.Position;
import io.ruin.services.discord.configuration.SlashCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Move implements SlashCommand {

	@AllArgsConstructor
	enum Locations {

		HOME("Home", new Position(3103, 3502, 0)),

		;
		@Getter
		private final String name;
		@Getter
		private final Position position;
		public static final Locations[] VALUES = values();
	}

	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		event.deferReply().setEphemeral(true).queue();
		String playerName = event.getOption("player").getAsString().toLowerCase();
		String location = event.getOption("location").getAsString().toLowerCase();

		Player player = World.getPlayer(playerName);
		if (player == null) {
			event.getHook().sendMessageFormat("Player %s not found", playerName).queue();
			return;
		}
		for (Locations value : Locations.VALUES) {
			if (value.getName().equalsIgnoreCase(location)) {
				player.getMovement().teleport(value.getPosition());
				event.getHook().sendMessageFormat("%s has been moved home", player.getName()).queue();
				return;
			}
		}
		event.getHook().sendMessageFormat("Location (%s) not found", location).queue();
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

		if (event.getFocusedOption().getName().equals("location")) {
			List<Command.Choice> options = Arrays.stream(Locations.values())
				.filter(word -> word.getName().contains(event.getFocusedOption().getValue()))
				.map(word -> new Command.Choice(word.getName(), word.getName()))
				.collect(Collectors.toList());
			event.replyChoices(options).queue();
		}
	}

	@Override
	public String getName() {
		return "move";
	}

}
