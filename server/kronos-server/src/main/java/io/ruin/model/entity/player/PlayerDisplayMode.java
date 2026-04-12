package io.ruin.model.entity.player;

import io.ruin.Server;
import io.ruin.cache.EnumType;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.ToplevelInterfaceType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.CS2Script;
import io.ruin.model.inter.questtab.JournalTab;

import static io.ruin.model.inter.InterfaceEventMask.*;

import java.util.concurrent.TimeUnit;

public class PlayerDisplayMode {
	public enum Mode {
		Fixed,
		Resizable,
		ResizableModern;

		public int raw() {
			switch (this) {
				case Fixed -> {
					return 1;
				}
				case Resizable -> {
					return 2;
				}
				case ResizableModern -> {
					return 3;
				}
				default -> {
					throw new IllegalStateException();
				}
			}
		}

		public static Mode fromRaw(int raw) {
			switch (raw) {
				case 1 -> {
					return Mode.Fixed;
				}
				case 2 -> {
					return Mode.Resizable;
				}
				case 3 -> {
					return Mode.ResizableModern;
				}
				default -> {
					return Mode.Fixed;
				}
			}
		}
	}

	public static PlayerDisplayMode.Mode mode(Player player) {
		return Mode.fromRaw(player.displayMode);
	}

	public static void set(Player player, Mode mode) {
		var previous = PlayerDisplayMode.Mode.fromRaw(player.displayMode);
		if (previous != mode) {
			player.displayMode = mode.raw();
			refresh(player);
		}
	}

	public static void refresh(Player player) {
		var mode = PlayerDisplayMode.Mode.fromRaw(player.displayMode);
		update(player, mode);
	}

	public static void update(Player player, Mode mode) {
		var ps = player.getPacketSender();

		updateDisplay(player);

		player.openInterface(ToplevelComponent.CHATBOX);
		player.openInterface(ToplevelComponent.PRIVATE_CHAT);
		VarPlayerRepository.PLAYER_INFO_COMBAT_LVL.set(player, player.getCombat().getLevel());

		if (VarPlayerRepository.HIDE_DATA_ORBS.get(player) == 0) {
			player.openInterface(ToplevelComponent.ORBS);
		}

		if (VarPlayerRepository.XP_COUNTER_SHOWN.get(player) == 1) {
			player.openInterface(ToplevelComponent.XP_TRACKER);
		}

		player.openInterface(ToplevelComponent.SKILLS_TAB_AREA);
		player.openInterface(ToplevelComponent.INVENTORY_TAB_AREA);
		player.openInterface(ToplevelComponent.EQUIPMENT_TAB_AREA);
		player.openInterface(ToplevelComponent.PRAYER_TAB_AREA);
		player.openInterface(ToplevelComponent.SPELLBOOK_TAB_AREA);
		player.openInterface(ToplevelComponent.ACCOUNT_MANAGEMENT_TAB_AREA);

		var socialComponent = VarPlayerRepository.FRIENDS_AND_IGNORE_TOGGLE.get(player) == 0
				? Interface.FRIENDS_LIST
				: Interface.IGNORE_LIST;
		ps.sendInterface(socialComponent, ToplevelComponent.FRIENDLIST_TAB_AREA);

		player.openInterface(ToplevelComponent.SIDEBAR);

		ps.sendClientScript(3970, "IIi", 46661634, 46661635,
				(int) TimeUnit.MILLISECONDS.toMinutes(player.playTime * Server.tickMs()));

		player.openInterface(ToplevelComponent.CLAN_TAB_AREA);
		player.openInterface(ToplevelComponent.LOGOUT_TAB_AREA);
		player.openInterface(ToplevelComponent.SETTINGS_TAB_AREA);
		player.openInterface(ToplevelComponent.EMOTE_TAB_AREA);
		player.openInterface(ToplevelComponent.COMBAT_TAB_AREA);
		player.openInterface(ToplevelComponent.QUEST_TAB_AREA);
		player.openInterface(ToplevelComponent.MUSIC_TAB_AREA);

		updateDisplayEvents(player);
		CS2Script.SETTINGS_CLIENT_MODE.sendScript(player, mode.raw() - 1);
	}

