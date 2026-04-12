package io.ruin.services.http;

import com.google.gson.Gson;
import io.ruin.model.World;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.HttpMethod;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpClient {

	private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

	public static final Gson GSON = new Gson();

	/**
	 * Used to lock the main thread and allow safe access
	 */
	private static final Object KEY = new Object();

	/**
	 * Where we store the http auth cookie
	 */
	private static String authCookieValue;
	private static String COOKIE_HEADER_VALUE;

	private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

	private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
			.readTimeout(3000, TimeUnit.MILLISECONDS)
			.connectTimeout(3000, TimeUnit.MILLISECONDS)
			.build();

	public static void authenticate() throws Exception {
		String authUrl = World.apiBaseUrl + "authenticate/login";
		String password = World.apiPassword;

		String requestBody = GSON.toJson(password);

		RequestBody body = RequestBody.create(requestBody, MEDIA_TYPE);
		Request request = new Request.Builder()
				.url(authUrl)
				.post(body)
				.build();

		try {
			Response response = HTTP_CLIENT.newCall(request).execute();
			if (response.isSuccessful()) {
				int code = response.code();
				if (code == 200) {
					synchronized (KEY) {
						authCookieValue = new Gson().fromJson(response.body().string(), String.class);
						COOKIE_HEADER_VALUE = "r_auth=%s".formatted(authCookieValue);
					}
					logger.info("API Authenticated successful - Fetched auth token.");
				} else {
					throw new Exception(
							"Error Authenticating API - Invalid Credentials - Fix Credentials or Set api.enable=false in application.properties");
				}
			} else {
				throw new Exception("Error Authenticating API, Set api.enable=false in application.properties");
			}
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static HttpResponse makeRequest(String url, HttpRequestMethod method, Object body) {
		HttpUrl httpUrl = HttpUrl.parse(World.apiBaseUrl + url);
		RequestBody requestBody = RequestBody.create("", MEDIA_TYPE);
		if (body != null) {
			String string = GSON.toJson(body);
			requestBody = RequestBody.create(string, MEDIA_TYPE);
		}

		Request request = null;
		try {
			if (HttpMethod.requiresRequestBody(method.toString())) {
				request = new Request.Builder()
						.url(httpUrl)
						.method(method.toString(), requestBody)
						.addHeader("Cookie", COOKIE_HEADER_VALUE)
						.build();
			} else {

				// Get request
				request = new Request.Builder()
						.url(httpUrl)
						.method(method.toString(), null)
						.addHeader("Cookie", COOKIE_HEADER_VALUE)
						.build();
			}

			try {
				Response httpResponse = HTTP_CLIENT.newCall(request).execute();
				HttpResponse response = new HttpResponse(httpResponse);
				return response;
			} catch (IOException e) {
				logger.error("Failed with the following: " + e.getMessage(), e);
			}
		} catch (Exception ex) {
			logger.error("Unable to handle http request", ex);
		}

		// Error response
		Response response = new Response.Builder()
				.protocol(Protocol.HTTP_1_0)
				.message("")
				.request(request)
				.code(HttpStatus.SC_INTERNAL_SERVER_ERROR)
				.build();

		try {
			return new HttpResponse(response);
		} catch (Exception ex) {
			return new HttpResponse(response);
		}
	}
}
