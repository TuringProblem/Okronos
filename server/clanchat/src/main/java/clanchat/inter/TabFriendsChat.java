package clanchat.inter;

import clanchat.FriendChatDb;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;

import static core.task.api.API.*;

public class TabFriendsChat {

	public static void register() {
		InterfaceHandler.register(7, h -> h.actions[20] = (SimpleAction) p -> {
			p.openInterface(ToplevelComponent.MAINMODAL, Interface.CLAN_CHAT_SETTINGS);
			queue(() -> {
				var it = FriendChatDb.db().load(p.getName()).await();
				FriendChatSettingsInterface.updateView(p, it);
			});

		});
	}

}
