package io.ruin.model.activities.raids.tob.dungeon.room;

import discord.webhooks.notifications.RareDropHook;
import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.activities.raids.toa.ToAObjects;
import io.ruin.model.activities.raids.tob.dungeon.RoomType;
import io.ruin.model.activities.raids.tob.party.TheatreParty;
import io.ruin.model.activities.raids.tob.party.TheatrePartyManager;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Utils;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author NuLL on 28/12/2021 https://www.rune-server.ee/members/null1001/
 * @project Drako
 */
public class TreasureRoom extends TheatreRoom {



	public TreasureRoom(TheatreParty party) throws DynamicMapBuildException {
		super(party);
	}

	@Override
	public void onLoad() {
		try {
			build(12867, 1);
		} catch (DynamicMap.DynamicMapBuildException e) {
			throw new RuntimeException(e);
		}
		var players = new AtomicInteger();
		var guide = new NPC(8332)
				.spawn(convertX(3236), convertY(4307), 0);

		addNpc(guide);

		for (String user : party.users) {
			TheatrePartyManager.instance().forUsername(user).ifPresent(player -> {
				switch (players.intValue()) {
					case 0 -> GameObject.spawn(33086, convertX(3240), convertY(4323), 0, 10, 1);
					case 1 -> GameObject.spawn(33087, convertX(3240), convertY(4328), 0, 10, 1);
					case 2 -> GameObject.spawn(33088, convertX(3226), convertY(4327), 0, 10, 3);
					case 3 -> GameObject.spawn(33089, convertX(3226), convertY(4323), 0, 10, 3);
					case 4 -> GameObject.spawn(33090, convertX(3233), convertY(4331), 0, 10, 0);
				}
				players.getAndIncrement();
			});
		}
	}

