package io.ruin.rsprot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import net.rsprot.protocol.api.traffic.ConcurrentNetworkTrafficWriter;
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock;
import net.rsprot.protocol.metrics.snapshots.impl.ConcurrentNetworkTrafficSnapshot;

@Slf4j
public class RSProtNetworkSnapshotService {
	private static final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(r -> {
		var thread = new Thread(r, "network-snapshot");
		thread.setDaemon(true);
		return thread;
	});

	public static void start() {
		service.scheduleAtFixedRate(RSProtNetworkSnapshotService::save, 5, 5, TimeUnit.MINUTES);
	}

	private static void save() {
		var monitor = RSProtService.service().getTrafficMonitor();
		@SuppressWarnings("unchecked")
		var snapshot = (ConcurrentNetworkTrafficSnapshot<LoginBlock<?>>) monitor.resetTransient();
		var data = ConcurrentNetworkTrafficWriter.INSTANCE.write(snapshot);
		var dir = new File("data/runtime/network/");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		try {
			Files.write(dir.toPath().resolve(new Date().toString().replace(":", "-") + ".txt"), data.getBytes(), StandardOpenOption.CREATE_NEW);
		} catch (Exception e) {
			log.error("Unable to write net log.", e);
		}
	}
}
