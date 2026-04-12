package io.ruin.model.entity.npc.actions.edgeville;

import io.ruin.model.entity.shared.listeners.SpawnListener;

public class MiscShop {

	private final static int SHOPKEEPER = 4579;

	public static void register() {
		SpawnListener.register(SHOPKEEPER, npc -> npc.skipReachCheck = p -> p.equals(3078, 3512));
	}
}
