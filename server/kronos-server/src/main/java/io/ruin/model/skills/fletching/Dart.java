package io.ruin.model.skills.fletching;

import io.ruin.cache.ObjType;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.FletchingDreams;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.dialogue.skill.SkillItem;

import static io.ruin.model.skills.Tool.FEATHER;

public enum Dart {

	BRONZE_DART(806, 819, 1, 1.8, 8482),
	IRON_DART(807, 820, 22, 3.8, 8483),
	STEEL_DART(808, 821, 37, 7.5, 8484),
	MITHRIL_DART(809, 822, 52, 11.2, 8485),
	ADAMANT_DART(810, 823, 67, 15.0, 8486),
	RUNE_DART(811, 824, 81, 18.8, 8487),
	AMETHYST_DART(25849, 25853, 90, 21.0, 8488),
	DRAGON_DART(11230, 11232, 95, 25.0, 8488),

	BRONZE_BOLT(877, 9375, 9, 0.5, 8463),
	IRON_BOLT(9140, 9377, 39, 1.5, 8464),
	SILVER_BOLT(9145, 9382, 43, 3.0, 8467),
	STEEL_BOLT(9141, 9378, 46, 3.5, 8467),
	BROAD_BOLT(11875, 11876, 55, 2.5, 8467),
	MITHRIL_BOLT(9142, 9379, 54, 5.0, 8468),
	ADAMANT_BOLT(9143, 9380, 61, 7.0, 8469),
	RUNE_BOLT(9144, 9381, 69, 10.0, 8470),
	AMETHYST_BROAD_BOLTS(21316, 21338, 76, 10.6, 8470),
	DRAGON_BOLTS(21905, 21930, 84, 12.0, 8471),
	;

	public final int lvlReq;

	public final double xp;

	public final int unfinishedId, finishedId, animationId;

	public final String pluralName;

	Dart(int dartId, int tipId, int lvlReq, double xp, int animationId) {
		this.lvlReq = lvlReq;
		this.xp = xp;
		this.unfinishedId = tipId;
		this.finishedId = dartId;
		this.animationId = animationId;
		this.pluralName = ObjType.get(dartId).name.toLowerCase() + (ObjType.get(dartId).name.endsWith("s") ? "" : "s");
	}

	private void make(Player player, Item tipItem, Item featherItem) {
		if (VarPlayerRepository.SETTINGS_MAKE_X_DARTS_AND_BOLTS.get(player) == 1) {
			makeDialogue(player, tipItem, featherItem);
			return;
		}
		makeSet(player, tipItem, featherItem);
	}

	private void makeDialogue(Player player, Item tipItem, Item featherItem) {
		var skillItem = new SkillItem(finishedId).addAction((p, amount, event) -> {
			makeLoop(player, tipItem, featherItem, amount);
		});
		var dialogue = new SkillDialogue("How many sets of 10 do you wish to feather?", skillItem)
				.maxAmount(10)
				.noMakeX();
		player.dialogue(dialogue);
	}

	private void makeLoop(Player player, Item tipItem, Item featherItem, int sets) {
		player.startEvent(e -> {
			var count = sets;
			while (count-- > 0) {
				if (!makeSet(player, tipItem, featherItem)) {
					break;
				}
				e.delay(1);
			}
		});
	}

	private boolean makeSet(Player player, Item tipItem, Item featherItem) {
		if (!player.getStats().check(StatType.Fletching, lvlReq, finishedId, "make " + pluralName))
			return false;
		if (this == BROAD_BOLT && VarPlayerRepository.BROADER_FLETCHING.get(player) == 0) {
			player.sendMessage("You haven't unlocked the ability to fletch broad bolts yet.");
			return false;
		}
		var tipItemAmount = player.getInventory().count(tipItem.getId());
		var featherItemAmount = player.getInventory().count(featherItem.getId());

		var supplyCount = Math.min(tipItemAmount, featherItemAmount);
		var amount = Math.min(supplyCount, 10);

		var inv = player.getInventory();

		if (!inv.contains(tipItem.getId(), amount)) {
			player.sendMessage("You ran out of tips.");
			return false;
		}

		if (!inv.contains(featherItem.getId(), amount)) {
			player.sendMessage("You ran out of feathers.");
			return false;
		}

		inv.remove(tipItem.getId(), amount);
		inv.remove(featherItem.getId(), amount);
		inv.add(finishedId, amount);

		player.animate(animationId);
		double newExp = xp;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.FLETCHING_DREAMS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.FLETCHING_DREAMS);
			FletchingDreams c = (FletchingDreams) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			float multiplier = 1;
			multiplier += c.getExpBoost();
			newExp *= multiplier;
		}
		player.getStats().addXp(StatType.Fletching, newExp * amount, true);
		player.sendFilteredMessage("You make " + amount + " " + pluralName + ".");
		if (finishedId == 11230) {
			player.dragonDartsFletched += amount;
			if (player.dragonDartsFletched == Achievements.SMALL_BUT_DEADLY.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
						+ Achievements.SMALL_BUT_DEADLY.getAchievementName());
		}

		return true;
	}

	public static void register() {
		for (Dart dart : values())
			ItemItemAction.register(dart.unfinishedId, FEATHER, dart::make);
	}

}
