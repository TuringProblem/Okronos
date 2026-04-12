package io.ruin.model.skills.events;


import io.ruin.Server;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.questtab.QuestTabEntry;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.skills.woodcutting.Hatchet;
import io.ruin.model.stat.StatType;
import io.ruin.services.discord.Discord;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CrystalTrees {

	private static boolean DISABLED = true;
	public static boolean ALIVE = false;
	private static long spawnTicks = 0;
	private static final int TREE_NF = 34918;
	private static final int TREE_HALF = 34915;
	private static final int TREE_NE = 34913;
	private static int TREE_CLARITY = 2500;
	public static CrystalTrees ACTIVE;
	private static long timeRemaining;
	private static GameObject tree;
	private static NPC npc;
	public static ArrayList<Player> players = new ArrayList<>(500);
	public static int TREE_CURRENCY = 23962;

	public static final CrystalTrees[] SPAWNS = {
		new CrystalTrees(new Position(2999, 3383, 0)),
		new CrystalTrees(new Position(2776, 3456, 0)),
		new CrystalTrees(new Position(2484, 3071, 0)),
		new CrystalTrees(new Position(3221, 6126, 0)), //prif 1
		new CrystalTrees(new Position(3316, 6080, 0)), //prif 2
		new CrystalTrees(new Position(3287, 6179, 0)), //prif 3
		new CrystalTrees(new Position(3226, 6030, 0)) //prif 4
	};

	private static String getLocation() {
		if (ACTIVE.treeSpawn.equals(2999, 3383)) {
			return "in Falador park";
		}
		if (ACTIVE.treeSpawn.equals(2776, 3456)) {
			return "Near Beehive";
		}
		if (ACTIVE.treeSpawn.equals(2484, 3071)) {
			return "Near Castle Wars";
		}
		if (ACTIVE.treeSpawn.equals(3221, 6126) || ACTIVE.treeSpawn.equals(3316, 6080)
			|| ACTIVE.treeSpawn.equals(3287, 6179) || ACTIVE.treeSpawn.equals(3226, 6030)) {
			return "Somewhere in Reason";
		}
		return "Uknown";
	}

	;

	/**
	 * Separator
	 */

	public final Position treeSpawn;

	public CrystalTrees(Position treeSpawn) {
		this.treeSpawn = treeSpawn;
	}

	public static void register() {
		/*
		 * Event
		 */
		if (!DISABLED) {
			/* event runs every 4 Hours */
			World.startEvent(e -> {
				while (true) {
					spawnTicks = Server.getEnd(60 * 100); // 1 Hour
					CrystalTrees next = Random.get(SPAWNS);
					if (next == ACTIVE) {
						e.delay(1);
						continue;
					}
					ACTIVE = next;
					String eventMessage = "There's been earth quakes around " + getLocation() + "!";
					broadcastEvent(eventMessage);
					addTree();
					ALIVE = true;
					/* stop event after 15 minutes */
					timeRemaining = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30);


					e.delay(30 * 100);
					removeTree(false);
					ALIVE = false;
					/*delay 1 hour before starting event */
					e.delay(60 * 100);
				}
			});

			/*
			 * Boulder
			 */
			ObjectAction.register(TREE_NF, 1, (player, obj) -> cut(player));
			ObjectAction.register(TREE_NF, 2, (player, obj) -> inspect(player));

			ObjectAction.register(TREE_HALF, 1, (player, obj) -> cut(player));
			ObjectAction.register(TREE_HALF, 2, (player, obj) -> inspect(player));

			ObjectAction.register(TREE_NE, 1, (player, obj) -> cut(player));
			ObjectAction.register(TREE_NE, 2, (player, obj) -> inspect(player));

		}
	}

	private static void broadcastEvent(String eventMessage) {
		for (Player p : World.players()) {
			p.getPacketSender().sendBroadcast(eventMessage);
		}
	}

	private static void addTree() {
		tree = GameObject.spawn(TREE_NF, ACTIVE.treeSpawn.getX(), ACTIVE.treeSpawn.getY(), 0, 10, 0);
		TREE_CLARITY = 2500;
	}

	public static void addTree(int x, int y, int z) {
		tree = GameObject.spawn(TREE_NF, x, y, z, 10, 0);
		TREE_CLARITY = 2500;
	}

	private static void attackingRoots(Player player, Position position) {
		World.startEvent(event -> {
			event.delay(1);
			for (Player p : player.localPlayers()) {
				if (p.getPosition().equals(position)) {
					p.sendFilteredMessage(Color.DARK_RED.wrap("The ground starts to shake"));
					event.delay(4);
					World.sendGraphics(179, 96, 0, position);
					event.delay(2);
					if (p.getPosition().equals(position)) {
						p.hit(new Hit().randDamage(10, 30));
						p.resetAnimation();
						p.sendFilteredMessage(Color.DARK_RED.wrap("Roots come from the ground, you're injured as the thorns enter your skin!"));
					} else {
						p.sendFilteredMessage(Color.DARK_RED.wrap("You move just in time to prevent the rooms from stinging you!"));
					}
				}
			}
		});
	}

	private static void removeTree(boolean success) {
		if (tree != null) {
			tree.setId(-1);
			tree = null;
			if (success) {
				String successMessage = "The Crystal Tree at " + getLocation() + " has been depleted!";
				broadcastEvent(successMessage);
			} else {
				String failedMessage = "The Crystal Tree at " + getLocation() + " wasn't harvested by Players, so the Elves did!";
				broadcastEvent(failedMessage);
			}
		}
	}

	private static void removeShards(int amt) {
		TREE_CLARITY -= amt;
//        if(TREE_CLARITY > 1000 && TREE_CLARITY <= 1500) {
//            tree.setId(TREE_HALF);
//        }
//
//        if(TREE_CLARITY >= 100 && TREE_CLARITY <= 500) {
//            tree.setId(TREE_NE);
//        }

		if (TREE_CLARITY <= 0)
			TREE_CLARITY = 0;
	}

	private static String boulderColor() {
		if (TREE_CLARITY > 1500)
			return "<col=00FF00>";
		else if (TREE_CLARITY > 500)
			return "<col=ffff00>";
		return "<col=FF0000>";
	}


	private static void inspect(Player player) {
		player.dialogue(new MessageDialogue("The tree looks like it has " + TREE_CLARITY + " x shards left in it."));
	}

	private static void cut(Player player) {
		player.startEvent(event -> {
			Hatchet hatchet = Hatchet.find(player);
			if (hatchet == null) {
				player.sendMessage("You do not have an axe which you have the woodcutting level to use.");
				player.privateSound(2277);
				return;
			}
			if (player.getInventory().isFull() && !player.getInventory().hasId(TREE_CURRENCY)) {
				player.dialogue(new MessageDialogue("Your inventory is too full to do this."));
				return;
			}
			player.animate(hatchet.animationId);
			player.sendFilteredMessage("You swing your axe at the tree.");
			event.delay(Random.get(3, 8));
			while (true) {
				if (player.getInventory().isFull() && !player.getInventory().hasId(TREE_CURRENCY)) {
					player.dialogue(new MessageDialogue("You can't hold anymore " + ObjType.get(TREE_CURRENCY).name.toLowerCase() + " in your inventory."));
					return;
				}
				int random = Random.get(1, 2);
				player.animate(hatchet.animationId);
				player.getInventory().add(TREE_CURRENCY, random);
				player.getStats().addXp(StatType.Woodcutting, 30, true);
				removeShards(random);
				player.sendFilteredMessage("You cut " + random + " " + ObjType.get(TREE_CURRENCY).name.toLowerCase() + ".");
				if (TREE_CLARITY <= 0) {
					removeTree(true);
					player.resetAnimation();
					break;
				}
				if (tree == null) {
					player.resetAnimation();
					break;
				}
				event.delay(Random.get(3, 8));
			}
		});
	}

	private static String timeRemaining(long ms) {
		long hours = TimeUnit.MILLISECONDS.toHours(ms);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(ms);
		if (ms < 0)
			return "None!";
		return String.format(
			"%02d:%02d",
			minutes - TimeUnit.HOURS.toMinutes(hours),
			TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(minutes)
		);
	}

	/**
	 * Entry
	 */
	public static final class Entry extends QuestTabEntry {

		public static final Entry INSTANCE = new Entry();

		@Override
		public void send(Player player) {
			int minsLeft = (int) ((spawnTicks - Server.currentTick()) / 100);
			int mins = minsLeft - 60;
			if (minsLeft == 0) {
				send(player, "Crystal tree", getLocation(), Color.GREEN);
			} else {
				send(player, "Crystal tree in " + minsLeft + " M.", Color.RED);
			}
		}

		@Override
		public void select(Player player) {
			player.sendMessage("The Crystal tree will be near " + getLocation() + ".");
		}
	}

}
