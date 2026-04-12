package io.ruin.model.inter.handlers.advancedsettings;

import io.ruin.cache.EnumType;
import io.ruin.cache.StructType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerDisplayMode;
import lombok.EqualsAndHashCode;

/**
 * @author Kris | 10/06/2022
 */
@SuppressWarnings({ "unused", "BooleanMethodIsAlwaysInverted", "SpellCheckingInspection" })
@EqualsAndHashCode
public class Setting {
	private static final int IS_DESKTOP_PARAM = 739;
	private static final int IS_MOBILE_PARAM = 740;
	private static final int IS_NON_IRONMAN_PARAM = 741;
	private static final int IS_IRONMAN_PARAM = 742;
	private static final int ID_PARAM = 1077;
	private static final int TYPE_PARAM = 1078;
	private static final int PRE_REQUIREMENTS_ENUM_ID_PARAM = 1080;
	private static final int PRE_REQUIREMENTS_VALUES_ENUM_ID_PARAM = 1081;
	private static final int PRE_REQUIREMENTS_INVERSED_ENUM_ID_PARAM = 1082;
	private static final int PRE_REQUIREMENTS_INVERSED_VALUES_ENUM_ID_PARAM = 1083;
	private static final int NAME_PARAM = 1086;
	private static final int SEARCH_KEYWORDS_PARAM = 1088;
	private static final int DROPDOWN_ENTRIES_PARAM = 1091;
	private static final int DROPDOWN_ENTRIES_MOBILE_PARAM = 1092;
	private static final int SLIDER_NOTCH_COUNT_PARAM = 1101;
	private static final int IS_SLIDER_TRANSMITTED_PARAM = 1105;
	private static final int HAS_CUSTOM_REQUIREMENTS_PARAM = 1115;
	private static final int HAS_TOGGLE_INVERSED_PARAM = 1084;
	private static final int CHOOSE_TRANSMIT_PARAM = 1085;
	private static final int MOBILE_NAME_PARAM = 1087;
	private static final int DESCRIPTION_PARAM = 1096;
	private static final int KEYBIND_SPITE_PARAM = 1098;
	private static final int KEYBIND_SPRITE_SIZE_COORDGRID_PARAM = 1099;
	private static final int SLIDER_SECTORS_PARAM = 1102;
	private static final int SLIDER_SECTOR_TEXT_ENUM_ID_PARAM = 1103;
	private static final int SLIDER_CUSTOM_ON_OP_PARAM = 1106;
	private static final int SLIDER_CUSTOM_SETPOS_PARAM = 1107;
	private static final int IS_SLIDER_DRAGGABLE_PARAM = 1108;
	private static final int SLIDER_DEADZONE_PARAM = 1109;
	private static final int SLIDER_DEADTIME_PARAM = 1110;
	private static final int SLIDER_INPUT_SINGULAR_PARAM = 1111;
	private static final int SLIDER_INPUT_PLURAL_PARAM = 1112;
	private static final int SLIDER_INPUT_ZERO_PARAM = 1113;
	private static final int SLIDER_OP_CHECKER_MESSAGE_PARAM = 1116;
	private static final int SLIDER_MOBILE_OP_CHECKER_MESSAGE_PARAM = 1117;
	private static final int HAS_COLLAPSIBLE_INFOBOX_PARAM = 1118;
	private static final int HIDE_DESCRIPTION_PARAM = 1119;
	private static final int IS_ENHANCED_CLIENT_PARAM = 1157;
	private static final int CUSTOM_NAME_EXTRA_TEXT_PARAM = 1158;
	private static final int IS_MOBILE_ALWAYS_ENABLED_PARAM = 1186;
	private static final int HAS_CUSTOM_CHECK_PARAM = 1229;
	private static final int DEFAULT_COLOUR_PARAM = 1230;
	private static final int IS_NON_DESKTOP_ONLY_PARAM = 1271;
	private static final int IS_LEAGUE_WORLD_ONLY_PARAM = 1272;
	private static final int IS_LEAGUE_ENHANCED_CLIENT_ONLY_PARAM = 1273;
	private static final int PROFANITY_FILTER_ENABLED_PARAM = 1074;

