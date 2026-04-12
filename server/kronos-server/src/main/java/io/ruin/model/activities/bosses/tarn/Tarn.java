package io.ruin.model.activities.bosses.tarn;

import io.ruin.Server;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.TickDelay;

import java.util.ArrayList;
import java.util.List;

public class Tarn extends NPCCombat {


	private static final Projectile MAGIC_PROJECTILE = new Projectile(2007, 90, 43, 35, 40, 6, 16, 192).regionBased();
	private static final Projectile RANGED_PROJECTILE = new Projectile(1615, 90, 43, 35, 40, 6, 16, 192).regionBased();
	private static final Projectile SNAKE_PROJECTILE = new Projectile(1483, 22, 43, 35, 40, 6, 16, 192).regionBased();

	private AttackStyle lastBasicAttackStyle = Random.rollPercent(50) ? AttackStyle.MAGIC : AttackStyle.RANGED;

	AttackStyle lastAttackType = null;

	int attackCounter = 0;

	private final int EARTHQUAKE_GFX = 1028;
	boolean portalSpecialStarted = false;

	AttackStyle blockedAttackStyle = null;

	TickDelay absorptionSwitchDelay = new TickDelay();
	TickDelay snakelingsSpawnDelay = new TickDelay();
	TickDelay dangerSnakeSpawnDelay = new TickDelay();
	TickDelay earthquakeDelay = new TickDelay();
	TickDelay timeOnHealthBombs = new TickDelay();
	TickDelay healthBombDelay = new TickDelay();

	public List<NPC> activeSnakelings = new ArrayList<>();

	int attackSpeed = 5;
	int lastAttackedInTicks = 5;

	public Bounds bossBounds;

