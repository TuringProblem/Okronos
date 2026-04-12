package npc.nex.scripts;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.NPCType;
import npc.nex.attacks.impl.spec.BloodSiphonAttack;
import npc.nex.attacks.impl.spec.ContainmentAttack;
import npc.nex.attacks.impl.spec.SoulSplitAttack;
import npc.nex.attacks.impl.std.*;
import npc.nex.old.NexUtils;
import npc.nex.attacks.Attack;
import npc.nex.attacks.SpecialAttack;
import npc.nex.attacks.impl.std.*;
import npc.nex.modes.Forms;
import npc.nex.modes.Phase;
import npc.nex.utils.NexCombatAchievements;
import npc.nex.utils.NexDropI;
import npc.nex.utils.ZarosUtils;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Region;
import io.ruin.model.map.object.GameObject;
import io.ruin.utility.Misc;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import static npc.nex.modes.Phase.SPAWN;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
@Slf4j
public class NexCombat extends NPCCombat {

	private final List<NPC> minions = new ArrayList<>(); // list of active minions

	@Getter
	private final List<NPC> bloodReavers = new ArrayList<>(); // list of active blood reavers
	@Getter
	private final List<GameObject> icicles = new ArrayList<>(); // list of active icicles
	@Getter
	private final List<GameObject> stalagmites = new ArrayList<>(); // list of active stalagmites
	@Getter
	private final Map<Player, Integer> damagingPlayers = new HashMap<>();

	public final Bounds ATTACK_BOUNDS;

	@Getter
	private Phase phase = SPAWN;
	// soul split dmg
	private int soulSplitDamage;
	// blood siphon delay
	public int damageHealsDelay;
	private int regularAttackCount;

	private NPC UMBRA_SPAWN;
	private NPC CRUOR_SPAWN;
	private NPC GLACIES_SPAWN;
	private NPC FUMUS_SPAWN;

	boolean healedFromSiphon = false;
	boolean hasHealedInZarosForm = false;
	public boolean damagedByShadow = false;
	public boolean damagedByIce = false;
	boolean dead = false;
	boolean phaseLocked = true;

	public boolean overOnePlayers = false;
	public boolean overTwoPlayers = false;
	public boolean overThreePlayers = false;

	// force melee is prioritized last, after all special attacks
	public boolean forceMelee = false;

	public int playersKilled = 0;

	public static void register() {
		NPCType.registerCombat(NexCombat.class, 11278, 11280, 11282, 11281, 11279);
	}


	public NexCombat() {
		var region = Region.get(11601);
		ATTACK_BOUNDS = new Bounds(
				region.baseX + 29,
				region.baseY + 4,
				region.baseX + 62,
				region.baseY + 34,
				0);
	}

	@Override
	public void init() {
		npc.hitListener = new HitListener()
				.preDefend(this::preDefend)
				.postTargetDefend(this::postTargetDefend)
				.postDamage(this::postDamage);

		npc.deathStartListener = (entity, _, _) -> {
			if (npc.getId() != Forms.WRATH_NEX.getNpcId())
				npc.transform(Forms.WRATH_NEX.getNpcId());

			new WrathAttack().invoke(entity.player, this);
			resetState();
			AtomicBoolean playerCoughing = new AtomicBoolean(false);
			entity.getPosition().getRegion().players.forEach(p -> {
				if (p.get(NexUtils.INFECTION) != null)
					playerCoughing.set(true);
			});
			for (var entry : damagingPlayers.entrySet()) {
				var plr = entry.getKey();
				var damage = entry.getValue();
				if (plr == null)
					continue;
				int rolls;
				if (damage < 300) {
					plr.sendMessage(Color.DARK_GREEN.wrap("You did not do enough damage to receive a drop."));
					continue;
				}
				if (damage < 750)
					rolls = 1;
				else if (damage < 1100)
					rolls = 2;
				else if (damage < 3000)
					rolls = 3;
				else
					rolls = 4;
				// handle drops
				NexDropI.handleNexDrop(plr, npc, rolls);
				// handle achievements for players who did damage
				NexCombatAchievements.onNexDeath(plr, this, playersKilled, healedFromSiphon, damagedByShadow, damagedByIce,
						playerCoughing.get());
			}
		};
		npc.deathEndListener = (_, _, _) -> {
			if (dead)
				return;
			dead = true;
			npc.remove();
			resetState();
		};

		resetState();

		if (phase.equals(SPAWN))
			handleIntro();
	}

