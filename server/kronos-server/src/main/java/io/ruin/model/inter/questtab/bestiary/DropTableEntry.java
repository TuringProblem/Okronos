package io.ruin.model.inter.questtab.bestiary;

import io.ruin.cache.NPCType;
import io.ruin.model.item.loot.LootTable;

public class DropTableEntry {

	public int id;
	public String name;
	public LootTable table;

	public DropTableEntry(int npcId) {
		this.id = npcId;
		this.name = NPCType.get(npcId).name.replaceAll("<col=\\w{6}>|</col>", "");
	}

	public DropTableEntry(String name, LootTable table) {
		this.name = name;
		this.table = table;
	}

}
