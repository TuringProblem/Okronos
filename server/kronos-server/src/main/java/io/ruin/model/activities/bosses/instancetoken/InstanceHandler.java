package io.ruin.model.activities.bosses.instancetoken;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.dynamic.DynamicChunk;
import io.ruin.model.map.dynamic.DynamicMap;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InstanceHandler {

	@Getter
	@Setter
	private String password;
	@Setter
	@Getter
	private boolean allowVisitors;
	MapHandler mapHandler;

	// Flag to prevent multiple destroy calls
	private boolean destroyed = false;
	// Add hostName field
	private final String hostName;

	public InstanceHandler(String hostName) {
		this.hostName = hostName;
		InstanceManager.getInstances().put(hostName, this);
	}

	public void buildMap(Player player, InstanceMaps mapToBuild) throws DynamicMap.DynamicMapBuildException {
		this.mapHandler = mapToBuild.createMapHandler();
		createChunkList(mapToBuild);
		if (mapToBuild.isUsingRegionId()) {
			if (mapToBuild == InstanceMaps.BALANCE_ELEMENTAL) {
				this.mapHandler.setMap(new DynamicMap().build(10127, 1).buildNw(10128, 1));
			} else if (mapToBuild == InstanceMaps.ARAXXOR) {
				this.mapHandler.setMap(new DynamicMap().build(mapToBuild.regionId, mapToBuild.getHeight()).buildSe(14745, 0));
			} else {
				this.mapHandler.setMap(new DynamicMap().build(mapToBuild.regionId, mapToBuild.getHeight()));
			}
		} else {
			var chunkList = createChunkList(mapToBuild);
			this.mapHandler.setMap(new DynamicMap().buildSw(chunkList));
		}
		if (password != null && !password.isEmpty()) {
			this.mapHandler.setPassword(password);
		}
		this.mapHandler.setHost(player);
		this.mapHandler.movePlayerToInstance(player);
		this.mapHandler.init();
		player.currentDynamicMap = this.mapHandler.map;
		player.inDynamicMap = true;
	}

	private static List<DynamicChunk> createChunkList(InstanceMaps mapToBuild) {
		var result = new ArrayList<DynamicChunk>();
		int index = 0;
		for (int i = 0; i < mapToBuild.getChunksX(); i++) {
			for (int j = 0; j < mapToBuild.getChunksY(); j++) {
				result.add(
						new DynamicChunk(mapToBuild.getChunks()[index][0], mapToBuild.getChunks()[index][1], mapToBuild.getHeight())
								.pos(j, i, mapToBuild.getHeight()));
				index++;
			}
		}
		return result;
	}

	public void requestToJoinInstance(Player player) {
		if (allowVisitors) {
			this.mapHandler.requestJoin(player);
		} else {
			player.sendMessage("This instance isn't allowing visitors.");
		}
	}

	public void destroy() {
		if (!destroyed) {
			if (this.mapHandler != null) {
				this.mapHandler.destroy();
				this.mapHandler = null;
			}
			InstanceManager.removeInstance(hostName.toLowerCase());
			destroyed = true;
		}
	}

}
