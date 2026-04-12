package io.ruin.model.inter.handlers.advancedsettings;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerDisplayMode;
import io.ruin.model.inter.AccessMasks;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.actions.SlotAction;

import java.util.List;
import java.util.Map;

import static io.ruin.model.var.VarPlayerRepository.appendPersistentVarbit;
import static io.ruin.model.var.VarPlayerRepository.appendPersistentVarp;
import static io.ruin.utility.CS2Script.CHATDEFAULT_RESTOREINPUT;
import static io.ruin.model.inter.handlers.advancedsettings.SettingVariables.*;

/**
 * @author Kris | 10/06/2022
 */
@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class SettingsInterface {

	static final int INTERFACE_ID = 134;

	static final int COMPONENT_MORE_INFO = 5;
	static final int COMPONENT_CLOSE = 4;
	static final int COMPONENT_SEARCH = 10;
	static final int COMPONENT_SETTING = 19;
	static final int COMPONENT_SLIDER = 21;
	static final int COMPONENT_CATEGORY = 23;
	static final int COMPONENT_CANCEL = 25;
	static final int COMPONENT_DROPDOWN = 28;

	public static void register() {
		for (final Map<Integer, Integer> varpMaps : SettingVariables.ALL_VARP_SETTINGS) {
			for (final Integer value : varpMaps.values()) {
				appendPersistentVarp(value);
			}
		}
		for (final Map<Integer, Integer> varbitMap : SettingVariables.ALL_VARBIT_SETTINGS) {
			for (final Integer value : varbitMap.values()) {
				appendPersistentVarbit(value);
			}
		}

		for (SliderSettingVarps value : SliderSettingVarps.values) {
			appendPersistentVarp(value.getVarp(), value.getDefaultValue());
		}

		for (ToggleSettingVarps value : ToggleSettingVarps.values) {
			appendPersistentVarp(value.getVarp(), value.getDefaultValue());
		}

		for (ToggleSettingVarBits value : ToggleSettingVarBits.values) {
			appendPersistentVarbit(value.getVarbit(), value.getDefaultValue());
		}

		for (OtherVarBits value : OtherVarBits.values) {
			appendPersistentVarbit(value.getVarbit(), value.getDefaultValue());
		}

		InterfaceHandler.register(INTERFACE_ID, h -> {
			h.closedAction = (p, i) -> {
				CHATDEFAULT_RESTOREINPUT.sendScript(p);
			};
			h.actions[COMPONENT_CLOSE] = (SimpleAction) p -> {
				p.closeInterfaces();
			};

			h.actions[COMPONENT_MORE_INFO] = (SimpleAction) p -> {
				p.varbitFlip(SHOW_LESS_OR_MORE_INFORMATION_VARBIT);
			};

			h.actions[COMPONENT_SEARCH] = (SimpleAction) p -> {
				Settings.setCurrentSelectedCategory(p, null);
				p.varbitSend(SETTINGS_SEARCH_LEFT_VARBIT, 1);
				p.varbitSend(SETTINGS_SEARCH_RIGHT_VARBIT, 1);
			};

			h.actions[COMPONENT_SLIDER] = (SlotAction) (p, slot) -> {
				var category = Settings.getCurrentSelectedCategory(p);
				var position = Settings.findSliderPosition(category, slot);
				slider(p, position.getSlider(), slot - position.getStartingPosition());
			};

			h.actions[COMPONENT_DROPDOWN] = (SlotAction) (player, slotId) -> {
				final Setting setting = Settings.getCurrentDropdownSetting(player);
				if (setting == null)
					return;
				if (!setting.checkSetting(player))
					return;
				final int selectedEntryIndex = slotId / 3;
				final boolean keybind = Settings.isCurrentDropdownSettingKeybind(player);
				if (setting.getStructId() == SettingStructs.GAME_CLIENT_LAYOUT_STRUCT_ID) {
					if (selectedEntryIndex == 0) {
						PlayerDisplayMode.set(player, PlayerDisplayMode.Mode.Fixed);
					} else if (selectedEntryIndex == 1) {
						PlayerDisplayMode.set(player, PlayerDisplayMode.Mode.Resizable);
					} else if (selectedEntryIndex == 2) {
						PlayerDisplayMode.set(player, PlayerDisplayMode.Mode.ResizableModern);
					}
					return;
				}
				if (keybind) {
					final List<Setting> keybinds = Settings.SETTINGS_BY_TYPE.get(SettingType.KEYBIND);
					for (final Setting otherSetting : keybinds) {
						if (otherSetting == setting)
							continue;
						if (SettingVariables.getVariableValue(otherSetting, player) == selectedEntryIndex) {
							SettingVariables.setVariableValue(otherSetting, player, 0);
						}
					}
				}
				SettingVariables.setVariableValue(setting, player, selectedEntryIndex);
			};

			h.actions[COMPONENT_CATEGORY] = (SlotAction) (p, slot) -> {
				Settings.setCurrentSelectedCategory(p, Settings.getCategory(slot));
				p.varbitSend(SETTINGS_SEARCH_LEFT_VARBIT, 0);
				p.varbitSend(SETTINGS_SEARCH_RIGHT_VARBIT, 0);
				p.varbitSend(SETTINGS_CURRENT_CATEGORY_VARBIT, slot);
				p.sendClientScript(2158);
			};

			h.actions[COMPONENT_CANCEL] = (SimpleAction) (p) -> {
				Settings.setCurrentDropdownSetting(p, null);
			};

			h.actions[COMPONENT_SETTING] = (SlotAction) (p, slot) -> {
				var category = Settings.getCurrentSelectedCategory(p);
				var settings = category != null ? category.getSettings() : Settings.ALL_SETTINGS;
				var setting = settings.get(slot);
				p.debug(
						"setting: name: " + setting.getName() + " struct: " + setting.getStructId() + " id: " + setting.getId()
								+ " type: " + setting.getType());
				if (!setting.checkSetting(p)) {
					p.debug("setting disabled");
					return;
				}

				switch (setting.getType()) {
					case TOGGLE:
						toggle(p, setting);
						break;
					case DROPDOWN:
						dropDown(p, setting, false);
						break;
					case KEYBIND:
						dropDown(p, setting, true);
						break;
					case INPUT:
						input(p, setting);
						break;
					case BUTTON:
						button(p, setting);
						break;
					case COLOUR:
						colour(p, setting);
						break;
					default:
						p.debug("Illegal setting type '" + setting.getType());
						break;
				}
			};
		});
	}

	protected void attach() {
		// put(4, "Close");
		// put(5, "Show less/more information");
		// put(10, "Search");
		// put(19, "Setting");
		// put(21, "Slider");
		// put(23, "Category");
		// put(25, "Cancel drop-down selection");
		// put(28, "Drop-down menu");
	}

	protected void build() {
		// bind("Drop-down menu", (player, slotId, itemId, option) -> {
	}

	public static void open(Player player) {
		player.varbitSend(SETTINGS_SEARCH_LEFT_VARBIT, 0);
		player.varbitSend(SETTINGS_SEARCH_RIGHT_VARBIT, 0);
		player.openInterface(ToplevelComponent.WORLD_MAP, INTERFACE_ID);
		player.accessMasks(INTERFACE_ID, COMPONENT_SETTING, 0,
				Settings.ALL_SETTINGS.size(), AccessMasks.ClickOp1);
		player.accessMasks(INTERFACE_ID, COMPONENT_SLIDER, 0, Settings.SLIDER_NOTCH_COUNT_SUM, AccessMasks.ClickOp1);
		player.accessMasks(INTERFACE_ID, COMPONENT_CATEGORY, 0, Settings.CATEGORIES.size(), AccessMasks.ClickOp1);
		player.accessMasks(INTERFACE_ID, COMPONENT_DROPDOWN, 0, Settings.DROPDOWN_ENTRIES_MAX_COUNT * 3,
				AccessMasks.ClickOp1);
		final SettingCategory currentCategory = Settings.getCurrentSelectedCategory(player);
		if (currentCategory == null) {
			player.varbitSend(SETTINGS_CURRENT_CATEGORY_VARBIT, 0);
		}
	}

	private static void toggle(Player player, Setting setting) {
		switch (setting.getStructId()) {
			case SettingStructs.SHOW_DATA_ORBS_STRUCT_ID:
				if (player.varbit(SettingVariables.HIDE_DATA_ORBS_VARBIT_ID) == 0) {
					player.closeInterface(ToplevelComponent.ORBS);
				} else {
					player.openInterface(ToplevelComponent.ORBS, Interface.ORBS);
				}
				break;
			case SettingStructs.LOOT_DROP_NOTIFICATIONS_STRUCT_ID:
				if (player.varbit(MINIMUM_LOOT_ITEM_VALUE_VARBIT_ID) == 0) {
					final Setting otherSetting = Settings
							.findSettingByStructId(SettingStructs.MINIMUM_ITEM_VALUE_NEEDED_FOR_LOOT_NOTIFICATION_STRUCT_ID);
					input(player, otherSetting);
					return;
				}
				break;
			case SettingStructs.DROP_ITEM_WARNING_STRUCT_ID:
				if (player.varbit(MINIMUM_DROP_ITEM_VALUE_VARBIT_ID) == 0) {
					final Setting otherSetting = Settings
							.findSettingByStructId(SettingStructs.MINIMUM_ITEM_VALUE_NEEDED_FOR_DROP_ITEM_WARNING_STRUCT_ID);
					input(player, otherSetting);
					return;
				}
				break;
			case SettingStructs.COLLECTION_LOG_NEW_ADDITION_NOTIFICATION_STRUCT_ID: {
				final int currentValue = player.varbit(COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID);
				final int newValue = (currentValue + 1) & 0x1;
				player.varbitSend(COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID,
						(currentValue & ~0x1) | newValue);
				return;
			}
			case SettingStructs.COLLECTION_LOG_NEW_ADDITION_POPUP_STRUCT_ID: {
				final int currentValue = player.varbit(COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID);
				final int newValue = (currentValue + 2) & 0x2;
				player.varbitSend(COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID,
						(currentValue & ~0x2) | newValue);
				return;
			}
		}

		var currentValue = SettingVariables.getVariableValue(setting, player);
		SettingVariables.setVariableValue(setting, player, currentValue == 1 ? 0 : 1);
	}

	private static void dropDown(Player player, Setting setting, boolean keybind) {
		Settings.setCurrentDropdownSetting(player, setting);
		Settings.setCurrentDropdownSettingKeybind(player, keybind);
	}

	private static void slider(Player player, Setting setting, int value) {
		switch (setting.getStructId()) {
			case SettingStructs.MUSIC_VOLUME_STRUCT_ID:
				if (value != 0) {
					if (SettingVariables.getVariableValue(setting, player) == 0) {
						// TODO(polish) player.getMusic().restartCurrent();
					}
				}
				SettingVariables.setVariableValue(setting, player, value * 5);
				player.varbitSend(MUSIC_VOLUME_MUTED_VARBIT_ID, value == 0 ? 1 : 0);
				break;
			case SettingStructs.SOUND_EFFECT_VOLUME_STRUCT_ID:
				SettingVariables.setVariableValue(setting, player, value * 5);
				player.varbitSend(SOUND_EFFECT_VOLUME_MUTED_VARBIT_ID, value == 0 ? 1 : 0);
				break;
			case SettingStructs.AREA_SOUND_VOLUME_STRUCT_ID:
				SettingVariables.setVariableValue(setting, player, value * 5);
				player.varbitSend(AREA_EFFECT_MUTED_VARBIT_ID, value == 0 ? 1 : 0);
				break;
			case SettingStructs.SCREEN_BRIGHTNESS_STRUCT_ID:
				SettingVariables.setVariableValue(setting, player, value * 5);
				break;
			default:
				SettingVariables.setVariableValue(setting, player, value);
				break;
		}
	}

	private static void button(Player player, Setting setting) {
		switch (setting.getStructId()) {
			case SettingStructs.CLEAR_YOUR_HIGHLIGHTED_TILES_STRUCT_ID:
			case SettingStructs.RESET_INTERFACE_SCALING_STRUCT_ID:
			case SettingStructs.RESTORE_MINIMAP_ZOOM_STRUCT_ID:
				throw new IllegalStateException("Enhanced client not supported.");
			case SettingStructs.RESTORE_DEFAULT_KEYBINDS_STRUCT_ID:
				promptDefaultKeybinds(player);
				break;
			case SettingStructs.RESET_OPAQUE_CHAT_COLOURS_STRUCT_ID:
				setAllSettings(player, Settings.OPAQUE_COLOUR_SETTINGS, 0);
				break;
			case SettingStructs.RESET_TRANSPARENT_CHAT_COLOURS_STRUCT_ID:
				setAllSettings(player, Settings.TRANSPARENT_COLOUR_SETTINGS, 0);
				break;
			case SettingStructs.RESET_QUEST_LIST_TEXT_COLOURS_STRUCT_ID:
				setAllSettings(player, Settings.QUEST_COLOUR_SETTINGS, 0);
				break;
			case SettingStructs.RESET_SPLIT_CHAT_COLOURS_STRUCT_ID:
				setAllSettings(player, Settings.SPLIT_COLOUR_SETTINGS, 0);
				break;
			case SettingStructs.ENABLE_TELEPORT_WARNINGS_STRUCT_ID:
				setAllSettings(player, Settings.TELEPORT_WARNING_SETTINGS, 1);
				break;
			case SettingStructs.DISABLE_TELEPORT_WARNINGS_STRUCT_ID:
				setAllSettings(player, Settings.TELEPORT_WARNING_SETTINGS, 0);
				break;
			case SettingStructs.ENABLE_TABLET_WARNINGS_STRUCT_ID:
				setAllSettings(player, Settings.TABLET_WARNING_SETTINGS, 0);
				break;
			case SettingStructs.DISABLE_TABLET_WARNINGS_STRUCT_ID:
				setAllSettings(player, Settings.TABLET_WARNING_SETTINGS, 1);
				break;
		}
	}

	private static void setAllSettings(Player player, List<Setting> settings, int value) {
		for (final Setting setting : settings) {
			SettingVariables.setVariableValue(setting, player, value);
		}
	}

	private static void input(Player player, Setting setting) {
		switch (setting.getStructId()) {
			case SettingStructs.MINIMUM_ITEM_VALUE_NEEDED_FOR_LOOT_NOTIFICATION_STRUCT_ID:
				player.inputInt("Set threshold value:", value -> {
					final int result = Math.min(value, 500_000_000);
					player.varbitSend(SettingVariables.LOOT_DROP_NOTIFICATIONS_VARBIT_ID,
							result == 0 ? 0 : 1);
					player.varbitSend(MINIMUM_LOOT_ITEM_VALUE_VARBIT_ID, result);
				});
				break;
			case SettingStructs.MINIMUM_ITEM_VALUE_NEEDED_FOR_DROP_ITEM_WARNING_STRUCT_ID:
				player.inputInt("Set threshold value:", value -> {
					final int result = Math.min(value, 500_000_000);
					player.varbitSend(SettingVariables.DROP_ITEM_WARNING_VARBIT_ID, result == 0 ? 0 : 1);
					player.varbitSend(MINIMUM_DROP_ITEM_VALUE_VARBIT_ID, result);
					if (value == 0) {
						player.varbitSend(SettingVariables.UNTRADEABLE_LOOT_NOTIFICATIONS_VARBIT_ID, 0);
					}
				});
				break;
			case SettingStructs.MINIMUM_ITEM_VALUE_NEEDED_FOR_ALCHEMY_SPELLS_WARNING_STRUCT_ID:
				player.inputInt("Set threshold value:", value -> {
					player.varbitSend(MINIMUM_ALCH_TRIGGER_VALUE_VARBIT_ID, value);
				});
				break;
			case SettingStructs.MAX_HIT_HITSPLATS_THRESHOLD_STRUCT_ID:
				player.inputInt("Set threshold for max hits (2-500):", value -> {
					player.varbitSend(MAX_HITSPLATS_THRESHOLD_VARBIT_ID, value);
				});
				break;
		}
	}

	private static void colour(Player player, Setting setting) {
		player.varbitSend(CURRENT_SETTING_VARBIT_ID, setting.getId());
		// final int currentColour = SettingVariables.getVariableValue(setting, player);
		// final int defaultColour = setting.getDefaultColour();
		// player.getInterfaceHandler().sendInterface(GameInterface.COLOUR_PICKER.getId(),
		// InterfacePosition.COLOUR_PICKER.getResizableComponent(),
		// PaneType.ADVANCED_SETTINGS, false);
		// player.sendClientScript(
		// 4185,
		// (INTERFACE_ID << 16) |
		// InterfacePosition.COLOUR_PICKER.getResizableComponent(),
		// (currentColour == 0 ? defaultColour : currentColour) - 1);
		// player.inputInt(value -> {
		// player.getInterfaceHandler().closeInterfaceSpecific(
		// InterfacePosition.COLOUR_PICKER.getResizableComponent(),
		// PaneType.ADVANCED_SETTINGS);
		// if (value != Integer.MAX_VALUE) {
		// SettingVariables.setVariableValue(setting, player, value + 1);
		// }
		// final SettingCategory category = Settings.getCurrentSelectedCategory(player);
		// if (category != null)
		// return;
		// player.varbitSend(SETTINGS_SEARCH_LEFT_VARBIT, 1);
		// player.varbitSend(SETTINGS_SEARCH_RIGHT_VARBIT, 1);
		// player.sendClientScript(4020);
		// });
	}

	private static void promptDefaultKeybinds(Player player) {
		// player.getInterfaceHandler().closeInterface(getInterface());
		// player.getDialogueManager().start(new Dialogue(player) {
		// @Override
		// public void buildDialogue() {
		// options("Are you sure you want to reset your keybinds?",
		// new DialogueOption("Yes.", () -> {
		// setDefaultKeybinds(player);
		// GameInterface.ADVANCED_SETTINGS.open(player);
		// }),
		// new DialogueOption("No.", () ->
		// GameInterface.ADVANCED_SETTINGS.open(player)));
		// }
		// });
	}

	public static void setDefaultKeybinds(Player player) {
		try {
			for (final Map.Entry<Setting, Integer> entry : Settings.DEFAULT_KEYBINDS.entrySet()) {
				final Setting setting = entry.getKey();
				final int value = entry.getValue();
				SettingVariables.setVariableValue(setting, player, value);
			}
		} catch (Error e) {
			e.printStackTrace();
		}
	}
}
