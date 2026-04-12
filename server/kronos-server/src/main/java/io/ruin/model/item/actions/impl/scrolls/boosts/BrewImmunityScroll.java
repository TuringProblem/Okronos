package io.ruin.model.item.actions.impl.scrolls.boosts;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;

public class BrewImmunityScroll {
	private static final int BREW_IMMUNITY_SCROLL = 30455;

	public static double getScrollDonatorBoost(Player player) {
		switch (player.getSecondaryGroup()) {
			case DONATOR -> {
				return 1.05;
			}
			case SUPER_DONATOR -> {
				return 1.10;
			}
			case ELITE_DONATOR -> {
				return 1.12;
			}
			case NOBLE_DONATOR -> {
				return 1.15;
			}
			case GOLD_DONATOR -> {
				return 1.2;
			}
			case PLATINUM_DONATOR -> {
				return 1.30;
			}
			case LEGENDARY_DONATOR -> {
				return 1.40;
			}
			case SUPREME_DONATOR -> {
				return 1.50;
			}
		}
		return 1;
	}

	public static void register() {
		ItemAction.registerInventory(BREW_IMMUNITY_SCROLL, "read", (player, item) -> {
			player.dialogue(new OptionsDialogue("Are you sure you want to activate your scroll?",
				new Option("Yes, activate my brew immunity scroll!", () -> {
					item.remove(1);
					double timeAddition = 3000 * getScrollDonatorBoost(player);
					int timeAdditionMinutes = (int) (timeAddition / 100);
					int currentTime = player.brewImmunityTimer.remaining();
					if (currentTime <= 0) currentTime = 0;
					player.brewImmunityTimer.delay((int) (currentTime + timeAddition));
					int delay = player.brewImmunityTimer.remaining();
					int minutes = delay / 100;
					player.sendMessage("You now have a brew immunity for " + minutes + " minutes.");
					player.dialogue(new ItemDialogue().one(item.getId(), "You have activated your brew immunity scroll. You " +
						"will now have brew immunity for an additional " + timeAdditionMinutes + " minutes."));
				}),
				new Option("No, I'm not ready yet!")));
		});
		ItemAction.registerInventory(608, "Activate", (player, item) -> {
			player.dialogue(new OptionsDialogue("Are you sure you want to activate your scroll?",
				new Option("Yes, activate my 5% drop rate scroll!", () -> {
					item.remove(1);
					int currentTime = player.dropRateBoostTimer.remaining();
					double timeAddition = 6000 * getScrollDonatorBoost(player);
					int timeAdditionMinutes = (int) (timeAddition / 100);
					if (currentTime <= 0) currentTime = 0;
					player.dropRateBoostTimer.delay((int) (currentTime + timeAddition));
					int delay = player.dropRateBoostTimer.remaining();
					int minutes = delay / 100;
					player.sendMessage("You now have a 5% drop rate boost for " + minutes + " minutes.");
					player.dialogue(new ItemDialogue().one(item.getId(), "You have activated your drop rate scroll. You " +
						"will now have a boosted drop rate for an additional " + timeAdditionMinutes + " minutes."));
				}),
				new Option("No, I'm not ready yet!")));
		});
		LoginListener.register(player -> {
			if (player.brewImmunityRemaining > 0)
				player.brewImmunityTimer.delay(player.brewImmunityRemaining);
			if (player.dropRateBoostRemaining > 0)
				player.dropRateBoostTimer.delay(player.dropRateBoostRemaining);
			if (player.brewImmunityTimer.remainingToMins() > 0)
				player.sendMessage("You currently have " + player.brewImmunityTimer.remainingToMins() + " minutes of brew immunity left.");
			if (player.dropRateBoostTimer.remainingToMins() > 0)
				player.sendMessage("You currently have " + player.dropRateBoostTimer.remainingToMins() + " minutes of boosted drop rates remaining.");
		});
	}
}
