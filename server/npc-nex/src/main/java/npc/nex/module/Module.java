package npc.nex.module;

import core.module.api.IModule;
import io.ruin.model.activities.bosses.globalboss.globalbosses.bloodreaper.BloodReaper;
import npc.nex.loc.AncientBarrier;
import npc.nex.loc.Scoreboard;
import npc.nex.loc.ZarosAltar;
import npc.nex.loc.ZarosDoors;
import npc.nex.module.hook.DoorHook;
import npc.nex.old.Nex;
import npc.nex.old.NexUtils;
import npc.nex.scripts.BloodReaverCombat;
import npc.nex.scripts.CruorCombat;
import npc.nex.scripts.FumusCombat;
import npc.nex.scripts.GlaciesCombat;
import npc.nex.scripts.NexCombat;
import npc.nex.scripts.UmbraCombat;
import npc.nex.utils.CombatUtils;

public class Module implements IModule {

	@Override
	public void start() {
		Nex.register();
		ZarosAltar.register();
		Scoreboard.register();
		AncientBarrier.register();
		CombatUtils.register();
		ZarosDoors.register();
		DoorHook.register();

		NexCombat.register();
		BloodReaverCombat.register();
		CruorCombat.register();
		FumusCombat.register();
		GlaciesCombat.register();
		UmbraCombat.register();

		NexUtils.register();
	}

}
