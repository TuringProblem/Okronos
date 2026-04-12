package io.ruin.model.inter.handlers.advancedsettings.module;

import core.module.api.IModule;
import io.ruin.model.inter.handlers.advancedsettings.SettingsInterface;

public class Module implements IModule {
	@Override
	public void start() {
		SettingsInterface.register();
	}
}
