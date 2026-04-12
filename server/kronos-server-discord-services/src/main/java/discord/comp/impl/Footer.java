package discord.comp.impl;

import discord.comp.Component;
import lombok.Setter;
import org.json.JSONObject;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
@Setter
public class Footer implements Component {
	private String text;
	private String iconUrl;
	private String proxyIconUrl;

	@Override
	public JSONObject toJson() {
		if (text == null)
			return null;
		var result = new JSONObject();
			result.put("text", text);
		if (iconUrl != null)
			result.put("icon_url", iconUrl);
		if (proxyIconUrl != null)
			result.put("proxy_icon_url", proxyIconUrl);
		return result;
	}
}
