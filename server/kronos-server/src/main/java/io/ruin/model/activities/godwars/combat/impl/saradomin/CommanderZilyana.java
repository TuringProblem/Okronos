package io.ruin.model.activities.godwars.combat.impl.saradomin;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.godwars.combat.General;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.route.routes.ProjectileRoute;
import io.ruin.model.skills.prayer.Prayer;

import java.util.ArrayList;
import java.util.List;

public class CommanderZilyana extends General {
//    {"id": 2205, "x": 2899, "y": 5267, "walkRange": 9}, // Commander Zilyana
//    {"id": 2206, "x": 2901, "y": 5264, "walkRange": 9}, // Starlight
//    {"id": 2207, "x": 2897, "y": 5263, "walkRange": 9}, // Growler
//    {"id": 2208, "x": 2895, "y": 5265, "walkRange": 9}, // Bree


	public boolean attackedWithNonMelee = false;
	public boolean animalWhispererFailed = false;


	public CommanderZilyana() {
		super(new Lieutenant(2206, 2, -3, 9),
			new Lieutenant(2207, -2, -4, 9),
			new Lieutenant(2208, -4, -2, 9));
	}

	private void postDamage(Hit hit) {
		if (hit.attacker != null && hit.attackStyle != null) {
			if (!hit.attackStyle.isMelee())
				attackedWithNonMelee = true;
		}
	}

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(this::postDamage);
		super.init();
	}


	@Override
	public void follow() {
		follow(1);
	}


	@Override
	public boolean attack() {
		if (!withinDistance(1))
			return false;
		if (Random.rollDie(5, 1))
			npc.forceText(Random.get(SHOUTS));
		if (Random.rollDie(7, 1))
			magicAttack();
		else {
			basicAttack();
			animalWhispererFailed = true;
		}
		return true;
	}


	@Override
	public void process() {

	}


	private void magicAttack() {
		npc.animate(6970);
		npc.lock(LockType.MOVEMENT); // ?
		npc.localPlayers().forEach(p -> {
			if (ProjectileRoute.allow(npc, p)) {
				Hit hit = new Hit(npc, AttackStyle.MAGIC, null).randDamage(27);
				Hit hit2 = new Hit(npc, AttackStyle.MAGIC, null).randDamage(27).delay(1);

				p.hit(hit, hit2);
				hit.postDamage(t -> {
					if (t.player != null && !t.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
						animalWhispererFailed = true;
					}
				});
				hit2.postDamage(t -> {
					if (t.player != null && !t.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
						animalWhispererFailed = true;
					}
				});
				p.graphics(1221, 30, 0);
			}
		});
		npc.addEvent(event -> {
			event.delay(2);
			npc.unlock();
		});


	}

	private static final String[] SHOUTS = {
		"Death to the enemies of the light!",
		"Slay the evil ones!",
		"Saradomin lend me strength!",
		"By the power of Saradomin!",
		"May Saradomin be my sword!",
		"Good will always triumph!",
		"Forward! Our allies are with us!",
		"Saradomin is with us!",
		"In the name of Saradomin!",
		"Attack! Find the Godsword!",
	};

	@Override
	public int getAttackBoundsRange() {
		return 20;
	}
}
