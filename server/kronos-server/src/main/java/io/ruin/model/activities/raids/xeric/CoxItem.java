package io.ruin.model.activities.raids.xeric;

import io.ruin.cache.EnumType;
import io.ruin.cache.ObjType;

public class CoxItem {

	public static void register() {
		EnumType.get(1666).getIntValues().forEach((k, v) -> {
			mark(v);
		});
	}

	private static void mark(int id) {
		ObjType def = ObjType.get(id);
		if (def != null)
			mark(def);
	}

	private static void mark(ObjType def) {
		def.coxItem = true;
		def.tradeable = true;
	}

}
