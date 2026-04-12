package io.ruin.model.activities.tempevents.summerevent;

import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SummerBoss extends NPCCombat {
	private static final Projectile RANGED_PROJECTILE =
			new Projectile(2033, 90, 35, 17, 40, 6, 16, 192).regionBased();
	private static final Projectile MAGIC_PROJECTILE =
			new Projectile(2065, 90, 35, 17, 40, 6, 16, 192).regionBased();
	private List<Player> damagingPlayers = new ArrayList<>();

	public static final LootTable table = new LootTable().addTable(1,
			new LootItem(33010, 1, 1, 1).broadcast(Broadcast.GLOBAL),
			new LootItem(33007, 1, 1, 1).broadcast(Broadcast.GLOBAL),
			new LootItem(ItemID.DRUIDIC_WREATH, 1, 1).broadcast(Broadcast.GLOBAL),
			new LootItem(ItemID.SHINY_MINERALS, 50, 150, 6),
			new LootItem(ItemID.GLISTENING_MINERALS, 50, 120, 4),
			new LootItem(ItemID.INNOVATIVE_ENHANCER, 5, 10, 4),
			new LootItem(ItemID.MODERN_ENHANCER, 5, 25, 5),
			new LootItem(ItemID.PERK_POINT_SCROLL, 5, 10, 5),
			new LootItem(ItemID.DOUBLE_DROP_SCROLL, 4, 6, 5),
			new LootItem(ItemID.DOUBLE_EXP_SCROLL, 4, 6, 9),
			new LootItem(ItemID.DAMAGE_BOOST_SCROLL, 2, 3, 5),
			new LootItem(ItemID.DAMAGE_REDUCTION_SCROLL, 2, 3, 8),
			new LootItem(ItemID.SIPHON_DEAD_SIGIL, 1, 1, 8),
			new LootItem(ItemID.FREEZE_CHANCE_SIGIL, 1, 1, 8),
			new LootItem(ItemID.AOE_SWIPE_SIGIL, 1, 1, 8),
			new LootItem(ItemID.VENOM_TIPPED_SIGIL, 1, 1, 8),
			new LootItem(ItemID.SPECIAL_SAVER_SIGIL, 1, 1, 8),
			new LootItem(ItemID.RESPECT_DEAD_SIGIL, 1, 1, 8),
			new LootItem(ItemID.DAMAGE_FOR_HIRE_HIGH_SIGIL, 1, 1, 4),
			new LootItem(ItemID.HEALTH_SIPHON_SIGIL, 1, 1, 3),
			new LootItem(ItemID.CRITICAL_HIT_SIGIL, 1, 1, 3),
			new LootItem(ItemID.ARMOUR_BREAK_SIGIL, 1, 1, 3),
			new LootItem(ItemID.SLAYER_TASK_PICK_SCROLL, 3, 6, 7),
			new LootItem(608, 3, 6, 4),
			new LootItem(ItemID.SLAYER_SKIP_SCROLL, 5, 10, 8),
			new LootItem(ItemID.DONATOR_MYSTERY_BOX, 1, 1, 2).broadcast(Broadcast.GLOBAL),
			new LootItem(28655, 1, 1, 2).broadcast(Broadcast.GLOBAL),
			new LootItem(24539, 1, 1, 2).broadcast(Broadcast.GLOBAL),
			new LootItem(4810, 1, 1, 1).broadcast(Broadcast.GLOBAL),
			new LootItem(2528, 1, 1, 1).broadcast(Broadcast.GLOBAL));

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDamage(this::preDamage);
		npc.deathEndListener = (entity, killer, killHit) -> {
			npc.getPosition().getRegion().players.forEach(p -> {
				if (damagingPlayers.contains(p)) {
					rewardPlayer(p);
				}
			});
			npc.remove();
			SummerEvent.newEventStart();
		};
	}

	private void preDamage(Hit hit) {
		if (hit.damage > 1000)
			hit.damage = 1000;
	}


	private void postDamage(Hit hit) {
		if (hit.damage > 0 && hit.attacker != null) {
			if (!damagingPlayers.contains(hit.attacker.player))
				damagingPlayers.add(hit.attacker.player);
		}
	}

	private void rewardPlayer(Player player) {
		Item reward = table.rollItem();
		player.sendMessage(Color.RED.wrap("You have received " + reward.getDef().name + "!"));
		player.getInventory().addOrDrop(reward.getId(), reward.getAmount());

		if (reward.lootBroadcast != null)
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR,
					"<col=000000>" + player.getName(),
					" just received " + reward.getDef().descriptiveName + " from the " + npc.getDef().name + "!");
		player.summerPoints += Random.get(10, 20);
		player.sendMessage(
				"You now have <col=000000><shad=29F1FE>" + player.summerPoints + " Summer points<col=000000></shad>.");
	}

	@Override
	public void follow() {

	}

	int attacks = 0;
	int attackType = 0;

	@Override
	public boolean attack() {
		if (attackType == 0)
			magicAttack();
		else
			rangedAttack();
		if (attacks++ >= 5) {
			attacks = 0;
			if (attackType == 0)
				attackType = 1;
			else
				attackType = 0;
		}
		return true;
	}

	@Override
	public void process() {

	}

	@Override
	public boolean allowPj(Entity attacker) {
		return true;
	}

	private void magicAttack() {
		npc.animate(11530);
		AtomicInteger maxDamage = new AtomicInteger(62);
		npc.getPosition().getRegion().players.forEach(p -> {
			int delay = MAGIC_PROJECTILE.send(npc, p);
			World.startEvent(e -> {
				e.delay(World.getTicks(delay) + 1);
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
					maxDamage.set(9);
				p.hit(new Hit(npc).randDamage(maxDamage.get()));
			});
		});
	}

	private void rangedAttack() {
		npc.animate(11535);
		AtomicInteger maxDamage = new AtomicInteger(57);
		npc.getPosition().getRegion().players.forEach(p -> {
			int delay = RANGED_PROJECTILE.send(npc, p);
			World.startEvent(e -> {
				e.delay(World.getTicks(delay) + 1);
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
					maxDamage.set(5);
				p.hit(new Hit(npc).randDamage(maxDamage.get()));
			});
		});
	}
}
