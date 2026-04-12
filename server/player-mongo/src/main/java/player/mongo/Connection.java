package player.mongo;

import com.mongodb.reactivestreams.client.MongoDatabase;
import io.ruin.model.entity.player.Player;
import player.mongo.codec.CodecUtilities;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.bson.codecs.configuration.CodecRegistries;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import properties.ServerProperties;
import reactor.core.publisher.Mono;

public class Connection {
	private static final Connection INSTANCE = new Connection();

	private final MongoDatabase database;
	private final MongoCollection<Player> collection;

	private Connection() {
		String host = ServerProperties.get("mongo_host", "127.0.0.1");
		String port = ServerProperties.get("mongo_port", "27017");
		String username = ServerProperties.get("mongo_username", "root");
		String password = encodePassword(ServerProperties.get("mongo_password", "password"));
		var connectionString = new ConnectionString("mongodb://" + username + ":" + password + "@" + host + ":" + port);
		var client = MongoClients.create(connectionString);
		var codecsProviders = CodecRegistries.fromProviders(CodecUtilities.fromRoots(Player.class));
		this.database = client.getDatabase("reason").withCodecRegistry(codecsProviders);

		Mono.from(this.database.createCollection("players")).block();
		this.collection = this.database.getCollection("players", Player.class);
	}

	public static Connection get() {
		return INSTANCE;
	}

	public static MongoCollection<Player> collection() {
		return get().collection;
	}

	private static String encodePassword(String password) {
		try {
			return URLEncoder.encode(password, StandardCharsets.UTF_8.toString());
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
