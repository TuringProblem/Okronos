package clanchat;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.services.Punishment;
import io.ruin.utility.BadWords;
import lombok.experimental.ExtensionMethod;
import net.rsprot.protocol.game.outgoing.friendchat.MessageFriendChannel;

import static core.task.api.API.queue;

@ExtensionMethod(Attributes.class)
public final class FriendChatMessaging {

	public static void sendMessage(Player from, String message) {
		if (Punishment.isMuted(from)) {
			if (!from.shadowMute) {
				from.sendMessage("You're muted and can't talk.");
			}
			return;
		}

		queue(() -> {
			var clanInfo = from.clan();
			if (clanInfo.joinedName.isEmpty()) {
				return;
			}

			var clan = FriendChatDb.db().load(clanInfo.joinedName).await();

			final String fromName = from.getName();

			var _member = clan.member(fromName);
			if (_member == null) {
				return;
			}

			final MessageFriendChannel msg = new MessageFriendChannel(
					fromName,
					clan.channelName,
					World.id,
					World.getNextWorldMessageCounter(),
					from.getMessagingRank().raw,
					BadWords.filterBadWords(message));

			for (var member : clan.members) {
				var player = World.getPlayer(member.getUsername());
				if (player == null) {
					continue;
				}

				player.getPacketSender().write(msg);
			}
		});
	}

}
