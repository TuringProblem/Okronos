package friendlist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import core.database.Database;
import core.database.DatabaseLoader;
import lombok.extern.slf4j.Slf4j;
import properties.ServerProperties;

@Slf4j
public class FriendListDb {
	private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	private static final Database<FriendList> DB = new Database<>(DatabaseLoader.jsonFiles(
			GSON,
			Path.of(ServerProperties.dataPath(), "runtime", "saves", "friendlist"),
			FriendList.class,
			FriendList::new));

	public static Database<FriendList> db() {
		return DB;
	}
}
