package io.ruin.model.inter.handlers.advancedsettings;

import static io.ruin.model.inter.handlers.advancedsettings.SettingVariables.*;

/**
 * @author Jire
 */
public enum OtherVarBits {

	SHOW_LESS_OR_MORE_INFORMATION(SHOW_LESS_OR_MORE_INFORMATION_VARBIT),
	SETTINGS_SEARCH_LEFT(SETTINGS_SEARCH_LEFT_VARBIT),
	SETTINGS_SEARCH_RIGHT(SETTINGS_SEARCH_RIGHT_VARBIT),
	SETTINGS_CURRENT_CATEGORY(SETTINGS_CURRENT_CATEGORY_VARBIT),
	MUSIC_VOLUME_MUTED(MUSIC_VOLUME_MUTED_VARBIT_ID),
	SOUND_EFFECT_VOLUME_MUTED(SOUND_EFFECT_VOLUME_MUTED_VARBIT_ID),
	AREA_EFFECT_MUTED(AREA_EFFECT_MUTED_VARBIT_ID),
	MINIMUM_LOOT_ITEM_VALUE(MINIMUM_LOOT_ITEM_VALUE_VARBIT_ID),
	MINIMUM_DROP_ITEM_VALUE(MINIMUM_DROP_ITEM_VALUE_VARBIT_ID),
	MINIMUM_ALCH_TRIGGER_VALUE(MINIMUM_ALCH_TRIGGER_VALUE_VARBIT_ID, 30_000),
	CURRENT_SETTING(CURRENT_SETTING_VARBIT_ID),
	COLLECTION_LOG_NEW_ADDITIONS(COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID),
	FRIEND_LOGIN_LOGOUT_MESSAGE_TIMEOUT(FRIEND_LOGIN_LOGOUT_MESSAGE_TIMEOUT_VARBIT_ID),
	SIDE_PANELS(SIDE_PANELS_VARBIT_ID);

	private final int varbit;
	private final int defaultValue;

	OtherVarBits(int varbit, int defaultValue) {
		this.varbit = varbit;
		this.defaultValue = defaultValue;
	}

	OtherVarBits(int varbit) {
		this(varbit, 0);
	}

	public int getVarbit() {
		return varbit;
	}

	public int getDefaultValue() {
		return defaultValue;
	}

	public static final OtherVarBits[] values = values();

}
