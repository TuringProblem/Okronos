package io.ruin.model.skills.crafting;

import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ArtsnCrafts;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.dialogue.skill.SkillItem;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.skills.Tool;
import io.ruin.model.stat.StatType;

public enum Amethyst {

	AMETHYST_BOLT_TIPS(21338, 83, 60.0, 15),
	AMETHYST_ARROW_TIPS(21350, 85, 60.0, 15),
	AMETHYST_JAVELIN_HEADS(21352, 87, 60.0, 5),
	AMETHYST_DART_TIPS(25853, 89, 60.0, 8);

	public final int itemID, levelReq, amount;
	public final double exp;

	Amethyst(int itemID, int levelReq, double exp, int amount) {
		this.itemID = itemID;
		this.levelReq = levelReq;
		this.exp = exp;
		this.amount = amount;
	}

	private static final int AMETHYST = 21347;

	private static void craft(Player player, Amethyst crystal, int amount) {
		player.closeInterfaces();
		if (!player.getStats().check(StatType.Crafting, crystal.levelReq, "make this"))
			return;

		int maxAmount = player.getInventory().getAmount(AMETHYST);
		if (maxAmount < amount)
			amount = maxAmount;

		final int amt = amount;
		player.startEvent(event -> {
			int made = 0;
			while (made++ < amt) {
				Item item = player.getInventory().findItem(AMETHYST);
				if (item == null)
					return;
				item.remove();
				player.getInventory().add(crystal.itemID, crystal.amount);
				player.animate(6295);
				double xpToGain = crystal.exp;
				float multiplier = 1;
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ARTS_N_CRAFTS)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ARTS_N_CRAFTS);
					ArtsnCrafts c = (ArtsnCrafts) player.getPlayerPerkHandler().
						getActivePerks(player).get(perkIndex).getPerk(player);
					multiplier += c.getExperienceBoost();
				}
				xpToGain *= multiplier;
				DailyTasks.handleItemObtained(player, crystal.itemID, StatType.Crafting);
				player.getStats().addXp(StatType.Crafting, xpToGain, true);
				if (crystal == AMETHYST_ARROW_TIPS) {
					player.amethystArrowTipsCrafted += crystal.amount;
					if (player.amethystArrowTipsCrafted == Achievements.COLOURFUL_TIPS.getCompletionAmount())
						player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.COLOURFUL_TIPS.getAchievementName());
				}
				event.delay(2);
			}
		});
	}

	public static void register() {
		ItemItemAction.register(AMETHYST, Tool.CHISEL, (player, primary, secondary) -> {
			SkillDialogue.make(player,
				new SkillItem(AMETHYST_BOLT_TIPS.itemID).addAction((p, amount, event) -> craft(p, AMETHYST_BOLT_TIPS, amount)),
				new SkillItem(AMETHYST_ARROW_TIPS.itemID).addAction((p, amount, event) -> craft(p, AMETHYST_ARROW_TIPS, amount)),
				new SkillItem(AMETHYST_JAVELIN_HEADS.itemID).addAction((p, amount, event) -> craft(p, AMETHYST_JAVELIN_HEADS, amount)),
				new SkillItem(AMETHYST_DART_TIPS.itemID).addAction((p, amount, event) -> craft(p, AMETHYST_DART_TIPS, amount)));
		});
	}
}
