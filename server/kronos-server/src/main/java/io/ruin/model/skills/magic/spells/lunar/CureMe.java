package io.ruin.model.skills.magic.spells.lunar;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.Toxins;
import io.ruin.model.item.Item;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.skills.magic.rune.RuneRemoval;
import io.ruin.model.stat.StatType;

public class CureMe extends Spell {

	public CureMe() {
		Item[] runes = {
			Rune.LAW.toItem(1),
			Rune.ASTRAL.toItem(2),
			Rune.COSMIC.toItem(2)
		};
		clickAction = (p, i) -> {
			if (!p.getStats().check(StatType.Magic, 71, "cast this spell"))
				return;

			if (!p.isPoisoned() && !p.isVenomImmune()) {
				p.sendMessage("You're not poisoned, so there is no need to cast this!");
				return;
			}
			p.startEvent(event -> {
				p.lock();
				p.animate(4411);
				p.publicSound(2886);
				p.getStats().addXp(StatType.Magic, 69, true);
				RuneRemoval r = null;
				if (runes != null && (r = RuneRemoval.get(p, runes)) == null) {
					p.sendMessage("You don't have enough runes to cast this spell.");
					return;
				}
				event.delay(4);
				p.graphics(738, 95, 0);
				boolean venom = false;
				if (p.isEnvenomed()) {
					p.toxins.cure(Toxins.ToxinType.VENOM, 0);
					venom = true;
				}
				if (!venom)
					p.toxins.cure(Toxins.ToxinType.POSION, 0);
				p.sendMessageFormat("You cured the %s.", venom ? "venom" : "poison");

				p.unlock();
			});
			return;
		};
	}

}
