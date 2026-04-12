package io.ruin.model.skills.events;

import io.ruin.Server;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.questtab.QuestTabEntry;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.skills.mining.Pickaxe;
import io.ruin.model.stat.StatType;
import io.ruin.services.discord.Discord;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ShootingStars {

	private static final Projectile LAVA_PROJECTILE = new Projectile(1839, 150, 0, 0, 100, 0, 45, 0);

	private static boolean DISABLED = false;

	private static long spawnTicks = 0;
	private static final int STAR_START = 41223;
	private static final int STAR_PROGRESS_1 = 41224;
	private static final int STAR_PROGRESS_2 = 41225;
	private static final int STAR_PROGRESS_3 = 41226;
	private static final int STAR_PROGRESS_4 = 41227;
	private static final int STAR_PROGRESS_5 = 41228;
	private static final int STAR_FINISH = 41229;
	private static int METEORITE_REMAINING = 1000;
	public static ShootingStars ACTIVE;
	private static long timeRemaining;
	private static GameObject rock;
	public static ArrayList<Player> players = new ArrayList<>(500);
	public static int STAR_CURRENCY = 25527;

	private static final ShootingStars[] SPAWNS = {
			new ShootingStars(new Position(3299, 3303, 0)),
			new ShootingStars(new Position(2830, 3200, 0)),
			new ShootingStars(new Position(3045, 3470, 0)),
			new ShootingStars(new Position(2572, 3411, 0)),
			new ShootingStars(new Position(3287, 3354, 0)), // Varrock 1
			new ShootingStars(new Position(3175, 3378, 0)), // Varrock 2
	};

	private static String getLocation() {
		if (ACTIVE.starSpawn.equals(3299, 3303)) {
			return "Alkharid mine";
		}
		if (ACTIVE.starSpawn.equals(2830, 3200)) {
			return "Karamja";
		}
		if (ACTIVE.starSpawn.equals(2572, 3411)) {
			return "Fishing Guild";
		}
		if (ACTIVE.starSpawn.equals(3045, 3470)) {
			return "Edgeville Mon.";
		}
		if (ACTIVE.starSpawn.equals(3287, 3354) || ACTIVE.starSpawn.equals(3175, 3378)) {
			return "Varrock mine";
		}
		return "Unknown";
	}

	;

	/**
	 * Separator
	 */

	public final Position starSpawn;

	public ShootingStars(Position starSpawn) {
		this.starSpawn = starSpawn;
	}

	public static void register() {
		/*
		 * Event
		 */
		if (!DISABLED) {
			/* event runs every 45 minutes */
			World.startEvent(e -> {
				while (true) {
					spawnTicks = Server.getEnd(90 * 100); // 4 Hours
					ShootingStars next = Random.get(SPAWNS);
					if (next == ACTIVE) {
						e.delay(1);
						continue;
					}
					ACTIVE = next;
					String eventMessage =
							"There's been a sighting of a star around " + getLocation() + ", type ::star to get there!";
					broadcastEvent(eventMessage);
					addStar();



					/* stop event after 15 minutes */
					timeRemaining = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15);
					e.delay(75 * 100);
					removeStar(false);
					/* delay 90 minutes before starting event */
					e.delay(90 * 100);
				}
			});

			/*
			 * Boulder
			 */
			ObjectAction.register(STAR_PROGRESS_1, 1, (player, obj) -> mine(player));
			ObjectAction.register(STAR_PROGRESS_1, 2, (player, obj) -> inspect(player));

			ObjectAction.register(STAR_START, 1, (player, obj) -> mine(player));
			ObjectAction.register(STAR_START, 2, (player, obj) -> inspect(player));

			ObjectAction.register(STAR_PROGRESS_2, 1, (player, obj) -> mine(player));
			ObjectAction.register(STAR_PROGRESS_2, 2, (player, obj) -> inspect(player));

			ObjectAction.register(STAR_PROGRESS_3, 1, (player, obj) -> mine(player));
			ObjectAction.register(STAR_PROGRESS_3, 2, (player, obj) -> inspect(player));

			ObjectAction.register(STAR_PROGRESS_4, 1, (player, obj) -> mine(player));
			ObjectAction.register(STAR_PROGRESS_4, 2, (player, obj) -> inspect(player));

			ObjectAction.register(STAR_PROGRESS_5, 1, (player, obj) -> mine(player));
			ObjectAction.register(STAR_PROGRESS_5, 2, (player, obj) -> inspect(player));

			ObjectAction.register(STAR_FINISH, 1, (player, obj) -> mine(player));
			ObjectAction.register(STAR_FINISH, 2, (player, obj) -> inspect(player));

		}
	}

	private static void broadcastEvent(String eventMessage) {
		for (Player p : World.players()) {
			p.getPacketSender().sendBroadcast(eventMessage);
		}
	}

	private static void addStar() {
		rock = GameObject.spawn(STAR_START, ACTIVE.starSpawn.getX(), ACTIVE.starSpawn.getY(), 0, 10, 0);
		METEORITE_REMAINING = 16000;
	}

	public static void addStar(int x, int y, int z) {
		rock = GameObject.spawn(STAR_START, x, y, z, 10, 0);
		METEORITE_REMAINING = 2200;
	}


	private static void attackingLava(Player player, Position position) {
		Position source = ACTIVE.starSpawn;
		Position pos = player.getPosition().copy();
		World.startEvent(event -> {
			event.delay(1);
			for (Player p : player.localPlayers()) {
				int delay = LAVA_PROJECTILE.send(source.getX(), source.getY(), pos.getX(), pos.getY());
				if (p.getPosition().equals(position)) {
					p.sendFilteredMessage(Color.DARK_RED.wrap("You hit a vein of molten lava within the meteorite, move!"));
					rock.graphics(delay);
					event.delay(delay);
					World.sendGraphics(1840, 0, delay, pos);
					if (p.getPosition().equals(position)) {
						p.hit(new Hit().randDamage(10, 30));
						p.resetAnimation();
						p.sendFilteredMessage(Color.DARK_RED.wrap("You're badly burned as the molten lava hits your skin."));
					} else {
						p.sendFilteredMessage(Color.DARK_RED.wrap("You move just in time to avoid the heat from the lava!"));
					}
				}
			}
		});
	}

	private static void removeStar(boolean success) {
		if (rock != null) {
			rock.setId(-1);
			rock = null;
			if (success) {
				String successMessage = "The shooting star has now been mined and has been removed from the world.";
				broadcastEvent(successMessage);
			} else {
				String failedMessage = "The shooting star has dissolved into the ground.";
				broadcastEvent(failedMessage);
			}
		}
	}

	private static void removeShards(int amt) {
		METEORITE_REMAINING -= amt;
		if (METEORITE_REMAINING <= 0)
			METEORITE_REMAINING = 0;
	}

	private static String boulderColor() {
		if (METEORITE_REMAINING > 300)
			return "<col=00FF00>";
		else if (METEORITE_REMAINING > 100)
			return "<col=ffff00>";
		return "<col=FF0000>";
	}


	private static void inspect(Player player) {
		player.dialogue(new MessageDialogue("The rock looks like it has " + METEORITE_REMAINING + " x fragments in it."));
	}

	private static double yieldBoost(Player player) {
		switch (player.getSecondaryGroup()) {
			case DONATOR -> {
				return 1.02;
			}
			case SUPER_DONATOR -> {
				return 1.04;
			}
			case ELITE_DONATOR -> {
				return 1.06;
			}
			case NOBLE_DONATOR -> {
				return 1.08;
			}
			case GOLD_DONATOR -> {
				return 1.1;
			}
			case PLATINUM_DONATOR -> {
				return 1.13;
			}
			case LEGENDARY_DONATOR -> {
				return 1.17;
			}
			case SUPREME_DONATOR -> {
				return 1.25;
			}
			default -> {
				return 1;
			}
		}
	}

	private static void mine(Player player) {
		player.startEvent(event -> {
			event.setCancelCondition(() -> {
				if (rock == null) {
					return true;
				}
				return player.getPosition().regionId() != rock.getPosition().regionId();
			});
			Pickaxe pickaxe = Pickaxe.find(player);
			if (pickaxe == null) {
				player.sendMessage("You do not have an pick-axe which you have the mining level to use.");
				player.privateSound(2277);
				return;
			}
			if (player.getInventory().isFull() && !player.getInventory().hasId(STAR_CURRENCY)) {
				player.dialogue(new MessageDialogue("Your inventory is too full to do this."));
				return;
			}
			player.animate(pickaxe.crystalAnimationID);
			player.sendFilteredMessage("You swing your pickaxe at the rock.");
			event.delay(Random.get(3, 8));
			var rockWhenMiningStarts = rock; // avoid NPEs
			while (true) {
				if (rockWhenMiningStarts.getId() == -1 || rock == null) { // rock finished
					player.resetAnimation();
					break;
				}
				if (player.getInventory().isFull() && !player.getInventory().hasId(STAR_CURRENCY)) {
					player.dialogue(new MessageDialogue(
							"You can't hold anymore " + ObjType.get(STAR_CURRENCY).name.toLowerCase() + " in your inventory."));
					return;
				}
				if (player.breakAction) {
					// If the player has moved, teleported, or forcefully moved by a teleport (Honour Guard)
					player.breakAction = false;
					player.resetAnimation();
					return;
				}
				int random = Random.get(1, 3);
				random = (int) (random * yieldBoost(player));
				player.animate(pickaxe.crystalAnimationID);
				player.getInventory().add(STAR_CURRENCY, random);
				player.getStats().addXp(StatType.Mining, 250, true);
				removeShards(random);
				player.sendFilteredMessage("You mine " + random + " " + ObjType.get(STAR_CURRENCY).name.toLowerCase() + ".");
				if (rock != null) {
					if (METEORITE_REMAINING > 11000 && METEORITE_REMAINING < 14000) {
						rock.setId(STAR_PROGRESS_1);
					}
					if (METEORITE_REMAINING >= 8000 && METEORITE_REMAINING < 11000) {
						rock.setId(STAR_PROGRESS_2);
					}
					if (METEORITE_REMAINING >= 6000 && METEORITE_REMAINING < 8000) {
						rock.setId(STAR_PROGRESS_3);
					}
					if (METEORITE_REMAINING >= 4000 && METEORITE_REMAINING < 6000) {
						rock.setId(STAR_PROGRESS_4);
					}
					if (METEORITE_REMAINING >= 2000 && METEORITE_REMAINING < 4000) {
						rock.setId(STAR_PROGRESS_5);
					}
					if (METEORITE_REMAINING >= 500 && METEORITE_REMAINING < 2000) {
						rock.setId(STAR_FINISH);
					}
					if (METEORITE_REMAINING <= 0) {
						removeStar(true);
						player.resetAnimation();
						break;
					}
				}
				if (rock == null) {
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
				TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(minutes));
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
				send(player, "Shooting star:", "<col=0fd60f>" + getLocation(), Color.GREEN);
			} else {
				send(player, "Shooting star:", "<col=FF0000>" + +minsLeft + " Minutes", Color.RED);
			}
		}


		@Override
		public void select(Player player) {
			player.sendMessage("The shooting star will be near... " + getLocation() + "");
		}

	}

}
