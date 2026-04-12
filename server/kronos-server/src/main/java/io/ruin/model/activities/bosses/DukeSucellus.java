package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.activities.tempevents.hweenevent.HweenEvent;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.model.skills.slayer.SlayerCreature;
import io.ruin.model.skills.slayer.master.BossMaster;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.TickDelay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DukeSucellus extends NPCCombat {
	List<Position> meleeAttackPositions = new ArrayList<>();
	List<Position> pillarSafeSpots = new ArrayList<>();

	//2439 the melee gfx

	TickDelay poisonVents = new TickDelay();

	boolean sentVents = false;

	boolean playerRan = false;
	boolean damagedPlayer = false;

	boolean ventPoisonedPlayer = false;

	Projectile magicProjectile = new Projectile(2434, 65, 0, 20, 72, 0, 18, 10);

	@Override
	public void init() {
		meleeAttackPositions.add(new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 51, 0));
		meleeAttackPositions.add(new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 51, 0));
		meleeAttackPositions.add(new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 51, 0));
		meleeAttackPositions.add(new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 51, 0));
		meleeAttackPositions.add(new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 51, 0));
		meleeAttackPositions.add(new Position(npc.getPosition().getRegion().baseX + 33, npc.getPosition().getRegion().baseY + 51, 0));
		meleeAttackPositions.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 51, 0));
		pillarSafeSpots.add(new Position(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 51, 0));
		pillarSafeSpots.add(new Position(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 47, 0));
		pillarSafeSpots.add(new Position(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 43, 0));
		pillarSafeSpots.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 51, 0));
		pillarSafeSpots.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 47, 0));
		pillarSafeSpots.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 43, 0));
		refreshVentPositions();
	}

	@Override
	public void follow() {

	}

	@Override
	public void startDeath(Hit killHit) {
		if (npc.getId() == 12192) return;
		if (!npc.getPosition().getRegion().players.isEmpty())
			npc.getPosition().getRegion().players.stream().findFirst().get().getCombat().reset();
		npc.transform(12192);
		BossMaster.handleBossSlayerKill(getKiller().player, npc);
		npc.clearHits();
		npc.getCombat().setTarget(null);
		npc.getCombat().handleNewDrop(getKiller().player, npc.getId(), getKiller().player.getPosition());
		if (npc.getDef().killCounter != null)
			npc.getDef().killCounter.apply(getKiller().player).increment(getKiller().player);
		Player player = getKiller().player;
		if (player != null) {
			float petDropAverage = 950;
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
				ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				petDropAverage *= (float) c.getPetChanceBoost();
			}
			if (player.petDropBonus.isDelayed())
				petDropAverage *= 0.8f;
			if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
				petDropAverage *= 0.85F;

			petDropAverage *= getPetDonatorBoost(player);

			if (player.dukeSucellusTimer != null)
				player.dukeSucellusBestTime = player.dukeSucellusTimer.stop(player, player.dukeSucellusBestTime);

			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.DUKE_SUCELLUS_ADEPT.ordinal())).
				getCombatAchievement()).check(player);
			Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.DUKE_SUCELLUS_MASTER.ordinal())).
				getCombatAchievement()).check(player);
			if (ActivityTimer.timeInSeconds(player.dukeSucellusBestTime) < 45) {
				SlayerCreature task = SlayerCreature.lookup(VarPlayerRepository.SLAYER_TASK.get(player));
				boolean slayerTask = player.bossSlayerName != null && player.bossSlayerName.equalsIgnoreCase("Duke Sucellus");
				if (!slayerTask) {
					Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.DUKE_SUCELLUS_SPEED_TRIALIST.ordinal())).
						getCombatAchievement()).check(player);
				}
			}
			if (ActivityTimer.timeInSeconds(player.dukeSucellusBestTime) < 40) {
				boolean slayerTask = player.bossSlayerName != null && player.bossSlayerName.equalsIgnoreCase("Duke Sucellus");
				if (!slayerTask) {
					Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.DUKE_SUCELLUS_SPEED_CHASER.ordinal())).
						getCombatAchievement()).check(player);
				}
			}
			if (ActivityTimer.timeInSeconds(player.dukeSucellusBestTime) < 30) {
				boolean slayerTask = player.bossSlayerName != null && player.bossSlayerName.equalsIgnoreCase("Duke Sucellus");
				if (!slayerTask) {
					Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.DUKE_SUCELLUS_SPEED_RUNNER.ordinal())).
						getCombatAchievement()).check(player);
				}
			}
			if (Random.get(0, (int) petDropAverage) == 0) {
				Pet.DUKE.unlock(player, 12191);
			}
			if (!playerRan && !damagedPlayer) {
				Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.COLD_FEET.ordinal())).
					getCombatAchievement()).check(player);
			}
			if (!damagedPlayer) {
				player.perfectDukeKills++;
				if (player.perfectDukeKills >= 5) {
					Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_DUKE_SUCELLUS.ordinal())).
						getCombatAchievement()).check(player);
				}
			} else {
				player.perfectDukeKills = 0;
			}
		}
		World.startEvent(e -> {
			e.delay(10);
			npc.transform(12191);
			npc.getCombat().restore();
			timerStarted = false;
			playerRan = false;
			damagedPlayer = false;
			sentVents = false;
			ventPoisonedPlayer = false;
			poisonVents.reset();
			poisonImpactPositions.clear();
			poisonBounds.clear();
		});
	}

	boolean timerStarted = false;

	@Override
	public boolean attack() {
		if (!timerStarted) {
			timerStarted = true;
			target.player.dukeSucellusTimer = new ActivityTimer();
		}
		if (npc.getId() != 12191) return false;
		if (isVenting) {
			return true;
		}
		boolean canSendVent = !sentVents && npc.getHp() < 1700;
		if (sentVents || canSendVent) {
			if (poisonVents.remaining() < 1) {
				sendPoisonedVents();
				return true;
			}
		}
		if (inMeleeRange(target)) {
			sendMeleeAttack(target);
		} else {
			sendMagicAttack(target);
		}
		return true;
	}

	private void sendMeleeAttack(Entity target) {
		npc.graphics(2439);
		npc.animate(10176);
		for (Position position : meleeAttackPositions) {
			World.sendGraphics(2440, 0, 0, position);
		}
		World.startEvent(event -> {
			event.delay(2);
			int minimumDamage = 43;
			int maximumDamage = 88;
			if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE)) {
				minimumDamage /= 2;
				maximumDamage /= 2;
			}
			if (inMeleeRange(target)) {
				if (npc.getHp() > 0) {
					target.hit(new Hit().randDamage(minimumDamage, maximumDamage));
					damagedPlayer = true;
				}
			}
		});
	}

	private boolean isBehindPillar(Entity target) {
		boolean behindPillar = false;
		for (Position position : pillarSafeSpots) {
			if (target.getPosition().distance(position) < 1) {
				behindPillar = true;
				break;
			}
		}
		return behindPillar;
	}

	private boolean inMeleeRange(Entity target) {
		boolean inRange = false;
		for (Position position : meleeAttackPositions) {
			if (target.getPosition().distance(position) < 1) {
				inRange = true;
				break;
			}
		}
		return inRange;
	}

	private void sendMagicAttack(Entity target) {
		npc.animate(10177);
		World.startEvent(event -> {
			event.delay(1);
			int delay = magicProjectile.send(npc, target);
			event.delay(World.getTicks(delay) + 1);
			int minimumDamage = 92;
			int maximumDamage = 180;
			if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
				minimumDamage /= 2;
				maximumDamage /= 2;
			}
			if (npc.getHp() > 0) {
				target.hit(new Hit().randDamage(minimumDamage, maximumDamage));
				damagedPlayer = true;
			}
			target.graphics(2435, 22, 0);
		});
	}

	private void gazeAttack() {
		if (npc.getPosition().getRegion().players.isEmpty())
			return;
		Player player = npc.getPosition().getRegion().players.getFirst();
		player.sendMessage(Color.PURPLE.wrap("Duke Sucellus turns his gaze upon you..."));
		npc.animate(10180);
		World.startEvent(e -> {
			e.delay(3);
			for (int i = 0; i < 2; i++) {
				e.delay(1);
				if (!isBehindPillar(player)) {
					if (npc.getHp() > 0)
						player.hit(new Hit().randDamage(55, 90));
					break;
				}
			}
		});
	}

	Projectile poisonVentProjectile = new Projectile(2436, 65, 0, 20, 72, 14, 18, 10);
	List<Bounds> poisonBounds = new ArrayList<>();
	List<Position> poisonImpactPositions = new ArrayList<>();
	int lastVentHit = -1;
	boolean isVenting = false;

	private void sendPoisonedVents() {
		isVenting = true;
		poisonVents.delay(20);
		poisonBounds.clear();
		poisonImpactPositions.clear();

		World.startEvent(event -> {
			int ventsHit = npc.getHp() > 1000 ? 1 : 2;
			int firstVent = Random.get(1) == 0 ? 0 : 2;
			if (firstVent == lastVentHit) {
				firstVent = firstVent == 0 ? 2 : 0;
			}
			lastVentHit = firstVent;
			for (int i = 0; i < ventsHit; i++) {
				npc.animate(10179);
				Position targetPosition = ventPositions.get(firstVent == 2 ? firstVent - i : firstVent + i);
				int delay = poisonVentProjectile.send(npc, targetPosition);
				event.delay(World.getTicks(delay) + 1);
				Bounds poisonBoundary = new Bounds(targetPosition.getX() - 1, targetPosition.getY() - 1, targetPosition.getX() + 1, targetPosition.getY() + 1, targetPosition.getZ());
				poisonBounds.add(poisonBoundary);
				poisonImpactPositions.add(targetPosition);
				if (i == ventsHit - 1) {
					isVenting = false;
				}
			}
		});

	}

	List<Position> ventPositions = new ArrayList<>();

	private void refreshVentPositions() {
		ventPositions.clear();
		ventPositions.add(new Position(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 50, 0));
		ventPositions.add(new Position(npc.getPosition().getRegion().baseX + 31, npc.getPosition().getRegion().baseY + 50, 0));
		ventPositions.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 50, 0));
	}

	@Override
	public void process() {
		if (npc.getId() != 12191) return;
		if (npc.getPosition().getRegion().players.isEmpty()) {
			return;
		}
		Player player = npc.getPosition().getRegion().players.getFirst();
		if (VarPlayerRepository.RUNNING.get(player) == 1)
			playerRan = true;
		for (int i = 0; i < poisonBounds.size(); i++) {
			Bounds bounds = poisonBounds.get(i);
			Player target = npc.getPosition().getRegion().players.getFirst();
			if (bounds == null)
				continue;
			if (target.getPosition().inBounds(bounds) && !ventPoisonedPlayer) {
				ventPoisonedPlayer = true;
				if (npc.getHp() > 0) {
					target.hit(new Hit(HitType.POISON).fixedDamage(12));
					damagedPlayer = true;
				}
				World.startEvent(event -> {
					event.delay(2);
					ventPoisonedPlayer = false;
				});
			}
		}

		for (int i = 0; i < poisonImpactPositions.size(); i++) {
			Position position = poisonImpactPositions.get(i);
			if (position == null)
				continue;
			World.sendGraphics(2433, 0, 0, position);
		}

	}
}
