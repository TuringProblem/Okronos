package discord.webhooks.logs;

import discord.comp.impl.Embed;
import discord.comp.impl.Field;
import discord.comp.impl.Message;
import discord.webhooks.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import properties.ServerProperties;

import java.awt.*;
import java.util.function.Supplier;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
@Slf4j
public class DonationClaimHook {

	public static void sendDonatorLogsToDiscord(Supplier<JSONObject> object) {
		try {
			sendDonatorLogsToDiscord(object.get());
		} catch (Exception e) {
			log.error("Webhook error: ", e);
		}
	}

	static void sendDonatorLogsToDiscord(JSONObject object) {
		var embedMessage = new Embed();
		embedMessage.setTitle("Donation Claim Event");
		embedMessage.setDescription("`%s` has just claimed a donation"
				.formatted(object.getString("player")));
		embedMessage.setColor(new Color(255, 0, 255));
		embedMessage.setFields(
				new Field("HWID", object.get("player_hwid").toString(), false),
				new Field("Item Name", object.get("item_name").toString(), true),
				new Field("Item amount", object.get("item_amount").toString(), true));

		var message = new Message();
		message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("discord_hook_donation_claim", ""), message);
	}
}