	@Override
	public void follow() {
		follow(forceMelee ? 1 : 5);
	}

	@Override
	public boolean attack() {
		if (npc.isLocked())
			return false;

		if (target == null)
			return false;

		if (regularAttackCount >= 5) {
			regularAttackCount = 0;
			return new SpecialAttack()
					.decideSpecialAttack(this)
					.invoked(this);
		}

		// handle specials 1st, melee 2nd
		if (forceMelee) {
			npc.startEvent(e -> {
				e.delay(4);
				if (forceMelee) {
					forceMelee = false;
				}
			});
			new StandardMeleeAttack().invoke(target.player, this);
			forceMelee = false;
			return true;
		}

		regularAttackCount++;
		// do some type of attack dependent on the phase
		return switch (phase) {
			case SMOKE, SMOKE_LOCKED -> {
				new SmokeAttack().invoke(target.player, this);
				yield true;
			}
			case SHADOW, SHADOW_LOCKED -> {
				new ShadowAttack().invoke(target.player, this);
				yield true;
			}
			case BLOOD, BLOOD_LOCKED -> {
				new BloodAttack().invoke(target.player, this);
				yield true;
			}
			case ICE, ICE_LOCKED -> {
				new IceAttack().invoke(target.player, this);
				yield true;
			}
			case ZAROS -> {
				new ZarosAttack().invoke(target.player, this);
				yield true;
			}
			default -> {
				new StandardMeleeAttack().invoke(target.player, this);
				yield false;
			}
		};
	}