	private final int structId;
	private final int id;
	private final int typeId;
	private final String name;
	private final String searchKeywords;
	private final boolean sliderTransmitted;
	private final int sliderNotchCount;
	private final boolean desktop;
	private final boolean mobile;
	private final boolean nonIronman;
	private final boolean ironman;
	private final boolean hasCustomRequirements;
	private final int preRequirementsEnumId;
	private final int preRequirementsValuesEnumId;
	private final int inversedPreRequirementsEnumId;
	private final int inversedPreRequirementsValuesEnumId;
	private final boolean toggleInversed;
	private final boolean chooseTransmit;
	private final String mobileName;
	private final String description;
	private final int keyBindSprite;
	private final int keyBindSpriteCoordGrid;
	private final int sliderSectors;
	private final int sliderSectorsTextEnumId;
	private final boolean sliderCustomOnOpScript;
	private final boolean sliderCustomSetPos;
	private final boolean sliderDraggable;
	private final int sliderDeadZone;
	private final int sliderDeadTime;
	private final String inputSingular;
	private final String inputPlural;
	private final String inputZero;
	private final String opCheckerMessage;
	private final String mobileOpCheckerMessage;
	private final boolean collapsibleInfobox;
	private final boolean hideDescription;
	private final boolean enhancedClientOnly;
	private final boolean customNameExtraText;
	private final boolean mobileAlwaysEnabled;
	private final boolean hasCustomCheck;
	private final boolean profanityFilterEnabled;
	private final int defaultColour;
	private final boolean nonDesktopOnly;
	private final boolean leagueWorldOnly;
	private final boolean leagueWorldEnhancedClientOnly;
	private final int dropdownEntriesEnumId;
	private final int mobileDropDownEntriesEnumId;

	private final SettingType type;

	private final EnumType preRequirementsEnum;

	private final EnumType preRequirementsValuesEnum;

	private final EnumType inversedPreRequirementsEnum;

	private final EnumType inversedPreRequirementsValuesEnum;

	private final EnumType sliderSectorsTextEnum;

	private final EnumType dropdownEntriesEnum;

	private final EnumType mobileDropdownEntriesEnum;

