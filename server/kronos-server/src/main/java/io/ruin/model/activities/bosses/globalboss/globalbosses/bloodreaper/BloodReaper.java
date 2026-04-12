package io.ruin.model.activities.bosses.globalboss.globalbosses.bloodreaper;

import io.ruin.Server;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.VoteHandler;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.Tile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Broadcast;

import java.util.ArrayList;
import java.util.List;

public class BloodReaper extends NPCCombat {
	private static final LootTable table = new LootTable().addTable(1,
		new LootItem(ItemID.RANARR_SEED, 15, 150),
		new LootItem(ItemID.SHINY_MINERALS, 15, 25, 60),
		new LootItem(ItemID.GLISTENING_MINERALS, 2, 5, 10),
		new LootItem(ItemID.SNAPDRAGON_SEED, 10, 150),
		new LootItem(ItemID.TORSTOL_SEED, 8, 150),
		new LootItem(ItemID.STEEL_BAR + 1, 175, 150),
		new LootItem(ItemID.ADAMANTITE_BAR + 1, 50, 150),
		new LootItem(ItemID.RUNITE_BAR + 1, 100, 150),
		new LootItem(ItemID.MANTA_RAY + 1, 100, 75, 150),
		new LootItem(ItemID.BATTLESTAFF + 1, 20, 25, 150),
		new LootItem(ItemID.GRIMY_RANARR_WEED + 1, 15, 25, 150),
		new LootItem(ItemID.GRIMY_SNAPDRAGON + 1, 15, 25, 150),
		new LootItem(ItemID.GRIMY_TORSTOL + 1, 15, 25, 150),
		new LootItem(ItemID.YEW_LOGS + 1, 90, 125, 150),

		new LootItem(ItemID.DRAGON_BONES + 1, 50, 70, 60),
		new LootItem(ItemID.RUNITE_ORE + 1, 30, 50, 60),
		new LootItem(ItemID.MAGIC_LOGS + 1, 50, 100, 60),
		new LootItem(ItemID.ANGLERFISH + 1, 50, 100, 60),
		new LootItem(ItemID.SUPER_RESTORE4 + 1, 20, 40, 60),
		new LootItem(ItemID.PRAYER_POTION4 + 1, 30, 50, 60),
		new LootItem(ItemID.SARADOMIN_BREW4 + 1, 20, 25, 60),
		new LootItem(ItemID.INSTANCE_TOKEN, 2, 4, 60),
		new LootItem(ItemID.ROBINS_DIARY, 1, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.PERK_TASK_SKIP_SCROLL, 1, 2, 60),
		new LootItem(ItemID.SLAYER_TASK_PICK_SCROLL, 1, 2, 60),
		new LootItem(ItemID.HIGH_TIER_MBOX, 1, 2, 60),

		new LootItem(ItemID.UNCUT_ONYX, 1, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.DRAGON_HARPOON, 1, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.DONATOR_MYSTERY_BOX, 1, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.FIVE_BOND, 1, 1, 1).broadcast(Broadcast.GLOBAL)

	);
	List<Player> damagingPlayers = new ArrayList<>();
	private static final Projectile MAGIC_PROJECTILE = new Projectile(1700, 90, 43, 35, 40, 6, 16, 192);
	private static final Projectile BOMB_PROJECTILE = new Projectile(1495, 90, 0, 35, 40, 6, 16, 192);


	int attacks = 0;
	boolean attackingWithMagic = false;
	boolean flamesActive = false;
	boolean bombsActive = false;
	int bombsTimer = 0;

	List<GameObject> bombs = new ArrayList<>();

