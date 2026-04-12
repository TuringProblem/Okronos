package io.ruin.model.activities.nightmarezone;

import io.ruin.api.utils.NumberUtils;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.handlers.OptionScroll;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.stat.StatType;

public enum ScrollOfRedirection {

	RIMMINGTON(11741, 0),
	TAVERLEY(11742, 10),
	POLLNIVNEACH(11743, 20),
	HOSIDIUS(19651, 25),
	RELLEKA(11744, 30),
	BRIMHAVEN(11745, 40),
	YANILLE(11746, 50),
	PRIFDDINAS(23771, 70),
	TROLLHEIM(11747, 0);

	public final int id, req;

	ScrollOfRedirection(int id, int req) {
		this.id = id;
		this.req = req;
	}

	private static final int SCROLL_OF_REDIRECT = 11740;
	private static final int HOUSE_TABLET = 8013;

	private static void convertTabletOptions(Player p) {
		OptionScroll.open(p, "House Tablets", false,
			new Option("Rimmington - no requirement", () -> convertTablet(p, RIMMINGTON.id, RIMMINGTON.req)),
			new Option("Taverley - level 10 Construction", () -> convertTablet(p, TAVERLEY.id, TAVERLEY.req)),
			new Option("Pollnivneach - level 20 Construction", () -> convertTablet(p, POLLNIVNEACH.id, POLLNIVNEACH.req)),
			new Option("Hosidius - level 25 Construction", () -> convertTablet(p, HOSIDIUS.id, HOSIDIUS.req)),
			new Option("Rellekka - level 30 Construction", () -> convertTablet(p, RELLEKA.id, RELLEKA.req)),
			new Option("Brimhaven - level 40 Construction", () -> convertTablet(p, BRIMHAVEN.id, BRIMHAVEN.req)),
			new Option("Yanille - level 50 Construction", () -> convertTablet(p, YANILLE.id, YANILLE.req)),
			new Option("Prifddinas - level 70 Construction", () -> convertTablet(p, PRIFDDINAS.id, PRIFDDINAS.req)),
			new Option("Trollheim - no requirement", () -> convertTablet(p, TROLLHEIM.id, TROLLHEIM.req))
		);
	}

	private static void convertTablet(Player p, int tablet, int levelRequirement) {
		if (!p.getStats().check(StatType.Construction, levelRequirement)) {
			p.sendMessage("You don't have the required level to make that tablet.");
			return;
		}
		p.dialogue(new OptionsDialogue("How many would you like to redirect?",
			new Option("1", () -> convert(p, tablet, 1)),
			new Option("All", () -> convert(p, tablet, p.getInventory().getAmount(SCROLL_OF_REDIRECT))),
			new Option("X", () -> p.integerInput("Enter amount:", amt -> convert(p, tablet, amt))),
			new Option("(Cancel)", Player::closeDialogue)

		));
		return;
	}

	private static void convert(Player p, int tablet, int amount) {
		if (!p.getInventory().contains(tablet) && !p.getInventory().hasFreeSlots(1)) {
			p.sendMessage("You need at least one free inventory space to do this.");
			return;
		}
		int houseTabs = p.getInventory().getAmount(HOUSE_TABLET);
		int scrollRedirects = p.getInventory().getAmount(SCROLL_OF_REDIRECT);
		int maxAmount = scrollRedirects > houseTabs ? houseTabs : scrollRedirects;

		if (amount > maxAmount)
			amount = maxAmount;


		p.getInventory().remove(HOUSE_TABLET, amount);
		p.getInventory().remove(SCROLL_OF_REDIRECT, amount);
		p.getInventory().add(tablet, amount);
		p.dialogue(new ItemDialogue().one(tablet, "You redirect your tablet."));
	}

	private void revertOption(Player p, Item tab) {

		switch (tab.getAmount()) {
			case 1:
				p.dialogue(
					new OptionsDialogue("Revert this tablet?",
						new Option("Yes ~ you will not get your scroll back.", () -> revert(p, tab, 1)),
						new Option("No ~ leave it alone.", Player::closeDialogue)
					)
				);
				break;
			case 2:
				p.dialogue(
					new OptionsDialogue("Revert these tablets?",
						new Option("Revert BOTH ~ you will not get your scrolls back.", () -> revert(p, tab, 2)),
						new Option("Revert ONE ~ you will not get your scroll back.", () -> revert(p, tab, 1)),
						new Option("No ~ leave it alone.", Player::closeDialogue)
					)
				);
				break;
			default:
				p.dialogue(
					new OptionsDialogue("Revert these tablets?",
						new Option("Revert ALL ~ you will not get your scrolls back.", () -> revert(p, tab, tab.getAmount())),
						new Option("Revert SOME ~ you will not get your scrolls back.", () -> p.integerInput("How many do you wish to revert? " + tab.getAmount(), amt -> revert(p, tab, amt))),
						new Option("No ~ leave it alone.", Player::closeDialogue)
					)
				);
				break;
		}

	}

	private void revert(Player p, Item tab, int amount) {
		if (!p.getInventory().contains(HOUSE_TABLET) && !p.getInventory().hasFreeSlots(1)) {
			p.sendMessage("You don't have enough inventory space to do this.");
			return;
		}
		int maxAmt = tab.getAmount();

		if (amount > maxAmt)
			amount = maxAmt;

		p.getInventory().remove(tab.getId(), amount);
		p.getInventory().add(HOUSE_TABLET, amount);
		p.dialogue(new ItemDialogue().one(HOUSE_TABLET, "You revert the tablets to their original form."));
	}

	private static void refund(Player p, Item redirect) {
		p.dialogue(
			new OptionsDialogue("Exchange " + redirect.getAmount() + " for " + NumberUtils.formatNumber(775 * redirect.getAmount()) + " points?",
				new Option("Yes", () -> {
					p.getInventory().remove(redirect.getId(), redirect.getAmount());
					PlayerCounter.NMZ_REWARD_POINTS.increment(p, 775 * redirect.getAmount());
					p.dialogue(new ItemDialogue().one(SCROLL_OF_REDIRECT, "New rewards points total: " + NumberUtils.formatNumber(PlayerCounter.NMZ_REWARD_POINTS.get(p))));
				}),
				new Option("No", Player::closeDialogue)
			));
	}

	public static void register() {
		ItemItemAction.register(HOUSE_TABLET, SCROLL_OF_REDIRECT, ((player, primary, secondary) -> convertTabletOptions(player)));
		ItemAction.registerInventory(SCROLL_OF_REDIRECT, "refund", ((player, item) -> refund(player, item)));
		for (ScrollOfRedirection tab : values())
			ItemAction.registerInventory(tab.id, "revert", tab::revertOption);
	}
}
