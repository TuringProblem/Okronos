package io.ruin.model.activities.jail;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.entity.shared.listeners.LogoutListener;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;

public class Jail {

	/* Constants */
	public static final Position JAIL_LOCATION = new Position(3289, 9435, 0);
	private static final int GUARD_ID = 5442, ROCK = 968;
	private static final int MIN_MINE_DELAY = 3;
	private static final int MAX_MINE_DELAY = 8;

	private static final Bounds BOUNDS = new Bounds(3283, 9427, 3292, 9452, 0);

	/**
	 * Main jailing method
	 */
	public static void jailPlayer(Player player, String jailerName, int rocksAssigned) {
		if (player == null || !player.isPlayer())
			return;

		player.jailed = true;
		player.jailerName = jailerName;
		player.jailOresAssigned = rocksAssigned;
		player.jailOresCollected = 0;

		startEvent(player);

		player.sendMessage(Color.RED.wrap("You have been jailed! You must mine " + rocksAssigned + " rocks to be freed."));
		if (jailerName != null)
			player.sendMessage(Color.RED.wrap("You were jailed by: " + jailerName));
	}

	/**
	 * Player monitoring
	 */
	private static void monitorPlayer(Player player) {
		if (player == null || !player.isOnline() || !player.isPlayer())
			return;

		player.addEvent(event -> {
			while (player.isJailed()) {
				// Simple heal and cure check
				if (player.getHp() < player.getMaxHp() || player.isPoisoned()) {
					player.setHp(player.getMaxHp());
					player.curePoison(0);
					player.cureVenom(0);
				}

				Position pos = player.getPosition();
				if (!BOUNDS.inBounds(pos.getX(), pos.getY(), pos.getZ(), 0)) {
					forceReturnToJail(player);
				}
				validateOreValues(player);
				event.delay(1);
			}
		});
	}

	/**
	 * Jail event starter
	 */
	public static void startEvent(Player player) {
		if (player == null || !player.isPlayer())
			return;

		validateOreValues(player);
		forceReturnToJail(player);
		applyJailRestrictions(player);
		monitorPlayer(player);

		player.addEvent(e -> {
			while (player.isJailed()) {
				validateOreValues(player);
				if (player.jailOresCollected >= player.jailOresAssigned) {
					releasePlayer(player);
					break;
				}
				e.delay(1);
			}
		});
	}

	/**
	 * Ore value validation
	 */
	private static void validateOreValues(Player player) {
		if (player.jailOresCollected < 0) {
			player.jailOresCollected = 0;
		}
		if (player.jailOresAssigned <= 0) {
			player.jailOresAssigned = Math.max(1, player.botWarnings * 100);
		}
	}

	/**
	 * Force return to jail
	 */
	private static void forceReturnToJail(Player player) {
		if (!player.isPlayer())
			return;

		player.getMovement().teleport(JAIL_LOCATION);
		player.sendMessage(Color.RED.wrap("You have been returned to jail."));
		applyJailRestrictions(player);
		validateOreValues(player);
	}

	/**
	 * Apply jail restrictions
	 */
	private static void applyJailRestrictions(Player player) {
		if (!player.isPlayer())
			return;

		player.teleportListener = p -> {
			if (!p.isPlayer()) return true;
			forceReturnToJail(p);
			p.sendMessage("You cannot teleport while in jail.");
			return false;
		};

		player.deathEndListener = (DeathListener.Simple) () -> {
			if (player.isPlayer()) {
				forceReturnToJail(player);
			}
		};

		player.logoutListener = new LogoutListener()
			.onAttempt(p -> {
				Position pos = p.getPosition();
				if (!p.isPlayer() || !BOUNDS.inBounds(pos.getX(), pos.getY(), pos.getZ(), 0)) {
					forceReturnToJail(p);
					p.sendMessage("You cannot logout outside of the jail area.");
					return false;
				}
				p.lastPosition = pos.copy();
				return true;
			});

		player.lock(LockType.NONE);
	}

