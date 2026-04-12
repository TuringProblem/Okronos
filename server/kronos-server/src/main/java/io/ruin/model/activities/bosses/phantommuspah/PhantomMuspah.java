package io.ruin.model.activities.bosses.phantommuspah;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.model.var.VarPlayerRepository;

import java.util.ArrayList;
import java.util.List;

public class PhantomMuspah extends NPCCombat {
	private static final int MELEE = 12078;
	private static final int SHIELDED = 12079;
	private static final int RANGED = 12077;

	private final Projectile MAGIC_PROJECTILE = new Projectile(2327, 65, 20, 20, 15, 12, 15, 10);
	private final Projectile RANGED_PROJECTILE = new Projectile(2329, 65, 20, 20, 15, 12, 15, 10);

	int formSwitchCounter = 0;

	List<Position> teleportPositions = new ArrayList<>();
	int currentTeleportIndex = 0;
	boolean teleporting = false;

	public boolean damagedPlayer = false;

	Bounds bossBounds;

	int specialsUsed = 0;
	public boolean targetUsedRun = false;

	boolean timerStarted = false;

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDefend(this::postDefend).preDefend(this::preDefend);
		teleportPositions
				.add(new Position(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 38, 0));
		teleportPositions
				.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 38, 0));
		teleportPositions
				.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 28, 0));
		teleportPositions
				.add(new Position(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 28, 0));
		bossBounds = new Bounds(npc.getPosition().getRegion().baseX + 20, npc.getPosition().getRegion().baseY + 23,
				npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 45, npc.getPosition().getZ());
		specialsUsed = 0;
	}

	public void resetVariables() {
		formSwitchCounter = 0;
		specialsUsed = 0;
		targetUsedRun = false;
		currentTeleportIndex = 0;
		teleporting = false;
		timerStarted = false;
		damagedPlayer = false;
	}

	private void preDefend(Hit hit) {
		if (hit.attacker != null && hit.attackStyle != null) {
			if (npc.getId() == MELEE) {
				if (hit.attackStyle == AttackStyle.MAGIC)
					hit.boostDefence(0.5);
			} else if (npc.getId() == RANGED) {
				if (hit.attackStyle == AttackStyle.MAGIC)
					hit.boostDefence(2.5);
			}
		}
		if (npc.getId() == 12082) {
			if (hit.attacker != null && hit.attacker.isPlayer()) {
				hit.attacker.getCombat().reset();
			}
		}
	}

	private void teleportSpecial() {
		teleporting = true;
		npc.transform(12082);
		if (target != null)
			target.getCombat().reset();
		startCloudEvent();
		World.startEvent(e -> {
			for (int i = 0; i < 15; i++) {
				e.delay(1);
				npc.animate(9930);
				npc.getMovement().teleport(getNextTeleportPosition());
				if (i == 14) {
					teleporting = false;
					npc.getMovement().teleport(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 35,
							0);
					npc.transform(RANGED);
				}
			}
		});
	}

	private void startCloudEvent() {
		World.startEvent(e -> {
			List<Cloud> clouds = new ArrayList<>();
			for (int i = 0; i < 20; i++) {
				Cloud cloud = new Cloud(12083, bossBounds.randomPosition(), npc);
				clouds.add(cloud);
			}
			e.delay(20);
			for (Cloud cloud : clouds) {
				cloud.remove();
			}
		});
	}

	private Position getNextTeleportPosition() {
		currentTeleportIndex++;
		if (currentTeleportIndex >= teleportPositions.size())
			currentTeleportIndex = 0;
		return teleportPositions.get(currentTeleportIndex);
	}

	private void homingSpecial() {
		World.startEvent(e -> {
			npc.animate(9923);
			e.delay(2);
			int spikesToSpawn = Random.get(2, 4);
			for (int i = 0; i < spikesToSpawn; i++) {
				Position spikePosition = getSpikePositionFromBoss();
				if (spikePosition == null) {
					continue;
				}
				handleSpikeEvent(spikePosition);
			}
		});
	}

	private void handleSpikeEvent(final Position startPosition) {
		World.startEvent(e -> {
			if (target == null) {
				return;
			}
			Position playerPosition = target.getPosition();
			Position currentPosition = startPosition.copy();
			GameObject spike = null;

			for (int i = 0; i < 20; i++) {
				if (npc.getHp() < 1) {
					break;
				}
				e.delay(2);
				if (spike != null) {
					spike.remove();
				}

				int dx = playerPosition.getX() - currentPosition.getX();
				int dy = playerPosition.getY() - currentPosition.getY();

				// Move only one tile towards the player
				if (Math.abs(dx) > Math.abs(dy)) {
					currentPosition = new Position(currentPosition.getX() + Integer.compare(dx, 0), currentPosition.getY());
				} else {
					currentPosition = new Position(currentPosition.getX(), currentPosition.getY() + Integer.compare(dy, 0));
				}
				if (currentPosition.regionId() != npc.getPosition().regionId())
					continue;

				spike = GameObject.spawn(46697, currentPosition.getX(), currentPosition.getY(), currentPosition.getZ(), 10, 0)
						.spawn();

				if (currentPosition.equals(playerPosition)) {
					if (target != null) {
						damagedPlayer = true;
						target.hit(new Hit(npc).randDamage(12, 20));
					}
				}

				if (i == 19) {
					spike = GameObject.spawn(46695, currentPosition.getX(), currentPosition.getY(), currentPosition.getZ(), 10, 0)
							.spawn();
				}
			}
		});
	}


	private Position getSpikePositionFromBoss() {
		Bounds spikeArea = new Bounds(npc.getPosition().getX() - 2, npc.getPosition().getY() - 2,
				npc.getPosition().getX() + 2, npc.getPosition().getY() + 2, npc.getPosition().getZ());
		Position spikePosition = null;
		var max = 10;
		while (spikePosition == null && max-- > 0) {
			Position randomPosition = spikeArea.randomPosition();
			if (randomPosition.equals(npc.getPosition()))
				continue;
			if (Tile.get(randomPosition) == null || Tile.get(randomPosition).clipping != 0)
				continue;
			spikePosition = randomPosition;
		}
		return spikePosition;
	}

	private void postDefend(Hit hit) {
		formSwitchCounter += hit.damage;
		if (formSwitchCounter >= 300 && !teleporting) {
			switchForm();
			formSwitchCounter = 0;
		}


		float npcHPPerc = (float) npc.getHp() / npc.getMaxHp();
		if (npcHPPerc <= 0.66 && specialsUsed == 0) {
			specialsUsed++;
			teleportSpecial();
		} else if (npcHPPerc <= 0.33 && specialsUsed == 1) {
			specialsUsed++;
			homingSpecial();
		}
	}

	@Override
	public void follow() {
		if (npc.getId() == MELEE)
			follow(2);

	}


	@Override
	public boolean attack() {
		if (!timerStarted) {
			timerStarted = true;
			target.player.phantomMuspahTimer = new ActivityTimer();
		}
		if (teleporting)
			return false;
		if (npc.getId() == MELEE && withinDistance(2)) {
			meleeAttack();
		} else if (npc.getId() == RANGED) {
			if (Random.get(10) == 0)
				chargedMagicAttack((Player) target);
			else
				rangedAttack((Player) target);
		}
		return true;
	}

	protected Hit meleeAttack() {
		npc.animate(9920);
		Hit hit = new Hit(npc, AttackStyle.SLASH, null).randDamage(55);
		target.hit(hit);
		hit.preDamage(h -> {

			if (this.target == null) {
				return;
			}
			if (target.player == null) {
				return;
			}

			if (!target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE)) {
				damagedPlayer = true;
			}
		});
		return hit;
	}

	@Override
	public void process() {
		if (target != null && VarPlayerRepository.RUNNING.get(target.player) == 1) {
			targetUsedRun = true;
		}
	}

	private void switchForm() {
		if (npc.getId() == MELEE) {
			npc.transform(RANGED);
		} else if (npc.getId() == RANGED) {
			npc.transform(MELEE);
		}
		switchFormEvent();
	}

	private void switchFormEvent() {
		World.startEvent(e -> {
			if (target == null) {
				return;
			}
			Position spikePos = target.getPosition().copy();
			for (int i = 0; i < 5; i++) {
				World.sendGraphics(2335, 0, 0, spikePos);
				e.delay(1);
			}
			if (target != null && target.getPosition().distance(spikePos) <= 1) {
				damagedPlayer = true;
				target.hit(new Hit(npc).randDamage(20, 30));
			}
			GameObject spike = GameObject.spawn(46693, spikePos.getX(), spikePos.getY(), spikePos.getZ(), 10, 0).spawn();
			e.delay(1);
			spike.setId(46695);
		});
	}

	private void rangedAttack(Player target) {
		World.startEvent(e -> {

			// NOTE: player might have teleported out.
			if (target != null && target.getPosition().distance(npc.getPosition()) > 64) {
				return;
			}

			npc.animate(9922);
			int delay = RANGED_PROJECTILE.send(npc, target);
			int damage = 43;
			e.delay(World.getTicks(delay));
			if (target == null || target.getHp() < 1 || target.getCombat().isDead())
				return;
			if (target.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
				damage = 9;
			} else {
				damagedPlayer = true;
			}
			target.hit(new Hit(npc).randDamage(damage).ignorePrayer().clientDelay(delay));
		});
	}

	private void chargedMagicAttack(Player target) {
		World.startEvent(e -> {
			npc.animate(9918);
			npc.graphics(2319);
			e.delay(2);

			if (target == null || target.getHp() < 1 || target.getCombat().isDead()) {
				return;
			}

			// NOTE: player might have teleported out.
			if (target != null && target.getPosition().distance(npc.getPosition()) > 64) {
				return;
			}

			int delay = MAGIC_PROJECTILE.send(npc, target);
			int damage = 47;
			boolean isProtected = false;
			if (target.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
				damage = 9;
				isProtected = true;
			}
			target.hit(new Hit(npc).randDamage(damage).ignorePrayer().clientDelay(delay));
			if (!isProtected) {
				damagedPlayer = true;
				target.sendMessage("The Phantom Muspah's magic attack has corrupted you!");
				startCorruptionEvent(target);
			}
		});
	}

	private void startCorruptionEvent(Player player) {
		World.startEvent(e -> {
			for (int i = 0; i < 10; i++) {
				if (!player.getPosition().inBounds(bossBounds))
					break;
				e.delay(6);
				if (player == null || player.getHp() < 1 || player.getCombat().isDead())
					break;
				player.getPrayer().drain(Random.get(3, 7));
			}
		});
	}
}
