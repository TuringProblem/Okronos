package royaltitans;

import static core.api.API.*;
import static core.api.APIUtils.*;

public class IceQueen implements Continuation {
	public final RoyalTitansArea area;
	public final NPC npc;
	public Stage stage = Stage.Normal;
	public Stage stageLast = Stage.Normal;

	public IceQueen(RoyalTitansArea area, Coord spawn) {
		this.area = area;
		this.npc = npc(14147);
		this.npc.spawn(spawn);

		this.npc.allow_combat();
		this.npc.allow_respawn(false);
		this.npc.stats(600, 300, 100, 250, 150, 100);
		this.npc.stats_aggresive(150, 0, 100, 100, 100, 100);
		this.npc.stats_melee_defence(12, 12, 0);
		this.npc.stats_magic_defence(700);
		this.npc.stats_ranged_defence(700, 700, 700);
	}

	@Override
	public void started() {}

	@Override
	public void call() {

		loop((ctx) -> {
			if (this.npc.removed()) {
				ctx.stop();
				return;
			}

			var attack = fork(() -> {
				switch (stage) {
					case Normal -> {
						randomWalk();
						sleep(1);
					}

					case Under30Hp -> {
						this.npc.face(Direction.North);
						sleep(1);
					}
				}
			});

			while (!attack.done()) {
				sleep(1);
				if (this.stage == Stage.Under30Hp && stageLast == Stage.Normal) {
					attack.stop();
					this.npc.teleport(coord(this.area.map, 30, 25));
					this.npc.set_models(55940);
					this.npc.animate(11963);
					this.npc.graphics(3216);
					spawnIceFloor();
				}
				this.stageLast = this.stage;
			}

		});
	}

	void aoe_02() {
		if (players(this.npc).isEmpty()) {
			sleep(1);
			return;
		}

		var target = players(this.npc).get(0);
		this.npc.face(target);
		sleep(1);
		this.npc.animate(11982);
		this.npc.graphics(3215);

		graphics(coord(target), gfx(3209));

		sleep(6);
	}

	void fires() {
		if (players(this.npc).isEmpty()) {
			sleep(1);
			return;
		}

		var target = players(this.npc).get(0);
		this.npc.face(target);
		sleep(1);
		this.npc.animate(11976);
		this.npc.graphics(3212);

		// for (int xx = 0; xx < 5; xx++) {
		// var baseX = pos(this.area.map, 29, 0).relative(xx, 0);
		// var baseY = pos(target);
		// graphic(pos(baseX, baseY), gfx(3218));
		// }

		sleep(2);
	}

	void randomWalk() {
		var tiles = coords().rect(coord(this.area.map, 34, 37), coord(this.area.map, 39, 25));
		var target = tiles.random();
		for (int i = 0; i < 2; i++) {
			//step(this.npc, target);
			sleep(1);
		}
	}

	void meleeAttack() {
		var players = players(this.npc);
		if (players.isEmpty()) {
			sleep(1);
			return;
		}

		this.npc.say("I'm coming for you!");
		var target = players.get(0);
		this.npc.face_none();
		this.npc.face(target);
		// var step = walk(this.npc, target, 10, (pos) -> {
		// 	if (pos.regionX() > 28) {
		// 		return false;
		// 	}
		// 	return true;
		// }).await();
		sleep(1);

		this.npc.face(Direction.East);
		this.npc.animate(11974);

		for (var x = 0; x < 5; x++) {
			for (var y = 0; y < 3; y++) {
				graphics(coord(this.npc).translate(x + 3, y), gfx(3211, x * 2));
			}
		}

		// players(graphics).forEach(it -> {
		// it.hit(new Hit().damage(10));
		// });
		sleep(1);
		this.npc.face_none();
	}

	void spawnIceFloor() {
		obj(55997, 10, (coord(this.area.map, 33, 25))).spawn();
		obj(55998, 10, (coord(this.area.map, 29, 25))).spawn();

		obj(26209, 10, (coord(this.area.map, 30, 25))).spawn();
		obj(26209, 10, (coord(this.area.map, 30, 26))).spawn();
		obj(26209, 10, (coord(this.area.map, 30, 27))).spawn();
		obj(26209, 10, (coord(this.area.map, 31, 25))).spawn();
		obj(26209, 10, (coord(this.area.map, 31, 26))).spawn();
		obj(26209, 10, (coord(this.area.map, 31, 27))).spawn();
		obj(26209, 10, (coord(this.area.map, 32, 25))).spawn();
		obj(26209, 10, (coord(this.area.map, 32, 26))).spawn();
		obj(26209, 10, (coord(this.area.map, 32, 27))).spawn();
	}

	@Override
	public void stopped(StopReason reason) {
		this.npc.remove();
	}

}
