package npc.nex.old;

import io.ruin.cache.ObjType;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Common;
import npc.nex.modes.Phase;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class contains all the Nex utilities.
 * <p>
 * Last updated to Nex v1.2 by Riley on 6/8/2022.
 * <p>
 * Changes: 1.1: Fixling drop issue 1.2: Change drop mechanics to drop loot to the entire group
 *
 * @author R-Y-M-R
 * @version 1.2
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 * @since 2/25/2022
 */
public class NexUtils {


	public static final String ATTRIBUTE_LOOT = "NEX_DMG";
	public static final int MINIMUM_DAMAGE_FOR_LOOT = 100;
	public static final String INFECTION = "NEX_INFECTION_EFFECT";
	private static final String RONA = "*Cough*";
	private static final int INFECTION_DURATION_SECONDS = 8;
	private static final int INFECTION_DURATION_TICKS = Common.convertSecondsToTicksInt(INFECTION_DURATION_SECONDS);
	private static final int INFECTION_PRAYER_DRAIN = 4;
	private static final int INFECTION_PRAYER_DRAIN_SPECTRAL = 3;
	public static final Position NEX_WAITING_ROOM = new Position(2904, 5203, 0);

	public static void register() {
		MapListener.registerBounds(Nex.NEX_BOUNDS)
				.onEnter(p -> {
					if (p.hitListener == null)
						p.getCombat().init(p);
					p.hitListener.postTargetDamage(NexUtils::playerPostTargetDamage);
				})
				.onExit((player, logout) -> {
					if (logout) {
						// moves the player out
						player.getMovement().teleport(NEX_WAITING_ROOM);
					}
					removePlayerNexHook(player); // removes the hook
					player.getCombat().init(player);
				});
	}

	public static boolean shouldSpawnNex(Player p) {
		Bounds bounds = new Bounds(p.getPosition().getRegion().baseX + 29, p.getPosition().getRegion().baseY + 3,
				p.getPosition().getRegion().baseX + 61, p.getPosition().getRegion().baseY + 36, 0);
		if (World.npcsNonNull().filter(npc -> npc.getPosition().inBounds(bounds))
				.anyMatch(npc -> Nex.Forms.isNex(npc.getId()))) {
			// System.out.println("Found an alive nex. Returned false.");
			return false;
		}
		// System.out.println("No live nex found.");
		return true;
	}

	/**
	 * The player's post target damage
	 *
	 * @param hit
	 * @param entity
	 */
	static void playerPostTargetDamage(Hit hit, Entity entity) {
		if (hit.attacker == null) {
			return;
		}
		Player p = hit.attacker.player;
		if (entity.isNpc()) {
			if (Nex.Forms.isNex(entity.npc.getId())) {
				if (hit.damage > 0) {
					if (hit.isBlocked() || hit.type != HitType.DAMAGE) {
						return;
					}
					NexUtils.setNexDamageCounter(p, hit.damage, true);
				}
			}
		}
	}

	/**
	 * sets the Nex Damage Counter
	 *
	 * @param p
	 * @param amount
	 * @param addition
	 */
	public static void setNexDamageCounter(Player p, int amount, boolean addition) {
		int previous = p.get(NexUtils.ATTRIBUTE_LOOT, 0);
		if (addition) {
			p.set(NexUtils.ATTRIBUTE_LOOT, amount + previous);
		} else {
			p.set(NexUtils.ATTRIBUTE_LOOT, amount);
		}
		// System.out.println((addition ? "Added " : "Set ")+p.getName()+"'s Nex Damage ("+amount+").");
	}

	/**
	 * Sets Nex Damage Counter to zero
	 *
	 * @param p
	 */
	public static void resetNexDamageCounter(Player p) {
		p.set(NexUtils.ATTRIBUTE_LOOT, 0);
		// System.out.println("Reset "+p.getName()+"'s Nex Damage Counter");
	}

	/**
	 * Removes Nex Hook
	 *
	 * @param p
	 */
	static void removePlayerNexHook(Player p) {
		p.hitListener.postTargetDamage(null);
		resetNexDamageCounter(p);
		p.getCombat().init(p);
		// System.out.println("Removed "+p.getName()+"'s Nex Hook");
	}

