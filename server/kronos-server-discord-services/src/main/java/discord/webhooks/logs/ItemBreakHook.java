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
public interface ItemBreakHook {

	static void sendItemBreakMessage(JSONObject object) {
		var embedMessage = new Embed();
			embedMessage.setTitle("Item Breakdown Event");
			embedMessage.setDescription("`%s` has just brokedown an Item"
				.formatted(object.get("player").toString()));
			embedMessage.setColor(new Color(15, 255, 15));
			embedMessage.setFields(new Field("Item",
				object.get("item_name").toString()
					.concat(" (ID: ")
					.concat(object.get("item_id").toString())
					.concat(")"), false));

		var fields = new ArrayList<Field>();
		var minerals = object.getJSONArray("minerals");
		for (int i = 0; i < minerals.length(); i++) {
			var item = minerals.getJSONObject(i);
			fields.add(new Field(
				item.get("item_name").toString(),
				item.get("item_amount").toString(),
				false)
			);
		}
		var mineralEmbed = new Embed();
			mineralEmbed.setTitle("Mineral Breakdown");
			mineralEmbed.setFields(fields.toArray(Field[]::new));

		var message = new Message();
			message.setEmbeds(embedMessage, mineralEmbed);

		Webhook.send(ServerProperties.get("discord_hook_item_breakdown", ""), message);
	}
}
