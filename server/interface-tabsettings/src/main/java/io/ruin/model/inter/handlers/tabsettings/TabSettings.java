package io.ruin.model.inter.handlers.tabsettings;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerDisplayMode;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.ToplevelInterfaceType;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.actions.SlotAction;
import io.ruin.model.inter.handlers.advancedsettings.SettingsInterface;
import io.ruin.model.skills.construction.HouseViewer;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.CS2Script;

import static io.ruin.model.inter.InterfaceEventMask.ClickOp1;
import static io.ruin.model.inter.InterfaceEventMask.getMask;

public class TabSettings {

	public static void register() {

		InterfaceHandler.register(116, h -> {
			h.openAction = (p) -> {
				var ps = p.getPacketSender();
				ps.sendIfEvents(116, 104, 0, 21, getMask(ClickOp1));
				ps.sendIfEvents(116, 118, 0, 21, getMask(ClickOp1));
				ps.sendIfEvents(116, 133, 0, 21, getMask(ClickOp1));
				ps.sendIfEvents(116, 38, 1, 4, getMask(ClickOp1));
				ps.sendIfEvents(116, 39, 1, 5, getMask(ClickOp1));
				ps.sendIfEvents(116, 41, 0, 21, getMask(ClickOp1));
			};

			h.actions[5] = (SlotAction) (p, slot) -> VarPlayerRepository.PK_SKULL_PREVENTION.toggle(p);
			h.actions[29] = (SimpleAction) VarPlayerRepository.ACCEPT_AID::toggle;
			h.actions[30] = (SimpleAction) p -> p.getMovement().toggleRunning();
			h.actions[31] = (SimpleAction) TabSettings.HouseOptions::open;
			h.actions[32] = (SimpleAction) SettingsInterface::open;
			h.actions[38] = (SlotAction) (p, slot) -> VarPlayerRepository.PLAYER_ATTACK_OPTION.set(p, slot - 1);
			h.actions[39] = (SlotAction) (p, slot) -> VarPlayerRepository.NPC_ATTACK_OPTION.set(p, slot - 1);

			h.actions[41] = (SlotAction) (p, slot) -> {
				switch (slot) {
					case 1 -> {
						PlayerDisplayMode.set(p, PlayerDisplayMode.Mode.Fixed);
					}
					case 2 -> {
						PlayerDisplayMode.set(p, PlayerDisplayMode.Mode.Resizable);
					}
					case 3 -> {
						PlayerDisplayMode.set(p, PlayerDisplayMode.Mode.ResizableModern);
					}
				}
			};

			h.actions[44] = (SlotAction) (p, slot) -> {
				VarPlayerRepository.ZOOMING_DISABLED.toggle(p);
			};

			h.actions[104] = (SlotAction) (p, slot) -> {
				VarPlayerRepository.MUSIC_VOLUME.set(p, slot * 5);
			};

			h.actions[118] = (SlotAction) (p, slot) -> {
				VarPlayerRepository.SOUND_EFFECT_VOLUME.set(p, slot * 5);
			};

			h.actions[133] = (SlotAction) (p, slot) -> {
				VarPlayerRepository.AREA_SOUND_EFFECT_VOLUME.set(p, slot * 5);
			};

		});

		HouseOptions.register();
	}

	private static final class HouseOptions {

		public static void register() {
			InterfaceHandler.register(Interface.HOUSE_OPTIONS, h -> {
				h.actions[17] = (SimpleAction) p -> p.closeInterface(ToplevelComponent.INVENTORY_TAB_AREA);
				h.actions[1] = (SimpleAction) HouseViewer::open;
				h.actions[5] = (SimpleAction) p -> {
					if (!p.isInOwnHouse())
						p.sendMessage("You should probably try to be in your house first.");
					else
						setBuildingMode(p, 1);
				};
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
			player.openInterface(ToplevelComponent.SIDEMODAL, Interface.HOUSE_OPTIONS);
			player.getPacketSender().sendString(370, 16,
					"Number of rooms: " + (player.house == null ? "0" : player.house.getRoomCount()));
		}

	}
}
