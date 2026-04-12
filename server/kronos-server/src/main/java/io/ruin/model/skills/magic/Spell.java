package io.ruin.model.skills.magic;

import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.skills.magic.rune.RuneRemoval;
import io.ruin.model.skills.magic.spells.lunar.SpellbookSwap;
import io.ruin.model.stat.StatType;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class Spell {

	public BiConsumer<Player, Integer> clickAction;

	public BiConsumer<Player, Item> itemAction;

	public BiConsumer<Player, GameObject> objectAction;

	public BiConsumer<Player, Entity> entityAction;

	public int BlightedSack = -1;

	public void setBlightedSack(int id) {
		this.BlightedSack = id;
	}

	public final void registerClick(int lvlReq, double xp, boolean useXpMultiplier, Item[] runeItems,
			BiPredicate<Player, Integer> check) {
		clickAction = (p, i) -> {
			if (p.isLocked())
				return;
			if (!p.getStats().check(StatType.Magic, lvlReq, "cast this spell"))
				return;
			RuneRemoval r = null;
			if (runeItems != null && (r = RuneRemoval.get(p, runeItems)) == null
					&& !p.getInventory().hasItem(BlightedSack, 1)) {
				p.sendMessage("You don't have enough runes to cast this spell.");
				return;
			}
			if (check.test(p, i)) {
				if (BlightedSack != -1 && p.getInventory().hasItem(BlightedSack, 1)) {
					p.getInventory().remove(BlightedSack, 1);
				} else if (r != null) {
					r.remove();
				}
				if (lvlReq == 80) {
					assert runeItems != null;
					if (runeItems[0].getId() == 554) {
						if (p.getEquipment().get(3).getId() > 2414 && p.getEquipment().get(3).getId() < 2418) {
							p.chargesCastWearingGodStaff++;
							if (p.chargesCastWearingGodStaff == Achievements.PRAYERS_ANSWERED.getCompletionAmount())
								p.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
										+ Achievements.PRAYERS_ANSWERED.getAchievementName());
						}
					}
				}
				p.getStats().addXp(StatType.Magic, xp, useXpMultiplier);
				SpellbookSwap.resetBookPostCast(p);
			}
		};
	}

	public final void registerItem(
			int lvlReq, double xp, boolean useXpMultiplier,
			Item[] runeItems,
			BiPredicate<Player, Item> check) {
		itemAction = (p, i) -> {
			cast(p, i, lvlReq, xp, useXpMultiplier, runeItems, check);
		};
	}

	public void cast(Player player, Item item, int lvlReq, double xp, boolean useXpMultiplier, Item[] runeItems) {
		this.cast(player, item, lvlReq, xp, useXpMultiplier, runeItems, null);
	}

	public void cast(Player player, Item item, int lvlReq, double xp, boolean useXpMultiplier, Item[] runeItems,
			BiPredicate<Player, Item> check) {
		if (!castCheck(player, lvlReq, xp, useXpMultiplier, runeItems)) {
			return;
		}

		if (check != null && !check.test(player, item)) {
			return;
		}

		var runeRemoval = runeRemoval(player, runeItems);

		if (runeRemoval != null) {
			runeRemoval.remove();
		}

		player.getStats().addXp(StatType.Magic, xp, useXpMultiplier);
		SpellbookSwap.resetBookPostCast(player);
		return;
	}

	public boolean castCheck(Player p, int lvlReq, double xp, boolean useXpMultiplier, Item[] runeItems) {
		if (!p.getStats().check(StatType.Magic, lvlReq, "cast this spell")) {
			return false;
		}

		if (runeRemoval(p, runeItems) == null) {
			p.sendMessage("You don't have enough runes to cast this spell.");
			return false;
		}

		return true;
	}

	private RuneRemoval runeRemoval(Player player, Item[] runeItems) {
		if (runeItems != null) {
			return RuneRemoval.get(player, runeItems);
		}
		return null;
	}

	public final void registerEntity(int lvlReq, Item[] runeItems, BiPredicate<Player, Entity> check) {
		entityAction = (p, e) -> {
			if (!p.getStats().check(StatType.Magic, lvlReq, "cast this spell"))
				return;
			RuneRemoval r = null;
			if (runeItems != null && (r = RuneRemoval.get(p, runeItems)) == null) {
				p.sendMessage("You don't have enough runes to cast this spell.");
				return;
			}
			if (check.test(p, e)) {
				if (r != null)
					r.remove();
				SpellbookSwap.resetBookPostCast(p);
			}
		};
	}

	public final void registerObject(int lvlReq, Item[] runeItems, BiPredicate<Player, GameObject> check) {
		objectAction = (p, o) -> {
			if (!p.getStats().check(StatType.Magic, lvlReq, "cast this spell"))
				return;
			RuneRemoval r = null;
			if (runeItems != null && (r = RuneRemoval.get(p, runeItems)) == null) {
				p.sendMessage("You don't have enough runes to cast this spell.");
				return;
			}
			if (check.test(p, o)) {
				if (r != null)
					r.remove();
				SpellbookSwap.resetBookPostCast(p);
			}
		};
	}

}
