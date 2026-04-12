package io.ruin.model.activities.wilderness;

import io.ruin.HooksV2.Result;
import io.ruin.HooksV2;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.Player.Hook;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.var.VarPlayerRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.ExtensionMethod;
import player.attributes.PlayerAttributesRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.google.gson.annotations.Expose;

@ExtensionMethod(EscapeCaves.Attributes.class)
public class EscapeCaves {

	public static class Attributes {
		public static void register() {
			PlayerAttributesRegistry.registerPersistent(EscapeCave.class, EscapeCave::new);
		}

		public static EscapeCave cave(Player player) {
			return PlayerAttributesRegistry.get(player.getIndex(), EscapeCave.class);
		}
	}

	@Data
	static class EscapeCave {
		@Expose
		public Set<Position> disabledEntrances = new HashSet<>();
		@Expose
		public long lastCaveDisabledMs = 0L;
	}

	static final CaveEntrance[] ENTRANCES = new CaveEntrance[] {
			new CaveEntrance(new Position(3321, 3829), new GameObject(47175, 10, new Position(3320, 3830))),
			new CaveEntrance(new Position(3261, 3831), new GameObject(47175, 10, new Position(3260, 3832))),
			new CaveEntrance(new Position(3283, 3773), new GameObject(47175, 10, new Position(3282, 3774))),
			new CaveEntrance(new Position(3285, 3806), new GameObject(47175, 10, new Position(3284, 3807))),
	};

	@Data
	static class CaveEntrance {
		// The position that exists the cave
		public final Position exitPosition;
		// The object id to exit.
		public final GameObject entranceObj;
	}

	@AllArgsConstructor
	private static enum CaveExit {
		CENTER(new Position(3361, 10273, 0), 47149),
		NORTH_WEST(new Position(3336, 10287, 0), 47148),
		NORT_EAST(new Position(3382, 10287, 0), 47148),
		SOUTH(new Position(3358, 10244, 0), 47147),
		;

		// The position that exists the cave
		public final Position exitPosition;
		// The object id to exit.
		public final int exitObj;
	}

	public static ArrayList<Player> players = new ArrayList<>(1000);

	private static final Bounds ESCAPE_CAVE_BOUNDS = new Bounds(3329, 10244, 3395, 10299, 0);

	private static boolean checkActive(Player player) {
		if (player == null) {
			return false;
		}

		if (player.singlesPlus) {
			return true;
		}
		player.singlesPlus = true;
		player.attackPlayerListener = EscapeCaves::allowAttack;
		players.add(player);
		return true;
	}

	private static void exitCave(Player player, GameObject obj) {
		var cave = Arrays.stream(CaveExit.values()).filter(it -> {
			return it.exitPosition.equals(obj.pos()) && it.exitObj == obj.getId();
		}).findFirst().orElse(null);
		if (cave == null) {
			player.sendMessage("Unknown cave.");
			return;
		}

		var availableEntrances = new ArrayList<>(Arrays.asList(ENTRANCES));
		availableEntrances.removeIf(it -> player.cave().disabledEntrances.contains(it.entranceObj.pos()));
		if (availableEntrances.isEmpty()) {
			player.sendMessage("All of the entrances collapsed, you cannot exit now.");
			return;
		}

		var exit = io.ruin.utility.Random.get(availableEntrances);
		disableCave(player, exit);
		player.cave().disabledEntrances.add(exit.entranceObj.pos());

		player.getMovement().teleport(exit.exitPosition);
		player.resetAnimation();
		player.sendFilteredMessage("You exit the escape cave from the " + cave.name().toLowerCase().replace("_", " "));
	}

	private static void disableCave(Player player, CaveEntrance cave) {
		player.cave().disabledEntrances.add(cave.entranceObj.pos());
		player.cave().lastCaveDisabledMs = System.currentTimeMillis();
	}

	private static boolean allowAttack(Player player, Player pTarget, boolean message) {
		if (Math.abs(player.getCombat().getLevel() - pTarget.getCombat().getLevel()) > player.wildernessLevel) {
			if (message)
				player.sendMessage("You can't attack " + pTarget.getName() + " - your level difference is too great.");
			return false;
		}
		if (!pTarget.inMulti()) {
			int last = pTarget.getCombat().lastPlayerAttacked;
			if (last != -1 && last != player.getUserId() && pTarget.getCombat().isAttacking(15)) {
				if (message)
					player.sendMessage(pTarget.getName() + " is fighting another player.");
				return false;
			}
		}
		return true;
	}

