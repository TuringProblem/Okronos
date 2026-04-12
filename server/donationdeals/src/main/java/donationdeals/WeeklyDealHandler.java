package donationdeals;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import io.ruin.api.utils.ExecutorUtils;
import io.ruin.cache.ItemID;
import io.ruin.model.item.Item;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WeeklyDealHandler {
	protected String weeklyDealTitle;
	protected Item weeklyDealItem;

	private List<DealJson> deals;
	private int currentIndex = 0;
	private Instant dealExpirationTime;
	private final ScheduledExecutorService scheduler;

	private static class DealJson {
		String title;
		Item item;
	}

	public WeeklyDealHandler() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
	}

	public void loadDealsFromJson(String filePath) {
		try (FileReader reader = new FileReader(filePath)) {
			Gson gson = new Gson();
			Map<String, JsonElement> data = gson.fromJson(reader, new TypeToken<Map<String, JsonElement>>() {}.getType());

			JsonElement expirationElement = data.get("dealExpirationTime");
			if (expirationElement == null || expirationElement.isJsonNull()) {
				setNewDealExpirationTime();
			} else {
				dealExpirationTime = Instant.ofEpochSecond(expirationElement.getAsLong());
			}

			JsonElement dealsElement = data.get("deals");
			if (dealsElement == null || dealsElement.isJsonNull()) {
				createNewPromoDeal();
			} else {
				deals = gson.fromJson(dealsElement, new TypeToken<List<DealJson>>() {}.getType());
				if (deals == null || deals.isEmpty()) {
					createNewPromoDeal();
				}
			}

			currentIndex = 0;
			applyCurrentDeal();

			if (Instant.now().isAfter(dealExpirationTime)) {
				refreshDeal();
			} else {
				scheduleDealExpirationEvent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveDealsToJson(String filePath) {
		try (FileWriter writer = new FileWriter(filePath)) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Map<String, Object> data = Map.of(
				"deals", deals,
				"dealExpirationTime", dealExpirationTime.getEpochSecond()
			);
			gson.toJson(data, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void scheduleDealExpirationEvent() {
		Instant now = Instant.now();
		long remainingTime = dealExpirationTime.getEpochSecond() - now.getEpochSecond();

		if (remainingTime > 0) {
			scheduler.schedule(this::refreshDeal, remainingTime + 5, TimeUnit.SECONDS);
		}
	}

	public void refreshDeal() {
		Instant now = Instant.now();
		if (deals == null || deals.isEmpty()) return;

		if (now.isAfter(dealExpirationTime)) {
			currentIndex = (currentIndex + 1) % deals.size();
			setNewDealExpirationTime();
			applyCurrentDeal();
			saveDealsToJson("data/runtime/donation_deals/weekly_deals.json");

			scheduleDealExpirationEvent();
		}
	}

	private void setNewDealExpirationTime() {
		dealExpirationTime = Instant.now().plus(7, ChronoUnit.SECONDS);
		saveDealsToJson("data/runtime/donation_deals/weekly_deals.json");
	}

	private void createNewPromoDeal() {
		DealJson newDeal = new DealJson();
		newDeal.title = "Summer Box";
		newDeal.item = new Item(ItemID.SUMMER_MYSTERY_BOX);

		deals = List.of(newDeal);
		currentIndex = 0;
		setNewDealExpirationTime();
	}

	private void applyCurrentDeal() {
		DealJson current = deals.get(currentIndex);
		this.weeklyDealTitle = current.title;
		this.weeklyDealItem = current.item;
	}

	public String getTimeRemainingString() {
		Instant now = Instant.now();
		long remaining = dealExpirationTime.getEpochSecond() - now.getEpochSecond();

		if (remaining <= 0) {
			return "00:00:00";
		}

		long hours = (remaining / 3600) % 24;
		long minutes = (remaining / 60) % 60;
		long seconds = remaining % 60;

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	public void shutdown() {
		ExecutorUtils.shutdown(this.scheduler);
	}
}
