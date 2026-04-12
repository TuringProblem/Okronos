package io.ruin.services.discord.configuration;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public class DiscordHelper {

	//Roles
	public static final long STAFF_ROLE = 1018323715240493067L;
	public static final long DEVELOPER_ROLE = 808979497721921547L;

	//Channels
	public static final String CHANNEL_PUNISHMENTS = "982417820396699659";
	public static final String CHANNEL_STAFF_COMMANDS = "861302996302430258";

	public static Role getSpecificRole(Member member, Long role) {
		List<Role> roles = member.getRoles();
		return roles.stream()
			.filter(memberRoles -> memberRoles.getIdLong() == role)
			.findFirst()
			.orElse(null);
	}

	public static Role getSpecificRole(Member member, String role) {
		List<Role> roles = member.getRoles();
		return roles.stream()
			.filter(memberRoles -> memberRoles.getId().equalsIgnoreCase(role))
			.findFirst()
			.orElse(null);
	}

	public static boolean hasRole(Member member, String role) {
		List<Role> roles = member.getRoles();
		return roles.stream().anyMatch(memberRoles -> memberRoles.getId().equalsIgnoreCase(role));
	}

	public static boolean hasRole(Member member, Long role) {
		List<Role> roles = member.getRoles();
		return roles.stream().anyMatch(memberRoles -> memberRoles.getIdLong() == role);
	}
}