	@Override
	public void registerObjects() {
		ObjectAction.register(33014, 1, (player, obj) -> {
			openMVPInterface(player);
		});
		ObjectAction.register(32996, "use", (player, obj) -> {
			if (player.getName().equalsIgnoreCase(party.leaderName) && party.users.size() > 1) {
				player.dialogue(new MessageDialogue("Has everyone claimed their loot, if not they will lose it!"),
						new OptionsDialogue(
								new Option("Yes.", this::finishRaid),
								new Option("No.")));
			} else if (player.getName().equalsIgnoreCase(party.leaderName)) {
				finishRaid();
			} else {
				party.leave(player.getName(), false);
				teleportOut(player);
			}
		});
		InterfaceHandler.register(23, h -> {
			h.actions[6] = (SimpleAction) p -> {
				if (p.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
					p.sendMessage("You cannot bank items as an Ultimate Ironman.");
					return;
				}
				for (Item item : p.theatreReward) {
					if (item == null || item.getAmount() < 1)
						continue;
					List<Integer> uniques = Arrays.asList(
							ItemID.AVERNIC_DEFENDER_HILT,
							ItemID.GHRAZI_RAPIER,
							ItemID.SANGUINESTI_STAFF_UNCHARGED,
							ItemID.JUSTICIAR_CHESTGUARD,
							ItemID.JUSTICIAR_FACEGUARD,
							ItemID.JUSTICIAR_LEGGUARDS,
							ItemID.SCYTHE_OF_VITUR_UNCHARGED,
							ItemID.LIL_ZIK,
							30545,
							30553);
					p.localPlayers().forEach(plr -> {
						plr.sendFilteredMessage(Color.DARK_GREEN.wrap(p.getName() + " received a drop: "
								+ NumberUtils.formatNumber(item.getAmount()) + " x " + item.getDef().name));
					});
					if (uniques.contains(item.getId())) {
						int kills = p.theatreOfBloodKills.getKills();
						p.addToCollectionLog(item);
						Broadcast.WORLD.sendNewsDropMessage(p, Icon.ADMINISTRATOR, "<col=000000>" + p.getName(),
								" received <shad=D80808>" + item.getAmount() + "x " + item.getDef().name.toLowerCase()
										+ "</shad> from The Theatre of Blood" + "! (<col=FC0101>" + kills + " KC<col=000000>)");
						String message = p.getName() + " just received ";
						message += item.getDef().descriptiveName;

						RareDropHook.sendDiscordMessage(() -> {
							var jsonObject = new JSONObject();
							jsonObject.put("player", p.getName());
							jsonObject.put("game_mode", p.getGameMode());
							jsonObject.put("item_id", item.getId());
							jsonObject.put("item_name", item.getDef().name);
							jsonObject.put("source", "Theatre of Blood");
							jsonObject.put("total_attempts", Utils.formatMoneyString(kills));
							return jsonObject;
						});
					}
					if (item.getDef().isNote())
						item.setId(item.getDef().unnotedId());
					p.getBank().deposit(item.getId(), item.getAmount());
				}
				p.theatreReward.clear();
				p.closeInterface(ToplevelComponent.MAINMODAL);
			};
			h.actions[8] = (SimpleAction) p -> {
				for (Item item : p.theatreReward) {
					if (item == null || item.getAmount() < 1)
						continue;
					List<Integer> uniques = Arrays.asList(
							ItemID.AVERNIC_DEFENDER_HILT,
							ItemID.GHRAZI_RAPIER,
							ItemID.SANGUINESTI_STAFF_UNCHARGED,
							ItemID.JUSTICIAR_CHESTGUARD,
							ItemID.JUSTICIAR_FACEGUARD,
							ItemID.JUSTICIAR_LEGGUARDS,
							ItemID.SCYTHE_OF_VITUR_UNCHARGED,
							ItemID.LIL_ZIK,
							30545,
							30553);
					p.localPlayers().forEach(plr -> {
						plr.sendFilteredMessage(Color.DARK_GREEN.wrap(p.getName() + " received a drop: "
								+ NumberUtils.formatNumber(item.getAmount()) + " x " + item.getDef().name));
					});
					if (uniques.contains(item.getId())) {
						int kills = p.theatreOfBloodKills.getKills();
						p.addToCollectionLog(item);
						Broadcast.WORLD.sendNewsDropMessage(p, Icon.ADMINISTRATOR, "<col=000000>" + p.getName(),
								" received <shad=D80808>" + item.getAmount() + "x " + item.getDef().name.toLowerCase()
										+ "</shad> from The Theatre of Blood" + "! (<col=FC0101>" + kills + " KC<col=000000>)");
						String message = p.getName() + " just received ";
						message += item.getDef().descriptiveName;
						// RareDropEmbedMessage.sendDiscordMessage(p, message, "The Theatre of Blood", item.getId(), kills);


						RareDropHook.sendDiscordMessage(() -> {
							var jsonObject = new JSONObject();
							jsonObject.put("player", p.getName());
							jsonObject.put("game_mode", p.getGameMode());
							jsonObject.put("item_id", item.getId());
							jsonObject.put("item_name", item.getDef().name);
							jsonObject.put("source", "Theatre of Blood");
							jsonObject.put("total_attempts", Utils.formatMoneyString(kills));
							return jsonObject;
						});
					}
					if (!item.getDef().stackable && item.getAmount() > 1)
						item.setId(item.getDef().notedId);
					p.getInventory().addOrDrop(item.getId(), item.getAmount());
				}
				p.theatreReward.clear();
				p.closeInterface(ToplevelComponent.MAINMODAL);
			};
			h.actions[10] = (SimpleAction) p -> {
				p.theatreReward.clear();
				p.closeInterface(ToplevelComponent.MAINMODAL);
			};
		});

		ObjectAction.register(35836, "search", (player, obj) -> {
			player.dialogue(new MessageDialogue("The chest is empty."));
		});

		ObjectAction.register(35836, "shut", (player, obj) -> {
			player.dialogue(new MessageDialogue("The chest seems to be stuck now."));
		});

		ObjectAction.register(32994, "search", (player, obj) -> {
			if (player.tobreward) {
				player.dialogue(new MessageDialogue("The chest is empty, I wonder what was once inside."));
			} else {
				player.openInterface(ToplevelComponent.MAINMODAL, 23);
				player.getPacketSender().sendItems(
						-1,
						12,
						ToAObjects.containerId,
						player.theatreReward.toArray(new Item[0]));
				player.getPacketSender().sendClientScript(
						149, "IviiiIsssss",
						23 << 16 | 12, ToAObjects.containerId,
						2, 4, 1, -1, "", "", "", "", "");
				ToAObjects.containerId++;
			}
		});

		ObjectAction.register(33086, 1, (player, obj) -> { // reward chest
			if (player.tobChestId != 33086) {
				player.sendMessage("This isn't your reward chest!");
				return;
			}
			if (player.tobreward) {
				player.dialogue(new MessageDialogue("It would seem you have already obtained your reward from this chest."));
				return;
			}
			SummerEvent.handleKill(player, "Theatre of Blood");
			player.openInterface(ToplevelComponent.MAINMODAL, 23);
			player.getPacketSender().sendItems(
					-1,
					12,
					ToAObjects.containerId,
					player.theatreReward.toArray(new Item[0]));
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					23 << 16 | 12, ToAObjects.containerId,
					2, 4, 1, -1, "", "", "", "", "");
			ToAObjects.containerId++;
			obj.setId(32994);
		});
		ObjectAction.register(33087, 1, (player, obj) -> { // reward chest
			if (player.tobChestId != 33087) {
				player.sendMessage("This isn't your reward chest!");
				return;
			}
			if (player.tobreward) {
				player.dialogue(new MessageDialogue("It would seem you have already obtained your reward from this chest."));
				return;
			}
			SummerEvent.handleKill(player, "Theatre of Blood");
			player.openInterface(ToplevelComponent.MAINMODAL, 23);
			player.getPacketSender().sendItems(
					-1,
					12,
					ToAObjects.containerId,
					player.theatreReward.toArray(new Item[0]));
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					23 << 16 | 12, ToAObjects.containerId,
					2, 4, 1, -1, "", "", "", "", "");
			ToAObjects.containerId++;
			obj.setId(32994);
		});
		ObjectAction.register(33088, 1, (player, obj) -> { // reward chest
			if (player.tobChestId != 33088) {
				player.sendMessage("This isn't your reward chest!");
				return;
			}
			if (player.tobreward) {
				player.dialogue(new MessageDialogue("It would seem you have already obtained your reward from this chest."));
				return;
			}
			SummerEvent.handleKill(player, "Theatre of Blood");
			player.openInterface(ToplevelComponent.MAINMODAL, 23);
			player.getPacketSender().sendItems(
					-1,
					12,
					ToAObjects.containerId,
					player.theatreReward.toArray(new Item[0]));
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					23 << 16 | 12, ToAObjects.containerId,
					2, 4, 1, -1, "", "", "", "", "");
			ToAObjects.containerId++;
			obj.setId(32994);
		});
		ObjectAction.register(33089, 1, (player, obj) -> { // reward chest
			if (player.tobChestId != 33089) {
				player.sendMessage("This isn't your reward chest!");
				return;
			}
			if (player.tobreward) {
				player.dialogue(new MessageDialogue("It would seem you have already obtained your reward from this chest."));
				return;
			}
			SummerEvent.handleKill(player, "Theatre of Blood");
			player.openInterface(ToplevelComponent.MAINMODAL, 23);

			player.getPacketSender().sendItems(
					-1,
					12,
					ToAObjects.containerId,
					player.theatreReward.toArray(new Item[0]));
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					23 << 16 | 12, ToAObjects.containerId,
					2, 4, 1, -1, "", "", "", "", "");
			ToAObjects.containerId++;
			obj.setId(32994);
		});
		ObjectAction.register(33090, 1, (player, obj) -> { // reward chest
			if (player.tobChestId != 33090) {
				player.sendMessage("This isn't your reward chest!");
				return;
			}
			if (player.tobreward) {
				player.dialogue(new MessageDialogue("It would seem you have already obtained your reward from this chest."));
				return;
			}
			SummerEvent.handleKill(player, "Theatre of Blood");
			player.openInterface(ToplevelComponent.MAINMODAL, 23);
			player.getPacketSender().sendItems(
					-1,
					12,
					ToAObjects.containerId,
					player.theatreReward.toArray(new Item[0]));
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					23 << 16 | 12, ToAObjects.containerId,
					2, 4, 1, -1, "", "", "", "", "");
			ToAObjects.containerId++;
			obj.setId(32994);
		});
	}

	@Override
	public void registerNpcs() {
		NPCAction.register(8332, "talk-to", (player, npc) -> {
			if (player.getPosition().getRegion().id == 14642) {
				player.sendMessage("The guide isn't interested in talking to you right now.");
				return;
			}
			player.dialogue(
					new NPCDialogue(npc, "Hello there, use my right click options to loot the chest or leave the raid."));
		});
		NPCAction.register(8332, "Loot-Chest", (player, npc) -> {
			if (player.getPosition().getRegion().id == 14642) {
				player.sendMessage("The guide isn't interested in talking to you right now.");
				return;
			}
			SummerEvent.handleKill(player, "Theatre of Blood");
			player.openInterface(ToplevelComponent.MAINMODAL, 23);
			player.getPacketSender().sendItems(
					-1,
					12,
					ToAObjects.containerId,
					player.theatreReward.toArray(new Item[0]));
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					23 << 16 | 12, ToAObjects.containerId,
					2, 4, 1, -1, "", "", "", "", "");
			ToAObjects.containerId++;
		});
		NPCAction.register(8332, "Leave-Raid", (player, npc) -> {
			if (player.getPosition().getRegion().id == 14642) {
				player.sendMessage("The guide isn't interested in talking to you right now.");
				return;
			}
			if (player.getName().equalsIgnoreCase(party.leaderName) && party.users.size() > 1) {
				player.dialogue(new MessageDialogue("Has everyone claimed their loot, if not they will lose it!"),
						new OptionsDialogue(
								new Option("Yes.", this::finishRaid),
								new Option("No.")));
			} else if (player.getName().equalsIgnoreCase(party.leaderName)) {
				finishRaid();
			} else {
				party.leave(player.getName(), false);
				teleportOut(player);
			}
		});
	}

	public void openMVPInterface(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, 459);
		player.getPacketSender().sendString(459, 14, getMVP() == null ? "N/A" : getMVP().getName());
		player.getPacketSender().sendString(459, 33, String.valueOf(party.deaths));
		player.getPacketSender().sendString(459, 45, format(party.maidenTimerEnd));
		player.getPacketSender().sendString(459, 47, format(party.bloatTimerEnd));
		player.getPacketSender().sendString(459, 49, format(party.vasillasTimerEnd));
		player.getPacketSender().sendString(459, 51, format(party.sotetsegTimerEnd));
		player.getPacketSender().sendString(459, 53, format(party.xarpusTimerEnd));
		player.getPacketSender().sendString(459, 55, format(party.verzikTimerEnd));
		player.getPacketSender().sendString(459, 36, format(party.overallTimerEnd));
		player.getPacketSender().sendString(459, 11, format(getChallengeTimer()));

		AtomicInteger playerComponent = new AtomicInteger(22);
		AtomicInteger playerDeathComponent = new AtomicInteger(23);
		for (String user : party.users) {
			TheatrePartyManager.instance().forUsername(user).ifPresent(p -> {
				player.getPacketSender().sendString(459, playerComponent.get(), p.getName());
				player.getPacketSender().sendString(459, playerDeathComponent.get(), "" + party.playerDeaths.get(p));

				playerComponent.addAndGet(2);
				playerDeathComponent.addAndGet(2);
			});
		}
	}

	private long getChallengeTimer() {
		return party.maidenTimerEnd + party.bloatTimerEnd + party.vasillasTimerEnd + party.sotetsegTimerEnd
				+ party.xarpusTimerEnd + party.verzikTimerEnd;
	}

	private void finishRaid() {
		party.forPlayers(this::teleportOut);

		// Clean up rooms
		for (Map.Entry<RoomType, TheatreRoom> entry : party.dungeon.rooms.entrySet()) {
			var room = entry.getValue();
			room.removeMultiArea();
			room.destroy();
		}
		party.dungeon.rooms.clear();
		TheatrePartyManager.instance().deregister(party);
	}

	private void teleportOut(Player p) {
		p.getMovement().teleport(3665, 3219, 0);
		p.getCombat().reset(); // we're not loading their combat, we're resetting it...

		p.inTob = false;
		p.insideRaid = false;
		p.tobreward = false;

		p.theatreOfBloodStage = 0;

		p.teleportListener = null;
		p.deathEndListener = null;
		p.deathStartListener = null;
	}

	private static String format(long millis) {
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
		return String.format("%02d:%02d", minutes, seconds);
	}

	@Override
	public List<Position> getSpectatorSpots() {
		return null;
	}

	@Override
	public Position getEntrance() {
		return Position.of(3237, 4306, 0);
	}

	private List<Player> getPlayersByPerformance() {
		List<Player> playerPerformanceOrder = new ArrayList<>();
		for (String user : party.users) {
			TheatrePartyManager.instance().forUsername(user).ifPresent(playerPerformanceOrder::add);
		}

		// Sort the list based on supplyChestDamage in descending order
		playerPerformanceOrder.sort(Comparator.comparingInt(Player::getSupplyChestDamage).reversed());

		return playerPerformanceOrder;
	}

	private Player getMVP() {
		if (getPlayersByPerformance().isEmpty())
			return null;
		return getPlayersByPerformance().getFirst();
	}

}
