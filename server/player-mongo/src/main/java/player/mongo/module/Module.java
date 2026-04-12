package player.mongo.module;

import core.module.api.IModule;
import player.mongo.Connection;
import player.mongo.hook.HookCommand;
import player.mongo.hook.HookPlayer;
import properties.ServerProperties;

public class Module implements IModule {

	@Override
	public void start() {
		if (!ServerProperties.get("mongo_enabled", true)) {
			return;
		}

		HookPlayer.register();
		HookCommand.register();

		// just initialize connection on start
		Connection.get();
	}

}
