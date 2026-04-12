package io.ruin.model.inter.handlers;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.actions.SlotAction;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.skills.magic.SpellBook;
import io.ruin.model.skills.magic.spells.TargetSpell;

public class TabCombat {

	public static void register() {
		InterfaceHandler.register(Interface.COMBAT_OPTIONS, h -> {
			h.actions[5] = (SimpleAction) p -> p.getCombat().changeAttackSet(0);
			h.actions[9] = (SimpleAction) p -> p.getCombat().changeAttackSet(1);
			h.actions[13] = (SimpleAction) p -> p.getCombat().changeAttackSet(2);
			h.actions[17] = (SimpleAction) p -> p.getCombat().changeAttackSet(3);
			h.actions[22] = (SimpleAction) p -> openAutocast(p, true);
			h.actions[27] = (SimpleAction) p -> openAutocast(p, false);
			h.actions[31] = (SimpleAction) VarPlayerRepository.AUTO_RETALIATE::toggle;
			h.actions[38] = (SimpleAction) p -> p.getCombat().toggleSpecial();
		});
		InterfaceHandler.register(Interface.AUTOCAST_SELECTION, h -> {
			h.actions[1] = (SlotAction) TabCombat::selectAutocast;
		});
	}

	private static void open(Player player, int interfaceId) {//meehhhh (Todo better interface positioning system..)
		player.getPacketSender().sendInterface(interfaceId, ToplevelComponent.COMBAT_TAB_AREA);
	}

	private static void openAutocast(Player player, boolean defensive) {
		Integer autocastId = getAutocastId(player);
		if (autocastId == null) {
			player.sendMessage("Your staff can't autocast with that spellbook.");
			return;
		}
		open(player, Interface.AUTOCAST_SELECTION);
		player.getPacketSender().sendIfEvents(Interface.AUTOCAST_SELECTION, 1, 0, 52, 2);
		VarPlayerRepository.AUTOCAST_SET.set(player, autocastId);
		VarPlayerRepository.DEFENSIVE_CAST.set(player, defensive ? 1 : 0);
	}

	public static void updateAutocast(Player player, boolean login) {
		if (login) {
			int index = VarPlayerRepository.AUTOCAST.get(player);
			player.getCombat().autocastSpell = TargetSpell.AUTOCASTS[index];
		} else {
			if (player.isVisibleInterface(Interface.AUTOCAST_SELECTION))
				open(player, Interface.COMBAT_OPTIONS);
			resetAutocast(player);
		}
	}

