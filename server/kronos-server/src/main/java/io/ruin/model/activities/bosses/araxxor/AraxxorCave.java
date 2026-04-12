package io.ruin.model.activities.bosses.araxxor;

import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.map.object.actions.impl.dungeons.StrongholdSecurity;

public class AraxxorCave {

	private static void crawlThroughTunnel(Player player, boolean exit) {
		crawlToPosition(player, new Position(exit ? 3657 : 3679, exit ? 3407 : 9797, 0));
	}

	private static void crawlToPosition(Player player, Position pos) {
		player.startEvent(e -> {
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.animate(2796);
			e.delay(2);
			player.getMovement().teleport(pos);
			player.animate(-1);
			player.unlock();
		});
	}

	private static void useTunnel(Player player, GameObject obj) {
		if(obj.x == 3693 && obj.y == 9836)
			crawlToPosition(player, new Position(3689, 9837, 0));
		else if(obj.x == 3690 && obj.y == 9836)
			crawlToPosition(player, new Position(3696, 9837, 0));
		else if(obj.x == 3681 && obj.y == 9819)
			crawlToPosition(player, new Position(3677, 9820, 0));
		else if(obj.x == 3678 && obj.y == 9819)
			crawlToPosition(player, new Position(3684, 9819, 0));
		else if(obj.x == 3677 && obj.y == 9843)
			crawlToPosition(player, new Position(3679, 9849, 0));
		else if(obj.x == 3677 && obj.y == 9846)
			crawlToPosition(player, new Position(3679, 9842, 0));
	}

	public static void register() {
		ObjectAction.register(42594, "enter", (player, obj) -> crawlThroughTunnel(player, false));
		ObjectAction.register(42595, "exit", (player, obj) -> crawlThroughTunnel(player, true));
		ObjectAction.register(54274, 1, (player, obj) -> crawlToPosition(player, new Position(3658, 9816, 0)));
		ObjectAction.register(54272, 1, AraxxorCave::useTunnel);
		ObjectAction.register(54271, 1, AraxxorCave::useTunnel);
		ObjectAction.register(54273, 1, AraxxorCave::useTunnel);
		ObjectAction.register(54161, 1, (player, obj) -> {
			player.getInstanceTokenInterface().selectedBoss = InstanceMaps.ARAXXOR;
			player.getInstanceTokenInterface().startInstance(player, true);
		});
	}
}
