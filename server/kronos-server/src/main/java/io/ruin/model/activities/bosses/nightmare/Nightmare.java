package io.ruin.model.activities.bosses.nightmare;

import io.ruin.model.World;
import io.ruin.model.combat.CombatUtils;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.Position;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Misc;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Nightmare extends NPC {
	private List<NPC> activeSleepwalkers = new ArrayList<>();
	private static final int SLEEPWALKER_ID = 9450;
	private volatile boolean phaseTransitioning = false;
	private static final SpecialAttacks[][] SPECIAL_ATTACKS = {
			{
					SpecialAttacks.GRASPING_CLAWS,
					SpecialAttacks.FLOWER_POWER,
					// SpecialAttacks.HUSKS,
					SpecialAttacks.GRASPING_CLAWS,
					SpecialAttacks.FLOWER_POWER,
					// SpecialAttacks.HUSKS,
					SpecialAttacks.GRASPING_CLAWS,
					SpecialAttacks.FLOWER_POWER,
					// SpecialAttacks.HUSKS,
					SpecialAttacks.GRASPING_CLAWS,
					SpecialAttacks.FLOWER_POWER,
			// SpecialAttacks.HUSKS,
			},
			{
					SpecialAttacks.GRASPING_CLAWS,
					SpecialAttacks.CURSE,
					// SpecialAttacks.PARASITES,
					SpecialAttacks.GRASPING_CLAWS,
					SpecialAttacks.CURSE,
					// SpecialAttacks.PARASITES,
					SpecialAttacks.GRASPING_CLAWS,
					SpecialAttacks.CURSE,
			// SpecialAttacks.PARASITES,
			},
			{
					SpecialAttacks.GRASPING_CLAWS,
					SpecialAttacks.GRASPING_CLAWS,
					SpecialAttacks.GRASPING_CLAWS,
					// SpecialAttacks.SURGE,
					SpecialAttacks.SPORES,
					SpecialAttacks.SPORES,
					SpecialAttacks.SPORES
			}
	};

	public void spawnSleepwalker(Position position) {
		try {
			NPC sleepwalker = new NPC(SLEEPWALKER_ID);
			// Use the proper NPC spawn method from the base class
			sleepwalker.spawn(position);

			// Add proper death handling using built-in listeners
			sleepwalker.deathEndListener = (entity, killer, killHit) -> {
				activeSleepwalkers.remove(sleepwalker);
				sleepwalkerCount--;

				// Check if all sleepwalkers are dead
				// In the death listener:
				if (sleepwalkerCount == 0 && stageDelta == -1 && !phaseTransitioning) {
					phaseTransitioning = true;
					World.startEvent(event -> {
						try {
							transform(9425 + stage);
							getCombat().setSpecial(null);
							specialDelta = 60;
						} finally {
							phaseTransitioning = false;
						}
					});
				}
			};

			// Add to tracking lists
			activeSleepwalkers.add(sleepwalker);
			sleepwalkerCount++;

			// Handle combat settings
			if (sleepwalker.getCombat() != null) {
				sleepwalker.getCombat().setAllowRespawn(false);
			}

		} catch (Exception e) {
		}
	}

	// Override NPC's remove method
	@Override
	public void remove() {
		cleanupSleepwalkers();
		if (totems != null) {
			for (TotemPlugin totem : totems) {
				if (totem != null) {
					totem.remove();
				}
			}
			totems = null;
		}
		super.remove();
	}

	private void cleanupSleepwalkers() {
		// Add error handling and logging
		if (!activeSleepwalkers.isEmpty()) {
			for (NPC sleepwalker : new ArrayList<>(activeSleepwalkers)) {
				try {
					if (sleepwalker != null && !sleepwalker.isRemoved()) {
						sleepwalker.remove();
					}
				} catch (Exception e) {
					log.error("[Nightmare] Error removing sleepwalker: " + e.getMessage(), e);
				}
			}
			activeSleepwalkers.clear();
			sleepwalkerCount = 0;
		}
	}

	public void spawnAllSleepwalkers() {
		cleanupSleepwalkers(); // Clean up any existing sleepwalkers first
		for (Position pos : getSleepwalkerPositions()) {
			spawnSleepwalker(pos);
		}
	}

	public static final int NO_TELEPORT = 0, CENTER = 1, EDGE = 2;

	private int stage = -1, specialDelta = 60, stageDelta = -1, sleepwalkerCount = 0, parasiteDelta = -1,
			flowerRotary = -1, flyDirection = -1;

	private TotemPlugin[] totems;

	private Position base;

	private boolean shield;

	public Nightmare(int id, Position base) {
		super(id);
		this.base = base;
		init();
	}

	public static void register() {
		NPCAction.register(9461, "disturb", (player, npc) -> {
			World.startEvent(event -> {
				player.dialogue(new MessageDialogue("" + player.getName() + ", would you like to fight The Nightmare?"),
						new OptionsDialogue("Choose an option",
								new Option("Start a new instance", () -> {
									if (player.getInventory().contains(6) || player.getInventory().contains(8)
											|| player.getInventory().contains(10) || player.getInventory().contains(12)) {
										player.sendMessage("You cannot bring a cannon into The Nightmare");
										return;
									}
									ArrayList<Player> team = new ArrayList<>();
									team.add(player);
									NightmareEvent.createInstance(team);
								}),
								new Option("Join a friends instance", () -> {
									if (player.getInventory().contains(6) || player.getInventory().contains(8)
											|| player.getInventory().contains(10) || player.getInventory().contains(12)) {
										player.sendMessage("You cannot bring a cannon into The Nightmare");
										return;
									}
									player.nameInput("Enter friend's name:", key -> {
										NightmareEvent.joinInstance(key, player);
									});
								}),
								new Option("No thanks.")));
				return;
			});

		});
	}

	@Override
	public boolean isMovementBlocked(boolean message, boolean ignoreFreeze) {
		return getFlyDirection() != -1 || get("next_attack") == null || getId() >= 9431;
	}

	@Override
	public int getMaxHp() {
		return shield ? 80 * getPosition().getRegion().players.size() : 2400;
	}

	public ArrayList<Position> getSleepwalkerPositions() {
		ArrayList<Position> Positions = new ArrayList<>();
		int[][] spots = new int[][] { { 26, 24 }, { 28, 24 }, { 36, 24 }, { 38, 24 } };
		// 24 spawns wtf? I made it 4 { 41, 21 }, { 41, 19 }, { 41, 11 }, { 41, 9 }, {
		// 38, 6 }, { 36, 6 }, { 28, 6 }, { 26, 6 }, { 23, 9 }, { 23, 11 }, { 23, 19 },
		// { 23, 21 }, { 23, 14 }, { 23, 16 }, { 41, 16 }, { 41, 14 }, { 31, 5 }, { 33,
		// 5 }, { 31, 25 }, { 33, 25 }
		for (int i = 0; i < spots.length; i++) {
			Positions.add(getBase().translated(spots[i][0], spots[i][1], 0));
		}
		return Positions;
	}

	@Override
	public int animate(int id) {
		if (getId() == 9431 && id != 8604 && id > 0) {
			return 0;
		}
		return super.animate(id);
	}

	@Override
	public int hit(Hit... hits) {
		int damage = 0;
		boolean process = true;
		boolean dead = false;
		for (Hit hit : hits) {
			Entity attacker = hit.attacker;

			if ((attacker instanceof TotemPlugin) && stageDelta == -1 && stage < 2) {
				stageDelta = 6;
				toggleShield();
				process = false;
			} else if ((attacker instanceof TotemPlugin) && stage < 2) {
				process = false;
				queuedHits.add(hit);
			} else if (attacker instanceof TotemPlugin) {
				if (stage >= 2) {
					dead = true;
				}
			}

			if (attacker instanceof Parasite) {
				hit.type = HitType.HEAL;
				hit.damage = (Misc.random(100));
			}

			if (attacker != null && attacker.isPlayer() && !isShield()) {
				process = false;
			}

			if (process && hit.defend(this)) {
				if (!isLocked(LockType.FULL_NULLIFY_DAMAGE))
					queuedHits.add(hit);
				damage += hit.damage;
			}

			if (shield && 300 >= getCombat().getStat(StatType.Hitpoints).currentLevel && stage <= 2) {
				toggleShield();
				for (Player p : getPosition().getRegion().players) {
					p.sendMessage("<col=ff0000>As the Nightmare's shield fails, the totems in the area are activated!");
				}
				process = false;
			}

		}
		Hit baseHit = hits[0];
		if (process) {
			if (baseHit.type.resetActions) {
				if (player != null)
					player.resetActions(true, false, false);
				else
					npc.resetActions(false, false);
			}
			if (baseHit.attacker != null) {
				if (baseHit.attacker.player != null && baseHit.attackStyle != null) {
					if (player != null) // important that this happens here for things that hit multiple targets
						baseHit.attacker.player.getCombat().skull(player);
					if (baseHit.attackSpell == null)
						CombatUtils.addXp(baseHit.attacker.player, this, baseHit.attackStyle, baseHit.attackType, damage);
				}
				getCombat().updateLastDefend(baseHit.attacker);
			}
		}
		if (getCombat().getStat(StatType.Hitpoints).currentLevel <= 0) {
			for (TotemPlugin t : totems) {
				t.setChargeable(false);
			}
		}
		if (dead && getCombat().getStat(StatType.Hitpoints).currentLevel > 0) {
			if (!npc.getCombat().isDead())
				this.npc.getCombat().startDeath(hits[0]);
		}
		return damage;
	}

	@Override
	public NightmareCombat getCombat() {
		if (!(super.getCombat() instanceof NightmareCombat)) {
			initCombat();
		}
		return (NightmareCombat) super.getCombat();
	}

	@Override
	public void process() {
		if (stage < 0) {
			super.process();
			return;
		}
		if (stageDelta > 0 && --stageDelta == 0 && stage < 2) {
			stageDelta = -1;
			getCombat().reset();
			transform(9431);
			animate(-1);

			World.startEvent(event -> {
				getCombat().setSpecial(SpecialAttacks.SLEEPWALKERS);
				spawnAllSleepwalkers();
				getCombat().attack();
				specialDelta = 30;
				stage++;
			});
		}

		if (stageDelta == 4) {
			teleport(getSpawnPosition());
		}
		if (stageDelta != -1) {
			super.process();
			return;
		}

		if (isAttackable() && !getCombat().hasAttackDelay() && getCombat().getTarget() == null) {
			for (Entity mob : getPossibleTargets()) {
				getCombat().setTarget(mob);
				break;
			}
		}

		if (specialDelta == 0 && stage < 3) {
			SpecialAttacks nextAttack = SPECIAL_ATTACKS[stage][Misc.random(SPECIAL_ATTACKS[stage].length - 1)];
			// nextAttack = SpecialAttacks.SPORES;
			if (nextAttack.teleportOption != NO_TELEPORT) {
				int diffX = 0, diffY = 0;
				if (nextAttack.teleportOption == EDGE) {
					setFlyDirection(0);
					switch (getFlyDirection()) {
						case 0:
							diffY = -10;
							break;
						case 1:
							diffX = 10;
							break;
						case 2:
							diffY = -10;
							break;
						case 3:
							diffX = -10;
							break;
					}
				}
				Position dest = getSpawnPosition().translate(diffX, diffY, 0);
				teleport(dest);
			}
			set("next_attack", nextAttack);
			getCombat().delayAttack(12);
		}
		if (specialDelta == -4) {
			getCombat().setSpecial(remove("next_attack"));
			getCombat().attack();
		}
		if (--specialDelta == -10) {
			getCombat().setSpecial(null);
			specialDelta = 60;
		}
		super.process();
	}

	public ArrayList<Entity> getPossibleTargets() {
		return getPossibleTargets(14, true, false);
	}

	public ArrayList<Entity> getPossibleTargets(int ratio, boolean players, boolean npcs) {
		ArrayList<Entity> possibleTargets = new ArrayList<>();
		if (players) {
			final Position centrePosition = getCentrePosition();
			for (Player player : World.players()) {
				if (player == null) {
					continue;
				}
				if (player.getCombat().isDead()
					|| player.getPosition().distance(centrePosition) > ratio) {
					continue;
				}
				possibleTargets.add(player);
			}
		}
		if (npcs) {
			final Position centrePosition = getCentrePosition();
			for (final Object o : World.npcsSlots()) {
				if (o == null || o == this) {
					continue;
				}
				final NPC npc = (NPC) o;
				if (npc.getCombat().isDead()
					|| npc.getCentrePosition().distance(centrePosition) > ratio) {
					continue;
				}
				possibleTargets.add(npc);
			}
		}
		return possibleTargets;
	}

	public boolean isAttackable() {
		return stage > -1 && getId() < 9430 && stageDelta == -1;
	}

	private void teleport(Position dest) {
		animate(8607);
		getCombat().reset();
		World.startEvent(event -> {
			event.delay(1);
			getMovement().teleport(dest);
			event.delay(1);
			transform(stageDelta == -1 ? 9425 + getStage() : 9431);
			animate(8609);
		});
	}

	public void toggleShield() {
		shield = !shield;

		// Calculate new HP based on player count first
		int newHp = shield
				? (getPosition().getRegion().players.size() <= 1 ? 800 : 800 + (200 * getPosition().getRegion().players.size()))
				: (2400 - (800 * stage));

		// Apply HP change
		getCombat().getStat(StatType.Hitpoints).alter(newHp);

		// Update totems state
		if (totems != null) {
			for (TotemPlugin totem : totems) {
				if (totem != null) {
					totem.setChargeable(!shield);
				}
			}
		}
	}

	public boolean isShield() {
		return shield;
	}

	public void setShield(boolean shield) {
		this.shield = shield;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public TotemPlugin[] getTotems() {
		return totems;
	}

	public void setTotems(TotemPlugin[] totems) {
		this.totems = totems;
	}

	public int getFlowerRotary() {
		return flowerRotary;
	}

	public void setFlowerRotary(int flowerRotary) {
		this.flowerRotary = flowerRotary;
	}

	public int getFlyDirection() {
		return flyDirection;
	}

	public void setFlyDirection(int flyDirection) {
		this.flyDirection = flyDirection;
	}

	public int getSleepwalkerCount() {
		return sleepwalkerCount;
	}

	public void setSleepwalkerCount(int sleepwalkerCount) {
		this.sleepwalkerCount = sleepwalkerCount;
	}

	public int getParasiteDelta() {
		return parasiteDelta;
	}

	public void setParasiteDelta(int parasiteDelta) {
		this.parasiteDelta = parasiteDelta;
	}

	public Position getBase() {
		return base;
	}

	public void initCombat() {
		combat = new NightmareCombat(this);
		combat.init(this, getDef().combatInfo);
	}
}
