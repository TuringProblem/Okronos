package io.ruin.model.map.object.actions.impl.prifddinas.trahaearndistrict;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.utility.TickDelay;

import static io.ruin.model.combat.AttackStyle.MAGIC;

public class ZalcanoCombat extends NPCCombat {

	private Bounds arenaBounds;

	//Zalcano GFX
	private static final Projectile BOULDER_DROP_PROJECTILE = new Projectile(1727, 50, 0, 0, 75, 0, 0, 127);
	private static final Projectile MAGIC_BALL_GOLEM_SPAWN = new Projectile(1729, 25, 31, 25, 35, 10, 0, 32);
	private static final Projectile MAGIC_PROJECTILE = new Projectile(1728, 25, 31, 25, 35, 10, 0, 32);
	private static final Projectile PEBBLE_PROJECTILE = new Projectile(1302, 25, 31, 25, 35, 10, 0, 32);
	private static final int RANGED_HIT_GFX = 1303;
	private static final int GOLEM_EXPLOSION = 1730;

	private boolean spawnedGolem = false;
	private NPC[] golem = new NPC[1];

	//Zalcano Anims
	private static final int MAGE_ANIM = 8432;//Spawns Golem
	private static final int MAGE_ANIM2 = 8434;//Shoots magic ball at rocks
	private static final int DROP_ROCK_ANIM = 8435;//dropping boulder
	private static final int THROW_PEBBLE = 8435;//throwing pebbles
	private static final int SPAWN_SYMBOLS = 8433;//spawning symbols

	private TickDelay boulderCooldown = new TickDelay();
	private int misses = 0;
	private static final int MISS_THRESHOLD = 3;
	private AttackStyle style = MAGIC;


	@Override
	public void init() {
		npc.hitListener = new HitListener().preDefend(this::preDefend);
	}

	private void preDefend(Hit hit) {
		if ((hit.attackStyle != null && !hit.attackStyle.isMelee())
			|| hit.attacker != null && hit.attacker.player != null && VarPlayerRepository.WEAPON_TYPE.get(hit.attacker.player) != 11) {
			hit.block();
			if (hit.attacker.player != null)
				hit.attacker.player.sendMessage("<col=880000>Zalcano resists your attack.");
		}
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		if (!withinDistance(7))
			return false;
		if (Random.rollDie(5, 1))
			dropBoulder();
		else if (Random.rollDie(5, 1))
			magicBall();
		else
			stoneThrow();
		return true;
	}

	@Override
	public void process() {

	}

	private void stoneThrow() {
		npc.animate(8431);
		int delay = PEBBLE_PROJECTILE.send(npc, target);
		Hit hit = new Hit(npc, AttackStyle.RANGED).randDamage(info.max_damage).clientDelay(delay);
		target.hit(hit);
	}

	private void magicBall() {
		npc.animate(8432);
		int delay = MAGIC_BALL_GOLEM_SPAWN.send(npc, target);
		Hit hit = new Hit(npc, AttackStyle.RANGED).randDamage(info.max_damage).clientDelay(delay);
		target.graphics(1730);
		target.hit(hit);
	}

	private void dropBoulder() {
		boulderCooldown.delay(30);
		npc.animate(8433);
		Position pos = target.getPosition().copy();
		int clientDelay = 5;
		int tickDelay = (((25 * clientDelay)) / 600) - 2;
		World.sendGraphics(1727, 35, clientDelay, pos.getX(), pos.getY(), pos.getZ());
		npc.addEvent(event -> {
			event.delay(tickDelay);
			if (target == null)
				return;
			if (target.getPosition().equals(pos)) {
				event.delay(4);
				if (target != null)
					target.hit(new Hit(npc, null, null).fixedDamage(target.getHp() / 3).ignoreDefence().ignorePrayer());
			}
		});
	}
}
