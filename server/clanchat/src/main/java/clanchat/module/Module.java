package clanchat.module;

import clanchat.Attributes;
import clanchat.hook.ChatHook;
import clanchat.hook.FriendListHook;
import clanchat.hook.PlayerHook;
import clanchat.hook.ProtocolHook;
import clanchat.hook.TickHook;
import clanchat.inter.FriendChatSettingsInterface;
import clanchat.inter.TabFriendsChat;
import core.module.api.IModule;

public class Module implements IModule {
	@Override
	public void init() {
		ProtocolHook.register();
	}

	@Override
	public void start() {
		Attributes.register();
		FriendChatSettingsInterface.register();
		TabFriendsChat.register();
		PlayerHook.register();
		FriendListHook.register();
		ChatHook.register();
		TickHook.register();
	}
}
