package io.ruin.model.inter.handlers.tabsettings.module;

import core.module.api.IModule;
import io.ruin.model.inter.handlers.tabsettings.TabSettings;

public class Module implements IModule {

	@Override
	public void start() {
		TabSettings.register();
	}
}
