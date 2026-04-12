package io.ruin.model.activities.bosses.phantommuspah;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;

public class Cloud extends NPC {

	Bounds ORB_BOUNDS;
	private static final int ORB_RADIUS = 1;
	int x;
	int y;
	NPC boss;

	public Cloud(int id, Position position, NPC boss) {
		super(id);
		this.boss = boss;
		this.spawn(position);
		ORB_BOUNDS = new Bounds(this.getPosition().getRegion().baseX + 20,
			this.getPosition().getRegion().baseY + 23, this.getPosition().getRegion().baseX + 41,
			this.getPosition().getRegion().baseY + 45, this.getPosition().getZ());
		this.x = Random.get(-1, 1);
		this.y = Random.get(-1, 1);


		moveOrb();

		World.startEvent(event -> {
			while (!this.isRemoved()) {
				event.delay(1);
				update();
			}
		});
	}


	private void update() {
		handleWallCollision();
		moveOrb();
		for (Player player : this.localPlayers()) {
			if (player.getPosition().isWithinDistance(this.getPosition(), 0)) {
				player.hit(new Hit().fixedDamage(25));
				PhantomMuspah b = (PhantomMuspah) boss.getCombat();
				b.damagedPlayer = true;
			}
		}
	}


	public void moveOrb() {
		int x = this.getPosition().getX() - this.x;
		int y = this.getPosition().getY() - this.y;
		this.stepAbs(x, y, StepType.WALK);
	}


	private void handleWallCollision() {
		if (!ORB_BOUNDS.inBounds(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ(), ORB_RADIUS)) {
			if (this.x == 1)
				this.x = -1;
			else if (this.x == -1)
				this.x = 1;
			if (this.y == 1)
				this.y = -1;
			else if (this.y == -1)
				this.y = 1;
		}
	}

}
