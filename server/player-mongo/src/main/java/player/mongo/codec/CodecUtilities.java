package player.mongo.codec;

import org.bson.codecs.BooleanCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectionCodecProvider;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.DoubleCodec;
import org.bson.codecs.IterableCodecProvider;
import org.bson.codecs.LongCodec;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.jsr310.InstantCodec;
import org.bson.codecs.pojo.ClassModelBuilder;
import org.bson.codecs.pojo.Convention;
import org.bson.codecs.pojo.PojoCodecProvider;
import com.mongodb.MongoClientSettings;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class CodecUtilities {

	public record GenericInformation<T>(Class<T> root, List<Class<? extends T>> implementors) {
		public static class GenericInformationBuilder<T> {
			private Class<T> root;
			private List<Class<? extends T>> implementors;

			public GenericInformationBuilder<T> root(Class<T> root) {
				this.root = root;
				return this;
			}

			public GenericInformationBuilder<T> implementors(List<Class<? extends T>> implementors) {
				this.implementors = implementors;
				return this;
			}

			public GenericInformation<T> build() {
				return new GenericInformation<>(this.root, this.implementors);
			}
		}

		public static <T> GenericInformationBuilder<T> builder() {
			return new GenericInformationBuilder<>();
		}
	}

	public static class AutomaticCodecRegistryFactoryBuilder {
		private final List<Class<?>> roots = new ArrayList<>();
		private final List<GenericInformation<?>> genericInformations = new ArrayList<>();

		public AutomaticCodecRegistryFactoryBuilder withGenericInformation(GenericInformation<?> info) {
			this.genericInformations.add(info);
			return this;
		}

		public AutomaticCodecRegistryFactoryBuilder withRoot(Class<?> root) {
			this.roots.add(root);
			return this;
		}

		public AutomaticCodecRegistryFactoryBuilder withRoots(Class<?>... roots) {
			this.roots.addAll(Arrays.asList(roots));
			return this;
		}

		public AutomaticCodecRegistryFactory build() {
			return new AutomaticCodecRegistryFactory(this.genericInformations, this.roots);
		}
	}

	public static class AutomaticCodecRegistryFactory {
		private final List<GenericInformation<?>> genericInformations;
		private final List<Class<?>> roots;

		public AutomaticCodecRegistryFactory(
				List<GenericInformation<?>> genericInformations, List<Class<?>> roots) {
			this.genericInformations = genericInformations;
			this.roots = roots;
		}

		public CodecRegistry get() {
			var visitSet = new HashSet<Class<?>>();
			var customCodecs = new HashMap<Class<?>, Codec<?>>();
			var reflectiveModels = new HashSet<Class<?>>();

			customCodecs.put(boolean.class, new BooleanCodec());
			customCodecs.put(long.class, new LongCodec());
			customCodecs.put(double.class, new DoubleCodec());
			customCodecs.put(Instant.class, new InstantCodec());
			customCodecs.put(Duration.class, new DurationCodec());
			customCodecs.put(Instant.class, new InstantCodec());
			customCodecs.put(BigInteger.class, new BigIntegerCodec());

			for (var generic : this.genericInformations) {
				collectCodecs(generic.root(), visitSet, customCodecs, reflectiveModels);

				for (var implClass : generic.implementors()) {
					collectCodecs(implClass, visitSet, customCodecs, reflectiveModels);
				}
			}

			for (var root : this.roots) {
				collectCodecs(root, visitSet, customCodecs, reflectiveModels);
			}

			return this.singleRootAutomatic(customCodecs, reflectiveModels);
		}

		private CodecRegistry singleRootAutomatic(
				HashMap<Class<?>, Codec<?>> customCodecs, HashSet<Class<?>> reflectiveModels) {
			var reflectiveProviders = createReflectiveProviders(reflectiveModels);

			var providers = new ArrayList<CodecProvider>();
			providers.add(new ArrayCodec.Provider());
			providers.add(new ValueCodecProvider());
			for (var provider : reflectiveProviders) {
				providers.add(provider);
			}

			var customCodecsRegistry = CodecRegistries.fromCodecs(customCodecs.values().stream().toList());

			return CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
					customCodecsRegistry,
					CodecRegistries.fromProviders(providers));
		}
	}

	private static void collectCodecs(
			Class<?> from,
			Set<Class<?>> reflectiveOut,
			Map<Class<?>, Codec<?>> customOut,
			Set<Class<?>> visitSet) {

		if (isImplicitCodec(from)) {
			return;
		}

		if (visitSet.contains(from)) {
			return;
		}

		visitSet.add(from);

		if (from.isEnum()) {
			var codec = new EnumCodec<>((Enum[]) from.getEnumConstants());
			customOut.putIfAbsent(from, codec);
			return;
		}

		var fields = from.getDeclaredFields();
		for (var field : fields) {
			if ((field.getModifiers() & Modifier.TRANSIENT) != 0) {
				continue;
			}

			if ((field.getModifiers() & Modifier.STATIC) != 0) {
				continue;
			}

			Class<?> type = field.getType();

			if (customOut.containsKey(type)) {
				continue;
			}

			if (type.isAssignableFrom(ArrayList.class) || type.isAssignableFrom(Set.class)) {
				var genericType = (ParameterizedType) field.getGenericType();
				var elementType = (Class<?>) genericType.getActualTypeArguments()[0];
				if (isImplicitCodec(elementType)) {
					continue;
				}
				collectCodecs(elementType, reflectiveOut, customOut, visitSet);
				continue;
			}

			if (type.isAssignableFrom(Map.class)) {
				var genericType = (ParameterizedType) field.getGenericType();

				if (!(genericType.getActualTypeArguments()[0] instanceof Class)) {
					continue;
				}

				if (!(genericType.getActualTypeArguments()[1] instanceof Class)) {
					continue;
				}

				var elementTypeKey = (Class<?>) genericType.getActualTypeArguments()[0];
				var elementTypeValue = (Class<?>) genericType.getActualTypeArguments()[1];

				if (isImplicitCodec(elementTypeKey) && isImplicitCodec(elementTypeValue)) {
					continue;
				}

				if (isImplicitCodec(elementTypeKey)) {
					collectCodecs(elementTypeKey, reflectiveOut, customOut, visitSet);
					continue;
				}

				if (isImplicitCodec(elementTypeValue)) {
					collectCodecs(elementTypeValue, reflectiveOut, customOut, visitSet);
					continue;
				}

				collectCodecs(elementTypeKey, reflectiveOut, customOut, visitSet);
				collectCodecs(elementTypeValue, reflectiveOut, customOut, visitSet);
				continue;
			}

			if (isImplicitCodec(type)) {
				continue;
			}

			if (type.isAssignableFrom(Iterable.class)) {
				continue;
			}

			if (type.isArray()) {
				// the element of an array needs to have registered codec even for generic arrays
				var component = type.getComponentType();
				while (component.isArray()) {
					component = component.getComponentType();
				}

				if (isImplicitCodec(component)) {
					continue;
				}

				reflectiveOut.add(type);
				collectCodecs(component, reflectiveOut, customOut, visitSet);
				continue;
			}

			reflectiveOut.add(type);

			if (visitSet.contains(type)) {
				continue;
			}

			collectCodecs(type, reflectiveOut, customOut, visitSet);
		}

		reflectiveOut.add(from);
		collectGenericCodecs(from, reflectiveOut, customOut, visitSet);
	}

	private static void collectGenericCodecs(
			Class<?> from,
			Set<Class<?>> reflectiveOut,
			Map<Class<?>, Codec<?>> customOut,
			Set<Class<?>> visitSet) {
		var extendsClass = from.getSuperclass();
		while (extendsClass != null && extendsClass != java.lang.Object.class) {
			collectCodecs(extendsClass, reflectiveOut, customOut, visitSet);
			extendsClass = extendsClass.getSuperclass();
		}
	}

	private static boolean isImplicitCodec(Class<?> cls) {

		if (cls.isPrimitive()) {
			return true;
		}

		if (cls.equals(Byte.class)) {
			return true;
		}

		if (cls.equals(Number.class)) {
			return true;
		}

		if (cls.equals(Short.class)) {
			return true;
		}

		if (cls.equals(Integer.class)) {
			return true;
		}

		if (cls.equals(Long.class)) {
			return true;
		}

		if (cls.equals(String.class)) {
			return true;
		}

		if (cls.equals(java.lang.Object.class)) {
			return true;
		}

		return false;
	}

	public static CodecRegistry reflectiveCodecRegistry(Class<?>... model) {
		var factoryBuilder = new AutomaticCodecRegistryFactoryBuilder();
		for (var root : model) {
			factoryBuilder.withRoot(root);
		}
		return factoryBuilder.build().get();
	}

	public static List<CodecProvider> createReflectiveProviders(Iterable<Class<?>> model) {
		var pojoCodecProvider = pojoCodecBuilder();
		for (var clz : model) {
			pojoCodecProvider.register(clz);
		}
		pojoCodecProvider.automatic(true);
		var providers = new ArrayList<CodecProvider>();
		providers.add(new ValueCodecProvider());
		providers.add(new IterableCodecProvider());
		providers.add(new DocumentCodecProvider());
		providers.add(pojoCodecProvider.build());
		return providers;
	}

	public static CodecProvider fromRoots(Class<?>... cls) {
		return new AutomaticCodecRegistryFactoryBuilder().withRoots(cls).build().get();
	}

	private static PojoCodecProvider.Builder pojoCodecBuilder() {
		var pojoCodecProvider = PojoCodecProvider.builder();
		pojoCodecProvider.conventions(List.of(new PersistingConvention()));
		pojoCodecProvider.register(new MapCodecProvider());
		return pojoCodecProvider;
	}


	@Slf4j
	public static class PersistingConvention implements Convention {

		@Override
		public void apply(ClassModelBuilder<?> classModelBuilder) {
			stripNonProperties(classModelBuilder);
		}

		private void stripNonProperties(final ClassModelBuilder<?> builder) {
			var names = new ArrayList<String>();
			var type = builder.getType();
			var typeFieldNames = fieldNames(type);
			var typeMethodNames = methodNames(type);

			for (final org.bson.codecs.pojo.PropertyModelBuilder<?> property : builder.getPropertyModelBuilders()) {
				final String name = property.getName();

				if (!typeFieldNames.contains(name.toLowerCase())) {
					names.add(name);
				} else if (typeMethodNames.contains("get" + name.toLowerCase())) {
					log.warn("property: " + name + " could be used as getter a field on: " + type);
				}
			}

			for (final String name : names) {
				builder.removeProperty(name);
			}
		}

		private Set<String> fieldNames(Class<?> cls) {
			return classHierarchy(cls).stream()
					.flatMap(it -> Arrays.stream(it.getFields()).map(it1 -> it1.getName().toLowerCase()))
					.collect(Collectors.toSet());
		}

		private Set<String> methodNames(Class<?> cls) {
			return classHierarchy(cls).stream()
					.flatMap(it -> Arrays.stream(it.getMethods()).map(it1 -> it1.getName().toLowerCase()))
					.collect(Collectors.toSet());
		}

		private List<Class<?>> classHierarchy(Class<?> top) {
			var result = new ArrayList<Class<?>>();
			result.add(top);
			Class<?> nxt = top;
			while ((nxt = nxt.getSuperclass()) != null) {
				result.add(nxt);
			}
			return result;
		}
	}
}
