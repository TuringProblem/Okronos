package io.ruin.model.skills.construction.actions;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.OptionAction;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.item.Item;

import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.skills.construction.Buildable;
import io.ruin.model.skills.construction.Construction;
import io.ruin.model.skills.magic.rune.RuneRemoval;
import io.ruin.model.skills.magic.spells.modern.*;
import io.ruin.model.stat.StatType;

import static io.ruin.model.skills.construction.Buildable.*;

public class Study {

	enum Lectern {
		OAK(OAK_LECTERN, 0, 0),

		OAK_EAGLE(EAGLE_LECTERN, 1, 0),
		TEAK_EAGLE(TEAK_EAGLE_LECTERN, 2, 0),
		MAHOGANY_EAGLE(MAHOGANY_EAGLE_LECTERN, 3, 0),

		OAK_DEMON(DEMON_LECTERN, 0, 1),
		TEAK_DEMON(TEAK_DEMON_LECTERN, 0, 2),
		MAHOGANY_DEMON(MAHOGANY_DEMON_LECTERN, 0, 3);

		Buildable buildable;
		int eagleSetting, demonSetting;

		Lectern(Buildable buildable, int eagleSetting, int demonSetting) {
			this.buildable = buildable;
			this.eagleSetting = eagleSetting;
			this.demonSetting = demonSetting;
		}

		public void open(Player player) {
			player.startEvent(event -> {
				player.animate(Construction.READ_LECTERN);
				event.delay(1);
				VarPlayerRepository.LECTERN_EAGLE.set(player, eagleSetting);
				VarPlayerRepository.LECTERN_DEMON.set(player, demonSetting);
				player.openInterface(ToplevelComponent.MAINMODAL, Interface.TABLET_MAKING);
			});
		}
	}

	public static void register() {
		for (Lectern l : Lectern.values()) {
			ObjectAction.register(l.buildable.getBuiltObjects()[0], "study", (player, obj) -> l.open(player));
		}

		InterfaceHandler.register(Interface.TABLET_MAKING, h -> {
			for (Tablet tab : Tablet.VALUES) {
				h.actions[tab.childId] = (OptionAction) (p, option) -> {
					switch (option) {
						case 1:
							tab.make(p, 1);
							break;
						case 2:
							tab.make(p, 5);
							break;
						case 3:
							tab.make(p, p.getInventory().getAmount(1761));
							break;
						case 4:
							p.integerInput("Enter amount:", amt -> tab.make(p, amt));
							break;
					}
				};
			}
		});
	}

	public enum Tablet {
		ENCHANT_SAPPHIRE(3, 8016, 0, 0, JewelleryEnchant.EnchantLevel.ONE),
		ENCHANT_EMERALD(4, 8017, 0, 1, JewelleryEnchant.EnchantLevel.TWO),
		ENCHANT_RUBY(6, 8018, 0, 2, JewelleryEnchant.EnchantLevel.THREE),
		ENCHANT_DIAMOND(7, 8019, 0, 2, JewelleryEnchant.EnchantLevel.FOUR),
		ENCHANT_DRAGONSTONE(8, 8020, 0, 3, JewelleryEnchant.EnchantLevel.FIVE),
		ENCHANT_ONYX(9, 8021, 0, 3, JewelleryEnchant.EnchantLevel.SIX),

		HOUSE_TELEPORT(17, 8013, TeleportToHouse.LVL_REQ, TeleportToHouse.XP, 3, 0, TeleportToHouse.RUNES),

		BONES_TO_BANANAS(5, 8014, BonesBananas.LVL_REQ, BonesBananas.XP, 0, 1, BonesBananas.RUNES),
		BONES_TO_PEACHES(10, 8015, BonesPeaches.LVL_REQ, BonesPeaches.XP, 0, 3, BonesPeaches.RUNES);

		int childId;

		int tabId;

		int magicLevel;
		int eagleLevel, demonLevel;
		double xp;
		Item[] runes;

		Tablet(int childId, int tabId, int eagleLevel, int demonLevel, JewelleryEnchant.EnchantLevel enchant) {
			this(childId, tabId, enchant.levelReq, enchant.exp, eagleLevel, demonLevel, enchant.runes);
		}

		public static final Tablet[] VALUES = values();

		Tablet(int childId, int tabId, int eagleLevel, int demonLevel, ModernTeleport tele) {
			this(childId, tabId, tele.getLvlReq(), tele.getXp(), eagleLevel, demonLevel, tele.getRunes());
		}

		Tablet(int childId, int tabId, int magicLevel, double xp, int eagleLevel, int demonLevel, Item... runes) {
			this.childId = childId;
			this.tabId = tabId;
			this.magicLevel = magicLevel;
			this.xp = xp;
			this.eagleLevel = eagleLevel;
			this.demonLevel = demonLevel;
			this.runes = runes;
		}

		public void make(Player player, int amount) {
			player.startEvent(event -> {
				int left = amount;
				player.closeInterfaces();
				while (left > 0) {
					if (!player.getStats().check(StatType.Magic, magicLevel, "create this tablet")) {
						return;
					}
					if (!player.getInventory().contains(1761, 1)) {
						player.sendMessage("A piece of soft clay is required to create a tablet.");
						return;
					}
					if (VarPlayerRepository.LECTERN_DEMON.get(player) < demonLevel
						|| VarPlayerRepository.LECTERN_EAGLE.get(player) < eagleLevel) {
						player.sendMessage("Your lectern cannot create that type of tablet.");
						return;
					}
					RuneRemoval r = RuneRemoval.get(player, runes);
					if (r == null) {
						player.sendMessage("You don't have the required runes to create that tablet.");
						return;
					}
					player.animate(Construction.MAKE_TABLET);
					player.getStats().addXp(StatType.Magic, xp, true);
					player.getInventory().remove(1761, 1);
					r.remove();
					player.getInventory().add(tabId, 1);
					left--;
					event.delay(7);
				}
			});
		}
	}


}
