package io.ruin.model.skills.construction.actions.impl.costume_room;

import io.ruin.cache.EnumMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-07
 */
public final class Outfit {
	// A Map of Costume Pieces, and how many are stored of each one
	private final Map<Integer, Integer> contents;

	public Outfit(int displayedItemId) {
		this.contents = new HashMap<>();
		// load the contents for this outfit
		var outfitPieces = getValidCostumeIdsForCostumePiece(displayedItemId);
		// if the list is empty, it's a single item
		if (outfitPieces.length == 0) {
			contents.put(displayedItemId, 0);
		}
		else {
			for (int costumePieceId : outfitPieces) {
				contents.put(costumePieceId, 0);
			}
		}
	}

	public Map<Integer, Integer> contents() {
		return contents;
	}

	private int[] getValidCostumeIdsForCostumePiece(int displayedId) {
		var BASE_MAP = 3077;
		var enumMap = EnumMap.get(BASE_MAP);
		var ticker = 0;
		for (var subKey : enumMap.keys) {
			if (displayedId == subKey) {
				var subValue = enumMap.intValues[ticker];
				return EnumMap.get(subValue).intValues;
			}
			ticker++;
		}
		return new int[0];
	}
}