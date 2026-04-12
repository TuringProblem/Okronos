package discord.webhooks.logs;

import discord.comp.impl.Embed;
import discord.comp.impl.Field;
import discord.comp.impl.Message;
import discord.webhooks.Webhook;
import properties.ServerProperties;

import java.awt.*;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
public interface ReferralHook {

	static void sendRefClaimLogToDiscord(String player, String code) {
		var message = new Message();
		var embedMessage = new Embed();
			embedMessage.setTitle("Referral Event");
			embedMessage.setDescription("`%s` has claimed a referral code.".formatted(player));
			embedMessage.setColor(new Color(32, 96, 255));
			embedMessage.setFields(new Field("Code", code,true));
		message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("discord_hook_referral", ""), message);
	}
}
