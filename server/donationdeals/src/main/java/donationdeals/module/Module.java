package donationdeals.module;

import core.module.api.IModule;
import donationdeals.DailyDealHandler;
import donationdeals.DonationDealsInterface;
import donationdeals.DonationManager;
import donationdeals.hook.Commands;
import io.ruin.Server;

public class Module implements IModule {

	@Override
	public void start() {
		DonationDealsInterface.register();
		Commands.register();
		DonationManager donationManager = new DonationManager();
		donationManager.loadDeals();

		Server.hooks.registerSilent(Server.Hook.OnShutdown.class, (_ctx) -> {
			DonationManager.shutdown();
		});
	}
}
