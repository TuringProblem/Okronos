package io.ruin.model.entity.player;

//import io.ruin.api.utils.XenPost;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import io.ruin.Server;

/**
 * Descending order from highest priority group
 */
@RequiredArgsConstructor
public enum PlayerGroup { // Todo @F16 VarpIds should be <img=THISID>
	OWNER("Owner", 100, 82, 13, 0),
	DEVELOPER("Developer", 20, 80, 11, 0),
	COMMUNITY_ADMIN("Admin", 8, 17, 12, 0),
	ADMINISTRATOR("Head Admin", 5, 1, 2, 0),
	MODERATOR("Mod", 4, 0, 1, 0),
	FORUM_MODERATOR("F Mod", 7, 0, 0, 0),
	SUPPORT("Support", 9, 74, 0, 0),
	YOUTUBER("Youtuber", 10, 69, 0, 0),
	BETA_TESTER("Beta", 6, 0, 0, 0),
	REGISTERED("Player", 2, 0, 0, 0),
	BANNED("Sit", 19, -1, 0, 0),
	HEAD_MODERATOR("Head Mod", 21, 0, 1, 0)
	;

	public final String Name;
	public final int id;
	public final int clientImgId;

	public final int varpId;

	public String title;
	@Getter
	public final int doubleDropChance;

	public void sync(Player player, String type) {
		sync(player, type, null);
	}

	public void sync(Player player, String type, Runnable successAction) {
		Server.executeAsync(() -> {
			Map<Object, Object> map = new HashMap<>();
			map.put("userId", player.getUserId());
			map.put("type", type);
			map.put("groupId", id);
		});
	}

	public void removePKMode(Player player, String type) {
		removePKMode(player, type, null);
	}

	public void removePKMode(Player player, String type, Runnable successAction) {
		Server.executeAsync(() -> {
			Map<Object, Object> map = new HashMap<>();
			map.put("userId", player.getUserId());
			map.put("type", type);
		});
	}

	public String tag() {
		return "<img=" + clientImgId + ">";
	}

	public static final PlayerGroup[] GROUPS_BY_ID;
	public static final PlayerGroup[] VALUES = values();

	static {
		int highestGroupId = 0;
		for (PlayerGroup group : values()) {
			if (group.id > highestGroupId)
				highestGroupId = group.id;
		}
		GROUPS_BY_ID = new PlayerGroup[highestGroupId + 1];
		for (PlayerGroup group : values())
			GROUPS_BY_ID[group.id] = group;
	}

	public PlayerClientRank clientRank() {

		return switch (this) {
			case ADMINISTRATOR,
				 COMMUNITY_ADMIN,
				 DEVELOPER,
				 FORUM_MODERATOR,
				 HEAD_MODERATOR,
				 MODERATOR -> PlayerClientRank.P_MOD;
			case OWNER -> PlayerClientRank.J_MOD;
			case SUPPORT -> PlayerClientRank.IDK_9;
			case YOUTUBER -> PlayerClientRank.IDK_10;
			default -> PlayerClientRank.NORMAL;
		};

	}
}
