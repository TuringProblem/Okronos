package io.ruin.model.activities.bosses.galvek;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.AttackNpcListener;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.TickDelay;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class Galvek extends NPCCombat {
    /*
    Anims: 7900-7916

     */

	AttackStyle previousStyle = null;
	AttackStyle style = null;


	private boolean quickAttackStarted = false;
	private boolean knockBackCooldown = false;

	private int attackCounter = 0;

	private static final int FIRE_GALVEK = 8095;
	private static final int EARTH_GALVEK = 8098;
	private static final int AIR_GALVEK = 8097;
	private static final int WATER_GALVEK = 8096;
	private static final int WAVE = 8099;

	Position airPosition;// = new Position(1629, 5725, 2);
	Position firePosition;// = new Position(1621, 5721, 2);
	Position earthPosition;// = new Position(1628, 5717, 2);

	private AttackStyle lastBasicAttackStyle = Random.rollPercent(50) ? AttackStyle.MAGIC : AttackStyle.RANGED;

	private Position lastShadowPortal;
	private int shadowsSpawned = 0;

	private TickDelay transitioning = new TickDelay();


	private Bounds galvekLair;// = new Bounds(1626, 5711, 1636, 5732, 2);
	private Bounds portalBounds;// = new Bounds(1628, 5717, 1634, 5726, 2);

	private static final Projectile METEOR_DROP_PROJECTILE = new Projectile(1482, 150, 0, 0, 135, 0, 0, 0);
	private static final Projectile METEOR_BOMB_PROJECTILE = new Projectile(1481, 35, 0, 30 + 20, 180 - 30, 0, 55, 255);
	private static final Projectile EARTH_BOMB_PROJECTILE = new Projectile(1329, 35, 0, 30 + 20, 180 - 30, 0, 55, 255);
	private static final Projectile EARTH_BOMB_BREAKOFF_PROJECTILE = new Projectile(1731, 0, 0, 30 + 20, 180 - 30, 0, 55, 255);

	private static final Projectile MAGIC_PROJECTILE = new Projectile(1375, 90, 43, 35, 40, 6, 16, 192);
	private static final Projectile RANGED_PROJECTILE = new Projectile(1598, 90, 43, 35, 40, 6, 16, 192);
	private static final Projectile MAGIC_SPHERE = new Projectile(1341, 90, 43, 35, 100, 1, 16, 192);
	private static final Projectile RANGED_SPHERE = new Projectile(1343, 90, 43, 35, 100, 1, 16, 192);
	private static final Projectile MELEE_SPHERE = new Projectile(1345, 90, 43, 35, 100, 1, 16, 192);

	private boolean canBeAttacked = true;

	private final int TORNADO_GFX = 1608;
	private final int TORNADO_NPC_ID = 20015;
	int quickAttacksDone = 0;

	TickDelay healDelay = new TickDelay();

	int activeMeteors = 0;
	final int MAX_METEORS = 15;

	private ArrayList players = new ArrayList();

	ArrayList<GameObject> bombs = new ArrayList<>();
	ArrayList<NPC> waves = new ArrayList<>();
	ArrayList<NPC> tornado = new ArrayList<>();




	@Override
	public void init() {
		npc.hitListener = new HitListener().preDamage(this::postDamage).preDefend(this::preHitDefend);
		AttackNpcListener listener = (p, npc, message) -> canBeAttacked;
		npc.isMovementBlocked(true, false);
		npc.attackNpcListener = listener;
		airPosition = new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 29, npc.getPosition().getZ());
		firePosition = new Position(npc.getPosition().getRegion().baseX + 21, npc.getPosition().getRegion().baseY + 25, npc.getPosition().getZ());
		earthPosition = new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 21, npc.getPosition().getZ());
		galvekLair = new Bounds(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 15, npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 36, npc.getPosition().getZ());
		portalBounds = new Bounds(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 21, npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 30, npc.getPosition().getZ());
	}

	@Override
	public void follow() {
		//leave empty so npc does not walk
	}


	@Override
	public int getDefendAnimation() {
		int animation = 0;
		switch (npc.getId()) {
			case AIR_GALVEK:
			case WATER_GALVEK:
				animation = 7903;
				break;
			case FIRE_GALVEK:
			case EARTH_GALVEK:
				animation = 7902;
				break;
		}
		return animation;
	}

	@Override
	protected boolean withinDistance(int distance) {
		if (target != null && target.getPosition().inBounds(galvekLair)) {
			return super.withinDistance(distance);
		}
		return super.withinDistance(distance);
	}

	@Override
	public void startDeath(Hit killHit) {
		waves.forEach(w -> {
			w.remove();
		});
		tornado.forEach(t -> {
			t.remove();
		});
		waves.clear();
		tornado.clear();
		attackCounter = 0;
		npc.animate(7915);
		npc.transform(WATER_GALVEK);
		npc.setHeadIcon(NPC.DefaultHeadIconIndex.RangeAndMelee);
		if (!damagedPlayer) {
			npc.getPosition().getRegion().players.forEach(p -> {
				Objects.requireNonNull(p.combatAchievementsList.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_GALVEK.ordinal())).
					getCombatAchievement()).check(p);
			});
		}
		damagedPlayer = false;
		super.startDeath(killHit);
	}

	@Override
	public int getAttackBoundsRange() {
		return 20;
	}

	@Override
	public int getAggressionRange() {
		return 20;
	}


	@Override
	public boolean allowRetaliate(Entity attacker) {
		if (transitioning.isDelayed())
			return false;
		else
			return super.allowRetaliate(attacker);
	}

	public boolean timerStarted = false;

	@Override
	public boolean attack() {
		if (!timerStarted) {
			timerStarted = true;
			target.player.galvekTimer = new ActivityTimer();
		}

		if (target != null) {
			npc.face(target);
		}
		if (transitioning.isDelayed())
			return false;
		List<Player> targets = getAllTargets();
		if (npc.getId() == FIRE_GALVEK) {
			if (activeMeteors < MAX_METEORS)
				fallingMeteors(2);
		}
		switch (attackCounter) {
			case 3:
				healthSiphon();
				break;
			case 6:
				basicAttack(targets);
				if (shadowsSpawned < 1)
					spawnFirstShadowPortal();
				break;
			case 9:
				quickAttack(quickAttacksDone);
				break;
			case 2:
				switch (npc.getId()) {
					case WATER_GALVEK:
						waveAttack();
						break;
					case EARTH_GALVEK:
						earthBombAttack();
						break;
					case AIR_GALVEK:
					case FIRE_GALVEK:
						meteorBomb();
						break;
				}
				attackCounter++;
				break;
			default:
				basicAttack(targets);
				attackCounter++;
				break;
		}
		return true;
	}

	private void resetGalvek() {
		npc.setId(WATER_GALVEK);
		npc.setHeadIcon(NPC.DefaultHeadIconIndex.RangeAndMelee);
		npc.getMovement().teleport(npc.spawnPosition);
		restore();
		npc.getCombat().reset();
		npc.isMovementBlocked(true, false);
		waves.forEach(w -> {
			w.remove();
		});
		waves.clear();
		tornado.forEach(t -> {
			t.remove();
		});
		tornado.clear();
		npc.isMovementBlocked(true, false);
		attackCounter = 0;
		shadowsSpawned = 0;
	}

    /*
    Throws 5 attacks at the player and the player has to switch prayers based on the attack
     */

	private void quickAttack(int attacksDone) {
		quickAttacksDone++;
		int finalQuickAttacksDone = attacksDone;
		getAllTargets().forEach(p -> {
			npc.addEvent(event -> {
				String message = "";
				event.delay(1);
				Projectile projectile;
				Prayer prayer;
				int hitGfx;

				if (previousStyle == null) {
					style = Random.get() < 1d / 3 ? AttackStyle.MAGIC : (Random.get() < 1d / 2 ? AttackStyle.RANGED : AttackStyle.STAB);
				} else {
					switch (previousStyle) {
						case STAB:
							style = Random.get(0, 1) == 0 ? AttackStyle.MAGIC : AttackStyle.RANGED;
							break;
						case MAGIC:
							style = Random.get(0, 1) == 0 ? AttackStyle.STAB : AttackStyle.RANGED;
							break;
						case RANGED:
							style = Random.get(0, 1) == 0 ? AttackStyle.MAGIC : AttackStyle.STAB;
							break;
					}
				}
				switch (style) {
					case MAGIC:
						message = Color.PURPLE.wrap("Galvek fires a sphere of magical power your way.");
						projectile = MAGIC_SPHERE;
						hitGfx = 1342;
						prayer = Prayer.PROTECT_FROM_MAGIC;
						break;
					case RANGED:
						message = Color.DARK_GREEN.wrap("Galvek fires a sphere of accuracy and dexterity your way.");
						projectile = RANGED_SPHERE;
						hitGfx = 1344;
						prayer = Prayer.PROTECT_FROM_MISSILES;
						break;
					case STAB:
						message = Color.RED.wrap("Galvek fires a sphere of aggression your way.");
						projectile = MELEE_SPHERE;
						hitGfx = 1346;
						prayer = Prayer.PROTECT_FROM_MELEE;
						break;
					default:
						return;
				}
				p.sendMessage(message);
				event.delay(3);
				previousStyle = style;
				int delay = projectile.send(npc, p);
				p.graphics(hitGfx, 100, delay);
				npc.animate(7905);
				p.addEvent(handleHit -> {
					handleHit.delay(4);
					if (!p.getPrayer().isActive(prayer)) {
						damagedPlayer = true;
						p.hit(new Hit(npc).randDamage(30, 50));
					}
					handleHit.delay(2);
				});
				if (finalQuickAttacksDone >= 5) {
					attackCounter++;
					quickAttacksDone = 0;
				}
			});
		});
	}

	private void preHitDefend(Hit hit) {
		if (hit.damage > 50)
			hit.damage = 50;

		if (hit.attackStyle != null) {
			switch (npc.getId()) {
				case WATER_GALVEK:
					if (!hit.attackStyle.isMagic()) {
						if (hit.attacker.player != null) {
							hit.attacker.player.sendMessage("Galvek resists your attack!");
							hit.block();
						}
					}
					break;
				case EARTH_GALVEK:
					if (!hit.attackStyle.isMelee()) {
						hit.attacker.player.sendMessage("Galvek resists your attack!");
						hit.block();
					}
					break;
				case AIR_GALVEK:
					if (!hit.attackStyle.isRanged()) {
						hit.attacker.player.sendMessage("Galvek resists your attack!");
						hit.block();
					}
					break;
				case FIRE_GALVEK:
					if (hit.attackStyle.isMelee()) {
						hit.attacker.player.sendMessage("Galvek resists your attack!");
						hit.block();
					}
					break;
			}
		}
	}

	private void postDamage(Hit hit) {

		if (npc.getId() == WATER_GALVEK && npc.getHp() <= 1000) {
			if (!quickAttackStarted)
				transformToEarthGalvek();
		} else if (npc.getHp() <= 650 && npc.getId() == EARTH_GALVEK) {
			if (!quickAttackStarted)
				transformToAirGalvek();
		} else if (npc.getHp() <= 350 && npc.getId() == AIR_GALVEK) {
			if (!quickAttackStarted)
				transformToFireGalvek();
		}
	}

	private void transformToAirGalvek() {
		npc.faceNone(false);
		if (target != null)
			target.getCombat().reset();
		npc.animate(7909);
		transitioning.delay(5);
		World.startEvent(event -> {
			canBeAttacked = false;
			event.delay(2);
			npc.getMovement().teleport(airPosition);
			npc.setHp(750);
			npc.transform(AIR_GALVEK);
			npc.setHeadIcon(NPC.DefaultHeadIconIndex.MageAndMelee);
			npc.isMovementBlocked(true, false);
			npc.animate(7907);
			attackCounter = 0;
			event.delay(2);
			canBeAttacked = true;
		});
		// spawnBombs();
		getAllTargets().forEach(p -> {
			p.sendMessage(Color.DARK_GREEN.wrap("A gust of wind nearly knocks you over as Galvek propels himself into the air."));
		});
	}

	boolean transforming = false;

	private void transformToEarthGalvek() {
		if (npc == null)
			return;
		npc.faceNone(false);
		npc.animate(7906);
		transitioning.delay(5);
		World.startEvent(event -> {
			canBeAttacked = false;
			attackCounter = 0;
			npc.setHp(1000);
			event.delay(2);
			npc.getMovement().teleport(earthPosition);
			npc.transform(EARTH_GALVEK);
			npc.setHeadIcon(NPC.DefaultHeadIconIndex.RangeAndMage);
			npc.isMovementBlocked(true, false);
			//TODO: Add a check if player is on earth position when he lands if so damage player  (maybe one hit)
			npc.animate(7908);
			event.delay(2);
			canBeAttacked = true;
		});
		getAllTargets().forEach(p -> {
			p.sendMessage(Color.DARK_GREEN.wrap("The ground shakes as Galvek slams to the floor."));
		});
	}

	private void transformToFireGalvek() {
		npc.faceNone(false);
		if (target != null)
			target.getCombat().reset();
		npc.animate(7906);
		transitioning.delay(5);
		World.startEvent(event -> {
			canBeAttacked = false;
			npc.setHp(1000);
			attackCounter = 0;
			event.delay(2);
			npc.getMovement().teleport(firePosition);
			npc.transform(FIRE_GALVEK);
			npc.removeHeadIcon();
			npc.isMovementBlocked(true, false);
			npc.animate(7908);
			event.delay(2);
			canBeAttacked = true;
		});
		getAllTargets().forEach(p -> {
			p.sendMessage(Color.RED.wrap("You feel a strange heat within as Galvek rises from the flames."));
		});
	}

	private void basicAttack(List<Player> targets) {
		lastBasicAttackStyle = Random.rollPercent(75) ? lastBasicAttackStyle : (lastBasicAttackStyle == AttackStyle.RANGED ? AttackStyle.MAGIC : AttackStyle.RANGED);
		targets.forEach(p -> {
			int delay = (lastBasicAttackStyle == AttackStyle.RANGED ? RANGED_PROJECTILE : MAGIC_PROJECTILE).send(npc, p);
			npc.animate(7904);
			int maxDamage = info.max_damage;
			if (p.getPrayer().isActive(lastBasicAttackStyle == AttackStyle.RANGED ? Prayer.PROTECT_FROM_MISSILES : Prayer.PROTECT_FROM_MAGIC))
				maxDamage = 0;
			else damagedPlayer = true;
			p.hit(new Hit(npc, lastBasicAttackStyle).randDamage(maxDamage).ignorePrayer().clientDelay(delay));
		});
	}

	/*
	Fires a meteor bomb at the players, one hits player on impact 30-60 damage one tile away
	 */
	private void meteorBomb() {
		getAllTargets().forEach(p -> {
			Position targetPos = p.getPosition().copy();
			npc.animate(7910);
			int delay = METEOR_BOMB_PROJECTILE.send(npc, targetPos.getX(), targetPos.getY());
			World.sendGraphics(157, 20, delay, targetPos);
			npc.addEvent(event -> {
				event.delay(3);
				if (isDead())
					return;
				if (p != null && p.getPosition().distance(targetPos) < 1) {
					p.hit(new Hit(npc).randDamage(20, 60).delay(0));
					damagedPlayer = true;
				}
			});
		});
		attackCounter++;
	}

	boolean damagedPlayer = false;

	/*
	Spawns a pool under the players if the player does not move in time they will take damage and
	Galvek will be healed 50 per player.
	 */
	private void healthSiphon() {
		AtomicInteger playersSiphoned = new AtomicInteger();
		int allPlayers = getAllTargets().size();
		npc.addEvent(event -> {
			for (Player p : getAllTargets()) {
				Position pos = p.getPosition().copy();
				//GameObject siphonPool = GameObject.spawn(30033, pos, 10, 0);
				World.sendGraphics(1026, 0, 0, pos);
				int ticks = 0;
				while (ticks++ < 2) {
					World.sendGraphics(1026, 0, 0, pos);
					event.delay(2);
				}
				//siphonPool.setId(30034);
				if (p.getPosition().equals(pos)) {
					p.hit(new Hit(npc).randDamage(10, 15));
					p.graphics(1677);
					playersSiphoned.getAndIncrement();
				}
				// siphonPool.remove();


			}
			if (allPlayers == playersSiphoned.get())
				npc.hit(new Hit(HitType.HEAL).fixedDamage(playersSiphoned.get() * 50 + 100));
			else
				npc.hit(new Hit(HitType.HEAL).fixedDamage(playersSiphoned.get() * 50));

			playersSiphoned.set(0);
		});
		attackCounter++;
	}

	/*
	Handles an action for all the targets in the area
	 */
	private void forAllTargets(Consumer<Player> action) {
		npc.getPosition().getRegion().players.stream()
			.filter(p -> p.getPosition().inBounds(galvekLair))
			.forEach(action);
	}

	/*
	Gets all the targets in the area
	 */
	private List<Player> getAllTargets() {
		return npc.getPosition().getRegion().players.stream()
			.filter(p -> p.getPosition().inBounds(galvekLair))
			.collect(Collectors.toList());
	}

	public int playersInArea() {
		AtomicInteger players = new AtomicInteger();
		forAllTargets(p -> {
			players.getAndIncrement();
		});
		return players.get();
	}


	private void windPushBack() {
		getAllTargets().forEach(p -> {
			if (!knockBackCooldown) {
				int knockedBackYPos = p.getPosition().getY() - 1;
				Position newPos = new Position(p.getPosition().getX(), knockedBackYPos, p.getPosition().getZ());
				p.getMovement().teleport(newPos);
				p.sendMessage("Galvek's strong winds knock you back.");
				knockBackCooldown = true;
				return;
			}
		});
	}


	private void Tornados() {
		npc.animate(7900);
		forAllTargets(p -> {
			NPC tornado = new NPC(TORNADO_NPC_ID).spawn(galvekLair.randomPosition());
			AtomicInteger ticks = new AtomicInteger();
			tornado.startEvent(event -> {
				while (ticks.getAndIncrement() < 30) {
					tornado.animate(-1);
					tornado.graphics(TORNADO_GFX);
					tornado.stepAbs(p.getPosition().getX(), p.getPosition().getY(), StepType.WALK);
					if (tornado.getPosition().isWithinDistance(p.getPosition(), 1)) {
						p.hit(new Hit(npc).randDamage(12, 20));
					}
					event.delay(1);

					if (ticks.get() >= 30)
						tornado.remove();
				}
			});
		});
	}


	@Override
	public void process() {
		if (attackCounter > 10)
			attackCounter = 0;
		if (playersInArea() == 0)
			resetGalvek();

		//windPushBack();
		if (playersInArea() > 0) {
			Player randomPlayer = getAllTargets().get(0);
			if (target == null && playersInArea() > 0) {
				npc.getCombat().setTarget(randomPlayer);
			}
		}


		forAllTargets(p -> {
			if (p.getHp() <= 0 && !healDelay.isDelayed()) {
				npc.hit(new Hit(HitType.HEAL).fixedDamage(99));
				healDelay.delay(5);
			}
		});
	}

	private void earthBombAttack() {
		getAllTargets().forEach(p -> {
			Position targetPos = p.getPosition().copy();
			npc.animate(7910);
			int delay = EARTH_BOMB_PROJECTILE.send(npc, targetPos.getX(), targetPos.getY());
			npc.addEvent(event -> {
				event.delay(2);
				World.sendGraphics(1732, 20, delay, targetPos);
				if (isDead())
					return;
				if (p.getPosition().distance(targetPos) < 1) {
					damagedPlayer = true;
					p.hit(new Hit(npc).randDamage(20, 60).delay(0));
				}
				for (int x = -1; x < 2; x++) {
					for (int y = -1; y < 2; y++) {
						int randX = Random.get(1, 2);
						int randY = Random.get(1, 2);
						int xPos = x * randX + targetPos.getX();
						int yPos = y * randY + targetPos.getY();

						Position breakOffPosition = new Position(xPos, yPos, 2);
						int breakOffDelay = EARTH_BOMB_BREAKOFF_PROJECTILE.send(targetPos.getX() - 2, targetPos.getY() - 2, breakOffPosition.getX(), breakOffPosition.getY());
						World.sendGraphics(1732, 20, breakOffDelay, breakOffPosition);
						if (target != null) {
							target.addEvent(breakOffEvent -> {
								breakOffEvent.delay(4);
								if (isDead())
									return;
								if (p != null && p.getPosition().distance(breakOffPosition) < 1) {
									damagedPlayer = true;
									p.hit(new Hit(npc).randDamage(5, 8).delay(0));
								}
							});
						}
					}
				}
			});
		});
	}

	private void spawnFirstShadowPortal() {
		shadowsSpawned++;
		Position portalPosition = portalBounds.randomPosition();
		//GameObject shadow = GameObject.spawn(SHADOW_PORTAL_OBJECT_ID, portalPosition, 10, 0);
		lastShadowPortal = portalPosition;
		npc.addEvent(event -> {
			event.setCancelCondition(() -> npc.getHp() < 1);
			int ticks = 0;
			while (ticks++ < 8) {
				World.sendGraphics(1359, 0, 0, portalPosition);
				event.delay(1);
			}
			forAllTargets(p -> {
				if (portalPosition.distance(p.getPosition()) > 0) {
					damagedPlayer = true;
					p.hit(new Hit(npc).randDamage(30, 50));
					p.sendMessage("The shadows consume you.");
					p.graphics(1416, 48, 0);
				}
			});
			// shadow.remove();
			spawnSecondShadowPortal();
		});
	}

	private void spawnSecondShadowPortal() {
		shadowsSpawned++;
		if (target != null) {
			target.addEvent(event -> {
				event.setCancelCondition(() -> npc.getHp() < 1);
				Position portalPosition = portalBounds.randomPosition();
				if (portalPosition == lastShadowPortal)
					portalPosition = galvekLair.randomPosition();
				// GameObject shadow = GameObject.spawn(SHADOW_PORTAL_OBJECT_ID, portalPosition, 10, 0);
				int ticks = 0;
				while (ticks++ < 8) {
					World.sendGraphics(1361, 0, 0, portalPosition);
					event.delay(1);
				}
				lastShadowPortal = portalPosition;
				// event.delay(8);
				Position finalPortalPosition = portalPosition;

				forAllTargets(p -> {
					if (finalPortalPosition.distance(p.getPosition()) > 0) {
						damagedPlayer = true;
						p.hit(new Hit(npc).randDamage(30, 50));
						p.sendMessage("The shadows consume you.");
						p.graphics(1416, 48, 0);
					}
				});
				//shadow.remove();
				spawnThirdShadowPortal();
			});
		}
	}

	private void spawnThirdShadowPortal() {
		shadowsSpawned++;
		npc.addEvent(event -> {
			event.setCancelCondition(() -> npc.getHp() < 1);
			Position portalPosition = portalBounds.randomPosition();
			if (portalPosition == lastShadowPortal)
				portalPosition = galvekLair.randomPosition();
			//GameObject shadow =  GameObject.spawn(SHADOW_PORTAL_OBJECT_ID, portalPosition, 10, 0);
			int ticks = 0;
			while (ticks++ < 8) {
				World.sendGraphics(1362, 0, 0, portalPosition);
				event.delay(1);
			}
			lastShadowPortal = portalPosition;
			//event.delay(8);
			Position finalPortalPosition = portalPosition;
			forAllTargets(p -> {
				if (finalPortalPosition.distance(p.getPosition()) > 0) {
					damagedPlayer = true;
					p.hit(new Hit(npc).randDamage(30, 50));
					p.sendMessage("The shadows consume you.");
					p.graphics(1416, 48, 0);
				}
			});
			// shadow.remove();
			attackCounter++;
			shadowsSpawned = 0;
		});
	}


	/*
	A tsunami is spawned and the player has to get out of the way of it or they'll take heavy damage
	 */
	private void waveAttack() {
		ArrayList<Position> possibleSpawns = new ArrayList<>();
		for (int i = npc.getPosition().getRegion().baseX + 26; i <= npc.getPosition().getRegion().baseX + 36; i++) {
			possibleSpawns.add(new Position(i, npc.getPosition().getRegion().baseY + 36, npc.getPosition().getZ()));
		}
		npc.animate(7912);
		for (int i = 0; i < 10; i++) {
			Position spawnLocation = Random.get(possibleSpawns);
			waves.add(new NPC(WAVE).spawn(spawnLocation));
			possibleSpawns.remove(spawnLocation);
		}
		waves.forEach(w -> {
			AtomicInteger ticks = new AtomicInteger();
			w.startEvent(event -> {
				while (ticks.getAndIncrement() < 23) {
					event.delay(1);
					w.step(0, -1, StepType.WALK);
					forAllTargets(p -> {
						if (p.isAt(w.getPosition())) {
							p.hit(new Hit(npc).fixedDamage(p.getHp()));
							damagedPlayer = true;
						}
					});
					if (w.isAt(w.getPosition().getX(), npc.getPosition().getRegion().baseY + 15)) {
						w.remove();
						waves.clear();
					}
				}
			});
		});
		attackCounter++;
	}


	/*
	Meteors fall from the sky and damage the player the connect with.
	 */
	private void fallingMeteors(int delay) {
		npc.addEvent(event -> {
			event.setCancelCondition(this::isDead);
			event.delay(delay);
			while (npc.getId() == FIRE_GALVEK) {
				if (activeMeteors >= MAX_METEORS) return;
				for (int i = 0; i < 2; i++) {
					Position position;
					if (i == 0 && target != null)
						position = target.getPosition().copy();
					else
						position = galvekLair.randomPosition();
					Position src = Random.get() < 0.5 ? position.relative(1, 0) : position.relative(0, 1);
					int projDelay = METEOR_DROP_PROJECTILE.send(src, position);
					activeMeteors++;
					// World.sendGraphics(1358, 0, projDelay, position);
					//npc.addEvent(e -> {
					event.delay(projDelay / 25);
					activeMeteors--;
					forAllTargets(p -> {
						int distance = p.getPosition().distance(position);
						if (distance <= 1) {
							p.hit(new Hit(npc).randDamage(distance == 0 ? 20 : 10));

						}
					});
					// });
				}
				event.delay(3);
			}
		});
	}
}