package clanchat;

import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import core.database.Database;
import core.database.DatabaseLoader;
import lombok.extern.slf4j.Slf4j;
import properties.ServerProperties;

@Slf4j
public class FriendChatDb {
	private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	private static final Database<FriendChat> DB = new Database<>(DatabaseLoader.jsonFiles(
			GSON,
			Path.of(ServerProperties.dataPath(), "runtime", "saves", "friendchat"),
			FriendChat.class,
			FriendChat::new));

	public static Database<FriendChat> db() {
		return DB;
	}
}
