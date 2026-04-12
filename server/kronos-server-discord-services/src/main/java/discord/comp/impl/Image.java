package discord.comp.impl;

import discord.comp.Component;
import lombok.Builder;
import org.json.JSONObject;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
@Builder
public class Image implements Component {
	private String url;
	private String proxyUrl;
	private int height;
	private int width;

	@Override
	public JSONObject toJson() {
		if (url == null)
			return null;
		var result = new JSONObject();
			result.put("url", url);
		if (proxyUrl != null)
			result.put("proxy_url", proxyUrl);
		if (height != 0)
			result.put("height", height);
		if (width != 0)
			result.put("width", width);
		return result;
	}
}
