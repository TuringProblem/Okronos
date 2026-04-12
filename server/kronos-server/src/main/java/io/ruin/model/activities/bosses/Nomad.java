package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.utility.Misc;
import io.ruin.utility.TickDelay;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.lang.Math.max;

public class Nomad extends NPCCombat {

	private static final Projectile BOULDER_DROP_PROJECTILE = new Projectile(1493, 150, 0, 0, 135, 0, 0, 127);
	private static final int BOULDER_HIT_GFX = 1494;

	private static final TickDelay HealDelay = new TickDelay();

	private List<Position> bubbles = new ArrayList<>(20);
	private static final Projectile MAGIC_PROJECTILE = new Projectile(1577, 0, 0, 20, 15, 12, 0, 10);

	private static final Projectile LAVA_PROJECTILE = new Projectile(660, 0, 0, 0, 100, 0, 45, 0);
	private boolean forceQuake = false;

	@Override
	public void init() {
		npc.hitsUpdate.hpbarId = 2;
		npc.hitListener = new HitListener().postDefend(this::postDefend);
	}

	private void postDefend(Hit hit) {
		if (hit.damage > 100 && !hit.isBlocked())
			hit.damage = 100;
	}

	@Override
	public void updateLastDefend(Entity attacker) {
		super.updateLastDefend(attacker);
		super.updateLastDefend(attacker);
		if (attacker.player != null && !attacker.player.getCombat().isSkulled()) {
			attacker.player.getCombat().skullNormal();
			attacker.player.sendMessage("<col=6f0000>You've been marked with a skull for attacking Nomad!");
		}
	}

	@Override
	public void follow() {
		follow(6);
	}

	public void heal() {
		int heal = (int) ((npc.getMaxHp() - npc.getHp()) * 0.50);
		//HitType.HEAL
		if (heal > 50) {
			heal = 50;
		}
		npc.hit(new Hit(HitType.HEAL).fixedDamage(heal));
		int random = Random.get(1, 3);
		switch (random) {
			case 1: {
				npc.forceText("Feel the power of the gods!");
				break;
			}
		}
		heal = (int) (heal / max(1, npc.getPosition().getRegion().players.size() * 0.5));
		if (heal > 15) {
			HealDelay.delay(heal);
		} else {
			HealDelay.delay(15);
		}
	}

	@Override
	public boolean attack() {
		if (!withinDistance(8))
			return false;
		if (forceQuake || Random.rollDie(5, 1)) {
			earthquake();
			forceQuake = false;
		} else if (withinDistance(1) || Random.rollPercent(80)) {
			rangedAttack();
		} else {
			taunt();
		}
		return true;
	}

	@Override
	public void process() {

	}

	private void earthquake() {
		npc.animate(8184);
		npc.graphics(1627);
		npc.forceText("Feel my wrath!");
		npc.addEvent(event -> {
			event.delay(1);
			npc.localPlayers().forEach(p -> {
				if (!canAttack(p))
					return;
				int distance = Misc.getEffectiveDistance(npc, p);
				if (distance >= 8)
					return;
				int damage = 50 - (distance * 5);
				int delay = MAGIC_PROJECTILE.send(npc, p);
				p.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(info.max_damage).clientDelay(delay));
			});
		});
	}

	private void rangedAttack() {
		npc.animate(6147);
		npc.addEvent(event -> {
			event.delay(1);
			npc.localPlayers().forEach(p -> {
				if (!canAttack(p))
					return;
				int distance = Misc.getEffectiveDistance(npc, p);
				if (distance >= 8)
					return;
				p.getPrayer().deactivateAll();
				p.sendMessage("Nomad's slam disables your prayers!");
				Position pos = target.getPosition().copy();
				int clientDelay = BOULDER_DROP_PROJECTILE.send(npc.getAbsX(), npc.getAbsY(), pos.getX(), pos.getY());
				p.hit(new Hit(npc, null, null).fixedDamage(target.getHp() / 3).ignoreDefence());
				World.sendGraphics(BOULDER_HIT_GFX, 35, clientDelay, pos.getX(), pos.getY(), pos.getZ());
				p.addEvent(camEvent -> {
					camEvent.delay(2);
					p.getPacketSender().resetCamera();
				});

			});
		});
	}

	private void taunt() {
		Player player = target.player;
		npc.animate(1056);
		npc.forceText("Kneel before your master!");
		delayAttack(-2);
		forceQuake = true;
		if (target.player != null)
			target.player.sendMessage(Color.RED.wrap("Nomad's taunt forces you to mindlessly run towards him!"));
		player.addEvent(event -> {
			player.lock();
			player.getCombat().reset();
			player.face(npc);
			player.getRouteFinder().routeEntity(npc);
			event.delay(2);
			player.unlock();
			player.hit(new Hit().randDamage(1, 25).delay(0));
		});
	}
}
