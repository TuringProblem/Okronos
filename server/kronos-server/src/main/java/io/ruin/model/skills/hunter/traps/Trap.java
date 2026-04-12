package io.ruin.model.skills.hunter.traps;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.skills.hunter.Hunter;
import io.ruin.model.skills.hunter.creature.Creature;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trap {

	private final Player owner;
	private final TrapType trapType;
	private GameObject object;
	private Creature trappedCreature;
	private boolean busy;
	private boolean flaggedForReset = false;

	private boolean removed = false;

	public Trap(Player owner, TrapType trapType, GameObject object) {
		this.owner = owner;
		this.trapType = trapType;
		this.object = object;
		object.trap = this;
	}

	public void resetTrap() {
		flaggedForReset = true;
	}
}
