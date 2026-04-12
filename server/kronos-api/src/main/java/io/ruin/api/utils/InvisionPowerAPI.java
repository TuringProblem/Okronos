package io.ruin.api.utils;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import io.ruin.api.protocol.web.login.LoginResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class InvisionPowerAPI {


	public static final String API_TOKEN = "03cbe8ade79100c4cfd790575b3c0000";
	private static final String API_URL = "https://prifddinas.io/forums/api/index.php?";
	private static final String LOGIN_AUTH_URL = "https://prifddinas.io/forums/verify/"; //now just fix whatever the other shit is idk lol Will do
	private static final String LOGIN_AUTH_TOKEN = "TFKwcsSxGeK2xmYAkrj5qKTt5aD4ddbHCX2vkCy9cDVkAZcWeZcCC5CuuQJXDfDfGS66pWeQWVGdbAfdjHcMJv5TXYuvb3jn6JqztECL8LW6QaV9d758GcUtQFvBpXFq";
	private static final Type RESPONSE_TYPE = new TypeToken<Map<String, Object>>() {
	}.getType();

	static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	static JsonFactory JSON_FACTORY = new GsonFactory();

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getRequest(String apiPath, Map<String, String> params) throws IOException {
		HttpRequestFactory requestFactory = getFactory();

		String paramUrl = (apiPath.endsWith("/") ? "&" : "/&") + params.entrySet().stream().map((entry) -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("&"));
		GenericUrl url = new GenericUrl(API_URL + apiPath + paramUrl);
		log.info("Attempting to send API call to {}", url.build());
		HttpRequest request = requestFactory.buildGetRequest(url);


		request.getHeaders().setBasicAuthentication(API_TOKEN, "");
		return (Map<String, Object>) request

			.execute()
			.parseAs(RESPONSE_TYPE);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> postRequest(String apiPath, Map<String, Object> postVariables) throws IOException {
		HttpRequestFactory requestFactory = getFactory();

		GenericUrl url = new GenericUrl(API_URL + apiPath);

		HttpRequest request = requestFactory.buildPostRequest(url, new UrlEncodedContent(postVariables));

		request.getHeaders().setContentType("application/x-www-form-urlencoded");

		request.getHeaders().setBasicAuthentication(API_TOKEN, "");
		return (Map<String, Object>) request
			.execute()
			.parseAs(RESPONSE_TYPE);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> postAuth(Map<String, Object> postVariables) throws IOException {
		HttpRequestFactory requestFactory = getFactory();
		postVariables.put("token", LOGIN_AUTH_TOKEN);

		GenericUrl url = new GenericUrl(LOGIN_AUTH_URL);
		HttpRequest request = requestFactory.buildPostRequest(url, new UrlEncodedContent(postVariables));
		//request.getHeaders().setContentType("application/json");
		request.getHeaders().setContentType("application/x-www-form-urlencoded");
		log.info("Attempting to send Login AUTH call to {}", url.build());
		log.info("Post request data is {}", ((UrlEncodedContent) request.getContent()).getData());
		log.info("Post request headers are {}", request.getHeaders().getContentType());
		log.info(url.build() + " " + ((UrlEncodedContent) request.getContent()).getData());
		return (Map<String, Object>) request
			.execute()
			.parseAs(RESPONSE_TYPE);//it makes the accounts now,  just wont login -
	}

	@SuppressWarnings("unchecked")
	public static LoginResponse attemptLogin(String email, String password) throws IOException {
		HttpRequestFactory requestFactory = getFactory();
		Map<String, Object> postVariables = Maps.newConcurrentMap();
		postVariables.put("token", LOGIN_AUTH_TOKEN);
		postVariables.put("email", email);
		postVariables.put("password", password);

		GenericUrl url = new GenericUrl(LOGIN_AUTH_URL);
		HttpRequest request = requestFactory.buildPostRequest(url, new UrlEncodedContent(postVariables));
//		request.getHeaders().setContentType("application/json");
		request.getHeaders().setContentType("application/x-www-form-urlencoded");
		log.info("Attempting to send Login AUTH call to {}", url.build());
		log.info("Post request data is {}", ((UrlEncodedContent) request.getContent()).getData());
		return new LoginResponse((Map<String, Object>) request
			.execute()
			.parseAs(RESPONSE_TYPE)); //
	}

	private static HttpRequestFactory getFactory() {
		return HTTP_TRANSPORT.createRequestFactory(
			(HttpRequest request) -> {
				request.setParser(new JsonObjectParser(JSON_FACTORY));
			});

	}


}
