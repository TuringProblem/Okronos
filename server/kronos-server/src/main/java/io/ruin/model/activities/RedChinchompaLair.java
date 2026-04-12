package io.ruin.model.activities;

import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;

public class RedChinchompaLair {
	static Bounds area = new Bounds(new Position(2514, 9289, 0), new Position(2528, 9302, 0), 0);

	public static void init() {
		for (int i = 0; i < 50; i++) {
			Position pos = area.randomPosition();
			System.out.println("{\n" +
				"    \"name\": \"Carnivorous chinchompa\",\n" +
				"    \"id\": 2911,\n" +
				"    \"x\": " + pos.getX() + ",\n" +
				"    \"y\": " + pos.getY() + ",\n" +
				"    \"z\": 0,\n" +
				"    \"direction\": \"S\",\n" +
				"    \"walkRange\": 6\n" +
				"  },");
		}
	}
}
