package io.ruin.model.activities.bosses.grotesqueguardians;

import io.ruin.api.utils.Random;
import io.ruin.cache.NpcID;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.route.routes.DumbRoute;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Dusk extends NPCCombat {
	Bounds bossBounds;

	int attackingWith = 0;

	NPC dawn;

	private static final int NON_ATTACKABLE_DAWN = 7402;
	private static final Projectile RANGED_PROJECTILE = new Projectile(1437, 60, 31, 35, 35, 22, 0, 32);


	@Override
	public void init() {
		dawn = new NPC(7852).spawn(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 36, 0, 1);
		bossBounds = new Bounds(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 23, npc.getPosition().getRegion().baseX + 40, npc.getPosition().getRegion().baseY + 38, 0);
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preDefend);
		npc.localNpcs().forEach(npc -> {
			if (npc.getId() == NpcID.DAWN || npc.getId() == NpcID.DAWN_7852 || npc.getId() == NpcID.DAWN_7853 || npc.getId() == NpcID.DAWN_7884 || npc.getId() == NpcID.DAWN_7885) {
				dawn = npc;
				dawn.deathEndListener = (entity, killer, hit) -> {
					flamePrisonSpecial(killer.player);
				};
			}
		});
	}

	private void preDefend(Hit hit) {
		if (!dawn.isHidden() && dawn.getHp() > 0) {
			if (hit.attacker != null && hit.attacker.isPlayer())
				hit.attacker.player.sendFilteredMessage("Dusk blocks all incoming damage using its wings!");
			hit.block();
		}
		if (hit.attacker != null && hit.attacker.isPlayer() && hit.attackStyle != null && !hit.attackStyle.isMelee()) {
			hit.attacker.player.sendMessage("Dusk is immune to ranged and magic attacks!");
			hit.block();
		}
	}


	private void startEndLightsEvent(Position pos) {
		World.startEvent(e -> {
			World.sendGraphics(1418, 0, 0, pos);
			e.delay(2);
			for (int i = 0; i < 5; i++) {
				World.sendGraphics(1420, 0, 0, pos);
				npc.localPlayers().forEach(p -> {
					if (p.getPosition().distance(pos) < 1) {
						p.hit(new Hit().fixedDamage(14));
					}
				});
			}
		});
	}

	private void duskMoveEvent() {
		lightsActive = true;
		npc.lock();
		Position westSpawn = new Position(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 30, 0);
		World.startEvent(e -> {
			npc.stepAbs(westSpawn.getX(), westSpawn.getY(), StepType.WALK);
			e.delay(6);
			npc.unlock();
			npc.face(Direction.EAST);
			npc.lock();
			for (int i = 0; i < 6; i++) {
				Position randomPosition = Random.get(possibleImpactPositions);
				possibleImpactPositions.remove(randomPosition);
				startEndLightsEvent(randomPosition);
			}
			e.delay(5);
			for (int i = 0; i < 6; i++) {
				Position randomPosition = Random.get(possibleImpactPositions);
				possibleImpactPositions.remove(randomPosition);
				startEndLightsEvent(randomPosition);
			}
			e.delay(12);
			npc.transform(7888);
			npc.getCombat().setAllowRetaliate(true);
			npc.unlock();
			lightsActive = false;
		});
	}

	private void dawnMoveEvent() {
		dawn.lock();
		Position eastSpawn = new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 30, 0);
		World.startEvent(e -> {
			dawn.stepAbs(eastSpawn.getX(), eastSpawn.getY(), StepType.WALK);
			e.delay(6);
			dawn.unlock();
			dawn.face(Direction.WEST);
			dawn.lock();
			for (int i = 0; i < 6; i++) {
				Position randomPosition = Random.get(possibleImpactPositions);
				possibleImpactPositions.remove(randomPosition);
				startEndDawnLightsEvent(randomPosition);
			}
			e.delay(5);
			for (int i = 0; i < 6; i++) {
				Position randomPosition = Random.get(possibleImpactPositions);
				possibleImpactPositions.remove(randomPosition);
				startEndDawnLightsEvent(randomPosition);
			}
			e.delay(12);
			dawn.transform(7884);
			dawn.getCombat().setAllowRetaliate(true);
			dawn.unlock();
		});
	}

	List<Position> possibleImpactPositions = new ArrayList<>();

	private void startPhaseTwo(Player player) {
		possibleImpactPositions.clear();
		dawn.setHidden(false);
		npc.transform(NpcID.DUSK_7887);
		dawn.transform(7853);
		dawn.getCombat().reset();
		npc.getCombat().reset();
		npc.getCombat().setAllowRetaliate(false);
		dawn.getCombat().setAllowRetaliate(false);
		player.getCombat().reset();
		phase = 2;
		bossBounds.forEachPos(possibleImpactPositions::add);
		npc.transform(NpcID.DUSK_7887);
		dawnMoveEvent();
		duskMoveEvent();
	}

	private void startEndDawnLightsEvent(Position pos) {
		World.startEvent(e -> {
			World.sendGraphics(1430, 0, 0, pos);
			e.delay(2);
			for (int i = 0; i < 5; i++) {
				World.sendGraphics(1432, 0, 0, pos);
				npc.localPlayers().forEach(p -> {
					if (p.getPosition().distance(pos) < 1) {
						p.hit(new Hit().fixedDamage(14));
					}
				});
			}
		});
	}

	int phase = 1;

	private void postDamage(Hit hit) {
		int hpPercent = npc.getHp() * 100 / npc.getMaxHp();
		if (phase == 1 && hpPercent < 55) {
			startPhaseTwo(hit.attacker.player);
		}
	}

	@Override
	public void follow() {
		follow(2);
	}

	@Override
	public boolean attack() {
		if (npc.getId() == 7887)
			return false;
		npc.face(target);
		if (Random.get(6) == 0) {
			npc.localNpcs().forEach(npc -> {
				if (npc.getId() == NpcID.DAWN) {
					if (npc.isHidden() || npc.isRemoved() && !knockbackSpecial) {
						rockFall(target.player);
					}
				}
			});
		}
		if (attackingWith == 0) {
			meleeAttack();
			if (Random.get(9) == 0)
				attackingWith = 1;
		} else if (attackingWith == 1) {
			rangedAttack(target.player);
			if (Random.get(9) == 0)
				attackingWith = 0;
		}
		return true;
	}

	private void meleeAttack() {
		npc.animate(phase == 1 ? 7785 : 7800);
		defendAnim();
		Hit hit = new Hit(npc, AttackStyle.CRUSH, null).randDamage(35);
		target.hit(hit);
	}

	@Override
	public int getAggressionRange() {
		if (npc.getId() == 7887)
			return 0;
		return 600;
	}

	@Override
	public int getAttackBoundsRange() {
		if (npc.getId() == 7887)
			return 0;
		return 600;
	}

	boolean knockbackSpecial = false;

	@Override
	public void process() {

		if (target == null && !npc.localPlayers().isEmpty())
			target = Random.get(npc.localPlayers());
		if (dawn == null) {
			npc.localNpcs().forEach(npc -> {
				if (npc.getId() == NpcID.DAWN || npc.getId() == NpcID.DAWN_7852 || npc.getId() == NpcID.DAWN_7853 || npc.getId() == NpcID.DAWN_7884 || npc.getId() == NpcID.DAWN_7885) {
					dawn = npc;
					dawn.deathEndListener = (entity, killer, hit) -> {
						flamePrisonSpecial(killer.player);
					};
				}
			});
		}

		if ((dawn.isHidden() || dawn.isRemoved()) || dawn == null) {
			AtomicBoolean dawnFound = new AtomicBoolean(false);
			npc.localNpcs().forEach(npc -> {
				if (npc.getId() == NpcID.DAWN || npc.getId() == NpcID.DAWN_7852 || npc.getId() == NpcID.DAWN_7853 || npc.getId() == NpcID.DAWN_7884 || npc.getId() == NpcID.DAWN_7885) {
					dawnFound.set(true);
				}
			});
			if (!dawnFound.get() && !rocksFalling) {
				rockFall(target.player);
			}

		}
		if ((dawn.isHidden() || dawn.isRemoved()) && !knockbackSpecial) {
			knockbackSpecial = true;
			World.startEvent(e -> {
				e.delay(8);
				if (target != null)
					orangeSpecial(target.player);
			});
		}

	}


	private void rangedAttack(Player player) {
		npc.animate(phase == 1 ? 7788 : 7801);
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1 || target == null);
			for (int i = 0; i < 2; i++) {
				int delay = RANGED_PROJECTILE.send(npc, target);
				Hit hit = new Hit(npc, AttackStyle.RANGED)
					.randDamage(9)
					.clientDelay(delay);
				player.hit(hit);
				e.delay(1);
			}
		});
	}

	private void orangeSpecial(Player player) {
		rockFall(player);
		World.startEvent(e -> {
			npc.animate(7397);
			npc.graphics(1423);
			e.delay(2);
			if (player.getPosition().distance(npc.getPosition()) < 3) {
				knockback(player, true);
			}
		});
	}

	private int getClosestX(Entity target) {
		if (target.getAbsX() < npc.getAbsX())
			return npc.getAbsX();
		else if (target.getAbsX() >= npc.getAbsX() && target.getAbsX() <= npc.getAbsX() + npc.getSize() - 1)
			return target.getAbsX();
		else
			return npc.getAbsX() + npc.getSize() - 1;
	}

	private void knockback(Player target, boolean damage) {
		if (target == null) return;

		// Desired Y position based on the region's base Y plus 20
		int targetY = target.getPosition().getRegion().baseY + 20;

		int vecX = (target.getAbsX() - getClosestX(target));
		if (vecX != 0)
			vecX /= Math.abs(vecX); // determines X component for knockback

		int endX = target.getAbsX();
		int endY = target.getAbsY();
		int newY = target.getAbsY();

		for (int i = 0; i < 4; i++) {
			// Check if the target Y has been reached or exceeded
			if (newY >= targetY) {
				endY = targetY; // Set the Y to the target Y position
				break;
			}

			if (DumbRoute.getDirection(endX, newY, npc.getHeight(), target.getSize(), endX, newY + 1) != null) {
				newY += 1; // Move towards the target Y position
			} else {
				break; // Cannot move further
			}
		}

		// Adjust the end position to make sure it's within the bossBounds
		Position newPosition = new Position(endX, endY, npc.getHeight());
		newPosition = Bounds.clamp(newPosition, bossBounds); // Clamps the newPosition to stay within the bossBounds
		endX = newPosition.getX();
		endY = newPosition.getY();

		// If end position changed, perform the teleport and damage actions
		if (endY != target.getAbsY()) {
			if (target.player != null) {
				int finalEndX = endX;
				int finalEndY = endY;
				World.startEvent(e -> {
					e.delay(1);
					final Player p = target.player;
					p.lock();
					p.animate(1157);
					p.graphics(245, 5, 124);
					p.stun(2, true);
					p.getMovement().teleport(finalEndX, finalEndY, npc.getHeight());
					p.sendMessage("Dusk, knocks you back!");
					if (damage) {
						target.hit(new Hit().fixedDamage(22));
					}
					p.unlock();
				});
			}
		} else {
			if (damage) {
				target.hit(new Hit().fixedDamage(22));
			}
			target.animate(1157);
			target.graphics(245, 5, 124);
			target.stun(2, true);
			target.getCombat().reset();
			if (target.player != null)
				target.player.sendMessage("Dusk, knocks you back!");
		}
	}

	private void flamePrisonSpecial(Player player) {
		World.startEvent(e -> {
			knockback(player, false);
			player.lock();
			e.delay(3);
			player.unlock();
			Position centrePosition = player.getPosition().copy();
			List<Position> outterPositions = new ArrayList<>();
			List<Position> innerPositions = new ArrayList<>();
			for (int x = -2; x < 3; x++) {
				for (int y = -2; y < 3; y++) {
					if (Math.abs(x) < 2 && Math.abs(y) < 2) {
						innerPositions.add(new Position(centrePosition.getX() + x, centrePosition.getY() + y, centrePosition.getZ()));
						continue; // Skips (-1, -1) to (1, 1), including (0, 0)
					}
					outterPositions.add(new Position(centrePosition.getX() + x, centrePosition.getY() + y, centrePosition.getZ()));
				}
			}
			for (Position position : innerPositions) {
				World.sendGraphics(157, 0, 0, position);//Could be an object it spawns idk yet
			}
			e.delay(3);
			for (Position position : innerPositions) {
				World.sendGraphics(157, 0, 0, position);
				if (player.getPosition().distance(position) < 1) {
					player.hit(new Hit().randDamage(50, 77));
				}
			}
		});
	}

	boolean rocksFalling = false;
	boolean lightsActive = false;
	public boolean damagedByRock = false;

	private void rockFall(Player player) {
		if (rocksFalling)
			return;

		rocksFalling = true;
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1 || lightsActive);
			List<Position> impactPositions = new ArrayList<>();
			impactPositions.add(player.getPosition().copy());
			for (int i = 0; i < 18; i++) {
				Position randomPosition = bossBounds.randomPosition();
				impactPositions.add(randomPosition);
			}
			for (Position impactPosition : impactPositions) {
				World.sendGraphics(1727, 0, 0, impactPosition);
				e.delay(7);
				if (player.getPosition().distance(impactPosition) < 2) {
					damagedByRock = true;
					player.hit(new Hit().fixedDamage(22));
				}
				rocksFalling = false;
			}
		});
	}
}