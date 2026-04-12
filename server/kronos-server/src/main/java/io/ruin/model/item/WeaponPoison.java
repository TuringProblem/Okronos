package io.ruin.model.item;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.ItemItemAction;

public class WeaponPoison {
	/**
	 * All poisons
	 */
	private static final int WEAPON_POISON_15235 = 15235;
	private static final int WEAPON_POISON_15236 = 15236;

	/**
	 * All non poison Variants
	 */
	public static final int DRAGON_DAGGER = 1215;
	public static final int DRAGON_SPEAR = 1249;


	/**
	 * All poison Variants
	 */
	public static final int DRAGON_DAGGERP_5680 = 5680;
	public static final int DRAGON_DAGGERP_5698 = 5698;
	public static final int DRAGON_SPEARP_5716 = 5716;
	public static final int DRAGON_SPEARP_5730 = 5730;


	private static void combine(Player player, Item itemOne, Item itemTwo, int result) {
		itemOne.remove();
		itemTwo.remove();
		player.getInventory().add(result, 1);
	}

	public static void register() {
		ItemItemAction.register(WEAPON_POISON_15235, DRAGON_DAGGER, (player, primary, secondary) -> combine(player, primary, secondary, DRAGON_DAGGERP_5680));
		ItemItemAction.register(WEAPON_POISON_15235, DRAGON_SPEAR, (player, primary, secondary) -> combine(player, primary, secondary, DRAGON_SPEARP_5716));
		ItemItemAction.register(WEAPON_POISON_15236, DRAGON_DAGGER, (player, primary, secondary) -> combine(player, primary, secondary, DRAGON_DAGGERP_5698));
		ItemItemAction.register(WEAPON_POISON_15236, DRAGON_SPEAR, (player, primary, secondary) -> combine(player, primary, secondary, DRAGON_SPEARP_5730));
	}
}
