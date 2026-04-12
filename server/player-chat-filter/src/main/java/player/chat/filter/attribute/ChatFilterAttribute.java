package player.chat.filter.attribute;

import com.google.gson.annotations.Expose;

import player.chat.filter.ChatFiltering;

public class ChatFilterAttribute {
	@Expose
	public ChatFiltering.FilterPublic filterPublic = ChatFiltering.FilterPublic.STANDARD;
}
