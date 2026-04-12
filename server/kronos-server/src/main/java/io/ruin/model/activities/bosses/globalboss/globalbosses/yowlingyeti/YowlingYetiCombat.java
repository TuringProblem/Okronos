package io.ruin.model.activities.bosses.globalboss.globalbosses.yowlingyeti;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.Tile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.route.routes.DumbRoute;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Broadcast;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class YowlingYetiCombat extends NPCCombat {
	private static final LootTable table = new LootTable().addTable(1,
		new LootItem(ItemID.RANARR_SEED, 15, 150),
		new LootItem(ItemID.SNAPDRAGON_SEED, 10, 150),
		new LootItem(ItemID.TORSTOL_SEED, 8, 150),
		new LootItem(ItemID.STEEL_BAR + 1, 100, 150),
		new LootItem(ItemID.ADAMANTITE_BAR + 1, 25, 150),
		new LootItem(ItemID.MANTA_RAY + 1, 50, 75, 150),
		new LootItem(ItemID.BATTLESTAFF + 1, 10, 25, 150),
		new LootItem(ItemID.GRIMY_RANARR_WEED + 1, 10, 25, 150),
		new LootItem(ItemID.GRIMY_SNAPDRAGON + 1, 10, 25, 150),
		new LootItem(ItemID.GRIMY_TORSTOL + 1, 10, 25, 150),
		new LootItem(ItemID.YEW_LOGS + 1, 50, 125, 150),

		new LootItem(ItemID.DRAGON_BONES + 1, 20, 50, 60),
		new LootItem(ItemID.RUNITE_BAR + 1, 5, 10, 60),
		new LootItem(ItemID.RUNITE_ORE + 1, 5, 17, 60),
		new LootItem(ItemID.MAGIC_LOGS + 1, 30, 50, 60),
		new LootItem(ItemID.ANGLERFISH + 1, 30, 50, 60),
		new LootItem(ItemID.SUPER_RESTORE4 + 1, 30, 50, 60),
		new LootItem(ItemID.PRAYER_POTION4 + 1, 30, 50, 60),
		new LootItem(ItemID.SARADOMIN_BREW4 + 1, 10, 20, 60),
		new LootItem(ItemID.INSTANCE_TOKEN, 1, 2, 60),
		new LootItem(ItemID.ROBINS_DIARY, 1, 2, 60),
		new LootItem(ItemID.PERK_TASK_SKIP_SCROLL, 1, 2, 60),
		new LootItem(ItemID.HIGH_TIER_MBOX, 1, 2, 60),

		new LootItem(ItemID.UNCUT_ONYX, 1, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.DRAGON_HARPOON, 1, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.DONATOR_MYSTERY_BOX, 1, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.FIVE_BOND, 1, 1, 1).broadcast(Broadcast.GLOBAL)

	);
	List<Player> damagingPlayers = new ArrayList<>();

	private static final Projectile MAGIC_PROJECTILE = new Projectile(395, 90, 43, 35, 40, 6, 16, 192);
	private static final Projectile RANGED_PROJECTILE = new Projectile(1324, 90, 43, 35, 40, 6, 16, 192);
	private static final int EXPLOSIION_GFX = 659;

	List<YetiSpawn> spawns = new ArrayList<>();

	int attacksSinceHealer = 0;

	int attacks = 0;
	boolean attackingWithMagic = false;

	@Override
	public void init() {
		npc.deathEndListener = (entity, killer, killHit) -> {
			npc.localPlayers().forEach(p -> {
				if (damagingPlayers.contains(p)) {
					rewardPlayer(p);
				}
			});
			npc.remove();
		};
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend);
	}

	private void rewardPlayer(Player player) {
		player.sendMessage(Color.RED.wrap("Your reward has been dropped to your feet!"));
		player.globalBossKills.increment(player);
		int rolls = Random.get(3, 6);
		for (int i = 0; i < rolls; i++) {
			Item reward = table.rollItem();
			new GroundItem(reward).owner(player).position(player.getPosition()).spawn();
			if (reward.lootBroadcast != null)
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " just received " + reward.getDef().descriptiveName + " from a Blood Reaper!");
		}
	}

	private void preHitDefend(Hit hit) {
		if (hit.damage > 50)
			hit.damage = 50;
	}

	private void postDamage(Hit hit) {
		if (hit.damage > 0) {
			if (!damagingPlayers.contains(hit.attacker.player))
				damagingPlayers.add(hit.attacker.player);
		}
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		attacksSinceHealer++;
		if (Random.get(8) == 0 && attacksSinceHealer >= 10) {
			spawnMinions();
			return true;
		} else if (Random.get(5) == 0) {
			Optional<Player> targetOptional = npc.localPlayers().stream()
				.filter(p -> p.getPosition().distance(npc.getPosition()) < 8)
				.findFirst();

			if (targetOptional.isPresent()) {
				Entity target = targetOptional.get();
				slamAttack(target);
			} else {
				return true;
			}
		} else {
			if (attackingWithMagic)
				magicAttack();
			else
				rangeAttack();
		}
		return true;
	}

	@Override
	public void process() {

	}

	private void spawnMinions() {
		attacksSinceHealer = 0;
		npc.animate(5723);
		int amountToSpawn = Random.get(2, npc.localPlayers().size() / 2);
		while (amountToSpawn > 0) {
			Position spawnPosition = new Position(npc.getPosition().getX() + Random.get(-5, 5), npc.getPosition().getY() + Random.get(-5, 5), npc.getPosition().getZ());
			if (spawnPosition.distance(npc.getPosition()) < 2)
				continue;
			if (Tile.get(spawnPosition) != null && Tile.get(spawnPosition).clipping == 0)
				continue;
			YetiSpawn minion = new YetiSpawn(853);
			minion.spawn(spawnPosition);
			spawns.add(minion);
			minion.deathEndListener = (DeathListener.Simple) () -> spawns.remove(minion);
			minion.targetPlayer(npc.localPlayers().get(0), false); //target but don't attack (needed so they don't check bounds when attacking!)
			minion.face(npc);
			minion.startEvent(e -> { //when attacked, this event will stop.
				int healTicks = 4;
				while (!npc.getCombat().isDead()) {
					DumbRoute.step(minion, npc, 1);
					if (++healTicks >= 4 && DumbRoute.withinDistance(minion, npc, 1)) {
						healTicks = 0;
						minion.animate(5723);
						npc.graphics(444, 250, 0);
						npc.incrementHp(Random.get(8, 25));

					}
					e.delay(1);
				}
			});
			amountToSpawn--;
		}
	}

	private void slamAttack(Entity target) {
		npc.animate(5725);
		npc.face(target);
		Direction direction = Direction.getDirection(npc.getPosition(), target.getPosition());
		Position firstExplositionPosition = npc.getPosition().copy().translate(direction.deltaX * 2, direction.deltaY * 2, 0);
		World.startEvent(e -> {
			for (int i = 0; i < 5; i++) {
				e.delay(1);
				World.sendGraphics(EXPLOSIION_GFX, 0, 0, firstExplositionPosition);
				npc.localPlayers().forEach(p -> {
					if (p.getPosition().distance(firstExplositionPosition) <= 2)
						p.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(50).ignorePrayer());
				});
				firstExplositionPosition.set(firstExplositionPosition.getX() + direction.deltaX, firstExplositionPosition.getY() + direction.deltaY, 0);
			}
		});
	}


	private void magicAttack() {
		npc.face(target);
		npc.localPlayers().forEach(p -> {
			if (p.getPosition().isWithinDistance(npc.getPosition(), 8)) {
				int delay = MAGIC_PROJECTILE.send(npc, p);
				npc.animate(5724);
				int maxDamage = info.max_damage;
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
					maxDamage /= 3;
				p.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(maxDamage).ignorePrayer().clientDelay(delay));
				p.graphics(160, 0, delay / 2);
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
		npc.localPlayers().forEach(p -> {
			if (p.getPosition().isWithinDistance(npc.getPosition(), 8)) {
				int delay = RANGED_PROJECTILE.send(npc, p);
				npc.animate(5724);
				int maxDamage = info.max_damage;
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
					maxDamage /= 3;
				p.hit(new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).ignorePrayer().clientDelay(delay));
				p.graphics(1325, 0, delay / 2);
			}
		});
		attacks++;
		if (attacks >= 5) {
			attacks = 0;
			attackingWithMagic = true;
		}
	}
}