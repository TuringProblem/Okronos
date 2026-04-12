package gemstonecrab.module;

import core.module.api.IModule;
import gemstonecrab.GemstoneCrabCommands;
import gemstonecrab.Init;


public class Module implements IModule {

	@Override
	public void start() {
		GemstoneCrabCommands.register();
		Init.init();
	}
}
