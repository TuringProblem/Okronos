package io.ruin.services.discord.old.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class Embed {

	private String title;
	private static final String TYPE = "rich";
	private String description;
	private String url;
	private int color;
	private Footer footer;
	private Image image;
	private Thumbnail thumbnail;
	private Author author;
	private Field[] fields;
	private String content; // New field for content

	public Embed setTitle(String title) {
		this.title = title;
		return this;
	}

	public Embed setDescription(String description) {
		this.description = description;
		return this;
	}

	public Embed setUrl(String url) {
		this.url = url;
		return this;
	}

	public Embed setColor(int color) {
		this.color = color;
		return this;
	}

	public Embed setFooter(Footer footer) {
		this.footer = footer;
		return this;
	}

	public Embed setAuthor(Author author) {
		this.author = author;
		return this;
	}

	public Embed setImage(Image image) {
		this.image = image;
		return this;
	}

	public Embed setThumbnail(Thumbnail thumbnail) {
		this.thumbnail = thumbnail;
		return this;
	}

	public Embed setFields(Field... fields) {
		this.fields = fields;
		return this;
	}

	public Embed setContent(String content) {
		this.content = content;
		return this;
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		// Check if content is null or empty, if so, set to a default value
		if (content == null || content.isEmpty()) {
			content = "Just testing webhooks"; // Set to a single space to ensure message is not empty
		}
		result.put("content", content); // Add content field
		if (title != null)
			result.put("title", title);
		result.put("type", TYPE);
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
		JSONArray fieldArray = new JSONArray();
		if (fields != null)
			Arrays.stream(fields).forEachOrdered(field -> fieldArray.put(field.toJson()));
		result.put("fields", fieldArray);
		result.put("color", color);
		return result;
	}
}
