package io.ruin.model.entity.npc.actions;

import io.ruin.api.utils.Random;
import io.ruin.model.entity.shared.listeners.SpawnListener;

public class KourendSoldiers {

	public static final int[] SOLDIERS = new int[]{6883, 6893, 6894, 6892, 6885, 6884, 6888, 6887, 6886, 6889, 6890, 6891};

	private static final String[] SHOUT = {
		"We do this for Rocky!",
		"Crazz gets spit on!",
		"Coins is better then Crazz!",
		"Guwop is shit!"};

	public static void register() {
		SpawnListener.register(SOLDIERS, npc -> {
			npc.addEvent(e -> {
				while (true) {
					e.delay(Random.get(2, 4));
					npc.forceText(Random.get(SHOUT));
					npc.animate(2763);
				}
			});
		});
	}
}
