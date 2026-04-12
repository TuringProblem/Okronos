package io.ruin.model.inter.handlers;

import io.ruin.model.combat.RangedAmmo;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.OptionAction;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.quiver.DizanaQuiver;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.var.VarPlayerRepository;

import java.util.List;

import static io.ruin.cache.ItemID.*;

public class EquipmentStats {

	public static final int STAB_ATTACK = 0, SLASH_ATTACK = 1, CRUSH_ATTACK = 2, MAGIC_ATTACK = 3, RANGE_ATTACK = 4;

	public static final int STAB_DEFENCE = 5, SLASH_DEFENCE = 6, CRUSH_DEFENCE = 7, MAGIC_DEFENCE = 8, RANGE_DEFENCE = 9;

	public static final int MELEE_STRENGTH = 10, RANGED_STRENGTH = 11, MAGIC_DAMAGE = 12, PRAYER = 13;

	public static final int UNDEAD = 14, SLAYER = 15;

	public static final String[] STAT_NAMES = {
			"Stab Attack",
			"Slash Attack",
			"Crush Attack",
			"Magic Attack",
			"Range Attack",
			"Stab Defence",
			"Slash Defence",
			"Crush Defence",
			"Magic Defence",
			"Range Defence",
			"Melee Strength",
			"Ranged Strength",
			"Magic Damage",
			"Prayer",
			"Undead",
			"Slayer" };

	public static String getNameForIndex(int index) {
		return STAT_NAMES[index];
	}

	public static void open(Player player) {
		player.getPacketSender().sendClientScript(917, "ii", -1, -1);
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.EQUIPMENT_STATS);
		player.openInterface(ToplevelComponent.INVENTORY_TAB_AREA, Interface.EQUIPMENT_STATS_INVENTORY);
		player.getPacketSender().sendClientScript(149, "IviiiIsssss", 5570560, 93, 4, 7, 1, -1, "Equip", "", "", "", "");
		player.getPacketSender().sendIfEvents(Interface.EQUIPMENT_STATS_INVENTORY, 0, 0, 27, 1180674);
		update(player, Interface.EQUIPMENT_STATS, 24);
	}

	public enum Stat {
		STAB_ATTACK("Stab"),
		SLASH_ATTACK("Slash"),
		CRUSH_ATTACK("Crush"),
		MAGIC_ATTACK("Magic"),
		RANGE_ATTACK("Range", false, true),

		STAB_DEFEND("Stab"),
		SLASH_DEFEND("Slash"),
		CRUSH_DEFEND("Crush"),
		MAGIC_DEFEND("Magic"),
		RANGE_DEFEND("Range", false, true),

		MELEE_STRENGTH("Melee strength"),
		RANGED_STRENGTH("Ranged strength"),
		MAGIC_DAMAGE("Magic damage", true),
		PRAYER("Prayer", false, true),

		UNDEAD("Undead", true),
		SLAYER("Slayer", true);

		public final String stringName;
		public final boolean percent;
		public final boolean skipChild;

		public final String string;

		Stat(String stringName, boolean percent, boolean skipChild) {
			this.stringName = stringName;
			this.percent = percent;
			this.skipChild = skipChild;

			string = stringName + ": ";
		}

		public static final Stat[] VALUES = values();

		Stat(String stringName, boolean percent) {
			this(stringName, percent, false);
		}

		Stat(String stringName) {
			this(stringName, false);
		}

		public void sendString(Player player, int bonus, int interfaceID, int childID) {
			player.getPacketSender().sendString(interfaceID, childID, string + asBonus(bonus, percent));
		}
	}

	public static void update(Player player, int interfaceID, int startChildID) {
		int[] bonuses = player.getEquipment().bonuses;
		Stat[] stats = Stat.VALUES;
		int childID = startChildID;
		for (int i = 0; i < bonuses.length; i++) {
			int bonus = bonuses[i];
			Stat stat = stats[i];
//			if (stat == Stat.RANGED_STRENGTH)
//				bonus = DizanaQuiver.getPrimaryOrQuiverCombatBonus(player);
			stat.sendString(player, bonus, interfaceID, childID);
			childID += stat.skipChild ? 2 : 1;
		}
		player.getPacketSender().sendWeight((int) (player.getInventory().weight + player.getEquipment().weight));
	}

	public static String asBonus(int value, boolean percent) {
		String s = (value >= 0 ? "+" : "") + value;
		if (percent)
			s += "%";
		return s;
	}

	/**
	 * Handler
	 */

	public static void register() {
		InterfaceHandler.register(Interface.EQUIPMENT_STATS, h -> {
			h.actions[10] = (OptionAction) (player, option) -> TabEquipment.itemAction(player, option, Equipment.SLOT_HAT);
			h.actions[11] = (OptionAction) (player, option) -> TabEquipment.itemAction(player, option, Equipment.SLOT_CAPE);
			h.actions[12] = (OptionAction) (player, option) -> TabEquipment.itemAction(player, option, Equipment.SLOT_AMULET);
			h.actions[13] = (OptionAction) (player, option) -> TabEquipment.itemAction(player, option, Equipment.SLOT_WEAPON);
			h.actions[14] = (OptionAction) (player, option) -> TabEquipment.itemAction(player, option, Equipment.SLOT_CHEST);
			h.actions[15] = (OptionAction) (player, option) -> TabEquipment.itemAction(player, option, Equipment.SLOT_SHIELD);
			h.actions[16] = (OptionAction) (player, option) -> TabEquipment.itemAction(player, option, Equipment.SLOT_LEGS);
			h.actions[17] = (OptionAction) (player, option) -> TabEquipment.itemAction(player, option, Equipment.SLOT_HANDS);
			h.actions[18] = (OptionAction) (player, option) -> TabEquipment.itemAction(player, option, Equipment.SLOT_FEET);
			h.actions[19] = (OptionAction) (player, option) -> TabEquipment.itemAction(player, option, Equipment.SLOT_RING);
			h.actions[20] = (OptionAction) (player, option) -> TabEquipment.itemAction(player, option, Equipment.SLOT_AMMO);
			h.closedAction = (p, i) -> {
				p.closeInterface(ToplevelComponent.INVENTORY_TAB_AREA);
			};
		});
		InterfaceHandler.register(Interface.EQUIPMENT_STATS_INVENTORY, h -> {
			h.actions[0] = (DefaultAction) (player, option, slot, itemId) -> {
				Item item = player.getInventory().get(slot, itemId);
				if (item == null)
					return;
				if (option == 1)
					player.getEquipment().equip(item);
				else
					item.examine(player);
			};
		});
	}

}
