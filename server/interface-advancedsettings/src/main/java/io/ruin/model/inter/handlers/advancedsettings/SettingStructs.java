package io.ruin.model.inter.handlers.advancedsettings;

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * @author Kris | 10/06/2022
 * A class containing the struct ids for all the settings that exist. This is used throughout the settings interface.
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class SettingStructs {
    public static final int SHOW_BOSS_HEALTH_OVERLAY_STRUCT_ID = 3002;
    public static final int DATA_ORBS_REGENERATION_INDICATORS_STRUCT_ID = 3009;
    public static final int FISHING_SPOT_INDICATORS_MOUSE_OVER_TOOLTIP_STRUCT_ID = 3015;
    public static final int LAST_MAN_STANDING_FOG_COLOUR_STRUCT_ID = 2854;
    public static final int HIGHLIGHT_ENTITIES_ON_MOUSEOVER_STRUCT_ID = 3738;
    public static final int HIGHLIGHT_DESTINATION_TILE_STRUCT_ID = 3647;
    public static final int HIGHLIGHT_DESTINATION_TILE_ALWAYS_ON_TOP_STRUCT_ID = 3653;
    public static final int HIGHLIGHT_HOVERED_TILE_COLOUR_STRUCT_ID = 3648;
    public static final int HIGHLIGHT_CURRENT_TILE_STRUCT_ID = 3646;
    public static final int HITSPLAT_TINTING_STRUCT_ID = 1147;
    public static final int MAX_HIT_HITSPLAT_DISPLAY_STRUCT_ID = 346;
    public static final int MAX_HIT_HITSPLATS_THRESHOLD_STRUCT_ID = 347;
    public static final int COMBAT_STRUCT_ID = 3626;
    public static final int FISHING_SPOT_INDICATORS_TOOLS_ONLY_STRUCT_ID = 3014;
    public static final int MISSING_NAME_STRUCT_ID = 3000;
    public static final int FISHING_SPOT_INDICATORS_STRUCT_ID = 3013;
    public static final int AGILITY_HELPER_STRUCT_ID = 3642;
    public static final int HIGHLIGHT_AGILITY_OBSTACLES_STRUCT_ID = 3643;
    public static final int HIGHLIGHT_AGILITY_SHORTCUTS_SHORTCUT_REQUIREMENTS_STRUCT_ID = 3758;
    public static final int HIGHLIGHT_AGILITY_SHORTCUTS_STRUCT_ID = 3644;
    public static final int BIRD_NEST_NOTIFICATION_STRUCT_ID = 3737;
    public static final int WOODCUTTING_RESPAWN_TIMER_STRUCT_ID = 3736;
    public static final int ORE_RESPAWN_TIMER_STRUCT_ID = 3735;
    public static final int CHAMBERS_OF_XERIC_HELPER_STRUCT_ID = 3011;
    public static final int TILE_HIGHLIGHT_COLOUR_STRUCT_ID = 3005;
    public static final int SLAYER_HELPER_STRUCT_ID = 3732;
    public static final int HIGHLIGHT_HOVERED_TILE_STRUCT_ID = 3645;
    public static final int SKILLS_STRUCT_ID = 3625;
    public static final int HIGHLIGHT_DESTINATION_TILE_COLOUR_STRUCT_ID = 3650;
    public static final int IRON_LOOT_RESTRICTION_INDICATOR_STRUCT_ID = 2731;
    public static final int HIGHLIGHT_CURRENT_TILE_COLOUR_STRUCT_ID = 3649;
    public static final int HIGHLIGHT_CURRENT_TILE_ALWAYS_ON_TOP_STRUCT_ID = 3652;
    public static final int IRON_LOOT_RESTRICTION_MESSAGES_STRUCT_ID = 3711;
    public static final int TILE_HIGHLIGHTING_STRUCT_ID = 3004;
    public static final int CLEAR_YOUR_HIGHLIGHTED_TILES_STRUCT_ID = 3010;
    public static final int SHOW_NORMAL_HEALTH_OVERLAY_STRUCT_ID = 3003;
    public static final int HIGHLIGHT_HOVERED_TILE_ALWAYS_ON_TOP_STRUCT_ID = 3651;
    public static final int MINIGAMES_STRUCT_ID = 3627;
    public static final int HIGHLIGHT_ENTITIES_ON_TAP_STRUCT_ID = 4149;
    public static final int SOUND_EFFECT_VOLUME_STRUCT_ID = 2754;
    public static final int MUSIC_VOLUME_STRUCT_ID = 2753;
    public static final int AREA_SOUND_VOLUME_STRUCT_ID = 2755;
    public static final int MUSIC_AREA_MODE_STRUCT_ID = 2974;
    public static final int MUSIC_UNLOCK_MESSAGE_STRUCT_ID = 2756;
    public static final int CONTROL_OPTIONS_STRUCT_ID = 3628;
    public static final int MUSIC_TAB_KEYBIND_STRUCT_ID = 2750;
    public static final int FRIENDS_LIST_KEYBIND_STRUCT_ID = 2746;
    public static final int SKILLS_TAB_KEYBIND_STRUCT_ID = 2742;
    public static final int MODERN_LAYOUT_SIDE_PANEL_CAN_BE_CLOSED_BY_THE_HOTKEYS_STRUCT_ID = 1053;
    public static final int SINGLE_MOUSE_BUTTON_MODE_STRUCT_ID = 2769;
    public static final int SHOW_THE_FUNCTION_BUTTON_STRUCT_ID = 2773;
    public static final int SELECT_FUNCTIONMODE_STRUCT_ID = 2851;
    public static final int ANTI_DRAG_DELAY_STRUCT_ID = 4278;
    public static final int ANTI_DRAG_ENABLE_KEY_STRUCT_ID = 4279;
    public static final int FRIENDS_CHAT_TAB_KEYBIND_STRUCT_ID = 2747;
    public static final int JOURNAL_TAB_KEYBIND_STRUCT_ID = 2745;
    public static final int SETTINGS_TAB_KEYBIND_STRUCT_ID = 2741;
    public static final int MAGIC_TAB_KEYBIND_STRUCT_ID = 2743;
    public static final int PRAYER_TAB_KEYBIND_STRUCT_ID = 2740;
    public static final int MINIMENU_LONGPRESS_TIME_STRUCT_ID = 4285;
    public static final int VIBRATE_ON_INTERACTION_STRUCT_ID = 4281;
    public static final int VIBRATE_WHEN_MINIMENU_OPENS_STRUCT_ID = 4283;
    public static final int VIBRATE_ON_DRAG_STRUCT_ID = 4282;
    public static final int COMBAT_TAB_KEYBIND_STRUCT_ID = 2739;
    public static final int KEYBINDS_STRUCT_ID = 3629;
    public static final int VIBRATE_WHEN_HOVERING_OVER_MINIMENU_ENTRIES_STRUCT_ID = 4284;
    public static final int LOGOUT_TAB_KEYBIND_STRUCT_ID = 2752;
    public static final int NPC_ATTACK_OPTIONS_STRUCT_ID = 2738;
    public static final int EMOTES_TAB_KEYBIND_STRUCT_ID = 2744;
    public static final int SHIFT_CLICK_TO_DROP_ITEMS_STRUCT_ID = 1104;
    public static final int CTRLCLICK_TO_INVERT_RUN_MODE_STRUCT_ID = 3762;
    public static final int DEADZONE_ADJUSTER_STRUCT_ID = 2774;
    public static final int INVENTORY_TAB_KEYBIND_STRUCT_ID = 2748;
    public static final int MOVE_FOLLOWER_OPTIONS_LOWER_DOWN_STRUCT_ID = 2771;
    public static final int ANTI_DRAG_DISABLE_KEY_STRUCT_ID = 4280;
    public static final int ACCOUNT_MANAGEMENT_TAB_KEYBIND_STRUCT_ID = 2749;
    public static final int PLAYER_ATTACK_OPTIONS_STRUCT_ID = 2737;
    public static final int PK_SKULL_PREVENTION_STRUCT_ID = 3760;
    public static final int EQUIPMENT_TAB_KEYBIND_STRUCT_ID = 2751;
    public static final int ANTI_DRAG_STRUCT_ID = 4277;
    public static final int ESC_CLOSES_THE_CURRENT_INTERFACE_STRUCT_ID = 2775;
    public static final int RESTORE_DEFAULT_KEYBINDS_STRUCT_ID = 2776;
    public static final int MIDDLE_MOUSE_BUTTON_CONTROLS_THE_CAMERA_STRUCT_ID = 2770;
    public static final int GAME_CHAT_STRUCT_ID = 941;
    public static final int OPAQUE_INCOMING_CHALLENGE_REQUEST_STRUCT_ID = 2912;
    public static final int OPAQUE_BROADCAST_STRUCT_ID = 2903;
    public static final int CHAT_COLOUR_SPLIT_STRUCT_ID = 2892;
    public static final int RESET_TRANSPARENT_CHAT_COLOURS_STRUCT_ID = 2915;
    public static final int ENABLE_SEPARATING_HOURS_STRUCT_ID = 2859;
    public static final int EXAMINE_PRICE_INFO_GRAND_EXCHANGE_STRUCT_ID = 4275;
    public static final int OPAQUE_AUTO_CHAT_STRUCT_ID = 2901;
    public static final int OPAQUE_FRIEND_CHAT_STRUCT_ID = 2906;
    public static final int COMBAT_ACHIEVEMENT_TASKS_FAILURE_STRUCT_ID = 323;
    public static final int COMBAT_ACHIEVEMENT_TASKS_REPEAT_FAILURE_STRUCT_ID = 326;
    public static final int OPAQUE_PRIVATE_CHAT_STRUCT_ID = 2898;
    public static final int COMBAT_ACHIEVEMENT_TASKS_REPEAT_COMPLETION_STRUCT_ID = 321;
    public static final int CHAT_COLOUR_TRANSPARENT_STRUCT_ID = 2891;
    public static final int TRANSPARENT_AUTO_CHAT_STRUCT_ID = 2902;
    public static final int OPAQUE_CLAN_CHAT_STRUCT_ID = 2908;
    public static final int TRANSPARENT_INCOMING_TRADE_REQUEST_STRUCT_ID = 2911;
    public static final int SPLIT_CHAT_OUTSIDE_CHATBOX_COLOUR = 2895;
    public static final int TRANSPARENT_INCOMING_CHALLENGE_REQUEST_STRUCT_ID = 2913;
    public static final int SPLIT_PRIVATE_CHAT_STRUCT_ID = 2900;
    public static final int OPAQUE_IRON_GROUP_BROADCASTS_STRUCT_ID = 3749;
    public static final int OPAQUE_INCOMING_TRADE_REQUEST_STRUCT_ID = 2910;
    public static final int RESET_OPAQUE_CHAT_COLOURS_STRUCT_ID = 2914;
    public static final int TRANSPARENT_IRON_GROUP_BROADCASTS_STRUCT_ID = 3750;
    public static final int TRANSPARENT_PRIVATE_CHAT_STRUCT_ID = 2899;
    public static final int TRANSPARENT_CHATBOX_CHAT_MESSAGES_COLOUR = 2894;
    public static final int TRANSPARENT_IRON_GROUP_CHAT_STRUCT_ID = 3746;
    public static final int TRANSPARENT_CLAN_BROADCASTS_STRUCT_ID = 3748;
    public static final int TRANSPARENT_CLAN_CHAT_STRUCT_ID = 2909;
    public static final int TRANSPARENT_FRIEND_CHAT_STRUCT_ID = 2907;
    public static final int TRANSPARENT_BROADCAST_STRUCT_ID = 2904;
    public static final int TRANSPARENT_GUEST_CLAN_CHAT_STRUCT_ID = 2976;
    public static final int OPAQUE_CLAN_BROADCASTS_STRUCT_ID = 3747;
    public static final int TRANSPARENT_PUBLIC_CHAT_STRUCT_ID = 2897;
    public static final int SPLIT_FRIENDS_PRIVATE_CHAT_STRUCT_ID = 2758;
    public static final int RESET_SPLIT_CHAT_COLOURS_STRUCT_ID = 2916;
    public static final int LOOT_DROP_NOTIFICATIONS_STRUCT_ID = 2761;
    public static final int MINIMUM_ITEM_VALUE_NEEDED_FOR_LOOT_NOTIFICATION_STRUCT_ID = 2762;
    public static final int MAKE_X_DART_BOLTS_STRUCT_ID = 990;
    public static final int SPLIT_BROADCAST_STRUCT_ID = 2905;
    public static final int OPAQUE_IRON_GROUP_CHAT_STRUCT_ID = 3745;
    public static final int OPAQUE_GUEST_CLAN_CHAT_STRUCT_ID = 2975;
    public static final int CHATBOX_MODE_SET_AUTOMATICALLY_STRUCT_ID = 3756;
    public static final int OPAQUE_PUBLIC_CHAT_STRUCT_ID = 2896;
    public static final int OPAQUE_CHATBOX_CHAT_MESSAGES_COLOUR = 2893;
    public static final int CHAT_COLOUR_OPAQUE_STRUCT_ID = 2890;
    public static final int FRIEND_LOGINLOGOUT_MESSAGES_STRUCT_ID = 2767;
    public static final int CHAT_TIMESTAMPS_STRUCT_ID = 3012;
    public static final int FILTER_OUT_BOSS_KILLCOUNT_WITH_SPAMFILTER_STRUCT_ID = 2764;
    public static final int UNTRADEABLE_LOOT_NOTIFICATIONS_STRUCT_ID = 2763;
    public static final int COLLECTION_LOG_NEW_ADDITION_NOTIFICATION_STRUCT_ID = 3640;
    public static final int EXAMINE_PRICE_INFO_ALCHEMY_STRUCT_ID = 4276;
    public static final int ENABLE_PROFANITY_FILTER_STRUCT_ID = 2760;
    public static final int SHOW_CHAT_EFFECTS_STRUCT_ID = 2757;
    public static final int ENABLE_PRECISE_TIMING_STRUCT_ID = 2855;
    public static final int HIDE_PRIVATE_CHAT_WHEN_THE_CHATBOX_IS_HIDDEN_STRUCT_ID = 2759;
    public static final int TELEPORTS_STRUCT_ID = 3637;
    public static final int OTHER_NOTIFICATIONS_STRUCT_ID = 3730;
    public static final int SHOW_WARNING_WHEN_CASTING_CARRALLANGAR_TELEPORT_STRUCT_ID = 2797;
    public static final int SHOW_WARNING_WHEN_USING_TABLET_CARRALLANGAR_STRUCT_ID = 2798;
    public static final int SHOW_WARNING_WHEN_USING_TABLET_ANNAKARL_STRUCT_ID = 2790;
    public static final int DISABLE_TELEPORT_WARNINGS_STRUCT_ID = 2794;
    public static final int TABLETS_STRUCT_ID = 3638;
    public static final int MINIMUM_ITEM_VALUE_NEEDED_FOR_ALCHEMY_SPELLS_WARNING_STRUCT_ID = 2784;
    public static final int LOGOUT_NOTIFIER_STRUCT_ID = 3733;
    public static final int CASTING_ALCHEMY_SPELLS_ON_UNTRADEABLE_ITEMS_ALWAYS_TRIGGERS_A_WARNING_STRUCT_ID = 2783;
    public static final int SHOW_WARNING_WHEN_USING_TABLET_WILDERNESS_CRABS_STRUCT_ID = 2787;
    public static final int SHOW_WARNING_WHEN_USING_TABLET_ICE_PLATEAU_STRUCT_ID = 2785;
    public static final int DISABLE_TABLET_WARNINGS_STRUCT_ID = 2792;
    public static final int ENABLE_TABLET_WARNINGS_STRUCT_ID = 2793;
    public static final int MINIMUM_ITEM_VALUE_NEEDED_FOR_DROP_ITEM_WARNING_STRUCT_ID = 2766;
    public static final int DROP_ITEM_WARNING_STRUCT_ID = 2765;
    public static final int ITEMS_STRUCT_ID = 3639;
    public static final int SHOW_WARNING_WHEN_CASTING_TELEPORT_TO_TARGET_STRUCT_ID = 2778;
    public static final int SHOW_WARNING_WHEN_CASTING_GHORROCK_TELEPORT_STRUCT_ID = 2782;
    public static final int SHOW_WARNING_WHEN_USING_TABLET_CEMETERY_STRUCT_ID = 2786;
    public static final int SHOW_WARNING_WHEN_USING_TABLET_GHORROCK_STRUCT_ID = 2791;
    public static final int SHOW_WARNING_WHEN_USING_TABLET_DAREEYAK_STRUCT_ID = 2788;
    public static final int TELEPORT_TABLET_WARNING_HEADER_STRUCT_ID = 968;
    public static final int TELEPORT_SPELL_WARNING_HEADER_STRUCT_ID = 996;
    public static final int SHOW_WARNING_WHEN_CASTING_DAREEYAK_TELEPORT_STRUCT_ID = 2779;
    public static final int ENABLE_TELEPORT_WARNINGS_STRUCT_ID = 2795;
    public static final int SHOW_WARNING_WHEN_CASTING_ANNAKARL_TELEPORT_STRUCT_ID = 2781;
    public static final int RESET_QUEST_LIST_TEXT_COLOURS_STRUCT_ID = 4252;
    public static final int CHARGE_BUFF_DURATION_STRUCT_ID = 3023;
    public static final int HIDE_UNAVAILABLE_QUESTS_STRUCT_ID = 2735;
    public static final int NUMBER_OF_MODIFIED_STATS_TO_SHOW_STRUCT_ID = 3042;
    public static final int DISPLAY_RELATIVE_STAT_VALUE_STRUCT_ID = 3043;
    public static final int CORRUPTION_ACTIVE_AND_DURATION_STRUCT_ID = 3112;
    public static final int POISON_DAMAGE_STRUCT_ID = 3734;
    public static final int SHOW_DATA_ORBS_STRUCT_ID = 2154;
    public static final int SHOW_MOUSEOVER_TEXT_STRUCT_ID = 3007;
    public static final int MAGIC_IMBUE_DURATION_STRUCT_ID = 3032;
    public static final int PRAYER_ENHANCE_DURATION_STRUCT_ID = 3030;
    public static final int TELEPORT_BLOCK_DURATION_STRUCT_ID = 3022;
    public static final int HIDE_QUESTS_STRUCT_ID = 3753;
    public static final int GODWARS_ALTAR_COOLDOWN_STRUCT_ID = 3024;
    public static final int QUEST_LIST_TEXT_SIZE_STRUCT_ID = 3754;
    public static final int QUEST_LIST_STRUCT_ID = 2255;
    public static final int QUEST_LIST_SORTING_STRUCT_ID = 3107;
    public static final int HIDE_MINIQUESTS_STRUCT_ID = 3752;
    public static final int SHOW_QUESTS_YOU_LACK_THE_REQUIREMENTS_FOR_STRUCT_ID = 3108;
    public static final int SHOW_THE_REMAINING_XP_FOR_A_LEVEL_IN_THE_STATS_PANEL_STRUCT_ID = 1055;
    public static final int DRAGONFIRE_SHIELD_COOLDOWN_STRUCT_ID = 3025;
    public static final int SHOW_SPECIAL_ATTACK_TOOLTIP_STRUCT_ID = 1233;
    public static final int MINIGAME_TELEPORT_COOLDOWN_STRUCT_ID = 3021;
    public static final int HIDE_QUEST_LIST_HEADERS_STRUCT_ID = 4286;
    public static final int DISPLAY_MODIFIED_STATS_OVERLAY_STRUCT_ID = 3040;
    public static final int BUFF_BAR_STRUCT_ID = 3634;
    public static final int SHOW_TOOLTIPS_FOR_MODIFIED_STAT_OVERLAYS_STRUCT_ID = 3041;
    public static final int HOME_TELEPORT_COOLDOWN_STRUCT_ID = 3020;
    public static final int DISPLAY_BUFF_BAR_STRUCT_ID = 3017;
    public static final int COMPLETED_QUEST_TEXT_COLOUR_STRUCT_ID = 4250;
    public static final int UNAVAILABLE_QUEST_TEXT_COLOUR_STRUCT_ID = 4251;
    public static final int TOOLTIPS_STRUCT_ID = 3631;
    public static final int MODIFIED_STATS_OVERLAY_STRUCT_ID = 3633;
    public static final int SHOW_PRAYER_TOOLTIPS_STRUCT_ID = 1122;
    public static final int SHOW_MOUSEOVER_TOOLTIPS_STRUCT_ID = 3006;
    public static final int MODERN_LAYOUT_SIDE_PANEL_VISUAL_APPEARANCE_STRUCT_ID = 2886;
    public static final int CHAT_BOX_SCROLLBAR_POSITION_STRUCT_ID = 997;
    public static final int TRANSPARENT_CHATBOX_STRUCT_ID = 2730;
    public static final int CLICK_THROUGH_TRANSPARENT_CHATBOX_STRUCT_ID = 2768;
    public static final int RESIZABLE_STRUCT_ID = 3632;
    public static final int TRANSPARENT_SIDE_PANEL_STRUCT_ID = 1052;
    public static final int STAFF_OF_THE_DEAD_SPECIAL_DURATION_STRUCT_ID = 3035;
    public static final int UNSTARTED_QUEST_TEXT_COLOUR_STRUCT_ID = 4157;
    public static final int DEATH_CHARGE_ACTIVE_AND_DURATION_STRUCT_ID = 3115;
    public static final int SHOW_NUMBER_OF_OPTIONS_IN_MOUSEOVER_TOOLTIPS_STRUCT_ID = 3757;
    public static final int INTERFACE_SCALING_MODE_STRUCT_ID = 3657;
    public static final int DISPLAY_FRAGMENTS_STRUCT_ID = 3019;
    public static final int SHOW_WIKI_ENTITY_LOOKUP_STRUCT_ID = 2232;
    public static final int COLLECTION_LOG_NEW_ADDITION_POPUP_STRUCT_ID = 3641;
    public static final int DIVINE_POTION_DURATIONS_STRUCT_ID = 3036;
    public static final int TOOLTIPS_FOR_BUFFS_STRUCT_ID = 3018;
    public static final int FREEZEENTANGLED_DURATION_STRUCT_ID = 3034;
    public static final int IMBUED_HEART_COOLDOWN_STRUCT_ID = 3026;
    public static final int IN_PROGRESS_QUEST_TEXT_COLOUR_STRUCT_ID = 4249;
    public static final int DISABLE_QUEST_LIST_TEXT_SHADOWS_STRUCT_ID = 3755;
    public static final int VENGEANCE_ACTIVE_STRUCT_ID = 3028;
    public static final int SHOW_QUESTS_YOU_LACK_THE_RECOMMENDED_STATS_FOR_STRUCT_ID = 3110;
    public static final int VENGEANCE_COOLDOWN_STRUCT_ID = 3027;
    public static final int HIDE_COMPLETED_QUESTS_STRUCT_ID = 3751;
    public static final int HIDE_QUESTS_IN_PROGRESS_STRUCT_ID = 3744;
    public static final int HIDE_UNSTARTED_QUESTS_STRUCT_ID = 3599;
    public static final int STAMINA_DURATION_STRUCT_ID = 3029;
    public static final int INTERFACE_SCALING_STRUCT_ID = 2852;
    public static final int RESTORE_MINIMAP_ZOOM_STRUCT_ID = 3655;
    public static final int ABYSSAL_SIRE_STUN_DURATION_STRUCT_ID = 3033;
    public static final int ACCEPT_TRADE_DELAY_STRUCT_ID = 3761;
    public static final int COMBAT_ACHIEVEMENT_TASKS_COMPLETION_POPUP_STRUCT_ID = 319;
    public static final int XP_TRACKER_STRUCT_ID = 3739;
    public static final int MARK_OF_DARKNESS_ACTIVE_STRUCT_ID = 3113;
    public static final int ANTIVENOM_AND_POISON_POTION_DURATIONS_STRUCT_ID = 3038;
    public static final int ANTIFIRE_POTION_DURATIONS_STRUCT_ID = 3037;
    public static final int SHOW_THE_STORE_BUTTON_ON_DESKTOP_STRUCT_ID = 3710;
    public static final int SHOW_THE_STORE_BUTTON_ON_MOBILE_STRUCT_ID = 3709;
    public static final int POPOUT_WINDOWS_STRUCT_ID = 3729;
    public static final int GAME_CLIENT_LAYOUT_STRUCT_ID = 2732;
    public static final int RESET_INTERFACE_SCALING_STRUCT_ID = 2853;
    public static final int SHOW_ATTACK_STYLE_STRUCT_ID = 3016;
    public static final int SHADOW_VEIL_ACTIVE_AND_DURATION_STRUCT_ID = 3114;
    public static final int WARD_OF_ARCEUUS_ACTIVE_AND_DURATION_STRUCT_ID = 3116;
    public static final int ENABLE_MINIMAP_ZOOM_STRUCT_ID = 3654;
    public static final int DESERT_HEAT_DAMAGE_STRUCT_ID = 3759;
    public static final int RESURRECTION_ACTIVE_AND_DURATION_STRUCT_ID = 3117;
    public static final int REMAINING_AMMO_STRUCT_ID = 3658;
    public static final int OVERLOAD_DURATION_STRUCT_ID = 3031;
    public static final int GRAPHICS_STRUCT_ID = 3630;
    public static final int SCREEN_BRIGHTNESS_STRUCT_ID = 2736;
    public static final int VIEW_DISTANCE_STRUCT_ID = 3656;
    public static final int CAMERA_ZOOM_DISTANCE_STRUCT_ID = 2734;
    public static final int ALWAYS_ON_TOP_STRUCT_ID = 3039;
    public static final int LIMIT_FRAMERATE_STRUCT_ID = 3008;
    public static final int HIDE_ROOFS_STRUCT_ID = 1768;
    public static final int SCROLL_WHEEL_CAN_CHANGE_ZOOM_DISTANCE_STRUCT_ID = 2733;
    public static final int FULL_SCREEN_STRUCT_ID = 4289;
    public static final int OPTIONS_STRUCT_ID = 3635;
    public static final int ACCEPT_AID_STRUCT_ID = 2777;
    public static final int TIMERS_AND_NOTIFICATIONS_STRUCT_ID = 3731;
    public static final int FOOD_AND_POTIONS_CAN_FORM_SUPPLY_PILES_ON_DEATH_STRUCT_ID = 2861;
    public static final int SHOW_CONFIRMATION_WHEN_PAYING_FOR_ITEMS_FROM_GRAVESTONE_STRUCT_ID = 3743;
    public static final int SHOW_ACTIVITY_ADVISER_STRUCT_ID = 299;
    public static final int DISABLE_LEVEL_UP_INTERFACE_STRUCT_ID = 4493;

    static final IntList OPAQUE_COLOUR_STRUCTS = IntList.of(
            OPAQUE_PUBLIC_CHAT_STRUCT_ID, OPAQUE_PRIVATE_CHAT_STRUCT_ID, OPAQUE_AUTO_CHAT_STRUCT_ID,
            OPAQUE_BROADCAST_STRUCT_ID, OPAQUE_FRIEND_CHAT_STRUCT_ID, OPAQUE_CLAN_CHAT_STRUCT_ID,
            OPAQUE_GUEST_CLAN_CHAT_STRUCT_ID, OPAQUE_INCOMING_TRADE_REQUEST_STRUCT_ID,
            OPAQUE_INCOMING_CHALLENGE_REQUEST_STRUCT_ID, OPAQUE_CLAN_BROADCASTS_STRUCT_ID,
            OPAQUE_IRON_GROUP_CHAT_STRUCT_ID, OPAQUE_IRON_GROUP_BROADCASTS_STRUCT_ID
    );

    static final IntList TRANSPARENT_COLOUR_STRUCTS = IntList.of(
            TRANSPARENT_AUTO_CHAT_STRUCT_ID, TRANSPARENT_CLAN_BROADCASTS_STRUCT_ID,
            TRANSPARENT_INCOMING_TRADE_REQUEST_STRUCT_ID, TRANSPARENT_INCOMING_CHALLENGE_REQUEST_STRUCT_ID,
            TRANSPARENT_IRON_GROUP_BROADCASTS_STRUCT_ID, TRANSPARENT_PRIVATE_CHAT_STRUCT_ID,
            TRANSPARENT_CLAN_CHAT_STRUCT_ID, TRANSPARENT_FRIEND_CHAT_STRUCT_ID,
            TRANSPARENT_BROADCAST_STRUCT_ID, TRANSPARENT_GUEST_CLAN_CHAT_STRUCT_ID,
            TRANSPARENT_PUBLIC_CHAT_STRUCT_ID, TRANSPARENT_IRON_GROUP_CHAT_STRUCT_ID
    );

    static final IntList SPLIT_COLOUR_STRUCTS = IntList.of(
            SPLIT_PRIVATE_CHAT_STRUCT_ID, SPLIT_BROADCAST_STRUCT_ID
    );

    static final IntList QUEST_COLOUR_STRUCTS = IntList.of(
            UNSTARTED_QUEST_TEXT_COLOUR_STRUCT_ID, IN_PROGRESS_QUEST_TEXT_COLOUR_STRUCT_ID,
            COMPLETED_QUEST_TEXT_COLOUR_STRUCT_ID, UNAVAILABLE_QUEST_TEXT_COLOUR_STRUCT_ID
    );
}
