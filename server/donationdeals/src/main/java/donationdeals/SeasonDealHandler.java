package donationdeals;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.ruin.model.item.Item;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeasonDealHandler {

	public static class DealTier {
		public int amount;
		public List<Item> items = new ArrayList<>();
		public int tierOrder;

	}

	private List<DealTier> tiers = new ArrayList<>();

	public void loadDealsFromJson(String filePath) {
		try (FileReader reader = new FileReader(filePath)) {
			Gson gson = new Gson();
			Map<String, List<DealTier>> data = gson.fromJson(reader,
				new TypeToken<Map<String, List<DealTier>>>() {}.getType());
			tiers = data.get("tiers");

			if (tiers == null || tiers.isEmpty()) {
				System.err.println("No seasonal deal tiers found in JSON.");
				return;
			}
			for (int i = 0; i < tiers.size(); i++) {
				tiers.get(i).tierOrder = i + 1;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DealTier getTierForAmount(int donationAmount) {
		DealTier best = null;
		for (DealTier tier : tiers) {
			if (donationAmount >= tier.amount) {
				if (best == null || tier.amount > best.amount)
					best = tier;
			}
		}
		return best;
	}

	public int getAmountForNextTier(int currentDonationAmount) {
		for (DealTier tier : tiers) {
			if (tier.amount > currentDonationAmount) {
				return tier.amount - currentDonationAmount;
			}
		}
		return 0;
	}

	public List<DealTier> getTiers() {
		return new ArrayList<>(tiers); // returns a shallow copy
	}

}
