package io.ruin.model.skills.magic.spells.arceuus;

import io.ruin.model.item.Item;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.skills.magic.rune.RuneRemoval;
import io.ruin.model.stat.StatType;

public class SinisterOffering extends Spell {

	public enum Bones {
		BONES(526, 13.5, 40.5),
		MONKEY_BONES(3183, 15, 45),
		BAT_BONES(530, 15.9, 47.7),
		BIG_BONES(532, 45, 135),
		JOGRE_BONES(3125, 45, 135),
		ZOGRE_BONES(4812, 67.5, 202.5),
		SHAIKAHAN_BONES(3123, 75, 225),
		BABYDRAGON_BONES(534, 90, 270),
		WYRM_BONES(6812, 150, 450),
		DRAGON_BONES(536, 216, 648),
		WYVERN_BONES(6812, 216, 648),
		DRAKE_BONES(6729, 240, 720),
		FAYRG_BONES(4830, 252, 756),
		LAVA_DRAGON_BONES(11943, 255, 765),
		RAURG_BONES(4832, 288, 864),
		HYDRA_BONES(6728, 330, 990),
		DAGANNOTH_BONES(6729, 375, 1125),
		OURG_BONES(4834, 420, 1260),
		SUPERIOR_DRAGON_BONES(14616, 450, 1350),


		;

		public final int boneId, prayerxp, magicxp;

		Bones(int boneId, double prayerxp, double magicxp) {
			this.boneId = boneId;
			this.prayerxp = (int) prayerxp;
			this.magicxp = (int) magicxp;
		}

		public static final Bones[] VALUES = values();
	}

	public SinisterOffering() {
		Item[] runes = {Rune.WRATH.toItem(1), Rune.BLOOD.toItem(1)};

		registerClick(92, 180, true, runes, (p, i) -> {
			p.startEvent(event -> {
				int amount = 0;
				for (Item item : p.getInventory().getItems()) {
					for (Bones bones : Bones.VALUES) {
						if (item != null && item.getId() == bones.boneId) {
							RuneRemoval r = null;
							if (runes != null && (r = RuneRemoval.get(p, runes)) == null) {
								p.sendMessage("You don't have enough runes to cast this spell.");
								return;
							}
							if (amount > 2) {
								return;
							}
							if (bones.boneId == Bones.SUPERIOR_DRAGON_BONES.boneId && p.getStats().get(StatType.Prayer).currentLevel < 70) {
								p.sendMessage("You need a Prayer level of 70 to use this spell with superior dragon bones.");
								return;
							}

							p.animate(8975);
							p.graphics(1872, 0, 0);
							p.getInventory().remove(bones.boneId, 1);
							r.remove();
							p.getStats().addXp(StatType.Magic, bones.magicxp, true);
							p.getStats().addXp(StatType.Prayer, bones.prayerxp, true);
							p.getStats().get(StatType.Prayer).restore(1, 0);
							if (bones.boneId == Bones.SUPERIOR_DRAGON_BONES.boneId) {
								p.getStats().get(StatType.Prayer).restore(2, 0);
							}
							event.delay(2);
							amount++;
						}
					}
				}
			});
			return false;
		});
	}
}


