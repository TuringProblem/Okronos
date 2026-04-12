package tradepost.module;

import core.module.api.IModule;
import tradepost.TradePostProcess;
import tradepost.hook.Attributes;
import tradepost.hook.Commands;
import tradepost.hook.PlayerHook;
import tradepost.inter.TradePostInterface;
import tradepost.notify.TradePostNotifyDiscord;
import tradepost.notify.TradePostNotifyGame;

public class Module implements IModule {

	public static boolean ENABLED = true;

	@Override
	public void start() {
		Attributes.register();
		PlayerHook.register();
		TradePostInterface.register();
		TradePostProcess.register();
		TradePostNotifyGame.register();
		TradePostNotifyDiscord.register();
		Commands.register();
	}
}
