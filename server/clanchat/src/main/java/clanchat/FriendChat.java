package clanchat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.annotations.Expose;

import lombok.Data;

public class FriendChat {
	@Data
	class Member {
		@Expose
		private final String username;
	}

	@Expose
	public final String owner;

	@Expose
	public String channelName = "unnamed";

	@Expose
	public ClanPermission permissionJoin = ClanPermission.Anyone;

	@Expose
	public ClanPermission permissionKick = ClanPermission.Owner;

	@Expose
	public ClanPermission permissionTalk = ClanPermission.Owner;

	@Expose
	public boolean exists = false;

	public FriendChat(String owner) {
		this.owner = owner;
	}

	@Expose
	public final Set<Member> members = new HashSet<>();

	public boolean isOwner(String username) {
		return this.owner.equalsIgnoreCase(username.toLowerCase().trim());
	}

	public void removeMember(String username) {
		this.members.removeIf(it -> username.equalsIgnoreCase(it.username));
	}

	public void addMember(String username) {
		this.members.removeIf(it -> username.equalsIgnoreCase(it.username));
		this.members.add(new Member(username));
	}

	public Member member(String name) {
		return members.stream().filter(it -> it.getUsername().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
}
