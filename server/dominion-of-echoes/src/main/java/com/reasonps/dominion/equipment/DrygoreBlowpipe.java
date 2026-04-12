package com.reasonps.dominion.equipment;

import io.ruin.cache.ObjType;
import io.ruin.model.combat.RangedData;
import io.ruin.model.combat.RangedWeapon;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.handlers.EquipmentStats;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-31
 */
public class DrygoreBlowpipe {
	private static final int MAX_AMOUNT = 0x3fff;

	public static final int DRYGORE_BLOWPIPE_UNCHARGED = 30373;
	public static final int DRYGORE_BLOWPIPE = 30374;

	public static void register() {
		ItemAction.registerInventory(DRYGORE_BLOWPIPE, "check", DrygoreBlowpipe::check);
		ItemAction.registerInventory(DRYGORE_BLOWPIPE, "unload", DrygoreBlowpipe::unload);

		Arrays.stream(Dart.values())
			.filter(dart -> dart != Dart.NONE)
			.forEach(dart -> {
				ItemItemAction.register(DRYGORE_BLOWPIPE_UNCHARGED, dart.id, (player, blowpipe, dartItem) ->
					load(player, blowpipe, dartItem, dart));
				ItemItemAction.register(DRYGORE_BLOWPIPE, dart.id, (player, blowpipe, dartItem) ->
					load(player, blowpipe, dartItem, dart));
			});
	}

	private static void check(Player player, Item blowpipe) {
		Dart dart = getDart(blowpipe);
		var darts = "None";
			darts = "%s x %d".formatted(
				ObjType.get(dart.id).name,
				getDartAmount(blowpipe)
			);

		player.sendMessage("Darts: <col=007f00>%s</col>".formatted(darts));
	}

	private static void load(Player player, Item blowpipe, Item dartItem, Dart dart) {
		Dart loadedDart = getDart(blowpipe);
		if (loadedDart != Dart.NONE && loadedDart != dart) {
			player.sendMessage("The blowpipe currently contains a different sort of dart.");
			return;
		}
		int dartAmount = getDartAmount(blowpipe);
		int allowedAmount = MAX_AMOUNT - dartAmount;
		if (allowedAmount == 0) {
			player.sendMessage("The blowpipe can't hold any more darts.");
			return;
		}
		int addAmount = Math.min(allowedAmount, dartItem.getAmount());
		dartItem.incrementAmount(-addAmount);
		AttributeExtensions.putAttribute(blowpipe, AttributeTypes.AMMO_ID, dart.ordinal());
		AttributeExtensions.putAttribute(blowpipe, AttributeTypes.AMMO_AMOUNT, (dartAmount + addAmount));
		if (blowpipe.getId() == DRYGORE_BLOWPIPE_UNCHARGED) {
			blowpipe.setId(DRYGORE_BLOWPIPE);
			player.getInventory().sendUpdates();
		}
		check(player, blowpipe);
	}

	private static void unload(Player player, Item blowpipe) {
		if (player.isLocked())
			return;
		Dart dart = getDart(blowpipe);
		if (dart == Dart.NONE) {
			player.sendMessage("The blowpipe has no darts in it.");
			return;
		}
		if (player.getInventory().add(dart.id, getDartAmount(blowpipe)) == 0) {
			player.sendMessage("You don't have space to do that.");
			return;
		}
		AttributeExtensions.putAttribute(blowpipe, AttributeTypes.AMMO_AMOUNT, 0);
		AttributeExtensions.putAttribute(blowpipe, AttributeTypes.AMMO_ID, 0);
		blowpipe.setId(DRYGORE_BLOWPIPE_UNCHARGED);
		player.getInventory().sendUpdates();
	}

	private static Dart getDart(Item blowpipe) {
		return Dart.values()[blowpipe.getAttributeInt(AttributeTypes.AMMO_ID, 0)];
	}

	public static int getDartAmount(Item blowpipe) {
		return blowpipe.getAttributeInt(AttributeTypes.AMMO_AMOUNT, 0);
	}

	@Getter
	@RequiredArgsConstructor
	public enum Dart {
		NONE(-1, null),
		BRONZE(806, RangedWeapon.BRONZE_DART.data),
		IRON(807, RangedWeapon.IRON_DART.data),
		STEEL(808, RangedWeapon.STEEL_DART.data),
		BLACK(3093, RangedWeapon.BLACK_DART.data),
		MITHRIL(809, RangedWeapon.MITHRIL_DART.data),
		ADAMANT(810, RangedWeapon.ADAMANT_DART.data),
		RUNE(811, RangedWeapon.RUNE_DART.data),
		DRAGON(11230, RangedWeapon.DRAGON_DART.data),
		AMETHYST(25849, RangedWeapon.AMETHYST_DART.data),
		DRYGORE(33011, RangedWeapon.AMETHYST_DART.data)
		;

		private final int id;
		private final RangedData rangedData;

		public int getDartRangedStrengthBonus() {
			return ObjType.get(id).equipBonuses[EquipmentStats.RANGED_STRENGTH];
		}
	}
}
