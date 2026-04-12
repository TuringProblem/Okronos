package io.ruin.services.discord.old.impl;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.ServerWrapper;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.services.discord.old.Webhook;
import io.ruin.services.discord.old.util.Embed;
import io.ruin.services.discord.old.util.Field;
import io.ruin.services.discord.old.util.Message;
import properties.ServerProperties;

import java.time.LocalDateTime;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-19
 */
@Deprecated(forRemoval = true)
public class VotingWebhook {

	private static final Webhook WEBHOOK = new Webhook(ServerProperties.get("vote_discord_hook", ""));

	public static void sendVoted(Player player) {
		if (!World.isLive())
			return;
		try {
			var message = new Message();
			var embedMessage = new Embed();
				embedMessage.setTitle("Vote Event");
				embedMessage.setDescription("`%s` has just voted".formatted(player.getName()));
				embedMessage.setColor(8917522);
				embedMessage.setFields(
					new Field("Vote Streak", NumberUtils.formatNumber(player.voteStreak), true),
					new Field("Voting Date/Time", LocalDateTime.now().toString(),true),
					new Field("Hardware ID", player.hwid, true)
				);
				message.setEmbeds(embedMessage);
			WEBHOOK.sendMessage(message.toJson());
		}
		catch (Exception e) {
			ServerWrapper.logError("Failed to send discord embed", e);
		}
	}

	public static void sendClaimedVoteStreak(Player player, Item reward) {
		if (!World.isLive())
			return;
		try {
			var message = new Message();
			var embedMessage = new Embed();
				embedMessage.setTitle("VoteStreak claim Event");
				embedMessage.setDescription("`%s` has just claimed their vote streak reward.".formatted(player.getName()));
				embedMessage.setColor(8917522);
				embedMessage.setFields(
					new Field("Item", reward.getDef().getName().concat(" x ").concat(NumberUtils.formatNumber(reward.getAmount())), true),
					new Field("Vote Streak", NumberUtils.formatNumber(player.voteStreak), true),
					new Field("Claimed Date/Time", LocalDateTime.now().toString(),true),
					new Field("Hardware ID", player.hwid, true)
				);
				message.setEmbeds(embedMessage);
			WEBHOOK.sendMessage(message.toJson());
		}
		catch (Exception e) {
			ServerWrapper.logError("Failed to send discord embed", e);
		}
	}
}
