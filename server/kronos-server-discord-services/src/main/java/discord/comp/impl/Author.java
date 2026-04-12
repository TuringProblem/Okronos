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
public class Author implements Component {

	private String name;
	private String url;
	private String iconUrl;
	private String proxyIconUrl;

	@Override
	public JSONObject toJson() {
		if (name == null)
			return null;
		var result = new JSONObject();
			result.put("name", name);
		if (url != null)
			result.put("url", url);
		if (iconUrl != null)
			result.put("icon_url", iconUrl);
		if (proxyIconUrl != null)
			result.put("proxy_icon_url", proxyIconUrl);
		return result;
	}
}
