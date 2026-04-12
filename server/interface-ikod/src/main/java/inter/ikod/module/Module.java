package inter.ikod.module;

import core.module.api.IModule;
import inter.ikod.IKOD;
import inter.ikod.hook.PlayerCombatHook;
import inter.ikod.hook.RiskProtectionHook;

public class Module implements IModule {

	@Override
	public void start() {
		PlayerCombatHook.register();
		RiskProtectionHook.register();

		IKOD.register();
	}

}
