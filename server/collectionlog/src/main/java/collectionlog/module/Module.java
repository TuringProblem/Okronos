package collectionlog.module;

import collectionlog.*;
import collectionlog.hook.*;
import core.module.api.IModule;

/**
 * Module
 */
public class Module implements IModule {

	@Override
	public void start() {
		Attributes.register();
		JournalTabHook.register();
		CollectionLogInterface.register();
		CollectionLogUpdated.register();
		PlayerHook.register();
		CommandHook.register();
	}
}
