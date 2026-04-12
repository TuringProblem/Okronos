package player.mongo.codec;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.BooleanCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.LongCodec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ArrayCodec {

	public static class Provider implements CodecProvider {

		@SuppressWarnings("all")
		@Override
		public <T> Codec<T> get(Class<T> clazzArray, CodecRegistry registry) {
			if (!clazzArray.isArray()) {
				return null;
			}

			if (clazzArray.equals(boolean[].class)) {
				return (Codec<T>) new Boolean();
			}

			if (clazzArray.equals(int[].class)) {
				return (Codec<T>) new Int();

			}
			if (clazzArray.equals(long[].class)) {
				return (Codec<T>) new Long();
			}

			var component = component(clazzArray);
			var concreteTypes = new Class<?>[] {component};
			var concreteCodecs = new Codec[concreteTypes.length];
			for (var i = 0; i < concreteCodecs.length; i++) {
				try {
					concreteCodecs[i] = registry.get(concreteTypes[i]);
				} catch (Exception e) {
					throw e;
				}
			}
			return new Generic(registry, clazzArray, concreteCodecs);
		}

		private Class<?> component(Class<?> array) {
			var component = array;
			while (component.isArray()) {
				component = component.getComponentType();
			}
			return component;
		}
	}

	public static class Int implements Codec<int[]> {
		@Override
		public void encode(
				final BsonWriter writer, final int[] value, final EncoderContext encoderContext) {
			writer.writeStartArray();
			for (final int cur : value) {
				writer.writeInt32(cur);
			}
			writer.writeEndArray();
		}

		@Override
		public int[] decode(final BsonReader reader, final DecoderContext decoderContext) {
			reader.readStartArray();
			ArrayList<Integer> list = new ArrayList<>();
			while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
				list.add(reader.readInt32());
			}
			reader.readEndArray();
			return list.stream().mapToInt(i -> i).toArray();
		}

		@Override
		public Class<int[]> getEncoderClass() {
			return int[].class;
		}
	}

	@SuppressWarnings("all")
	public static class Boolean implements Codec<boolean[]> {

		@Override
		public void encode(
				final BsonWriter writer, final boolean[] value, final EncoderContext encoderContext) {
			writer.writeStartArray();
			for (var cur : value) {
				encoderContext.encodeWithChildContext(new BooleanCodec(), writer, cur);
			}
			writer.writeEndArray();
		}

		@Override
		public boolean[] decode(final BsonReader reader, final DecoderContext decoderContext) {
			return null;
		}

		@Override
		public Class<boolean[]> getEncoderClass() {
			return boolean[].class;
		}
	}


	@SuppressWarnings("all")
	public static class Long implements Codec<long[]> {

		@Override
		public void encode(
				final BsonWriter writer, final long[] value, final EncoderContext encoderContext) {
			writer.writeStartArray();
			for (var cur : value) {
				encoderContext.encodeWithChildContext(new LongCodec(), writer, cur);
			}
			writer.writeEndArray();
		}

		@Override
		public long[] decode(final BsonReader reader, final DecoderContext decoderContext) {
			return null;
		}

		@Override
		public Class<long[]> getEncoderClass() {
			return long[].class;
		}
	}

	@SuppressWarnings("all")
	public static class Generic<T> implements Codec<T[]> {
		private final Class<T[]> clazz;
		private final Codec<? extends T>[] concreteTypes;
		private final CodecRegistry registry;

		public Generic(CodecRegistry registry, Class<T[]> clazz) {
			this.registry = registry;
			this.clazz = clazz;
			this.concreteTypes = null;
		}

		public Generic(CodecRegistry registry, Class<T[]> clazz, Codec<? extends T>... concreteTypes) {
			this.clazz = clazz;
			this.registry = registry;
			this.concreteTypes = concreteTypes;
		}

		@Override
		public void encode(
				final BsonWriter writer, final T[] value, final EncoderContext encoderContext) {
			writer.writeStartArray();
			for (final Object cur : value) {
				if (cur == null) {
					writer.writeNull();
				} else {
					Codec elementCodec = this.getElementCodec(cur.getClass());
					var elementClassName = cur.getClass().getName();
					writer.writeString(elementClassName);
					encoderContext.encodeWithChildContext(elementCodec, writer, cur);
				}
			}
			writer.writeEndArray();
		}

		@Override
		public T[] decode(final BsonReader reader, final DecoderContext decoderContext) {
			ArrayList<T> list = new ArrayList<>();

			reader.readStartArray();
			while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
				if (reader.getCurrentBsonType() == BsonType.NULL) {
					list.add(null);
					reader.readNull();
				} else {
					try {
						var codecClassName = Class.forName(reader.readString());
						reader.readBsonType();
						var elementCodec = this.getElementCodec(codecClassName);

						var decode = decoderContext.decodeWithChildContext(elementCodec, reader);
						if (decode == null) {
							try {
								decode = elementCodec.getEncoderClass().newInstance();
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
						}
						list.add((T) decode);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
			reader.readEndArray();

			Class<T> componentType = this.componentType();
			T[] array = (T[]) Array.newInstance(componentType, list.size());
			for (int i = 0; i < array.length; i++) {
				array[i] = list.get(i);
			}
			return array;
		}

		private Codec<?> getElementCodec(Class<?> componentType) {
			for (var codec : this.concreteTypes) {
				var clazz = codec.getEncoderClass();
				if (clazz.equals(componentType)) {
					return codec;
				}
			}

			return this.registry.get(componentType);
		}

		private Class<T> componentType() {
			return (Class<T>) this.clazz.getComponentType();
		}

		@Override
		public Class<T[]> getEncoderClass() {
			return this.clazz;
		}
	}
}
