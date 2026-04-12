package io.ruin.services.discord.old.impl;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.ServerWrapper;
import io.ruin.model.World;
import io.ruin.model.content.itembreaking.ItemBreakPerks;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.services.discord.old.Webhook;
import io.ruin.services.discord.old.util.Embed;
import io.ruin.services.discord.old.util.Field;
import io.ruin.services.discord.old.util.Message;
import properties.ServerProperties;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-19
 */
@Deprecated(forRemoval = true)
public class ItemPerkWebhook {

	private static final Webhook WEBHOOK = new Webhook(ServerProperties.get("item_perk_discord_hook", ""));

	public static void sendTierFiveUpgrade(Player player, Item item, ItemBreakPerks perk) {
		if (!World.isLive())
			return;
		try {
			var message = new Message();
			var embedMessage = new Embed();
				embedMessage.setTitle("Item Perk Event");
				embedMessage.setDescription("`%s` has just achieved Tier 5".formatted(player.getName()));
				embedMessage.setColor(new Color(15, 255, 15).hashCode());
				embedMessage.setFields(
					new Field("Perk", perk.getName(), true),
					new Field("Item", item.getDef().name,true)
				);
				message.setEmbeds(embedMessage);
			WEBHOOK.sendMessage(message.toJson());
		}
		catch (Exception e) {
			ServerWrapper.logError("Failed to send discord embed", e);
		}
	}

	public static void sendItemBreakdown(Player player, Item item, List<Item> minerals) {
		if (!World.isLive())
			return;
		try {
			var message = new Message();
			var embedMessage = new Embed();
				embedMessage.setTitle("Item Breakdown Event");
				embedMessage.setDescription("`%s` has just brokedown an Item".formatted(player.getName()));
				embedMessage.setColor(new Color(15, 255, 15).hashCode());
			var fields = new ArrayList<Field>();
			fields.add(new Field("Item", item.getDef().name, false));
			if (item.getAmount() > 1)
				fields.add(new Field("Amount", NumberUtils.formatNumber(item.getAmount()), false));
			fields.add(new Field("", "Returned Materials:", false));
			for (Item m : minerals)
				fields.add(new Field(m.getDef().name, NumberUtils.formatNumber(m.getAmount()), false));

			embedMessage.setFields(fields.toArray(Field[]::new));
			message.setEmbeds(embedMessage);
			WEBHOOK.sendMessage(message.toJson());
		}
		catch (Exception e) {
			ServerWrapper.logError("Failed to send discord embed", e);
		}
	}


}
