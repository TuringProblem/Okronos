package friendlist.hook;

import friendlist.FriendListMessaging;
import friendlist.FriendLists;
import friendlist.FriendRank;
import io.ruin.HooksV2.Result;
import io.ruin.model.entity.player.Player;
import io.ruin.rsprot.RSProtService;
import net.rsprot.protocol.game.incoming.friendchat.FriendChatSetRank;
import net.rsprot.protocol.game.incoming.messaging.MessagePrivate;
import net.rsprot.protocol.game.incoming.social.FriendListAdd;
import net.rsprot.protocol.game.incoming.social.FriendListDel;
import net.rsprot.protocol.game.incoming.social.IgnoreListAdd;
import net.rsprot.protocol.game.incoming.social.IgnoreListDel;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

public class ProtocolHook implements RSProtService.Hook {

	public static void register() {
		RSProtService.hooks.register(RSProtService.Hook.Accept.class, ProtocolHook::handle);
	}

	private static Result handle(RSProtService.Hook.Accept ctx) {
		var handlers = ctx.handlers();

		handlers.addListener(FriendListAdd.class, new FriendListAddHandler());
		handlers.addListener(FriendListDel.class, new FriendListRemoveHandler());
		handlers.addListener(IgnoreListAdd.class, new IgnoreListAddHandler());
		handlers.addListener(IgnoreListDel.class, new IgnoreListRemoveHandler());
		handlers.addListener(FriendChatSetRank.class, new FriendListSetRankHandler());
		handlers.addListener(MessagePrivate.class, new FriendMessageHandler());
		return Result.Pass;
	}

	static class FriendMessageHandler implements MessageConsumer<Player, MessagePrivate> {

		@Override
		public void consume(Player p, MessagePrivate msg) {
			FriendListMessaging.sendMessage(p.getName(), msg.getName(), msg.getMessage());
		}
	}

	static class FriendListSetRankHandler implements MessageConsumer<Player, FriendChatSetRank> {

		@Override
		public void consume(Player p, FriendChatSetRank msg) {
			FriendLists.setRank(p, msg.getName(), FriendRank.byId(msg.getRank()));
		}
	}

	static class FriendListAddHandler implements MessageConsumer<Player, FriendListAdd> {

		@Override
		public void consume(Player p, FriendListAdd msg) {
			FriendLists.addFriend(p, msg.getName());
		}
	}

	static class FriendListRemoveHandler implements MessageConsumer<Player, FriendListDel> {

		@Override
		public void consume(Player p, FriendListDel msg) {
			FriendLists.removeFriend(p, msg.getName());
		}
	}

	static class IgnoreListAddHandler implements MessageConsumer<Player, IgnoreListAdd> {

		@Override
		public void consume(Player p, IgnoreListAdd msg) {
			FriendLists.addIgnore(p, msg.getName());
		}
	}

	static class IgnoreListRemoveHandler implements MessageConsumer<Player, IgnoreListDel> {

		@Override
		public void consume(Player p, IgnoreListDel msg) {
			FriendLists.removeIgnore(p, msg.getName());
		}
	}

}
