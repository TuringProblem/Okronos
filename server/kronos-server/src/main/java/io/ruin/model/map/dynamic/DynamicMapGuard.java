package io.ruin.model.map.dynamic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.ruin.Server;
import io.ruin.api.utils.ServerWrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicMapGuard {

	private static Set<DynamicMap> maps = new HashSet<>();

	public static void register(DynamicMap map) {
		maps.add(map);
	}

	public static void unregister(DynamicMap map) {
		maps.remove(map);
	}

	public static void dumpActive() {
		var copy = new HashSet<>(maps);
		Server.executeAsync(() -> {
			var data = "";

			for (var map : copy) {
				data += "-------------------------------\n";
				data += map.creationStackTrace;
				data += "\n-------------------------------";
			}

			try {
				Files.writeString(Path.of(ServerWrapper.dataFolder.getAbsolutePath(), "runtime",
						"dynamic_map_dump_" + System.currentTimeMillis()), data);
			} catch (IOException e) {
				log.error("save error", e);
			}
		});
	}

	public static int size() {
		return maps.size();
	}
}