	public static void register() {
		Attributes.register();

		Player.hooks.register(Player.Hook.OnStart.class, ctx -> {
			EscapeCavesProcess.start(ctx.player());
			return Result.Pass;
		});

		ObjectAction.register(47175, 1, (player, obj) -> player.startEvent(event -> {
			if (player.cave().disabledEntrances.contains(obj.pos())) {
				player.sendMessage("This cave seems collapsed.");
				return;
			}

			player.resetAnimation();
			Random random = new Random();
			int randomIndex = random.nextInt(3);
			switch (randomIndex) {
				case 0:
					player.getMovement().teleport(3337, 10287, 0);
					player.sendFilteredMessage("You enter into one of the escape caves.");
					break;
				case 1:
					player.getMovement().teleport(3382, 10287, 0);
					player.sendFilteredMessage("You enter into one of the escape caves.");
					break;
				case 2:
					player.getMovement().teleport(3359, 10245, 0);
					player.sendFilteredMessage("You enter into one of the escape caves.");
					break;
				default:
					// Handle any unexpected case (though it's unlikely to happen)
					player.sendFilteredMessage("Error. How did you get here?");
					break;
			}
		}));

		/**
		 * Center entrance/exit
		 */
		ObjectAction.register(47149, 1, (player, obj) -> player.startEvent(event -> {
			exitCave(player, obj);

		}));

		/**
		 * Northeast entrance/exit
		 */
		ObjectAction.register(47148, 1, (player, obj) -> player.startEvent(event -> {
			exitCave(player, obj);
		}));

		/**
		 * Northwest entrance/exit
		 */
		ObjectAction.register(47148, 1, (player, obj) -> player.startEvent(event -> {
			exitCave(player, obj);
		}));

		/**
		 * South entrance/exit
		 */
		ObjectAction.register(47147, 1, (player, obj) -> player.startEvent(event -> {
			exitCave(player, obj);
		}));

		/**
		 * Vet'ion
		 */
		ObjectAction.register(46995, 1, (player, obj) -> player.startEvent(event -> {
			player.resetAnimation();
			player.getMovement().teleport(3295, 10191, 1);
			player.sendFilteredMessage("You emerge into Vetion's Rest.");
		}));
		ObjectAction.register(46925, 1, (player, obj) -> player.startEvent(event -> {
			player.resetAnimation();
			Random random = new Random();
			int randomIndex = random.nextInt(3); // Generate a random index between 0 and 2
			switch (randomIndex) {
				case 0:
					player.getMovement().teleport(3363, 10298, 0);
					player.sendFilteredMessage("You exit Vetion's Rest into one of the escape caves.");
					break;
				case 1:
					player.getMovement().teleport(3377, 10255, 0);
					player.sendFilteredMessage("You exit Vetion's Rest into one of the escape caves.");
					break;
				case 2:
					player.getMovement().teleport(3340, 10254, 0);
					player.sendFilteredMessage("You exit Vetion's Rest into one of the escape caves.");
					break;
				default:
					// Handle any unexpected case (though it's unlikely to happen)
					player.sendFilteredMessage("Error. How did you get here?");
					break;
			}
		}));

		/**
		 * Venenatis
		 */
		ObjectAction.register(47077, 1, (player, obj) -> player.startEvent(event -> {
			player.resetAnimation();
			player.getMovement().teleport(3423, 10184, 2);
			player.sendFilteredMessage("You emerge into the Silk Chasm.");
		}));
		ObjectAction.register(47000, 1, (player, obj) -> player.startEvent(event -> {
			player.resetAnimation();
			Random random = new Random();
			int randomIndex = random.nextInt(3); // Generate a random index between 0 and 2
			switch (randomIndex) {
				case 0:
					player.getMovement().teleport(3363, 10298, 0);
					player.sendFilteredMessage("You exit the Silk Chasm into one of the escape caves.");
					break;
				case 1:
					player.getMovement().teleport(3377, 10255, 0);
					player.sendFilteredMessage("You exit the Silk Chasm into one of the escape caves.");
					break;
				case 2:
					player.getMovement().teleport(3340, 10254, 0);
					player.sendFilteredMessage("You exit the Silk Chasm into one of the escape caves.");
					break;
				default:
					// Handle any unexpected case (though it's unlikely to happen)
					player.sendFilteredMessage("Error. How did you get here?");
					break;
			}
		}));

		/**
		 * Callisto
		 */
		ObjectAction.register(47140, 1, (player, obj) -> player.startEvent(event -> {
			player.resetAnimation();
			player.getMovement().teleport(3359, 10316, 0);
			player.sendFilteredMessage("You emerge into Callisto's Den.");
		}));
		ObjectAction.register(47122, 1, (player, obj) -> player.startEvent(event -> {
			player.resetAnimation();
			Random random = new Random();
			int randomIndex = random.nextInt(3); // Generate a random index between 0 and 2
			switch (randomIndex) {
				case 0:
					player.getMovement().teleport(3363, 10298, 0);
					player.sendFilteredMessage("You exit Callisto's Den into one of the escape caves.");
					break;
				case 1:
					player.getMovement().teleport(3377, 10255, 0);
					player.sendFilteredMessage("You exit Callisto's Den into one of the escape caves.");
					break;
				case 2:
					player.getMovement().teleport(3340, 10254, 0);
					player.sendFilteredMessage("You exit Callisto's Den into one of the escape caves.");
					break;
				default:
					// Handle any unexpected case (though it's unlikely to happen)
					player.sendFilteredMessage("Error. How did you get here?");
					break;
			}
		}));

		/**
		 * Register listener
		 */
		MapListener.registerBounds(ESCAPE_CAVE_BOUNDS).onEnter(player -> {
			VarPlayerRepository.SINGLES_PLUS.set(player, 1);
			checkActive(player);
		}).onExit((player, logout) -> {
			VarPlayerRepository.SINGLES_PLUS.set(player, 0);
		});
	}
}
