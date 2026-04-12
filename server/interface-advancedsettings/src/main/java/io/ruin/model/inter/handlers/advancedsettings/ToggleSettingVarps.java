package io.ruin.model.inter.handlers.advancedsettings;

import static io.ruin.model.inter.handlers.advancedsettings.SettingStructs.*;
import static io.ruin.model.inter.handlers.advancedsettings.SettingVariables.*;

/**
 * @author Jire
 */
public enum ToggleSettingVarps implements SettingStructResolvable {

    SHOW_CHAT_EFFECTS(SHOW_CHAT_EFFECTS_STRUCT_ID, SHOW_CHAT_EFFECTS_VARP_ID, 1),
    SPLIT_FRIENDS_PRIVATE_CHAT(SPLIT_FRIENDS_PRIVATE_CHAT_STRUCT_ID, SPLIT_FRIENDS_PRIVATE_CHAT_VARP_ID, 1),
    ENABLE_PROFANITY_FILTER(ENABLE_PROFANITY_FILTER_STRUCT_ID, ENABLE_PROFANITY_FILTER_VARP_ID, 0),
    SINGLE_MOUSE_BUTTON_MODE(SINGLE_MOUSE_BUTTON_MODE_STRUCT_ID, SINGLE_MOUSE_BUTTON_MODE_VARP_ID, 0);

    private final int struct;
    private final int varp;
    private final int defaultValue;

    ToggleSettingVarps(int struct, int varp, int defaultValue) {
        this.struct = struct;
        this.varp = varp;
        this.defaultValue = defaultValue;
    }

    ToggleSettingVarps(int struct, int varp) {
        this(struct, varp, 0);
    }

    @Override
    public int getStruct() {
        return struct;
    }

    public int getVarp() {
        return varp;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Integer get() {
        return getVarp();
    }

    public static final ToggleSettingVarps[] values = values();

}
