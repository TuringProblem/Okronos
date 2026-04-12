package io.ruin.services.http;

import io.ruin.Server;
import io.ruin.model.World;
import io.ruin.process.event.Event;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public class HttpRequestEvent extends Event {

	/**
	 * Responsible for locking the http response object
	 */
	protected final Object httpResponseLock = new Object();

	/**
	 * The url we are hitting
	 */
	private final String url;

	/**
	 * This is what happens when a http response is successful.
	 */
	protected Consumer<HttpResponse> onSuccess = (response) -> {
	};

	/**
	 * This consumer is called upon a failure of the http client request.
	 */
	protected Consumer<HttpResponse> onFailure = (response) -> {
	};

	/**
	 * The http response object
	 */
	protected volatile HttpResponse httpResponse;

	/**
	 * If the http call has started
	 */
	protected boolean started;

	private final Object body;

	private final HttpRequestMethod method;

	boolean finished = false;

	public HttpRequestEvent(String url, Object body,
			HttpRequestMethod method,
			Consumer<HttpResponse> onSuccess,
			Consumer<HttpResponse> onFailure) {
		super(null);

		this.url = url;
		this.body = body;
		this.onSuccess = onSuccess;
		this.onFailure = onFailure;
		this.method = method;
	}

	@Override
	public void call() {
		if (!World.apiEnabled) {
			finished = true;
			return;
		}

		if (this.finished) {
			return;
		}

		if (this.started) {
			return;
		}

		// Makes the API call
		this.started = true;
		Server.executeAsync(() -> {
			try {
				synchronized (httpResponseLock) {
					httpResponse = HttpClient.makeRequest(url, method, body);
					onFinish();
				}
			} catch (Exception e) {
				log.error("Unable to make http request: " + url, e);
			} finally {
				this.finished = true;
			}
		});
	}

	public void onFinish() {
		synchronized (httpResponseLock) {
			if (httpResponse.response.isSuccessful()) {
				onSuccess.accept(httpResponse);
			} else {
				onFailure.accept(httpResponse);
			}

			var response = httpResponse.response;
			if (response.body() != null) {
				httpResponse.response.close();
			}
		}
	}
}
