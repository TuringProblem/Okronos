package io.ruin.model.combat;

import io.ruin.cache.ItemID;
import io.ruin.model.combat.special.ranged.bolts.*;
import io.ruin.model.entity.Entity;
import io.ruin.model.item.Item;
import io.ruin.model.map.Projectile;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public enum RangedAmmo {

	/**
	 * Arrows
	 */
	BRONZE_ARROW(ItemID.BRONZE_ARROW, new RangedData(19, 1104, Projectile.arrow(10))),
	IRON_ARROW(ItemID.IRON_ARROW, new RangedData(18, 1105, Projectile.arrow(9))),
	STEEL_ARROW(ItemID.STEEL_ARROW, new RangedData(20, 1106, Projectile.arrow(11))),
	BROAD_ARROW(ItemID.BROAD_ARROWS, new RangedData(20, 1106, Projectile.arrow(11))),
	MITHRIL_ARROW(ItemID.MITHRIL_ARROW, new RangedData(21, 1107, Projectile.arrow(12))),
	ADAMANT_ARROW(ItemID.ADAMANT_ARROW, new RangedData(22, 1108, Projectile.arrow(13))),
	RUNE_ARROW(ItemID.RUNE_ARROW, new RangedData(24, 1109, Projectile.arrow(15))),
	AMETHYST_ARROW(ItemID.AMETHYST_ARROW, new RangedData(1385, 1383, Projectile.arrow(1384))),
	DRAGON_ARROW(ItemID.DRAGON_ARROW, new RangedData(1116, 1111, Projectile.arrow(1120))),

	V_BRONZE_ARROW(ItemID.BRONZE_ARROW, new RangedData(2289, 1104, Projectile.arrow(2291))),
	V_IRON_ARROW(ItemID.IRON_ARROW, new RangedData(2289, 1105, Projectile.arrow(2291))),
	V_STEEL_ARROW(ItemID.STEEL_ARROW, new RangedData(2289, 1106, Projectile.arrow(2291))),
	V_BROAD_ARROW(ItemID.BROAD_ARROWS, new RangedData(2289, 1106, Projectile.arrow(2291))),
	V_MITHRIL_ARROW(ItemID.MITHRIL_ARROW, new RangedData(2289, 1107, Projectile.arrow(2291))),
	V_ADAMANT_ARROW(ItemID.ADAMANT_ARROW, new RangedData(2289, 1108, Projectile.arrow(2291))),
	V_RUNE_ARROW(ItemID.RUNE_ARROW, new RangedData(2289, 1109, Projectile.arrow(2291))),
	V_AMETHYST_ARROW(ItemID.AMETHYST_ARROW, new RangedData(2289, 1383, Projectile.arrow(2291))),
	V_DRAGON_ARROW(ItemID.DRAGON_ARROW, new RangedData(2289, 1111, Projectile.arrow(2291))),
	/**
	 * Bolts
	 */
	DRAGON_SAPPHIRE_BOLTS_E(ItemID.SAPPHIRE_DRAGON_BOLTS_E, new RangedData(Projectile.DRAGON_BOLT), new SapphireBoltEffect()),
	DRAGON_EMERALD_BOLTS_E(ItemID.EMERALD_DRAGON_BOLTS_E, new RangedData(Projectile.DRAGON_BOLT), new EmeraldBoltEffect()),
	DRAGON_RUBY_BOLTS_E(ItemID.RUBY_DRAGON_BOLTS_E, new RangedData(Projectile.DRAGON_BOLT), new RubyBoltEffect()),
	DRAGON_OPAL_BOLTS_E(ItemID.OPAL_DRAGON_BOLTS_E, new RangedData(Projectile.DRAGON_BOLT), new OpalDragonBoltEffect()),
	DRAGON_DIAMOND_BOLTS_E(ItemID.DIAMOND_DRAGON_BOLTS_E, new RangedData(Projectile.DRAGON_BOLT), new DiamondBoltEffect()),
	DRAGON_DRAGONSTONE_BOLTS_E(ItemID.DRAGONSTONE_DRAGON_BOLTS_E, new RangedData(Projectile.DRAGON_BOLT), new DragonBoltEffect()),
	DRAGON_ONYX_BOLTS_E(ItemID.ONYX_DRAGON_BOLTS_E, new RangedData(Projectile.DRAGON_BOLT), new OnyxBoltEffect()),
	SAPPHIRE_BOLTS_E(ItemID.SAPPHIRE_BOLTS_E, new RangedData(Projectile.BOLT), new SapphireBoltEffect()),
	EMERALD_BOLTS_E(ItemID.EMERALD_BOLTS_E, new RangedData(Projectile.BOLT), new EmeraldBoltEffect()),
	DIAMOND_BOLTS_E(ItemID.DIAMOND_BOLTS_E, new RangedData(Projectile.BOLT), new DiamondBoltEffect()),
	DRAGONSTONE_BOLTS_E(ItemID.DRAGONSTONE_BOLTS_E, new RangedData(Projectile.BOLT), new DragonBoltEffect()),
	ONYX_BOLTS_E(ItemID.ONYX_BOLTS_E, new RangedData(Projectile.BOLT), new OnyxBoltEffect()),
	RUBY_BOLTS_E(ItemID.RUBY_BOLTS_E, new RangedData(Projectile.BOLT), new RubyBoltEffect()),
	BRONZE_BOLTS(ItemID.BRONZE_BOLTS, new RangedData(Projectile.BOLT)),
	IRON_BOLTS(ItemID.IRON_BOLTS, new RangedData(Projectile.BOLT)),
	STEEL_BOLTS(ItemID.STEEL_BOLTS, new RangedData(Projectile.BOLT)),
	MITHRIL_BOLTS(ItemID.MITHRIL_BOLTS, new RangedData(Projectile.BOLT)),
	ADAMANT_BOLTS(ItemID.ADAMANT_BOLTS, new RangedData(Projectile.BOLT)),
	BROAD_BOLTS(ItemID.BROAD_BOLTS, new RangedData(Projectile.BOLT)),
	RUNITE_BOLTS(ItemID.RUNITE_BOLTS, new RangedData(Projectile.BOLT)),
	AMETHYST_BROAD_BOLTS(ItemID.AMETHYST_BROAD_BOLTS, new RangedData(Projectile.BOLT)),
	BLURITE_BOLTS(ItemID.BLURITE_BOLTS, new RangedData(Projectile.BOLT)),
	BONE_BOLTS(ItemID.BONE_BOLTS, new RangedData(Projectile.BOLT)),
	SILVER_BOLTS(ItemID.SILVER_BOLTS, new RangedData(Projectile.BOLT)),
	OPAL_BOLTS(ItemID.OPAL_BOLTS, new RangedData(Projectile.BOLT)),
	JADE_BOLTS(ItemID.JADE_BOLTS, new RangedData(Projectile.BOLT)),
	PEARL_BOLTS(ItemID.PEARL_BOLTS, new RangedData(Projectile.BOLT)),
	TOPAZ_BOLTS(ItemID.TOPAZ_BOLTS, new RangedData(Projectile.BOLT)),
	SAPPHIRE_BOLTS(ItemID.SAPPHIRE_BOLTS, new RangedData(Projectile.BOLT)),
	EMERALD_BOLTS(ItemID.EMERALD_BOLTS, new RangedData(Projectile.BOLT)),
	RUBY_BOLTS(ItemID.RUBY_BOLTS, new RangedData(Projectile.BOLT)),
	DIAMOND_BOLTS(ItemID.DIAMOND_BOLTS, new RangedData(Projectile.BOLT)),
	DRAGONSTONE_BOLTS(ItemID.DRAGONSTONE_BOLTS, new RangedData(Projectile.BOLT)),
	ONYX_BOLTS(ItemID.ONYX_BOLTS, new RangedData(Projectile.BOLT)),
	BOLT_RACK(ItemID.BOLT_RACK, new RangedData(true, Projectile.BOLT)),
	KEBBIT_BOLTS(ItemID.KEBBIT_BOLTS, new RangedData(Projectile.BOLT)),
	LONG_KEBBIT_BOLTS(ItemID.LONG_KEBBIT_BOLTS, new RangedData(Projectile.BOLT)),


	/**
	 * Dragon bolts
	 */
	DRAGON_BOLTS(ItemID.DRAGON_BOLTS, new RangedData(Projectile.DRAGON_BOLT)),
	DRAGON_OPAL_BOLTS(ItemID.OPAL_DRAGON_BOLTS, new RangedData(Projectile.DRAGON_BOLT), new OpalDragonBoltEffect()),
	DRAGON_JADE_BOLTS(ItemID.JADE_DRAGON_BOLTS, new RangedData(Projectile.DRAGON_BOLT)),
	DRAGON_PEARL_BOLTS(ItemID.PEARL_DRAGON_BOLTS, new RangedData(Projectile.DRAGON_BOLT)),
	DRAGON_TOPAZ_BOLTS(ItemID.TOPAZ_DRAGON_BOLTS, new RangedData(Projectile.DRAGON_BOLT)),
	DRAGON_SAPPHIRE_BOLTS(ItemID.SAPPHIRE_DRAGON_BOLTS, new RangedData(Projectile.DRAGON_BOLT)),
	DRAGON_EMERALD_BOLTS(ItemID.EMERALD_DRAGON_BOLTS, new RangedData(Projectile.DRAGON_BOLT)),
	DRAGON_RUBY_BOLTS(ItemID.RUBY_DRAGON_BOLTS, new RangedData(Projectile.DRAGON_BOLT)),
	DRAGON_DIAMOND_BOLTS(ItemID.DIAMOND_DRAGON_BOLTS, new RangedData(Projectile.DRAGON_BOLT)),
	DRAGON_DRAGONSTONE_BOLTS(ItemID.DRAGONSTONE_DRAGON_BOLTS, new RangedData(Projectile.DRAGON_BOLT)),
	DRAGON_ONYX_BOLTS(ItemID.ONYX_DRAGON_BOLTS, new RangedData(Projectile.DRAGON_BOLT)),


	/**
	 * Javelins
	 */
	BRONZE_JAVELIN(ItemID.BRONZE_JAVELIN, new RangedData(true, Projectile.javelin(200))),
	IRON_JAVELIN(ItemID.IRON_JAVELIN, new RangedData(true, Projectile.javelin(201))),
	STEEL_JAVELIN(ItemID.STEEL_JAVELIN, new RangedData(true, Projectile.javelin(202))),
	MITHRIL_JAVELIN(ItemID.MITHRIL_JAVELIN, new RangedData(true, Projectile.javelin(203))),
	ADAMANT_JAVELIN(ItemID.ADAMANT_JAVELIN, new RangedData(true, Projectile.javelin(204))),
	RUNE_JAVELIN(ItemID.RUNE_JAVELIN, new RangedData(true, Projectile.javelin(205))),
	AMETHYST_JAVELIN(ItemID.AMETHYST_JAVELIN, new RangedData(true, Projectile.javelin(205))), //todo - find gfx
	DRAGON_JAVELIN(ItemID.DRAGON_JAVELIN, new RangedData(true, Projectile.javelin(1301)));

	public final RangedData data;

	public final BiFunction<Entity, Hit, Boolean> effect;
	public final int itemId;  // Add the item ID field

	RangedAmmo(int itemId, RangedData data) {
		this.itemId = itemId;
		this.data = data;
		this.effect = null;
	}


	RangedAmmo(int itemId, RangedData rangedData, BiFunction<Entity, Hit, Boolean> effect) {
		this.itemId = itemId;
		this.data = rangedData;
		this.effect = effect;
	}

	public static RangedAmmo getByItemId(int itemId) {
		System.out.println("item id is " + itemId);
		for (RangedAmmo ammo : values()) {
			if (ammo.itemId == itemId) {
				return ammo;
			}
		}
		return null;
	}

	public static RangedAmmo getByItemId(int itemId, int weaponId) {
		if (weaponId == 27610) { // Venator bow
			List<RangedAmmo> venatorAmmo = Arrays.asList(V_BRONZE_ARROW, V_IRON_ARROW, V_STEEL_ARROW, V_BROAD_ARROW, V_MITHRIL_ARROW, V_ADAMANT_ARROW, V_RUNE_ARROW, V_AMETHYST_ARROW, V_DRAGON_ARROW);
			for (RangedAmmo ammo : venatorAmmo) {
				if (ammo.itemId == itemId) {
					return ammo;
				}
			}
		} else if (new Item(weaponId).getDef().name.equalsIgnoreCase("Dark bow")) { // Dark Bow
			List<RangedAmmo> darkBowAmmo = Arrays.asList(BRONZE_ARROW, IRON_ARROW, STEEL_ARROW, BROAD_ARROW, MITHRIL_ARROW, ADAMANT_ARROW, RUNE_ARROW, AMETHYST_ARROW, DRAGON_ARROW);
			for (RangedAmmo ammo : darkBowAmmo) {
				if (ammo.itemId == itemId) {
					return ammo;
				}
			}
		} else {
			for (RangedAmmo ammo : values()) {
				if (ammo.itemId == itemId) {
					return ammo;
				}
			}
		}
		return null;
	}


}