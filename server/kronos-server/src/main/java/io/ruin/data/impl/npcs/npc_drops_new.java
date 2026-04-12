package io.ruin.data.impl.npcs;

import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;
import io.ruin.api.utils.JsonUtils;
import io.ruin.data.DataFile;
import io.ruin.utility.Broadcast;

import java.util.List;
import java.util.Map;

/**
 * Created on 10/1/2024
 */
public class npc_drops_new extends DataFile {


	public static List<DropTable> getDropInformationNPCCombat(int npcID) {//the fuck
		List<DropTable> dropTables = allDropTables.get(npcID);
		if (dropTables == null) {
			return null;
		}
		return dropTables;
	}

	public static List<DropTable> getDropInformationLootTable(int npcID) {
		return allDropTables.get(npcID);
	}

	public static Map<Integer, List<DropTable>> allDropTables = Maps.newConcurrentMap();

	@Override
	public Object fromJson(String fileName, String json) {
//        System.out.println("Loading drop tables from JSON..." + fileName);
		List<DropTable> dropTables = JsonUtils.fromJson(json, List.class, DropTable.class);
		allDropTables.put(Integer.parseInt(fileName), dropTables);

//        System.out.println(Arrays.toString(dropTables.toArray(new DropTable[0])));

//        for (DropTable dropTable : dropTables) {
//            System.out.println(dropTable.toString());
//        }

		return dropTables;
	}

	@Override
	public String path() {
		return "npcs/drops/newDrops/*.json";
	}

	public static class DropTable {
		@Expose
		public int itemid;
		@Expose
		public int minAmount;
		@Expose
		public int dropRate;
		@Expose
		public int maxAmount;
		@Expose
		public Broadcast broadcast;

		@Override
		public String toString() {
			return "DropTable{" +
				"itemid=" + itemid +
				", minAmount=" + minAmount +
				", dropRate=" + dropRate +
				", maxAmount=" + maxAmount +
				'}';
		}
	}
}