	/**
	 * Release player from jail
	 */
	private static void releasePlayer(Player player) {
		if (!player.isPlayer())
			return;

		player.jailed = false;
		player.jailerName = null;
		player.jailOresAssigned = 0;
		player.jailOresCollected = 0;
		player.botWarnings = Math.max(0, player.botWarnings - 1);

		player.teleportListener = null;
		player.deathEndListener = null;
		player.logoutListener = null;
		player.unlock();

		player.getMovement().teleport(World.HOME);
		player.sendMessage(Color.OLIVE.tag() + "Your jail sentence is up. Your bot warnings have been reduced to " + player.botWarnings);
	}

	/**
	 * Mining handler
	 */
	public static void mine(Player player) {
		if (!player.isJailed()) {
			player.sendMessage("Only prisoners can mine these rocks.");
			return;
		}

		player.startEvent(event -> {
			int miningLevel = player.getStats().get(StatType.Mining).currentLevel;
			int baseDelay = Math.max(MIN_MINE_DELAY, MAX_MINE_DELAY - (miningLevel / 10));

			player.animate(627);
			player.sendFilteredMessage("You swing your pick at the rock.");
			event.delay(Random.get(MIN_MINE_DELAY, baseDelay));

			while (true) {
				if (player.getInventory().isFull()) {
					player.sendMessage("Your inventory is too full to continue mining.");
					break;
				}

				player.animate(627);
				player.getInventory().add(ROCK, 1);
				player.sendFilteredMessage("You manage to mine some rock.");
				event.delay(Random.get(MIN_MINE_DELAY, baseDelay));
			}
		});
	}

	/**
	 * Static initialization
	 */
	public static void register() {
		// Register login listener to check jail status on login
		LoginListener.register(player -> {
			if (player.isJailed()) {
				forceReturnToJail(player);
				startEvent(player);
			}
		});

		// Setup jail objects and NPCs
		int gateX = 3292, gateY = 9434;
		GameObject[] gates = {
			Tile.getObject(2686, gateX, gateY, 0),
			Tile.getObject(2685, gateX, gateY + 1, 0),
		};

		for (GameObject gate : gates) {
			ObjectAction.register(gate, "Open", (player, obj) -> player.sendMessage("The gate is locked."));
			ObjectAction.register(gate, "Search", (player, obj) -> player.sendMessage("You search the gate but find nothing."));
		}

		// Guard setup
		NPC guard = new NPC(GUARD_ID).spawn(3292, 9434, 0, Direction.WEST, 0);
		guard.skipReachCheck = pos -> pos.equals(gateX, gateY, 0) || pos.equals(gateX, gateY + 1, 0);

		NPCAction.register(guard, "Talk-to", (player, npc) -> {
			if (player.jailerName == null) {
				player.dialogue(new NPCDialogue(npc, "I love my job."));
				return;
			}
			if (player.jailOresCollected >= player.jailOresAssigned) {
				player.dialogue(new NPCDialogue(npc, "Your sentence is up, you will be able to leave shortly."));
				return;
			}
			player.dialogue(
				new NPCDialogue(npc, "You were sentenced to " + player.jailOresAssigned + " rocks by " + player.jailerName + "."),
				new NPCDialogue(npc, "Rocks Remaining: " + (player.jailOresAssigned - player.jailOresCollected))
			);
		});

		ItemNPCAction.register(guard, (player, item, npc) -> {
			if (player.jailOresAssigned > 0) {
				if (item.getId() == ROCK) {
					int count = 0;
					for (Item i : player.getInventory().getItems()) {
						if (i != null && i.getId() == item.getId()) {
							count++;
							i.remove();
						}
					}
					player.jailOresCollected += count;
					player.dialogue(new ItemDialogue().one(ROCK, "You turn in " + count + " " + (count == 1 ? "rock" : "rocks") + "."));
					return;
				}
			}
			player.sendMessage("Nothing interesting happens.");
		});

		// Register mining rocks
		ObjectAction.register(2704, "Mine", (player, obj) -> mine(player));
		ObjectAction.register(2704, "Prospect", (player, obj) ->
			player.sendMessage("It's a rock."));
	}
}
