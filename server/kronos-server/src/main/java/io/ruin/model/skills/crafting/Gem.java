package io.ruin.model.skills.crafting;

import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ArtsnCrafts;
import io.ruin.model.activities.perktree.perks.PacksnPotions;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.dialogue.skill.SkillItem;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.stat.StatType;

import static io.ruin.model.skills.Tool.CHISEL;

public enum Gem {

	OPAL(1, 15.0, 1625, 1609, 886, "Opal", "Opals"),
	JADE(13, 20.0, 1627, 1611, 886, "Jade", "Jades"),
	RED_TOPAZ(16, 25.0, 1629, 1613, 887, "Red topaz", "Red topazes"),
	SAPPHIRE(20, 50.0, 1623, 1607, 888, "Sapphire", "Sapphires"),
	EMERALD(27, 67.5, 1621, 1605, 889, "Emerald", "Emeralds"),
	RUBY(34, 85.0, 1619, 1603, 887, "Ruby", "Rubies"),
	DIAMOND(43, 107.5, 1617, 1601, 886, "Diamond", "Diamonds"),
	DRAGONSTONE(55, 137.5, 1631, 1615, 885, "Dragonstone", "Dragonstones"),
	ONYX(67, 167.5, 6571, 6573, 2717, "Onyx", "Onyxes"),
	TAINTED(90, 187.5, 30475, 30474, 2717, "Tainted gem", "Tainted gems"),
	ZENYTE(89, 200.0, 19496, 19493, 7185, "zenyte", "zenyte");

	public final int lvlReq, uncutId, cutId, animId;
	public final double xp;
	public final String cutName, pluralName;

	Gem(int lvlReq, double xp, int uncutId, int cutId, int animId, String cutName, String pluralName) {
		this.lvlReq = lvlReq;
		this.xp = xp;
		this.uncutId = uncutId;
		this.cutId = cutId;
		this.animId = animId;
		this.cutName = cutName;
		this.pluralName = pluralName;
	}

	private void cut(Player player, Item uncutItem) {
		player.animate(animId);
		uncutItem.setId(cutId);
		double xpToGain = xp;
		float multiplier = 1;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ARTS_N_CRAFTS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ARTS_N_CRAFTS);
			ArtsnCrafts c = (ArtsnCrafts) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			multiplier += c.getExperienceBoost();
		}
		xpToGain *= multiplier;

		player.getStats().addXp(StatType.Crafting, xpToGain, true);
		DailyTasks.handleItemObtained(player, cutId, StatType.Crafting);
		if (cutId == 1601) {
			player.diamondsCut++;
			if (player.diamondsCut == Achievements.HOW_MANY_RINGS_DO_YOU_NEED.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.HOW_MANY_RINGS_DO_YOU_NEED.getAchievementName());
		}
	}

	public static void register() {
		for (Gem gem : values()) {
			SkillItem item = new SkillItem(gem.cutId).addAction((player, amount, event) -> {
				while (amount-- > 0) {
					if (!player.getInventory().hasId(CHISEL))
						break;
					Item uncut = player.getInventory().findItem(gem.uncutId);
					if (uncut == null)
						break;
					gem.cut(player, uncut);
					event.delay(2);
				}
				player.resetAnimation();
			});
			ItemItemAction.register(gem.uncutId, CHISEL, (player, uncutItem, chiselItem) -> {
				if (!player.getStats().check(StatType.Crafting, gem.lvlReq, CHISEL, gem.uncutId, "cut " + gem.pluralName))
					return;
				if (player.getInventory().hasMultiple(gem.uncutId)) {
					SkillDialogue.make(player, item);
					return;
				}
				gem.cut(player, uncutItem);
			});
		}
	}

}
