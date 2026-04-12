package io.ruin.model.skills.events;

import io.ruin.api.utils.Random;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.stat.StatType;

import java.util.Arrays;
import java.util.List;

public class GlobalMethods {

	static List<Integer> DWARF = Arrays.asList(7712, 7713, 7716, 7714, 7715, 7721);
	public static int GUILD_MASTER_WC = 7236;

	/**
	 * Shared constants
	 */

	public static int platinum_id = 13204;

	/**
	 * Mining ID's
	 */

	public static int runite_note = 452;
	public static int adamantite_note = 450;
	public static int mithril_note = 448;

	/**
	 * Woodcutting ID's
	 */

	public static int redwood_note = 19670;
	public static int magic_log_note = 1514;
	public static int mahogany_note = 8836;
	public static int teak_note = 6334;
	public static int oak_note = 1522;
	public static int logs_note = 1512;

	private static int[] plank_logs = new int[]{logs_note, oak_note, teak_note, mahogany_note};

	private static Random rand = new Random();

//    public static int getRandomPlankLog() {
//        return plank_logs[rand.nextInt(plank_logs.length)];
//    }


	public static void handleRewards(Player player, NPC npc, int amount) {
		int hi_lvl_reward_amount = 1000 / 5;
		int med_lvl_reward_amount = 1000 / 4;
		int low_lvl_reward_amount = 1000 / 3;
		int platinum_amount = 1000;
		int experience = 1000 * 30;
		/**
		 * Crystal Trees
		 */
		if (npc.getId() == GUILD_MASTER_WC) {
			player.getStats().addXp(StatType.Woodcutting, experience, true);
			for (int i = amount / 1000; i > 0; i--) {
				player.getInventory().addOrDrop(redwood_note, hi_lvl_reward_amount);
				player.getInventory().addOrDrop(magic_log_note, med_lvl_reward_amount);
				player.getInventory().addOrDrop(platinum_id, platinum_amount);
				if (io.ruin.api.utils.Random.rollDie(128, 1)) {
					if (io.ruin.api.utils.Random.rollDie(2, 1)) {
						player.getInventory().addOrDrop(23673, 1);
					} else {
						player.getInventory().addOrDrop(4670, 1);
						return;
					}
				}
				return;
			}
			return;
			/**
			 * Shooting stars
			 */
		} else if (DWARF.contains(npc.getId())) {
			player.getStats().addXp(StatType.Mining, experience, true);
			for (int i = amount / 1000; i > 0; i--) {
				player.getInventory().addOrDrop(runite_note, hi_lvl_reward_amount);
				player.getInventory().addOrDrop(adamantite_note, med_lvl_reward_amount);
				player.getInventory().addOrDrop(mithril_note, low_lvl_reward_amount);
				player.getInventory().addOrDrop(platinum_id, platinum_amount);
				if (io.ruin.api.utils.Random.rollDie(128, 1)) {
					if (io.ruin.api.utils.Random.rollDie(2, 1)) {
						player.getInventory().addOrDrop(23680, 1);
					} else {
						player.getInventory().addOrDrop(4670, 1);
						return;
					}
					return;
				}
			}
			return;
		}
	}

	public static Item getCurrencyType(NPC npc) {
		if (npc.getId() == GUILD_MASTER_WC) {
			return new Item(22207);
		} else if (DWARF.contains(npc.getId())) {
			return new Item(25527);
		}
		return null;
	}

	public static void exchangeCurrency(Player player, NPC npc) {
		Item currency = getCurrencyType(npc);
		int player_amount = player.getInventory().getAmount(currency.getId());
		if (player_amount < 1000) {
			player.dialogue(new NPCDialogue(npc, "It appears you don't have enough of " + currency.getDef().name.toLowerCase() + " for me to do anything with."));
			return;
		} else if (player_amount > 100_000) {
			player.dialogue(new NPCDialogue(npc, "You can only exchange 100,000 of " + currency.getDef().name.toLowerCase() + " at a time."),
				new PlayerDialogue("Okay thanks!"));
			return;
		}
		player.dialogue(
			new OptionsDialogue("Exchange your " + currency.getDef().name.toLowerCase() + "?",
				new Option("Yes please!", () -> {
					int currencyAmount = player_amount / 1000;
					int removeAmount = currencyAmount * 1000;
					player.getInventory().findItem(currency.getId()).remove(removeAmount);
					handleRewards(player, npc, removeAmount);
				}),
				new Option("Nevermind", player::closeDialogue)
			)
		);
	}
}

