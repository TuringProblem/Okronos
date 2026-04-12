package io.ruin.model.inter.handlers.advancedsettings;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Kris | 10/06/2022
 */
public enum SettingType {
    TOGGLE(0),
    SLIDER(1),
    DROPDOWN(2),
    KEYBIND(3),
    INPUT(4),
    HEADER(5),
    BUTTON(6),
    SEE_MORE(7),
    INFO(8),
    COLOUR(9),
    IDK(10)
		;

    private final int typeId;
    SettingType(final int typeId) {
        this.typeId = typeId;
    }

    public static final Int2ObjectMap<SettingType> MAP;

    static {
        final SettingType[] values = values();
        final Int2ObjectMap<SettingType> types = new Int2ObjectOpenHashMap<>(values.length);
        for (final SettingType type : values) {
            types.put(type.typeId, type);
        }
        MAP = Int2ObjectMaps.unmodifiable(types);
    }
}
