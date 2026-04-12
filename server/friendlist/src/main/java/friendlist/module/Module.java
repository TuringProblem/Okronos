package friendlist.module;

import core.module.api.IModule;
import friendlist.hook.PlayerHook;
import friendlist.hook.PrivacyHook;
import friendlist.hook.ProtocolHook;
import friendlist.hook.TickHook;
import friendlist.legacy.LegacyMigrationProcess;

public class Module implements IModule {

	@Override
	public void init() {
		ProtocolHook.register();
	}

	@Override
	public void start() {
		LegacyMigrationProcess.start();
		PlayerHook.register();
		PrivacyHook.register();
		TickHook.register();
	}

}
