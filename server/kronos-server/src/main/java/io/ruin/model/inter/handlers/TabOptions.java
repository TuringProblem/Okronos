package io.ruin.model.inter.handlers;

import io.ruin.api.utils.NumberUtils;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.actions.SlotAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.inter.utils.Unlock;
import io.ruin.model.skills.construction.HouseViewer;
import io.ruin.model.var.VarPlayerRepository;

public class TabOptions {

	public static void register() {
		AdvancedOptions.register();
		Notifications.register();
		Keybinding.register();
		HouseOptions.register();
	}

	/**
	 * Advanced
	 */

	private static final class AdvancedOptions {

		public static void register() {
          /*  InterfaceHandler.register(Interface.ADVANCED_OPTIONS, h -> {
                h.actions[4] = (SimpleAction) Config.CHATBOX_SCROLLBAR::toggle;
                h.actions[6] = (SimpleAction) Config.TRANSPARENT_SIDE_PANEL::toggle;
                h.actions[8] = (SimpleAction) Config.REMAINING_XP_TOOLTIP::toggle;
                h.actions[10] = (SimpleAction) Config.PRAYER_TOOLTIPS::toggle;
                h.actions[12] = (SimpleAction) Config.SPECIAL_ATTACK_TOOLTIPS::toggle;
                h.actions[16] = (SimpleAction) p -> {
                    if(Config.DATA_ORBS.toggle(p) == 1) {
                        if(p.isFixedScreen())
                            p.getPacketSender().removeInterface(Interface.FIXED_SCREEN, 10);
                        else
                            p.getPacketSender().removeInterface(p.getGameFrameId(), 28);
                    } else {
                        if(p.isFixedScreen())
                            p.getPacketSender().sendInterface(Interface.ORBS, Interface.FIXED_SCREEN, 10, 1);
                        else
                            p.getPacketSender().sendInterface(Interface.ORBS, p.getGameFrameId(), 28, 1);
                    }
                };
                h.actions[18] = (SimpleAction) Config.TRANSPARENT_CHATBOX::toggle;
                h.actions[20] = (SimpleAction) Config.CLICK_THROUGH_CHATBOX::toggle;
                h.actions[21] = (SimpleAction) p -> {
                    Config.SIDE_PANELS.toggle(p);
                    if(!p.isFixedScreen()) {
                        p.closeInterfaces();
                        DisplayHandler.updateResizedTabs(p);
                        p.openInterface(InterfaceType.MAIN, Interface.ADVANCED_OPTIONS);
                    }
                };
                h.actions[23] = (SimpleAction) Config.HOTKEY_CLOSING_PANELS::toggle;
            });*/
			//TODO fix


		}

		private static void open(Player player) {
			player.openInterface(ToplevelComponent.WORLD_MAP, Interface.ADVANCED_OPTIONS);
		}

	}

	/**
	 * Notifications
	 */

	private static final class Notifications {

		public static void register() {
            /*InterfaceHandler.register(Interface.NOTIFICATIONS, h -> {
                h.actions[2] = (SimpleAction) p -> p.closeInterface(InterfaceType.INVENTORY_OVERLAY);
                h.actions[7] = (OptionAction) (p, option) -> alterThreshold(p, option, Config.LOOT_DROP_NOTIFICATION_ENABLED, Config.LOOT_DROP_NOTIFICATION_VALUE);
                h.actions[11] = (SlotAction) Config.UNTRADEABLE_LOOT_NOTIFICATIONS::set;
                h.actions[12] = (SlotAction) Config.BOSS_KC_UPDATE::set;
                h.actions[14] = (OptionAction) (p, option) -> alterThreshold(p, option, Config.DROP_ITEM_WARNING_ENABLED, Config.DROP_ITEM_WARNING_VALUE);
            });*/
		}