	Setting(StructType struct) {
		this.structId = struct.id();
		this.id = struct.intParam(ID_PARAM, -1);
		this.typeId = struct.intParam(TYPE_PARAM, -1);
		this.name = struct.stringParam(NAME_PARAM, "");
		this.searchKeywords = struct.stringParam(SEARCH_KEYWORDS_PARAM, "");
		this.sliderTransmitted = struct.boolParam(IS_SLIDER_TRANSMITTED_PARAM, false);
		this.sliderNotchCount = struct.intParam(SLIDER_NOTCH_COUNT_PARAM, -1);
		this.desktop = struct.boolParam(IS_DESKTOP_PARAM, false);
		this.mobile = struct.boolParam(IS_MOBILE_PARAM, false);
		this.ironman = struct.boolParam(IS_IRONMAN_PARAM, false);
		this.nonIronman = struct.boolParam(IS_NON_IRONMAN_PARAM, false);
		this.hasCustomRequirements = struct.boolParam(HAS_CUSTOM_REQUIREMENTS_PARAM, false);
		this.preRequirementsEnumId = struct.intParam(PRE_REQUIREMENTS_ENUM_ID_PARAM, -1);
		this.preRequirementsValuesEnumId = struct.intParam(PRE_REQUIREMENTS_VALUES_ENUM_ID_PARAM, -1);
		this.inversedPreRequirementsEnumId = struct.intParam(PRE_REQUIREMENTS_INVERSED_ENUM_ID_PARAM, -1);
		this.inversedPreRequirementsValuesEnumId = struct.intParam(PRE_REQUIREMENTS_INVERSED_VALUES_ENUM_ID_PARAM, -1);
		this.toggleInversed = struct.boolParam(HAS_TOGGLE_INVERSED_PARAM, false);
		this.chooseTransmit = struct.boolParam(CHOOSE_TRANSMIT_PARAM, false);
		this.mobileName = struct.stringParam(MOBILE_NAME_PARAM, "");
		this.description = struct.stringParam(DESCRIPTION_PARAM, "");
		this.keyBindSprite = struct.intParam(KEYBIND_SPITE_PARAM, -1);
		this.keyBindSpriteCoordGrid = struct.intParam(KEYBIND_SPRITE_SIZE_COORDGRID_PARAM, -1);
		this.sliderSectors = struct.intParam(SLIDER_SECTORS_PARAM, -1);
		this.sliderSectorsTextEnumId = struct.intParam(SLIDER_SECTOR_TEXT_ENUM_ID_PARAM, -1);
		this.sliderCustomOnOpScript = struct.boolParam(SLIDER_CUSTOM_ON_OP_PARAM, false);
		this.sliderCustomSetPos = struct.boolParam(SLIDER_CUSTOM_SETPOS_PARAM, false);
		this.sliderDraggable = struct.boolParam(IS_SLIDER_DRAGGABLE_PARAM, false);
		this.sliderDeadZone = struct.intParam(SLIDER_DEADZONE_PARAM, -1);
		this.sliderDeadTime = struct.intParam(SLIDER_DEADTIME_PARAM, -1);
		this.inputSingular = struct.stringParam(SLIDER_INPUT_SINGULAR_PARAM, "");
		this.inputPlural = struct.stringParam(SLIDER_INPUT_PLURAL_PARAM, "");
		this.inputZero = struct.stringParam(SLIDER_INPUT_ZERO_PARAM, "");
		this.opCheckerMessage = struct.stringParam(SLIDER_OP_CHECKER_MESSAGE_PARAM, "");
		this.mobileOpCheckerMessage = struct.stringParam(SLIDER_MOBILE_OP_CHECKER_MESSAGE_PARAM, "");
		this.collapsibleInfobox = struct.boolParam(HAS_COLLAPSIBLE_INFOBOX_PARAM, false);
		this.hideDescription = struct.boolParam(HIDE_DESCRIPTION_PARAM, false);
		this.enhancedClientOnly = struct.boolParam(IS_ENHANCED_CLIENT_PARAM, false);
		this.customNameExtraText = struct.boolParam(CUSTOM_NAME_EXTRA_TEXT_PARAM, false);
		this.mobileAlwaysEnabled = struct.boolParam(IS_MOBILE_ALWAYS_ENABLED_PARAM, false);
		this.hasCustomCheck = struct.boolParam(HAS_CUSTOM_CHECK_PARAM, false);
		this.defaultColour = struct.intParam(DEFAULT_COLOUR_PARAM, -1);
		this.nonDesktopOnly = struct.boolParam(IS_NON_DESKTOP_ONLY_PARAM, false);
		this.leagueWorldOnly = struct.boolParam(IS_LEAGUE_WORLD_ONLY_PARAM, false);
		this.leagueWorldEnhancedClientOnly = struct.boolParam(IS_LEAGUE_ENHANCED_CLIENT_ONLY_PARAM, false);
		this.dropdownEntriesEnumId = struct.intParam(DROPDOWN_ENTRIES_PARAM, -1);
		this.mobileDropDownEntriesEnumId = struct.intParam(DROPDOWN_ENTRIES_MOBILE_PARAM, -1);
		this.profanityFilterEnabled = struct.boolParam(PROFANITY_FILTER_ENABLED_PARAM, true);

		/* Type-specific versions of the above variables. */
		this.type = SettingType.MAP.get(this.typeId);
		if (this.type == null) {
			throw new IllegalStateException("Unable to find type id: " + this.typeId);
		}
		this.preRequirementsEnum = EnumType.get(this.preRequirementsEnumId);
		this.preRequirementsValuesEnum = EnumType.get(this.preRequirementsValuesEnumId);
		this.inversedPreRequirementsEnum = EnumType.get(this.inversedPreRequirementsEnumId);
		this.inversedPreRequirementsValuesEnum = EnumType.get(this.inversedPreRequirementsValuesEnumId);
		this.sliderSectorsTextEnum = EnumType.get(this.sliderSectorsTextEnumId);
		this.dropdownEntriesEnum = EnumType.get(this.dropdownEntriesEnumId);
		this.mobileDropdownEntriesEnum = EnumType.get(this.mobileDropDownEntriesEnumId);
	}

	public int getStructId() {
		return structId;
	}

	public int getId() {
		return id;
	}

	public int getTypeId() {
		return typeId;
	}

	public String getName() {
		return name;
	}

	public String getSearchKeywords() {
		return searchKeywords;
	}

	public boolean isSliderTransmitted() {
		return sliderTransmitted;
	}

	public int getSliderNotchCount() {
		return sliderNotchCount;
	}

	public boolean isDesktop() {
		return desktop;
	}

	public boolean isMobile() {
		return mobile;
	}

	public boolean isNonIronman() {
		return nonIronman;
	}

	public boolean isIronman() {
		return ironman;
	}

	public boolean isHasCustomRequirements() {
		return hasCustomRequirements;
	}

	public int getPreRequirementsEnumId() {
		return preRequirementsEnumId;
	}

	public int getPreRequirementsValuesEnumId() {
		return preRequirementsValuesEnumId;
	}

	public int getInversedPreRequirementsEnumId() {
		return inversedPreRequirementsEnumId;
	}

	public int getInversedPreRequirementsValuesEnumId() {
		return inversedPreRequirementsValuesEnumId;
	}

	public boolean isToggleInversed() {
		return toggleInversed;
	}

