package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SuperiorRevenant extends NPCCombat {

	private static final Projectile MAGIC_PROJECTILE = new Projectile(1735, 90, 43, 35, 40, 6, 16, 192);
	private static final Projectile RANGED_PROJECTILE = new Projectile(1731, 90, 43, 35, 40, 6, 16, 192);
	List<Integer> revNpcIds = new ArrayList<>();
	List<NPC> spawnedRevs = new ArrayList<>();
	private AttackStyle lastBasicAttackStyle = Random.rollPercent(50) ? AttackStyle.MAGIC : AttackStyle.RANGED;
	boolean canAttack = true;
	int minionsSpawned = 0;

	@Override
	public void init() {
		addRevsToList();
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend);
	}

	private void postDamage(Hit hit) {

	}

	private void preHitDefend(Hit hit) {
		if (!canAttack) {
			hit.attacker.player.sendMessage("You must kill all the minions before attacking the superior revenant again!");
			hit.block();
		}
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		int random = Random.get(0, 7);
		if (random == 1)
			bombAttack();
		else if (random == 0 && spawnedRevs.size() == 0)
			spawnMinions();
		else
			basicAttack(getAllTargets());
		return true;
	}

	@Override
	public void process() {
		spawnedRevs.removeIf(minion -> minion.getCombat().isDead());
		canAttack = spawnedRevs.size() <= 0;
	}

	private void basicAttack(List<Player> targets) {
		lastBasicAttackStyle = Random.rollPercent(75) ? lastBasicAttackStyle : (lastBasicAttackStyle == AttackStyle.RANGED ? AttackStyle.MAGIC : AttackStyle.RANGED);
		targets.forEach(p -> {
			int delay = (lastBasicAttackStyle == AttackStyle.RANGED ? RANGED_PROJECTILE : MAGIC_PROJECTILE).send(npc, p);
			npc.animate(69);
			int maxDamage = info.max_damage;
			if (p.getPrayer().isActive(lastBasicAttackStyle == AttackStyle.RANGED ? Prayer.PROTECT_FROM_MISSILES : Prayer.PROTECT_FROM_MAGIC))
				maxDamage /= 3;
			p.hit(new Hit(npc, lastBasicAttackStyle).randDamage(maxDamage).ignorePrayer().clientDelay(delay));
			if (lastBasicAttackStyle == AttackStyle.MAGIC && maxDamage > 0)
				p.graphics(1736, 92, delay);
		});
	}

	private void bombAttack() {
		npc.animate(69);
		getAllTargets().forEach(p -> {
			npc.addEvent(event -> {
				Position targetPos = p.getPosition().copy();
				World.sendGraphics(1727, 0, 0, targetPos);
				event.delay(5);
				if (isDead()) // lets be nice
					return;
				if (target != null && target.getPosition().isWithinDistance(targetPos, 1))
					target.hit(new Hit(npc).randDamage(20, 60).delay(0));
			});
		});
	}

	private void addRevsToList() {
		revNpcIds.add(7940);
		revNpcIds.add(7939);
		revNpcIds.add(7938);
		revNpcIds.add(7937);
		revNpcIds.add(7936);
		revNpcIds.add(7935);
		revNpcIds.add(7934);
	}

	private void spawnMinions() {
		int minionAmount = playersInArea();
		minionsSpawned = minionAmount;
		getAllTargets().forEach(p -> {
			int randomNpcId = Random.get(revNpcIds);
			NPC minion = new NPC(randomNpcId).spawn(p.getPosition().getX() + Random.get(-2, 2), p.getPosition().getY() + Random.get(-2, 2), p.getPosition().getZ(), 1);
			minion.face(p);
			spawnedRevs.add(minion);
			minion.getCombat().setTarget(p);
			minion.getCombat().setAllowRespawn(false);
		});
	}

	private void forAllTargets(Consumer<Player> action) {
		npc.getPosition().getRegion().players.stream()
			.filter(p -> p.getPosition().isWithinDistance(npc.getPosition(), 12))
			.forEach(action);
	}

	private List<Player> getAllTargets() {
		return npc.getPosition().getRegion().players.stream()
			.filter(p -> p.getPosition().isWithinDistance(npc.getPosition(), 12))
			.collect(Collectors.toList());
	}

	public int playersInArea() {
		AtomicInteger players = new AtomicInteger();
		forAllTargets(p -> {
			players.getAndIncrement();
		});
		return players.get();
	}
}
