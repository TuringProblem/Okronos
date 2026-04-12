package mokhaiotl.loc;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import mokhaiotl.area.MokhaiotlArena;
import mokhaiotl.area.impl.MokhaiotlDungeonThree;
import mokhaiotl.area.impl.MokhaiotlDungeonTwo;
import mokhaiotl.inter.Investigation;

import java.util.Objects;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-28
*/
public class BurrowHole {

	private final static int BURROW_HOLE_ID = 57285;

	public static void registerBurrowHole() {
		ObjectAction.register(BURROW_HOLE_ID, "investigate", (player, _) ->
			Investigation.openInterfaceForPlayer(player));

		ObjectAction.register(BURROW_HOLE_ID, "descend", BurrowHole::jumpIntoBurrowHole);
	}


	private static MokhaiotlArena getArena(Player player) {
		var delveLevel = player.get("MOKHAIOTL_DELVE_LEVEL", 1);
		return delveLevel >= 6 ?
			new MokhaiotlDungeonThree(player) :
			new MokhaiotlDungeonTwo(player);
	}

	public static void jumpIntoBurrowHole(Player player, GameObject burrow) {
		player.set("MOKHAIOTL_DELVE_LEVEL", player.get("MOKHAIOTL_DELVE_LEVEL", 1) + 1);

		player.lock();
		player.animate(6132);
		player.getPacketSender().fadeOut();
		player.privateSound(2462, 1, 25);
		if (Objects.nonNull(burrow))
			player.getRouteFinder().routeAbsolute(burrow.getCenterPosition());
		player.startEvent(event -> {
			event.delay(2);
			var instance = getArena(player);
				instance.movePlayerToInstance(player);
			player.getPacketSender().fadeIn();
			event.delay(1);
			var delveLevel = ((int) player.get("MOKHAIOTL_DELVE_LEVEL"));
			player.sendMessage(Color.RED, "Delve level: %s"
				.formatted(delveLevel > 8 ?
					"8+ (%d)".formatted(delveLevel) :
					String.valueOf(delveLevel)
				)
			);
			player.startDelveTimer();
			player.unlock();
		});
	}
}
