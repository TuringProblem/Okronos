package collectionlog;

import io.ruin.cache.EnumMap;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;

import java.util.ArrayList;


public class CollectionLogDataSet {

	private final int id;

	private final int[] itemIds;

	private final String contentsMessage;

	private CollectionLogDataSet(EnumMap map) {
		this.id = map.intValues[0];
		this.itemIds = new int[map.length - 1];
		System.arraycopy(map.intValues, 1, this.itemIds, 0, map.length - 1);
		StringBuilder message = new StringBuilder();
		for (int i = 0; i < itemIds.length; i++) {
			message.append("<col=ef1020>").append(ObjType.get(itemIds[i]).name);
			if (i == itemIds.length - 1)
				message.append("</col>.");
			else
				message.append("</col>, ");
		}
		this.contentsMessage = message.toString();
	}

	private CollectionLogDataSet(int id, int... itemIds) {
		this.id = id;
		this.itemIds = itemIds;
		this.contentsMessage = null;
	}

	public static void sendCollectionLogEnumId(Player player, int enumId) {
		EnumMap map = EnumMap.get(enumId);
		ArrayList<CollectionLogDataSet> collectionLogEnumList = new ArrayList<>();
		for (int i = 0; i < map.length; i++) {
			EnumMap enumMap = EnumMap.get(map.intValues[i]);
			collectionLogEnumList.add(new CollectionLogDataSet(enumMap));
		}
		CollectionLogDataSet[] sets = collectionLogEnumList.toArray(new CollectionLogDataSet[collectionLogEnumList.size()]);
	}

}
