package io.ruin.model.skills.magic.rune;

import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.item.Item;

import java.util.concurrent.atomic.AtomicReference;

/**
 * SPECIFIC ORDER IS USED FOR RUNE POUCH
 */
public enum Rune {
	AIR(ItemID.AIR_RUNE),
	WATER(ItemID.WATER_RUNE),
	EARTH(ItemID.EARTH_RUNE),
	FIRE(ItemID.FIRE_RUNE),
	MIND(ItemID.MIND_RUNE),
	CHAOS(ItemID.CHAOS_RUNE),
	DEATH(ItemID.DEATH_RUNE),
	BLOOD(ItemID.BLOOD_RUNE),
	COSMIC(ItemID.COSMIC_RUNE),
	NATURE(ItemID.NATURE_RUNE),
	LAW(ItemID.LAW_RUNE),
	BODY(ItemID.BODY_RUNE),
	SOUL(ItemID.SOUL_RUNE),
	ASTRAL(ItemID.ASTRAL_RUNE),
	MIST(ItemID.MIST_RUNE, WATER, AIR),
	MUD(ItemID.MUD_RUNE, WATER, EARTH),
	DUST(ItemID.DUST_RUNE, EARTH, AIR),
	LAVA(ItemID.LAVA_RUNE, EARTH, FIRE),
	STEAM(ItemID.STEAM_RUNE, FIRE, WATER),
	SMOKE(ItemID.SMOKE_RUNE, FIRE, AIR),
	WRATH(ItemID.WRATH_RUNE);

	private int id = -1;

	private final Rune[] combinations;

	public static final Rune[] VALUES = values();

	Rune(int id, Rune... comboRunes) {
		this.combinations = comboRunes.length == 0 ? null : comboRunes;
		this.id = id;
	}

	public boolean accept(Rune rune) {
		if (rune == null)
			return false;
		if (rune == this)
			return true;
		if (rune.combinations != null) {
			for (Rune comboRune : rune.combinations) {
				if (comboRune == this)
					return true;
			}
		}
		return false;
	}

	public int getId() {
		return id;
	}

	public Item toItem(int amount) {
		return new Item(id, amount);
	}

	public static void register() {
		ObjType.forEach(def -> {
			String name = def.name.toLowerCase();
			for (Rune rune : values()) {
				if (name.contains("staff") && name.contains(rune.name().toLowerCase())) {
					def.staffRune = rune;
					break;
				}
			}
		});
		for (Rune rune : values()) {
			ObjType def = ObjType.get(rune.id);
			def.rune = rune;
		}
	}


}

