package core.database;

import com.google.gson.Gson;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface DatabaseLoader<T> {
	T load(String key);

	boolean write(String key, T value);

	T defaultValue(String key);

	static <T> DatabaseLoader<T> jsonFiles(Gson gson, Path rootDir, Class<T> cls,
			java.util.function.Function<String, T> defaultValue) {
		try {
			if (!rootDir.toFile().exists()) {
				Files.createDirectories(rootDir);
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}

		return new DatabaseLoader<T>() {
			@Override
			public boolean write(String key, T value) {
				key = key.toLowerCase().trim();

				var file = rootDir.resolve(key + ".json");
				var json = gson.toJson(value, cls);
				try {
					Files.writeString(file, json);
					return true;
				} catch (IOException e) {
					var logger = LoggerFactory.getLogger(DatabaseLoader.class);
					logger.error("Unable to serialize: " + file + " " + json, e);
					return false;
				}
			}

			@Override
			public T load(String key) {
				key = key.toLowerCase().trim();

				var file = rootDir.resolve(key + ".json");
				if (!file.toFile().exists()) {
					return defaultValue(key);
				}
				try {
					var text = Files.readString(file);
					var value = gson.fromJson(text, cls);
					if (value == null) {
						return defaultValue(key);
					}
					return value;
				} catch (Exception e) {
					return defaultValue(key);
				}
			}

			@Override
			public T defaultValue(String key) {
				return defaultValue.apply(key);
			}

		};
	}
}
