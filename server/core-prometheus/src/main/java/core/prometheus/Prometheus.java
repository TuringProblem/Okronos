package core.prometheus;

import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;
import lombok.extern.slf4j.Slf4j;
import properties.ServerProperties;

@Slf4j
public class Prometheus {
	private static HTTPServer server;

	public static void start() throws Exception {
		if (!ServerProperties.get("prometheus_enabled", false)) {
			return;
		}
		JvmMetrics.builder().register();
		server = HTTPServer.builder()
			.port(9000)
			.buildAndStart();
	}


	public static void shutdown() {
		if (server == null) {
			return;
		}
		try {
			server.stop();
		} catch (Exception e) {
			log.error("Unable to properly stop prometheus.", e);
		}
	}

}
