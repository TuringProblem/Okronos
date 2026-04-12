package io.ruin.process.event;

import io.ruin.services.http.HttpRequestEvent;
import io.ruin.services.http.HttpRequestMethod;
import io.ruin.services.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import core.task.Continuations;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

@Slf4j
public class EventWorker {
	public static Event createEvent(EventConsumer eventConsumer) {
		/* manual processing is required when using this! */
		return new Event(eventConsumer);
	}

	public static void submitHttpRequest(HttpRequestEvent httpRequestEvent) {
		Continuations.schedule(httpRequestEvent);
	}

	public static void submitHttpGetRequest(String url, Consumer<HttpResponse> success, Consumer<HttpResponse> fail) {
		submitHttpRequest(new HttpRequestEvent(url, null, HttpRequestMethod.GET, success, fail));
	}

	public static Event startEvent(EventConsumer eventConsumer) {
		Event event = new Event(eventConsumer);
		Continuations.schedule(event);
		return event;
	}

	public static Event startEvent(final int delayTicks, final EventConsumer eventConsumer) {
		return startEvent(e -> {
			e.delay(delayTicks);
			eventConsumer.accept(e);
		});
	}

}
