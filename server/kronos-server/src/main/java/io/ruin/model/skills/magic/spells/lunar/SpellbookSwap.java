package io.ruin.model.skills.magic.spells.lunar;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.SpellBook;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.skills.magic.rune.RuneRemoval;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;

public class SpellbookSwap extends Spell {

	public SpellbookSwap() {
		clickAction = (p, i) -> {
			if (!prechecks(p, false)) {
				return;
			}
			p.dialogue(new OptionsDialogue(
				new Option("Modern.", () -> change(p, SpellBook.MODERN)),
				new Option("Ancients.", () -> change(p, SpellBook.ANCIENT)),
				new Option("Lunar.", () -> change(p, SpellBook.LUNAR)),
				new Option("Arceuus.", () -> change(p, SpellBook.ARCEUUS))));
		};
	}

	private static final Item[] runeItems = {
		Rune.ASTRAL.toItem(3),
		Rune.LAW.toItem(1),
		Rune.COSMIC.toItem(2)
	};

	private boolean prechecks(Player p, boolean remove) {
		if (p.isLocked())
			return false;
		if (!p.getStats().check(StatType.Magic, 96, "cast this spell"))
			return false;
		RuneRemoval r = null;
		if (runeItems != null && (r = RuneRemoval.get(p, runeItems)) == null) {
			p.sendMessage("You don't have enough runes to cast this spell.");
			return false;
		}
		if (remove && r != null)
			r.remove();
		return true;
	}

	private void change(Player p, SpellBook to) {
		if (!prechecks(p, true))
			return;
		p.animate(6299);
		p.graphics(1062);
		p.spellbookSwapOriginalBook = SpellBook.VALUES[VarPlayerRepository.MAGIC_BOOK.get(p.player)];
		to.setActive(p);
		p.getStats().addXp(StatType.Magic, 130, true);
	}

	public static void resetBookPostCast(Player p) {
		if (p.spellbookSwapOriginalBook != null) { // cast one spell and return to original book
			p.spellbookSwapOriginalBook.setActive(p);
			p.spellbookSwapOriginalBook = null;
		}
	}

}