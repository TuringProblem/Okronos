package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.VoteHandler;
import io.ruin.model.World;
import io.ruin.model.activities.DonationBossHandler;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.Tile;
import io.ruin.model.map.route.routes.DumbRoute;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Broadcast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DonationBoss extends NPCCombat {
	private static final LootTable table = new LootTable().addTable(1,
		new LootItem(607, 1, 4, 100),
		new LootItem(30459, 3, 5, 100),
		new LootItem(30460, 3, 5, 100),
		new LootItem(30629, 3, 5, 100),
		new LootItem(30453, 1, 100),
		new LootItem(30461, 1, 2, 40).broadcast(Broadcast.GLOBAL),
		new LootItem(30456, 1, 75),
		new LootItem(30457, 1, 75),
		new LootItem(30462, 1, 25).broadcast(Broadcast.GLOBAL),
		new LootItem(22092, 1, 60).broadcast(Broadcast.GLOBAL),
		new LootItem(608, 1, 75));

	int attacks = 0;
	boolean attackingWithMagic = false;
	Map<Integer, Integer> damagingPlayers = new HashMap<>();
	List<NPC> spawns = new ArrayList<>();

	int attacksSinceHealer = 0;
	private static final Projectile MAGIC_PROJECTILE = new Projectile(1046, 90, 43, 35, 60, 6, 16, 192);
	private static final Projectile RANGED_PROJECTILE = new Projectile(1043, 90, 43, 35, 90, 6, 16, 192);

	@Override
	public void init() {
		npc.deathEndListener = (entity, killer, killHit) -> {
			List<Integer> rewardingPlayers = damagingPlayers.entrySet().stream()
				.filter(entry -> entry.getValue() >= 100)
				.map(Map.Entry::getKey)
				.toList();
			npc.getPosition().getRegion().players.forEach(p -> {
				if (rewardingPlayers.contains(p.getUserId())) {
					rewardPlayer(p);
				}
			});
			// Call onBossDeath to handle queued spawns
			DonationBossHandler.onBossDeath();
			npc.remove();
		};
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend)
			.preDamage(this::preHitDamage);
	}

	private void preHitDamage(Hit hit) {
		if (hit.attacker != null && hit.attacker.isPlayer() && hit.attacker.player.isOwner()) {
			return;
		}
		if (hit.damage > 1000) {
			hit.damage = 1000;
		}
	}

	private void rewardPlayer(Player player) {
		Item reward = table.rollItem();
		player.sendMessage(Color.RED.wrap("You have received " + reward.getDef().name + "!"));
		player.getInventory().addOrDrop(reward.getId(), reward.getAmount());
		int perkPointReward = VoteHandler.playerDonatedRecently(player) ? 8 : 4;
		player.perkPoints += perkPointReward;
		player.sendMessage(Color.RED.wrap("You've received " + perkPointReward + " perk points from the donation boss!"));
		if (reward.lootBroadcast != null)
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR,
				"<col=000000>" + player.getName(),
				" just received " + reward.getDef().descriptiveName + " from the donation boss!");
	}

	private void preHitDefend(Hit hit) {
		if (hit.maxDamage > 0 && hit.attacker != null && hit.attacker.player != null) {
			damagingPlayers.merge(hit.attacker.player.getUserId(), hit.maxDamage, Integer::sum);
		}
	}

	private void postDamage(Hit hit) {

	}

	@Override
	public void follow() {
		// Boss doesn't need to follow
	}

	@Override
	public boolean attack() {
		if (attackingWithMagic)
			magicAttack();
		else
			rangeAttack();
		return true;
	}

	@Override
	public void process() {
		// Additional processing if needed
	}

	private static int MAX_TARGETS = 55;

	private void magicAttack() {
		npc.face(target);
		AtomicInteger totalTargetsHit = new AtomicInteger();
		npc.localPlayers().forEach(p -> {
			if (totalTargetsHit.get() >= MAX_TARGETS)
				return;
			if (p.getPosition().isWithinDistance(npc.getPosition(), 8)) {
				int delay = MAGIC_PROJECTILE.send(npc, p);
				int maxDamage = info.max_damage;
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
					maxDamage /= 3;
				p.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(maxDamage).ignorePrayer().clientDelay(delay));
				p.graphics(157, 0, delay / 2);
				totalTargetsHit.getAndIncrement();
			}
		});
		attacks++;
		if (attacks >= 5) {
			attacks = 0;
			attackingWithMagic = false;
		}
	}

	private void rangeAttack() {
		npc.face(target);
		AtomicInteger totalTargetsHit = new AtomicInteger();
		npc.localPlayers().forEach(p -> {
			if (totalTargetsHit.get() >= MAX_TARGETS)
				return;
			if (p.getPosition().isWithinDistance(npc.getPosition(), 8)) {
				int delay = RANGED_PROJECTILE.send(npc, p);
				int maxDamage = info.max_damage;
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
					maxDamage /= 3;
				p.hit(new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).ignorePrayer().clientDelay((int) (delay * 1.5)));
				totalTargetsHit.getAndIncrement();
			}
		});
		attacks++;
		if (attacks >= 5) {
			attacks = 0;
			attackingWithMagic = true;
		}
	}

	private void healthDrainPool() {
		npc.forceText("DROWN IN MY POOLS!");
		List<Position> poolPositions = new ArrayList<>();
		World.startEvent(e -> {
			npc.localPlayers().forEach(p -> {
				poolPositions.add(p.getPosition());
				World.sendGraphics(1026, 0, 0, p.getPosition());
			});
			e.delay(1);
			for (int i = 0; i < 10; i++) {
				for (Position pos : poolPositions) {
					World.sendGraphics(1026, 0, 0, pos);
					e.delay(1);
					npc.localPlayers().forEach(p -> {
						if (p.getPosition().equals(pos)) {
							int damage = Random.get(10, 20);
							p.hit(new Hit(npc, AttackStyle.MAGIC).fixedDamage(damage).ignorePrayer().ignoreDefence());
							npc.hit(new Hit(HitType.HEAL).fixedDamage(damage));
						}
					});
				}
			}
		});
	}

	private void spawnMinions() {
		npc.forceText("HEAL ME!");
		attacksSinceHealer = 0;
		int amountToSpawn = Random.get(2, npc.localPlayers().size() / 2);
		while (amountToSpawn > 0) {
			Position spawnPosition = new Position(npc.getPosition().getX() + Random.get(-5, 5),
				npc.getPosition().getY() + Random.get(-5, 5),
				npc.getPosition().getZ());
			if (spawnPosition.distance(npc.getPosition()) < 2)
				continue;
			if (Tile.get(spawnPosition) != null && Tile.get(spawnPosition).clipping == 0)
				continue;
			NPC minion = new NPC(84);
			minion.spawn(spawnPosition);
			spawns.add(minion);
			minion.deathEndListener = (DeathListener.Simple) () -> spawns.remove(minion);
			minion.targetPlayer(npc.localPlayers().get(0), false); // target but don't attack
			minion.face(npc);
			minion.startEvent(e -> { // when attacked, this event will stop
				int healTicks = 4;
				while (!npc.getCombat().isDead()) {
					DumbRoute.step(minion, npc, 1);
					if (++healTicks >= 4 && DumbRoute.withinDistance(minion, npc, 1)) {
						healTicks = 0;
						npc.graphics(444, 250, 0);
						npc.incrementHp(Random.get(8, 25));
					}
					e.delay(1);
				}
			});
			amountToSpawn--;
		}
	}
}