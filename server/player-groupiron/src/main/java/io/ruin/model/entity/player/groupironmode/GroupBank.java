package io.ruin.model.entity.player.groupironmode;

import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.groupironmode.hook.Attributes;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.storage.LootingBag;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.item.containers.Inventory;
import io.ruin.model.var.VarPlayerRepository;
import lombok.experimental.ExtensionMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.ruin.model.inter.AccessMasks.*;

@ExtensionMethod(Attributes.class)
public class GroupBank {

	public static void register() {
		InterfaceHandler.register(Interface.BANK, h -> {
			h.actions[8] = (SimpleAction) p -> p.getGroupBank().open(p);
		});

		InterfaceHandler.register(1114, h -> {
			h.closedAction = (player, inter) -> {

				var pGroupIron = player.getGroupIron();
				var pGroupIronBank = player.getGroupBank();
				var pName = player.getName();

				if (!player.groupStorageSaving) {
					pGroupIronBank.returnItems(player);
				}

				if (pGroupIron != null && pGroupIron.bankOccupied && pName.equalsIgnoreCase(pGroupIron.bankOccupierName)) {
					pGroupIron.bankOccupied = false;
					pGroupIron.bankOccupierName = "";
				}
			};

			h.actions[57] = (SimpleAction) p -> p.getGroupBank().depositEquipment(p);
			h.actions[13] = (SimpleAction) p -> p.getGroupBank().closeInterface(p);
			h.actions[23] = (SimpleAction) p -> p.getGroupBank().save(p);
			h.actions[54] = (SimpleAction) p -> p.getGroupBank().depositInventory(p);
			h.actions[63] = (SimpleAction) p -> p.getGroupBank().setPlaceholder(p);
			h.actions[60] = (SimpleAction) p -> p.getGroupBank().search(p);
			h.actions[35] = (SimpleAction) p -> p.getGroupBank().toggleNotedOn(p);
			h.actions[33] = (SimpleAction) p -> p.getGroupBank().toggleNotedOff(p);
			h.actions[39] = (SimpleAction) (p) -> p.getGroupBank().setDefaultQuantity(p, 1, 39);
			h.actions[41] = (SimpleAction) (p) -> p.getGroupBank().setDefaultQuantity(p, 2, 41);
			h.actions[43] = (SimpleAction) (p) -> p.getGroupBank().setDefaultQuantity(p, 3, 43);
			h.actions[45] = (SimpleAction) (p) -> p.getGroupBank().setDefaultQuantity(p, 4, 45);
			h.actions[47] = (SimpleAction) (p) -> p.getGroupBank().setDefaultQuantity(p, 5, 47);
			for (int i = 68; i < 300; i++) {
				if (i == 164 || i == 261)
					continue;
				h.actions[i] = (DefaultAction) (p, option, slot, id) -> {
					int x = 0;
					if (VarPlayerRepository.BANK_LAST_X.get(p) > 0) {
						x = VarPlayerRepository.BANK_LAST_X.get(p);
					}
					switch (option) {
						case 1:
							switch (VarPlayerRepository.BANK_DEFAULT_QUANTITY.get(p)) {
								case 1:
									p.getGroupBank().withdraw(p, id, 5);
									break;
								case 2:
									p.getGroupBank().withdraw(p, id, 10);
									break;
								case 3:
									p.getGroupBank().withdraw(p, id, x);
									break;
								case 4:
									p.getGroupBank().withdraw(p, id, Integer.MAX_VALUE);
									break;
								case 0:
								default:
									p.getGroupBank().withdraw(p, id, 1);
									break;
							}
							break;
						case 2:
							switch (VarPlayerRepository.BANK_DEFAULT_QUANTITY.get(p)) {
								case 1:
									p.getGroupBank().withdraw(p, id, 1);
									break;
								case 2:
									p.getGroupBank().withdraw(p, id, 1);
									break;
								case 3:
									p.getGroupBank().withdraw(p, id, 1);
									break;
								case 4:
									p.getGroupBank().withdraw(p, id, 1);
									break;
								case 0:
								default:
									p.getGroupBank().withdraw(p, id, 5);
									break;
							}
							break;
						case 3:
							switch (VarPlayerRepository.BANK_DEFAULT_QUANTITY.get(p)) {
								case 1:
									p.getGroupBank().withdraw(p, id, 10);
									break;
								case 2:
									p.getGroupBank().withdraw(p, id, 5);
									break;
								case 3:
									p.getGroupBank().withdraw(p, id, 5);
									break;
								case 4:
									p.getGroupBank().withdraw(p, id, 5);
									break;
								case 0:
								default:
									p.getGroupBank().withdraw(p, id, 10);
									break;
							}
							break;
						case 4:

							switch (VarPlayerRepository.BANK_DEFAULT_QUANTITY.get(p)) {
								case 1:
									p.getGroupBank().withdraw(p, id, x);
									break;
								case 2:
									p.getGroupBank().withdraw(p, id, x);
									break;
								case 3:
									p.getGroupBank().withdraw(p, id, 10);
									break;
								case 4:
									p.getGroupBank().withdraw(p, id, 10);
									break;
								case 0:
								default:
									p.getGroupBank().withdraw(p, id, x);
									break;
							}
							break;
						case 5:
							switch (VarPlayerRepository.BANK_DEFAULT_QUANTITY.get(p)) {
								case 1:
									p.getGroupBank().withdraw(p, id, Integer.MAX_VALUE);
									break;
								case 2:
									p.getGroupBank().withdraw(p, id, Integer.MAX_VALUE);
									break;
								case 3:
									p.getGroupBank().withdraw(p, id, Integer.MAX_VALUE);
									break;
								case 4:
									p.getGroupBank().withdraw(p, id, x);
									break;
								case 0:
								default:
									p.getGroupBank().withdraw(p, id, Integer.MAX_VALUE);
									break;
							}
							break;
					}
				};
			}
		});
		InterfaceHandler.register(1116, h -> {
			h.actions[3] = new InterfaceAction() {
				@Override
				public void handleClick(Player player, int option, int slot, int itemId) {
					Item item = player.getInventory().get(slot, itemId);
					System.out.println("the option is " + option + " and the slot is " + slot + " and the item id is " + itemId);
					System.out.println("bank default quantity is " + VarPlayerRepository.BANK_DEFAULT_QUANTITY.get(player)
							+ " and bank last x is " + VarPlayerRepository.BANK_LAST_X.get(player));
					if (item == null)
						return;
					if (option == 1 && (item.getId() == LootingBag.CLOSED_LOOTING_BAG))
						return;
					if (option == 2) {
						switch (VarPlayerRepository.BANK_DEFAULT_QUANTITY.get(player)) {
							case 0:
								player.getGroupBank().deposit(player, slot, 1);
								break;
							case 1:
								player.getGroupBank().deposit(player, slot, 5);
								break;
							case 2:
								player.getGroupBank().deposit(player, slot, 10);
								break;
							case 3:
								if (VarPlayerRepository.BANK_LAST_X.get(player) > 0) {
									player.getGroupBank().deposit(player, slot, VarPlayerRepository.BANK_LAST_X.get(player));
								} else {
									player.integerInput("Enter amount:", amount -> {
										player.getGroupBank().deposit(player, slot, amount);
										VarPlayerRepository.BANK_LAST_X.set(player, amount);
									});
								}
								break;
							case 4:
								player.getGroupBank().deposit(player, slot, Integer.MAX_VALUE);
								break;
							default:
								player.getGroupBank().deposit(player, slot, 1);
								break;
						}
						return;
					}
					if (option == 3) {
						player.getGroupBank().deposit(player, slot, 1);
						return;
					}
					if (option == 4) {
						player.getGroupBank().deposit(player, slot, 5);
						return;
					}
					if (option == 5) {
						player.getGroupBank().deposit(player, slot, 10);
						return;
					}
					if (option == 6) {
						player.getGroupBank().deposit(player, slot, VarPlayerRepository.BANK_LAST_X.get(player));
						return;
					}
					if (option == 7) {
						player.integerInput("Enter amount:", amount -> {
							player.getGroupBank().deposit(player, slot, amount);
							VarPlayerRepository.BANK_LAST_X.set(player, amount);
						});
						return;
					}
					if (option == 8) {
						player.getGroupBank().deposit(player, slot, Integer.MAX_VALUE);
						return;
					}
					if (option == 9) {
						player.getEquipment().equip(item);
						return;
					}
					item.examine(player);
				}
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

		InterfaceHandler.register(877, h -> {
			h.actions[8] = (SimpleAction) p -> VarPlayerRepository.RAIDS_STORAGE_WARNING_DISMISSED.set(p, 1);
			h.actions[1] = (DefaultAction) (p, option, slot, id) -> {
				if (VarPlayerRepository.RAIDS_STORAGE_PRIVATE_INVENTORY.get(p) == 1) { // storing into shared storage
					switch (option) {
						case 1:
							p.getGroupBank().deposit(p, slot, 1);
							break;
						case 2:
							p.getGroupBank().deposit(p, slot, 5);
							break;
						case 3:
							p.getGroupBank().deposit(p, slot, 10);
							break;
						case 4:
							p.getGroupBank().deposit(p, slot, Integer.MAX_VALUE);
							break;
						case 5:
							p.integerInput("Enter amount:", amt -> p.getGroupBank().deposit(p, slot, amt));
							break;
					}
				}
			};
		});
	}

	String search = null;

	List<Item> storageItems = new ArrayList<>();
	boolean noted = false;
	int valueOne = 1;
	int valueTwo = 1;
	int valueThree = 1;
	int valueFour = 1;
	int valueFive = 1;
	boolean saveWarning = false;
	boolean disabled = true;

	public void save(Player p) {
		throw new IllegalStateException("unimplemented");
		// p.groupStorageSaving = true;
		// p.closeInterface(ToplevelComponent.MAINMODAL);
		// p.getGroupIron().setBankOccupied(false);
		// p.getGroupIron().setBankOccupierName(null);
		// HashMap<Integer, Integer> groupItems = new HashMap<>();
		// for (Item item : storageItems) {
		// 	groupItems.put(item.getId(), item.getAmount());
		// }
		// removeFakeContainer(p, true);
		// GroupIron.saveGroupItems(p.getGroupIron().getGroupName(), groupItems);
		// World.startEvent(e -> {
		// 	p.lock();
		// 	p.openInterface(ToplevelComponent.MAINMODAL, 1115);
		// 	p.getPacketSender().sendString(1115, 14, "Saving...");
		// 	e.delay(2);
		// 	p.unlock();
		// 	p.getBank().open();
		// });
		// storageItems.clear();
	}

	public void closeInterface(Player player) {
		if (!saveWarning) {
			saveWarning = true;
			player.sendMessage(
					Color.RED.wrap("Warning:") + " Please make sure to save your progress before closing the interface.");
			return;
		}
		player.closeInterface(ToplevelComponent.MAINMODAL);
		player.closeInterface(ToplevelComponent.SIDEMODAL);
	}

	public void returnItems(Player player) {
		removeFakeContainer(player, false);
	}

	public void open(Player player) {
		if (disabled) {
			player.sendMessage("This feature is currently disabled.");
			return;
		}
		if (player.getGroupIron().bankOccupied && World.getPlayer(player.getGroupIron().bankOccupierName) != null) {
			player.sendMessage("The storage is currently being occupied by " + player.getGroupIron().bankOccupierName + ".");
			return;
		}
		player.getGroupIron().bankOccupied = true;
		player.getGroupIron().bankOccupierName = player.getName();
		player.getGroupIron().setBankOccupied(true);
		player.getGroupIron().setBankOccupierName(player.getName());

		setFakeContainers(player);

		storageItems.clear();
		World.startEvent(e -> {
			player.lock();
			player.openInterface(ToplevelComponent.MAINMODAL, 1115);
			player.getPacketSender().sendString(1115, 14, "Loading...");
			e.delay(2);
			HashMap<Integer, Integer> groupItems = new HashMap<>();
			e.delay(2);
			for (Map.Entry<Integer, Integer> entry : groupItems.entrySet()) {
				storageItems.add(new Item(entry.getKey(), entry.getValue()));
			}

			player.getPacketSender().sendString(1114, 15, NumberUtils.formatNumber(storageItems.size()));
			int interfaceComponentId = -1;
			switch (VarPlayerRepository.BANK_DEFAULT_QUANTITY.get(player)) {
				case 1:
					interfaceComponentId = 41;
					break;
				case 2:
					interfaceComponentId = 43;
					break;
				case 3:
					interfaceComponentId = 45;
					break;
				case 4:
					interfaceComponentId = 47;
					break;
				case 0:
				default:
					interfaceComponentId = 39;
					break;
			}
			for (int i = 39; i < 47; i += 2) {
				int hash = 1114 << 16 | i;
				player.getPacketSender().sendClientScript(10628, "I", hash);
			}
			player.getPacketSender().sendClientScript(10629, "I", 1114 << 16 | interfaceComponentId);
			player.getPacketSender().sendClientScript(10624, "I", 1114 << 16 | 33);
			player.getPacketSender().sendClientScript(10625, "I", 1114 << 16 | 35);
			player.openInterface(ToplevelComponent.MAINMODAL, 1114);
			player.getPacketSender().sendIfEvents(1116, 3, 0, 27, ClickOp1, ClickOp2, ClickOp3, ClickOp4, ClickOp5, ClickOp6,
					ClickOp7, ClickOp8, ClickOp9, ClickOp10, DragDepth1, DragTargetable);
			player.getPacketSender().sendIfEvents(1116, 19, 0, 27, ClickOp1, ClickOp2, ClickOp3, ClickOp4, ClickOp10);
			player.getPacketSender().sendIfEvents(1116, 13, 0, 27, ClickOp1, ClickOp2, ClickOp3, ClickOp4, ClickOp1);
			player.getPacketSender().sendIfEvents(1116, 4, 0, 27, ClickOp1, ClickOp10, DragDepth1, DragTargetable);
			player.openInterface(ToplevelComponent.SIDEMODAL, 1116);
			sendItems(player, storageItems);
			player.unlock();
			saveWarning = false;
			player.groupStorageSaving = false;
		});
	}

	void removeFakeContainer(Player player, boolean save) {
		if (save) {
			replaceContainers(player);
		}
		if (player.temporaryInventory != null) {
			player.temporaryInventory.clear();
			player.temporaryInventory = null;
		}
		if (player.temporaryEquipment != null) {
			player.temporaryEquipment.clear();
			player.temporaryEquipment = null;
		}
		player.equipment.update(Equipment.SLOT_WEAPON);
		player.equipment.sendAll = true;
		player.inventory.sendAll = true;
	}

	private void withdraw(Player p, int id, int amount) {
		int index = 0;
		for (Item item : storageItems) {
			if (item.getId() == id) {
				index = storageItems.indexOf(item);
				break;
			}
			index++;
		}

		if (storageItems.get(index).getAmount() < 1) {
			p.sendMessage("Your storage no longer contains this item!");
			return;
		}

		if (amount > storageItems.get(index).getAmount()) {
			amount = storageItems.get(index).getAmount();
		}
		int amountInStorage = storageItems.get(index).getAmount();
		int inventorySpace = p.getInventory().getFreeSlots();
		int amountToWithdraw = amount;
		boolean canBeNoted = new Item(id).getDef().notedId != -1;
		if (!canBeNoted && !new Item(id).getDef().stackable)
			amountToWithdraw = Math.min(amountToWithdraw, inventorySpace);

		if (!new Item(id).getDef().stackable && !noted && amountToWithdraw > inventorySpace)
			amountToWithdraw = inventorySpace;
		if (amountToWithdraw > amountInStorage)
			amountToWithdraw = amountInStorage;
		if (amountToWithdraw < 1) {
			p.sendMessage("You don't have enough inventory space to withdraw that many.");
			return;
		}
		if (noted) {
			p.getInventory().add(new Item(storageItems.get(index).getDef().notedId, amountToWithdraw));
		} else {
			p.getInventory().add(new Item(storageItems.get(index).getId(), amountToWithdraw));
		}
		if (amountToWithdraw >= amountInStorage) {
			storageItems.remove(index);
		} else {
			storageItems.get(index).setAmount(storageItems.get(index).getAmount() - amountToWithdraw);
		}

		if (search == null) {
			sendItems(p, storageItems);
		} else {
			sendSearch(p, search);
		}
		p.getPacketSender().sendString(1114, 15, NumberUtils.formatNumber(storageItems.size()));
	}

	private void search(Player player) {
		if (search != null) {
			search = null;
			player.closeDialogue();
			sendItems(player, storageItems);
			int interfaceHash = 1114 << 16 | 60;
			player.getPacketSender().sendClientScript(10628, "I", interfaceHash);
			return;
		}
		search = "";
		int interfaceHash = 1114 << 16 | 60;
		player.getPacketSender().sendClientScript(10629, "I", interfaceHash);
		player.stringInput("Enter item name:", s -> {
			sendSearch(player, s);
		});
	}

	private List<Item> getStorageItemsBySearch(String search) {
		List<Item> items = new ArrayList<>();
		for (Item item : storageItems) {
			if (item.getDef().name.toLowerCase().contains(search.toLowerCase())) {
				items.add(item);
			}
		}
		return items;
	}

	private void sendSearch(Player player, String search) {
		this.search = search;
		sendItems(player, getStorageItemsBySearch(search));
	}

	private void toggleNotedOn(Player p) {
		if (!noted) {
			noted = true;
			p.sendMessage("Noted items will now be withdrawn.");
			int interfaceHash2 = 1114 << 16 | 35;
			p.getPacketSender().sendClientScript(10624, "I", interfaceHash2);
			int interfaceHash3 = 1114 << 16 | 33;
			p.getPacketSender().sendClientScript(10625, "I", interfaceHash3);
		}
	}

	private void toggleNotedOff(Player p) {
		if (noted) {
			noted = false;
			p.sendMessage("Items will no longer be withdrawn in note form.");
			int interfaceHash2 = 1114 << 16 | 33;
			p.getPacketSender().sendClientScript(10624, "I", interfaceHash2);
			int interfaceHash3 = 1114 << 16 | 35;
			p.getPacketSender().sendClientScript(10625, "I", interfaceHash3);
		}
	}

	private void setDefaultQuantity(Player p, int option, int componentId) {
		for (int i = 39; i < 47; i += 2) {
			int hash = 1114 << 16 | i;
			p.getPacketSender().sendClientScript(10627, "I", hash);
		}
		int interfaceHash = 1114 << 16 | componentId;
		System.out.println("component id is " + componentId + " and interface hash is " + interfaceHash);
		p.getPacketSender().sendClientScript(10626, "I", interfaceHash);
		switch (option) {
			case 1:
				VarPlayerRepository.BANK_DEFAULT_QUANTITY.set(p, 0);
				break;
			case 2:
				VarPlayerRepository.BANK_DEFAULT_QUANTITY.set(p, 1);
				break;
			case 3:
				VarPlayerRepository.BANK_DEFAULT_QUANTITY.set(p, 2);
				break;
			case 4:
				VarPlayerRepository.BANK_DEFAULT_QUANTITY.set(p, 3);
				if (VarPlayerRepository.BANK_LAST_X.get(p) <= 0) {
					p.integerInput("Enter amount:", amount -> VarPlayerRepository.BANK_LAST_X.set(p, amount));
				}
				break;
			case 5:
				VarPlayerRepository.BANK_DEFAULT_QUANTITY.set(p, 4);
				break;
		}
	}

	private void depositInventory(Player p) {
		for (int i = 0; i < p.getInventory().getItems().length; i++) {
			if (p.getInventory().get(i) != null)
				deposit(p, i, p.getInventory().get(i).getAmount());
		}
	}

	private void sendItems(Player player, List<Item> itemsToSend) {
		int startingComponent = 68;
		int startContainerId = 1239;
		System.out.println("items to send size " + itemsToSend.size());
		for (int i = 68; i < 300; i++) {
			if (i == 164 || i == 261)
				continue;
			player.getPacketSender().setHidden(1114, startingComponent, true);
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					1114 << 16 | startingComponent, startContainerId,
					4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendIfEvents(1114, startingComponent, 0, 27, 1086);
			player.getPacketSender().sendItems(
					-1,
					startingComponent,
					startContainerId,
					new Item(-1));
			startingComponent++;
			startContainerId++;
		}
		startingComponent = 68;
		startContainerId = 1239;

		for (Item item : itemsToSend) {
			System.out.println("item sent is " + item.getId() + " with amount " + item.getAmount());
			player.getPacketSender().setHidden(1105, startingComponent, false);
			int x = 0;
			if (VarPlayerRepository.BANK_LAST_X.get(player) > 0) {
				x = VarPlayerRepository.BANK_LAST_X.get(player);
			}
			String firstLine;
			String secondLine;
			String thirdLine;
			String fourthLine;
			String fifthLine;

			switch (VarPlayerRepository.BANK_DEFAULT_QUANTITY.get(player)) {
				case 1:
					firstLine = "Withdraw-5";
					valueOne = 5;
					secondLine = "Withdraw-1";
					valueTwo = 1;
					thirdLine = "Withdraw-10";
					valueThree = 10;
					fourthLine = "Withdraw-" + x;
					valueFour = x;
					fifthLine = "Withdraw-All";
					valueFive = item.getAmount();
					break;
				case 2:
					firstLine = "Withdraw-10";
					valueOne = 10;
					secondLine = "Withdraw-1";
					valueTwo = 1;
					thirdLine = "Withdraw-5";
					valueThree = 5;
					fourthLine = "Withdraw-" + x;
					valueFour = x;
					fifthLine = "Withdraw-All";
					valueFive = item.getAmount();
					break;
				case 3:
					firstLine = "Withdraw-" + x;
					valueOne = x;
					secondLine = "Withdraw-1";
					valueTwo = 1;
					thirdLine = "Withdraw-5";
					valueThree = 5;
					fourthLine = "Withdraw-10";
					valueFour = 10;
					fifthLine = "Withdraw-All";
					valueFive = item.getAmount();
					break;
				case 4:
					firstLine = "Withdraw-All";
					valueOne = item.getAmount();
					secondLine = "Withdraw-1";
					valueTwo = 1;
					thirdLine = "Withdraw-5";
					valueThree = 5;
					fourthLine = "Withdraw-10";
					valueFour = 10;
					fifthLine = "Withdraw-" + x;
					valueFive = x;
					break;
				case 0:
				default:
					firstLine = "Withdraw-1";
					valueOne = 1;
					secondLine = "Withdraw-5";
					valueTwo = 5;
					thirdLine = "Withdraw-10";
					valueThree = 10;
					fourthLine = "Withdraw-" + x;
					valueFour = x;
					fifthLine = "Withdraw-All";
					valueFive = item.getAmount();
					break;
			}

			player.getPacketSender().setHidden(1114, startingComponent, false);
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					1114 << 16 | startingComponent, startContainerId,
					4, 7, 1, -1, firstLine, secondLine, thirdLine, fourthLine, fifthLine);
			player.getPacketSender().sendIfEvents(1114, startingComponent, 0, 27, 1086);
			player.getPacketSender().sendItems(
					-1,
					startingComponent,
					startContainerId,
					item);
			if (startingComponent == 163 || startingComponent == 260)
				startingComponent++;
			startingComponent++;
			startContainerId++;
		}
	}

	// TODO: check if storage has space for each item, continue if doesn't, do same
	// for inv, need to check if storage has space of depositing too, and also need
	// to make sure item gets remove from db if amount is 0
	private void depositEquipment(Player p) {
		for (int i = 0; i < p.getEquipment().getItems().length; i++) {
			if (p.getEquipment().get(i) != null)
				deposit(p, i, p.getEquipment().get(i).getAmount());
		}
	}

	private void setPlaceholder(Player p) {
		p.sendMessage("Currently disabled.");
	}

	private void deposit(Player p, int slot, int amount) {
		if (storageItems.size() >= 150) {
			p.sendMessage("The storage is full.");
			return;
		}
		Item item = p.getInventory().get(slot);
		if (item == null)
			return;

		if (!item.getDef().tradeable) {
			p.sendMessage("You may only place tradeable items into the group storage.");
			return;
		}

		if (item.getAttributeHash() != 0) {
			p.sendMessage("You can't deposit this item.");
			return;
		}
		if (amount > 1 && (item.getDef().isNote()) || item.getDef().stackable) {
			if (amount > item.getAmount())
				amount = item.getAmount();

			if (item.getDef().isNote()) {
				p.getInventory().remove(item.getId(), amount);
				item = new Item(item.getDef().notedId, amount);
			} else {
				p.getInventory().remove(item.getId(), amount);
			}
			int index = -1;
			for (Item storageItem : storageItems) {
				if (storageItem.getId() == item.getId()) {
					index = storageItems.indexOf(storageItem);
					break;
				}
			}
			if (index == -1)
				storageItems.add(new Item(item.getId(), amount));
			else {
				storageItems.get(index).incrementAmount(amount);
			}
			if (search == null) {
				sendItems(p, storageItems);
			} else {
				sendSearch(p, search);
			}
		} else {
			int amountStored = 0;
			for (int i = 0; i < p.getInventory().getItems().length; i++) {
				Item invItem = p.getInventory().get(i);
				if (invItem == null || invItem.getId() != item.getId())
					continue;
				int index = -1;
				for (Item storageItem : storageItems) {
					if (storageItem.getId() == item.getId()) {
						index = storageItems.indexOf(storageItem);
						break;
					}
				}
				if (index == -1)
					storageItems.add(new Item(item.getId(), invItem.getAmount()));
				else {
					storageItems.get(index).incrementAmount(invItem.getAmount());
				}
				invItem.remove();
				if (search == null) {
					sendItems(p, storageItems);
				} else {
					sendSearch(p, search);
				}
				if (amountStored++ >= amount)
					break;
			}
		}
		p.getPacketSender().sendString(1114, 15, NumberUtils.formatNumber(storageItems.size()));
	}

	private void setFakeContainers(Player player) {
		player.temporaryInventory = new Inventory();
		player.temporaryEquipment = new Equipment();
		player.temporaryEquipment.init(player, 14, -1, 64208, 94, false);
		player.temporaryInventory.init(player, 28, -1, 0, 93, false);
		player.getEquipment().update(Equipment.SLOT_WEAPON);
		player.equipment.sendAll = true;
		player.temporaryInventory.sendAll = true;

		for (int i = 0; i < player.inventory.getItems().length; i++) {
			Item item = player.inventory.get(i) == null ? null : player.inventory.get(i).copy();
			player.temporaryInventory.set(i, item);
		}
		for (int i = 0; i < player.equipment.getItems().length; i++) {
			Item item = player.equipment.get(i) == null ? null : player.equipment.get(i).copy();
			player.temporaryEquipment.set(i, item);
		}
	}

	private void replaceContainers(Player player) {
		player.equipment.clear();
		player.inventory.clear();
		for (int i = 0; i < player.temporaryEquipment.getItems().length; i++) {
			Item item = player.temporaryEquipment.get(i) == null ? null : player.temporaryEquipment.get(i).copy();
			player.equipment.set(i, item);
		}
		for (int i = 0; i < player.temporaryInventory.getItems().length; i++) {
			Item item = player.temporaryInventory.get(i) == null ? null : player.temporaryInventory.get(i).copy();
			player.inventory.set(i, item);
		}
	}
}