	boolean portalsStarted = false;
	boolean portalsEnded = false;

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend);
		npc.isMovementBlocked(true, false);
		portalsStarted = false;
		bossBounds = new Bounds(npc.getPosition().getRegion().baseX + 8, npc.getPosition().getRegion().baseY + 36, npc.getPosition().getRegion().baseX + 18, npc.getPosition().getRegion().baseY + 50, 0);
	}

	private void preHitDefend(Hit hit) {
		if (hit.attacker != null && !hit.attacker.player.getPosition().inBounds(bossBounds)) {
			hit.block();
			hit.attacker.player.sendMessage(npc.getDef().name + "'s magic shield absorbs your attack from outside the boundary!");
		}
		if (blockedAttackStyle != null && hit.attackStyle != null) {
			if (blockedAttackStyle == AttackStyle.MAGIC && hit.attackStyle.isMagic()) {
				hit.type = HitType.HEAL;
			}
			if (blockedAttackStyle == AttackStyle.RANGED && hit.attackStyle.isRanged()) {
				hit.type = HitType.HEAL;
			}
			if (blockedAttackStyle == AttackStyle.CRUSH && hit.attackStyle.isMelee()) {
				hit.type = HitType.HEAL;
			}
		}
	}

	private void postDamage(Hit hit) {
		if (getHpPercentage() < 30 && !portalSpecialStarted) {
			startContinuousPortalAttack();
			portalSpecialStarted = true;
		}
		if (hit.attacker != null)
			lastAttackType = hit.attackStyle;
	}

	@Override
	public void follow() {

	}


	private void earthquakeAttack() {
		if (earthquakeDelay.remaining() > 0)
			return;
		npc.forceText("MY BOMBS WILL HANDLE YOU!");
		npc.animate(5618);

		earthquakeDelay.delay(30);

		World.startEvent(e -> {
			e.delay(2);

			for (int xOffset = -2; xOffset <= 2; xOffset++) {
				for (int yOffset = -2; yOffset <= 2; yOffset++) {
					Position pos = new Position(npc.getPosition().getX() + xOffset, npc.getPosition().getY() + yOffset, npc.getPosition().getZ());
					World.sendGraphics(EARTHQUAKE_GFX, 0, 0, pos);
					checkAndDamagePlayers(pos); // Check and damage players in the area
					e.delay(1); // Delay of 1 tick between each tile
				}
			}

			for (int i = -2; i <= 2; i++) {
				Position posX = new Position(npc.getPosition().getX() + i, npc.getPosition().getY() + i, npc.getPosition().getZ());
				Position negX = new Position(npc.getPosition().getX() + i, npc.getPosition().getY() - i, npc.getPosition().getZ());

				World.sendGraphics(EARTHQUAKE_GFX, 0, 0, posX);
				World.sendGraphics(EARTHQUAKE_GFX, 0, 0, negX);
				checkAndDamagePlayers(posX);
				checkAndDamagePlayers(negX);
				e.delay(1);
			}
		});
	}

	private void checkAndDamagePlayers(Position pos) {
		getLocalPlayers().stream()
			.filter(player -> player.getPosition().equals(pos))
			.forEach(player -> {
				player.hit(new Hit(npc).randDamage(25, 50));
				player.forceText("UGH!");
			});
	}


	private void spawnDangerSnake() {
		if (dangerSnakeSpawnDelay.remaining() > 0)
			return;
		int minDistance = 4;
		int maxDistance = 5;


		dangerSnakeSpawnDelay.delay(60);

		double angle = Math.toRadians(Random.get(0, 360));
		int distance = Random.get(minDistance, maxDistance);

		int x = npc.getPosition().getX() + (int) (distance * Math.cos(angle));
		int y = npc.getPosition().getY() + (int) (distance * Math.sin(angle));

		int minX = Math.min(bossBounds.swX, bossBounds.neX);
		int maxX = Math.max(bossBounds.swX, bossBounds.neX);
		int minY = Math.min(bossBounds.swY, bossBounds.neY);
		int maxY = Math.max(bossBounds.swY, bossBounds.neY);

		x = Math.max(minX, Math.min(maxX, x));
		y = Math.max(minY, Math.min(maxY, y));


		NPC snakeling = new NPC(7903).spawn(new Position(x, y, npc.getPosition().getZ()));
		snakeling.getCombat().setAllowRespawn(false);
		snakeling.getCombat().setAllowRetaliate(false);
		snakeling.isMovementBlocked(false, false);
		snakeling.setMaxHp(50 * getLocalPlayers().size());
		snakeling.setHp(50 * getLocalPlayers().size());
		final Player[] target = {Random.get(getLocalPlayers())};
		snakeling.face(target[0]);


		World.startEvent(e -> {
			e.setCancelCondition(this::targetIsNotInBossRegion);
			e.delay(30);
			while (snakeling.getHp() > 0 && !snakeling.isRemoved()) {
				if (npc.getHp() < 1 || getLocalPlayers().isEmpty())
					snakeling.remove();
				if (target[0] == null || target[0].getHp() < 1) {
					target[0] = Random.get(getLocalPlayers());
					snakeling.face(target[0]);
				}
				SNAKE_PROJECTILE.send(snakeling, target[0]);
				e.delay(3);
				if (target[0] == null || target[0].getHp() < 1 || !target[0].getPosition().inBounds(bossBounds))
					continue;
				if (npc.getHp() > 0) {
					target[0].hit(new Hit(snakeling).fixedDamage(40));
					damagedPlayer = true;
				}
				e.delay(1);
			}
		});

	}

	private int getTicks(int delay) {
		return Math.max(1, (delay * 16) / Server.tickMs());
	}

	TickDelay shadowOfLightningDelay = new TickDelay();

	private void handleShadowOfLightning() {
		if (shadowOfLightningDelay.remaining() > 0 || npc.getHp() < 1)
			return;
		shadowOfLightningDelay.delay(6);
		World.startEvent(event -> {
			event.setCancelCondition(this::targetIsNotInBossRegion);
			for (Player p : getLocalPlayers()) {
				Position pos = p.getPosition().copy();
				handleLightningStrike(pos);
				for (int i = 0; i < 3; i++) {
					World.sendGraphics(1796, 0, 0, pos);
					event.delay(1);
				}
			}
		});
	}

	private void handleLightningStrike(Position pos) {
		World.startEvent(event -> {
			event.setCancelCondition(this::targetIsNotInBossRegion);
			event.delay(4);
			World.sendGraphics(1797, 0, 0, pos);
			for (Player p : getLocalPlayers()) {
				if (p.getPosition().distance(pos) == 0 && npc.getHp() > 0) {
					p.hit(new Hit(npc).randDamage(25, 55));
					damagedPlayer = true;
				}
			}
		});
	}


	private void spawnHealingSnakes() {
		if (snakelingsSpawnDelay.remaining() > 0)
			return;
		int snakesToSpawn = getLocalPlayers().size() * 2;
		int minDistance = 4;
		int maxDistance = 5;

		for (int i = 0; i < snakesToSpawn; i++) {


			double angle = Math.toRadians(Random.get(0, 360));
			int distance = Random.get(minDistance, maxDistance);

			int x = npc.getPosition().getX() + (int) (distance * Math.cos(angle));
			int y = npc.getPosition().getY() + (int) (distance * Math.sin(angle));

			int minX = Math.min(bossBounds.swX, bossBounds.neX);
			int maxX = Math.max(bossBounds.swX, bossBounds.neX);
			int minY = Math.min(bossBounds.swY, bossBounds.neY);
			int maxY = Math.max(bossBounds.swY, bossBounds.neY);

			x = Math.max(minX, Math.min(maxX, x));
			y = Math.max(minY, Math.min(maxY, y));

			NPC snakeling = new NPC(5617).spawn(new Position(x, y, npc.getPosition().getZ()));
			snakeling.setMaxHp(getLocalPlayers().size() * 25);
			snakeling.setHp(getLocalPlayers().size() * 25);
			snakeling.getCombat().setAllowRespawn(false);
			snakeling.getCombat().setAllowRetaliate(false);
			snakeling.isMovementBlocked(false, false);

			activeSnakelings.add(snakeling);
		}
		snakelingsSpawnDelay.delay(80);
	}


	@Override
	public boolean attack() {
		if (lastAttackedInTicks < attackSpeed) {
			lastAttackedInTicks++;
			return false;
		}
		npc.face(target);
		lastAttackedInTicks = 0;
		if (Random.get(1, 4) == 1 && getHpPercentage() < 50 && getHpPercentage() > 20) {
			spawnHealingSnakes();
			return true;
		}
		if (Random.get(1, 4) == 1) {
			spawnDangerSnake();
			return true;
		}

		autoAttack();
		return true;
	}

	private Position getRandomSafeTileAroundPlayer(Position playerPosition) {
		int offsetX = Random.get(-1, 1);
		int offsetY = Random.get(-1, 1);

		int newX = playerPosition.getX() + offsetX;
		int newY = playerPosition.getY() + offsetY;

		return new Position(newX, newY, playerPosition.getZ());
	}

	private void activateTiles(List<Position> tiles, int graphicsId) {
		World.startEvent(e -> {
			e.setCancelCondition(this::targetIsNotInBossRegion);
			for (int i = 0; i < 4; i++) {
				tiles.forEach(pos -> World.sendGraphics(graphicsId, 0, 0, pos));
				e.delay(1);
			}
		});

	}

	boolean portalForceChatActivated = false;

	private void startContinuousPortalAttack() {
		if (!portalForceChatActivated) {
			npc.forceText("RAAARGH! MY PORTALS WILL CONSUME YOU!");
			portalForceChatActivated = true;
		}
		npc.animate(5618);
		portalsStarted = true;
		World.startEvent(event -> {
			while (npc.getHp() > 0 && !targetIsNotInBossRegion()) {
				if (npc == null || npc.isRemoved() || npc.isHidden())
					break;

				List<Position> dangerousTiles = new ArrayList<>();
				List<Position> safeTiles = new ArrayList<>();

				getLocalPlayers().forEach(p -> {
					dangerousTiles.add(p.getPosition().copy());
					safeTiles.add(getRandomSafeTileAroundPlayer(p.getPosition().copy()));
				});

				bossBounds.forEachPos(pos -> {
					if (!safeTiles.contains(pos) && Random.rollPercent(50)) {
						dangerousTiles.add(pos);
					}
				});

				activateTiles(dangerousTiles, 1796);

				event.delay(4);
				for (Position pos : dangerousTiles) {
					World.sendGraphics(1797, 0, 0, pos);
					getLocalPlayers().forEach(p -> {
						if (p.getPosition().distance(pos) == 0 && npc.getHp() > 0) {
							damagedPlayer = true;
							p.hit(new Hit(npc).randDamage(25, 45));
						}
					});
				}
				event.delay(1);
				for (Position pos : dangerousTiles) {
					World.sendGraphics(-1, 0, 0, pos);
				}
			}
			portalsStarted = false;
		});
	}

	List<GameObject> healthBombs = new ArrayList<>();


	private void spawnHealthSiphonBombs() {
		if (healthBombDelay.remaining() > 0)
			return;
		npc.forceText("YOUR HEALTH IS MINE!");
		npc.animate(5618);
		healthBombDelay.delay(60);
		for (int i = 0; i < 2; i++) {
			Position pos = new Position(npc.getPosition().getX() + Random.get(-2, 2), npc.getPosition().getY() + Random.get(-2, 2), npc.getPosition().getZ());
			GameObject bomb = new GameObject(7606, pos.getX(), pos.getY(), pos.getZ(), 10, 0).spawn();
			healthBombs.add(bomb);
		}
	}


	private void attackStyleAbsorptionSpecial() {
		npc.animate(5618);
		if(lastAttackType == null)
			lastAttackType = Random.rollPercent(50) ? AttackStyle.CRUSH : AttackStyle.RANGED;
		blockedAttackStyle = lastAttackType;
		if (blockedAttackStyle == AttackStyle.SLASH || blockedAttackStyle == AttackStyle.STAB) {
			blockedAttackStyle = AttackStyle.CRUSH;
			npc.setHeadIcon(NPC.DefaultHeadIconIndex.ProtectFromMelee);
		} else if (blockedAttackStyle == AttackStyle.MAGIC) {
			npc.setHeadIcon(NPC.DefaultHeadIconIndex.ProtectFromMagic);
		} else if (blockedAttackStyle == AttackStyle.RANGED) {
			npc.setHeadIcon(NPC.DefaultHeadIconIndex.ProtectFromRanged);
		}
		String style = blockedAttackStyle == AttackStyle.MAGIC ? "MAGIC" : blockedAttackStyle == AttackStyle.RANGED ? "RANGED" : "MELEE";

		npc.forceText("YOUR " + style + " ATTACKS WILL NOW FUEL ME!");
		getLocalPlayers().forEach(p -> {
			p.sendMessage(Color.CRIMSON.wrap("Ophidia will now absorb " + style + " attacks and is now immune to them!"));
		});
	}

	private double getHpPercentage() {
		return ((double) npc.getHp() / npc.getMaxHp()) * 100;
	}

	TickDelay snakeAttackDelay = new TickDelay();

	@Override
	public void process() {
		if(blockedAttackStyle == null) {
			attackStyleAbsorptionSpecial();
		}
		if (npc.isRemoved() || npc.getHp() < 1 || npc.isHidden())
			return;
		if (getHpPercentage() > 99 && npc.getCombat().isDefending(5)) {
			portalsStarted = false;
			portalSpecialStarted = false;
		}
		if (absorptionSwitchDelay.remaining() < 1) {
			absorptionSwitchDelay.delay(25);
			attackStyleAbsorptionSpecial();
		}

		if (getHpPercentage() < 30)
			attackSpeed = 2;
		else if (getHpPercentage() < 50)
			attackSpeed = 3;
		else if (getHpPercentage() < 75)
			attackSpeed = 4;
		else
			attackSpeed = 5;


		if (!getLocalPlayers().isEmpty() && !portalsStarted)
			handleShadowOfLightning();


		if (!activeSnakelings.isEmpty()) {
			if (snakeAttackDelay.remaining() > 0)
				return;
			snakeAttackDelay.delay(4);
			for (int i = activeSnakelings.size() - 1; i >= 0; i--) {
				NPC snakeling = activeSnakelings.get(i);
				if (snakeling.isRemoved()) {
					activeSnakelings.remove(snakeling);
					continue;
				}
				if (snakeling.getHp() < 1 || getLocalPlayers().isEmpty()) {
					snakeling.remove();
					activeSnakelings.remove(snakeling);
				}
				snakeling.face(npc);
				World.startEvent(e -> {
					e.setCancelCondition(this::targetIsNotInBossRegion);
					e.delay(4);
					int delay = SNAKE_PROJECTILE.send(snakeling, npc);
					if (snakeling.getHp() > 0 && npc.getHp() > 0)
						npc.hit(new Hit(HitType.HEAL).fixedDamage(25).clientDelay(delay));
				});
			}
		}
		if (!healthBombs.isEmpty()) {
			for (int i = healthBombs.size() - 1; i >= 0; i--) {
				GameObject bomb = healthBombs.get(i);
				if (bomb.isRemoved()) {
					healthBombs.remove(bomb);
					continue;
				}
				if (timeOnHealthBombs.remaining() < 1) {
					bomb.remove();
					healthBombs.remove(bomb);
				}
				getLocalPlayers().forEach(p -> {
					if (p.getPosition().distance(bomb.getPosition()) < 4) {
						int damage = Random.get(20, 40);
						if (npc.getHp() > 0) {
							p.hit(new Hit(npc).fixedDamage(damage));
							npc.hit(new Hit(HitType.HEAL).fixedDamage(damage));
						}
					}
				});
			}
		}

	}

	private List<Player> getLocalPlayers() {
		List<Player> targets = new ArrayList<>(npc.getPosition().getRegion().players);
		for (Player player : npc.localPlayers()) {
			if (!targets.contains(player))
				targets.add(player);
		}
		return targets;
	}


	public boolean damagedPlayer = false;

	private void autoAttack() {
		attackCounter++;
		npc.animate(5617);
		getLocalPlayers().forEach(p -> {
			int delay = lastBasicAttackStyle == AttackStyle.RANGED ? RANGED_PROJECTILE.send(npc, p) : MAGIC_PROJECTILE.send(npc, p);

			if (lastBasicAttackStyle == AttackStyle.MAGIC)
				p.graphics(2008, 60, delay);
			int maxDamage = 39;
			if (lastBasicAttackStyle == AttackStyle.RANGED && p.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
				maxDamage = 11;
			else if (lastBasicAttackStyle == AttackStyle.MAGIC && p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
				maxDamage = 13;
			else damagedPlayer = true;
			if (npc.getHp() > 0)
				p.hit(new Hit(npc, lastBasicAttackStyle).randDamage(maxDamage).clientDelay(delay));

		});
		if (attackCounter == 5) {
			lastBasicAttackStyle = lastBasicAttackStyle == AttackStyle.RANGED ? AttackStyle.MAGIC : AttackStyle.RANGED;
			attackCounter = 0;
			npc.forceText("Hssssssss!");
		}
	}
}
