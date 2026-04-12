package io.ruin.model.entity.shared;

import lombok.Getter;

public enum MovementState {
	NONE(-1),
	CRAWL(0),
	WALK(1),
	RUN(2),
	TELEPORT(127);


	@Getter
	int id;

	MovementState(int id) {
		this.id = id;
	}

	public static MovementState fromId(int id) {
		for (MovementState p : values()) {
			if (p.id == id)
				return p;
		}

		return null;
	}
}
