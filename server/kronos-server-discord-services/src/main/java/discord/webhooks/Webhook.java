package discord.webhooks;

import discord.comp.impl.Message;
import discord.webhooks.WebhookSender.Result.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
public class Webhook {

	public static boolean disable = false;

	// NOTE: default 10/second seems to be alright, max batch size is related to the server's
	// max concurrent streams opened, which i cba to retrieve dynamically.
	private static long BATCH_MAX_SIZE = 10L;
	private static long BATCH_MIN_DELAY_MS = 1_000L;

	private static Logger log = LoggerFactory.getLogger(Webhook.class);
	private static Thread thread = new Thread(worker(), "discord-webhooks");
	private static Queue<Entry> queue = new ConcurrentLinkedQueue<>();

	record Entry(String target, Message message) {
	}

	public static void send(String targetUrl, Message message) {
		if (disable) {
			return;
		}
		if (targetUrl.isEmpty()) {
			return;
		}
		queue.offer(new Entry(targetUrl, message));
	}

	static Runnable worker() {
		return () -> {

			while (true) {
				var retryAfter = processBatch();
				try {
					Thread.sleep(BATCH_MIN_DELAY_MS + retryAfter);
				} catch (InterruptedException ignore) {
				}
			}
		};
	}

	// process the batch of webhook messages and return highest retry ms from any of the requests.
	private static long processBatch() {
		var batch = new ArrayList<Entry>();
		Entry next = null;
		while ((next = queue.poll()) != null) {
			batch.add(next);
			if (batch.size() >= BATCH_MAX_SIZE) {
				break;
			}
		}

		var batchFutures = new ArrayList<CompletableFuture<WebhookSender.Result>>();
		for (var msg : batch) {
			try {
				var future = WebhookSender.send(msg.target, msg.message);
				batchFutures.add(future);
			} catch (Exception e) {
				log.error("Unable to send webhook" + msg + " discarding: ", e);
				batchFutures.add(null);
			}
		}

		var retryBatch = new ArrayList<Entry>();
		var maxRetryMs = 0L;
		for (int i = 0; i < batch.size(); i++) {
			var msg = batch.get(i);
			var msgFuture = batchFutures.get(i);

			// ignore discarded futures
			if (msgFuture == null) {
				continue;
			}

			try {
				var result = msgFuture.get();

				switch (result.status()) {
					// discard errored futures
					case Error -> {
						log.error("Error on webhook: " + msg + " status: " + result.statusCode() + " body: " + result.body());
						continue;
					}

					case Retry -> {
						maxRetryMs = Math.max(result.retryMs(), maxRetryMs);
						retryBatch.add(msg);
					}

					case Ok -> {
					}
				}

			} catch (InterruptedException | ExecutionException e) {
				log.error("Error on webhook: " + msg, e);
				retryBatch.add(msg);
			}
		}

		for (var msg : retryBatch) {
			queue.offer(msg);
		}

		return maxRetryMs;
	}

	static {
		thread.setDaemon(true);
		thread.start();
	}

}
