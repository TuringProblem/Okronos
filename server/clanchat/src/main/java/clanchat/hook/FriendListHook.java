package clanchat.hook;

import clanchat.FriendChatDb;
import clanchat.FriendChatUpdater;
import friendlist.*;
import friendlist.FriendList.Hook;
import io.ruin.HooksV2.Result;
import io.ruin.model.World;

import static core.task.api.API.*;

public class FriendListHook {

	public static void register() {
		FriendList.hooks.register(Hook.OnFriendAdded.class, FriendListHook::onFriendAdded);
		FriendList.hooks.register(Hook.OnFriendRemoved.class, FriendListHook::onFriendRemoved);
		FriendList.hooks.register(Hook.OnFriendRankChanged.class, FriendListHook::onFriendRankChanged);
	}

	public static Result onFriendAdded(Hook.OnFriendAdded ctx) {
		updateFriendChat(ctx.list().owner, ctx.friend().username);
		return Result.Pass;
	}

	private static Result onFriendRemoved(FriendList.Hook.OnFriendRemoved ctx) {
		updateFriendChat(ctx.list().owner, ctx.friend().username);
		return Result.Pass;
	}

	private static Result onFriendRankChanged(FriendList.Hook.OnFriendRankChanged ctx) {
		updateFriendChat(ctx.list().owner, ctx.friend().username);
		return Result.Pass;
	}

	private static void updateFriendChat(String chatOwner, String memberUsername) {
		var friendPlayer = World.getPlayer(memberUsername);
		if (friendPlayer == null) {
			return;
		}

		queue(() -> {
			var chat = FriendChatDb.db().load(chatOwner).await();
			FriendChatUpdater.notifyMemberUpdated(memberUsername, chat);
		});
	}
}
