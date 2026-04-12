package discord.webhooks.logs;

import discord.comp.impl.Embed;
import discord.comp.impl.Field;
import discord.comp.impl.Message;
import discord.webhooks.Webhook;
import org.json.JSONObject;
import properties.ServerProperties;

import java.time.LocalDateTime;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-21
 */
public interface VotingHook {

	static void sendVoted(JSONObject dto) {
		var embedMessage = new Embed();
			embedMessage.setTitle("Vote Event");
			embedMessage.setDescription("`%s` has just voted".formatted(dto.getString("player")));
			embedMessage.setColor(8917522);
			embedMessage.setFields(
				new Field("Vote Streak", "" + dto.getInt("vote_streak"), true),
				new Field("Voting Date/Time", LocalDateTime.now().toString(),true),
				new Field("Hardware ID", dto.getString("hwid"), true)
			);

		var message = new Message();
			message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("vote_discord_hook", ""), message);
	}

	static void sendClaimedVoteStreak(JSONObject dto) {
		var message = new Message();
		var embedMessage = new Embed();
			embedMessage.setTitle("VoteStreak claim Event");
			embedMessage.setDescription("`%s` has just claimed their vote streak reward."
				.formatted(dto.getString("player")));
			embedMessage.setColor(8917522);
			embedMessage.setFields(
				new Field("Item", dto.getString("item_name").concat(" x ").concat("" + dto.getInt("item_amount")), true),
				new Field("Vote Streak", "" + dto.getInt("vote_streak"), true),
				new Field("Claimed Date/Time", LocalDateTime.now().toString(),true),
				new Field("Hardware ID", dto.getString("hwid"), true)
			);
		message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("vote_discord_hook", ""), message);
	}
}