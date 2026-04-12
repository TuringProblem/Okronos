package io.ruin.model.entity.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import io.ruin.Server;
import io.ruin.cache.Icon;

/**
 * Descending order from highest priority group
 */
@RequiredArgsConstructor
public enum SecondaryGroup { // Todo @F16 VarpIds should be <img=THISID>
	SUPREME_DONATOR("Supreme Donator", 18, Icon.SUPREME_DONATOR.imgId, 1, 7.00),
	LEGENDARY_DONATOR("Legendary Donator", 17, Icon.LEGENDARY_DONATOR.imgId, 1, 5.00),
	PLATINUM_DONATOR("Platinum Donator", 16, Icon.PLATINUM_DONATOR.imgId, 3, 4.00),
	GOLD_DONATOR("Gold Donator", 15, Icon.GOLD_DONATOR.imgId, 4, 3.50),
	NOBLE_DONATOR("Noble Donator", 14, Icon.NOBLE_DONATOR.imgId, 2, 3.20),
	ELITE_DONATOR("Elite Donator", 13, Icon.ELITE_DONATOR.imgId, 5, 3.00),
	SUPER_DONATOR("Super Donator", 12, Icon.SUPER_DONATOR.imgId, 7, 2.75),
	DONATOR("Donator", 11, Icon.DONATOR_ICON.imgId, 6, 2.50),
	NONE("None", 0, -1, 0, 0);

	public final String Name;
	public final int id;
	public final int clientImgId;
	public final int varpId;
	public String title;
	@Getter
	public final double doubleDropChance;

	public static final SecondaryGroup[] VALUES = values();

	public void sync(Player player, String type) {
		sync(player, type, null);
	}

	public void sync(Player player, String type, Runnable successAction) {
		Server.executeAsync(() -> {
			Map<Object, Object> map = new HashMap<>();
			map.put("userId", player.getUserId());
			map.put("type", type);
			map.put("groupId", id);
			// String result = XenPost.post("add_group", map);
			// if(successAction != null && "1".equals(result))
			// successAction.run();
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
			// String result = XenPost.post("remove_group", map);
			// if(successAction != null && "1".equals(result))
			// successAction.run();
		});
	}

	public String tag() {
		return "<img=" + clientImgId + ">";
	}

	public static final SecondaryGroup[] GROUPS_BY_ID;

	public boolean equalToOrGreaterThan(final SecondaryGroup member) {
		return id >= member.id;
	}

	static {
		int highestGroupId = 0;
		for (SecondaryGroup group : values()) {
			if (group.id > highestGroupId)
				highestGroupId = group.id;
		}
		GROUPS_BY_ID = new SecondaryGroup[highestGroupId + 1];
		for (SecondaryGroup group : values())
			GROUPS_BY_ID[group.id] = group;
	}

	public PlayerClientRank clientRank() {
		switch (this) {
			case NONE:
				return PlayerClientRank.NORMAL;
			case DONATOR:
				return PlayerClientRank.IDK_11;
			case ELITE_DONATOR:
				return PlayerClientRank.IDK_13;
			case GOLD_DONATOR:
				return PlayerClientRank.IDK_15;
			case NOBLE_DONATOR:
				return PlayerClientRank.IDK_14;
			case SUPER_DONATOR:
				return PlayerClientRank.IDK_12;
			// TODO(polish) - more ranks
			case PLATINUM_DONATOR:
				return PlayerClientRank.NORMAL;
			case LEGENDARY_DONATOR:
				return PlayerClientRank.NORMAL;
			case SUPREME_DONATOR:
				return PlayerClientRank.NORMAL;
		}

		return PlayerClientRank.NORMAL;
	}
}
