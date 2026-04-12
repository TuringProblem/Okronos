package donationdeals;

import io.ruin.model.entity.player.Player;

public class DonationManager {
	protected static DailyDealHandler dailyDealHandler;
	protected static WeeklyDealHandler weeklyDealHandler;
	protected static SeasonDealHandler seasonDealHandler;

	public void loadDeals() {
		if (dailyDealHandler == null)
			dailyDealHandler = new DailyDealHandler();
		if (weeklyDealHandler == null)
			weeklyDealHandler = new WeeklyDealHandler();
		if (seasonDealHandler == null)
			seasonDealHandler = new SeasonDealHandler();

		dailyDealHandler.loadDealsFromJson("data/runtime/donation_deals/daily_deals.json");
		weeklyDealHandler.loadDealsFromJson("data/runtime/donation_deals/weekly_deals.json");
		seasonDealHandler.loadDealsFromJson("data/runtime/donation_deals/seasonal_deals.json");
	}

	public static void shutdown() {
		dailyDealHandler.shutdown();
		weeklyDealHandler.shutdown();
	}

}
