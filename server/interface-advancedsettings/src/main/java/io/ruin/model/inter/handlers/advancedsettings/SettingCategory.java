package io.ruin.model.inter.handlers.advancedsettings;

import io.ruin.cache.EnumType;
import io.ruin.cache.StructType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 10/06/2022
 */
public final class SettingCategory {
	private static final int SETTINGS_CATEGORY_ID_PARAM = 743;
	private static final int SETTINGS_CATEGORY_NAME_PARAM = 744;
	private static final int SETTINGS_CATEGORY_SETTINGS_LIST_PARAM = 745;
	private final int id;
	private final String name;

	private final List<Setting> settings;

	SettingCategory(StructType struct) {
		if (!struct.hasParam(SETTINGS_CATEGORY_ID_PARAM)) {
			throw new IllegalStateException("struct: " + struct.id() + " doesnt have category param." + struct.params());
		}
		if (!struct.hasParam(SETTINGS_CATEGORY_NAME_PARAM)) {
			throw new IllegalStateException("struct: " + struct.id() + " doesnt have name param." + struct.params());
		}
		if (!struct.hasParam(SETTINGS_CATEGORY_SETTINGS_LIST_PARAM)) {
			throw new IllegalStateException("struct: " + struct.id() + " doesnt have list param." + struct.params());
		}
		this.id = struct.intParam(SETTINGS_CATEGORY_ID_PARAM);
		this.name = struct.stringParam(SETTINGS_CATEGORY_NAME_PARAM);
		final EnumType settings = EnumType.get(struct.intParam(SETTINGS_CATEGORY_SETTINGS_LIST_PARAM));
		this.settings = getSettings(settings);
	}

	private List<Setting> getSettings(EnumType settingsEnum) {
		final List<Setting> list = new ArrayList<>(settingsEnum.getSize());
		for (final int element : settingsEnum.valuesInt().values()) {
			var struct = StructType.get(element);
			list.add(new Setting(struct));
		}
		return list;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Setting> getSettings() {
		return settings;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		SettingCategory that = (SettingCategory) o;

		if (id != that.id)
			return false;
		if (!name.equals(that.name))
			return false;
		return settings.equals(that.settings);
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + name.hashCode();
		result = 31 * result + settings.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "SettingCategory{" +
				"id=" + id +
				", name='" + name + '\'' +
				", settings=" + settings +
				'}';
	}
}
