package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.VoteHandler;
import io.ruin.model.World;
import io.ruin.model.activities.VoteBossHandler;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Broadcast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteBoss extends NPCCombat {


	private static final Projectile MAGIC_PROJECTILE = new Projectile(2167, 90, 43, 35, 60, 6, 16, 192);
	private static final Projectile RANGED_PROJECTILE = new Projectile(1921, 90, 43, 35, 90, 6, 16, 192);
	private static final Projectile METEOR_DROP_PROJECTILE = new Projectile(1227, 150, 0, 0, 135, 0, 0, 0);

	int attacks = 0;
	boolean attackingWithMagic = false;
	int activeMeteors = 0;
	boolean nadosActive = false;

	int lastGraphicUpdate = 3;
	HashMap<NPC, Player> tornados = new HashMap<>();

	@Override
	public void init() {
		npc.deathEndListener = (entity, killer, killHit) -> {
			VoteBossHandler.boss = null;
			npc.getPosition().getRegion().players.forEach(p -> {
				if (damagingPlayers.contains(p)) {
					rewardPlayer(p);
				}
			});
			npc.remove();
		};
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend);
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		int npcHealthPercentage = (int) ((npc.getHp() / npc.getMaxHp()) * 100);
		if (Random.get(8) == 0 && activeMeteors < 1 && npcHealthPercentage < 50) {
			fallingMeteors(2);
			return true;
		} else if (Random.get(5) == 0) {
			shadowSpecial();
			return true;
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
		for (Map.Entry<NPC, Player> entry : tornados.entrySet()) {
			NPC nado = entry.getKey();
			Player p = entry.getValue();
			nado.stepAbs(p.getPosition().getX(), p.getPosition().getY(), StepType.WALK);
		}
		if (lastGraphicUpdate <= 0) {
			for (Map.Entry<NPC, Player> entry : tornados.entrySet()) {
				NPC nado = entry.getKey();
				nado.graphics(1842);
			}
			lastGraphicUpdate = 3;
		}

		lastGraphicUpdate--;
	}

	private static final LootTable table = new LootTable().addTable(1,
		new LootItem(ItemID.SHINY_MINERALS, 15, 25, 60),
		new LootItem(ItemID.GLISTENING_MINERALS, 2, 5, 10),
		new LootItem(620, 2, 6, 100), //Vote point ticket
		new LootItem(30459, 1, 2, 100), //Double drop
		new LootItem(30460, 2, 4, 100), //Double exp
		new LootItem(607, 1, 2, 100), //Reroll dailies
		new LootItem(608, 1, 1, 100), //10% drop rate
		new LootItem(30456, 1, 2, 100), //Damage boost
		new LootItem(30457, 1, 2, 100), // Damage reduction
		new LootItem(30453, 1, 2, 100), //Prayer drain reduction
		new LootItem(7478, 3, 7, 100), //Instance token
		new LootItem(7968, 3, 6, 100), // Perk task skip
		new LootItem(30629, 3, 6, 100), // Perk task skip
		new LootItem(30458, 3, 6, 100), //Slayer task skip
		new LootItem(30461, 1, 2, 5).broadcast(Broadcast.GLOBAL), //Donator mystery box
		new LootItem(30464, 1, 1, 1) //$5 Bond

	);

	private void rewardPlayer(Player player) {
		player.voteBossKills.increment(player);
		player.sendMessage(Color.RED.wrap("Your reward has been dropped to your feet!"));
		int perkPointReward = VoteHandler.playerVotedRecently(player) ? 2 : 1;
		player.perkPoints += perkPointReward;
		player.sendMessage(Color.RED.wrap("You've received " + perkPointReward + " perk points from the vote boss!"));
		Item reward = table.rollItem();
		new GroundItem(reward).owner(player).position(player.getPosition()).spawn();
		if (reward.lootBroadcast != null)
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " just received " + reward.getDef().descriptiveName + " from the vote boss!");
	}

	List<Player> damagingPlayers = new ArrayList<>();

	private void preHitDefend(Hit hit) {
		if (hit.damage > 50)
			hit.damage = 50;
	}

	private void postDamage(Hit hit) {
		if (hit.damage > 0 && hit.attacker != null) {
			if (!damagingPlayers.contains(hit.attacker.player))
				damagingPlayers.add(hit.attacker.player);
		}
	}


	private void fallingMeteors(int delay) {
		npc.forceText("RAAAAH!");
		npc.animate(8028);
		Bounds lair = new Bounds(npc.getPosition().getX() - 8,
			npc.getPosition().getY() - 8, npc.getPosition().getX() + 8, npc.getPosition().getY() + 8, npc.getPosition().getZ());
		World.startEvent(event -> {
			event.setCancelCondition(this::isDead);
			for (int j = 0; j < 15; j++) {
				int projDelay = 0;
				List<Position> impactPositions = new ArrayList<>();
				for (int i = 0; i < 8; i++) {
					Position position;
					if (i == 0 && target != null)
						position = target.getPosition().copy();
					else
						position = lair.randomPosition();
					Position src = Random.get() < 0.5 ? position.relative(1, 0) : position.relative(0, 1);
					projDelay = METEOR_DROP_PROJECTILE.send(src, position);
					impactPositions.add(position);
					activeMeteors++;
				}
				event.delay(projDelay / 25);
				activeMeteors--;
				impactPositions.forEach(position -> {
					World.sendGraphics(157, 0, 0, position);
					npc.localPlayers().forEach(p -> {
						int distance = p.getPosition().distance(position);
						if (distance <= 1) {
							p.hit(new Hit(npc).randDamage(distance == 0 ? 20 : 10));

						}
					});
				});
				impactPositions.clear();
				event.delay(3);
				if (j == 14)
					activeMeteors = 0;
			}
		});
	}

	private void tornadoAttack() {
		nadosActive = true;
		npc.animate(8026);
		tornados.clear();
		npc.localPlayers().forEach(p -> {
			NPC nado = new NPC(4593).spawn(new Position(p.getPosition().getX() + 2, p.getPosition().getY() + 2, p.getPosition().getZ()));
			tornados.put(nado, p);
		});
		World.startEvent(e -> {
			for (int i = 0; i < 16; i++) {
				npc.localPlayers().forEach(p -> {
					for (Map.Entry<NPC, Player> entry : tornados.entrySet()) {
						NPC nado = entry.getKey();
						if (nado.getPosition().distance(p.getPosition()) < 1)
							p.hit(new Hit(HitType.DAMAGE).randDamage(30, 50));
					}
				});
				e.delay(1);
				if (i == 15) {
					nadosActive = false;
					for (Map.Entry<NPC, Player> entry : tornados.entrySet()) {
						NPC nado = entry.getKey();
						if (nado != null)
							nado.remove();
					}
				}


			}
		});
	}

	private void magicAttack() {
		npc.face(target);
		npc.localPlayers().forEach(p -> {
			if (p.getPosition().isWithinDistance(npc.getPosition(), 8)) {
				int delay = MAGIC_PROJECTILE.send(npc, p);
				npc.animate(8025);
				int maxDamage = info.max_damage;
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
					maxDamage /= 3;
				p.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(maxDamage).ignorePrayer().clientDelay(delay));
				p.graphics(2168, 0, delay / 2);
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
				npc.animate(8025);
				int maxDamage = info.max_damage;
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
					maxDamage /= 3;
				p.hit(new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).ignorePrayer().clientDelay((int) (delay * 1.5)));
			}
		});
		attacks++;
		if (attacks >= 5) {
			attacks = 0;
			attackingWithMagic = true;
		}
	}

	private void shadowSpecial() {
		npc.animate(8027);
		List<Position> impactPositions = new ArrayList<>();
		npc.localPlayers().forEach(p -> impactPositions.add(p.getPosition().copy()));
		impactPositions.forEach(pos -> {
			World.sendGraphics(2073, 0, 0, pos);
		});
		World.startEvent(e -> {
			e.delay(4);
			impactPositions.forEach(pos -> {
				World.sendGraphics(2072, 0, 0, pos);
				npc.localPlayers().forEach(p -> {
					if (p.getPosition().distance(pos) < 1) {
						p.hit(new Hit(HitType.DAMAGE).randDamage(40, 60));
						p.getPrayer().deactivateAll();
						p.sendMessage("Your prayers have been deactivated!");
					}
				});
			});
		});
	}
}