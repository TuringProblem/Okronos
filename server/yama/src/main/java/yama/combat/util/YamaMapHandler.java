package yama.combat.util;

import io.ruin.model.World;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.GameObject;
import lombok.Getter;
import lombok.Setter;
import yama.npc.VoiceOfYama;


public class YamaMapHandler extends MapHandler {

	protected NPC yama;
	@Getter @Setter
	private boolean started = false;

	@Override
	public void init() {
		GameObject throne = Tile.getObject(56265, map.convertX(1499), map.convertY(10090), 0);
		throne.remove();
		NPC throneNPC = new NPC(123).spawn(new Position(map.convertX(1500), map.convertY(10089), 0));
		npcs.add(throneNPC);
	}

	@Override
	public void movePlayerToInstance(Player player) {
		if(started) {
			player.sendMessage("The fight has already started, you cannot join now.");
			return;
		}
		if(player.getSize() > 1) {
			player.sendMessage("There's already the maximum amount of players in this instance.");
			return;
		}
		players.add(player);
		try {
			if(host == null) {
				map = new DynamicMap().build(6045, 1);
				setMap(map);
				setHost(player);
				init();
			}
			player.getMovement().teleport(map.convertX(1503), map.convertY(10079), 0);
			player.currentDynamicMap = map;
			player.currentMapHandler = this;
			World.startEvent(e -> {
				e.delay(2);
				map.assignListener(player).onExit((p, logout) -> {
					defaultMapExit(p, logout);
					if (player.getName().equals(host.getName()))
						VoiceOfYama.instances.remove(host.getName());
				});
			});
		}
		catch (DynamicMap.DynamicMapBuildException e) {
			throw new IllegalStateException(e);
		}

	}

}
