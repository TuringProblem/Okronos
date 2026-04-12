package logging.sentry;

import io.sentry.Sentry;
import properties.ServerProperties;

public class SentryLogging {
	public static void initialize() {
		var enabled = Boolean.parseBoolean(ServerProperties.get("sentry_enabled", "false"));
		if (!enabled) {
			Sentry.init(options -> {
				options.setDsn("https://admin@sentry.local/123");
				options.setEnvironment(ServerProperties.get("sentry_env", "local"));
				options.setTracesSampleRate(1.0);
				options.setDebug(false);
				options.setEnabled(true);
				options.setTransportFactory((v0, v1) -> new SentryLocalTransport());
				options.setEnableShutdownHook(true);
			});
			return;
		}

		var dsn = normalizeDsn(ServerProperties.get("sentry_dsn", ""));
		Sentry.init(options -> {
			options.setDsn(dsn);
			options.setEnvironment(ServerProperties.get("sentry_env", "local"));
			options.setTracesSampleRate(0.01);
			options.setDebug(false);
		});
	}

	private static String normalizeDsn(String in) {
		if (in.startsWith("https://")) {
			return in;
		}
		return String.format("https://%s", in);
	}
}
