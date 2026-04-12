package io.ruin.model.inter;

import io.ruin.cache.EnumType;
import io.ruin.model.entity.player.Player;

/**
 * Defines the toplevel component
 */
public enum ToplevelComponent {
	POH_LOADING(1, ClientInterfaceType.MODAL), // check
	OVERLAY(1, ClientInterfaceType.OVERLAY),
	OVERLAY2(13, ClientInterfaceType.OVERLAY),
	HP_HUD(2, 303, ClientInterfaceType.OVERLAY),
	WILDERNESS_OVERLAY(3, ClientInterfaceType.OVERLAY),
	TARGET_OVERLAY(4, ClientInterfaceType.OVERLAY),
	MINIGAME_OVERLAY(5, ClientInterfaceType.OVERLAY), //
	BUFF_BAR(6, 651, ClientInterfaceType.OVERLAY),
	PC(8, 407, ClientInterfaceType.OVERLAY),
	XP_TRACKER(9, 122, ClientInterfaceType.OVERLAY),
	NOTIFICATION(13, ClientInterfaceType.OVERLAY), //
	MAINMODAL(16, ClientInterfaceType.MODAL),
	MAIN_OVERLAY(16, ClientInterfaceType.OVERLAY),
	WORLD_MAP(18, 595, ClientInterfaceType.MODAL),
	GRAVESTONE(20, ClientInterfaceType.OVERLAY),
	ORBS(33, 160, ClientInterfaceType.OVERLAY),
	SIDEBAR(34, 728, ClientInterfaceType.OVERLAY),
	SIDEMODAL(74, ClientInterfaceType.MODAL),

	// tabs
	COMBAT_TAB_AREA(76, 593, ClientInterfaceType.OVERLAY),
	SKILLS_TAB_AREA(77, 320, ClientInterfaceType.OVERLAY),
	QUEST_TAB_AREA(78, 629, ClientInterfaceType.OVERLAY),
	INVENTORY_TAB_AREA(79, 149, ClientInterfaceType.OVERLAY),
	EQUIPMENT_TAB_AREA(80, 387, ClientInterfaceType.OVERLAY),
	DIZANA_QUIVER_TAB_AREA(74, 592, ClientInterfaceType.OVERLAY),
	PRAYER_TAB_AREA(81, 541, ClientInterfaceType.OVERLAY),
	QUICK_PRAYERS_TAB_AREA(81, 77, ClientInterfaceType.OVERLAY),
	SPELLBOOK_TAB_AREA(82, 218, ClientInterfaceType.OVERLAY),
	CLAN_TAB_AREA(83, 7, ClientInterfaceType.OVERLAY),
	GROUP_CLAN_TAB_AREA(83, 727, ClientInterfaceType.OVERLAY),
	ACCOUNT_MANAGEMENT_TAB_AREA(84, 109, ClientInterfaceType.OVERLAY),
	FRIENDLIST_TAB_AREA(85, 429, ClientInterfaceType.OVERLAY),
	LOGOUT_TAB_AREA(86, 182, ClientInterfaceType.OVERLAY),
	SETTINGS_TAB_AREA(87, 116, ClientInterfaceType.OVERLAY),
	EMOTE_TAB_AREA(88, 216, ClientInterfaceType.OVERLAY),
	MUSIC_TAB_AREA(89, 239, ClientInterfaceType.OVERLAY),
	TAB_AREA(95, ClientInterfaceType.MODAL),

	PRIVATE_CHAT(93, 163, ClientInterfaceType.OVERLAY),
	CHATBOX(96, 162, ClientInterfaceType.OVERLAY),

	MINIMAP_HUD(ClientInterfaceType.OVERLAY, uc(ToplevelInterfaceType.RESIZABLE_CLASSIC, 93)),

	;

	private final int component;

	public int getComponent() {
		return component;
	}

	public int getInterfaceId() {
		return defaultInterfaceId;
	}

	public ClientInterfaceType getInterfaceType() {
		return interfaceType;
	}

	private final int defaultInterfaceId;
	private final ClientInterfaceType interfaceType;
	private UnmappableToplevelComponent[] unmappableComponents;

	ToplevelComponent(int component, int defaultInterfaceId, ClientInterfaceType interfaceType) {
		this.defaultInterfaceId = defaultInterfaceId;
		this.component = component;
		this.interfaceType = interfaceType;
	}

	public static final ToplevelComponent[] VALUES = values();

	ToplevelComponent(int defaultInterfaceId, ClientInterfaceType interfaceType,
	                  UnmappableToplevelComponent... components) {
		this.defaultInterfaceId = defaultInterfaceId;
		this.interfaceType = interfaceType;
		this.unmappableComponents = components;
		this.component = -1;
	}

	ToplevelComponent(ClientInterfaceType interfaceType, UnmappableToplevelComponent... components) {
		this.defaultInterfaceId = -1;
		this.interfaceType = interfaceType;
		this.unmappableComponents = components;
		this.component = -1;
	}

	ToplevelComponent(int component, ClientInterfaceType interfaceType) {
		this(component, -1, interfaceType);
	}

	public void close(Player player) {
		player.getPacketSender().removeInterface(this);
	}

	public int getInterface(Player player) {
		if (defaultInterfaceId != -1) {
			return defaultInterfaceId;
		} else {
			return player.getToplevelType().getInterfaceId();
		}
	}

	public int getInterface(ToplevelInterfaceType mode) {
		if (defaultInterfaceId != -1) {
			return defaultInterfaceId;
		} else {
			return mode.getInterfaceId();// do we really need this actually? dont think
		}
	}

	private int getComponentUnmappable(Player player) {
		return getComponentUnmappable(player.getToplevelType());
	}

	private int getComponentUnmappable(ToplevelInterfaceType mode) {
		for (UnmappableToplevelComponent c : unmappableComponents) {
			if (c.toplevel == mode) {
				return c.childId;
			}
		}
		return -1;
	}

	public int getComponent(ToplevelInterfaceType mode) {
		if (unmappableComponents != null) {
			return getComponentUnmappable(mode);
		} else {
			return getComponentMappable(mode);
		}
	}

	public int getComponent(Player player) {
		return getComponent(player.getToplevelType());
	}

	public int getComponentMappable(ToplevelInterfaceType mode) {
		if (mode == ToplevelInterfaceType.RESIZABLE_CLASSIC) {
			// the base components are based on this toplevel, so just return the component
			// id
			return component;
		}

		if (mode.getTopLevelEnum() == -1) {
			return -1;
		}

		// all mappings use interface 161 as the key
		int mappedChild = EnumType.get(mode.getTopLevelEnum())
			.lookup(ToplevelInterfaceType.RESIZABLE_CLASSIC.getInterfaceId() << 16 | component, -1);
		if (mappedChild == -1) {
			return mappedChild;
		}
		return mappedChild & 0xFFFF;

	}

	private static UnmappableToplevelComponent uc(ToplevelInterfaceType toplevel, int childId) {
		return new UnmappableToplevelComponent(toplevel, childId);
	}

	private static class UnmappableToplevelComponent {

		private int childId;
		private ToplevelInterfaceType toplevel;

		public UnmappableToplevelComponent(ToplevelInterfaceType toplevel, int childId) {
			this.toplevel = toplevel;
			this.childId = childId;
		}
	}
}
