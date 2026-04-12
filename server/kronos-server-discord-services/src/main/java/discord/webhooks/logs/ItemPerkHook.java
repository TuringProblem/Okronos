package discord.webhooks.logs;

import discord.comp.impl.Embed;
import discord.comp.impl.Field;
import discord.comp.impl.Message;
import discord.webhooks.Webhook;
import org.json.JSONObject;
import properties.ServerProperties;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
public interface ItemPerkHook {

	static void sendTierFiveUpgrade(JSONObject object) {
		var message = new Message();
		var embedMessage = new Embed();
			embedMessage.setTitle("Item Perk Event");
			embedMessage.setDescription("`%s` has just achieved Tier 5"
				.formatted(object.getString("player")));
			embedMessage.setColor(new Color(15, 255, 15));
			embedMessage.setFields(
				new Field("Perk", object.getString("perk_name"), true),
				new Field("Item", object.getString("item_name"),true)
			);
		message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("discord_hook_item_perk", ""), message);
	}
}
