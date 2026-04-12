package io.ruin.db;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.writer.ObjectWriter;
import io.ruin.api.utils.ExecutorUtils;
import io.ruin.model.World;
import io.ruin.model.map.Position;
import io.ruin.model.skills.construction.RoomDefinition;
import io.ruin.model.skills.construction.room.Room;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/// JSON file based database
@Slf4j
public class DatabaseFile<T> implements Database<T, String> {

	@FunctionalInterface
	public interface FileResolver {
		Path path(String uuid);
	}

	public static final ExecutorService service = Executors.newFixedThreadPool(
			World.db_threads,
			r -> new Thread(r, "save-worker"));

	static {
		var readerProvider = JSONFactory.getDefaultObjectReaderProvider();
		readerProvider.register(Room.class, (ObjectReader<Room>) (jsonReader, fieldType, fieldName, features) -> {
			if (jsonReader.nextIfNull()) {
				return null;
			}
			var object = jsonReader.readObject();
			var definition = RoomDefinition.valueOf((String) object.get("definition"));
			var inner = JSON.toJSONString(object);
			var room = decode(definition.getHandler(), inner);
			return room;
		}, true);
		readerProvider.register(Position.class, (ObjectReader<Position>) (jsonReader, fieldType, fieldName, features) -> {
			if (jsonReader.nextIfNull()) {
				return null;
			}
			var object = jsonReader.readObject();
			var x = (Integer) object.getOrDefault("x", 0);
			var y = (Integer) object.getOrDefault("y", 0);
			var z = (Integer) object.getOrDefault("z", 0);
			return new Position(x, y, z);
		}, true);

		var writerProvider = JSONFactory.getDefaultObjectWriterProvider();

		writerProvider.register(Position.class,
				(ObjectWriter<Position>) (jsonWriter, object, fieldName, fieldType, features) -> {
					var position = (Position) object;
					jsonWriter.write(new HashMap<>() {
						{
							put("x", position.x());
							put("y", position.y());
							put("z", position.z());
						}
					});
				}, true);
	}

	private final AtomicInteger pendingSaves = new AtomicInteger();
	private final Set<String> pendingSavesUuids = Collections.synchronizedSet(new HashSet<>());
	private final Map<String, Entry<T, String>> cache = new ConcurrentHashMap<>();
	private final Class<T> cls;
	private final FileResolver resolver;

	public DatabaseFile(Class<T> cls, FileResolver resolver) {
		this.cls = cls;
		this.resolver = resolver;
	}

	@Override
	public void awaitNoPendingSaves() {
		for (;;) {
			if (!hasPendingSaves()) {
				return;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean hasPendingSaves() {
		return pendingSaves.get() != 0;
	}

	@Override
	public int pendingSaves() {
		return this.pendingSaves.get();
	}

	@Override
	public void mutateAsync(String uuid, Consumer<T> consumer) {
		service.execute(() -> {
			var value = get(uuid).value();
			consumer.accept(value);
			if (value == null) {
				return;
			}
			insertQueue(uuid, value, null);
		});
	}

	@Override
	public void getAsync(String uuid, Consumer<T> consumer) {
		service.execute(() -> {
			var value = get(uuid).value();
			consumer.accept(value);
		});
	}

	@Override
	public boolean hasPendingSave(String uuid) {
		return this.pendingSavesUuids.contains(uuid);
	}

	@Override
	public Entry<T, String> get(String uuid) {
		var value = this.cache.computeIfAbsent(uuid, this::load);
		return value != null ? value : new Entry<>(null, null);
	}

	private Entry<T, String> load(String uuid) {
		try {
			var file = this.savePath(uuid);
			if (!file.toFile().exists()) {
				return null;
			}

			var text = Files.readString(file);
			if (text.trim().isEmpty()) {
				throw new IllegalStateException("Got empty text.");
			}

			var decoded = decode(this.cls, text);
			if (decoded == null) {
				throw new IllegalStateException("Got null: [" + text + "]");
			}
			return new Entry<>(decoded, text);
		} catch (Exception e) {
			throw new IllegalStateException("Unable to deserialize player " + uuid, e);
		}
	}

	@Override
	public boolean insertQueue(String uuid, T value, Runnable additionalTask) {
		if (!this.pendingSavesUuids.add(uuid)) {
			return false;
		}

		this.pendingSaves.incrementAndGet();
		service.execute(() -> {
			this.insert(uuid, value);
			if (additionalTask != null) {
				additionalTask.run();
			}
			this.pendingSaves.decrementAndGet();
		});
		return true;
	}

	@Override
	public void insert(String uuid, T value) {
		try {
			var path = this.savePath(uuid);
			var data = encode(value);
			if (data == null || data.trim().isEmpty()) {
				throw new IllegalStateException("Unable to write player data: " + uuid);
			}
			Files.writeString(path, data);
		} catch (Exception e) {
			log.error("Unable to save player: " + uuid + " value: " + value, e);
		} finally {
			this.pendingSavesUuids.remove(uuid);
		}
	}

	@Override
	public void remove(String uuid) {
		this.cache.remove(uuid);
	}

	private static <T> String encode(T value) {
		return JSON.toJSONString(value, JSONWriter.Feature.FieldBased, JSONWriter.Feature.WriteNonStringKeyAsString);
	}

	private static <T> T decode(Class<T> cls, String data) {
		return JSON.parseObject(data, cls, JSONReader.Feature.FieldBased);
	}

	private Path savePath(String uuid) {
		return this.resolver.path(uuid);
	}

	public static void shutdown() {
		ExecutorUtils.shutdown(service);
	}
}
