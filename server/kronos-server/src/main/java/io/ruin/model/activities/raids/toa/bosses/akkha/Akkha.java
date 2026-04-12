package io.ruin.model.activities.raids.toa.bosses.akkha;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.raids.toa.Invocations;
import io.ruin.model.combat.Hit;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.*;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.TickDelay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Akkha extends NPCCombat {

	private static final int MAGE_FORM = 11792;
	private static final int MELEE_FORM = 11790;
	private static final int RANGED_FORM = 11794;

	private static final Projectile MAGIC_PROJECTILE = new Projectile(1304, 60, 31, 25, 35, 10, 0, 32);
	private static final Projectile RANGED_PROJECTILE = new Projectile(2152, 60, 31, 0, 100, 0, 16, 64);

	private boolean invocationsSet = false;
	List<Invocations> invocations = new ArrayList<>();

	TickDelay memoryBlastDelay = new TickDelay();

	Bounds activeAttackBounds = null;

	Bounds southWestQuadrant;
	Bounds southEastQuadrant;
	Bounds northWestQuadrant;
	Bounds northEastQuadrant;
	Position southWestPosition;
	Position southEastPosition;
	Position northWestPosition;
	Position northEastPosition;

	Position shadowPositionOne;
	Position shadowPositionTwo;
	Position shadowPositionThree;
	Position shadowPositionFour;

	boolean firstShadow = true;
	boolean whiteOrbSpecialStarted = false;
	boolean shadowsActive = false;
	int shadowCount = 0;

	List<AkkhaShadow> shadows = new ArrayList<>();
	List<Position> shadowSpawnPositions = new ArrayList<>();

	boolean damagedPlayer = false;

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend).preDamage(this::preDamage);
		invocationsSet = false;
		southWestQuadrant = new Bounds(npc.getPosition().getRegion().baseX + 23, npc.getPosition().getRegion().baseY + 22, npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 31, 1);
		southEastQuadrant = new Bounds(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 22, npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 31, 1);
		northWestQuadrant = new Bounds(npc.getPosition().getRegion().baseX + 23, npc.getPosition().getRegion().baseY + 32, npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 41, 1);
		northEastQuadrant = new Bounds(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 32, npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 41, 1);
		southWestPosition = new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 30, 1);
		southEastPosition = new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 30, 1);
		northWestPosition = new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 33, 1);
		northEastPosition = new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 33, 1);
		quadrants.add(southWestQuadrant);
		quadrants.add(southEastQuadrant);
		quadrants.add(northWestQuadrant);
		quadrants.add(northEastQuadrant);
		quadPositions.add(southWestPosition);
		quadPositions.add(southEastPosition);
		quadPositions.add(northWestPosition);
		quadPositions.add(northEastPosition);
		shadowPositionOne = new Position(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 26, 1);
		shadowPositionTwo = new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 26, 1);
		shadowPositionThree = new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 37, 1);
		shadowPositionFour = new Position(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 37, 1);
		shadowSpawnPositions.add(shadowPositionOne);
		shadowSpawnPositions.add(shadowPositionTwo);
		shadowSpawnPositions.add(shadowPositionThree);
		shadowSpawnPositions.add(shadowPositionFour);
	}

	@Override
	public void startDeath(Hit killHit) {
		if(npc.localPlayers().isEmpty() && npc.getPosition().getRegion().players.isEmpty())
			return;
		Player player = null;
		if(!npc.localPlayers().isEmpty())
			player = npc.localPlayers().getFirst();
		else if (!npc.getPosition().getRegion().players.isEmpty())
			player = npc.getPosition().getRegion().players.getFirst();
		if(player == null)
			return;
		player.getCurrentToARaid().teleportDeadPlayers();
		long tumekenTime = timer == null ? 1000 : timer.stop(player, -1);
		npc.getPosition().getRegion().players.forEach(p -> {
			if (p != null) {
				if (invocations.contains(Invocations.DOUBLE_TROUBLE) && invocations.contains(Invocations.FEELING_SPECIAL) &&
					invocations.contains(Invocations.KEEP_BACK) && invocations.contains(Invocations.STAY_VIGILANT)
					&& p.getCurrentToARaid() != null && p.getCurrentToARaid().getAkkhaPathLevel() >= 4
					&& !p.getCurrentToARaid().deadMembers.contains(p.getName())) {
					Objects.requireNonNull(p.combatAchievementsList
						.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.AKKHANT_DO_IT.ordinal()))
						.getCombatAchievement()).check(p);
				}
				if (!damagedPlayer && p.getCurrentToARaid() != null && !p.getCurrentToARaid().deadMembers.contains(p.getName())
					&& p.getCurrentToARaid().currentParty.getMembers().size() > 1) {
					if (invocations.contains(Invocations.DOUBLE_TROUBLE) && invocations.contains(Invocations.FEELING_SPECIAL) &&
						invocations.contains(Invocations.KEEP_BACK) && invocations.contains(Invocations.STAY_VIGILANT)) {
						Objects.requireNonNull(p.combatAchievementsList
							.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_AKKHA.ordinal()))
							.getCombatAchievement()).check(p);
						if (p.getCurrentToARaid() != null) {
							p.getCurrentToARaid().perfectAkkha = true;
						}
					}
				}
				p.getStats().restore(false);
				p.cureVenom(0);
				p.getCombat().restoreSpecial(100);
				p.getCombat().reset();
				if (p.akkhaBestTime == -1) {
					p.sendMessage("Duration: <col=ef1020>" + format(tumekenTime));
					p.akkhaBestTime = tumekenTime;
					return;
				} else if (p.akkhaBestTime == 0 || tumekenTime < p.akkhaBestTime) {
					p.sendMessage("Duration: <col=ef1020>" + format(tumekenTime) + "</col> (new personal best)");
					p.akkhaBestTime = tumekenTime;
					return;
				} else {
					p.sendMessage("Duration: <col=ef1020>" + format(tumekenTime) + "</col>. Personal best: " + format(p.akkhaBestTime));
					return;
				}
			}

		});
		NPC osmumten = new NPC(11689).spawn(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 30, npc.getHeight());
		osmumten.face(Direction.WEST);
		whiteOrbs.forEach(WhiteOrb::remove);
		whiteBombs.forEach(NPC::remove);
		npc.remove();
	}

	private void preHitDefend(Hit hit) {
		if (activeAttackBounds != null && !npc.getPosition().inBounds(activeAttackBounds) && !whiteOrbSpecialStarted) {
			if (hit.attacker != null && hit.attacker.player != null)
				hit.attacker.player.sendMessage("Akkha's shadow form is protecting him from damage in this area.");
			hit.block();
		}
		if (npc.getId() == MELEE_FORM) {
			if (hit.attacker != null && hit.attacker.player != null && hit.attackStyle != null && !hit.attackStyle.isMagic())
				hit.block();
			else hit.boostAttack(1.5);
		}
		if (npc.getId() == MAGE_FORM) {
			if (hit.attacker != null && hit.attacker.player != null && hit.attackStyle != null && !hit.attackStyle.isRanged())
				hit.block();
		}
		if (npc.getId() == RANGED_FORM) {
			if (hit.attacker != null && hit.attacker.player != null && hit.attackStyle != null && !hit.attackStyle.isMelee())
				hit.block();
		}
		if (whiteOrbSpecialStarted) {
			if (hit.attacker != null && hit.attacker.player != null && hit.attackStyle != null && !hit.attackStyle.isMelee())
				hit.block();
		}
		if (shadowsActive && !whiteOrbSpecialStarted) {
			if (hit.attacker != null && hit.attacker.player != null)
				hit.attacker.player.sendMessage("Akkha's shadows are protecting him from damage.");
			hit.block();
		}
		if (hit.attacker != null) {
			Player hitter = hit.attacker.player;
			if (hitter != null) {
				if (hitter.getCurrentToARaid() != null) {
					hit.boostDefence(hitter.getCurrentToARaid().getAkkhaPathLevel() * 1.1);
				}
			}
		}
	}

	private void preDamage(Hit hit) {
		int boss80Percent = (npc.getMaxHp() * 80) / 100;
		int boss60Percent = (npc.getMaxHp() * 60) / 100;
		int boss40Percent = (npc.getMaxHp() * 40) / 100;
		int boss20Percent = (npc.getMaxHp() * 20) / 100;
		int boss15Percent = (npc.getMaxHp() * 15) / 100;
		if (npc.getHp() - hit.damage < boss80Percent && shadowCount < 1) {
			hit.damage = npc.getHp() - boss80Percent;
			shadowCount++;
			startShadowSpecial();
		} else if (npc.getHp() - hit.damage < boss60Percent && shadowCount < 2) {
			hit.damage = npc.getHp() - boss60Percent;
			shadowCount++;
			startShadowSpecial();
			memoryBlastDelay.delay(100);
			memoryBlast();
			memoryBlasts++;
		} else if (npc.getHp() - hit.damage < boss40Percent && shadowCount < 3) {
			hit.damage = npc.getHp() - boss40Percent;
			shadowCount++;
			startShadowSpecial();
			memoryBlastDelay.delay(100);
			memoryBlast();
			memoryBlasts++;
		} else if (npc.getHp() - hit.damage < boss20Percent && shadowCount < 4) {
			hit.damage = npc.getHp() - boss20Percent;
			shadowCount++;
			startShadowSpecial();
		} else if (npc.getHp() - hit.damage < boss15Percent && !whiteOrbSpecialStarted) {
			hit.damage = npc.getHp() - boss15Percent;
			whiteOrbSpecial();
		}
	}

	private int attacksSinceMoving = 0;
	private int memoryBlasts = 0;

	private void postDamage(Hit hit) {
		if (whiteOrbSpecialStarted) {
			if (hit.damage > 0) {
				attacksSinceMoving++;
				if (attacksSinceMoving >= 5) {
					moveToNextPosition();
					attacksSinceMoving = 0;
				}
			}
		} else {
			if (Random.get(50) == 0 && memoryBlastDelay.remaining() < 1) {
				memoryBlastDelay.delay(100);
				memoryBlast();
				memoryBlasts++;
			}

		}
	}

	Position currentShadowPosition = null;

	private void moveToNextPosition() {
		npc.getPosition().getRegion().players.forEach(p -> {
			if (p != null)
				p.getCombat().reset();
		});
		if (currentShadowPosition == null) {
			currentShadowPosition = Random.get(shadowSpawnPositions);
			npc.getMovement().teleport(currentShadowPosition);
		} else {
			boolean positionChanged = false;
			while (!positionChanged) {
				Position nextPosition = Random.get(shadowSpawnPositions);
				if (nextPosition.distance(currentShadowPosition) > 3) {
					currentShadowPosition = nextPosition;
					positionChanged = true;
					npc.getMovement().teleport(currentShadowPosition);
				}
			}
		}
	}

	private int getHpPercentage() {
		int hp = npc.getHp();
		int maxHp = npc.getMaxHp();
		return (hp * 100) / maxHp;
	}


	private void startShadowSpecial() {


		if (firstShadow) {
			firstShadow = false;

			AkkhaShadow firstShadow = new AkkhaShadow(11797, southWestQuadrant, shadowPositionOne, Direction.SOUTH_WEST);
			shadows.add(firstShadow);
			AkkhaShadow secondShadow = new AkkhaShadow(11797, southEastQuadrant, shadowPositionTwo, Direction.SOUTH_EAST);
			shadows.add(secondShadow);
			AkkhaShadow thirdShadow = new AkkhaShadow(11797, northEastQuadrant, shadowPositionThree, Direction.NORTH_EAST);
			shadows.add(thirdShadow);
			AkkhaShadow fourthShadow = new AkkhaShadow(11797, northWestQuadrant, shadowPositionFour, Direction.NORTH_WEST);
			shadows.add(fourthShadow);
			shadows.forEach(s -> {
				s.getCombat().setAllowRetaliate(false);
				s.getCombat().setAllowRespawn(false);
				s.deathStartListener = (DeathListener.SimpleKiller) killer -> {
					World.startEvent(e -> {
						//s.animate(11799);
						e.delay(2);
						shadows.remove(s);
						s.remove();
						activeAttackBounds = s.getQuadrant();
						shadowsActive = false;
						shadows.forEach(shad -> {
							shad.setCanAttack(false);
						});
					});
				};
			});
		}
		shadows.forEach(shad -> {
			shad.setCanAttack(true);
		});
		if (shadows.isEmpty()) return;
		shadowsActive = true;

	}

	@Override
	public void follow() {
		switch (npc.getId()) {
			case MAGE_FORM:
				follow(2);
				break;
			case MELEE_FORM:
				follow(2);
				break;
			case RANGED_FORM:
				follow(2);
				break;
		}
	}

	int attacks = 0;

	@Override
	public boolean attack() {
		if (npc.isHidden()) return false;
		if (Random.get(10) == 0) {
			int special = Random.get(1, 2);
			if (special == 1 && npc.localPlayers().size() > 1) {
				//blackSpecial();
				if (invocations.contains(Invocations.DOUBLE_TROUBLE) && memoryBlastDelay.remaining() < 1) {
					memoryBlastDelay.delay(100);
					memoryBlast();
					memoryBlasts++;
				}
			} else {
				//whiteSpecial();
				if (invocations.contains(Invocations.DOUBLE_TROUBLE) && memoryBlastDelay.remaining() < 1) {
					memoryBlastDelay.delay(100);
					memoryBlast();
					memoryBlasts++;
				}
			}
		}
		List<Player> playersWithin2Distance = npc.getPosition().getRegion().players.stream().filter(p -> p.getPosition().isWithinDistance(npc.getPosition(), 2)).toList();
		if (invocations.contains(Invocations.KEEP_BACK)) {
			if (!playersWithin2Distance.isEmpty()) {
				playersWithin2Distance.forEach(this::meleeAttack);
			}
		}
		switch (npc.getId()) {
			case MAGE_FORM:
				magicAttack();
				break;
			case MELEE_FORM:
				if (withinDistance(2))
					meleeAttack((Player) target);
				break;
			case RANGED_FORM:
				rangedAttack();
				break;
		}
		if (invocations.contains(Invocations.STAY_VIGILANT)) {
			if (Random.get(6) == 0) {
				changeForm();
				return true;
			}
		}
		if (attacks++ == 8) {
			changeForm();
			attacks = 0;
		}
		return true;
	}

	private void meleeAttack(Player target) {
		npc.animate(9770);
		World.startEvent(e -> {
			e.delay(1);
			if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE)) {
				if (target.player != null && target.player.getCurrentToARaid() != null && target.player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
					target.hit(new Hit(npc).randDamage(16).ignorePrayer());
				else
					target.hit(new Hit(npc).randDamage(12).ignorePrayer());
			} else
				target.hit(new Hit(npc).randDamage(42).ignorePrayer());
		});
	}

	public ActivityTimer timer;
	boolean timerSet = false;

	private static String format(long millis) {
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
		return String.format("%02d:%02d", minutes, seconds);
	}

	@Override
	public void process() {
		if (!npc.localPlayers().isEmpty()) {
			if (!timerSet) {
				timer = new ActivityTimer();
				timerSet = true;
			}
			if (!invocationsSet) {
				Player player = npc.localPlayers().getFirst();
				invocationsSet = true;
				invocations = player.getCurrentToARaid().getInvocations();
			}
		}
		if (shadowsActive) {
			shadows.forEach(shad -> {
				shad.setCanAttack(true);
			});
			int shadowIndex = IntStream.range(0, shadows.size())
				.filter(i -> shadows.get(i).getHp() < 1 || shadows.get(i).getCombat().isDead())
				.findFirst()
				.orElse(-1);

			if (shadowIndex != -1) {
				activeAttackBounds = shadows.get(shadowIndex).getQuadrant();
				shadows.get(shadowIndex).remove();
				shadows.remove(shadowIndex);
				shadowsActive = false;
			}
		}
		if (npc.getHp() < 1) {
			for (WhiteOrb orb : whiteOrbs) {
				orb.remove();
			}
		}
	}

	private void magicAttack() {
		npc.animate(9774);
		npc.localPlayers().forEach(p -> {
			if (p.getPosition().inBounds(getBossArea())) {
				int delay = MAGIC_PROJECTILE.send(npc, p);
				int maxDamage = 42;
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
					maxDamage = 12;
					if (target.player != null && target.player.getCurrentToARaid() != null && target.player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
						maxDamage = 16;
				}
				p.hit(new Hit(npc).randDamage(maxDamage).clientDelay(delay));
			}
		});
	}

	int lastMemoryTile = -1;
	List<Bounds> quadrants = new ArrayList<>();
	List<Position> quadPositions = new ArrayList<>();

	boolean memoryBlasting = false;

	private void memoryBlast() {
		if (memoryBlasting)
			return;
		memoryBlasting = true;

		// Pre-calculate all quadrants at start
		List<Bounds> targetQuadrants = new ArrayList<>(quadrants);

		World.startEvent(event -> {
			npc.animate(9776);
			event.delay(1);
			npc.setHidden(true);
			event.delay(3);

			int amountToFire = 4;
			Player player = npc.getPosition().getRegion().players.getFirst();
			if (player != null && player.getCurrentToARaid() != null) {
				if (player.getCurrentToARaid().getAkkhaPathLevel() >= 4) {
					amountToFire = 6;
				} else if (player.getCurrentToARaid().getAkkhaPathLevel() >= 2) {
					amountToFire = 5;
				}
			}

			// Pre-compute memory order for all phases
			List<Integer> memoryOrder = new ArrayList<>();
			for (int i = 0; i < amountToFire; i++) {
				int index;
				do {
					index = Random.get(0, quadrants.size() - 1);
				} while (i > 0 && index == memoryOrder.get(i-1)); // Avoid repeating
				memoryOrder.add(index);

				// Display first graphic immediately
				World.sendGraphics(157, 0, 0, quadPositions.get(index));
				if (i < amountToFire - 1) {
					event.delay(3);
				}
			}
			event.delay(2);
			// Process impacts with fewer events
			for (int i = 0; i < memoryOrder.size(); i++) {
				Bounds currentQuadrant = targetQuadrants.get(memoryOrder.get(i));

				// Build impact quadrants list once
				List<Bounds> impactBounds = new ArrayList<>(targetQuadrants);
				impactBounds.remove(currentQuadrant);

				// Handle all players in one pass
				List<Player> affectedPlayers = new ArrayList<>();
				for (Player p : npc.getPosition().getRegion().players) {
					for (Bounds b : impactBounds) {
						if (p.getPosition().inBounds(b)) {
							affectedPlayers.add(p);
							break; // Player only needs to be hit once
						}
					}
				}

				// Apply damage
				for (Player p : affectedPlayers) {
					p.hit(new Hit(npc).fixedDamage(40).ignorePrayer().ignoreDefence());
				}

				// Send graphics in batches
				for (Bounds b : impactBounds) {
					List<Position> allPositions = b.getAllPositions();
					for (int j = 0; j < allPositions.size(); j += 10) {
						int end = Math.min(j + 10, allPositions.size());
						for (int k = j; k < end; k++) {
							World.sendGraphics(157, 0, 0, allPositions.get(k));
						}
					}
				}

				event.delay(invocations.contains(Invocations.FEELING_SPECIAL) ? 2 : 4);

				if (i == 3) {
					npc.setHidden(false);
					memoryBlasting = false;
				}
			}
		});
	}


	List<Position> getPlusFormation(int centerX, int centerY, int radius) {
		List<Position> plusPositions = new ArrayList<>();

		plusPositions.add(new Position(centerX, centerY));

		for (int i = 1; i <= radius; i++) {
			plusPositions.add(new Position(centerX + i, centerY));  // Right
			plusPositions.add(new Position(centerX - i, centerY));  // Left
		}

		for (int i = 1; i <= radius; i++) {
			plusPositions.add(new Position(centerX, centerY + i));  // Down
			plusPositions.add(new Position(centerX, centerY - i));  // Up
		}

		plusPositions.removeIf(position -> position.getX() == centerX && position.getY() == centerY);

		return plusPositions;
	}

	private void blackSpecial() {
		World.startEvent(e -> {
			for (Player p : npc.localPlayers()) {
				p.graphics(2110);
				e.delay(5);
				List<Position> plusFormation = getPlusFormation(p.getPosition().getX(), p.getPosition().getY(), 5);
				plusFormation.forEach(pos -> {
					World.sendGraphics(157, 0, 0, pos);
				});
				for (Player plr : npc.localPlayers()) {
					if (plusFormation.contains(plr.getPosition())) {
						plr.hit(new Hit(npc).fixedDamage(40).ignorePrayer().ignoreDefence());
					}
				}
			}
		});
	}

	List<WhiteOrb> whiteOrbs = new ArrayList<>();

	private void whiteOrbSpecial() {
		if (whiteOrbSpecialStarted) return;
		whiteOrbSpecialStarted = true;
		whiteBombs.forEach(NPC::remove);
		shadows.forEach(shad -> {
			shad.hit(new Hit(npc).fixedDamage(9999).ignorePrayer().ignoreDefence());
		});
		npc.transform(RANGED_FORM);
		npc.lock();
		npc.getCombat().reset();
		npc.getCombat().setAllowRetaliate(false);
		moveToNextPosition();
		npc.setHeadIcon(NPC.DefaultHeadIconIndex.RangeAndMage);

		// Batch orb creation
		World.startEvent(e -> {
			Bounds bossBounds = getBossArea();
			List<Position> validPositions = new ArrayList<>();

			// First find valid positions to reduce trial and error
			for (int attempts = 0; attempts < 100 && validPositions.size() < 27; attempts++) {
				Position pos = bossBounds.randomPosition();
				if (Tile.get(pos, true).clipping == 0) {
					validPositions.add(pos);
				}
			}

			// Now create orbs in batches of 5
			for (int i = 0; i < validPositions.size(); i += 5) {
				int endIdx = Math.min(i + 5, validPositions.size());
				for (int j = i; j < endIdx; j++) {
					Position pos = validPositions.get(j);
					WhiteOrb whiteOrb = new WhiteOrb(11804, Random.get(3) == 0 ? 2 : 1, pos, getBossArea(), npc);
					whiteOrbs.add(whiteOrb);
				}
				e.delay(1); // Small delay between batches to spread processing
			}
		});
	}

	private Bounds getBossArea() {
		Bounds[] bounds = new Bounds[]{
			southWestQuadrant,
			southEastQuadrant,
			northWestQuadrant,
			northEastQuadrant
		};
		return Random.get(bounds);
	}

	private void whiteSpecial() {
		World.startEvent(event -> {
			event.setCancelCondition(() -> whiteOrbSpecialStarted);
			for (int i = 0; i < 20; i++) {
				if (npc.getPosition().getRegion().players.isEmpty())
					break;
				for (Player p : npc.getPosition().getRegion().players) {
					Position pos = p.getPosition().copy();
					p.graphics(2112);
					event.delay(1);
					if (pos.distance(p.getPosition()) > 0) {
						spawnWhiteBlob(pos);
						if (invocations.contains(Invocations.FEELING_SPECIAL)) {
							if (Tile.get(new Position(pos.getX() - 1, pos.getY(), pos.getZ()), true).clipping == 0)
								spawnWhiteBlob(new Position(pos.getX() - 1, pos.getY(), pos.getZ()));
						}
					}
				}
			}
		});
	}

	List<NPC> whiteBombs = new ArrayList<>();

	private boolean whiteBombInThisPosition(Position pos) {
		for (NPC bomb : whiteBombs) {
			if (bomb.getPosition().distance(pos) < 1)
				return true;
		}
		return false;
	}

	private void spawnWhiteBlob(Position pos) {
		if (whiteBombInThisPosition(pos))
			return;
		NPC whiteBlob = new NPC(11800).spawn(pos);
		whiteBlob.getDef().occupyTiles = false;
		World.startEvent(e -> {
			int ticks = 0;
			while (ticks++ < 60 && !whiteBlob.isRemoved()) {
				npc.localPlayers().forEach(p -> {
					if (p.getPosition().distance(whiteBlob.getPosition()) < 1) {
						p.hit(new Hit(npc).fixedDamage(p.getCurrentToARaid().getAkkhaPathLevel() >= 2 ? Random.get(25, 50) : Random.get(10, 30)).ignorePrayer().ignoreDefence());
						whiteBlob.remove();
					}
				});
				e.delay(1);
				if (ticks == 59) {
					whiteBombs.remove(whiteBlob);
					whiteBlob.remove();
				}
			}
		});
	}

	private void rangedAttack() {
		npc.animate(9774);
		npc.localPlayers().forEach(p -> {
			if (p.getPosition().inBounds(getBossArea())) {
				int delay = RANGED_PROJECTILE.send(npc, p);
				int maxDamage = 42;
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
					maxDamage = 11;
					if (target.player != null && target.player.getCurrentToARaid() != null && target.player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
						maxDamage = 13;
				}
				p.hit(new Hit(npc).randDamage(maxDamage).clientDelay(delay));
			}
		});
	}

	private void changeForm() {
		if(npc.isHidden())
			return;
		npc.animate(9776);
		switch (npc.getId()) {
			case MAGE_FORM:
				npc.transform(MELEE_FORM);
				break;
			case MELEE_FORM:
				npc.transform(RANGED_FORM);
				break;
			case RANGED_FORM:
				npc.transform(MAGE_FORM);
				break;
		}
	}
}
