package io.ruin.model.combat.special;

import io.ruin.api.utils.PackageLoader;
import io.ruin.cache.ObjType;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;

public interface Special extends ObjType.ItemDefPredicate {

	default boolean handle(Player player, Entity victim, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		/* override required */
		return false;
	}

	default boolean handleActivation(Player player) {
		/* override required */
		return false;
	}

	default int getDrainAmount() {
		return 0;
	}

	static void load() throws Exception {
		for (final Class<Special> c : PackageLoader.loadImplementing(
			Special.class,
			"io.ruin.model.combat.special")
		) {
			final Special special = c.getDeclaredConstructor().newInstance();
			ObjType.forEach(def -> {
				if (special.test(def)) {
					def.special = special;
				}
			});
		}
	}

}
