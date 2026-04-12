package io.ruin.services.http;

import java.util.function.Consumer;

public class HttpRequestBuilder {

	private HttpRequestMethod method;
	private String url;
	private Object body;
	private Consumer<HttpResponse> success = success -> {
	};
	private Consumer<HttpResponse> failure = response -> {
	};

	public HttpRequestBuilder(String url) {
		this.method = HttpRequestMethod.GET;
		this.url = url;
	}

	public HttpRequestBuilder(String url, HttpRequestMethod method) {
		this.method = method;
		this.url = url;
	}

	public HttpRequestBuilder body(Object body) {
		this.body = body;
		return this;
	}

	public HttpRequestBuilder onSuccess(Consumer<HttpResponse> success) {
		this.success = success;
		return this;
	}

	public HttpRequestBuilder onFailure(Consumer<HttpResponse> failure) {
		this.failure = failure;
		return this;
	}

	public HttpRequestEvent build() {
		return new HttpRequestEvent(url, body, method, success, failure);
	}

}
