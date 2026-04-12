package discord.comp.impl;

import discord.comp.Component;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.Arrays;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
@Setter
@Getter
@NoArgsConstructor
public class Embed implements Component {

	private String title;
	private static final String TYPE = "rich";
	private String description;
	private String url;
	private Color colorObject;
	private int color;
	private Footer footer;
	private Image image;
	private Thumbnail thumbnail;
	private Author author;
	private Field[] fields;
	private String content;

	public Embed setColor(Color colorObject) {
		this.colorObject = colorObject;
		// Keep the integer color (RGB) in sync with the Color object, strip alpha if present
		if (colorObject != null)
			this.color = colorObject.getRGB() & 0xFFFFFF;
		return this;
	}
	public Embed setColor(int color) {
		this.color = color;
		return this;
	}

	public Embed setFields(Field... fields) {
		this.fields = fields;
		return this;
	}

	@Override
	public JSONObject toJson() {
		// Check if content is null or empty, if so, set to a default value
		if (content == null || content.isEmpty())
			content = "test content";
		var result = new JSONObject();
			result.put("type", TYPE);
			result.put("content", content); // Add content field
		if (title != null)
			result.put("title", title);
		if (description != null)
			result.put("description", description);
		if (url != null)
			result.put("url", url);
		if (author != null)
			result.put("author", author.toJson());
		if (footer != null)
			result.put("footer", footer.toJson());
		if (image != null)
			result.put("image", image.toJson());
		if (thumbnail != null)
			result.put("thumbnail", thumbnail.toJson());
		var fieldArray = new JSONArray();
		if (fields != null)
			Arrays.stream(fields).forEachOrdered(field -> fieldArray.put(field.toJson()));
		result.put("fields", fieldArray);
		result.put("color", color);
		return result;
	}
}
