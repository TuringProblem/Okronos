package core.database;

import core.task.Continuation;
import io.ruin.db.DatabaseFile;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import static core.task.api.API.*;

@Slf4j
public class Database<T> {

	private class Entry {
		public T data = null;
		public ReentrantLock lock = new ReentrantLock(true);
	}

	private final ReentrantLock saveLock = new ReentrantLock();
	private final Map<String, Entry> cache = new ConcurrentHashMap<>();
	private final DatabaseLoader<T> loader;

	public Database(DatabaseLoader<T> loader) {
		this.loader = loader;
	}

	public T cached(String _key) {
		var key = _key.toLowerCase().trim();

		var entry = cache.get(key);
		if (entry == null) {
			return null;
		}
		return entry.data;
	}

	public void save() {
		DatabaseFile.service.submit(() -> {
			// NOTE: do not override saves
			if (!this.saveLock.tryLock()) {
				return;
			}

			try {
				for (var entry : this.cache.entrySet()) {
					var key = entry.getKey();
					var value = entry.getValue().data;

					// NOTE: do not write nulls, they might be loading
					if (value == null) {
						continue;
					}

					loader.write(key, value);
				}
			} catch (Exception e) {
				log.error("Unable to save database: ", e);
			} finally {
				this.saveLock.unlock();
			}
		});
	}

	public void upsertAsync(String _key, T value) {
		var key = _key.toLowerCase().trim();

		var entry = this.cache.computeIfAbsent(key, (ignore) -> new Entry());

		DatabaseFile.service.submit(() -> {
			try {
				entry.lock.lock();
				if (this.loader.write(key, value)) {
					entry.data = value;
				}
			} finally {
				entry.lock.unlock();
			}
		});
	}

	public void upsertBlocking(String _key, T value) {
		var key = _key.toLowerCase().trim();

		var entry = this.cache.computeIfAbsent(key, (ignore) -> new Entry());

		try {
			entry.lock.lock();
			if (this.loader.write(key, value)) {
				entry.data = value;
			}
		} finally {
			entry.lock.unlock();
		}
	}

	public Continuation<T> load(String _key) {
		var key = _key.toLowerCase().trim();

		var ref = new AtomicReference<T>();
		queueAsync(key, ref::set);
		return () -> {
			T value;
			while ((value = ref.get()) == null) {
				sleep(1);
			}
			return value;
		};
	}

	public T loadBlocking(String _key) {
		var key = _key.toLowerCase().trim();
		var entry = this.cache.computeIfAbsent(key, (ignore) -> new Entry());
		if (entry.data != null) {
			return entry.data;
		}

		try {
			entry.lock.lock();
			entry.data = loader.load(key);
		} catch (Exception e) {
			log.error("Unable to load data.", e);
		} finally {
			entry.lock.unlock();
		}

		return entry.data;
	}

	public void queueAsync(String _key, Consumer<T> action) {
		var key = _key.toLowerCase().trim();

		var entry = this.cache.computeIfAbsent(key, (ignore) -> new Entry());

		if (entry.data != null) {
			action.accept(entry.data);
			return;
		}

		DatabaseFile.service.submit(() -> {
			try {
				entry.lock.lock();
				if (entry.data != null) {
					action.accept(entry.data);
					return;
				}
				entry.data = loader.load(key);
				action.accept(entry.data);
			} finally {
				entry.lock.unlock();
			}
		});
	}

}
