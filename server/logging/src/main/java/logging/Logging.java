package logging;

import properties.ServerProperties;

public class Logging {

	public static void configure() {
		System.setProperty("logback.configurationFile", name());
	}

	private static String name() {
		return ServerProperties.get("log_config", "dev.xml");
	}
}