	public boolean isChooseTransmit() {
		return chooseTransmit;
	}

	public String getMobileName() {
		return mobileName;
	}

	public String getDescription() {
		return description;
	}

	public int getKeyBindSprite() {
		return keyBindSprite;
	}

	public int getKeyBindSpriteCoordGrid() {
		return keyBindSpriteCoordGrid;
	}

	public int getSliderSectors() {
		return sliderSectors;
	}

	public int getSliderSectorsTextEnumId() {
		return sliderSectorsTextEnumId;
	}

	public boolean isSliderCustomOnOpScript() {
		return sliderCustomOnOpScript;
	}

	public boolean isSliderCustomSetPos() {
		return sliderCustomSetPos;
	}

	public boolean isSliderDraggable() {
		return sliderDraggable;
	}

	public int getSliderDeadZone() {
		return sliderDeadZone;
	}

	public int getSliderDeadTime() {
		return sliderDeadTime;
	}

	public String getInputSingular() {
		return inputSingular;
	}

	public String getInputPlural() {
		return inputPlural;
	}

	public String getInputZero() {
		return inputZero;
	}

	public String getOpCheckerMessage() {
		return opCheckerMessage;
	}

	public String getMobileOpCheckerMessage() {
		return mobileOpCheckerMessage;
	}

	public boolean isCollapsibleInfobox() {
		return collapsibleInfobox;
	}

	public boolean isHideDescription() {
		return hideDescription;
	}

	public boolean isEnhancedClientOnly() {
		return enhancedClientOnly;
	}

	public boolean isCustomNameExtraText() {
		return customNameExtraText;
	}

	public boolean isMobileAlwaysEnabled() {
		return mobileAlwaysEnabled;
	}

	public boolean isHasCustomCheck() {
		return hasCustomCheck;
	}

	public int getDefaultColour() {
		return defaultColour;
	}

	public boolean isNonDesktopOnly() {
		return nonDesktopOnly;
	}

	public boolean isLeagueWorldOnly() {
		return leagueWorldOnly;
	}

	public boolean isLeagueWorldEnhancedClientOnly() {
		return leagueWorldEnhancedClientOnly;
	}

	public int getDropdownEntriesEnumId() {
		return dropdownEntriesEnumId;
	}

	public int getMobileDropDownEntriesEnumId() {
		return mobileDropDownEntriesEnumId;
	}

	public SettingType getType() {
		return type;
	}

	public EnumType getPreRequirementsEnum() {
		return preRequirementsEnum;
	}

	public EnumType getPreRequirementsValuesEnum() {
		return preRequirementsValuesEnum;
	}

	public EnumType getInversedPreRequirementsEnum() {
		return inversedPreRequirementsEnum;
	}

	public EnumType getInversedPreRequirementsValuesEnum() {
		return inversedPreRequirementsValuesEnum;
	}

	public EnumType getSliderSectorsTextEnum() {
		return sliderSectorsTextEnum;
	}

	public EnumType getDropdownEntriesEnum() {
		return dropdownEntriesEnum;
	}

	public EnumType getMobileDropdownEntriesEnum() {
		return mobileDropdownEntriesEnum;
	}

	public boolean checkSetting(Player player) {
		if (mobileAlwaysEnabled) {
			return true;
		}
		if (hasCustomRequirements && !checkCustomRequirement(player)) {
			return false;
		}
		if (preRequirementsEnumId != -1 && !checkOtherSetting(player, false)) {
			return false;
		}
		return inversedPreRequirementsEnumId == -1 || checkOtherSetting(player, true);
	}

	private boolean checkCustomRequirement(Player player) {
		if (SettingStructs.OPAQUE_COLOUR_STRUCTS.contains(structId) ||
				structId == SettingStructs.RESET_OPAQUE_CHAT_COLOURS_STRUCT_ID) {
			return PlayerDisplayMode.mode(player) == PlayerDisplayMode.Mode.Fixed
					|| player.varbit(SettingVariables.TRANSPARENT_CHATBOX_VARBIT_ID) != 1;
		}
		return true;
	}

