package discord.webhooks;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import discord.comp.impl.Message;

public class WebhookSender {

	public static record Result(Status status, int statusCode, String body, long retryMs) {
		public static enum Status {
			Ok, Retry, Error
		}
	};

	static HttpClient HTTP_CLIENT = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(5))
			.build();

	public static CompletableFuture<Result> send(String target, Message message) {
		var request = buildRequest(target, message.toJson().toString());
		return send(request);
	}

	private static CompletableFuture<Result> send(HttpRequest request) {
		return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenCompose(response -> {
					var status = response.statusCode();
					var body = response.body();
					if (status >= 200 && status < 300) {
						return CompletableFuture.completedFuture(new Result(Result.Status.Ok, status, body, 0));
					} else if (status == 429) {
						var delayMs = computeRetryDelayMs(response);
						return CompletableFuture.completedFuture(new Result(Result.Status.Ok, status, body, delayMs));
					}
					return CompletableFuture.completedFuture(new Result(Result.Status.Error, status, body, 0));
				});
	}

	private static long computeRetryDelayMs(HttpResponse<String> response) {
		Optional<String> resetAfter = firstHeader(response, "X-RateLimit-Reset-After");
		if (resetAfter.isPresent()) {
			double seconds = Double.parseDouble(resetAfter.get());
			return (long) Math.ceil(seconds * 1000.0);
		}

		// Discord/HTTP: Retry-After could be seconds or HTTP-date
		Optional<String> retryAfter = firstHeader(response, "Retry-After");
		if (retryAfter.isPresent()) {
			String value = retryAfter.get().trim();
			return (long) Math.ceil(Double.parseDouble(value) * 1000.0);
		}

		return 10_000;
	}

	private static Optional<String> firstHeader(HttpResponse<?> response, String name) {
		return response.headers().firstValue(name);
	}

	private static HttpRequest buildRequest(String url, String json) {
		return HttpRequest.newBuilder()
				.uri(URI.create(url))
				.timeout(Duration.ofSeconds(10))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(json))
				.build();
	}
}
