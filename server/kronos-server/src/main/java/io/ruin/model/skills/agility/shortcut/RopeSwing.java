package io.ruin.model.skills.agility.shortcut;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.stat.StatType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RopeSwing {

	OGRE_ISLAND(1, 5, Position.of(2511, 3096)),

	;

	private int levelReq;
	private int xp;
	private Position endPosition;


	public void traverse(Player player, GameObject obj) {
		if (!player.getStats().check(StatType.Agility, levelReq, "attempt this"))
			return;
		World.startEvent(event -> {
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.face(endPosition.getX(), endPosition.getY());
			obj.animate(54);
			player.animate(751);

			int xDiff = endPosition.getX() - player.getPosition().getX();
			int yDiff = endPosition.getY() - player.getPosition().getY();
			player.getMovement().force(xDiff, yDiff, 0, 0, 30, 60, Direction.getDirection(player.getPosition(), endPosition));
			event.delay(1);
			obj.animate(55);
			player.getStats().addXp(StatType.Agility, xp, true);
			player.lastAgilityObjId = obj.getId();
			player.unlock();
		});
	}
}
