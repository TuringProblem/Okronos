package player.attributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import properties.ServerProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.function.Supplier;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.writer.ObjectWriter;

/**
 * PlayerAttributeLoader
 */
public interface PlayerAttributeCodec<T> {

	static record LoadContext(String ownerUID) {
	};

	static record SaveContext(String ownerUID) {
	};

	static <T> PlayerAttributeCodec<T> temporary(Supplier<T> cls) {
		return new TemporaryBasicInstance<>(cls);
	}

	static <T> Persistent<T> persistent(Class<T> cls, Supplier<T> constructor) {
		return new Persistent<>(cls, constructor);
	}

	public static record TemporaryBasicInstance<T>(Supplier<T> cls) implements PlayerAttributeCodec<T> {
		@Override
		public T load(LoadContext ctx) {
			return cls.get();
		}

		@Override
		public void save(SaveContext ctx, T value) {
			// no-op
		}
	}

	public static record Persistent<T>(Class<T> cls, Supplier<T> constructor) implements PlayerAttributeCodec<T> {

		private static Path SAVE_DIR_ROOT = Paths.get(ServerProperties.dataPath(), "runtime", "saves", "attributes");

		public static void register() {
			try {
				Files.createDirectories(SAVE_DIR_ROOT);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}

		private static <T> String encode(T value) {
			return JSON.toJSONString(value, JSONWriter.Feature.FieldBased, JSONWriter.Feature.WriteNonStringKeyAsString);
		}

		private static <T> T decode(Class<T> cls, String data) {
			return JSON.parseObject(data, cls, JSONReader.Feature.FieldBased, JSONReader.Feature.ErrorOnEnumNotMatch);
		}

		@Override
		public T load(LoadContext ctx) {
			try {
				var saveFile = this.saveFile(ctx.ownerUID());
				if (!saveFile.toFile().exists()) {
					return constructor.get();
				}
				var json = Files.readString(saveFile);
				var jsonValue = decode(cls, json);
				if (jsonValue == null) {
					return constructor.get();
				}
				return jsonValue;
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}

		@Override
		public void save(SaveContext ctx, T value) {
			if (value == null) {
				return;
			}
			var saveFile = this.saveFile(ctx.ownerUID());
			var saveDir = saveFile.getParent();
			var json = encode(value);
			try {
				if (!saveDir.toFile().exists()) {
					Files.createDirectories(saveDir);
				}
				Files.write(saveFile, json.getBytes());
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}

		public boolean hasData(LoadContext ctx) {
			return saveFile(ctx.ownerUID).toFile().exists();
		}

		Path saveFile(String ownerUID) {
			return SAVE_DIR_ROOT.resolve(cls.getName()).resolve(ownerUID + ".json");
		}
	}

	T load(LoadContext ctx);

	@SuppressWarnings("unchecked")
	default void saveRaw(SaveContext ctx, Object obj) {
		this.save(ctx, (T) obj);
	}

	void save(SaveContext ctx, T value);
}
