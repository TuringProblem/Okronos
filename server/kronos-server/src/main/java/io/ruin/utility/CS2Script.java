package io.ruin.utility;

import io.ruin.model.entity.player.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author ReverendDread on 7/18/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project Kronos
 */
@RequiredArgsConstructor
@Getter
public enum CS2Script {

	// party leader ? 2 : 1, name|combatLevel|att|str|ranged|magic|def|hp|prayer|completedTheatres
	TOB_PARTYDETAILS_ADDMEMBER(2317, "is"),
	//name|combatLevel|att|str|ranged|magic|def|hp|prayer|completedTheatres
	TOB_PARTYDETAILS_ADDAPPLICANT(2321, "s"),
	//slot, name|memberSize|preferredSize|preferredCombatLevel
	TOB_PARTYLIST_ADDLINE(2340, "is"),
	//leader ? 2 : isMember ? 1 : isApplicant ? 3 : 4
	TOB_PARTYDETAILS_REFRESH(2323, "iii"),
	//name|name|name|name|name
	TOB_HUD_STATUSNAMES(2301, "sssss"),
	//
	TOB_HUD_FADE(2306, "iis"),
	//
	TOB_HUD_PORTAL(2307, "s"),
	//color transparency
	TOPLEVEL_MAINMODAL_OPEN(2524, "ii"),
	ACCOUNT_INFO_UPDATE(2498, "iii"),
	PLAYERMEMBER(828, "i"),
	WELCOME_CLICK(1080, "s"),
	MOWT_SETMODEL(233, "iiiiiiiii"),
	WELCOME_MEMBERS_GRAPHIC(3092, "is"),
	PVP_ICONS_LAYOUT(385, "i"),
	PVP_ICONS_COMLEVELRANGE(5224, "i"),
	HEALTH_REGEN_TIMER(4716, "ii"),
	HEALTH_REGEN_TIMER_UPDATE(4717, "i"),
	SPEC_REGEN_TIMER_UPDATE(4722, "i"),
	SPEC_REGEN_TIMER_OFF(4720, ""),
	SPEC_REGEN_TIMER(4721, "i"),
	SUMMARY_SIDEPANEL_TIMEPLAYED_TRANSMIT(3970, "iii"),
	SUMMARY_SIDEPANEL_COMBAT_LEVEL_TRANSMIT(3954, "iii"),
	STAT_BOOSTS_HUD_STATS_RESTORING_UPDATE(4519, ""),
	STAT_BOOSTS_HUD_STATS_RESTORING_TIMER(4517, "iii"),
	STAT_BOOSTS_HUD_BOOSTS_DRAINING_TIMER(4518, "ii"),
	SETUP_ENHANCED_CLIENT_FEATURES(876, "iiss"),
	FOSSIL_POOL_UPDATE(2014, "iiiiii"),
	FOSSIL_POOL_PROGRESS_UPDATE(2015, "i"),
	BUFF_BAR_2_INIT(5929, ""),
	BUFF_BAR_LAYOUT_REDRAW(5937, ""),
	SETTINGS_INTERFACE_SCALING(2358, "i"),
	WORLDMAP_TRANSMITDATA(1749, "cc"),
	SETTINGS_CLIENT_MODE(3998, "i"),
	CHATBOX_MULTI_INIT(58, "ss"),
	CHATBOX_EMPTY(1508, "s"),
	FADE_OVERLAY(948, "iiiii"),
	CC_DELETEALL(2249, "i"),
	ADD_OVERLAYTIMER_LOC(5474, "iiiiiii"),
	TOPLEVEL_MAINMODAL_BACKGROUND(917, "ii"),
	INTERFACE_INV_INIT(149, "iiiiiisssss"),
	SETTINGS_SETSEARCH(5968, "s"),
	CHATDEFAULT_RESTOREINPUT(2158, ""),
	MESOVERLAY(1974, "s"),
	toplevel_chatbox_resetbackground(2379, ""),

	DOOM_OF_MOKHAIOTL_REWARDS(149, "IviiiIsssss")
	;

	private final int scriptId;
	private final String argTypes;

	public void sendScript(Player player, Object... values) {
		player.getPacketSender().sendClientScript(getScriptId(), getArgTypes(), values);
	}

	@Override
	public String toString() {
		return "CS2Script{" +
			"scriptId=" + scriptId +
			", argTypes='" + argTypes + '\'' +
			'}';
	}

}