	public static void resetAutocast(Player player) {
		if (player.getCombat().autocastSpell != null) {
			player.getCombat().autocastSpell = null;
			VarPlayerRepository.AUTOCAST.set(player, 0);
			player.getCombat().updateCombatLevel();
		}
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) == null)
			return;
		if (player.wildernessLevel < 1) {
			if (player.weaponAutocastIds.containsKey(player.getEquipment().getId(Equipment.SLOT_WEAPON))) {
				TargetSpell spell = TargetSpell.AUTOCASTS[player.weaponAutocastIds.get(player.getEquipment().getId(Equipment.SLOT_WEAPON))];
				if (SpellBook.MODERN.isActive(player) && spell.isModernSpell(spell))
					TabCombat.selectAutocast(player, player.weaponAutocastIds.get(player.getEquipment().getId(Equipment.SLOT_WEAPON)));
				else if (SpellBook.ANCIENT.isActive(player) && spell.isAncientSpell(spell) && player.getEquipment().getId(Equipment.SLOT_WEAPON) != 24423)
					TabCombat.selectAutocast(player, player.weaponAutocastIds.get(player.getEquipment().getId(Equipment.SLOT_WEAPON)));
			}
		}
	}

	public static void selectAutocast(Player player, int slot) {
		if (slot < 0 || slot >= TargetSpell.AUTOCASTS.length)
			return;
		if (slot != 0) {
			player.getCombat().autocastSpell = TargetSpell.AUTOCASTS[slot];
			VarPlayerRepository.AUTOCAST.set(player, slot);
			if (player.weaponAutocastIds.get(player.getEquipment().getId(Equipment.SLOT_WEAPON)) == null)
				player.weaponAutocastIds.put(player.getEquipment().getId(Equipment.SLOT_WEAPON), slot);
			else
				player.weaponAutocastIds.replace(player.getEquipment().getId(Equipment.SLOT_WEAPON), slot);
		}
		open(player, Interface.COMBAT_OPTIONS);
		player.getCombat().updateWeapon(true);
		player.getCombat().updateCombatLevel();
	}

	private static Integer getAutocastId(Player player) {
		Item item = player.getEquipment().get(Equipment.SLOT_WEAPON);
		int staffId = item == null ? -1 : player.getEquipment().getId(Equipment.SLOT_WEAPON);
		int amuletId = item == null ? -1 : player.getEquipment().getId(Equipment.SLOT_AMULET);
		if (staffId == -1) //shouldn't happen
			return null;
		if (staffId == 28266 || staffId == 28260 || staffId == 4675 || staffId == 27624 || staffId == 28264 || staffId == 28262) //ancient staff + sceptre + smoke sceptre + ice sceptre
			return SpellBook.ANCIENT.isActive(player) ? 4675 : null;
		if (amuletId == 12853 && staffId == 4710) //ahrims ancients
			return SpellBook.ANCIENT.isActive(player) ? 4675 : staffId;
		if (staffId == 6914 || staffId == 20560) { //master wand
			if (SpellBook.MODERN.isActive(player)) {
				return 11791;
			} else if (SpellBook.ANCIENT.isActive(player)) {
				return 4675;
			}
			return null;
		}
		if (staffId == 22647) { //Zuriel's Staff
			if (SpellBook.MODERN.isActive(player)) {
				return 11791;
			} else if (SpellBook.ANCIENT.isActive(player)) {
				return 4675;
			}
			return null;
		}
		if (staffId == 22555) { //Thammaron's Sceptre
			if (SpellBook.MODERN.isActive(player)) {
				return 11791;
			} else if (SpellBook.ANCIENT.isActive(player)) {
				return 4675;
			}
			return null;
		}
		if (staffId == 8878) { //Thammaron's Sceptre
			if (SpellBook.MODERN.isActive(player)) {
				return 11791;
			} else if (SpellBook.ANCIENT.isActive(player)) {
				return 4675;
			}
			return null;
		}
		if (staffId == 22552) { //Thammaron's Sceptre Uncharged
			if (SpellBook.MODERN.isActive(player)) {
				return 11791;
			} else if (SpellBook.ANCIENT.isActive(player)) {
				return 4675;
			}
			return null;
		}
		if (staffId == 11791 || staffId == 12904) { //staff of the dead
			boolean augmented = AttributeExtensions.hasAttribute(item, AttributeTypes.AUGMENTED);
			if (SpellBook.MODERN.isActive(player)) {
				return 11791;
			} else if (SpellBook.ANCIENT.isActive(player) && augmented) {
				return 4675;
			}
			return null;
		}
		if ((staffId == 21006 || staffId == 30181) && SpellBook.ANCIENT.isActive(player)) //kodai wand
			return 4675;
		if ((staffId == 24422 || staffId == 24425 || staffId == 24424 || staffId == 25517) && SpellBook.ANCIENT.isActive(player)) //Nightmare staff
			return 4675;
		if ((staffId == 30598) && SpellBook.ANCIENT.isActive(player)) // tainted wand
			return 4675;
		if (staffId == 1409 || staffId == 12658) //ibans staff
			return SpellBook.MODERN.isActive(player) ? 1409 : null;
		if (staffId == 4170)
			return SpellBook.MODERN.isActive(player) ? 4170 : null;
		if (staffId == 22296) //staff of light
			return SpellBook.MODERN.isActive(player) ? 22296 : null;
		return SpellBook.MODERN.isActive(player) ? -1 : null;
	}

}
