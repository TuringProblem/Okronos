package io.ruin.services.http.models.store;

import java.util.List;

public class DonationIngameItems {

	public List<IngameItem> items;

	public static class IngameItem {
		public int itemId;
		public int amount;
	}

}
