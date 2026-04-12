package io.ruin.model.activities.raids.tob.dungeon.boss.verzik;

import io.ruin.Server;
import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.TheSoulsplit;
import io.ruin.model.activities.raids.tob.party.TheatrePartyManager;
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
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.route.routes.DumbRoute;
import io.ruin.model.map.route.routes.ProjectileRoute;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class VerzikCombat extends NPCCombat {


	private static final Projectile ELECTRIC = new Projectile(1580, 85, 30, 0, 46, 8, 0, 255).regionBased();
	private static final Projectile RANGED = new Projectile(1593, 85, 30, 0, 76, 8, 0, 255).regionBased();

	private static final Projectile RANGE_ATTACK = new Projectile(1583, 85, 30, 1, 46, 8, 0, 255).regionBased();

	private static final Projectile RED_ORB = new Projectile(1606, 35, 30, 1, 46, 8, 0, 255).regionBased();
	private static final Projectile LIGHTNING = new Projectile(1585, 35, 30, 1, 46, 8, 0, 255).regionBased();
	private static final Projectile BLOOD_MAGE_ATTACK = new Projectile(1591, 35, 30, 1, 46, 8, 0, 255).regionBased();
	private static final Projectile PURPLE_BLOB = new Projectile(1604, 35, 30, 1, 130, 8, 0, 150).regionBased();
	private static final Projectile PURPLE_NYLO_PROJECITLE = new Projectile(1586, 35, 30, 1, 130, 8, 0, 150).regionBased();
	private static final Projectile GREEN_BOMB = new Projectile(1596, 35, 0, 50, 150, 0, 55, 255).regionBased();
	private static final Projectile WEB = new Projectile(1601, 35, 30, 1, 130, 8, 0, 150).regionBased();
	List<Position> nyloSpawns = new ArrayList<>();
	public List<NPC> bloodNylos = new ArrayList<>();

	public boolean damagedPlayer = false;


	int startingHp = 0;
	Bounds bossRoom;

	public boolean popItFailed = false;

	public boolean bounced = false;
	int phase = 1;
	int nyloTimer = 30;

	boolean tornadosSpawned = false;
	int bloodCrabAttackCounter = 0;
	boolean shootingWebs = false;
	boolean bloodCrabsSpawned = false;
	boolean isAttackable = true;
	boolean canAttack = true;

	boolean anyPillarsDestroyed = false;
	public List<PurpleNylocas> purpleNylos = new ArrayList<>();
	public List<NPC> bombNylos = new ArrayList<>();
	public List<NylocasBomb> bombNyloList = new ArrayList<>();

	public List<VerzikWeb> webList = new ArrayList<>();
	public List<VerzikTornado> tornados = new ArrayList<>();

	private boolean spawnPurple = false;

	private int currentSpiderAttackDelay = 7;
	private int spiderAttackDelay = 7;
	GameObject throne;

	Player tank;
	public LinkedList<VerzikPillar> pillarList = new LinkedList<>();
	LinkedList<VerzikPillar> leftPillarList = new LinkedList<>();
	LinkedList<VerzikPillar> rightPillarList = new LinkedList<>();

	YellowPool yellowPoolHandler;

	int attackCounter = 0;

	@Override
	public void init() {
		npc.deathStartListener = (entity, killer, killHit) -> verzikStartEnd();
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend);
		VerzikPillar leftPillarOne = new VerzikPillar(
			GameObject.spawn(32687, npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 30, npc.getPosition().getZ(), 10, 0),
			new NPC(8379).spawn(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 30, npc.getPosition().getZ()),
			new Bounds(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 29,
				npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 29, 0), npc
		);
		VerzikPillar leftPillarTwo = new VerzikPillar(
			GameObject.spawn(32687, npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 24, npc.getPosition().getZ(), 10, 0),
			new NPC(8379).spawn(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 24, npc.getPosition().getZ()),
			new Bounds(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 23,
				npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 23, 0), npc
		);
		VerzikPillar leftPillarThree = new VerzikPillar(
			GameObject.spawn(32687, npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 18, npc.getPosition().getZ(), 10, 0),
			new NPC(8379).spawn(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 18, npc.getPosition().getZ()),
			new Bounds(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 17,
				npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 17, 0), npc
		);
		VerzikPillar rightPillarOne = new VerzikPillar(
			GameObject.spawn(32687, npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 24, npc.getPosition().getZ(), 10, 0),
			new NPC(8379).spawn(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 24, npc.getPosition().getZ()),
			new Bounds(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 23,
				npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 23, 0), npc
		);
		VerzikPillar rightPillarTwo = new VerzikPillar(
			GameObject.spawn(32687, npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 18, npc.getPosition().getZ(), 10, 0),
			new NPC(8379).spawn(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 18, npc.getPosition().getZ()),
			new Bounds(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 17,
				npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 17, 0), npc
		);
		VerzikPillar rightPillarThree = new VerzikPillar(GameObject.spawn(32687, npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 30, npc.getPosition().getZ(), 10, 0),
			new NPC(8379).spawn(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 30, npc.getPosition().getZ()),
			new Bounds(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 29,
				npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 29, 0), npc);
		bossRoom = new Bounds(npc.getPosition().getRegion().baseX + 19, npc.getPosition().getRegion().baseY + 15, npc.getPosition().getRegion().baseX + 44,
			npc.getPosition().getRegion().baseY + 35, npc.getPosition().getZ());
		pillarList.add(rightPillarThree);
		rightPillarList.add(rightPillarThree);
		pillarList.add(rightPillarOne);
		rightPillarList.add(rightPillarOne);
		pillarList.add(rightPillarTwo);
		rightPillarList.add(rightPillarTwo);
		pillarList.add(leftPillarOne);
		leftPillarList.add(leftPillarOne);
		pillarList.add(leftPillarTwo);
		leftPillarList.add(leftPillarTwo);
		pillarList.add(leftPillarThree);
		leftPillarList.add(leftPillarThree);
		npc.attackNpcListener = (player, npc, message) -> {
			if (npc.getId() != 8370) {
				return isAttackable;
			}
			boolean behindLeftPillar = false;
			boolean behindRightPillar = false;
			for (VerzikPillar pillar : rightPillarList) {
				if (pillar.playerBehindPillar(player)) {
					behindRightPillar = true;
					break;
				}
			}
			for (VerzikPillar pillar : leftPillarList) {
				if (pillar.playerBehindPillar(player)) {
					behindLeftPillar = true;
					break;
				}
			}
			if (behindLeftPillar) {
				World.startEvent(e -> {
					player.getCombat().reset();
					player.resetActions(true, true, true);
					player.face(npc);
					player.stepAbs(player.getPosition().getRegion().baseX + 28, player.getPosition().getY(), VarPlayerRepository.RUNNING.toggle(player) == 1 ? StepType.RUN : StepType.WALK);
					e.waitForTile(player, new Position(player.getPosition().getRegion().baseX + 28, player.getPosition().getY(), player.getPosition().getZ()));
					player.getCombat().setTarget(npc);
					player.face(npc);
					player.getCombat().attack();
				});
				return false;
			} else if (behindRightPillar) {
				World.startEvent(e -> {
					player.face(npc);
					player.stepAbs(player.getPosition().getRegion().baseX + 36, player.getPosition().getY(), VarPlayerRepository.RUNNING.toggle(player) == 1 ? StepType.RUN : StepType.WALK);
					e.waitForTile(player, new Position(player.getPosition().getRegion().baseX + 36, player.getPosition().getY(), player.getPosition().getZ()));
					player.getCombat().setTarget(npc);
					player.face(npc);
					player.getCombat().attack();
				});
				return false;
			} else {
				return isAttackable;
			}
		};
		npc.attackBounds = bossRoom;
	}

	private static final StatType[] SCALED_STATS = {StatType.Hitpoints, StatType.Defence, StatType.Attack, StatType.Strength, StatType.Magic, StatType.Ranged};

	public void scaleNPC(NPC npc) {
		if (npc.getCombat() == null) {
			return;
		}
		double factor;
		factor = 1 + (0.15 * npc.getPosition().getRegion().players.size());
		if (factor != 0) {
			for (StatType type : SCALED_STATS) {
				if (type == StatType.Hitpoints) {
					factor = 1 + (0.65 * npc.getPosition().getRegion().players.size());
				}
				double newLevel = npc.getCombat().getStat(type).fixedLevel * factor;
				npc.getCombat().getStat(type).fixedLevel = (int) newLevel;
				npc.getCombat().getStat(type).restore();
			}
		}
		scaleHP(npc);
	}

	public void scaleHP(NPC npc) {
		if (npc.getCombat() == null) {
			return;
		}
		double factor;

		factor = 3.5 + (0.7 * npc.getPosition().getRegion().players.size());
		if (factor != 0) {
			double newLevel = npc.getCombat().getStat(StatType.Hitpoints).fixedLevel * factor;
			npc.getCombat().getStat(StatType.Hitpoints).fixedLevel = (int) newLevel;
			npc.getCombat().getStat(StatType.Hitpoints).restore();
		}

	}

	private void postDamage(Hit hit) {
		if (hit.attacker != null && hit.attacker.isPlayer()) {
			if (hit.attacker != null && hit.attacker.isPlayer())
				hit.attacker.player.tobDamageDealt += hit.damage;
		}
	}

	private void preHitDefend(Hit hit) {

	}

	private Player getRandomAlivePlayer() {
		List<Player> players = getAlivePlayers();
		return players.isEmpty() ? null : Random.get(players);
	}

	private List<Player> getAlivePlayers() {
		List<Player> players = new ArrayList<>(npc.getPosition().getRegion().players);
		players.removeIf(p ->
			p.currentParty == null || p.currentParty.deadPlayers.contains(p) || p.getCombat().isDead()
		);
		return players;
	}

	Position thronePos;

	private void verzikStartEnd() {
		if (target != null)
			reset();
		npc.getMovement().reset();
		npc.getCombat().setAllowRetaliate(false);
		npc.getCombat().reset();
		npc.lock();
		World.startEvent(event -> {
			switch (npc.getId()) {
				case 8370:
					thronePos = new Position(npc.getAbsX() + 1, npc.getAbsY(), 0);
					npc.animate(8109);
					event.delay(2);
					npc.transformNPC(8380);
					npc.getPosition().getRegion().players.forEach(p -> {
						p.getCombat().reset();
					});
					event.delay(3);
					npc.transformNPC(8371);
					npc.getPosition().getRegion().players.forEach(p -> {
						p.getCombat().reset();
					});
					restore();
					npc.animate(8116);
					event.delay(1);
					if (!anyPillarsDestroyed) {
						for (Player p : npc.getPosition().getRegion().players) {
							if (p.currentParty.deadPlayers.contains(p))
								continue;
							Objects.requireNonNull(p.combatAchievementsList.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.NOPILLAR.ordinal())).
								getCombatAchievement()).check(p);
						}
					}
					for (int i = 0; i < pillarList.size(); i++) {
						pillarList.get(i).pillarDestroyed();
					}

					isAttackable = false;
					canAttack = false;
					//step instead
					for (int i = 0; i < 3; i++) {
						npc.step(0, -1, StepType.NORMAL);
						event.delay(1);
					}
					npc.stepAbs(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 23, StepType.WALK);
					pillarList.clear();
					npc.attackBounds = bossRoom;
					npc.getCombat().setAllowRetaliate(true);
					break;
				case 8372:
					npc.getPosition().getRegion().players.forEach(p -> {
						p.getCombat().reset();
					});
					restore();
					npc.animate(8118);
					event.delay(3);
					npc.transformNPC(8374);
					npc.animate(8119);
					event.delay(2);
					isAttackable = true;
					canAttack = true;
					tank = getRandomAlivePlayer();
					npc.getCombat().setTarget(tank);
					npc.attackBounds = bossRoom;
					for (NylocasBomb nyloBombs :
						bombNyloList) {
						nyloBombs.npc.remove();
					}
					for (PurpleNylocas purp :
						purpleNylos) {
						purp.npc.remove();
					}
					for (NPC bloodNylo :
						bloodNylos) {
						bloodNylo.remove();
					}
					npc.unlock();
					npc.animate(-1);
					spiderAttackDelay = 0;
					break;
				case 8374:
					throne = GameObject.spawn(32738, thronePos.getX(), thronePos.getY(), 0, 10, 0);
					npc.animate(8128);
					for (NylocasBomb nyloBombs :
						bombNyloList) {
						nyloBombs.npc.remove();
					}
					for (PurpleNylocas purp :
						purpleNylos) {
						purp.npc.remove();
					}
					for (NPC bloodNylo :
						bloodNylos) {
						bloodNylo.remove();
					}

					tornados.forEach(tornado -> tornado.tornado.remove());
					tornados.removeIf(verzikTornado -> verzikTornado.tornado.isRemoved());

					if (npc.getPosition().getRegion().players.isEmpty()) {
						break;
					}
					var partyPlayers = npc.getPosition().getRegion().players;
					partyPlayers.getFirst().currentParty.getUsers().forEach(user -> {
						TheatrePartyManager.instance().forUsername(user).ifPresent(p -> {
							if (p.insideRaid) {
								p.insideRaid = false;
								p.theatreOfBloodStage = 6;
								if (!wearingBarrows)
									p.currentParty.wearingBarrows = false;
								if (p.currentParty.wearingBarrows) {
									Objects.requireNonNull(p.combatAchievementsList.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.MORYTANIA_ONLY.ordinal())).
										getCombatAchievement()).check(p.player);
								}
								if (p.currentParty.getUsers().size() == 1 && p.activePerksList.isEmpty()) {
									Objects.requireNonNull(p.combatAchievementsList.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.PERKLESS_THEATRE.ordinal())).
										getCombatAchievement()).check(p.player);
								}
								if (!damagedPlayer && !p.currentParty.deadPlayers.contains(p)) {
									p.currentParty.perfectVerzik = true;
									Objects.requireNonNull(p.combatAchievementsList.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_VERZIK.ordinal())).
										getCombatAchievement()).check(p.player);
								}
								if (p.currentParty.deaths == 0 && p.currentParty.perfectMaiden && p.currentParty.perfectBloat && p.currentParty.perfectVasillas && p.currentParty.perfectSotetseg && p.currentParty.perfectVerzik
									&& p.currentParty.perfectXarpus) {
									Objects.requireNonNull(p.combatAchievementsList.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_THEATRE.ordinal())).
										getCombatAchievement()).check(p.player);
								}
								PerkTaskHandler.handleMonsterKill(p, "tob");
								Objects.requireNonNull(p.combatAchievementsList.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.THEATRE_OF_BLOOD_ADEPT.ordinal())).
									getCombatAchievement()).check(p.player);
								Objects.requireNonNull(p.combatAchievementsList.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.THEATRE_OF_BLOOD_NOVICE.ordinal())).
									getCombatAchievement()).check(p.player);
								Objects.requireNonNull(p.combatAchievementsList.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.THEATRE_OF_BLOOD_MASTER.ordinal())).
									getCombatAchievement()).check(p.player);
								Objects.requireNonNull(p.combatAchievementsList.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.THEATRE_OF_BLOOD_GRANDMASTER.ordinal())).
									getCombatAchievement()).check(p.player);
								if (!p.currentParty.deadPlayers.contains(p) && !bounced) {
									Objects.requireNonNull(p.combatAchievementsList.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.ATTACKSTEPWAIT.ordinal())).
										getCombatAchievement()).check(p.player);
								}
								if (!p.currentParty.deadPlayers.contains(p) && !popItFailed) {
									Objects.requireNonNull(p.combatAchievementsList.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.POP_IT.ordinal())).
										getCombatAchievement()).check(p.player);
								}
								p.sendMessage("You beat Theatre of blood! Congratulations! go claim your treasure!");
								p.theatreOfBloodKills.increment(p);
								DailyTasks.handleTaskDecrement(p, "tob");
								p.tobreward = false;
							}
						});
					});
					event.delay(3);
					npc.animate(-1);
					npc.transformNPC(8390);
					npc.getPosition().getRegion().players.forEach(p -> {
						p.getCombat().reset();
					});
					for (Player player :
						npc.getPosition().getRegion().players) {
						player.unlock();
						if (player.getPosition().getY() == player.getPosition().getRegion().baseY + 37) {
							player.unlock();
							player.getMovement().teleport(player.getPosition().getRegion().baseX + 32, player.getPosition().getRegion().baseY + 30, 0);
						}

					}
