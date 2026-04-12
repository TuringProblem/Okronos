package properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ServerProperties {
	private static Properties SERVER_PROPERTIES = new Properties();

	static {
		var serverPropertiesFile = new File("server.properties");
		try (InputStream in = new FileInputStream(serverPropertiesFile)) {
			SERVER_PROPERTIES.load(in);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String get(String name) {
		return SERVER_PROPERTIES.getProperty(name);
	}

	public static String get(String name, String defaultValue) {
		return SERVER_PROPERTIES.getProperty(name, defaultValue);
	}

	public static double get(String name, double defaultValue) {
		return Double.parseDouble(SERVER_PROPERTIES.getProperty(name, Double.toString(defaultValue)));
	}

	public static boolean get(String name, boolean defaultValue) {
		return Boolean.parseBoolean(SERVER_PROPERTIES.getProperty(name, defaultValue ? "true" : "false"));
	}

	public static String dataPath() {
		return get("data_path");
	}
}