	@Override
	public void init() {
		npc.deathEndListener = (entity, killer, killHit) -> {
			damagingPlayers.forEach(p -> {
				if (p != null) {
					rewardPlayer(p);
				}
			});
			npc.remove();
		};
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend);
	}

	private void rewardPlayer(Player player) {
		player.globalBossKills.increment(player);
		player.sendMessage(Color.RED.wrap("Your reward has been dropped to your feet!"));
		int rolls = Random.get(3, 6);
		int perkPointReward = VoteHandler.playerVotedRecently(player) ? 2 : 1;
		player.perkPoints += perkPointReward;
		player.sendMessage(Color.RED.wrap("You've received " + perkPointReward + " perk points from the Blood Reaper!"));
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
		if (hit.attacker != null && hit.damage > 0) {
			if (!damagingPlayers.contains(hit.attacker.player))
				damagingPlayers.add(hit.attacker.player);
		}
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		if (Random.get(8) == 0) {
			npc.localPlayers().forEach(p -> {
				if (p.getPosition().isWithinDistance(npc.getPosition(), 8)) {
					p.getPrayer().deactivateAll();
					p.sendMessage(Color.RED.wrap("Your prayers have been disabled!"));
				}
			});
		}

		if (Random.get(6) == 0 && !flamesActive) {
			randomFlames();
			return true;
		} else if (Random.get(6) == 0 && !bombsActive) {
			spawnBombs();
			return true;
		} else {
			magicAttack();
		}
		return true;
	}

	@Override
	public void process() {
		if (bombsActive) {
			for (int i = bombs.size() - 1; i >= 0; i--) {
				GameObject bomb = bombs.get(i);
				if (bomb == null || bomb.isRemoved()) {
					bombs.remove(i);
					continue;
				}
				int finalI = i;
				npc.localPlayers().forEach(p -> {
					if (p.getPosition().distance(bomb.getPosition()) < 2) {
						bomb.graphics(157, 0, 0);
						p.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(20).ignorePrayer());
						bomb.remove();
						bombs.remove(finalI);
					}
				});
			}
			if (bombsTimer-- <= 0) {
				for (int i = bombs.size() - 1; i >= 0; i--) {
					int finalI = i;
					npc.localPlayers().forEach(p -> {
						bombs.get(finalI).graphics(157, 0, 0);
						if (p.getPosition().distance(bombs.get(finalI).getPosition()) < 2)
							p.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(20).ignorePrayer());

						bombs.get(finalI).remove();
						bombs.remove(finalI);
					});
				}
			}
		}
		if (bombs.size() < 1)
			bombsActive = false;

	}

	private int getTicks(int delay) {
		return Math.max(1, (delay * 16) / Server.tickMs());
	}

	private void spawnBombs() {
		npc.animate(8375);
		bombs.clear();
		List<Position> bombPositions = new ArrayList<>();
		for (int x = -8; x < 8; x++) {
			for (int y = -8; y < 8; y++) {
				Position pos = new Position(npc.getPosition().getX() + x, npc.getPosition().getY() + y, npc.getPosition().getZ());
				if (Tile.get(pos) != null && Tile.get(pos).clipping != 0)
					bombPositions.add(pos);
			}
		}
		int bombsToSpawn = Random.get(2, npc.localPlayers().size() / 2);

		while (bombsToSpawn-- > 0) {
			if (bombPositions.isEmpty())
				break;

			World.startEvent(e -> {
				Position pos = Random.get(bombPositions);
				bombPositions.remove(pos);
				int delay = BOMB_PROJECTILE.send(npc.getPosition(), pos);
				e.delay(getTicks(delay));
				GameObject bomb = new GameObject(32378, pos, 10, 0).spawn();
				bombs.add(bomb);
			});
		}
		World.startEvent(e -> {
			e.delay(2);
			bombsActive = true;
			bombsTimer = 45;
		});
	}

	private void randomFlames() {
		npc.animate(8376);
		flamesActive = true;
		int flameCount = Random.get(3, 9);
		if (npc.localPlayers().size() < 1) return;
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.localPlayers().isEmpty());
			for (int i = 0; i < flameCount; i++) {
				Player target = Random.get(npc.localPlayers());
				List<Position> flamePositions = new ArrayList<>();
				for (int x = -2; x < 2; x++) {
					for (int y = -2; y < 2; y++) {
						flamePositions.add(new Position(target.getPosition().getX() + x, target.getPosition().getY() + y, npc.getPosition().getZ()));
					}
				}
				for (int j = 0; j < 10; j++) {
					flamePositions.forEach(pos -> {
						World.sendGraphics(78, 0, 0, pos);
						npc.localPlayers().forEach(p -> {
							if (p.getPosition().equals(pos)) {
								p.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(8).ignorePrayer());
							}
						});
					});
					e.delay(1);
				}
			}
		});
	}

	private void magicAttack() {
		npc.face(target);
		npc.animate(8374);
		npc.localPlayers().forEach(p -> {
			if (p.getPosition().isWithinDistance(npc.getPosition(), 8)) {
				int delay = MAGIC_PROJECTILE.send(npc, p);
				int maxDamage = info.max_damage;
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
					maxDamage /= 3;
				p.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(maxDamage).ignorePrayer().clientDelay(delay));
			}
		});
		attacks++;
		if (attacks >= 5) {
			attacks = 0;
			attackingWithMagic = false;
		}
	}
}