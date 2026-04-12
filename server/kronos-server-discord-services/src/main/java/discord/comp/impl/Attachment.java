package discord.comp.impl;

import discord.comp.Component;
import lombok.Builder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
@Builder
public class Attachment implements Component {
	private String authorName;
	private String authorIcon;
	private String color;
	private ArrayList<Field> fields;

	@Override
	public JSONObject toJson() {
		var result = new JSONObject();
			result.put("author_icon", this.authorIcon);
			result.put("author_name", this.authorName);
			result.put("color", this.color);
		if (!this.fields.isEmpty()) {
			var array = new JSONArray();
			this.fields.stream()
				.map(Field::toJson)
				.forEach(array::put);
			result.put("fields", array);
		}
		return (result);
	}
}
