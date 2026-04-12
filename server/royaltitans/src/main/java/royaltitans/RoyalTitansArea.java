package royaltitans;

import java.util.ArrayList;
import java.util.List;

import static core.api.API.*;
import static core.api.APIUtils.*;

public class RoyalTitansArea implements Continuation.Void {
	public Area map;
	public final List<ElementalFire> fireElementals = new ArrayList<>();

	public final FireQueen fireQueen;
	public final IceQueen iceQueen;

	public RoyalTitansArea() {
		this.map = area(11669);
		if (this.map == null) {
			throw new IllegalStateException();
		}
		this.fireQueen = new FireQueen(this, coord(this.map, 26, 30));
		this.iceQueen = new IceQueen(this, coord(this.map, 34, 30));
	}

	public void call() {
		fork(this.fireQueen);
		fork(this.iceQueen);

		while (!players(this.map).isEmpty()) {
			if (shouldSwitchStage()) {
				this.fireQueen.stage = Stage.Under30Hp;
				this.iceQueen.stage = Stage.Under30Hp;
			}

			sleep(1);
		}
	}

	boolean shouldSwitchStage() {
		return this.fireQueen.npc.hp_perc() <= 0.3
				|| this.iceQueen.npc.hp_perc() <= 0.3;
	}

	public void spawnElementalFire() {
		if (this.fireElementals.size() > 3) {
			return;
		}

		var elemental = new ElementalFire(this);
		this.fireElementals.add(elemental);
		fork(elemental, () -> {
			this.fireElementals.remove(elemental);
		});
	}

	public void move(Player player) {
		player.teleport(coord(this.map, 31, 25));
	}

	@Override
	public void stopped(StopReason reason) {
		map.destroy();
	}


	public final Coords combatArea() {
		return coords().rect(coord(this.map, 29, 37), coord(this.map, 33, 25));
	}

}
