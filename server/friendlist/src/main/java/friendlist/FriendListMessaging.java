package friendlist;

import discord.webhooks.logs.PrivateMessageHook;
import io.ruin.model.World;
import io.ruin.services.Punishment;
import io.ruin.utility.BadWords;
import net.rsprot.protocol.game.outgoing.social.MessagePrivate;
import net.rsprot.protocol.game.outgoing.social.MessagePrivateEcho;
import org.json.JSONObject;

import static core.task.api.API.queue;

public final class FriendListMessaging {

	public static void sendMessage(String from, String to, String message) {
		queue(() -> {
			var toPlayer = World.getPlayer(to);
			var fromPlayer = World.getPlayer(from);
			if (fromPlayer == null) {
				return;
			}

			if (Punishment.isMuted(fromPlayer)) {
				if (!fromPlayer.shadowMute) {
					fromPlayer.sendMessage("You're muted and can't talk.");
				}
				return;
			}

			if (toPlayer == null) {
				fromPlayer.sendMessage("That player is currently offline.");
				return;
			}

			var toFl = FriendListDb.db().load(to.toLowerCase().trim()).await();
			var toOnline = FriendListUpdater.isPlayerOnline(fromPlayer, toFl);

			if (!toOnline) {
				fromPlayer.sendMessage("That player is currently offline.");
				return;
			}

			final String filteredMessage = BadWords.filterBadWords(message);

			var msgFrom = new MessagePrivateEcho(to, filteredMessage);
			fromPlayer.getPacketSender().write(msgFrom);
			var icon = fromPlayer.getMessagingRank().raw;
			var msgTo = new MessagePrivate(
					from,
					World.id,
					World.getNextWorldMessageCounter(),
					icon,
					filteredMessage);
			toPlayer.getPacketSender().write(msgTo);

//			RareDropEmbedMessage.sendPMLogsToDiscord(fromPlayer, toPlayer.getName(), message);

			var jsonObject = new JSONObject();
				jsonObject.put("player", fromPlayer.getName());
				jsonObject.put("recipient", toPlayer.getName());
				jsonObject.put("message", message);

			PrivateMessageHook.sendPMLogsToDiscord(jsonObject);
		});
	}
}
