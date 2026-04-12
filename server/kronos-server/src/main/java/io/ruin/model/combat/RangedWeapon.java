package io.ruin.model.combat;

import io.ruin.model.map.Projectile;

import java.util.Arrays;

public enum RangedWeapon {
	/**
	 * Knifes
	 */
	BRONZE_KNIFE(new RangedData(219, Projectile.thrown(212, 11))),
	IRON_KNIFE(new RangedData(220, Projectile.thrown(213, 11))),
	STEEL_KNIFE(new RangedData(221, Projectile.thrown(214, 11))),
	BLACK_KNIFE(new RangedData(222, Projectile.thrown(215, 11))),
	MITHRIL_KNIFE(new RangedData(223, Projectile.thrown(216, 11))),
	ADAMANT_KNIFE(new RangedData(224, Projectile.thrown(217, 11))),
	RUNE_KNIFE(new RangedData(225, Projectile.thrown(218, 11))),
	DRAGON_KNIFE(new RangedData(-1, Projectile.thrown(28, 11))),
	/**
	 * Darts
	 */
	BRONZE_DART(new RangedData(232, Projectile.thrown(226, 11))),
	IRON_DART(new RangedData(233, Projectile.thrown(227, 11))),
	STEEL_DART(new RangedData(234, Projectile.thrown(228, 11))),
	BLACK_DART(new RangedData(273, Projectile.thrown(34, 11))), //todo - test projectile gfx
	MITHRIL_DART(new RangedData(235, Projectile.thrown(229, 11))),
	ADAMANT_DART(new RangedData(236, Projectile.thrown(230, 11))),
	RUNE_DART(new RangedData(237, Projectile.thrown(231, 11))),
	AMETHYST_DART(new RangedData(1937, Projectile.thrown(1936, 105))),
	DRAGON_DART(new RangedData(1123, Projectile.thrown(1122, 105))),
	DRYGORE_DART(new RangedData(1123, Projectile.thrown(1122, 105))),
	TONALZTICS_OF_RALOS(new RangedData(2734, Projectile.thrown(2739, 105))),

	/**
	 * Throwing axes
	 */
	BRONZE_THROWING_AXE(new RangedData(43, Projectile.thrown(36, 11))),
	IRON_THROWING_AXE(new RangedData(42, Projectile.thrown(35, 11))),
	STEEL_THROWING_AXE(new RangedData(44, Projectile.thrown(37, 11))),
	BLACK_THROWING_AXE(new RangedData(47, Projectile.thrown(40, 11))),
	MITHRIL_THROWING_AXE(new RangedData(45, Projectile.thrown(38, 11))),
	ADAMANT_THROWING_AXE(new RangedData(46, Projectile.thrown(39, 11))),
	RUNE_THROWING_AXE(new RangedData(48, Projectile.thrown(41, 11))),
	DRAGON_THROWING_AXE(new RangedData(1320, Projectile.thrown(1319, 11))),
	MORRIGANS_THROWING_AXE(new RangedData(1624, Projectile.thrown(1623, 11))),
	/**
	 * Thrown (misc)
	 */
	OBBY_RING(new RangedData(Projectile.thrown(442, 11))),
	CHINCHOMPA(new RangedData(Projectile.thrown(908, 11))),
	RED_CHINCHOMPA(new RangedData(Projectile.thrown(909, 11))),
	BLACK_CHINCHOMPA(new RangedData(Projectile.thrown(1272, 11))),
	MORRIGANS_JAVELIN(new RangedData(1619, Projectile.thrown(1620, 11))),
	/**
	 * Generated
	 */
	CRYSTAL_BOW(new RangedData(250, Projectile.arrow(249))),

	CRAWS_BOW(new RangedData(1611, Projectile.arrow(1574))),
	BOW_OF_FAERDHINEN(new RangedData(1933, Projectile.arrow(1934))),
	BOW_OF_FAERDHINEN_BLUE(new RangedData(1935, Projectile.arrow(1934))),

	BOW_OF_FAERDHINEN_BLACK(new RangedData(1927, Projectile.arrow(1926))),

	BOW_OF_FAERDHINEN_WHITE(new RangedData(1925, Projectile.arrow(1924))),