	public static boolean isNex(int npcId) {
		return Nex.Forms.isNex(npcId);
	}

	private static boolean isInNexBounds(Player player) {
		var arena = new Bounds(2909, 5218, 2942, 5188, 0);
		return player.getPosition().inBounds(arena);
	}

	static void plagueEffect(Player player, boolean wasSpread) {
		if (player == null || !isInNexBounds(player) || player.get(INFECTION) != null)
			return;

		player.forceText(RONA);
		player.animate(1156); // COUGH COUGH LADS
		StatType type = getDrainedStat(player);
		final int duration = hasSlayerHelm(player) ? INFECTION_DURATION_TICKS / 2 : INFECTION_DURATION_TICKS;
		player.set(INFECTION, true);
		player.addEvent(event -> {
			player.hit(new Hit(HitType.DAMAGE).randDamage(4, 6));
			player.getPrayer().drain(hasSpectral(player) ? INFECTION_PRAYER_DRAIN_SPECTRAL : INFECTION_PRAYER_DRAIN);
			for (int i = 0; i < duration; i++) {
				if (!player.getPosition().inBounds(Nex.NEX_BOUNDS))
					return;
				if (i % 2 == 0) {
					player.forceText(RONA);
					player.getPrayer().drain(hasSpectral(player) ? INFECTION_PRAYER_DRAIN_SPECTRAL : INFECTION_PRAYER_DRAIN);
					player.getStats().get(type).drain(2); // drain highest stat by 2
					player.getStats().get(StatType.Defence).drain(2); // drain def by 2
					player.localPlayers().forEach(other -> {
						if (other.getPosition().isWithinDistance(player.getPosition(), 1))
							plagueEffect(other, true);
					});
				}
				event.delay(1);
			}
			player.remove(INFECTION);
		});
	}

	static boolean isLocked(Phase phase) {
		return Arrays.stream(Phase.values()).filter(p -> p.toString().toLowerCase().contains("locked"))
				.anyMatch(p -> phase.equals(p));
	}

	/**
	 * nex's position is in the lower left position so if you're within the upper or right we check a further distance.
	 *
	 * @param p
	 * @param entity = nex
	 * @return
	 */
	static int calculateWrathDistance(Player p, Entity entity) {
		int dist = 2;
		if (p.getPosition().getY() >= entity.getPosition().getY())
			dist += 1;
		if (p.getPosition().getX() >= entity.getPosition().getX())
			dist += 1;
		return dist;
	}

	static StatType getDrainedStat(Player p) {
		int stat = 0;
		int bonus = 0;
		// mage
		if (p.getEquipment().bonuses[3] > bonus) {
			bonus = p.getEquipment().bonuses[3];
			stat = 3;
		}
		// range
		if (p.getEquipment().bonuses[4] > bonus) {
			bonus = p.getEquipment().bonuses[4];
			stat = 4;
		}
		// str
		if (p.getEquipment().bonuses[10] > bonus) {
			bonus = p.getEquipment().bonuses[10];
			stat = 10;
		}
		// stab atk, slash atk, crash (we'll drain atk)
		for (int i = 0; i < 2; i++) {
			if (p.getEquipment().bonuses[i] > bonus) {
				bonus = p.getEquipment().bonuses[i];
				stat = i;
			}
		}

		// System.out.println("Bonus: " + bonus + ", stat: " + stat);

		switch (stat) {
			case 0:
			case 1:
			case 2:
				return StatType.Attack;
			case 3:
				return StatType.Magic;
			case 5:
				return StatType.Strength;
			case 4:
			default:
				return StatType.Ranged;
		}
	}

