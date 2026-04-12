package io.ruin.model.inter.handlers.advancedsettings;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import io.ruin.cache.EnumType;
import io.ruin.cache.StructType;
import io.ruin.model.entity.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

/**
 * @author Kris | 10/06/2022
 */
@SuppressWarnings("SpellCheckingInspection")
public class Settings {

	private static final String CURRENT_CATEGORY_ATTR_KEY = "adv_search_current_selected_category";
	private static final String CURRENT_DROPDOWN_ATTR_KEY = "adv_search_dropdown";
	private static final String CURRENT_DROPDOWN_KEYBIND_ATTR_KEY = "adv_search_keybind";
	public static final List<SettingCategory> CATEGORIES = getCategories(
			EnumType.get(Enums.SEARCHABLE_SETTINGS_CATEGORIES_ID));
	public static final List<SettingCategory> NON_SEARCHABLE_CATEGORIES = getCategories(
			EnumType.get(Enums.NON_SEARCHABLE_SETTINGS_CATEGORIES_ID));

	public static void register() {
		/*
		 * Due to the two enums being identical at the time of writing this, we cannot
		 * predict the differences in the future.
		 */
		Preconditions.checkState(CATEGORIES.equals(NON_SEARCHABLE_CATEGORIES));
	}

	public static final Map<SettingCategory, List<Setting>> SLIDERS = getSliders();
	public static final List<Setting> ALL_SETTINGS = getAllSettings();
	public static final Map<SettingType, List<Setting>> SETTINGS_BY_TYPE = getTypeMap();
	public static final List<Setting> TELEPORT_WARNING_SETTINGS = getFilteredSettings(
			setting -> setting.getName().startsWith("Show warning when casting: "));
	public static final List<Setting> TABLET_WARNING_SETTINGS = getFilteredSettings(
			setting -> setting.getName().startsWith("Show warning when using tablet: "));
	public static final List<Setting> OPAQUE_COLOUR_SETTINGS = getFilteredSettings(
			setting -> SettingStructs.OPAQUE_COLOUR_STRUCTS.contains(setting.getStructId()));
	public static final List<Setting> TRANSPARENT_COLOUR_SETTINGS = getFilteredSettings(
			setting -> SettingStructs.TRANSPARENT_COLOUR_STRUCTS.contains(setting.getStructId()));
	public static final List<Setting> SPLIT_COLOUR_SETTINGS = getFilteredSettings(
			setting -> SettingStructs.SPLIT_COLOUR_STRUCTS.contains(setting.getStructId()));
	public static final List<Setting> QUEST_COLOUR_SETTINGS = getFilteredSettings(
			setting -> SettingStructs.QUEST_COLOUR_STRUCTS.contains(setting.getStructId()));
	public static final int SLIDER_NOTCH_COUNT_SUM = calculateSlidersNotchCountSum();
	public static final int DROPDOWN_ENTRIES_MAX_COUNT = getHighestAmountOfDropDownEntries();

	private static List<SettingCategory> getCategories(final EnumType categoriesEnum) {
		final List<SettingCategory> categories = new ArrayList<>(categoriesEnum.getSize());
		final Map<Integer, Integer> sorted = new TreeMap<>(categoriesEnum.valuesInt());
		for (final int element : sorted.values()) {
			var struct = StructType.get(element);
			categories.add(new SettingCategory(struct));
		}
		return Collections.unmodifiableList(categories);
	}

	private static Map<SettingCategory, List<Setting>> getSliders() {
		final Map<SettingCategory, List<Setting>> map = new HashMap<>();
		for (final SettingCategory category : NON_SEARCHABLE_CATEGORIES) {
			final List<Setting> settings = new ArrayList<>();
			for (final Setting setting : category.getSettings()) {
				if (setting.getType() != SettingType.SLIDER)
					continue;
				settings.add(setting);
			}
			map.put(category, Collections.unmodifiableList(settings));
		}
		return Collections.unmodifiableMap(map);
	}

	private static Map<SettingType, List<Setting>> getTypeMap() {
		final Map<SettingType, List<Setting>> map = new HashMap<>();
		for (final SettingCategory category : NON_SEARCHABLE_CATEGORIES) {
			for (final Setting setting : category.getSettings()) {
				final List<Setting> list = map.computeIfAbsent(setting.getType(), a -> new ArrayList<>());
				list.add(setting);
				map.put(setting.getType(), list);
			}
		}
		final Map<SettingType, List<Setting>> unmodifiable = new HashMap<>(map.size());
		for (final Map.Entry<SettingType, List<Setting>> entry : map.entrySet()) {
			unmodifiable.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
		}
		return Collections.unmodifiableMap(unmodifiable);
	}

	private static List<Setting> getAllSettings() {
		final List<Setting> allSettings = new ArrayList<>(200);
		for (final SettingCategory category : CATEGORIES) {
			allSettings.addAll(category.getSettings());
		}
		return Collections.unmodifiableList(allSettings);
	}

	private static List<Setting> getFilteredSettings(final Predicate<Setting> predicate) {
		final List<Setting> settings = new ArrayList<>();
		for (final SettingCategory category : CATEGORIES) {
			for (final Setting setting : category.getSettings()) {
				if (!predicate.test(setting))
					continue;
				settings.add(setting);
			}
		}
		return Collections.unmodifiableList(settings);
	}

