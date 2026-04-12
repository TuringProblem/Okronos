package io.ruin.model.activities.gauntlet;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.activities.perktree.PerkTasks;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPC.DefaultHeadIconIndex;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.AttackNpcListener;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.polly.RSPolygon;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.TickDelay;
import net.dv8tion.jda.api.entities.SelfUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Hunllef extends NPCCombat {
	boolean boundsSet = false;

	private static final Position CHEST_LOCATION = new Position(3031, 6121, 1);

	NPC CRYSTALLINE_TORNADO = new NPC(9025);
	NPC CORRUPTED_TORNADO = new NPC(9039);

	List<NPC> crystallineNPCs = new ArrayList<>();
	List<NPC> corruptedNPCs = new ArrayList<>();

	boolean corrupted = false;
	private static final Projectile MAGIC_PROJECTILE_CORRUPTED = new Projectile(1708, 90, 43, 35, 40, 6, 16, 192);
	private static final Projectile MAGIC_PROJECTILE_CRYSTALLINE = new Projectile(1713, 90, 43, 35, 40, 6, 16, 192);
	private static final Projectile CORRUPTED_RANGED_PROJECTILE = new Projectile(1712, 90, 43, 35, 40, 6, 16, 192);
	private static final Projectile CRYSTALLINE_RANGED_PROJECTILE = new Projectile(1711, 90, 43, 35, 40, 6, 16, 192);
	private static final Projectile CORRUPTED_SMITE_PROJECTILE = new Projectile(1714, 90, 43, 35, 40, 6, 16, 192);
	private static final Projectile CRYSTALLINE_SMITE_PROJECTILE = new Projectile(1713, 90, 43, 35, 40, 6, 16, 192);

	int armourTierRating = 0;
	int offPrayerAttackDealt = 0;
	int attackCounter = 0;

	public boolean damagedPlayer = false;
	private AttackStyle lastBasicAttackStyle = AttackStyle.RANGED;
	private boolean canBeAttacked = true;
	List<NPC> tornados = new ArrayList<>();

	Bounds bossRoomBounds;

	TickDelay smiteAttackDelay = new TickDelay();
	TickDelay tornadoAttackDelay = new TickDelay();

	Player player;

	DynamicMap map;
	public boolean hitOffPrayer = false;

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend);
		npc.deathEndListener = (entity, killer, killHit) -> startDeath();
		AttackNpcListener listener = (p, npc, message) -> canBeAttacked;
		npc.attackNpcListener = listener;
		if (npc.getId() > 9020 && npc.getId() < 9025)
			corrupted = false;
		else if (npc.getId() > 9034 && npc.getId() < 9039)
			corrupted = true;
	}

	@Override
	public void follow() {
		if (target == null || target.player.gauntlet == null) {
			return;
		}

		if (!target.player.gauntlet.bossFightStarted) {
			return;
		}

		follow(20);
	}

	private void postDamage(Hit hit) {
		if (hit.attackStyle == null) {
			System.err.println("Warning: hit.attackStyle is null");
			return;
		}

		if (hit.damage > 0) {
			offPrayerAttackDealt++;
		}

		if (offPrayerAttackDealt >= 6) {
			npc.animate(8754);
			transform(hit);
			offPrayerAttackDealt = 0;
		}

		if (player != null && player.gauntlet != null) {
			player.gauntlet.bossDamage += hit.damage;
		}
	}

	private void transform(Hit hit) {
		// Instead of creating new NPCs, just get the correct ID
		int newNpcId;
		if (corrupted) {
			switch (hit.attackStyle) {
				case MAGIC:
					newNpcId = 9037;
					npc.setHeadIcon(DefaultHeadIconIndex.ProtectFromMagic);
					break;
				case RANGED:
					newNpcId = 9036;
					npc.setHeadIcon(DefaultHeadIconIndex.ProtectFromRanged);
					break;
				default:
					newNpcId = 9035;
					npc.setHeadIcon(DefaultHeadIconIndex.ProtectFromMelee);
					break;
			}
		} else {
			switch (hit.attackStyle) {
				case MAGIC:
					newNpcId = 9023;
					npc.setHeadIcon(DefaultHeadIconIndex.ProtectFromMagic);
					break;
				case RANGED:
					newNpcId = 9022;
					npc.setHeadIcon(DefaultHeadIconIndex.ProtectFromRanged);
					break;
				default:
					newNpcId = 9021;
					npc.setHeadIcon(DefaultHeadIconIndex.ProtectFromMelee);
					break;
			}
		}

		// Direct transform without creating temporary NPC
		npc.transform(newNpcId);
	}

	private void Tornados(Position spawnPosition) {
		if (npc == null)
			return;

		npc.animate(8420);
		NPC tornado = new NPC(corrupted ? 9039 : 9025).spawn(spawnPosition);
		AtomicInteger ticks = new AtomicInteger();

		tornado.startEvent(event -> {
			while (ticks.getAndIncrement() < 30) {
				// Get target and validate it's not null
				var target = npc.getCombat().getTarget();
				if (target == null || !target.player.gauntlet.inGauntlet) {
					tornado.remove();
					return;
				}

				Position targetPos = target.getPosition();
				if (targetPos == null) {
					tornado.remove();
					return;
				}

				// Move tornado towards target
				tornado.stepAbs(targetPos.getX(), targetPos.getY(), StepType.WALK);

				// Check if tornado hits target
				if (tornado.getPosition().isWithinDistance(targetPos, 0)) {
					int randomDamage = Random.get(18, 35);
					armourTierRating = wearingProtectionArmour();

					switch (armourTierRating) {
						case 1:
							randomDamage *= 0.8;
							break;
						case 2:
							randomDamage *= 0.67;
							break;
						case 3:
							randomDamage *= 0.5;
							break;
					}

					if (target.player.gauntlet.inGauntlet) {
						damagedPlayer = true;
						target.player.hit(new Hit(npc).randDamage(randomDamage));
					}
				}

				event.delay(1);

				// Check if tornado should be removed
				if (ticks.get() >= 30 || npc.getHp() < 1) {
					tornado.remove();
					return;
				}
			}
		});
	}

	private void preHitDefend(Hit hit) {
		if (hit.attackStyle != null) {
			if (hit.damage > 100)
				hit.damage = 100;
			switch (npc.getId()) {
				case 9023:
				case 9037:
					if (hit.attackStyle.isMagic()) {
						if (hit.attacker.player != null) {
							hit.block();
							damagedPlayer = true;
						}
					}
					break;
				case 9021:
				case 9035:
					if (hit.attackStyle.isMelee()) {
						hit.block();
						damagedPlayer = true;
					}
					break;
				case 9022:
				case 9036:
					if (hit.attackStyle.isRanged()) {
						hit.block();
						damagedPlayer = true;
					}
					break;
			}
		}
	}

	public void startDeath() {
		tornados.clear();
		attackCounter = 0;
		if (map != null) {
			map.destroy();
		}
		player.gauntlet.inGauntlet = false;
		player.getMovement().teleport(CHEST_LOCATION);
		VarPlayerRepository.GAUNTLET_REWARD.set(player, 1);
		player.gauntlet.removeFakeContainer(player);

		Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.GAUNTLET_ADEPT.ordinal()))
				.getCombatAchievement()).check(player);
		Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.GAUNTLET_MASTER.ordinal()))
				.getCombatAchievement()).check(player);

		Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.GAUNTLET_NOVICE.ordinal()))
				.getCombatAchievement()).check(player);
		PerkTaskHandler.handleMonsterKill(player, "gauntlet");
		DailyTasks.handleTaskDecrement(player, "gauntlet");
		if (corrupted) {
			if (wearingProtectionArmour() == 3) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.CORRUPTED_WARRIOR.ordinal()))
						.getCombatAchievement()).check(player);
			}
			if (player.currentPerkTask != null && player.currentPerkTask == PerkTasks.COMPLETE_CORRUPTED_GAUNTLET)
				player.getPlayerPerkHandler().handleTaskAmountDecrement(player, 1);

			DailyTasks.handleTaskDecrement(player, "corruptedGauntlet");
			player.theCorruptedGauntletKills.increment(player);
			player.sendMessage("Your Corrupted Gauntlet kill count is: "
					+ Color.RED.wrap(NumberUtils.formatNumber(player.theCorruptedGauntletKills.getKills())));
		} else {
			player.theGauntletKills.increment(player);
			player.sendMessage("Your Gauntlet kill count is: "
					+ Color.RED.wrap(NumberUtils.formatNumber(player.theGauntletKills.getKills())));
			if (player.currentPerkTask != null && player.currentPerkTask == PerkTasks.COMPLETE_GAUNTLET)
				player.getPlayerPerkHandler().handleTaskAmountDecrement(player, 1);
		}
		player.teleportListener = null;
		player.deathStartListener = null;
		player.deathEndListener = null;
		player.closeInterface(ToplevelComponent.OVERLAY);
		if (!corrupted && !player.madeEgniolPotion) {
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.EGNIOL_DIET.ordinal()))
					.getCombatAchievement()).check(player);
		}
		if (corrupted && !player.madeEgniolPotion) {
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.EGNIOL_DIETII.ordinal()))
					.getCombatAchievement()).check(player);
		}
		if (corrupted) {
			if (player.attunedWeaponsMade < 2) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.WOLF_PUNCHERII.ordinal()))
						.getCombatAchievement()).check(player);
			}
			if (!player.gauntletArmourMade) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.DEFENCE_DOESNT_MATTER_II.ordinal()))
						.getCombatAchievement()).check(player);
			}
			if (!damagedPlayer) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_CORRUPTED_HUNLLEF.ordinal()))
						.getCombatAchievement()).check(player);
			}
			player.corruptedGauntletBossChestToBeLooted = true;
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.CORRUPTED_GAUNTLET_NOVICE.ordinal()))
					.getCombatAchievement()).check(player);
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.CORRUPTED_GAUNTLET_GRANDMASTER.ordinal()))
					.getCombatAchievement()).check(player);
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.CORRUPTED_GAUNTLET_MASTER.ordinal()))
					.getCombatAchievement()).check(player);
			if (player != null && player.corruptedHunllefTimer != null)
				player.corruptedHunllefBestTime = player.corruptedHunllefTimer.stop(player, player.corruptedHunllefBestTime);

			if (ActivityTimer.timeInSeconds(player.corruptedHunllefBestTime) < 150) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.CORRUPTED_HUNLLEF_SPEED_CHASER.ordinal()))
						.getCombatAchievement()).check(player);
			}
			if (ActivityTimer.timeInSeconds(player.corruptedHunllefBestTime) < 120) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.CORRUPTED_HUNLLEF_SPEED_RUNNER.ordinal()))
						.getCombatAchievement()).check(player);
			}
		} else {
			if (player.attunedWeaponsMade < 2) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.WOLF_PUNCHER.ordinal()))
						.getCombatAchievement()).check(player);
			}
			if (!damagedPlayer) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_CRYSTALLINE_HUNLLEF.ordinal()))
						.getCombatAchievement()).check(player);
			}
			if (!player.gauntletArmourMade) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.DEFENCE_DOESNT_MATTER_I.ordinal()))
						.getCombatAchievement()).check(player);
			}

			if (wearingProtectionArmour() == 3) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.CRYSTALLINE_WARRIOR.ordinal()))
						.getCombatAchievement()).check(player);
			}
			player.crystallineGauntletBossChestToBeLooted = true;
			if (player != null && player.crystallineHunllefTimer != null)
				player.crystallineHunllefBestTime = player.crystallineHunllefTimer.stop(player,
						player.crystallineHunllefBestTime);

			if (ActivityTimer.timeInSeconds(player.crystallineHunllefBestTime) < 90) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player
								.getCombatAchievementIndexByOrdinal(CombatAchievements.CRYSTALLINE_HUNLLEF_SPEED_CHASER.ordinal()))
						.getCombatAchievement()).check(player);
			}
			if (ActivityTimer.timeInSeconds(player.crystallineHunllefBestTime) < 60) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player
								.getCombatAchievementIndexByOrdinal(CombatAchievements.CRYSTALLINE_HUNLLEF_SPEED_RUNNER.ordinal()))
						.getCombatAchievement()).check(player);
			}
		}

		if (!hitOffPrayer && !corrupted) {
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.THREE_TWO_ONE_RANGE.ordinal()))
					.getCombatAchievement()).check(player);
		}

		if (!hitOffPrayer && corrupted) {
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.THREE_TWO_ONE_MAGE.ordinal()))
					.getCombatAchievement()).check(player);
		}
	}

	public RSPolygon[] polygons() {
		return new RSPolygon[] {
			new RSPolygon(new int[][] {
				{ map.swRegion.baseX + 34, map.swRegion.baseY + 50, },
				{ map.swRegion.baseX + 45, map.swRegion.baseY + 50, },
				{ map.swRegion.baseX + 45, map.swRegion.baseY + 61, },
				{ map.swRegion.baseX + 34, map.swRegion.baseY + 61, },
			})
		};
	}


	@Override
	public boolean attack() {
		if (target == null)
			return false;
		if (target.isPlayer() && target.player.gauntlet == null)
			return false;
		if (!target.player.gauntlet.inGauntlet)
			return false;
		npc.face(target);
		if (player == null)
			player = target.player;

		if (npc.getPosition().distance(target.getPosition()) < 2)
			stomp();
		else if (Random.get(0, 10) == 0 && tornadoAttackDelay.finished()) {
			int tornadoAmount = 1;
			if ((npc.getHp() / npc.getMaxHp()) * 100 < 65)
				tornadoAmount = 2;
			else if ((npc.getHp() / npc.getMaxHp()) * 100 < 40)
				tornadoAmount = 3;
			else if (corrupted && (npc.getHp() / npc.getMaxHp()) * 100 < 20)
				tornadoAmount = 4;
			for (int i = 0; i < tornadoAmount; i++) {
				Arrays.stream(polygons()).findFirst().ifPresent( poly -> {
					var position = poly.getRandomPosition(1);
					var ticker = 0;
					var cap = 5;
					if (position.distance(target.getPosition()) < 2) {
						ticker++;
						while (position.isWithinDistance(target.getPosition(), 2) && ticker < cap) {
							position = poly.getRandomPosition(1);
							ticker++;
						}
					}
					Tornados(position);
				});
			}
			tornadoAttackDelay.delay(35);
		}
		else {
			if (lastBasicAttackStyle == AttackStyle.MAGIC && Random.get(0, 3) == 0 && smiteAttackDelay.finished())
				prayerDisableAttack();
			else
				standardAttack();
		}
		return true;
	}

	@Override
	public void process() {
		if (target != null) {
			if (target.player.gauntlet != null) {
				map = target.player.gauntlet.map;
				corrupted = target.player.gauntlet.corrupted;
			}

			if (!boundsSet) {
				bossRoomBounds = new Bounds(map.swRegion.baseX + 34, map.swRegion.baseY + 50, map.swRegion.baseX + 45,
						map.swRegion.baseY + 61, 1);
				boundsSet = true;
			}
		} else {
			npc.localPlayers().forEach(p -> {
				if (p.gauntlet != null && p.gauntlet.bossFightStarted)
					target = p;
			});
		}
	}

	public void prayerDisableAttack() {
		if (target == null) {
			return;
		}
		World.startEvent(e -> {
			if (target == null) {
				return;
			}
			smiteAttackDelay.delay(50);
			npc.animate(8419);
			e.delay(1);

			if (target == null) {
				return;
			}

			int delay = corrupted ? CORRUPTED_SMITE_PROJECTILE.send(npc, target)
					: CRYSTALLINE_SMITE_PROJECTILE.send(npc, target);
			int maxDamage = 30;
			if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)
					|| target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)
					|| target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE))
				target.player.hit(new Hit(npc, lastBasicAttackStyle).randDamage(maxDamage).ignorePrayer().clientDelay(delay));

			if (target.player != null) {
				target.graphics(corrupted ? 1716 : 1715, 0, delay);
				target.player.getPrayer().deactivateAll();
				target.player.sendMessage(Color.RED.wrap("Your prayers have been disabled!"));
			}
		});
	}

	public void stomp() {
		if (target == null)
			return;
		npc.animate(8420);
		target.hit(new Hit(npc, null, null).fixedDamage(Random.get(30, 70)).ignoreDefence().ignorePrayer());
		damagedPlayer = true;
	}

	public void standardAttack() {
		Projectile magicProject = corrupted ? MAGIC_PROJECTILE_CORRUPTED : MAGIC_PROJECTILE_CRYSTALLINE;

		World.startEvent(e -> {
			npc.animate(8419);
			e.delay(1);
			if (target == null)
				return;
			int delay = (lastBasicAttackStyle == AttackStyle.RANGED
					? corrupted ? CORRUPTED_RANGED_PROJECTILE : CRYSTALLINE_RANGED_PROJECTILE
					: magicProject).send(npc, target);

			int maxDamage = info.max_damage;
			if (target.player.getPrayer().isActive(
					lastBasicAttackStyle == AttackStyle.RANGED ? Prayer.PROTECT_FROM_MISSILES : Prayer.PROTECT_FROM_MAGIC)) {
				maxDamage /= 3;
				armourTierRating = wearingProtectionArmour();
				switch (armourTierRating) {
					case 1:
						maxDamage *= 0.8;
						break;
					case 2:
						maxDamage *= 0.67;
						break;
					case 3:
						maxDamage *= 0.5;
						break;
				}
			} else {
				hitOffPrayer = true;
				damagedPlayer = true;
			}
			target.player.hit(new Hit(npc, lastBasicAttackStyle).randDamage(maxDamage).clientDelay(delay));
			attackCounter++;
			if (lastBasicAttackStyle == AttackStyle.MAGIC) {
				target.graphics(corrupted ? 1704 : 1703, 0, delay);
			}
			if (attackCounter == 4) {
				lastBasicAttackStyle = lastBasicAttackStyle == AttackStyle.RANGED ? AttackStyle.MAGIC : AttackStyle.RANGED;
				attackCounter = 0;
			}
		});
	}

	public int wearingProtectionArmour() {
		if (player != null) {
			if (player.getEquipment().get(0) != null
					&& player.getEquipment().get(0).getId() == (corrupted ? 23840 : 23886)
					&& player.getEquipment().get(4) != null
					&& player.getEquipment().get(4).getId() == (corrupted ? 23843 : 23889)
					&& player.getEquipment().get(7) != null
					&& player.getEquipment().get(7).getId() == (corrupted ? 23846 : 23892))
				return 1;
			else if (player.getEquipment().get(0) != null
					&& player.getEquipment().get(0).getId() == (corrupted ? 23841 : 23887)
					&& player.getEquipment().get(4) != null
					&& player.getEquipment().get(4).getId() == (corrupted ? 23844 : 23890)
					&& player.getEquipment().get(7) != null
					&& player.getEquipment().get(7).getId() == (corrupted ? 23847 : 23893))
				return 2;
			else if (player.getEquipment().get(0) != null
					&& player.getEquipment().get(0).getId() == (corrupted ? 23842 : 23888)
					&& player.getEquipment().get(4) != null
					&& player.getEquipment().get(4).getId() == (corrupted ? 23845 : 23891)
					&& player.getEquipment().get(7) != null
					&& player.getEquipment().get(7).getId() == (corrupted ? 23848 : 23894))
				return 3;
			else
				return 0;
		} else
			return 0;
	}
}
