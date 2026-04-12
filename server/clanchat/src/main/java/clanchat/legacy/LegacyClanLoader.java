package clanchat.legacy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;
import properties.ServerProperties;

@Slf4j
public class LegacyClanLoader {
	private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

	public static void loadAll(Consumer<LegacyClan> consumer) {
		var socialFolder = Path.of(ServerProperties.dataPath(), "/runtime/saves/social/");
		var list = socialFolder.toFile().list();
		if (list == null) {
			return;
		}

		for (var el : list) {
			var username = el.replaceAll(".json", "");
			var fl = load(username, socialFolder.resolve(el));
			if (fl == null) {
				continue;
			}
			consumer.accept(fl);
		}
	}

	private static LegacyClan load(String username, Path file) {
		try {
			var data = Files.readString(file);
			var result = GSON.fromJson(data, LegacyClan.class);
			return result;
		} catch (Exception e) {
			log.error("Unable to deserialize", e);
		}
		return null;
	}
}
