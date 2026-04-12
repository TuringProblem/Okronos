package io.ruin.api.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExecutorUtils {

	public static void shutdownNow(ExecutorService executor) {
		executor.shutdownNow();
	}

	public static void shutdown(ExecutorService executor) {
		try {
			executor.shutdown();
			if (!executor.awaitTermination(15, TimeUnit.SECONDS)) {
				shutdownNow(executor);
			}
		} catch (Exception e) {
			shutdownNow(executor);
			log.error("Unable to safely shutdown", e);
		}
	}
}
