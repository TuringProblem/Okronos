package io.ruin.model.activities.bosses.globalboss.globalbosses.stonepoltegeist;

import io.ruin.Server;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.VoteHandler;
import io.ruin.model.World;
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
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Broadcast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class StonePoltegeist extends NPCCombat {
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
	private static final Projectile MAGIC_PROJECTILE = new Projectile(1471, 90, 43, 35, 40, 6, 16, 192);
	private static final Projectile RANGED_PROJECTILE = new Projectile(1329, 90, 43, 35, 40, 6, 16, 192);
	private static final Projectile BOULDER_DROP_PROJECTILE = new Projectile(856, 150, 0, 0, 135, 0, 0, 127);

	private static final int TORNADO = 1608;
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
		player.globalBossKills.increment(player);
		player.sendMessage(Color.RED.wrap("Your reward has been dropped to your feet!"));
		int rolls = Random.get(3, 6);
		int perkPointReward = VoteHandler.playerVotedRecently(player) ? 2 : 1;
		player.perkPoints += perkPointReward;
		player.sendMessage(Color.RED.wrap("You've received " + perkPointReward + " perk points from the Stone Poltegeist!"));
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
		if (hit.damage > 0 && hit.attacker != null) {
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
			tornadoAttack();
			return true;
		} else if (Random.get(8) == 0) {
			aoeMeteorFall();
			return true;
		} else {
			if (attackingWithMagic)
				magicAttack();
			else rangedAttack();
		}
		return true;
	}

	@Override
	public void process() {

	}

	private void tornadoAttack() {
		Position tornadoOne = new Position(npc.getPosition().getX() + 1, npc.getPosition().getY() + 1, npc.getPosition().getZ());
		Position tornadoTwo = new Position(npc.getPosition().getX() + 1, npc.getPosition().getY() - 1, npc.getPosition().getZ());
		Position tornadoThree = new Position(npc.getPosition().getX() - 1, npc.getPosition().getY() + 1, npc.getPosition().getZ());
		Position tornadoFour = new Position(npc.getPosition().getX() - 1, npc.getPosition().getY() - 1, npc.getPosition().getZ());
		NPC tornadoOneNPC = new NPC(4593).spawn(tornadoOne);
		NPC tornadoTwoNPC = new NPC(4593).spawn(tornadoTwo);
		NPC tornadoThreeNPC = new NPC(4593).spawn(tornadoThree);
		NPC tornadoFourNPC = new NPC(4593).spawn(tornadoFour);
		List<NPC> tornados = new ArrayList<>();
		tornados.add(0, tornadoOneNPC);
		tornados.add(1, tornadoTwoNPC);
		tornados.add(2, tornadoThreeNPC);
		tornados.add(3, tornadoFourNPC);
		World.startEvent(e -> {
			for (int i = 0; i < 8; i++) {
				tornados.forEach(nado -> {
					nado.step(Random.get(-1, 1), Random.get(-1, 1), StepType.WALK);
				});
				tornadoOneNPC.graphics(TORNADO);
				tornadoTwoNPC.graphics(TORNADO);
				tornadoThreeNPC.graphics(TORNADO);
				tornadoFourNPC.graphics(TORNADO);
				npc.localPlayers().forEach(p -> {
					tornados.forEach(pos -> {
						if (pos.getPosition().distance(p.getPosition()) < 1)
							p.hit(new Hit(HitType.DAMAGE).randDamage(30, 50));
					});
				});
				e.delay(1);
				if (i == 7) {
					tornados.forEach(nado -> {
						if (nado != null)
							nado.remove();
					});
				}


			}
		});
	}

	private int getTicks(int delay) {
		return Math.max(1, (delay * 16) / Server.tickMs());
	}

	private void aoeMeteorFall() {
		npc.animate(1842);
		Player target = Random.get(npc.localPlayers());
		npc.face(target);
		List<Position> meteorPositions = new ArrayList<>();
		for (int x = -2; x < 2; x++) {
			for (int y = -2; y < 2; y++) {
				Position pos = new Position(target.getPosition().getX() + x, target.getPosition().getY() + y, target.getHeight());
				meteorPositions.add(pos);
			}
		}
		World.startEvent(e -> {
			AtomicInteger delay = new AtomicInteger();
			meteorPositions.forEach(pos -> {
				delay.set(BOULDER_DROP_PROJECTILE.send(npc.getPosition(), pos));
			});
			e.delay(getTicks(delay.get() + 3));
			npc.localPlayers().forEach(p -> {
				meteorPositions.forEach(pos -> {
					World.sendGraphics(305, 0, 0, pos);
					if (p.getPosition().distance(pos) < 2)
						p.hit(new Hit(HitType.DAMAGE).randDamage(22));
				});
			});
		});
	}

	private void magicAttack() {
		npc.face(target);
		npc.localPlayers().forEach(p -> {
			if (p.getPosition().isWithinDistance(npc.getPosition(), 8)) {
				int delay = MAGIC_PROJECTILE.send(npc, p);
				npc.animate(1840);
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

	private void rangedAttack() {
		npc.face(target);
		npc.localPlayers().forEach(p -> {
			if (p.getPosition().isWithinDistance(npc.getPosition(), 8)) {
				int delay = RANGED_PROJECTILE.send(npc, p);
				npc.animate(1844);
				int maxDamage = info.max_damage;
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
					maxDamage /= 3;
				p.hit(new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).ignorePrayer().clientDelay(delay));
				p.graphics(1330, 0, delay);
			}
		});
		attacks++;
		if (attacks >= 5) {
			attacks = 0;
			attackingWithMagic = true;
		}
	}
}