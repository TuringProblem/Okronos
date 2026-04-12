package io.ruin.api.discord.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import properties.ServerProperties;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-26
 */
@Getter
@RequiredArgsConstructor
public enum CommandNotificationType {
	OWNER(ServerProperties.get("discord_hook_command_owner", "")),
	COMMUNITY_MANAGER(ServerProperties.get("discord_hook_command_admin", "")),
	ADMINISTRATOR(ServerProperties.get("discord_hook_command_admin", "")),
	HEAD_MODERATOR(ServerProperties.get("discord_hook_command_head_mod", "")),
	MODERATOR(ServerProperties.get("discord_hook_command_mod", "")),
	SUPPORTER(ServerProperties.get("discord_hook_command_support", "")),
	BETA(ServerProperties.get("discord_hook_command_admin", "")),
	USER(ServerProperties.get("discord_hook_command_regular", ""))
	;
	private final String webhook;
}
