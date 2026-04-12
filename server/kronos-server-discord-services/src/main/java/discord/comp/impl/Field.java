package discord.comp.impl;

import discord.comp.Component;
import org.json.JSONObject;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
public record Field(
	String name,
	String value,
	boolean inline
) implements Component {
	@Override
	public JSONObject toJson() {
		var result = new JSONObject();
		result.put("name", this.name);
		result.put("value", this.value);
		result.put("inline", this.inline);
		return (result);
	}
}
