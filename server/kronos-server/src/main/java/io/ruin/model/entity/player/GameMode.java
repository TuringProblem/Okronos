package io.ruin.model.entity.player;

import io.ruin.Server;
import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.activities.DonationBossHandler;
import io.ruin.model.activities.VoteBossHandler;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.Broadcast;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public enum GameMode {
	STANDARD("Regular", -1),
	IRONMAN("Ironman", 20),
	ULTIMATE_IRONMAN("Ultimate", 21),
	HARDCORE_IRONMAN("Hardcore", 22),
	GROUP_IRONMAN("Gim", 23),
	HARDCORE_GROUP_IRONMAN("HC Gim", 24);

	public final int groupId;
	public String Name;

	GameMode(String Name, int groupId) {
		this.groupId = groupId;
		this.Name = Name;
	}

	/**
	 * NOTE: this returns true if this game mode is ANY of the 3 iron man modes, not
	 * only the regular ironman mode!
	 */
	public boolean isIronMan() {
		return this != STANDARD;
	}

	public boolean isAnyOf(GameMode... modes) {
		for (var otherMode : modes) {
			if (this == otherMode) {
				return true;
			}
		}
		return false;
	}

	public boolean isUltimateIronman() {
		return this == ULTIMATE_IRONMAN;
	}

	public boolean isHardcoreIronman() {
		return this == HARDCORE_IRONMAN;
	}

	public boolean isGroupIronman() {
		return this == GROUP_IRONMAN;
	}

	public boolean isHardcoreGroupIronman() {
		return this == HARDCORE_GROUP_IRONMAN;
	}

	public static GameMode forGroupId(int id) {
		for (GameMode gameMode : values()) {
			if (gameMode.groupId == id)
				return gameMode;
		}
		return STANDARD;
	}

	public static void hardcoreDeath(Player player, Hit killHit) {
		if (DonationBossHandler.map != null && DonationBossHandler.map.isIn(player)) {
			return;
		}
		if(SummerEvent.map != null && SummerEvent.map.isIn(player)) {
			return;
		}

		if (VoteBossHandler.map != null && VoteBossHandler.map.isIn(player)) {
			return;
		}

		if (player.getName().equalsIgnoreCase("Dan Gleebles"))
			return;
		if (player.getPosition().regionId() == 11576) {
			return;
		}
		VarPlayerRepository.IRONMAN_MODE.set(player, 1);
		changeForumsGroup(player, IRONMAN.groupId);
		player.sendMessage(Color.RED.wrap("You have fallen as a Hardcore Ironman, your Hardcore status has been revoked."));
		if (player.getStats().totalLevel >= 100) {
			String overall = NumberUtils.formatNumber(player.getStats().totalLevel);
			if (killHit == null) {
				Broadcast.GLOBAL.sendPlain(Color.RED.wrap(Icon.HCIM_DEATH.tag() + player.getName()
						+ " has died as a Hardcore Ironman with a total level of " + overall + "!"));
			} else if (killHit.attacker != null) {
				if (killHit.attacker instanceof Player) {
					Broadcast.GLOBAL.sendPlain(Color.RED
							.wrap(Icon.HCIM_DEATH.tag() + player.getName() + " has died as a Hardcore Ironman with a total level of "
									+ overall + ", losing a fight to " + killHit.attacker.player.getName() + "!"));
				} else {
					Broadcast.GLOBAL.sendPlain(Color.RED
							.wrap(Icon.HCIM_DEATH.tag() + player.getName() + " has died as a Hardcore Ironman with a total level of "
									+ overall + ", brutally executed by " + killHit.attacker.npc.getDef().descriptiveName + "!"));
				}
			} else {
				if (killHit.type == HitType.POISON) {
					Broadcast.GLOBAL.sendPlain(Color.RED.wrap(Icon.HCIM_DEATH.tag() + player.getName()
							+ " has been poisoned to death as a Hardcore Ironman with a total level of " + overall + "!"));
				} else if (killHit.type == HitType.VENOM) {
					Broadcast.GLOBAL.sendPlain(Color.RED.wrap(Icon.HCIM_DEATH.tag() + player.getName()
							+ " has succumbed to venom as a Hardcore Ironman with a total level of " + overall + "!"));
				} else { // not sure if this can happen? can't think of anything
					Broadcast.GLOBAL.sendPlain(Color.RED.wrap(Icon.HCIM_DEATH.tag() + player.getName()
							+ " has died as a Hardcore Ironman with a total level of " + overall + "!"));
				}
			}
		}
	}

	public static void hardcoreDeathByNPC(Player player, String npcName) {
		VarPlayerRepository.IRONMAN_MODE.set(player, 1);
		changeForumsGroup(player, IRONMAN.groupId);
		player.sendMessage(Color.RED.wrap("You have fallen as a Hardcore Ironman, your Hardcore status has been revoked."));
		if (player.getStats().totalLevel >= 1) { // hmm
			String overall = NumberUtils.formatNumber(player.getStats().totalLevel);
			Broadcast.GLOBAL.sendPlain(Color.RED
					.wrap(Icon.HCIM_DEATH.tag() + player.getName() + " has died as a Hardcore Ironman with a total level of "
							+ overall + ", brutally executed by " + npcName + "!"));
		}
	}

	public static void openSelection(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, 215);
		player.getPacketSender().setHidden(215, 16, true);
		player.getPacketSender().setHidden(215, 17, true);
	}

	public static void changeForumsGroup(Player player, int mode) {
		Server.executeAsync(() -> {
			Map<Object, Object> map = new HashMap<>();
			map.put("userId", player.getUserId());
			map.put("type", "ironman_mode");
			map.put("groupId", mode);
			// XenPost.post("add_group", map);
		});
	}

	public PlayerClientRank clientRank() {
		switch (this) {
			case GameMode.IRONMAN -> {
				return PlayerClientRank.P_IRONMAN;
			}
			case GameMode.ULTIMATE_IRONMAN -> {
				return PlayerClientRank.P_IRONMAN_ULTIMATE;
			}
			case GameMode.HARDCORE_IRONMAN -> {
				return PlayerClientRank.P_IRONMAN_HARDCORE;
			}
			case GameMode.HARDCORE_GROUP_IRONMAN -> {
				return PlayerClientRank.P_IRONMAN_GROUP_HARDCORE;
			}
			case GameMode.GROUP_IRONMAN -> {
				return PlayerClientRank.P_IRONMAN_GROUP;
			}
			default -> {
				return PlayerClientRank.NORMAL;
			}
		}
	}
}
