package io.ruin.model.item;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;

public class LightingStuff {
	/**
	 * All lit candles and lanterns // 32 lit black 33 lit normal  36 normal 38 black
	 */
	private static final int CANDLELIT = 33;
	private static final int CANDLEBLACKLIT = 32;
	private static final int LITBUG = 7053;

	/**
	 * All non lit candles and lanterns
	 */
	public static final int CANDLE = 36;
	public static final int BLACKCANDLE = 38;
	private static final int UNLITBUG = 7051;

	/**
	 * Tinderbox
	 */
	public static final int TINDERBOX = 590;

	private static void light(Player player, Item itemOne, int result) {
		itemOne.remove();
		player.getInventory().add(result, 1);
	}

	private static void extinguish(Player player, Item item, int resultOne) {
		item.remove();
		player.getInventory().add(resultOne, 1);
	}

	public static void register() {
		/**
		 * Light
		 */
		ItemItemAction.register(CANDLE, TINDERBOX, (player, primary, secondary) -> light(player, primary, CANDLELIT));
		ItemItemAction.register(BLACKCANDLE, TINDERBOX, (player, primary, secondary) -> light(player, primary, CANDLEBLACKLIT));
		ItemItemAction.register(UNLITBUG, TINDERBOX, (player, primary, secondary) -> light(player, primary, LITBUG));
		/**
		 * Extinguish
		 */
		ItemAction.registerInventory(CANDLEBLACKLIT, "extinguish", (player, item) -> extinguish(player, item, BLACKCANDLE));
		ItemAction.registerInventory(CANDLELIT, "extinguish", (player, item) -> extinguish(player, item, CANDLE));
		ItemAction.registerInventory(LITBUG, "extinguish", (player, item) -> extinguish(player, item, UNLITBUG));

	}
}
