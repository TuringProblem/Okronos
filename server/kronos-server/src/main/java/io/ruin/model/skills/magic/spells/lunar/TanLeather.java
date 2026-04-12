package io.ruin.model.skills.magic.spells.lunar;

import io.ruin.cache.ItemID;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.OptionAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.skills.magic.rune.RuneRemoval;
import io.ruin.model.stat.StatType;
import lombok.RequiredArgsConstructor;

public class TanLeather extends Spell {

	@RequiredArgsConstructor
	enum Recipes {
		COWHIDE(ItemID.COWHIDE, ItemID.LEATHER),
		SNAKEHIDE(ItemID.SNAKESKIN, ItemID.SNAKE_HIDE),
		GREEN_DRAGONHIDE(ItemID.GREEN_DRAGONHIDE, ItemID.GREEN_DRAGON_LEATHER),
		BLUE_DRAGONHIDE(ItemID.BLUE_DRAGONHIDE, ItemID.BLUE_DRAGON_LEATHER),
		RED_DRAGONHIDE(ItemID.RED_DRAGONHIDE, ItemID.RED_DRAGON_LEATHER),
		BLACK_DRAGONHIDE(ItemID.BLACK_DRAGONHIDE, ItemID.BLACK_DRAGON_LEATHER),
		COWHIDEHARD(ItemID.COWHIDE, ItemID.HARD_LEATHER),
		;
		public final int hide;
		public final int leather;
	}


	public TanLeather() {
		Item[] runes = {
			Rune.NATURE.toItem(1),
			Rune.ASTRAL.toItem(2),
			Rune.FIRE.toItem(5),
		};

	}
}