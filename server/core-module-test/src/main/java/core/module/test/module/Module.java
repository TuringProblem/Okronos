package core.module.test.module;

import core.module.api.IModule;

import java.util.ServiceLoader.Provider;

public class Module implements IModule, Provider<IModule> {

	@Override
	public void start() {
	}

	void tick() {
	}

	@Override
	public Class<? extends IModule> type() {
		return Module.class;
	}

	@Override
	public IModule get() {
		return new Module();
	}
}