		private static void alterThreshold(Player player, int option, VarPlayerRepository enabledConfig, VarPlayerRepository valueConfig) {
			boolean enabled = enabledConfig.get(player) == 1;
			int value = valueConfig.get(player);
			if (option == 1) {
				if (enabled) {
					/* Disable */
					enabledConfig.set(player, 0);
				} else if (value > 0) {
					/* Enable (value) */
					enabledConfig.set(player, 1);
				} else {
					/* Enable */
					player.integerInput("Set threshold value:", newValue -> {
						if (newValue > 0) {
							enabledConfig.set(player, 1);
							valueConfig.set(player, newValue);
						}
					});
				}
			} else {
				if (enabled && value > 0) {
					/* Change */
					player.integerInput("Change threshold value: (" + NumberUtils.formatNumber(value) + " coins)", newValue -> {
						if (newValue > 0) {
							valueConfig.set(player, newValue);
						} else {
							enabledConfig.set(player, 0);
							valueConfig.set(player, 0);
						}
					});
				}
			}
		}

		private static void open(Player player) {
			player.openInterface(ToplevelComponent.INVENTORY_TAB_AREA, Interface.NOTIFICATIONS);
			new Unlock(Interface.NOTIFICATIONS, 11).children(0, 1).unlockFirst(player);
			new Unlock(Interface.NOTIFICATIONS, 12).children(0, 1).unlockFirst(player);
		}

	}

	/**
	 * Display name
	 */

	private static final class DisplayName {

		public static void register() {
			InterfaceHandler.register(Interface.DISPLAY_NAME, h -> {
				h.actions[2] = (SimpleAction) p -> p.closeInterface(ToplevelComponent.INVENTORY_TAB_AREA);
				//todo - others
			});
		}

		private static void open(Player player) {
			player.openInterface(ToplevelComponent.INVENTORY_TAB_AREA, Interface.DISPLAY_NAME);
		}

	}

	/**
	 * Keybinding
	 */

	private static final class Keybinding {

		public static void register() {
			InterfaceHandler.register(Interface.KEYBINDING, h -> {
				/**
				 * Select tab
				 */
				h.actions[9] = (SimpleAction) p -> p.selectedKeybindConfig = VarPlayerRepository.KEYBINDS[0];
				h.actions[16] = (SimpleAction) p -> p.selectedKeybindConfig = VarPlayerRepository.KEYBINDS[1];
				h.actions[23] = (SimpleAction) p -> p.selectedKeybindConfig = VarPlayerRepository.KEYBINDS[2];
				h.actions[30] = (SimpleAction) p -> p.selectedKeybindConfig = VarPlayerRepository.KEYBINDS[3];
				h.actions[37] = (SimpleAction) p -> p.selectedKeybindConfig = VarPlayerRepository.KEYBINDS[4];
				h.actions[44] = (SimpleAction) p -> p.selectedKeybindConfig = VarPlayerRepository.KEYBINDS[5];
				h.actions[51] = (SimpleAction) p -> p.selectedKeybindConfig = VarPlayerRepository.KEYBINDS[6];
				h.actions[58] = (SimpleAction) p -> p.selectedKeybindConfig = VarPlayerRepository.KEYBINDS[7];
				h.actions[65] = (SimpleAction) p -> p.selectedKeybindConfig = VarPlayerRepository.KEYBINDS[8];
				h.actions[72] = (SimpleAction) p -> p.selectedKeybindConfig = VarPlayerRepository.KEYBINDS[9];
				h.actions[79] = (SimpleAction) p -> p.selectedKeybindConfig = VarPlayerRepository.KEYBINDS[10];
				h.actions[86] = (SimpleAction) p -> p.selectedKeybindConfig = VarPlayerRepository.KEYBINDS[11];
				h.actions[93] = (SimpleAction) p -> p.selectedKeybindConfig = VarPlayerRepository.KEYBINDS[12];
				h.actions[100] = (SimpleAction) p -> p.selectedKeybindConfig = VarPlayerRepository.KEYBINDS[13];
				/**
				 * Select key
				 */
				h.actions[111] = (SlotAction) (p, slot) -> {
					if (p.selectedKeybindConfig == null || slot < 0 || slot > 13)
						return;
					if (slot != 0) {
						for (VarPlayerRepository c : VarPlayerRepository.KEYBINDS) {
							if (c != p.selectedKeybindConfig && c.get(p) == slot) {
								c.set(p, 0);
								break;
							}
						}
					}
					p.selectedKeybindConfig.set(p, slot);
				};

				/**
				 * Toggle esc
				 */
				h.actions[103] = (SimpleAction) VarPlayerRepository.ESCAPE_CLOSES::toggle;
				/**
				 * Restore defaults
				 */
				h.actions[104] = (SimpleAction) p -> p.dialogue(
					new OptionsDialogue(
						new Option("Use OSRS Keybinds", () -> {
							for (VarPlayerRepository c : VarPlayerRepository.KEYBINDS)
								c.reset(p);
							VarPlayerRepository.ESCAPE_CLOSES.reset(p);
							open(p);
						}),
						new Option("Use Pre-EoC Keybinds", () -> {
							for (int i = 0; i < VarPlayerRepository.KEYBINDS.length; i++) {
								VarPlayerRepository c = VarPlayerRepository.KEYBINDS[i];
								if (i == 0)
									c.set(p, 5);
								else if (i >= 3 && i <= 6)
									c.set(p, i - 2);
								else
									c.set(p, 0);
							}
							VarPlayerRepository.ESCAPE_CLOSES.reset(p);
							open(p);
						}),
						new Option("Keep Current Keybinds", () -> {
							p.closeDialogue();
							open(p);
						})
					)
				);
				/**
				 * Closed action
				 */
				h.closedAction = (p, i) -> p.selectedKeybindConfig = null;
			});
		}

