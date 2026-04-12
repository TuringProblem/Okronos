package io.ruin.services.discord.commands.staff;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.services.discord.configuration.SlashCommand;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Reset implements SlashCommand {


	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		event.deferReply().setEphemeral(true).queue();
		String playerName = event.getOption("player").getAsString().toLowerCase();
		Player player = World.getPlayer(playerName);

		if (player == null) {
			event.getHook().sendMessageFormat("Player %s not found", playerName).queue();
			return;
		}

		if (event.getSubcommandName().equals("slayertask")) {
			VarPlayerRepository.SLAYER_TASK_AMOUNT.set(player, 0);
			VarPlayerRepository.SLAYER_TASK.set(player, 0);
			VarPlayerRepository.SLAYER_MASTER.set(player, 0);
			player.sendMessage("A Staff member has reset your slayer task...");
			event.getHook().sendMessageFormat("%s's %s has been reset.", playerName).queue();
		}

		if (event.getSubcommandName().equalsIgnoreCase("level")) {
			String skillname = event.getOption("skill").getAsString();
			StatType skill = StatType.get(StringUtils.capitalize(skillname.toLowerCase()));
			if (skill == null) {
				event.getHook().sendMessageFormat("Skill not found for %s", skillname).queue();
				return;
			}
			Stat stat = player.getStats().get(skill.ordinal());
			stat.currentLevel = stat.fixedLevel = 1;
			stat.experience = 0;
			stat.updated = true;
			player.getCombat().updateLevel();
			player.getAppearance().update();
			event.getHook().sendMessageFormat("%s's %s has been reset.", playerName, skillname).queue();
		}

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
		if (event.getFocusedOption().getName().equals("skill")) {
			List<Command.Choice> options = Stream.of(StatType.VALUES)
				.filter(word -> word.name().contains(event.getFocusedOption().getValue()))
				.map(word -> new Command.Choice(word.name(), word.name()))
				.collect(Collectors.toList());
			event.replyChoices(options).queue();
		}
	}

	@Override
	public String getName() {
		return "reset";
	}
}
