package io.ruin.model.inter.handlers;


import io.ruin.api.utils.StringUtils;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.Common;
import lombok.Getter;

import java.util.Arrays;


enum DropDownData {
	// Activities
	LMS_FOG(81, VarPlayerRepository.LMS_FOG_COLOUR, 37, 37),
	// Audio
	MUSIC_AREA(110, VarPlayerRepository.MUSIC_AREA_MODE, 4, 42),
	// Chat
	FRIEND_LOGIN_LOGOUT_MESSAGES(44, VarPlayerRepository.FRIEND_LOGIN_LOUT_MESSAGES, 2, 46),
	// Controls
	GAME_CLIENT_LAYOUT(69, VarPlayerRepository.WARN_ANNAKARL_TAB, 1, 153), // wrong child id, probably
	PLAYER_ATTACK_OPTION(55, VarPlayerRepository.PLAYER_ATTACK_OPTION, -1, 100),
	NPC_ATTACK_OPTION(56, VarPlayerRepository.NPC_ATTACK_OPTION, 2, 101),
	CTRL_CLICK(207, VarPlayerRepository.CTRLCLICK_INVERT_RUN, 15, 114),
	// Controls > Keybindings, left
	COMBAT(16, VarPlayerRepository.KEYBINDS[0], 22, 121),
	STATS(19, VarPlayerRepository.KEYBINDS[1], 25, 124),
	QUESTS(22, VarPlayerRepository.KEYBINDS[2], 28, 127),
	INVENTORY(25, VarPlayerRepository.KEYBINDS[3], 31, 130),
	EQUIPMENT(28, VarPlayerRepository.KEYBINDS[4], 34, 133),
	// middle
	PRAYER(17, VarPlayerRepository.KEYBINDS[5], 23, 122),
	MAGIC(20, VarPlayerRepository.KEYBINDS[6], 26, 125),
	FRIENDS(23, VarPlayerRepository.KEYBINDS[7], 29, 128),
	ACCOUNT_MANAGEMENT(26, VarPlayerRepository.KEYBINDS[8], 32, 131),
	LOGOUT(29, VarPlayerRepository.KEYBINDS[9], 35, 134),
	// right
	OPTIONS(18, VarPlayerRepository.KEYBINDS[10], 24, 123),
	EMOTES(21, VarPlayerRepository.KEYBINDS[11], 27, 126),
	CLAN(24, VarPlayerRepository.KEYBINDS[12], 30, 129),
	MUSIC(27, VarPlayerRepository.KEYBINDS[13], 33, 132),
	// interfaces
	//SCREEN_RESIZE(12, ?, 5), handle as a Special case as it doesnt use a config
	QUEST_LIST_SORTING(214, VarPlayerRepository.QUEST_LINE_SORTING, 18, 170),
	QUEST_LACK_REQUIREMENTS(215, VarPlayerRepository.SHOW_QUESTS_YOU_LACK_THE_REQUIREMENTS_FOR, 19, 171),
	QUEST_LACK_RECOMMENDATIONS(216, VarPlayerRepository.SHOW_QUESTS_YOU_LACK_THE_RECOMMENDATIONS_FOR, 20, 172),
	//    QUEST_TEXT_SIZE(222, Config.QUEST_LIST_TEXT_SIZE, 27), // 27
	CHAT_BOX_SCROLLBAR(1, VarPlayerRepository.CHATBOX_SCROLLBAR, 45, 197),
	//PANEL_VISUAL_APPEARANCE(86, Config.SIDE_PANEL_VISUAL_APPEARANCE_MODERN_LAYOUT, )
	;

	@Getter
	private final int childId;
	@Getter
	private final VarPlayerRepository config;
	@Getter
	private final int slot;
	@Getter
	private final int searchSlot;

	DropDownData(int childId, VarPlayerRepository config, int slot, int searchSlot) {
		this.childId = childId;
		this.config = config;
		this.slot = slot;
		this.searchSlot = searchSlot;
	}

	@Override
	public String toString() {
		return StringUtils.formatNoun(this.name().replace('_', ' '));
	}

	/**
	 * It takes an integer as an argument and returns a DropDownData object that has a childId that matches the integer
	 * argument
	 *
	 * @param childId The id of the child element.
	 * @return The first element in the stream that matches the predicate.
	 */
	static DropDownData getDataByChild(int childId) {
		return Arrays.stream(DropDownData.values()).filter(dropDownData -> dropDownData.getChildId() == childId).findAny().orElse(null);
	}

	/**
	 * It takes a config and returns the dropdown data that matches the config
	 *
	 * @param config The config that the dropdown data is for.
	 * @return The first element in the stream that matches the predicate.
	 */
	static DropDownData getDataByConfig(VarPlayerRepository config) {
		return Arrays.stream(DropDownData.values()).filter(dropDownData -> dropDownData.getConfig() == config).findAny().orElse(null);
	}

	/**
	 * It takes an integer as an argument and returns a DropDownData object that has the same slot value as the
	 * integer argument
	 *
	 * @param slot The search slot is the number that is used to identify the drop down data.
	 * @return The first element in the stream that matches the searchSlot.
	 */
	static DropDownData getDataBySlot(int slot) {
		return Arrays.stream(DropDownData.values()).filter(dropDownData -> dropDownData.getSlot() == slot).findFirst().orElse(null);
	}

	/**
	 * It takes an integer as an argument and returns a DropDownData object that has the same searchSlot value as the
	 * integer argument
	 *
	 * @param searchSlot The search slot is the number that is used to identify the drop down data.
	 * @return The first element in the stream that matches the searchSlot.
	 */
	static DropDownData getDataBySearchSlot(int searchSlot) {
		return Arrays.stream(DropDownData.values()).filter(dropDownData -> dropDownData.getSearchSlot() == searchSlot).findFirst().orElse(null);
	}

	static DropDownData getToggleData(int searchSlot) {
		return Arrays.stream(values()).filter(t -> t.getSearchSlot() == searchSlot).findAny().orElse(null);
	}

	/**
	 * Get the ToggleData object that has the given slot and page.
	 *
	 * @param slot The slot of the item in the inventory
	 * @param page The page the toggle is on.
	 * @return A ToggleData object
	 */
	static DropDownData getToggleData(int slot, int page) {
		return Arrays.stream(values()).filter(t -> t.getSlot() == slot && t.searchSlot == page).findAny().orElse(null);
	}

}
