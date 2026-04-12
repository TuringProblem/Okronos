package io.ruin.model.item.actions.impl.scrolls.boosts;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;

public class DamageBoostScroll {
	private static final int DAMAGE_BOOST_SCROLL = 30456;

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
		ItemAction.registerInventory(DAMAGE_BOOST_SCROLL, "read", (player, item) -> {
			player.dialogue(new OptionsDialogue("Are you sure you want to activate your scroll?",
				new Option("Yes, activate my damage boost scroll!", () -> {
					item.remove(1);
					double timeAddition = 3000 * getScrollDonatorBoost(player);
					double timeAdditionMinutes = timeAddition / 100;
					int currentTime = player.damageBoostTimer.remaining();
					if (currentTime <= 0) currentTime = 0;
					player.damageBoostTimer.delay((int) (currentTime + timeAddition));
					int delay = player.damageBoostTimer.remaining();
					int minutes = delay / 100;
					player.sendMessage("You now have a damage boost for " + minutes + " minutes.");
					player.dialogue(new ItemDialogue().one(item.getId(), "You have activated your damage boost scroll. You " +
						"will get damage boosts for an additional " + (int) timeAdditionMinutes + " minutes."));
				}),
				new Option("No, I'm not ready yet!")));
		});
		LoginListener.register(player -> {
			if (player.damageBoostRemaining > 0)
				player.damageBoostTimer.delay(player.damageBoostRemaining);
			if (player.damageBoostTimer.remainingToMins() > 0)
				player.sendMessage("You currently have " + player.damageBoostTimer.remainingToMins() + " minutes of damage boost left.");
		});
	}
}
