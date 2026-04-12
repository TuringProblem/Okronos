package io.ruin.model.item.containers.bank;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.OptionAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.handlers.TabInventory;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.storage.EssencePouch;
import io.ruin.model.item.actions.impl.storage.HerbSack;
import io.ruin.model.item.actions.impl.storage.LootingBag;
import io.ruin.model.item.actions.impl.storage.RunePouch;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.var.VarPlayerRepository;

import static io.ruin.cache.ItemID.HERB_SACK;
import static io.ruin.model.entity.player.SecondaryGroup.*;
import static io.ruin.model.inter.AccessMasks.*;

public class BankInterface {

	public static void register() {
		/**
		 * Main
		 */
		InterfaceHandler.register(Interface.BANK, h -> {
			h.actions[11] = new InterfaceAction() {
				@Override
				public void handleClick(Player player, int option, int slot, int itemId) {
					player.getBank().selectTab(option, slot);
				}

				@Override
				public void handleDrag(Player player, int fromSlot, int fromItemId, int toInterfaceId, int toChildId,
						int toSlot, int toItemId) {
					if (toChildId == 11)
						player.getBank().swapTabs(fromSlot, toSlot);
				}
			};
			// h.actions[120] = (SimpleAction) p -> p.getGearPresetInterface().open(p);
			h.actions[13] = new InterfaceAction() {
				@Override
				public void handleClick(Player player, int option, int slot, int itemId) {
					var pBank = player.getBank();
					var item = pBank.get(slot, itemId);
					if (item == null) {
						return;
					}

					var hasLastX = VarPlayerRepository.BANK_LAST_X.get(player) > 0;

					// handle placeholder
					if (option == 8 || !hasLastX && option == 7) {
						var iDef = item.getDef();
						if (iDef.isPlaceholder()) {
							pBank.placeholderRelease(item);
							return;
						}
						pBank.placehold(item);
						pBank.withdraw(item, item.getAmount());
						return;
					}

					if (option > 7) {
						return;
					}

					handleWithdrawMenu(player, pBank, option, item);
				}

				@Override
				public void handleDrag(Player player, int fromSlot, int fromItemId, int toInterfaceId, int toChildId,
						int toSlot, int toItemId) {
					if (toChildId == 11) {
						player.getBank().changeTab(fromSlot, fromItemId, toSlot);
						return;
					}
					if (toChildId == 13) {
						if (toSlot >= 818) {
							player.getBank().changeTab(fromSlot, fromItemId, toSlot - 808);
						} else {
							player.getBank().rearrange(fromSlot, toSlot);
						}
						return;
					}
				}
			};
			h.actions[19] = (SimpleAction) p -> VarPlayerRepository.BANK_INSERT_MODE.set(p, 0);
			h.actions[21] = (SimpleAction) p -> VarPlayerRepository.BANK_INSERT_MODE.set(p, 1);
			h.actions[24] = (SimpleAction) p -> p.getBank().asNote = false;
			h.actions[26] = (SimpleAction) p -> p.getBank().asNote = true;
			h.actions[30] = (SimpleAction) p -> VarPlayerRepository.BANK_DEFAULT_QUANTITY.set(p, 0);
			h.actions[32] = (SimpleAction) p -> VarPlayerRepository.BANK_DEFAULT_QUANTITY.set(p, 1);
			h.actions[34] = (SimpleAction) p -> VarPlayerRepository.BANK_DEFAULT_QUANTITY.set(p, 2);
			h.actions[36] = (OptionAction) (p, option) -> {
				switch (option) {
					case 1:
						VarPlayerRepository.BANK_DEFAULT_QUANTITY.set(p, 3);
						if (VarPlayerRepository.BANK_LAST_X.get(p) <= 0) {
							p.integerInput("Enter amount:", amount -> VarPlayerRepository.BANK_LAST_X.set(p, amount));
						}
						break;
					case 2:
						p.integerInput("Enter amount:", amount -> VarPlayerRepository.BANK_LAST_X.set(p, amount));
						break;
				}
			};
			h.actions[38] = (SimpleAction) p -> VarPlayerRepository.BANK_DEFAULT_QUANTITY.set(p, 4);
			h.actions[40] = (SimpleAction) VarPlayerRepository.BANK_ALWAYS_PLACEHOLDERS::toggle;
			h.actions[44] = (SimpleAction) p -> p.getBank().deposit(p.getInventory(), true);
			h.actions[49] = (DefaultAction) (p, option, slot, itemId) -> p.getBank().incinerate(slot, itemId);
			h.actions[46] = (SimpleAction) p -> p.getBank().deposit(p.getEquipment(), true);
			h.actions[56] = (SimpleAction) VarPlayerRepository.BANK_INCINERATOR::toggle;
			h.actions[62] = (SimpleAction) VarPlayerRepository.BANK_DEPOSIT_EQUIPMENT::toggle;
			h.actions[64] = (SimpleAction) p -> p.getBank().releasePlaceholders();
			h.actions[76] = (SimpleAction) p -> p.getBank().fillBank(); // todo@@ add support for amt

			/**
			 * Equipment
			 */
			h.actions[84] = (OptionAction) (player, option) -> Bank.unequip(player, Equipment.SLOT_HAT);
			h.actions[85] = (OptionAction) (player, option) -> Bank.unequip(player, Equipment.SLOT_CAPE);
			h.actions[86] = (OptionAction) (player, option) -> Bank.unequip(player, Equipment.SLOT_AMULET);
			h.actions[87] = (OptionAction) (player, option) -> Bank.unequip(player, Equipment.SLOT_WEAPON);
			h.actions[88] = (OptionAction) (player, option) -> Bank.unequip(player, Equipment.SLOT_CHEST);
			h.actions[89] = (OptionAction) (player, option) -> Bank.unequip(player, Equipment.SLOT_SHIELD);
			h.actions[90] = (OptionAction) (player, option) -> Bank.unequip(player, Equipment.SLOT_LEGS);
			h.actions[91] = (OptionAction) (player, option) -> Bank.unequip(player, Equipment.SLOT_HANDS);
			h.actions[92] = (OptionAction) (player, option) -> Bank.unequip(player, Equipment.SLOT_FEET);
			h.actions[93] = (OptionAction) (player, option) -> Bank.unequip(player, Equipment.SLOT_RING);
			h.actions[94] = (OptionAction) (player, option) -> Bank.unequip(player, Equipment.SLOT_AMMO);

			h.closedAction = (p, i) -> {
				p.getBank().removeBlankItems();
				p.getPacketSender().sendClientScript(101, "i", 11);
				p.closeInterface(ToplevelComponent.SIDEMODAL);
				p.setInterfaceUnderlay(0, -1);
			};
		});

		/**
		 * Inventory
		 */
		InterfaceHandler.register(Interface.BANK_INVENTORY, h -> {
			h.actions[3] = new InterfaceAction() {
				@Override
				public void handleClick(Player player, int option, int slot, int itemId) {
					Item item = player.getInventory().get(slot, itemId);
					if (item == null)
						return;
					if (option == 1 && (item.getId() == LootingBag.CLOSED_LOOTING_BAG))
						return;
					if (option == 2) {
						switch (VarPlayerRepository.BANK_DEFAULT_QUANTITY.get(player)) {
							case 0:
								player.getBank().deposit(item, 1, true);
								break;
							case 1:
								player.getBank().deposit(item, 5, true);
								break;
							case 2:
								player.getBank().deposit(item, 10, true);
								break;
							case 3:
								if (VarPlayerRepository.BANK_LAST_X.get(player) > 0) {
									player.getBank().deposit(item, VarPlayerRepository.BANK_LAST_X.get(player), true);
								} else {
									player.integerInput("Enter amount:", amount -> {
										player.getBank().deposit(item, amount, true);
										VarPlayerRepository.BANK_LAST_X.set(player, amount);
									});
								}
								break;
							case 4:
								player.getBank().deposit(item, Integer.MAX_VALUE, true);
								break;
							default:
								player.getBank().deposit(item, 1, true);
								break;
						}
						return;
					}
					if (option == 3) {
						player.getBank().deposit(item, 1, true);
						return;
					}
					if (option == 4) {
						player.getBank().deposit(item, 5, true);
						return;
					}
					if (option == 5) {
						player.getBank().deposit(item, 10, true);
						return;
					}
					if (option == 6) {
						player.getBank().deposit(item, VarPlayerRepository.BANK_LAST_X.get(player), true);
						return;
					}
					if (option == 7) {
						player.integerInput("Enter amount:", amount -> {
							player.getBank().deposit(item, amount, true);
							VarPlayerRepository.BANK_LAST_X.set(player, amount);
						});
						return;
					}
					if (option == 8) {
						player.getBank().deposit(item, Integer.MAX_VALUE, true);
						return;
					}
					if (option == 9) {
						if (item.getId() == RunePouch.RUNE_POUCH)
							player.getRunePouch().empty(false);
						else if (item.getId() == HERB_SACK)
							HerbSack.emptyToBank(player);
						else if (item.getId() == ItemID.SEED_BOX)
							player.getSeedBox().emptyToBank(player);
						else if (item.getId() == 26784)
							EssencePouch.fillFromBank(player, EssencePouch.COLOSSAL_POUCH);
						else
							player.getEquipment().equip(item);
						return;
					}
					item.examine(player);
				}

				@Override
				public void handleDrag(Player player, int fromSlot, int fromItemId, int toInterfaceId, int toChildId,
						int toSlot, int toItemId) {
					TabInventory.drag(player, fromSlot, toSlot);
				}
			};

			/**
			 * Looting bag
			 */
			h.actions[8] = (SimpleAction) p -> {
				boolean messaged = false;
				for (Item item : p.getLootingBag().getItems()) {
					if (item != null) {
						messaged = item.getAmount() != p.getBank().deposit(item, item.getAmount(), !messaged);
					}
				}
			};
			h.actions[13] = (DefaultAction) (player, option, slot, itemId) -> {
				Item item = player.getLootingBag().get(slot, itemId);
				if (item == null)
					return;
				if (option == 1) {
					player.getBank().deposit(item, 1, true);
					return;
				}
				if (option == 2) {
					player.getBank().deposit(item, 5, true);
					return;
				}
				if (option == 3) {
					player.getBank().deposit(item, Integer.MAX_VALUE, true);
					return;
				}
				if (option == 4) {
					player.integerInput("Enter amount:", amount -> player.getBank().deposit(item, amount, true));
					return;
				}
				item.examine(player);
			};

			/**
			 * Equipment
			 */
			h.actions[4] = (DefaultAction) (player, option, slot, itemId) -> {
				Item item = player.getInventory().get(slot, itemId);
				if (item == null)
					return;
				if (option == 1)
					player.getEquipment().equip(item);
				else
					item.examine(player);
			};
		});
		/**
		 * Deposit box
		 */
		InterfaceHandler.register(Interface.DEPOSIT_BOX, h -> {

			h.actions[2] = (DefaultAction) (player, option, slot, itemId) -> {
				Item item = player.getInventory().get(slot, itemId);
				if (item == null)
					return;
				if (option == 2) {
					player.getBank().deposit(item, 1, true);
					return;
				}
				if (option == 3) {
					player.getBank().deposit(item, 5, true);
					return;
				}
				if (option == 4) {
					player.getBank().deposit(item, 10, true);
					return;
				}
				if (option == 5) {
					player.getBank().deposit(item, Integer.MAX_VALUE, true);
					return;
				}
			};

			h.actions[4] = (SimpleAction) p -> p.getBank().deposit(p.getInventory(), true);
			h.actions[6] = (SimpleAction) p -> p.getBank().deposit(p.getEquipment(), true);
			h.actions[8] = (SimpleAction) p -> p.getBank().deposit(p.getLootingBag(), true);

			h.closedAction = (p, i) -> {
				p.getPacketSender().sendClientScript(915, "i", 3);
				p.getPacketSender().sendClientScript(917, "ii", -1, -1);
				p.closeInterface(ToplevelComponent.SIDEMODAL);
			};
		});
	}

