package io.ruin.model.skills.fletching;

import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.FletchingDreams;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.dialogue.skill.SkillItem;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.stat.StatType;

public enum StringBow {

	SHORT_BOW(50, 841, 5, 5.0, 6678),
	LONG_BOW(48, 839, 10, 10.0, 6684),
	OAK_SHORT_BOW(54, 843, 20, 16.5, 6679),
	OAK_LONG_BOW(56, 845, 25, 25.0, 6685),
	COMPOSITE_BOW(4825, 4827, 30, 45.0, 6686),
	WILLOW_SHORT_BOW(60, 849, 35, 33.3, 6680),
	WILLOW_LONG_BOW(58, 847, 40, 41.5, 6686),
	MAPLE_SHORT_BOW(64, 853, 50, 50.0, 6681),
	MAPLE_LONG_BOW(62, 851, 55, 58.3, 6687),
	YEW_SHORT_BOW(68, 857, 65, 68.5, 6682),
	YEW_LONG_BOW(66, 855, 70, 75.0, 6688),
	MAGIC_SHORT_BOW(72, 861, 80, 83.3, 6683),
	MAGIC_LONG_BOW(70, 859, 85, 91.5, 6689),

	;

	public final int unstrung, strung, levelReq, animation;
	public final double exp;

	StringBow(int unstrung, int strung, int levelReq, double exp, int animation) {
		this.unstrung = unstrung;
		this.strung = strung;
		this.levelReq = levelReq;
		this.exp = exp;
		this.animation = animation;
	}

	private void make(Player player, Item unstrung, Item bowString) {
		unstrung.remove();
		bowString.remove();
		player.getInventory().add(strung, 1);
		player.sendFilteredMessage("You add a string to the bow.");
		player.animate(animation);
		double newExp = exp;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.FLETCHING_DREAMS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.FLETCHING_DREAMS);
			FletchingDreams c = (FletchingDreams) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			float multiplier = 1;
			multiplier += c.getExpBoost();
			newExp *= multiplier;
		}
		player.getStats().addXp(StatType.Fletching, newExp, true);
		PerkTaskHandler.handleGatherResource(player, strung, 1);
		DailyTasks.handleItemObtained(player, strung, StatType.Fletching);
		switch (unstrung.getId()) {
			case 50:
			case 54:
			case 60:
			case 64:
			case 68:
			case 72:
				player.shortbowsFletchedCounter++;
				if (player.shortbowsFletchedCounter == Achievements.THESE_SHOULDNT_TAKE_LONG_I.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.THESE_SHOULDNT_TAKE_LONG_I.getAchievementName());
				break;
		}
		if (strung == 861) {
			player.magicShortbowsFletched++;
			if (player.magicShortbowsFletched == Achievements.THESE_SHOULDNT_TAKE_LONG_II.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.THESE_SHOULDNT_TAKE_LONG_II.getAchievementName());
		}
	}

	private static final int BOW_STRING = 1777;

	public static void register() {
		for (StringBow bow : values()) {
			SkillItem item = new SkillItem(bow.strung).addAction((player, amount, event) -> {
				while (amount-- > 0) {
					Item unstrung = player.getInventory().findItem(bow.unstrung);
					if (unstrung == null)
						return;
					Item bowString = player.getInventory().findItem(BOW_STRING);
					if (bowString == null)
						return;
					bow.make(player, unstrung, bowString);
					event.delay(2);
				}
			});
			ItemItemAction.register(bow.unstrung, BOW_STRING, (player, unstrung, bowString) -> {
				if (!player.getStats().check(StatType.Fletching, bow.levelReq, bow.strung, "do that"))
					return;
				boolean multiple = player.getInventory().hasMultiple(unstrung.getId(), bowString.getId());
				if (multiple) {
					SkillDialogue.make(player, item);
					return;
				}
				bow.make(player, unstrung, bowString);
			});
		}
	}
}
