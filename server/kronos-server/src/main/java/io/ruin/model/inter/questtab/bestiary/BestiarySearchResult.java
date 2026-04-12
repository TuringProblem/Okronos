package io.ruin.model.inter.questtab.bestiary;

import io.ruin.cache.Color;
import io.ruin.cache.NPCType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.questtab.QuestTabEntry;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;

import java.util.ArrayList;
import java.util.List;

public class BestiarySearchResult extends QuestTabEntry {

	public final int id;

	public String name;

	public BestiarySearchResult(int npcId) {
		this.id = npcId;
		this.name = NPCType.get(npcId).name.replaceAll("<col=\\w{6}>|</col>", "");
	}

	private void showInfo(Player player) {

	}

	private void showDrops(Player player) {
		NPCType def = NPCType.get(id);
		if (def.lootTable == null) {
			player.sendMessage("<img=108>" + Color.DARK_GREEN.tag() + " Bestiary: " + Color.OLIVE.tag() + name + " has no drops.");
			return;
		}
		int petId, petAverage;
		if (def.combatInfo != null && def.combatInfo.pet != null) {
			petId = def.combatInfo.pet.itemId;
			petAverage = def.combatInfo.pet.dropAverage;
		} else {
			petId = -1;
			petAverage = 0;
		}
		List<Integer[]> drops = new ArrayList<>();
		double totalTablesWeight = def.lootTable.totalWeight;
		if (def.lootTable.guaranteed != null) {
			for (LootItem item : def.lootTable.guaranteed) {
				Integer[] drop = new Integer[5];
				drop[0] = item.id;
				drop[1] = item.broadcast == null ? -1 : item.broadcast.ordinal();
				drop[2] = item.min;
				drop[3] = item.max;
				drop[4] = 1; //average 1/1
				drops.add(drop);
			}
		}
		if (def.lootTable.tables != null) {
			for (LootTable.ItemsTable table : def.lootTable.tables) {
				if (table != null) {
					double tableChance = table.weight / totalTablesWeight;
					if (table.items.length == 0) {
						//Nothing!
						//nothingPercentage = tableChance * 100D;
					} else {
						for (LootItem item : table.items) {
							Integer[] drop = new Integer[5];
							drop[0] = item.id;
							drop[1] = item.broadcast == null ? -1 : item.broadcast.ordinal();
							drop[2] = item.min;
							drop[3] = item.max;
                            /*if (player.xpMode == XpMode.HARD) {
                                if (item.weight == 0)
                                    drop[4] = (int) ((1D / tableChance) * .9);
                                else
                                    drop[4] = (int) ((1D / (tableChance * (item.weight / table.totalWeight))) * .9);
                            } else {*/
							if (item.weight == 0)
								drop[4] = (int) (1D / tableChance);
							else
								drop[4] = (int) (1D / (tableChance * (item.weight / table.totalWeight)));
							//   }
							drops.add(drop);
						}
					}
				}
			}
		}
		//todo - some how generate this string in the constructor! ^^^ :)
		player.openInterface(ToplevelComponent.MAINMODAL, 383);
		player.getPacketSender().sendDropTable(name, petId, petAverage, drops);
	}

	@Override
	public void send(Player player) {
		send(player, "<img=109> " + name, Color.BRONZE);
	}

	@Override
	public void select(Player player) {
		showInfo(player);
		showDrops(player);
	}

	public static void register() {
		InterfaceHandler.register(522, h -> {
			h.actions[17] = (SimpleAction) p -> p.closeInterface(ToplevelComponent.SIDEMODAL);
		});
	}

}
