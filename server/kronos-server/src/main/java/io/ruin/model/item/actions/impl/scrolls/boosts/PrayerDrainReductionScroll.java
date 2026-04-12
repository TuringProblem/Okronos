package io.ruin.model.item.actions.impl.scrolls.boosts;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.utility.TickDelay;

public class PrayerDrainReductionScroll {
	private static final int PRAYER_DRAIN_REDUCTION_SCROLL = 30453;

	public static void register() {
		ItemAction.registerInventory(PRAYER_DRAIN_REDUCTION_SCROLL, "read", (player, item) -> {
			player.dialogue(new OptionsDialogue("Are you sure you want to activate your scroll?",
					new Option("Yes, activate my prayer reduction scroll!", () -> {
						item.remove(1);
						var timeAddition = defaultTimeAddition(player);
						var timeAdditionMinutes = (int) (timeAddition / 100);
						activate(player, timeAddition);
						var delay = player.prayerBoostTimer.remaining();
						var minutes = delay / 100;
						player.sendMessage("You now have prayer drain reduction for " + minutes + " minutes.");
						player.dialogue(
								new ItemDialogue().one(item.getId(), "You have activated your prayer drain reduction scroll. You " +
										"will get prayer reduction for an additional " + timeAdditionMinutes + " minutes."));
					}),
					new Option("No, I'm not ready yet!")));
		});
		LoginListener.register(player -> {
			if (player.prayerBoostBonusRemaining > 0) {
				player.prayerBoostTimer.delay(player.prayerBoostBonusRemaining);
			}

			if (player.prayerBoostTimer.remainingToMins() > 0) {
				var remMins = player.prayerBoostTimer.remainingToMins();
				player.sendMessage("You currently have " + remMins + " minutes of prayer reduction left.");
			}
		});
	}

	public static double getScrollDonatorBoost(Player player) {
		return BonusExpScroll.getScrollDonatorBoost(player);
	}

	public static void deactivate(Player player) {
		player.prayerBoostTimer = new TickDelay();
	}

	public static void activate(Player player) {
		activate(player, defaultTimeAddition(player));
	}

	public static void activate(Player player, double timeAddition) {
		int currentTime = player.prayerBoostTimer.remaining();
		if (currentTime <= 0) {
			currentTime = 0;
		}
		player.prayerBoostTimer.delay((int) (currentTime + timeAddition));
	}

	private static double defaultTimeAddition(Player player) {
		return 3000 * getScrollDonatorBoost(player);
	}
}