	static boolean hasSlayerHelm(Player p) {
		try {
			if (p.getEquipment().get(Equipment.SLOT_HAT) != null) {
				if (ObjType.get(p.getEquipment().get(Equipment.SLOT_HAT).getId()).name.toLowerCase().contains("slayer helm")) {
					p.sendFilteredMessage("Your slayer helm reduces the duration of the infection.");
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	static boolean hasSpectral(Player player) {
		if (player.getEquipment().contains(ItemID.SPECTRAL_SPIRIT_SHIELD)) {
			player.sendFilteredMessage("Your spectral reduces the prayer drain.");
			return true;
		}
		return false;
	}

	static void spreadInfection(Player player) {
		if (player == null || !isInNexBounds(player) || player.get(INFECTION) != null)
			return;
		player.localPlayers().forEach(other -> {
			if (other.getPosition().isWithinDistance(player.getPosition(), 1))
				plagueEffect(other, true);
		});
	}

	static Bounds getContainmentBounds(Entity nex) {
		// Bounds temp = new Bounds(2909, 5187, 2941, 5220, 0);
		return new Bounds(nex.getPosition().getX() - 1, nex.getPosition().getY() - 1, nex.getPosition().getX() + 4,
				nex.getPosition().getY() + 4, 0);
	}

	static List<Position> getContainmentTiles(Entity e) {
		return Arrays.asList(
				new Position(e.getPosition().getX() - 1, e.getPosition().getY() - 1, e.getHeight()), // good
				new Position(e.getPosition().getX(), e.getPosition().getY() - 1, e.getHeight()), // good
				new Position(e.getPosition().getX() + 1, e.getPosition().getY() - 1, e.getHeight()), // good
				new Position(e.getPosition().getX() + 2, e.getPosition().getY() - 1, e.getHeight()), // good
				new Position(e.getPosition().getX() + 3, e.getPosition().getY() - 1, e.getHeight()), // good
				new Position(e.getPosition().getX() - 1, e.getPosition().getY() + 3, e.getHeight()), // good
				new Position(e.getPosition().getX(), e.getPosition().getY() + 3, e.getHeight()), // good
				new Position(e.getPosition().getX() + 1, e.getPosition().getY() + 3, e.getHeight()), // good
				new Position(e.getPosition().getX() + 2, e.getPosition().getY() + 3, e.getHeight()), // good
				new Position(e.getPosition().getX() + 3, e.getPosition().getY() + 3, e.getHeight()), // good
				new Position(e.getPosition().getX() + -1, e.getPosition().getY(), e.getHeight()), // good
				new Position(e.getPosition().getX() + -1, e.getPosition().getY() + 1, e.getHeight()), // good
				new Position(e.getPosition().getX() + -1, e.getPosition().getY() + 2, e.getHeight()), // good
				new Position(e.getPosition().getX() + 3, e.getPosition().getY(), e.getHeight()), // good
				new Position(e.getPosition().getX() + 3, e.getPosition().getY() + 1, e.getHeight()), // good
				new Position(e.getPosition().getX() + 3, e.getPosition().getY() + 2, e.getHeight()) // good
		);
	}

	static List<Position> getWrathTiles(Entity e) {
		return Arrays.asList(new Position(e.getPosition().getX() + 1, e.getPosition().getY() - 2, e.getHeight()),
				new Position(e.getPosition().getX() - 1, e.getPosition().getY() - 1, e.getHeight()),
				new Position(e.getPosition().getX() + 3, e.getPosition().getY() - 1, e.getHeight()),
				new Position(e.getPosition().getX() + 3, e.getPosition().getY() - 1, e.getHeight()),
				new Position(e.getPosition().getX() - 2, e.getPosition().getY() + 1, e.getHeight()),
				new Position(e.getPosition().getX() + 4, e.getPosition().getY() + 1, e.getHeight()),
				new Position(e.getPosition().getX() - 1, e.getPosition().getY() + 3, e.getHeight()),
				new Position(e.getPosition().getX() + 3, e.getPosition().getY() + 3, e.getHeight()),
				new Position(e.getPosition().getX() + 1, e.getPosition().getY() + 4, e.getHeight()));
	}

	/**
	 * Nex has an offset of +1, +1 x, y because of her size.
	 *
	 * @param e
	 * @param booleanArrayIndex
	 * @return
	 */
	static List<Position> getPossiblePullPositions(Entity e, int booleanArrayIndex) {
		if (booleanArrayIndex == 0) { // East
			return Arrays.asList(
					new Position(e.getPosition().getX() + 1 + 4, e.getPosition().getY() + 1, e.getHeight()),
					new Position(e.getPosition().getX() + 1 + 3, e.getPosition().getY() + 1, e.getHeight()),
					new Position(e.getPosition().getX() + 1 + 2, e.getPosition().getY() + 1, e.getHeight()));
		} else if (booleanArrayIndex == 1) { // West
			return Arrays.asList(
					new Position(e.getPosition().getX() + 1 - 4, e.getPosition().getY() + 1, e.getHeight()),
					new Position(e.getPosition().getX() + 1 - 3, e.getPosition().getY() + 1, e.getHeight()),
					new Position(e.getPosition().getX() + 1 - 2, e.getPosition().getY() + 1, e.getHeight()));
		} else if (booleanArrayIndex == 2) { // North
			return Arrays.asList(
					new Position(e.getPosition().getX() + 1, e.getPosition().getY() + 1 + 4, e.getHeight()),
					new Position(e.getPosition().getX() + 1, e.getPosition().getY() + 1 + 3, e.getHeight()),
					new Position(e.getPosition().getX() + 1, e.getPosition().getY() + 1 + 2, e.getHeight()));
		} else if (booleanArrayIndex == 3) { // South
			return Arrays.asList(
					new Position(e.getPosition().getX() + 1, e.getPosition().getY() + 1 - 4, e.getHeight()),
					new Position(e.getPosition().getX() + 1, e.getPosition().getY() + 1 - 3, e.getHeight()),
					new Position(e.getPosition().getX() + 1, e.getPosition().getY() + 1 - 2, e.getHeight()));
		} else {
			return (Arrays.asList(
					new Position(2925, 5203, 0)));
		}
	}

	static List<Position> getIcePrisonPositions(Entity e) {
		return Arrays.asList(
				new Position(e.getPosition().getX() - 1, e.getPosition().getY() - 1, e.getHeight()),
				new Position(e.getPosition().getX() - 1, e.getPosition().getY(), e.getHeight()),
				new Position(e.getPosition().getX() - 1, e.getPosition().getY() + 1, e.getHeight()),
				new Position(e.getPosition().getX(), e.getPosition().getY() - 1, e.getHeight()),
				new Position(e.getPosition().getX() + 1, e.getPosition().getY() - 1, e.getHeight()),
				// new Position(e.getPosition().getX(), e.getPosition().getY(), e.getHeight()),
				new Position(e.getPosition().getX(), e.getPosition().getY() + 1, e.getHeight()),
				new Position(e.getPosition().getX() + 1, e.getPosition().getY(), e.getHeight()),
				new Position(e.getPosition().getX() + 1, e.getPosition().getY() + 1, e.getHeight()));
	}

	static Bounds getIcePrisonBounds(Entity nex) {
		return new Bounds(
				nex.getPosition().getX() - 1,
				nex.getPosition().getY() - 1,
				nex.getPosition().getX() + 1,
				nex.getPosition().getY() + 1,
				0);
	}

	public static Stream<Player> getPlayersAtNex() {
		return World.playersNonNull().filter(p -> p.getPosition().inBounds(Nex.NEX_BOUNDS));
	}

	static boolean anyPlayerInNex(NPC npc) {
		return World.playersNonNull().filter(p -> p.getPosition().regionId() == npc.getPosition().regionId())
				.anyMatch(p -> !p.dead());
	}


	static long countPlayersInNex(NPC npc) {
		return World.playersNonNull().filter(p -> p.getPosition().regionId() == npc.getPosition().regionId()).count();
	}

	static long countPlayersInNex() {
		return World.playersNonNull().filter(p -> p.getPosition().inBounds(Nex.NEX_BOUNDS)).count();
	}


	static void removeNpc(List<NPC> toRemove) {
		for (NPC n : toRemove) {
			if (n != null) {
				if (n.alive() && !n.isRemoved()) {
					n.remove();
				}
			}
		}
	}

	static void removeObjects(List<GameObject> i) {
		for (GameObject r : i) {
			if (r != null) {
				if (!r.isRemoved()) {
					r.remove();
				}
			}
		}
	}

}
