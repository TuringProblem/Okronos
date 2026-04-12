package io.ruin.model.entity.npc.actions.edgeville;

import io.ruin.model.entity.shared.listeners.SpawnListener;

public class UntradableShop {

	private final static int SHOPKEEPER = 4225;

	public static void register() {
		SpawnListener.register(SHOPKEEPER, npc -> npc.skipReachCheck = p -> p.equals(3078, 3507));
	}
}