	public static SliderPosition findSliderPosition(SettingCategory category, final int slot) {
		int offset = 0;
		final List<Setting> settings = category != null ? category.getSettings() : ALL_SETTINGS;
		for (final Setting setting : settings) {
			if (setting.getType() != SettingType.SLIDER)
				continue;
			if (slot >= offset && slot < offset + setting.getSliderNotchCount()) {
				return new SliderPosition(setting, offset);
			}
			offset += setting.getSliderNotchCount();
		}
		throw new IllegalArgumentException("Slot '" + slot + "' out of bounds for category '" + category + "'.");
	}

	public static Setting findSettingByStructId(final int id) {
		for (final Setting setting : ALL_SETTINGS) {
			if (setting.getStructId() == id)
				return setting;
		}
		throw new IllegalArgumentException("Setting with struct '" + id + "' does not exist.");
	}

	private static int calculateSlidersNotchCountSum() {
		int count = 0;
		for (final Map.Entry<SettingCategory, List<Setting>> entry : SLIDERS.entrySet()) {
			for (final Setting setting : entry.getValue()) {
				count += setting.getSliderNotchCount();
			}
		}
		return count;
	}

	private static int getHighestAmountOfDropDownEntries() {
		int count = 0;
		for (final Setting setting : ALL_SETTINGS) {
			final EnumType nonMobileEnum = setting.getDropdownEntriesEnum();
			final EnumType mobileEnum = setting.getMobileDropdownEntriesEnum();
			if (nonMobileEnum != null) {
				final int nonMobileEntries = nonMobileEnum.getSize();
				if (nonMobileEntries > count)
					count = nonMobileEntries;
			}
			if (mobileEnum != null) {
				final int mobileEntries = mobileEnum.getSize();
				if (mobileEntries > count)
					count = mobileEntries;
			}
		}
		return count;
	}

	public static SettingCategory getCurrentSelectedCategory(Player player) {
		final Object value = player.get(CURRENT_CATEGORY_ATTR_KEY);
		if (value instanceof SettingCategory) {
			return (SettingCategory) value;
		}
		return null;
	}

	public static void setCurrentSelectedCategory(Player player, SettingCategory category) {
		player.set(CURRENT_CATEGORY_ATTR_KEY, category);
	}

	public static Setting getCurrentDropdownSetting(Player player) {
		final Object value = player.get(CURRENT_DROPDOWN_ATTR_KEY);
		if (value instanceof Setting) {
			return (Setting) value;
		}
		return null;
	}

	public static void setCurrentDropdownSetting(Player player, Setting category) {
		player.set(CURRENT_DROPDOWN_ATTR_KEY, category);
	}

	public static boolean isCurrentDropdownSettingKeybind(Player player) {
		final Object value = player.get(CURRENT_DROPDOWN_KEYBIND_ATTR_KEY);
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		return false;
	}

	public static void setCurrentDropdownSettingKeybind(Player player, final boolean value) {
		player.set(CURRENT_DROPDOWN_KEYBIND_ATTR_KEY, value);
	}

	public static SettingCategory getCategory(int id) {
		for (final SettingCategory category : CATEGORIES) {
			if (category.getId() == id) {
				return category;
			}
		}
		return null;
	}

	private static final Map<Integer, String> DEFAULT_KEYBINDS_STRUCTS = ImmutableMap.<Integer, String>builder()
			.put(SettingStructs.COMBAT_TAB_KEYBIND_STRUCT_ID, "F1")
			.put(SettingStructs.SKILLS_TAB_KEYBIND_STRUCT_ID, "F2")
			.put(SettingStructs.JOURNAL_TAB_KEYBIND_STRUCT_ID, "F3")
			.put(SettingStructs.EQUIPMENT_TAB_KEYBIND_STRUCT_ID, "F4")
			.put(SettingStructs.PRAYER_TAB_KEYBIND_STRUCT_ID, "F5")
			.put(SettingStructs.MAGIC_TAB_KEYBIND_STRUCT_ID, "F6")
			.put(SettingStructs.FRIENDS_CHAT_TAB_KEYBIND_STRUCT_ID, "F7")
			.put(SettingStructs.FRIENDS_LIST_KEYBIND_STRUCT_ID, "F8")
			.put(SettingStructs.ACCOUNT_MANAGEMENT_TAB_KEYBIND_STRUCT_ID, "F9")
			.put(SettingStructs.LOGOUT_TAB_KEYBIND_STRUCT_ID, "None")
			.put(SettingStructs.SETTINGS_TAB_KEYBIND_STRUCT_ID, "F10")
			.put(SettingStructs.EMOTES_TAB_KEYBIND_STRUCT_ID, "F11")
			.put(SettingStructs.MUSIC_TAB_KEYBIND_STRUCT_ID, "F12")
			.build();

	public static final Map<Setting, Integer> DEFAULT_KEYBINDS = getIndexedDefaultKeybinds();

	private static Map<Setting, Integer> getIndexedDefaultKeybinds() {
		final Map<Setting, Integer> map = new HashMap<>(DEFAULT_KEYBINDS_STRUCTS.size());
		for (final Map.Entry<Integer, String> entry : DEFAULT_KEYBINDS_STRUCTS.entrySet()) {
			final int structId = entry.getKey();
			final String selection = entry.getValue();
			final Setting setting = findSettingByStructId(structId);
			final var stringEnum = setting.getDropdownEntriesEnum();
			Preconditions.checkNotNull(stringEnum);
			final int key = stringEnum.key(selection, 0);
			map.put(setting, key);
		}
		return Collections.unmodifiableMap(map);
	}
}
