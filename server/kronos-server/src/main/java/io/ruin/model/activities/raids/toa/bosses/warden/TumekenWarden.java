package io.ruin.model.activities.raids.toa.bosses.warden;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.raids.toa.Invocations;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.*;
import io.ruin.model.map.object.GameObject;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static io.ruin.model.map.Tile.get;

public class TumekenWarden extends NPCCombat {

	private static Bounds LEFT_SIDE;
	private static Bounds RIGHT_SIDE;
	private static Bounds RIGHT_SIDE_FRONT_BOUNDS;
	private static Bounds LEFT_SIDE_FRONT_BOUNDS;

	private static final int SLAM_LEFT = 9675;

	private static final int SLAM_RIGHT = 9677;

	boolean attackedWithNonMelee = false;

	private static final int SLAM_CENTER = 9678;

	public NPC zebak;

	public NPC baba;
	boolean canAttack = true;

	List<Position> rowOne = new ArrayList<>();
	List<Position> rowTwo = new ArrayList<>();
	List<Position> rowThree = new ArrayList<>();
	List<Position> rowFour = new ArrayList<>();
	List<Position> rowFive = new ArrayList<>();
	List<Position> rowSix = new ArrayList<>();
	List<Position> rowSeven = new ArrayList<>();
	List<Position> rowEight = new ArrayList<>();
	Projectile tileProjectile = new Projectile(2228, 0, 30, 25, 35, 10, 0, 32);
	List<Position> lightningTiles = new ArrayList<>();

	@Override
	public void init() {
		npc.hitListener = new HitListener().preDefend(this::preDefend).postDefend(this::postDefend).postDamage(this::postDamage).preDamage(this::preDamage);
		npc.setIgnoreMulti(true);
		npc.face(Direction.NORTH);

		LEFT_SIDE = new Bounds(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 37, npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 45, 1);
		RIGHT_SIDE = new Bounds(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 37, npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 45, 1);
		RIGHT_SIDE_FRONT_BOUNDS = new Bounds(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 37, npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 41, 1);
		LEFT_SIDE_FRONT_BOUNDS = new Bounds(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 36, npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 41, 1);
		for (int x = 22; x < 43; x++) {
			rowOne.add(new Position(npc.getPosition().getRegion().baseX + x, npc.getPosition().getRegion().baseY + 45, 1));
			rowTwo.add(new Position(npc.getPosition().getRegion().baseX + x, npc.getPosition().getRegion().baseY + 44, 1));
			rowThree.add(new Position(npc.getPosition().getRegion().baseX + x, npc.getPosition().getRegion().baseY + 43, 1));
		}
		for (int x = 23; x < 42; x++) {
			rowFour.add(new Position(npc.getPosition().getRegion().baseX + x, npc.getPosition().getRegion().baseY + 42, 1));
		}
		for (int x = 24; x < 41; x++) {
			rowFive.add(new Position(npc.getPosition().getRegion().baseX + x, npc.getPosition().getRegion().baseY + 41, 1));
		}
		for (int x = 25; x < 40; x++) {
			rowSix.add(new Position(npc.getPosition().getRegion().baseX + x, npc.getPosition().getRegion().baseY + 40, 1));
		}
		for (int x = 26; x < 39; x++) {
			rowSeven.add(new Position(npc.getPosition().getRegion().baseX + x, npc.getPosition().getRegion().baseY + 39, 1));
		}
		for (int x = 27; x < 38; x++) {
			rowEight.add(new Position(npc.getPosition().getRegion().baseX + x, npc.getPosition().getRegion().baseY + 38, 1));
		}
		for (int x = 28; x < 38; x++) {
			lightningTiles.add(new Position(npc.getPosition().getRegion().baseX + x, npc.getPosition().getRegion().baseY + 37, 1));
		}
		lightningTiles.addAll(rowOne);
		lightningTiles.addAll(rowTwo);
		lightningTiles.addAll(rowThree);
		lightningTiles.addAll(rowFour);
		lightningTiles.addAll(rowFive);
		lightningTiles.addAll(rowSix);
		lightningTiles.addAll(rowSeven);
		lightningTiles.addAll(rowEight);


	}


	private void postDamage(Hit hit) {
		if (hit.attacker != null && hit.attackType != null && !hit.attackStyle.isMelee())
			attackedWithNonMelee = true;
	}

	private int getHpPercent() {
		return (npc.getHp() * 100) / npc.getMaxHp();
	}

	private boolean positionInLightningTiles(Position pos) {
		for (Position position : lightningTiles) {
			if (position.distance(pos) < 1)
				return true;
		}
		return false;
	}

	List<Position> unflaggedTiles = new ArrayList<>();

	private void removeTile(Position pos) {
		Tile tile = get(pos.getX(), pos.getY(), pos.getZ(), false);
		if(tile == null)
			return;
		tile.flagUnmovable();
		unflaggedTiles.add(pos);
		tileProjectile.send(pos, new Position(pos.getX(), pos.getY() + 10, pos.getZ()));
		npc.getPosition().getRegion().players.forEach(p -> {
			if (p.getPosition().distance(pos) < 1) {
				p.hit(new Hit().fixedDamage(p.getHp()));
				if (p.getCurrentToARaid() != null)
					p.getCurrentToARaid().failedPerfectWarden = true;
			}
		});
		World.startEvent(e -> {
			e.delay(1);
			if (tile.gameObjects != null)
				tile.gameObjects.forEach(GameObject::remove);
		});
	}

	private void updateLightningPositions(Position pos) {
		for (int i = lightningTiles.size() - 1; i > 0; i--) {
			Position p = lightningTiles.get(i);
			if (p.distance(pos) < 1) {
				lightningTiles.remove(i);
				break;
			}
		}
	}

	boolean enraged = false;
	boolean lightningStarted = false;

