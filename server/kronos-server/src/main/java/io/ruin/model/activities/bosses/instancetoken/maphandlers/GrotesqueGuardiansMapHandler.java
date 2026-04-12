package io.ruin.model.activities.bosses.instancetoken.maphandlers;

import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;

public class GrotesqueGuardiansMapHandler extends MapHandler {

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(map.convertX(1697), map.convertY(4567), 0);
		player.privateGrotesqueGuardianKills = 0;
		player.grotesqueGuardiansTimer = new ActivityTimer();

		super.movePlayerToInstance(player);
	}


	@Override
	public void init() {
		npcs.add(new NPC(7851).spawn(map.convertX(1692), map.convertY(4573), 0, 1));
		GameObject bell = Tile.getObject(31669, map.swRegion.baseX + 31, map.swRegion.baseY + 39, 0);
		ObjectAction.register(bell, 1, (player, obj) -> {
			if (player.localNpcs().stream().anyMatch(npc ->
				npc.getDef().name.equalsIgnoreCase("Dusk") ||
				npc.getDef().name.equalsIgnoreCase("Dawn")))
				player.sendMessage("You can't do this now.");
			else {
				player.dialogue(
					new OptionsDialogue("Are you sure you want to respawn the Grotesque Guardians?",
						new Option("Yes.", () -> {
							player.grotesqueGuardiansTimer = new ActivityTimer();
							npcs.add(new NPC(7851).spawn(map.convertX(1692), map.convertY(4573), 0, 1));
						}),
						new Option("No.", player::closeDialogue)
					)
				);

			}
		});
	}
}