		private static void open(Player player) {
			player.openInterface(ToplevelComponent.MAINMODAL, Interface.KEYBINDING);
			new Unlock(Interface.KEYBINDING, 111).children(0, 13).unlockFirst(player);
		}

	}

	/**
	 * House
	 */

	private static final class HouseOptions {

		public static void register() {
			InterfaceHandler.register(Interface.HOUSE_OPTIONS, h -> {
				h.actions[17] = (SimpleAction) p -> p.closeInterface(ToplevelComponent.INVENTORY_TAB_AREA);
				h.actions[1] = (SimpleAction) HouseViewer::open;
				h.actions[5] = (SimpleAction) p -> setBuildingMode(p, 1);
				h.actions[6] = (SimpleAction) p -> setBuildingMode(p, 0);
				h.actions[12] = (SimpleAction) p -> VarPlayerRepository.RENDER_DOORS_MODE.set(p, 0);
				h.actions[14] = (SimpleAction) p -> VarPlayerRepository.RENDER_DOORS_MODE.set(p, 1);
				h.actions[16] = (SimpleAction) p -> VarPlayerRepository.RENDER_DOORS_MODE.set(p, 2);
				h.actions[8] = (SimpleAction) p -> VarPlayerRepository.TELEPORT_INSIDE.set(p, 0);
				h.actions[9] = (SimpleAction) p -> VarPlayerRepository.TELEPORT_INSIDE.set(p, 1);
				h.actions[17] = (SimpleAction) p -> {
					if (!p.isInOwnHouse())
						p.sendMessage("You're not in your house.");
					else
						p.house.expelGuests();
				};
				h.actions[18] = (SimpleAction) p -> {
					if (p.getCurrentHouse() == null)
						p.sendMessage("You're not in a house.");
					else
						p.getCurrentHouse().leave(p);
				};
				h.actions[19] = (SimpleAction) p -> {
					if (!p.isInOwnHouse()) {
						p.sendMessage("You're not in your house.");
						return;
					}
					if (!p.house.isHasBellPull()) {
						p.sendMessage("Your house must have a bell-pull in order to use this feature.");
						return;
					}
					p.house.callServant();
				};
			});
		}

		private static void setBuildingMode(Player player, int value) {
			if (VarPlayerRepository.BUILDING_MODE.get(player) != value) {
				VarPlayerRepository.BUILDING_MODE.set(player, value);
				if (player.isInOwnHouse()) {
					player.house.expelGuests();
					player.house.buildAndEnter(player, player.getPosition().localPosition(), value == 1);
				}
			}
		}

		private static void open(Player player) {
			player.openInterface(ToplevelComponent.SKILLS_TAB_AREA, Interface.HOUSE_OPTIONS);
			player.getPacketSender().sendString(370, 16, "Number of rooms: " + (player.house == null ? "0" : player.house.getRoomCount()));
		}

	}

}