	@Override
	public void process() {
		// If no players in nex, clear the room out.
		if (!ZarosUtils.anyPlayerInNex(npc)) {
			log.trace("Nex room is empty, clearing out.");
			npc.remove();
			resetState();
			return;
		}
		if (ZarosUtils.countPlayersInNex(npc) > 2)
			overTwoPlayers = true;
		else if (ZarosUtils.countPlayersInNex(npc) > 3)
			overThreePlayers = true;
		else if (ZarosUtils.countPlayersInNex(npc) > 1)
			overOnePlayers = true;

		if (target == null)
			if (!npc.getPosition().getRegion().players.isEmpty()) {
				target = npc.getPosition().getRegion().players.getFirst();
				npc.getCombat().setTarget(target);
			}

		try {
			if (target == null)
				if (npc.getPosition().getRegion().players.isEmpty())
					target = npc.getPosition().getRegion().players.getFirst();
				else
					target = npc.getPosition().getRegion().players.getFirst();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		if (npc.isLocked()) // if npc is locked, stop
			return;

		if (damageHealsDelay > 0) // if we have a heal delay, de-increment it.
			damageHealsDelay = damageHealsDelay - 1;

		checkPhase();

		if (wrathHook())
			getNpc().transform(Forms.WRATH_NEX.getNpcId());

		// if can attack and 20% chance to hit, force a melee
		if (this.canMeleeAttack() && Random.get(1, 5) == 1)
			forceMelee = true;
	}

	@Override
	protected boolean canAggro(Player player) {
		// if nex is unattackable form, don't aggro
		if (npc.getId() == Forms.UNATTACKABLE_NEX.getNpcId())
			return false;

		if (player == null || player.isHidden())
			return false;

		if (player.getCombat() == null)
			return false;

		final var canAtk = canAttack(player);
		final var inPos = player.getPosition().inBounds(ATTACK_BOUNDS);

		return inPos && canAtk;
	}

	@Override
	public boolean isAggressive() {
		return true;
	}

	@Override
	public int getAggressionRange() {
		return 16;
	}

	private void checkPhase() {
		phaseLocked = true;
		if (phase.getMinionId() != -1) {
			for (NPC minion : minions) {
				if (minion == null)
					continue;
				if (minion.getId() == phase.getMinionId()) {
					if (minion.isInvincible() || minion.dead())
						phaseLocked = false;
				}
			}
		}

		var changedPhase = Phase.getPhase(npc.getHp() / (double) npc.getMaxHp(), phaseLocked);
		if (phase == changedPhase || phase.ordinal() >= changedPhase.ordinal())
			return;

		phase = changedPhase;
		if (phase.getMinionId() != -1) {
			for (NPC minion : minions) {
				if (minion == null)
					continue;
				if (minion.getId() == phase.getMinionId()) {
					if (minion.alive()) {
						minion.setInvincible(false);
						Attack.MINION_SPAWN_PROJ.send(minion.getPosition().copy(), npc.getPosition().copy());
					}
				}
			}
		}
		npc.forceText(phase.getPhrase());
		npc.getPosition().getRegion().players.forEach(p -> {
			if (!p.getPosition().inBounds(ATTACK_BOUNDS))
				return;
			p.sendMessage("Nex: " + phase.getPhrase());
		});
		if (!phaseLocked) {
			regularAttackCount = 0;
			npc.getCombat().delayAttack(4);
			// consider reducing delay here if its not procing fast enough. It doesn't "have" to be 4.
			switch (phase) {
				case SHADOW:
					new ShadowAttack().invoke(getTarget().player, this);
					break;
				case BLOOD:
					npc.addEvent(event -> {
						event.setCancelCondition(this::targetIsNotInBossRegion);
						event.delay(4);
						new BloodSiphonAttack().invoke(getTarget().player, this);
					});
					break;
				case ICE:
					BloodSiphonAttack.bloodSiphonHeal(this);
					npc.addEvent(event -> {
						event.setCancelCondition(this::targetIsNotInBossRegion);
						event.delay(4);
						new ContainmentAttack().invoke(getTarget().player, this);
					});
					break;
				case ZAROS: {
					npc.hit(new Hit(HitType.HEAL).fixedDamage(500).ignoreDefence().ignorePrayer());
					hasHealedInZarosForm = true; // toggle this to true.
					npc.animate(9179);
					npc.graphics(2016);
					npc.addEvent(event -> {
						event.setCancelCondition(this::targetIsNotInBossRegion);
						event.delay(4);
						new SoulSplitAttack(true).invoke(getTarget().player, this);
					});
					break;
				}
			}
		}
	}

	private void preDefend(Hit hit) {
		// When heal delay and locked, heal for all incoming dmg.
		if (damageHealsDelay > 0 && hit.type != HitType.HEAL && npc.isLocked()) {
			hit.type = HitType.HEAL;
			healedFromSiphon = true;
		}
		// Stop here for heals. Only dmg below.
		if (hit.type == HitType.HEAL)
			return;

		// handle reflecting melee damage back to melee attackers.
		if (hit.attackStyle != null && npc.getId() == Forms.REFLECT_MELEE_NEX.getNpcId() && hit.attackStyle.isMelee()) {
			var opponent = hit.attacker;
			if (opponent == null)
				return;

			opponent.player.hit(
					new Hit(npc, AttackStyle.MAGICAL_MELEE, null)
							.fixedDamage(hit.damage) // reflect back what we're hit for
							.ignorePrayer()
							.ignoreDefence());
			hit.block();
			return;
		}
		// If you've attacked a locked nex, block all dmg.
		switch (phase) {
			case SPAWN: // handle the spawn phase. Should not be attack-able.
			case SMOKE_LOCKED:
			case SHADOW_LOCKED:
			case BLOOD_LOCKED:
			case ICE_LOCKED:
				hit.block();
				break;
		}
	}

	private void postTargetDefend(Hit hit, Entity entity) {
		if (npc.getId() == Forms.SOUL_SPLIT_NEX.getNpcId()) {
			if (hit.damage > 0) {
				soulSplitDamage += hit.damage; // add damage to a counter
				npc.addEvent(e -> {
					e.setCancelCondition(this::targetIsNotInBossRegion);
					e.delay(1);
					Attack.SOUL_SPLIT_PROJ.send(entity, npc);
				});
			}
		}
	}

	private void postDamage(Hit hit) {
		if (hit.damage > 0 && hit.attacker != null) {
			if (damagingPlayers.get(hit.attacker.player) != null)
				damagingPlayers.replace(hit.attacker.player, (damagingPlayers.get(hit.attacker.player) + hit.damage));
			else
				damagingPlayers.put(hit.attacker.player, hit.damage);
		}
		if (npc.getId() == Forms.SOUL_SPLIT_NEX.getNpcId()) {
			if (soulSplitDamage > 0) {
				int damage = soulSplitDamage;
				soulSplitDamage = 0;
				npc.addEvent(e -> {
					e.delay(3);
					npc.hit(new Hit(HitType.HEAL).fixedDamage(damage));
				});
			}
		}
	}

	private void handleIntro() {
		if (npc.getId() != Forms.UNATTACKABLE_NEX.getNpcId())
			npc.transform(Forms.UNATTACKABLE_NEX.getNpcId());

		npc.lock(LockType.FULL);
		npc.publicSound(3950, 1, 0);

		npc.face(Direction.EAST);
		npc.forceText(phase.getPhrase());
		npc.getPosition().getRegion().players.forEach(p -> {
			if (!p.getPosition().inBounds(ATTACK_BOUNDS))
				return;
			p.sendMessage("Nex: " + phase.getPhrase());
		});

		npc.startEvent(event -> {
			event.delay(5);
			// talk to minion noobs
			npc.face(Direction.NORTH_WEST);
			npc.animate(9188);
			npc.forceText("Fumus!");
			npc.getPosition().getRegion().players.forEach(p -> {
				if (!p.getPosition().inBounds(ATTACK_BOUNDS))
					return;
				p.sendMessage("Nex: Fumus!");
			});
			FUMUS_SPAWN = new NPC(FumusCombat.ID).spawn(
					npc.getPosition().getRegion().baseX + 33,
					npc.getPosition().getRegion().baseY + 31,
					npc.getPosition().getZ(),
					Direction.SOUTH_EAST,
					0);
			FUMUS_SPAWN.setInvincible(true);
			minions.add(FUMUS_SPAWN);
			Attack.MINION_SPAWN_PROJ.send(FUMUS_SPAWN.getPosition(), Position.of(
					npc.getPosition().getRegion().baseX + 45,
					npc.getPosition().getRegion().baseY + 19,
					npc.getPosition().getZ()));

			event.delay(4);

			npc.face(Direction.NORTH_EAST);
			npc.animate(9188);
			npc.forceText("Umbra!");
			npc.getPosition().getRegion().players.forEach(p -> {
				if (!p.getPosition().inBounds(ATTACK_BOUNDS))
					return;
				p.sendMessage("Nex: Umbra!");
			});
			UMBRA_SPAWN = new NPC(UmbraCombat.ID).spawn(
					npc.getPosition().getRegion().baseX + 57,
					npc.getPosition().getRegion().baseY + 31,
					npc.getPosition().getZ(),
					Direction.SOUTH_WEST,
					0);
			UMBRA_SPAWN.setInvincible(true);
			minions.add(UMBRA_SPAWN);
			Attack.MINION_SPAWN_PROJ.send(UMBRA_SPAWN.getPosition(), Position.of(
					npc.getPosition().getRegion().baseX + 45,
					npc.getPosition().getRegion().baseY + 19,
					npc.getPosition().getZ()));

			event.delay(4);

			npc.face(Direction.SOUTH_EAST);
			npc.animate(9188);
			npc.forceText("Cruor!");
			npc.getPosition().getRegion().players.forEach(p -> {
				if (!p.getPosition().inBounds(ATTACK_BOUNDS))
					return;
				p.sendMessage("Nex: Cruor!");
			});
			CRUOR_SPAWN = new NPC(CruorCombat.ID).spawn(
					npc.getPosition().getRegion().baseX + 57,
					npc.getPosition().getRegion().baseY + 7,
					npc.getPosition().getZ(),
					Direction.NORTH_WEST,
					0);
			CRUOR_SPAWN.setInvincible(true);
			CRUOR_SPAWN.deathEndListener = (DeathListener.Simple) () -> {
				phase = Phase.ICE;
				npc.forceText(Phase.ICE.getPhrase());
				npc.getPosition().getRegion().players.forEach(p -> {
					if (!p.getPosition().inBounds(ATTACK_BOUNDS))
						return;
					p.sendMessage("Nex: " + Phase.ICE.getPhrase());
				});
				CRUOR_SPAWN.remove();
			};
			minions.add(CRUOR_SPAWN);
			Attack.MINION_SPAWN_PROJ.send(CRUOR_SPAWN.getPosition(), Position.of(
					npc.getPosition().getRegion().baseX + 45,
					npc.getPosition().getRegion().baseY + 19,
					npc.getPosition().getZ()));

			event.delay(4);

			npc.face(Direction.SOUTH_WEST);
			npc.animate(9188);
			npc.forceText("Glacies!");
			npc.getPosition().getRegion().players.forEach(p -> {
				if (!p.getPosition().inBounds(ATTACK_BOUNDS))
					return;
				p.sendMessage("Nex: Glacies!");
			});
			GLACIES_SPAWN = new NPC(GlaciesCombat.ID).spawn(
					npc.getPosition().getRegion().baseX + 33,
					npc.getPosition().getRegion().baseY + 7,
					0, Direction.NORTH_EAST, 0);
			GLACIES_SPAWN.setInvincible(true);
			minions.add(GLACIES_SPAWN);
			Attack.MINION_SPAWN_PROJ.send(GLACIES_SPAWN.getPosition(), Position.of(
					npc.getPosition().getRegion().baseX + 45,
					npc.getPosition().getRegion().baseY + 19,
					npc.getPosition().getZ()));

			event.delay(4);

			npc.forceText("FILL ME WITH SMOKE!");
			npc.getPosition().getRegion().players.forEach(p -> {
				if (!p.getPosition().inBounds(ATTACK_BOUNDS))
					return;
				p.sendMessage("Nex: FILL ME WITH SMOKE!");
			});
			phase = Phase.SMOKE;
			npc.unlock();
			npc.transform(Forms.DEFAULT_NEX.getNpcId());
		});
	}

	private boolean canMeleeAttack() {
		var target = npc.getPosition()
				.getRegion().players
						.stream()
						.filter(t -> Misc.getDistance(t.getPosition().copy(), npc.getPosition().copy()) == 1)
						.findFirst();
		if (target.isPresent()) {
			forceMelee = true;
			return true;
		}
		return false;
	}

	private boolean wrathHook() {
		// If we're already in wrath form, stop.
		if (npc.getId() == Forms.WRATH_NEX.getNpcId())
			return false;
		// if we're not in zaros phase, stop.
		if (phase != Phase.ZAROS)
			return false;

		// if we have healed already, stop. (This can only occur once)
		if (!hasHealedInZarosForm)
			return false;

		final double currentHP = npc.getHp() / (double) npc.getMaxHp();
		return currentHP <= 0.075;
	}

	@Override
	protected Entity findAggressionTarget() {
		var npc = this.getNpc();
		if (npc.getPosition().getRegion().players.isEmpty())
			return null;
		if (npc.hasTarget())
			return null;
		List<Player> targets = npc.getPosition()
				.getRegion().players
						.stream()
						.filter(t -> t.getPosition().inBounds(ATTACK_BOUNDS))
						.filter(this::canAggro)
						.collect(Collectors.toList());

		if (targets.isEmpty())
			return null;
		return Random.get(targets);
	}

	private void resetState() {
		ZarosUtils.removeNpc(bloodReavers);
		ZarosUtils.removeNpc(minions);
		ZarosUtils.removeObjects(icicles);
		ZarosUtils.removeObjects(stalagmites);

		phase = SPAWN;
		hasHealedInZarosForm = false;
		regularAttackCount = 0;
		damageHealsDelay = 0;
		bloodReavers.clear();
		minions.clear();
		icicles.clear();
		stalagmites.clear();
		healedFromSiphon = false;
		hasHealedInZarosForm = false;
		damagedByShadow = false;
		damagedByIce = false;
		dead = false;
		phaseLocked = true;
		overOnePlayers = false;
		overTwoPlayers = false;
		overThreePlayers = false;
		forceMelee = false;
		playersKilled = 0;
	}
}
