package io.ruin.model.inter.handlers.advancedsettings;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Jire
 */
public final class SettingStructResolvableMap<T extends SettingStructResolvable>
        implements SettingStructResolver {

    private final Int2ObjectMap<T> map;

    @SafeVarargs
    public SettingStructResolvableMap(T... values) {
        this.map = new Int2ObjectOpenHashMap<>(values.length);
        for (T value : values) {
            map.put(value.getStruct(), value);
        }
    }

    @Override
    public Integer forStruct(int struct) {
        T resolvable = map.get(struct);
        return resolvable == null ? null : resolvable.get();
    }

}
