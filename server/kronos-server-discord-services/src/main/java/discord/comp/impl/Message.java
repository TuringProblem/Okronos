package discord.comp.impl;

import discord.comp.Component;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
@Setter
@NoArgsConstructor
public class Message implements Component {

	private String content;
	private String username;
	private String avatarUrl;
	private boolean tts;
	@Getter
	private Embed[] embeds;

	public Message setEmbeds(Embed... embeds) {
		this.embeds = embeds;
		return this;
	}

	@Override
	public JSONObject toJson() {
		if (content == null && embeds == null)
			return null;
		JSONObject result = new JSONObject();
		if (content != null)
			result.put("content", content);
		if (username != null)
			result.put("username", username);
		if (avatarUrl != null)
			result.put("avatarUrl", avatarUrl);
		result.put("tts", tts);
		JSONArray embedArray = new JSONArray();
		if (embeds != null) {
			Arrays.stream(embeds).forEachOrdered(embed -> embedArray.put(embed.toJson()));
			result.put("embeds", embedArray);
		}
		return result;
	}
}
