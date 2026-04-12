package io.ruin.model.skills.runecrafting;

import io.ruin.cache.ObjType;

public enum Essence {

	REGULAR(1436),
	PURE(7936),
	DARK(7938);

	public final int id;
	public final String name;

	Essence(int id) {
		this.id = id;
		this.name = ObjType.get(id).name.toLowerCase();
	}

}
