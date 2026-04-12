package io.ruin.model.inter.handlers.advancedsettings;

import static io.ruin.model.inter.handlers.advancedsettings.SettingStructs.*;
import static io.ruin.model.inter.handlers.advancedsettings.SettingVariables.*;

/**
 * @author Jire
 */
public enum SliderSettingVarps implements SettingStructResolvable {

    MUSIC_VOLUME(MUSIC_VOLUME_STRUCT_ID, MUSIC_VOLUME_VARP_ID),
    SOUND_EFFECT_VOLUME(SOUND_EFFECT_VOLUME_STRUCT_ID, SOUND_EFFECT_VOLUME_VARP_ID),
    AREA_SOUND_VOLUME(AREA_SOUND_VOLUME_STRUCT_ID, AREA_SOUND_VOLUME_VARP_ID),
    SCREEN_BRIGHTNESS(SCREEN_BRIGHTNESS_STRUCT_ID, SCREEN_BRIGHTNESS_VARP_ID),
    ;

    private final int struct;
    private final int varp;

    private final int defaultValue;

    SliderSettingVarps(int struct, int varp, int defaultValue) {
        this.struct = struct;
        this.varp = varp;
        this.defaultValue = defaultValue;
    }

    SliderSettingVarps(int struct, int varp) {
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

    public static final SliderSettingVarps[] values = values();

}