	BOW_OF_FAERDHINEN_GREEN(new RangedData(1931, Projectile.arrow(1930))),

	BOW_OF_FAERDHINEN_YELLOW(new RangedData(1933, Projectile.arrow(1932))),

	BOW_OF_FAERDHINEN_RED(new RangedData(1923, Projectile.arrow(1922))),

	BOW_OF_FAERDHINEN_CYAN(new RangedData(1935, Projectile.arrow(1934))),

	BOW_OF_FAERDHINEN_PURPLE(new RangedData(1929, Projectile.arrow(1928))),
	/**
	 * Fired (ammo)
	 */
	NORMAL_BOW(
		RangedAmmo.BRONZE_ARROW,
		RangedAmmo.IRON_ARROW,
		RangedAmmo.STEEL_ARROW,
		RangedAmmo.BROAD_ARROW,
		RangedAmmo.MITHRIL_ARROW,
		RangedAmmo.ADAMANT_ARROW,
		RangedAmmo.RUNE_ARROW,
		RangedAmmo.AMETHYST_ARROW,
		RangedAmmo.DRAGON_ARROW
	),
	DARK_BOW(
		RangedAmmo.BRONZE_ARROW,
		RangedAmmo.IRON_ARROW,
		RangedAmmo.STEEL_ARROW,
		RangedAmmo.BROAD_ARROW,
		RangedAmmo.MITHRIL_ARROW,
		RangedAmmo.ADAMANT_ARROW,
		RangedAmmo.RUNE_ARROW,
		RangedAmmo.AMETHYST_ARROW,
		RangedAmmo.DRAGON_ARROW
	),
	BALLISTA(
		RangedAmmo.BRONZE_JAVELIN,
		RangedAmmo.IRON_JAVELIN,
		RangedAmmo.STEEL_JAVELIN,
		RangedAmmo.MITHRIL_JAVELIN,
		RangedAmmo.ADAMANT_JAVELIN,
		RangedAmmo.RUNE_JAVELIN,
		RangedAmmo.AMETHYST_JAVELIN,
		RangedAmmo.DRAGON_JAVELIN
	),
	CROSSBOW(
		Arrays.stream(RangedAmmo.values()).filter(a -> a.name().toLowerCase().contains("bolt")).toArray(RangedAmmo[]::new)
	),
	TWISTED_BOW(
		RangedAmmo.BRONZE_ARROW,
		RangedAmmo.IRON_ARROW,
		RangedAmmo.STEEL_ARROW,
		RangedAmmo.BROAD_ARROW,
		RangedAmmo.MITHRIL_ARROW,
		RangedAmmo.ADAMANT_ARROW,
		RangedAmmo.RUNE_ARROW,
		RangedAmmo.AMETHYST_ARROW,
		RangedAmmo.DRAGON_ARROW
	),
	VENATOR_BOW(
		RangedAmmo.V_BRONZE_ARROW,
		RangedAmmo.V_IRON_ARROW,
		RangedAmmo.V_STEEL_ARROW,
		RangedAmmo.V_BROAD_ARROW,
		RangedAmmo.V_MITHRIL_ARROW,
		RangedAmmo.V_ADAMANT_ARROW,
		RangedAmmo.V_RUNE_ARROW,
		RangedAmmo.V_AMETHYST_ARROW,
		RangedAmmo.V_DRAGON_ARROW
	),
	KARILS_CROSSBOW(RangedAmmo.BOLT_RACK),
	HUNTERS_CROSSBOW(RangedAmmo.KEBBIT_BOLTS, RangedAmmo.LONG_KEBBIT_BOLTS),
	TOXIC_BLOWPIPE,
	DRYGORE_BLOWPIPE,
	TONALZTICS_OF_RALOS_UNCHARGED,
	MAGMA_BLOWPIPE,
	CORRUPTED_JAVELIN;

	public final RangedData data;

	public final boolean[] allowedAmmo;

	RangedWeapon() {
		this.data = null;
		this.allowedAmmo = null;
	}

	RangedWeapon(RangedData data) {
		this.data = data;
		this.allowedAmmo = null;
	}

