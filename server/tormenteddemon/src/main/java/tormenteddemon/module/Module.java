package tormenteddemon.module;
import core.module.api.IModule;
import tormenteddemon.combat.util.TormentedCommands;

public class Module implements IModule {

	@Override
	public void start() {
		TormentedCommands.register();

	}
}
