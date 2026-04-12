package io.ruin.model.item.actions.impl;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.StringUtils;
import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.stat.StatType;

public enum SkillLamp {

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

	SkillLamp(int childId, int hiddenChildId, StatType statType, boolean combat, boolean disabled) {
		this.childId = childId;
		this.statType = statType;
		this.combat = combat;
		this.disabled = disabled;
		this.hiddenChildId = hiddenChildId;
	}

	public static final SkillLamp[] VALUES = values();


	public static final int SKILL_CAMP = 30470;
	private static int baseXp;
	private static int lampId;

	private static void handleSkill(Player player, StatType skill, Item lamp) {
		lamp.remove();
		player.getStats().addXp(skill, 5000, true);
		player.sendMessage("You have been rewarded " + NumberUtils.formatNumber((long) (5000 * player.getDifficulty().GetExperienceBoost())) + " " +  skill.name() + " experience.");
	}

	private static void showPageOne(Player player, Item lamp) {
		player.dialogue(new OptionsDialogue(
			new Option("Attack", () -> handleSkill(player, StatType.Attack, lamp)),
			new Option("Strength", () -> handleSkill(player, StatType.Strength, lamp)),
			new Option("Defence", () -> handleSkill(player, StatType.Defence, lamp)),
			new Option("Ranged", () -> handleSkill(player, StatType.Ranged, lamp)),
			new Option("Next", () -> showPageTwo(player, lamp))
		));
	}

	private static void showPageTwo(Player player, Item lamp) {
		player.dialogue(new OptionsDialogue(
			new Option("Magic", () -> handleSkill(player, StatType.Magic, lamp)),
			new Option("Hitpoints", () -> handleSkill(player, StatType.Hitpoints, lamp)),
			new Option("Prayer", () -> handleSkill(player, StatType.Prayer, lamp)),
			new Option("Previous", () -> showPageOne(player, lamp)),
			new Option("Nevermind.")
		));
	}

	public static void register() {
		ItemAction.registerInventory(30579, "rub", (player, item) -> {
			if (player.experienceLock) {
				player.sendMessage("Your experience is currently locked.");
				return;
			}
			baseXp = 250000;
			lampId = 30579;
			player.openInterface(ToplevelComponent.MAINMODAL, 1111);
			player.getPacketSender().sendSkillinterface("");
		});
		ItemAction.registerInventory(28800, "rub", (SkillLamp::showPageOne));
		ItemAction.registerInventory(30580, "rub", (player, item) -> {
			if (player.experienceLock) {
				player.sendMessage("Your experience is currently locked.");
				return;
			}
			baseXp = 350000;
			lampId = 30580;
			player.openInterface(ToplevelComponent.MAINMODAL, 1111);
			player.getPacketSender().sendSkillinterface("");
		});
		ItemAction.registerInventory(30581, "rub", (player, item) -> {
			if (player.experienceLock) {
				player.sendMessage("Your experience is currently locked.");
				return;
			}
			baseXp = 500000;
			lampId = 30581;
			player.openInterface(ToplevelComponent.MAINMODAL, 1111);
			player.getPacketSender().sendSkillinterface("");
		});
		ItemAction.registerInventory(21027, "commune", (player, item) -> {
			if (player.experienceLock) {
				player.sendMessage("Your experience is currently locked.");
				return;
			}
			baseXp = 666666;
			lampId = 21027;
			player.openInterface(ToplevelComponent.MAINMODAL, 1111);
			player.getPacketSender().sendSkillinterface("");
		});

		InterfaceHandler.register(1111, h -> {

			for (SkillLamp skill : SkillLamp.VALUES) {
				h.actions[skill.childId] = (SimpleAction) player -> {
					player.openInterface(ToplevelComponent.MAINMODAL, 1111);
					String skillName = StringUtils.getFormattedEnumName(skill);
					if (skill.disabled) {
						player.sendMessage(Color.DARK_RED.wrap(skillName + " is currently disabled!"));
						return;
					}
					for (SkillLamp skills : SkillLamp.VALUES) {
						player.getPacketSender().setHidden(1111, skills.hiddenChildId, true);
					}
					player.getPacketSender().setHidden(1111, skill.hiddenChildId, false);
					player.getPacketSender().sendString(1111, 75, "You will receive " + baseXp + " experience in " + skillName + ".");
					player.getPacketSender().setHidden(1111, 75, false);
					player.openInterface(ToplevelComponent.MAINMODAL, 1111);
					player.selectedSkillLampSkill = skill.statType;
					player.getPacketSender().sendSkillinterface(skillName);
				};
			}
			h.actions[72] = (SimpleAction) player -> {
				player.closeInterface(ToplevelComponent.MAINMODAL);
			};
			h.actions[77] = (SimpleAction) player -> {
				if (player.selectedSkillLampSkill == null) {
					player.sendMessage(Color.DARK_RED.wrap("You must select a skill before confirming."));
					return;
				}

				int experience = baseXp;
				player.closeInterface(ToplevelComponent.MAINMODAL);
				player.getInventory().remove(lampId, 1);
				player.getStats().addXp(player.selectedSkillLampSkill, experience, false);
				long experienceNumber = (long) (experience * player.getDifficulty().GetExperienceBoost());
				player.sendMessage(Color.DARK_GREEN.wrap("You have been rewarded " + baseXp + " " + player.selectedSkillLampSkill.name() + " experience."));
			};
		});

	}
}