//					event.delay(6);
//					if (!partyPlayers.isEmpty())
//						partyPlayers.getFirst().currentParty.getDeadPlayers()
//							.stream()
//							.filter(Objects::nonNull)
//							.filter(Player::isOnline)
//							.forEach(p -> {
//								p.sendMessage("You will be moved soon..");
//								event.delay(6);
//								p.getMovement().teleport(npc.getPosition());
//								p.unlock();
//								p.getCombat().restore();
//								p.setInvincible(false);
//								p.supplyChestPoints = 0;
//							});
					break;
			}
		});
	}

	@Override
	public void follow() {
		if (npc.getId() == 8374) {
			follow(3);
		}
	}

	@Override
	public boolean attack() {
		if (startingHp == 0)
			startingHp = npc.getHp();
		if (!getAlivePlayers().contains(tank))
			tank = getRandomAlivePlayer();
		if (npc.getId() == 8369) return false;
		npc.attackBounds = bossRoom;
		if (!canAttack) return false;
		if (getNpc().isLocked()) return false;
		if (currentSpiderAttackDelay > 0 && npc.getId() == 8374) return false;
		attackCounter++;
		npc.getCombat().getInfo().attack_ticks = 6;
		switch (npc.getId()) {
			case 8370: {
				npc.getCombat().getInfo().attack_ticks = 9;
				phaseOneAttack();
				break;
			}
			case 8372: {
				Player target = Random.get(npc.getPosition().getRegion().players);
				npc.face(target);
				for (Player player :
					npc.getPosition().getRegion().players) {
					int distance = player.getPosition().distance(npc.getPosition());
					if (distance < 2) {
						bounced = true;
						knockback(player);
					}
				}
				if (spawnPurple) {
					spawnPurple = false;
					spawnPurpleNylo(target);
				}
				else if (attackCounter == 4) {
					lightningAttack(npc, target);
					attackCounter = 0;
				}
				else if (npc.getHp() > 700) {
					npc.face(target);
					for (Player player :
						npc.getPosition().getRegion().players) {
						rangedAttack(player);
					}
				}
				else if (Random.get(4) == 0) {
					npc.face(target);
					for (Player player :
						npc.getPosition().getRegion().players) {
						rangedAttack(player);
					}
				}
				else
					bloodMageAttack(target);

				break;
			}
			case 8374:
				currentSpiderAttackDelay = spiderAttackDelay;
				npc.face(tank);
				switch (attackCounter) {
					case 2:
						spawnNylos(getAlivePlayers().size());
						break;
					case 4:
						startWebSpecial();
						break;

					case 6:
						spawnYellowPool();
						break;
					case 8:
						greenBomb(npc, Random.get(getAlivePlayers()));
						attackCounter = 0;
						break;
					default:
						spiderAutoAttack(tank);
						if (attackCounter > 8)
							attackCounter = 0;
						break;
				}
				break;
		}
		return true;
	}

	private void bloodMageAttack(Entity target) {
		npc.animate(8114);
		int delay = BLOOD_MAGE_ATTACK.send(npc, target);
		int maxDamage = 55;
		if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
			maxDamage = 0;
		}
		int damage = Random.get(maxDamage);
		if (damage > 0 && npc.getHp() > 0) {
			npc.hit(new Hit(HitType.HEAL).fixedDamage(damage).clientDelay(delay));
		}
		if (npc.getHp() > 0)
			target.hit(new Hit(npc, AttackStyle.MAGIC).fixedDamage(damage).clientDelay(delay));
	}

	private void spawnPurpleNylo(Entity target) {
		World.startEvent(e -> {
			npc.animate(8114);
			Position pos = target.getPosition().copy();
			int delay = PURPLE_NYLO_PROJECITLE.send(npc, pos);
			int ticks = getTicks(delay);
			e.delay(ticks);
			PurpleNylocas purpleNylocas = new PurpleNylocas(pos, npc);
			purpleNylos.add(purpleNylocas);
			for (Player player :
				getAlivePlayers()) {
				if (player.getPosition().isWithinDistance(pos, 0) && npc.getHp() > 0) {
					player.hit(new Hit(npc, AttackStyle.MAGICAL_MELEE).randDamage(50, 78));
					damagedPlayer = true;
				}
			}
		}).setCancelCondition(() -> isDead() || getNpc().isRemoved());
	}

	private int getHPPercent() {
		int currentHp = npc.getHp();
		int maxHp = npc.getMaxHp();
		return (currentHp * 100) / maxHp;
	}

	public boolean wearingBarrows = true;

	@Override
	public void process() {
		if (!npc.getPosition().getRegion().players.isEmpty()) {
			for (Player p : npc.getPosition().getRegion().players) {
				if (!p.wearingBarrows())
					wearingBarrows = false;
			}
		}
		if (npc.getId() == 8369) return;
		if (yellowPoolHandler != null) {
			yellowPoolHandler.process();
		}
		for (NPC bN : bloodNylos) {
			if (bN.isFrozen())
				popItFailed = true;
		}
		if (npc.getId() == 8374) {
			currentSpiderAttackDelay--;
			if (tank == null || tank.dead() && !getAlivePlayers().isEmpty()) {
				tank = getRandomAlivePlayer();
			}
			if (getHPPercent() < 75) {
				spiderAttackDelay = 5;
				if (!tornadosSpawned) {
					tornadosSpawned = true;
					getAlivePlayers().forEach(p -> {
						tornados.add(new VerzikTornado(p, bossRoom, npc));
					});
				}
			}
		}
		if (!bombNyloList.isEmpty()) {
			for (int i = bombNyloList.size() - 1; i >= 0; i--) {
				if (npc.getId() == 8375) {
					bombNyloList.get(i).npc.remove();
					bombNyloList.remove(i);
					continue;
				}
				bombNyloList.get(i).process();
			}
		}
		if (!tornados.isEmpty()) {
			for (int i = tornados.size() - 1; i >= 0; i--) {
				if (npc.getId() == 8375) {
					tornados.get(i).tornado.remove();
					tornados.remove(i);
					continue;
				}
				if (tornados.get(i).tornado == null) tornados.remove(i);

				tornados.get(i).process();
			}
		}
		if (!purpleNylos.isEmpty()) {
			for (int i = purpleNylos.size() - 1; i >= 0; i--) {
				if (npc.getId() == 8375) {
					purpleNylos.get(i).npc.remove();
					purpleNylos.remove(i);
					continue;
				}
				purpleNylos.get(i).process();
			}
		}

		if (webList != null && !webList.isEmpty()) {
			for (int i = webList.size() - 1; i >= 0; i--) {
				if (npc.getId() == 8375) {
					webList.get(i).webDestroyed();
					webList.get(i).webObject.remove();
					webList.get(i).webDestroyed = true;
					webList.get(i).webNPC.remove();
				}
				if (webList.get(i).webDestroyed || npc.getHp() < 1) {
					webList.get(i).webDestroyed();
					webList.remove(i);
					continue;
				}
				webList.get(i).process(getAlivePlayers());
			}
		}
		//TODO: FIX WEBS SOMETIMES NOT SPAWNING NPCS
		//TODO: spider web start wont move to middle

		if (npc.getId() == 8372) {
			nyloTimer--;
			if (nyloTimer <= 0) {
				if (npc.getHp() > 700)
					spawnNylos(getAlivePlayers().size());
				else
					spawnBloodNylo();
				nyloTimer = 90;
			}
		}
		if (npc.getHp() <= 0) {
			verzikStartEnd();
		}

		if (npc.getId() == 8371) {
			int distance = npc.getPosition().distance(new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 23, npc.getPosition().getZ()));
			if (distance < 2) {
				npc.transformNPC(8372);
				isAttackable = true;
				canAttack = true;
				npc.unlock();
			}
		}

		for (int i = 0; i < pillarList.size(); i++) {
			if (pillarList.get(i).getPillarNPC().getHp() <= 0)
				pillarList.remove(pillarList.get(i));
		}

		if (bloodCrabAttackCounter >= 8) {
			bloodCrabAttackCounter = 0;
			for (int i = 0; i < bloodNylos.size(); i++) {
				int healAmount = bloodNylos.get(i).getHp();
				bloodNylos.get(i).remove();
				npc.hit(new Hit(HitType.HEAL).fixedDamage(healAmount));
			}
			bloodNylos.clear();
			bloodCrabsSpawned = false;
		}
	}

	@Override
	public void startDeath(Hit killHit) {
		if (npc.getId() != 8374) return;
		super.startDeath(killHit);
	}

	private void phaseOneAttack() {
		World.startEvent(e -> {
			e.delay(2);
			npc.attackBounds = bossRoom;
			npc.animate(8110);
			List<Player> possibleTargets = new ArrayList<>(getAlivePlayers());
			List<NPC> pillarsToTarget = new ArrayList<>();
			Entity target;
			for (int i = possibleTargets.size() - 1; i >= 0; i--) {
				for (VerzikPillar pillar : pillarList) {
					if (possibleTargets.isEmpty()) break;
					if (possibleTargets.get(i).getPosition().inBounds(pillar.safeZone)) {
						possibleTargets.remove(i);
						break;
					}
				}
			}

			if (possibleTargets.isEmpty()) {
				for (VerzikPillar pillar :
					pillarList) {
					if (pillar.getPlayersBehindPillar(npc.getPosition().getRegion().players) > 0)
						pillarsToTarget.add(pillar.getPillarNPC());
				}
				if (pillarsToTarget.isEmpty()) {
					return;
				}
				target = Random.get(pillarsToTarget);
			} else
				target = Random.get(possibleTargets);

			int delay = ELECTRIC.send(npc, target);
			int maxDamage = 92;
			if (target != null && target.isPlayer()) {
				maxDamage = 190;
				damagedPlayer = true;
				if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
					maxDamage *= 0.5;
			}
			e.delay(World.getTicks(delay));
			if (target != null && npc.getHp() > 0)
				target.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(maxDamage).ignorePrayer().ignoreDefence());
		}).setCancelCondition(() -> isDead() || getNpc().isRemoved());
	}

	private void spawnYellowPool() {
		World.startEvent(event -> {
			canAttack = false;
			npc.animate(-1);
			npc.animate(8126);
			if (yellowPoolHandler == null)
				yellowPoolHandler = new YellowPool(getAlivePlayers(), npc, bossRoom);
			else
				yellowPoolHandler.setActive(getAlivePlayers(), npc, bossRoom);
			event.delay(15);
			canAttack = true;
		});
	}


	private void spiderAutoAttack(Entity target) {
		int attack = Random.get(2);
		npc.getRouteFinder().routeAbsolute(target.getAbsX(), target.getAbsY());
		if (attack == 0) {
			npc.animate(8124);
			npc.getPosition().getRegion().players.forEach(p -> {
				World.startEvent(e -> {
					e.delay(1);
					int delay = ELECTRIC.send(npc, p);
					npc.graphics(1582);
					int maxDamage = 66;
					e.delay(World.getTicks(delay) + 1);
					if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
						maxDamage *= 0.2;

					if (npc.getId() == 8374 || npc.getHp() > 0)
						p.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(maxDamage).ignoreDefence().ignorePrayer());
				}).setCancelCondition(() -> isDead() || getNpc().isRemoved());
			});
		} else if (attack == 1) {
			npc.animate(8125);
			npc.getPosition().getRegion().players.forEach(p -> {
				World.startEvent(e -> {
					e.delay(1);
					int delay = RANGED.send(npc, p);
					int maxDamage = 65;
					e.delay(World.getTicks(delay));
					if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
						maxDamage *= 0.2;

					if (npc.getId() == 8374 || npc.getHp() > 0)
						p.hit(new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).ignoreDefence().ignorePrayer());
				}).setCancelCondition(() -> isDead() || getNpc().isRemoved());
			});
		} else {
			World.startEvent(e -> {
				e.delay(1);
				npc.animate(8125);
				e.delay(1);
				basicAttack();
				e.delay(1);

				if (target.getPosition().isWithinDistance(npc.getPosition(), 3)) {
					getAlivePlayers().forEach(player -> {
						if (player != tank) {
							if (npc.getId() == 8374 || npc.getHp() > 0)
								player.hit(new Hit(npc, AttackStyle.CRUSH).randDamage(info.max_damage));
						}
					});
				}
			}).setCancelCondition(() -> isDead() || getNpc().isRemoved());
		}
	}

	private void lightningAttack(NPC npc, Entity target) {
		AtomicBoolean verzikHit = new AtomicBoolean(false);
		World.startEvent(event -> {
			npc.animate(8114);
			Entity from = npc;
			Entity to = target;
			List<Player> targets = new ArrayList<>(getAlivePlayers());
			Set<Player> hitPlayers = new HashSet<>(); // Track already hit players

			hitPlayers.add(to.player);
			int maxBounces = targets.size();
			int bounces = 0;

			while (bounces < maxBounces) {
				targets.remove(to.player);
				boolean throughBoss = edgeInterceptRadius(from, to, npc, 2);
				if (from == npc) {
					throughBoss = false;
				}

				event.delay(1);
				int ticks = sendLightning(from, to, throughBoss);

				if (throughBoss) {
					verzikHit.set(true);
					break;
				}

				from = to;
				Player nextTarget = getRandomValidTarget(from, targets, hitPlayers);

				if (nextTarget == null) {
					to.hit(new Hit().fixedDamage(48));
					break;
				}

				to = nextTarget;
				hitPlayers.add(to.player);

				event.delay(ticks);
				bounces++;
			}
			if (!verzikHit.get()) {
				to.hit(new Hit().fixedDamage(48));
			}
		}).setCancelCondition(() -> verzikHit.get() || isDead() || getNpc().isRemoved());
	}

	private int sendLightning(Entity from, Entity to, boolean throughBoss) {
		int delay;
		if (!throughBoss && npc.getHp() > 0) {
			delay = LIGHTNING.send(from, to);
			to.hit(new Hit(HitType.DAMAGE).randDamage(8, 12).clientDelay(delay));
		} else {
			delay = LIGHTNING.send(from, npc);
			npc.hit(new Hit(HitType.DAMAGE).randDamage(25, 35).clientDelay(delay));
		}
		return Math.max(1, (delay * 16) / Server.tickMs());
	}

	private Player getRandomValidTarget(Entity from, List<Player> players, Set<Player> hitPlayers) {
		List<Player> validTargets = new ArrayList<>();
		for (Player p : players) {
			if (!hitPlayers.contains(p) && p.getPosition().isWithinDistance(from.getPosition(), 9)) {
				validTargets.add(p);
			}
		}
		return validTargets.isEmpty() ? null : Random.get(validTargets);
	}


	private void redOrb(List<Player> targets) {
		targets.forEach(p -> {
			int delay = RED_ORB.send(npc, p);
			p.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(info.max_damage).clientDelay(delay));
			int current = p.getStats().get(StatType.Prayer).currentLevel;
			p.getPrayer().drain((int) (10 + ((current + 1) / 5.0)));
			if (current > 0)
				p.sendMessage("Your prayer was drained!");
		});
	}

	private void purpleBlob() {
		World.startEvent(event -> {
			int randomIndex = getAlivePlayers().size();
			Position randomTargetPos = getAlivePlayers().get(randomIndex).getPosition();
			int delay = PURPLE_BLOB.send(npc, randomTargetPos);
			event.delay(5);
			getAlivePlayers().forEach(player -> {
				if (player.getPosition().isWithinDistance(randomTargetPos, 0) && npc.getHp() > 0)
					player.hit(new Hit(HitType.DAMAGE).randDamage(35, 70).clientDelay(delay));
			});
		}).setCancelCondition(() -> isDead() || getNpc().isRemoved());
	}

	private void spawnBloodNylo() {
		bloodCrabAttackCounter = 0;
		int amountToSpawn = getAlivePlayers().size();
		for (int i = 0; i < amountToSpawn; i++) {
			Position spawnPos = bossRoom.randomPosition();
			bloodNylos.add(new NPC(8366).spawn(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 15));
			bloodCrabsSpawned = true;
		}
	}

	private void forAllNyloBombs(Consumer<NPC> action) {
		bombNylos.forEach(action);
	}

	private void handleNyloExplosion() {
		forAllNyloBombs(nylo -> {
			nylo.startEvent(e -> {
				if (!nylo.hasTarget())
					nylo.getCombat().setTarget(Random.get(npc.localPlayers()));
				int damage = 64;
				for (Player player :
					nylo.getPosition().getRegion().players) {
					int distance = player.getPosition().distance(nylo.getPosition());
					if (distance < 4) {
						nylo.getCombat().reset();
						e.delay(3);
						nylo.hit(new Hit().fixedDamage(nylo.getHp()));
						nylo.animate(8006);
						e.delay(2);
						distance = player.getPosition().distance(nylo.getPosition());
						nylo.remove();
						switch (distance) {
							case 0:
								damage = 64;
								break;
							case 1:
								damage = 32;
								break;
							case 2:
								damage = 16;
								break;
							case 3:
								damage = 8;
								break;
							case 4:
								damage = 4;
								break;
						}
						int multiplier = nylo.getHp() / nylo.getMaxHp();
						damage *= multiplier;
						if (npc.getHp() > 0)
							player.hit(new Hit().fixedDamage(damage));
					}
				}
			});
		});
	}

	private void spawnNylos(Entity target) {
		Position spawn = nyloSpawns.get(Random.get(0, nyloSpawns.size() - 1));
		int[] nyloIDs = new int[]{8345, 8346, 8347};
		NylocasBomb nylocas = new NylocasBomb(nyloIDs[Random.get(nyloIDs.length - 1)], spawn, target.player, npc);
		bombNyloList.add(nylocas);

	}

	private void spawnNylos(int amountToSpawn) {
		spawnPurple = true;
		updateNyloSpawns();
		if (npc.getPosition().getRegion().players.isEmpty())
			return;
		for (int i = 0; i < amountToSpawn; i++) {
			spawnNylos(Random.get(npc.getPosition().getRegion().players));
		}
	}

	private void updateNyloSpawns() {
		nyloSpawns.clear();
		nyloSpawns.add(new Position(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 18, npc.getPosition().getZ()));
		nyloSpawns.add(new Position(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 21, npc.getPosition().getZ()));
		nyloSpawns.add(new Position(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 24, npc.getPosition().getZ()));
		nyloSpawns.add(new Position(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 27, npc.getPosition().getZ()));
		nyloSpawns.add(new Position(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 30, npc.getPosition().getZ()));
		nyloSpawns.add(new Position(npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 32, npc.getPosition().getZ()));
		nyloSpawns.add(new Position(npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 29, npc.getPosition().getZ()));
		nyloSpawns.add(new Position(npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 26, npc.getPosition().getZ()));
		nyloSpawns.add(new Position(npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 23, npc.getPosition().getZ()));
		nyloSpawns.add(new Position(npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 20, npc.getPosition().getZ()));
	}

	private void disperseWebs(Entity target) {
		npc.lock();
		World.startEvent(event -> {
			for (int ticks = 0; ticks < 13; ticks++) {
				if (npc.getHp() < 1)
					break;
				Position targetsPosition = target.getPosition().copy();
				List<Position> possibleSpawns = new ArrayList<>();
				for (int x = -4; x < 4; x++) {
					for (int y = -4; y < 4; y++) {
						Position newPosition = new Position(targetsPosition.getX() + x, targetsPosition.getY() + y, targetsPosition.getZ());
						if (!newPosition.inBounds(bossRoom)) continue;
						possibleSpawns.add(newPosition);
					}
				}
				possibleSpawns.remove(target.getPosition());

				npc.face(targetsPosition.getX(), targetsPosition.getY());
				npc.animate(8127);
				if (possibleSpawns.isEmpty())
					return;
				var delay = -1;
				for (int i = 0; i < 3; i++) {
					var spawn = getRandomPosition(possibleSpawns);
					if (delay == -1) delay = WEB.send(npc, spawn);
					else WEB.send(npc, spawn);
					// little delay
					event.delay(getTicks(delay));
					var ninjaWebNpc = new NPC(8376).spawn(spawn);
					var webObject = GameObject.spawn(32734, spawn, 11, 0);
					var web = new VerzikWeb(webObject, ninjaWebNpc);
					webList.add(web);
					possibleSpawns.remove(spawn);
				}
				if (ticks == 12) {
					canAttack = true;
					npc.unlock();
				}
			}
		}).setCancelCondition(() -> isDead() || getNpc().isRemoved());
	}
	private Position getRandomPosition(List<Position> possibleSpawns) {
		var spawn = Random.get(possibleSpawns);
		assert spawn != null;
		if (!spawn.inBounds(bossRoom))
			return getRandomPosition(possibleSpawns);
		if (!Tile.get(spawn).isFloorFree())
			return getRandomPosition(possibleSpawns);
		return spawn;
	}

	private void startWebSpecial() {
		canAttack = false;
		World.startEvent(e -> {
			npc.getRouteFinder().routeAbsolute(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 23);
			e.delay(3);
			shootingWebs = true;
			disperseWebs(Random.get(getAlivePlayers()));
		});
	}

	private void knockback(Player target) {
		if (target == null) return;
		int vecX = (target.getAbsX() - getClosestX(target));
		if (vecX != 0)
			vecX /= Math.abs(vecX); // determines X component for knockback
		int vecY = (target.getAbsY() - getClosestY(target));
		if (vecY != 0)
			vecY /= Math.abs(vecY); // determines Y component for knockback
		int endX = target.getAbsX();
		int endY = target.getAbsY();
		for (int i = 0; i < 4; i++) {
			if (DumbRoute.getDirection(endX, endY, npc.getHeight(), target.getSize(), endX + vecX, endY + vecY) != null) {
				endX += vecX;
				endY += vecY;
			} else
				break; // cant take the step, stop here
		}
		if (endX != target.getAbsX() || endY != target.getAbsY()) {
			if (target.player != null) {
				final Player p = target.player;
				p.animate(1157);
				p.graphics(245, 5, 124);
				p.stun(2, true);
				p.getMovement().teleport(endX, endY, npc.getHeight());
			}
		} else {
			if (npc.getHp() > 0)
				target.hit(new Hit().fixedDamage(20));
			target.animate(1157);
			target.graphics(245, 5, 124);
			target.stun(2, true);
		}
	}

	private int getClosestX(Entity target) {
		if (target.getAbsX() < npc.getAbsX())
			return npc.getAbsX();
		else if (target.getAbsX() >= npc.getAbsX() && target.getAbsX() <= npc.getAbsX() + npc.getSize() - 1)
			return target.getAbsX();
		else
			return npc.getAbsX() + npc.getSize() - 1;
	}

	private int getClosestY(Entity target) {
		if (target.getAbsY() < npc.getAbsY())
			return npc.getAbsY();
		else if (target.getAbsY() >= npc.getAbsY() && target.getAbsY() <= npc.getAbsY() + npc.getSize() - 1)
			return target.getAbsY();
		else
			return npc.getAbsY() + npc.getSize() - 1;
	}

	private VerzikNPC asVerzik() {
		return (VerzikNPC) npc;
	}

	private boolean edgeInterceptRadius(Entity originalPlayer, Entity targetPlayer, Entity boss, float radius) {
		Position lineVector = new Position(0, 0, 0);
		lineVector.set(targetPlayer.getPosition().getX() - originalPlayer.getPosition().getX(), targetPlayer.getPosition().getY() - originalPlayer.getPosition().getY());

		Position centreVector = new Position(0, 0, 0);
		centreVector.set(originalPlayer.getPosition().getX() - boss.getPosition().getX(), originalPlayer.getPosition().getY() - boss.getPosition().getY());

		float lineDot = dot(lineVector, lineVector);
		float centreLineDot = 2 * dot(centreVector, lineVector);
		float centreDot = dot(centreVector, centreVector) - radius * radius;

		float disc = centreLineDot * centreLineDot - 4 * lineDot * centreDot;

		if (disc < 0)
			return false;
		else {
			disc = (float) Math.sqrt(disc);
			float t1 = (-centreLineDot - disc) / (2 * lineDot);
			float t2 = (-centreLineDot + disc) / (2 * lineDot);

			if (t1 >= 0 && t1 <= 1)
				return true;
			if (t2 >= 0 && t2 < 1)
				return true;
		}
		return false;
	}

	public float dot(Position pointA, Position pointB) {
		return (float) (pointA.getX() * pointB.getX() + pointA.getY() * pointB.getY());
	}


	private void tornados() {
		npc.localPlayers().forEach(p -> {
			NPC tornado = new NPC(20015).spawn(bossRoom.randomPosition());
			AtomicInteger ticks = new AtomicInteger();
			tornado.startEvent(event -> {
				while (ticks.getAndIncrement() < 30) {
					tornado.animate(-1);
					tornado.graphics(1602);
					tornado.stepAbs(p.getPosition().getX(), p.getPosition().getY(), StepType.WALK);
					if (tornado.getPosition().isWithinDistance(p.getPosition(), 1) && npc.getHp() > 0) {
						p.hit(new Hit(npc).fixedDamage(p.getHp() / 2));
						npc.hit(new Hit(HitType.HEAL).randDamage(30, 55));
					}
					event.delay(1);

					if (ticks.get() >= 30) {
						tornado.remove();
					}
				}
			});
		});
	}

	private void purpleSlowOrb(NPC npc, Entity target) {
		if (target == null) return;
		Position targetPos = target.getPosition();
		World.startEvent(e -> {
			int delay = RED_ORB.send(npc, targetPos);
			e.delay(5);
			World.sendGraphics(1, 0, delay, targetPos);
			for (Player player :
				npc.getPosition().getRegion().players) {
				if (player.getPosition().isWithinDistance(targetPos, 0))
					player.hit(new Hit(npc, AttackStyle.MAGICAL_MELEE).randDamage(50, 75));
			}
		});
	}

	private void greenBomb(NPC npc, Entity target) {
		npc.animate(8123);
		if (target == null) return;
		World.startEvent(event -> {
			Entity from;
			Entity to = target;
			List<Player> targets = new ArrayList<>(getAlivePlayers());
			Set<Player> bouncedPlayers = new HashSet<>();

			bouncedPlayers.add(to.player);
			int maxBounces = targets.size();
			int bounces = 0;

			while (bounces < maxBounces) {
				Player nextTarget = getNearestValidTarget(to, targets, bouncedPlayers);
				if (nextTarget == null) {
					to.hit(new Hit().randDamage(40, 70));
					break;
				}
				from = to;
				to = nextTarget;
				bouncedPlayers.add(to.player);
				if (bouncedPlayers.contains(to.player)) {
					to.hit(new Hit().randDamage(40, 70));
					from.hit(new Hit().randDamage(40, 70));
					break;
				}
				int delay = GREEN_BOMB.send(from, to);
				int ticks = World.getTicks(delay) + 1;
				event.delay(ticks);
				bounces++;
			}
		}).setCancelCondition(() -> isDead() || getNpc().isRemoved());
	}

	private Player getNearestValidTarget(Entity from, List<Player> players, Set<Player> bouncedPlayers) {
		Player nearest = null;
		double minDistance = Double.MAX_VALUE;

		for (Player p : players) {
			if (!bouncedPlayers.contains(p)) {
				double distance = from.getPosition().distance(p.getPosition());
				if (distance < minDistance) {
					minDistance = distance;
					nearest = p;
				}
			}
		}
		return nearest;
	}


	private void rangedAttack(Entity target) {
		npc.animate(8114);
		Position position = target.getPosition().copy();
		AtomicInteger damage = new AtomicInteger(50);
		World.startEvent(event -> {
			event.delay(1);
			if (ProjectileRoute.allow(npc, position)) {
				int delay = RANGE_ATTACK.send(npc, position);
				Hit hit = new Hit(npc, AttackStyle.RANGED)
					.randDamage(damage.get());
				event.delay(getTicks(delay) + 1);
				World.sendGraphics(1584, 0, 0, position);
				if (target.getPosition().distance(position) == 0) {
					damagedPlayer = true;
					if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
						damage.updateAndGet(v -> v / 2);
					if (npc.getHp() > 0)
						target.hit(hit);
				}
			}
		}).setCancelCondition(() -> isDead() || getNpc().isRemoved());
	}

	private int getTicks(int delay) {
		return Math.max(1, (delay * 16) / Server.tickMs());
	}


	private void electricBall(Entity target, NPC npc) {
		npc.animate(8123);
		npc.localPlayers().forEach(p -> {
			if (ProjectileRoute.allow(npc, p)) {
				int delay = ELECTRIC.send(npc, p);
				Hit hit = new Hit(npc, AttackStyle.MAGIC)
					.randDamage(45, 75)
					.clientDelay(delay);
				hit.postDamage(t -> {
					if (hit.damage > 0) {
						t.graphics(1581, 0, 0);
					}
				});
				p.hit(hit);
			}
		});
	}
}

