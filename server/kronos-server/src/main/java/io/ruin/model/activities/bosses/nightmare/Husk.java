package io.ruin.model.activities.bosses.nightmare;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class Husk extends NPC {

	private Player player;
	private Nightmare nm;

	public Husk(int id, Player target, io.ruin.model.activities.bosses.nightmare.Nightmare nm) {
		super(id);
		this.player = target;
		this.nm = nm;
	}

	@Override
	public void process() {
		super.process();
		if (getCombat().getTarget() == null) {
			getCombat().setTarget(player);
			face(player);
		}
		if (getPosition().distance(nm.getBase()) > 64) {
			// stop bug where husks are appearing outside instance following the player. no idea how.
			remove();
		}
	}

}
