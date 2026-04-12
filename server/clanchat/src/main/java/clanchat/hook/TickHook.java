package clanchat.hook;

import clanchat.FriendChatDb;
import io.ruin.HooksV2.Result;
import io.ruin.process.CoreWorker;

public class TickHook {

	public static void register() {
		CoreWorker.hooks.register(CoreWorker.Hook.PostUpdate.class, TickHook::handle);
	}

	private static Result handle(CoreWorker.Hook.PostUpdate ctx) {
		if (ctx.tick() % 50 == 0) {
			FriendChatDb.db().save();
		}
		return Result.Pass;
	}
}
