package net.runelite.client.plugins.toa.module;

import net.runelite.client.plugins.toa.TombsOfAmascutConfig;
import net.runelite.client.plugins.toa.util.RaidState;

public interface PluginLifecycleComponent
{

	default boolean isEnabled(TombsOfAmascutConfig config, RaidState raidState)
	{
		return true;
	}

	void startUp();

	void shutDown();

}
