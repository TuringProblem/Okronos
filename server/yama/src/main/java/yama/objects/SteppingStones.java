package yama.objects;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;

public class SteppingStones {
	private static void jump(Player player, GameObject stone) {
		int baseY = player.getPosition().getRegion().baseY;
		int y = player.getPosition().getY() - baseY;
		if(y > 46) {
			player.sendMessage("Maybe I shouldn't do that right now.");
			return;
		}
		if(player.getPosition().distance(stone.getPosition()) <= 3) {
			player.animate(12196);
			int xDiff = stone.getX() - player.getPosition().getX();
			int yDiff = stone.getY() - player.getPosition().getY();
			player.getMovement().force(xDiff, yDiff, 0,
				0, 5, 35, Direction.getDirection(player.getPosition(), stone.getPosition()));
		} else {
			player.sendMessage("You can't reach that!");
		}
	}

	public static void register() {
		ObjectAction.register(56246, "jump", SteppingStones::jump);
	}
}
