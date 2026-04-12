package io.ruin.model.item.actions.impl.scrolls.boosts;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;

public class BonusExpScroll {
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

	private static final int BONUS_EXP_SCROLL = 30460;

	public static void register() {
		ItemAction.registerInventory(BONUS_EXP_SCROLL, "read", (player, item) -> {
			player.dialogue(new OptionsDialogue("Are you sure you want to activate your scroll?",
				new Option("Yes, activate my experience boost scroll!", () -> {
					item.remove(1);
					int currentTime = player.doubleExpTimer.remaining();
					double timeAddition = 3000 * getScrollDonatorBoost(player);
					int timeAdditionMinutes = (int) (timeAddition / 100);
					if (currentTime < 0) currentTime = 0;
					player.doubleExpTimer.delay((int) (currentTime + timeAddition));
					int delay = player.doubleExpTimer.remaining();
					int minutes = delay / 100;
					player.sendMessage("You now have double experience for " + minutes + " minutes.");
					player.dialogue(new ItemDialogue().one(item.getId(), "You have activated your experience boost scroll. Your experience " +
						"will be boosted by 100% for an additional " + timeAdditionMinutes + " minutes."));
				}),
				new Option("No, I'm not ready yet!")));
		});
		LoginListener.register(player -> {
			if (player.doubleExpRemaining > 0)
				player.doubleExpTimer.delay(player.doubleExpRemaining);
			if (player.doubleExpTimer.remainingToMins() > 0)
				player.sendMessage("You currently have " + player.doubleExpTimer.remainingToMins() + " minutes of double exp left.");
		});
	}
}
