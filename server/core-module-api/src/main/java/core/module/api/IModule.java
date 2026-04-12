package core.module.api;

public interface IModule {
	default void init() {}

	default void start() {}

	default void shutdown() {}
}
