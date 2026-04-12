package io.ruin.model.activities.bosses.hydra;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.NPCType;
import io.ruin.model.World;

import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.*;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.map.route.routes.DumbRoute;
import io.ruin.model.map.route.routes.ProjectileRoute;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Misc;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AlchemicalHydra extends NPCCombat {

	private static final Position SPAWN_POSITION = new Position(1364, 10265, 0);
	static DynamicMap map;
	private static final double[] PHASE_HP_THRESHOLDS = {0.75, 0.50, 0.25};
	private boolean inTransition = false;
	private int currentPhase = 0; // 0=GREEN, 1=BLUE, 2=RED, 3=GREY

	public boolean damagedByPoison = false;
	public boolean damagedPlayer = false;
	public boolean damagedByLightning = false;
	public boolean flameSpawned = false;
	public boolean empowered = false;
	public boolean hitByFlame = false;

	public boolean timerStarted = false;

	public static void register() {
		for (int id : new int[]{8615, 8616, 8617, 8618, 8619, 8620, 8621, 8622})
			NPCType.get(id).ignoreOccupiedTiles = true;

		ObjectAction.register(34548, 1351, 10251, 0, "climb", (player, obj) -> climbWall(player));
		ObjectAction.register("Alchemical door", "Open", AlchemicalHydra::confirmAndEnterBossRoom);
		ObjectAction.register("Alchemical door", "quick-open", AlchemicalHydra::enterBossRoom);
	}

	private FireArea fireLockdownArea;

	private enum HydraPhase {
		GREEN(8615, 1.0),  // 100% HP
		BLUE(8619, 0.75),  // 75% HP
		RED(8620, 0.50),   // 50% HP
		GREY(8621, 0.25);  // 25% HP

		final int npcId;
		final double hpThreshold;

		HydraPhase(int npcId, double hpThreshold) {
			this.npcId = npcId;
			this.hpThreshold = hpThreshold;
		}
	}

	private static void confirmAndEnterBossRoom(Player player, GameObject door) {
		if (player.getAbsX() > door.x) {
			player.dialogue(new OptionsDialogue("Leave the boss room?", new Option("Yes.", () -> player.getMovement().teleport(1355, 10259, 0)), new Option("No.")));
		} else {
			player.dialogue(new OptionsDialogue("Enter the boss room?", new Option("Yes.", () -> enterBossRoom(player, door)), new Option("No.")));
		}
	}

	private static void enterBossRoom(Player player, GameObject door) {
		player.getInstanceTokenInterface().selectedBoss = InstanceMaps.ALCHEMICAL_HYDRA;
		player.getInstanceTokenInterface().startInstance(player, true);
	}

	public static void climbWall(Player player) {
		player.startEvent(event -> {
			player.lock(LockType.FULL_NULLIFY_DAMAGE);
			event.delay(1);
			player.animate(839);
			player.getMovement().force(0, 2, 0, 0, 0, 60, Direction.NORTH);
			event.delay(2);
			player.unlock();
		});
	}


	private static final Projectile MAGIC_PROJECTILE_1 = new Projectile(1662, 60, 21, 25, 30, 10, 16, 64);
	private static final Projectile MAGIC_PROJECTILE_2 = new Projectile(1662, 50, 21, 40, 30, 10, 16, 64);

	private static final Projectile RANGED_PROJECTILE_1 = new Projectile(1663, 60, 31, 25, 30, 10, 16, 64);
	private static final Projectile RANGED_PROJECTILE_2 = new Projectile(1663, 50, 31, 40, 30, 10, 16, 64);

	private static final Projectile POISON_PROJECTILE = new Projectile(1644, 50, 0, 25, 75, 0, 16, 64);

	private static final Projectile SHOCK_PROJECTILE_1 = new Projectile(1665, 50, 60, 15, 60, 0, 16, 64);
	private static final Projectile SHOCK_PROJECTILE_2 = new Projectile(1665, 60, 0, 0, 50, 0, 16, 64);

	private static final Projectile FIRE_PROJECTILE = new Projectile(1667, 50, 0, 25, 50, 0, 16, 80);

	public static final int[] POISON_POOLS = { // indexed by direction as in Direction class
		1658,
		1659,
		1660,
		1657,
		1661,
		1656,
		1655,
		1654,
	};

	private static final int[][] SHOCK_SPAWN_POINTS = {
		{7, -1},
		{8, 7},
		{-2, 8},
		{-2, -1},
	};

	private enum Form {
		GREEN(8615, 8616, 8237, 3, -1, 8234, 8235, 8236, 34568),
		BLUE(8619, 8617, 8244, 2, 8238, 8241, 8242, 8243, 34569),
		RED(8620, 8618, 8251, 3, 8245, 8248, 8249, 8250, 34570),
		GREY(8621, -1, -1, 0, 8252, -1, 8256, 8255, -1);

		Form(int npcId, int loseHeadNPCId, int loseHeadAnim, int loseHeadDuration, int fadeInAnim, int middleHeadAttackAnim, int rightHeadsAttackAnim, int leftHeadsAttackAnim, int weaknessVent) {
			this.npcId = npcId;
			this.loseHeadNPCId = loseHeadNPCId;
			this.loseHeadAnim = loseHeadAnim;
			this.loseHeadDuration = loseHeadDuration;
			this.fadeInAnim = fadeInAnim;
			this.middleHeadAttackAnim = middleHeadAttackAnim;
			this.rightHeadsAttackAnim = rightHeadsAttackAnim;
			this.leftHeadsAttackAnim = leftHeadsAttackAnim;
			this.weaknessVent = weaknessVent;
		}

		private int npcId; // main npc id (the one that fights)
		private int loseHeadNPCId; // npc id this phase transforms into to do the losing head animation
		private int loseHeadAnim; // the losing head animation
		private int loseHeadDuration; // how many ticks to wait for lose head anim
		private int fadeInAnim; // the animation this form performs when switching into it (comes right after the previous form's lose head animation)
		private int middleHeadAttackAnim, rightHeadsAttackAnim, leftHeadsAttackAnim; // attack anims
		private int weaknessVent;

	}

	private GameObject redVent, greenVent, blueVent;
	private Form currentForm = Form.GREEN;
	private AttackStyle currentStyle = Random.rollPercent(50) ? AttackStyle.RANGED : AttackStyle.MAGIC;
	private int attackCounter = -3; // start at -3 because the first special attack comes after 3 attacks
	private int lastSpecial = -1;

	private boolean resistant = true;
	private int power = 0;

	private boolean firesActive = false;

	@Override
	public void init() {
		npc.hitListener = new HitListener()
			.postDamage(this::postDamage)
			.postDefend(this::postDefend);
		npc.attackBounds = new Bounds(npc.getPosition(), 20);

		// Initialize vents
		redVent = Tile.getObject(34568, npc.getSpawnPosition().getX() + 7,
			npc.getSpawnPosition().getY() + -2, npc.getSpawnPosition().getZ(), 10, -1);
		greenVent = Tile.getObject(34569, npc.getSpawnPosition().getX() + 7,
			npc.getSpawnPosition().getY() + 7, npc.getSpawnPosition().getZ(), 10, -1);
		blueVent = Tile.getObject(34570, npc.getSpawnPosition().getX() - 2,
			npc.getSpawnPosition().getY() + 7, npc.getSpawnPosition().getZ(), 10, -1);
		startVents();
	}

	public boolean noPressureFailed = false;

	public void resetCombatAchievementVariables() {
		damagedByPoison = false;
		damagedPlayer = false;
		damagedByLightning = false;
		flameSpawned = false;
		empowered = false;
		hitByFlame = false;
		noPressureFailed = false;
	}

	private void postDefend(Hit hit) {
		if (resistant && currentForm != Form.GREY) {
			hit.damage *= 0.25;
			if (hit.attacker != null && hit.attacker.player != null)
				hit.attacker.player.sendMessage("The Alchemical Hydra's defences partially absorb your attack!");
		}
	}

	private void startVents() {
		for (GameObject vent : new GameObject[]{redVent, greenVent, blueVent}) {
			Bounds ventBounds = new Bounds(new Position(vent.x, vent.y, vent.z), 2);
			npc.addEvent(event -> {
				event.delay(6);
				while (true) {
					vent.animate(8279);
					event.delay(1);
					if (target != null) {
						if (target.getPosition().equals(vent.x, vent.y)) {
							target.hit(new Hit().randDamage(20));
							target.player.sendMessage("The chemical burns you as it cascades over you.");
							damagedPlayer = true;
						}
						Bounds npcBounds = new Bounds(npc.getPosition().getX() + 1, npc.getPosition().getY() + 1, npc.getPosition().getX() + npc.getSize() - 2, npc.getPosition().getY() + npc.getSize() - 2, npc.getPosition().getZ());
						if (ventBounds.intersects(npcBounds)) {
							if (vent.getId() == currentForm.weaknessVent) {
								if (resistant) {
									resistant = false;
									npc.forceText("Roaaaaaaaaaaaaaaaaaar!");
									target.player.sendMessage("The chemicals neutralise the Alchemical Hydra's defences!");
								}
							} else {
								power++;
								target.player.sendMessage("The chemicals are absorbed by the Alchemical Hydra; empowering it further!");
								empowered = true;
							}
						}
					}
					event.delay(4);
					vent.animate(8280);
					event.delay(5);
				}
			});
		}
	}

	private void onDeath() {
		npc.transform(8615);
		currentForm = Form.GREEN;
		resistant = true;
		power = 0;
		attackCounter = -3;
		lastSpecial = -1;
	}

	List<Integer> dhAxes = Arrays.asList(
		4718, 4886, 4887, 4888, 4889
	);

	private void postDamage(Hit hit) {
		if (inTransition || npc.isLocked()) {
			return;
		}

		double hpRatio = (double) npc.getHp() / npc.getMaxHp();

		// Check if we should transition to next phase
		if (currentPhase < HydraPhase.values().length - 1 &&
			hpRatio <= PHASE_HP_THRESHOLDS[currentPhase]) {
			nextPhase();
		}
		if (hit.attacker != null && hit.attacker.isPlayer() && hit.attacker.player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (!dhAxes.contains(hit.attacker.player.getEquipment().get(Equipment.SLOT_WEAPON).getId()))
				noPressureFailed = true;
		}
	}

	private void nextPhase() {
		if (inTransition) {
			return;
		}
		if (currentForm == Form.GREY)
			return;
		inTransition = true;

		currentPhase++;
		Form nextForm = Form.values()[currentPhase];
		npc.addEvent(event -> {
			try {
				npc.lock();
				// Transform to next phase
				npc.transform(currentForm.loseHeadNPCId);
				npc.animate(currentForm.loseHeadAnim);
				event.delay(currentForm.loseHeadDuration);
				currentForm = nextForm;

				// Reset phase-specific variables
				if (currentPhase < 3) { // Not final phase
					resistant = true;
					power = 0;
				} else { // Final phase (GREY)
					switchStyle();
					power = 8;
					attackCounter = -3;
					lastSpecial = -1;
				}
				// Small delay for transition
				npc.transform(nextForm.npcId);
				npc.animate(nextForm.fadeInAnim);
				// Face target and unlock
				if (target != null)
					npc.face(target);
				event.delay(2);
			} finally {
				npc.unlock();
				inTransition = false;
			}
		});
	}

	private void nextForm() {
		if (currentForm == Form.GREY)
			return;
		Form nextForm = Form.values()[currentForm.ordinal() + 1];
		npc.addEvent(event -> {
			npc.lock();
			npc.transform(currentForm.loseHeadNPCId);
			npc.animate(currentForm.loseHeadAnim);
			int delay = currentForm.loseHeadDuration;
			currentForm = nextForm;
			event.delay(delay);
			if (currentForm != Form.GREY) {
				resistant = true;
				power = 0;
			} else {
				switchStyle();
				power = 8;
				attackCounter = -3;
				lastSpecial = -1;
			}
			npc.transform(nextForm.npcId);
			npc.animate(nextForm.fadeInAnim);
			npc.face(target);
			event.delay(2);
			npc.unlock();
		});
	}

	@Override
	public void follow() {
		follow(firesActive ? 12 : 4);
	}

	@Override
	public boolean attack() {
		if (!timerStarted) {
			timerStarted = true;
			target.player.alchemicalHydraTimer = new ActivityTimer();
		}
		if (npc.isLocked())
			return false;
		if (!withinDistance(firesActive ? 16 : 6))
			return false;
		if (attackCounter % 9 == 0 && attackCounter != lastSpecial) {
			specialAttack();
			lastSpecial = attackCounter;
		} else {
			if (currentStyle == AttackStyle.MAGIC)
				magicAttack();
			else
				rangedAttack();
			attackCounter++;
			if (currentForm == Form.GREY || attackCounter % 3 == 0) {
				switchStyle();
			}
		}

		return true;
	}

	@Override
	public void process() {
		if (!npc.getPosition().getRegion().players.isEmpty()) {
			npc.getPosition().getRegion().players.forEach(p -> {
				if (p.getHp() > 10)
					noPressureFailed = true;
			});
		}
	}

	private void switchStyle() {
		currentStyle = currentStyle == AttackStyle.MAGIC ? AttackStyle.RANGED : AttackStyle.MAGIC;
	}

	private void rangedAttack() {
		int maxDamage = info.max_damage;
		maxDamage *= 1 + Math.min(0.5, power * 0.0625);
		npc.animate(currentForm.rightHeadsAttackAnim);
		if (currentForm == Form.GREEN || currentForm == Form.BLUE) { // 2 attacks
			maxDamage /= 2;
			Hit hit = projectileAttack(RANGED_PROJECTILE_1, -1, AttackStyle.RANGED, maxDamage);
			Hit hit2 = projectileAttack(RANGED_PROJECTILE_2, -1, AttackStyle.RANGED, maxDamage);
			hit.preDamage(e -> {
				if (target != null && !target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
					damagedPlayer = true;
			});
			hit2.preDamage(e -> {
				if (target != null && !target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
					damagedPlayer = true;
			});
		} else { // only 1, but stronger, attack
			Hit hit = projectileAttack(RANGED_PROJECTILE_1, -1, AttackStyle.RANGED, maxDamage);
			hit.preDamage(e -> {
				if (target != null && !target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
					damagedPlayer = true;
			});
		}
	}

	private void magicAttack() {
		int maxDamage = info.max_damage;
		maxDamage *= 1 + Math.min(0.5, power * 0.0625);
		npc.animate(currentForm.leftHeadsAttackAnim);
		if (currentForm == Form.GREEN) { // 2 attacks
			maxDamage /= 2;
			Hit hit = projectileAttack(MAGIC_PROJECTILE_1, -1, AttackStyle.MAGIC, maxDamage);
			Hit hit2 = projectileAttack(MAGIC_PROJECTILE_2, -1, AttackStyle.MAGIC, maxDamage);
			hit.preDamage(e -> {
				if (target != null && !target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
					damagedPlayer = true;
			});
			hit2.preDamage(e -> {
				if (target != null && !target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
					damagedPlayer = true;
			});
		} else { // only 1, but stronger, attack
			Hit hit = projectileAttack(MAGIC_PROJECTILE_1, -1, AttackStyle.MAGIC, maxDamage);
			hit.preDamage(e -> {
				if (target != null && !target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
					damagedPlayer = true;
			});
		}
	}

	private void specialAttack() {
		switch (currentForm) {
			case GREEN:
			case GREY:
				poisonAttack();
				break;
			case BLUE:
				shockAttack();
				break;
			case RED:
				fireAttack();
				break;
		}
	}

	private void poisonAttack() {
		if (currentForm == Form.GREEN)
			npc.animate(currentForm.middleHeadAttackAnim);
		else
			npc.animate(currentForm.leftHeadsAttackAnim);
		List<Position> targets = new LinkedList<>();
		targets.add(target.getPosition().copy());
		if (currentForm != Form.GREY || Random.rollDie(2, 1)) {
			Bounds hydraBounds = npc.getBounds();
			List<Position> positions = target.getPosition().area(3, pos -> pos.getTile().clipping == 0 && !pos.inBounds(hydraBounds) && ProjectileRoute.allow(npc, pos));
			for (int i = 0; i < 4; i++)
				targets.add(Random.get(positions));
		}
		targets.forEach(pos -> {
			npc.addEvent(event -> {
				int delay = POISON_PROJECTILE.send(npc, pos);
				Direction dir = Direction.getDirection(Misc.getClosestPosition(npc, pos), pos);
				World.sendGraphics(1645, 0, delay, pos.getX(), pos.getY(), pos.getZ());
				World.sendGraphics(POISON_POOLS[dir.ordinal()], 0, delay, pos.getX(), pos.getY(), pos.getZ());
				event.delay(3);
				for (int i = 0; i < 15; i++) {
					if (target == null)
						return;
					if (target.getPosition().isWithinDistance(pos, i == 0 ? 1 : 0)) {
						target.hit(new Hit(HitType.POISON).randDamage(1, 12));
						damagedByPoison = true;
						damagedPlayer = true;
					}
					event.delay(1);
				}
			});
		});
	}

	private void shockAttack() {
		npc.animate(currentForm.middleHeadAttackAnim);
		Position src = npc.getSpawnPosition().relative(2, 2);
		int delay = SHOCK_PROJECTILE_1.send(npc, src);
		World.sendGraphics(1664, 0, delay, src);
		for (int[] coords : SHOCK_SPAWN_POINTS) {
			npc.addEvent(event -> {
				Position shockPosition = npc.getSpawnPosition().relative(coords[0], coords[1]);
				event.delay(3);
				SHOCK_PROJECTILE_2.send(src, shockPosition);
				event.delay(2);
				for (int i = 0; i < 12; i++) {
					if (target == null)
						return;
					int moveX = shockPosition.unitVectorX(target.getPosition());
					int moveY = shockPosition.unitVectorY(target.getPosition());
					if (moveX != 0 || moveY != 0) {
						shockPosition.translate(moveX, moveY, 0);
						World.sendGraphics(1666, 0, 0, shockPosition);
						if (target.getPosition().equals(shockPosition)) {
							target.hit(new Hit(npc).randDamage(20));
							damagedPlayer = true;
							damagedByLightning = true;
							target.root(4, true);
							target.player.sendMessage(Color.RED.wrap("The electricity temporarily paralyzes you!"));
							return;
						}
					}
					event.delay(1);
				}
			});
		}

	}

	private void fireAttack() {
		npc.lock();
		npc.faceNone(false);
		npc.addEvent(event -> {
			while (!npc.isAt(npc.getSpawnPosition())) {
				DumbRoute.route(npc, npc.getSpawnPosition().getX(), npc.getSpawnPosition().getY());
				event.delay(1);
			}
			if (target == null) {
				cancelFireAttack();
				return;
			}
			Bounds blockBounds = npc.getBounds();
			blockBounds.forEachPos(pos -> pos.getTile().flagUnmovable());
			if (target.getPosition().inBounds(npc.getBounds())) {
				target.addEvent(e -> {
					target.lock();
					target.getMovement().reset();
					target.player.getMovement().force(-6, 0, 0, 0, 10, 60, Direction.EAST);
					target.animate(1157);
					e.delay(1);
					target.unlock();
				});
				fireLockdownArea = FireArea.WEST;
			} else {
				fireLockdownArea = FireArea.getTargetArea(target, npc);
			}
			if (fireLockdownArea == null) { //?
				cancelFireAttack();
				return;
			}
			target.root(6, true);
			if (target.player != null)
				target.player.sendMessage("The Alchemical Hydra temporarily stuns you!");

			event.delay(1);

			if (currentForm != Form.RED || target == null) {
				cancelFireAttack();
				return;
			}
			firesActive = true;
			flameSpawned = true;
			for (FireArea adjacents : fireLockdownArea.getAdjacents()) {
				npc.face(Direction.getDirection(adjacents.waveStep[0], adjacents.waveStep[1]));
				npc.animate(currentForm.middleHeadAttackAnim);
				for (Position p : adjacents.waveStart) {
					FIRE_PROJECTILE.send(npc, npc.getSpawnPosition().getX() + p.getX(), npc.getSpawnPosition().getY() + p.getY());
				}
				adjacents.coverInFire(npc);
				target.addEvent(e -> { // fire damage
					e.delay(2);
					for (int i = 0; i < 40; i++) {
						if (target == null)
							return;
						if (adjacents.isInFireArea(target, npc)) {
							doFireDamage(target, true);
						}
						e.delay(1);
					}
				});
				event.delay(3);
				if (currentForm != Form.RED || target == null) {
					cancelFireAttack();
					return;
				}
			}
			npc.face(Direction.getDirection(fireLockdownArea.waveStep[0], fireLockdownArea.waveStep[1]));
			npc.animate(currentForm.middleHeadAttackAnim);
			Position firePosition = npc.getSpawnPosition().relative(fireLockdownArea.waveStart[2].getX(), fireLockdownArea.waveStart[2].getY());
			FIRE_PROJECTILE.send(npc, firePosition);
			sendTrackingFire(firePosition);

			event.delay(2);
			npc.face(target);
			npc.unlock();
			event.delay(40);
			cancelFireAttack();
		});
	}

	private void sendTrackingFire(Position firePosition) {
		target.addEvent(e -> {
			e.delay(1);
			World.sendGraphics(1668, 0, 0, firePosition);
			e.delay(1);
			int fireLife = 18;
			int buildUp = 0;
			while (fireLife > 0 && target != null && currentForm == Form.RED) {
				int x = firePosition.unitVectorX(target.getLastPosition());
				int y = firePosition.unitVectorY(target.getLastPosition());
				if (x == 0 && y == 0) {
					if (++buildUp >= 2) {
						target.hit(new Hit().randDamage(20));
						damagedPlayer = true;
						doFireDamage(target, false);
						fireLife -= 3;
						buildUp = 0;
						e.delay(2);
					}
				} else {
					firePosition.translate(x, y, 0);
					World.sendGraphics(1668, 0, 0, firePosition);
					registerFire(firePosition);
				}
				fireLife--;
				e.delay(1);
			}
		});
	}

	private void registerFire(Position firePosition) {
		Position finalFirePosition = firePosition.copy();
		npc.addEvent(event -> {
			for (int i = 0; i < 40 && target != null; i++) {
				if (target.getPosition().equals(finalFirePosition)) {
					doFireDamage(target, false);
				}
				event.delay(1);
			}
		});
	}

	private void doFireDamage(Entity entity, boolean move) {
		if (move && entity.player != null) {
			Bounds bounds = fireLockdownArea.getBounds(npc);
			int centerX = (bounds.swX + bounds.neX) / 2;
			int centerY = (bounds.swY + bounds.neY) / 2;
			Position center = new Position(centerX, centerY, npc.getHeight());
			entity.player.getMovement().force(target.getPosition().unitVectorX(center) * 3, target.getPosition().unitVectorY(center) * 3, 0, 0, 10, 60,
				Direction.getDirection(target.getPosition().unitVectorX(center) * 3, target.getPosition().unitVectorY(center) * 3));
			entity.animate(1157);
		}
		entity.forceText("Yowch!");
		World.startEvent(event -> {
			for (int i = 0; target != null && i < 4; i++) {
				target.hit(new Hit().fixedDamage(5));
				damagedPlayer = true;
				hitByFlame = true;
				event.delay(2);
			}
		});
	}

	private void cancelFireAttack() {
		npc.getBounds().forEachPos(pos -> pos.getTile().unflagUnmovable());
		firesActive = false;
		npc.unlock();
	}

	@Override
	public void startDeath(Hit killHit) {
		setDead(true);
		if (target != null) {
			reset();
		}

		npc.addEvent(event -> {
			npc.animate(8257);
			event.delay(3);
			npc.transform(8622); // Death form
			npc.animate(8258);
			super.startDeath(killHit);
			event.delay(10);
			resetBoss();
		});
	}

	private void resetBoss() {
		npc.transform(8615); // Reset to initial form
		currentForm = Form.GREEN;
		currentPhase = 0;
		inTransition = false;
		resistant = true;
		power = 0;
		attackCounter = -3;
		lastSpecial = -1;
	}

	private enum FireArea {
		WEST(-8, 0, -1, 4,
			new Position[]{
				new Position(-1, 4, 0),
				new Position(-1, 3, 0),
				new Position(-1, 2, 0),
				new Position(-1, 1, 0)
			}, new int[]{-1, 0}),
		NORTH_WEST(-8, 5, -1, 14,
			new Position[]{
				new Position(-1, 4, 0),
				new Position(-1, 5, 0),
				new Position(-1, 6, 0),
				new Position(0, 6, 0),
				new Position(1, 6, 0),
			}, new int[]{-1, 1}),
		NORTH(0, 5, 5, 14,
			new Position[]{
				new Position(1, 6, 0),
				new Position(2, 6, 0),
				new Position(3, 6, 0),
				new Position(4, 6, 0),
			}, new int[]{0, 1}),
		NORTH_EAST(6, 5, 13, 13,
			new Position[]{
				new Position(6, 4, 0),
				new Position(6, 5, 0),
				new Position(6, 6, 0),
				new Position(5, 6, 0),
				new Position(4, 6, 0),

			}, new int[]{1, 1}),
		EAST(5, 0, 13, 5,
			new Position[]{
				new Position(6, 1, 0),
				new Position(6, 2, 0),
				new Position(6, 3, 0),
				new Position(6, 4, 0),
			}, new int[]{1, 0}),
		SOUTH_EAST(5, -8, 13, -1,
			new Position[]{
				new Position(6, 1, 0),
				new Position(6, 0, 0),
				new Position(6, -1, 0),
				new Position(5, -1, 0),
				new Position(4, -1, 0),
			}, new int[]{1, -1}),
		SOUTH(0, -9, 5, 0,
			new Position[]{
				new Position(1, -1, 0),
				new Position(2, -1, 0),
				new Position(3, -1, 0),
				new Position(4, -1, 0),
			}, new int[]{0, -1}),
		SOUTH_WEST(-8, -8, 0, -1,
			new Position[]{
				new Position(-1, 1, 0),
				new Position(-1, 0, 0),
				new Position(-1, -1, 0),
				new Position(0, -1, 0),
				new Position(1, -1, 0),
			}, new int[]{-1, -1});

		int swX, swY, neX, neY;

		FireArea(int swX, int swY, int neX, int neY, Position[] waveStart, int[] waveStep) {
			this.swX = swX;
			this.swY = swY;
			this.neX = neX;
			this.neY = neY;
			this.waveStart = waveStart;
			this.waveStep = waveStep;
		}

		Position[] waveStart;
		int[] waveStep;

		private FireArea[] getAdjacents() {
			int previous = ordinal() - 1;
			if (previous < 0)
				previous = values().length + previous;
			int next = ordinal() + 1;
			return new FireArea[]{values()[previous % values().length], values()[next % values().length]};
		}

		private void coverInFire(NPC hydra) {
			Position[] wave = new Position[waveStart.length];
			for (int i = 0; i < wave.length; i++)
				wave[i] = hydra.getSpawnPosition().relative(waveStart[i].getX(), waveStart[i].getY());
			for (int i = 0; i < 8; i++) {
				for (Position pos : wave) {
					{
						World.sendGraphics(1668, 0, 50 + i * 5, pos);
					}
					pos.translate(waveStep[0], waveStep[1], 0);
				}
			}
		}

		private boolean isInArea(Entity target, NPC hydra) {
			Bounds bounds = getBounds(hydra);
			if (target.getPosition().inBounds(bounds))
				return true;
			return false;
		}

		private Bounds getBounds(NPC hydra) {
			return new Bounds(hydra.getSpawnPosition().relative(swX, swY), hydra.getSpawnPosition().relative(neX, neY), hydra.getSpawnPosition().getZ());
		}

		private boolean isInFireArea(Entity target, NPC hydra) { // there's probably a math based way to do this, but right now i can't
			Position[] wave = new Position[waveStart.length];
			for (int i = 0; i < wave.length; i++)
				wave[i] = hydra.getSpawnPosition().relative(waveStart[i].getX(), waveStart[i].getY());
			for (int i = 0; i < 8; i++) {
				for (Position pos : wave) {
					if (target.getPosition().equals(pos))
						return true;
					pos.translate(waveStep[0], waveStep[1], 0);
				}
			}
			return false;
		}

		private static FireArea getTargetArea(Entity target, NPC hydra) {
			for (FireArea area : values()) {
				if (area.isInArea(target, hydra))
					return area;
			}
			return null;
		}

	}

}
