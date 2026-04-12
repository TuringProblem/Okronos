package player.attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * PlayerAttributesRegistry
 */
public class PlayerAttributesRegistry {
	private static final Logger log = LoggerFactory.getLogger(PlayerAttributesRegistry.class);

	private static final List<Entry> loaders = new ArrayList<>();
	private static final Map<Class<?>, Integer> attributeKeys = new HashMap<>();
	private static final PlayerAttributes attributes = new PlayerAttributes();

	record Entry(Class<?> cls, PlayerAttributeCodec<?> loader) {
	}

	public static <T> void register(Class<T> cls, PlayerAttributeCodec<T> loader) {
		var index = attributes.register();
		attributeKeys.put(cls, index);
		loaders.add(new Entry(cls, loader));
	}

	public static <T> void registerPersistent(Class<T> cls, Supplier<T> constructor) {
		var loader = PlayerAttributeCodec.persistent(cls, constructor);
		var index = attributes.register();
		attributeKeys.put(cls, index);
		loaders.add(new Entry(cls, loader));
	}

	public static void unregister(Class<?> cls) {
		var key = attributeKeys.get(cls);
		int loaderIndex = -1;
		for (var loader : loaders) {
			if (loader.cls().equals(cls)) {
				break;
			}
			loaderIndex += 1;
		}
		if (loaderIndex == -1) {
			return;
		}
		attributes.remove(key);
		loaders.set(loaderIndex, null);
	}

	public static void load(int index, PlayerAttributeCodec.LoadContext context) {
		for (var entry : loaders) {
			var cls = entry.cls;
			var loader = entry.loader;
			var value = loader.load(context);
			if (value == null) {
				log.error("Unable to properly load " + entry.cls);
			}
			var key = attributeKeys.get(cls);
			var attribute = attributes.get(key);
			attribute.setRaw(index, value);
		}
	}

	public static List<AttributeSnapshot> snapshot(int index) {
		var snapshots = new ArrayList<AttributeSnapshot>();
		for (var entry : loaders) {
			var cls = entry.cls;
			var codec = entry.loader;
			var key = attributeKeys.get(cls);
			var attribute = attributes.get(key);
			var value = attribute.get(index);
			snapshots.add(new AttributeSnapshot(codec, value));
		}
		return snapshots;
	}

	public static void unload(int index) {
		attributes.unload(index);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(int index, Class<T> cls) {
		var key = attributeKeys.get(cls);
		if (key == null) {
			throw new IllegalStateException("Unable to find attribute model: " + cls);
		}
		var attribute = (PlayerAttribute<T>) attributes.get(key);
		return attribute.get(index);
	}

	/**
	 * Snapshot of the data
	 **/
	public static record AttributeSnapshot(PlayerAttributeCodec<?> codec, Object value) {
	};
}
