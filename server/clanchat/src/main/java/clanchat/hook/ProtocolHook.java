package clanchat.hook;

import clanchat.FriendChats;
import io.ruin.HooksV2.Result;
import io.ruin.model.entity.player.Player;
import io.ruin.rsprot.RSProtService;
import net.rsprot.protocol.game.incoming.friendchat.FriendChatJoinLeave;
import net.rsprot.protocol.game.incoming.friendchat.FriendChatKick;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

public class ProtocolHook {
	public static void register() {
		RSProtService.hooks.register(RSProtService.Hook.Accept.class, ProtocolHook::handle);
	}

	private static Result handle(RSProtService.Hook.Accept ctx) {
		var handlers = ctx.handlers();
		handlers.addListener(FriendChatJoinLeave.class, new JoinLeave());
		handlers.addListener(FriendChatKick.class, new Kick());
		return Result.Pass;
	}

	static class JoinLeave implements MessageConsumer<Player, FriendChatJoinLeave> {

		@Override
		public void consume(Player player, FriendChatJoinLeave msg) {
			if (msg.getName() == null) {
				FriendChats.leaveCurrent(player);
			} else {
				FriendChats.join(player, msg.getName().toLowerCase().trim());
			}
		}
	}

	static class Kick implements MessageConsumer<Player, FriendChatKick> {

		@Override
		public void consume(Player player, FriendChatKick msg) {
			FriendChats.kick(player, msg.getName());
		}
	}
}