	private void handleLightningEvent() {
		if (lightningStarted)
			return;
		lightningStarted = true;
		World.startEvent(event -> {
			while (!npc.isRemoved()) {

				List<Position> dangerousTiles = new ArrayList<>();
				List<Position> safeTiles = new ArrayList<>();

				getLocalPlayers().forEach(p -> {
					Position playerPosition = p.getPosition().copy();
					dangerousTiles.add(playerPosition);
					safeTiles.add(getRandomSafeTileAroundPlayer(playerPosition));
				});
				lightningTiles.forEach(pos -> {
					if (!safeTiles.contains(pos)) {
						dangerousTiles.add(pos);
					}
				});

				int maxTiles = dangerousTiles.size() / 3;
				List<Position> tilesToHit = new ArrayList<>();
				for (int i = 0; i < maxTiles; i++) {
					Position pos = Random.get(dangerousTiles);
					tilesToHit.add(pos);
					dangerousTiles.remove(pos);
				}
				activateTiles(tilesToHit, 2111);

				event.delay(invocations.contains(Invocations.INSANITY) ? 3 : 4);
				for (Position pos : tilesToHit) {
					if (positionInLightningTiles(pos))
						World.sendGraphics(2199, 0, 0, pos);
					getLocalPlayers().forEach(p -> {
						if (p.getPosition().distance(pos) == 0 && npc.getHp() > 0) {
							p.hit(new Hit(npc).randDamage(25, 45));
							if (p.getCurrentToARaid() != null)
								p.getCurrentToARaid().failedPerfectWarden = true;
						}
					});
				}
				event.delay(1);
				for (Position pos : tilesToHit) {
					World.sendGraphics(-1, 0, 0, pos);
				}
			}
		});
	}


	private Position getRandomSafeTileAroundPlayer(Position playerPosition) {
		int offsetX = Random.get(-1, 1);
		int offsetY = Random.get(-1, 1);

		int newX = playerPosition.getX() + offsetX;
		int newY = playerPosition.getY() + offsetY;

		return new Position(newX, newY, playerPosition.getZ());
	}

	private void handleCenterTilesOutwards(final int centerX, final int centerY, final int z) {
		for (int radius = 0; radius <= 10; radius++) {
			for (int x = centerX - radius; x <= centerX + radius; x++) {
				for (int y = centerY - radius; y <= centerY + radius; y++) {
					// Check if the current position is within the specified radius
					if (Math.abs(x - centerX) == radius || Math.abs(y - centerY) == radius) {
						Position newPos = new Position(x, y, z);
						Tile tile = get(x, y, z, false);
						if (tile != null && tile.gameObjects != null)
							World.sendGraphics(2220, 0, radius * 5, newPos);
					}
				}
			}
		}
		World.startEvent(e -> {
			Bounds starterBounds = new Bounds(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 39,
				npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 42, 1);
			Bounds secondaryBounds = new Bounds(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 34,
				npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 45, 1);
			List<Position> damagingSecondaryTiles = new ArrayList<>();
			List<Position> damagingPrimaryTiles = new ArrayList<>(starterBounds.getAllPositions());
			secondaryBounds.forEachPos(pos -> {
				boolean inPrimary = false;
				for (Position position : damagingPrimaryTiles) {
					if (position.distance(pos) < 1) {
						inPrimary = true;
						break;
					}
				}
				if (!inPrimary)
					damagingSecondaryTiles.add(pos);
			});
			getLocalPlayers().forEach(p -> {
				for (Position pos : damagingPrimaryTiles) {
					if (pos.distance(p.getPosition()) < 1) {
						p.hit(new Hit(npc).randDamage(104, 273));
						if (p.getCurrentToARaid() != null)
							p.getCurrentToARaid().failedPerfectWarden = true;
					}
				}
			});
			e.delay(2);
			canAttack = true;
			if (!invocations.contains(Invocations.INSANITY)) {
				attacks = 0;
			}
			attackSpeed = 1;
			getLocalPlayers().forEach(p -> {
				for (Position pos : damagingSecondaryTiles) {
					if (pos.distance(p.getPosition()) < 1) {
						p.hit(new Hit(npc).randDamage(104, 273));
						if (p.getCurrentToARaid() != null)
							p.getCurrentToARaid().failedPerfectWarden = true;
					}
				}
			});
		});
	}

	private void activateTiles(List<Position> tiles, int graphicsId) {
		int amount;
		if (invocations.contains(Invocations.INSANITY))
			amount = 2;
		else {
			amount = 4;
		}
		World.startEvent(e -> {
			for (int i = 0; i < amount; i++) {
				tiles.forEach(pos -> {
					if (positionInLightningTiles(pos))
						World.sendGraphics(graphicsId, 0, 0, pos);
				});
				e.delay(1);
			}
		});

	}

	private List<Player> getLocalPlayers() {
		return npc.getPosition().getRegion().players;
	}

