package io.ruin.model.activities.wilderness;

import io.ruin.model.World;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;

import java.util.ArrayList;

import static core.task.api.API.queue;
import static core.task.api.API.sleep;

public class RevCaves { // todo add the agility requirements for this (for the economy world)

	public static ArrayList<Player> players = new ArrayList<>(1000);

	private static final Bounds DUNGEON_BOUNDS = new Bounds(3138, 10050, 3261, 10237, -1);

	private static void jumpToPillar(Player player, int x, int y, Direction dir, int agilityReq) {
		if (player.getStats().get(StatType.Agility).currentLevel < agilityReq) {
			player.dialogue(
					new ItemDialogue().one(6514, "You need an agility level of " + agilityReq + " to jump across this pillar."));
			return;
		}
		World.startEvent(event -> {
			player.animate(741);
			player.getMovement().force(x, y, 0, 0, 5, 35, dir);
			event.delay(1);
		});
	}

	private static boolean checkActive(Player player) {
		if (player == null) {
			return false;
		}

		if (player.singlesPlus)
			return true;
		player.singlesPlus = true;
		player.attackPlayerListener = RevCaves::allowAttack;
		players.add(player);
		return true;
	}

	public static boolean allowNPCAttack(NPC source, Entity target) {
		if (target == null) {
			return false;
		}

		var player = target.player;

		// npcs can attack each other no matter what
		if (player == null) {
			return true;
		}

		if (!player.singlesPlus) {
			return true;
		}

		if (player.getCombat().isDefendingFromPlayer()) {
			return false;
		}

		return true;
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
		/**
		 * South entrance/exit
		 */
		ObjectAction.register(43868, 1, (player, obj) -> World.startEvent(event -> {
			// System.out.println("exit");
			player.getMovement().teleport(3124, 3805, 0);
			player.sendFilteredMessage("You exit the cave.");
		}));
		ObjectAction.register(31555, 3073, 3654, 0, "enter", (player, obj) -> World.startEvent(event -> {
			player.lock();
			player.animate(2796);
			event.delay(2);
			player.resetAnimation();
			player.getMovement().teleport(3197, 10056, 0);
			player.sendFilteredMessage("You enter the cave.");
			player.unlock();
		}));

		/**
		 * Center entrance/exit
		 */
		ObjectAction.register(40386, 3067, 3740, 0, 1, (player, obj) -> World.startEvent(event -> {
			player.lock();
			player.animate(2796);
			event.delay(2);
			player.resetAnimation();
			player.getMovement().teleport(3187, 10128, 0);
			player.sendFilteredMessage("You enter the cave.");
			player.unlock();
		}));

		/**
		 * North entrance/exit
		 */
		ObjectAction.register(31558, 1, (player, obj) -> World.startEvent(event -> {
			// System.out.println("exit");
			// TODO: PROPER EXIT LOCATIONS
			player.resetAnimation();
			player.getMovement().teleport(3102, 3655, 0);
			player.sendFilteredMessage("You exit the cave.");
		}));
		ObjectAction.register(31556, 3124, 3831, 0, "enter", (player, obj) -> World.startEvent(event -> {
			player.lock();
			player.animate(2796);
			event.delay(2);
			player.resetAnimation();
			player.getMovement().teleport(3241, 10233, 0);
			player.sendFilteredMessage("You enter the cave.");
			player.unlock();
		}));

		/**
		 * Register listener
		 */
		MapListener.registerBounds(DUNGEON_BOUNDS).onEnter(player -> {
			VarPlayerRepository.SINGLES_PLUS.set(player, 1);
			checkActive(player);
		})
				.onExit((player, logout) -> {
					VarPlayerRepository.SINGLES_PLUS.set(player, 0);
				});

		/**
		 * Southwest pillar
		 */
		ObjectAction.register(31561, 3200, 10136, 0, "jump-to", (player, obj) -> {
			var initialJump = player.getAbsX() == 3198 ? Direction.EAST : Direction.WEST;
			var offset = initialJump == Direction.EAST ? 2 : -2;
			player.lock(LockType.FULL_DELAY_DAMAGE);
			jumpToPillar(player, offset, 0, initialJump, 75);
			queue(() -> {
				sleep(2);
				jumpToPillar(player, offset, 0, initialJump, 75);
				sleep(1);
				player.unlock();
			});
		});
		GameObject southWestPillar = Tile.getObject(31561, 3200, 10136, 0);
		southWestPillar.skipReachCheck = p -> p.equals(3198, 10136) || p.equals(3202, 10136);
		Tile.get(3201, 10136, 0).flagUnmovable();
		Tile.get(3199, 10136, 0).flagUnmovable();

		/**
		 * South pillar
		 */
		ObjectAction.register(31561, 3220, 10086, 0, "jump-to", (player, obj) -> {
			var initialJump = player.getAbsY() == 10084 ? Direction.NORTH : Direction.SOUTH;
			var offset = initialJump == Direction.NORTH ? 2 : -2;
			player.lock(LockType.FULL_DELAY_DAMAGE);
			jumpToPillar(player, 0, offset, initialJump, 65);
			queue(() -> {
				sleep(2);
				jumpToPillar(player, 0, offset, initialJump, 65);
				sleep(1);
				player.unlock();
			});
		});
		GameObject southPillar = Tile.getObject(31561, 3220, 10086, 0);
		southPillar.skipReachCheck = p -> p.equals(3220, 10084) || p.equals(3220, 10088);
		Tile.get(3220, 10085, 0).flagUnmovable();
		Tile.get(3220, 10087, 0).flagUnmovable();

		/**
		 * Southeast pillar
		 */
		ObjectAction.register(31561, 3241, 10145, 0, "jump-to", (player, obj) -> {
			var initialJump = player.getAbsX() == 3239 ? Direction.EAST : Direction.WEST;
			var offset = initialJump == Direction.EAST ? 2 : -2;
			player.lock(LockType.FULL_DELAY_DAMAGE);
			jumpToPillar(player, offset, 0, initialJump, 89);
			queue(() -> {
				sleep(2);
				jumpToPillar(player, offset, 0, initialJump, 89);
				sleep(1);
				player.unlock();
			});
		});
		GameObject southEastPillar = Tile.getObject(31561, 3241, 10145, 0);
		southEastPillar.skipReachCheck = p -> p.equals(3243, 10145) || p.equals(3239, 10145);
		Tile.get(3240, 10145, 0).flagUnmovable();
		Tile.get(3242, 10145, 0).flagUnmovable();

		/**
		 * North pillar
		 */
		ObjectAction.register(31561, 3202, 10196, 0, "jump-to", (player, obj) -> {
			var initialJump = player.getAbsX() == 3200 ? Direction.EAST : Direction.WEST;
			var offset = initialJump == Direction.EAST ? 2 : -2;
			player.lock(LockType.FULL_DELAY_DAMAGE);
			jumpToPillar(player, offset, 0, initialJump, 75);
			queue(() -> {
				sleep(2);
				jumpToPillar(player, offset, 0, initialJump, 75);
				sleep(1);
				player.unlock();
			});
		});
		GameObject northPillar = Tile.getObject(31561, 3202, 10196, 0);
		northPillar.skipReachCheck = p -> p.equals(3204, 10196) || p.equals(3200, 10196);
		Tile.get(3201, 10196, 0).flagUnmovable();
		Tile.get(3203, 10196, 0).flagUnmovable();

		/**
		 * Northwest pillar
		 */
		ObjectAction.register(31561, 3180, 10209, 0, "jump-to", (player, obj) -> {
			var initialJump = player.getAbsY() == 10207 ? Direction.NORTH : Direction.SOUTH;
			var offset = initialJump == Direction.NORTH ? 2 : -2;
			player.lock(LockType.FULL_DELAY_DAMAGE);
			jumpToPillar(player, 0, offset, initialJump, 75);
			queue(() -> {
				sleep(2);
				jumpToPillar(player, 0, offset, initialJump, 75);
				sleep(1);
				player.unlock();
			});
		});
		GameObject northWestPillar = Tile.getObject(31561, 3180, 10209, 0);
		northWestPillar.skipReachCheck = p -> p.equals(3180, 10207) || p.equals(3180, 10211);
		Tile.get(3180, 10210, 0).flagUnmovable();
		Tile.get(3180, 10208, 0).flagUnmovable();
	}
}