	private static void updateDisplay(Player player) {
		var oldMode = player.getToplevelType();
		var ps = player.getPacketSender();

		switch (mode(player)) {
			case Mode.Fixed -> {
				ps.sendToplevel(ToplevelInterfaceType.FIXED);
			}
			case Mode.Resizable -> {
				VarPlayerRepository.SIDE_PANELS.set(player, 0);
				ps.sendToplevel(ToplevelInterfaceType.RESIZABLE_CLASSIC);
			}
			case Mode.ResizableModern -> {
				VarPlayerRepository.SIDE_PANELS.set(player, 1);
				ps.sendToplevel(ToplevelInterfaceType.RESIZABLE_MODERN);
			}
		}

		var newMode = player.getToplevelType();
		if (oldMode != null && oldMode != newMode) {
			moveSubInterfaces(oldMode, newMode, player);
		}
	}

	private static void moveSubInterfaces(ToplevelInterfaceType fromMode, ToplevelInterfaceType toMode, Player player) {
		var ps = player.getPacketSender();
		var oldEnumMap = EnumType.get(fromMode.getTopLevelEnum()).getIntValues();
		var newEnumMap = EnumType.get(toMode.getTopLevelEnum()).getIntValues();

		for (var newComponent : newEnumMap.entrySet()) {
			var key = newComponent.getKey();
			var to = newComponent.getValue();

			if (to == -1) {
				continue;
			}

			int from = oldEnumMap.getOrDefault(key, -1);

			if (from == -1) {
				continue;
			}

			ps.moveInterface(from >> 16, from & 0xffff, to >> 16, to & 0xffff);
		}
	}

	//@formatter:off
	public static void updateDisplayEvents(Player player) {
		var ps = player.getPacketSender();

		ps.sendIfEvents(712, 3, 3, 7, getMask(ClickOp1, ClickOp2, ClickOp3, ClickOp4));
		ps.sendIfEvents(712, 3, 3, 7, getMask(ClickOp1, ClickOp2, ClickOp3, ClickOp4));

		ps.sendIfEvents(259, 2, 0, 11, getMask(ClickOp1, ClickOp2));
		ps.sendIfEvents(76, 26, 0, 23, getMask(ClickOp1));

		ps.sendIfEvents(116, 23, 0, 21, getMask(ClickOp1));
		ps.sendIfEvents(116, 55, 0, 21, getMask(ClickOp1));
		ps.sendIfEvents(116, 84, 1, 3, getMask(ClickOp1));
		ps.sendIfEvents(116, 38, 1, 4, getMask(ClickOp1));
		ps.sendIfEvents(116, 39, 1, 5, getMask(ClickOp1));
		ps.sendIfEvents(116, 41, 0, 21, getMask(ClickOp1));

		ps.sendIfEvents(116, 69, 0, 21, getMask(ClickOp1));
		ps.sendIfEvents(728, 15, 0, 0, getMask(ClickOp1));
		ps.sendIfEvents(149, 0, 0, 27, getMask(ClickOp2, ClickOp3, ClickOp4, ClickOp6, ClickOp7, ClickOp10, UseOnGroundItem, UseOnNpc, UseOnObject, UseOnPlayer, UseOnInventory, UseOnComponent, DragDepth1, DragTargetable, ComponentTargetable));
		ps.sendIfEvents(Interface.MAGIC_BOOK, 198, 0, 6, getMask(ClickOp1));
		ps.sendIfEvents(216, 2, 0, 53, getMask(ClickOp1, ClickOp2));
		CS2Script.SETTINGS_INTERFACE_SCALING.sendScript(player, 0);
		CS2Script.BUFF_BAR_LAYOUT_REDRAW.sendScript(player);
		if (player.currentSection == null)
			player.currentSection = JournalTab.Section.PLAYER_INFO;
		JournalTab.updateCurrentTab(player);
	}
	//@formatter:on
}
