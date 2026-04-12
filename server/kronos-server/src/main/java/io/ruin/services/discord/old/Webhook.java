package io.ruin.services.discord.old;

import io.ruin.Server;
import io.ruin.services.discord.old.util.Constants;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

@Slf4j
public class Webhook {

	private final String url;
	private boolean verbose;

	public Webhook() {
		this.url = Constants.WEBHOOK_URL;
	}

	public Webhook(boolean verbose) {
		this.url = Constants.WEBHOOK_URL;
		this.verbose = verbose;
	}

	public Webhook(String url) {
		this.url = url;
	}

	public Webhook(String url, boolean verbose) {
		this.url = url;
		this.verbose = verbose;
	}

	public void sendMessage(JSONObject message) {
		Server.executeAsync(() -> {
			try {
				HttpResponse<String> response = Unirest.post(url)
						.header("Content-Type", "application/json")
						.body(message.toString())
						.asString();
				log.trace("Message sent successfully. response: '" + response.getBody() + "'");
			} catch (Exception e) {
				log.error("Failed to send message: ", e);
			}
		});
	}
}
