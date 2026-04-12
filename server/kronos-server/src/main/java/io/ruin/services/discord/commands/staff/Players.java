package io.ruin.services.discord.commands.staff;

import io.ruin.model.World;
import io.ruin.services.discord.configuration.DiscordHelper;
import io.ruin.services.discord.configuration.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Players implements SlashCommand {
	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		String parentCategoryId = event.getChannel().asTextChannel().getParentCategoryId();
		if (parentCategoryId != null && parentCategoryId.equalsIgnoreCase(DiscordHelper.CHANNEL_STAFF_COMMANDS)) {
			event.deferReply().setEphemeral(false).queue();
		} else {
			event.deferReply().setEphemeral(true).queue();
		}
		StringBuilder stringBuilder = new StringBuilder();
		int count = World.playerCount();
		World.players().forEach(player -> {
			stringBuilder.append(StringUtils.capitalize(player.getName()));
			if (player.discordId > 0) {
				stringBuilder.append(" <@").append(player.discordId).append(">");
			}
			stringBuilder.append(", ");
		});
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle("Current players " + count + " at " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
		embedBuilder.addField("Players", stringBuilder.toString(), false);
		event.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
		if (Math.random() <= .10) {
			event.getHook().sendMessage("You lazy fuck maybe you should hop in the game sometime to check").queue();
		}
	}

	@Override
	public String getName() {
		return "players";
	}
}
