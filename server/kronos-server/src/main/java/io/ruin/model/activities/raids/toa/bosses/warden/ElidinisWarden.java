package io.ruin.model.activities.raids.toa.bosses.warden;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.raids.toa.Invocations;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.TickDelay;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class ElidinisWarden extends NPCCombat {
	//2205 circle red blob tjing one of specials
	//2235 maybe windmill

	Projectile swordSpecialProjectile = new Projectile(2204, 60, 31, 25, 35, 39, 0, 32).regionBased();
	Projectile arrowSpecialProjectile = new Projectile(2206, 60, 31, 25, 35, 39, 0, 32).regionBased();
	Projectile blueballSpecialProjectile = new Projectile(2208, 60, 31, 25, 35, 39, 0, 32).regionBased();
	Projectile mageProjectile = new Projectile(2224, 60, 31, 25, 35, 10, 0, 32).regionBased();
	Projectile rangeProjectile = new Projectile(2241, 60, 31, 25, 35, 10, 0, 32).regionBased();
	Projectile stuckInStoneProjectile = new Projectile(2210, 60, 31, 25, 35, 29, 0, 32).regionBased();
	Projectile skullShadowProjectile = new Projectile(2225, 60, 31, 25, 35, 21, 0, 32).regionBased();

	Bounds swQuadrant;
	Bounds seQuadrant;
	Bounds nwQuadrant;
	Bounds neQuadrant;
	Bounds bossBounds;

	NPC core;

	private static final int MAGE_FORM = 11753;
	private static final int RANGE_FORM = 11754;
	private int attacksSinceSpecial = 0;

	List<String> playersInSpecial = new ArrayList<>();

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDefend(this::postDefend).preDefend(this::preDefend);
		swQuadrant = new Bounds(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 23, npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 31, 1);
		seQuadrant = new Bounds(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 23, npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 33, 1);
		nwQuadrant = new Bounds(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 35, npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 45, 1);
		neQuadrant = new Bounds(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 36, npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 45, 1);
		bossBounds = new Bounds(npc.getPosition().getRegion().baseX + 18, npc.getPosition().getRegion().baseY + 19, npc.getPosition().getRegion().baseX + 46, npc.getPosition().getRegion().baseY + 49, 1);
		reloadSpecials();
		core = new NPC(11771).spawn(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 37, 1, Direction.EAST, 0);
		core.setHidden(true);
		core.getCombat().setAllowRetaliate(false);
		core.deathStartListener = (entity, killer, hit) -> completeRoom();
	}

	@Override
	public int getAggressionRange() {
		return 1000;
	}

	private static String format(long millis) {
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
		return String.format("%02d:%02d", minutes, seconds);
	}

	private void completeRoom() {
		if (coreDowns < 2) {
			npc.getPosition().getRegion().players.forEach(p -> {
				if (p.getCurrentToARaid() != null && !p.getCurrentToARaid().deadMembers.contains(p.getName())) {
					Objects.requireNonNull(p.combatAchievementsList
						.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.DOWN_DO_SPECS.ordinal()))
						.getCombatAchievement()).check(p);
				}
			});
		}
		npc.animate(9662);
		World.startEvent(e -> {
			e.delay(8);
			Player player = npc.getPosition().getRegion().players.getFirst();
			long tumekenTime = timer.stop(player, -1);
			npc.getPosition().getRegion().players.forEach(p -> {
				if (p != null) {
					p.getStats().restore(false);
					p.cureVenom(0);
					p.getCombat().restoreSpecial(100);
					p.getCombat().reset();
					if (p.elidinisBestTime == -1) {
						p.sendMessage("Duration: <col=ef1020>" + format(tumekenTime));
						p.elidinisBestTime = tumekenTime;
					} else if (p.elidinisBestTime == 0 || tumekenTime < p.elidinisBestTime) {
						p.sendMessage("Duration: <col=ef1020>" + format(tumekenTime) + "</col> (new personal best)");
						p.elidinisBestTime = tumekenTime;
					} else {
						p.sendMessage("Duration: <col=ef1020>" + format(tumekenTime) + "</col>. Personal best: " + format(p.elidinisBestTime));
					}
				}
				if (p.currentToARaidId != -1 && p.getCurrentToARaid() != null) {
					if (p.getCurrentToARaid().deadMembers.contains(p.getName()))
						return;
					if (p.getCurrentToARaid().currentParty.getLeader().equalsIgnoreCase(p.getName()))
						p.getCurrentToARaid().currentRoom = "wardensp2";
					p.getCurrentToARaid().setDeathPosition(new Position(p.getPosition().getRegion().baseX + 29, p.getPosition().getRegion().baseY + 44, 2));
					p.getMovement().teleport(p.getCurrentToARaid().getRooms().get("wardensp2").getEnterPosition());
				}
			});
		});
	}

	private void preDefend(Hit hit) {
		if (npc.getId() == MAGE_FORM) {
			if (hit.attackStyle != AttackStyle.MAGIC) {
				hit.block();
				return;
			} else {
				hit.boostDamage(0.3);
				hit.boostAttack(2.0);
			}
		}
		if (npc.getId() == RANGE_FORM && hit.attackStyle != AttackStyle.RANGED) {
			hit.block();
			return;
		}
		//TODO: Room completion deadmembers?

	}

	Projectile coreProjectile = new Projectile(2227, 60, 0, 25, 35, 10, 0, 32).regionBased();
	boolean firstCore = true;
	int coreDowns = 0;

	private void sendCore() {
		npc.getPosition().getRegion().players.forEach(p -> p.getCombat().reset());
		coreDowns++;
		if (firstCore) {
			firstCore = false;
			Player plr = npc.getPosition().getRegion().players.getFirst();
			if (plr != null) {
				if (plr.getCurrentToARaid() != null) {
					plr.getCurrentToARaid().scaleNPC(core, 6);
				}
			}
		}
		World.startEvent(event -> {
			event.delay(1);
			int delay = coreProjectile.send(npc, core);
			event.delay(World.getTicks(delay) + 1);
			core.setHidden(false);
			core.getCombat().getInfo().aggressive_level = 1;
			core.getCombat().setAllowRetaliate(false);
			event.delay(35);
			core.getCombat().reset();
			core.setHidden(true);
		});
	}

	private void postDefend(Hit hit) {
	}

	@Override
	public void follow() {

	}

	TickDelay specialDelay = new TickDelay();
	List<Invocations> invocations = new ArrayList<>();

	@Override
	public boolean attack() {
		if (invocations.contains(Invocations.ACCELERATION))
			npc.getCombat().getInfo().attack_ticks = 6;
		getPlayers().forEach(p -> {
			if (p.getPosition().distance(npc.getPosition()) < 3)
				meleeAttack();
		});

		if (attacksSinceSpecial++ >= 7 && specialDelay.finished()) {
			attacksSinceSpecial = 0;
			sendSpecial(Random.get(specials));
		}

		if (Random.get(10) == 0) {
			attackingWithMage = !attackingWithMage;
		}
		if (attackingWithMage) {
			mageAttack();
		} else {
			rangeAttack();
		}
		return true;
	}

	int lastForm;

	@Override
	public void startDeath(Hit hit) {
		sendCore();
		lastForm = npc.getId();
		npc.transform(11755);
		npc.animate(9670);
		npc.lock();
		World.startEvent(event -> {
			event.delay(42);
			if (core.getHp() > 1) {
				npc.transform(lastForm == RANGE_FORM ? MAGE_FORM : RANGE_FORM);
				npc.animate(9672);
				event.delay(1);
				npc.unlock();
				npc.getCombat().restore();
			}
		});
	}

	private void sendSpecial(String specialId) {
		switch (specialId) {
			case "shadowLightning":
				shadowLightningAttack();
				break;
			case "lines":
				handleLinesSpecial();
				break;
			case "windmill":
				handleWindmillSpecial();
				windmillSpecialDelay.delay(35);
				break;
		}
		specials.remove(specialId);
		if (specials.isEmpty())
			reloadSpecials();
		specialDelay.delay(35);
	}

	List<String> specials = new ArrayList<>();

	private void reloadSpecials() {
		specials.clear();
		specials.add("shadowLightning");
		specials.add("lines");
		specials.add("windmill");
	}


	boolean attackingWithMage = false;
	boolean invocationsSet = false;
	public ActivityTimer timer;
	boolean timerSet = false;

	@Override
	public void process() {
		if (!npc.getPosition().getRegion().players.isEmpty() && Objects.nonNull(player.getCurrentToARaid())) {
			if (!timerSet) {
				timer = new ActivityTimer();
				timerSet = true;
			}
			if (!invocationsSet) {
				Player player = npc.getPosition().getRegion().players.getFirst();
				invocationsSet = true;
				invocations = player.getCurrentToARaid().getInvocations();
			}
		}
	}

	private void meleeAttack() {
		npc.animate(9770);
		if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE)) {
			if (target.player != null && target.player.getCurrentToARaid() != null && target.player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
				target.hit(new Hit(npc).randDamage(15).ignorePrayer());
			else
				target.hit(new Hit(npc).randDamage(12));
		} else {
			target.hit(new Hit(npc).randDamage(42).ignorePrayer());
			if (target.player != null && target.player.getCurrentToARaid() != null)
				target.player.getCurrentToARaid().failedPerfectWarden = true;
		}
	}

	private void launchSwordSpecial(Player target) {
		playersInSpecial.add(target.getName());
		World.startEvent(e -> {
			npc.animate(9660);
			e.delay(1);
			int delay;
			if (npc.getPosition().distance(target.getPosition()) < 6)
				delay = new Projectile(2204, 60, 31, 25, 35, 41, 0, 32).regionBased().send(npc, target);
			else delay = swordSpecialProjectile.send(npc, target);
			e.delay(World.getTicks(delay) + 1);
			playersInSpecial.remove(target.getName());
			int damage = 50;
			if (target.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE)) {
				damage = 8;
				if (target.player != null && target.player.getCurrentToARaid() != null && target.player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
					damage = 10;
			} else {
				if (target.player != null && target.player.getCurrentToARaid() != null)
				target.player.getCurrentToARaid().failedPerfectWarden = true;
			}
			target.hit(new Hit(npc).randDamage(damage).ignorePrayer());
		});
	}

	private void launchArrowSpecial(Player target) {
		playersInSpecial.add(target.getName());
		World.startEvent(e -> {
			npc.animate(9660);
			e.delay(1);
			int delay;
			if (npc.getPosition().distance(target.getPosition()) < 6)
				delay = new Projectile(2206, 60, 31, 25, 35, 41, 0, 32).regionBased().send(npc, target);
			else delay = arrowSpecialProjectile.send(npc, target);
			e.delay(World.getTicks(delay) + 1);
			playersInSpecial.remove(target.getName());
			int damage = 50;
			if (target.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
				damage = 8;
				if (target.player != null && target.player.getCurrentToARaid() != null && target.player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
					damage = 10;
			} else {
				if (target.player != null && target.player.getCurrentToARaid() != null)
					target.player.getCurrentToARaid().failedPerfectWarden = true;
			}
			target.hit(new Hit(npc).randDamage(damage).ignorePrayer());
		});
	}

	private void launchBlueballSpecial(Player target) {
		playersInSpecial.add(target.getName());
		World.startEvent(e -> {
			npc.animate(9660);
			e.delay(1);
			int delay;
			if (npc.getPosition().distance(target.getPosition()) < 6)
				delay = new Projectile(2208, 60, 31, 25, 35, 41, 0, 32).regionBased().send(npc, target);
			else delay = blueballSpecialProjectile.send(npc, target);
			e.delay(World.getTicks(delay) + 1);
			playersInSpecial.remove(target.getName());
			int damage = 50;
			if (target.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
				damage = 8;
				if (target.player != null && target.player.getCurrentToARaid() != null && target.player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
					damage = 10;
			} else {
				if (target.player != null && target.player.getCurrentToARaid() != null)
					target.player.getCurrentToARaid().failedPerfectWarden = true;
			}
			target.hit(new Hit(npc).randDamage(damage).ignorePrayer());
		});
	}

	private List<Player> getPlayers() {
		List<Player> players = new ArrayList<>();
		if (npc.getPosition().getRegion().players.isEmpty()) return new ArrayList<>();
		Player player = npc.getPosition().getRegion().players.getFirst();
		if (player != null && player.getCurrentToARaid() != null) {
			player.getCurrentToARaid().currentParty.getMembers().forEach(name -> {
				Player p = World.getPlayer(name);
				if (p != null)
					players.add(p);
			});
		}
		return players;
	}

	private void rangeAttack() {
		getPlayers().forEach(player -> {
			if (player.getPosition().inBounds(bossBounds) && !playersInSpecial.contains(player.getName())) {
				if (invocations.contains(Invocations.PENETRATION) && Random.get(10) == 0) {
					player.getPrayer().deactivateProtectionPrayer();
				}
				if (Random.get(10) == 0) {
					int special = Random.get(4);
					switch (special) {
						case 0:
							launchArrowSpecial(player);
							break;
						case 1:
							launchBlueballSpecial(player);
							break;
						case 2:
							launchSwordSpecial(player);
							break;
						case 3:
							launchStuckInRockSpecial();
							break;
					}
				} else {
					World.startEvent(e -> {
						npc.animate(9660);
						e.delay(1);
						int delay = rangeProjectile.send(npc, player);
						e.delay(World.getTicks(delay));
						int damage = 50;
						if (player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
							damage = 8;
							if (target.player != null && target.player.getCurrentToARaid() != null && target.player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
								damage = 10;
						} else {
							if (target.player != null && target.player.getCurrentToARaid() != null)
								target.player.getCurrentToARaid().failedPerfectWarden = true;
						}
						player.hit(new Hit(npc).randDamage(damage).ignorePrayer());
					});
				}
			}
		});
	}

	private void launchStuckInRockSpecial() {
		Player target = Random.get(getPlayers());
		Position targetPos = target.getPosition().copy();
		for (int i = 0; i < 3; i++)
			World.sendGraphics(2111, 0, i * 15, targetPos);
		World.startEvent(e -> {
			int delay;
			if (npc.getPosition().distance(targetPos) < 6)
				delay = new Projectile(2210, 60, 31, 25, 35, 41, 0, 32).regionBased().send(npc, targetPos);
			else delay = stuckInStoneProjectile.send(npc, targetPos);
			e.delay(World.getTicks(delay) + 1);
			if (target.getPosition().distance(targetPos) < 1) {
				if (target.player != null && target.player.getCurrentToARaid() != null)
					target.player.getCurrentToARaid().failedPerfectWarden = true;
				handleStuckInStone(target);
			}
		});
	}

	private void handleStuckInStone(Player target) {
		World.startEvent(event -> {
			target.lock();
			target.getCombat().reset();
			for (int i = 0; i < 8; i++) {
				target.animate(739);
				target.graphics(2211);
				event.delay(1);
				if (i == 7) {
					target.unlock();
					target.graphics(-1);
					target.animate(-1);
				}
			}
		});
	}

	private void mageAttack() {
		getPlayers().forEach(player -> {
			if (player.getPosition().inBounds(bossBounds) && !playersInSpecial.contains(player.getName())) {
				if (invocations.contains(Invocations.PENETRATION) && Random.get(10) == 0) {
					player.getPrayer().deactivateProtectionPrayer();
				}
				if (Random.get(10) == 0) {
					int special = Random.get(4);
					switch (special) {
						case 0:
							launchArrowSpecial(player);
							break;
						case 1:
							launchBlueballSpecial(player);
							break;
						case 2:
							launchSwordSpecial(player);
							break;
						case 3:
							launchStuckInRockSpecial();
							break;
					}
				} else {
					World.startEvent(e -> {
						npc.animate(9660);
						e.delay(1);
						int delay = mageProjectile.send(npc, player);
						e.delay(World.getTicks(delay));
						int damage = 50;
						if (player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
							damage = 8;
							if (target != null && target.player != null && target.player.getCurrentToARaid() != null && target.player.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS))
								damage = 10;
						} else {
							if (target.player != null && target.player.getCurrentToARaid() != null)
								target.player.getCurrentToARaid().failedPerfectWarden = true;
						}
						player.hit(new Hit(npc).randDamage(damage).ignorePrayer());
					});
				}
			}
		});
	}

	private void shadowLightningAttack() {
		// Precompute lightning positions for all quadrants
		List<Position> lightningPositions = new ArrayList<>();

		for (Bounds bounds : new Bounds[]{swQuadrant, seQuadrant, nwQuadrant, neQuadrant}) {
			int playersInQuadrant = getPlayersInQuadrant(bounds);
			if (playersInQuadrant > 0) {
				// For quadrants with players, add player-targeted positions
				for (int i = 0; i < playersInQuadrant; i++) {
					Player target = Random.get(getPlayers());
					if (target != null) {
						Position targetPos = new Position(
							target.getPosition().getX() + Random.get(-3, 3),
							target.getPosition().getY() + Random.get(-3, 3),
							target.getPosition().getZ()
						);
						lightningPositions.add(targetPos);
					}
				}
			} else {
				// For empty quadrants, add a random position
				Position middlePoint = new Position(
					bounds.getMiddlePosition().getX() + Random.get(-3, 3),
					bounds.getMiddlePosition().getY() + Random.get(-3, 3),
					bounds.getMiddlePosition().getZ()
				);
				lightningPositions.add(middlePoint);
			}
		}

		// Now handle all positions with fewer events
		World.startEvent(e -> {
			// Send all projectiles first, saving delays
			Map<Position, Integer> delayMap = new HashMap<>();
			for (Position pos : lightningPositions) {
				int delay;
				if (npc.getPosition().distance(pos) < 6)
					delay = new Projectile(2225, 60, 31, 25, 35, 42, 0, 32).regionBased().send(npc, pos);
				else
					delay = skullShadowProjectile.send(npc, pos);

				delayMap.put(pos, World.getTicks(delay) + 1);
			}

			// Group by delays to reduce event switches
			Map<Integer, List<Position>> delayGroups = new HashMap<>();
			delayMap.forEach((pos, delay) ->
				delayGroups.computeIfAbsent(delay, k -> new ArrayList<>()).add(pos));

			// Process in delay order
			List<Integer> delays = new ArrayList<>(delayGroups.keySet());
			Collections.sort(delays);

			int currentTick = 0;
			for (int delay : delays) {
				if (delay > currentTick) {
					e.delay(delay - currentTick);
					currentTick = delay;
				}

				for (Position pos : delayGroups.get(delay)) {
					World.sendGraphics(2111, 0, 0, pos);
					// Start lightning for each target position
					sendLightning(pos, 1);
					e.delay(1);
					sendLightning(pos, 2);
					e.delay(1);
					sendLightning(pos, 3);
				}
			}
		});
	}


	public void handleLinesSpecial() {
		Position northStart = new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 36, 1);
		Position southStart = new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 32, 1);


		World.startEvent(e -> {
			e.setCancelCondition(() -> !core.isHidden());
			// Draw the vertical lines first
			for (int i = 0; i < 11; i++) {
				World.sendGraphics(2234, 0, i * 5, new Position(northStart.getX(), northStart.getY() + i, northStart.getZ()));
				World.sendGraphics(2234, 0, i * 5, new Position(southStart.getX(), southStart.getY() - i, southStart.getZ()));
			}
			e.delay(2); // Delay before drawing the triangle

			int lines = 1;
			Position northStartTriangleOne = new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 46, 1);
			Position southStartTriangleOne = new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 22, 1);
			for(int i = 0; i < 10; i++) {
				for(int j = 0; j < lines; j++) {
					Position positionOne = new Position(northStartTriangleOne.getX() - (i - j), northStartTriangleOne.getY() - j, northStartTriangleOne.getZ());
					Position positionTwo = new Position(northStartTriangleOne.getX() + (i - j), northStartTriangleOne.getY() - j, northStartTriangleOne.getZ());
					Position positionThree = new Position(southStartTriangleOne.getX() - (i - j), southStartTriangleOne.getY() + j, southStartTriangleOne.getZ());
					Position positionFour = new Position(southStartTriangleOne.getX() + (i - j), southStartTriangleOne.getY() + j, southStartTriangleOne.getZ());
					World.sendGraphics(2235, 0, 0, positionOne);
					World.sendGraphics(2235, 0, 0, positionTwo);
					World.sendGraphics(2235, 0, 0, positionThree);
					World.sendGraphics(2235, 0, 0, positionFour);
					getPlayers().forEach(player -> {
						if(player.getPosition().distance(positionOne) < 1 || player.getPosition().distance(positionTwo) < 1 || player.getPosition().distance(positionThree) < 1 || player.getPosition().distance(positionFour) < 1) {
							player.hit(new Hit(npc).randDamage(18, 25));
							if (player.getCurrentToARaid() != null)
								player.getCurrentToARaid().failedPerfectWarden = true;
						}
					});
				}
				lines++;
				if(lines == 6)
					startSideTriangles();
				e.delay(2);
			}
		});
	}


	private void startSideTriangles() {
		Position westStart = new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 34, 1);
		Position eastStart = new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 34, 1);
		World.startEvent(e -> {
			for (int i = 0; i < 11; i++) {
				World.sendGraphics(2234, 0, i * 5, new Position(westStart.getX() - i, westStart.getY(), westStart.getZ()));
				World.sendGraphics(2234, 0, i * 5, new Position(eastStart.getX() + i, eastStart.getY(), eastStart.getZ()));
			}
			e.delay(2);

			int lines = 1;
			Position westStartTriangleOne = new Position(npc.getPosition().getRegion().baseX + 21, npc.getPosition().getRegion().baseY + 34, 1);
			Position eastStartTriangleOne = new Position(npc.getPosition().getRegion().baseX + 43, npc.getPosition().getRegion().baseY + 34, 1);
			for(int i = 0; i < 10; i++) {
				for(int j = 0; j < lines; j++) {
					Position positionOne = new Position(eastStartTriangleOne.getX() - j, eastStartTriangleOne.getY() - (i - j), eastStartTriangleOne.getZ());
					Position positionTwo = new Position(eastStartTriangleOne.getX() - j, eastStartTriangleOne.getY() + (i - j), eastStartTriangleOne.getZ());
					Position positionThree = new Position(westStartTriangleOne.getX() + j, westStartTriangleOne.getY() - (i - j), westStartTriangleOne.getZ());
					Position positionFour = new Position(westStartTriangleOne.getX() + j, westStartTriangleOne.getY() + (i - j), westStartTriangleOne.getZ());
					World.sendGraphics(2235, 0, 0, positionOne);
					World.sendGraphics(2235, 0, 0, positionTwo);
					World.sendGraphics(2235, 0, 0, positionThree);
					World.sendGraphics(2235, 0, 0, positionFour);
					getPlayers().forEach(player -> {
						if(player.getPosition().distance(positionOne) < 1 || player.getPosition().distance(positionTwo) < 1 || player.getPosition().distance(positionThree) < 1 || player.getPosition().distance(positionFour) < 1) {
							player.hit(new Hit(npc).randDamage(18, 25));
						}
					});
				}
				lines++;
				e.delay(2);
			}
		});
	}



	TickDelay windmillSpecialDelay = new TickDelay();

	public void handleWindmillSpecial() {
		World.startEvent(event -> {
			event.setCancelCondition(() -> windmillSpecialDelay.finished() || !core.isHidden());
			// Define the initial windmill shape
			List<Position> windmillShapeA = createWindmillShapeA();


			for (Position position : windmillShapeA) {
				World.sendGraphics(2236, 0, 15, position);
			}
			getPlayers().forEach(player -> {
				for (Position position : windmillShapeA) {
					if (player.getPosition().distance(position) < 1) {
						player.hit(new Hit(npc).randDamage(18, 25));
						if (player.getCurrentToARaid() != null)
							player.getCurrentToARaid().failedPerfectWarden = true;
					}
				}
			});
			event.delay(2);
			for (Position position : windmillShapeA) {
				World.sendGraphics(2234, 0, 0, position);
			}
			getPlayers().forEach(player -> {
				for (Position position : windmillShapeA) {
					if (player.getPosition().distance(position) < 1) {
						player.hit(new Hit(npc).randDamage(18, 25));
						if (player.getCurrentToARaid() != null)
							player.getCurrentToARaid().failedPerfectWarden = true;
					}
				}
			});
			event.delay(1);


			List<Position> windmillShapeB = createWindmillShapeB();
			for (Position position : windmillShapeB) {
				World.sendGraphics(2236, 0, 15, position);
			}
			getPlayers().forEach(player -> {
				for (Position position : windmillShapeB) {
					if (player.getPosition().distance(position) < 1) {
						player.hit(new Hit(npc).randDamage(18, 25));
						if (player.getCurrentToARaid() != null)
							player.getCurrentToARaid().failedPerfectWarden = true;
					}
				}
			});
			event.delay(2);
			for (Position position : windmillShapeB) {
				World.sendGraphics(2234, 0, 0, position);
			}
			getPlayers().forEach(player -> {
				for (Position position : windmillShapeB) {
					if (player.getPosition().distance(position) < 1) {
						player.hit(new Hit(npc).randDamage(18, 25));
						if (player.getCurrentToARaid() != null)
							player.getCurrentToARaid().failedPerfectWarden = true;
					}
				}
			});
			event.delay(1);
			List<Position> windmillShapeC = createWindmillShapeC();
			for (Position position : windmillShapeC) {
				World.sendGraphics(2236, 0, 15, position);
			}
			getPlayers().forEach(player -> {
				for (Position position : windmillShapeC) {
					if (player.getPosition().distance(position) < 1) {
						player.hit(new Hit(npc).randDamage(18, 25));
						if (player.getCurrentToARaid() != null)
							player.getCurrentToARaid().failedPerfectWarden = true;
					}
				}
			});
			event.delay(2);
			for (Position position : windmillShapeC) {
				World.sendGraphics(2234, 0, 0, position);
			}
			getPlayers().forEach(player -> {
				for (Position position : windmillShapeC) {
					if (player.getPosition().distance(position) < 1) {
						player.hit(new Hit(npc).randDamage(18, 25));
						if (player.getCurrentToARaid() != null)
							player.getCurrentToARaid().failedPerfectWarden = true;
					}
				}
			});
			event.delay(1);
			List<Position> windmillShapeD = createWindmillShapeD();
			for (Position position : windmillShapeD) {
				World.sendGraphics(2236, 0, 15, position);
			}
			getPlayers().forEach(player -> {
				for (Position position : windmillShapeD) {
					if (player.getPosition().distance(position) < 1) {
						player.hit(new Hit(npc).randDamage(18, 25));
						if (player.getCurrentToARaid() != null)
							player.getCurrentToARaid().failedPerfectWarden = true;
					}
				}
			});
			event.delay(2);
			for (Position position : windmillShapeD) {
				World.sendGraphics(2234, 0, 0, position);
			}
			getPlayers().forEach(player -> {
				for (Position position : windmillShapeD) {
					if (player.getPosition().distance(position) < 1) {
						player.hit(new Hit(npc).randDamage(18, 25));
						if (player.getCurrentToARaid() != null)
							player.getCurrentToARaid().failedPerfectWarden = true;
					}
				}
			});
			event.delay(1);
			List<Position> windmillShapeE = createWindmillShapeE();
			for (Position position : windmillShapeE) {
				World.sendGraphics(2236, 0, 15, position);
			}
			getPlayers().forEach(player -> {
				for (Position position : windmillShapeE) {
					if (player.getPosition().distance(position) < 1) {
						player.hit(new Hit(npc).randDamage(18, 25));
						if (player.getCurrentToARaid() != null)
							player.getCurrentToARaid().failedPerfectWarden = true;
					}
				}
			});
			event.delay(2);
			for (Position position : windmillShapeE) {
				World.sendGraphics(2234, 0, 0, position);
			}
			getPlayers().forEach(player -> {
				for (Position position : windmillShapeE) {
					if (player.getPosition().distance(position) < 1) {
						player.hit(new Hit(npc).randDamage(18, 25));
						if (player.getCurrentToARaid() != null)
							player.getCurrentToARaid().failedPerfectWarden = true;
					}
				}
			});
			event.delay(1);
			if(!windmillSpecialDelay.finished())
				handleWindmillSpecial();
		});
	}
	private Map<Integer, List<Position>> windmillShapeCache = new HashMap<>();

	private List<Position> createWindmillShapeC() {//third
		return windmillShapeCache.computeIfAbsent(1, k -> {
			List<Position> shape = new ArrayList<>();
			Position southTurbineStartTile = new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 32, 1);
			for (int i = 0; i < 11; i++) {
				shape.add(new Position(southTurbineStartTile.getX(), southTurbineStartTile.getY() - i, southTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 9; i++) {
				shape.add(new Position(southTurbineStartTile.getX() - 1, (southTurbineStartTile.getY() - 2) - i, southTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position(southTurbineStartTile.getX() - 2, (southTurbineStartTile.getY() - 5) - i, southTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(southTurbineStartTile.getX() - 3, (southTurbineStartTile.getY() - 8) - i, southTurbineStartTile.getZ()));
			}
			Position eastTurbineStartTile = new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 34, 1); // Adjust the starting position accordingly
			for (int i = 0; i < 11; i++) {
				shape.add(new Position(eastTurbineStartTile.getX() + i, eastTurbineStartTile.getY(), eastTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 9; i++) {
				shape.add(new Position((eastTurbineStartTile.getX() + 2) + i, eastTurbineStartTile.getY() - 1, eastTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position((eastTurbineStartTile.getX() + 5) + i, eastTurbineStartTile.getY() - 2, eastTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position((eastTurbineStartTile.getX() + 8) + i, eastTurbineStartTile.getY() - 3, eastTurbineStartTile.getZ()));
			}
			Position northTurbineStartTile = new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 36, 1); // Adjust the starting position accordingly
			for (int i = 0; i < 11; i++) {
				shape.add(new Position(northTurbineStartTile.getX(), northTurbineStartTile.getY() + i, northTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 9; i++) {
				shape.add(new Position(northTurbineStartTile.getX() + 1, (northTurbineStartTile.getY() + 2) + i, northTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position(northTurbineStartTile.getX() + 2, (northTurbineStartTile.getY() + 5) + i, northTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(northTurbineStartTile.getX() + 3, (northTurbineStartTile.getY() + 8) + i, northTurbineStartTile.getZ()));
			}
			Position westTurbineStartTile = new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 34, 1); // Adjust the starting position accordingly
			for (int i = 0; i < 11; i++) {
				shape.add(new Position(westTurbineStartTile.getX() - i, westTurbineStartTile.getY(), westTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 9; i++) {
				shape.add(new Position((westTurbineStartTile.getX() - 2) - i, westTurbineStartTile.getY() + 1, westTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position((westTurbineStartTile.getX() - 5) - i, westTurbineStartTile.getY() + 2, westTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position((westTurbineStartTile.getX() - 8) - i, westTurbineStartTile.getY() + 3, westTurbineStartTile.getZ()));
			}

			return shape;
		});
	}

	private List<Position> createWindmillShapeE() {//fifth
		return windmillShapeCache.computeIfAbsent(1, k -> {
			List<Position> shape = new ArrayList<>();
			//North east
			shape.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 35, 1));
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 35 + i, 1));
			}
			shape.add(new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 36, 1));
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 36 + i, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 36 + i, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 37 + i, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 40, npc.getPosition().getRegion().baseY + 37 + i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 37 + i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 38 + i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 43, npc.getPosition().getRegion().baseY + 38 + i, 1));
			}
			//North west
			shape.add(new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 36, 1));
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 31 - i, npc.getPosition().getRegion().baseY + 37, 1));
			}
			shape.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 38, 1));
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 30 - i, npc.getPosition().getRegion().baseY + 39, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 30 - i, npc.getPosition().getRegion().baseY + 40, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 29 - i, npc.getPosition().getRegion().baseY + 41, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 29 - i, npc.getPosition().getRegion().baseY + 42, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 29 - i, npc.getPosition().getRegion().baseY + 43, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 28 - i, npc.getPosition().getRegion().baseY + 44, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 29 - i, npc.getPosition().getRegion().baseY + 45, 1));
			}
			//South east
			shape.add(new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 32, 1));
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 33 + i, npc.getPosition().getRegion().baseY + 31, 1));
			}
			shape.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 30, 1));
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 35 + i, npc.getPosition().getRegion().baseY + 29, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 36 + i, npc.getPosition().getRegion().baseY + 28, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 37 + i, npc.getPosition().getRegion().baseY + 27, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 38 + i, npc.getPosition().getRegion().baseY + 26, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 39 + i, npc.getPosition().getRegion().baseY + 25, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 40 + i, npc.getPosition().getRegion().baseY + 24, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 41 + i, npc.getPosition().getRegion().baseY + 23, 1));
			}
			//South west
			shape.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 33, 1));
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 33 - i, 1));
			}
			shape.add(new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 32, 1));
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 32 - i, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 32 - i, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 31 - i, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 31 - i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 23, npc.getPosition().getRegion().baseY + 31 - i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 30 - i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 21, npc.getPosition().getRegion().baseY + 30 - i, 1));
			}

			return shape;
		});
	}

	private List<Position> createWindmillShapeD() {//fourth
		return windmillShapeCache.computeIfAbsent(1, k -> {
			List<Position> shape = new ArrayList<>();
			Position southTurbineStartTile = new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 32, 1);
			for (int i = 0; i < 11; i++) {
				shape.add(new Position(southTurbineStartTile.getX(), southTurbineStartTile.getY() - i, southTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 9; i++) {
				shape.add(new Position(southTurbineStartTile.getX() + 1, (southTurbineStartTile.getY() - 2) - i, southTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position(southTurbineStartTile.getX() + 2, (southTurbineStartTile.getY() - 5) - i, southTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(southTurbineStartTile.getX() + 3, (southTurbineStartTile.getY() - 8) - i, southTurbineStartTile.getZ()));
			}
			Position eastTurbineStartTile = new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 34, 1); // Adjust the starting position accordingly
			for (int i = 0; i < 11; i++) {
				shape.add(new Position(eastTurbineStartTile.getX() + i, eastTurbineStartTile.getY(), eastTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 9; i++) {
				shape.add(new Position((eastTurbineStartTile.getX() + 2) + i, eastTurbineStartTile.getY() + 1, eastTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position((eastTurbineStartTile.getX() + 5) + i, eastTurbineStartTile.getY() + 2, eastTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position((eastTurbineStartTile.getX() + 8) + i, eastTurbineStartTile.getY() + 3, eastTurbineStartTile.getZ()));
			}
			Position northTurbineStartTile = new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 36, 1); // Adjust the starting position accordingly
			for (int i = 0; i < 11; i++) {
				shape.add(new Position(northTurbineStartTile.getX(), northTurbineStartTile.getY() + i, northTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 9; i++) {
				shape.add(new Position(northTurbineStartTile.getX() - 1, (northTurbineStartTile.getY() + 2) + i, northTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position(northTurbineStartTile.getX() - 2, (northTurbineStartTile.getY() + 5) + i, northTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(northTurbineStartTile.getX() - 3, (northTurbineStartTile.getY() + 8) + i, northTurbineStartTile.getZ()));
			}
			Position westTurbineStartTile = new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 34, 1); // Adjust the starting position accordingly
			for (int i = 0; i < 11; i++) {
				shape.add(new Position(westTurbineStartTile.getX() - i, westTurbineStartTile.getY(), westTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 9; i++) {
				shape.add(new Position((westTurbineStartTile.getX() - 2) - i, westTurbineStartTile.getY() - 1, westTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position((westTurbineStartTile.getX() - 5) - i, westTurbineStartTile.getY() - 2, westTurbineStartTile.getZ()));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position((westTurbineStartTile.getX() - 8) - i, westTurbineStartTile.getY() - 3, westTurbineStartTile.getZ()));
			}

			return shape;
		});
	}

	private List<Position> createWindmillShapeUnknown() {
		List<Position> shape = new ArrayList<>();
		//North
		shape.add(new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 36, 1));
		shape.add(new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 37, 1));
		shape.add(new Position(npc.getPosition().getRegion().baseX + 40, npc.getPosition().getRegion().baseY + 46, 1));

		for (int i = 0; i < 4; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 37 + i, 1));
		}
		for (int i = 0; i < 5; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 39 + i, 1));
		}
		for (int i = 0; i < 7; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 40 + i, 1));
		}
		for (int i = 0; i < 6; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 41 + i, 1));
		}
		for (int i = 0; i < 4; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 43 + i, 1));
		}
		for (int i = 0; i < 3; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 44 + i, 1));
		}
		//South
		shape.add(new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 32, 1));
		shape.add(new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 31, 1));
		shape.add(new Position(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 22, 1));
		for (int i = 0; i < 4; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 31 - i, 1));
		}
		for (int i = 0; i < 5; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 29 - i, 1));
		}
		for (int i = 0; i < 7; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 28 - i, 1));
		}
		for (int i = 0; i < 6; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 27 - i, 1));
		}
		for (int i = 0; i < 4; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 25 - i, 1));
		}
		for (int i = 0; i < 3; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 24 - i, 1));
		}
		//East
		shape.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 33, 1));
		shape.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 33, 1));
		for (int i = 0; i < 4; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 35 + i, npc.getPosition().getRegion().baseY + 32, 1));
		}
		for (int i = 0; i < 5; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 37 + i, npc.getPosition().getRegion().baseY + 31, 1));
		}
		for (int i = 0; i < 7; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 38 + i, npc.getPosition().getRegion().baseY + 30, 1));
		}
		for (int i = 0; i < 6; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 39 + i, npc.getPosition().getRegion().baseY + 29, 1));
		}
		for (int i = 0; i < 4; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 41 + i, npc.getPosition().getRegion().baseY + 28, 1));
		}
		for (int i = 0; i < 3; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 42 + i, npc.getPosition().getRegion().baseY + 27, 1));
		}
		//West
		shape.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 35, 1));
		shape.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 35, 1));
		for (int i = 0; i < 4; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 29 - i, npc.getPosition().getRegion().baseY + 36, 1));
		}
		for (int i = 0; i < 5; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 27 - i, npc.getPosition().getRegion().baseY + 37, 1));
		}
		for (int i = 0; i < 7; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 26 - i, npc.getPosition().getRegion().baseY + 38, 1));
		}
		for (int i = 0; i < 6; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 25 - i, npc.getPosition().getRegion().baseY + 39, 1));
		}
		for (int i = 0; i < 4; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 23 - i, npc.getPosition().getRegion().baseY + 40, 1));
		}
		for (int i = 0; i < 3; i++) {
			shape.add(new Position(npc.getPosition().getRegion().baseX + 22 - i, npc.getPosition().getRegion().baseY + 41, 1));
		}
		return shape;
	}

	private List<Position> createWindmillShapeA() {//first
		return windmillShapeCache.computeIfAbsent(1, k -> {
			List<Position> shape = new ArrayList<>();
			//NorthEast
			shape.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 36, 1));
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 37 + i, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 37 + i, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 38 + i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 39 + i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 40 + i, 1));
			}
			for (int i = 0; i < 7; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 40, npc.getPosition().getRegion().baseY + 40 + i, 1));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 41 + i, 1));
			}
			for (int i = 0; i < 5; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 42 + i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 43, npc.getPosition().getRegion().baseY + 42 + i, 1));
			}
			//North west
			shape.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 36, 1));
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 37 + i, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 37 + i, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 38 + i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 39 + i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 40 + i, 1));
			}
			for (int i = 0; i < 7; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 40 + i, 1));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 23, npc.getPosition().getRegion().baseY + 41 + i, 1));
			}
			for (int i = 0; i < 5; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 42 + i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 21, npc.getPosition().getRegion().baseY + 42 + i, 1));
			}
			//South east
			shape.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 32, 1));
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 31 - i, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 31 - i, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 30 - i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 29 - i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 28 - i, 1));
			}
			for (int i = 0; i < 7; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 40, npc.getPosition().getRegion().baseY + 28 - i, 1));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 27 - i, 1));
			}
			for (int i = 0; i < 5; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 26 - i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 43, npc.getPosition().getRegion().baseY + 26 - i, 1));
			}
			//South west
			shape.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 32, 1));
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 31 - i, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 31 - i, 1));
			}
			for (int i = 0; i < 3; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 30 - i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 29 - i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 28 - i, 1));
			}
			for (int i = 0; i < 7; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 28 - i, 1));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 23, npc.getPosition().getRegion().baseY + 27 - i, 1));
			}
			for (int i = 0; i < 5; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 22, npc.getPosition().getRegion().baseY + 26 - i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 21, npc.getPosition().getRegion().baseY + 26 - i, 1));
			}
			return shape;
		});
	}

	private List<Position> createWindmillShapeB() {//second
		return windmillShapeCache.computeIfAbsent(1, k -> {
			List<Position> shape = new ArrayList<>();
			//North
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 36 + i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 37 + i, 1));
			}
			for (int i = 0; i < 5; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 39 + i, 1));
			}
			for (int i = 0; i < 7; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 40 + i, 1));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 41 + i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 43 + i, 1));
			}
			shape.add(new Position(npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 46, 1));
			//South
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 32 - i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 31 - i, 1));
			}
			for (int i = 0; i < 5; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 29 - i, 1));
			}
			for (int i = 0; i < 7; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 28 - i, 1));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 27 - i, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 25 - i, 1));
			}
			shape.add(new Position(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 22, 1));
			//East
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 34 + i, npc.getPosition().getRegion().baseY + 33, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 35 + i, npc.getPosition().getRegion().baseY + 32, 1));
			}
			for (int i = 0; i < 5; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 37 + i, npc.getPosition().getRegion().baseY + 31, 1));
			}
			for (int i = 0; i < 7; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 38 + i, npc.getPosition().getRegion().baseY + 30, 1));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 39 + i, npc.getPosition().getRegion().baseY + 29, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 41 + i, npc.getPosition().getRegion().baseY + 28, 1));
			}
			shape.add(new Position(npc.getPosition().getRegion().baseX + 43, npc.getPosition().getRegion().baseY + 26, 1));
			//West
			for (int i = 0; i < 2; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 30 - i, npc.getPosition().getRegion().baseY + 35, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 29 - i, npc.getPosition().getRegion().baseY + 36, 1));
			}
			for (int i = 0; i < 5; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 27 - i, npc.getPosition().getRegion().baseY + 37, 1));
			}
			for (int i = 0; i < 7; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 26 - i, npc.getPosition().getRegion().baseY + 38, 1));
			}
			for (int i = 0; i < 6; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 25 - i, npc.getPosition().getRegion().baseY + 39, 1));
			}
			for (int i = 0; i < 4; i++) {
				shape.add(new Position(npc.getPosition().getRegion().baseX + 23 - i, npc.getPosition().getRegion().baseY + 40, 1));
			}
			shape.add(new Position(npc.getPosition().getRegion().baseX + 21, npc.getPosition().getRegion().baseY + 42, 1));
			return shape;
		});
	}


	private void handleShadowLightning(Position shadowPos) {
		int delay;
		if (npc.getPosition().distance(shadowPos) < 6)
			delay = new Projectile(2225, 60, 31, 25, 35, 42, 0, 32).regionBased().send(npc, shadowPos);
		else delay = skullShadowProjectile.send(npc, shadowPos);
		World.startEvent(event -> {
			World.sendGraphics(2111, 0, 0, shadowPos);
			event.delay(World.getTicks(delay) + 1);

			// Start sending lightning for each radius, staggering the events
			for (int radius = 1; radius <= 3; radius++) {
				sendLightning(shadowPos, radius);
				event.delay(1); // Adjust the delay between each radius as needed
			}
		});
	}

	private void sendLightning(Position shadowPos, int radius) {
		// Get all affected positions once
		List<Position> lightningPositions = new ArrayList<>();

		for (int x = shadowPos.getX() - radius; x <= shadowPos.getX() + radius; x++) {
			for (int y = shadowPos.getY() - radius; y <= shadowPos.getY() + radius; y++) {
				if (Math.abs(x - shadowPos.getX()) == radius || Math.abs(y - shadowPos.getY()) == radius) {
					lightningPositions.add(new Position(x, y, shadowPos.getZ()));
				}
			}
		}

		// Check player positions just once
		Map<Position, Player> playerPositions = new HashMap<>();
		for (Player player : getPlayers()) {
			playerPositions.put(player.getPosition(), player);
		}

		// Apply damage and graphics
		for (Position pos : lightningPositions) {
			Player player = playerPositions.get(pos);
			if (player != null) {
				player.hit(new Hit(npc).randDamage(40).ignorePrayer());
				if (player.getCurrentToARaid() != null)
					player.getCurrentToARaid().failedPerfectWarden = true;
			}
			World.sendGraphics(2198, 0, 0, pos);
		}
	}


	private int getPlayersInQuadrant(Bounds quadr) {
		return (int) getPlayers().stream().filter(p -> p.getPosition().inBounds(quadr)).count();
	}

}
