package io.ruin.db;

import java.util.function.Consumer;

public interface Database<T, E> {

	public static record Entry<T, E>(T value, String raw) {
	}

	/**
	 * Get the specific data by uuid.
	 **/
	Entry<T, E> get(String uuid);

	/**
	 * Inserts the data in sync.
	 **/
	void insert(String uuid, T value);

	/**
	 * Mutates database data by uuid.
	 **/
	void mutateAsync(String uuid, Consumer<T> consumer);

	/**
	 * Get the player data async.
	 **/
	void getAsync(String uuid, Consumer<T> consumer);

	/**
	 * Insert data to be executed async.
	 *
	 * @return - wether the save was queued or not.
	 * Conflicting saves of specific uuid are not queued.
	 **/
	boolean insertQueue(String uuid, T value, Runnable additionalTask);

	/**
	 * Checks wether the save for specific uuid is in queue.
	 **/
	boolean hasPendingSave(String uuid);

	/**
	 * Waits for all saves to be flushed.
	 **/
	void awaitNoPendingSaves();

	/**
	 * Gets current pending save count.
	 */
	int pendingSaves();

	void remove(String uuid);
}