	private static void handleWithdrawMenu(Player player, Bank pBank, int option, BankItem item) {
		var hasLastX = VarPlayerRepository.BANK_LAST_X.get(player) > 0;
		var defaultQuantity = VarPlayerRepository.BANK_DEFAULT_QUANTITY.get(player);

		var withdraw1 = (Runnable) () -> {
			pBank.withdraw(item, 1);
		};

		var withdraw5 = (Runnable) () -> {
			pBank.withdraw(item, 5);
		};

		var withdraw10 = (Runnable) () -> {
			pBank.withdraw(item, 10);
		};

		var withdrawAllButOne = (Runnable) () -> {
			pBank.withdraw(item, item.getAmount() - 1);
		};

		var withdrawAll = (Runnable) () -> {
			pBank.withdraw(item, item.getAmount());
		};

		var withdrawXPrompt = (Runnable) () -> {
			player.integerInput("Enter amount:", amount -> {
				VarPlayerRepository.BANK_LAST_X.set(player, amount);
				pBank.withdraw(item, amount);
			});
		};

		var withdrawX = (Runnable) () -> {
			player.getBank().withdraw(item, VarPlayerRepository.BANK_LAST_X.get(player));
		};

		// The default ordering when quantity is set to 1
		var ordered = new Runnable[] {
				withdraw1,
				withdraw5,
				withdraw10,
				hasLastX ? withdrawX : withdrawXPrompt,
				hasLastX ? withdrawXPrompt : withdrawAll,
				hasLastX ? withdrawAll : withdrawAllButOne,
				withdrawAllButOne,
		};

		// Order first option based on default first quantity setting
		// As-Is on the interface clientsided
		switch (defaultQuantity) {
			// 5
			case 1 -> {
				ordered[0] = withdraw5;
				ordered[1] = withdraw1;
				ordered[2] = withdraw10;
				ordered[3] = hasLastX ? withdrawX : withdrawXPrompt;
				ordered[4] = hasLastX ? withdrawXPrompt : withdrawAll;
				ordered[5] = hasLastX ? withdrawAll : withdrawAllButOne;
				ordered[6] = withdrawAllButOne;
			}
			// 10
			case 2 -> {
				ordered[0] = withdraw10;
				ordered[1] = withdraw1;
				ordered[2] = withdraw5;
				ordered[3] = hasLastX ? withdrawX : withdrawXPrompt;
				ordered[4] = hasLastX ? withdrawXPrompt : withdrawAll;
				ordered[5] = hasLastX ? withdrawAll : withdrawAllButOne;
				ordered[6] = withdrawAllButOne;
			}
			// X
			case 3 -> {
				ordered[0] = hasLastX ? withdrawX : withdrawXPrompt;
				ordered[1] = withdraw1;
				ordered[2] = withdraw5;
				ordered[3] = withdraw10;
				ordered[4] = withdrawXPrompt;
				ordered[5] = withdrawAll;
				ordered[6] = withdrawAllButOne;
			}
			// All
			case 4 -> {
				ordered[0] = withdrawAll;
				ordered[1] = withdraw1;
				ordered[2] = withdraw5;
				ordered[3] = withdraw10;
				ordered[4] = hasLastX ? withdrawX : withdrawXPrompt;
				ordered[5] = hasLastX ? withdrawXPrompt : withdrawAllButOne;
			}
		}

		ordered[option - 1].run();
	}
}
