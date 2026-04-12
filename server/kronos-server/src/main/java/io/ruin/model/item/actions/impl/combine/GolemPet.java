package io.ruin.model.item.actions.impl.combine;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;

public class GolemPet {
	/**
	 * Golem
	 */
	private static final int GOLEM = 13321;

	/**
	 * Ores
	 */
	private static final int TIN = 438;
	private static final int CLAY = 434;
	private static final int IRON = 440;
	private static final int BLURITE = 668;
	private static final int SILVER = 442;
	private static final int COAL = 453;
	private static final int GOLD = 444;
	private static final int MITHRIL = 447;
	private static final int ADAMANT = 449;
	private static final int RUNITE = 451;
	private static final int AMETHYST = 21347;
	private static final int LOVAKITE = 13356;
	private static final int DAEYALT = 24704;

	/**
	 * Colored Golems
	 */
	private static final int TIN_GOLEM = 21187;
	private static final int CLAY_GOLEM = 21188;
	private static final int IRON_GOLEM = 21189;
	private static final int BLURITE_GOLEM = 21190;
	private static final int SILVER_GOLEM = 21191;
	private static final int COAL_GOLEM = 21192;
	private static final int GOLD_GOLEM = 21193;
	private static final int MITHRIL_GOLEM = 21194;
	private static final int ADAMANT_GOLEM = 21196;
	private static final int RUNITE_GOLEM = 21197;
	private static final int AMETHYST_GOLEM = 21340;
	private static final int LOVAKITE_GOLEM = 21359;
	private static final int DAEYALT_GOLEM = 21360;

	private static void combine(Player player, Item itemOne, Item itemTwo, int result) {
		itemOne.remove();
		itemTwo.remove();
		player.getInventory().add(result, 1);
	}


	public static void register() {
		/**
		 * Combining
		 */
		ItemItemAction.register(GOLEM, TIN, (player, primary, secondary) -> combine(player, primary, secondary, TIN_GOLEM));
		ItemItemAction.register(GOLEM, CLAY, (player, primary, secondary) -> combine(player, primary, secondary, CLAY_GOLEM));
		ItemItemAction.register(GOLEM, IRON, (player, primary, secondary) -> combine(player, primary, secondary, IRON_GOLEM));
		ItemItemAction.register(GOLEM, BLURITE, (player, primary, secondary) -> combine(player, primary, secondary, BLURITE_GOLEM));
		ItemItemAction.register(GOLEM, COAL, (player, primary, secondary) -> combine(player, primary, secondary, COAL_GOLEM));
		ItemItemAction.register(GOLEM, SILVER, (player, primary, secondary) -> combine(player, primary, secondary, SILVER_GOLEM));
		ItemItemAction.register(GOLEM, GOLD, (player, primary, secondary) -> combine(player, primary, secondary, GOLD_GOLEM));
		ItemItemAction.register(GOLEM, MITHRIL, (player, primary, secondary) -> combine(player, primary, secondary, MITHRIL_GOLEM));
		ItemItemAction.register(GOLEM, ADAMANT, (player, primary, secondary) -> combine(player, primary, secondary, ADAMANT_GOLEM));
		ItemItemAction.register(GOLEM, RUNITE, (player, primary, secondary) -> combine(player, primary, secondary, RUNITE_GOLEM));
		ItemItemAction.register(GOLEM, AMETHYST, (player, primary, secondary) -> combine(player, primary, secondary, AMETHYST_GOLEM));
		ItemItemAction.register(GOLEM, LOVAKITE, (player, primary, secondary) -> combine(player, primary, secondary, LOVAKITE_GOLEM));
		ItemItemAction.register(GOLEM, DAEYALT, (player, primary, secondary) -> combine(player, primary, secondary, DAEYALT_GOLEM));
	}
}
