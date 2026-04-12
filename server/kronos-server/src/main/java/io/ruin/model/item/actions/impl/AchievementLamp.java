package io.ruin.model.item.actions.impl;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.StringUtils;
import io.ruin.cache.Color;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.stat.StatType;

@SuppressWarnings("unused")
public enum AchievementLamp {

	ATTACK(3, 26, StatType.Attack, true, false),
	STRENGTH(4, 27, StatType.Strength, true, false),
	RANGED(5, 28, StatType.Ranged, true, false),
	MAGIC(6, 29, StatType.Magic, true, false),
	DEFENCE(7, 30, StatType.Defence, true, false),
	HITPOINTS(8, 31, StatType.Hitpoints, true, false),
	PRAYER(9, 32, StatType.Prayer, true, false),
	AGILITY(10, 33, StatType.Agility, false, false),
	HERBLORE(11, 34, StatType.Herblore, false, false),
	THIEVING(12, 35, StatType.Thieving, false, false),
	CRAFTING(13, 36, StatType.Crafting, false, false),
	RUNECRAFTING(14, 37, StatType.Runecrafting, false, false),
	SLAYER(22, 38, StatType.Slayer, false, false),
	FARMING(23, 39, StatType.Farming, false, false),
	MINING(15, 40, StatType.Mining, false, false),
	SMITHING(16, 41, StatType.Smithing, false, false),
	FISHING(17, 42, StatType.Fishing, false, false),
	COOKING(18, 43, StatType.Cooking, false, false),
	FIREMAKING(19, 44, StatType.Firemaking, false, false),
	WOODCUTTING(20, 45, StatType.Woodcutting, false, false),
	FLETCHING(21, 46, StatType.Fletching, false, false),
	CONSTRUCTION(24, 48, StatType.Construction, false, false),
	HUNTER(25, 47, StatType.Hunter, false, false);

	private int childId;
	private StatType statType;
	private boolean combat;
	private boolean disabled;
	private int hiddenChildId;

	AchievementLamp(int childId, int hiddenChildId, StatType statType, boolean combat, boolean disabled) {
		this.childId = childId;
		this.statType = statType;
		this.combat = combat;
		this.disabled = disabled;
		this.hiddenChildId = hiddenChildId;
	}

	public static final AchievementLamp[] VALUES = values();


	public static final int SKILL_CAMP = 30470;

	public static void register() {
		ItemAction.registerInventory(SKILL_CAMP, "rub", (player, item) -> {
			if (player.experienceLock) {
				player.sendMessage("Your experience is currently locked.");
				return;
			}
			player.openInterface(ToplevelComponent.MAINMODAL, 1103);
			player.getPacketSender().sendSkillinterface("");
		});

		InterfaceHandler.register(1103, h -> {

			for (AchievementLamp skill : AchievementLamp.VALUES) {
				h.actions[skill.childId] = (SimpleAction) player -> {
					player.openInterface(ToplevelComponent.MAINMODAL, 1103);
					String skillName = StringUtils.getFormattedEnumName(skill);
					if (skill.disabled) {
						player.sendMessage(Color.DARK_RED.wrap(skillName + " is currently disabled!"));
						return;
					}
					for (AchievementLamp skills : AchievementLamp.VALUES) {
						player.getPacketSender().setHidden(1103, skills.hiddenChildId, true);
					}
					player.getPacketSender().setHidden(1103, skill.hiddenChildId, false);
					player.getPacketSender().sendString(1103, 75, "You will receive " + NumberUtils.formatNumber((long) (25000 * player.getDifficulty().GetExperienceBoost())) + "  experience in " + skillName + ".");
					player.getPacketSender().setHidden(1103, 75, false);
					player.openInterface(ToplevelComponent.MAINMODAL, 1103);
					player.selectedSkillLampSkill = skill.statType;
					player.getPacketSender().sendSkillinterface(skillName);
				};
			}
			h.actions[72] = (SimpleAction) player -> {
				player.closeInterface(ToplevelComponent.MAINMODAL);
			};
			h.actions[77] = (SimpleAction) player -> {
				Item lamp = player.getInventory().findItem(SKILL_CAMP);
				if (lamp == null)
					return;
				if (player.selectedSkillLampSkill == null) {
					player.sendMessage(Color.DARK_RED.wrap("You must select a skill before confirming."));
					return;
				}

				int experience = 25000;
				player.closeInterface(ToplevelComponent.MAINMODAL);
				lamp.remove();
				player.getStats().addXp(player.selectedSkillLampSkill, experience, true);
				long experienceNumber = (long) (experience * player.getDifficulty().GetExperienceBoost());
				player.sendMessage(Color.DARK_GREEN.wrap("You have been rewarded " + NumberUtils.formatNumber(experienceNumber) + " " + player.selectedSkillLampSkill.name() + " experience."));
			};
		});

	}
}
