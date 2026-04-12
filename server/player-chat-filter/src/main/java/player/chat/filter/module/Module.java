package player.chat.filter.module;

import core.module.api.IModule;
import player.chat.filter.hook.*;
import player.chat.filter.attribute.*;

public class Module implements IModule {

	@Override
	public void start() {
		PlayerHook.register();
		PrivacyHook.register();
		Attributes.register();
	}

}
