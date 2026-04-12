package io.ruin.model.activities.bosses.hueycoatl;

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
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.model.stat.StatType;

import java.util.ArrayList;
import java.util.List;

public class Hueycoatl extends NPCCombat {
	int phase = 0;


	private static final Projectile MELEE_PROJECTILE = new Projectile(1341, 90, 43, 30, 150, 0, 16, 192);
	private static final Projectile RANGED_PROJECTILE = new Projectile(1343, 90, 43, 30, 150, 0, 16, 192);
	private static final Projectile MAGIC_PROJECTILE = new Projectile(1345, 90, 43, 30, 150, 0, 16, 192);
	List<Position> cavePhaseTiles = new ArrayList<>();

	int magicPillarFill = 0;
	int rangedPillarFill = 0;
	int meleePillarFill = 0;

	NPC tail = null;

	Position tailSpawnPos;

	@Override
	public void init() {
		//TODO: Spawn boss at top, spawn hueycoatl bodys in all the caves, spawn tail blocking way up to boss, gotta kill all caves to unblock tail

		//TODO: add cavePhaseTiles
		tailSpawnPos = new Position(0, 0, 0);//TODO:

		npc.hitListener = new HitListener().preDamage(this::preDamage).postDamage(this::postDamage);
	}

	boolean tailActive = false;

	private void postDamage(Hit hit) {
		if (phase > 0 && tail == null) {
			int hpPercent = npc.getHp() * 100 / npc.getMaxHp();
			if (hpPercent < 50) {
				tail = new NPC(0).spawn(tailSpawnPos);
				tailActive = true;
				tail.deathStartListener = (entity, killer, killHit) -> {
					tailActive = false;
				};
			}
		}
	}

	private void preDamage(Hit hit) {
		if (pillarsFilled)
			hit.boostDamage(1.5);
		if (tailActive) {
			hit.block();
			if (hit.attacker != null && hit.attacker.isPlayer()) {
				hit.attacker.player.sendFilteredMessage("The Hueycoatl's tail blocks all incoming damage!");
			}
		}
	}


	private void floorIndicatorSpecial() {
		int tiles = 15;
		if (phase > 0) {
			tiles = 20;
		}
		List<Position> tilesToIndicate = new ArrayList<>();
		for (int i = 0; i < tiles; i++) {
			Position pos = Random.get(cavePhaseTiles);
			if (!tilesToIndicate.contains(pos)) {
				tilesToIndicate.add(pos);
			}
		}
		for (Position pos : tilesToIndicate) {
			World.sendGraphics(2111, 0, 0, pos);
		}
		World.startEvent(e -> {
			e.delay(2);
			for (Position pos : tilesToIndicate) {
				for (Player p : npc.getPosition().getRegion().players) {
					if (p.getPosition().distance(pos) < 1) {
						p.hit(new Hit().randDamage(35, 65));
					}
				}
			}
		});
	}

	private void sendSpecial() {
		if (Random.get(1) == 0)
			floorIndicatorSpecial();
		else
			boulderAttack();
	}

	private void boulderAttack() {
		npc.localPlayers().forEach(p -> {
			AttackStyle style = Random.get() < 1d / 3 ? AttackStyle.MAGIC : (Random.get() < 1d / 2 ? AttackStyle.RANGED : AttackStyle.STAB);
			String message;
			Projectile projectile;
			Prayer prayer;
			int hitGfx;
			switch (style) {
				case MAGIC:
					message = Color.PURPLE.wrap("The Hueycoatl fires a magic boulder in your direction!");
					projectile = MAGIC_PROJECTILE;
					hitGfx = 1342;
					prayer = Prayer.PROTECT_FROM_MAGIC;
					break;
				case RANGED:
					message = Color.DARK_GREEN.wrap("The Hueycoatl fires a ranged boulder in your direction!");
					projectile = RANGED_PROJECTILE;
					hitGfx = 1344;
					prayer = Prayer.PROTECT_FROM_MISSILES;
					break;
				case STAB:
					message = Color.RED.wrap("The Hueycoatl fires a melee boulder in your direction!");
					projectile = MELEE_PROJECTILE;
					hitGfx = 1346;
					prayer = Prayer.PROTECT_FROM_MELEE;
					break;
				default:
					return;
			}
			int delay = projectile.send(npc, p);
			p.graphics(hitGfx, 100, delay);
			p.sendMessage(message);
			p.addEvent(event -> {
				event.delay(4);
				if (!p.getPrayer().isActive(prayer)) {
					p.hit(new Hit(npc).fixedDamage(Math.min(200, p.getHp() / 2)));
				}
			});
		});
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		return false;
	}

	int ticksSinceSpecial = 0;
	boolean pillarsFilled = false;

	@Override
	public void process() {
		if (phase == 0) {
			if (ticksSinceSpecial++ >= 10) {
				ticksSinceSpecial = 0;
				sendSpecial();
			}
		} else {
			npc.localPlayers().forEach(p -> {
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE))
					meleePillarFill++;
				else if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
					rangedPillarFill++;
				else if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
					magicPillarFill++;
			});
			if (magicPillarFill >= 35 && rangedPillarFill >= 35 && meleePillarFill >= 35 && !pillarsFilled) {
				pillarsFilled = true;
			}
		}
	}
}
