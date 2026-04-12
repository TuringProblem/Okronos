package io.ruin.api.utils;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.util.List;
import java.util.stream.Collectors;

public final class PackageLoader {

	@SuppressWarnings("unchecked")
	public static <T> List<Class<T>> loadExtending(
		final Class<T> superclass,
		final String... packageNames
	) {
		try (final ScanResult scanResult = new ClassGraph()
			.enableMethodInfo()
			.acceptPackages(packageNames)
			.initializeLoadedClasses()
			.scan()) {
			return scanResult.getSubclasses(superclass)
				.loadClasses()
				.stream()
				.map(clazz -> (Class<T>) clazz)
				.collect(Collectors.toList());
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> List<Class<T>> loadImplementing(
		final Class<T> interfaceClass,
		final String... packageNames
	) {
		try (final ScanResult scanResult = new ClassGraph()
			.enableMethodInfo()
			.initializeLoadedClasses()
			.acceptPackages(packageNames)
			.scan()) {
			return scanResult.getClassesImplementing(interfaceClass)
				.loadClasses()
				.stream()
				.map(clazz -> (Class<T>) clazz)
				.collect(Collectors.toList());
		}
	}

	private PackageLoader() {
		throw new UnsupportedOperationException();
	}

}
