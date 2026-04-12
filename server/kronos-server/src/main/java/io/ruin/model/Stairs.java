package io.ruin.model;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;

public class Stairs {

	private static final String UP = "Climb-up";
	private static final String DOWN = "Climb-down";
	private static final int[] SPIRAL_STAIR_IDS = {9582, 9584, 11511, 11789, 11790, 11888, 11889, 11890, 12536, 12537, 12538, 16671, 16672, 16672, 16673, 16675, 16677, 17143, 17155, 24072, 24074, 24075, 24076, 24303, 25801, 25935, 34502, 34503, 34864, 34865, 34924};
	private static final int[] LONG_STAIR_IDS = {11796, 11797, 11798, 11799, 11800, 11807, 34923, 35791, 35792};

	public static void register() {
		ObjectAction.register(23562, 2578, 9584, 0, UP, (player, obj) -> player.getMovement().teleport(2570, 9525, 0));
		ObjectAction.register(15656, 2620, 9497, 0, DOWN, (player, obj) -> player.getMovement().teleport(2620, 9565, 0));
		ObjectAction.register(15657, 2620, 9562, 0, UP, (player, obj) -> player.getMovement().teleport(2620, 9496, 0));
		ObjectAction.register(23563, 2616, 9572, 0, UP, (player, obj) -> player.getMovement().teleport(2614, 9505, 0));
		ObjectAction.register(23564, 2615, 9504, 0, DOWN, (player, obj) -> player.getMovement().teleport(2616, 9571, 0));
		ObjectAction.register(11800, 3187, 3433, 0, DOWN, (player, obj) -> player.getMovement().teleport(3190, 9833, 0));
		ObjectAction.register(11805, 3187, 9833, 0, UP, (player, obj) -> player.getMovement().teleport(3186, 3433, 0));
		for (int spiralStairId : SPIRAL_STAIR_IDS) {
			ObjectAction.register(spiralStairId, "Climb-up", Stairs::climbUpSpiral);
			ObjectAction.register(spiralStairId, "Climb-down", Stairs::climbDownSpiral);
			ObjectAction.register(spiralStairId, "Climb", (player, obj) -> player.dialogue(new OptionsDialogue(
				new Option("Climb-up", () -> climbUpSpiral(player, obj)),
				new Option("Climb-down", () -> climbDownSpiral(player, obj))
			)));
		}
		//Stairs around Varrock h etc.
		for (int longStairId : LONG_STAIR_IDS) {
			ObjectAction.register(longStairId, "Climb-up", Stairs::climbUpLong);
			ObjectAction.register(longStairId, "Climb-down", Stairs::climbDownLong);
		}
	}

	private static void climbUpSpiral(Player player, GameObject obj) {
		int up = obj.getPosition().getZ() + 1;
		int newX = 0;
		int newY = 0;
		switch (obj.getDirection()) {
			case 0:
				newX = 2;
				newY = 0;
				break;
			case 1:
				newX = 0;
				newY = -1;
				break;
			case 2:
				newY = 1;
				newX = -1;
				break;
			case 3:
				newY = 2;
				newX = 1;
				break;
		}
		player.getMovement().teleport(obj.getPosition().getX() + newX, obj.getPosition().getY() + newY, up);
	}

	private static void climbUpLong(Player player, GameObject obj) {
		int up = obj.getPosition().getZ() + 1;
		int newX = 0;
		int newY = 0;
		switch (obj.getDirection()) {
			case 0:
				newY = 4;
				break;
			case 3: //having 0 still works, just not as good.
			case 1:
				newX = 4; //should be -4?
				break;
			case 2:
				newX = 0;
				newY = -1;
				break;
		}
		if ((newX == 0 && newY == 0) || (newX == 4 || newY == 4))
			player.getMovement().teleport(player.getPosition().getX() + newX, player.getPosition().getY() + newY, up);
		else
			player.getMovement().teleport(obj.getPosition().getX() + newX, obj.getPosition().getY() + newY, up);
	}

	private static void climbDownLong(Player player, GameObject obj) {
		int down = obj.getPosition().getZ() - 1;
		int newX = 0;
		int newY = 0;
		switch (obj.getDirection()) {
			case 0:
				newY = -4;
				break;
			case 3: //having 0 still works, just not as good.
			case 1:
				newX = -4; //should be -4?
				break;
			case 2:
				newY = 3;
				break;
		}
		if ((newX == 0 && newY == 0) || (newX == -4 || newY == -4))
			player.getMovement().teleport(player.getPosition().getX() + newX, player.getPosition().getY() + newY, down);
		else
			player.getMovement().teleport(obj.getPosition().getX() + newX, obj.getPosition().getY() + newY, down);
	}


	private static void climbDownSpiral(Player player, GameObject obj) {
		int down = obj.getPosition().getZ() - 1;
		int newX = 0;
		int newY = 0;
       /* switch(obj.direction){
            case 0:
                newX = 2;
                newY = 0;
                break;
            case 1:
                newX = 0;
                newY = -1;
                break;
            case 2:
                newY = 1;
                newX = -1;
                break;
            case 3:
                newY = 2;
                newX = 1;
                break;
        }*/
		player.getMovement().teleport(obj.getPosition().getX() + newX, obj.getPosition().getY() + newY, down);
	}
}
