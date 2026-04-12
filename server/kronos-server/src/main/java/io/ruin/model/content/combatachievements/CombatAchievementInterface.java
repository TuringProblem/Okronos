package io.ruin.model.content.combatachievements;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.map.object.actions.ObjectAction;

import java.util.List;

public class CombatAchievementInterface {
	private final int INTERFACE_ID = 1130;
	private CombatAchievement.Tier tier;
	private FilterType filterType;
	public void open(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, INTERFACE_ID);
		loadCompletionCount(player);
		if(tier == null) {
			sendTier(player, CombatAchievement.Tier.EASY);
		} else {
			sendTier(player, tier);
		}

	}
	boolean typeDropdownOpen = false;
	private void toggleTypeDropDown(Player player, boolean force) {
		if(!typeDropdownOpen) {
			typeDropdownOpen = true;
			player.getPacketSender().setHidden(INTERFACE_ID, 871, false);
		} else {
			typeDropdownOpen = false;
			player.getPacketSender().setHidden(INTERFACE_ID, 871, true);
		}
		if(tierDropdownOpen && !force)
			toggleTierDropDown(player, true);
	}

	boolean tierDropdownOpen = false;
	private void toggleTierDropDown(Player player, boolean force) {
		if(!tierDropdownOpen) {
			tierDropdownOpen = true;
			player.getPacketSender().setHidden(INTERFACE_ID, 855, false);
		} else {
			tierDropdownOpen = false;
			player.getPacketSender().setHidden(INTERFACE_ID, 855, true);
		}
		if(typeDropdownOpen && !force)
			toggleTypeDropDown(player, true);
	}

	private void loadCompletionCount(Player player) {
		player.getPacketSender().sendString(INTERFACE_ID, 23, sendAchievementProgress(player, CombatAchievement.Tier.EASY));
		player.getPacketSender().sendString(INTERFACE_ID, 34, sendAchievementProgress(player, CombatAchievement.Tier.MEDIUM));
		player.getPacketSender().sendString(INTERFACE_ID, 45, sendAchievementProgress(player, CombatAchievement.Tier.HARD));
		player.getPacketSender().sendString(INTERFACE_ID, 56, sendAchievementProgress(player, CombatAchievement.Tier.ELITE));
		player.getPacketSender().sendString(INTERFACE_ID, 67, sendAchievementProgress(player, CombatAchievement.Tier.MASTER));
		player.getPacketSender().sendString(INTERFACE_ID, 78, sendAchievementProgress(player, CombatAchievement.Tier.GRANDMASTER));
	}

	private void sendTier(Player player, CombatAchievement.Tier tier) {
		this.tier = tier;
		int startingParentComp = 94;
		int startingSpriteComp = 96;
		int startingNameComp = 97;
		int startingDescComp = 98;
		List<CombatAchievements> achievements = player.combatAchievementsList.stream().filter(
			achievement -> achievement.getCombatAchievement().getTier() == tier &&
				((filterType == FilterType.COMPLETED) == player.combatAchievementStore.getOrDefault(achievement.ordinal(), false))
		).toList();


		for(int i = 94; i < 844; i+=5) {
			player.getPacketSender().setHidden(INTERFACE_ID, i, true);
		}
		for (CombatAchievements achievement : achievements) {
			player.getPacketSender().sendString(INTERFACE_ID, startingNameComp, achievement.getCombatAchievement().getName());
			player.getPacketSender().sendString(INTERFACE_ID, startingDescComp, achievement.getCombatAchievement().getDesc());
			player.getPacketSender().setHidden(INTERFACE_ID, startingParentComp, false);
			startingDescComp += 5;
			startingSpriteComp += 5;
			startingNameComp += 5;
			startingParentComp += 5;
		}
		player.getPacketSender().sendString(INTERFACE_ID, 853, tier.toString().substring(0, 1).toUpperCase() + tier.toString().substring(1).toLowerCase());
		if(tierDropdownOpen)
			toggleTierDropDown(player, false);
	}

	private void sendFilter(Player player, FilterType filterType) {
		this.filterType = filterType;
		sendTier(player, tier);
		toggleTypeDropDown(player, false);
		player.getPacketSender().sendString(INTERFACE_ID, 869, filterType == FilterType.UNCOMPLETED ? "Uncompleted" : "Completed");
	}

	private String sendAchievementProgress(Player player, CombatAchievement.Tier tier) {
		int completed = CombatAchievementSystem.getAchievementsCompletedByTier(player, tier);
		int total = CombatAchievementSystem.getCombatAchievementsOfType(tier);
		String colour;
		if (completed == 0) {
			colour = "FF0000";
		} else if (completed == total) {
			colour = "27ae60";
		} else {
			colour = "c2a532";
		}
		int barComp = -1;
		switch (tier) {
			case EASY -> barComp = 26;
			case MEDIUM -> barComp = 37;
			case HARD -> barComp = 48;
			case ELITE -> barComp = 59;
			case MASTER -> barComp = 70;
			case GRANDMASTER -> barComp = 81;
		}
		int barInterfaceHash = INTERFACE_ID << 16 | barComp;
		float width = (float) completed / total;
		float barWidth = (width * 70);
		if(completed == 0)
			barWidth = 0;
		player.getPacketSender().sendClientScript(10606, "Ii", barInterfaceHash, (int) barWidth);
		return "<col=" + colour + ">" + completed + "/" + total;
	}

	public static void register() {
		InterfaceHandler.register(1130, h -> {
			h.actions[870] = (SimpleAction) player -> player.getCombatAchievementInterface().toggleTypeDropDown(player, false);
			h.actions[868] = (SimpleAction) player -> player.getCombatAchievementInterface().toggleTypeDropDown(player, false);
			h.actions[854] = (SimpleAction) player -> player.getCombatAchievementInterface().toggleTierDropDown(player, false);
			h.actions[852] = (SimpleAction) player -> player.getCombatAchievementInterface().toggleTierDropDown(player, false);
			h.actions[20] = (SimpleAction) player -> player.getCombatAchievementInterface().sendTier(player, CombatAchievement.Tier.EASY);
			h.actions[859] = (SimpleAction) player -> player.getCombatAchievementInterface().sendTier(player, CombatAchievement.Tier.EASY);
			h.actions[31] = (SimpleAction) player -> player.getCombatAchievementInterface().sendTier(player, CombatAchievement.Tier.MEDIUM);
			h.actions[860] = (SimpleAction) player -> player.getCombatAchievementInterface().sendTier(player, CombatAchievement.Tier.MEDIUM);
			h.actions[42] = (SimpleAction) player -> player.getCombatAchievementInterface().sendTier(player, CombatAchievement.Tier.HARD);
			h.actions[861] = (SimpleAction) player -> player.getCombatAchievementInterface().sendTier(player, CombatAchievement.Tier.HARD);
			h.actions[53] = (SimpleAction) player -> player.getCombatAchievementInterface().sendTier(player, CombatAchievement.Tier.ELITE);
			h.actions[862] = (SimpleAction) player -> player.getCombatAchievementInterface().sendTier(player, CombatAchievement.Tier.ELITE);
			h.actions[64] = (SimpleAction) player -> player.getCombatAchievementInterface().sendTier(player, CombatAchievement.Tier.MASTER);
			h.actions[863] = (SimpleAction) player -> player.getCombatAchievementInterface().sendTier(player, CombatAchievement.Tier.MASTER);
			h.actions[75] = (SimpleAction) player -> player.getCombatAchievementInterface().sendTier(player, CombatAchievement.Tier.GRANDMASTER);
			h.actions[864] = (SimpleAction) player -> player.getCombatAchievementInterface().sendTier(player, CombatAchievement.Tier.GRANDMASTER);
			h.actions[875] = (SimpleAction) player -> player.getCombatAchievementInterface().sendFilter(player, FilterType.UNCOMPLETED);
			h.actions[876] = (SimpleAction) player -> player.getCombatAchievementInterface().sendFilter(player, FilterType.COMPLETED);
		});
	}

	enum FilterType {
		COMPLETED,
		UNCOMPLETED
	}
}
