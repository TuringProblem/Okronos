package io.ruin.model.inter.handlers.advancedsettings;

import com.google.common.collect.ImmutableMap;
import io.ruin.model.entity.player.Player;

import java.util.List;
import java.util.Map;

/**
 * @author Kris | 10/06/2022
 */
@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class SettingVariables {
	private SettingVariables() {
	}

	public static final int SHOW_LESS_OR_MORE_INFORMATION_VARBIT = 9665;
	public static final int SETTINGS_SEARCH_LEFT_VARBIT = 9638;
	public static final int SETTINGS_SEARCH_RIGHT_VARBIT = 9639;
	public static final int SETTINGS_CURRENT_CATEGORY_VARBIT = 9656;
	public static final int CURRENT_SETTING_VARBIT_ID = 9657;
	public static final int COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID = 11959;
	public static final int FRIEND_LOGIN_LOGOUT_MESSAGE_TIMEOUT_VARBIT_ID = 1627;
	public static final int SIDE_PANELS_VARBIT_ID = 4607;
	public static final int MUSIC_VOLUME_MUTED_VARBIT_ID = 9666;
	public static final int SOUND_EFFECT_VOLUME_MUTED_VARBIT_ID = 9674;
	public static final int AREA_EFFECT_MUTED_VARBIT_ID = 9675;
	public static final int MINIMUM_LOOT_ITEM_VALUE_VARBIT_ID = 5400;
	public static final int MINIMUM_DROP_ITEM_VALUE_VARBIT_ID = 5412;
	public static final int MINIMUM_ALCH_TRIGGER_VALUE_VARBIT_ID = 6091;
	public static final int MAX_HITSPLATS_THRESHOLD_VARBIT_ID = 14195;
	public static final int HIGHLIGHT_ENTITIES_ON_MOUSEOVER_VARBIT_ID = 13088;
	public static final int HIGHLIGHT_AGILITY_SHORTCUTS_SHORTCUT_REQUIREMENTS_VARBIT_ID = 13136;
	public static final int BIRD_NEST_NOTIFICATION_VARBIT_ID = 13087;
	public static final int SLAYER_HELPER_VARBIT_ID = 13082;
	public static final int IRON_LOOT_RESTRICTION_INDICATOR_VARBIT_ID = 13039;
	public static final int IRON_LOOT_RESTRICTION_MESSAGES_VARBIT_ID = 13040;
	public static final int HIGHLIGHT_ENTITIES_ON_TAP_VARBIT_ID = 13666;
	public static final int VIBRATE_ON_INTERACTION_VARBIT_ID = 13831;
	public static final int VIBRATE_WHEN_MINIMENU_OPENS_VARBIT_ID = 13833;
	public static final int VIBRATE_ON_DRAG_VARBIT_ID = 13832;
	public static final int VIBRATE_WHEN_HOVERING_OVER_MINIMENU_ENTRIES_VARBIT_ID = 13834;
	public static final int PK_SKULL_PREVENTION_VARBIT_ID = 13131;
	public static final int ANTI_DRAG_VARBIT_ID = 12345;
	public static final int EXAMINE_PRICE_INFO_GRAND_EXCHANGE_VARBIT_ID = 13826;
	public static final int CHATBOX_MODE_SET_AUTOMATICALLY_VARBIT_ID = 13120;
	public static final int EXAMINE_PRICE_INFO_ALCHEMY_VARBIT_ID = 13827;
	public static final int LOGOUT_NOTIFIER_VARBIT_ID = 13083;
	public static final int HIDE_UNAVAILABLE_QUESTS_VARBIT_ID = 13890;
	public static final int POISON_DAMAGE_VARBIT_ID = 13084;
	public static final int HIDE_QUESTS_VARBIT_ID = 13774;
	public static final int HIDE_MINIQUESTS_VARBIT_ID = 13773;
	public static final int HIDE_QUEST_LIST_HEADERS_VARBIT_ID = 13889;
	public static final int SHOW_NUMBER_OF_OPTIONS_IN_MOUSEOVER_TOOLTIPS_VARBIT_ID = 13133;
	public static final int DISABLE_QUEST_LIST_TEXT_SHADOWS_VARBIT_ID = 13781;
	public static final int HIDE_COMPLETED_QUESTS_VARBIT_ID = 13777;
	public static final int HIDE_QUESTS_IN_PROGRESS_VARBIT_ID = 13775;
	public static final int HIDE_UNSTARTED_QUESTS_VARBIT_ID = 13776;
	public static final int ACCEPT_TRADE_DELAY_VARBIT_ID = 13130;
	public static final int XP_TRACKER_VARBIT_ID = 13089;
	public static final int DESERT_HEAT_DAMAGE_VARBIT_ID = 13134;
	public static final int SHOW_CONFIRMATION_WHEN_PAYING_FOR_ITEMS_FROM_GRAVESTONE_VARBIT_ID = 13113;
	public static final int WOODCUTTING_RESPAWN_TIMER_VARBIT_ID = 13086;
	public static final int ORE_RESPAWN_TIMER_VARBIT_ID = 13085;
	public static final int ANTI_DRAG_ENABLE_KEY_VARBIT_ID = 13828;
	public static final int CTRLCLICK_TO_INVERT_RUN_MODE_VARBIT_ID = 13132;
	public static final int ANTI_DRAG_DISABLE_KEY_VARBIT_ID = 13829;
	public static final int QUEST_LIST_TEXT_SIZE_VARBIT_ID = 13780;
	public static final int QUEST_LIST_SORTING_VARBIT_ID = 13772;
	public static final int SHOW_QUESTS_YOU_LACK_THE_REQUIREMENTS_FOR_VARBIT_ID = 13778;
	public static final int SHOW_QUESTS_YOU_LACK_THE_RECOMMENDED_STATS_FOR_VARBIT_ID = 13779;
	public static final int OPAQUE_IRON_GROUP_BROADCASTS_VARP_ID = 3193;
	public static final int TRANSPARENT_IRON_GROUP_BROADCASTS_VARP_ID = 3196;
	public static final int TRANSPARENT_IRON_GROUP_CHAT_VARP_ID = 3194;
	public static final int TRANSPARENT_CLAN_BROADCASTS_VARP_ID = 3195;
	public static final int OPAQUE_CLAN_BROADCASTS_VARP_ID = 3192;
	public static final int OPAQUE_IRON_GROUP_CHAT_VARP_ID = 3191;
	public static final int COMPLETED_QUEST_TEXT_COLOUR_VARP_ID = 3411;
	public static final int UNAVAILABLE_QUEST_TEXT_COLOUR_VARP_ID = 3412;
	public static final int UNSTARTED_QUEST_TEXT_COLOUR_VARP_ID = 3410;
	public static final int IN_PROGRESS_QUEST_TEXT_COLOUR_VARP_ID = 3409;
	public static final int HIDE_ROOFS_VARBIT_ID = 12378;
	public static final int MUSIC_UNLOCK_MESSAGE_VARBIT_ID = 10078;
	public static final int HITSPLAT_TINTING_VARBIT_ID = 10236;
	public static final int SHOW_WIKI_ENTITY_LOOKUP_VARBIT_ID = 10113;
	public static final int HIDE_DATA_ORBS_VARBIT_ID = 4084;
	public static final int MAX_HIT_HITSPLAT_DISPLAY_VARBIT_ID = 14196;
	public static final int MAX_HIT_HITSPLATS_THRESHOLD = 14195;
	public static final int TRANSPARENT_SIDE_PANEL_VARBIT_ID = 4609;
	public static final int SHOW_THE_REMAINING_XP_FOR_A_LEVEL_IN_THE_STATS_PANEL_VARBIT_ID = 4181;
	public static final int SHOW_PRAYER_TOOLTIPS_VARBIT_ID = 5711;
	public static final int SHOW_SPECIAL_ATTACK_TOOLTIP_VARBIT_ID = 5712;
	public static final int SHOW_BOSS_HEALTH_OVERLAY_VARBIT_ID = 12389;
	public static final int SHOW_NORMAL_HEALTH_OVERLAY_VARBIT_ID = 12390;
	public static final int TRANSPARENT_CHATBOX_VARBIT_ID = 4608;
	public static final int SCROLL_WHEEL_CAN_CHANGE_ZOOM_DISTANCE_VARBIT_ID = 6357;
	public static final int HIDE_PRIVATE_CHAT_WHEN_THE_CHATBOX_IS_HIDDEN_VARBIT_ID = 4089;
	public static final int LOOT_DROP_NOTIFICATIONS_VARBIT_ID = 5399;
	public static final int UNTRADEABLE_LOOT_NOTIFICATIONS_VARBIT_ID = 5402;
	public static final int FILTER_OUT_BOSS_KILLCOUNT_WITH_SPAMFILTER_VARBIT_ID = 4930;
	public static final int DROP_ITEM_WARNING_VARBIT_ID = 5411;
	public static final int CLICK_THROUGH_TRANSPARENT_CHATBOX_VARBIT_ID = 2570;
	public static final int SHOW_THE_STORE_BUTTON_ON_MOBILE_VARBIT_ID = 13036;
	public static final int SHOW_THE_STORE_BUTTON_ON_DESKTOP_VARBIT_ID = 13037;
	public static final int MIDDLE_MOUSE_BUTTON_CONTROLS_THE_CAMERA_VARBIT_ID = 4134;
	public static final int MOVE_FOLLOWER_OPTIONS_LOWER_DOWN_VARBIT_ID = 5599;
	public static final int SHIFT_CLICK_TO_DROP_ITEMS_VARBIT_ID = 11556;
	public static final int SHOW_THE_FUNCTION_BUTTON_VARBIT_ID = 6257;
	public static final int MODERN_LAYOUT_SIDE_PANEL_CAN_BE_CLOSED_BY_THE_HOTKEYS_VARBIT_ID = 4611;
	public static final int ESC_CLOSES_THE_CURRENT_INTERFACE_VARBIT_ID = 4681;
	public static final int ACCEPT_AID_VARBIT_ID = 4180;
	public static final int SHOW_WARNING_WHEN_CASTING_TELEPORT_TO_TARGET_VARBIT_ID = 236;
	public static final int SHOW_WARNING_WHEN_CASTING_DAREEYAK_TELEPORT_VARBIT_ID = 6284;
	public static final int SHOW_WARNING_WHEN_CASTING_CARRALLANGAR_TELEPORT_VARBIT_ID = 6285;
	public static final int SHOW_WARNING_WHEN_CASTING_ANNAKARL_TELEPORT_VARBIT_ID = 6286;
	public static final int SHOW_WARNING_WHEN_CASTING_GHORROCK_TELEPORT_VARBIT_ID = 6287;
	public static final int CASTING_ALCHEMY_SPELLS_ON_UNTRADEABLE_ITEMS_ALWAYS_TRIGGERS_A_WARNING_VARBIT_ID = 6092;
	public static final int SHOW_WARNING_WHEN_USING_TABLET_ICE_PLATEAU_VARBIT_ID = 2323;
	public static final int SHOW_WARNING_WHEN_USING_TABLET_CEMETERY_VARBIT_ID = 2322;
	public static final int SHOW_WARNING_WHEN_USING_TABLET_WILDERNESS_CRABS_VARBIT_ID = 3932;
	public static final int SHOW_WARNING_WHEN_USING_TABLET_DAREEYAK_VARBIT_ID = 3930;
	public static final int SHOW_WARNING_WHEN_USING_TABLET_CARRALLANGAR_VARBIT_ID = 2325;
	public static final int SHOW_WARNING_WHEN_USING_TABLET_ANNAKARL_VARBIT_ID = 2324;
	public static final int SHOW_WARNING_WHEN_USING_TABLET_GHORROCK_VARBIT_ID = 3931;
	public static final int ENABLE_PRECISE_TIMING_VARBIT_ID = 11866;
	public static final int ENABLE_SEPARATING_HOURS_VARBIT_ID = 11890;
	public static final int FOODPOTIONS_CAN_FORM_SUPPLY_PILES_ON_DEATH_VARBIT_ID = 11893;
	public static final int TILE_HIGHLIGHTING_VARBIT_ID = 12342;
	public static final int SHOW_MOUSEOVER_TOOLTIPS_VARBIT_ID = 12344;
	public static final int SHOW_MOUSEOVER_TEXT_VARBIT_ID = 12377;
	public static final int LIMIT_FRAMERATE_VARBIT_ID = 12345;
	public static final int DATA_ORBS_REGENERATION_INDICATORS_VARBIT_ID = 12346;
	public static final int CHAMBERS_OF_XERIC_HELPER_VARBIT_ID = 12347;
	public static final int AGILITY_HELPER_VARBIT_ID = 12379;
	public static final int HIGHLIGHT_AGILITY_OBSTACLES_VARBIT_ID = 12380;
	public static final int HIGHLIGHT_AGILITY_SHORTCUTS_VARBIT_ID = 12976;
	public static final int FISHING_SPOT_INDICATORS_VARBIT_ID = 12349;
	public static final int FISHING_SPOT_INDICATORS_TOOLS_ONLY_VARBIT_ID = 12350;
	public static final int FISHING_SPOT_INDICATORS_MOUSE_OVER_TOOLTIP_VARBIT_ID = 12351;
	public static final int SHOW_ATTACK_STYLE_VARBIT_ID = 12352;
	public static final int HOME_TELEPORT_COOLDOWN_VARBIT_ID = 12353;
	public static final int MINIGAME_TELEPORT_COOLDOWN_VARBIT_ID = 12354;
	public static final int DISPLAY_BUFF_BAR_VARBIT_ID = 9528;
	public static final int DISPLAY_FRAGMENTS_VARBIT_ID = 12383;
	public static final int TOOLTIPS_FOR_BUFFS_VARBIT_ID = 9529;
	public static final int TELEPORT_BLOCK_DURATION_VARBIT_ID = 12355;
	public static final int CHARGE_BUFF_DURATION_VARBIT_ID = 12356;
	public static final int GODWARS_ALTAR_COOLDOWN_VARBIT_ID = 12357;
	public static final int DRAGONFIRE_SHIELD_COOLDOWN_VARBIT_ID = 12358;
	public static final int IMBUED_HEART_COOLDOWN_VARBIT_ID = 12359;
	public static final int VENGEANCE_COOLDOWN_VARBIT_ID = 12360;
	public static final int VENGEANCE_ACTIVE_VARBIT_ID = 12361;
	public static final int STAMINA_DURATION_VARBIT_ID = 12362;
	public static final int PRAYER_ENHANCE_DURATION_VARBIT_ID = 12363;
	public static final int OVERLOAD_DURATION_VARBIT_ID = 12364;
	public static final int MAGIC_IMBUE_DURATION_VARBIT_ID = 12365;
	public static final int ABYSSAL_SIRE_STUN_DURATION_VARBIT_ID = 12366;
	public static final int FREEZEENTANGLED_DURATION_VARBIT_ID = 12367;
	public static final int STAFF_OF_THE_DEAD_SPECIAL_DURATION_VARBIT_ID = 12368;
	public static final int DIVINE_POTION_DURATIONS_VARBIT_ID = 12369;
	public static final int ANTIFIRE_POTION_DURATIONS_VARBIT_ID = 12370;
	public static final int ANTIVENOM_AND_POISON_POTION_DURATIONS_VARBIT_ID = 12371;
	public static final int ALWAYS_ON_TOP_VARBIT_ID = 12372;
	public static final int DISPLAY_MODIFIED_STATS_OVERLAY_VARBIT_ID = 12373;
	public static final int SHOW_TOOLTIPS_FOR_MODIFIED_STAT_OVERLAYS_VARBIT_ID = 12374;
	public static final int DISPLAY_RELATIVE_STAT_VALUE_VARBIT_ID = 12376;
	public static final int CORRUPTION_ACTIVE_AND_DURATION_VARBIT_ID = 12405;
	public static final int MARK_OF_DARKNESS_ACTIVE_VARBIT_ID = 12406;
	public static final int SHADOW_VEIL_ACTIVE_AND_DURATION_VARBIT_ID = 12407;
	public static final int DEATH_CHARGE_ACTIVE_AND_DURATION_VARBIT_ID = 12408;
	public static final int WARD_OF_ARCEUUS_ACTIVE_AND_DURATION_VARBIT_ID = 12409;
	public static final int RESURRECTION_ACTIVE_AND_DURATION_VARBIT_ID = 12410;
	public static final int COMBAT_ACHIEVEMENT_TASKS_FAILURE_VARBIT_ID = 12454;
	public static final int COMBAT_ACHIEVEMENT_TASKS_COMPLETION_POPUP_VARBIT_ID = 12455;
	public static final int COMBAT_ACHIEVEMENT_TASKS_REPEAT_COMPLETION_VARBIT_ID = 12456;
	public static final int COMBAT_ACHIEVEMENT_TASKS_REPEAT_FAILURE_VARBIT_ID = 12457;
	public static final int REMAINING_AMMO_VARBIT_ID = 12985;
	public static final int HIGHLIGHT_HOVERED_TILE_VARBIT_ID = 12977;
	public static final int HIGHLIGHT_CURRENT_TILE_VARBIT_ID = 12978;
	public static final int HIGHLIGHT_DESTINATION_TILE_VARBIT_ID = 12979;
	public static final int HIGHLIGHT_HOVERED_TILE_ALWAYS_ON_TOP_VARBIT_ID = 12980;
	public static final int HIGHLIGHT_CURRENT_TILE_ALWAYS_ON_TOP_VARBIT_ID = 12981;
	public static final int HIGHLIGHT_DESTINATION_TILE_ALWAYS_ON_TOP_VARBIT_ID = 12982;
	public static final int ENABLE_MINIMAP_ZOOM_VARBIT_ID = 12983;
	public static final int SHOW_CHAT_EFFECTS_VARP_ID = 171;
	public static final int SPLIT_FRIENDS_PRIVATE_CHAT_VARP_ID = 287;
	public static final int ENABLE_PROFANITY_FILTER_VARP_ID = 1074;
	public static final int SINGLE_MOUSE_BUTTON_MODE_VARP_ID = 170;
	public static final int INTERFACE_SCALING_VARBIT_ID = 11864;
	public static final int MUSIC_VOLUME_VARP_ID = 168;
	public static final int SOUND_EFFECT_VOLUME_VARP_ID = 169;
	public static final int AREA_SOUND_VOLUME_VARP_ID = 872;
	public static final int SCREEN_BRIGHTNESS_VARP_ID = 166;
	public static final int LAST_MAN_STANDING_FOG_COLOUR_VARBIT_ID = 11865;
	public static final int MUSIC_AREA_MODE_VARBIT_ID = 12233;
	public static final int FRIEND_LOGINLOGOUT_MESSAGES_VARBIT_ID = 12274;
	public static final int CHAT_TIMESTAMPS_VARBIT_ID = 12384;
	public static final int SELECT_FUNCTIONMODE_VARBIT_ID = 6255;
	public static final int CHAT_BOX_SCROLLBAR_POSITION_VARBIT_ID = 6374;
	public static final int MODERN_LAYOUT_SIDE_PANEL_VISUAL_APPEARANCE_VARBIT_ID = 12135;
	public static final int NUMBER_OF_MODIFIED_STATS_TO_SHOW_VARBIT_ID = 12375;
	public static final int PLAYER_ATTACK_OPTIONS_VARP_ID = 1107;
	public static final int NPC_ATTACK_OPTIONS_VARP_ID = 1306;
	public static final int COMBAT_TAB_KEYBIND_VARBIT_ID = 4675;
	public static final int PRAYER_TAB_KEYBIND_VARBIT_ID = 4680;
	public static final int SETTINGS_TAB_KEYBIND_VARBIT_ID = 4686;
	public static final int SKILLS_TAB_KEYBIND_VARBIT_ID = 4676;
	public static final int MAGIC_TAB_KEYBIND_VARBIT_ID = 4682;
	public static final int EMOTES_TAB_KEYBIND_VARBIT_ID = 4687;
	public static final int JOURNAL_TAB_KEYBIND_VARBIT_ID = 4677;
	public static final int FRIENDS_LIST_KEYBIND_VARBIT_ID = 4684;
	public static final int FRIENDS_CHAT_TAB_KEYBIND_VARBIT_ID = 4683;
	public static final int INVENTORY_TAB_KEYBIND_VARBIT_ID = 4678;
	public static final int ACCOUNT_MANAGEMENT_TAB_KEYBIND_VARBIT_ID = 6517;
	public static final int MUSIC_TAB_KEYBIND_VARBIT_ID = 4688;
	public static final int EQUIPMENT_TAB_KEYBIND_VARBIT_ID = 4679;
	public static final int LOGOUT_TAB_KEYBIND_VARBIT_ID = 4689;
	public static final int OPAQUE_PUBLIC_CHAT_VARP_ID = 2992;
	public static final int TRANSPARENT_PUBLIC_CHAT_VARP_ID = 3000;
	public static final int OPAQUE_PRIVATE_CHAT_VARP_ID = 2993;
	public static final int TRANSPARENT_PRIVATE_CHAT_VARP_ID = 3001;
	public static final int SPLIT_PRIVATE_CHAT_VARP_ID = 3008;
	public static final int OPAQUE_AUTO_CHAT_VARP_ID = 2994;
	public static final int TRANSPARENT_AUTO_CHAT_VARP_ID = 3002;
	public static final int OPAQUE_BROADCAST_VARP_ID = 2995;
	public static final int TRANSPARENT_BROADCAST_VARP_ID = 3003;
	public static final int SPLIT_BROADCAST_VARP_ID = 3009;
	public static final int OPAQUE_FRIEND_CHAT_VARP_ID = 2996;
	public static final int TRANSPARENT_FRIEND_CHAT_VARP_ID = 3004;
	public static final int OPAQUE_CLAN_CHAT_VARP_ID = 2997;
	public static final int TRANSPARENT_CLAN_CHAT_VARP_ID = 3005;
	public static final int OPAQUE_INCOMING_TRADE_REQUEST_VARP_ID = 2998;
	public static final int TRANSPARENT_INCOMING_TRADE_REQUEST_VARP_ID = 3006;
	public static final int OPAQUE_INCOMING_CHALLENGE_REQUEST_VARP_ID = 2999;
	public static final int TRANSPARENT_INCOMING_CHALLENGE_REQUEST_VARP_ID = 3007;
	public static final int OPAQUE_GUEST_CLAN_CHAT_VARP_ID = 3060;
	public static final int TRANSPARENT_GUEST_CLAN_CHAT_VARP_ID = 3061;
	public static final int TILE_HIGHLIGHT_COLOUR_VARP_ID = 3108;
	public static final int HIGHLIGHT_HOVERED_TILE_COLOUR_VARP_ID = 3155;
	public static final int HIGHLIGHT_CURRENT_TILE_COLOUR_VARP_ID = 3156;
	public static final int HIGHLIGHT_DESTINATION_TILE_COLOUR_VARP_ID = 3157;
	public static final int SHOW_ACTIVITY_ADVISER_VARBIT_ID = 5368;
	public static final int DISABLE_LEVEL_UP_INTERFACE_VARBIT_ID = 9452;
	public static final int MAKE_X_BOLT_DARTS_VARBIT_ID = 10971;

	//@formatter:off
	private static final Map<Integer, Integer> SLIDER_SETTING_VARBITS = ImmutableMap.<Integer, Integer>builder()
			.put(SettingStructs.INTERFACE_SCALING_STRUCT_ID, INTERFACE_SCALING_VARBIT_ID)
			.build();

	private static final Map<Integer, Integer> DROPDOWN_SETTING_VARBITS = ImmutableMap.<Integer, Integer>builder()
			.put(SettingStructs.LAST_MAN_STANDING_FOG_COLOUR_STRUCT_ID, LAST_MAN_STANDING_FOG_COLOUR_VARBIT_ID)
			.put(SettingStructs.MUSIC_AREA_MODE_STRUCT_ID, MUSIC_AREA_MODE_VARBIT_ID)
			.put(SettingStructs.FRIEND_LOGINLOGOUT_MESSAGES_STRUCT_ID, FRIEND_LOGINLOGOUT_MESSAGES_VARBIT_ID)
			.put(SettingStructs.CHAT_TIMESTAMPS_STRUCT_ID, CHAT_TIMESTAMPS_VARBIT_ID)
			.put(SettingStructs.SELECT_FUNCTIONMODE_STRUCT_ID, SELECT_FUNCTIONMODE_VARBIT_ID)
			.put(SettingStructs.CHAT_BOX_SCROLLBAR_POSITION_STRUCT_ID, CHAT_BOX_SCROLLBAR_POSITION_VARBIT_ID)
			.put(SettingStructs.MODERN_LAYOUT_SIDE_PANEL_VISUAL_APPEARANCE_STRUCT_ID, MODERN_LAYOUT_SIDE_PANEL_VISUAL_APPEARANCE_VARBIT_ID)
			.put(SettingStructs.NUMBER_OF_MODIFIED_STATS_TO_SHOW_STRUCT_ID, NUMBER_OF_MODIFIED_STATS_TO_SHOW_VARBIT_ID)
			.put(SettingStructs.WOODCUTTING_RESPAWN_TIMER_STRUCT_ID, WOODCUTTING_RESPAWN_TIMER_VARBIT_ID)
			.put(SettingStructs.ORE_RESPAWN_TIMER_STRUCT_ID, ORE_RESPAWN_TIMER_VARBIT_ID)
			.put(SettingStructs.ANTI_DRAG_ENABLE_KEY_STRUCT_ID, ANTI_DRAG_ENABLE_KEY_VARBIT_ID)
			.put(SettingStructs.CTRLCLICK_TO_INVERT_RUN_MODE_STRUCT_ID, CTRLCLICK_TO_INVERT_RUN_MODE_VARBIT_ID)
			.put(SettingStructs.ANTI_DRAG_DISABLE_KEY_STRUCT_ID, ANTI_DRAG_DISABLE_KEY_VARBIT_ID)
			.put(SettingStructs.QUEST_LIST_TEXT_SIZE_STRUCT_ID, QUEST_LIST_TEXT_SIZE_VARBIT_ID)
			.put(SettingStructs.QUEST_LIST_SORTING_STRUCT_ID, QUEST_LIST_SORTING_VARBIT_ID)
			.put(SettingStructs.SHOW_QUESTS_YOU_LACK_THE_REQUIREMENTS_FOR_STRUCT_ID, SHOW_QUESTS_YOU_LACK_THE_REQUIREMENTS_FOR_VARBIT_ID)
			.put(SettingStructs.SHOW_QUESTS_YOU_LACK_THE_RECOMMENDED_STATS_FOR_STRUCT_ID, SHOW_QUESTS_YOU_LACK_THE_RECOMMENDED_STATS_FOR_VARBIT_ID)
			.build();

	private static final Map<Integer, Integer> DROPDOWN_SETTING_VARPS = ImmutableMap.<Integer, Integer>builder()
			.put(SettingStructs.PLAYER_ATTACK_OPTIONS_STRUCT_ID, PLAYER_ATTACK_OPTIONS_VARP_ID)
			.put(SettingStructs.NPC_ATTACK_OPTIONS_STRUCT_ID, NPC_ATTACK_OPTIONS_VARP_ID)
			.build();

	private static final Map<Integer, Integer> KEYBIND_SETTING_VARBITS = ImmutableMap.<Integer, Integer>builder()
			.put(SettingStructs.COMBAT_TAB_KEYBIND_STRUCT_ID, COMBAT_TAB_KEYBIND_VARBIT_ID)
			.put(SettingStructs.PRAYER_TAB_KEYBIND_STRUCT_ID, PRAYER_TAB_KEYBIND_VARBIT_ID)
			.put(SettingStructs.SETTINGS_TAB_KEYBIND_STRUCT_ID, SETTINGS_TAB_KEYBIND_VARBIT_ID)
			.put(SettingStructs.SKILLS_TAB_KEYBIND_STRUCT_ID, SKILLS_TAB_KEYBIND_VARBIT_ID)
			.put(SettingStructs.MAGIC_TAB_KEYBIND_STRUCT_ID, MAGIC_TAB_KEYBIND_VARBIT_ID)
			.put(SettingStructs.EMOTES_TAB_KEYBIND_STRUCT_ID, EMOTES_TAB_KEYBIND_VARBIT_ID)
			.put(SettingStructs.JOURNAL_TAB_KEYBIND_STRUCT_ID, JOURNAL_TAB_KEYBIND_VARBIT_ID)
			.put(SettingStructs.FRIENDS_LIST_KEYBIND_STRUCT_ID, FRIENDS_LIST_KEYBIND_VARBIT_ID)
			.put(SettingStructs.FRIENDS_CHAT_TAB_KEYBIND_STRUCT_ID, FRIENDS_CHAT_TAB_KEYBIND_VARBIT_ID)
			.put(SettingStructs.INVENTORY_TAB_KEYBIND_STRUCT_ID, INVENTORY_TAB_KEYBIND_VARBIT_ID)
			.put(SettingStructs.ACCOUNT_MANAGEMENT_TAB_KEYBIND_STRUCT_ID, ACCOUNT_MANAGEMENT_TAB_KEYBIND_VARBIT_ID)
			.put(SettingStructs.MUSIC_TAB_KEYBIND_STRUCT_ID, MUSIC_TAB_KEYBIND_VARBIT_ID)
			.put(SettingStructs.EQUIPMENT_TAB_KEYBIND_STRUCT_ID, EQUIPMENT_TAB_KEYBIND_VARBIT_ID)
			.put(SettingStructs.LOGOUT_TAB_KEYBIND_STRUCT_ID, LOGOUT_TAB_KEYBIND_VARBIT_ID)
			.build();

	private static final Map<Integer, Integer> KEYBIND_SETTING_VARPS = ImmutableMap.<Integer, Integer>builder()
			.build();

	private static final Map<Integer, Integer> COLOUR_SETTING_VARBITS = ImmutableMap.<Integer, Integer>builder()
			.build();

	private static final Map<Integer, Integer> COLOUR_SETTING_VARPS = ImmutableMap.<Integer, Integer>builder()
			.put(SettingStructs.OPAQUE_PUBLIC_CHAT_STRUCT_ID, OPAQUE_PUBLIC_CHAT_VARP_ID)
			.put(SettingStructs.TRANSPARENT_PUBLIC_CHAT_STRUCT_ID, TRANSPARENT_PUBLIC_CHAT_VARP_ID)
			.put(SettingStructs.OPAQUE_PRIVATE_CHAT_STRUCT_ID, OPAQUE_PRIVATE_CHAT_VARP_ID)
			.put(SettingStructs.TRANSPARENT_PRIVATE_CHAT_STRUCT_ID, TRANSPARENT_PRIVATE_CHAT_VARP_ID)
			.put(SettingStructs.SPLIT_PRIVATE_CHAT_STRUCT_ID, SPLIT_PRIVATE_CHAT_VARP_ID)
			.put(SettingStructs.OPAQUE_AUTO_CHAT_STRUCT_ID, OPAQUE_AUTO_CHAT_VARP_ID)
			.put(SettingStructs.TRANSPARENT_AUTO_CHAT_STRUCT_ID, TRANSPARENT_AUTO_CHAT_VARP_ID)
			.put(SettingStructs.OPAQUE_BROADCAST_STRUCT_ID, OPAQUE_BROADCAST_VARP_ID)
			.put(SettingStructs.TRANSPARENT_BROADCAST_STRUCT_ID, TRANSPARENT_BROADCAST_VARP_ID)
			.put(SettingStructs.SPLIT_BROADCAST_STRUCT_ID, SPLIT_BROADCAST_VARP_ID)
			.put(SettingStructs.OPAQUE_FRIEND_CHAT_STRUCT_ID, OPAQUE_FRIEND_CHAT_VARP_ID)
			.put(SettingStructs.TRANSPARENT_FRIEND_CHAT_STRUCT_ID, TRANSPARENT_FRIEND_CHAT_VARP_ID)
			.put(SettingStructs.OPAQUE_CLAN_CHAT_STRUCT_ID, OPAQUE_CLAN_CHAT_VARP_ID)
			.put(SettingStructs.TRANSPARENT_CLAN_CHAT_STRUCT_ID, TRANSPARENT_CLAN_CHAT_VARP_ID)
			.put(SettingStructs.OPAQUE_INCOMING_TRADE_REQUEST_STRUCT_ID, OPAQUE_INCOMING_TRADE_REQUEST_VARP_ID)
			.put(SettingStructs.TRANSPARENT_INCOMING_TRADE_REQUEST_STRUCT_ID,	TRANSPARENT_INCOMING_TRADE_REQUEST_VARP_ID)
			.put(SettingStructs.OPAQUE_INCOMING_CHALLENGE_REQUEST_STRUCT_ID, OPAQUE_INCOMING_CHALLENGE_REQUEST_VARP_ID)
			.put(SettingStructs.TRANSPARENT_INCOMING_CHALLENGE_REQUEST_STRUCT_ID, TRANSPARENT_INCOMING_CHALLENGE_REQUEST_VARP_ID)
			.put(SettingStructs.OPAQUE_GUEST_CLAN_CHAT_STRUCT_ID, OPAQUE_GUEST_CLAN_CHAT_VARP_ID)
			.put(SettingStructs.TRANSPARENT_GUEST_CLAN_CHAT_STRUCT_ID, TRANSPARENT_GUEST_CLAN_CHAT_VARP_ID)
			.put(SettingStructs.TILE_HIGHLIGHT_COLOUR_STRUCT_ID, TILE_HIGHLIGHT_COLOUR_VARP_ID)
			.put(SettingStructs.HIGHLIGHT_HOVERED_TILE_COLOUR_STRUCT_ID, HIGHLIGHT_HOVERED_TILE_COLOUR_VARP_ID)
			.put(SettingStructs.HIGHLIGHT_CURRENT_TILE_COLOUR_STRUCT_ID, HIGHLIGHT_CURRENT_TILE_COLOUR_VARP_ID)
			.put(SettingStructs.HIGHLIGHT_DESTINATION_TILE_COLOUR_STRUCT_ID, HIGHLIGHT_DESTINATION_TILE_COLOUR_VARP_ID)
			.put(SettingStructs.OPAQUE_IRON_GROUP_BROADCASTS_STRUCT_ID, OPAQUE_IRON_GROUP_BROADCASTS_VARP_ID)
			.put(SettingStructs.TRANSPARENT_IRON_GROUP_BROADCASTS_STRUCT_ID, TRANSPARENT_IRON_GROUP_BROADCASTS_VARP_ID)
			.put(SettingStructs.TRANSPARENT_IRON_GROUP_CHAT_STRUCT_ID, TRANSPARENT_IRON_GROUP_CHAT_VARP_ID)
			.put(SettingStructs.TRANSPARENT_CLAN_BROADCASTS_STRUCT_ID, TRANSPARENT_CLAN_BROADCASTS_VARP_ID)
			.put(SettingStructs.OPAQUE_CLAN_BROADCASTS_STRUCT_ID, OPAQUE_CLAN_BROADCASTS_VARP_ID)
			.put(SettingStructs.OPAQUE_IRON_GROUP_CHAT_STRUCT_ID, OPAQUE_IRON_GROUP_CHAT_VARP_ID)
			.put(SettingStructs.COMPLETED_QUEST_TEXT_COLOUR_STRUCT_ID, COMPLETED_QUEST_TEXT_COLOUR_VARP_ID)
			.put(SettingStructs.UNAVAILABLE_QUEST_TEXT_COLOUR_STRUCT_ID, UNAVAILABLE_QUEST_TEXT_COLOUR_VARP_ID)
			.put(SettingStructs.UNSTARTED_QUEST_TEXT_COLOUR_STRUCT_ID, UNSTARTED_QUEST_TEXT_COLOUR_VARP_ID)
			.put(SettingStructs.IN_PROGRESS_QUEST_TEXT_COLOUR_STRUCT_ID, IN_PROGRESS_QUEST_TEXT_COLOUR_VARP_ID)
			.build();

	public static final List<Map<Integer, Integer>> ALL_VARP_SETTINGS = List.of(
			DROPDOWN_SETTING_VARPS,
			KEYBIND_SETTING_VARPS, COLOUR_SETTING_VARPS
	);

	public static final List<Map<Integer, Integer>> ALL_VARBIT_SETTINGS = List.of(
			SLIDER_SETTING_VARBITS, DROPDOWN_SETTING_VARBITS,
			KEYBIND_SETTING_VARBITS, COLOUR_SETTING_VARBITS
	);

	private static final SettingStructResolver TOGGLE_SETTING_VARPS_RESOLVER = new SettingStructResolvableMap<>(ToggleSettingVarps.values);
	private static final SettingStructResolver TOGGLE_SETTING_VARBITS_RESOLVER = new SettingStructResolvableMap<>(ToggleSettingVarBits.values);

	private static final SettingStructResolver SLIDER_SETTING_VARPS_RESOLVER = new SettingStructResolvableMap<>(SliderSettingVarps.values);
	private static final SettingStructResolver SLIDER_SETTING_VARBITS_RESOLVER = new IntIntMapStructResolver(SLIDER_SETTING_VARBITS);

	private static final SettingStructResolver DROPDOWN_SETTING_VARPS_RESOLVER = new IntIntMapStructResolver(DROPDOWN_SETTING_VARPS);
	private static final SettingStructResolver DROPDOWN_SETTING_VARBITS_RESOLVER = new IntIntMapStructResolver(DROPDOWN_SETTING_VARBITS);

	private static final SettingStructResolver KEYBIND_SETTING_VARPS_RESOLVER = new IntIntMapStructResolver(KEYBIND_SETTING_VARPS);
	private static final SettingStructResolver KEYBIND_SETTING_VARBITS_RESOLVER = new IntIntMapStructResolver(KEYBIND_SETTING_VARBITS);

	private static final SettingStructResolver COLOUR_SETTING_VARPS_RESOLVER = new IntIntMapStructResolver(COLOUR_SETTING_VARPS);
	private static final SettingStructResolver COLOUR_SETTING_VARBITS_RESOLVER = new IntIntMapStructResolver(COLOUR_SETTING_VARBITS);
	//@formatter:on

	public static int getVariableValue(Setting setting, Player player) {
		final SettingType type = setting.getType();
		switch (type) {
			case TOGGLE:
				return getVariableValue(setting, player, TOGGLE_SETTING_VARPS_RESOLVER, TOGGLE_SETTING_VARBITS_RESOLVER);
			case SLIDER:
				return getVariableValue(setting, player, SLIDER_SETTING_VARPS_RESOLVER, SLIDER_SETTING_VARBITS_RESOLVER);
			case DROPDOWN:
				return getVariableValue(setting, player, DROPDOWN_SETTING_VARPS_RESOLVER, DROPDOWN_SETTING_VARBITS_RESOLVER);
			case KEYBIND:
				return getVariableValue(setting, player, KEYBIND_SETTING_VARPS_RESOLVER, KEYBIND_SETTING_VARBITS_RESOLVER);
			case COLOUR:
				return getVariableValue(setting, player, COLOUR_SETTING_VARPS_RESOLVER, COLOUR_SETTING_VARBITS_RESOLVER);
			default:
				throw new IllegalArgumentException("Setting '" + setting.getId() + "' uses unsupported type '" + type + "'.");
		}
	}

	public static void setVariableValue(Setting setting, Player player, int value) {
		final SettingType type = setting.getType();
		switch (type) {
			case TOGGLE:
				setVariableValue(setting, player, value, TOGGLE_SETTING_VARPS_RESOLVER, TOGGLE_SETTING_VARBITS_RESOLVER);
				return;
			case SLIDER:
				setVariableValue(setting, player, value, SLIDER_SETTING_VARPS_RESOLVER, SLIDER_SETTING_VARBITS_RESOLVER);
				return;
			case DROPDOWN:
				setVariableValue(setting, player, value, DROPDOWN_SETTING_VARPS_RESOLVER, DROPDOWN_SETTING_VARBITS_RESOLVER);
				return;
			case KEYBIND:
				setVariableValue(setting, player, value, KEYBIND_SETTING_VARPS_RESOLVER, KEYBIND_SETTING_VARBITS_RESOLVER);
				return;
			case COLOUR:
				setVariableValue(setting, player, value, COLOUR_SETTING_VARPS_RESOLVER, COLOUR_SETTING_VARBITS_RESOLVER);
				return;
			default:
				throw new IllegalArgumentException("Setting '" + setting.getId() + "' uses unsupported type '" + type + "'.");
		}
	}

	private static int getVariableValue(Setting setting, Player player, SettingStructResolver varps,
			SettingStructResolver varbits) {
		final Integer varp = varps.forStruct(setting.getStructId());
		if (varp != null) {
			return player.varp(varp);
		}
		final Integer varbit = varbits.forStruct(setting.getStructId());
		if (varbit == null)
			throw new IllegalStateException("Setting '" + setting.getId() + "' does not define a variable.");
		return player.varbit(varbit);
	}

	private static void setVariableValue(Setting setting, Player player, int value, SettingStructResolver varps,
			SettingStructResolver varbits) {
		final Integer varp = varps.forStruct(setting.getStructId());
		if (varp != null) {
			player.debug("setting_varp: " + varp + "_" + value);
			player.varpSend(varp, value);
			return;
		}
		final Integer varbit = varbits.forStruct(setting.getStructId());
		if (varbit == null)
			throw new IllegalStateException("Setting '" + setting.getId() + "' does not define a variable.");

		player.debug("setting_varp_bit: " + varbit + " " + value);
		player.varbitSend(varbit, value);
	}

}
