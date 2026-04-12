package mokhaiotl.npc;

import io.ruin.model.entity.npc.NPC;

import java.util.Objects;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-15
 */
public class EarthShield extends NPC {

	public EarthShield() {
		super(14715);
	}

	@Override
	public void process() {
		super.process();
		// Check for poison pools, and remove them
		getCentrePosition().getRegion().activeTiles.forEach(tile -> {
			if (tile.gameObjects != null && !tile.gameObjects.isEmpty()) {
				tile.gameObjects.forEach(object -> {
					// if the object is within the bounds of the shield
					if (getBounds().inBounds(object.getPosition())) {
						// check if we're either of the 2 acid pools
						if (object.getId() == 57283 || object.getId() == 57284)
							object.remove();
					}
				});
			}
		});

		// If we're walking to our target, and at the position, then remove the shield
		if (Objects.nonNull(walkTo) && getPosition().isAtPosition(walkTo)) {
			walkTo = null;
			remove();
		}
	}
}
