package io.ruin.model.activities.gauntlet;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;

public abstract class GauntletResource {
	protected int health = 3;
	protected boolean corrupted;
	protected Position position;
	GameObject object;


	public GauntletResource(GameObject object, boolean corrupted) {
		this.object = object;
		this.corrupted = corrupted;
		this.position = object.getPosition();
	}

	public abstract void harvest(Player player, GameObject object);

	public abstract void deplete(GameObject object);
}
