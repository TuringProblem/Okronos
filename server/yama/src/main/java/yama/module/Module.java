package yama.module;

import core.module.api.IModule;
import yama.commands.YamaCommands;
import yama.npc.VoiceOfYama;
import yama.npc.YamaThrone;
import yama.objects.SteppingStones;

public class Module implements IModule {

	@Override
	public void start() {
		VoiceOfYama.register();
		YamaCommands.register();
		YamaThrone.register();
		SteppingStones.register();

	}
}
