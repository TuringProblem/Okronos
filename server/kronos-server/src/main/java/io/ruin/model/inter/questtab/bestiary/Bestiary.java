package io.ruin.model.inter.questtab.bestiary;

import io.ruin.Server;
import io.ruin.api.utils.StringUtils;
import io.ruin.cache.Color;
import io.ruin.cache.NPCType;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.actions.SlotAction;
import io.ruin.model.item.actions.impl.WintertodtCrate;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;

import java.util.*;

public class Bestiary {

	private static final HashMap<Integer, LinkedHashSet<NPCType>> drops = new HashMap<>();

	public static void open(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.DROP_TABLES);
		player.dropTableSearchResults = new ArrayList<>(Arrays.asList(
			new DropTableEntry(5886), new DropTableEntry(5862), new DropTableEntry(319),
			new DropTableEntry(7144), new DropTableEntry(2215), new DropTableEntry(5779),
			new DropTableEntry(963), new DropTableEntry(3162), new DropTableEntry(3129),
			new DropTableEntry(6766), new DropTableEntry(8061), new DropTableEntry(2042)
		));
		sendEntries(player);
	}


	public static void openBoxLoot(Player player) {
		openBoxLoot(player, -1);
	}

	public static void openBoxLoot(Player player, int slot) {
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.DROP_TABLES);
		sendBoxes(player);

		if (slot != -1) {
			clickEntry(player, slot);
		}
	}

	public static void search(Player player, String name, boolean monster) {
		String imgTag;
		if (monster)
			player.sendMessage((imgTag = "<img=108>") + Color.DARK_GREEN.tag() + " Bestiary: " + Color.OLIVE.tag() + "Searching for monster \"" + name + "\"...");
		else
			player.sendMessage((imgTag = "<img=33>") + Color.DARK_GREEN.tag() + " Bestiary: " + Color.OLIVE.tag() + "Searching for monsters that drop \"" + name + "\"...");
		Server.executeAsync(() -> {
			List<DropTableEntry> results = new ArrayList<>();
			String search = formatForSearch(name);
			if (!search.isEmpty()) {
				LinkedHashMap<String, TreeMap<Integer, List<NPCType>>> map = new LinkedHashMap<>();
				if (monster) {
					NPCType.forEach(NPCType -> {
						String searchName = formatForSearch(NPCType.name);
						if (!searchName.contains(search))
							return;
						map.computeIfAbsent(searchName, k -> new TreeMap<>()).computeIfAbsent(NPCType.combatLevel, k -> new ArrayList<>()).add(NPCType);
					});
				} else {
					ObjType.forEach(itemDef -> {
						if (!formatForSearch(itemDef.name).contains(search))
							return;
						HashSet<NPCType> NPCTypes = drops.get(itemDef.id);
						if (NPCTypes == null)
							return;
						NPCTypes.forEach(NPCType -> map.computeIfAbsent(formatForSearch(NPCType.name), k -> new TreeMap<>()).computeIfAbsent(NPCType.combatLevel, k -> new ArrayList<>()).add(NPCType));
					});
				}
				map.values().forEach(levelsMap -> {
					List<NPCType> matched = new ArrayList<>();
					levelsMap.values().forEach(defs -> {
						defs.sort(new Comparator<NPCType>() {
							@Override
							public int compare(NPCType d1, NPCType d2) {
								return Integer.compare(priority(d2), priority(d1));
							}

							private int priority(NPCType def) {
								int priority = 0;
								if (def.combatInfo != null)
									priority++;
								if (def.lootTable != null)
									priority++;
								if (def.combatLevel > 0)
									priority++;
								if (def.hasOption("attack"))
									priority++;
								return priority;
							}
						});
						NPCType def = defs.get(0);
						if (def.combatInfo != null || def.lootTable != null)
							matched.add(def);
						//^ only match the highest priority npc with this combat level.
					});
					for (NPCType def : matched) {
						DropTableEntry result = new DropTableEntry(def.id);

						if (result.name.startsWith("Vorka") && def.combatLevel == 0) {
							continue;
						}

						if (matched.size() > 1) //multiple same levels
							result.name += " (" + def.combatLevel + ")";
						result.name = result.name.replace("Greater Skeleton Hellhound", "Grtr. Skeleton Hellhound");
						results.add(result);
					}
				});
				if (results.size() > 30) {
					results.clear();
					player.sendMessage("Too many results..");
					return;
				}
			}

			Server.worker.execute(() -> {
				int found = results.size(); //minus two because of the search entries
				if (found == 0) {
					player.dropTableSearchResults = null;
					player.sendMessage(imgTag + Color.DARK_GREEN.tag() + " Bestiary: " + Color.OLIVE.tag() + "No results found.");
				} else {
					player.dropTableSearchResults = results;
					if (found == 1)
						player.sendMessage(imgTag + Color.DARK_GREEN.tag() + " Bestiary: " + Color.OLIVE.tag() + "1 result found.");
					else
						player.sendMessage(imgTag + Color.DARK_GREEN.tag() + " Bestiary: " + Color.OLIVE.tag() + found + " results found.");
					sendEntries(player);
				}
			});
		});
	}

	private static void sendEntries(Player player) {
		List<DropTableEntry> results = player.dropTableSearchResults;

		String entries = buildEntries(results);

		player.getPacketSender().sendIfEvents(847, 16, 0, results.size(), 2);
		player.getPacketSender().sendClientScript(10191, "is", results.size(), entries);
	}

	private static void sendBoxes(Player player) {
		List<DropTableEntry> results = player.dropTableSearchResults = new ArrayList<>(Arrays.asList(
//                new DropTableEntry("Mystery Box", MysteryBox.MYSTERY_BOX_TABLE),
//                new DropTableEntry("Vote Mystery Box", VoteMysteryBox.ECO_VOTING_BOX_TABLE),
//                new DropTableEntry("Super Mystery Box", SuperMysteryBox.ECO_MYSTERY_BOX_TABLE),
//                new DropTableEntry("Legendary Mystery Box", LegendaryMysteryBox.summerMboxLoot),
//                new DropTableEntry("Infernal Mystery Box", InfernalMysteryBox.table),
			new DropTableEntry("Wintertodt Crate", WintertodtCrate.table)
		));

		String entries = buildEntries(results);

		player.getPacketSender().sendIfEvents(847, 16, 0, results.size(), 2);
		player.getPacketSender().sendClientScript(10191, "is", results.size(), entries);
	}

	private static void clickEntry(Player player, int slot) {
		List<DropTableEntry> results = player.dropTableSearchResults;

		DropTableEntry result = results.get(slot);

		if (result.table != null) {
			displayDrops(player, result.name, result.table);
		} else {
			displayDrops(player, result.id, result.name);
		}
	}

	private static void displayDrops(Player player, String name, LootTable lootTable) {
		List<DropTableResult> drops = new ArrayList<>();
		double totalTablesWeight = lootTable.totalWeight;
		if (lootTable.guaranteed != null) {
			for (LootItem item : lootTable.guaranteed) {
				drops.add(new DropTableResult(item.id, item.min, item.max, 1, -1));
			}
		}
		if (lootTable.tables != null) {
			for (LootTable.ItemsTable table : lootTable.tables) {
				if (table != null) {
					double tableChance = table.weight / totalTablesWeight;
					if (table.items.length == 0) {
						//Nothing!
						//nothingPercentage = tableChance * 100D;
					} else {
						for (LootItem item : table.items) {
							int chance;
                            /*if (player.xpMode == XpMode.HARD) {
                                if (item.weight == 0)
                                    chance = (int) ((1D / tableChance) * .9);
                                else
                                    chance = (int) ((1D / (tableChance * (item.weight / table.totalWeight))) * .9);
                            } else {*/
							if (item.weight == 0)
								chance = (int) (1D / tableChance);
							else
								chance = (int) (1D / (tableChance * (item.weight / table.totalWeight)));
							//  }
							drops.add(new DropTableResult(item.id, item.min, item.max, chance, table.rollChance));

						}
					}
				}
			}
		}

		if (!drops.isEmpty()) {
			player.getPacketSender().sendClientScript(10526, "iis", 47644708, 1, "Viewing drop table for: <col=ff0000>" + name + "</col>");
			player.getPacketSender().sendClientScript(10525, "is", drops.size(), buildDrops(drops));

			int slot = 1;

			for (DropTableResult drop : drops) {
				player.getPacketSender().sendClientScript(10202, "iii", slot, drop.getId(), (drop.getMin() + drop.getMax()) / 2);
				slot += 8;
			}
		}
	}

	private static void displayDrops(Player player, int id, String name) {
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
		List<DropTableResult> drops = new ArrayList<>();
		double totalTablesWeight = def.lootTable.totalWeight;
		if (def.lootTable.guaranteed != null) {
			for (LootItem item : def.lootTable.guaranteed) {
				drops.add(new DropTableResult(item.id, item.min, item.max, 1, -1));
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
							int chance;
                            /*if (player.xpMode == XpMode.HARD) {
                                if (item.weight == 0)
                                    chance = (int) ((1D / tableChance) * .9);
                                else
                                    chance = (int) ((1D / (tableChance * (item.weight / table.totalWeight))) * .9);
                            } else {*/
							if (item.weight == 0)
								chance = (int) (1D / tableChance);
							else
								chance = (int) (1D / (tableChance * (item.weight / table.totalWeight)));
							// }
							drops.add(new DropTableResult(item.id, item.min, item.max, chance, table.rollChance));
						}
					}
				}
			}
		}

		if (!drops.isEmpty()) {
			showInfo(player, id, name);
			player.getPacketSender().sendClientScript(10526, "iis", 47644708, 1, "Viewing drop table for: <col=ff0000>" + name + "</col>");
			player.getPacketSender().sendClientScript(10525, "is", drops.size(), buildDrops(drops));

			int slot = 1;

			for (DropTableResult drop : drops) {
				player.getPacketSender().sendClientScript(10202, "iii", slot, drop.getId(), (drop.getMin() + drop.getMax()) / 2);
				slot += 8;
			}
		}
	}

	private static void showInfo(Player player, int id, String name) {
		NPCType def = NPCType.get(id);
		if (def.combatInfo == null) {
			player.sendMessage("<img=108>" + Color.DARK_GREEN.tag() + " Bestiary: " + Color.OLIVE.tag() + name + " has no combat information.");
			return;
		}
		String stats = (Color.ORANGE.tag() + "Stats") + "<br>" +
			"Combat Level: " + def.combatLevel + "<br>" +
			"Hitpoints: " + def.combatInfo.hitpoints + "<br>" +
			"Attack: " + def.combatInfo.attack + "<br>" +
			"Strength: " + def.combatInfo.strength + "<br>" +
			"Defence: " + def.combatInfo.defence + "<br>" +
			"Ranged: " + def.combatInfo.ranged + "<br>" +
			"Magic: " + def.combatInfo.magic + "<br>" +
			"Max Standard Hit: " + def.combatInfo.max_damage + "<br>" +
			"Main Attack Style: " + StringUtils.capitalizeFirst(def.combatInfo.attack_style.name().toLowerCase());
		String aggressiveStats = (Color.ORANGE.tag() + "Aggressive Stats") + "<br>" +
			"Stab: " + def.combatInfo.stab_attack + "<br>" +
			"Slash: " + def.combatInfo.slash_attack + "<br>" +
			"Crush: " + def.combatInfo.crush_attack + "<br>" +
			"Magic: " + def.combatInfo.magic_attack + "<br>" +
			"Ranged: " + def.combatInfo.ranged_attack;
		String defensiveStats = (Color.ORANGE.tag() + "Defensive Stats") + "<br>" +
			"Stab: " + def.combatInfo.stab_defence + "<br>" +
			"Slash: " + def.combatInfo.slash_defence + "<br>" +
			"Crush: " + def.combatInfo.crush_defence + "<br>" +
			"Magic: " + def.combatInfo.magic_defence + "<br>" +
			"Ranged: " + def.combatInfo.ranged_defence;
		double attackSeconds = ((def.combatInfo.attack_ticks * (double) Server.tickMs()) / 1000D);
		double deathSeconds = ((def.combatInfo.death_ticks * (double) Server.tickMs()) / 1000D);
		double respawnSeconds = ((def.combatInfo.respawn_ticks * (double) Server.tickMs()) / 1000D);
		String other = (Color.ORANGE.tag() + "Other Information") + "<br>" +
			"Attack Delay: " + (attackSeconds % 1 == 0 ? Integer.toString((int) attackSeconds) : attackSeconds) + " seconds<br>" +
			"Death Delay: " + (deathSeconds % 1 == 0 ? Integer.toString((int) deathSeconds) : deathSeconds) + " seconds<br>" +
			"Respawn Delay: " + (respawnSeconds % 1 == 0 ? Integer.toString((int) respawnSeconds) : respawnSeconds) + " seconds<br>" +
			"Immune to Poison: " + (def.combatInfo.poison_immunity ? "Yes" : "No") + "<br>" +
			"Immune to Venom: " + (def.combatInfo.venom_immunity ? "Yes" : "No");
		if (def.combatInfo.slayer_tasks != null && def.combatInfo.slayer_tasks.length > 0) {
			other += "<br>Slayer Level: " + def.combatInfo.slayer_level + "<br>" +
				"Slayer Experience: " + (def.combatInfo.slayer_xp % 1 == 0 ? Integer.toString((int) def.combatInfo.slayer_xp) : def.combatInfo.slayer_xp) + "<br>" +
				"Slayer Tasks: " + Arrays.toString(def.combatInfo.slayer_tasks).replace("[", "").replace("]", "");
		}
		if (def.combatInfo.combat_xp_modifier != 1.0) {
			int mod = (int) (def.combatInfo.combat_xp_modifier * 100);
			other += "<br>Combat XP Modifier: " + mod + "%<br>";
		}
		player.getPacketSender().sendString(522, 3, name);
		player.getPacketSender().sendClientScript(1179, "s", stats + "|" + aggressiveStats + "|" + defensiveStats + "|" + other);
		//todo - generate this string in the constructor! ^^^ :)
		player.openInterface(ToplevelComponent.SIDEMODAL, 522);
		//Achievements.progress(player, AchievementKeys.THE_BESTIARY);
	}

	public static String buildEntries(List<DropTableEntry> entries) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < entries.size(); i++) {
			DropTableEntry e = entries.get(i);
			sb.append(e.name);

			if (i != entries.size() - 1) {
				sb.append("|");
			}
		}

		return sb.toString();
	}

	public static String buildDrops(List<DropTableResult> drops) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < drops.size(); i++) {
			DropTableResult r = drops.get(i);
			sb.append(r.get());

			if (i != drops.size() - 1) {
				sb.append("|");
			}
		}

		return sb.toString();
	}

	private static String formatForSearch(String string) {
		return string.replace("'", "")
			.toLowerCase()
			.trim();
	}

	public static void register() {
		NPCType.forEach(def -> {
			if (def != null && def.lootTable != null)
				def.lootTable.allItems().forEach(item -> drops.computeIfAbsent(item.getId(), k -> new LinkedHashSet<>()).add(def));
			if (def != null && def.combatInfo != null && def.combatInfo.pet != null)
				drops.computeIfAbsent(def.combatInfo.pet.itemId, k -> new LinkedHashSet<>()).add(def);
		});

		InterfaceHandler.register(847, h -> {
			h.actions[16] = (SlotAction) (player, slot) -> clickEntry(player, slot);
			h.actions[17] = (SimpleAction) player -> player.stringInput("<img=108> Enter monster name to search for:", name -> Bestiary.search(player, name, true));
			h.actions[18] = (SimpleAction) player -> player.stringInput("<img=33> Enter drop name to search for:", name -> Bestiary.search(player, name, false));

			// h.actions[40] = (SimpleAction) Bestiary::openBoxLoot;
		});
	}

}