	private void startFloorBreakingEvent() {
		if (enraged || skullsSent < 4)
			return;
		enraged = true;
		World.startEvent(e -> {
			npc.animate(9685);
			e.delay(2);
			int players = npc.getPosition().getRegion().players.size();
			int heal = 1400 * players;
			npc.hit(new Hit(HitType.HEAL).fixedDamage(heal));
			handleLightningEvent();
		});
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.isRemoved());
			e.delay(invocations.contains(Invocations.INSANITY) ? 2 : 5);
			for (Position pos : rowOne) {
				removeTile(pos);
				updateLightningPositions(pos);
			}
			e.delay(invocations.contains(Invocations.INSANITY) ? 8 : 15);
			for (Position pos : rowTwo) {
				removeTile(pos);
				updateLightningPositions(pos);
			}
			e.delay(invocations.contains(Invocations.INSANITY) ? 9 : 18);
			for (Position pos : rowThree) {
				removeTile(pos);
				updateLightningPositions(pos);
			}
			e.delay(invocations.contains(Invocations.INSANITY) ? 8 : 15);
			for (Position pos : rowFour) {
				removeTile(pos);
				updateLightningPositions(pos);
			}
			e.delay(invocations.contains(Invocations.INSANITY) ? 7 : 15);
			for (Position pos : rowFive) {
				removeTile(pos);
				updateLightningPositions(pos);
			}
			e.delay(invocations.contains(Invocations.INSANITY) ? 7 : 15);
			for (Position pos : rowSix) {
				removeTile(pos);
				updateLightningPositions(pos);
			}
			e.delay(invocations.contains(Invocations.INSANITY) ? 8 : 15);
			for (Position pos : rowSeven) {
				removeTile(pos);
				updateLightningPositions(pos);
			}
			e.delay(invocations.contains(Invocations.INSANITY) ? 10 : 17);
			for (Position pos : rowEight) {
				removeTile(pos);
				updateLightningPositions(pos);
			}
		});
	}

	public Position getAbsolute(int localX, int localY) {
		return new Position(npc.getPosition().getRegion().baseX + localX, npc.getPosition().getRegion().baseY + localY, npc.getPosition().getZ());
	}


	@Override
	public void follow() {
	}

	private int getHpPercentage() {
		return (npc.getHp() * 100) / npc.getMaxHp();
	}

	int skullsSent = 0;
	boolean zebakSpawned = false;
	boolean babaSpawned = false;

	private void postDefend(Hit hit) {


	}

	private void preDamage(Hit hit) {
		int boss80Percent = (npc.getMaxHp() * 85) / 100;
		int boss60Percent = (npc.getMaxHp() * 65) / 100;
		int boss40Percent = (npc.getMaxHp() * 45) / 100;
		int boss20Percent = (npc.getMaxHp() * 27) / 100;
		int boss15Percent = (npc.getMaxHp() * 15) / 100;
		if (npc.getHp() - hit.damage < boss80Percent && skullsSent == 0 && !skullsActive) {
			hit.damage = npc.getHp() - boss80Percent;
			npc.clearHits();
			sendSkulls();
			zebakSpawned = true;
			zebak = new NPC(11774).spawn(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 33, 1, Direction.NORTH_WEST, 0);

		}
		if (npc.getHp() - hit.damage < boss60Percent && skullsSent == 1 && !skullsActive) {
			hit.damage = npc.getHp() - boss60Percent;
			npc.clearHits();
			sendSkulls();
			babaSpawned = true;
			baba = new NPC(11775).spawn(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 33, 1, Direction.NORTH_EAST, 0);
		}
		if (npc.getHp() - hit.damage < boss40Percent && skullsSent == 2 && !skullsActive) {
			hit.damage = npc.getHp() - boss40Percent;
			npc.clearHits();
			sendSkulls();
		}
		if (npc.getHp() - hit.damage < boss20Percent && skullsSent == 3 && !skullsActive) {
			hit.damage = npc.getHp() - boss20Percent;
			npc.clearHits();
			sendSkulls();
		}
		if (npc.getHp() - hit.damage < boss15Percent && !skullsActive && !enraged && skullsSent == 4) {
			hit.damage = npc.getHp() - boss15Percent;
			npc.clearHits();
			startFloorBreakingEvent();
		}

	}


	private void preDefend(Hit hit) {
		if (skullsActive || !canAttack) {
			if (hit.attacker != null && hit.attacker.isPlayer())
				hit.block();
		}
	}

	int attacks = 0;

	@Override
	public boolean attack() {
		return false;
	}

	int attackSpeed = 4;
	List<Invocations> invocations = new ArrayList<>();
	boolean invocationsSet = false;
	public ActivityTimer timer;
	boolean timerSet = false;
	boolean initialized = false;
	boolean fightStarted = false;

	@Override
	public void process() {
		if (!npc.localPlayers().isEmpty()) {
			if (!initialized) {
				initialized = true;
				World.startEvent(e -> {
					e.delay(2);
					fightStarted = true;
				});
			}
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
		int ticksUntilAttack = 5;
		if (invocations.contains(Invocations.OVERCLOCKED))
			ticksUntilAttack--;
		if (invocations.contains(Invocations.OVERCLOCKED2))
			ticksUntilAttack--;
		if (invocations.contains(Invocations.INSANITY))
			ticksUntilAttack--;
		if (canAttack && !enraged && fightStarted && !skullsActive) {
			if (attackSpeed-- <= 0) {
				attackSpeed = ticksUntilAttack;
				if (attacks == 0)
					leftAttack();
				else if (attacks == 1)
					rightAttack();
				else if (attacks == 2)
					drawLCenterAttack();
				attacks++;
				if (attacks == 3)
					attacks = 0;
			}
		}
		if (skullsActive && ticksTilSkullMode-- <= 0) {
			ticksTilSkullMode = 2;
			handleSkullMode();
		}
	}

	private int ticksTilSkullMode = 2;

	@Override
	public void startDeath(Hit killHit) {
		Player player = npc.getPosition().getRegion().players.get(0);
		Position teleportCrystalPos = npc.getPosition().copy();

		npc.animate(-1);
		npc.animate(9682);
		if (player != null) {
			player.getCurrentToARaid().teleportDeadPlayers();
			Instant startTime = player.getCurrentToARaid().raidStartTime;
			Instant finishTime = Instant.now();
			Duration duration = Duration.between(startTime, finishTime);
			long minutes = duration.toMinutes();
			if (player.getCurrentToARaid() != null) {
				if (player.getCurrentToARaid().getInvocations().contains(Invocations.WALK_FOR_IT)) {
					if (minutes >= 30) {
						player.getCurrentToARaid().currentParty.getMembers().forEach(p -> {
							Player plr = World.getPlayer(p);
							if (plr != null) {
								plr.sendMessage("You did not complete the raid in time and have been penalized.");
							}
						});
						player.getCurrentToARaid().setInvocationLevel(player.getCurrentToARaid().getInvocationLevel() - 20);
					}
				} else if (player.getCurrentToARaid().getInvocations().contains(Invocations.JOG_FOR_IT)) {
					if (minutes >= 25) {
						player.getCurrentToARaid().currentParty.getMembers().forEach(p -> {
							Player plr = World.getPlayer(p);
							if (plr != null) {
								plr.sendMessage("You did not complete the raid in time and have been penalized.");
							}
						});
						player.getCurrentToARaid().setInvocationLevel(player.getCurrentToARaid().getInvocationLevel() - 30);
					}
				} else if (player.getCurrentToARaid().getInvocations().contains(Invocations.RUN_FOR_IT)) {
					if (minutes >= 22) {
						player.getCurrentToARaid().currentParty.getMembers().forEach(p -> {
							Player plr = World.getPlayer(p);
							if (plr != null) {
								plr.sendMessage("You did not complete the raid in time and have been penalized.");
							}
						});
						player.getCurrentToARaid().setInvocationLevel(player.getCurrentToARaid().getInvocationLevel() - 40);
					}
				} else if (player.getCurrentToARaid().getInvocations().contains(Invocations.SPRINT_FOR_IT)) {
					if (minutes >= 18) {
						player.getCurrentToARaid().currentParty.getMembers().forEach(p -> {
							Player plr = World.getPlayer(p);
							if (plr != null) {
								plr.sendMessage("You did not complete the raid in time and have been penalized.");
							}
						});
						player.getCurrentToARaid().setInvocationLevel(player.getCurrentToARaid().getInvocationLevel() - 50);
					}
				}
				long tumekenTime = timer.stop(player, -1);
				player.getCurrentToARaid().currentParty.getMembers().forEach(name -> {
					Player plr = World.getPlayer(name);
					if (plr != null) {
						if (plr.getCurrentToARaid() != null && !plr.getCurrentToARaid().deadMembers.contains(plr.getName())
							&& plr.getCurrentToARaid().getInvocationLevel() >= 300) {
							if (invocations.contains(Invocations.ACCELERATION) && invocations.contains(Invocations.PENETRATION)
								&& invocations.contains(Invocations.PENETRATION)
								&& invocations.contains(Invocations.OVERCLOCKED) && invocations.contains(Invocations.OVERCLOCKED2)
								&& invocations.contains(Invocations.INSANITY)) {
								Objects.requireNonNull(plr.combatAchievementsList
									.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.WARDENT_YOU_BELIEVE_IT.ordinal()))
									.getCombatAchievement()).check(plr);
							}
						}
						if (!attackedWithNonMelee && plr.getCurrentToARaid() != null
							&& !plr.getCurrentToARaid().deadMembers.contains(plr.getName())
							&& plr.getCurrentToARaid().currentParty.getMembers().size() > 1) {
							if (invocations.contains(Invocations.INSANITY)) {
								Objects.requireNonNull(plr.combatAchievementsList
									.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.FANCY_FEET.ordinal()))
									.getCombatAchievement()).check(plr);
							}
						}
						if (plr.getCurrentToARaid() != null && !plr.getCurrentToARaid().failedPerfectWarden
							&& plr.getCurrentToARaid().currentParty.getMembers().size() > 1
							&& !plr.getCurrentToARaid().deadMembers.contains(plr.getName())) {
							if (invocations.contains(Invocations.ACCELERATION) && invocations.contains(Invocations.PENETRATION)
								&& invocations.contains(Invocations.PENETRATION)
								&& invocations.contains(Invocations.OVERCLOCKED) && invocations.contains(Invocations.OVERCLOCKED2)
								&& invocations.contains(Invocations.INSANITY)) {
								Objects.requireNonNull(plr.combatAchievementsList
									.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_WARDENS.ordinal()))
									.getCombatAchievement()).check(plr);
								plr.getCurrentToARaid().perfectWarden = true;
								if (plr.getCurrentToARaid().getInvocationLevel() >= 500) {
									Objects.requireNonNull(plr.combatAchievementsList
										.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.INSANITY.ordinal()))
										.getCombatAchievement()).check(plr);
								}
							}
						}
						if (plr.tumekenBestTime == -1) {
							plr.sendMessage("Duration: <col=ef1020>" + format(tumekenTime));
							plr.tumekenBestTime = tumekenTime;
							return;
						} else if (plr.tumekenBestTime == 0 || tumekenTime < plr.tumekenBestTime) {
							plr.sendMessage("Duration: <col=ef1020>" + format(tumekenTime) + "</col> (new personal best)");
							plr.tumekenBestTime = tumekenTime;
							return;
						} else {
							plr.sendMessage("Duration: <col=ef1020>" + format(tumekenTime) + "</col>. Personal best: " + format(plr.tumekenBestTime));
							return;
						}
					}
					if (plr != null) {
						plr.toaBestTime = plr.toaRaidTimer.stop(plr, plr.toaBestTime);
						if (ActivityTimer.timeInSeconds(plr.toaBestTime) < 600 && plr.getCurrentToARaid() != null) {
							int invoLevel = plr.getCurrentToARaid().getInvocationLevel();
							if (invoLevel > 149 && invoLevel < 300) {
								Objects.requireNonNull(plr.combatAchievementsList
									.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.TOMBS_SPEED_RUNNER.ordinal()))
									.getCombatAchievement()).check(plr);
							} else if (invoLevel >= 300) {
								Objects.requireNonNull(plr.combatAchievementsList
									.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.TOMBS_SPEED_RUNNERII.ordinal()))
									.getCombatAchievement()).check(plr);
							}
							if (plr.getCurrentToARaid().currentParty.getMembers().size() >= 5) {
								Objects.requireNonNull(plr.combatAchievementsList
									.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.TOMBS_SPEED_RUNNERIII.ordinal()))
									.getCombatAchievement()).check(plr);
							}
						}
					}
				});
			}
		}
		unflaggedTiles.forEach(pos -> {
			Tile tile = get(pos.getX(), pos.getY(), pos.getZ(), true);
			if (tile != null)
				tile.unflagUnmovable();
		});
		World.startEvent(e -> {
			if (zebak != null)
				zebak.remove();
			if (baba != null)
				baba.remove();
			e.delay(2);
			GameObject teleportCrystal = new GameObject(45138, npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 36, 1, 10, 0).spawn();
			npc.remove();
		});

	}

	private static String format(long millis) {
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
		return String.format("%02d:%02d", minutes, seconds);
	}

	private void floorShake() {
		npc.startEvent(event -> {
			event.delay(2);
			shakeFirst();
			event.delay(2);
			shakeSecond();
			event.delay(2);
			shakeThird();
			event.delay(2);
			shakeFourth();
			event.delay(2);
			floorShake();
		});
	}

	Projectile skullToBossProjectile = new Projectile(2226, 60, 31, 25, 35, 3, 0, 32);

	Projectile skullProjectile = new Projectile(2227, 60, 0, 25, 35, 10, 0, 32);
	List<NPC> skulls = new ArrayList<>();
	List<NPC> aliveSkulls = new ArrayList<>();
	List<Position> skullPositions = new ArrayList<>();
	boolean skullsActive = false;

	private void sendFiveSkulls() {
		if(skullsActive)
			return;
		canAttack = false;
		skullsActive = true;
		skullPositions.clear();
		aliveSkulls.clear();
		skulls.forEach(NPC::remove);
		skulls.clear();
		List<Position> skullPositions = new ArrayList<>();
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 41, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 41, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 44, 1));
		for (Position pos : skullPositions) {
			World.startEvent(e -> {
				int delay = skullProjectile.send(npc, pos);
				e.delay(World.getTicks(delay) + 1);
				NPC skull = new NPC(11772).spawn(pos);
				skull.getCombat().setAllowRetaliate(false);
				skulls.add(skull);
				aliveSkulls.add(skull);
				skullPositions.add(pos);
				skull.hitListener = new HitListener().preDamage(hit -> {
					if (hit.attackStyle != null && hit.attackStyle.isMelee())
						hit.damage = 1;
					else hit.block();
					if(hit.attacker.player != null) {
						hit.attacker.player.getCombat().updateLastAttack(0);
					}
				});
				skull.deathStartListener = (DeathListener.Simple) () -> {
				};
				skull.deathEndListener = (DeathListener.Simple) () -> {
					skull.transform(11773);
					aliveSkulls.remove(skull);
					if (aliveSkulls.isEmpty()) {
						sendSkullsToBoss();
						skullsActive = false;
						skullsSent++;
						if (!invocations.contains(Invocations.INSANITY)) {
							attacks = 0;
						}
						attackSpeed = 1;
					}
				};
			});
		}
		startSkullEvent(invocations.contains(Invocations.INSANITY) ? 7 : 10);
	}

	private void sendTenSkulls() {
		if(skullsActive)
			return;
		canAttack = false;
		skullsActive = true;
		skullPositions.clear();
		aliveSkulls.clear();
		skulls.forEach(NPC::remove);
		skulls.clear();
		List<Position> skullPositions = new ArrayList<>();
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 41, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 42, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 39, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 39, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 42, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 41, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 44, 1));
		for (Position pos : skullPositions) {
			World.startEvent(e -> {
				int delay = skullProjectile.send(npc, pos);
				e.delay(World.getTicks(delay) + 1);
				NPC skull = new NPC(11772).spawn(pos);
				skull.getCombat().setAllowRetaliate(false);
				skulls.add(skull);
				aliveSkulls.add(skull);
				skullPositions.add(pos);
				skull.hitListener = new HitListener().preDamage(hit -> {
					if (hit.attackStyle != null && hit.attackStyle.isMelee())
						hit.damage = 1;
					else hit.block();
					if(hit.attacker.player != null) {
						hit.attacker.player.getCombat().updateLastAttack(0);
					}
				});
				skull.deathStartListener = (DeathListener.Simple) () -> {
				};
				skull.deathEndListener = (DeathListener.Simple) () -> {
					skull.transform(11773);
					aliveSkulls.remove(skull);
					if (aliveSkulls.isEmpty()) {
						sendSkullsToBoss();
						skullsActive = false;
						skullsSent++;
						if (!invocations.contains(Invocations.INSANITY)) {
							attacks = 0;
						}
						attackSpeed = 1;
					}
				};
			});
		}
		startSkullEvent(invocations.contains(Invocations.INSANITY) ? 15 : 20);
	}

	private void sendEightSkulls() {
		if(skullsActive)
			return;
		canAttack = false;
		skullsActive = true;
		skullPositions.clear();
		aliveSkulls.clear();
		skulls.forEach(NPC::remove);
		skulls.clear();
		List<Position> skullPositions = new ArrayList<>();
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 41, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 41, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 41, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 41, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 40, npc.getPosition().getRegion().baseY + 44, 1));
		for (Position pos : skullPositions) {
			World.startEvent(e -> {
				int delay = skullProjectile.send(npc, pos);
				e.delay(World.getTicks(delay) + 1);
				NPC skull = new NPC(11772).spawn(pos);
				skull.getCombat().setAllowRetaliate(false);
				skulls.add(skull);
				aliveSkulls.add(skull);
				skullPositions.add(pos);
				skull.hitListener = new HitListener().preDamage(hit -> {
					if (hit.attackStyle != null && hit.attackStyle.isMelee())
						hit.damage = 1;
					else hit.block();
					if(hit.attacker.player != null) {
						hit.attacker.player.getCombat().updateLastAttack(0);
					}
				});
				skull.deathStartListener = (DeathListener.Simple) () -> {
				};
				skull.deathEndListener = (DeathListener.Simple) () -> {
					skull.transform(11773);
					aliveSkulls.remove(skull);
					if (aliveSkulls.isEmpty()) {
						sendSkullsToBoss();
						skullsActive = false;
						skullsSent++;
						if (!invocations.contains(Invocations.INSANITY)) {
							attacks = 0;
						}
						attackSpeed = 1;
					}
				};
			});
		}
		startSkullEvent(invocations.contains(Invocations.INSANITY) ? 12 : 16);
	}

	private void sendThirteenSkulls() {
		if(skullsActive)
			return;
		canAttack = false;
		skullsActive = true;
		skullPositions.clear();
		aliveSkulls.clear();
		skulls.forEach(NPC::remove);
		skulls.clear();
		List<Position> skullPositions = new ArrayList<>();
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 39, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 41, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 43, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 42, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 40, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 38, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 40, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 42, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 43, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 41, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 39, 1));

		for (Position pos : skullPositions) {
			World.startEvent(e -> {
				int delay = skullProjectile.send(npc, pos);
				e.delay(World.getTicks(delay) + 1);
				NPC skull = new NPC(11772).spawn(pos);
				skull.getCombat().setAllowRetaliate(false);
				skulls.add(skull);
				aliveSkulls.add(skull);
				skullPositions.add(pos);
				skull.hitListener = new HitListener().preDamage(hit -> {
					if (hit.attackStyle != null && hit.attackStyle.isMelee())
						hit.damage = 1;
					else hit.block();
					if(hit.attacker.player != null) {
						hit.attacker.player.getCombat().updateLastAttack(0);
					}
				});
				skull.deathStartListener = (DeathListener.Simple) () -> {
				};
				skull.deathEndListener = (DeathListener.Simple) () -> {
					skull.transform(11773);
					aliveSkulls.remove(skull);
					if (aliveSkulls.isEmpty()) {
						sendSkullsToBoss();
						skullsActive = false;
						skullsSent++;
						if (!invocations.contains(Invocations.INSANITY)) {
							attacks = 0;
						}
						attackSpeed = 1;
					}
				};
			});
		}
		startSkullEvent(invocations.contains(Invocations.INSANITY) ? 11 : 16);
	}

	private void handleSkullMode() {
		npc.animate(9683);
		for (NPC skull : skulls) {
			skullProjectile.send(npc, skull.getPosition());
		}
	}

	private void sendSkulls() {
		canAttack = false;
		npc.animate(9684);
		int players = npc.getPosition().getRegion().players.size();
		if (players == 1 && skullsSent == 0)
			sendFourSkulls();
		else if (players == 1 && skullsSent == 1)
			sendFiveSkulls();
		else if (players == 1 && skullsSent == 2)
			sendSixSkulls();
		else if (players == 1 && skullsSent == 3)
			sendSevenSkulls();
		else if (players == 2 && skullsSent == 0)
			sendSixSkulls();
		else if (players == 2 && skullsSent == 1)
			sendEightSkulls();
		else if (players == 2 && skullsSent == 2)
			sendNineSkulls();
		else if (players == 2 && skullsSent == 3)
			sendTenSkulls();
		else if (players >= 3 && skullsSent == 0)
			sendNineSkulls();
		else if (players >= 3 && skullsSent == 1)
			sendTenSkulls();
		else if (players >= 3 && skullsSent == 2)
			sendThirteenSkulls();
		else if (players >= 3 && skullsSent == 3)
			sendThirteenSkulls();
	}

	private void sendSixSkulls() {
		if(skullsActive)
			return;
		canAttack = false;
		skullsActive = true;
		skullPositions.clear();
		aliveSkulls.clear();
		skulls.forEach(NPC::remove);
		skulls.clear();
		List<Position> skullPositions = new ArrayList<>();
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 42, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 42, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 40, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 40, 1));
		for (Position pos : skullPositions) {
			World.startEvent(e -> {
				int delay = skullProjectile.send(npc, pos);
				e.delay(World.getTicks(delay) + 1);
				NPC skull = new NPC(11772).spawn(pos);
				skull.getCombat().setAllowRetaliate(false);
				skulls.add(skull);
				aliveSkulls.add(skull);
				skullPositions.add(pos);
				skull.hitListener = new HitListener().preDamage(hit -> {
					if (hit.attackStyle != null && hit.attackStyle.isMelee())
						hit.damage = 1;
					else hit.block();
					if(hit.attacker.player != null) {
						hit.attacker.player.getCombat().updateLastAttack(0);
					}
				});
				skull.deathStartListener = (DeathListener.Simple) () -> {
				};
				skull.deathEndListener = (DeathListener.Simple) () -> {
					skull.transform(11773);
					aliveSkulls.remove(skull);
					if (aliveSkulls.isEmpty()) {
						sendSkullsToBoss();
						skullsActive = false;
						skullsSent++;
						if (!invocations.contains(Invocations.INSANITY)) {
							attacks = 0;
						}
						attackSpeed = 1;
					}
				};
			});
		}
		startSkullEvent(invocations.contains(Invocations.INSANITY) ? 8 : 12);
	}

	private void sendSevenSkulls() {
		if(skullsActive)
			return;
		canAttack = false;
		skullsActive = true;
		skullPositions.clear();
		aliveSkulls.clear();
		skulls.forEach(NPC::remove);
		skulls.clear();
		List<Position> skullPositions = new ArrayList<>();
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 41, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 41, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 41, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 41, 1));
		for (Position pos : skullPositions) {
			World.startEvent(e -> {
				int delay = skullProjectile.send(npc, pos);
				e.delay(World.getTicks(delay) + 1);
				NPC skull = new NPC(11772).spawn(pos);
				skull.getCombat().setAllowRetaliate(false);
				skulls.add(skull);
				aliveSkulls.add(skull);
				skullPositions.add(pos);
				skull.hitListener = new HitListener().preDamage(hit -> {
					if (hit.attackStyle != null && hit.attackStyle.isMelee())
						hit.damage = 1;
					else hit.block();
					if(hit.attacker.player != null) {
						hit.attacker.player.getCombat().updateLastAttack(0);
					}
				});
				skull.deathStartListener = (DeathListener.Simple) () -> {
				};
				skull.deathEndListener = (DeathListener.Simple) () -> {
					skull.transform(11773);
					aliveSkulls.remove(skull);
					if (aliveSkulls.isEmpty()) {
						sendSkullsToBoss();
						skullsActive = false;
						skullsSent++;
						if (!invocations.contains(Invocations.INSANITY)) {
							attacks = 0;
						}
						attackSpeed = 1;
					}
				};
			});
		}
		startSkullEvent(invocations.contains(Invocations.INSANITY) ? 9 : 14);
	}


	private void sendNineSkulls() {
		if(skullsActive)
			return;
		canAttack = false;
		skullsActive = true;
		skullPositions.clear();
		aliveSkulls.clear();
		skulls.forEach(NPC::remove);
		skulls.clear();
		List<Position> skullPositions = new ArrayList<>();
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 43, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 40, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 43, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 40, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 43, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 40, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 43, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 40, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 43, 1));
		for (Position pos : skullPositions) {
			World.startEvent(e -> {
				int delay = skullProjectile.send(npc, pos);
				e.delay(World.getTicks(delay) + 1);
				NPC skull = new NPC(11772).spawn(pos);
				skull.getCombat().setAllowRetaliate(false);
				skulls.add(skull);
				aliveSkulls.add(skull);
				skullPositions.add(pos);
				skull.hitListener = new HitListener().preDamage(hit -> {
					if (hit.attackStyle != null && hit.attackStyle.isMelee())
						hit.damage = 1;
					else hit.block();
					if(hit.attacker.player != null) {
						hit.attacker.player.getCombat().updateLastAttack(0);
					}
				});
				skull.deathStartListener = (DeathListener.Simple) () -> {
				};
				skull.deathEndListener = (DeathListener.Simple) () -> {
					skull.transform(11773);
					aliveSkulls.remove(skull);
					if (aliveSkulls.isEmpty()) {
						sendSkullsToBoss();
						skullsActive = false;
						skullsSent++;
						if (!invocations.contains(Invocations.INSANITY)) {
							attacks = 0;
						}
						attackSpeed = 1;
					}
				};
			});
		}
		startSkullEvent(invocations.contains(Invocations.INSANITY) ? 12 : 18);
	}

	private void sendFourSkulls() {
		canAttack = false;
		skullsActive = true;
		skullPositions.clear();
		aliveSkulls.clear();
		skulls.forEach(NPC::remove);
		skulls.clear();
		List<Position> skullPositions = new ArrayList<>();
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 40, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 44, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 42, 1));
		skullPositions.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 42, 1));
		for (Position pos : skullPositions) {
			World.startEvent(e -> {
				int delay = skullProjectile.send(npc, pos);
				e.delay(World.getTicks(delay) + 1);
				NPC skull = new NPC(11772).spawn(pos);
				skull.getCombat().setAllowRetaliate(false);
				skulls.add(skull);
				aliveSkulls.add(skull);
				skullPositions.add(pos);
				skull.hitListener = new HitListener().preDamage(hit -> {
					if (hit.attackStyle != null && hit.attackStyle.isMelee())
						hit.damage = 1;
					else hit.block();
					if(hit.attacker.player != null) {
						hit.attacker.player.getCombat().updateLastAttack(0);
					}
				});
				skull.deathStartListener = (DeathListener.Simple) () -> {
				};
				skull.deathEndListener = (DeathListener.Simple) () -> {
					skull.transform(11773);
					aliveSkulls.remove(skull);
					if (aliveSkulls.isEmpty()) {
						sendSkullsToBoss();
						skullsActive = false;
						skullsSent++;
						if (!invocations.contains(Invocations.INSANITY)) {
							attacks = 0;
						}
						attackSpeed = 1;
					}
				};
			});
		}
		startSkullEvent(invocations.contains(Invocations.INSANITY) ? 6 : 8);
	}
	//TODO: players in raid check or death mechanics will break. (maybe on enter room tbf)

	private void startSkullEvent(int delay) {
		int eventNumber = skullsSent;
		int eDelay = delay * 2;
		World.startEvent(event -> {
			event.setCancelCondition(() -> !skullsActive || aliveSkulls.isEmpty() || skulls.isEmpty());
			event.delay(eDelay);
			if (anyAliveSkulls() && eventNumber == skullsSent) {
				skullsFailedEvent();
			}
		});
	}

	private boolean anyAliveSkulls() {
		if (aliveSkulls.isEmpty())
			return false;
		boolean anyAlive = false;
		for (NPC skull : aliveSkulls) {
			if (skull.getHp() > 0)
				anyAlive = true;
		}
		return anyAlive;
	}

	private void skullsFailedEvent() {
		if (aliveSkulls.isEmpty())
			return;
		for (NPC skull : skulls) {
			World.sendGraphics(2160, 0, 0, skull.getPosition());
			npc.getPosition().getRegion().players.forEach(p -> {
				if (p.getPosition().distance(skull.getPosition()) < 2) {
					p.hit(new Hit().fixedDamage(25).ignorePrayer().ignoreDefence());
					if (p.getCurrentToARaid() != null)
						p.getCurrentToARaid().failedPerfectWarden = true;
				}
			});
			skull.remove();
		}
		skulls.forEach(NPC::remove);
		skulls.clear();
		skullsActive = false;
		skullsSent++;
		World.startEvent(e -> {
			e.delay(2);
			handleCenterTilesOutwards(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 41, 1);
		});
	}


	private void sendSkullsToBoss() {
		int hit = 0;
		for (NPC skull : skulls) {
			skullToBossProjectile.send(skull.getPosition(), npc);
			hit += 25;
		}
		int finalHit = hit;
		World.startEvent(event -> {
			event.delay(1);
			for (NPC skull : skulls) {
				skull.remove();
			}
			event.delay(3);
			npc.hit(new Hit().fixedDamage(finalHit).ignorePrayer().ignoreDefence());
			canAttack = true;

		});
	}

	private void shakeFirst() {
		shakeFloor(FIRST_SHAKE_ROW_BOUNDS, FIRST_SHAKE_ROW_POSITION);
	}

	private void shakeSecond() {
		shakeFloor(SECOND_SHAKE_ROW_BOUNDS, SECOND_SHAKE_ROW_POSITION);
	}

	private void shakeThird() {
		shakeFloor(THIRD_SHAKE_ROW_BOUNDS, THIRD_SHAKE_ROW_POSITION);
	}

	private void shakeFourth() {
		shakeFloor(FOURTH_SHAKE_ROW_BOUNDS, FOURTH_SHAKE_ROW_POSITION);
	}

	/**
	 * Shaking the floor
	 *
	 * @return
	 */
	private static final Bounds FIRST_SHAKE_ROW_BOUNDS = new Bounds(3926, 5183, 3946, 5183, 2);
	private static final Position FIRST_SHAKE_ROW_POSITION = new Position(3926, 5165, 1);

	private static final Bounds SECOND_SHAKE_ROW_BOUNDS = new Bounds(3926, 5180, 3946, 5181, 2);
	private static final Position SECOND_SHAKE_ROW_POSITION = new Position(3926, 5163, 1);

	private static final Bounds THIRD_SHAKE_ROW_BOUNDS = new Bounds(3926, 5177, 3946, 5178, 2);
	private static final Position THIRD_SHAKE_ROW_POSITION = new Position(3926, 5163, 1);

	private static final Bounds FOURTH_SHAKE_ROW_BOUNDS = new Bounds(3926, 5174, 3946, 5175, 2);
	private static final Position FOURTH_SHAKE_ROW_POSITION = new Position(3926, 5162, 1);


	private void shakeFloor(final Bounds bounds, final Position startPosition) {
		//Anim for this is 9685
		List<GameObject> boxZone = getBoxGameObject(bounds);

		if (boxZone.isEmpty()) {
			return;
		}

		for (GameObject object : boxZone) {
			GameObject.spawn(object.getId(), startPosition.getX() + (object.x - bounds.swX), startPosition.getY() + (object.y - bounds.swY), startPosition.getZ(), object.getType(), object.getDirection());
		}
	}

	/**
	 * Note: x1 = swX, y1 = swY, x2 = neX, y2 = neY
	 * The box always has to be positive, not negative
	 * Example: (1,1) (2,1) (3,1)
	 * (1,2) (2,2) (3,2)
	 * <p>
	 * We do not set x1,y1 as (3,2) and x2,y2 as (1,1)
	 * This would be negative sides box. 1-3=-2 AND 1-2=-1
	 * But we do the opposite for a positive box
	 * x1,y1 = (1,1)
	 * x2,y2 = (3,2)
	 * This will lead to a positive box 3-1=2 AND 2-1=1
	 */
	public static List<GameObject> getBoxGameObject(final Bounds bounds) {
		// The list with objects
		List<GameObject> objects = new ArrayList<>();

		// The box loop for every coordinate in the box
		for (int width = bounds.swX; width <= bounds.neX; width++) {
			for (int length = bounds.swY; length <= bounds.neY; length++) {
				// Get the tiles and do not create them
				Tile tile = get(width, length, bounds.z, false);

				// The objects on the tiles are saved if and only if the object is not null since every tile has [4] objects regardless if nulled
				if (tile != null && tile.gameObjects != null) {
					for (GameObject object : tile.gameObjects) {
						if (object != null) {
							objects.add(object);
						}
					}
				}
			}
		}
		return objects;
	}

	private void leftAttack() {
		npc.animate(SLAM_LEFT);
		drawLPatternAttack(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 37, 1, ONE_SIDED_L_PATTERN_LENGTH, true, false);
		List<Position> backPositions = new ArrayList<>();
		Bounds RIGHTSIDE = new Bounds(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 37, npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 45, 1);
		World.startEvent(e -> {
			e.delay(1);
			npc.getPosition().getRegion().players.forEach(p -> {
				if (p.getPosition().inBounds(RIGHT_SIDE_FRONT_BOUNDS)) {
					p.hit(new Hit().fixedDamage(25).ignorePrayer().ignoreDefence());
					if (p.getCurrentToARaid() != null)
						p.getCurrentToARaid().failedPerfectWarden = true;
				}

			});
			e.delay(1);
			for (Position pos : RIGHTSIDE.getAllPositions()) {
				if (!pos.inBounds(RIGHTSIDE)) {
					backPositions.add(pos);
				}
			}
			npc.getPosition().getRegion().players.forEach(p -> {
				for (Position pos : backPositions) {
					if (p.getPosition().distance(pos) < 1) {
						p.hit(new Hit().fixedDamage(25).ignorePrayer().ignoreDefence());
						if (p.getCurrentToARaid() != null)
							p.getCurrentToARaid().failedPerfectWarden = true;
					}
				}
			});
		});
	}

	private void rightAttack() {
		npc.animate(SLAM_RIGHT);
		drawLPatternAttack(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 37, 1, ONE_SIDED_L_PATTERN_LENGTH, false, false);
		List<Position> backPositions = new ArrayList<>();
		Bounds LEFTSIDE = new Bounds(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 36, npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 45, 1);
		World.startEvent(e -> {
			e.delay(1);
			npc.getPosition().getRegion().players.forEach(p -> {
				if (p.getPosition().inBounds(LEFT_SIDE_FRONT_BOUNDS)) {
					p.hit(new Hit().fixedDamage(25).ignorePrayer().ignoreDefence());
					if (p.getCurrentToARaid() != null)
						p.getCurrentToARaid().failedPerfectWarden = true;
				}

			});
			e.delay(1);
			for (Position pos : LEFTSIDE.getAllPositions()) {
				if (!pos.inBounds(LEFT_SIDE_FRONT_BOUNDS)) {
					backPositions.add(pos);
				}
			}
			npc.getPosition().getRegion().players.forEach(p -> {
				for (Position pos : backPositions) {
					if (p.getPosition().distance(pos) < 1) {
						p.hit(new Hit().fixedDamage(25).ignorePrayer().ignoreDefence());
						if (p.getCurrentToARaid() != null)
							p.getCurrentToARaid().failedPerfectWarden = true;
					}
				}
			});
		});
	}

	private void drawLCenterAttack() {
		npc.animate(SLAM_CENTER);

		// Draw right attack
		drawLPatternAttack(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 37, 1, CENTER_L_PATTERN_LENGTH, true, true);

		// Draw left attack
		drawLPatternAttack(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 37, 1, CENTER_L_PATTERN_LENGTH, false, true);
		List<Position> backPositions = new ArrayList<>();
		Bounds RIGHTSIDE = new Bounds(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 37, npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 45, 1);
		Bounds LEFTSIDE = new Bounds(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 37, npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 45, 1);
		Bounds RIGHT_SIDE_FRONT_BOUNDS = new Bounds(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 37, npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 40, 1);
		Bounds LEFT_SIDE_FRONT_BOUNDS = new Bounds(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 36, npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 41, 1);

		World.startEvent(e -> {
			e.delay(1);
			npc.getPosition().getRegion().players.forEach(p -> {
				if (p.getPosition().inBounds(LEFT_SIDE_FRONT_BOUNDS) || p.getPosition().inBounds(RIGHT_SIDE_FRONT_BOUNDS)) {
					p.hit(new Hit().fixedDamage(25).ignorePrayer().ignoreDefence());
					if (p.getCurrentToARaid() != null)
						p.getCurrentToARaid().failedPerfectWarden = true;
				}

			});
			e.delay(1);
			for (Position pos : LEFTSIDE.getAllPositions()) {
				if (!pos.inBounds(LEFT_SIDE_FRONT_BOUNDS)) {
					backPositions.add(pos);
				}
			}
			for (Position pos : RIGHTSIDE.getAllPositions()) {
				if (!pos.inBounds(RIGHT_SIDE_FRONT_BOUNDS)) {
					backPositions.add(pos);
				}
			}
			npc.getPosition().getRegion().players.forEach(p -> {
				for (Position pos : backPositions) {
					if (p.getPosition().distance(pos) < 1) {
						p.hit(new Hit().fixedDamage(25).ignorePrayer().ignoreDefence());
						if (p.getCurrentToARaid() != null)
							p.getCurrentToARaid().failedPerfectWarden = true;
					}
				}
			});
		});
	}

	private static final int[] ONE_SIDED_L_PATTERN_LENGTH = new int[]{2, 3, 4, 5, 5, 5, 6, 5};
	private static final int[] CENTER_L_PATTERN_LENGTH = new int[]{3, 4, 5, 5, 5, 5, 5};

	private void drawLPatternAttack(final int startPosX, final int startPosY, final int z, final int[] pattern, final boolean right, final boolean center) {
		for (int index = 0; index < (center ? 10 : 11); index++) {
			// L shape is only done 8 times + initial point
			if (index <= (center ? 7 : 8)) {
				// Draw initial point
				if (index == 0) {
					World.sendGraphics(2220, 0, 50 + index * 5, startPosX, startPosY, z);
					if (center) {
						World.sendGraphics(2220, 0, 50 + index * 5, startPosX, startPosY + 1, z);
					}
					// Draw the length and rows in L shape
				} else {
					int width;
					// Start with width
					for (width = 0; width < 1 + (index - 1); width++) {
						World.sendGraphics(2220, 0, 50 + index * 5, startPosX + (width * (right ? 1 : -1)), startPosY + index + (center ? 1 : 0), z);
					}
					// End with length
					for (int length = 0; length < pattern[index - 1]; length++) {
						World.sendGraphics(2220, 0, 50 + index * 5, startPosX + (width * (right ? 1 : -1)), startPosY + index - length + (center ? 1 : 0), z);
					}
				}
			} else {
				// Draw ending rows
				for (int row = 0; row < (index == (center ? 8 : 9) ? 4 : 3); row++) {
					World.sendGraphics(2220, 0, 50 + index * 5, startPosX + (index * (right ? 1 : -1)), startPosY + (index == (center ? 8 : 9) ? 5 : 6) + row, z);
				}
			}
		}
	}
}
