package royaltitans.module;

import core.module.api.IModule;
import royaltitans.hook.*;

public class Module implements IModule {
	@Override
	public void start() {
		Commands.register();
	}
}
