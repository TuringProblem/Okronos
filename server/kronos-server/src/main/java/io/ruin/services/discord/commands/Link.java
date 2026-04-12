package io.ruin.services.discord.commands;

import io.ruin.model.entity.player.Player;
import io.ruin.services.discord.Discord;
import io.ruin.services.discord.configuration.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.HashMap;

public class Link implements SlashCommand {

	public static HashMap<String, Long> accountLinkingQueue = new HashMap<>();

	@Override
	public void handleSlashCommandInteraction(SlashCommandInteractionEvent event) {
		event.deferReply().setEphemeral(true).queue();
		String username = event.getOption("username").getAsString().toLowerCase();
		String s = addUser(username, event.getUser().getIdLong());
		event.getHook().sendMessage(s).setEphemeral(true).queue();
	}

	private String addUser(String account, long idLong) {
		if (!accountLinkingQueue.containsKey(account)) {
			accountLinkingQueue.put(account, idLong);
			return "Finish your linking by typing in ;;link in-game.";
		}
		return "This account is already linked.";
	}

	@Override
	public String getName() {
		return "link";
	}

	public static void complete(Player player) {
		if (player.discordId > 0) {
			player.sendMessage("Your discord account is already linked.");
			return;
		}
		String name = player.getName().toLowerCase();
		if (!accountLinkingQueue.containsKey(name)) {
			player.sendMessageFormat("You need to go in Discord and type /link");
			return;
		}
		long discordId = accountLinkingQueue.get(name);
		player.discordId = discordId;
		Discord.builder.retrieveUserById(discordId).queue(user -> user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageFormat("Your discord account is now linked to %s", player.getName()).queue()));
		accountLinkingQueue.remove(name);
	}
}
