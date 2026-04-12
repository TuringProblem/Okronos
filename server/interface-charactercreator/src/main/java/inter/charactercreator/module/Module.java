package inter.charactercreator.module;

import core.module.api.IModule;
import inter.charactercreator.CharacterCreator;

public class Module implements IModule {

	@Override
	public void start() {
		CharacterCreator.register();
	}
	
}
