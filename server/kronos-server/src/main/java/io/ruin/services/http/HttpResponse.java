package io.ruin.services.http;

import okhttp3.Response;

import java.lang.reflect.Type;
import java.util.Optional;

public class HttpResponse {

	public HttpResponse(Response response) {
		this.statusCode = response.code();
		this.response = response;
	}

	protected int statusCode;

	protected Response response;
	private Optional<Object> responseObject;

	protected Optional<String> parseError;

	public <T> T getResponseObject(Type type) {
		try {
			String result = response.peekBody(Long.MAX_VALUE).string();
			T responseObject = HttpClient.GSON.fromJson(result, type);
			return responseObject;
		} catch (Exception ex) {
			parseError = Optional.of(ex.getMessage());
		}
		return null;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public HttpResponse setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		return this;
	}

	public Response getResponse() {
		return response;
	}

	public HttpResponse setResponse(Response response) {
		this.response = response;
		return this;
	}

}