	private boolean checkOtherSetting(Player player, boolean inverse) {
		var settingsToCheck = inverse ? inversedPreRequirementsEnum : preRequirementsEnum;
		var settingValues = inverse ? inversedPreRequirementsValuesEnum : preRequirementsValuesEnum;
		for (final var entry : settingsToCheck.valuesInt().entrySet()) {
			final int index = entry.getKey();
			final int settingId = entry.getValue();
			final Setting settingToCheck = Settings.findSettingByStructId(settingId);
			final int value = settingValues.valueInt(index);
			if (settingToCheck.preRequirementsEnumId != -1 && !settingToCheck.checkOtherSetting(player, false)) {
				return false;
			}
			if (settingToCheck.inversedPreRequirementsEnumId != -1 && !settingToCheck.checkOtherSetting(player, true)) {
				return false;
			}
			final int currentSettingValue = settingToCheck.getCurrentSettingValue(player);
			if (!inverse) {
				if (value != currentSettingValue)
					return false;
			} else {
				if (value == currentSettingValue)
					return false;
			}
		}
		return true;
	}

	private int getCurrentSettingValue(Player player) {
		switch (type) {
			case TOGGLE:
				return getToggleSetting(player);
			case SLIDER:
				return getSlider(player);
			case DROPDOWN:
				return getDropdown(player);
			case KEYBIND:
				return getKeybind(player);
			default:
				return 0;
		}
	}

	// https://github.com/RuneStar/cs2-scripts/blob/master/scripts/%5Bproc,settings_get_toggle%5D.cs2
	private int getToggleSetting(Player player) {
		switch (structId) {
			case SettingStructs.COMBAT_ACHIEVEMENT_TASKS_REPEAT_FAILURE_STRUCT_ID:
				if (player.varbit(SettingVariables.COMBAT_ACHIEVEMENT_TASKS_FAILURE_VARBIT_ID) == 0) {
					return 0;
				}
				break;
			case SettingStructs.COLLECTION_LOG_NEW_ADDITION_NOTIFICATION_STRUCT_ID:
				return player.varbit(SettingVariables.COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID & 0x1);
			case SettingStructs.COLLECTION_LOG_NEW_ADDITION_POPUP_STRUCT_ID:
				return player.varbit(SettingVariables.COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID >> 1) & 0x1;
		}
		return SettingVariables.getVariableValue(this, player);
	}

	// https://github.com/RuneStar/cs2-scripts/blob/master/scripts/%5Bproc,settings_get_slider%5D.cs2
	private int getSlider(Player player) {
		switch (structId) {
			case SettingStructs.VIEW_DISTANCE_STRUCT_ID:
				throw new IllegalStateException("Enhanced client not supported.");
			case SettingStructs.MUSIC_VOLUME_STRUCT_ID:
			case SettingStructs.SOUND_EFFECT_VOLUME_STRUCT_ID:
			case SettingStructs.AREA_SOUND_VOLUME_STRUCT_ID:
			case SettingStructs.SCREEN_BRIGHTNESS_STRUCT_ID:
				return SettingVariables.getVariableValue(this, player) / 5;
		}
		return SettingVariables.getVariableValue(this, player);
	}

	// https://github.com/RuneStar/cs2-scripts/blob/master/scripts/%5Bproc,settings_get_dropdown%5D.cs2
	private int getDropdown(Player player) {
		switch (structId) {
			case SettingStructs.FRIEND_LOGINLOGOUT_MESSAGES_STRUCT_ID:
				final int timeout = player.varbit(SettingVariables.FRIEND_LOGIN_LOGOUT_MESSAGE_TIMEOUT_VARBIT_ID);
				return timeout != 0 ? timeout : SettingVariables.getVariableValue(this, player);
			case SettingStructs.LIMIT_FRAMERATE_STRUCT_ID:
			case SettingStructs.INTERFACE_SCALING_MODE_STRUCT_ID:
				throw new IllegalStateException("Enhanced and mobile client not supported.");
			case SettingStructs.GAME_CLIENT_LAYOUT_STRUCT_ID:
				if (PlayerDisplayMode.mode(player) == PlayerDisplayMode.Mode.Fixed)
					return 0;
				if (player.varbit(SettingVariables.SIDE_PANELS_VARBIT_ID) == 0)
					return 1;
				return 2;
		}
		return SettingVariables.getVariableValue(this, player);
	}

	// https://github.com/RuneStar/cs2-scripts/blob/master/scripts/%5Bproc,settings_get_keybind%5D.cs2
	private int getKeybind(Player player) {
		final int value = SettingVariables.getVariableValue(this, player);
		return EnumType.get(Enums.KEYBINDINGS_ID).lookup(value);
	}

	@Override
	public String toString() {
		return "Setting [structId=" + structId + ", id=" + id + ", typeId=" + typeId + ", name=" + name
				+ ", searchKeywords=" + searchKeywords + ", sliderTransmitted=" + sliderTransmitted + ", sliderNotchCount="
				+ sliderNotchCount + "]";
	}


}
