package io.ruin.model.entity.player.offline_player;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.ruin.api.utils.ServerWrapper;
import io.ruin.model.World;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-23
 */
public class PlayerProfileService {
	private final Gson gson = new Gson();

	private final String SAVE_PATH = Paths.get(ServerWrapper.dataFolder.getAbsolutePath(),
		"runtime", "saves", "players",
		World.stage.name().toLowerCase(),
		World.type.name().toLowerCase()
		).toString();

	public String getPlayerHwid(String profileName) throws IOException {
		var profileFile = new File("%s/%s.json".formatted(SAVE_PATH, profileName));
		try (FileReader reader = new FileReader(profileFile)) {
			OfflinePlayer profile = gson.fromJson(reader, OfflinePlayer.class);
			return profile.hwid;
		}
	}

	public Optional<List<String>> findAltAccountsForHwid(String hwid) {
		var saveDirectory = new File(SAVE_PATH);
		if (!saveDirectory.exists() || !saveDirectory.isDirectory())
			return Optional.empty();

		var matchingProfiles = new ArrayList<String>();
		var jsonFiles = saveDirectory.listFiles((dir, name) -> name.endsWith(".json"));

		if (jsonFiles == null)
			return Optional.empty();

		Arrays.stream(jsonFiles).forEach(file -> {
			try (var reader = new FileReader(file)) {
				var profile = gson.fromJson(reader, OfflinePlayer.class);
				if (profile != null && hwid.equals(profile.hwid))
					matchingProfiles.add(profile.name);
			}
			catch (IOException | JsonSyntaxException e) {
				System.err.printf("Error reading file: %s - %s%n", file.getName(), e.getMessage());
			}
		});
		return matchingProfiles.isEmpty() ?
			Optional.empty() :
			Optional.of(matchingProfiles);
	}
}
