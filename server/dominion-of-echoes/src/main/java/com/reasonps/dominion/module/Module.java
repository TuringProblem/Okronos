package com.reasonps.dominion.module;

import com.reasonps.dominion.equipment.DrygoreBlowpipe;
import com.reasonps.dominion.hook.Commands;
import com.reasonps.dominion.hook.ObjectActions;
import com.reasonps.dominion.loot.SarcophagusObject;
import com.reasonps.dominion.npc.KeeperOfEchos;
import core.module.api.IModule;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class Module implements IModule {

	@Override
	public void start() {
		Commands.register();
		KeeperOfEchos.register();

		// Equipment
		DrygoreBlowpipe.register();

		// Objects
		SarcophagusObject.register();
		ObjectActions.register();
	}
}
