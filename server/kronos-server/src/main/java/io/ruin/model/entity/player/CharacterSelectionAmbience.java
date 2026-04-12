package io.ruin.model.entity.player;

import io.ruin.model.map.Position;
import io.ruin.model.var.VarPlayerRepository;

public class CharacterSelectionAmbience {

	private static Position[] locations = new Position[]{
		new Position(2016, 3677 - 20, 0), //HOME
		new Position(3264, 6082 - 20, 0), //PRIFDDINAS
		new Position(3367, 3268 - 20, 0), //DUEL ARENA
	};

	public static void init(Player player) {

		// player.getPacketSender().setZoom(128);
		player.getPacketSender().sendVarp(168, 4);
		player.getPacketSender().sendVarp(169, 4);
		player.getPacketSender().sendVarp(872, 4);

		player.startEvent((event -> {
			// while(player.isSelectingCharacter()){
			Position first = new Position(1312, 5190, 1);//Random.get(locations);

			player.getMovement().teleport(first);

			event.delay(1);
			//Config.LOCK_CAMERA.set(player, 1);
			//player.getPacketSender().moveCameraToLocation(player.getAbsX(), player.getAbsY() + 30, 2500, 10, 0);
			//player.getPacketSender().turnCameraToLocation(player.getAbsX(), player.getAbsY() + 30, 400, 100, 100);
			event.delay(14);
			//  }

			player.getPacketSender().resetCamera();
			VarPlayerRepository.LOCK_CAMERA.set(player, 0);
		}));
	}
}
