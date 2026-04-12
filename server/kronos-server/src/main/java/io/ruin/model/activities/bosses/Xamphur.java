package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.Tile;
import io.ruin.model.map.route.routes.ProjectileRoute;
import io.ruin.utility.Misc;
import io.ruin.utility.TickDelay;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Xamphur extends NPCCombat {

	private static final Projectile MAGIC_PROJECTILE = new Projectile(1907, 40, 31, 25, 56, 10, 15, 220);

	private List<NPC> minions = new ArrayList<>(3);

	private TickDelay minionCooldown = new TickDelay();

	private void postDefend(Hit hit) {
		if (!minions.isEmpty() && minions.stream().anyMatch(n -> !n.isRemoved() && !n.getCombat().isDead())) {
			hit.damage *= 0.20;
			if (hit.attacker != null && hit.attacker.player != null)
				hit.attacker.player.sendMessage(Color.RED.wrap("Xamphur resists your damage through her minions' energy!"));
		}
		if (hit.damage > 100 && !hit.isBlocked())
			hit.damage = 100;
	}

	private void killMinions() {
		minions.forEach(n -> {
			if (n != null && !n.isRemoved() && !n.getCombat().isDead()) {
				n.getCombat().startDeath(null);
			}
		});
	}

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDefend(this::postDefend);
		npc.deathStartListener = (entity, killer, killHit) -> killMinions();
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		if (!withinDistance(10))
			return false;
		if (withinDistance(10) && Random.rollPercent(40)) {
			xamphurClaws();
		} else if (npc.getHp() <= npc.getMaxHp() / 2
			&& !minionCooldown.isDelayed()
			&& Random.rollPercent(35)
			&& (minions.isEmpty() || minions.stream().allMatch(n -> n.isRemoved() || n.getCombat().isDead()))) {
			spawnMinions();
		} else {
			mageAttack();
		}
		return true;
	}

	@Override
	public void process() {

	}

	private void spawnMinions() {
		minionCooldown.delay(30);
		minions.clear();
		npc.transform(10955);
		npc.animate(9066);
		for (int i = 0; i < 3; i++) {
			Player player = Random.get(npc.localPlayers());
			if (player == null)
				return;
			Position spawnPos = Random.get(player.getPosition().area(1, pos -> Tile.get(pos) == null || Tile.get(pos).clipping == 0));
			npc.addEvent(event -> {
				event.delay(1);
				if (target == null || isDead() || npc.isRemoved())
					return;
				NPC minion = new NPC(10958).spawn(spawnPos);
				minions.add(minion);
				minion.getCombat().setTarget(player);
				minion.face(player);
				World.sendGraphics(1919, 0, 0, spawnPos);
				minion.deathStartListener = (entity, killer, killHit) -> {
					final Position explosionTile = minion.getPosition().copy();
					minion.addEvent(boomEvent -> {
						boomEvent.delay(4);
						World.sendGraphics(1328, 0, 0, explosionTile);
						boomEvent.delay(1);
						explosionTile.getRegion().players.forEach(p -> {
							if (Misc.getDistance(p.getPosition(), explosionTile) <= 1) {
								p.hit(new Hit(null, null).randDamage(7, 18).ignorePrayer().ignoreDefence());
							}
						});
					});
				};
			});
		}
	}

	private void xamphurClaws() {
		npc.animate(9064);
		npc.face(target);
		List<Player> targets = npc.localPlayers().stream().filter(t -> ProjectileRoute.allow(npc, t)).collect(Collectors.toList());
		target.graphics(1914, 0, 0);
		targets.forEach(p -> {
			Hit hit = new Hit(npc, AttackStyle.MAGIC)
				.randDamage(45)
				.ignorePrayer();
			npc.incrementHp((int) (hit.damage * 0.25)); // heal 1/4 of dmg done
			target.hit(hit);
		});

	}

	private void mageAttack() {//2100 2050
		npc.animate(9068);
		int delay = MAGIC_PROJECTILE.send(npc, target);
		Hit hit = new Hit(npc, AttackStyle.MAGIC).randDamage(info.max_damage).clientDelay(delay);
		target.hit(hit);
		target.graphics(1908, 100, delay);
	}


}
