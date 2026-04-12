package io.ruin.model.item.actions.impl.storage;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;

public class GodBooks {

	private static final int DAMAGED_SARA_BOOK = 3839;
	private static final int DAMAGED_ZAMMY_BOOK = 3841;
	private static final int DAMAGED_GUTHIX_BOOK = 3843;
	private static final int DAMAGED_BANDOS_BOOK = 12607;
	private static final int DAMAGED_ARMADYL_BOOK = 12609;
	private static final int DAMAGED_ANCIENT_BOOK = 12611;
	private static final int SARA_PAGE_1 = 3827;
	private static final int SARA_PAGE_2 = 3828;
	private static final int SARA_PAGE_3 = 3829;
	private static final int SARA_PAGE_4 = 3830;
	private static final int ZAMORAK_PAGE_1 = 3831;
	private static final int ZAMORAK_PAGE_2 = 3832;
	private static final int ZAMORAK_PAGE_3 = 3833;
	private static final int ZAMORAK_PAGE_4 = 3834;
	private static final int GUTHIX_PAGE_1 = 3835;
	private static final int GUTHIX_PAGE_2 = 3836;
	private static final int GUTHIX_PAGE_3 = 3837;
	private static final int GUTHIX_PAGE_4 = 3838;
	private static final int BANDOS_PAGE_1 = 12613;
	private static final int BANDOS_PAGE_2 = 12614;
	private static final int BANDOS_PAGE_3 = 12615;
	private static final int BANDOS_PAGE_4 = 12616;
	private static final int ARMADYL_PAGE_1 = 12617;
	private static final int ARMADYL_PAGE_2 = 12618;
	private static final int ARMADYL_PAGE_3 = 12619;
	private static final int ARMADYL_PAGE_4 = 12620;
	private static final int ANCIENT_PAGE_1 = 12621;
	private static final int ANCIENT_PAGE_2 = 12622;
	private static final int ANCIENT_PAGE_3 = 12623;
	private static final int ANCIENT_PAGE_4 = 12624;
	private static final int COMPLETED_SARA_BOOK = 3840;
	private static final int COMPLETED_ZAMMY_BOOK = 3842;
	private static final int COMPLETED_GUTHIX_BOOK = 3844;
	private static final int COMPLETED_BANDOS_BOOK = 12608;
	private static final int COMPLETED_ARMADYL_BOOK = 12610;
	private static final int COMPLETED_ANCIENT_BOOK = 12612;

	private static final int[] ALL_ZAMMY_PAGES = {ZAMORAK_PAGE_1, ZAMORAK_PAGE_2, ZAMORAK_PAGE_3, ZAMORAK_PAGE_4};
	private static final int[] ALL_SARA_PAGES = {SARA_PAGE_1, SARA_PAGE_2, SARA_PAGE_3, SARA_PAGE_4};
	private static final int[] ALL_GUTHIX_PAGES = {GUTHIX_PAGE_1, GUTHIX_PAGE_2, GUTHIX_PAGE_3, GUTHIX_PAGE_4};
	private static final int[] ALL_BANDOS_PAGES = {BANDOS_PAGE_1, BANDOS_PAGE_2, BANDOS_PAGE_3, BANDOS_PAGE_4};
	private static final int[] ALL_ARMADYL_PAGES = {ARMADYL_PAGE_1, ARMADYL_PAGE_2, ARMADYL_PAGE_3, ARMADYL_PAGE_4};
	private static final int[] ALL_ANCIENT_PAGES = {ANCIENT_PAGE_1, ANCIENT_PAGE_2, ANCIENT_PAGE_3, ANCIENT_PAGE_4};

	public static void register() {
		ItemItemAction.register(DAMAGED_SARA_BOOK, GodBooks::check);
		ItemItemAction.register(DAMAGED_ZAMMY_BOOK, GodBooks::check);
		ItemItemAction.register(DAMAGED_GUTHIX_BOOK, GodBooks::check);
		ItemItemAction.register(DAMAGED_BANDOS_BOOK, GodBooks::check);
		ItemItemAction.register(DAMAGED_ARMADYL_BOOK, GodBooks::check);
		ItemItemAction.register(DAMAGED_ANCIENT_BOOK, GodBooks::check);
	}

	private static void check(Player player, Item damagedBook, Item primary) {
		// Determine which set of pages the book belongs to
		int[] allPages = null;
		int completedBookId = -1;

		if (damagedBook.getId() == DAMAGED_ZAMMY_BOOK) {
			allPages = ALL_ZAMMY_PAGES;
			completedBookId = COMPLETED_ZAMMY_BOOK;
		} else if (damagedBook.getId() == DAMAGED_SARA_BOOK) {
			allPages = ALL_SARA_PAGES;
			completedBookId = COMPLETED_SARA_BOOK;
		} else if (damagedBook.getId() == DAMAGED_GUTHIX_BOOK) {
			allPages = ALL_GUTHIX_PAGES;
			completedBookId = COMPLETED_GUTHIX_BOOK;
		} else if (damagedBook.getId() == DAMAGED_BANDOS_BOOK) {
			allPages = ALL_BANDOS_PAGES;
			completedBookId = COMPLETED_BANDOS_BOOK;
		} else if (damagedBook.getId() == DAMAGED_ARMADYL_BOOK) {
			allPages = ALL_ARMADYL_PAGES;
			completedBookId = COMPLETED_ARMADYL_BOOK;
		} else if (damagedBook.getId() == DAMAGED_ANCIENT_BOOK) {
			allPages = ALL_ANCIENT_PAGES;
			completedBookId = COMPLETED_ANCIENT_BOOK;
		}

		if (allPages == null) {
			// Invalid book ID
			return;
		}

		// Check if the player has all pages
		boolean hasAllPages = true;
		for (int pageId : allPages) {
			if (!player.getInventory().contains(pageId)) {
				hasAllPages = false;
				break;
			}
		}

		if (hasAllPages) {
			// Remove the pages from the inventory
			for (int pageId : allPages) {
				player.getInventory().remove(pageId, 1);
			}

			// Convert the damaged book to the completed book
			damagedBook.setId(completedBookId);
			player.sendMessage("You combine all of the pages in your inventory to complete the god book.");
		} else {
			player.sendMessage("You must have all of the pages for this book in your inventory to complete it.");
		}
	}
}
