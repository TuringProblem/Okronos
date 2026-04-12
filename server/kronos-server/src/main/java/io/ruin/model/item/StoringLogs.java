package io.ruin.model.item;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.stat.StatType;

public class StoringLogs {

	private static void Store(Player player, Item itemOne, int result) {
		int item = itemOne.getId();
		player.LogId = item;
		player.LogAmount = result;
		player.getInventory().remove(item, result);
	}

	private static void Burn(Player player) {
		player.addEvent(event -> {
			if (player.LogId != 0 && player.LogAmount != 0) {
				if (player.LogId == 1511 || player.LogId == 1512 & player.LogAmount != 0) {
					for (int i = 0; i <= player.LogAmount; i++) {
						event.delay(3);
						player.getStats().addXp(StatType.Firemaking, 40, false);
						if (i == player.LogAmount) {
							player.LogAmount = 0;
							player.LogId = 0;
							player.sendMessage("You have finished burning your logs!");
							return;
						}
					}
				} else if (player.LogId == 1521 || player.LogId == 1522 & player.LogAmount != 0) {
					for (int i = 0; i <= player.LogAmount; i++) {
						event.delay(3);
						player.getStats().addXp(StatType.Firemaking, 60, false);
						if (i == player.LogAmount) {
							player.LogAmount = 0;
							player.LogId = 0;
							player.sendMessage("You have finished burning your logs!");
							return;
						}
					}
				} else if (player.LogId == 1519 || player.LogId == 1520 & player.LogAmount != 0) {
					for (int i = 0; i <= player.LogAmount; i++) {
						event.delay(3);
						player.getStats().addXp(StatType.Firemaking, 90, false);
						if (i == player.LogAmount) {
							player.LogAmount = 0;
							player.LogId = 0;
							player.sendMessage("You have finished burning your logs!");
							return;
						}
					}
				} else if (player.LogId == 1517 || player.LogId == 1518 & player.LogAmount != 0) {
					for (int i = 0; i <= player.LogAmount; i++) {
						event.delay(3);
						player.getStats().addXp(StatType.Firemaking, 135, false);
						if (i == player.LogAmount) {
							player.LogAmount = 0;
							player.LogId = 0;
							player.sendMessage("You have finished burning your logs!");
							return;
						}
					}
				} else if (player.LogId == 1515 || player.LogId == 1516 & player.LogAmount != 0) {
					for (int i = 0; i <= player.LogAmount; i++) {
						event.delay(3);
						player.getStats().addXp(StatType.Firemaking, 202.5, false);
						if (i == player.LogAmount) {
							player.LogAmount = 0;
							player.LogId = 0;
							player.sendMessage("You have finished burning your logs!");
							return;
						}
					}
				} else if (player.LogId == 1513 || player.LogId == 1514 & player.LogAmount != 0) {
					for (int i = 0; i <= player.LogAmount; i++) {
						event.delay(3);
						player.getStats().addXp(StatType.Firemaking, 303.8, false);
						if (i == player.LogAmount) {
							player.LogAmount = 0;
							player.LogId = 0;
							player.sendMessage("You have finished burning your logs!");
							return;
						}
					}
				} else if (player.LogId == 19669 || player.LogId == 19670 & player.LogAmount != 0) {
					for (int i = 0; i <= player.LogAmount; i++) {
						event.delay(3);
						player.getStats().addXp(StatType.Firemaking, 350, false);
						if (i == player.LogAmount) {
							player.LogAmount = 0;
							player.LogId = 0;
							player.sendMessage("You have finished burning your logs!");
							return;
						}
					}
				}
			}
		});
	}

	private static final int[] LogsId = new int[]{
		1511, 1521, 1519, 1517, 1515, 1513, 19669,
		1512, 1522, 1520, 1518, 1516, 1514, 19670
	};

	public static void register() {
		for (int logg : LogsId)
			ItemObjectAction.register(logg, 26185, ((player, item, obj) -> {
				if (player.getPosition().getRegion().id == 4904) {
					if (player.LogId == logg || player.LogId == 0) {
						int logs = player.getInventory().count(logg);
						int removeAmount = Math.min(logs, player.LogStorage) - player.LogAmount;
						if (player.LogAmount == player.LogStorage) {
							player.dialogue(new MessageDialogue("You have too many logs stored at the moment!"));
							return;
						}
						if (player.LogAmount != player.LogStorage) {
							Store(player, item, removeAmount);
							Burn(player);
						} else {
							player.sendMessage("You either have too many logs already, or you have a different log burning!");
							player.sendMessage("Wait until the the current log has finished burning!");
						}
					}
				}
				if (player.getEquipment().contains(20708) && player.getEquipment().contains(20704) && player.getEquipment().contains(20706) && player.getEquipment().contains(20710) && player.getEquipment().contains(20712)) {
					if (player.LogId == logg || player.LogId == 0) {
						int logs = player.getInventory().count(logg);
						int removeAmount = Math.min(logs, player.LogStorage) - player.LogAmount;
						if (player.LogAmount == player.LogStorage) {
							player.dialogue(new MessageDialogue("You have too many logs stored at the moment!"));
							return;
						}
						if (player.LogAmount != player.LogStorage) {
							Store(player, item, removeAmount);
							Burn(player);
						} else {
							player.sendMessage("You either have too many logs already, or you have a different log burning!");
							player.sendMessage("Wait until the the current log has finished burning!");
						}
					}
				} else {
					player.dialogue(new MessageDialogue("You need to have a full set of pyromancer & warmgloves equipped!"));
				}
			}));
	}

}
