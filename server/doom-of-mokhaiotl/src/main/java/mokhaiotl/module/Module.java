package mokhaiotl.module;

import core.module.api.IModule;
import mokhaiotl.hook.MokhaiotlCommands;
import mokhaiotl.inter.Investigation;
import mokhaiotl.loc.BurrowHole;
import mokhaiotl.loc.Gap;
import mokhaiotl.loc.LootChest;
import mokhaiotl.loc.Scoreboard;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-28
 */
public class Module implements IModule {

	@Override
	public void start() {
		MokhaiotlCommands.register();

		// interface
		Investigation.register();

		// Objects
		Scoreboard.registerScoreboardActions();
		Gap.registerGap();
		BurrowHole.registerBurrowHole();
		LootChest.registerLootChest();

		// NPCs
	}
}
