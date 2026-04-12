package io.ruin.model.activities.bosses.worldboss;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.route.routes.ProjectileRoute;
import io.ruin.model.stat.StatType;
import io.ruin.utility.TickDelay;

import java.util.function.Consumer;

public class AvatarOfDestruction extends NPCCombat {

	private static StatType[] DRAIN = {StatType.Attack, StatType.Strength, StatType.Defence, StatType.Ranged, StatType.Magic};
	private static final Projectile MAGIC_PROJECTILE = new Projectile(1714, 100, 10, 25, 70, 10, 12, 64);
	private static final TickDelay HealDelay = new TickDelay();
	private static final Projectile RANGE_ATTACK = new Projectile(1713, 100, 10, 50, 70, 10, 12, 64);
	private static final Projectile CRYSTAL_BOMB_PROJECTILE = new Projectile(1357, 90, 0, 30, 100, 0, 16, 0);

	@Override
	public void init() {
		HealDelay.delay(50);
		npc.setIgnoreMulti(true);
	}

	@Override
	public void follow() {
		follow(8);
	}

	private void forAllTargets(Consumer<Player> action) {
		npc.getPosition().getRegion().players.stream()
			.filter(p -> p.getHeight() == npc.getPosition().getZ()) // on olm floor, past the barrier
			.forEach(action);
	}

	public void meteorAttack() {
		npc.forceText("May the crystals bomb you!");
		npc.addEvent(event -> {
			Position bombPos = target.getPosition().copy();
			CRYSTAL_BOMB_PROJECTILE.send(npc, bombPos);
			event.delay(3);
			GameObject bomb = GameObject.spawn(29766, bombPos, 10, 0);
			event.delay(8);
			bomb.remove();
			World.sendGraphics(40, 0, 0, bombPos);
			forAllTargets(p -> {
				int distance = p.getPosition().distance(bombPos);
				if (distance > 3)
					return;
				p.hit(new Hit(npc).fixedDamage(p.getMaxHp() - (distance * 10)));
			});
			if (npc.getPosition().distance(bombPos) <= 1) {
				npc.hit(new Hit(npc).fixedDamage(73));
			}
		});

	}

	;

	public void heal() {
		int heal = (int) ((npc.getMaxHp() - npc.getHp()) * 0.50);
		//HitType.HEAL
		if (heal > 200) {
			heal = 200;
		}
		npc.hit(new Hit(HitType.HEAL).fixedDamage(heal));
		int random = Random.get(1, 3);

		switch (random) {
			case 1: {
				npc.forceText("Face destruction!");
				break;
			}
			case 2: {
				npc.forceText("I will destroy you!");
				break;
			}
			case 3: {
				npc.forceText("Meet you destruction!");
				break;
			}
		}
		if (heal > 20) {
			HealDelay.delay(heal);
		} else {
			HealDelay.delay(20);
		}
	}

	@Override
	public boolean attack() {
		if (!withinDistance(8)) {
			return false;
		}
		boolean healed = false;
		if (!HealDelay.isDelayed()) {
			healed = true;
			heal();
		}
		if (withinDistance(1)) {
			int random = Random.get(1, 10);
			switch (random) {
				case 1:
				case 2:
				case 3:
				case 4:
					RangeAttack();
					break;
				case 5:
				case 6:
					if (healed) {
						basicAttack();
					} else {
						meteorAttack();
					}
					break;
				case 7:
				case 8:
				case 9:
				case 10:
					basicAttack();
					break;
			}
			return true;
		}
		int random = Random.get(1, 10);
		switch (random) {
			case 1:
			case 2:
			case 3:
			case 4:
				RangeAttack();
				break;
			case 5:
			case 6:
				if (healed) {
					magicAttack();
				} else {
					meteorAttack();
				}
				break;
			case 7:
			case 8:
			case 9:
			case 10:
				magicAttack();
				break;
		}

		return true;
	}

	@Override
	public void process() {

	}

	private void magicAttack() {
		npc.animate(8840);
		npc.localPlayers().forEach(p -> {
			if (ProjectileRoute.allow(npc, p)) {
				int delay = MAGIC_PROJECTILE.send(npc, p);
				Hit hit = new Hit(npc, AttackStyle.MAGIC)
					.randDamage((int) (info.max_damage))
					.clientDelay(delay);
				hit.postDamage(t -> {
					if (hit.damage > 0) {
						t.graphics(1716, 124, 0);
					} else {
						t.graphics(85, 124, 0);
					}
				});
				p.hit(hit);
			}
		});
	}

	private void RangeAttack() {
		npc.animate(8840);
		npc.localPlayers().forEach(p -> {
			if (ProjectileRoute.allow(npc, p)) {
				int delay = RANGE_ATTACK.send(npc, p);
				Hit hit = new Hit(npc, AttackStyle.RANGED)
					.randDamage((int) (info.max_damage * 0.25))
					.clientDelay(delay);
				hit.postDamage(t -> {
					if (hit.damage > 0) {
						t.graphics(1715, 124, 0);
					} else {
						t.graphics(85, 124, 0);
					}
				});
				p.hit(hit);
			}
		});
	}
}