	RangedWeapon(RangedAmmo... ammo) {
		this.data = null;
		this.allowedAmmo = new boolean[RangedAmmo.values().length];
		for (RangedAmmo a : ammo)
			this.allowedAmmo[a.ordinal()] = true;
	}

	public boolean allowAmmo(RangedAmmo ammo) {
		if(this.allowedAmmo == null)
			return false;
		return allowedAmmo[ammo.ordinal()];
	}

	/**
	 * Determines whether the weapon is self-supplied in terms of ammunition.
	 * <p>
	 * This method checks if the weapon is one of the predefined types capable of
	 * supplying its own ammunition without an external input. Specifically, it evaluates
	 * if the weapon is either a TOXIC_BLOWPIPE or a MAGMA_BLOWPIPE.
	 *
	 * @return true if the weapon is self-supplied with ammunition, false otherwise.
	 */
	public Boolean isWeaponSelfSupplied() {
		return switch (this) {
			case TOXIC_BLOWPIPE, MAGMA_BLOWPIPE, DRYGORE_BLOWPIPE -> true;
			default -> false;
		};
	}

	/**
	 * Determines if the weapon is capable of generating its own ammunition.
	 * <p>
	 * This method evaluates if the weapon either belongs to the "Thrown" weapon type
	 * or is among a specific set of weapons, such as CRYSTAL_BOW, CRAWS_BOW, or
	 * various types of BOW_OF_FAERDHINEN, which have the capability of being
	 * self-sufficient in generating ammunition.
	 *
	 * @return true if the weapon is self-generating its own ammunition, otherwise false
	 */
	public Boolean isWeaponSelfGeneratingAmmo() {
		if (this.isWeaponOfTypeThrown())
			return true;
		return switch (this) {
			case CRYSTAL_BOW,
				 CRAWS_BOW,
				 BOW_OF_FAERDHINEN,
				 BOW_OF_FAERDHINEN_BLUE,
				 BOW_OF_FAERDHINEN_RED,
				 BOW_OF_FAERDHINEN_YELLOW,
				 BOW_OF_FAERDHINEN_WHITE,
				 BOW_OF_FAERDHINEN_CYAN,
				 BOW_OF_FAERDHINEN_GREEN,
				 BOW_OF_FAERDHINEN_BLACK,
				 BOW_OF_FAERDHINEN_PURPLE -> true;
			default -> false;
		};
	}

	/**
	 * Determines if the weapon is of type "Thrown".
	 * <p>
	 * This method evaluates the specific weapon and checks if it falls under
	 * the thrown weapon category, including knives, darts, throwing axes,
	 * and specific thrown items like chinchompas and javelins.
	 * </p>
	 * @return true if the weapon belongs to the thrown weapon type, otherwise false
	 */
	public Boolean isWeaponOfTypeThrown() {
		return switch (this) {
			case OBBY_RING,
				 CHINCHOMPA,
				 RED_CHINCHOMPA,
				 BLACK_CHINCHOMPA,
				 MORRIGANS_JAVELIN,
				 TONALZTICS_OF_RALOS,
				 // Knives
				 BRONZE_KNIFE,
				 IRON_KNIFE,
				 STEEL_KNIFE,
				 BLACK_KNIFE,
				 MITHRIL_KNIFE,
				 ADAMANT_KNIFE,
				 RUNE_KNIFE,
				 DRAGON_KNIFE,
				 // Darts
				 BRONZE_DART,
				 IRON_DART,
				 STEEL_DART,
				 BLACK_DART,
				 MITHRIL_DART,
				 ADAMANT_DART,
				 RUNE_DART,
				 AMETHYST_DART,
				 DRAGON_DART,
				 // Throwing Axes
				 BRONZE_THROWING_AXE,
				 IRON_THROWING_AXE,
				 STEEL_THROWING_AXE,
				 BLACK_THROWING_AXE,
				 MITHRIL_THROWING_AXE,
				 ADAMANT_THROWING_AXE,
				 RUNE_THROWING_AXE,
				 DRAGON_THROWING_AXE,
				 MORRIGANS_THROWING_AXE -> true;
			default -> false;
		};
	}
}