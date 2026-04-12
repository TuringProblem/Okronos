package io.ruin.model.inter.handlers;

import io.ruin.cache.NPCType;
import io.ruin.cache.ObjType;
import io.ruin.data.impl.npcs.npc_drops_new;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DropViewer {

	public static final int WIDGET_ID = 855;
	public static HashMap<Integer, Integer> npcIds = new HashMap<>();
	public static HashMap<String, NPCType> npcSearch = new HashMap<>();
	public static int npcId = 0;
	public static LootTable loot = new LootTable();

	public void open(Player player) {
		player.getPacketSender().sendString(855, 12, "Monster Drop Viewer");
		if (player.inTutorial) return;
		for (int i = 22; i < 41; i++) {
			player.getPacketSender().setHidden(WIDGET_ID, i, true);
		}
		for (int i = 51; i <= 2781; i += 10) {
			player.getPacketSender().setHidden(855, i, true);
		}
		player.getPacketSender().setHidden(855, 21, false);
		player.openInterface(ToplevelComponent.MAINMODAL, WIDGET_ID);
		player.inMonsterViewer = true;
	}

	public void setComponentToID(Player player, int componentID) {
		if (npcIds != null) {
			npcId = npcIds.get(componentID);
			loot.calculate(npcId, player);
		}
	}

	public void searchNpcs(Player player, String name) {
		for (int i = 22; i < 41; i++) {
			player.getPacketSender().setHidden(WIDGET_ID, i, true);
		}
		String search = formatForSearch(name);
		ArrayList<NPCType> npcs = new ArrayList<>();
		npcSearch.clear();
		int componentId = 22;
		if (name.length() < 3) {
			player.sendMessage("Your search must be at least 3 characters long!");
			return;
		}
		for (NPCType def : NPCType.cached.values()) {
			if (def == null || def.name == null)
				continue;
			List<npc_drops_new.DropTable> dropInformation = npc_drops_new.getDropInformationLootTable(def.id);
			if (dropInformation == null || dropInformation.isEmpty()) {
				continue;
			}
			if (def.name.toLowerCase().contains(search)) {
				if (npcSearch.containsKey(def.name))
					continue;
				npcSearch.put(def.name, def);
			}
		}
		for (NPCType defs : npcSearch.values()) {
			player.getPacketSender().setHidden(WIDGET_ID, componentId, false);
			player.getPacketSender().sendString(WIDGET_ID, componentId, "" + defs.name);
			npcIds.put(componentId, defs.id);
			if (componentId == 40)
				break;
			componentId++;
		}
	}

	private static int calculateLevenshteinDistance(String word1, String word2) {
		int[][] dp = new int[word1.length() + 1][word2.length() + 1];

		for (int i = 0; i <= word1.length(); i++) {
			for (int j = 0; j <= word2.length(); j++) {
				if (i == 0) {
					dp[i][j] = j;
				} else if (j == 0) {
					dp[i][j] = i;
				} else {
					int cost = (word1.charAt(i - 1) != word2.charAt(j - 1)) ? 1 : 0;
					dp[i][j] = Math.min(
						Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
						dp[i - 1][j - 1] + cost
					);
				}
			}
		}

		return dp[word1.length()][word2.length()];
	}


	public void searchItem(Player player, String name) {
		for (int i = 22; i < 41; i++) {
			player.getPacketSender().setHidden(WIDGET_ID, i, true);
		}

		String search = formatForSearch(name);
		ArrayList<NPCType> npcs = new ArrayList<>();
		npcSearch.clear();
		int componentId = 22;

		if (name.length() < 3) {
			player.sendMessage("Your search must be at least 3 characters long!");
			return;
		}

		ObjType closestMatch = null;

		int highestScore = Integer.MAX_VALUE;  // Initialize with a high score
		List<ObjType> items = new ArrayList<>();

		for (ObjType itemDef : ObjType.cached.values()) {
			if (itemDef == null || itemDef.name == null) {
				continue;
			}

			String itemName = itemDef.name.toLowerCase();

			if (itemName.contains(name.toLowerCase()))
				items.add(itemDef);
		}
		if (items.size() < 1) {
			player.sendMessage("No item found.");
			return;
		}
		// Create a new list to store items you want to keep
		List<ObjType> itemsToKeep = new ArrayList<>();

		for (NPCType def : NPCType.cached.values()) {
			if (def == null || def.name == null) {
				continue;
			}
			List<npc_drops_new.DropTable> dropInformation = npc_drops_new.getDropInformationLootTable(def.id);
			if (dropInformation == null || dropInformation.isEmpty()) {
				continue;
			}

			for (ObjType item : items) {
				boolean dropItem = false;
				for (npc_drops_new.DropTable drop : dropInformation) {
					if (item.id == drop.itemid) {
						dropItem = true;
						break; // Item found in a drop table, no need to check further
					}
				}

				if (dropItem) {
					itemsToKeep.add(item); // Add the item to the list of items to keep
				}
			}
		}

		items = itemsToKeep;
		if (items.size() < 1)
			return;

		closestMatch = items.get(0);

		if (closestMatch == null) {
			player.sendMessage("No item found.");
			return;
		}
		player.sendMessage("Searching for NPCs that drop the item " + closestMatch.name + ".");

		for (NPCType def : NPCType.cached.values()) {
			if (def == null || def.name == null)
				continue;
			List<npc_drops_new.DropTable> dropInformation = npc_drops_new.getDropInformationLootTable(def.id);
			if (dropInformation == null || dropInformation.isEmpty()) {
				continue;
			}
			for (npc_drops_new.DropTable drop : dropInformation) {
				if (drop.itemid == closestMatch.id) {
					if (npcSearch.containsKey(def.name))
						continue;

					npcSearch.put(def.name, def);
				}
			}
		}
		for (NPCType defs : npcSearch.values()) {
			player.getPacketSender().setHidden(WIDGET_ID, componentId, false);
			player.getPacketSender().sendString(WIDGET_ID, componentId, "" + defs.name);
			npcIds.put(componentId, defs.id);
			if (componentId == 40)
				break;
			componentId++;
		}
	}

	private String formatForSearch(String string) {
		return string.replace("'", "")
			.toLowerCase()
			.trim();
	}

	public static void register() {
		InterfaceHandler.register(WIDGET_ID, h -> {
			for (int i = 60; i < 2790; i += 10) {
				h.actions[i] = (DefaultAction) (player, option, slot, itemId) -> {
					switch (option) {
						case 10:
							player.sendMessage("" + new Item(itemId).getDef().examine);
							break;
					}
				};
			}
			h.actions[42] = (SimpleAction) (p) -> {
				if (p.inMonsterViewer) {
					p.dialogue(new OptionsDialogue(
						new Option("Search for NPC", () -> {
							p.getDropViewer().open(p);
							p.stringInput("Search monster (name):", s -> {
								p.getDropViewer().searchNpcs(p, s);
							});
						}),
						new Option("Search for item", () -> {
							p.getDropViewer().open(p);
							p.stringInput("Search item (name):", s -> {
								p.getDropViewer().searchItem(p, s);
							});
						})));
				} else {
					p.stringInput("Search box/chest (name):", s -> {
						p.getLootsViewer().searchNpcs(p, s);
					});
				}


			};
			for (int i = 22; i < 41; i++) {
				int finalI = i;
				h.actions[finalI] = (SimpleAction) (p) -> {
					if (p.inMonsterViewer)
						p.getDropViewer().setComponentToID(p, finalI);
					else {
						p.getLootsViewer().updateInterface(p, p.getLootsViewer().tableComponents.get(finalI));
					}
				};
			}
		});
	}
}
