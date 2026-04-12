package io.ruin.model.map.object.actions.impl.dungeons;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.map.object.actions.impl.Ladder;
import io.ruin.model.stat.StatType;

public class FremennikSlayerDungeon {

	public static void register() {

		/**s
		 * Entrance/exit
		 */
		ObjectAction.register(2123, 1, (player, obj) -> player.getMovement().teleport(2808, 10002, 0));
		ObjectAction.register(2141, 1, (player, obj) -> player.getMovement().teleport(2795, 3615, 0));

		ObjectAction.register(29993, 1, (player, obj) -> {
			if (!player.getStats().check(StatType.Agility, 81, "use this shortcut"))
				return;
			var dir = player.getAbsY() - obj.y < 0 ? Direction.NORTH : Direction.SOUTH;
			if (dir == Direction.SOUTH) {
				if (player.task == null || !player.task.getTaskName().equalsIgnoreCase("kurask")) {
					player.sendMessage("You need to be on task to enter this area.");
					return;
				}
			}
			player.startEvent(e -> {
				player.lock();
				player.getMovement().force(0, dir == Direction.NORTH ? 2 : -2, 0, 0, 15, 30, dir);
				e.delay(1);
				player.unlock();
			});
		});


//        ObjectAction.register(29993, 2703, 9990, 0, "climb steps", (player, obj) -> squeezeThroughCrack(player, obj, new Position(2703, 9989, 0), 62)); // crack
//        ObjectAction.register(29993, 2703, 9989, 0, "climb steps", (player, obj) -> squeezeThroughCrack(player, obj, new Position(2703, 9990, 0), 62)); // crack


		/**
		 * Strange floor
		 */
		ObjectAction.register(16544, 2774, 10003, 0, "jump-over", (player, obj) -> player.startEvent(event -> {
			if (!player.getStats().check(StatType.Agility, 81, "use this shortcut"))
				return;
			Direction dir = player.getAbsX() - obj.x > 0 ? Direction.WEST : Direction.EAST;
			player.lock();
			player.animate(741);
			player.getMovement().force(dir == Direction.WEST ? -2 : 2, 0, 0, 0, 15, 30, dir);
			event.delay(1);
			player.unlock();
		}));

		ObjectAction.register(16544, 2769, 10002, 0, "jump-over", (player, obj) -> player.startEvent(event -> {
			if (!player.getStats().check(StatType.Agility, 81, "use this shortcut"))
				return;
			Direction dir = player.getAbsX() - obj.x > 0 ? Direction.WEST : Direction.EAST;
			player.lock();
			player.animate(741);
			player.getMovement().force(dir == Direction.WEST ? -2 : 2, 0, 0, 0, 15, 30, dir);
			event.delay(1);
			player.unlock();
		}));

		ObjectAction.register(16539, 2734, 10008, 0, "squeeze-through", (player, obj) -> squeezeThroughCrack(player, obj, new Position(2730, 10008, 0), 62)); // crack
		ObjectAction.register(16539, 2731, 10008, 0, "squeeze-through", (player, obj) -> squeezeThroughCrack(player, obj, new Position(2735, 10008, 0), 62)); // crack


	}

	private static void squeezeThroughCrack(Player player, GameObject crack, Position destination, int levelReq) {
		if (!player.getStats().check(StatType.Agility, levelReq, "use this shortcut"))
			return;
		player.lock();
		player.startEvent(event -> {
			player.face(crack);
			player.animate(746);
			event.delay(1);
			player.getMovement().teleport(destination);
			player.animate(748);
			event.delay(1);
			player.unlock();
		});
	}
}
